package uk.ac.strath.contextualtriggers.conditions;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import uk.ac.strath.contextualtriggers.data.AltitudeData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Basic condition that checks if a notification has been sent recently.
 * Condition is satisfied if time elapsed since last condition is more than
 * specified amount.
 */
public class AltitudeTransitionCondition extends DataCondition<AltitudeData>
{

    private double AltitudeTransition=0;
    private double oldAltitude=0;
    private int targetTransition;


    public AltitudeTransitionCondition(int transition,IDataManager dataManager)
    {
        super(dataManager, 30,new AltitudeData());
        this.targetTransition = transition;
    }

    @Override
    public void notifyUpdate(AltitudeData data)
    {
        // Override since an update always means condition isn't satisfied,
        AltitudeTransition=data.altitude-oldAltitude;
        oldAltitude=data.altitude;
        super.notifyUpdate(data);
    }

    @Override
    public boolean isSatisfied()
    {
        return  AltitudeTransition>targetTransition;
    }
}

