package com.hyl.util;

import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.core.content.res.ResourcesCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.hyl.accountbook.R;
import com.hyl.bean.Accounts;
import com.hyl.bean.RecordCategory;


import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static org.litepal.LitePalApplication.getContext;

/**
 * 记账本饼状图
 */

public class PieChartUtil {

    /**
     * 初始化饼状图
     * @param mChart
     */
    public static void initPieChart(PieChart mChart){
        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);
        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);
        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);
        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);
        mChart.setDrawCenterText(true);
//        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(false);
        mChart.setHighlightPerTapEnabled(true);
        // add a selection listener
        // mChart.spin(2000, 0, 360);
        Legend l = mChart.getLegend();
        l.setEnabled(false);
    }





    /**
     * 设置饼状图数据
     * @param mChart
     * @param entries
     * @param colors
     */
    public static void setPieChartData(PieChart mChart, ArrayList<PieEntry> entries, ArrayList<Integer> colors){

        PieDataSet dataSet = new PieDataSet(entries, "Election Results");

        dataSet.setDrawIcons(true);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 1));
        dataSet.setSelectionShift(5f);

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.TRANSPARENT);
//        data.setValueTypeface(mTfLight);
        mChart.setData(data);
        // undo all highlights
        mChart.highlightValues(null);


        setStartAngle(mChart);
        setAnimate(mChart);
    }



    /**
     * 设置初始角度
     * @param mChart
     */
    public static void setStartAngle(PieChart mChart){
        //--------------设置初始角度----------------------
        float[] mDrawAngles = mChart.getDrawAngles();
        float offset = mDrawAngles[0] / 2;
        mChart.setRotationAngle(90 - offset);
    }



    /**
     * 设置初始旋转动画
     * @param mChart
     */
    public static void setAnimate(PieChart mChart){
        //--------------设置动画----------------------
        mChart.animateY(1000, Easing.EasingOption.EaseInOutQuad);
        mChart.invalidate();
    }




    /**
     * 设置点击相应区域旋转角度
     * @param mChart
     * @param entryIndex
     */
    public static void setRotationAngle(PieChart mChart, int entryIndex){

        float[] mDrawAngles = mChart.getDrawAngles();

//        float offset = mDrawAngles[entryIndex] / 2;
//        Log.e("TAG",
//                "offset=============" + offset);
        Log.e("TAG",
                "rotationAngle==========" + mChart.getRotationAngle());

//--------------初始角度----------------------
        float inAngle = 90 - mDrawAngles[0] / 2;

        switch (entryIndex){
            case 0:
                mChart.setRotationAngle(inAngle);
                break;
            case 1:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] / 2));
                break;
            case 2:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] / 2));
                break;
            case 3:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] / 2));
                break;
            case 4:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] + mDrawAngles[4] / 2));
                break;
            case 5:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] + mDrawAngles[4] + mDrawAngles[5] / 2));
                break;
            case 6:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] + mDrawAngles[4] + mDrawAngles[5] + mDrawAngles[6] / 2));
                break;
            case 7:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] + mDrawAngles[4] + mDrawAngles[5] + mDrawAngles[6] + mDrawAngles[7] / 2));
                break;
            case 8:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] + mDrawAngles[4] + mDrawAngles[5] + mDrawAngles[6] + mDrawAngles[7] + mDrawAngles[8] / 2));
                break;
            case 9:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] + mDrawAngles[4] + mDrawAngles[5] + mDrawAngles[6] + mDrawAngles[7] + mDrawAngles[8] + mDrawAngles[9] / 2));
                break;
            case 10:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] + mDrawAngles[4] + mDrawAngles[5] + mDrawAngles[6] + mDrawAngles[7] + mDrawAngles[8] + mDrawAngles[9] + mDrawAngles[10] / 2));
                break;
            case 11:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] + mDrawAngles[4] + mDrawAngles[5] + mDrawAngles[6] + mDrawAngles[7] + mDrawAngles[8] + mDrawAngles[9] + mDrawAngles[10] + mDrawAngles[11] / 2));
                break;
            case 12:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] + mDrawAngles[4] + mDrawAngles[5] + mDrawAngles[6] + mDrawAngles[7] + mDrawAngles[8] + mDrawAngles[9] + mDrawAngles[10] + mDrawAngles[11] + mDrawAngles[12] / 2));
                break;
            case 13:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] + mDrawAngles[4] + mDrawAngles[5] + mDrawAngles[6] + mDrawAngles[7] + mDrawAngles[8] + mDrawAngles[9] + mDrawAngles[10] + mDrawAngles[11] + mDrawAngles[12] + mDrawAngles[13] / 2));
                break;
            case 14:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] + mDrawAngles[4] + mDrawAngles[5] + mDrawAngles[6] + mDrawAngles[7] + mDrawAngles[8] + mDrawAngles[9] + mDrawAngles[10] + mDrawAngles[11] + mDrawAngles[12] + mDrawAngles[13] + mDrawAngles[14] / 2));
                break;
            case 15:
                mChart.setRotationAngle(inAngle - (mDrawAngles[0] / 2 + mDrawAngles[1] + mDrawAngles[2] + mDrawAngles[3] + mDrawAngles[4] + mDrawAngles[5] + mDrawAngles[6] + mDrawAngles[7] + mDrawAngles[8] + mDrawAngles[9] + mDrawAngles[10] + mDrawAngles[11] + mDrawAngles[12] + mDrawAngles[13] + mDrawAngles[14] + mDrawAngles[15] / 2));
                break;
            default:
                break;
        }

        mChart.invalidate();
    }


    /**
     * 根据服务器返回的type设置与之相对应的本地图片
     * @param type
     * @return
     */
    public static Drawable getTypeDrawable(String type, int level){
        Drawable drawable = null;
        if (level == 1) {
            RecordCategory recordCategory = DataSupport.where("level_1 = ?", type).findFirst(RecordCategory.class);
            if (recordCategory.getLevel_1().equals("")) {
                drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.qitashouru, null);
            }
            else {
                drawable = ResourcesCompat.getDrawable(getContext().getResources(), recordCategory.getImageId_1(), null);
            }
        }
        else {
            RecordCategory recordCategory = DataSupport.where("level_2 = ?", type).findFirst(RecordCategory.class);
            if (recordCategory.getLevel_2().equals("")) {
                drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.qitashouru, null);
            }
            else {
                drawable = ResourcesCompat.getDrawable(getContext().getResources(), recordCategory.getImageId_2(), null);
            }
        }
        return drawable;
    }


}
