package com.jnu.pureaccount.ui.item;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jnu.pureaccount.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddExpendItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddExpendItemFragment extends Fragment {

    public AddExpendItemFragment(){

    }

    public static AddExpendItemFragment newInstance() {
        AddExpendItemFragment fragment = new AddExpendItemFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_add_expend_item,container,false);
        return rootView;
    }
}