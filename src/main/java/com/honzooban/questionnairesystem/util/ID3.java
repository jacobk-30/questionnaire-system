package com.honzooban.questionnairesystem.util;

import java.io.Serializable;
import java.util.ArrayList;

import weka.classifiers.AbstractClassifier;
import weka.core.*;

/**
 *
 * @author 93231
 */
public class ID3 extends AbstractClassifier {
    /**
     * 训练数据集
     */
    private Instances m_Train = null;
    /**
     * 基于训练集构建的决策树
     */
    private TreeNodes m_Tree = null;

    /**
     * 属性值个数
     */
    private int m_NumAttributes = -1;
    /**
     * 可选属性的集合
     */
    private ArrayList<Attribute> m_AttributesOptions = new ArrayList<>();
    /**
     * 类标签的个数
     */
    private int m_NumClassValues = -1;
    /**
     * 阈值
     */
    private double m_Threshold = 0.1;

    @Override
    public void buildClassifier(Instances data) {
        m_Train = new Instances(data);
        m_NumAttributes = m_Train.numAttributes();
        for (int i = 0; i < m_NumAttributes; i++) {
            if (i!=m_Train.classIndex()) {
                m_AttributesOptions.add(m_Train.attribute(i));
            }
        }
        m_NumClassValues = data.numClasses();
        m_Tree = BuildTree(m_Train);
    }

    /**
     * 建立决策树
     * @param data
     * @return
     */
    private TreeNodes BuildTree(Instances data) {
        // 数据集中所有实例属于同一类标签时，构建叶节点，类标签分布由该类标签决定
        if (SameClassValue(data)) {
            double []classDistribution = new double[m_NumClassValues];
            classDistribution[(int)data.instance(0).classValue()] = 1.0;
            return new TreeNodes(data,classDistribution);
        }
        // 可选属性为空时，以当前数据集构建叶节点，类标签分布由数据集决定
        if (m_AttributesOptions.size() == 0) {
            double[] classDistribution = ClassDistribution(data);
            return new TreeNodes(data, classDistribution);
        }
        TreeNodes root = null;
        double gain[] = computeInformationGain(data);
        int selectedAttIndex = Utils.maxIndex(gain);
        // 最大信息增益小于阈值时，构建叶节点，类标签分布由数据集决定
        if (gain[selectedAttIndex] < m_Threshold) {
            double[] classDistribution = ClassDistribution(data);
            return new TreeNodes(data, classDistribution);
        } else {
            Attribute selectedAttribute = m_AttributesOptions.get(selectedAttIndex);
            m_AttributesOptions.remove(selectedAttIndex);
            //以所选属性构建非叶节点，类标签分布由数据集决定
            root = new TreeNodes(data, ClassDistribution(data), selectedAttribute);
            Instances subData[] = SplitDataByAttribute(data,selectedAttribute);
            for (int i = 0; i < subData.length; i++) {
                if (subData[i].numInstances() != 0 ) {
                    root.m_SubTree[i] = BuildTree(subData[i]);
                }
            }
        }
        return root;
    }


    /**
     * 根据属性对数据进行分类
     * @param data 数据
     * @param attribute 属性
     * @return
     */
    private Instances[] SplitDataByAttribute(Instances data, Attribute attribute) {
        Instances subInstances[] = new Instances[attribute.numValues()];
        for (int i = 0; i < subInstances.length; i++) {
            subInstances[i] = new Instances(data,0);
        }
        for (int i = 0; i < data.numInstances(); i++) {
            Instance instance = data.instance(i);
            int attValue = (int)instance.value(attribute);
            subInstances[attValue].add(instance);
        }
        return subInstances;
    }

    /**
     * 计算数据的信息增益
     * @param data 数据
     * @return
     */
    private double[] computeInformationGain(Instances data) {
        double []gain = new double[m_AttributesOptions.size()];
        double entropy_before = calculateEntropy(data);
        for (int i = 0; i < gain.length; i++) {
            Attribute attribute = m_AttributesOptions.get(i);
            Instances []subInstances = SplitDataByAttribute(data, attribute);
            double[] entropies = new double[attribute.numValues()];
            for (int j = 0; j < entropies.length; j++) {
                if (subInstances[j].numInstances() != 0) {
                    entropies[j] = calculateEntropy(subInstances[j]);
                }
            }
            gain[i] = entropy_before;
            for (int j = 0; j < entropies.length; j++) {
                gain[i] -= ((double)subInstances[j].numInstances() / (double)data.numInstances())*entropies[j];
            }
        }

        return gain;
    }

    /**
     * 计算数据熵
     * @param data 数据
     * @return
     */
    private double calculateEntropy(Instances data) {
        double []count = new double[m_NumClassValues];
        for (int i = 0; i < data.numInstances(); i++) {
            int classValue = (int)data.instance(i).classValue();
            count[classValue] ++;
        }
        double entropy = 0.0;
        for (double v : count) {
            entropy += v / (double) data.numInstances() * log2((double) v, (double) data.numInstances());
        }
        return -entropy;
    }

    /**
     * 计算log2x
     * @param x
     * @param y
     * @return
     */
    private double log2(double x,double y){
        if(x<1e-6||y<1e-6) {
            return 0.0;
        } else {
            return Math.log(x/y)/Math.log(2);
        }
    }

    /**
     * 返回数据的类分布
     * @param data 数据
     * @return
     */
    private double[] ClassDistribution(Instances data) {
        double[] count = new double[m_NumClassValues];
        for (int i = 0; i < data.numInstances(); i++) {
            Instance instance = data.instance(i);
            int classValue = (int)instance.classValue();
            count[classValue] ++;
        }
        for (int i = 0; i < count.length; i++) {
            count[i] = count[i] / data.numInstances();
        }
        return count;
    }

    /**
     * 判断数据中的实例是否都是相同的类值
     * @param data 数据
     * @return
     */
    private boolean SameClassValue(Instances data) {
        if (data.numInstances() == 0) {
            return true;
        }
        int sameClassValue = (int)data.instance(0).classValue();
        for (int i = 1; i < data.numInstances(); i++) {
            if ((int)data.instance(i).classValue() != sameClassValue) {
                return false;
            }
        }

        return true;
    }

    @Override
    public double[] distributionForInstance(Instance instance) {
        TreeNodes current = m_Tree;
        //根据待测样本找到决策树的叶节点
        while (current.m_SubTree!=null) {
            Attribute attribute = current.m_Attribute;
            int attValue = (int)instance.value(attribute);
            current = current.m_SubTree[attValue];
        }
        //返回叶节点的类标签分布即可
        return current.m_ClassDistribution;
    }

    /**
     * 树节点的类定义
     */
    private class TreeNodes implements Serializable {
        /**
         * 节点的类标签分布
         */
        public double m_ClassDistribution[] = null;
        /**
         * 节点对应的数据集
         */
        public Instances m_SubTrain = null;
        /**
         * 节点对应的选择属性
         */
        public Attribute m_Attribute = null;
        /**
         * 节点对应的多个子树
         */
        public TreeNodes m_SubTree[] = null;

        public TreeNodes(Instances data,double[] value)
        {
            this.m_SubTrain = new Instances(data);
            this.m_ClassDistribution = value;
        }
        public TreeNodes(Instances data,double[] value,Attribute attribute)
        {
            this.m_SubTrain = new Instances(data);
            this.m_ClassDistribution = value;
            this.m_Attribute = attribute;
            this.m_SubTree = new TreeNodes[this.m_Attribute.numValues()];
        }
    }
}

