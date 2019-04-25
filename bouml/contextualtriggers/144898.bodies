class RequestLocationPermission
!!!160130.java!!!	onCreate(inout savedInstanceState : Bundle) : void
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
!!!160258.java!!!	onRequestPermissionsResult(in requestCode : int, inout permissions : String [[]], inout grantResults : int [[]]) : void
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
!!!160386.java!!!	onPermissionGranted(in permission : String) : void
            this.finish();
!!!160514.java!!!	turnOffLocationManagers() : void
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
