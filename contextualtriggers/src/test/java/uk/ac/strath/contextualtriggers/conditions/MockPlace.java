package uk.ac.strath.contextualtriggers.conditions;

import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlusCode;

import java.util.Arrays;
import java.util.List;

public class MockPlace extends Place {

    private final List<Place.Type> types;

    public MockPlace(Place.Type... types) {
        this.types = Arrays.asList(types);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    @Override
    public String getId() {
        return null;
    }


    @Nullable
    @Override
    public String getAddress() {
        return null;
    }

    @Nullable
    @Override
    public AddressComponents getAddressComponents() {
        return null;
    }

    @Nullable
    @Override
    public List<String> getAttributions() {
        return null;
    }

    @Nullable
    @Override
    public OpeningHours getOpeningHours() {
        return null;
    }

    @Nullable
    @Override
    public String getPhoneNumber() {
        return null;
    }

    @Override
    public LatLng getLatLng() {
        return null;
    }

    @Nullable
    @Override
    public String getName() {
        return null;
    }

    @Override
    public LatLngBounds getViewport() {
        return null;
    }

    @Override
    public Uri getWebsiteUri() {
        return null;
    }

    @Nullable
    @Override
    public List<PhotoMetadata> getPhotoMetadatas() {
        return null;
    }

    @Nullable
    @Override
    public PlusCode getPlusCode() {
        return null;
    }

    @Nullable
    @Override
    public Integer getPriceLevel() {
        return null;
    }

    @Nullable
    @Override
    public Double getRating() {
        return null;
    }

    @Override
    public List<Type> getTypes() {
        return types;
    }

    @Nullable
    @Override
    public Integer getUserRatingsTotal() {
        return null;
    }
}
