package com.example.onfest;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Festival {
    int imageResId;       // 축제 이미지 리소스 ID
    String title;         // 축제 이름
    String subtitle;      // 축제 부제목
    String startDate;     // 시작 날짜 (YYYY-MM-DD 형식)
    String endDate;       // 끝나는 날짜 (YYYY-MM-DD 형식)
    String body;
    String location;      // 축제 지역
    @SerializedName("shop_list")
    private List<Festival> fesetival_list;
    public List<Festival> getFestival_list() {
        return fesetival_list;
    }

    public String getTitle() {return title;}
    public String getSubtitle() { return subtitle; }
    public String getStartDate() { return startDate; }
    public String getEndDate() {
        return endDate;
    }
    public String getBody() { return body;}
    public String getLocation() { return location;}
    public Festival(int imageResId, String title, String subtitle, String startDate, String endDate, String location) {
        this.imageResId = imageResId;
        this.title = title;
        this.subtitle = subtitle;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
    }
}