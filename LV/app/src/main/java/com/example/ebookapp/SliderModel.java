package com.example.ebookapp;

public class SliderModel {

    private  String banner;
    private String backroundColor;

    public SliderModel(String banner, String backroundColor) {
        this.banner = banner;
        this.backroundColor = backroundColor;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBackroundColor() {
        return backroundColor;
    }

    public void setBackroundColor(String backroundColor) {
        this.backroundColor = backroundColor;
    }
}
