package com.jnu.pureaccount.ui.item;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import static com.jnu.pureaccount.event.AddItemActivity.*;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jnu.pureaccount.R;
import com.jnu.pureaccount.event.AddItemActivity;
import com.jnu.pureaccount.utils.CalendarUtils;
import com.jnu.pureaccount.utils.DataUtils;
import com.jnu.pureaccount.utils.KeyBoardUtils;

public class AddIncomeItemFragment extends Fragment implements View.OnClickListener{

    public int selectItem = 0;
    public double account;
    public String selectDate;
    private LinearLayout linearLayout;
    private ImageButton btnSalary, btnWinning, btnInvestment, btnBusiness;
    private Button btnTime;
    private EditText etRemarks;
    //备注编辑框
    private TextView addItemReason;
    //账目类型名称显示
    private EditText accountEdit;
    //金额编辑框
    private KeyboardView keyboardView;
    //自定义软键盘

    int[] nowDate = new CalendarUtils().getNowDate();
    int[] IntSelectDate = new int[5];

    private void initSelectDate(){
        selectDate = new CalendarUtils().IntToTimeString(nowDate[0],nowDate[1],nowDate[2]);
        IntSelectDate = new CalendarUtils().TimeStringToInt(selectDate,IntSelectDate);
        btnTime.setText(IntSelectDate[1]+"月"+IntSelectDate[2]+"日");
    }

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
        initView(rootView);
        KeyBoardUtils keyBoardUtils = new KeyBoardUtils(keyboardView,accountEdit);
        keyBoardUtils.showKeyboard();

        initSelectDate();

        //初始化选择，默认选第一个
        linearLayout.setBackgroundColor(getResources().getColor(R.color.salary));
        addItemReason.setText(getResources().getString(R.string.salary));
        selectItem = ITEM_SALARY;

        if(operationTAG == OPERATION_EDIT){
            //如果是修改，说明之前有数据，相关控件的值初始时都要保持原状
            selectDate = previousSelectTime;
            selectItem = previousSelectItem;
            accountEdit.setHint(previousAccount+"");
        }

        //键盘的确认按钮
        keyBoardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener(){
            @Override
            public void onEnsure() {
                //点击"="号
                if(accountEdit.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "请输入金额！", Toast.LENGTH_SHORT).show();
                }
                else if(selectItem == 0){
                    Toast.makeText(getContext(),"请选择类型！",Toast.LENGTH_SHORT).show();
                }
                else {
                    DataUtils dataUtils = new DataUtils(getActivity());
                    if(operationTAG == OPERATION_ADD) {
                        dataUtils.InsertItemData(selectItem, Double.parseDouble(accountEdit.getText().toString()), selectDate);
                    }
                    else if(operationTAG == OPERATION_EDIT){
                        dataUtils.EditItemData(selectItem,Double.parseDouble(accountEdit.getText().toString()),selectDate,createTime);
                    }
                    getActivity().finish();
                }
            }
        });

        //选择时间的按钮
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //month是DatePicker时间选择器返回的month，也少1，手动补
                                selectDate = new CalendarUtils().IntToTimeString(year,month+1,dayOfMonth);
                                IntSelectDate = new CalendarUtils().TimeStringToInt(selectDate,IntSelectDate);
                                btnTime.setText(month+1+"月"+dayOfMonth+"日");
                            }
                        },IntSelectDate[0],IntSelectDate[1]-1,IntSelectDate[2]).show();
            }
        });


        //点击编辑框时隐藏自定义软键盘
        etRemarks.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    //etRemarks.requestFocus();
                    keyBoardUtils.hideKeyboard();
                    return false;
                }
                return false;
            }
        });

        //点击完成后编辑框失去焦点并收回键盘
        etRemarks.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    etRemarks.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        //点击金额编辑框时收回系统软键盘，弹出自定义软键盘
        accountEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    //etRemarks.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
                    keyBoardUtils.showKeyboard();
                    return false;
                }
                return false;
            }
        });

        return rootView;
    }

    private void initView(View rootView){
        linearLayout = rootView.findViewById(R.id.linear_layout_add_income_item);
        addItemReason = rootView.findViewById(R.id.tv_add_income_item_reason);
        btnTime = rootView.findViewById(R.id.btn_time);
        etRemarks = rootView.findViewById(R.id.et_remarks);
        accountEdit = rootView.findViewById(R.id.et_add_income_item_account);
        keyboardView = rootView.findViewById(R.id.keyBoard);

        btnSalary = rootView.findViewById(R.id.btn_add_income_item_salary);
        btnWinning = rootView.findViewById(R.id.btn_add_income_item_winning);
        btnInvestment = rootView.findViewById(R.id.btn_add_income_item_investment);
        btnBusiness = rootView.findViewById(R.id.btn_add_income_item_business);
        btnSalary.setOnClickListener(this);
        btnWinning.setOnClickListener(this);
        btnInvestment.setOnClickListener(this);
        btnBusiness.setOnClickListener(this);
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