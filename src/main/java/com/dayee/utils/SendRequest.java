package com.dayee.utils;



import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.slf4j.LoggerFactory;

/**
 * 发送请求
 * 
 * @author Legend、
 */
@SuppressWarnings("deprecation")
public class SendRequest {

    public static final int HTTP_TIMEOUT = 1000000;

    // 这是模拟get请求
    public static Result sendGet(String url,
                                 Map<String, String> headers,
                                 Map<String, String> params,
                                 String encoding,
                                 boolean duan)
            throws ClientProtocolException, IOException {

        LoggerFactory.getLogger(SendRequest.class).info("请求URL===>" + url + ",params===>" + params.toString());
        HttpClient client = getHttpClient();
        // 如果有参数的就拼装起来
        url = url + (null == params ? "" : assemblyParameter(params));
        // 这是实例化一个get请求
        HttpGet hp = new HttpGet(url);
        // 如果需要头部就组装起来
        if (null != headers) {
            hp.setHeaders(assemblyHeader(headers));
        }
        HttpContext context = HttpClientContext.create();
        // 执行请求后返回一个HttpResponse
        HttpResponse response = client.execute(hp, context);
        // 如果为true则断掉这个get请求
        if (duan) {
            hp.abort();
        }
        // 返回一个HttpEntity
        HttpEntity entity = response.getEntity();
        // 封装返回的参数
        Result result = new Result();
        // 设置返回的cookie
        CookieStore cookieStore = (CookieStore) context.getAttribute(HttpClientContext.COOKIE_STORE);
        if (cookieStore != null) {
            result.setCookie(assemblyCookie(cookieStore.getCookies()));
        }
        // 设置返回的状态
        result.setStatusCode(response.getStatusLine().getStatusCode());
        // 设置返回的头部信心
        result.setHeaders(response.getAllHeaders());
        // 设置返回的信息
        result.setHttpEntity(entity);
        LoggerFactory.getLogger(SendRequest.class).info("请求URL===>" + url);
        return result;
    }

    public static HttpClient getHttpClient() {

        // 设置协议http和https对应的处理socket链接工厂的对象
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(createIgnoreVerifySSL())).build();
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        HttpClients.custom().setConnectionManager(connManager);

