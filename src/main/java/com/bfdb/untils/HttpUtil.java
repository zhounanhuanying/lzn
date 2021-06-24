package com.bfdb.untils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtil {
    private static Logger logger = Logger.getLogger( HttpUtil.class );

    private static final Integer RESPONSEFAIL = 599;

    public static CloseableHttpClient digestHttpClient(String username, String password, String host, Integer port) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials( new AuthScope( StringUtils.isBlank( host ) ? AuthScope.ANY_HOST : host, port == null ? AuthScope.ANY_PORT : port ),
                new UsernamePasswordCredentials( username, password ) );
        return HttpClients.custom().setDefaultCredentialsProvider( credentialsProvider ).build();
    }

    /**
     * get请求
     * @return
     */
    public static HttpResponseResult doGet(String url, String username, String password) {
        try {
            CloseableHttpClient client = digestHttpClient(username, password, null, null);
            //发送get请求
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                InputStream content = response.getEntity().getContent();
                String strResult = EntityUtils.toString(response.getEntity());
//                System.out.println(strResult);
                //解析json
                JSONObject jsonObject = JSONObject.parseObject(strResult);
                String data = jsonObject.getString("Response");
//                System.out.println(data);
                HttpResponseResult httpResponseResult = JSON.parseObject(data, HttpResponseResult.class);
                return httpResponseResult;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static HttpResponse doGetImage(String url, String username, String password) {
        CloseableHttpClient client = digestHttpClient( username, password, null, null );
        //发送get请求
        HttpGet request = new HttpGet( url );
        HttpResponse response = null;
        try {
            response = client.execute( request );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
    /**
     * post请求（用于请求json格式的参数）
     * @param url
     * @param params
     * @return
     */
    public static HttpResponseResult doPost(String url, String params,String username, String password) throws Exception {

        CloseableHttpClient client = digestHttpClient(username,password,null,null);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                .setSocketTimeout(5000).build();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setConfig(requestConfig);
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        HttpResponseResult httpResponseResult = new HttpResponseResult();
        try {
            //到这里卡住了，不向下执行了。但不抛出任何错误，也没有被catch捕获
            response = client.execute(httpPost);
            if(response!=null){
                StatusLine status = response.getStatusLine();
                int state = status.getStatusCode();
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    HttpEntity responseEntity = response.getEntity();
                    String jsonString = EntityUtils.toString(responseEntity);
                    //解析json
                    JSONObject jsonObject = JSONObject.parseObject(jsonString);
                    String data = jsonObject.getString("Response");
//                System.out.println(data);
                     httpResponseResult = JSON.parseObject(data, HttpResponseResult.class);
                    return httpResponseResult;
                }
                else{
                    logger.error("请求返回:"+state+"("+url+")");
                }
            }else{
                httpResponseResult.setResponseCode(-5);
                return httpResponseResult;
            }
        }
        finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else{
                httpResponseResult.setResponseCode(-5);
                return httpResponseResult;
            }
        }
        return new HttpResponseResult();
    }


    public static String post(String url, String parms) {
        //创建一个httpClient对象，相当于创建了一个浏览器，用来访问URL链接
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                .setSocketTimeout(5000).build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setConfig(requestConfig);
        //创建JSON实体
        StringEntity requestEntity = new StringEntity(JSON.toJSONString(parms), ContentType.APPLICATION_JSON);
        requestEntity.setContentEncoding("UTF-8");
        //封装实体
        httpPost.setEntity(requestEntity);
        JSONObject res = null;
        String data=null;
        try {
            //执行
            CloseableHttpResponse response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                String result = EntityUtils.toString(response.getEntity());
                // 返回json格式：
                res = JSONObject.parseObject(result);
                data = res.getString("Response");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;

    }
    /**
     * put请求（用于请求json格式的参数）
     *
     * @param url
     * @param params
     * @return
     */
    public static HttpResponseResult doPut(String url, String params, String username, String password) throws Exception {
        CloseableHttpClient client = digestHttpClient( username, password, null, null );
        HttpPut httpPut = new HttpPut( url );
        httpPut.setHeader( "Accept", "application/json" );
        httpPut.setHeader( "Content-Type", "application/json" );
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity( params, charSet );
        httpPut.setEntity( entity );
        CloseableHttpResponse response = null;
        try {
            response = client.execute( httpPut );
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString( responseEntity );
                //解析json
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                String data = jsonObject.getString("Response");
//                String result=jsonString.substring(jsonString.indexOf("{\n" +"\t\t\"ResponseURL"),jsonString.length()-1);
                HttpResponseResult httpResponseResult = JSON.parseObject(data, HttpResponseResult.class);
                return httpResponseResult;
            } else {
                logger.error( "请求返回:" + state + "(" + url + ")" );
                HttpResponseResult httpResponseResult = new HttpResponseResult();
                httpResponseResult.setResponseCode( state );
                return httpResponseResult;
            }
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * delete请求
     *
     * @return
     */
    public static HttpResponseResult doDelete(String url, String username, String password) {
        HttpResponseResult httpResponseResult = new HttpResponseResult();
        try {
            CloseableHttpClient client = digestHttpClient( username, password, null, null );
            //发送get请求
            HttpDelete request = new HttpDelete( url );
            HttpResponse response = client.execute( request );
            /**请求发送成功，并得到响应**/
            int statusCode = response.getStatusLine().getStatusCode();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String jsonString = EntityUtils.toString( response.getEntity() );
                //解析json
                JSONObject jsonObject = JSONObject.parseObject(jsonString);
                String data = jsonObject.getString("Response");
//                String result = jsonString.substring( 15, jsonString.length() - 1 );
                httpResponseResult = JSON.parseObject( data, HttpResponseResult.class );
                return httpResponseResult;
            } else if (response.getStatusLine().getStatusCode() == RESPONSEFAIL) {
                httpResponseResult.setResponseCode( RESPONSEFAIL );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return httpResponseResult;
    }


    /**
     * post请求(用于key-value格式的参数)
     *
     * @param url
     * @param params
     * @return
     */
    public static String doPost(String url, String username, String password, Map params) {

        BufferedReader in = null;
        try {
            // 定义HttpClient
            CloseableHttpClient client = digestHttpClient( username, password, null, null );
            // 实例化HTTP方法
            HttpPost request = new HttpPost();
            request.setURI( new URI( url ) );

            //设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
                String name = (String) iter.next();
                String value = String.valueOf( params.get( name ) );
                nvps.add( new BasicNameValuePair( name, value ) );

                //System.out.println(name +"-"+value);
            }
            request.setEntity( new UrlEncodedFormEntity( nvps, StandardCharsets.UTF_8 ) );

            HttpResponse response = client.execute( request );
            int code = response.getStatusLine().getStatusCode();
            if (code == 200) {    //请求成功
                in = new BufferedReader( new InputStreamReader( response.getEntity()
                        .getContent(), "utf-8" ) );
                StringBuffer sb = new StringBuffer( "" );
                String line = "";
                String NL = System.getProperty( "line.separator" );
                while ((line = in.readLine()) != null) {
                    sb.append( line + NL );
                }

                in.close();

                return sb.toString();
            } else {    //
                System.out.println( "状态码：" + code );
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 验证IP是否可连接
     *
     * @param host
     * @param timeOut
     * @return
     */
    public static boolean isHostReachable(String host, Integer timeOut) {
        if (isIP( host ) && timeOut != null) {
            try {
                return InetAddress.getByName( host ).isReachable( timeOut );
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 验证是否为IP
     *
     * @param addr
     * @return
     */
    public static boolean isIP(String addr) {
        if (addr == null || "".equals( addr )) {
            return false;
        }
        if (addr.length() < 7 || addr.length() > 15 || "".equals( addr )) {
            return false;
        }
        /**
         * 判断IP格式和范围
         */
//        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
//        Pattern pat = Pattern.compile( rexp );
//        Matcher mat = pat.matcher( addr );
//        boolean ipAddress = mat.find();
//        return ipAddress;
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(rexp);
        Matcher matcher = pattern.matcher(addr);
        boolean matches = matcher.matches();
        return matches;
    }


}
