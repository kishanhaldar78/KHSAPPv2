package com.example.khsapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    public SignUpFragment() {
        // Required empty public constructor
    }

    private TextView alreadyHaveAccount;
    private FrameLayout parentFrameLayout;

    //Variable declaration
    private EditText email;
    private EditText fullname;
    private EditText password;
    private EditText confirmPassword;

    private ImageButton closeBtn;
    private Button signUpBtn;

    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String emailPattern= "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);
        alreadyHaveAccount=view.findViewById(R.id.tv_already_have_an_account);
        parentFrameLayout=getActivity().findViewById(R.id.register_flamelayout);

        //assign
        email=view.findViewById(R.id.sign_up_email);
        fullname=view.findViewById(R.id.sign_up_fullname);
        password=view.findViewById(R.id.sign_up_password);
        confirmPassword=view.findViewById(R.id.sign_up_confirm_password);

        closeBtn=view.findViewById(R.id.sign_up_cross_btn);
        signUpBtn=view.findViewById(R.id.sign_up_btn);

        progressBar=view.findViewById(R.id.progressBar);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore=FirebaseFirestore.getInstance();

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
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
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //after empty field check
                checkEmailAndPassword();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainIntent();
            }


        });

    }

    //Transition animation on fragment
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slideout_from_right);
        fragmentTransaction.replace(parentFrameLayout.getId(),fragment);
        fragmentTransaction.commit();

    }

    private void checkInputs() {
        if(!TextUtils.isEmpty(email.getText())){
            if(!TextUtils.isEmpty(fullname.getText())){
                if(!TextUtils.isEmpty(password.getText()) && password.length()>=8) {
                    if(!TextUtils.isEmpty(confirmPassword.getText())){
                        signUpBtn.setEnabled(true);
                        signUpBtn.setTextColor(Color.rgb(255,255,255));
                    }else{
                        signUpBtn.setEnabled(false);
                        signUpBtn.setTextColor(Color.argb(50,255,255,255));
                    }
                }else{
                    signUpBtn.setEnabled(false);
                    signUpBtn.setTextColor(Color.argb(50,255,255,255));
                }
            }else{
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.argb(50,255,255,255));
            }
        }else{
            signUpBtn.setEnabled(false);
            signUpBtn.setTextColor(Color.argb(50,255,255,255));
        }
    }

    private void checkEmailAndPassword() {

        //Here set the Error Icon
        Drawable customErrorIcon=getResources().getDrawable(R.drawable.ic_report_problem_black_24dp);
        // Drawable customErrorIcon=getResources().getDrawable(R.midmap.customImg);
        customErrorIcon.setBounds(0,0,customErrorIcon.getIntrinsicWidth(),customErrorIcon.getIntrinsicHeight());

        if(email.getText().toString().matches(emailPattern)){
            if(password.getText().toString().equals(confirmPassword.getText().toString())){

                //while connect to net then we enable our progress Bar
                progressBar.setVisibility(View.VISIBLE);

                //User not press again and again signupBtn so we Invisable the button
                signUpBtn.setEnabled(false);
                signUpBtn.setTextColor(Color.argb(50,250,250,250));

                //Authenication with emailId and password and create the account firebase
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            //before going to main activity ,store the full name in database
                            Map<Object,String> userdata=new HashMap<>();
                            userdata.put("fullname",fullname.getText().toString());
                            //upload to firestore database
                            firebaseFirestore.collection("USERS")
                                    .add(userdata).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                    if(task.isSuccessful()){
                                        //After successfully signup shift to main Activity
                                          mainIntent();

                                    }else{
                                        progressBar.setVisibility(View.INVISIBLE);
                                        signUpBtn.setEnabled(true);
                                        signUpBtn.setTextColor(Color.rgb(250,250,250));
                                        String error=task.getException().getMessage();
                                        Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            //If getting error then disable progressBar
                            progressBar.setVisibility(View.INVISIBLE);

                            //Incase any Error occur the enable the signUp button
                            signUpBtn.setEnabled(true);
                            signUpBtn.setTextColor(Color.rgb(250,250,250));

                            String error=task.getException().getMessage();
                            Toast.makeText(getActivity(),error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }else{
                confirmPassword.setError("Password doesn't matched!",customErrorIcon);//set error Icon
                //confirmPassword.setError("Password doesn't matched!"); default error icon
            }
        }else{
            email.setError("Invalid Email!",customErrorIcon);
        }
    }


    private void mainIntent() {
        Intent mainIntent=new Intent(getActivity(),MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish(); //to stop the current activity
    }

}
