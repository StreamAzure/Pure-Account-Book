package com.jnu.pureaccount;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.jnu.pureaccount.event.AddItemActivity;
import com.jnu.pureaccount.ui.analysis.AnalysisFragment;
import com.jnu.pureaccount.ui.history.HistoryFragment;
import com.jnu.pureaccount.ui.home.HomeFragment;
import com.jnu.pureaccount.utils.AndroidBarUtils;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private HomeFragment homeFragment;
    private HistoryFragment historyFragment;
    private AnalysisFragment analysisFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //状态栏颜色更改
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.background));

        AndroidBarUtils.setBarDarkMode(this,true); //状态栏文字图标颜色为黑色

        bindView();
        initToolBar();
    }

    private void initToolBar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation);
        toolbar.setTitle("纯记账");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
    }

    private void bindView(){
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        //初始Fragment就是HomeFragment
        homeFragment = new HomeFragment();
        initFragment(homeFragment);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    if(homeFragment == null) {
                        homeFragment = new HomeFragment();
                    }
                    initFragment(homeFragment);
                }
                else if (item.getItemId()==R.id.nav_history){
                    if(historyFragment == null) {
                        historyFragment = new HistoryFragment();
                    }
                    initFragment(historyFragment);
                }
                else if(item.getItemId() == R.id.nav_analysis){
                    if(analysisFragment == null) {
                        analysisFragment = new AnalysisFragment();
                    }
                    initFragment(analysisFragment);
                }
                //向左收回抽屉
                drawerLayout.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
    }

    //侧滑菜单的按钮切换Fragment
    private void initFragment(Fragment fragment){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }
}