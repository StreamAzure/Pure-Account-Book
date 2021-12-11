package com.jnu.pureaccount.ui.history;

import static com.jnu.pureaccount.event.AddItemActivity.OPERATION_EDIT;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
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
import com.jnu.pureaccount.adapter.HomeItemAdapter;
import com.jnu.pureaccount.data.AccountItem;
import com.jnu.pureaccount.data.DayTotalItem;
import com.jnu.pureaccount.data.HomeItem;
import com.jnu.pureaccount.event.AddItemActivity;
import com.jnu.pureaccount.event.ShowItemActivity;
import com.jnu.pureaccount.utils.CalendarUtils;
import com.jnu.pureaccount.utils.DataUtils;
import com.jnu.pureaccount.utils.MyDatePickerDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeMap;

public class MonthHistoryFragment extends Fragment {
    private FloatingActionButton floatingActionButton;
    private String selectMonth;
    private TextView tvNoRecord;
    private RecyclerView recyclerView;
    MonthAdapter mAdapter;
    ArrayList<HomeItem> mMonthList;
    TreeMap<String,List<HomeItem>> listTreeMap;

    @Override
    public void onResume(){
        super.onResume();
        updateData();
    }

    private void updateData(){
        mMonthList.clear();
        int[] intDate = new int[5];
        new CalendarUtils().TimeStringToInt(selectMonth,intDate);
        boolean MonthListExist = new DataUtils(getContext()).QueryMonthHistory(mMonthList, listTreeMap, intDate[0],intDate[1]);
        if(!MonthListExist) tvNoRecord.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
        //数据一多必出问题……但是position也很难用时间复杂度更好的方法获得，先将就一下吧
    }

    public MonthHistoryFragment() {
        // Required empty public constructor
    }

    public static MonthHistoryFragment newInstance(String param1, String param2) {
        MonthHistoryFragment fragment = new MonthHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectMonth = new CalendarUtils().getNowDateString();
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_month_history, container, false);
        mMonthList = new ArrayList<>();
        listTreeMap = new TreeMap<>();
        initRecyclerView(rootView);
        initFloatingActionButton(rootView);
        tvNoRecord = rootView.findViewById(R.id.no_record);
        return rootView;
    }

    private void initRecyclerView(View view){
        //RecyclerView设置
        recyclerView = view.findViewById(R.id.recycler_view_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MonthHistoryFragment.this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MonthAdapter(mMonthList,getActivity(),getContext());
        recyclerView.setAdapter(mAdapter);
    }

    private void initFloatingActionButton(View rootView){
        floatingActionButton = rootView.findViewById(R.id.fab_choose_month);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new MyDatePickerDialog(getActivity(), 0, new MyDatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth) {
                        selectMonth = new CalendarUtils().IntToTimeString(startYear,startMonthOfYear+1,startDayOfMonth);
                        Log.e("stream",selectMonth);
                        updateData();
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE),0).show();
            }
        });
    }

    public class MonthAdapter extends HomeItemAdapter {
        public MonthAdapter(List<HomeItem> adpList, Activity activity, Context context) {
            super(adpList, activity, context);
        }

        @Override
        public void updateDataWhenDeleteItem() {
            updateData();
        }
    }
}