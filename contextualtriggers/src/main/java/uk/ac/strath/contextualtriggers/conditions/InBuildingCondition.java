package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import java.util.List;

import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class InBuildingCondition extends DataCondition<List<PlaceLikelihood>> {


    public InBuildingCondition(IDataManager<List<PlaceLikelihood>> dataManager) {
        super(dataManager);
    }

    @Override
    public boolean isSatisfied() {
        for(PlaceLikelihood p : getData())
        {
          if(p.getLikelihood()>0.75)
          {
              return true;
          }
        }
        return false;
    }

}
