package com.example.hospital_management_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome_page_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        Button buttonsignup =findViewById(R.id.signupbtn);
        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Welcome_page_activity.this,SignUpActivity.class);
                startActivity(intent);

            }
        });

        Button buttonlogin =findViewById(R.id.loginbtn);
        buttonlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome_page_activity.this, LoginPage.class);
                startActivity(intent);

            }

       });
    }
}