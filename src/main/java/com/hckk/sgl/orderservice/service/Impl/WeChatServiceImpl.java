package com.hckk.sgl.orderservice.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.hckk.sgl.orderservice.common.HTTPReqGen;
import com.hckk.sgl.orderservice.common.StringUtils;
import com.hckk.sgl.orderservice.service.WeChatService;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sun Guolei 2018/6/21 20:08
 */
public class WeChatServiceImpl implements WeChatService {
    private static final Logger logger = LoggerFactory.getLogger(WeChatServiceImpl.class);
    private final String agentId = "1000008";
    private static String accessToken;
    private static long lastTime = 0;

    private static volatile WeChatServiceImpl singleton = null;

    private WeChatServiceImpl() {
    }

    // 单例模式，保证只有一个微信工具实例
    public static WeChatServiceImpl getSingleton() {
        if (singleton == null) {
            // 线程锁，保证同一时间只有一个线程处理下面的代码
            synchronized (WeChatServiceImpl.class) {
                if (singleton == null) {
                    singleton = new WeChatServiceImpl();
                }
            }
        }
        return singleton;
    }
//
//    private Object readResolve() {
//        return singleton;
//    }

    /**
     * 使用方法
     */
    @Override
    public void sendMessage(Map<String, String> params) {

        String receivers = params.get("receivers");
        String content = params.get("content");

        getAccessToken();
        HTTPReqGen reqGen = new HTTPReqGen();
        Response response;
        String json;
        JsonPath jsonPath;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("touser", StringUtils.replaceBlank(receivers.trim()));
        jsonObject.put("msgtype", "text");
        jsonObject.put("agentid", agentId);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("content", content);
        jsonObject.put("text", jsonObject1);
        jsonObject.put("safe", "0");

        // 发送信息给指定的人员
        String method = "POST";
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;
        reqGen.setCallType(method);
        reqGen.setCallString(StringUtils.replaceBlank(url.trim()));
        reqGen.setBody(jsonObject.toString());
        Map<String, String> mapHeaders = new HashMap<>();
        // 消息字符编码设置为 UTF-8
        mapHeaders.put("Content-Type", "text/plain; charset=UTF-8");
        reqGen.setHeaders(mapHeaders);
        response = reqGen.perform_request();
        json = response.asString();
        logger.info("返回的微信信息为{}", json);

        jsonPath = new JsonPath(json);
        String errorMessage = jsonPath.get("errmsg");

        logger.info(errorMessage);
    }

    /**
     * 使用方法,发送给研发部所有人订餐通知
     */
    @Override
    public void sendToEverybody(Map<String, String> params) {

        String partyId = params.get("partyId");
        String content = params.get("content");

        getAccessToken();

        HTTPReqGen reqGen = new HTTPReqGen();
        Response response;
        String json;
        JsonPath jsonPath;

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("toparty", partyId);
        jsonObject.put("totag", "");
        jsonObject.put("msgtype", "text");
        jsonObject.put("agentid", agentId);
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("content", content);
        jsonObject.put("text", jsonObject1);
        jsonObject.put("safe", "0");

        // 发送信息给指定的人员
        String method = "POST";
        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;
        reqGen.setCallType(method);
        reqGen.setCallString(StringUtils.replaceBlank(url.trim()));
        reqGen.setBody(jsonObject.toString());
        Map<String, String> mapHeaders = new HashMap<>();
        // 消息字符编码设置为 UTF-8
        mapHeaders.put("Content-Type", "text/plain; charset=UTF-8");
        reqGen.setHeaders(mapHeaders);
        response = reqGen.perform_request();
        json = response.asString();
        logger.info("返回的微信信息为{}", json);

        jsonPath = new JsonPath(json);
        String errorMessage = jsonPath.get("errmsg");

        logger.info(errorMessage);
    }

    private void getAccessToken() {
        long currentTime = System.currentTimeMillis();
        HTTPReqGen reqGen = new HTTPReqGen();
        Response response;
        String json;
        JsonPath jsonPath;
        String method = "GET";
        String corpId = "wwa5bfb8f102dad85f";
        String corpSecret = "h1WjDBGrywLTeg5tskZgD39kD8dUH2-Nd9zvmF8vCqY";
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpId + "&corpsecret=" + corpSecret;

        logger.info("当前时间为：{}", currentTime);
        logger.info("lastTime 为：{}", lastTime);

        // 如果 accessToken 超时，就重新获取一遍
        if (lastTime == 0 || (currentTime - lastTime) / 1000 > 7200) {
            lastTime = currentTime;

            reqGen.setCallType(method);
            reqGen.setCallString(StringUtils.replaceBlank(url.trim()));

            response = reqGen.perform_request();

            json = response.asString();

            jsonPath = new JsonPath(json);
            accessToken = jsonPath.get("access_token");

            logger.info(accessToken);
        }
    }

//    private void getUserInfo(String userId) {
//        HTTPReqGen reqGen = new HTTPReqGen();
//        Response response;
//        String json;
//
//        getAccessToken();
//        String method = "GET";
//        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token=" + accessToken + "&department_id=106958";
//        reqGen.setCallType(method);
//        reqGen.setCallString(StringUtils.replaceBlank(url.trim()));
//        response = reqGen.perform_request();
//        json = response.prettyPrint();
//        logger.info("返回的微信信息为{}", json);
//    }

//    public static void main(String[] args) {
//        WeChatServiceImpl weChatService = WeChatServiceImpl.getSingleton();
//        Map<String, String> map = new HashMap<>();
//        map.put("partyId", "37");
//        map.put("content", "企业微信测试");
//        weChatService.sendToEverybody(map);
//    }
}
