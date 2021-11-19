package com.example.ebookapp;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.ebookapp.ui.home.HomePageAdapter;
import com.example.ebookapp.ui.home.HomePageModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBqueries {


    public  static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    public static String email, fullname, profile ;
    public static List<CategoryModel> categoryModelList = new ArrayList<>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesNames = new ArrayList<String>();
    public static  List<String> wishlist = new ArrayList<>();
    public  static List<WishlistModel> wishlistModelList = new ArrayList<>();
    public  static  List<String> myRatedIds = new ArrayList<>();
    public  static List<Long> myRating = new ArrayList<>();


    public  static void loadCategories(final CategoryAdapter categoryAdapter, final Context context){
       categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()){
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(),documentSnapshot.get("CategoryName").toString()));
                            }
                           categoryAdapter.notifyDataSetChanged();
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    public  static  void loadFragmentData(HomePageAdapter adapter ,Context context , final int index ,String CategoryName){
        firebaseFirestore.collection("CATEGORIES")
                .document(CategoryName.toUpperCase())
                .collection("Banners").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot:task.getResult()){
                                if((long)documentSnapshot.get("view_type")==0){
                                    List<SliderModel>sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long)documentSnapshot.get("no_of_banners");
                                    for (long x =1; x < no_of_banners+1 ; x++){
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner_"+x).toString()
                                                ,documentSnapshot.get("banner_"+x+"_backround").toString()));
                                    }

                                    lists.get(index).add(new HomePageModel(0,sliderModelList));
                                }else if ((long)documentSnapshot.get("view_type")==1){
                                    lists.get(index).add(new HomePageModel(1,documentSnapshot.get("strip_ad_banner").toString()
                                            , documentSnapshot.get("backround").toString()));
                                }else if((long)documentSnapshot.get("view_type")==2){


                                    List<WishlistModel> viewAllProductList = new ArrayList<>();
                                    List<HorizontalProductModel> horizontalProductModelList =  new ArrayList<>();
                                    long no_of_products = (long)documentSnapshot.get("no_of_products");
                                    for (long x =1; x < no_of_products +1 ; x++) {
                                        horizontalProductModelList.add(new HorizontalProductModel(documentSnapshot.get("product_ID_"+x).toString()                                                ,documentSnapshot.get("story_image_"+x).toString()
                                                ,documentSnapshot.get("story_title_"+x).toString()
                                                ,documentSnapshot.get("story_author_"+x).toString()
                                                ,documentSnapshot.get("genre of stories_"+x).toString()));

                                        viewAllProductList.add(new WishlistModel(
                                                documentSnapshot.get("product_ID_"+x).toString()
                                                ,documentSnapshot.get("story_image_"+x).toString()
                                                ,documentSnapshot.get("story_title_"+x).toString()
                                                ,documentSnapshot.get("rating_"+x).toString()
                                                ,(long)documentSnapshot.get("total_rating_"+x)
                                                ,documentSnapshot.get("story_author_"+x).toString()));
                                    }

                                    lists.get(index).add(new HomePageModel(2,documentSnapshot.get("layout_title").toString(),horizontalProductModelList, viewAllProductList));
                                }else if((long)documentSnapshot.get("view_type")==3){

                                    List<HorizontalProductModel> gridProductModelList =  new ArrayList<>();
                                    List<WishlistModel> viewAllProductList = new ArrayList<>();
                                    long no_of_products = (long)documentSnapshot.get("no_of_products");
                                    for (long x =1; x < no_of_products +1 ; x++) {
                                        gridProductModelList.add(new HorizontalProductModel(documentSnapshot.get("product_ID_"+x).toString()
                                               , documentSnapshot.get("story_image_"+x).toString()
                                                ,documentSnapshot.get("story_title_"+x).toString()
                                                ,documentSnapshot.get("story_author_"+x).toString()
                                                ,documentSnapshot.get("genre of stories_"+x).toString()));

                                        viewAllProductList.add(new WishlistModel(
                                                documentSnapshot.get("product_ID_"+x).toString()
                                                ,documentSnapshot.get("story_image_"+x).toString()
                                                ,documentSnapshot.get("story_title_"+x).toString()
                                                ,documentSnapshot.get("rating_"+x).toString()
                                                ,(long)documentSnapshot.get("total_rating_"+x)
                                                ,documentSnapshot.get("story_author_"+x).toString()));


                                    }

                                    lists.get(index).add(new HomePageModel(3,documentSnapshot.get("layout_title").toString(),gridProductModelList, viewAllProductList));
                                }

                            }
                            adapter.notifyDataSetChanged();
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadWishList(final Context context ,final Dialog dialog , final Boolean loadProductData){
        wishlist.clear();
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_WISHLIST")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    for (long x= 0; x< (long)task.getResult().get("list_size"); x++){
                            wishlist.add(task.getResult().get("product_ID_"+x).toString());
                        if (DBqueries.wishlist.contains(ProductDetailsActivity.productID)) {
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = true;

                            if(ProductDetailsActivity.addToWishlistBtn != null) {
                                ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(context.getResources().getColorStateList(R.color.colorPrimary));
                            }
                        } else {
                            if(ProductDetailsActivity.addToWishlistBtn != null) {
                                ProductDetailsActivity.addToWishlistBtn.setSupportBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                            }
                            ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                        }

                            if(loadProductData) {
                                wishlist.clear();
                                String productID = task.getResult().get("product_ID_" + x).toString();
                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            wishlistModelList.add(new WishlistModel(
                                                   productID
                                                    ,task.getResult().get("story_image_1").toString()
                                                    , task.getResult().get("story_title").toString()
                                                    , task.getResult().get("average_rating").toString()
                                                    , (long) task.getResult().get("total_ratings")
                                                    , task.getResult().get("story_author").toString()));

                                            MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                                        } else {
                                            String error = task.getException().getMessage();
                                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                    }

                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
                  dialog.dismiss();
            }
        });

    }




    public static void RemoveFromWishList( int index, Context context){
        wishlist.remove(index);
        Map<String,Object> updateWishlist = new HashMap<>();

        for (int x=0 ; x<wishlist.size(); x++){
            updateWishlist.put("product_ID_"+x,wishlist.get(x));
        }
        updateWishlist.put("list_size", (long) wishlist.size());
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_WISHLIST")
                .set(updateWishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()){

                       if(wishlistModelList.size() !=0){
                           wishlistModelList.remove(index);
                           MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                       }
                       ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                       Toast.makeText(context,"Bạn đã xóa thành công!",Toast.LENGTH_SHORT).show();

                       ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));

                       
                   }else {


                       String error = task.getException().getMessage();
                       Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                   }
                //if(ProductDetailsActivity.addToWishlistBtn != null) {

                  //  ProductDetailsActivity.addToWishlistBtn.setEnabled(true);
                //}

                ProductDetailsActivity.running_wishlist_query = false;
            }
        });


    }

    public static void removeFromWishList( int index, Context context){

        Map<String,Object> updateWishlist = new HashMap<>();

        for (int x=0 ; x<wishlist.size(); x++){
            updateWishlist.put("product_ID_"+x,wishlist.get(x));
        }
        updateWishlist.put("list_size", (long) wishlist.size());
        firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA")
                .document("MY_WISHLIST")
                .set(updateWishlist).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    if(wishlistModelList.size() !=0){
                        wishlistModelList.remove(index);
                        MyWishlistFragment.wishlistAdapter.notifyDataSetChanged();
                    }
                    ProductDetailsActivity.ALREADY_ADDED_TO_WISHLIST = false;
                    Toast.makeText(context,"Removed successfully!",Toast.LENGTH_SHORT).show();

                   // ProductDetailsActivity.addToWishlistBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));


                }else {


                    String error = task.getException().getMessage();
                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show();
                }
                //if(ProductDetailsActivity.addToWishlistBtn != null) {

                //  ProductDetailsActivity.addToWishlistBtn.setEnabled(true);
                //}

                ProductDetailsActivity.running_wishlist_query = false;
            }
        });


    }

    public static void loadRating (Context context){
        if(!ProductDetailsActivity.running_rating_query) {
            ProductDetailsActivity.running_rating_query = true;
            myRatedIds.clear();
            myRating.clear();
            firebaseFirestore.collection("USERS").document(FirebaseAuth.getInstance().getUid())
                    .collection("USER_DATA").document("MY_RATINGS")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        for (long x = 0; x < (long) task.getResult().get("list_size"); x++) {
                            myRatedIds.add(task.getResult().get("product_ID_" + x).toString());
                            myRating.add((long) task.getResult().get("rating_" + x));

                            if (task.getResult().get("product_ID_" + x).toString().equals(ProductDetailsActivity.productID)) {
                                ProductDetailsActivity.initialRating = Integer.parseInt(String.valueOf((long) task.getResult().get("rating_" + x))) - 1;
                                if(ProductDetailsActivity.rateNowContainer != null) {
                                    ProductDetailsActivity.setRating(ProductDetailsActivity.initialRating);
                                }

                            }
                        }

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                    }
                    ProductDetailsActivity.running_rating_query = false;
                }
            });
        }
    }

    public static void clearData(){
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();
        wishlist.clear();
        wishlistModelList.clear();

    }
}
