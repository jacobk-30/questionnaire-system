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
        id3Util.init();
        return id3Util;
    }
}
