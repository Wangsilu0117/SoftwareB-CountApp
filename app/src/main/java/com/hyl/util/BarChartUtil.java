package com.hyl.util;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.hyl.bean.RecordCategory;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;

/**
 * 记账本条形图
 */

public class BarChartUtil {

    /**
     * 初始化条形图
     * @param mChart
     */
    public static void initBarChart(BarChart mChart){

        // 设置是否可以触摸
        mChart.setTouchEnabled(true);
        // 是否可以拖拽
        mChart.setDragEnabled(true);
        // 是否可以缩放
        mChart.setScaleEnabled(true);
        // 集双指缩放
        mChart.setPinchZoom(true);
        // 显示数据
        mChart.setDrawValueAboveBar(true);

        XAxis xAxis = mChart.getXAxis();
        //取消x轴横线
        xAxis.setDrawAxisLine(false);
        //取消x轴竖线
        xAxis.setDrawGridLines(false);
        //设置x轴位于底部
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //显示标签数
        xAxis.setLabelCount(7, false);



        //在定义y轴的时候,需要两边都进行设置，而不是直接setEnabled禁用。否则将产生数据偶尔不显示的问题
        YAxis left = mChart.getAxisLeft();
        left.setAxisMinimum(0);
        left.setDrawGridLines(false);
        left.setDrawAxisLine(false);
        left.setEnabled(true);

        YAxis right = mChart.getAxisRight();
        right.setDrawGridLines(false);
        right.setDrawAxisLine(true);
        right.setEnabled(false);


        //禁用描述
        mChart.getDescription().setEnabled(false);
        //禁用图例
//        mChart.getLegend().setEnabled(false);


        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);

        mChart.setHighlightPerTapEnabled(true);
        Legend l = mChart.getLegend();
        l.setEnabled(true);
    }





    /**
     * 设置条形图数据
     * @param mChart
     * @param entriesIn
     * @param entriesOut
     * @param colorsIn
     * @param colorsOut
     */
    public static void setBarChartData(BarChart mChart, ArrayList<BarEntry> entriesIn, ArrayList<BarEntry> entriesOut, Integer colorsIn, Integer colorsOut, List<String> xList){

        float groupSpace = 0.1f;
        float barSpace = 0.05f; // x4 DataSet
        float barWidth = 0.4f; // x4 DataSet
        //关键： (0.4 + 0.06) * 2 + 0.08 = 1.00 -> interval per "group" 一定要等于1,乘以2是表示每组有两个数据


        XAxis xAxis = mChart.getXAxis();
        // x轴最大长度 (注意自定义标签时，它与BarEntry(x,y),与x的关系)
        xAxis.setAxisMaximum(xList.size());
        xAxis.setLabelCount(xList.size());
        xAxis.setAxisMinimum(0f);
        xAxis.setCenterAxisLabels(true);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return  (value >= xList.size() || value <0) ? "" : xList.get((int) value);
            }
        });

        BarDataSet dataSetIn = new BarDataSet(entriesIn, "收入");
        BarDataSet dataSetOut = new BarDataSet(entriesOut, "支出");


//        dataSetIn.setDrawIcons(true);
//        dataSetIn.setSliceSpace(3f);
//        dataSetIn.setIconsOffset(new MPPointF(0, 1));
//        dataSetIn.setSelectionShift(5f);

        dataSetIn.setColors(colorsIn);
        dataSetOut.setColors(colorsOut);
        //dataSet.setSelectionShift(0f);


        BarData data = new BarData(dataSetIn, dataSetOut);
        data.setValueTextSize(11f);
//        data.setValueTextColor(Color.TRANSPARENT);
//        data.setValueTypeface(mTfLight);
        data.setBarWidth(barWidth);
        mChart.setData(data);
        // undo all highlights
        mChart.highlightValues(null);
        mChart.groupBars(0, groupSpace, barSpace);
        setAnimate(mChart);
    }



    /**
     * 设置初始旋转动画
     * @param mChart
     */
    public static void setAnimate(BarChart mChart){
        //--------------设置动画----------------------
        mChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
        mChart.invalidate();
    }



    /**
     * 根据服务器返回的type设置与之相对应的本地图片
     * @param type
     * @return
     */
    public static Drawable getTypeDrawable(String type){
        Drawable drawable = null;
        RecordCategory recordCategory = DataSupport.where("level_2 = ?", type).findFirst(RecordCategory.class);
        drawable = ResourcesCompat.getDrawable(getContext().getResources(), recordCategory.getImageId_2(), null);
        return drawable;
    }


}
