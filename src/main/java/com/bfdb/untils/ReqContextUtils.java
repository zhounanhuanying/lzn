package com.bfdb.untils;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * 人证核验终端设备，请求
 */
public class ReqContextUtils {


    /**
     * put请求以及参数是json
     *
     * @param url
     * @param jsonParams
     * @return
     */
    public static HttpResponseResult doPostForJson(String url, String jsonParams) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        JSONObject jsonObject = null;
        HttpResponseResult httpResponseResult =null;
        HttpPut httpPut = new HttpPut( url );
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout( 180 * 1000 ).setConnectionRequestTimeout( 180 * 1000 )
                .setSocketTimeout( 180 * 1000 ).setRedirectsEnabled( true ).build();
        httpPut.setConfig( requestConfig );
        httpPut.setHeader( "Content-Type", "application/json" );
        try {
            httpPut.setEntity( new StringEntity( jsonParams, ContentType.create( "application/json", "utf-8" ) ) );
//            System.out.println( "request parameters" + EntityUtils.toString( httpPut.getEntity() ) );
//            System.out.println( "httpPut:" + httpPut );
            HttpResponse response = httpClient.execute( httpPut );
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString( response.getEntity() );
                //解析json
                jsonObject = JSONObject.parseObject(result);
                String data = jsonObject.getString("Response");
                 httpResponseResult = JSON.parseObject(data, HttpResponseResult.class);
//                System.out.println( "httpResponseResult:" + httpResponseResult );
                return httpResponseResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != httpClient) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return httpResponseResult;
        }
    }

}

