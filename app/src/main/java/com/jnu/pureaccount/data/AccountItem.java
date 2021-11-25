package com.jnu.pureaccount.data;
import java.util.Date;

public class AccountItem extends HomeItem {
    private int icon;
    private int reason;
    private int account;
    private boolean type; //0支出 1收入
    private Date date;


    public AccountItem(int icon,int reason, int account, boolean type, Date date) {
        this.icon = icon;
        this.reason = reason;
        this.account = account;
        this.type = type;
        this.date = date;
    }


    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }
    public int getReason(){
        return reason;
    }

    public void setReason(int reason){
        this.reason = reason;
    }
}
