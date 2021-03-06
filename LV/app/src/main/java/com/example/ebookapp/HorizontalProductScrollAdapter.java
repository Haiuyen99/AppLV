package com.example.ebookapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class HorizontalProductScrollAdapter extends RecyclerView.Adapter <HorizontalProductScrollAdapter.ViewHolder>{

    private List<HorizontalProductModel> horizontalProductModelList;

    public HorizontalProductScrollAdapter(List<HorizontalProductModel> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
    }

    @NonNull
    @Override
    public HorizontalProductScrollAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.horizontal_scroll_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalProductScrollAdapter.ViewHolder viewHolder, int position) {
        String resource = horizontalProductModelList.get(position).getProductImage();
        String title =horizontalProductModelList.get(position).getProductTitle();
        String description = horizontalProductModelList.get(position).getProductDescription();
        String price = horizontalProductModelList.get(position).getProductPrice();
        String productID= horizontalProductModelList.get(position).getProductID();

        viewHolder.setData(productID,resource,title,description,price);
    }

    @Override
    public int getItemCount() {
     if (horizontalProductModelList.size()>8){
         return 8;
     }else {
         return  horizontalProductModelList.size();
     }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle , productDescription, productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_img);
            productTitle = itemView.findViewById(R.id.product_title);
            productPrice = itemView.findViewById(R.id.product_price);
            productDescription = itemView.findViewById(R.id.product_description);



        }
        private  void setData(String productID, String resource,String title ,String description,String price){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.banner)).into(productImage);
            productTitle.setText(title);
            productPrice.setText(price);
            productDescription.setText(description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent(itemView.getContext(),ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("product_ID",productID);
                    itemView.getContext().startActivity(productDetailsIntent);
                }
            });

        }


        private void setProductImage(String resource){

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.banner)).into(productImage);
        }

        private void setProductTitle( String title){
            productTitle.setText(title);
        }

        private void setProductDescription( String description){
            productDescription.setText(description);
        }

        private void setProductPrice( String price){

            productPrice.setText(price);
        }
        private void setProductID (String ID){

            productPrice.setText(ID);
        }
    }
}
