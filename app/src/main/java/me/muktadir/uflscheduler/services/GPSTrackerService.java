package me.muktadir.uflscheduler.services;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.muktadir.uflscheduler.R;
import me.muktadir.uflscheduler.utilities.CommonUtils;

public class GPSTrackerService extends Service implements LocationListener {
    public static final int REQUEST_LOCATION = 0;
    private final Activity mActivity;
    protected LocationManager mLocationManager;
    Location mLocation;

    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1000 * 60 * 2;

    public GPSTrackerService(Activity activity) {
        mActivity = activity;
        mLocationManager = (LocationManager) activity.getSystemService(LOCATION_SERVICE);

        getLocation();
    }

    private void showNetworkAlert() {
        CommonUtils.showErrorDialog(mActivity, mActivity.getResources().getString(R.string.network_error_title), mActivity.getResources().getString(R.string.network_error_message), mActivity.getResources().getString(R.string.ok_title));
    }

    private void showSettingsAlert() {
        final SweetAlertDialog sDialog = new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_MODIFIED_TYPE);
        sDialog.setTitleText("GPS Settings");
        sDialog.setContentText("GPS is not enabled. Do you want to go to settings menu?");
        sDialog.setConfirmText("SETTINGS");
        sDialog.setCancelText(mActivity.getResources().getString(R.string.dialog_cancel_text));
        sDialog.setCancelable(false);
        sDialog.setCanceledOnTouchOutside(false);
        sDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            public void onClick(SweetAlertDialog mDialog) {
                mDialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mActivity.startActivity(intent);
            }
        });
        sDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            public void onClick(SweetAlertDialog mDialog) {
                mDialog.dismiss();
            }
        });
        sDialog.show();
    }


    public Location getLocation() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(mActivity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_LOCATION);
        else {
            if (mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_FOR_UPDATE,
                        MIN_DISTANCE_FOR_UPDATE, this);
                if (mLocationManager != null) {
                    mLocation = mLocationManager
                            .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
            } else {
                showSettingsAlert();
            }

            if (mLocation == null) {
                if (mLocationManager
                        .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_FOR_UPDATE,
                            MIN_DISTANCE_FOR_UPDATE, this);
                    if (mLocationManager != null) {
                        mLocation = mLocationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    }
                } else {
                    showNetworkAlert();
                }
            }
        }

        return mLocation;
    }

    public void stopUsingGPS() {
        if (mLocationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mLocationManager.removeUpdates(GPSTrackerService.this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}