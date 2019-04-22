package uk.ac.strath.contextualtriggers.data;

import com.google.android.libraries.places.api.model.PlaceLikelihood;

import java.util.List;

public class PlacesData extends AbstractData {
    public List<PlaceLikelihood> places;

    public PlacesData(List<PlaceLikelihood> places) {
        this.places = places;
    }
}
