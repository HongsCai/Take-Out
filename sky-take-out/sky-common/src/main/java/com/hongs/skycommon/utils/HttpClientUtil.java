package com.hongs.skycommon.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongs.skycommon.json.JacksonBaseConfig;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpClientUtil {
    private final static int TIMEOUT = 5 * 1000;
    private final static String CHARSET = "UTF-8";
    private final static String CONTENT_TYPE_JSON = "application/json";

    private final static ObjectMapper objectMapper = JacksonBaseConfig.createObjectMapper();

    private static RequestConfig builderRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(TIMEOUT)
                .setConnectionRequestTimeout(TIMEOUT)
                .setSocketTimeout(TIMEOUT)
                .build();
    }

    public static String doGet(String url, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";

        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    if (entry.getValue() != null) {
                        uriBuilder.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
                    }
                }
            }
            URI uri = uriBuilder.build();
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setConfig(builderRequestConfig());
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), CHARSET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(response, httpClient);
        }
        return result;
    }

    public static String doPost(String url, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";

        try {
            HttpPost httpPost = new HttpPost(url);
            if (params != null) {
                List<NameValuePair> nameValuePairs = params.entrySet().stream()
                        .filter(entry -> entry.getValue() != null)
                        .map(entry -> new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()))
                ).collect(Collectors.toList());
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, CHARSET);
                httpPost.setEntity(entity);
            }
            httpPost.setConfig(builderRequestConfig());
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), CHARSET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(response, httpClient);
        }
        return result;
    }

    public static String doPostJson(String url, Map<String, Object> params) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";

        try {
            HttpPost httpPost = new HttpPost(url);
            if (params != null) {
                String json = objectMapper.writeValueAsString(params);
                StringEntity stringEntity = new StringEntity(json, CHARSET);
                stringEntity.setContentType(CONTENT_TYPE_JSON);

                httpPost.setEntity(stringEntity);
            }
            httpPost.setConfig(builderRequestConfig());
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                result = EntityUtils.toString(response.getEntity(), CHARSET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources(response, httpClient);
        }
        return result;
    }

    // 提取一个通用的关闭资源方法
    private static void closeResources(CloseableHttpResponse response, CloseableHttpClient httpClient) {
        try {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Object> jsonToMap(String json) {
        Map<String, Object> result = null;
        try {
             result = objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
