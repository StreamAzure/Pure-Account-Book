package com.jnu.pureaccount.adapter;

import static com.jnu.pureaccount.event.AddItemActivity.OPERATION_EDIT;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.pureaccount.R;
import com.jnu.pureaccount.data.AccountItem;
import com.jnu.pureaccount.data.DayTotalItem;
import com.jnu.pureaccount.data.HomeItem;
import com.jnu.pureaccount.event.AddItemActivity;
import com.jnu.pureaccount.event.ShowItemActivity;
import com.jnu.pureaccount.utils.DataUtils;

import java.util.List;

public abstract class HomeItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int VIEW_TYPE_ACCOUNT_ITEM = 100;
    private static final int VIEW_TYPE_DAY_TOTAL_ITEM = 300;

    private static final int MENU_ITEM_EDIT = 1;
    private static final int MENU_ITEM_DETAIL = 2;
    private static final int MENU_ITEM_DELETE = 3;

    private final List<HomeItem> adpList;
    private Context mContext;
    private Activity mActivity;

    public HomeItemAdapter(List<HomeItem> adpList, Activity activity, Context context) {
        this.adpList = adpList;
        this.mActivity = activity;
        this.mContext = context;
    }

    /**
    @description:自己定义长按删除后的数据刷新事件（一般是onResume）
     */
    public abstract void updateDataWhenDeleteItem();

    @Override
    public int getItemViewType(int position){
        if(adpList.get(position) instanceof AccountItem){
            return VIEW_TYPE_ACCOUNT_ITEM;
        }
        else if(adpList.get(position) instanceof DayTotalItem){
            return VIEW_TYPE_DAY_TOTAL_ITEM;
        }
        else return VIEW_TYPE_ACCOUNT_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        switch (viewType) {
            case VIEW_TYPE_ACCOUNT_ITEM:
                itemView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_account_layout, parent, false);
                return new HomeItemAdapter.AccountItemHolder(itemView);
            case VIEW_TYPE_DAY_TOTAL_ITEM:
                itemView = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_daytotal_layout, parent, false);
                return new HomeItemAdapter.DayTotalItemHolder(itemView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HomeItemAdapter.AccountItemHolder){
            HomeItemAdapter.AccountItemHolder viewHolder = (HomeItemAdapter.AccountItemHolder) holder;
            AccountItem accountItem = (AccountItem) adpList.get(position);
            viewHolder.icon.setBackgroundResource(accountItem.getIcon(accountItem.getReason()));
            viewHolder.reason.setText(accountItem.getTitle(mContext,accountItem.getReason()));
            if(accountItem.getType()==0) {
                viewHolder.account.setText("-"+String.format("%.2f",accountItem.getAccount()));
                viewHolder.account.setTextColor(mContext.getResources().getColor(R.color.expend));
            }
            else{
                viewHolder.account.setText("+"+String.format("%.2f",accountItem.getAccount()));
                viewHolder.account.setTextColor(mContext.getResources().getColor(R.color.income));
            }
        }
        else if(holder instanceof HomeItemAdapter.DayTotalItemHolder){
            HomeItemAdapter.DayTotalItemHolder viewHolder = (HomeItemAdapter.DayTotalItemHolder) holder;
            DayTotalItem dayTotalItem = (DayTotalItem) adpList.get(position);
            viewHolder.date.setText(dayTotalItem.getPrintDate());
            viewHolder.expend.setText("支: "+String.format("%.2f",dayTotalItem.getExpendSubTotal()));
            viewHolder.income.setText("收: "+String.format("%.2f",dayTotalItem.getIncomeSubTotal()));
        }
    }

    @Override
    public int getItemCount() {
        return adpList.size();
    }

    class AccountItemHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{
        ImageView icon;
        TextView account;
        TextView reason;
        public AccountItemHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iv_item_account_icon);
            account = itemView.findViewById(R.id.tv_item_account_account);
            reason = itemView.findViewById(R.id.tv_item_account_reason);

            //让Item可以响应创建菜单的点击事件
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //添加三个菜单项
            MenuItem menuItemEdit = menu.add(Menu.NONE, MENU_ITEM_EDIT, 1, "修改");
            MenuItem menuItemDetail = menu.add(Menu.NONE, MENU_ITEM_DETAIL, 2, "详情");
            MenuItem menuItemDelete = menu.add(Menu.NONE, MENU_ITEM_DELETE, 3, "删除");
            //菜单项的响应事件
            menuItemEdit.setOnMenuItemClickListener(this);
            menuItemDetail.setOnMenuItemClickListener(this);
            menuItemDelete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int position = getAdapterPosition();
            //这个位置就是adpList里的位置，adpList.get(position)可以拿到对应项
            Intent intent;
            AccountItem accountItem = (AccountItem) adpList.get(position);
            switch (item.getItemId()) { //item.getItemId表示按的是右键菜单中的哪一项
                case MENU_ITEM_EDIT:
                    intent = new Intent(mActivity, AddItemActivity.class);
                    intent.putExtra("operation", OPERATION_EDIT);
                    intent.putExtra("previousAccount", accountItem.getAccount());
                    intent.putExtra("previousSelectTime", accountItem.getTagDate());
                    intent.putExtra("previousSelectItem", accountItem.getReason());
                    intent.putExtra("createTime",accountItem.getCreateTime());
                    mActivity.startActivity(intent);
                    //跳转过去修改，直接更新数据库，返回时因为onResume，直接重新载入整个数据库了
                    break;
                case MENU_ITEM_DETAIL:
                    intent = new Intent(mActivity, ShowItemActivity.class);
                    intent.putExtra("account",accountItem.getAccount());
                    intent.putExtra("reason",accountItem.getTitle(mContext));
                    intent.putExtra("type",accountItem.getPrintType());
                    intent.putExtra("date",accountItem.getTagDate());
                    intent.putExtra("createTime", accountItem.getCreateTime());
                    mActivity.startActivity(intent);
                    break;
                case MENU_ITEM_DELETE:
                    //弹出对话框，只有文本提示、确定和取消，可以不写自定义布局
                    AlertDialog.Builder bookDeleteDialog = new AlertDialog.Builder(mContext);
                    bookDeleteDialog.setMessage("确定要删除吗？");
                    bookDeleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            DataUtils dataUtils = new DataUtils(mActivity);
                            dataUtils.DeleteItem(accountItem.getCreateTime());
                            updateDataWhenDeleteItem();
                        }
                    });
                    bookDeleteDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    bookDeleteDialog.create().show();
                    break;
            }
            return false;
        }
    }
    class DayTotalItemHolder extends RecyclerView.ViewHolder{
        TextView date;
        TextView income;
        TextView expend;
        public DayTotalItemHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.tv_item_daytotal_date);
            income = itemView.findViewById(R.id.tv_item_daytotal_income);
            expend = itemView.findViewById(R.id.tv_item_daytotal_expend);
        }
    }
}