package com.hyl.accountbook;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hyl.adapter.MyAdapter;
import com.hyl.bean.Accounts;
import com.hyl.bean.records;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class WatchFlowActivity extends AppCompatActivity {
    private static final String TAG = "main";
    private Spinner years_spinner;
    private Spinner months_spinner;
    private Spinner days_spinner;
    private Spinner account_spinner;
    private Spinner saler_spinner;
    private Spinner item_spinner;
    private Button makesure_button;
    private Button add_button;
    private TextView total_amount_edittext;
    private TextView total_income_edittext;
    private TextView total_expense_edittext;
    int year_selected, month_selected, day_selected;
    int year_now, month_now;
    double total_amount = 0, total_income = 0, total_expense = 0;
    String account_selected, saler_selected, item_selected;
    List<Accounts> all_accounts;
    List<records> all_salers, all_items;
    List<String>all_accounts_str, all_salers_str, all_items_str;
    //    private RecyclerView recyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager layoutManager;
//    private String[] myDataset = {"1", "2", "3"};
    List<records> recordList = new ArrayList<records>();
//    myDataset = {"1", "2", "3"};

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_flow);
        //设置下拉刷新
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(R.color.color_primary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh(year_now, month_now);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
            }
        });


        makesure_button = findViewById(R.id.make_sure);
        total_amount_edittext = findViewById(R.id.total_amount);
        total_income_edittext = findViewById(R.id.total_income);
        total_expense_edittext = findViewById(R.id.total_expense);

        Calendar cal;
//        int year_now, month_now;
        Flow new_flow = new Flow();
        cal = Calendar.getInstance();
        calculateTotalIncome cal_income = new calculateTotalIncome();
        calculateTotalExpense cal_expense = new calculateTotalExpense();
        cal.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        year_now = cal.get(Calendar.YEAR);
        month_now = cal.get(Calendar.MONTH)+1;
        Log.d(TAG, "onCreate: "+year_now+month_now);
        DecimalFormat df = new DecimalFormat( "0.00 ");
        total_income = cal_income.calculateTotalIncome(year_now, month_now, 0, "所有账户", "所有商家","所有商品");
//        total_income_edittext.setText(String.valueOf(total_income));
        total_income_edittext.setText(df.format(total_income));
        Log.d(TAG, "onCreate: total income"+total_income);
        total_expense = cal_expense.calculateTotalExpense(year_now, month_now, 0, "所有账户", "所有商家","所有商品");
//        total_expense_edittext.setText(String.valueOf(0-total_expense));
        Log.d(TAG, "onCreate: total expense"+total_expense);
        total_expense_edittext.setText(df.format(total_expense));
//        total_expense_edittext.setText("-" + cal_expense.calculateTotalExpense(year_now, month_now, 0, "所有账户"));
        total_amount = total_income - total_expense;
//        total_amount_edittext.setText(String.valueOf(total_amount));
        total_amount_edittext.setText(df.format(total_amount));

        recordList.clear();
