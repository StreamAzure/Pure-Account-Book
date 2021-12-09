package com.jnu.pureaccount.ui.history;

import static com.jnu.pureaccount.event.AddItemActivity.OPERATION_EDIT;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.pureaccount.R;
import com.jnu.pureaccount.data.AccountItem;
import com.jnu.pureaccount.data.DayTotalItem;
import com.jnu.pureaccount.data.HomeItem;
import com.jnu.pureaccount.data.MonthTotalItem;
import com.jnu.pureaccount.event.AddItemActivity;
import com.jnu.pureaccount.event.ShowItemActivity;
import com.jnu.pureaccount.ui.home.HomeFragment;
import com.jnu.pureaccount.utils.CalendarUtils;
import com.jnu.pureaccount.utils.DataUtils;
import com.jnu.pureaccount.utils.MyDatePickerDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayHistoryFragment extends Fragment {
    private FloatingActionButton floatingActionButton;
    private String selectDay;
    private RecyclerView recyclerView;
    HomeItemAdapter mAdapter;
    ArrayList<HomeItem> mDayList;

    @Override
    public void onResume(){
        super.onResume();
        int[] intDate = new int[5];
        new CalendarUtils().TimeStringToInt(selectDay,intDate);
        try {
            new DataUtils(getContext()).QueryDayHistory(mDayList, intDate[0],intDate[1],intDate[2]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
        //数据一多必出问题……但是position也很难用时间复杂度更好的方法获得，先将就一下吧
    }

    public DayHistoryFragment() {
        // Required empty public constructor
    }
    public static DayHistoryFragment newInstance(String param1, String param2) {
        DayHistoryFragment fragment = new DayHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectDay = new CalendarUtils().getNowDateString();
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_month_history, container, false);
        mDayList = new ArrayList<>();
        initRecyclerView(rootView);
        initFloatingActionButton(rootView);
        return rootView;
    }

    private void initRecyclerView(View view){
        //RecyclerView设置
        recyclerView = view.findViewById(R.id.recycler_view_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DayHistoryFragment.this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new DayHistoryFragment.HomeItemAdapter(mDayList);
        recyclerView.setAdapter(mAdapter);
    }

    private void initFloatingActionButton(View view){
        floatingActionButton = view.findViewById(R.id.fab_choose_month);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(getActivity(), 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth) {
                        selectDay = new CalendarUtils().IntToTimeString(startYear,startMonthOfYear+1,startDayOfMonth);
                        Log.e("stream",selectDay);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE)).show();
            }
        });
    }


    public class HomeItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private static final int VIEW_TYPE_ACCOUNT_ITEM = 100;
        private static final int VIEW_TYPE_DAY_TOTAL_ITEM = 300;

        private static final int MENU_ITEM_EDIT = 1;
        private static final int MENU_ITEM_DETAIL = 2;
        private static final int MENU_ITEM_DELETE = 3;

        private final List<HomeItem> adpList;

        public HomeItemAdapter(List<HomeItem> adpList) {
            this.adpList = adpList;
        }

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
                    return new DayHistoryFragment.HomeItemAdapter.AccountItemHolder(itemView);
                case VIEW_TYPE_DAY_TOTAL_ITEM:
                    itemView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.item_daytotal_layout, parent, false);
                    return new DayHistoryFragment.HomeItemAdapter.DayTotalItemHolder(itemView);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof DayHistoryFragment.HomeItemAdapter.AccountItemHolder){
                DayHistoryFragment.HomeItemAdapter.AccountItemHolder viewHolder = (DayHistoryFragment.HomeItemAdapter.AccountItemHolder) holder;
                AccountItem accountItem = (AccountItem) adpList.get(position);
                viewHolder.icon.setBackgroundResource(accountItem.getIcon(accountItem.getReason()));
                viewHolder.reason.setText(accountItem.getTitle(DayHistoryFragment.this.getContext(),accountItem.getReason()));
                if(accountItem.getType()==0) {
                    viewHolder.account.setText("-"+String.format("%.2f",accountItem.getAccount()));
                    viewHolder.account.setTextColor(getResources().getColor(R.color.expend));
                }
                else{
                    viewHolder.account.setText("+"+String.format("%.2f",accountItem.getAccount()));
                    viewHolder.account.setTextColor(getResources().getColor(R.color.income));
                }
            }
            else if(holder instanceof DayHistoryFragment.HomeItemAdapter.DayTotalItemHolder){
                DayHistoryFragment.HomeItemAdapter.DayTotalItemHolder viewHolder = (DayHistoryFragment.HomeItemAdapter.DayTotalItemHolder) holder;
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
                        intent = new Intent(getActivity(), AddItemActivity.class);
                        intent.putExtra("operation", OPERATION_EDIT);
                        intent.putExtra("previousAccount", accountItem.getAccount());
                        intent.putExtra("previousSelectTime", accountItem.getTagDate());
                        intent.putExtra("previousSelectItem", accountItem.getReason());
                        intent.putExtra("createTime",accountItem.getCreateTime());
                        startActivity(intent);
                        //跳转过去修改，直接更新数据库，返回时因为onResume，直接重新载入整个数据库了
                        break;
                    case MENU_ITEM_DETAIL:
                        intent = new Intent(getActivity(), ShowItemActivity.class);
                        intent.putExtra("account",accountItem.getAccount());
                        intent.putExtra("reason",accountItem.getTitle(getContext()));
                        intent.putExtra("type",accountItem.getPrintType());
                        intent.putExtra("date",accountItem.getTagDate());
                        intent.putExtra("createTime", accountItem.getCreateTime());
                        startActivity(intent);
                        break;
                    case MENU_ITEM_DELETE:
                        //弹出对话框，只有文本提示、确定和取消，可以不写自定义布局
                        AlertDialog.Builder bookDeleteDialog = new AlertDialog.Builder(DayHistoryFragment.this.getContext());
                        bookDeleteDialog.setMessage("确定要删除吗？");
                        bookDeleteDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DataUtils dataUtils = new DataUtils(getActivity());
                                dataUtils.DeleteItem(accountItem.getCreateTime());
                                onResume();
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
}