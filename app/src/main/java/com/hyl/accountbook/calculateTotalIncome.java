package com.hyl.accountbook;

import android.util.Log;

import com.hyl.bean.records;

import org.litepal.crud.DataSupport;

import static android.content.ContentValues.TAG;

public class calculateTotalIncome {
    public double calculateTotalIncome(int year, int month, int day, String account, String saler, String item){
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
        if (account.equals("所有账户") && item.equals("所有商品") && saler.equals("所有商家")){
            Log.d(TAG, "calculateTotalExpense: three all");
            if(month == 0){
                amount = DataSupport.where("substr(date,1,4) =? and type = 0", s_year).sum(records.class, "amount", float.class);
            }else if(day == 0){
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and type = 0", s_year,s_month).sum(records.class, "amount", float.class);
            }else{
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and type = 0", s_year, s_month, s_day).sum(records.class, "amount", float.class);
            }
        }else if(account.equals("所有账户") && item.equals("所有商品") && !saler.equals("所有商家")){
            if(month == 0){
                amount = DataSupport.where("substr(date,1,4) =? and type = 0 and saler = ?", s_year, saler).sum(records.class, "amount", float.class);
            }else if(day == 0){
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and type = 0 and saler = ?", s_year,s_month, saler).sum(records.class, "amount", float.class);
            }else{
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and type = 0 and saler = ?", s_year, s_month, s_day, saler).sum(records.class, "amount", float.class);
            }
        }else if(account.equals("所有账户") && !item.equals("所有商品") && saler.equals("所有商家")){
            if(month == 0){
                amount = DataSupport.where("substr(date,1,4) =? and type = 0 and item = ?", s_year, item).sum(records.class, "amount", float.class);
            }else if(day == 0){
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and type = 0 and item = ?", s_year,s_month, item).sum(records.class, "amount", float.class);
            }else{
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and type = 0 and item = ?", s_year, s_month, s_day, item).sum(records.class, "amount", float.class);
            }
        }else if(!account.equals("所有账户") && item.equals("所有商品") && saler.equals("所有商家")){
            if(month == 0){
                amount = DataSupport.where("substr(date,1,4) =? and type = 0 and account = ?", s_year, account).sum(records.class, "amount", float.class);
            }else if(day == 0){
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and type = 0 and account = ?", s_year,s_month, account).sum(records.class, "amount", float.class);
            }else{
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and type = 0 and account = ?", s_year, s_month, s_day, account).sum(records.class, "amount", float.class);
            }
        }else if (account.equals("所有账户")){
            Log.d(TAG, "calculateTotalExpense: all account");
            if(month == 0){
                amount = DataSupport.where("substr(date,1,4) =? and type = 0 and saler = ? and item = ?", s_year, saler, item).sum(records.class, "amount", float.class);
            }else if(day == 0){
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and type = 0 and saler = ? and item = ?", s_year,s_month, saler, item).sum(records.class, "amount", float.class);
            }else{
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and type = 0 and saler = ? and item = ?", s_year, s_month, s_day, saler, item).sum(records.class, "amount", float.class);
            }
        }else if (saler.equals("所有商家")){
            Log.d(TAG, "calculateTotalExpense: all saler");
            if(month == 0){
                amount = DataSupport.where("substr(date,1,4) =? and type = 0 and account = ? and item = ?", s_year, account, item).sum(records.class, "amount", float.class);
            }else if(day == 0){
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and type = 0 and account = ? and item = ?", s_year,s_month, account, item).sum(records.class, "amount", float.class);
            }else{
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and type = 0 and account = ? and item = ?", s_year, s_month, s_day, account, item).sum(records.class, "amount", float.class);
            }
        }else if (item.equals("所有商品")){
            Log.d(TAG, "calculateTotalExpense: all item");
            if(month == 0){
                amount = DataSupport.where("substr(date,1,4) =? and type = 0 and account = ? and saler = ?", s_year, account, saler).sum(records.class, "amount", float.class);
            }else if(day == 0){
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and type = 0 and account = ? and saler = ?", s_year,s_month, account, saler).sum(records.class, "amount", float.class);
            }else{
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and type = 0 and account = ? and saler = ?", s_year, s_month, s_day, account, saler).sum(records.class, "amount", float.class);
            }
        }else{
            Log.d(TAG, "calculateTotalExpense: else");
            if(month == 0){
                amount = DataSupport.where("substr(date,1,4) =? and account = ? and type = 0 and saler = ? and item = ?", s_year, account, saler, item).sum(records.class, "amount", float.class);
            }else if(day == 0){
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and account = ? and type = 0 and saler = ? and item = ?", s_year,s_month, account, saler, item).sum(records.class, "amount", float.class);
            }else{
                amount = DataSupport.where("substr(date,1,4) = ? and substr(date,6,2) = ? and substr(date,9,2) = ? and account = ? and type = 0 and saler = ? and item = ?", s_year, s_month, s_day, account, saler, item).sum(records.class, "amount", float.class);
            }
        }
        Log.d(TAG, "calculateTotalAmount: "+amount);
        return amount;
    }
}
