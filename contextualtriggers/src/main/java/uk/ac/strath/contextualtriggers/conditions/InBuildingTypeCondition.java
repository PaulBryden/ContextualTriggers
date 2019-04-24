package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition that is satisfied if the user is in a building of the target type.
 */
public class InBuildingTypeCondition extends DataCondition<PlacesData> {

    Place.Type targetType;

    public InBuildingTypeCondition(Place.Type buildingType, IDataManager<PlacesData> dataManager) {
        super(dataManager, 30);
        this.targetType = buildingType;
    }

    @Override
    public boolean isSatisfied() {
        for (PlaceLikelihood p : getData().places) {
            if (p.getLikelihood() > 0.75) {
                for (Place.Type type : p.getPlace().getTypes()) {
                    if (type == targetType) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
