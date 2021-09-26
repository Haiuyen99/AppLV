package com.example.ebookapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SingUpFragment extends Fragment {

    private TextView alreadyhaveaccount;
    private FrameLayout frameLayout;
    private EditText email,fullname,password,confirmpassword;
    private ImageButton btnclose;
    private Button SignUpbtn;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore ;

    private  String emailPattern ="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    public SingUpFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sing_up, container, false);

        alreadyhaveaccount = view.findViewById(R.id.tv_allready_have_account);

        frameLayout= view.findViewById(R.id.registe_framelayout);

        email = view.findViewById(R.id.edt_email);
        password= view.findViewById(R.id.edt_password);
        fullname= view.findViewById(R.id.edt_fullname);
        confirmpassword= view.findViewById(R.id.edt_confirmpassword);

        btnclose= view.findViewById(R.id.btn_close);
        SignUpbtn = view.findViewById(R.id.btn_singup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInput();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInput();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInput();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                checkInput();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        SignUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            checkEmailAndPassword();
            }
        });

        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mainIntent();
            }
        });
    }

    private  void setFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(frameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("ResourceAsColor")
    private  void checkInput(){
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(fullname.getText())){

                if(!TextUtils.isEmpty(password.getText())&&password.length()>=8){

                    if(!TextUtils.isEmpty(confirmpassword.getText())){
                      SignUpbtn.setEnabled(true);
                      SignUpbtn.setTextColor(R.color.white);
                    }else {

                        SignUpbtn.setEnabled(true);
                        SignUpbtn.setTextColor(R.color.white);
                    }

                }else {

                    SignUpbtn.setEnabled(true);
                    SignUpbtn.setTextColor(R.color.white);
                }

            }else {

                SignUpbtn.setEnabled(true);
                SignUpbtn.setTextColor(R.color.white);
            }

        }else {

            SignUpbtn.setEnabled(true);
            SignUpbtn.setTextColor(R.color.white);

        }
    }

    @SuppressLint("ResourceAsColor")
    private  void checkEmailAndPassword(){
        Drawable customErrorIcon = getResources().getDrawable(R.mipmap.error);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());

        if (email.getText().toString().matches(emailPattern)){

            if(password.getText().toString().equals(confirmpassword.getText().toString())){

                SignUpbtn.setEnabled(false);
                SignUpbtn.setTextColor(R.color.white);

                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                 if(task.isSuccessful()){

                                     Map<String,Object> userdata = new HashMap<>();
                                     userdata.put("fullname",fullname.getText().toString() );
                                     userdata.put("email",email.getText().toString());
                                     userdata.put("profile","");
                                     firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                             .set(userdata)
                                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                 @Override
                                                 public void onComplete(@NonNull Task<Void> task) {
                                                     if (task.isSuccessful()){

                                                         CollectionReference userDataReference =  firebaseFirestore.collection("USERS").document(firebaseAuth.getUid())
                                                                 .collection("USER_DATA");

                                                         Map<String,Object> wishlistMap = new HashMap<>();
                                                         wishlistMap.put("list_size",(long) 0 );

                                                         Map<String,Object> ratingsMap = new HashMap<>();
                                                         ratingsMap.put("list_size",(long) 0 );


                                                        final List <String> documentNames = new ArrayList<>();
                                                         documentNames.add("MY_WISHLIST");
                                                         documentNames.add("MY_RATINGS");


                                                         List<Map<String,Object>> documentFields  = new ArrayList<>();
                                                         documentFields.add(wishlistMap);
                                                         documentFields.add(ratingsMap);
                                                         for (int x=0; x<documentNames.size(); x++ ){
                                                             int finalX = x;
                                                             userDataReference.document(documentNames.get(x))
                                                                     .set(documentFields.get(x))
                                                                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                         @Override
                                                                         public void onComplete(@NonNull Task<Void> task) {
                                                                             if (task.isSuccessful()){
                                                                                 if(finalX == documentNames.size() -1) {
                                                                                     mainIntent();
                                                                                 }
                                                                             }else {
                                                                                 SignUpbtn.setEnabled(true);
                                                                                 SignUpbtn.setTextColor(R.color.white);
                                                                                 String error = task.getException().getMessage();
                                                                                 Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                                             }
                                                                         }
                                                                     });
                                                         }
                                                         ;
                                                     }else {

                                                         String error = task.getException().getMessage();
                                                         Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                                     }
                                                 }
                                             });



                                 }else {
                                     String error = task.getException().getMessage();

                                     SignUpbtn.setEnabled(true);
                                     SignUpbtn.setTextColor(R.color.white);
                                     Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                 }
                            }
                        });

            }else {
                confirmpassword.setError("Password doesn't matched",customErrorIcon);
            }
        } else {
              email.setError("Invalue email ", customErrorIcon);
        }
    }
 private  void mainIntent(){
     Intent mainIntent = new Intent(getActivity(),MainActivity.class);
     startActivity(mainIntent);
     getActivity().finish();

 }
}