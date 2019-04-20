package uk.ac.strath.contextualtriggers.intentReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import uk.ac.strath.contextualtriggers.managers.ActualStepAndGoalDataManager;

public class StepAndGoalIntentReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
            /*
            Intent pushIntent = new Intent(context, ContextualTriggersService.class);
            context.startService(pushIntent);*/
        if (intent.getAction() == "uk.ac.strath.contextualtriggers.step")
        {
            Log.d("StepAndGoalIntentReceiver", intent.getAction());
            Log.d("StepAndGoalIntentReceiver", Integer.toString(intent.getIntExtra("steps", 0)));
            Intent pushIntent = new Intent(context, ActualStepAndGoalDataManager.class);
            pushIntent.putExtra("steps", intent.getIntExtra("steps", 0));
            pushIntent.putExtra("goal", intent.getIntExtra("goal", 0));
            context.startService(pushIntent);
        }
    }
}
