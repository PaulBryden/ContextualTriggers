package uk.ac.strath.contextualtriggers.conditions;

import android.os.Parcel;
import android.support.annotation.NonNull;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

public class MockPlaceLikelihood extends PlaceLikelihood {

    private final Place place;
    private final double likelihood;

    public MockPlaceLikelihood(Place place, double likelihood) {
        this.place = place;
        this.likelihood = likelihood;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {}

    @NonNull
    @Override
    public Place getPlace() {
        return place;
    }

    @Override
    public double getLikelihood() {
        return likelihood;
    }
}
