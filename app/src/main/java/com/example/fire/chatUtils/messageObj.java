package com.example.fire.chatUtils;

public class messageObj {
    String message,senderID,messageID,chatID,timestamp;

    public messageObj(String message, String senderID, String messageID, String chatID, String timestamp) {
        this.message = message;
        this.senderID = senderID;
        this.messageID = messageID;
        this.chatID = chatID;
        this.timestamp = timestamp;
    }

    public messageObj(){}

    public messageObj(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderID() {
        return senderID;
    }

    public String getMessageID() {
        return messageID;
    }

    public String getChatID() {
        return chatID;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
