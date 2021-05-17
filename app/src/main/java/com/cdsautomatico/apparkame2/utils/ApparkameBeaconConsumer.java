package com.cdsautomatico.apparkame2.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.RemoteException;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

/**
 * Created by alangvara on 08/06/18.
 */

public class ApparkameBeaconConsumer implements BeaconConsumer {

    private static final String TAG = "ApparkameBeaconConsumer";

    BeaconManager beaconManager;
    Activity activity;

    public ApparkameBeaconConsumer(Activity activity){
        this.activity = activity;
        beaconManager = org.altbeacon.beacon.BeaconManager.getInstanceForApplication(activity);

//        beaconManager = BeaconManager.getInstanceForApplication(getContext());
        // Layout para detectar solo iBeacons
//        beaconManager.getBeaconParsers().add(new BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"));
//        beaconManager.bind(this);
        beaconManager.setBackgroundScanPeriod(1000);
    }

    public void bind(){
        beaconManager.bind(this);
    }

    public void unbind(){
        beaconManager.unbind(this);
//        beaconManager.setEnableScheduledScanJobs(false);
        boolean isit = beaconManager.isBound(this);

        Log.v(TAG, "" + isit);
    }

    @Override
    public Context getApplicationContext() {
        return activity.getBaseContext();
    }

    @Override
    public boolean bindService(Intent intent, ServiceConnection serviceConnection, int i) {
        return false;
    }

    @Override
    public void unbindService(ServiceConnection serviceConnection) {

    }

    @Override
    public void onBeaconServiceConnect() {

        beaconManager.addRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {

                if (beacons.size() > 0) {
                    for(Beacon beacon : beacons){
                        Log.v(TAG, beacon.getId1().toString());
                    }

                    Log.v(TAG, "----");

                }
            }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("apparkameBeaconRanging", null, null, null));
        } catch (RemoteException e) {   }

    }
}