//                recordList = new_flow.flow(year_selected, month_selected, day_selected);
        recordList.addAll(new_flow.flow(year_now, month_now, 0, "所有账户", "所有商家", "所有商品"));
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(10, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
//                recyclerView.addItemDecoration(new DividerItemDecoration(WatchFlowActivity.this, DividerItemDecoration.VERTICAL));
//                recyclerView.
        MyAdapter adapter = new MyAdapter(recordList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);


        years_spinner = (Spinner)findViewById(R.id.years_spinner);
        years_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String content = parent.getItemAtPosition(position).toString();
                year_selected = Integer.valueOf(content).intValue();
                System.out.println(year_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        months_spinner = (Spinner)findViewById(R.id.months_spinner);
        months_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String content = parent.getItemAtPosition(position).toString();
                month_selected = Integer.valueOf(content).intValue();
                System.out.println(month_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        days_spinner = (Spinner)findViewById(R.id.days_spinner);
        days_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String content = parent.getItemAtPosition(position).toString();
                day_selected = Integer.valueOf(content).intValue();
                System.out.println(account_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        String sql = "select distinct account from records";
        all_accounts = DataSupport.select("name").find(Accounts.class);
        getAccountsItems getaccountsitems = new getAccountsItems();
        all_accounts_str = getaccountsitems.getAccountsItems(all_accounts);//all_accounts中存的是accounts类，将其装换成string
        ArrayAdapter accountsAdapter = new ArrayAdapter(WatchFlowActivity.this, android.R.layout.simple_spinner_item, all_accounts_str);
        accountsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        account_spinner = (Spinner)findViewById(R.id.account_spinner);
        account_spinner.setAdapter(accountsAdapter);
        account_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String content = parent.getItemAtPosition(position).toString();
//                day_selected = Integer.valueOf(content).intValue();
                account_selected = parent.getItemAtPosition(position).toString();
                System.out.println(account_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        all_salers = DataSupport.select("saler").find(records.class);
        getRecordsItems getrecordsitems1 = new getRecordsItems();
        all_salers_str = getrecordsitems1.getRecordsItems(all_salers, "salers");//all_accounts中存的是accounts类，将其装换成string
        all_salers_str = (List) all_salers_str.stream().distinct().collect(Collectors.toList());
        Log.d(TAG, "onCreate: "+all_accounts_str);
        ArrayAdapter salerAdapter = new ArrayAdapter(WatchFlowActivity.this, android.R.layout.simple_spinner_item, all_salers_str);
        salerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saler_spinner = (Spinner)findViewById(R.id.saler_spinner);
        saler_spinner.setAdapter(salerAdapter);
        saler_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String content = parent.getItemAtPosition(position).toString();
//                day_selected = Integer.valueOf(content).intValue();
                saler_selected = parent.getItemAtPosition(position).toString();
                System.out.println(saler_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        all_items = DataSupport.select("item").find(records.class);
        getRecordsItems getrecordsitems2 = new getRecordsItems();
        all_items_str = getrecordsitems2.getRecordsItems(all_items, "items");//all_accounts中存的是accounts类，将其装换成string
        all_items_str = (List)all_items_str.stream().distinct().collect(Collectors.toList());
        ArrayAdapter itemAdapter = new ArrayAdapter(WatchFlowActivity.this, android.R.layout.simple_spinner_item, all_items_str);
        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        item_spinner = (Spinner)findViewById(R.id.item_spinner);
        item_spinner.setAdapter(itemAdapter);
        item_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String content = parent.getItemAtPosition(position).toString();
//                day_selected = Integer.valueOf(content).intValue();
                item_selected = parent.getItemAtPosition(position).toString();
                System.out.println(account_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



//        makesure_button = findViewById(R.id.make_sure);
//        total_amount_edittext = findViewById(R.id.total_amount);
//        total_income_edittext = findViewById(R.id.total_income);
//        total_expense_edittext = findViewById(R.id.total_expense);
        makesure_button.setOnClickListener(new View.OnClickListener() {
            private static final String TAG = "into flow";

            @Override
            public void onClick(View view) {
                System.out.print("makesure");
                Log.d(TAG, "onClick: ");
                Log.d(TAG, year_selected+ " " + month_selected+ " " + day_selected);
                Flow new_flow = new Flow();
                Log.d(TAG, "onClick: calculate");
                calculateTotalAmount cal_amount = new calculateTotalAmount();
                calculateTotalIncome cal_income = new calculateTotalIncome();
                calculateTotalExpense cal_expense = new calculateTotalExpense();
//                total_amount = cal_amount.calculateTotalAmount(year_selected,month_selected,day_selected,account_selected);
//                total_amount_edittext.setText(String.valueOf(total_amount));
                DecimalFormat df = new DecimalFormat( "0.00 ");
                total_income = cal_income.calculateTotalIncome(year_selected, month_selected, day_selected, account_selected, saler_selected, item_selected);
//        total_income_edittext.setText(String.valueOf(total_income));
                total_income_edittext.setText(df.format(total_income));
                Log.d(TAG, "onCreate: total income"+total_income);
                total_expense = cal_expense.calculateTotalExpense(year_selected, month_selected, day_selected, account_selected, saler_selected, item_selected);
//        total_expense_edittext.setText(String.valueOf(0-total_expense));
                Log.d(TAG, "onCreate: total expense"+total_expense);
                total_expense_edittext.setText(df.format(total_expense));
//        total_expense_edittext.setText("-" + cal_expense.calculateTotalExpense(year_now, month_now, 0, "所有账户"));
                total_amount = total_income - total_expense;
//        total_amount_edittext.setText(String.valueOf(total_amount));
                total_amount_edittext.setText(df.format(total_amount));
//                total_income = cal_income.calculateTotalIncome(year_selected, month_selected, day_selected, account_selected);
//                total_income_edittext.setText(String.valueOf(total_income));
//                total_expense = cal_expense.calculateTotalExpense(year_selected, month_selected, day_selected, account_selected);
//                total_expense_edittext.setText(String.valueOf(0-total_expense));
//                total_amount = total_income - total_expense;
//                total_amount_edittext.setText(String.valueOf(total_amount));

                recordList.clear();
//                recordList = new_flow.flow(year_selected, month_selected, day_selected);
                recordList.addAll(new_flow.flow(year_selected, month_selected, day_selected, account_selected, saler_selected, item_selected));
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(10, StaggeredGridLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(layoutManager);
//                recyclerView.addItemDecoration(new DividerItemDecoration(WatchFlowActivity.this, DividerItemDecoration.VERTICAL));
//                recyclerView.
                MyAdapter adapter = new MyAdapter(recordList);
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
        });

        add_button = findViewById(R.id.add);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(WatchFlowActivity.this, SpendingActivity.class);
                startActivity(intent);
            }
        });


//        setContentView(R.layout.recyclerview);
//        initRecords();
//        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(layoutManager);
//        MyAdapter adapter = new MyAdapter(recordList);
//        adapter.notifyDataSetChanged();
//        recyclerView.setAdapter(adapter);
    }


    //用于测试recyclerview的数据
    private void initRecords(){
        records record_1 = new records();
        record_1.setId(2);
        record_1.setAccount("借记卡");
        record_1.setAmount(-150);
        record_1.setType(1);
        record_1.setCatagory1("三餐");
        record_1.setCatagory2("晚饭");
//        record_1.setYear(2016);
//        record_1.setMonth(1);
//        record_1.setDay(1);
        record_1.setDate("2020/10/1");
        record_1.setHour(19);
        record_1.setMin(0);
        record_1.setSaler("益田");
        record_1.setItem("海底捞");
//        record_1.setItem(getRandomLengthName("海底捞"));
        record_1.setNotes("好吃");
        record_1.setImageId();
        record_1.save();
        recordList.add(record_1);

        records record_2 = new records();
        record_2.setId(3);
        record_2.setAccount("信用卡");
        record_2.setAmount(-100);
        record_2.setType(1);
        record_2.setCatagory1("三餐");
        record_2.setCatagory2("午饭");
//        record_2.setYear(2016);
//        record_2.setMonth(1);
//        record_2.setDay(1);
        record_2.setDate("2020/10/1");
        record_2.setHour(19);
        record_2.setMin(0);
        record_2.setSaler("益田");
        record_2.setItem("八合里");
//        record_2.setItem(getRandomLengthName("八合里"));
        record_2.setNotes("好吃");
        record_2.setImageId();
        record_2.save();
        recordList.add(record_2);

        records record_3 = new records();
        record_3.setId(3);
        record_3.setAccount("借记卡");
        record_3.setAmount(100);
        record_3.setType(0);
        record_3.setCatagory1("收入");
        record_3.setCatagory2("零花钱");
//        record_3.setYear(2016);
//        record_3.setMonth(2);
//        record_3.setDay(1);
        record_3.setDate("2020/10/2");
        record_3.setHour(19);
        record_3.setMin(0);
        record_3.setSaler("");
        record_3.setItem("");
//        record_2.setItem(getRandomLengthName("零花钱"));
        record_3.setNotes("");
        record_3.setImageId();
        record_3.save();
        recordList.add(record_3);
        Log.d(TAG, "initRecords: ");
    }

    private String getRandomLengthName(String name) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(name);
        }
        return builder.toString();
    }

    private void refresh(int year_now, int month_now){
        Flow new_flow = new Flow();
        calculateTotalIncome cal_income = new calculateTotalIncome();
        calculateTotalExpense cal_expense = new calculateTotalExpense();
        DecimalFormat df = new DecimalFormat( "0.00 ");
        total_income = cal_income.calculateTotalIncome(year_now, month_now, 0, account_selected, saler_selected, item_selected);
        total_income_edittext.setText(df.format(total_income));
        Log.d(TAG, "onCreate: total income"+total_income);
        total_expense = cal_expense.calculateTotalExpense(year_now, month_now, 0, account_selected, saler_selected, item_selected);
        Log.d(TAG, "onCreate: total expense"+total_expense);
        total_expense_edittext.setText(df.format(total_expense));
        total_amount = total_income - total_expense;
        total_amount_edittext.setText(df.format(total_amount));

        recordList.clear();
        recordList.addAll(new_flow.flow(year_now, month_now, 0, account_selected, saler_selected, item_selected));
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(10, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        MyAdapter adapter = new MyAdapter(recordList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


}
