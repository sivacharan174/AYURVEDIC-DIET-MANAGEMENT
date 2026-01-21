package com.saveetha.ayurnutrition;

public class ChatMessage {
    public static final int TYPE_USER = 0;
    public static final int TYPE_AI = 1;

    public String message;
    public int type;
    public long timestamp;

    public ChatMessage(String message, int type) {
        this.message = message;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    public boolean isUser() {
        return type == TYPE_USER;
    }

    public boolean isAI() {
        return type == TYPE_AI;
    }
}
