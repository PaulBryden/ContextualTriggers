package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import java.util.Collections;
import java.util.List;

import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if any place with a likelihood greater than a threshold value matches
 * any target place type.
 */
public class InPlaceTypeCondition extends DataCondition<PlacesData> {

    protected static final double DEFAULT_THRESHOLD = 0.1;
    private List<Place.Type> targetTypes;
    private double threshold;

    public InPlaceTypeCondition(Place.Type buildingType, IDataManager<PlacesData> dataManager) {
        this(Collections.singletonList(buildingType), DEFAULT_THRESHOLD, dataManager);
    }

    public InPlaceTypeCondition(Place.Type buildingType, double threshold, IDataManager<PlacesData> dataManager) {
        this(Collections.singletonList(buildingType), threshold, dataManager);
    }

    public InPlaceTypeCondition(List<Place.Type> buildingTypes, IDataManager<PlacesData> dataManager) {
        this(buildingTypes, DEFAULT_THRESHOLD, dataManager);
    }

    public InPlaceTypeCondition(List<Place.Type> buildingTypes, double threshold, IDataManager<PlacesData> dataManager) {
        super(dataManager, 30);
        this.targetTypes = buildingTypes;
        this.threshold = threshold;
    }

    @Override
    public boolean isSatisfied() {
        if (getData() == null) {
            return false;
        }
        for(PlaceLikelihood p : getData().places)
        {
          if(p.getLikelihood() >= threshold)
          {
              for(Place.Type type : p.getPlace().getTypes())
              {
                  if(targetTypes.contains(type))
                  {
                      return true;
                  }
              }
          }
        }
        return false;
    }

}
