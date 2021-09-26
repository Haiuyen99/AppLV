package com.example.ebookapp.ui.home;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ebookapp.GridProductLayoutAdapter;
import com.example.ebookapp.HorizontalProductModel;
import com.example.ebookapp.HorizontalProductScrollAdapter;
import com.example.ebookapp.ProductDetailsActivity;
import com.example.ebookapp.R;
import com.example.ebookapp.SliderAdapter;
import com.example.ebookapp.SliderModel;
import com.example.ebookapp.ViewAllActivity;
import com.example.ebookapp.WishlistModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    private List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;


    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType){
            case HomePageModel.BANNER_SLIDER:
                View bannerSliderview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sliding_ad_layout,viewGroup,false);
                return new BannerSliderViewholder(bannerSliderview);
            case HomePageModel.STRIP_AD_BANNER:
                View stripAdView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.strip_ad_layout,viewGroup,false);
                return new StripBannerViewholder(stripAdView);
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                View horizonatlProductView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_layout,viewGroup,false);
                return new HorizontalProductViewholder(horizonatlProductView);

            case HomePageModel.GRID_PRODUCT_VIEW:
                View gridProductView= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_product_view,viewGroup,false);
                return new GridProductViewholder(gridProductView);
            default:
                return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
         switch (homePageModelList.get(position).getType()){
             case HomePageModel.BANNER_SLIDER:
                 List<SliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                 ((BannerSliderViewholder) viewHolder).setBannerSliderViewpager(sliderModelList);
                 break;

              case HomePageModel.STRIP_AD_BANNER:
                  String resource = homePageModelList.get(position).getResource();
                  String color = homePageModelList.get(position).getBackroundColor();
                  ((StripBannerViewholder) viewHolder).setStripAd(resource,color);
                  break;

             case HomePageModel.HORIZONTAL_PRODUCT_VIEW:

                 String horizontaltitle = homePageModelList.get(position).getTitle();
                 List<HorizontalProductModel>horizontalProductModelList = homePageModelList.get(position).getHorizontalProductModelList();
                 List<WishlistModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
                 ((HorizontalProductViewholder) viewHolder).setHorizontalProductLayout(horizontalProductModelList,horizontaltitle,viewAllProductList);
                 break;

             case HomePageModel.GRID_PRODUCT_VIEW:
                 String gridLayoutTitle = homePageModelList.get(position).getTitle();
                 List<HorizontalProductModel>gridProductModelList = homePageModelList.get(position).getHorizontalProductModelList();
                 List<WishlistModel> viewAllProductList1 = homePageModelList.get(position).getViewAllProductList();
                 ((GridProductViewholder) viewHolder).setGridProductLayout(gridProductModelList,gridLayoutTitle,viewAllProductList1);

             default:
                 return;
         }

         


    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    @Override
    public int getItemViewType(int position) {
       switch (homePageModelList.get(position).getType()){
           case 0:
               return HomePageModel.BANNER_SLIDER;
           case 1:
               return HomePageModel.STRIP_AD_BANNER;
           case 2:
               return HomePageModel.HORIZONTAL_PRODUCT_VIEW;
           case 3:
               return HomePageModel.GRID_PRODUCT_VIEW;

           default:
               return -1;
       }
    }

    public class BannerSliderViewholder extends RecyclerView.ViewHolder{

        private ViewPager bannerSliderViewpager;
        private  List<SliderModel> sliderModelList;

        private  int currentPage ;
        private Timer timer;
        final private long DELAY_TIME = 3000;
        final private  long PERIOD_TIME =3000;
        private List<SliderModel> arrangedList;


        public BannerSliderViewholder(@NonNull View itemView) {
            super(itemView);


            bannerSliderViewpager= itemView.findViewById(R.id.banner_slider_view_pager);


        }
        private  void setBannerSliderViewpager(List<SliderModel> sliderModelList){

            currentPage = 2;
            if(timer != null){
                timer.cancel();
            }
            arrangedList = new ArrayList<>();
            for(int x=0 ; x<sliderModelList.size(); x++){
                arrangedList.add(x,sliderModelList.get(x));
            }
            arrangedList.add(0,sliderModelList.get(sliderModelList.size()-2));
            arrangedList.add(1,sliderModelList.get(sliderModelList.size()-1));
            arrangedList.add(sliderModelList.get(0));
            arrangedList.add(sliderModelList.get(1));
            SliderAdapter sliderAdapter =  new SliderAdapter(arrangedList);
            bannerSliderViewpager.setAdapter(sliderAdapter);
            bannerSliderViewpager.setClipToPadding(false);
            bannerSliderViewpager.setPageMargin(20);
            bannerSliderViewpager.setCurrentItem(currentPage);

            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int i, float v, int il)   {


                }

                @Override
                public void onPageSelected(int i) {
                    currentPage = i;
                }

                @Override
                public void onPageScrollStateChanged(int i) {
                    if(i == ViewPager.SCROLL_STATE_IDLE){
                        pagerLooper(arrangedList);
                    }

                }
            };

            bannerSliderViewpager.addOnPageChangeListener(onPageChangeListener);
            startbannerSliderShow(sliderModelList);

            bannerSliderViewpager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pagerLooper(sliderModelList);
                    stopbannerSliderShow();
                    if(event.getAction() == MotionEvent.ACTION_UP){
                        startbannerSliderShow(sliderModelList);
                    }
                    return false;
                }
            });

        }
        private void  pagerLooper(List<SliderModel> sliderModelList){
            if( currentPage == 1){
                currentPage = sliderModelList.size()-3;
                bannerSliderViewpager.setCurrentItem(currentPage);

            }
        }

        private void startbannerSliderShow( final List<SliderModel> sliderModelList) {
            Handler handler = new Handler();
            Runnable update  = new Runnable() {
                @Override
                public void run() {
                    if (currentPage>= sliderModelList.size()){
                        currentPage = 1;
                    }

                    bannerSliderViewpager.setCurrentItem(currentPage++,true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            },DELAY_TIME,PERIOD_TIME );

        }

        private void stopbannerSliderShow(){
            timer.cancel();
        }


    }

    public class StripBannerViewholder extends RecyclerView.ViewHolder{
        private ImageView stripAdImage;
        private ConstraintLayout stripadContainer;

        public StripBannerViewholder(@NonNull View itemView) {
            super(itemView);

            stripAdImage = itemView.findViewById(R.id.strip_ad_image);
            stripadContainer = itemView.findViewById(R.id.strip_ad_container);
        }
        private void setStripAd(String resource , String color ){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.banner)).into(stripAdImage);
            stripadContainer.setBackgroundColor(Color.parseColor(color));
        }
    }
    public class HorizontalProductViewholder extends RecyclerView.ViewHolder{
        private TextView horizontalLayoutTitle;
        private Button horizontalLayoutviewAllbtn;
        private  RecyclerView horizontalReclerView;

        public HorizontalProductViewholder(@NonNull View itemView) {
            super(itemView);
            horizontalLayoutTitle= itemView.findViewById(R.id.horizontal_scroll_layout_title);
            horizontalLayoutviewAllbtn = itemView.findViewById(R.id.horizontal_scroll_layout_btn);
            horizontalReclerView = itemView.findViewById(R.id.horizontal_scroll_layout_recyclerview);
            horizontalReclerView.setRecycledViewPool(recycledViewPool);
        }
        private void setHorizontalProductLayout(List<HorizontalProductModel>horizontalProductModelList, String title, List<WishlistModel>viewAllProductList){

            horizontalLayoutTitle.setText(title);

            HorizontalProductScrollAdapter horizontalProductScrollAdapter = new HorizontalProductScrollAdapter(horizontalProductModelList);
            LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(itemView.getContext());
            linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalReclerView.setLayoutManager(linearLayoutManager1);
            horizontalReclerView.setAdapter(horizontalProductScrollAdapter);
            horizontalProductScrollAdapter.notifyDataSetChanged();

            if(horizontalProductModelList.size()>2){
                horizontalLayoutviewAllbtn.setVisibility(View.VISIBLE);
                horizontalLayoutviewAllbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewAllActivity.wishlistModelList = viewAllProductList;
                        Intent viewAllIntent = new Intent(itemView.getContext(),ViewAllActivity.class);
                        viewAllIntent.putExtra("layout_code",0);
                        viewAllIntent.putExtra("title",title);
                        itemView.getContext().startActivity(viewAllIntent);
                    }
                });

            }else {
                horizontalLayoutviewAllbtn.setVisibility(View.INVISIBLE);
            }



        }
    }

    public class  GridProductViewholder extends RecyclerView.ViewHolder{
        private TextView gridLayoutTitle;
        private Button gridLayoutButton;
        private GridLayout gridView;

        public GridProductViewholder(@NonNull View itemView) {
            super(itemView);
            gridLayoutTitle   = itemView.findViewById(R.id.grid_layout_title);
            gridLayoutButton  = itemView.findViewById(R.id.grid_layout_btn);
            gridView          = itemView.findViewById(R.id.grid_layout_product);

        }

        private void setGridProductLayout( List<HorizontalProductModel> horizontalProductModelList ,String title,List<WishlistModel>viewAllProductList){
            gridLayoutTitle.setText(title);


            for (int x =0 ; x<4;x++){
                ImageView productImage = gridView.getChildAt(x).findViewById(R.id.product_img);
                TextView productTitle = gridView.getChildAt(x).findViewById(R.id.product_title);
                TextView productdescription = gridView.getChildAt(x).findViewById(R.id.product_description);
                TextView productprice = gridView.getChildAt(x).findViewById(R.id.product_price);

                Glide.with(itemView.getContext()).load(horizontalProductModelList.get(x).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.banner)).into(productImage);
                productTitle.setText(horizontalProductModelList.get(x).getProductTitle());
                productdescription.setText(horizontalProductModelList.get(x).getProductDescription());
                productprice.setText(horizontalProductModelList.get(x).getProductPrice());

                int finalX = x;
                gridView.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productDetailsIntent = new Intent( itemView.getContext(), ProductDetailsActivity.class);
                        productDetailsIntent.putExtra("product_ID",horizontalProductModelList.get(finalX).getProductID());
                        itemView.getContext().startActivity(productDetailsIntent);
                    }
                });
           }
            gridLayoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewAllActivity.wishlistModelList = viewAllProductList;
                    Intent viewAllIntent = new Intent(itemView.getContext(),ViewAllActivity.class);
                    viewAllIntent.putExtra("layout_code",0);
                    viewAllIntent.putExtra("title",title);
                    itemView.getContext().startActivity(viewAllIntent);
                }
            });


        }
    }


}
