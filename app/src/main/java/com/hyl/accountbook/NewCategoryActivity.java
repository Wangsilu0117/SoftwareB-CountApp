package com.hyl.accountbook;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.hyl.BaseActivity;
import com.hyl.bean.RecordCategory;
import com.hyl.bean.records;
import com.hyl.dao.DBOpenHelper;
import com.hyl.util.pubFun;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

public class NewCategoryActivity extends BaseActivity {

    private EditText editcate1;
    private EditText editcate2;
    private int type = 0;//0:income   1:payout  2:transfer
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_category_activity);
        editcate1 = (EditText) findViewById(R.id.editcate1);
        editcate2 = (EditText) findViewById(R.id.editcate2);

        final Intent intent = getIntent();
        type = intent.getIntExtra("Type", 0);
    }
    public void OnMyConfirmClick(View v) {
        confirmInfo();
    }
    public void OnMyCancelClick(View v){

        Intent intent = new Intent(NewCategoryActivity.this, ExpenseProcesActivity.class);
        intent.putExtra("strType", type);
        NewCategoryActivity.this.startActivity(intent);
        this.finish();
    }

    private void confirmInfo() {
        if(pubFun.isEmpty(editcate1.getText().toString()) || pubFun.isEmpty(editcate2.getText().toString())){
            Toast.makeText(this, "一级分类或二级分类不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //List<RecordCategory>  recordcategories = DataSupport.order("level_1 desc").find(RecordCategory.class);
        if(type==0)
        {
            String level2=editcate2.getText().toString();
            if(DataSupport.where("level_2 = ?",level2).find(RecordCategory.class).isEmpty()){
                RecordCategory recordcategory=new RecordCategory(0, editcate1.getText().toString(), editcate2.getText().toString(), R.drawable.shouru_zidingyi);
                recordcategory.save();
                Toast.makeText(this, "收入类别新增成功", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(NewCategoryActivity.this, ExpenseProcesActivity.class);
                intent.putExtra("strType", type);
                NewCategoryActivity.this.startActivity(intent);
                this.finish();
            }
            else{
                Toast.makeText(this, "本二级分类已存在", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        else if(type==1)
        {
            String level2=editcate2.getText().toString();
            if(DataSupport.where("level_2 = ?",level2).find(RecordCategory.class).isEmpty()){
                RecordCategory recordcategory=new RecordCategory(1, editcate1.getText().toString(), editcate2.getText().toString(), R.drawable.zhichu_zidingyi);
                recordcategory.save();
                Toast.makeText(this, "支出类别新增成功", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(NewCategoryActivity.this, ExpenseProcesActivity.class);
                intent.putExtra("strType", type);
                NewCategoryActivity.this.startActivity(intent);
                this.finish();
            }
            else{
                Toast.makeText(this, "本二级分类已存在", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
}