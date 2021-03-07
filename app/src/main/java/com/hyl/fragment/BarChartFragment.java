package com.hyl.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hyl.accountbook.R;
import com.hyl.adapter.BarChartRankAdapter;
import com.hyl.bean.AccountsTypeChoose;
import com.hyl.bean.LevelChoose;
import com.hyl.bean.RecordType;
import com.hyl.bean.records;
import com.hyl.util.BarChartUtil;
import com.hyl.util.DateUtils;
import com.hyl.util.FormatUtils;
import com.hyl.util.InitDataUtil;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.hyl.util.InitDataUtil.getAccountsType;

public class BarChartFragment extends Fragment implements OnChartValueSelectedListener {

    private static final String TAG = "BarChartFragment";
    @BindView(R.id.bar_chart)
    BarChart mChart;
    @BindView(R.id.bar_begin_data_year)
    TextView beginDataYear;
    @BindView(R.id.bar_begin_data_month)
    TextView beginDataMonth;
    @BindView(R.id.bar_begin_data_day)
    TextView beginDataDay;
    @BindView(R.id.layout_bar_begin_data)
    LinearLayout layoutBeginData;
    @BindView(R.id.bar_end_data_year)
    TextView endDataYear;
    @BindView(R.id.bar_end_data_month)
    TextView endDataMonth;
    @BindView(R.id.bar_end_data_day)
    TextView endDataDay;
    @BindView(R.id.layout_bar_end_data)
    LinearLayout layoutEndData;
    @BindView(R.id.bar_by_member)
    ImageView byMember;
    @BindView(R.id.bar_by_member_tv)
    TextView byMember_tv;
    @BindView(R.id.bar_by_saler)
    ImageView bySaler;
    @BindView(R.id.bar_by_saler_tv)
    TextView bySaler_tv;
    @BindView(R.id.bar_by_item)
    ImageView byItem;
    @BindView(R.id.bar_by_item_tv)
    TextView byItem_tv;
    @BindView(R.id.bar_by_accounts)
    ImageView byAccounts;
    @BindView(R.id.bar_by_accounts_tv)
    TextView byAccounts_tv;
    @BindView(R.id.bar_detail_title)
    TextView detailTitle;
    @BindView(R.id.bar_detail_in_money)
    TextView detailInMoney;
    @BindView(R.id.bar_detail_out_money)
    TextView detailOutMoney;
    @BindView(R.id.bar_swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.bar_item_type)
    RelativeLayout itemType;
    @BindView(R.id.bar_detail_list)
    RecyclerView detailList;
    @BindView(R.id.layout_bar_typedata)
    LinearLayout layoutTypedata;

    private Unbinder mUnBinder;


    private int select = 0; // 默认商家分类
    private List<RecordType.TypeMoney> typeMoneyList;
    private String type_name;


    // 起始日期
    private int beginDate;
    private int endDate;
    // 分类选择器
    private OptionsPickerView pvLevelOptions;
    private List<LevelChoose> levelItems = new ArrayList<>();


    private BarChartRankAdapter adapter;
    private List<records> rankList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.barchart_fragment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        BarChartUtil.initBarChart(mChart);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawValueAboveBar(true);
        mChart.setDrawBarShadow(false);

        beginDataYear.setText(DateUtils.getCurYear("yyyy 年"));
        endDataYear.setText(DateUtils.getCurYear("yyyy 年"));
        beginDataMonth.setText(DateUtils.date2Str(new Date(), "MM"));
        endDataMonth.setText(DateUtils.date2Str(new Date(), "MM"));
        beginDataDay.setText(DateUtils.date2Str(new Date(), "dd"));
        endDataDay.setText(DateUtils.date2Str(new Date(), "dd"));

        beginDate = getDate(new Date());
        endDate = getDate(new Date());
        Log.d(TAG, "today is:" + beginDate);