        RequestConfig config = RequestConfig.custom().setConnectTimeout(HTTP_TIMEOUT).setSocketTimeout(HTTP_TIMEOUT)
                .build();
        HttpClientBuilder builder = HttpClients.custom();
        builder.setDefaultRequestConfig(config);
        builder.setConnectionManager(connManager);
        return builder.build();
    }

    public static SSLContext createIgnoreVerifySSL() {

        SSLContext sc = null;

        try {
            
            sc = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) {
                    return true;
                }
            }).build();
            
            // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
            X509TrustManager trustManager = new X509TrustManager() {

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                               String paramString)
                         {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] paramArrayOfX509Certificate,
                                               String paramString)
                        {

                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                    return null;
                }
                
            };

            sc.init(null, new TrustManager[] { trustManager }, null);
        } catch (Exception e) {
        }
        return sc;
    }

    public static Result sendGet(String url) throws ClientProtocolException, IOException {

        return sendGet(url, new HashMap<String, String>(), new HashMap<String, String>(), "UTF-8", false);
    }

    public static Result sendGet(String url, Map<String, String> headers, Map<String, String> params, String encoding)
            throws ClientProtocolException, IOException {

        return sendGet(url, headers, params, encoding, false);
    }

    public static Result sendGetHttps(String url)
            throws NoSuchAlgorithmException, KeyManagementException, ClientProtocolException, IOException {

        LoggerFactory.getLogger(SendRequest.class).info("请求URL===>" + url);
        RequestConfig config = RequestConfig.custom().setConnectTimeout(HTTP_TIMEOUT).setSocketTimeout(HTTP_TIMEOUT)
                .build();

        HttpClientBuilder builder = HttpClients.custom();
        builder.setDefaultRequestConfig(config);
        builder.setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE);

        HttpClient httpClient = builder.build();
        HttpContext context = HttpClientContext.create();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpget, context);
        HttpEntity entity = response.getEntity();

        Result result = new Result();
        result.setHttpEntity(entity);
        CookieStore cookieStore = (CookieStore) context.getAttribute(HttpClientContext.COOKIE_STORE);
        if (cookieStore != null) {
            result.setCookie(assemblyCookie(cookieStore.getCookies()));
        }
        return result;
    }

    // 这是模拟post请求
    public static Result sendPost(String url, Map<String, String> headers, Map<String, String> params, String encoding)
            throws ClientProtocolException, IOException {

        LoggerFactory.getLogger(SendRequest.class).info("请求URL===>" + url + ",params===>" + params.toString());
        // 设置需要提交的参数
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (String temp : params.keySet()) {
            list.add(new BasicNameValuePair(temp, params.get(temp)));
        }
        return sendPost(url, headers, new UrlEncodedFormEntity(list, encoding), encoding, new HttpPost(url));

    }

    // 这是模拟post请求
    public static Result sendPut(String url, Map<String, String> headers, Map<String, String> params, String encoding)
            throws ClientProtocolException, IOException {

        LoggerFactory.getLogger(SendRequest.class).info("请求URL===>" + url + ",params===>" + params.toString());
        // 设置需要提交的参数
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        for (String temp : params.keySet()) {
            list.add(new BasicNameValuePair(temp, params.get(temp)));
        }
        return sendPost(url, headers, new UrlEncodedFormEntity(list, encoding), encoding, new HttpPut(url));

    }

    // 这是模拟post请求
    public static Result sendPost(String url, Map<String, String> headers, String body, String encoding)
            throws ClientProtocolException, IOException {

        LoggerFactory.getLogger(SendRequest.class).info("请求URL===>" + url + ",params===>" + body);
        BasicHttpEntity requestBody = new BasicHttpEntity();
        requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
        requestBody.setContentLength(body.getBytes("UTF-8").length);

        return sendPost(url, headers, requestBody, encoding, new HttpPost(url));

    }

    // 这是模拟post请求
    public static Result sendPost(String url,
                                  Map<String, String> headers,
                                  HttpEntity httpEntity,
                                  String encoding,
                                  HttpEntityEnclosingRequestBase post)
            throws ClientProtocolException, IOException {

        HttpClient client = getHttpClient();

        post.setEntity(httpEntity);

        // 设置头部
        if (null != headers) {
            post.setHeaders(assemblyHeader(headers));
        }
        HttpContext context = HttpClientContext.create();
        // 实行请求并返回
        HttpResponse response = client.execute(post, context);
        HttpEntity entity = response.getEntity();

        // 封装返回的参数
        Result result = new Result();
        // 设置返回状态代码
        result.setStatusCode(response.getStatusLine().getStatusCode());
        // 设置返回的头部信息
        result.setHeaders(response.getAllHeaders());
        // 设置返回的cookie信心
        CookieStore cookieStore = (CookieStore) context.getAttribute(HttpClientContext.COOKIE_STORE);
        if (cookieStore != null) {
            result.setCookie(assemblyCookie(cookieStore.getCookies()));
        }
        // 设置返回到信息
        result.setHttpEntity(entity);
        LoggerFactory.getLogger(SendRequest.class).info("请求URL===>" + url);
        ;
        return result;
    }

    // 这是组装头部
    public static Header[] assemblyHeader(Map<String, String> headers) {

        Header[] allHeader = new BasicHeader[headers.size()];
        int i = 0;
        for (String str : headers.keySet()) {
            allHeader[i] = new BasicHeader(str, headers.get(str));
            i++;
        }
        return allHeader;
    }

    // 这是组装cookie
    public static String assemblyCookie(List<Cookie> cookies) {

        StringBuilder sbu = new StringBuilder();
        for (Cookie cookie : cookies) {
            sbu.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }
        if (sbu.length() > 0) {
            sbu.deleteCharAt(sbu.length() - 1);
        }
        return sbu.toString();
    }

    // 这是组装参数
    public static <T> String assemblyParameter(Map<String, T> parameters) {

        StringBuilder para = new StringBuilder("?");
        for (String str : parameters.keySet()) {
            para.append(str).append("=").append(parameters.get(str)).append("&");
        }
        return para.substring(0, para.length() - 1);
    }
}
