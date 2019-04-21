package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.gms.location.places.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import java.util.List;

import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class NoLongerInBuildingTypeCondition extends DataCondition<List<PlaceLikelihood>> {
    int placeType;
    boolean isInPlace;
    boolean justLeftPlace;
    long lastInPlace;
    public NoLongerInBuildingTypeCondition(int PlaceType, IDataManager<List<PlaceLikelihood>> dataManager) {
        super(dataManager);

    }
    @Override
    public void notifyUpdate(List<PlaceLikelihood> data)
    {
        // Override since an update always means condition isn't satisfied,
        // so no need to notify the Trigger of the change.
       for(PlaceLikelihood p : data)
       {
           for(Enum type : p.getPlace().getTypes())
           {
               if(type.equals(placeType) && p.getLikelihood()>0.75)
               {
                   isInPlace =true;
               }
           }
       }
    }

    @Override
    public boolean isSatisfied()
    {   for(PlaceLikelihood p : getData())
        {
            for(Enum type : p.getPlace().getTypes())
            {
                if(type.equals(placeType) && p.getLikelihood()<0.35)
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
