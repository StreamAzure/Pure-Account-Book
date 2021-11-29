package com.jnu.pureaccount.event;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jnu.pureaccount.R;
import com.jnu.pureaccount.utils.AndroidBarUtils;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        AndroidBarUtils.setBarDarkMode(this,true); //状态栏文字图标颜色为黑色
    }
}