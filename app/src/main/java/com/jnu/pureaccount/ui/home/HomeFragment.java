package com.jnu.pureaccount.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.pureaccount.MainActivity;
import com.jnu.pureaccount.R;
import com.jnu.pureaccount.data.AccountItem;
import com.jnu.pureaccount.data.DayTotalItem;
import com.jnu.pureaccount.data.HomeItem;
import com.jnu.pureaccount.data.MonthTotalItem;
import com.jnu.pureaccount.db.DatabaseHelper;
import com.jnu.pureaccount.event.AddItemActivity;
import com.jnu.pureaccount.ui.item.AddExpendItemFragment;
import com.jnu.pureaccount.utils.CalendarUtils;
import com.jnu.pureaccount.utils.DataUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HomeFragment extends Fragment{
    public static final int RESULT_CODE_ADD_OK = 200;
    private RecyclerView recyclerView;

    HomeItemAdapter mAdapter;
    TreeMap<String,List<HomeItem>> listTreeMap;
    //先用一个Map进行管理，若干个日期为键，一个日期对应一组列表，每组列表以一个DayTotalItem开头
    List<HomeItem> mHomeItemList;
    //传入Adapter的数据源

    @Override
    public void onResume(){
        super.onResume();
        arrangeData(listTreeMap);
        mAdapter.notifyDataSetChanged();
        //数据一多必出问题……但是position也很难用时间复杂度更好的方法获得，先将就一下吧
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        initData();//初始化mHomeItemList
        View rootView = inflater.inflate(R.layout.fragment_home,container,false);

        initFloatingActionButton(rootView);
        initRecyclerView(rootView);
        return rootView;
    }

    private void initRecyclerView(View view){
        //RecyclerView设置
        recyclerView = view.findViewById(R.id.recycler_view_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(HomeFragment.this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new HomeItemAdapter(mHomeItemList);
        recyclerView.setAdapter(mAdapter);
    }

    //初始化浮动按钮
    private void initFloatingActionButton(View view) {
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        mHomeItemList = new ArrayList<>();
        listTreeMap = new TreeMap<>();
        //虽然不知道为什么，用默认排序就对了
        DataUtils dataUtils = new DataUtils(this.getActivity());

        //dataUtils.DeleteTable("item");

        dataUtils.loadItemData(listTreeMap);
        arrangeData(listTreeMap);
    }

    private void arrangeData(TreeMap<String, List<HomeItem>> treeMap){
        //加载数据库
        DataUtils dataUtils = new DataUtils(this.getActivity());
        dataUtils.loadItemData(listTreeMap);
        //重整为要传入Adapter的数据源mHomeItemList
        mHomeItemList.clear();
        Iterator<Map.Entry<String,List<HomeItem>>> it = treeMap.entrySet().iterator();

        while(it.hasNext()){
            Map.Entry<String,List<HomeItem>> entry = it.next();
            List<HomeItem> homeItems = entry.getValue();
            //获取对应的homeItem列表
            Calendar calendar = ((AccountItem)homeItems.get(0)).getDate();
            //TODO:当日小结金额显示
            DayTotalItem dayTotalItem = new DayTotalItem(calendar,100,100);
            mHomeItemList.add(dayTotalItem);
            //加入对应日期Item
            for(int i = 0 ;i < homeItems.size();i++){
                mHomeItemList.add(homeItems.get(i));
            }
        }
        //debug
        for(int i = 0;i < mHomeItemList.size();i++){
            if(mHomeItemList.get(i) instanceof AccountItem){
                AccountItem accountItem = (AccountItem) mHomeItemList.get(i);
                Log.e("HomeFragment",
                        accountItem.getTitle(getContext(),accountItem.getReason())
                        +" "+accountItem.getAccount()
                        +" "+accountItem.getTagDate());
            }
            else{
                DayTotalItem dayTotalItem = (DayTotalItem) mHomeItemList.get(i);
                Log.e("HomeFragment", dayTotalItem.getPrintDate());
            }
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
                viewHolder.icon.setBackgroundResource(accountItem.getIcon(accountItem.getReason()));
                viewHolder.reason.setText(accountItem.getTitle(HomeFragment.this.getContext(),accountItem.getReason()));
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
            return adpList.size();
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
        class MonthTotalItemHolder extends RecyclerView.ViewHolder{

            public MonthTotalItemHolder(@NonNull View itemView) {
                super(itemView);
            }
            //TODO
        }
    }
}