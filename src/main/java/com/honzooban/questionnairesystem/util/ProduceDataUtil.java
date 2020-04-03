package com.honzooban.questionnairesystem.util;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName test.java
 * @Description
 * @createTime 2020年04月02日 12:40:00
 */
public class ProduceDataUtil {

    public static void main(String[] args) {
        Random random = new Random(4);
        for(int j = 0; j <= 2000; j++){
            String[] array1 = {"5","4","3","2","1"};
            String[] array2 = {"0","1"};
            int i = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while(i < 80){
                stringBuilder.append(array1[random.nextInt(5)] + ",");
                i++;
            }
            stringBuilder.append(array2[random.nextInt(2)]);
            System.out.println(stringBuilder);
        }
    }
}
