package com.seven.testbdlocation;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private LocationClient mClient;
    private MyLocationListener mListener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        requestPermission();
        
        mClient = new LocationClient(getApplicationContext());
        mListener = new MyLocationListener();
        mClient.registerLocationListener(mListener);
    
        LocationClientOption option = new LocationClientOption();
    
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；
    
        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认gcj02
        //gcj02：国测局坐标；
        //bd09ll：百度经纬度坐标；
        //bd09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回wgs84类型坐标
    
        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效
    
        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true
    
        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false
    
        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)
    
        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false
    
        option.setWifiCacheTimeOut(5*60*1000);
        //可选，7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前WiFi是否超出有效期，若超出有效期，会先重新扫描WiFi，然后定位
    
        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false
    
        mClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        
        mClient.start();
    }
    
    @TargetApi(23)
    private void requestPermission() {
        List<String> allPermission = new ArrayList<>();
        List<String> unGetPermissionList = new ArrayList<>();
        allPermission.add(Manifest.permission.ACCESS_FINE_LOCATION);
        allPermission.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        allPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        allPermission.add(Manifest.permission.READ_PHONE_STATE);
        allPermission.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        allPermission.add(Manifest.permission.CHANGE_WIFI_STATE);
        for (String permission : allPermission) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager
                .PERMISSION_GRANTED) {
                unGetPermissionList.add(permission);
            }
        }
    
        if (!unGetPermissionList.isEmpty()) {
            String[] permissions = unGetPermissionList.toArray(new String[unGetPermissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            //jumpToNext();
        }
    }
    
    @Override
    protected void onDestroy() {
        mClient.unRegisterLocationListener(mListener);
        super.onDestroy();
    }
    
    private class MyLocationListener extends BDAbstractLocationListener {
    
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            Log.i("", "onReceiveLocation: " + bdLocation.getLocType());
        }
    }
}
