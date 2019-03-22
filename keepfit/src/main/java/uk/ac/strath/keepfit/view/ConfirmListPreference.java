package uk.ac.strath.keepfit.view;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.preference.ListPreference;
import android.util.AttributeSet;

import uk.co.daviddunphy.keepfit.R;

public class ConfirmListPreference extends ListPreference {

    private int mClickedDialogEntryIndex;

    // Constructors; these are standard and required for the subclass
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConfirmListPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ConfirmListPreference(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }
    public ConfirmListPreference(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }
    public ConfirmListPreference(Context context)
    {
        super(context);
    }

    private void confirmInput(boolean confirmed) {
        if (confirmed && mClickedDialogEntryIndex >= 0 && getEntryValues() != null) {
            String value = getEntryValues()[mClickedDialogEntryIndex].toString();
            if (callChangeListener(value)) {
                setValue(value);
            }
        }
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        // Copied from ListPreference so that we have access to mClickedDialogEntryIndex, which
        // is a private field.
        super.onPrepareDialogBuilder(builder);

        mClickedDialogEntryIndex = findIndexOfValue(this.getValue());
        builder.setSingleChoiceItems(getEntries(), mClickedDialogEntryIndex,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mClickedDialogEntryIndex = which;

                        /*
                         * Clicking on an item simulates the positive button
                         * click, and dismisses the dialog.
                         */
                        ConfirmListPreference.this.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        dialog.dismiss();
                    }
                });
        builder.setCancelable(true);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            int oldValue = Integer.parseInt(this.getValue());
            int newValue = Integer.parseInt(getEntryValues()[mClickedDialogEntryIndex].toString());
            if (newValue < 0 || (newValue >= oldValue && oldValue >= 0)) {
                confirmInput(true);
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(R.string.reduce_history_warning_message);
            builder.setTitle(R.string.reduce_history_warning_title);
            builder.setCancelable(true);
            builder.setIcon(R.drawable.ic_warning_blue_24dp);
            builder.setPositiveButton(R.string.yes_button_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirmInput(true);
                }
            });
            builder.setNegativeButton(R.string.no_button_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirmInput(false);
                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    confirmInput(false);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            confirmInput(false);
        }
    }
}
