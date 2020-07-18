package com.example.khsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //it is just bcz we don't user again to login only one time purpose so after cross btn the directly open in main activity
        firebaseAuth=FirebaseAuth.getInstance();

        SystemClock.sleep(5000);

    }


    //it call when activity start
    @Override
    protected void onStart() {
        super.onStart();
        //check user exits or not, accordingly we shift to user main Activity
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if(currentUser==null){
            //if new user then register page
            Intent loginIntent=new Intent(SplashActivity.this,RegisterActivity.class);
            startActivity(loginIntent);
            finish();
        }else{
            Intent mainIntent=new Intent(SplashActivity.this,MainActivity.class);
            startActivity(mainIntent);
            finish();

        }
    }
}
