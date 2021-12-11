package com.jnu.pureaccount.ui.history;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.pureaccount.R;
import com.jnu.pureaccount.adapter.HomeItemAdapter;
import com.jnu.pureaccount.data.HomeItem;
import com.jnu.pureaccount.utils.CalendarUtils;
import com.jnu.pureaccount.utils.DataUtils;
import com.jnu.pureaccount.utils.MyDatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;


public class YearHistoryFragment extends Fragment {
    private FloatingActionButton floatingActionButton;
    private String selectYear;
    private TextView tvNoRecord;
    private RecyclerView recyclerView;
    YearAdapter mAdapter;
    ArrayList<HomeItem> mYearList;
    TreeMap<String, List<HomeItem>> listTreeMap;

    @Override
    public void onResume(){
        super.onResume();
        updateData();
    }

    private void updateData(){
        mYearList.clear();
        int[] intDate = new int[5];
        new CalendarUtils().TimeStringToInt(selectYear,intDate);
        boolean YearListExist = new DataUtils(getContext()).QueryMonthHistory(mYearList, listTreeMap, intDate[0],intDate[1]);
        if(!YearListExist) tvNoRecord.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
        //数据一多必出问题……但是position也很难用时间复杂度更好的方法获得，先将就一下吧
    }

    public YearHistoryFragment() {
        // Required empty public constructor
    }

    public static YearHistoryFragment newInstance(String param1, String param2) {
        YearHistoryFragment fragment = new YearHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectYear = new CalendarUtils().getNowDateString();
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_year_history, container, false);
        mYearList = new ArrayList<>();
        listTreeMap = new TreeMap<>();
        initRecyclerView(rootView);
        initFloatingActionButton(rootView);
        tvNoRecord = rootView.findViewById(R.id.no_record);
        return rootView;
    }

    private void initRecyclerView(View view){
        //RecyclerView设置
        recyclerView = view.findViewById(R.id.recycler_view_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(YearHistoryFragment.this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new YearHistoryFragment.YearAdapter(mYearList,getActivity(),getContext());
        recyclerView.setAdapter(mAdapter);
    }

    private void initFloatingActionButton(View rootView) {
        floatingActionButton = rootView.findViewById(R.id.fab_choose_year);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new MyDatePickerDialog(getActivity(), 0, new MyDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        selectYear = new CalendarUtils().IntToTimeString(startYear,startMonthOfYear+1,startDayOfMonth);
                        Log.e("stream",selectYear);
                        updateData();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE),1).show();
            }
        });
    }

    public class YearAdapter extends HomeItemAdapter {
        public YearAdapter(List<HomeItem> adpList, Activity activity, Context context) {
            super(adpList, activity, context);
        }

        @Override
        public void updateDataWhenDeleteItem() {
            updateData();
        }
    }
}