package com.honzooban.questionnairesystem.util;

import com.honzooban.questionnairesystem.common.Constant;
import com.honzooban.questionnairesystem.dto.SubmitParam;
import com.honzooban.questionnairesystem.util.vaild.CommonValidator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.core.Utils;

import javax.rmi.CORBA.Util;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName Id3Util.java
 * @Description
 * @createTime 2020年04月01日 09:31:00
 */
public class Id3Util {

    /**
     * 存储属性名称
     */
    private ArrayList<String> attribute = new ArrayList<>();
    /**
     * 存储每个属性的取值
     */
    private ArrayList<ArrayList<Integer>> attributeValue = new ArrayList<>();
    /**
     * 原始数据
     */
    private ArrayList<Integer[]> data = new ArrayList<>();
    /**
     * 决策变量在属性集中的索引
     */
    int decatt = 0;
    /**
     * 解析arff文件的正则表达式
     */
    public static final String patternString = "@attribute(.*)[{](.*?)[}]";

    private Document document;
    private static Element root;

    private static final Logger logger = LoggerFactory.getLogger(Id3Util.class);

    public Id3Util(){
        document = DocumentHelper.createDocument();
        root = document.addElement("root");
        root.addElement("DecisionTree").addAttribute("value", "null");
    }

    /**
     * 初始化决策树
     */
    public void init(){
        readArff(new File(Constant.TRAINING_SET_FILE));
        setDecatt(Constant.DECATT);
        LinkedList<Integer> attributeIndexList =new LinkedList<>();
        for(int i = 0; i < attribute.size(); i++){
            if(i != decatt) {
                attributeIndexList.add(i);
            }
        }
        ArrayList<Integer> dataIndexList = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
            dataIndexList.add(i);
        }
        this.buildDecisionTree("DecisionTree", null, dataIndexList, attributeIndexList, root);
        this.writeXML(Constant.DECISION_TREE_FILE);
    }

    /**
     * 读取arff文件
     * @param file arff文件
     */
    private void readArff(File file){
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = null;
            Pattern pattern = Pattern.compile(patternString);
            while((line = bufferedReader.readLine()) != null){
                Matcher matcher = pattern.matcher(line);
                if(matcher.find()){
                    attribute.add(matcher.group(1).trim());
                    String[] values = matcher.group(2).split(",");
                    ArrayList<Integer> array = new ArrayList<>(values.length);
                    for(String value: values){
                        array.add(Integer.parseInt(value.trim()));
                    }
                    attributeValue.add(array);
                }else if(line.startsWith("@data")){
                    while((line = bufferedReader.readLine()) != null){
                        if(line == ""){
                            continue;
                        }
                        String[] array = line.split(",");
                        Integer[] row = new Integer[array.length];
                        for(int i = 0; i < array.length; i++){
                             row[i] = Integer.parseInt(array[i]);
                        }
                        data.add(row);
                    }
                }else {
                    continue;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置决策变量
     * @param n 决策变量索引
     */
    private void setDecatt(int n) {
        if (n < 0 || n >= attribute.size()) {
            logger.error("决策变量指定错误");
            System.exit(2);
        }
        decatt = n;
    }

    /**
     * 设置决策变量
     * @param name 决策变量名称
     */
    private void setDecatt(String name) {
        int n = attribute.indexOf(name);
        setDecatt(n);
    }

    /**
     * 计算给定数组的熵
     * @param array 数组
     * @return
     */
    private double getEntropy(int[] array){
        int sum = 0;
        for(int i = 0; i < array.length; i++){
            sum += array[i];
        }
        return getEntropy(array, sum);
    }

    /**
     * 计算给定数组的熵
     * @param array 数组
     * @param sum 给定数组的算术和
     * @return
     */
    private double getEntropy(int[] array, int sum) {
        double entropy = 0.0;
        for (int i = 0; i < array.length; i++) {
            if(array[i] == 0){
                continue;
            }
            entropy -= ((double) array[i] / sum) * Utils.log2((double) array[i] / sum);
        }
        return entropy;
    }

    /**
     * 检查给定数据集的类标号是否一致
     * @param subset 数据集
     * @return 检查结果
     */
    private boolean isClassesUnanimous(ArrayList<Integer> subset){
        int count = 1;
        Integer value = data.get(subset.get(0))[decatt];
        for(int i = 1; i < subset.size(); i++){
            Integer next = data.get(subset.get(i))[decatt];
            if(value.equals(next)){
                ++count;
            }
        }
        double ratio = (double) count / subset.size();
        return ratio >= Constant.CLASSES_UNANIMOUS ? true : false;
    }

    /**
     * 计算原始数据的子集以第index个属性为节点时计算它的信息熵
     * @param subset 数据集子集索引集合
     * @param index 属性索引
     * @return
     */
    private double calNodeEntropy(ArrayList<Integer> subset, int index){
        // 数据集剩余个数
        int sum = subset.size();
        double entropy = 0.0;
        int[][] info = new int[attributeValue.get(index).size()][];
        for(int i = 0; i < info.length; i++){
            info[i] = new int[attributeValue.get(decatt).size()];
        }
        int[] count = new int[attributeValue.get(index).size()];
        for(int i = 0; i < sum; i++){
            int n = subset.get(i);
            Integer nodeValue = data.get(n)[index];
            int nodeIndex = attributeValue.get(index).indexOf(nodeValue);
            count[nodeIndex]++;
            Integer decattValue = data.get(n)[decatt];
            int decattIndex = attributeValue.get(decatt).indexOf(decattValue);
            info[nodeIndex][decattIndex]++;
        }
        for(int i = 0; i < info.length; i++){
            entropy += getEntropy(info[i]) * count[i] / sum;
        }
        return entropy;
    }

    /**
     * 建立决策树
     * @param name 节点名
     * @param value 值
     * @param subset 数据集的索引集合
     * @param selatt 属性集的索引集合
     */
    private void buildDecisionTree(String name, String value, ArrayList<Integer> subset, LinkedList<Integer> selatt, Element parent){
        Element element = null;
        List<Element> list = parent.selectNodes(name);
        Iterator<Element> iterator = list.iterator();
        // 确定element的位置
        while(iterator.hasNext()){
            element = iterator.next();
            if(element.attributeValue("value").equals(value)){
                break;
            }
        }
        // 可选属性已为空时结束递归
        if(selatt.size() == 0){
            element.setText(String.valueOf(data.get(subset.get(0))[decatt]));
            return;
        }
        // 如果数据集中所有取值的类标号一致，则创建叶结点
        if(isClassesUnanimous(subset)) {
            element.setText(String.valueOf(data.get(subset.get(0))[decatt]));
            return;
        }
        int minIndex = 0;
        int minEntropySelatt = selatt.get(0);
        double minEntropy = Double.MAX_VALUE;
        // 获取最低属性熵的索引并赋值给minIndex
        for(int i = 1; i < selatt.size(); i++){
            if(i == decatt){
                continue;
            }
            // 计算每个属性的信息熵
            double entropy = calNodeEntropy(subset, selatt.get(i));
            if(entropy < minEntropy){
                minEntropySelatt = selatt.get(i);
                minIndex = i;
                minEntropy = entropy;
            }
        }
        String nodeName = attribute.get(minEntropySelatt);
//        System.out.println(nodeName);
        // 从属性表中去除此属性
        selatt.remove(minIndex);
        ArrayList<Integer> attributeValues = attributeValue.get(minEntropySelatt);
        for(Integer attValue : attributeValues){
            Element newElement = element.addElement(nodeName).addAttribute("value", String.valueOf(attValue));
            ArrayList<Integer> arrayList = new ArrayList<>();
            for (int i = 0; i < subset.size(); i++){
                if(data.get(subset.get(i))[minEntropySelatt].equals(attValue)){
                    arrayList.add(subset.get(i));
                }
            }
            // 样本子集为空，删除该结点
            if(arrayList.size() == 0){
                element.remove(newElement);
                continue;
            }
            // 样本子集全部属于同一个类别，则创建叶子结点并标号
            if(isClassesUnanimous(arrayList)){
                newElement.setText(String.valueOf(data.get(subset.get(0))[decatt]));
                continue;
            }
            // 递归调用建立树
            buildDecisionTree(nodeName, String.valueOf(attValue), arrayList, selatt, element);
        }
    }

    /**
     * 将xml写入文件
     * @param fileName 文件名
     */
    private void writeXML(String fileName){
            try {
                File file = new File(fileName);
                if(!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fileWriter = new FileWriter(file);
                OutputFormat outputFormat = OutputFormat.createPrettyPrint();
                XMLWriter writer = new XMLWriter(fileWriter, outputFormat);
                writer.write(document);
                writer.close();
            } catch (IOException e) {
                logger.error("输出xml失败");
            }
    }

    public static void main(String[] args) {
        Id3Util id3Util = new Id3Util();
        id3Util.init();
        SubmitParam data = new SubmitParam();
        data.setQuestion30(1);
        data.setQuestion4(1);
        data.setQuestion53(1);
        data.setQuestion22(4);
        data.setQuestion73(2);
        data.setQuestion3(2);
        System.out.println(id3Util.getPredictedResult(data));
//        Id3Util inst = new Id3Util();
//        inst.readArff(new File(Constant.TRAINING_SET_FILE));
//        inst.setDecatt("is_like");
//        LinkedList<Integer> ll=new LinkedList<Integer>();
//        for(int i=0;i<inst.attribute.size();i++){
//            if(i!=inst.decatt) {
//                ll.add(i);
//            }
//        }
//        ArrayList<Integer> al=new ArrayList<Integer>();
//        for(int i=0;i<inst.data.size();i++){
//            al.add(i);
//        }
//        inst.buildDecisionTree("DecisionTree", "null", al, ll, root);
//        inst.writeXML(Constant.DECISION_TREE_FILE);
//        return;
    }

    /**
     * 根据决策树获取预测结果
     * @return
     */
    public Integer getPredictedResult(SubmitParam data){
        Element decisionTree = document.getRootElement().element("DecisionTree");
        String predictedResult = getResult(decisionTree, data);
        return CommonValidator.notNull(predictedResult)? Integer.parseInt(predictedResult): null;
    }

    /**
     * 根据问卷数据在决策树中遍历获取结果
     * @param parent 上一个节点
     * @param data 问卷数据
     * @return
     */
    private String getResult(Element parent, SubmitParam data){
        Iterator iterator = parent.elementIterator();
        while(iterator.hasNext()){
            Element next = (Element) iterator.next();
            String methodName = "get_" + next.getName();
            try {
                Integer value = (Integer) MethodUtil.getMethod(methodName, data).invoke(data, null);
                if(next.attributeValue("value").equals(String.valueOf(value))){
                    return getResult(next, data);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return parent.getTextTrim();
    }
}
