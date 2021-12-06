package com.jnu.pureaccount.event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jnu.pureaccount.R;
import com.jnu.pureaccount.utils.AndroidBarUtils;

public class ShowItemActivity extends AppCompatActivity {
    TextView tvAccount,tvReason,tvType,tvDate,tvCreateTime,tvRemarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item);

        //状态栏颜色更改
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        AndroidBarUtils.setBarDarkMode(this,true); //状态栏文字图标颜色为黑色

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //确保点击返回键后会顺便销毁当前Activity，从而观感保持与其他APP一致
                //否则观感比较违和
            }
        });

        tvAccount = findViewById(R.id.account);
        tvReason = findViewById(R.id.reason);
        tvType = findViewById(R.id.type);
        tvDate = findViewById(R.id.date);
        tvCreateTime = findViewById(R.id.createtime);

        Intent intent = getIntent();
        tvAccount.setText(intent.getDoubleExtra("account",0.00)+"");
        tvReason.setText(intent.getStringExtra("reason"));
        tvType.setText(intent.getStringExtra("type"));
        tvDate.setText(intent.getStringExtra("date"));
        tvCreateTime.setText(intent.getStringExtra("createTime").substring(0,19));
    }
}