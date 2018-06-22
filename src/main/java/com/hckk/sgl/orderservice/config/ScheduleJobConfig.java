package com.hckk.sgl.orderservice.config;

import com.hckk.sgl.orderservice.common.NetUtils;
import com.hckk.sgl.orderservice.service.Impl.WeChatServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sun Guolei 2018/6/21 20:01
 */
@Configuration
@EnableScheduling
public class ScheduleJobConfig {
    private String orderUrl = "192.168.154.219  order.hckk.com";
    private final static Logger logger = LoggerFactory.getLogger(ScheduleJobConfig.class);

    @Scheduled(cron = "0 30 15 * * MON-FRI")
    public void sendOrderNotification() {
        // 产品技术部 ID - 20
        // 技术部 ID - 37
        logger.info("发送订餐通知：");

        Map<String, String> map = new HashMap<>();
        map.put("partyId", "20");
        map.put("content", "努力工作记得照顾肚子，快去订餐，错过了就要饿肚子了！！！\n配置 host\n" + orderUrl);
        WeChatServiceImpl weChatService = WeChatServiceImpl.getSingleton();
        weChatService.sendToEverybody(map);

        logger.info("发送主机名和 IP 地址通知：");
        // 获取内网 IP 地址和主机名
        String nameAndIp = NetUtils.getHostNameAndInternalIP();
        Map<String, String> map1 = new HashMap<>();
        map1.put("receivers", "sunguolei|wanghongpeng");
        map1.put("content", nameAndIp);
        weChatService.sendMessage(map1);
    }

    @Scheduled(cron = "0 10 16 * * MON-FRI")
    public void sendOrderNotification1() {
        // 产品技术部 ID - 20
        // 技术部 ID - 37
        logger.info("发送订餐通知：");
        Map<String, String> map = new HashMap<>();
        map.put("partyId", "20");
        map.put("content", "还有五分钟订餐截止，抓住最后机会，快去订餐，错过了就要饿肚子了！！！\n配置 host\n" + orderUrl);

        WeChatServiceImpl weChatService = WeChatServiceImpl.getSingleton();
        weChatService.sendToEverybody(map);
    }
}
