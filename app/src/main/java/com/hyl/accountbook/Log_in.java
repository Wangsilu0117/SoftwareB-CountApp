package com.hyl.accountbook;

import com.hyl.util.pubFun;
import com.hyl.dao.DBOpenHelper;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Log_in extends AppCompatActivity {
    //private EditText mEditText_username;//用户名
    private EditText mEditText_password;//密码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        //绑定
        //mEditText_username = findViewById(R.id.Edit_username);
        mEditText_password = findViewById(R.id.Edit_password);
        Button mButton_login = findViewById(R.id.btn_log_in);
//        Button mButton_enroll = findViewById(R.id.btn_enroll);
        String userID = "11111111111";


        //事件
        mButton_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(pubFun.isEmpty(mEditText_password.getText().toString())){
                    Toast.makeText(Log_in.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //call DBOpenHelper  userID=? and
                DBOpenHelper helper = new DBOpenHelper(Log_in.this,"qianbao.db",null,1);
                SQLiteDatabase db = helper.getWritableDatabase();
                Cursor c = db.query("user_tb",null,"pwd=?",new String[]{mEditText_password.getText().toString()},null,null,null);
                if(c!=null && c.getCount() >= 1){
                    String[] cols = c.getColumnNames();
                    while(c.moveToNext()){
                        for(String ColumnName:cols){
                            Log.i("info",ColumnName+":"+c.getString(c.getColumnIndex(ColumnName)));
                        }
                    }
                    c.close();
                    db.close();

                    //将登陆用户信息存储到SharedPreferences中
                    SharedPreferences mySharedPreferences= getSharedPreferences("setting", Activity.MODE_PRIVATE); //实例化SharedPreferences对象
                    SharedPreferences.Editor editor = mySharedPreferences.edit();//实例化SharedPreferences.Editor对象
                    editor.putString("userID", userID); //用putString的方法保存数据
                    editor.commit(); //提交当前数据
                    Toast.makeText(Log_in.this, "登录成功！", Toast.LENGTH_LONG).show();
                    //Log_in.this.finish();
                    Intent intent=new Intent(Log_in.this,MainActivity.class);
                    Log_in.this.startActivity(intent);
                    Log_in.this.finish();
                }
                else{
                    Toast.makeText(Log_in.this, "密码输入错误！", Toast.LENGTH_SHORT).show();
                }
            }


        });

        /*mButton_enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Log_in.this,RegistActivity.class);
                //intent.putExtra("info", "No66778899");
                Log_in.this.startActivity(intent);
                }
        });*/
    }
    /**
     * Jump to the registration interface
     * @param v
     */
    public void OnMyChangePwdClick(View v)  {
        Intent intent=new Intent(Log_in.this,ChangePsw.class);
        //intent.putExtra("info", "No66778899");
        Log_in.this.startActivity(intent);
    }

    /**
     * Jump to reset password interface
     * @param v
     */
    public void OnMyResPwdClick(View v){
        Intent intent=new Intent(Log_in.this,ResPwdActivity.class);
        Log_in.this.startActivity(intent);
    }
}