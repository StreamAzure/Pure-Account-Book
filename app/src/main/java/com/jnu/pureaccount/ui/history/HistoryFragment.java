package com.jnu.pureaccount.ui.history;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.jnu.pureaccount.R;
import com.jnu.pureaccount.utils.MyDatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoryFragment extends Fragment {
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private String[] tabTitle;
    private List<Fragment> mFragments = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history,container,false);
        initTabLayout(rootView);
        mViewPager.setSaveEnabled(false);
        return rootView;
    }

    private void initTabLayout(View v){
        mTabLayout = v.findViewById(R.id.tl_tab);
        mViewPager = v.findViewById(R.id.vp_content);
        tabTitle = new String[]{"按日","按月","按年"};
        DayHistoryFragment dayHistoryFragment = new DayHistoryFragment();
        MonthHistoryFragment monthHistoryFragment = new MonthHistoryFragment();
        YearHistoryFragment yearHistoryFragment = new YearHistoryFragment();
        mFragments.add(dayHistoryFragment);
        mFragments.add(monthHistoryFragment);
        mFragments.add(yearHistoryFragment);

        mViewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return mFragments.get(position);
            }
            @Override
            public int getItemCount() {
                return 3;
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}