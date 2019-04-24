package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition that checks for nearby gyms.
 */
public class GymNearbyCondition extends DataCondition<PlacesData> {

    public GymNearbyCondition(IDataManager<PlacesData> dataManager) {
        super(dataManager);
    }

    @Override
    public boolean isSatisfied() {
        for (PlaceLikelihood p : getData().places) {
            for (Place.Type type : p.getPlace().getTypes()) {
                if (type == Place.Type.GYM && p.getLikelihood() > 0.5) {
                    return true;
                }
            }
        }
        return false;
    }

}
