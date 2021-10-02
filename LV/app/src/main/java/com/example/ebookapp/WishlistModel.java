package com.example.ebookapp;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WishlistModel {
    private  String productImage;
    private  String productTitle;
    private  String ratings;
    private  long totalRatings;
    private  String productPrice;
    private  String productID;
    private ArrayList<String> tags;

    public WishlistModel(String productID,String productImage, String productTitle, String ratings, long totalRatings, String productPrice) {
        this.productID = productID;
        this.productImage = productImage;
        this.productTitle = productTitle;
        this.ratings = ratings;
        this.totalRatings = totalRatings;
        this.productPrice = productPrice;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public long getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(long totalRatings) {
        this.totalRatings = totalRatings;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
