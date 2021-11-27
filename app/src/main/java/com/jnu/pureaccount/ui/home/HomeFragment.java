package com.jnu.pureaccount.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.pureaccount.R;
import com.jnu.pureaccount.data.AccountItem;
import com.jnu.pureaccount.data.DayTotalItem;
import com.jnu.pureaccount.data.HomeItem;
import com.jnu.pureaccount.data.MonthTotalItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    HomeItemAdapter mAdapter;
    TreeMap<String,List<HomeItem>> listTreeMap;
    //先用一个Map进行管理，若干个日期为键，一个日期对应一组列表，每组列表以一个DayTotalItem开头
    List<HomeItem> mHomeItemList;
    //传入Adapter的数据源

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        initData();//初始化mHomeItemList
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);
        //RecyclerView设置
        recyclerView = rootView.findViewById(R.id.recycler_view_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeFragment.this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new HomeItemAdapter(mHomeItemList);
        recyclerView.setAdapter(mAdapter);

        return rootView;
    }

    private void initData() {
//        listTreeMap = new TreeMap<String,List<HomeItem>>(new Comparator<String>() {
//            @Override
//            public int compare(String o1, String o2) {
//                return o2.compareTo(o1);
//            }
//        });

        listTreeMap = new TreeMap<>();
        //虽然不知道为什么，用默认排序就对了

        List<HomeItem> accountItemList1 = new ArrayList<>();
        List<HomeItem> accountItemList2 = new ArrayList<>();
        //测试时注意！！一个List里的所有Item的日期是相同的！！

        accountItemList1.add(new AccountItem(R.drawable.ic_expend_present,1,50,true,2021,11,27));
        accountItemList1.add(new AccountItem(R.drawable.ic_expend_rent,2,60,true,2021,11,27));
        accountItemList2.add(new AccountItem(R.drawable.ic_expend_snacks,3,70,true,2020,1,1));
        accountItemList2.add((new AccountItem(R.drawable.ic_expend_medicine,4,80,true,2020,1,1)));

        String date1 = ((AccountItem)accountItemList1.get(0)).getTagDate();
        String date2 = ((AccountItem)accountItemList2.get(0)).getTagDate();
        listTreeMap.put(date2,accountItemList1);
        listTreeMap.put(date1,accountItemList2);

        arrangeData(listTreeMap);

    }

    private void arrangeData(TreeMap<String, List<HomeItem>> treeMap){
        //重整为要传入Adapter的数据源mHomeItemList
        mHomeItemList = new ArrayList<>();
        Iterator<Map.Entry<String,List<HomeItem>>> it = treeMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,List<HomeItem>> entry = it.next();
            List<HomeItem> homeItems = entry.getValue();
            //获取对应的homeItem列表
            Calendar calendar = ((AccountItem)homeItems.get(0)).getDate();
            DayTotalItem dayTotalItem = new DayTotalItem(calendar,100,100);
            mHomeItemList.add(dayTotalItem);
            //加入对应日期Item
            for(int i = 0 ;i < homeItems.size();i++){
                mHomeItemList.add(homeItems.get(i));
            }
        }

        //debug
        for(int i = 0;i < mHomeItemList.size();i++){
            Log.e("MYTAG",mHomeItemList.get(i)+"");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public class HomeItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private static final int VIEW_TYPE_ACCOUNT_ITEM = 1;
        private static final int VIEW_TYPE_MONTH_TOTAL_ITEM = 2;
        private static final int VIEW_TYPE_DAY_TOTAL_ITEM = 3;

        private final List<HomeItem> adpList;

        public HomeItemAdapter(List<HomeItem> adpList) {
            this.adpList = adpList;
        }

        @Override
        public int getItemViewType(int position){
            if(adpList.get(position) instanceof AccountItem){
                return VIEW_TYPE_ACCOUNT_ITEM;
            }
            else if(adpList.get(position) instanceof MonthTotalItem){
                return VIEW_TYPE_MONTH_TOTAL_ITEM;
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
                    return new AccountItemHolder(itemView);
                case VIEW_TYPE_MONTH_TOTAL_ITEM:
                    itemView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.item_monthtotal_layout, parent, false);
                    return new MonthTotalItemHolder(itemView);
                case VIEW_TYPE_DAY_TOTAL_ITEM:
                    itemView = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_daytotal_layout, parent, false);
                    return new DayTotalItemHolder(itemView);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof AccountItemHolder){
                AccountItemHolder viewHolder = (AccountItemHolder) holder;
                AccountItem accountItem = (AccountItem) adpList.get(position);
                viewHolder.icon.setBackgroundResource(accountItem.getIcon());
                viewHolder.reason.setText(accountItem.getReason()+"");
                viewHolder.account.setText(accountItem.getAccount()+"");
            }
            else if(holder instanceof MonthTotalItemHolder){
                MonthTotalItemHolder viewHolder = (MonthTotalItemHolder) holder;
                //TODO
            }
            else if(holder instanceof DayTotalItemHolder){
                DayTotalItemHolder viewHolder = (DayTotalItemHolder) holder;
                DayTotalItem dayTotalItem = (DayTotalItem) adpList.get(position);
                viewHolder.date.setText(dayTotalItem.getPrintDate());
                viewHolder.expend.setText(dayTotalItem.getExpendSubTotal()+"");
                viewHolder.income.setText(dayTotalItem.getIncomeSubTotal()+"");
            }
        }

        @Override
        public int getItemCount() {
            return mHomeItemList.size();
        }

        class AccountItemHolder extends RecyclerView.ViewHolder{
            ImageView icon;
            TextView account;
            TextView reason;
            public AccountItemHolder(@NonNull View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.iv_item_account_icon);
                account = itemView.findViewById(R.id.tv_item_account_account);
                reason = itemView.findViewById(R.id.tv_item_account_reason);
                Log.e("MYTAG","调用AccountItemHolder");
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
                Log.e("MYTAG","调用DayTotalItemHolder");
            }

        }
        class MonthTotalItemHolder extends RecyclerView.ViewHolder{

            public MonthTotalItemHolder(@NonNull View itemView) {
                super(itemView);
            }
            //TODO
        }
    }
}