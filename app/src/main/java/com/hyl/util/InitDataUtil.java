package com.hyl.util;

import android.accounts.Account;
import android.graphics.Color;
import android.util.Log;

import com.hyl.accountbook.R;
import com.hyl.bean.Accounts;
import com.hyl.bean.RecordCategory;
import com.hyl.bean.RecordType;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class InitDataUtil {

    // 账户初始化数据
    public static List<Accounts> getAccountsInitData() {
        List<Accounts> accounts = new ArrayList<>();
        Accounts account1 = new Accounts();
        account1.setType("现金");
        account1.setName("现金");
        account1.setBalance(0);
        account1.setImageId();
        accounts.add(account1);

        Accounts account2 = new Accounts();
        account2.setType("信用卡");
        account2.setName("信用卡");
        account2.setBalance(0);
        account2.setImageId();
        accounts.add(account2);

        Accounts account3 = new Accounts();
        account3.setType("金融");
        account3.setName("银行卡");
        account3.setBalance(0);
        account3.setImageId();
        accounts.add(account3);

        Accounts account4 = new Accounts();
        account4.setType("虚拟");
        account4.setName("饭卡");
        account4.setBalance(0);
        account4.setImageId();
        accounts.add(account4);

        Accounts account5 = new Accounts();
        account5.setType("虚拟");
        account5.setName("公交卡");
        account5.setBalance(0);
        account5.setImageId();
        accounts.add(account5);

        Accounts account6 = new Accounts();
        account6.setType("虚拟");
        account6.setName("支付宝");
        account6.setBalance(0);
        account6.setImageId();
        accounts.add(account6);

        Accounts account7 = new Accounts();
        account7.setType("虚拟");
        account7.setName("微信钱包");
        account7.setBalance(0);
        account7.setImageId();
        accounts.add(account7);

        Accounts account8 = new Accounts();
        account8.setType("负债");
        account8.setName("应付款项");
        account8.setBalance(0);
        account8.setImageId();
        accounts.add(account8);

        Accounts account9 = new Accounts();
        account9.setType("债权");
        account9.setName("应收款项");
        account9.setBalance(0);
        account9.setImageId();
        accounts.add(account9);

        Accounts account10 = new Accounts();
        account10.setType("基金账户");
        account10.setName("投资");
        account10.setBalance(0);
        account10.setImageId();
        accounts.add(account10);

        Accounts account11 = new Accounts();
        account11.setType("投资");
        account11.setName("股票");
        account11.setBalance(0);
        account11.setImageId();
        accounts.add(account11);

        return accounts;
    }

    // 账户分类数据
    public static List<String> getAccountsType() {
        List<String> accountsType = new ArrayList<>();
        accountsType.add("现金");
        accountsType.add("信用卡");
        accountsType.add("金融");
        accountsType.add("虚拟");
        accountsType.add("负债");
        accountsType.add("债权");
        accountsType.add("投资");
        return accountsType;
    }

    // 账单分类初始化数据
    // 图标来源：iconfont.cn/collections/detail?spm=a313x.7781069.1998910419.d9df05512&cid=11894
    public static List<RecordCategory> getRecordCategoryInitData() {
        List<RecordCategory> recordCategories  = new ArrayList<>();
        recordCategories.add(new RecordCategory(1, "食品酒水", "早午晚餐", R.drawable.shipin_zaowuwancan));
        recordCategories.add(new RecordCategory(1, "食品酒水", "烟酒茶", R.drawable.shipin_yanjiucha));
        recordCategories.add(new RecordCategory(1, "食品酒水", "水果零食", R.drawable.shipin_shuiguolingshi));

        recordCategories.add(new RecordCategory(1, "衣服饰品", "衣服裤子", R.drawable.yifu_yifukuzi));
        recordCategories.add(new RecordCategory(1, "衣服饰品", "鞋帽包包", R.drawable.yifu_xiemaobaobao));
        recordCategories.add(new RecordCategory(1, "衣服饰品", "化妆饰品", R.drawable.yifu_huazhuangshipin));

        recordCategories.add(new RecordCategory(1, "居家物业", "日常用品", R.drawable.jujia_richangyongpin));
        recordCategories.add(new RecordCategory(1, "居家物业", "水电煤气", R.drawable.jujia_shuidianmeiqi));
        recordCategories.add(new RecordCategory(1, "居家物业", "房租", R.drawable.jujia_fangzu));
        recordCategories.add(new RecordCategory(1, "居家物业", "物业管理", R.drawable.jujia_wuyeguanli));
        recordCategories.add(new RecordCategory(1, "居家物业", "维修保养", R.drawable.jujia_weixiubaoyang));

        recordCategories.add(new RecordCategory(1, "行车交通", "公共交通", R.drawable.xingche_gonggongjiaotong));
        recordCategories.add(new RecordCategory(1, "行车交通", "打车租车", R.drawable.xingche_dachezuche));
        recordCategories.add(new RecordCategory(1, "行车交通", "私家车费用", R.drawable.xingche_sijiachefeiyong));

        recordCategories.add(new RecordCategory(1, "交流通讯", "座机费", R.drawable.jiaoliu_zuojifei));
        recordCategories.add(new RecordCategory(1, "交流通讯", "手机费", R.drawable.jiaoliu_shoujifei));
        recordCategories.add(new RecordCategory(1, "交流通讯", "上网费", R.drawable.jiaoliu_shangwangfei));
        recordCategories.add(new RecordCategory(1, "交流通讯", "邮寄费", R.drawable.jiaoliu_youjifei));

        recordCategories.add(new RecordCategory(1, "休闲娱乐", "运动健身", R.drawable.xiuxian_yundongjiashen));
        recordCategories.add(new RecordCategory(1, "休闲娱乐", "腐败聚会", R.drawable.xiuxian_fubaijuhui));
        recordCategories.add(new RecordCategory(1, "休闲娱乐", "休闲玩乐", R.drawable.xiuxian_xiuxianwanle));
        recordCategories.add(new RecordCategory(1, "休闲娱乐", "宠物宝贝", R.drawable.xiuxian_chongwubaobei));
        recordCategories.add(new RecordCategory(1, "休闲娱乐", "旅游度假", R.drawable.xiuxian_lvyoudujia));

        recordCategories.add(new RecordCategory(1, "学习进修", "书报杂志", R.drawable.xuexi_shubaozazhi));
        recordCategories.add(new RecordCategory(1, "学习进修", "培训进修", R.drawable.xuexi_peixunjinxiu));
        recordCategories.add(new RecordCategory(1, "学习进修", "数码装备", R.drawable.xuexi_shumazhuangbei));

        recordCategories.add(new RecordCategory(1, "人情往来", "送礼请客", R.drawable.renqing_songliqingke));
        recordCategories.add(new RecordCategory(1, "人情往来", "孝敬家长", R.drawable.renqing_xiaojingjiazhang));
        recordCategories.add(new RecordCategory(1, "人情往来", "还人钱财", R.drawable.renqing_huanrenqianwu));
        recordCategories.add(new RecordCategory(1, "人情往来", "慈善捐助", R.drawable.renqing_cishanjuanzhu));

        recordCategories.add(new RecordCategory(1, "医疗保健", "药品费", R.drawable.yiliao_yaowufei));
        recordCategories.add(new RecordCategory(1, "医疗保健", "保健费", R.drawable.yiliao_baojianfei));
        recordCategories.add(new RecordCategory(1, "医疗保健", "美容费", R.drawable.yiliao_meirongfei));
        recordCategories.add(new RecordCategory(1, "医疗保健", "治疗费", R.drawable.yiliao_zhiliaofei));

        recordCategories.add(new RecordCategory(1, "金融保险", "银行手续", R.drawable.jinrong_yinhangshouxu));
        recordCategories.add(new RecordCategory(1, "金融保险", "投资亏损", R.drawable.jinrong_touzikuisun));
        recordCategories.add(new RecordCategory(1, "金融保险", "按揭还款", R.drawable.jinrong_anjiehuankuan));
        recordCategories.add(new RecordCategory(1, "金融保险", "消费税收", R.drawable.jinrong_xiaofeishuishou));
        recordCategories.add(new RecordCategory(1, "金融保险", "利息支出", R.drawable.jinrong_lixizhichu));
        recordCategories.add(new RecordCategory(1, "金融保险", "赔偿罚款", R.drawable.jinrong_peichangfakuan));

        recordCategories.add(new RecordCategory(1, "其他杂项", "其他支出", R.drawable.qita_qitazhichu));
        recordCategories.add(new RecordCategory(1, "其他杂项", "意外丢失", R.drawable.qita_yiwaidiushi));
        recordCategories.add(new RecordCategory(1, "其他杂项", "烂账损坏", R.drawable.qita_lanzhangsunhuai));

        recordCategories.add(new RecordCategory(0, "职业收入", "工资收入", R.drawable.zhiye_gongzishouru));
        recordCategories.add(new RecordCategory(0, "职业收入", "利息收入", R.drawable.zhiye_lixishouru));
        recordCategories.add(new RecordCategory(0, "职业收入", "加班收入", R.drawable.zhiye_jiabanshouru));
        recordCategories.add(new RecordCategory(0, "职业收入", "奖金收入", R.drawable.zhiye_jiangjinshouru));
        recordCategories.add(new RecordCategory(0, "职业收入", "投资收入", R.drawable.zhiye_touzishouru));
        recordCategories.add(new RecordCategory(0, "职业收入", "兼职收入", R.drawable.zhiye_jianzhishouru));

        recordCategories.add(new RecordCategory(0, "其他收入", "礼金收入", R.drawable.qita_lijinshouru));
        recordCategories.add(new RecordCategory(0, "其他收入", "中奖收入", R.drawable.qita_zhongjiangshouru));
        recordCategories.add(new RecordCategory(0, "其他收入", "意外来钱", R.drawable.qita_yiwailaiqian));
        recordCategories.add(new RecordCategory(0, "其他收入", "经营所得", R.drawable.qita_jingyingsuode));

        return recordCategories;
    }

    // 获取所有一级分类
    public static List<String> getCategory1List(int type) {
        List<String> category1list = new ArrayList<>();
        List<RecordCategory> recordCategories = DataSupport.select("level_1").where("type = ?", ""+type).find(RecordCategory.class);
        for (RecordCategory recordCategory : recordCategories) {
            if (!category1list.contains(recordCategory.getLevel_1())) {
                category1list.add(recordCategory.getLevel_1());
            }
        }
        return category1list;
    }

    // 获取所有二级分类
    public static List<String> getCategory2List(int type) {
        List<String> category2list = new ArrayList<>();
        List<RecordCategory> recordCategories = DataSupport.select("level_2").where("type = ?", ""+type).find(RecordCategory.class);
        for (RecordCategory recordCategory : recordCategories) {
            category2list.add(recordCategory.getLevel_2());
        }
        return category2list;
    }

    // 获取所有商家
    public static List<String> getSalerList() {
        List<String> salerlist = new ArrayList<>();
        salerlist.add("无商家/地点");
        salerlist.add("银行");
        salerlist.add("公交");
        salerlist.add("饭堂");
        salerlist.add("商场");
        salerlist.add("超市");
        salerlist.add("其他");
        return salerlist;
    }

    // 获取所有成员
    public static List<String> getMemberList() {
        List<String> memberList = new ArrayList<>();
        memberList.add("无成员");
        memberList.add("本人");
        memberList.add("丈夫");
        memberList.add("妻子");
        memberList.add("子女");
        memberList.add("父母");
        memberList.add("家庭");
        return memberList;
    }

    // 获取所有项目
    public static List<String> getItemList() {
        List<String> itemList = new ArrayList<>();
        itemList.add("无项目");
        itemList.add("红包");
        itemList.add("过年");
        itemList.add("出差");
        itemList.add("报销");
        itemList.add("装修");
        itemList.add("旅游");
        itemList.add("腐败");
        return itemList;
    }

    // 获取所有账户
    public static List<String> getAccountList() {
        List<String> accountList = new ArrayList<>();
        List<Accounts> accounts = DataSupport.select("name").find(Accounts.class);
        for (Accounts account : accounts) {
            accountList.add(account.getName());
        }
        return accountList;
    }


    public static ArrayList<Integer> getChartColor(int type) {
        ArrayList<Integer> color = new ArrayList<>();
        if (type == 0) {
            // 收入
            color.add(Color.parseColor("#89c3eb"));
            color.add(Color.parseColor("#a0d8ef"));
            color.add(Color.parseColor("#abced8"));
            color.add(Color.parseColor("#a3d7dd"));
            color.add(Color.parseColor("#bce2e8"));
            color.add(Color.parseColor("#c1e4e9"));
            color.add(Color.parseColor("#ebf6f7"));
            color.add(Color.parseColor("#e8ecef"));
            color.add(Color.parseColor("#eaedf7"));
        }
        else {
            // 支出
            color.add(Color.parseColor("#e198b4"));
            color.add(Color.parseColor("#e4ab9b"));
            color.add(Color.parseColor("#e09e87"));
            color.add(Color.parseColor("#d69090"));
            color.add(Color.parseColor("#d4acad"));
            color.add(Color.parseColor("#c97586"));
            color.add(Color.parseColor("#c099a0"));
            color.add(Color.parseColor("#b88884"));
            color.add(Color.parseColor("#b48a76"));
            color.add(Color.parseColor("#ddbb99"));
            color.add(Color.parseColor("#d7a98c"));
            color.add(Color.parseColor("#f2c9ac"));
            color.add(Color.parseColor("#fce2c4"));
        }
        return color;
    }

}
