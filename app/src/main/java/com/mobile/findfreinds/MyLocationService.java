package com.mobile.findfreinds;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.telephony.SmsManager;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MyLocationService extends Service {
    String num;
    public MyLocationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Récupérer la position gps
        num=intent.getStringExtra("num");
        FusedLocationProviderClient mClient = LocationServices.getFusedLocationProviderClient(this);
        mClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location!=null){
                    sendSms(location);
                }
            }
        });
        LocationRequest request = (new LocationRequest.Builder(100).setMinUpdateDistanceMeters(10)).build();
        //LocationRequest request = LocationRequest.create().setSmallestDisplacement(100).setFastestInterval(10000);
        LocationCallback action = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                if (location!=null){
                    sendSms(location);
                }
            }
        };
        mClient.requestLocationUpdates(request,action,null);
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendSms(Location location) {
        SmsManager manager = SmsManager.getDefault();
        manager.sendTextMessage(num,null,"findfriends: ma position est #"+location.getLatitude()+'#'+location.getLongitude(),null,null);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }
}