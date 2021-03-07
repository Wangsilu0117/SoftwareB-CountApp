package com.hyl.accountbook;

import android.util.Log;

import com.hyl.bean.records;

import org.litepal.crud.DataSupport;

import java.math.BigDecimal;
import java.util.List;

import java.text.DecimalFormat;

import static android.content.ContentValues.TAG;

public class calculateTotalAmount {
    public double calculateTotalAmount(int year, int month, int day, String account){
        Log.d(TAG, "calc: begin query");
        String s_year = String.valueOf(year);//将year的类型转换为string
//        String s_month = String.valueOf(month);
//        String s_day = String.valueOf(day);
        String s_month = "";
        String s_day = "";
        if(month < 10){
            s_month = "0"+month;
        }else{
            s_month = String.valueOf(month);
        }
        if(day < 10){
            s_day = "0"+day;
        }else{
            s_day = String.valueOf(day);
        }
        float amount = 0;
        if (account.equals("所有账户")){
            if(month == 0){
//                amount = DataSupport.where("year =?", s_year).sum(records.class, "amount", float.class);
                amount = DataSupport.where("substr(date,1,4) =?", s_year).sum(records.class, "amount", float.class);
            }else if(day == 0){
//                amount = DataSupport.where("year = ? and month = ?", s_year,s_month).sum(records.class, "amount", float.class);
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ?", s_year,s_month).sum(records.class, "amount", float.class);
            }else{
//                amount = DataSupport.where("year = ? and month = ? and day = ?", s_year, s_month, s_day).sum(records.class, "amount", float.class);
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ?", s_year, s_month, s_day).sum(records.class, "amount", float.class);
            }
        }else{
            if(month == 0){
//                amount = DataSupport.where("year =? and account = ?", s_year, account).sum(records.class, "amount", float.class);
                amount = DataSupport.where("substr(date,1,4) =? and account = ?", s_year, account).sum(records.class, "amount", float.class);
            }else if(day == 0){
//                amount = DataSupport.where("year = ? and month = ? and account = ?", s_year,s_month, account).sum(records.class, "amount", float.class);
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and account = ?", s_year,s_month, account).sum(records.class, "amount", float.class);
            }else{
//                amount = DataSupport.where("year = ? and month = ? and day = ? and account = ?", s_year, s_month, s_day, account).sum(records.class, "amount", float.class);
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and account = ?", s_year, s_month, s_day, account).sum(records.class, "amount", float.class);
            }
        }
        Log.d(TAG, "calculateTotalAmount: "+amount);
        return amount;
    }
}
