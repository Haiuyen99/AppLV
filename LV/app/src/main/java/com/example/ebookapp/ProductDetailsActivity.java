package com.example.ebookapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {
    private ViewPager productImageViewPager;
    private TabLayout viewpagerIndicator;
    public static FloatingActionButton addToWishlistBtn;
    private TextView productTitle, productRatingminiView, totalRating, getProductPrice;
    private TextView category;
    private TextView TotalRating, averagerating;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;

    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;
    public static int initialRating;
    private LinearLayout ratingsNumberContainer;
    private TextView totalRatingFigure;
    private LinearLayout ratingProgressBar;
    private Button buttonRead;

    private Dialog loadingDialog;
    private DocumentSnapshot documentSnapshot;
    private FirebaseFirestore firebaseFirestore;

    public static LinearLayout rateNowContainer;
    public static FirebaseUser currentUser;

    public static String productID;
    public static  boolean fromSearch = false;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        productTitle = findViewById(R.id.product_title);
        productRatingminiView = findViewById(R.id.tv_product_rating_miniview);
        totalRating = findViewById(R.id.product_status);
        getProductPrice = findViewById(R.id.author);
        ratingsNumberContainer = findViewById(R.id.ratings_number_container);
        totalRatingFigure = findViewById(R.id.total_rating_figure);
        ratingProgressBar = findViewById(R.id.ratings_progressbar_container);
        category = findViewById(R.id.category);
        TotalRating = findViewById(R.id.total_rating);
        averagerating = findViewById(R.id.average_rating);



        initialRating = 0;

        //loading Dialog
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.banner_backround));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        firebaseFirestore = FirebaseFirestore.getInstance();
        List<String> productImages = new ArrayList<>();

        productID = getIntent().getStringExtra("product_ID");

        firebaseFirestore.collection("PRODUCTS").document(getIntent().getStringExtra("product_ID"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();
                    for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                        productImages.add(documentSnapshot.get("story_image_" + x).toString());
                    }
                    ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                    productImageViewPager.setAdapter(productImagesAdapter);

                    productTitle.setText(documentSnapshot.get("story_title").toString());
                    productRatingminiView.setText(documentSnapshot.get("average_rating").toString());
                    totalRating.setText("(" + (long) documentSnapshot.get("total_ratings") + ") ratings");
                    getProductPrice.setText(documentSnapshot.get("story_author").toString());
                    category.setText(documentSnapshot.get("genre of stories").toString());

                    for (int x = 0; x < 5; x++) {
                        TextView rating = (TextView) ratingsNumberContainer.getChildAt(x);
                        rating.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));
                        ProgressBar progressBar = (ProgressBar) ratingProgressBar.getChildAt(x);
                        int maxprogress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                        progressBar.setMax(maxprogress);
                        progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));

                    }
                    totalRatingFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
                    TotalRating.setText((long) documentSnapshot.get("total_ratings") + " ratings");
                    averagerating.setText(documentSnapshot.get("average_rating").toString());

                    if (currentUser != null) {
                        if (DBqueries.myRating.size() == 0) {
                            DBqueries.loadRating(ProductDetailsActivity.this);
                        }
                        if (DBqueries.wishlist.size() == 0) {
                            DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);

                        } else {
                            loadingDialog.dismiss();
                        }

                    } else {
                        loadingDialog.dismiss();
                    }

                    if (DBqueries.myRatedIds.contains(productID)) {
                           int index = DBqueries.myRatedIds.indexOf(productID);
                           initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index)))-1;
                           setRating(initialRating);
                       }

                    if (DBqueries.wishlist.contains(productID)) {
                        ALREADY_ADDED_TO_WISHLIST = true;
                        addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                    } else {
                        ALREADY_ADDED_TO_WISHLIST = false;
                        addToWishlistBtn.setSupportBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                    }

                } else {

                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });


        addToWishlistBtn = findViewById(R.id.add_to_wishlist_btn);
        productImageViewPager = findViewById(R.id.product_images_viewpager);
        viewpagerIndicator = findViewById(R.id.viewpager_indicator);
        viewpagerIndicator.setupWithViewPager(productImageViewPager, true);

        addToWishlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //addToWishlistBtn.setEnabled(false);
                if (!running_wishlist_query) {
                    running_wishlist_query = true;
                    if (ALREADY_ADDED_TO_WISHLIST) {
                        ALREADY_ADDED_TO_WISHLIST = false;
                        int index = DBqueries.wishlist.indexOf(productID);
                        DBqueries.RemoveFromWishList(index, ProductDetailsActivity.this);
                        addToWishlistBtn.setSupportBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                    } else {

                        Map<String, Object> addproductID = new HashMap<>();
                        addproductID.put("product_ID_" + String.valueOf(DBqueries.wishlist.size()), productID);

                        firebaseFirestore.collection("USERS").document(currentUser.getUid())
                                .collection("USER_DATA")
                                .document("MY_WISHLIST")
                                .update(addproductID).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> updatelistsize = new HashMap<>();
                                    updatelistsize.put("list_size", (long) DBqueries.wishlist.size() + 1);

                                    firebaseFirestore.collection("USERS").document(currentUser.getUid())
                                            .collection("USER_DATA")
                                            .document("MY_WISHLIST")
                                            .update(updatelistsize).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                if (DBqueries.wishlistModelList.size() != 0) {

                                                    DBqueries.wishlistModelList.add(new WishlistModel(
                                                            productID,
                                                            documentSnapshot.get("story_image_1").toString()
                                                            , documentSnapshot.get("story_title").toString()
                                                            , documentSnapshot.get("average_rating").toString()
                                                            , (long) documentSnapshot.get("total_ratings")
                                                            , documentSnapshot.get("story_author").toString()));
                                                }
                                                ALREADY_ADDED_TO_WISHLIST = true;
                                                addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                                DBqueries.wishlist.add(productID);
                                                Toast.makeText(ProductDetailsActivity.this, "Đã thêm vào danh sách truyện yêu thích", Toast.LENGTH_SHORT).show();

                                            } else {
                                                String error = task.getException().getMessage();
                                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                            }
                                            //addToWishlistBtn.setEnabled(true);
                                            running_wishlist_query = false;
                                        }
                                    });

                                } else {
                                    //   addToWishlistBtn.setEnabled(true);
                                    running_wishlist_query = false;
                                    String error = task.getException().getMessage();
                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    }
                }

            }
        });
        //Rating Layout


        buttonRead = findViewById(R.id.button_read);

      buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailsActivity.this, ChapterActivity.class);
                intent.putExtra("product_ID",productID);
                startActivity(intent);
            }
        });



        rateNowContainer = findViewById(R.id.rate_now_container);
        for( int x=0 ; x<rateNowContainer.getChildCount();x++ ){
            final int startPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(startPosition);
                    if (!running_rating_query) {
                        running_rating_query = true;
                        Map<String, Object> updateRating = new HashMap<>();
                        if(DBqueries.myRatedIds.contains(productID)){
                            TextView oldRating = (TextView) ratingsNumberContainer.getChildAt(5 - initialRating - 1);
                            TextView finalRating = (TextView) ratingsNumberContainer.getChildAt(5 - startPosition - 1);

                            updateRating.put(initialRating +1 +"_star" ,Long.parseLong(oldRating.getText().toString())-1);
                            updateRating.put(startPosition+1+"_star",Long.parseLong(finalRating.getText().toString())+1);
                            updateRating.put("average_rating", calculateAverageRating((long) startPosition- initialRating, true));

                        }else {

                            updateRating.put(startPosition + 1 + "_star", (long) documentSnapshot.get(startPosition + 1 + "_star") + 1);
                            updateRating.put("average_rating",calculateAverageRating((long) startPosition+1 , false));
                            updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);


                            firebaseFirestore.collection("PRODUCTS").document(productID)
                                    .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> myRating = new HashMap<>();
                                        if(DBqueries.myRatedIds.contains(productID)){
                                            myRating.put("rating_"+DBqueries.myRatedIds.indexOf(productID), (long) startPosition +1);

                                        }else {
                                            myRating.put("list_size", (long) DBqueries.myRatedIds.size()+1);
                                            myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                            myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) startPosition + 1);
                                        }

                                        firebaseFirestore.collection("USERS")
                                                .document(currentUser.getUid())
                                                .collection("USER_DATA")
                                                .document("MY_RATINGS")
                                                .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    if(DBqueries.myRatedIds.contains(productID)) {

                                                        DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID),(long) startPosition+1);

                                                        TextView oldRating = (TextView) ratingsNumberContainer.getChildAt(5 - startPosition - 1);
                                                        TextView finalRating = (TextView) ratingsNumberContainer.getChildAt(5 - startPosition - 1);
                                                        oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) + 1));
                                                        finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));

                                                    }else {

                                                        DBqueries.myRatedIds.add(productID);
                                                        DBqueries.myRating.add((long) startPosition + 1);


                                                        TextView rating = (TextView) ratingsNumberContainer.getChildAt(5 - startPosition - 1);
                                                        rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                        totalRating.setText("(" + (long) documentSnapshot.get("total_ratings") + 1 + ") ratings");
                                                        TotalRating.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                        totalRatingFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));



                                                        Toast.makeText(ProductDetailsActivity.this, "Thank you for rating ", Toast.LENGTH_SHORT).show();
                                                    }

                                                    for (int x = 0; x < 5; x++) {
                                                        TextView ratingfigures = (TextView) ratingsNumberContainer.getChildAt(x);
                                                        ProgressBar progressBar = (ProgressBar) ratingProgressBar.getChildAt(x);

                                                            int maxprogress = Integer.parseInt(totalRatingFigure.getText().toString());
                                                            progressBar.setMax(maxprogress);
                                                            progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));

                                                    }

                                                    productRatingminiView.setText(calculateAverageRating(0,true));
                                                    averagerating.setText(calculateAverageRating(0,true));

                                                       if(!DBqueries.wishlist.contains(productID)&& DBqueries.wishlistModelList.size()!=0){
                                                           int index =  DBqueries.wishlist.indexOf(productID);
                                                           DBqueries.wishlistModelList.get(index).setRatings(averagerating.getText().toString());
                                                           DBqueries.wishlistModelList.get(index).setTotalRatings(Long.parseLong(totalRatingFigure.getText().toString()));
                                                       }


                                                } else {
                                                    setRating(initialRating);
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                running_rating_query =  false;
                                            }
                                        });

                                    } else {
                                        running_rating_query = false;
                                        setRating(initialRating);
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        }
                    }

                }
            });

        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
           if (DBqueries.myRating.size() == 0) {
                DBqueries.loadRating(ProductDetailsActivity.this);
            }
            if (DBqueries.wishlist.size() == 0) {
                DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);


            } else {
                loadingDialog.dismiss();
            }

        } else {
            loadingDialog.dismiss();
        }
      if(DBqueries.myRatedIds.contains(productID)){
            int index = DBqueries.myRatedIds.indexOf(productID);
         initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) -1 ;
         setRating(initialRating);
       }

        if (DBqueries.wishlist.contains(productID)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishlistBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
        } else {

            addToWishlistBtn.setSupportBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }

    }


    public static void setRating(int starPosition) {

            for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
                ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
                }
                if (x <= starPosition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#BB0000")));
                    }
                }

    }
    }

    private String calculateAverageRating( long currentUserRating , boolean update ){
        Double totalStars = Double.valueOf(0);
        for( int x=1; x<6 ;x++){
           TextView ratingNo = (TextView) ratingsNumberContainer.getChildAt(5-x);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString())*x);

        }
        totalStars = totalStars + currentUserRating;
        if(update){
            return String.valueOf(totalStars / Long.parseLong(totalRatingFigure.getText().toString())).substring(0,3);
        }else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingFigure.getText().toString())+1)).substring(0,3);
        }



    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_carticon, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSearch = false;
    }
}