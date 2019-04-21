package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.gms.location.places.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import java.util.List;

import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class GymNearbyCondition extends DataCondition<List<PlaceLikelihood>> {


    public GymNearbyCondition(IDataManager<List<PlaceLikelihood>> dataManager) {
        super(dataManager);
    }

    @Override
    public boolean isSatisfied() {
        for(PlaceLikelihood p : getData())
        {
            for(Enum type : p.getPlace().getTypes())
            {
                if(type.equals(Place.TYPE_GYM) && p.getLikelihood()<0.5)
                {
                    return true;
                }
            }
        }
        return false;
    }

}
