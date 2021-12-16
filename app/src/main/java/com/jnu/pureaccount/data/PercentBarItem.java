package com.jnu.pureaccount.data;

import static com.jnu.pureaccount.utils.AccountTypeUtils.getIconByName;

import com.jnu.pureaccount.utils.AccountTypeUtils;

//分析界面下方条形图的单条数据
public class PercentBarItem {
    private int icon;
    private String name;
    private int percent;
    private double account;

    public PercentBarItem(String name, int percent, double account){
        this.name = name;
        this.percent = percent;
        this.account = account;
        this.icon = getIconByName(name);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }
}
