package com.hyl.accountbook;


import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.hyl.BaseActivity;
import com.hyl.bean.Accounts;
import com.hyl.bean.AccountsType;
import com.hyl.bean.ProvinceBean;
import com.hyl.bean.RecordCategory;
import com.hyl.bean.records;
import com.hyl.util.pubFun;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;

/**
 * @programName: ExpenseProcesActivity.java
 * @programFunction: Add an income and expense record
 * @createDate: 2018/09/19
 * @author: AnneHan
 * @version:
 * xx.   yyyy/mm/dd   ver    author    comments
 * 01.   2018/09/19   1.00   AnneHan   New Create
 */

public class ExpenseProcesActivity extends BaseActivity {

    private int type = 0;//0:income   1:payout  2:transfer
    final static int EDIT_MODE = 2;
    //////////////////////////////////////////////////////////
    private Button btn_Options;
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private OptionsPickerView pvOptions, pvCustomOptions, pvNoLinkOptions;


    private String[] str = null;
    private String[] accountId = null;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog datePicker = null;
    private AlertDialog dialog = null;
    private ArrayAdapter<String> adapter;
    private List<String> list = null;


    private TextView title_tv = null;
    private RadioGroup trans_type_tab_rg = null;
    private RadioButton rb1=null;
    private RadioButton rb2=null;

    private FrameLayout corporation_fl = null;
    private FrameLayout empty_fl = null;
    private Button cost_btn = null;
    private String  value="0";
    private Spinner first_level_category_spn = null;
    private Spinner sub_category_spn = null;
    private int type_sub_id = 0;
    private Spinner account_spn = null;
    private Spinner corporation_spn = null;
    private Button trade_time_btn = null;
    private Spinner project_spn = null;
    private Button memo_btn = null;
    private Button save_btn = null;
    private Button cancel_btn = null;

    private EditText edit = null;
    private int isInitOnly = 0;

    private Context context;

    //分类
    //private static String[] bigCategoryList = { "" };
    //private static String[] defaultSubCategory_info = { "" };
    //子分类
    //private static String[][] subCategory_info = new String[][] {{ "" }, { "" }};
    //账户
    private static List<String> accountList=new ArrayList<>();
    private ArrayList<ProvinceBean> options1Items = new ArrayList<>();
    //private static String[] accountList;
    //商家
    //private static String[] shopList = { "" };
    private ArrayList<String> shopList = new ArrayList<>();
    //成员
    //private static String[] memberList = {""};
    private ArrayList<String> memberList = new ArrayList<>();
    //项目
    //private static String[] eventList = {""};
    private ArrayList<String> eventList = new ArrayList<>();
    //备注
    //private static String[] noteList = { "" };
    private ArrayList<String> noteList = new ArrayList<>();


    //private TextView txtBigCategory_view;
    //private Spinner BigCategory_spinner;
    //private ArrayAdapter<String> BigCategory_adapter;

    //private TextView txtSubCategory_view;
    //private Spinner subCategory_spinner;
    //private ArrayAdapter<String> subCategory_adapter;

    private TextView txtAccount_view;
    private Button account_btn;
    //private ArrayAdapter<String> account_adapter;
    //private ArrayList<String> account_ArrayList = new ArrayList<>();

    private TextView txtShop_view;
    private Button shop_btn;
    private ArrayAdapter<String> shop_adapter;

    private TextView txtMember_view;
    private Button member_btn;
    private ArrayAdapter<String> member_adapter;

    private TextView txtEvent_view;
    private Button event_btn;
    private ArrayAdapter<String> event_adapter;

    private TextView txtNote_view;
    private EditText editnote;
    private ArrayAdapter<String> note_adapter;

    private String txtBigCategory="";
    private String txtSubCategory = "";
    private String txtAccount = "";
    private String txtShop = "无商家/地点";
    private String txtMember = "无成员";
    private String txtEvent = "无项目";
    private String txtNote = "";
    private String txtTransfer = "";
    private TextView txtDate;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_proces);
        //////////////////////////////////////////
        getNoLinkData();
//////////////////////////////////////////////////////
        //接收传递过来的参数从何而来:spending_putExtra()
        final Intent intent = getIntent();
        type = intent.getIntExtra("strType", 0);

        context = this;


        loadingFormation();
        trade_time_btn.setText(pubFun.format(calendar.getTime()));
