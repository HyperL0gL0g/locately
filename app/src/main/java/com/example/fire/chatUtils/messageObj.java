package com.example.fire.chatUtils;

import java.util.ArrayList;

public class messageObj {
    String message,senderID,messageID,chatID,timestamp;

    String requestIDs;

    public messageObj(String message, String senderID, String messageID, String chatID, String timestamp,String requestIDs) {
        this.message = message;
        this.senderID = senderID;
        this.messageID = messageID;
        this.chatID = chatID;
        this.timestamp = timestamp;
        this.requestIDs = requestIDs;
    }


    public messageObj(String message,String timestamp,String requestIDs,String messageID,String chatID) {
        this.messageID = messageID;
        this.message = message;
        this.timestamp = timestamp;
        this.requestIDs = requestIDs;
        this.chatID = chatID;
    }



    public messageObj(){}

    public messageObj(String message){
        this.message = message;
    }

    public String getrequestIDs(){return requestIDs;}

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
