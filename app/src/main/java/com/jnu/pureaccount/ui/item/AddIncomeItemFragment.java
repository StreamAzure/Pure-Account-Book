package com.jnu.pureaccount.ui.item;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import static com.jnu.pureaccount.event.AddItemActivity.*;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public int selectItem;
    public int account;
    public String selectDate;
    private LinearLayout linearLayout;
    private ImageButton btnSalary, btnWinning, btnInvestment, btnBusiness;
    private Button btnTime, btnRemark;
    //时间选择按钮；备注按钮，点击后再弹出编辑框
    private TextView addItemReason;
    //账目类型名称显示
    private EditText accountEdit;
    //金额编辑框
    private KeyboardView keyboardView;
    //自定义软键盘

    int[] nowDate = new CalendarUtils().getNowDate();
    int[] IntSelectDate = new int[5];

    private void initSelectDate(){
        selectDate = new CalendarUtils().IntToTimeString(nowDate[0],nowDate[1]+1,nowDate[2]);
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

        //键盘的确认按钮
        keyBoardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener(){
            @Override
            public void onEnsure() {
                //点击"="号
                if(accountEdit.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "请输入金额！", Toast.LENGTH_SHORT).show();
                }
                else {
                    DataUtils dataUtils = new DataUtils(getActivity());
                    dataUtils.InsertItemData(selectItem, Integer.parseInt(accountEdit.getText().toString()), selectDate);
                    Log.e("AddIncomeItemFragment","selectDate: "+selectDate);
                    getActivity().finish();
                }
            }
        });

        //TODO：备注功能：点击后呼出一个上半部分透明的fragment，一个编辑框
        //输入完改变按钮文字，注意长度限制
        //至于内容放哪以后再说T_T
        btnRemark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                                selectDate = new CalendarUtils().IntToTimeString(year,month+1,dayOfMonth);
                                IntSelectDate = new CalendarUtils().TimeStringToInt(selectDate,IntSelectDate);
                                btnTime.setText(month+1+"月"+dayOfMonth+"日");
                            }
                        },IntSelectDate[0],IntSelectDate[1]-1,IntSelectDate[2]).show();
            }
        });

        return rootView;
    }

    private void initView(View rootView){
        linearLayout = rootView.findViewById(R.id.linear_layout_add_income_item);
        addItemReason = rootView.findViewById(R.id.tv_add_income_item_reason);
        btnTime = rootView.findViewById(R.id.btn_time);
        btnRemark = rootView.findViewById(R.id.btn_remarks);
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