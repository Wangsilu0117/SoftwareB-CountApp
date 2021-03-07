package com.hyl.accountbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class pwd_text_set extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_text_set);
        Button mButton_pwd_text_set = findViewById(R.id.button_pwd_text_set);
        Button mButton_pwd_text_change = findViewById(R.id.button_pwd_text_change);

        mButton_pwd_text_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(pwd_text_set.this, RegistActivity.class);
                //intent.putExtra("info", "No66778899");
                pwd_text_set.this.startActivity(intent);
            }
        });

        mButton_pwd_text_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(pwd_text_set.this, ResPwdActivity.class);
                //intent.putExtra("info", "No66778899");
                pwd_text_set.this.startActivity(intent);
            }
        });
    }
}