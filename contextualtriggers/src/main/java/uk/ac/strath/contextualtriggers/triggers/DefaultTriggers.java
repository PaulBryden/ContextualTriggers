package uk.ac.strath.contextualtriggers.triggers;


import android.os.IBinder;
import android.util.Log;

import uk.ac.strath.contextualtriggers.Action;
import uk.ac.strath.contextualtriggers.Condition;
import uk.ac.strath.contextualtriggers.actions.NotificationAction;
import uk.ac.strath.contextualtriggers.conditions.StepCountCondition;
import uk.ac.strath.contextualtriggers.data.StepData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.managers.StepDataManager;

public class DefaultTriggers {

    //This is a POINTLESS TRIGGER DO NOT USE THIS
    //should probably throw an exception on casting rather than deal with it here
    public static ITrigger createStepMonitorTrigger(IBinder binder){
        IDataManager<StepData> dataManager;
        try{
            dataManager = ((StepDataManager.LocalBinder) binder).getInstance();
            Trigger.Builder builder = new Trigger.Builder();
            Condition c = new StepCountCondition(StepCountCondition.LESS_THAN, 10000, dataManager);
            Action a = new NotificationAction("Go for a walk ya lazy. It's even sunny ootside!");
            builder.setCondition(c);
            builder.setAction(a);
            return builder.build();
        }catch(ClassCastException e){
            Log.e("Trigger Creation","Oops you did a booboo");
            return null;
        }

    }
}
