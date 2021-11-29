package com.jnu.pureaccount.ui.item;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnu.pureaccount.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddIncomeItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddIncomeItemFragment extends Fragment {

    public AddIncomeItemFragment(){

    }

    public static AddIncomeItemFragment newInstance() {
        AddIncomeItemFragment fragment = new AddIncomeItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_income_item,container,false);
        return rootView;
    }
}