package com.jnu.pureaccount.ui.item;


import static com.jnu.pureaccount.event.AddItemActivity.*;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jnu.pureaccount.R;
import com.jnu.pureaccount.event.AddItemActivity;

public class AddExpendItemFragment extends Fragment implements View.OnClickListener {

    public int selectItem;
    private LinearLayout linearLayout;
    private ImageButton btnFood, btnEntertainment, btnClothes, btnPet, btnHouseRent, btnMedicine, btnShopping, btnTraffic, btnTour, btnStudy;
    private Button btnOk, btnCancel;
    private TextView addItemReason;

    // ------Fragment向Activity传数据的接口------
    private CallBackInterface callBackInterface;
    public interface CallBackInterface{
        void getValues(Bundle bundle);
    }

    public AddExpendItemFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_add_expend_item, container, false);
        linearLayout = rootView.findViewById(R.id.linear_layout_add_income_item);
        addItemReason = rootView.findViewById(R.id.tv_add_income_item_reason);
        btnCancel = rootView.findViewById(R.id.btn_cancel);
        btnOk = rootView.findViewById(R.id.btn_ok);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("reason", selectItem);
                //TODO 往AddItemActivity传数据，账目类型+金额
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        btnFood = rootView.findViewById(R.id.btn_add_expend_item_food);
        btnEntertainment = rootView.findViewById(R.id.btn_add_expend_item_entertainment);
        btnClothes = rootView.findViewById(R.id.btn_add_expend_item_clothes);
        btnPet = rootView.findViewById(R.id.btn_add_expend_item_pet);
        btnHouseRent = rootView.findViewById(R.id.btn_add_expend_item_houserent);
        btnMedicine = rootView.findViewById(R.id.btn_add_expend_item_medicine);
        btnShopping = rootView.findViewById(R.id.btn_add_expend_item_shopping);
        btnTraffic = rootView.findViewById(R.id.btn_add_expend_item_traffic);
        btnTour = rootView.findViewById(R.id.btn_add_expend_item_tour);
        btnStudy = rootView.findViewById(R.id.btn_add_expend_item_study);
        btnFood.setOnClickListener(this);
        btnEntertainment.setOnClickListener(this);
        btnPet.setOnClickListener(this);
        btnClothes.setOnClickListener(this);
        btnHouseRent.setOnClickListener(this);
        btnMedicine.setOnClickListener(this);
        btnTour.setOnClickListener(this);
        btnShopping.setOnClickListener(this);
        btnTraffic.setOnClickListener(this);
        btnStudy.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick (View v){
        switch (v.getId()) {
            case R.id.btn_add_expend_item_food:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.food));
                addItemReason.setText(getResources().getString(R.string.food));
                selectItem = ITEM_FOOD;
                Log.e("MYLOG","selectItem "+selectItem);
                break;
            case R.id.btn_add_expend_item_entertainment:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.entertainment));
                addItemReason.setText(getResources().getString(R.string.entertainment));
                selectItem = ITEM_ENTERTAINMENT;
                Log.e("MYLOG","selectItem "+selectItem);
                break;
            case R.id.btn_add_expend_item_clothes:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.clothes));
                addItemReason.setText(getResources().getString(R.string.clothes));
                selectItem = ITEM_CLOTHES;
                Log.e("MYLOG","selectItem "+selectItem);
                break;
            case R.id.btn_add_expend_item_pet:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.makeup));
                addItemReason.setText(getResources().getString(R.string.pets));
                selectItem = ITEM_PETS;
                Log.e("MYLOG","selectItem "+selectItem);
                break;
            case R.id.btn_add_expend_item_houserent:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.houserent));
                addItemReason.setText(getResources().getString(R.string.houserent));
                selectItem = ITEM_HOUSERENT;
                Log.e("MYLOG","selectItem "+selectItem);
                break;
            case R.id.btn_add_expend_item_medicine:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.medicine));
                addItemReason.setText(getResources().getString(R.string.medicine));
                selectItem = ITEM_MEDICINE;
                Log.e("MYLOG","selectItem "+selectItem);
                break;
            case R.id.btn_add_expend_item_shopping:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.shopping));
                addItemReason.setText(getResources().getString(R.string.shopping));
                selectItem = ITEM_SHOPPING;
                Log.e("MYLOG","selectItem "+selectItem);
                break;
            case R.id.btn_add_expend_item_traffic:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.traffic));
                addItemReason.setText(getResources().getString(R.string.traffic));
                selectItem = ITEM_TRAFFIC;
                Log.e("MYLOG","selectItem "+selectItem);
                break;
            case R.id.btn_add_expend_item_tour:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.tour));
                addItemReason.setText(getResources().getString(R.string.tour));
                selectItem = ITEM_TOUR;
                Log.e("MYLOG","selectItem "+selectItem);
                break;
            case R.id.btn_add_expend_item_study:
                linearLayout.setBackgroundColor(getResources().getColor(R.color.study));
                addItemReason.setText(getResources().getString(R.string.study));
                selectItem = ITEM_STUDY;
                Log.e("MYLOG","selectItem "+selectItem);
                break;
        }
    }
}