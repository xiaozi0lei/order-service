package com.hckk.sgl.orderservice.service;

import java.util.Map;

public interface WeChatService {
    void sendMessage(Map<String, String> params);
    void sendToEverybody(Map<String, String> params);
}
