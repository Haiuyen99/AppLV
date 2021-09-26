package com.example.ebookapp;

public class ChapterModel {
    private  String  chapterNumber;
    private  String  chapterName;
    private String chaptercontent;

    public ChapterModel(String chapterNumber, String chapterName, String chaptercontent) {

        this.chapterNumber = chapterNumber;
        this.chapterName = chapterName;
        this.chaptercontent = chaptercontent;
    }

    public String getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(String chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChaptercontent() {
        return chaptercontent;
    }

    public void setChaptercontent(String chaptercontent) {
        this.chaptercontent = chaptercontent;
    }
}
