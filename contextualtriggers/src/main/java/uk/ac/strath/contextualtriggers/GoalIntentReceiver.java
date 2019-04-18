package uk.ac.strath.contextualtriggers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import uk.ac.strath.contextualtriggers.managers.ActualGoalDataManager;
import uk.ac.strath.contextualtriggers.managers.ActualStepDataManager;

public class GoalIntentReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {

            /*
            Intent pushIntent = new Intent(context, ContextualTriggersService.class);
            context.startService(pushIntent);*/
        if (intent.getAction() == "uk.ac.strath.contextualtriggers.goal")
        {
            Log.d("goalIntentReceiver", intent.getAction());
            Log.d("goalIntentReceiver", Integer.toString(intent.getIntExtra("goal", 0)));
            Intent pushIntent = new Intent(context, ActualGoalDataManager.class);
            pushIntent.putExtra("goal", intent.getIntExtra("goal", 0));
            context.startService(pushIntent);
        }
    }
}
