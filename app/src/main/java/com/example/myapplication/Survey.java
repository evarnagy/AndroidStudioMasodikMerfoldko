package com.example.myapplication;

import java.util.List;

public class Survey {
    private String id;
    private String userId;
    private String favoriteColor;
    private String carType;
    private String age;
    private long timestamp;

    public Survey() {

    }

    public Survey(String id, String userId, String favoriteColor, String carType, String age, long timestamp) {
        this.id = id;
        this.userId = userId;
        this.favoriteColor = favoriteColor;
        this.carType = carType;
        this.age = age;
        this.timestamp = timestamp;
    }


    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getFavoriteColor() { return favoriteColor; }
    public void setFavoriteColor(String favoriteColor) { this.favoriteColor = favoriteColor; }

    public String getCarType() { return carType; }
    public void setCarType(String carType) { this.carType = carType; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}
