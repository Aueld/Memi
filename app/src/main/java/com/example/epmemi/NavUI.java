package com.example.epmemi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.w3c.dom.Text;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

// Nav UI 최상단의 부모클래스로 정의
public class NavUI extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    protected Context context;

    public DrawerLayout drawerLayout;
    public NavigationView navigationView;
    public ActionBarDrawerToggle drawerToggle;
    public Toolbar toolbar;
    public String currentActivityName;
    public static boolean vidia = false;
    public TextView pLevel;
    public static int level = 1;

    public ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        //text.setText(result.getData().getStringExtra("INPUT_TEXT"));
                    }
                }
            });

    protected void customToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_launcher_round);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.nav_home:
                if (currentActivityName.equals("MainActivity")) {
                    Toast.makeText(context, "이미 메모장입니다", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    Toast.makeText(context, "메모를 확인합니다", Toast.LENGTH_SHORT).show();
                    startActivityResult.launch(intent);
                }
                break;
            case R.id.nav_custom:
                Toast.makeText(context, "커스텀 설정 아직 구현되지 않았습니다", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_deleter:
                if (currentActivityName.equals("DeleteActivity")) {
                    Toast.makeText(context, "이미 휴지통입니다", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    intent = new Intent(getApplicationContext(), DeleteActivity.class);
                    Toast.makeText(context, "휴지통으로 이동합니다", Toast.LENGTH_SHORT).show();
                    startActivityResult.launch(intent);
                }
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}
