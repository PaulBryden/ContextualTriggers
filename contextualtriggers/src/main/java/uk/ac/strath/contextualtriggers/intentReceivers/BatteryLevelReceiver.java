package uk.ac.strath.contextualtriggers.intentReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import uk.ac.strath.contextualtriggers.managers.BatteryDataManager;

public class BatteryLevelReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.d("BatteryLevelReceiver","Received Battery Event");
        Intent pushIntent = new Intent(context, BatteryDataManager.class);
        if (intent.getAction() == "android.intent.action.BATTERY_LOW")
        {
            pushIntent.putExtra("level", true);
        }
        else if(intent.getAction() == "android.intent.action.BATTERY_OKAY")
        {
            pushIntent.putExtra("level", false);

        }
        context.startService(pushIntent);
    }

}
