package com.cineflex.api.service;

import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketPushService {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketPushService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendToBillngQueue(UUID id, String message) {
        System.out.println(id);
        System.out.println(message);
        messagingTemplate.convertAndSend("/bill." + id.toString(), message);
    }
}
