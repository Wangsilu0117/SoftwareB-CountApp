package com.hyl.accountbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.hyl.BaseActivity;
import com.hyl.bean.Accounts;
import com.hyl.bean.ProvinceBean;
import com.hyl.bean.records;
import com.hyl.util.pubFun;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransferActivity extends BaseActivity {
    private int type = 2;//0:income   1:payout  2:transfer
    final static int EDIT_MODE = 3;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog datePicker = null;
    private Button cost_btn = null;
    private String  value="0";
    private Button trade_time_btn = null;
    private Context context;
    private OptionsPickerView pvOptions, pvCustomOptions, pvNoLinkOptions;
    //账户
    private static List<String> accountList=new ArrayList<>();
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    //成员
    private ArrayList<String> memberList = new ArrayList<>();
    //备注
    private ArrayList<String> noteList = new ArrayList<>();
    //转账对象
    private ArrayList<String> transferList = new ArrayList<>();

    private TextView txtAccount_view;
    private Button account_btn;
    private ArrayAdapter<String> account_adapter;

    private TextView txtMember_view;
    private Button member_btn;
    private ArrayAdapter<String> member_adapter;

    private TextView txtNote_view;
    private EditText editnote;
    private ArrayAdapter<String> note_adapter;

    private TextView txtTransfer_view;
    private Button transfer_btn;
    private ArrayAdapter<String> transfer_adapter;

    private String txtAccount = "";
    private String txtNote = "";
    private String txtMember = "无成员";
    private String txtTransfer = "";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        getNoLinkData();
        //接收传递过来的参数从何而来？
        final Intent intent = getIntent();
        //type = intent.getIntExtra("strType", 0);

        context = this;

        loadingFormation();

        trade_time_btn.setText(pubFun.format(calendar.getTime()));

        cost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(TransferActivity.this, KeyPad.class);
                i.putExtra("value", value);
                startActivityForResult(i, 0);
            }
        });
        trade_time_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                openDate();
            }
        });

        account_btn=(Button)findViewById(R.id.account_btn);
        account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNoLinkOptionsPicker(1);
                pvNoLinkOptions.show(); //弹出条件选择器
            }

        });


        member_btn=(Button)findViewById(R.id.member_btn);
        member_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNoLinkOptionsPicker(2);
                pvNoLinkOptions.show(); //弹出条件选择器
            }

        });

        transfer_btn=(Button)findViewById(R.id.transfer_btn);
        transfer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNoLinkOptionsPicker(3);
                pvNoLinkOptions.show(); //弹出条件选择器
            }

        });

        editnote = (EditText) findViewById(R.id.editnote);
    }

    private void loadingFormation(){
        cost_btn=(Button)findViewById(R.id.cost_btn);
        trade_time_btn=(Button)findViewById(R.id.trade_time_btn);
    }

    private void openDate() {
        datePicker = new DatePickerDialog(this, mDateSetListenerSatrt,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePicker.show();
    }

    @Override
    /**
     * return money
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
            Bundle extras = data.getExtras();
            value = extras.getString("value");
            cost_btn.setText(DecimalFormat.getCurrencyInstance().format(Double.parseDouble(value)));
        }
    }

    /**
     * return date
     */
    private DatePickerDialog.OnDateSetListener mDateSetListenerSatrt = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.YEAR, year);
            trade_time_btn.setText(pubFun.format(calendar.getTime()));
        }
    };

    /**
     * 初始化spinner
     */

    public void OnMySaveClick(View v) {
        saveInfo(0);
        //exit();
    }
    public void OnMyCancelClick(View v) {
        exit();
    }
    public void OnMySavingClick(View v){
        saveInfo(1);
    }

    /**
     * cancel event
     */
    private void exit() {
        if(type != EDIT_MODE){
            Intent intent = new Intent(this,SpendingActivity.class);
            startActivity(intent);
            finish();
        }else{
            this.setResult(RESULT_OK, getIntent());
            this.finish();
        }
    }

    /**
     * save event
     */
    private void saveInfo(int i) {
        //Save之前先判断用户是否登录
        SharedPreferences sharedPreferences= getSharedPreferences("setting",Activity.MODE_PRIVATE);
        String userID =sharedPreferences.getString("userID", "");

        Log.i("info", "此次登录的用户是" + userID);

        if(userID.isEmpty()){
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("您还未登录，请点击确定按钮进行登录！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            setResult(RESULT_OK);
                            Intent intent=new Intent(TransferActivity.this,Log_in.class);
                            TransferActivity.this.startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            return;
                        }
                    })
                    .show();
        }else{
            if(value.equals("") || value == null || Double.parseDouble(value) <= 0){
                Toast.makeText(getApplicationContext(), getString(R.string.input_message),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if(txtAccount.equals("") ||txtAccount==null){
                Toast.makeText(getApplicationContext(),"请记录账户",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if(txtTransfer.equals("") || txtTransfer == null){
                Toast.makeText(getApplicationContext(), "请记录转账目标",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            txtNote=editnote.getText().toString();
            int intdate=construct_intdate();
            records Records=new records();
            Records.setUser(userID);
            Records.setAccount(txtAccount);
            Records.setAmount(Float.parseFloat(value));//将字符串转为数字
            Records.setType(type);
            Records.setDate(pubFun.format(calendar.getTime()));
            Records.setIntdate(intdate);
            Records.setMember(txtMember);
            Records.setNotes(txtNote);
            Records.setTransfer(txtTransfer);
            //Records.setImageId();
            Records.save();

            Accounts account = DataSupport.where("name = ?", txtAccount).findFirst(Accounts.class);
            float balance1 = account.getBalance();
            balance1 -= Float.parseFloat(value);

            Accounts transfer = DataSupport.where("name = ?", txtTransfer).findFirst(Accounts.class);
            float balance2 = transfer.getBalance();
            balance2 += Float.parseFloat(value);

            Accounts new_account = new Accounts();
            new_account.setBalance(balance1);
            new_account.updateAll("name = ?", account.getName());
            Accounts new_transfer = new Accounts();
            new_transfer.setBalance(balance2);
            new_transfer.updateAll("name = ?", transfer.getName());

            if(i==0){
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                exit();
            }
            else{
                Toast.makeText(this, "保存成功，再记一笔", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public int construct_intdate() {
        String date=pubFun.format(calendar.getTime());
        String regEx="[-]";
        String stringdate = date.replaceAll(regEx,"");
        return Integer.parseInt(stringdate);
    }
    private void getNoLinkData() {
        //账户
        List<Accounts> accounts = DataSupport.findAll(Accounts.class);
        for (Accounts account : accounts) {
            //accountList=accountList+account.getName()};
            accountList.add(account.getName());
        }

        memberList.add("无成员");
        memberList.add("本人");
        memberList.add("丈夫");
        memberList.add("妻子");
        memberList.add("子女");
        memberList.add("父母");
        memberList.add("家庭");

        List<Accounts> transfers = DataSupport.findAll(Accounts.class);
        for (Accounts transfer : transfers) {
            //accountList=accountList+account.getName()};
            transferList.add(transfer.getName());
        }

    }

    private void initNoLinkOptionsPicker(int i) {// 不联动的多级选项
        pvNoLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if(i==1){
                    txtAccount = accountList.get(options1);
                    account_btn.setText(txtAccount);
                    //Toast.makeText(TransferActivity.this, txtAccount, Toast.LENGTH_SHORT).show();
                }

                else if(i==2){
                    txtMember = memberList.get(options1);
                    member_btn.setText(txtMember);
                    //Toast.makeText(TransferActivity.this, txtMember, Toast.LENGTH_SHORT).show();
                }
                else if(i==3){
                    txtTransfer = transferList.get(options1);
                    transfer_btn.setText(txtTransfer);
                    //Toast.makeText(TransferActivity.this, txtTransfer, Toast.LENGTH_SHORT).show();
                }

            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1;
                        //Toast.makeText(TransferActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .setItemVisibleCount(5)
                // .setSelectOptions(0, 1, 1)
                .build();
//        pvNoLinkOptions.setNPicker(accountList,null,null);
//        pvNoLinkOptions.setSelectOptions(0);
        if(i==1){
            pvNoLinkOptions.setNPicker(accountList,null,null);
            pvNoLinkOptions.setTitleText("选择账户");
        }
        else if(i==2){
            pvNoLinkOptions.setNPicker(memberList,null,null);
            pvNoLinkOptions.setTitleText("选择成员");
        }
        else if(i==3){
            pvNoLinkOptions.setNPicker(transferList,null,null);
            pvNoLinkOptions.setTitleText("选择目标账户");
        }
        pvNoLinkOptions.setSelectOptions(0);
    }
}






