package com.example.fire;

public class data {

    private  String name,address,phone,lat,lng,online,uid;

    public data(){}

    public data(String name, String address, String phone,String lat,String lng,String online,String uid) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.lat=lat;
        this.lng=lng;
        this.online=online;
        this.uid = uid;
    }

    public data(String lat,String lng){
        this.lat = lat;
        this.lng = lng;

    }
    public data (String online)
    {
        this.online=online;
    }

    public String getUid() {
        return uid;
    }

    public String getOnline() {
        return online;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }


    public String getLat(){
    return lat;
    }
    public String getLng(){
        return lng;
    }
}
