package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class NoLongerInBuildingTypeCondition extends DataCondition<PlacesData> {
    Place.Type targetType;
    boolean isInPlace;
    boolean justLeftPlace;
    long lastInPlace;

    public NoLongerInBuildingTypeCondition(Place.Type targetType, IDataManager<PlacesData> dataManager) {
        super(dataManager);
        this.targetType = targetType;
    }

    @Override
    public void notifyUpdate(PlacesData data)
    {
        // Override since an update always means condition isn't satisfied,
        // so no need to notify the Trigger of the change.
       for(PlaceLikelihood p : data.places)
       {
           for(Place.Type type : p.getPlace().getTypes())
           {
               if(type == targetType && p.getLikelihood()>0.75)
               {
                   isInPlace =true;
               }
           }
       }
        super.notifyUpdate(data);
    }

    @Override
    public boolean isSatisfied()
    {   for(PlaceLikelihood p : getData().places)
        {
            for(Place.Type type : p.getPlace().getTypes())
            {
                if(type == targetType && p.getLikelihood()<0.35)
                {
                    if(isInPlace)
                    {
                        isInPlace = false;
                        justLeftPlace = true;
                        lastInPlace=System.currentTimeMillis();
                    }
                }
            }
        }
        if(System.currentTimeMillis()-lastInPlace<600000)
        {
            return justLeftPlace;
        }else
        {
            return false;
        }
    }

}
