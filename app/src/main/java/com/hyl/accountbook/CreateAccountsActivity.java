package com.hyl.accountbook;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.hyl.bean.Accounts;
import com.hyl.bean.AccountsTypeChoose;
import com.hyl.bean.records;
import com.hyl.filter.LengthFilter;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.hyl.util.InitDataUtil.getAccountsType;

public class CreateAccountsActivity extends AppCompatActivity {

    private static final String TAG = "CreateAccountsActivity";


    private DrawerLayout mDrawerLayout;
    private List<AccountsTypeChoose> typeItems = new ArrayList<>();
    private EditText type_et, name_et, balance_et;
    private OptionsPickerView pvTypeOptions;
    private String pre_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_accounts);

        // 等数据加载完毕再初始化并显示Picker,以免还未加载完数据就显示,造成APP崩溃
        getOptionData();
        initTypeOptionPicker();

        Toolbar toolbar = (Toolbar) findViewById(R.id.create_accounts_toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.create_accounts_layout);

        // 设置关闭键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 设置类型输入
        type_et = (EditText) findViewById(R.id.create_accounts_type);
        type_et.setFocusable(false);
        type_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pvTypeOptions != null) {
                    pvTypeOptions.show();
                }
            }
        });
        // 设置名字输入
        name_et = (EditText) findViewById(R.id.create_accounts_name);
        // 设置余额输入
        balance_et = (EditText) findViewById(R.id.create_accounts_balance);
        balance_et.setFilters(new InputFilter[] {new LengthFilter()});  // 限制输入小数位数
        balance_et.setSelectAllOnFocus(true);

        // 获取上一个活动传递的参数
        Intent intent = getIntent();
        pre_name = intent.getStringExtra("name");
        if (pre_name != null) {
            Log.d(TAG, "change: " + pre_name);
            Accounts pre_account = DataSupport.where("name = ?", pre_name).findFirst(Accounts.class);
            type_et.setText(pre_account.getType());
            name_et.setText(pre_name);
            balance_et.setText("" + pre_account.getBalance());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_accounts_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.accounts_save_btn:
                if(type_et.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请选择新建账户类型！", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (name_et.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请输入新建账户名称！", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (balance_et.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "请输入账户余额！", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    String type = type_et.getText().toString();
                    String name = name_et.getText().toString();
                    float balance = Float.parseFloat(balance_et.getEditableText().toString().trim());
                    if (pre_name != null) {
                        // 修改账户
                        Log.d(TAG, "change to: type-" + type + " name-" + name + " balance-" + balance);
                        Accounts account = new Accounts();
                        account.setName(name);
                        account.setType(type);
                        account.setBalance(balance);
                        account.setImageId();
                        account.updateAll("name = ?", pre_name);
                        if (pre_name != name) {
                            records record = new records();
                            record.setAccount(name);
                            record.updateAll("account = ?", pre_name);
                        }
                        Toast.makeText(this, "修改账户成功！", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        // 新建账户
                        if (DataSupport.where("name=?", name).find(Accounts.class).size() != 0) {
                            Toast.makeText(this, "账户 " + name + " 已存在！", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        Accounts account = new Accounts();
                        account.setName(name);
                        account.setType(type);
                        account.setBalance(balance);                        account.setImageId();
                        account.setImageId();
                        account.save();
                        Log.d(TAG, "new account! name:" + account.getName() + " type:" + account.getType() + " balance:" + account.getBalance());
                        Toast.makeText(this, "新建账户成功！", Toast.LENGTH_SHORT).show();
                    }
                    finish();
                    break;
                }
            default:
        }
        return true;
    }

    private void initTypeOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvTypeOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = typeItems.get(options1).getPickerViewText();
                type_et.setText(tx);
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTypeOptions.returnData();
                                pvTypeOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTypeOptions.dismiss();
                            }
                        });
                    }
                })
                .isDialog(true)
                .setOutSideCancelable(false)
                .build();

        pvTypeOptions.setPicker(typeItems);//添加数据
    }

    private void getOptionData() {
        List<String> typeList = getAccountsType();
        int i = 0;
        for (String type : typeList) {
            typeItems.add(new AccountsTypeChoose(i, type));
            i++;
        }
    }

}