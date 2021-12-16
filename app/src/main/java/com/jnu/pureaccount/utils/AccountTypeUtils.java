package com.jnu.pureaccount.utils;

import android.content.Context;

import com.jnu.pureaccount.R;

import java.util.HashMap;

/**
 * 用于类型名、类型代号、对应图片资源之间的麻烦转换
 */
public class AccountTypeUtils {

    public static String getNameByCode(int code){
        switch (code){
            case 1:
                return "吃喝饮食";
            case 2:
                return "影视娱乐";
            case 3:
                return "日常用品";
            case 4:
                return "宠物花销";
            case 5:
                return "房租水电";
            case 6:
                return "医疗费用";
            case 7:
                return "逛街购物";
            case 8:
                return "交通出行";
            case 9:
                return "旅游度假";
            case 10:
                return "学习用品";
            case 11:
                return "工资薪水";
            case 12:
                return "奖品奖金";
            case 13:
                return "投资理财";
            case 14:
                return "经费津贴";
            default:
                return "其他";
        }
    }
    public static int getIconByCode(int code){
        switch (code) {
            case 1:
                return R.drawable.icon_food;
            case 2:
                return R.drawable.icon_entertainment;
            case 3:
                return R.drawable.icon_clothes;
            case 4:
                return R.drawable.icon_makeup;
            case 5:
                return R.drawable.icon_houserent;
            case 6:
                return R.drawable.icon_medicine;
            case 7:
                return R.drawable.icon_shopping;
            case 8:
                return R.drawable.icon_traffic;
            case 9:
                return R.drawable.icon_tour;
            case 10:
                return R.drawable.icon_study;
            case 11:
                return R.drawable.icon_salary;
            case 12:
                return R.drawable.icon_winning;
            case 13:
                return R.drawable.icon_investment;
            case 14:
                return R.drawable.icon_business;
            default:
                return R.drawable.icon_other;
        }
    }
    public static int getIconByName(String name){
        return getIconByCode(getCodeByName(name));
    }
    public static int getCodeByName(String name){
        HashMap<Integer,String> hashMap = new HashMap<>();
        for(int i=1;i<=14;i++) {
            hashMap.put(i, getNameByCode(i));
        }
        for(int key: hashMap.keySet()){
            if(hashMap.get(key).equals(name)){
                return key;
            }
        }
        return 14;
    }
}
