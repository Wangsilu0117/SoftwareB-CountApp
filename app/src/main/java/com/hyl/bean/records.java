package com.hyl.bean;

import com.hyl.accountbook.R;

import org.litepal.crud.DataSupport;

public class records extends DataSupport {
    private int id;           //账的编号
    private String user;      //用户名称
    private float amount;     //账数值
    private String account;   //账户名称
    private int type;      //进账出账、、、
    private String catagory1; //母分类
    private String catagory2; //子分类
    private String date;         //使用year尝试存储年月日,记得修改相关函数
    private int intdate;
    private int hour;         //
    private int min;
    private String member;    //成员
    private String saler;     //商家
    private String item;      // 项目
    private String notes;     //备注
    private String transfer; //转账对象    //余额名称类型  //密码
    private int imageId;

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }

    public String getUser(){
        return user;
    }
    public void setUser(String user){
        this.user=user;
    }

    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount){
        this.amount=amount;
    }

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }

    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type=type;
    }

    public String getCatagory1() {
        return catagory1;
    }
    public void setCatagory1(String catagory1) {
        this.catagory1 = catagory1;
    }

    public String getCatagory2() {
        return catagory2;
    }
    public void setCatagory2(String catagory2) {
        this.catagory2 = catagory2;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public int getIntdate() {
        return intdate;
    }
    public void setIntdate(int intdate) {
        this.intdate = intdate;
    }

    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin(){
        return min;
    }
    public void setMin(int min) {
        this.min = min;
    }

    public String getMember() {
        return member;
    }
    public void setMember(String member) {
        this.member = member;
    }

    public String getSaler() {
        return saler;
    }
    public void setSaler(String saler) {
        this.saler = saler;
    }

    public String getItem() {
        return item;
    }
    public void setItem(String item) {
        this.item = item;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTransfer() {
        return transfer;
    }
    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public int getImageId() {
        return imageId;
    }
    public void setImageId() {
        switch (catagory1) {
            case "食品酒水": this.imageId = R.drawable.shipinjiushui; break;
            case "衣服饰品": this.imageId = R.drawable.yifushipin; break;
            case "居家物业": this.imageId = R.drawable.jujiawuye; break;
            case "行车交通": this.imageId = R.drawable.xingchejiaotong; break;
            case "交流通讯": this.imageId = R.drawable.jiaoliutongxun; break;
            case "休闲娱乐": this.imageId = R.drawable.xiuxianyule; break;
            case "学习进修": this.imageId = R.drawable.xuexijinxiu; break;
            case "人情往来": this.imageId = R.drawable.renqingwanglai; break;
            case "医疗保健": this.imageId = R.drawable.yiliaobaojian; break;
            case "金融保险": this.imageId = R.drawable.jinrongbaoxian; break;
            case "其他杂项": this.imageId = R.drawable.qitazaxiang; break;

            case "职业收入": this.imageId = R.drawable.zhiyeshouru; break;
            case "其他收入": this.imageId = R.drawable.qitashouru; break;
//            case "现金":
//                this.imageId = R.mipmap.accounts_xianjin;
//                break;
//            case "信用卡":
//            case "银行卡":
//                this.imageId = R.mipmap.accounts_xinyongka;
//                break;
//            case "金融":
//                this.imageId = R.mipmap.accounts_jinrong;
//                break;
//            case "虚拟":
//                this.imageId = R.mipmap.accounts_xuni;
//                break;
//            case "负债":
//                this.imageId = R.mipmap.accounts_fuzhai;
//                break;
//            case "债权":
//                this.imageId = R.mipmap.accounts_zhaiquan;
//                break;
//            case "投资":
//                this.imageId = R.mipmap.accounts_touzi;
//                break;
////            case "投资":
////                this.imageId = R.mipmap.accounts_jinrong;
////                break;
//            default:
//                this.imageId = R.mipmap.accounts_xianjin;
        }
    }

}