        //设置向下拉多少出现刷新
        swipe.setDistanceToTriggerSync(200);
        //设置刷新出现的位置
        swipe.setProgressViewEndTarget(false, 200);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                BarChartUtil.setAnimate(mChart);
            }
        });

        rankList = new ArrayList<>();
        detailList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new BarChartRankAdapter(getActivity(), rankList, Color.parseColor("#89c3eb"), Color.parseColor("#e198b4"));
        detailList.setAdapter(adapter);

        bySaler.setColorFilter(Color.parseColor("#0c92d4"));
        bySaler_tv.setTextColor(Color.parseColor("#0c92d4"));
        byMember.setColorFilter(Color.parseColor("#666666"));
        byMember_tv.setTextColor(Color.parseColor("#666666"));
        byItem.setColorFilter(Color.parseColor("#666666"));
        byItem_tv.setTextColor(Color.parseColor("#666666"));
        byAccounts.setColorFilter(Color.parseColor("#666666"));
        byAccounts_tv.setTextColor(Color.parseColor("#666666"));
        setReportData(select);

    }

    private void setReportData(int select) {
        RecordType recordType = null;
        if (select == 0) {
            recordType = new RecordType(3, 3, beginDate, endDate);
        } else if (select == 1) {
            recordType = new RecordType(4, 3, beginDate, endDate);
        } else if (select == 2) {
            recordType = new RecordType(5, 3, beginDate, endDate);
        } else {
            recordType = new RecordType(6, 3, beginDate, endDate);
        }
        if (recordType == null) {
            return;
        }

        typeMoneyList = recordType.getTypeMoneyList();

        List<String> xList = new ArrayList<>();
        for (RecordType.TypeMoney typeMoney : typeMoneyList) {
            if (!xList.contains(typeMoney.getType())) {
                xList.add(typeMoney.getType());
            }
        }

        Log.d(TAG, "=======================================================");
        Log.d(TAG, "money:" + recordType.getMoney());
        Log.d(TAG, "beginDate:" + recordType.getBeginDate());
        Log.d(TAG, "endDate:" + recordType.getEndDate());
        Log.d(TAG, "level:" + recordType.getLevel());
        Log.d(TAG, "listSize:" + recordType.getTypeMoneyList().size());

        ArrayList<BarEntry> entriesIn = new ArrayList<BarEntry>();
        ArrayList<BarEntry> entriesOut = new ArrayList<BarEntry>();
        Integer colorsIn = Color.parseColor("#89c3eb");
        Integer colorsOut = Color.parseColor("#e198b4");


        if (typeMoneyList != null && typeMoneyList.size() > 0) {
            layoutTypedata.setVisibility(View.VISIBLE);
            for (int i = 0; i < typeMoneyList.size(); i++) {
                float valueIn = typeMoneyList.get(i).getMoneyIn();
                float valueOut = typeMoneyList.get(i).getMoneyOut();
                entriesIn.add(new BarEntry(i, valueIn));
                entriesOut.add(new BarEntry(i, valueOut));
            }
            setNoteData(0);
        } else {//无数据时的显示
            layoutTypedata.setVisibility(View.GONE);
            entriesIn.add(new BarEntry(0, 0));
            colorsIn = 0xffAAAAAA;
            colorsOut = 0xffAAAAAA;
        }

        BarChartUtil.setBarChartData(mChart, entriesIn, entriesOut, colorsIn, colorsOut, xList);
    }

    /**
     * 点击饼状图上区域后相应的数据设置
     * @param index
     */
    private void setNoteData(int index){
        if (typeMoneyList.isEmpty()) {
            return;
        }
        type_name = typeMoneyList.get(index).getType();
        detailInMoney.setText("-"+ getMoneyStr(typeMoneyList.get(index).getMoneyIn()));
        detailOutMoney.setText("+"+ getMoneyStr(typeMoneyList.get(index).getMoneyOut()));
        detailTitle.setText(type_name);

        if (select == 0) {
            rankList = DataSupport.where("saler = ? and intdate >= ? and intdate <= ? and type < ?", type_name, ""+beginDate, ""+endDate, ""+2).order("amount desc").find(records.class);
        }
        else if (select == 1) {
            rankList = DataSupport.where("member = ? and intdate >= ? and intdate <= ? and type < ?", type_name, ""+beginDate, ""+endDate, ""+2).order("amount desc").find(records.class);
        } else if (select == 2){
            rankList = DataSupport.where("item = ? and intdate >= ? and intdate <= ? and type < ?", type_name, ""+beginDate, ""+endDate, ""+2).order("amount desc").find(records.class);
        } else {
            rankList = DataSupport.where("account = ? and intdate >= ? and intdate <= ? and type < ? ", type_name, ""+beginDate, ""+endDate, ""+2).order("amount desc").find(records.class);
        }
        Integer colorIn = Color.parseColor("#89c3eb");
        Integer colorOut = Color.parseColor("#e198b4");
        adapter = new BarChartRankAdapter(getActivity(), rankList, colorIn, colorOut);
        adapter.notifyDataSetChanged();
        detailList.setAdapter(adapter);

    }



    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        int entryIndex = (int) h.getX();
        setNoteData(entryIndex);
    }


    @Override
    public void onNothingSelected() {
        Log.i("BarChartFragment", "nothing selected");
    }


    @OnClick({R.id.layout_bar_begin_data, R.id.layout_bar_end_data, R.id.bar_by_saler, R.id.bar_by_member, R.id.bar_by_item, R.id.bar_by_accounts})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_bar_begin_data:
                //时间选择器
                new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        beginDataDay.setText(DateUtils.date2Str(date, "yyyy 年"));
                        beginDataMonth.setText(DateUtils.date2Str(date, "MM"));
                        beginDataDay.setText(DateUtils.date2Str(date, "dd"));
                        beginDate = getDate(date);
                        setReportData(select);
                        BarChartUtil.setAnimate(mChart);
                    }
                })
                        .setRangDate(null, Calendar.getInstance())
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .build()
                        .show();
                break;
            case R.id.layout_bar_end_data:
                //时间选择器
                new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        endDataDay.setText(DateUtils.date2Str(date, "yyyy 年"));
                        endDataMonth.setText(DateUtils.date2Str(date, "MM"));
                        endDataDay.setText(DateUtils.date2Str(date, "dd"));
                        endDate = getDate(date);
                        setReportData(select);
                        BarChartUtil.setAnimate(mChart);
                    }
                })
                        .setRangDate(null, Calendar.getInstance())
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .build()
                        .show();
                break;
            case R.id.bar_by_saler:
                select = 0;
                bySaler.setColorFilter(Color.parseColor("#0c92d4"));
                bySaler_tv.setTextColor(Color.parseColor("#0c92d4"));
                byMember.setColorFilter(Color.parseColor("#666666"));
                byMember_tv.setTextColor(Color.parseColor("#666666"));
                byItem.setColorFilter(Color.parseColor("#666666"));
                byItem_tv.setTextColor(Color.parseColor("#666666"));
                byAccounts.setColorFilter(Color.parseColor("#666666"));
                byAccounts_tv.setTextColor(Color.parseColor("#666666"));
                setReportData(select);
                BarChartUtil.setAnimate(mChart);
                break;
            case R.id.bar_by_member:
                select = 1;
                bySaler.setColorFilter(Color.parseColor("#666666"));
                bySaler_tv.setTextColor(Color.parseColor("#666666"));
                byMember.setColorFilter(Color.parseColor("#0c92d4"));
                byMember_tv.setTextColor(Color.parseColor("#0c92d4"));
                byItem.setColorFilter(Color.parseColor("#666666"));
                byItem_tv.setTextColor(Color.parseColor("#666666"));
                byAccounts.setColorFilter(Color.parseColor("#666666"));
                byAccounts_tv.setTextColor(Color.parseColor("#666666"));
                setReportData(select);
                BarChartUtil.setAnimate(mChart);
                break;
            case R.id.bar_by_item:
                select = 2;
                bySaler.setColorFilter(Color.parseColor("#666666"));
                bySaler_tv.setTextColor(Color.parseColor("#666666"));
                byMember.setColorFilter(Color.parseColor("#666666"));
                byMember_tv.setTextColor(Color.parseColor("#666666"));
                byItem.setColorFilter(Color.parseColor("#0c92d4"));
                byItem_tv.setTextColor(Color.parseColor("#0c92d4"));
                byAccounts.setColorFilter(Color.parseColor("#666666"));
                byAccounts_tv.setTextColor(Color.parseColor("#666666"));
                setReportData(select);
                BarChartUtil.setAnimate(mChart);
                break;
            case R.id.bar_by_accounts:
                select = 3;
                bySaler.setColorFilter(Color.parseColor("#666666"));
                bySaler_tv.setTextColor(Color.parseColor("#666666"));
                byMember.setColorFilter(Color.parseColor("#666666"));
                byMember_tv.setTextColor(Color.parseColor("#666666"));
                byItem.setColorFilter(Color.parseColor("#666666"));
                byItem_tv.setTextColor(Color.parseColor("#666666"));
                byAccounts.setColorFilter(Color.parseColor("#0c92d4"));
                byAccounts_tv.setTextColor(Color.parseColor("#0c92d4"));
                setReportData(select);
                BarChartUtil.setAnimate(mChart);
                break;
        }
    }


    private String getMoneyStr(float money) {
        return FormatUtils.MyDecimalFormat("##,###,###.##", money);
    }



    private int getDate(Date date) {
        String dateString = DateUtils.date2Str(date, "yyyy-MM-dd");
        String regEx="[-]";
        dateString = dateString.replaceAll(regEx,"");
        return Integer.parseInt(dateString);
    }

}

