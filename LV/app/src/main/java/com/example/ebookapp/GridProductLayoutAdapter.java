package com.example.ebookapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class GridProductLayoutAdapter extends BaseAdapter {
    List<HorizontalProductModel> horizontalProductModelList;

    public GridProductLayoutAdapter(List<HorizontalProductModel> horizontalProductModelList) {
        this.horizontalProductModelList = horizontalProductModelList;
    }

    @Override
    public int getCount() {

        return horizontalProductModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item, null);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsIntent = new Intent( parent.getContext(),ProductDetailsActivity.class);
                    productDetailsIntent.putExtra("product_ID",horizontalProductModelList.get(position).getProductID());
                    parent.getContext().startActivity(productDetailsIntent);
                }
            });



            ImageView productImage = view.findViewById(R.id.product_img);
            TextView  productTitle = view.findViewById(R.id.product_title);
            TextView  productDescription = view.findViewById(R.id.product_description);
            TextView productPrice = view.findViewById(R.id.product_price);


            Glide.with(parent.getContext()).load(horizontalProductModelList.get(position).getProductImage()).apply(new RequestOptions().placeholder(R.drawable.banner)).into(productImage);
            productTitle.setText(horizontalProductModelList.get(position).getProductTitle());
            productDescription.setText(horizontalProductModelList.get(position).getProductDescription());
            productPrice.setText(horizontalProductModelList.get(position).getProductPrice());

        } else {
           view = convertView;
        }
        return  view;
    }
}