/////////////////////////////////////////////////////////////////////////////////////////////
        OptionsDialog optionsDialog=init_OptionsDialog();

        //设置标题
        optionsDialog.setTitle("选择分类");
        //设置加号点击事件
        optionsDialog.setOnAddClickListener(new OptionsDialog.OnAddClickListener() {
            @Override
            public void OnAddClick(View v) {
                Intent intent_type = new Intent(ExpenseProcesActivity.this, NewCategoryActivity.class);
                intent_type.putExtra("Type", type);
                ExpenseProcesActivity.this.startActivity(intent_type);

                Toast.makeText(getContext(),"新建分类",Toast.LENGTH_SHORT).show();
                finish();
                //Intent intent = new Intent(ExpenseProcesActivity.this, NewCategoryActivity.class);
                //ExpenseProcesActivity.this.startActivity(intent);
            }
        });
        //设置一级分类被选中的事件
        optionsDialog.setOnPrimarySelectedListener(new OptionsDialog.OnPrimaryItemSelectedListener() {
            @Override
            public void OnItemSelected(int primaryId, int position) {
                // Toast.makeText(getContext(),"一级分类被选中："+primaryId,Toast.LENGTH_SHORT).show();
            }
        });
        //设置确认被点击的事件
        optionsDialog.setOnConfirmListener(new OptionsDialog.OnConfirmListener() {
            @Override
            public void OnConfirm(String primaryText, String secondaryText) {
                Toast.makeText(getContext(),"确认："+primaryText+"-"+secondaryText,Toast.LENGTH_SHORT).show();
                String tx=primaryText+"-"+secondaryText;
                txtBigCategory=primaryText;
                txtSubCategory=secondaryText;
                btn_Options.setText(tx);
            }
        });
        //弹出窗口
        //在Activity中使用，第一个参数可以用getSupportFragmentManager()
        //optionsDialog.show(getSupportFragmentManager(),"option");
        btn_Options = (Button) findViewById(R.id.btn_Options);
        btn_Options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出窗口
                //在Activity中使用，第一个参数可以用getSupportFragmentManager()
                optionsDialog.show(getSupportFragmentManager(),"option");
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

        shop_btn=(Button)findViewById(R.id.shop_btn);
        shop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNoLinkOptionsPicker(2);
                pvNoLinkOptions.show(); //弹出条件选择器
            }

        });

        member_btn=(Button)findViewById(R.id.member_btn);
        member_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNoLinkOptionsPicker(3);
                pvNoLinkOptions.show(); //弹出条件选择器
            }

        });

        event_btn=(Button)findViewById(R.id.event_btn);
        event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initNoLinkOptionsPicker(4);
                pvNoLinkOptions.show(); //弹出条件选择器
            }

        });

        editnote = (EditText) findViewById(R.id.editnote);
