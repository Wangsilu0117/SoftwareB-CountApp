package com.hyl.accountbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hyl.dao.DBOpenHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OpenView extends AppCompatActivity {
    private long mExitTime;
    private SharedPreferences Daytime;
    private String setTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_view);
        Button mButton_text_login = findViewById(R.id.text_login);
        Button mButton_gesture_login = findViewById(R.id.gesture_login);
        Button mButton_finger_login = findViewById(R.id.finger_login);
        TextView time = (TextView) findViewById(R.id.daytime);

        mButton_text_login.setOnClickListener(new View.OnClickListener() {//跳转到文本密码登录界面
            @Override
            public void onClick(View v){
                DBOpenHelper helper = new DBOpenHelper(OpenView.this,"qianbao.db",null,1);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor c = db.rawQuery("select * from user_tb",null);
                if(c.getCount()==0)
                {
                    c.close();
                    db.close();
                    Intent intent=new Intent(OpenView.this, RegistActivity.class);
                    //intent.putExtra("info", "No66778899");
                    OpenView.this.startActivity(intent);
                }else{
                    c.close();
                    db.close();
                    Intent intent=new Intent(OpenView.this, Log_in.class);
                    //intent.putExtra("info", "No66778899");
                    OpenView.this.startActivity(intent);
                }
                finish();
            }
        });
        mButton_gesture_login.setOnClickListener(new View.OnClickListener() {//跳转到手势密码登录界面
            @Override
            public void onClick(View v) {
                DBOpenHelper helper = new DBOpenHelper(OpenView.this, "qianbao.db", null, 1);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor c = db.rawQuery("select * from user_tb", null);
                if (c.getCount() == 0) {
                    c.close();
                    db.close();
                    Toast.makeText(getApplicationContext(), "请先设置文本密码", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OpenView.this, RegistActivity.class);
                    OpenView.this.startActivity(intent);
                } else {
                    c.close();
                    db.close();
                    Intent intent = new Intent(OpenView.this, Act.class);
                    //intent.putExtra("info", "No66778899");
                    OpenView.this.startActivity(intent);
                }
                finish();
            }
        });

        mButton_finger_login.setOnClickListener(new View.OnClickListener() {//跳转到指纹密码登录界面
            @Override
            public void onClick(View v){
                Intent intent=new Intent(OpenView.this, Finger.class);
                OpenView.this.startActivity(intent);
                finish();
            }
        });

        //Calendar calendar = Calendar.getInstance();
        //设置开屏“这是你记账的第几天”数字计算
        Daytime = getSharedPreferences("time", Context.MODE_PRIVATE);

        setTime = Daytime.getString("firstTime", "");
        if (setTime.isEmpty()) {

            Date first_now = new Date();
            //DateFormat df_first = new SimpleDateFormat("yyyy-MM-dd HH:mm");用于分钟测试
            DateFormat df_first = new SimpleDateFormat("yyyy-MM-dd");
            Daytime.edit().putString("firstTime", df_first.format(first_now)).apply();
            time.setText("欢迎使用账本管家，这是你记账的第1天");
        }else{

            //DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");用于分钟测试
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            try
            {
                Date d1 = df.parse(setTime);//12:00:00
                Date d2 = df.parse(df.format(now));//11:20:00
                long diff = d2.getTime() - d1.getTime();//这样得到的差值是微秒级别

                long days = (diff / (1000 * 60 * 60 * 24))+1;
                //long hours = (diff-(days-1)*(1000 * 60 * 60 * 24))/(1000* 60 * 60);
                //long minutes = (diff-(days-1)*(1000 * 60 * 60 * 24)-hours*(1000* 60 * 60))/(1000* 60);
                time.setText("欢迎使用账本管家，这是你记账的第"+days+"天");

            }
            catch (Exception e)
            {
            }
        }

    }



    /**
     * 点击两次返回退出app
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出记账管家", Toast.LENGTH_SHORT).show();
                //System.currentTimeMillis()系统当前时间
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}

