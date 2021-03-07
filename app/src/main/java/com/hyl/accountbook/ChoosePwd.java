package com.hyl.accountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChoosePwd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_pwd);
        Button mButton_pwd_text = findViewById(R.id.button_pwd_text);
        Button mButton_pwd_gesture = findViewById(R.id.button_pwd_gesture);
        Button mButton_pwd_finger = findViewById(R.id.button_pwd_finger);
        mButton_pwd_text.setOnClickListener(new View.OnClickListener() {//跳到文本密码设置界面
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ChoosePwd.this,ChangePsw.class);
                //intent.putExtra("info", "No66778899");
                ChoosePwd.this.startActivity(intent);
            }
        });

        mButton_pwd_gesture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ChoosePwd.this, RstGesturePwd.class);
                //intent.putExtra("info", "No66778899");
                ChoosePwd.this.startActivity(intent);
            }
        });

        mButton_pwd_finger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(ChoosePwd.this, FingerSet.class);
                //intent.putExtra("info", "No66778899");
                ChoosePwd.this.startActivity(intent);
            }
        });
    }
}