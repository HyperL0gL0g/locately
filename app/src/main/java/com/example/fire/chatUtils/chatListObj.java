package com.example.fire.chatUtils;

public class chatListObj {
    String deviceName,deviceID;


    public chatListObj(){

    }

    public chatListObj(String id, String name) {
        this.deviceName = name;
        this.deviceID = id;
    }


    public String getdeviceName() {
        return deviceName;
    }

    public String getdeviceID() {
        return deviceID;
    }


    public void setName(String name) {
        this.deviceName = name;
    }

    public void setId(String id) {
        this.deviceID = id;
    }
}

