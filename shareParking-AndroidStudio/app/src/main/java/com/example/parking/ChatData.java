package com.example.parking;

import java.io.Serializable;

public class

ChatData implements Serializable{
    private String msg;
    private String nickname;
    private String username;
    private String parkingAdmin;

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getParkingAdmin() { return parkingAdmin; }
    public void setParkingAdmin(String parkingAdmin) { this.parkingAdmin = parkingAdmin; }
}
