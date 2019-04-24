package uk.ac.strath.contextualtriggers.permissions;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import uk.ac.strath.contextualtriggers.managers.AltitudeDataManager;
import uk.ac.strath.contextualtriggers.managers.CalendarDataManager;
import uk.ac.strath.contextualtriggers.managers.IntervalsDataManager;
import uk.ac.strath.contextualtriggers.managers.PlacesDataManager;
import uk.ac.strath.contextualtriggers.managers.WeatherDataManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class RequestLocationPermission extends AppCompatActivity{

    private int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    /**
     *
     * @param savedInstanceState contains information pertaining to previous states of the activity
     *                           if it has been used before
     * @see android.support.v7.app.AppCompatActivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Activity activity = this;
        Log.d("REQUEST", "HERE");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Location Permissions");
        builder.setMessage("This app needs location permission for contextual services.");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(activity,new String[]{ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                turnOffLocationManagers();
                dialog.cancel();
                activity.finish();
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    onPermissionGranted(permissions[i]);
                } else {
                    turnOffLocationManagers();
                }
            }
        }
    }
    protected void onPermissionGranted(String permission) {
            this.finish();
    }

    private void turnOffLocationManagers(){
        Log.d("PERMISSION DENIED", "TURN OFF ALL LOCATION BASED DATA MANAGERS");
        Intent i = new Intent(this, AltitudeDataManager.class);
        stopService(i);
        i = new Intent(this, IntervalsDataManager.class);
        stopService(i);
        i = new Intent(this, PlacesDataManager.class);
        stopService(i);
        i = new Intent(this, WeatherDataManager.class);
        stopService(i);
        this.finish();
    }
}