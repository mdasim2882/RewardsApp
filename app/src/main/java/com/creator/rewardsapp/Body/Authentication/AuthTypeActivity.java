package com.creator.rewardsapp.Body.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.creator.rewardsapp.R;

public class AuthTypeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_type);
    }

    public void signUpButton(View view) {
        Intent i=new Intent(this,SignUpActivity.class);
        startActivity(i);
    }

    public void loginbtn(View view) {
        Intent i=new Intent(this, LoginActivity.class);
        startActivity(i);
    }
}