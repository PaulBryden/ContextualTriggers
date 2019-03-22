package uk.ac.strath.keepfit.view;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import uk.co.daviddunphy.keepfit.R;
import uk.co.daviddunphy.keepfit.view.Toaster;

public class ConfirmDialogPreference extends DialogPreference {

    public ConfirmDialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult) {
            Toaster.makeMagicToast(getContext(), getContext().getResources().getString(R.string.delete_history_confirmation));
        }
        persistBoolean(positiveResult);
    }
}
