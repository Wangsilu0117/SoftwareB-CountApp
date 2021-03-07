package com.hyl.bean;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * 饼图的一级分类/二级分类的PickerView所需的类
 */

public class LevelChoose implements IPickerViewData {
    int id;
    String level;

    public LevelChoose(int id, String level) {
        this.id = id;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String getPickerViewText() {
        return level;
    }
}