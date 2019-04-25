class PlacesDataManager
!!!157314.java!!!	onBind(inout intent : Intent) : IBinder
        setup();
        return binder;
!!!157570.java!!!	PlacesDataManager()
        super(60, 600);
        setup();
!!!157826.java!!!	onStartCommand(inout intent : Intent, in flags : int, in startId : int) : int
        super.onStartCommand(intent, flags, startId);
        monitor();
        alarm();
        return START_STICKY;
!!!157954.java!!!	monitor() : void
        PlacesClient placesClient;
        Places.initialize(this, getString(R.string.API_key));
        placesClient = Places.createClient(this);
        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.TYPES);

// Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request =
                FindCurrentPlaceRequest.builder(placeFields).build();

        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {   // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainApplication.getAppActivity(),
                    ACCESS_FINE_LOCATION)) {
                Intent i = new Intent(this, RequestLocationPermission.class);
                startActivity(i);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainApplication.getAppActivity(),
                        new String[]{ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        } else {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task ->
            {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse response = task.getResult();
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Log.i(TAG, String.format("Place '%s' has type '%s' has likelihood: %f",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getPlace().getTypes(),
                                placeLikelihood.getLikelihood()));
                    }
                    sendUpdate(new PlacesData(response.getPlaceLikelihoods()));
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                }
            });
        }
