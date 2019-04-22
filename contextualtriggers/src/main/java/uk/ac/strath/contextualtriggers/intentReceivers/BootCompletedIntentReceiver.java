package uk.ac.strath.contextualtriggers.intentReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import uk.ac.strath.contextualtriggers.MainApplication;

public class BootCompletedIntentReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction()))
        {
            Intent pushIntent = new Intent(context, MainApplication.class);
            context.startActivity(pushIntent);
        }
    }
}
