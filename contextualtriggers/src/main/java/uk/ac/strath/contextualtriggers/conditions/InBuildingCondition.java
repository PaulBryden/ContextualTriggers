package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.PlaceLikelihood;

import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition that is satisfied if the user is in a building.
 */
public class InBuildingCondition extends DataCondition<PlacesData> {

    public InBuildingCondition(IDataManager<PlacesData> dataManager) {
        super(dataManager, 30);
    }

    @Override
    public boolean isSatisfied() {
        for (PlaceLikelihood p : getData().places) {
            if (p.getLikelihood() > 0.75) {
                return true;
            }
        }
        return false;
    }

}
