package com.example.fire;

public class data {

    private  String name,address,phone;
    private double lat,lng;

    public data(){}

    public data(String name, String address, String phone,double lat,double lng) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.lat=lat;
        this.lng=lng;
    }

    public data(double lat,double lng){
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


    public double getLat(){
    return lat;
    }
    public double getLng(){
        return lng;
    }
}
