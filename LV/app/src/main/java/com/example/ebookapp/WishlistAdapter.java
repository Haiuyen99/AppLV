package com.example.ebookapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {

    private List<WishlistModel> wishlistModelList;
    private  boolean wishlist;
   // private  int lastPosition = -1 ;
    private Boolean fromSearch ;


    public Boolean getFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(Boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public WishlistAdapter(List<WishlistModel> wishlistModelList , Boolean wishlist ) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishlist;
    }

    public List<WishlistModel> getWishlistModelList() {
        return wishlistModelList;
    }

    public void setWishlistModelList(List<WishlistModel> wishlistModelList) {
        this.wishlistModelList = wishlistModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlist_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String resource = wishlistModelList.get(position).getProductImage();
        String title = wishlistModelList.get(position).getProductTitle();
        String ratings = wishlistModelList.get(position).getRatings();
        long totalRating = wishlistModelList.get(position).getTotalRatings();
        String price = wishlistModelList.get(position).getProductPrice();
        String productID = wishlistModelList.get(position).getProductID();


       viewHolder.setData(productID,resource,title,ratings,totalRating,price,position);




    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView ratings;
        private TextView totalRatings;
        private TextView productPrice;
        private ImageButton deleteBbtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.product_price);
            ratings = itemView.findViewById(R.id.tv_product_ratings);
            totalRatings = itemView.findViewById(R.id.total_rating);
            deleteBbtn= itemView.findViewById(R.id.delete_btn);
        }

        private void setData(String productID,String resource , String title , String averageRate , long totalRatingsNo,
                             String price, int index){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.banner)).into(productImage);
            productTitle.setText(title);
            ratings.setText(averageRate);
            totalRatings.setText(totalRatingsNo+"(ratings)");
            productPrice.setText(price);
             if(wishlist){
                 deleteBbtn.setVisibility(View.VISIBLE);
             }
             else{
                 deleteBbtn.setVisibility(View.GONE);
             }
            deleteBbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!ProductDetailsActivity.running_wishlist_query) {
                        ProductDetailsActivity.running_wishlist_query = true;
                        DBqueries.removeFromWishList(index, itemView.getContext());
                    }

                }
            });
             itemView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                    // if(fromSearch){
                      // ProductDetailsActivity.fromSearch = true;
                     //}

                     Intent productDetails = new Intent(itemView.getContext(),ProductDetailsActivity.class);
                      productDetails.putExtra("product_ID",productID);
                     itemView.getContext().startActivity(productDetails);
                 }
             });
        }
    }
}
