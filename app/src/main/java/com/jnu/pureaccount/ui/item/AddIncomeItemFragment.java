package com.jnu.pureaccount.ui.item;

import android.os.Bundle;
import static com.jnu.pureaccount.event.AddItemActivity.*;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jnu.pureaccount.R;
import com.jnu.pureaccount.event.AddItemActivity;

public class AddIncomeItemFragment extends Fragment implements View.OnClickListener{

    public int selectItem;
    private LinearLayout linearLayout;
    private ImageButton btnSalary, btnWinning, btnInvestment, btnBusiness;
    private TextView addItemReason;

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
        linearLayout = rootView.findViewById(R.id.linear_layout_add_income_item);
        addItemReason = rootView.findViewById(R.id.tv_add_income_item_reason);

        btnSalary = rootView.findViewById(R.id.btn_add_income_item_salary);
        btnWinning = rootView.findViewById(R.id.btn_add_income_item_winning);
        btnInvestment = rootView.findViewById(R.id.btn_add_income_item_investment);
        btnBusiness = rootView.findViewById(R.id.btn_add_income_item_business);
        btnSalary.setOnClickListener(this);
        btnWinning.setOnClickListener(this);
        btnInvestment.setOnClickListener(this);
        btnBusiness.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add_income_item_salary:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.salary));
                addItemReason.setText(getResources().getString(R.string.salary));
                selectItem = ITEM_SALARY;
                break;
            case R.id.btn_add_income_item_winning:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.winning));
                addItemReason.setText(getResources().getString(R.string.winning));
                selectItem = ITEM_WINNING;
                break;
            case R.id.btn_add_income_item_investment:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.investment));
                addItemReason.setText(getResources().getString(R.string.investment));
                selectItem = ITEM_INVESTMENT;
                break;
            case R.id.btn_add_income_item_business:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.business));
                addItemReason.setText(getResources().getString(R.string.business));
                selectItem = ITEM_BUSINESS;
                break;
        }
    }
}