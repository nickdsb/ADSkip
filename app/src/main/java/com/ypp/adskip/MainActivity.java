package com.ypp.adskip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatToggleButton;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";
    private AppCompatToggleButton buttonStart;
    private TextView permissionTv;
    private LinearLayout permissionLayout;
    private LinearLayout appSettingLayout;
    private LinearLayout permissionOverlayLayout;
    private TextView permissionOverlayTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonStart = findViewById(R.id.button_start);
        buttonStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Intent intent = new Intent(MainActivity.this, AccessService.class);
                if (isChecked){
                    startService(intent);
                }
                else {
                    intent.putExtra("tryDisable",true);
                    startService(intent);
                    stopService(intent);
                }
            }
        });
        permissionTv = findViewById(R.id.permission_tv);
        permissionLayout = findViewById(R.id.permission_layout);
        permissionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AccessUtils.isAccessibilityServiceEnabled(getApplicationContext(), AccessService.class)){
                    Toast.makeText(MainActivity.this, "权限已开启",Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                }
            }
        });
        permissionOverlayLayout = findViewById(R.id.permission_overlay_layout);
        permissionOverlayTv = findViewById(R.id.permission_overlay_tv);
        permissionOverlayLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.canDrawOverlays(getApplicationContext())){
                    Toast.makeText(MainActivity.this, "权限已开启",Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION));
                }
            }
        });
        appSettingLayout = findViewById(R.id.app_setting_layout);
        appSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AppsSettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        permissionTv.post(new Runnable() {
            @Override
            public void run() {
                int height = permissionTv.getMeasuredHeight();
                if (AccessUtils.isAccessibilityServiceEnabled(getApplicationContext(),AccessService.class)){
                    Drawable drawable = getResources().getDrawable(R.drawable.permission_yes, null);
                    drawable.setBounds(0, (int) (0.1*height), (int) (0.8*height), (int) (0.9*height));
                    permissionTv.setCompoundDrawablesRelative(null,null,drawable,null);
                }
                else {
                    Drawable drawable = getResources().getDrawable(R.drawable.permission_no, null);
                    drawable.setBounds(0, (int) (0.1*height), (int) (0.8*height), (int) (0.9*height));
                    permissionTv.setCompoundDrawablesRelative(null,null,drawable,null);
                }
            }
        });
        permissionOverlayTv.post(new Runnable() {
            @Override
            public void run() {
                int height = permissionOverlayTv.getMeasuredHeight();
                if (Utils.canDrawOverlays(getApplicationContext())){
                    Drawable drawable = getResources().getDrawable(R.drawable.permission_yes, null);
                    drawable.setBounds(0, (int) (0.1*height), (int) (0.8*height), (int) (0.9*height));
                    permissionOverlayTv.setCompoundDrawablesRelative(null,null,drawable,null);
                }
                else {
                    Drawable drawable = getResources().getDrawable(R.drawable.permission_no, null);
                    drawable.setBounds(0, (int) (0.1*height), (int) (0.8*height), (int) (0.9*height));
                    permissionOverlayTv.setCompoundDrawablesRelative(null,null,drawable,null);
                }
            }
        });
        buttonStart.setChecked(Utils.isServiceRunning(getApplicationContext()));
    }
}
