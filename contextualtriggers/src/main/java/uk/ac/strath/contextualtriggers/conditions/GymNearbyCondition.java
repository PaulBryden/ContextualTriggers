package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import java.util.List;

import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class GymNearbyCondition extends DataCondition<PlacesData> {


    public GymNearbyCondition(IDataManager<PlacesData> dataManager) {
        super(dataManager);
    }

    @Override
    public boolean isSatisfied() {
        for(PlaceLikelihood p : getData().places)
        {
            for(Place.Type type : p.getPlace().getTypes())
            {
                if(type == Place.Type.GYM && p.getLikelihood()<0.5)
                {
                    return true;
                }
            }
        }
        return false;
    }

}
