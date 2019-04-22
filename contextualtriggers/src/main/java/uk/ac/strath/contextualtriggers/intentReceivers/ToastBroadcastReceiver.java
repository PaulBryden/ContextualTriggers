package uk.ac.strath.contextualtriggers.intentReceivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ToastBroadcastReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context.getApplicationContext(), "The Contextual Triggers Framework is a foreground service.", Toast.LENGTH_SHORT).show();
    }
}