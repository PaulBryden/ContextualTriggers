package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.PlaceLikelihood;

import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class InBuildingCondition extends DataCondition<PlacesData> {


    public InBuildingCondition(IDataManager<PlacesData> dataManager) {
        super(dataManager);
    }

    @Override
    public boolean isSatisfied() {
        for(PlaceLikelihood p : getData().places)
        {
          if(p.getLikelihood() > 0.75)
          {
              return true;
          }
        }
        return false;
    }

}
