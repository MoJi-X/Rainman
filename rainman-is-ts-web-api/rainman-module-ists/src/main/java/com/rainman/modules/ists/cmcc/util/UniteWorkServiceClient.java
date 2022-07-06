package com.rainman.modules.ists.cmcc.util;

import com.rainman.modules.ists.cmcc.dto.UniteWork;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.jeecg.common.util.SpringContextUtils;

import java.util.TimeZone;

/**
 * Title: UniteWorkServiceClient
 * Description: 发送服务
 *
 * @author 李京泽
 * @date 2021年12月7日
 */
@Slf4j
public class UniteWorkServiceClient {

    private static final String FIVE = "5";

    private static final String SIX = "6";


    public static String getRequestUrl() {
        return SpringContextUtils.getApplicationContext().getEnvironment()
                .getProperty("rainman.cmcc.url", String.class,
                        "http://10.154.142.222:20925/com.tw.ei.commonservices.unitework.service.WsUniteWorkSyncService?wsdl");
    }

    public static boolean getEnabled() {
        return SpringContextUtils.getApplicationContext().getEnvironment()
                .getProperty("rainman.cmcc.enabled", Boolean.class,
                        false);
    }

    public static String getAppCode() {
        return SpringContextUtils.getApplicationContext().getEnvironment()
                .getProperty("rainman.cmcc.app-code", String.class,
                        "xxaqglzcpt");
    }

    public static String getRequestMothed() {
        return "syncUniteWork";
    }

    /**
     * 调用OA WebService
     *
     * @param uniteWork
     * @return
     */
    public static String requestWebService(UniteWork uniteWork) {
        if (!getEnabled()) {
            return null;
        }

        String result = null;
        String strAppId = getAppCode();

        try {
            XStream xstream = new XStream();
            xstream.autodetectAnnotations(true);
            xstream.registerConverter(new DateConverter("yyyy-MM-dd HH:mm:ss", null, TimeZone.getTimeZone("GMT+8")));
            String requestXml = xstream.toXML(uniteWork);
            log.info("统一待办请求xml:" + requestXml);
            JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
            org.apache.cxf.endpoint.Client client = dcf
                    .createClient(getRequestUrl());
            result = client.invoke(getRequestMothed(), requestXml, strAppId)[0].toString();
            log.info("统一待办返回结果：" + result);
            if (FIVE.equals(result) || SIX.equals(result)) {
                //数据入库失败  || 其他错误原因,重发
                UniteWorkServiceClient.requestWebService(uniteWork);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
