package com.jnu.pureaccount.ui.history;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.pureaccount.R;
import com.jnu.pureaccount.utils.CalendarUtils;
import com.jnu.pureaccount.utils.MyDatePickerDialog;

import java.util.Calendar;

public class MonthHistoryFragment extends Fragment {
    private FloatingActionButton floatingActionButton;
    private String selectMonth;

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
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_month_history, container, false);
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
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE),0).show();
            }
        });
        return rootView;
    }
}