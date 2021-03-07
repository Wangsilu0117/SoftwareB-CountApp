package com.hyl.accountbook;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyl.dao.DBOpenHelper;
import com.hyl.util.pubFun;

public class ChangePsw extends AppCompatActivity {
    private EditText pastPsw;
    private EditText editnewPwd;
    private EditText editnewResPwd;
    private Button btn_Confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_psw);
        pastPsw = (EditText) findViewById(R.id.pastPsw);
        editnewPwd = (EditText) findViewById(R.id.editnewPwd);
        editnewResPwd = (EditText) findViewById(R.id.editnewResPwd);
        btn_Confirm = (Button) findViewById(R.id.btn_Confirm);
    }
    /**
     * confirm event
     * @param v
     */
    public void OnMyCfmClick(View v) {
        confirmInfo();
    }

    /**
     * confirm event
     */
    private void confirmInfo() {
        if(pubFun.isEmpty(pastPsw.getText().toString()) || pubFun.isEmpty(editnewPwd.getText().toString()) || pubFun.isEmpty(editnewResPwd.getText().toString())){
            Toast.makeText(this, "原密码或新密码不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!editnewPwd.getText().toString().equals(editnewResPwd.getText().toString())){
            Toast.makeText(this, "新密码两次输入不一致！", Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            //调用DBOpenHelper
            DBOpenHelper helper = new DBOpenHelper(this,"qianbao.db",null,1);
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor c = db.query("user_tb",null,"pwd=?",new String[]{pastPsw.getText().toString()},null,null,null);
            if(c!=null && c.getCount() >= 1){
                ContentValues cv = new ContentValues();
                cv.put("pwd", editnewPwd.getText().toString());
                String[] args = {String.valueOf(pastPsw.getText().toString())};
                long rowid = db.update("user_tb", cv, "pwd=?",args);
                c.close();
                db.close();
                Toast.makeText(this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                this.finish();
            }
            else{
                Toast.makeText(this, "旧密码错误！", Toast.LENGTH_SHORT).show();
            }
        }


    }
}