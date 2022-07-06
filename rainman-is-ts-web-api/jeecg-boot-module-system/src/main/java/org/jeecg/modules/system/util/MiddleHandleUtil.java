package org.jeecg.modules.system.util;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiddleHandleUtil {
    private static Logger log = LoggerFactory.getLogger(MiddleHandleUtil.class);

    /**
     * TODO 验证accessToken
     * {
     * "refresh_token": "7ed8a285-b95a-495d-814f-0452001b260c",
     * "code": "200",
     * "username": "yaoqin"
     * }
     * 验证accessToken并获取用户昵称
     *
     * @param accessToken
     * @return
     */
    public static String getByAccessToken(String url, String accessToken) {
        url = url + "?access_token=" + accessToken;
        String rs = HttpConnectUtil.doGet(url);
        log.info("getByAccessToken认证结果={}", rs);
        JSONObject json = JSONObject.parseObject(rs);
        if ("200".equals(json.getString("code"))) {
            return json.getString("username");
        }
        return null;
    }

}
