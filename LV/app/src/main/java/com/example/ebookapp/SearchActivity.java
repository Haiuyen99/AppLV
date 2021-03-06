package com.example.ebookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
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
        adapter.setFromSearch(true);
        recyclerView.setAdapter(adapter);

       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String s) {
               list.clear();
               ids.clear();
               //t??ch chu???i t??? kho???ng tr???ng
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

                                   model.setTags((ArrayList<String>) documentSnapshot.get("tags"));

                                   // t??m ki???m chu???i k?? t??? trong chu???i
                                   //Ki???m tra xem trong productID c?? th??ng tin c???a t??? ?????u ti??n ch??a
                                   //N???u ch??a s??? add v??o list
                                   //productID ???? s??? ??c add v??o ids
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

      private List<WishlistModel> originalList;
        public Adapter(List<WishlistModel> wishlistModelList, Boolean wishlist) {
            super(wishlistModelList, wishlist);
             originalList = wishlistModelList;


        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                     List<WishlistModel> filteredList = new ArrayList<>();
                    final  String[] tags = constraint.toString().split("");

                    for(WishlistModel model:originalList){

                       ArrayList<String> presentTags  = new ArrayList<>();
                       for (String tag:tags){
                           if(model.getTags().contains(tag)){
                              presentTags.add(tag);

                           }
                       }
                       model.setTags(presentTags);
                    }
                    for (int i= tags.length -1 ; i>0; i--){
                        for(WishlistModel model : originalList){
                            if(model .getTags().size()== i ){
                                filteredList.add(model);

                            }
                        }
                    }

                    results.values = filteredList;
                    results.count = filteredList.size();


                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    if(results.count >0){
                        setWishlistModelList((List<WishlistModel>) results.values);
                    }

                    notifyDataSetChanged();
                }
            };
        }
    }
}