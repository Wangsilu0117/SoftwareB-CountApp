package com.hyl.bean;

import android.telecom.StatusHints;

import com.hyl.accountbook.R;

import org.litepal.crud.DataSupport;

public class Accounts extends DataSupport {
    private int id;
    private String name;
    private float balance;
    private String type;
    private int imageId;


    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public float getBalance() {
        return balance;
    }
    public void setBalance(float balance) {
        this.balance = balance;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId() {
        switch (type) {
            case "现金":
                this.imageId = R.mipmap.accounts_xianjin;
                break;
            case "信用卡":
                this.imageId = R.mipmap.accounts_xinyongka;
                break;
            case "金融":
                this.imageId = R.mipmap.accounts_jinrong;
                break;
            case "虚拟":
                this.imageId = R.mipmap.accounts_xuni;
                break;
            case "负债":
                this.imageId = R.mipmap.accounts_fuzhai;
                break;
            case "债权":
                this.imageId = R.mipmap.accounts_zhaiquan;
                break;
            case "投资":
                this.imageId = R.mipmap.accounts_touzi;
                break;
            default:
        }
    }

}
