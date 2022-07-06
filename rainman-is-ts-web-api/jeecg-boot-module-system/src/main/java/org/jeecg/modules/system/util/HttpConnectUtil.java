package org.jeecg.modules.system.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xiezhisheng
 * @version V1.0
 * @Title: http 请求工具类
 * @description: 提供get/post 请求方式
 * @mail xiezhisheng123@163.com
 * @category com.rainmain
 * @date 2019年3月8日
 */
public class HttpConnectUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpConnectUtil.class);

    public static String doGet(String serverUrl) {
        InputStream in = null;
        String result = "";
        try {
            long a = System.currentTimeMillis();
            URL url = new URL(serverUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(60 * 1000);// 设置五秒等待时间
            conn.setRequestMethod("GET");
            in = conn.getInputStream();
            // out = conn.getOutputStream();
            byte[] bytedata = readInputStream(in);
            String json = new String(bytedata, "UTF-8");
            logger.info(url + " :" + (System.currentTimeMillis() - a));
            result = json;
        } catch (Exception e) {
            logger.info("请求接口={}，异常信息={}", serverUrl, e);
            JSONObject js = new JSONObject();
            js.put("code", 500);
            js.put("message", e.getMessage());
            return js.toJSONString();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String doPost(String serverUrl, String data, long timeout) {
        StringBuilder responseBuilder = null;
        BufferedReader reader = null;
        OutputStreamWriter wr = null;
        URL url;
        try {
            url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(1000 * 5);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            responseBuilder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (wr != null) {
                try {
                    wr.close();
                } catch (IOException e) {
                    logger.error("close error", e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("close error", e);
                }
            }
        }
        return responseBuilder.toString();
    }

    /**
     * @param urlPath
     * @param requestParam json格式参数
     * @return
     */
    public static String doGet(String urlPath, JSON requestParam) {
        InputStream in = null;
        String result = "";
        try {
            long a = System.currentTimeMillis();
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //conn.setReadTimeout(60 * 1000);//设置五秒等待时间
            conn.setConnectTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            conn.setReadTimeout(10000);
///			 conn.setRequestProperty("Content-type","application/x-java-serialized-object");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setDoOutput(true);//设置允许输出 JSON格式的输出
            conn.setDoInput(true);
//			conn.setRequestMethod("POST");
//			conn.setRequestProperty("User-Agent", "Fiddler");
//			conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Charset", "UTF_8");
            //参数输入流
            OutputStream os = conn.getOutputStream();
            byte[] bytes = requestParam.toString().getBytes();
            os.write(bytes);
            os.flush();
            os.close();
            in = conn.getInputStream();
            byte[] rsbytes = readInputStream(in);
            String json = new String(rsbytes, "UTF-8");
            result = json;
        } catch (Exception e) {
            logger.info("错误:{}", urlPath);
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    /**
     * @param urlPath  请求路径
     * @param jsonText json格式字符串
     * @return
     */
    public static String doGet(String urlPath, String jsonText) {
        if (jsonText != null && "".equals(jsonText)) {
            JSONObject jsonObj = JSONObject.parseObject(jsonText);
            return doGet(urlPath, jsonObj);
        } else return doGet(urlPath);
    }

    /**
     * @param url
     * @param param
     * @return
     */
    public static String doGet(String url, Map<String, String> param) {

        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultString;
    }


    /**
     * @param url   请求地址
     * @param param map参数
     * @return
     */
    public static String doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null && param.size() > 0) {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

    /**
     * 参数为JSON格式字符串
     *
     * @param url
     * @param json
     * @return
     */
    public static String doPost(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return resultString;
    }

    /**
     * 参数为JSON格式字符串
     *
     * @param url
     * @param json
     * @return
     */
    public static String doPost(String url, JSON json) {
        if (json == null) return doPost(url, new HashMap());
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json.toJSONString(), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return resultString;
    }


    private static byte[] readInputStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        return outStream.toByteArray();
    }
}
