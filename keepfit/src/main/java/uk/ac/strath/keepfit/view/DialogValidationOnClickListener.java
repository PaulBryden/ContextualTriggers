package uk.ac.strath.keepfit.view;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

public abstract class DialogValidationOnClickListener implements View.OnClickListener {

    private final Activity mActivity;
    private final Dialog mDialog;

    public class ValidationException extends RuntimeException {
        private String toastMessage;

        public ValidationException(String toastMessage) {
            this.toastMessage = toastMessage;
        }
    }

    public DialogValidationOnClickListener(Activity activity, Dialog dialog) {
        this.mActivity = activity;
        mDialog = dialog;
    }

    @Override
    public void onClick(View v) {
        try {
            validate();
            onSuccess();
            mDialog.dismiss();
        } catch (ValidationException e) {
            Toaster.makeMagicToast(mActivity, e.toastMessage);
        }
    }

    /**
     * Does nothing if dialog entries are valid, throws an exception with an error message
     * to display in a toast otherwise.
     *
     * @throws ValidationException if not valid
     */
    abstract void validate();

    /**
     * Executed on successful validation of the dialog's inputs.
     */
    abstract void onSuccess();

}
