package com.jnu.pureaccount.event;

import static com.jnu.pureaccount.ui.home.HomeFragment.RESULT_CODE_ADD_OK;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.pureaccount.R;
import com.jnu.pureaccount.ui.item.AddExpendItemFragment;
import com.jnu.pureaccount.ui.item.AddIncomeItemFragment;
import com.jnu.pureaccount.utils.AndroidBarUtils;
import com.jnu.pureaccount.utils.CalendarUtils;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity{
    public final static int ITEM_FOOD = 1;
    public final static int ITEM_ENTERTAINMENT = ITEM_FOOD +1;
    public final static int ITEM_CLOTHES = ITEM_FOOD +2;
    public final static int ITEM_PETS = ITEM_FOOD +3;
    public final static int ITEM_HOUSERENT = ITEM_FOOD +4;
    public final static int ITEM_MEDICINE = ITEM_FOOD +5;
    public final static int ITEM_SHOPPING = ITEM_FOOD +6;
    public final static int ITEM_TRAFFIC = ITEM_FOOD +7;
    public final static int ITEM_TOUR = ITEM_FOOD +8;
    public final static int ITEM_STUDY = ITEM_FOOD +9;
    public final static int ITEM_SALARY = ITEM_FOOD +10;
    public final static int ITEM_WINNING = ITEM_FOOD +11;
    public final static int ITEM_INVESTMENT = ITEM_FOOD +12;
    public final static int ITEM_BUSINESS = ITEM_FOOD +13;

    public final static int OPERATION_EDIT = 500;
    public final static int OPERATION_ADD = 600;

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private String[] tabTitle;
    private List<Fragment> mFragments = new ArrayList<>();

    public static int operationTAG = OPERATION_ADD;
    public static String previousSelectTime;
    public static double previousAccount;
    public static int previousSelectItem;
    public static String createTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //状态栏颜色更改
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        AndroidBarUtils.setBarDarkMode(this,true); //状态栏文字图标颜色为黑色

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //确保点击返回键后会顺便销毁当前Activity，从而观感保持与其他APP一致
                //否则观感比较违和
            }
        });
        initTabLayout();

        //接收从HomeFragment传来的操作标记
        Intent intent = getIntent();
        operationTAG = intent.getIntExtra("operation",OPERATION_ADD);
        previousAccount = intent.getDoubleExtra("previousAccount",0);
        previousSelectTime = intent.getStringExtra("previousSelectTime");
        previousSelectItem = intent.getIntExtra("previousSelectItem",14);
        createTime = intent.getStringExtra("createTime");

    }

    private void initTabLayout(){
        mTabLayout = findViewById(R.id.tl_tab);
        mViewPager = findViewById(R.id.vp_content);
        tabTitle = new String[]{"收入","支出"};
        AddIncomeItemFragment incomeFragment = new AddIncomeItemFragment();
        AddExpendItemFragment expendFragment = new AddExpendItemFragment();
        mFragments.add(incomeFragment);
        mFragments.add(expendFragment);

        mViewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return mFragments.get(position);
            }
            @Override
            public int getItemCount() {
                return 2;
            }
        });

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(mTabLayout, mViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabTitle[position]);
            }
        });
        tabLayoutMediator.attach();
    }
}