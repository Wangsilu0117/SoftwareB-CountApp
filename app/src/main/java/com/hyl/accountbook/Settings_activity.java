package com.hyl.accountbook;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.hyl.BaseActivity;
import com.hyl.bean.records;
import com.hyl.dao.DBOpenHelper;
import com.hyl.util.pubFun;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class Settings_activity extends BaseActivity {
    private Button mButton_pwd_set;
    private Button mButton_remind;
    private Button mButton_backup;
    private Button mButton_restore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_activity);
        mButton_pwd_set = findViewById(R.id.button_password);
        mButton_remind = findViewById(R.id.button_remind);
        mButton_backup = findViewById(R.id.button_backup);
        mButton_restore = findViewById(R.id.button_restore);

        DBOpenHelper helper = new DBOpenHelper(this,"accountbook.db",null,5);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query("records",null,null,null,null,null,"id desc");

        mButton_pwd_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Settings_activity.this, ChoosePwd.class);
                //intent.putExtra("info", "No66778899");
                Settings_activity.this.startActivity(intent);
            }
        });
        mButton_remind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent intent=new Intent(Settings_activity.this, Alarm.class);
                Settings_activity.this.startActivity(intent);
            }
        });
        mButton_backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                int i=0;
               dataBackup();
            }
        });
        mButton_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                dataRecover();
                //restore();
//                Toast.makeText(getApplicationContext(), "数据已成功恢复",
//                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    ////////////////////////////////////////////////////////////////////
    // 数据恢复
    private void dataRecover() {
        // TODO Auto-generated method stub
        new BackupTask(this).execute("restoreDatabase");
    }

    // 数据备份
    private void dataBackup() {
        // TODO Auto-generated method stub
        new BackupTask(this).execute("backupDatabase");
    }



}