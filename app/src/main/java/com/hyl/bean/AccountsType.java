package com.hyl.bean;

import com.hyl.accountbook.R;

import org.litepal.crud.DataSupport;

import java.util.List;

public class AccountsType {
    private String type;
    private float balance;
    private int imageId;
    private List<Accounts> accountsList;

    public AccountsType(String type) {
        this.type = type;
        this.balance = DataSupport.where("type = ?", type).sum(Accounts.class, "balance", float.class);
        this.accountsList = DataSupport.where("type = ?", type).find(Accounts.class);
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

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public float getBalance() {
        return balance;
    }
    public void setBalance(float balance) {
        this.balance = balance;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public List<Accounts> getAccountsList() {
        return accountsList;
    }
    public void setAccountsList(List<Accounts> accountsList) {
        this.accountsList = accountsList;
    }
}
