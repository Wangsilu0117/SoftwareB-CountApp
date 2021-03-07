package com.hyl.accountbook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hyl.adapter.AccountsTypeAdapter;
import com.hyl.bean.AccountsType;
import com.hyl.stickyheader.StickyHeaderGridLayoutManager;
import com.hyl.util.InitDataUtil;

import java.util.ArrayList;
import java.util.List;

public class AccountsActivity extends AppCompatActivity {

    private List<AccountsType> accountsTypeList = new ArrayList<>();
    private AccountsTypeAdapter adapter;

    private DrawerLayout mDrawerLayout;

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accounts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.accounts_toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.accounts_layout);
        // 设置返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 设置加号按钮
        FloatingActionButton add_fab = (FloatingActionButton) findViewById(R.id.add_accounts);
        add_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountsActivity.this, CreateAccountsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 账户列表
        initAccountsTypeList();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.accounts_recycler_view);
        StickyHeaderGridLayoutManager mLayoutManager = new StickyHeaderGridLayoutManager(1);
        mLayoutManager.setHeaderBottomOverlapMargin(5);
//        recyclerView.setItemAnimator(new DefaultItemAnimator() {
//            @Override
//            public boolean animateRemove(RecyclerView.ViewHolder holder) {
//                dispatchRemoveFinished(holder);
//                return false;
//            }
//        });
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new AccountsTypeAdapter(AccountsActivity.this, accountsTypeList);
        recyclerView.setAdapter(adapter);
        // 设置下拉刷新
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.accounts_swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary); // 设置下拉刷新进度条颜色
        swipeRefresh.setDistanceToTriggerSync(200);        //设置向下拉多少出现刷新
        swipeRefresh.setProgressViewEndTarget(false, 200);        //设置刷新出现的位置
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAccounts();
//                adapter = new AccountsTypeAdapter(accountsTypeList);
//                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.accounts_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
        }
        return true;
    }

    // 刷新账户
    private void refreshAccounts() {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    Thread.sleep(20);      // 让线程沉睡0.2s，以便看到刷新过程
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initAccountsTypeList();
                        adapter.setmAccountsType(accountsTypeList);
                        adapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    // 根据账户种类初始化accoutsTypeList
    private void initAccountsTypeList() {
        accountsTypeList.clear();
        List<String> types = InitDataUtil.getAccountsType();
        for(String type : types) {
            accountsTypeList.add(new AccountsType(type));
        }
        Log.d("AAccountsActivity", "--------------------------------");
        for(AccountsType accountsType : accountsTypeList) {
            Log.d("AccountsActivity", "accountsTypeList  type:" + accountsType.getType() + " balance:" + accountsType.getBalance());
        }
    }

}

