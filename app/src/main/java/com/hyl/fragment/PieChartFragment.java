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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hyl.accountbook.ChartActivity;
import com.hyl.accountbook.R;
import com.hyl.adapter.PieChartRankAdapter;
import com.hyl.bean.AccountsTypeChoose;
import com.hyl.bean.LevelChoose;
import com.hyl.bean.RecordCategory;
import com.hyl.bean.RecordType;
import com.hyl.bean.records;
import com.hyl.util.DateUtils;
import com.hyl.util.FormatUtils;
import com.hyl.util.InitDataUtil;
import com.hyl.util.PieChartUtil;
import com.hyl.view.CircleImageView;

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

public class PieChartFragment extends Fragment implements OnChartValueSelectedListener {

    private static final String TAG = "PieChartFragment";
    @BindView(R.id.pie_chart)
    PieChart mChart;
    @BindView(R.id.pie_center_title)
    TextView centerTitle;
    @BindView(R.id.pie_center_money)
    TextView centerMoney;
    @BindView(R.id.pie_layout_center)
    LinearLayout layoutCenter;
    @BindView(R.id.pie_center_img)
    ImageView centerImg;
    @BindView(R.id.pie_begin_data_year)
    TextView beginDataYear;
    @BindView(R.id.pie_begin_data_month)
    TextView beginDataMonth;
    @BindView(R.id.pie_begin_data_day)
    TextView beginDataDay;
    @BindView(R.id.layout_pie_begin_data)
    LinearLayout layoutBeginData;
    @BindView(R.id.pie_end_data_year)
    TextView endDataYear;
    @BindView(R.id.pie_end_data_month)
    TextView endDataMonth;
    @BindView(R.id.pie_end_data_day)
    TextView endDataDay;
    @BindView(R.id.layout_pie_end_data)
    LinearLayout layoutEndData;
    @BindView(R.id.pie_choose_level)
    TextView chosenLevel;
    @BindView(R.id.pie_circle_bg)
    CircleImageView circleBg;
    @BindView(R.id.pie_circle_img)
    ImageView circleImg;
    @BindView(R.id.pie_layout_circle)
    RelativeLayout layoutCircle;
    @BindView(R.id.pie_detail_title)
    TextView detailTitle;
    @BindView(R.id.pie_detail_money)
    TextView detailMoney;
    @BindView(R.id.pie_swipe)
    SwipeRefreshLayout swipe;
    @BindView(R.id.pie_item_type)
    RelativeLayout itemType;
    @BindView(R.id.pie_detail_list)
    RecyclerView detailList;
    @BindView(R.id.layout_pie_typedata)
    LinearLayout layoutTypedata;

    private Unbinder mUnBinder;


    private int type = 1;//默认总支出
    private List<RecordType.TypeMoney> typeMoneyList;
    private String type_name;


    // 分级选择
    private int level;
    // 起始日期
    private int beginDate;
    private int endDate;
    // 分类选择器
    private OptionsPickerView pvLevelOptions;
    private List<LevelChoose> levelItems = new ArrayList<>();


    private PieChartRankAdapter adapter;
    private List<records> rankList;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.piechart_fragment, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        PieChartUtil.initPieChart(mChart);
        mChart.setOnChartValueSelectedListener(this);

        beginDataYear.setText(DateUtils.getCurYear("yyyy 年"));
        endDataYear.setText(DateUtils.getCurYear("yyyy 年"));
        beginDataMonth.setText(DateUtils.date2Str(new Date(), "MM"));
        endDataMonth.setText(DateUtils.date2Str(new Date(), "MM"));
        beginDataDay.setText(DateUtils.date2Str(new Date(), "dd"));
        endDataDay.setText(DateUtils.date2Str(new Date(), "dd"));

        beginDate = getDate(new Date());
        endDate = getDate(new Date());
        Log.d(TAG, "today is:" + beginDate);

        level = 1;

        // 等数据加载完毕再初始化并显示Picker,以免还未加载完数据就显示,造成APP崩溃
        getOptionData();
        initTypeOptionPicker();

