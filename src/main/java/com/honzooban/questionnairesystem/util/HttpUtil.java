package com.honzooban.questionnairesystem.util;

import com.alibaba.druid.support.json.JSONUtils;
import com.honzooban.questionnairesystem.common.Constant;
import com.honzooban.questionnairesystem.util.vaild.CommonValidator;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;


/**
 * @author honzooban
 * @version 1.0.0
 * @ClassName HttpUtil.java
 * @Description http工具类
 * @createTime 2020年12月28日 12:55:00
 */
public class HttpUtil {

    /**
     * 发送get请求
     * @param url 请求地址
     * @param map 参数集合
     * @return 响应结果
     */
    public static JSONObject doGet(String url, Map<String, Object> map){
        CloseableHttpClient client = HttpClients.createDefault();
        String modifiedUrl = url;
        Integer paramCount = 1;
        if(CommonValidator.notNull(map)){
            for(Map.Entry<String, Object> entry : map.entrySet()){
                if(paramCount == 1){
                    // 请求参数只有一个时
                    modifiedUrl = modifiedUrl + ("?" + entry.getKey() + "=" + entry.getValue());
                }else{
                    // 请求参数有多个时
                    modifiedUrl = modifiedUrl + ("&&" + entry.getKey() + "=" + entry.getValue());
                }
                paramCount++;
            }
        }
        HttpGet get = new HttpGet(modifiedUrl);
        get.setHeader("Accept", Constant.CONTENT_TYPE_JSON);
        try {
            HttpResponse response = client.execute(get);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                HttpEntity entity = response.getEntity();
                return JSONObject.fromObject(EntityUtils.toString(entity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发送post请求
     * @param url 请求地址
     * @param map 参数集合
     * @return 响应结果
     */
    public static JSONObject doPost(String url, Map<String, Object> map) {
        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        String jsonParam = JSONUtils.toJSONString(map);
        try {
            StringEntity s = new StringEntity(jsonParam, Constant.ED_UTF_8);
            s.setContentType(Constant.CONTENT_TYPE_JSON);
            httpPost.setEntity(s);
            HttpResponse response = client.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                return JSONObject.fromObject(EntityUtils.toString(entity));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
