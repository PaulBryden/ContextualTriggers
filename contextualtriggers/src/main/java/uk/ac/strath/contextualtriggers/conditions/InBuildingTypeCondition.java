package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import java.util.List;

import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class InBuildingTypeCondition extends DataCondition<List<PlaceLikelihood>> {
    Place.Type buildingType;

    public InBuildingTypeCondition(Place.Type buildingType, IDataManager<List<PlaceLikelihood>> dataManager) {
        super(dataManager);
        this.buildingType=buildingType;
    }

    @Override
    public boolean isSatisfied() {
        for(PlaceLikelihood p : getData())
        {
          if(p.getLikelihood()>0.75)
          {
              for(Place.Type type:p.getPlace().getTypes())
              {
                  if(type==buildingType)
                  {
                      return true;
                  }
              }
          }
        }
        return false;
    }

}
