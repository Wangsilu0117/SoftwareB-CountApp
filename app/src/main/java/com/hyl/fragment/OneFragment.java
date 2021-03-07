package com.hyl.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.hyl.accountbook.Log_in;
import com.hyl.accountbook.R;
import com.hyl.dao.DBOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/////////////////////////////////////
import android.content.Context;

/**
 * @programName: OneFragment.java
 * @programFunction: Recording details of income and expenditure
 * @createDate: 2018/09/19
 * @author: AnneHan
 * @version:
 * xx.   yyyy/mm/dd   ver    author    comments
 * 01.   2018/09/19   1.00   AnneHan   New Create
 */
public class OneFragment extends Fragment {

    List<Map<String, Object>> listitem = new ArrayList<Map<String, Object>>(); //存储数据的数组列表
    int[] image_expense = new int[]{R.mipmap.detail_income, R.mipmap.detail_payout, R.mipmap.detail_transfer}; //存储图片,可更改

    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_one, container, false);

        getData();

        SimpleAdapter adapter = new SimpleAdapter(getActivity()
                , listitem
                , R.layout.fragment_one_item
                , new String[]{"expense_category", "expense_date", "expense_money", "image_expense"}
                , new int[]{R.id.tv_expense_category,R.id.tv_expense_date, R.id.tv_expense_money, R.id.image_expense});
        // 第一个参数是上下文对象
        // 第二个是listitem
        // 第三个是指定每个列表项的布局文件
        // 第四个是指定Map对象中定义的两个键（这里通过字符串数组来指定）
        // 第五个是用于指定在布局文件中定义的id（也是用数组来指定）

        ListView listView = (ListView) v.findViewById(R.id.lv_expense);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置监听器
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(), map.get("expense_category").toString(), Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }

    /**
     * 从数据库获得适配器数据
     */
    private void getData(){
        //先判断用户是否登录
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("setting", Activity.MODE_PRIVATE);
        String userID =sharedPreferences.getString("userID", "");

        Log.i("info", "当前登录的用户是" + userID);

        if(userID.isEmpty()){
            new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage("您还未登录，请点击确定按钮进行登录！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            getActivity().setResult(-1);
                            Intent intent=new Intent(getActivity(), Log_in.class);
                            getActivity().startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            return;
                        }
                    })
                    .show();
        }else{
            //call DBOpenHelper
            DBOpenHelper helper = new DBOpenHelper(getActivity(),"accountbook.db",null,5);
            SQLiteDatabase db = helper.getWritableDatabase();

            Cursor c = db.query("records",null,"user=?",new String[]{userID},null,null,"id desc");
            c.moveToFirst();
            //int iColCount = c.getColumnCount();
            int iNumber = 0;
            String strType = "";
            while (iNumber < c.getCount()){
                Map<String, Object> map = new HashMap<String, Object>();
                strType = c.getString(c.getColumnIndex("type"));
                map.put("image_expense", image_expense[Integer.parseInt(strType)]);
                if(strType.equals("0")){
                    map.put("expense_category", c.getString(c.getColumnIndex("catagory2")));
                    map.put("expense_date", c.getString(c.getColumnIndex("date")));
                    map.put("expense_money", "+" + c.getString(c.getColumnIndex("amount")));
                }else if(strType.equals("1")){
                    map.put("expense_category", c.getString(c.getColumnIndex("catagory2")));
                    map.put("expense_date", c.getString(c.getColumnIndex("date")));
                    map.put("expense_money", "-" + c.getString(c.getColumnIndex("amount")));
                }
                else{
                    map.put("expense_category", c.getString(c.getColumnIndex("account"))+"-->"+c.getString(c.getColumnIndex("transfer")));
                    map.put("expense_date", c.getString(c.getColumnIndex("date")));
                    map.put("expense_money", "(" + c.getString(c.getColumnIndex("amount")) + ")");
                }

                c.moveToNext();
                listitem.add(map);
                iNumber++;
                System.out.println(listitem);
            }
            c.close();
            db.close();
        }
    }
}

