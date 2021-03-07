package com.hyl.accountbook;

import android.provider.ContactsContract;
import android.util.Log;

import com.hyl.bean.records;

import org.litepal.crud.DataSupport;

import java.util.List;

import static android.content.ContentValues.TAG;

public class Flow {
    public List<records> flow(int year, int month, int day, String account, String saler, String item){
        Log.d(TAG, "flow: begin query");
        float amount = 0;
        String s_year = String.valueOf(year);//将year的类型转换为string
//        String s_month = String.valueOf(month);
//        String s_day = String.valueOf(day);
        //确保小于10的月份或日期存储格式正确
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
        List<records> records_selected;
        List<records> amounts;//记录查询到的每一笔钱的数额
        if (account.equals("所有账户") && item.equals("所有商品") && saler.equals("所有商家")){
            Log.d(TAG, "calculateTotalExpense: three all");
            if(month == 0){
                records_selected = DataSupport.where("substr(date,1,4) =?", s_year).order("date asc").find(records.class);
            }else if(day == 0){
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ?", s_year,s_month).order("date asc").find(records.class);
            }else{
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ?", s_year, s_month, s_day).order("date asc").find(records.class);
            }
        }else if(account.equals("所有账户") && item.equals("所有商品") && !saler.equals("所有商家")){
            if(month == 0){
                records_selected = DataSupport.where("substr(date,1,4) =? and saler = ?", s_year, saler).order("date asc").find(records.class);
            }else if(day == 0){
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and saler = ?", s_year,s_month, saler).order("date asc").find(records.class);
            }else{
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and saler = ?", s_year, s_month, s_day, saler).order("date asc").find(records.class);
            }
        }else if(account.equals("所有账户") && !item.equals("所有商品") && saler.equals("所有商家")){
            if(month == 0){
                records_selected = DataSupport.where("substr(date,1,4) =? and item = ?", s_year, item).order("date asc").find(records.class);
            }else if(day == 0){
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and item = ?", s_year,s_month, item).order("date asc").find(records.class);
            }else{
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and item = ?", s_year, s_month, s_day, item).order("date asc").find(records.class);
            }
        }else if(!account.equals("所有账户") && item.equals("所有商品") && saler.equals("所有商家")){
            if(month == 0){
                records_selected = DataSupport.where("substr(date,1,4) =? and account = ?", s_year, account).order("date asc").find(records.class);
            }else if(day == 0){
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and account = ?", s_year,s_month, account).order("date asc").find(records.class);
            }else{
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and account = ?", s_year, s_month, s_day, account).order("date asc").find(records.class);
            }
        }else if (account.equals("所有账户")){
            Log.d(TAG, "calculateTotalExpense: all account");
            if(month == 0){
                records_selected = DataSupport.where("substr(date,1,4) =? and saler = ? and item = ?", s_year, saler, item).order("date asc").find(records.class);
            }else if(day == 0){
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and saler = ? and item = ?", s_year,s_month, saler, item).order("date asc").find(records.class);
            }else{
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and saler = ? and item = ?", s_year, s_month, s_day, saler, item).order("date asc").find(records.class);
            }
        }else if (saler.equals("所有商家")){
            Log.d(TAG, "calculateTotalExpense: all saler");
            if(month == 0){
                records_selected = DataSupport.where("substr(date,1,4) =? and account = ? and item = ?", s_year, account, item).order("date asc").find(records.class);
            }else if(day == 0){
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and account = ? and item = ?", s_year,s_month, account, item).order("date asc").find(records.class);
            }else{
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and account = ? and item = ?", s_year, s_month, s_day, account, item).order("date asc").find(records.class);
            }
        }else if (item.equals("所有商品")){
            Log.d(TAG, "calculateTotalExpense: all item");
            if(month == 0){
                records_selected = DataSupport.where("substr(date,1,4) =? and account = ? and saler = ?", s_year, account, saler).order("date asc").find(records.class);
            }else if(day == 0){
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and account = ? and saler = ?", s_year,s_month, account, saler).order("date asc").find(records.class);
            }else{
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and account = ? and saler = ?", s_year, s_month, s_day, account, saler).order("date asc").find(records.class);
            }
        }else{
            Log.d(TAG, "calculateTotalExpense: else");
            if(month == 0){
                records_selected = DataSupport.where("substr(date,1,4) =? and account = ? and saler = ? and item = ?", s_year, account, saler, item).order("date asc").find(records.class);
            }else if(day == 0){
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and account = ? and saler = ? and item = ?", s_year,s_month, account, saler, item).order("date asc").find(records.class);
            }else{
                records_selected = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and account = ? and saler = ? and item = ?", s_year, s_month, s_day, account, saler, item).order("date asc").find(records.class);
            }
        }


        Log.d(TAG, "flow: flow finish!");
        Log.d(TAG, "flow: " + records_selected);
//        System.out.print("flow finish!");
//        System.out.print(records_selected);
        return records_selected;
    }
}

