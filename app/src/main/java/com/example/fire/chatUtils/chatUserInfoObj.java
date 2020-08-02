package com.example.fire.chatUtils;

public class chatUserInfoObj  {
    String deviceID,deviceName;

    public chatUserInfoObj(String deviceID, String deviceName) {
        this.deviceID = deviceID;
        this.deviceName = deviceName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceID() {
        return deviceID;
    }

}
