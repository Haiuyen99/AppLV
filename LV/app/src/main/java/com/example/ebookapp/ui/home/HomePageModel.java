package com.example.ebookapp.ui.home;


import com.example.ebookapp.HorizontalProductModel;
import com.example.ebookapp.SliderModel;
import com.example.ebookapp.WishlistModel;

import java.util.List;

public class HomePageModel  {

    public static final int BANNER_SLIDER =0;
    public static final int STRIP_AD_BANNER=1;
    public static final int HORIZONTAL_PRODUCT_VIEW = 2;
    public static final int GRID_PRODUCT_VIEW = 3;
    private String backroundColor;
    private  int type ;
    private List<SliderModel> sliderModelList;


    public HomePageModel(int type,List<SliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;

    }


    public void setViewAllProductList(List<WishlistModel> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }

    public List<SliderModel> getSliderModelList() {
        return sliderModelList;
    }

    public void setSliderModelList(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private  String resource ;


    public HomePageModel(int type, String resource, String backroundColor) {
        this.type = type;
        this.resource = resource;
        this.backroundColor = backroundColor;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getBackroundColor() {
        return backroundColor;
    }

    public void setBackroundColor(String backroundColor) {
        this.backroundColor = backroundColor;
    }


    private String title;
    private List<HorizontalProductModel> horizontalProductModelList;
    private List<WishlistModel> viewAllProductList;
    public HomePageModel(int type, String title, List<HorizontalProductModel> horizontalProductModelList, List<WishlistModel> viewAllProductList) {
        this.type = type;
        this.title = title;
        this.horizontalProductModelList = horizontalProductModelList;
        this.viewAllProductList=viewAllProductList;
    }

    public List<WishlistModel> getViewAllProductList() {
        return viewAllProductList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<HorizontalProductModel> getHorizontalProductModelList() {
        return horizontalProductModelList;
    }

    public void setHorizontalProductModelList(List<HorizontalProductModel> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
    }



    public HomePageModel(int type, String title, List<HorizontalProductModel> horizontalProductModelList) {
        this.type = type;
        this.title = title;
        this.horizontalProductModelList = horizontalProductModelList;
    }


}