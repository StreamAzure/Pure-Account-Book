package com.jnu.pureaccount.ui.history;

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


public class YearHistoryFragment extends Fragment {
    private FloatingActionButton floatingActionButton;
    private String selectYear;

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
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_year_history, container, false);
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
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE),1).show();
            }
        });
        return rootView;
    }
}