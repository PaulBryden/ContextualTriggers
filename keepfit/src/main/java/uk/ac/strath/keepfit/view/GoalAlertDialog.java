package uk.ac.strath.keepfit.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;
import java.util.Locale;

import uk.ac.strath.keepfit.model.Goal;
import uk.ac.strath.keepfit.R;
import uk.ac.strath.keepfit.view.DialogValidationOnClickListener;


public class GoalAlertDialog extends AlertDialog {

    private EditText goalNameInput;
    private EditText goalTargetInput;

    protected GoalAlertDialog(Context context, int title_id) {
        super(context);
        setTitle(context.getString(title_id));
        setMessage(getContext().getResources().getString(R.string.edit_goal_dialog_message));
    }

    protected void onCreate(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.alert_new_goal, null, false);
        setView(v);
        goalNameInput = v.findViewById(R.id.newGoalNameTextView);
        goalTargetInput = v.findViewById(R.id.newGoalTargetTextView);
        goalTargetInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(7)});
        setCancelable(true);
        setButton(DialogInterface.BUTTON_POSITIVE, getContext().getString(R.string.save_button_text),
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // This is replaced later
                    }
                });

        setButton(DialogInterface.BUTTON_NEGATIVE, getContext().getString(R.string.cancel_button_text),
                new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // This is replaced later
                    }
                });
        super.onCreate(savedInstanceState);
    }

    public void show(DialogValidationOnClickListener listener) {
        super.show();
        Button positiveButton = getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(listener);
    }

    public void show(Activity activity, final RecyclerView.Adapter adapter, final List<Goal> goals) {
        this.show(activity, adapter, goals, null);
    }

    public void show(Activity activity, final RecyclerView.Adapter adapter, final List<Goal> goals, final Goal goal) {
        super.show();
        Button positiveButton = getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(getListener(activity, adapter, goals, goal));
        if (goal == null) {
            goalNameInput.setText("");
            goalTargetInput.setText("");
        } else {
            goalNameInput.setText(goal.getName());
            goalTargetInput.setText(String.format(Locale.UK, "%d", goal.getTarget()));
        }
    }

    public DialogValidationOnClickListener getListener(Activity activity, final RecyclerView.Adapter adapter, final List<Goal> goals, final Goal goal) {
        return new DialogValidationOnClickListener(activity, this) {
            @Override
            void validate() {
                try {
                    String name = goalNameInput.getText().toString();
                    int target = Integer.parseInt(goalTargetInput.getText().toString());
                    if (name.equals("")) {
                        String m = getContext().getString(R.string.validation_error_missing_goal_name);
                        throw new ValidationException(m);
                    }
                    if (target == 0) {
                        String m = getContext().getString(R.string.validation_error_zero_step_count);
                        throw new ValidationException(m);
                    }
                    for (Goal g : goals) {
                        if (!g.equals(goal) && g.getName().equals(name)) {
                            String m = getContext().getString(R.string.validation_error_goal_name_taken);
                            throw new ValidationException(m);
                        }
                    }
                } catch (NumberFormatException e) {
                    String m = getContext().getString(R.string.validation_error_missing_step_count);
                    throw new ValidationException(m);
                }
            }

            @Override
            void onSuccess() {
                String name = goalNameInput.getText().toString();
                int target = Integer.parseInt(goalTargetInput.getText().toString());
                if (goal != null) {
                    goal.setName(name);
                    goal.setTarget(target);
                } else {
                    goals.add(new Goal(name, target));
                }
                adapter.notifyDataSetChanged();
            }
        };
    }
}
