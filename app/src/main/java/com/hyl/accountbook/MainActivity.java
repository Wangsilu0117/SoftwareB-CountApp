package com.hyl.accountbook;

import com.hyl.BaseActivity;
import com.hyl.accountbook.CircleMenuLayout.OnMenuItemClickListener;
import com.hyl.bean.Accounts;
import com.hyl.bean.RecordCategory;
import com.hyl.util.InitDataUtil;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

/**
 * @programName: MainActivity.java
 * @programFunction: the home page
 * @createDate: 2018/09/19
 * @author: AnneHan
 * @version:
 * xx.   yyyy/mm/dd   ver    author    comments
 * 01.   2018/09/19   1.00   AnneHan   New Create
 */
public class MainActivity extends BaseActivity {

    //第一次点击事件发生的时间
    private long mExitTime;

    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[] { "设置",
            //"关于我们", "心愿墙","特色设置",
            "账户管理",
            "图表分析",
            "查看流水",
             "收入&支出",
            //"统计"
    };
    private int[] mItemImgs = new int[] { R.mipmap.home_mbank_4_normal,
            R.mipmap.home_mbank_1_normal,
            R.mipmap.tubiao,
            R.mipmap.liushui,
            R.mipmap.home_mbank_5_normal,
            //R.mipmap.home_mbank_6_normal
    };

    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //litepal创建数据库
        Connector.getDatabase();
        checkPermission();
        ////////////////////////
        initDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);

        mCircleMenuLayout.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            public void itemClick(View view, int pos) {
                if (mItemTexts[pos] == "收入&支出") {
                    openSpendingWind(view);
                } else if (mItemTexts[pos] == "设置") {
                    openLoginWind(view);
                } else if (mItemTexts[pos] == "统计") {
                    openCountWind(view);
                } else if(mItemTexts[pos] == "查看流水"){
                    openFlowWind(view);
                } else if (mItemTexts[pos] == "账户管理") {
                    openAccountsWind(view);
                }
                else if (mItemTexts[pos] == "图表分析") {
                    //openAboutUsAddWind(view);
                    openChartWind(view);
                }
            }

            public void itemCenterClick(View view) {
                if(count == 0){
                    count = 1;
                    Toast.makeText(MainActivity.this, "通过经常记账，时刻掌握财务状态~", Toast.LENGTH_SHORT).show();
                }else if(count == 1){
                    count = 2;
                    Toast.makeText(MainActivity.this, "通过财务分析，了解个人消费习惯~", Toast.LENGTH_SHORT).show();
                }else if(count == 2){
                    count = 3;
                    Toast.makeText(MainActivity.this, "了解收支情况，适度控制消费欲望~", Toast.LENGTH_SHORT).show();
                }else{
                    count = 0;
                    Toast.makeText(MainActivity.this, "通过长久记录，发现生活本来意义~", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    /**
     * 跳转至设置界面
     * @param v
     */
    private void openLoginWind(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, Settings_activity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转至开销界面，记录收入与支出
     * @param v
     */
    private void openSpendingWind(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SpendingActivity.class);
        this.startActivity(intent);
    }


    /**
     * 跳转至统计界面
     * @param v
     */
    private void openCountWind(View v){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, CountActivity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转至流水界面
     * @param v
     */
    private void openFlowWind(View v){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, WatchFlowActivity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转至特色设置界面
     * @param v
     */
   /* private void openSettingWind(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SettingActivity.class);
        this.startActivity(intent);
    }*/

    /**
     * 跳转至心愿墙界面
     * @param v
     */
    private void openWishWind(View v){
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, WishActivity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转至关于我们界面
     * @param v
     */
    private void openAboutUsAddWind(View v){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this, AboutUsActivity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转至账户界面
     * @param v
     */
    private void openAccountsWind(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, AccountsActivity.class);
        this.startActivity(intent);
    }

    /**
     * 跳转至图表统计页面
     * @param v
     */
    private void openChartWind(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, ChartActivity.class);
        this.startActivity(intent);
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
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化数据库
     */
    private void initDatabase () {
        Connector.getDatabase();
        if (DataSupport.findAll(Accounts.class).isEmpty()) {
            // 若Accounts为空，则添加初始化数据
            List<Accounts> accounts = InitDataUtil.getAccountsInitData();
            DataSupport.saveAll(accounts);
        }
        if (DataSupport.findAll(RecordCategory.class).isEmpty()) {
            // 若RecordCategory为空，则添加初始化数据
            List<RecordCategory> recordCategories = InitDataUtil.getRecordCategoryInitData();
            DataSupport.saveAll(recordCategories);
        }
    }

    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();

    // private ImageView welcomeImg = null;
    private static final int PERMISSION_REQUEST = 1;
    // 检查权限

    private void checkPermission() {
        mPermissionList.clear();

        //判断哪些权限未授予
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        /**
         * 判断是否为空
         */
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了

        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, PERMISSION_REQUEST);
        }
    }

    /**
     * 响应授权
     * 这里不管用户是否拒绝，都进入首页，不再重复申请权限
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST:

                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }


}
