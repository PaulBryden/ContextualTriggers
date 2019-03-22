package uk.ac.strath.keepfit.view;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

public class Toaster {

    static void makeMagicToast(Context context, String text) {
        makeMagicToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void makeMagicToast(Context context, String text, int duration) {
        Toast t = Toast.makeText(context, text, duration);
        if (softKeyboardShown(context)) {
            t.setGravity(Gravity.CENTER, 0, 0);
        }
        t.show();
    }

    private static boolean softKeyboardShown(Context context) {
        // Hacky solution based on this Stackoverflow answer: https://stackoverflow.com/a/4737265
        Activity activity = (Activity) context;
        final View content = activity.findViewById(android.R.id.content);
        int heightDiff = content.getRootView().getHeight() - content.getHeight();
        DisplayMetrics metrics = activity.getResources().getDisplayMetrics();
        float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, metrics);
        return heightDiff > dp;
    }

}
