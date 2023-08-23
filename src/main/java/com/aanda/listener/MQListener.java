package com.aanda.listener;

import com.aanda.model.Message;
import org.springframework.stereotype.Component;

/**
 * @author AANDA
 * @date 2023/8/23 22:44
 * @description
 */
@Component
public class MQListener {

    public void listenerMessage(String request) {
        Message<String> message = new Message<>();
        message.setData(request);
    }

    public void init() {
    }
}
