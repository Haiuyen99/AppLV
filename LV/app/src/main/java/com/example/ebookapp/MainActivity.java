package com.example.ebookapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ebookapp.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import de.hdodenhof.circleimageview.CircleImageView;
import io.grpc.okhttp.internal.framed.Header;


public class MainActivity extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    private FrameLayout frameLayout;
    private ImageView actionbar;

    private CircleImageView circleImageView;
    private TextView fullname, email;
    private ImageView addProfileIcon;
  private ConstraintLayout constraintLayout;
    private NavigationView navigationView;

    private  DrawerLayout drawer;

    private  static int currentFragment;



    public static FirebaseUser currentUser;

    private  static final int HOME_FRAGMENT=0;
    private  static final int WISHLIST_FRAGMENT=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionbar = findViewById(R.id.image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        frameLayout = findViewById(R.id.main_framelayout);
        setFragment(new HomeFragment(),HOME_FRAGMENT);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        final Dialog signinDialog = new Dialog(MainActivity.this);
        signinDialog.setContentView(R.layout.fragment_sign_in);
        signinDialog.setCancelable(true);
        signinDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);


        circleImageView = findViewById(R.id.main_profile_image);
        fullname = findViewById(R.id.main_fullname);
        email =findViewById(R.id.main_email);
        addProfileIcon = findViewById(R.id.main_add_icon);



    }


    @Override
    protected void onStart() {
        super.onStart();
            navigationView.getMenu().getItem(navigationView.getMenu().size()-1).setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (currentFragment == HOME_FRAGMENT) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_search_icon) {
           Intent searchIntent = new Intent(this,SearchActivity.class);
           startActivity(searchIntent);
           return true;
        } else if (id == R.id.main_notification_icon) {
            return true;
        } else if (id == R.id.main_cart_icon) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        actionbar.setVisibility(View.VISIBLE);
        NavigationView navigationView = findViewById(R.id.nav_view);
        invalidateOptionsMenu();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
      setFragment(new HomeFragment(),HOME_FRAGMENT);
      navigationView.getMenu().getItem(0).setChecked(true);

    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_mymall) {
        setFragment(new HomeFragment(), HOME_FRAGMENT);
        }else if (id == R.id.nav_myorder) {

        } else if (id == R.id.nav_rewards) {

        } else if (id == R.id.nav_mycart) {

       } else if (id == R.id.nav_wishlist) {
     gotoFragment("My wishlist",new MyWishlistFragment(),WISHLIST_FRAGMENT);

       } else if (id == R.id.nav_account) {

      }else  if (id == R.id.nav_singout){
            FirebaseAuth.getInstance().signOut();
            DBqueries.clearData();
            Intent registerIntent = new Intent(MainActivity.this,RegissterActivity.class);
            startActivity(registerIntent);
            fileList();
        }



        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private  void setFragment( Fragment fragment , int fragmentNo){
        currentFragment=fragmentNo;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    private void gotoFragment( String title , Fragment fragment, int fragmentNo ){
        actionbar.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        invalidateOptionsMenu();
       setFragment(fragment,fragmentNo);

    }
}