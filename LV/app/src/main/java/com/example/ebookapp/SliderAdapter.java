package com.example.ebookapp;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

 public class SliderAdapter extends PagerAdapter {

    private List<SliderModel> sliderModelList;

    public SliderAdapter(List<SliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

     @Override
     public int getCount() {
         return  sliderModelList.size();
     }
     @Override
     public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {

         return view == object;
     }
     @Override
     public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
         container.removeView((View)object);
     }

     @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
     @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
         View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slider_layout,container,false);
         ConstraintLayout bannerContainer = view.findViewById(R.id.banner_container);
         bannerContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sliderModelList.get(position).getBackroundColor())));
         ImageView banner = view.findViewById(R.id.banner_slider);
         Glide.with(container.getContext()).load(sliderModelList.get(position).getBanner()).apply(new RequestOptions().placeholder(R.drawable.banner)).into(banner);
         container.addView(view,0);
        return view;
    }


 }
