package com.example.ebookapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ChapterActivity  extends AppCompatActivity {

    RecyclerView chapterRecyclerView;
    LinearLayoutManager layoutManager;
    List<ChapterModel> chapterModelList;
    ChapterAdapter chapterAdapter;
    FirebaseFirestore firebaseFirestore =  FirebaseFirestore.getInstance();
    public static  List<String>  chapter = new ArrayList<>();
    private DocumentSnapshot documentSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chapterRecyclerView=findViewById(R.id.chapter_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        chapterModelList = new ArrayList<>();


        firebaseFirestore.collection("PRODUCTS")
                .document(getIntent().getStringExtra("product_ID"))
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                        chapter.add(task.getResult().get("chapter_ID").toString());
                        String chapterID = task.getResult().get("chapter_ID").toString();

                    firebaseFirestore.collection("CHAPTER")
                            .document(chapterID)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){

                                documentSnapshot = task.getResult();
                                long  no_of_chapter =(long) documentSnapshot.get("no_of_chapter");
                                for( int x =1; x< no_of_chapter; x++) {
                                    chapterModelList.add(new ChapterModel(
                                            documentSnapshot.get("chapter_"+x).toString(),
                                            documentSnapshot.get("title_chapter_"+x).toString(),
                                            documentSnapshot.get("chapter_content_"+x).toString()
                                    ));
                                }

                                layoutManager.setOrientation(RecyclerView.VERTICAL);
                                chapterRecyclerView.setLayoutManager(layoutManager);
                                chapterAdapter=new ChapterAdapter(chapterModelList);
                                chapterRecyclerView.setAdapter(chapterAdapter);
                                chapterAdapter.notifyDataSetChanged();

                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(ChapterActivity.this,error,Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }

            }
        });

    }

    private void initRecyclerView() {
        chapterRecyclerView=findViewById(R.id.chapter_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        chapterRecyclerView.setLayoutManager(layoutManager);
        chapterAdapter=new ChapterAdapter(chapterModelList);
        chapterRecyclerView.setAdapter(chapterAdapter);
        chapterAdapter.notifyDataSetChanged();




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
        } else if (id == R.id.main_search_icon) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}