////////////////////////////////////////////////////////////////////////////////////

        cost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(ExpenseProcesActivity.this, KeyPad.class);
                i.putExtra("value", value);
                startActivityForResult(i, 0);
            }
        });
        trade_time_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                openDate();
            }
        });
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
                            Intent intent=new Intent(ExpenseProcesActivity.this,Log_in.class);
                            ExpenseProcesActivity.this.startActivity(intent);
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
            if(txtSubCategory.equals("") ||txtSubCategory==null){
                Toast.makeText(getApplicationContext(),"请记录分类",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if(txtAccount.equals("") ||txtAccount==null){
                Toast.makeText(getApplicationContext(),"请记录账户",
                        Toast.LENGTH_SHORT).show();
                return;
            }
////////////////////////////////////////////////////////////////////////////////////////向数据库记录数据
            txtNote=editnote.getText().toString();
            int intdate=construct_intdate();
            records Records=new records();
            Records.setUser(userID);
            Records.setAccount(txtAccount);
            Records.setAmount(Float.parseFloat(value));//将字符串转为数字
            Records.setType(type);
            Records.setCatagory1(txtBigCategory);
            Records.setCatagory2(txtSubCategory);
            Records.setDate(pubFun.format(calendar.getTime()));
            Records.setIntdate(intdate);
            Records.setMember(txtMember);
            Records.setSaler(txtShop);
            Records.setItem(txtEvent);
            Records.setNotes(txtNote);
            Records.setTransfer(txtTransfer);
            Records.setImageId();
            Records.save();
            Accounts account = DataSupport.where("name = ?", txtAccount).findFirst(Accounts.class);
            float balance = account.getBalance();
            if (type == 0) {
                balance += Float.parseFloat(value);
            }
            else {
                balance -= Float.parseFloat(value);
            }
            Accounts new_account = new Accounts();
            new_account.setBalance(balance);
            new_account.updateAll("name = ?", account.getName());

            if(i==0){
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                exit();
            }
            else{
                Toast.makeText(this, "保存成功，再记一笔", Toast.LENGTH_SHORT).show();
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////
    public int construct_intdate() {
        String date=pubFun.format(calendar.getTime());
        String regEx="[-]";
        String stringdate = date.replaceAll(regEx,"");
        return Integer.parseInt(stringdate);
    }
    ///////////////////////////////////////////////////////////////////////
    public OptionsDialog init_OptionsDialog(){
        OptionsDialog optionsDialog = new OptionsDialog();
        List<RecordCategory>  recordcategories = DataSupport.order("level_1 desc").find(RecordCategory.class);
        int i=1;//收入第一级id
        int j=1;//收入第二级id
        int k=1;//支出第一级id
        int l=1;//支出第二级id
        String level_ru="none";
        String level_chu="none";
        if(type==0){   //收入
            for (RecordCategory recordcategory:recordcategories){
                //添加一级分类项目
                if(!level_ru.equals(recordcategory.getLevel_1()) &&  recordcategory.getType()==0){
                    optionsDialog.addPrimaryItem(i,recordcategory.getImageId_1(),recordcategory.getLevel_1());
                    //Log.d("ExpenseProcesActivity","level_1:"+level_1+";recordcategory:"+recordcategory.getLevel_1());
                    level_ru=recordcategory.getLevel_1();
                    i++;
                    j=1;
                }
                if(recordcategory.getType()==0)
                {
                    //添加二级分类项目，其中parentId为归属的一级分类的id
                    optionsDialog.addSecondaryItem(i-1,j,recordcategory.getImageId_2(),recordcategory.getLevel_2());
                    j++;
                }
            }
        }
        else{
            for (RecordCategory recordcategory:recordcategories){
                //添加一级分类项目
                if(!level_chu.equals(recordcategory.getLevel_1()) && recordcategory.getType()==1){
                    optionsDialog.addPrimaryItem(k,recordcategory.getImageId_1(),recordcategory.getLevel_1());
                    //Log.d("ExpenseProcesActivity","level_1:"+level_1+";recordcategory:"+recordcategory.getLevel_1());
                    level_chu=recordcategory.getLevel_1();
                    k++;
                    l=1;
                }
                //添加二级分类项目，其中parentId为归属的一级分类的id
                if(recordcategory.getType()==1){
                    optionsDialog.addSecondaryItem(k-1,l,recordcategory.getImageId_2(),recordcategory.getLevel_2());
                    l++;
                }

            }
        }
        return optionsDialog;
    }

    private void getNoLinkData() {
        //账户
        List<Accounts> accounts = DataSupport.findAll(Accounts.class);
        for (Accounts account : accounts) {
            //accountList=accountList+account.getName()};
            accountList.add(account.getName());
        }
        shopList.add("无商家/地点");
        shopList.add("银行");
        shopList.add("公交");
        shopList.add("饭堂");
        shopList.add("商场");
        shopList.add("超市");
        shopList.add("其他");

        memberList.add("无成员");
        memberList.add("本人");
        memberList.add("丈夫");
        memberList.add("妻子");
        memberList.add("子女");
        memberList.add("父母");
        memberList.add("家庭");

        eventList.add("无项目");
        eventList.add("红包");
        eventList.add("出差");
        eventList.add("报销");
        eventList.add("装修");
        eventList.add("旅游");
        eventList.add("过年");

    }

    private void initNoLinkOptionsPicker(int i) {// 不联动的多级选项
        pvNoLinkOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                if(i==1){
                    txtAccount = accountList.get(options1);
                    account_btn.setText(txtAccount);
                    //Toast.makeText(ExpenseProcesActivity.this, txtAccount, Toast.LENGTH_SHORT).show();
                }
                else if(i==2){
                    txtShop = shopList.get(options1);
                    shop_btn.setText(txtShop);
                    //Toast.makeText(ExpenseProcesActivity.this, txtShop, Toast.LENGTH_SHORT).show();
                }
                else if(i==3){
                    txtMember = memberList.get(options1);
                    member_btn.setText(txtMember);
                    //Toast.makeText(ExpenseProcesActivity.this, txtMember, Toast.LENGTH_SHORT).show();
                }
                else if(i==4){
                    txtEvent = eventList.get(options1);
                    event_btn.setText(txtEvent);
                    //Toast.makeText(ExpenseProcesActivity.this, txtEvent, Toast.LENGTH_SHORT).show();
                }


            }
        })
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1;
                        //Toast.makeText(ExpenseProcesActivity.this, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .setItemVisibleCount(5)
                // .setSelectOptions(0, 1, 1)
                .build();
//        pvNoLinkOptions.setNPicker(accountList,null,null);
//        pvNoLinkOptions.setSelectOptions(0);
        if(i==1){
            pvNoLinkOptions.setTitleText("选择账户");
            pvNoLinkOptions.setNPicker(accountList,null,null);
        }
        else if(i==2){
            pvNoLinkOptions.setTitleText("选择场所");
            pvNoLinkOptions.setNPicker(shopList,null,null);
        }
        else if(i==3){
            pvNoLinkOptions.setTitleText("选择成员");
            pvNoLinkOptions.setNPicker(memberList,null,null);
        }
        else if(i==4){
            pvNoLinkOptions.setTitleText("选择事件");
            pvNoLinkOptions.setNPicker(eventList,null,null);
        }
        pvNoLinkOptions.setSelectOptions(0);
    }
}
