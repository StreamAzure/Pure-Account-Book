package com.jnu.pureaccount.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.pureaccount.R;
import com.jnu.pureaccount.ui.item.AddExpendItemFragment;
import com.jnu.pureaccount.ui.item.AddIncomeItemFragment;
import com.jnu.pureaccount.utils.AndroidBarUtils;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private String[] tabTitle;
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

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