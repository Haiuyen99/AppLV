package com.example.ebookapp.ui.home;


import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telecom.Connection;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.net.ConnectivityManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.ebookapp.CategoryAdapter;
import com.example.ebookapp.CategoryModel;
import com.example.ebookapp.HorizontalProductModel;
import com.example.ebookapp.R;
import com.example.ebookapp.SliderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import static com.example.ebookapp.DBqueries.categoryModelList;
import static com.example.ebookapp.DBqueries.lists;

import static com.example.ebookapp.DBqueries.loadCategories;
import static com.example.ebookapp.DBqueries.loadFragmentData;
import static com.example.ebookapp.DBqueries.loadedCategoriesNames;


public class HomeFragment extends Fragment {

   private ConnectivityManager connectivityManager;
   private NetworkInfo networkInfo;

    private RecyclerView categoryReclerView;
    private CategoryAdapter categoryAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView homepageRecyclerView;

    private HomePageAdapter adapter;



    private FirebaseFirestore firebaseFirestore;
    private List<HomePageModel> homepageModelist;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        categoryReclerView = view.findViewById(R.id.category_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryReclerView.setLayoutManager(linearLayoutManager);


        categoryAdapter = new CategoryAdapter(categoryModelList);
        categoryReclerView.setAdapter(categoryAdapter);

        if(categoryModelList.size() == 0){

         loadCategories(categoryAdapter,getContext());
        }else {
            categoryAdapter.notifyDataSetChanged();
        }





        homepageRecyclerView  = view.findViewById(R.id.home_page_recyclerView);
        LinearLayoutManager testingManager =  new LinearLayoutManager(getContext());
        testingManager.setOrientation(LinearLayoutManager.VERTICAL);
        homepageRecyclerView.setLayoutManager(testingManager);





        if(lists.size() == 0){
            loadedCategoriesNames.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            adapter = new HomePageAdapter(lists.get(0));
            loadFragmentData(adapter,getContext(),0,"Home");
        }else {
            adapter = new HomePageAdapter(lists.get(0));
            adapter.notifyDataSetChanged();
        }

        homepageRecyclerView.setAdapter(adapter);
        return view;
    }

}