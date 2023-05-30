package com.gangyunshihua.utils;

import com.gangyunshihua.pojo.Config;
import com.gangyunshihua.pojo.SSLClient;
import com.github.qcloudsms.SmsSingleSender;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

/**
 * 用HttpClient得到Entity工具类
 */
public class HttpClientUtil {

    //发短信
    public static void sendMessage() throws Exception {
        try {
            SmsSingleSender ssender = new SmsSingleSender(Config.SmsSdkAppid, Config.SmsAppKey);
            ssender.sendWithParam("86", Config.myMobile, Config.SmsTemplateID, new String[0], Config.SmsSign, "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getGetValue(String url) throws Exception {
        //避免需要证书 引用新建的SSLClient
        CloseableHttpClient httpClient = new SSLClient();
        HttpGet httpGet = new HttpGet(url);
        //设置连接超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
        httpGet.setConfig(requestConfig);
        //执行请求内容
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        String value = null;
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                value = EntityUtils.toString(entity, "utf-8");
            }
        }
        httpResponse.close();
        httpClient.close();
        return value;
    }

    public static String getPostValue(String url, String jsonString) throws Exception {
        //避免需要证书 引用新建的SSLClient
        CloseableHttpClient httpClient = new SSLClient();
        HttpPost httpPost = new HttpPost(url);
        //设置HttpEntity
        StringEntity stringEntity = new StringEntity(jsonString, "utf-8");
        httpPost.setEntity(stringEntity);
        //设置连接超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(5000).build();
        httpPost.setConfig(requestConfig);
        //执行请求内容
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        String value = null;
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                value = EntityUtils.toString(entity, "utf-8");
            }
        }
        httpResponse.close();
        httpClient.close();
        return value;
    }
}

