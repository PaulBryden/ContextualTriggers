package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import java.util.Collections;
import java.util.List;

import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition that is satisfied if the user leaves a building of the target type.
 */
public class NoLongerInPlaceTypeCondition extends DataCondition<PlacesData> {

    protected static final double DEFAULT_UPPER_THRESHOLD = 0.2;
    protected static final double DEFAULT_LOWER_THRESHOLD = 0.1;
    private static final int TIMEOUT = 180; // in seconds
    private List<Place.Type> targetTypes;
    private double upperThreshold;
    private double lowerThreshold;
    boolean isInPlace;
    boolean justLeftPlace;
    long lastInPlace;

    public NoLongerInPlaceTypeCondition(Place.Type buildingType, IDataManager<PlacesData> dataManager) {
        this(Collections.singletonList(buildingType), DEFAULT_LOWER_THRESHOLD, DEFAULT_UPPER_THRESHOLD, dataManager);
    }

    public NoLongerInPlaceTypeCondition(Place.Type buildingType, double lowerThreshold, double upperThreshold, IDataManager<PlacesData> dataManager) {
        this(Collections.singletonList(buildingType), lowerThreshold, upperThreshold, dataManager);
    }

    public NoLongerInPlaceTypeCondition(List<Place.Type> buildingTypes, IDataManager<PlacesData> dataManager) {
        this(buildingTypes, DEFAULT_LOWER_THRESHOLD, DEFAULT_UPPER_THRESHOLD, dataManager);
    }

    public NoLongerInPlaceTypeCondition(List<Place.Type> buildingTypes, double lowerThreshold, double upperThreshold, IDataManager<PlacesData> dataManager) {
        super(dataManager, 30);
        this.targetTypes = buildingTypes;
        this.lowerThreshold = lowerThreshold;
        this.upperThreshold = upperThreshold;
    }

    @Override
    public void notifyUpdate(PlacesData data) {
        // Override since an update always means condition isn't satisfied,
        // so no need to notify the Trigger of the change.
        for (PlaceLikelihood p : data.places) {
            for (Place.Type type : p.getPlace().getTypes()) {
                if (targetTypes.contains(type) && p.getLikelihood() > upperThreshold) {
                    isInPlace = true;
                }
            }
        }
        super.notifyUpdate(data);
    }

    @Override
    public boolean isSatisfied() {
        if (getData() == null) {
            return false;
        }
        for (PlaceLikelihood p : getData().places) {
            for (Place.Type type : p.getPlace().getTypes()) {
                if (targetTypes.contains(type) && p.getLikelihood() < lowerThreshold) {
                    if (isInPlace) {
                        isInPlace = false;
                        justLeftPlace = true;
                        lastInPlace = getData().getTimestamp();
                    }
                }
            }
        }
        if (System.currentTimeMillis() - lastInPlace < TIMEOUT * 1000) {
            return justLeftPlace;
        } else {
            return false;
        }
    }

}
