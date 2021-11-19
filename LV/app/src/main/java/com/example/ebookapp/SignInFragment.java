package com.example.ebookapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.ebookapp.RegissterActivity.onResetPasswordFragment;

public class SignInFragment extends Fragment {

    

    public SignInFragment() {

    }

  private  TextView dontHaveAccount;
  private FrameLayout parentFrameLayout;
  private EditText email, password;
  private TextView forgotpassword;
  private ImageButton closeBtn;
  private Button  singInBtn;
  private FirebaseAuth firebaseAuth;
    private  String emailPattern ="[a-zA-Z0-9._-]+@[a-z]+.[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_sign_in, container, false);
        dontHaveAccount = view.findViewById(R.id.tv_dont_have_account);
        parentFrameLayout = view.findViewById(R.id.registe_framelayout);
        email= view.findViewById(R.id.edt_singinemail);
        password = view.findViewById(R.id.edt_signinpassword);
        closeBtn= view.findViewById(R.id.close);
        singInBtn= view.findViewById(R.id.btn_singin);
        forgotpassword= view.findViewById(R.id.tv_forgotpassword);

        firebaseAuth= FirebaseAuth.getInstance();
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SingUpFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               checkInput ();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInput();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        singInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkEmailandPassword();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onResetPasswordFragment = true ;
                setFragment(new ResetPasswordFragment());
            }
        });
    }

    private  void setFragment( Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slideout_from_left);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();
    }
    private  void checkInput(){
        if(!TextUtils.isEmpty(email.getText())){
            if (!TextUtils.isEmpty(password.getText())){
             singInBtn.setEnabled(true);
             singInBtn.setTextColor(Color.RED);
            }else {
                singInBtn.setEnabled(false);
                singInBtn.setTextColor(Color.RED);
            }
        }else {
            singInBtn.setEnabled(false);
            singInBtn.setTextColor(Color.RED);
        }
    }

    private  void checkEmailandPassword(){
        if(email.getText().toString().matches(emailPattern)){
            if (password.length() >=8){

                singInBtn.setEnabled(false);
                singInBtn.setTextColor(Color.RED);

                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                   mainIntent();
                                }else {
                                    singInBtn.setEnabled(true);
                                    singInBtn.setTextColor(Color.RED);

                                    String error = task.getException().getMessage();
                                    Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }else {
                Toast.makeText(getActivity(),"Email hoặc mật khẩu bạn nhập không đúng!",Toast.LENGTH_SHORT).show();

            }
        }else {
            Toast.makeText(getActivity(),"Email hoặc mật khẩu bạn nhập không đúng. !",Toast.LENGTH_SHORT).show();
        }
    }

    private  void mainIntent(){
        Intent mainIntent = new Intent(getActivity(),MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }
}