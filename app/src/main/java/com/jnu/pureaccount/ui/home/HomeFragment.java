package com.jnu.pureaccount.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.jnu.pureaccount.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    List<HomeItem> mHomeItemList;
    HomeItemAdapter mAdapter;


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
        mHomeItemList = new ArrayList<>();
        mHomeItemList.add(new AccountItem(R.drawable.ic_menu_camera,1,1,true,new Date()));
        Log.e("MYTAG",Integer.toString(((AccountItem)mHomeItemList.get(0)).getReason()));
        mHomeItemList.add(new AccountItem(R.drawable.ic_launcher_foreground,2,2,true,new Date()));
        Log.e("MYTAG",Integer.toString(((AccountItem)mHomeItemList.get(1)).getReason()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public class HomeItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private static final int VIEW_TYPE_ACCOUNT_ITEM = 1;
        private static final int VIEW_TYPE_MONTH_TOTAL_ITEM = 2;
        private static final int VIEW_TYPE_DAY_TOTAL_ITEM = 3;

        private final List<HomeItem> adpHomeItemList;

        public HomeItemAdapter(List<HomeItem> adpHomeItemList) {
            this.adpHomeItemList = adpHomeItemList;
        }

        @Override
        public int getItemViewType(int position){
            if(mHomeItemList.get(position) instanceof AccountItem){
                return VIEW_TYPE_ACCOUNT_ITEM;
            }
            else if(mHomeItemList.get(position) instanceof MonthTotalItem){
                return VIEW_TYPE_MONTH_TOTAL_ITEM;
            }
            else if(mHomeItemList.get(position) instanceof DayTotalItem){
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
                AccountItem accountItem = (AccountItem) mHomeItemList.get(position);
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
                //TODO
            }
        }

        @Override
        public int getItemCount() {
            return mHomeItemList.size();
        }

        class AccountItemHolder extends RecyclerView.ViewHolder{
            public AccountItemHolder(@NonNull View itemView) {
                super(itemView);
            }
            private ImageView icon = itemView.findViewById(R.id.iv_item_account_icon);
            private TextView account = itemView.findViewById(R.id.tv_item_account_account);
            private TextView reason = itemView.findViewById(R.id.tv_item_account_reason);
        }
        class DayTotalItemHolder extends RecyclerView.ViewHolder{
            public DayTotalItemHolder(@NonNull View itemView) {
                super(itemView);
            }
            //TODO
        }
        class MonthTotalItemHolder extends RecyclerView.ViewHolder{
            public MonthTotalItemHolder(@NonNull View itemView) {
                super(itemView);
            }
            //TODO
        }
    }
}