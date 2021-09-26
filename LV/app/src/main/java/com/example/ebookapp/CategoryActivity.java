package com.example.ebookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.ebookapp.ui.home.HomePageAdapter;
import com.example.ebookapp.ui.home.HomePageModel;

import java.util.ArrayList;

import static com.example.ebookapp.DBqueries.categoryModelList;
import static com.example.ebookapp.DBqueries.lists;
import static com.example.ebookapp.DBqueries.loadFragmentData;
import static com.example.ebookapp.DBqueries.loadedCategoriesNames;
public class CategoryActivity extends AppCompatActivity {


    private RecyclerView categorieyRecyclerView;
    private  HomePageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        String title = getIntent().getStringExtra("CategoryName");

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      categorieyRecyclerView = findViewById(R.id.category_recyclerView);
      LinearLayoutManager testingLinearLayout = new LinearLayoutManager(this);
      testingLinearLayout.setOrientation(LinearLayoutManager.VERTICAL);
      categorieyRecyclerView.setLayoutManager(testingLinearLayout);


      int listPosition = 0;
      for ( int x =0 ; x< loadedCategoriesNames.size(); x++){
          if(loadedCategoriesNames.get(x).equals(title.toUpperCase())){
             listPosition = x ;

          }
      }

      if(listPosition == 0){
         loadedCategoriesNames.add(title.toUpperCase());
          lists.add(new ArrayList<HomePageModel>());
          adapter = new HomePageAdapter(lists.get(loadedCategoriesNames.size()-1));
          loadFragmentData(adapter,this,loadedCategoriesNames.size()-1,title);
      }else {
          adapter = new HomePageAdapter(lists.get(listPosition));

        }


        categorieyRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_search_icon) {
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }else if(id == android.R.id.home ){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}