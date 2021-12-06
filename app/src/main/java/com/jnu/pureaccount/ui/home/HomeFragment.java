package com.jnu.pureaccount.ui.home;

import static com.jnu.pureaccount.event.AddItemActivity.OPERATION_ADD;
import static com.jnu.pureaccount.event.AddItemActivity.OPERATION_EDIT;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
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
import com.jnu.pureaccount.event.ShowItemActivity;
import com.jnu.pureaccount.ui.item.AddExpendItemFragment;
import com.jnu.pureaccount.utils.CalendarUtils;
import com.jnu.pureaccount.utils.DataUtils;

import org.w3c.dom.Text;

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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
                intent.putExtra("operation", OPERATION_ADD);
                startActivity(intent);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        mHomeItemList = new ArrayList<>();
        listTreeMap = new TreeMap<String, List<HomeItem>>(Comparator.reverseOrder());
        //倒序显示
        DataUtils dataUtils = new DataUtils(this.getActivity());

        //dataUtils.DeleteTable("item");

        dataUtils.loadItemData(listTreeMap);
        arrangeData(listTreeMap);
    }

    boolean isNowMonth(AccountItem accountItem){
        int nowYear = new CalendarUtils().getNowDate()[0];
        //获取本年的年份
        int nowMonth = new CalendarUtils().getNowDate()[1] + 1;
        //获取本月的月份
        if(accountItem.getDate().get(Calendar.YEAR) == nowYear
                && accountItem.getDate().get(Calendar.MONTH) + 1 == nowMonth){
            return true;
        }
        else return false;
    }

    private void arrangeData(TreeMap<String, List<HomeItem>> treeMap){
        //加载数据库，注意数据库中目前只有账目的数据
        DataUtils dataUtils = new DataUtils(this.getActivity());
        dataUtils.loadItemData(listTreeMap);
        //重整为要传入Adapter的数据源mHomeItemList
        mHomeItemList.clear();
        MonthTotalItem monthTotalItem = new MonthTotalItem(0,0);
        mHomeItemList.add(monthTotalItem);
        //第一项是本月统计
        double monthIncome = 0, monthExpend = 0;

        Iterator<Map.Entry<String,List<HomeItem>>> it = treeMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String,List<HomeItem>> entry = it.next();
            List<HomeItem> homeItems = entry.getValue();
            //获取对应的homeItem列表
            Calendar calendar = ((AccountItem)homeItems.get(0)).getDate();
            DayTotalItem dayTotalItem = new DayTotalItem(calendar,0,0);
            mHomeItemList.add(dayTotalItem);
            //加入对应日期Item
            double incomeSubTotal = 0, expendSubTotal = 0;
            for(int i = 0 ;i < homeItems.size();i++){
                mHomeItemList.add(homeItems.get(i));
                AccountItem accountItem = ((AccountItem)homeItems.get(i));
                if(accountItem.getType()==0){
                    expendSubTotal += accountItem.getAccount();
                    if(isNowMonth(accountItem)){
                        monthExpend += accountItem.getAccount();
                    }
                }
                else {
                    incomeSubTotal += accountItem.getAccount();
                    if(isNowMonth(accountItem)){
                        monthIncome += accountItem.getAccount();
                    }
                }
            }
            //更新日统计数据。笨办法，等数据库熟练了再优化
            if(mHomeItemList.get(mHomeItemList.size()- homeItems.size()-1) instanceof DayTotalItem){
                //Log.e("HomeFragment","DayTotalItem position正确");
                dayTotalItem = (DayTotalItem) mHomeItemList.get(mHomeItemList.size()-homeItems.size()-1);
                dayTotalItem.setExpendSubTotal(expendSubTotal);
                dayTotalItem.setIncomeSubTotal(incomeSubTotal);
            }
        }

        //更新月统计数据
        ((MonthTotalItem)mHomeItemList.get(0)).setExpend(monthExpend);
        ((MonthTotalItem)mHomeItemList.get(0)).setIncome(monthIncome);

        //debug
        Log.e("HomeFragment","===========账目全部信息===========");
        for(int i = 0;i < mHomeItemList.size();i++){
            if(mHomeItemList.get(i) instanceof AccountItem){
                AccountItem accountItem = (AccountItem) mHomeItemList.get(i);
                Log.e("HomeFragment",
                        accountItem.getTitle(getContext(),accountItem.getReason())
                        +" "+accountItem.getAccount()
                        +" TagTime:"+accountItem.getTagDate()
                        +" CreateTime:"+accountItem.getCreateTime());
            }
            else if(mHomeItemList.get(i) instanceof DayTotalItem){
                DayTotalItem dayTotalItem = (DayTotalItem) mHomeItemList.get(i);
                Log.e("HomeFragment", dayTotalItem.getPrintDate());
            }
        }
        Log.e("HomeFragment","==============结束==============");
        dataUtils.QueryTable("daysum");
        dataUtils.QueryTable("monthsum");
        dataUtils.QueryTable("yearsum");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public class HomeItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private static final int VIEW_TYPE_ACCOUNT_ITEM = 100;
        private static final int VIEW_TYPE_MONTH_TOTAL_ITEM = 200;
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
                if(accountItem.getType()==0) {
                    viewHolder.account.setText("-"+String.format("%.2f",accountItem.getAccount()));
                    viewHolder.account.setTextColor(getResources().getColor(R.color.expend));
                }
                else{
                    viewHolder.account.setText("+"+String.format("%.2f",accountItem.getAccount()));
                    viewHolder.account.setTextColor(getResources().getColor(R.color.income));
                }
            }
            else if(holder instanceof MonthTotalItemHolder){
                MonthTotalItemHolder viewHolder = (MonthTotalItemHolder) holder;
                MonthTotalItem monthTotalItem = (MonthTotalItem) adpList.get(position);
                viewHolder.expend.setText("-" + String.format("%.2f",monthTotalItem.getExpend()));
                viewHolder.income.setText("+" + String.format("%.2f",monthTotalItem.getIncome()));
                viewHolder.expend.setTextColor(getResources().getColor(R.color.expend));
                viewHolder.income.setTextColor(getResources().getColor(R.color.income));
                //TODO: viewHolder.total.setText();
            }
            else if(holder instanceof DayTotalItemHolder){
                DayTotalItemHolder viewHolder = (DayTotalItemHolder) holder;
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
                        intent = new Intent(getActivity(),AddItemActivity.class);
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
                        startActivity(intent);
                        break;
                    case MENU_ITEM_DELETE:
                        //弹出对话框，只有文本提示、确定和取消，可以不写自定义布局
                        AlertDialog.Builder bookDeleteDialog = new AlertDialog.Builder(HomeFragment.this.getContext());
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
        class MonthTotalItemHolder extends RecyclerView.ViewHolder{
            TextView total;
            TextView income;
            TextView expend;
            public MonthTotalItemHolder(@NonNull View itemView) {
                super(itemView);
                total = itemView.findViewById(R.id.tv_total);
                income = itemView.findViewById(R.id.tv_monthtotal_income);
                expend = itemView.findViewById(R.id.tv_monthtotal_expend);
            }
        }
    }
}