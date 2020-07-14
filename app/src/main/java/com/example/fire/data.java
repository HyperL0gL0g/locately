package com.example.fire;

public class data {

    private  String name,address,phone,lat,lng;

    public data(){}

    public data(String name, String address, String phone,String lat,String lng) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.lat=lat;
        this.lng=lng;
    }

    public data(String lat,String lng){
        this.lat = lat;
        this.lng = lng;
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
