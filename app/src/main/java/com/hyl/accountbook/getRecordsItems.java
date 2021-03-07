package com.hyl.accountbook;

import com.hyl.bean.records;

import java.util.ArrayList;
import java.util.List;

public class getRecordsItems {
    public List<String> getRecordsItems(List<records> all_records, String option){
        List<String>name_of_records = new ArrayList<>();
        if (option == "salers"){
            name_of_records.add("所有商家");
            for(int i = 0; i < all_records.size(); i++){
                name_of_records.add(all_records.get(i).getSaler());
            }
        }else if (option == "items"){
            name_of_records.add("所有商品");
            for(int i = 0; i < all_records.size(); i++){
                name_of_records.add(all_records.get(i).getItem());
            }
        }
        return name_of_records;
    }
}
