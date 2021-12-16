package com.jnu.pureaccount.ui.history;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.pureaccount.R;
import com.jnu.pureaccount.adapter.HomeItemAdapter;
import com.jnu.pureaccount.data.HomeItem;
import com.jnu.pureaccount.data.ShowYearData;
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
    private BarChart barChart;
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
        boolean YearListExist = new DataUtils(getContext()).QueryYearHistory(mYearList, listTreeMap, intDate[0]);
        if(YearListExist) tvNoRecord.setVisibility(View.GONE);
        else tvNoRecord.setVisibility(View.VISIBLE);
        mAdapter.notifyDataSetChanged();
        //数据一多必出问题……但是position也很难用时间复杂度更好的方法获得，先将就一下吧
        updateBarChart();
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
        initBarChart(rootView);
        tvNoRecord = rootView.findViewById(R.id.no_record);
        return rootView;
    }

    private void updateBarChart(){
        List<ShowYearData> showYearDataList  = new ArrayList<>();
        int[] tmp = new int[5];
        new CalendarUtils().TimeStringToInt(selectYear,tmp);
        int year = tmp[0];
        for(int i = 1;i <= 12;i++){
            showYearDataList.add(new ShowYearData(getContext(),year,i));
        }
        //-------支出数据集-------
        List<BarEntry> expendEntries = new ArrayList<>();
        for(int i = 0;i <showYearDataList.size();i++){
            expendEntries.add(new BarEntry(showYearDataList.get(i).getMonth(),
                    (float)showYearDataList.get(i).getExpend()));
        }
        BarDataSet expendDataSet = new BarDataSet(expendEntries,"月支出");
        expendDataSet.setColor(Color.rgb(129, 216, 200));

        //-------收入数据集-------
        List<BarEntry> incomeEntries = new ArrayList<>();
        for(int i=0;i<showYearDataList.size();i++){
            incomeEntries.add(new BarEntry(showYearDataList.get(i).getMonth(),
                    (float)showYearDataList.get(i).getIncome()));
        }
        BarDataSet incomeDataSet = new BarDataSet(incomeEntries,"月收入");
        incomeDataSet.setColor( Color.rgb(241, 214, 145));

        ArrayList<IBarDataSet> barDataSets = new ArrayList<>();
        barDataSets.add(expendDataSet);
        barDataSets.add(incomeDataSet);

        BarData barData = new BarData(barDataSets);
        barChart.setData(barData);
        barChart.getBarData().setBarWidth(0.45f);
        barChart.groupBars(0, 0.04f/*组与组之间的距离*/, 0.03f/*组中每个柱子之间的距离*/);
        barChart.invalidate();
    }

    private void initBarChart(View view){
        barChart = view.findViewById(R.id.bar_chart);

        List<ShowYearData> showYearDataList  = new ArrayList<>();
        int[] tmp = new int[5];
        new CalendarUtils().TimeStringToInt(selectYear,tmp);
        int year = tmp[0];
        for(int i = 1;i <= 12;i++){
            showYearDataList.add(new ShowYearData(getContext(),year,i));
        }
        //-------支出数据集-------
        List<BarEntry> expendEntries = new ArrayList<>();
        for(int i = 0;i <showYearDataList.size();i++){
            expendEntries.add(new BarEntry(showYearDataList.get(i).getMonth(),
                    (float)showYearDataList.get(i).getExpend()));
        }
        BarDataSet expendDataSet = new BarDataSet(expendEntries,"月支出");
        expendDataSet.setColor(Color.rgb(129, 216, 200));

        //-------收入数据集-------
        List<BarEntry> incomeEntries = new ArrayList<>();
        for(int i=0;i<showYearDataList.size();i++){
            incomeEntries.add(new BarEntry(showYearDataList.get(i).getMonth(),
                    (float)showYearDataList.get(i).getIncome()));
        }
        BarDataSet incomeDataSet = new BarDataSet(incomeEntries,"月收入");
        incomeDataSet.setColor( Color.rgb(241, 214, 145));

        ArrayList<IBarDataSet> barDataSets = new ArrayList<>();
        barDataSets.add(expendDataSet);
        barDataSets.add(incomeDataSet);

        BarData barData = new BarData(barDataSets);
        barChart.setData(barData);
        barChart.setTouchEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisRight().setEnabled(false);//禁用右侧坐标轴
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getBarData().setBarWidth(0.45f);
        barChart.groupBars(0, 0.04f/*组与组之间的距离*/, 0.03f/*组中每个柱子之间的距离*/);
        barChart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);

        barChart.invalidate();
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