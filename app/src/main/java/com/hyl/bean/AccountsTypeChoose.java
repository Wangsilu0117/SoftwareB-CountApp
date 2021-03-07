package com.hyl.bean;

import com.contrarywind.interfaces.IPickerViewData;

public class AccountsTypeChoose implements IPickerViewData {
    int id;
    String type;

    public AccountsTypeChoose(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getPickerViewText() {
        return type;
    }
}
