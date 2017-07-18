package com.tuikes.demotest.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.tuikes.demotest.R;

/**
 * 启动页
 * Created by chendx on 2017/6/22.
 */

public class AppStartActivity extends Activity {
    public static final int REQUEST_CODE_PERMISSIONS_STORAGE = 14;
    public static final int REQUEST_CODE_PERMISSIONS_LOCATION = 15;
    public static final int REQUEST_CODE_PERMISSIONS_READ_PHONE_STATE = 16;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 0x11) {
                Intent intent = new Intent(AppStartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_start);
        checkStoragePermission();
    }


    // 检查权限
    private void checkStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // 有权限, 接着检查定位权限
            checkLocationPermission();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.permission_title);
                builder.setMessage(R.string.permission_msg_storage);
                builder.setPositiveButton(R.string.permission_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请求用户授权
                        ActivityCompat.requestPermissions(AppStartActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS_STORAGE);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(AppStartActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSIONS_STORAGE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS_STORAGE) {
            // 不管用户授权结果，检查下一项
            checkLocationPermission();
        } else if (requestCode == REQUEST_CODE_PERMISSIONS_LOCATION) {
            checkReadStatePermission();
        } else if (requestCode == REQUEST_CODE_PERMISSIONS_READ_PHONE_STATE) {
            startApp();
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // 有权限, 下一步
            checkReadStatePermission();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.permission_title);
                builder.setMessage(R.string.permission_msg_location);
                builder.setPositiveButton(R.string.permission_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请求用户授权
                        ActivityCompat.requestPermissions(AppStartActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSIONS_LOCATION);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(AppStartActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSIONS_LOCATION);
            }
        }
    }

    private void checkReadStatePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // 有权限, 下一步
            startApp();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.permission_title);
                builder.setMessage(R.string.permission_msg_read_phone_state);
                builder.setPositiveButton(R.string.permission_sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 请求用户授权
                        ActivityCompat.requestPermissions(AppStartActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_PERMISSIONS_READ_PHONE_STATE);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(AppStartActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_CODE_PERMISSIONS_READ_PHONE_STATE);
            }
        }
    }

    private void startApp() {
        mHandler.sendEmptyMessageDelayed(0x11, 500);
    }
}
