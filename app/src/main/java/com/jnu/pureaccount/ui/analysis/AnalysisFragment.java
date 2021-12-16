package com.jnu.pureaccount.ui.analysis;

import static com.jnu.pureaccount.utils.AccountTypeUtils.getNameByCode;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.jnu.pureaccount.R;
import com.jnu.pureaccount.adapter.AnalysisPercentBarAdapter;
import com.jnu.pureaccount.data.PercentBarItem;
import com.jnu.pureaccount.ui.history.MonthHistoryFragment;
import com.jnu.pureaccount.utils.CalendarUtils;
import com.jnu.pureaccount.utils.DataUtils;

import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnalysisFragment extends Fragment {
    private PieChart pieChart;
    private Button btnStartTime;
    private Button btnEndTime;
    private String startTime = new CalendarUtils().getNowDateString();
    private String endTime = new CalendarUtils().getNowDateString();
    private HashMap dataMap2 = new HashMap(); //各类型对应的金额
    private ArrayList<PercentBarItem> percentBarItems = new ArrayList<>();

    //RecyclerView
    private RecyclerView recyclerView;
    private AnalysisAdapter mAdapter;

    public static final int[] PIE_COLORS = {
            Color.rgb(181, 194, 202), Color.rgb(129, 216, 200), Color.rgb(241, 214, 145),
            Color.rgb(108, 176, 223), Color.rgb(195, 221, 155), Color.rgb(251, 215, 191),
            Color.rgb(237, 189, 189), Color.rgb(172, 217, 243)
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_analysis,container,false);
        pieChart = rootView.findViewById(R.id.pie_chart);
        initRecyclerView(rootView);
        updateData();
        initPieChart(rootView,pieChart,dataMap2,false);
        initSelectTimeButton(rootView);

        return rootView;
    }

    public class AnalysisAdapter extends AnalysisPercentBarAdapter{
        public AnalysisAdapter(Context context, ArrayList<PercentBarItem> list) {
            super(context, list);
        }
    }

    private void initRecyclerView(View view){
        //RecyclerView设置
        recyclerView = view.findViewById(R.id.percent_bar_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new AnalysisAdapter(getContext(), percentBarItems);
        recyclerView.setAdapter(mAdapter);
    }

    private HashMap getPercent(){
        //已转换为百分数
        HashMap percentMap = new HashMap();
        double sum = 0;
        for(int i=1;i<=10;i++){
            sum += (double)dataMap2.get(getNameByCode(i));
        }
        for(int i=1;i<=10;i++){
            if(dataMap2.get(getNameByCode(i))!=null) {
                percentMap.put(getNameByCode(i), (double) dataMap2.get(getNameByCode(i)) / sum *100);
            }
        }
        return percentMap;
    }

    private void updateData(){
        //仅支出
        HashMap dataMap = new HashMap();
        dataMap = new DataUtils(getContext()).QueryReasonAccount(startTime,endTime);
        dataMap2.clear();
        //将Code→金额改为Name→金额
        for(int i=1;i<=10;i++){
            if(dataMap.get(i)==null) dataMap2.put(getNameByCode(i),0.00);
            else dataMap2.put(getNameByCode(i),(double)dataMap.get(i));
            Log.e("An",getNameByCode(i)+" "+dataMap2.get(getNameByCode(i))+"");
        }
        updateBarChart();
    }

    private void updateBarChart(){
        HashMap percentMap = getPercent(); // Name→Percent（仅支出）

        List<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(percentMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            //降序排序
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                //return o1.getValue().compareTo(o2.getValue());
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        percentBarItems.clear();
        for (Map.Entry<String, Double> mapping : list) {
            //为0的不显示
            if(mapping.getValue()>0) {
                percentBarItems.add(new PercentBarItem(mapping.getKey(),
                        (int) ((double) mapping.getValue()),
                        (double) dataMap2.get(mapping.getKey())));
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initSelectTimeButton(View view){
        btnStartTime = view.findViewById(R.id.btn_start_time);
        btnEndTime = view.findViewById(R.id.btn_end_time);
        btnStartTime.setText(startTime);
        btnEndTime.setText(endTime);
        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth) {
                        startTime = new CalendarUtils().IntToTimeString(startYear,startMonthOfYear+1,startDayOfMonth);
                        Log.e("stream",startTime);
                        updateData();
                        setPieChartData(pieChart,dataMap2);
                        btnStartTime.setText(startTime);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });
        btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), 0, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear, int startDayOfMonth) {
                        endTime = new CalendarUtils().IntToTimeString(startYear,startMonthOfYear+1,startDayOfMonth);
                        Log.e("stream",endTime);
                        updateData();
                        setPieChartData(pieChart,dataMap2);
                        btnEndTime.setText(endTime);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
                datePickerDialog.show();
            }
        });
    }

    private void initPieChart(View view, PieChart pieChart, HashMap<String, Double> pieValues, boolean showLegend){
        pieChart.setUsePercentValues(true);//设置使用百分比
        pieChart.getDescription().setEnabled(false);//设置描述
        pieChart.setExtraOffsets(25, 10, 25, 25); //设置边距
        pieChart.setEntryLabelColor(Color.GRAY);//设置标签颜色
        pieChart.setRotationEnabled(false);//不可旋转
        pieChart.setHighlightPerTapEnabled(true);//点击是否放大
        pieChart.setTransparentCircleRadius(61f);//设置半透明圆环的半径,看着就有一种立体的感觉
        //这个方法为true就是环形图，为false就是饼图
        pieChart.setDrawHoleEnabled(true);
        //设置环形中间空白颜色是白色
        pieChart.setHoleColor(Color.WHITE);
        //设置半透明圆环的颜色
        pieChart.setTransparentCircleColor(Color.WHITE);
        //设置半透明圆环的透明度
        pieChart.setTransparentCircleAlpha(110);
        pieChart.animateY(1400, Easing.EaseInOutQuart);

        //图例设置
        Legend legend = pieChart.getLegend();
        if (showLegend) {
            legend.setEnabled(true);//是否显示图例
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);//图例相对于图表横向的位置
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//图例相对于图表纵向的位置
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);//图例显示的方向
            legend.setDrawInside(false);
            legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
        } else {
            legend.setEnabled(false);
        }

        //设置饼图数据
        setPieChartData(pieChart, pieValues);

        //pieChart.animateX(1500, Easing.EasingOption.EaseInOutQuad);//数据显示动画

    }

    private void setPieChartData(PieChart pieChart, HashMap<String, Double> pieValues) {

        List<PieEntry> entries = new ArrayList<>();
        Set set = pieValues.entrySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            if((Double)entry.getValue() > 0)
                entries.add(new PieEntry((float) Double.parseDouble(entry.getValue().toString()), entry.getKey().toString()));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(1f);//设置饼块之间的间隔
        dataSet.setSelectionShift(20f);//设置饼块选中时偏离饼图中心的距离
        dataSet.setColors(PIE_COLORS);//设置饼块的颜色

        //设置数据显示方式
        dataSet.setValueLinePart1OffsetPercentage(60f);//数据连接线前半段距图形片内部边界的距离，为百分数
        dataSet.setValueLinePart1Length(0.5f);//前半段长度
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setValueLineColor(Color.rgb(238, 187, 0));//设置连接线的颜色
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart)); //要传入饼状图对象才会显示百分号
        pieData.setValueTextSize(15f);
        pieData.setValueTextColor(Color.DKGRAY);

        pieChart.setData(pieData);
        pieChart.highlightValues(null);
        pieChart.animateY(1400, Easing.EaseInOutQuart);
        pieChart.invalidate();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}