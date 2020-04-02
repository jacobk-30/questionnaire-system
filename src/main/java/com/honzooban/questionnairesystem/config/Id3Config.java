package com.honzooban.questionnairesystem.config;

import com.honzooban.questionnairesystem.common.Constant;
import com.honzooban.questionnairesystem.util.Id3Util;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName Id3Config.java
 * @Description
 * @createTime 2020年04月02日 19:43:00
 */
@Configuration
public class Id3Config {

    /**
     * 启动容器时创建决策树并存储在服务器中
     * @return
     */
    @Bean
    Id3Util id3Util(){
        Id3Util id3Util = new Id3Util();
        id3Util.setDecatt(Constant.DECATT);
        LinkedList<Integer> attributeIndexList =new LinkedList<>();
        for(int i = 0; i < id3Util.getAttribute().size(); i++){
            if(i != id3Util.getDecatt()) {
                attributeIndexList.add(i);
            }
        }
        ArrayList<Integer> dataIndexList = new ArrayList<>();
        for(int i = 0; i < id3Util.getData().size(); i++){
            dataIndexList.add(i);
        }
        id3Util.buildDecisionTree("DecisionTree", null, dataIndexList, attributeIndexList);
        id3Util.writeXML(Constant.DECISION_TREE_FILE);
        return id3Util;
    }
}
