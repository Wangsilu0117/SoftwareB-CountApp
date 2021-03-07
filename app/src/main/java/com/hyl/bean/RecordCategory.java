package com.hyl.bean;

import com.hyl.accountbook.R;

import org.litepal.crud.DataSupport;

public class RecordCategory extends DataSupport {
    private int id;
    private int type;         // 0-收入  1-支出
    private String level_1;   // 一级分类
    private String level_2;   // 二级分类
    private int imageId_1;    // 一级分类图标
    private int imageId_2;    // 二级分类图标

    public RecordCategory(int type, String level_1, String level_2, int imageId_2) {
        this.type = type;
        this.level_1 = level_1;
        this.level_2 = level_2;
        this.imageId_2 = imageId_2;

        switch (level_1) {
            case "食品酒水": imageId_1 = R.drawable.shipinjiushui; break;
            case "衣服饰品": imageId_1 = R.drawable.yifushipin; break;
            case "居家物业": imageId_1 = R.drawable.jujiawuye; break;
            case "行车交通": imageId_1 = R.drawable.xingchejiaotong; break;
            case "交流通讯": imageId_1 = R.drawable.jiaoliutongxun; break;
            case "休闲娱乐": imageId_1 = R.drawable.xiuxianyule; break;
            case "学习进修": imageId_1 = R.drawable.xuexijinxiu; break;
            case "人情往来": imageId_1 = R.drawable.renqingwanglai; break;
            case "医疗保健": imageId_1 = R.drawable.yiliaobaojian; break;
            case "金融保险": imageId_1 = R.drawable.jinrongbaoxian; break;
            case "其他杂项": imageId_1 = R.drawable.qitazaxiang; break;

            case "职业收入": imageId_1 = R.drawable.zhiyeshouru; break;
            case "其他收入": imageId_1 = R.drawable.qitashouru; break;

            default:
                if (type == 0) imageId_1 = R.drawable.shouru_zidingyi;
                else           imageId_1 = R.drawable.zhichu_zidingyi;
                break;
        }



    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getType() {
        return type;
    }
    public void setLevel_1(String level_1) {
        this.level_1 = level_1;
    }
    public String getLevel_1() {
        return level_1;
    }
    public void setLevel_2(String level_2) {
        this.level_2 = level_2;
    }
    public String getLevel_2() {
        return level_2;
    }
    public void setImageId_1(int imageId_1) {
        this.imageId_1 = imageId_1;
    }
    public int getImageId_1() {
        return imageId_1;
    }
    public void setImageId_2(int imageId_2) {
        this.imageId_2 = imageId_2;
    }
    public int getImageId_2() {
        return imageId_2;
    }
}
