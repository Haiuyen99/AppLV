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

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
private List<CategoryModel> catoryModelList;

  public  CategoryAdapter(List<CategoryModel> catoryModelList){
      this.catoryModelList =    catoryModelList;
  }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
      View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_item,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder viewholder, int position) {
      String icon = catoryModelList.get(position).getCategoryIconLink();
      String name = catoryModelList.get(position).getCategoryName();
      viewholder.setCategoryName(name,position);

      viewholder.setCategoyIcon(icon);

    }

    @Override
    public int getItemCount() {
        return  catoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

      private ImageView categoyIcon;
      private TextView categoryName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoyIcon = itemView.findViewById(R.id.category_icon);
            categoryName= itemView.findViewById(R.id.category_name);

        }

        private void setCategoyIcon(String iconUrl){

          if (!iconUrl.equals("null")){
            Glide.with(itemView.getContext()).load(iconUrl).apply(new RequestOptions().placeholder(R.drawable.ic_home)).into(categoyIcon);
          }

        }

        private  void setCategoryName( final String name , int position){

          categoryName.setText(name);
          itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (position !=0) {
                Intent categoryIntent = new Intent(itemView.getContext(), CategoryActivity.class);
                categoryIntent.putExtra("CategoryName", name);
                itemView.getContext().startActivity(categoryIntent);
              }
            }
          });
        }
    }
}
