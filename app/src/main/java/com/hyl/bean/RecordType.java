package com.hyl.bean;

import android.util.Log;

import com.hyl.util.InitDataUtil;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 分类所得的支出/收入总计数据
 */

public class RecordType {

    private int type;       // 0-收入  1-支出  3-商家  4-成员  5-项目  6-账户
    private int level;      // 一级分类/二级分类
    private int beginDate;  // 开始日期
    private int endDate;    // 结束日期
    private float money;    // 总计
    private List<TypeMoney> typeMoneyList;

    public RecordType(int type, int level, int beginDate, int endDate) {
        this.type = type;
        this.level = level;
        this.beginDate = beginDate;
        this.endDate = endDate;

        if (type != 0 && type != 1)
            this.money = 0;
        else {
            this.money = DataSupport.where("type = ? and intdate >= ? and intdate <= ?", "" + type, "" + beginDate, "" + endDate).sum(records.class, "amount", float.class);
        }


        typeMoneyList = new ArrayList<>();
        if (type == 3) {
            // 商家分类
            List<String> salerList = InitDataUtil.getSalerList();
            float moneyIn;
            float moneyOut;
            float moneySum;
            for (String msaler : salerList) {
                moneyIn = DataSupport.where("type = ? and saler = ? and intdate >= ? and intdate <= ?", ""+0, msaler, ""+beginDate, ""+endDate).sum(records.class, "amount", float.class);
                moneyOut = DataSupport.where("type = ? and saler = ? and intdate >= ? and intdate <= ?", ""+1, msaler, ""+beginDate, ""+endDate).sum(records.class, "amount", float.class);
                moneySum = moneyIn - moneyOut;
                typeMoneyList.add(new TypeMoney(msaler, moneySum, moneyIn, moneyOut));
            }
        }
        else if (type == 4) {
            // 成员分类
            List<String> memberList = InitDataUtil.getMemberList();
            float moneyIn;
            float moneyOut;
            float moneySum;
            for (String mmember : memberList) {
                moneyIn = DataSupport.where("type = ? and member = ? and intdate >= ? and intdate <= ?", ""+0, mmember, ""+beginDate, ""+endDate).sum(records.class, "amount", float.class);
                moneyOut = DataSupport.where("type = ? and member = ? and intdate >= ? and intdate <= ?", ""+1, mmember, ""+beginDate, ""+endDate).sum(records.class, "amount", float.class);
                moneySum = moneyIn - moneyOut;
                typeMoneyList.add(new TypeMoney(mmember, moneySum, moneyIn, moneyOut));
            }
        }
        else if (type == 5) {
            // 项目分类
            List<String> itemList = InitDataUtil.getItemList();
            float moneyIn;
            float moneyOut;
            float moneySum;
            for (String mitem : itemList) {
                moneyIn = DataSupport.where("type = ? and item = ? and intdate >= ? and intdate <= ?", ""+0, mitem, ""+beginDate, ""+endDate).sum(records.class, "amount", float.class);
                moneyOut = DataSupport.where("type = ? and item = ? and intdate >= ? and intdate <= ?", ""+1, mitem, ""+beginDate, ""+endDate).sum(records.class, "amount", float.class);
                moneySum = moneyIn - moneyOut;
                typeMoneyList.add(new TypeMoney(mitem, moneySum, moneyIn, moneyOut));
            }
        }
        else if (type == 6) {
            // 账户分类
            List<String> accountList = InitDataUtil.getAccountList();
            float moneyIn;
            float moneyOut;
            float moneySum;
            for (String maccount : accountList) {
                if (DataSupport.where("type = ? and account = ? and intdate >= ? and intdate <= ?", ""+0, maccount, ""+beginDate, ""+endDate).sum(records.class, "amount", float.class) == 0 &&
                        DataSupport.where("type = ? and account = ? and intdate >= ? and intdate <= ?", ""+1, maccount, ""+beginDate, ""+endDate).sum(records.class, "amount", float.class) == 0) {
                    continue;
                }
                moneyIn = DataSupport.where("type = ? and account = ? and intdate >= ? and intdate <= ?", ""+0, maccount, ""+beginDate, ""+endDate).sum(records.class, "amount", float.class);
                moneyOut = DataSupport.where("type = ? and account = ? and intdate >= ? and intdate <= ?", ""+1, maccount, ""+beginDate, ""+endDate).sum(records.class, "amount", float.class);
                moneySum = moneyIn - moneyOut;
                typeMoneyList.add(new TypeMoney(maccount, moneySum, moneyIn, moneyOut));
            }
        }
        else {
            if (level == 1) {
                List<String> categories = InitDataUtil.getCategory1List(type);
                for (String category : categories) {
                    if (DataSupport.where("type = ? and catagory1 = ? and intdate >= ? and intdate <= ?",
                            "" + type, category, "" + beginDate, "" + endDate).sum(records.class, "amount", float.class) != 0) {
                        typeMoneyList.add(new TypeMoney(category, DataSupport.where("type = ? and catagory1 = ? and intdate >= ? and intdate <= ?",
                                "" + type, category, "" + beginDate, "" + endDate).sum(records.class, "amount", float.class), 0, 0));
                    }
                }

            } else if (level == 2) {
                List<String> categories = InitDataUtil.getCategory2List(type);
                for (String category : categories) {
                    if (DataSupport.where("type = ? and catagory2 = ? and intdate >= ? and intdate <= ?",
                            "" + type, category, "" + beginDate, "" + endDate).sum(records.class, "amount", float.class) != 0) {
                        typeMoneyList.add(new TypeMoney(category, DataSupport.where("type = ? and catagory2 = ? and intdate >= ? and intdate <= ?",
                                "" + type, category, "" + beginDate, "" + endDate).sum(records.class, "amount", float.class), 0, 0));
                    }
                }

            }
        }
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(int beginDate) {
        this.beginDate = beginDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public List<TypeMoney> getTypeMoneyList() {
        return typeMoneyList;
    }

    public void setTypeMoneyList(List<TypeMoney> typeMoneyList) {
        this.typeMoneyList = typeMoneyList;
    }

    public static class TypeMoney {

        private String type;
        private float moneySum;
        private float moneyIn;
        private float moneyOut;

        public TypeMoney(String type, float moneySum, float moneyIn, float moneyOut) {
            this.type = type;
            this.moneySum = moneySum;
            this.moneyIn = moneyIn;
            this.moneyOut = moneyOut;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public float getMoneySum() {
            return moneySum;
        }

        public void setMoneySum(float moneySum) {
            this.moneySum = moneySum;
        }

        public float getMoneyIn() {
            return moneyIn;
        }

        public void setMoneyIn(float moneyIn) {
            this.moneyIn = moneyIn;
        }

        public float getMoneyOut() {
            return moneyOut;
        }

        public void setMoneyOut(float moneyOut) {
            this.moneyOut = moneyOut;
        }
    }
}
