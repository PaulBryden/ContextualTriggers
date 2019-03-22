package uk.ac.strath.keepfit.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import uk.co.daviddunphy.keepfit.model.Goal;
import uk.co.daviddunphy.keepfit.R;

class GoalAdapter extends RecyclerView.Adapter<GoalAdapter.GoalViewHolder> {

    private final Activity mActivity;
    private List<Goal> mGoals;
    private Goal currentGoal;
    private boolean goalEditingEnabled;

    public static class GoalViewHolder extends RecyclerView.ViewHolder {
        private TextView label;
        private TextView target;
        private ImageButton editButton;
        private ImageButton deleteButton;
        private GoalAlertDialog editGoalAlert;
        private AlertDialog deleteGoalAlert;
        private View view;
        private boolean goalEditingEnabled;

        public GoalViewHolder(View v, boolean goalEditingEnabled) {
            super(v);
            view = v;
            label = v.findViewById(R.id.goalLabelTextView);
            target = v.findViewById(R.id.goalTargetTextView);
            editButton = v.findViewById(R.id.goalEditButton);
            deleteButton = v.findViewById(R.id.goalDeleteButton);
            this.goalEditingEnabled = goalEditingEnabled;
        }

        public void setValues(Goal goal, Goal currentGoal) {
            label.setText(goal.getName());
            target.setText(String.format(Locale.UK, "%d", goal.getTarget()));
            if (goal.equals(currentGoal)) {
                label.setText(view.getResources().getString(R.string.active_goal_label, goal.getName()));
            } else {
                label.setText(goal.getName());
            }
            if (!goalEditingEnabled) {
                editButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
            } else {
                if (goal.equals(currentGoal)) {
                    editButton.setEnabled(false);
                    deleteButton.setEnabled(false);
                } else {
                    editButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        }

    }

    public GoalAdapter(Activity activity, List<Goal> goals, Goal currentGoal, boolean goalEditingEnabled) {
        mActivity = activity;
        mGoals = goals;
        this.currentGoal = currentGoal;
        this.goalEditingEnabled = goalEditingEnabled;
    }

    @NonNull
    @Override
    public GoalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goal_list_entry, parent, false);
        return new GoalViewHolder(v, goalEditingEnabled);
    }

    @Override
    public void onBindViewHolder(@NonNull final GoalViewHolder holder, int i) {
        final Goal goal = mGoals.get(i);
        holder.setValues(goal, currentGoal);
        final GoalAdapter adapter = this;
        holder.editGoalAlert = new GoalAlertDialog(mActivity, R.string.edit_goal_dialog_title);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.editGoalAlert.show(mActivity, adapter, mGoals, goal);
            }
        });
        holder.deleteGoalAlert = createDeleteDialog(goal);
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.deleteGoalAlert.show();
            }
        });
    }

    private AlertDialog createDeleteDialog(final Goal goal) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle(R.string.confirm_delete_title);
        builder.setMessage(mActivity.getResources().getString(R.string.confirm_delete_message, goal.getName()));
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.yes_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mGoals.remove(goal);
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton(R.string.no_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        return builder.create();
    }

    @Override
    public int getItemCount() {
        if (mGoals == null) {
            return 0;
        }
        return mGoals.size();
    }

}
