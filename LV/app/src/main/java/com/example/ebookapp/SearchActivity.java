package com.example.ebookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
//import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private SearchView searchView;
    private TextView textView;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_view);
        textView = findViewById(R.id.textView);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        final List<WishlistModel> list = new ArrayList<>();
        final List<String> ids = new ArrayList<>();
        Adapter adapter = new Adapter(list,false);
        recyclerView.setAdapter(adapter);

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String s) {
               list.clear();
               ids.clear();
               //tách chuỗi từ khoảng trắng
               String[] tags = s.split("");
               for (String tag:tags){
                   tag.trim();
                   FirebaseFirestore.getInstance().collection("PRODUCTS")
                           .whereArrayContains("tags",tag)
                           .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                       @Override
                       public void onComplete(@NonNull Task<QuerySnapshot> task) {
                           if (task.isSuccessful()) {
                               for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                   WishlistModel model = new WishlistModel(
                                            documentSnapshot.getId()
                                           , documentSnapshot.get("story_image_1").toString()
                                           , documentSnapshot.get("story_title").toString()
                                           , documentSnapshot.get("average_rating").toString()
                                           , (long) documentSnapshot.get("total_ratings")
                                           , documentSnapshot.get("story_author").toString());

                                   // tìm kiếm chuỗi ký tự trong chuỗi
                                   //Kiểm tra xem trong productID có thông tin của từ đầu tiên chưa
                                   //Nếu chưa sẽ add vào list
                                   //productID đó sẽ đc add vào ids
                                   if( !ids.contains(model.getProductID())){
                                       list.add(model);
                                       ids.add(model.getProductID());
                                   }


                               }

                               if(tag.equals(tags[tags.length-1])){
                                   if(list.size() == 0){
                                       textView.setVisibility(View.VISIBLE);
                                       recyclerView.setVisibility(View.GONE);

                                   }else {
                                       textView.setVisibility(View.GONE);
                                       recyclerView.setVisibility(View.VISIBLE);
                                   }
                                   adapter.getFilter().filter(s);
                               }


                           }else {
                               String error = task.getException().getMessage();
                               Toast.makeText(SearchActivity.this,error, Toast.LENGTH_SHORT).show();
                           }

                       }
                   });
               }
               return false;
           }

           @Override
           public boolean onQueryTextChange(String newText) {
               return false;
           }
       });
    }


    class Adapter  extends WishlistAdapter implements Filterable{

        public Adapter(List<WishlistModel> wishlistModelList, Boolean wishlist) {
            super(wishlistModelList, wishlist);
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    // Filter logic
                    return null;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    notifyDataSetChanged();
                }
            };
        }
    }
}