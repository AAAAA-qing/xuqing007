package com.fh.shopapi.utils;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.*;

public class HttpClientUtil {

    //java后台代码 发起请求
    public static String sendPost(String url, Map<String, String> headers, Map<String, String> params) {
        String result = "";
        CloseableHttpClient client = null;
        HttpPost httpPost = null;
        HttpResponse response = null;
        try {
            client = HttpClientBuilder.create().build();
            httpPost = new HttpPost(url);
            if (headers != null && headers.size() > 0) {
                Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    // 设置请求的header
                    httpPost.addHeader(key, value);
                }
            }
            // 设置请求的的参数，requestBody参数
            List<NameValuePair> nvps = new ArrayList<>();
            if (params != null && params.size() > 0) {
                Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    String key = entry.getKey();
                    String value = entry.getValue();
                    BasicNameValuePair basicNameValuePair = new BasicNameValuePair(key, value);
                    nvps.add(basicNameValuePair);
                }
            }
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, "utf-8");
            httpPost.setEntity(urlEncodedFormEntity);
            // 执行请求
            response = client.execute(httpPost);
            result = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            try {
                if (response != null) {
                    client.close();
                }
                if (httpPost != null) {
                    httpPost.releaseConnection();
                }
                if (client != null) {
                    client.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }



}