        //设置向下拉多少出现刷新
        swipe.setDistanceToTriggerSync(200);
        //设置刷新出现的位置
        swipe.setProgressViewEndTarget(false, 200);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);
                PieChartUtil.setAnimate(mChart);
            }
        });

        rankList = new ArrayList<>();
        detailList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PieChartRankAdapter(getActivity(), rankList);
        detailList.setAdapter(adapter);

        setReportData();

    }

    private void setReportData() {
        RecordType recordType = null;
        if (type == 1) {
            centerTitle.setText("总支出");
            centerImg.setImageResource(R.mipmap.piechart_output);
            recordType = new RecordType(type, level, beginDate, endDate);
        } else {
            centerTitle.setText("总收入");
            centerImg.setImageResource(R.mipmap.piechart_input);
            recordType = new RecordType(type, level, beginDate, endDate);
        }
        if (recordType == null) {
            return;
        }

        if (type == 1) {
            centerMoney.setText("-" + recordType.getMoney());
        } else {
            centerMoney.setText("+" + recordType.getMoney());
        }
        typeMoneyList = recordType.getTypeMoneyList();

        Log.d(TAG, "=======================================================");
        Log.d(TAG, "money:" + recordType.getMoney());
        Log.d(TAG, "beginDate:" + recordType.getBeginDate());
        Log.d(TAG, "endDate:" + recordType.getEndDate());
        Log.d(TAG, "level:" + recordType.getLevel());
        Log.d(TAG, "listSize:" + recordType.getTypeMoneyList().size());

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        ArrayList<Integer> colors = InitDataUtil.getChartColor(type);

        if (typeMoneyList != null && typeMoneyList.size() > 0) {
            layoutTypedata.setVisibility(View.VISIBLE);
            for (int i = 0; i < typeMoneyList.size(); i++) {
                float scale = typeMoneyList.get(i).getMoneySum() / recordType.getMoney();
                float value = (scale < 0.06f) ? 0.06f : scale;
                Log.d(TAG, "Try to get picture of:" + typeMoneyList.get(i).getType());
                entries.add(new PieEntry(value, PieChartUtil.getTypeDrawable(typeMoneyList.get(i).getType(), level)));
            }
            setNoteData(0);
//            String back_color = (type == 1) ? "#eaf4fc" : "#f8f4e6";
//            circleBg.setImageDrawable(new ColorDrawable(Color.parseColor(back_color)));
        } else {//无数据时的显示
            layoutTypedata.setVisibility(View.GONE);
            entries.add(new PieEntry(1f));
            colors.clear();
            colors.add(0xffAAAAAA);
        }

        PieChartUtil.setPieChartData(mChart, entries, colors);
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
        if (type == 1) {
            detailMoney.setText("-"+ getMoneyStr(typeMoneyList.get(index).getMoneySum()));
        }else {
            detailMoney.setText("+"+ getMoneyStr(typeMoneyList.get(index).getMoneySum()));
        }

        circleImg.setImageDrawable(PieChartUtil.getTypeDrawable(type_name, level));

        if (level == 1) {
            detailTitle.setText(type_name);
            Log.d(TAG, "touch: (level 1) " + type_name);
            rankList = DataSupport.where("catagory1 = ? and intdate >= ? and intdate <= ?", type_name, ""+beginDate, ""+endDate).order("amount desc").find(records.class);
        }
        else {
            String category1 = DataSupport.where("level_2 = ?", type_name).findFirst(RecordCategory.class).getLevel_1();
            detailTitle.setText(category1 + "-" + type_name);
            Log.d(TAG, "touch: (level 2) " + category1 + "-" + type_name);
            rankList = DataSupport.where("catagory2 = ? and intdate >= ? and intdate <= ?", type_name, ""+beginDate, ""+endDate).order("amount desc").find(records.class);
        }
        adapter = new PieChartRankAdapter(getActivity(), rankList);
        adapter.notifyDataSetChanged();
        detailList.setAdapter(adapter);

    }



    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        int entryIndex = (int) h.getX();
        PieChartUtil.setRotationAngle(mChart, entryIndex);
        setNoteData(entryIndex);
    }


    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }


    @OnClick({R.id.pie_layout_center, R.id.layout_pie_begin_data, R.id.layout_pie_end_data, R.id.pie_choose_level})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pie_layout_center:

                if(type == 1){
                    type = 0;
                }else{
                    type = 1;
                }
                setReportData();

                break;
            case R.id.layout_pie_begin_data:
                //时间选择器
                new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        beginDataDay.setText(DateUtils.date2Str(date, "yyyy 年"));
                        beginDataMonth.setText(DateUtils.date2Str(date, "MM"));
                        beginDataDay.setText(DateUtils.date2Str(date, "dd"));
                        beginDate = getDate(date);
                        setReportData();
                        PieChartUtil.setAnimate(mChart);
                    }
                })
                        .setRangDate(null, Calendar.getInstance())
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .build()
                        .show();
                break;
            case R.id.layout_pie_end_data:
                //时间选择器
                new TimePickerBuilder(getActivity(), new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {//选中事件回调
                        endDataDay.setText(DateUtils.date2Str(date, "yyyy 年"));
                        endDataMonth.setText(DateUtils.date2Str(date, "MM"));
                        endDataDay.setText(DateUtils.date2Str(date, "dd"));
                        endDate = getDate(date);
                        setReportData();
                        PieChartUtil.setAnimate(mChart);
                    }
                })
                        .setRangDate(null, Calendar.getInstance())
                        .setType(new boolean[]{true, true, true, false, false, false})
                        .build()
                        .show();
                break;
            case R.id.pie_choose_level:
                pvLevelOptions.show();
                break;
        }
    }


    private String getMoneyStr(double money) {
        return FormatUtils.MyDecimalFormat("##,###,###.##", money);
    }


    private void initTypeOptionPicker() {//条件选择器初始化，自定义布局
        /**
         * @description
         *
         * 注意事项：
         * 自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针。
         * 具体可参考demo 里面的两个自定义layout布局。
         */
        pvLevelOptions = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = levelItems.get(options1).getPickerViewText();
                chosenLevel.setText(tx);
                if (chosenLevel.getText().toString() == "一级分类") {
                    level = 1;
                }
                else if (chosenLevel.getText().toString() == "二级分类") {
                    level = 2;
                }
                setReportData();
                PieChartUtil.setAnimate(mChart);
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
                                pvLevelOptions.returnData();
                                pvLevelOptions.dismiss();
                            }
                        });

                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvLevelOptions.dismiss();
                            }
                        });
                    }
                })
                .isDialog(true)
                .setOutSideCancelable(false)
                .build();

        pvLevelOptions.setPicker(levelItems);//添加数据
    }

    private void getOptionData() {
        levelItems.add(new LevelChoose(0, "一级分类"));
        levelItems.add(new LevelChoose(1, "二级分类"));
    }

    private int getDate(Date date) {
        String dateString = DateUtils.date2Str(date, "yyyy-MM-dd");
        String regEx="[-]";
        dateString = dateString.replaceAll(regEx,"");
        return Integer.parseInt(dateString);
    }

}

