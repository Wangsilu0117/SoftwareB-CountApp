package com.hyl.accountbook;

import android.widget.ArrayAdapter;

import com.hyl.bean.Accounts;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;
//使用litepal中datasupport方法查询出的list是Accounts，不能直接作为spinner的item
//此函数将(List<Accounts> all_acounts转换成List<String>name_of_accounts
public class getAccountsItems {
    public List<String>getAccountsItems(List<Accounts> all_acounts){
        List<String>name_of_accounts = new ArrayList<>();
        name_of_accounts.add("所有账户");
        for(int i = 0; i < all_acounts.size(); i++){
            name_of_accounts.add(all_acounts.get(i).getName());
        }
        return name_of_accounts;
    }
}
