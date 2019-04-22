package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class InBuildingTypeCondition extends DataCondition<PlacesData> {
    Place.Type targetType;

    public InBuildingTypeCondition(Place.Type buildingType, IDataManager<PlacesData> dataManager) {
        super(dataManager);
        this.targetType = buildingType;
    }

    @Override
    public boolean isSatisfied() {
        for(PlaceLikelihood p : getData().places)
        {
          if(p.getLikelihood()>0.75)
          {
              for(Place.Type type : p.getPlace().getTypes())
              {
                  if(type == targetType)
                  {
                      return true;
                  }
              }
          }
        }
        return false;
    }

}
