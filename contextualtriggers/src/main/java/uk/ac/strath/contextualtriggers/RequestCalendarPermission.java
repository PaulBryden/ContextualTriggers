package uk.ac.strath.contextualtriggers;

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

import uk.ac.strath.contextualtriggers.managers.CalendarDataManager;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CALENDAR;


public class RequestCalendarPermission extends AppCompatActivity{

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
        builder.setTitle("Need Calendar Permissions");
        builder.setMessage("This app needs to read calendar data only for contextual services.");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(activity,new String[]{READ_CALENDAR},MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                turnOffCalendarManager();
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
                    turnOffCalendarManager();
                }
            }
        }
    }
    protected void onPermissionGranted(String permission) {
        this.finish();
    }

    private void turnOffCalendarManager(){
        Log.d("PERMISSION DENIED", "TURN OFF ALL CALENDAR BASED DATA MANAGERS");
        Intent i = new Intent(this, CalendarDataManager.class);
        stopService(i);
        this.finish();
    }
}