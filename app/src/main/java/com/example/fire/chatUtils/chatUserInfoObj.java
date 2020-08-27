package com.example.fire.chatUtils;

public class chatUserInfoObj  {
    String deviceID,deviceName,allowed,username;

    public chatUserInfoObj(String deviceID, String deviceName,String allowed,String username) {
        this.deviceID = deviceID;
        this.deviceName = deviceName;
        this.allowed = allowed;
        this.username = username;
    }

    public chatUserInfoObj(String deviceID, String username) {
        this.deviceID = deviceID;
        this.username = username;
    }


    public  chatUserInfoObj(){}

    public String getAllowed(){return  allowed;}
    public String getDeviceName() {
        return deviceName;
    }
    public String getusername() {
        return username;
    }
    public String getDeviceID() {
        return deviceID;
    }

}
