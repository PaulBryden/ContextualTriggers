package uk.ac.strath.keepfit.view;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.ac.strath.keepfit.model.Goal;
import uk.ac.strath.keepfit.model.HistoryEntry;
import uk.ac.strath.keepfit.model.NotificationService;
import uk.ac.strath.keepfit.R;
import uk.ac.strath.keepfit.model.SharedPreferencesManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TextView progressPercentTextView;
    private TextView stepCountTextView;
    private ProgressBar progressBar;
    private ImageButton addStepCountButton;
    private int stepCount;
    private List<Goal> mGoals;
    private Goal mCurrentGoal;
    private AlertDialog mAddStepCountAlert;
    private EditText mAddStepCountAlertInput;
    private Spinner mGoalSpinner;
    private CalendarDay activeDay;
    private SharedPreferencesManager spm;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private View.OnClickListener mAddStepCountListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAddStepCountAlert.show();
            // Focus on the input and open the keyboard
            mAddStepCountAlertInput.requestFocus();
            mAddStepCountAlertInput.postDelayed(new Runnable() {
               @Override public void run(){
                   InputMethodManager keyboard=(InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                   keyboard.showSoftInput(mAddStepCountAlertInput,0);
               }
           },100);
        }
    };

    private AdapterView.OnItemSelectedListener mSelectGoalListener = new AdapterView.OnItemSelectedListener()
    {
        public void onItemSelected(AdapterView<?> parent, View view,
                                   int position, long arg3)
        {
            String goalName = parent.getItemAtPosition(position).toString();
            for (Goal g : mGoals) {
                if (g.getName().equals(goalName)) {
                    mCurrentGoal = g;
                    updateStepCount(stepCount);
                }
            }

        }

        public void onNothingSelected(AdapterView<?> arg0)
        {
            // Should not be possible - if it does happen, ignore.
        }
    };

    private void updateStepCount(int sc) {
        // Progress bar sometimes doesn't redraw - possible Android bug?
        // https://stackoverflow.com/questions/4348032/android-progressbar-does-not-update-progress-view-drawable
        // To reproduce: Add steps, clear steps, switch to another fragment, switch back to Home fragment
        stepCount = sc;
        int progPercent = (int) Math.round(sc * 100.0 / mCurrentGoal.getTarget());
        progressPercentTextView.setText(String.format(Locale.UK, "%d%%", progPercent));
        progressBar.setProgress(progPercent);
        stepCountTextView.setText(String.format(Locale.UK, "%d / %d", sc, mCurrentGoal.getTarget()));
        if (stepCount >= 0.5 * mCurrentGoal.getTarget() && stepCount < 0.7 * mCurrentGoal.getTarget()) {
            Intent intent = new Intent(getContext(), NotificationService.class);
            getContext().startService(intent);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        stepCountTextView = view.findViewById(R.id.stepCountTextView);
        progressPercentTextView = view.findViewById(R.id.progressPercentTextView);
        progressBar = view.findViewById(R.id.progressBar);
        addStepCountButton = view.findViewById(R.id.addStepCountButton);
        mGoalSpinner = view.findViewById(R.id.goalSpinner);
        addStepCountButton.setOnClickListener(mAddStepCountListener);

        spm = new SharedPreferencesManager(getActivity());
        mGoals = spm.loadGoals();
        mCurrentGoal = spm.currentGoal(mGoals);
        activeDay = spm.loadCurrentDate();
        int sc = spm.loadStepCount();
        CalendarDay today = CalendarDay.today();
        if (!activeDay.equals(today)) {
            rolloverDay(today, sc);
        } else {
            updateStepCount(sc);
            spm.saveCurrentDate(activeDay);
        }

        List<String> goalStrings = new ArrayList<>();
        for (Goal g : mGoals) {
            goalStrings.add(g.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), R.layout.spinner_item, goalStrings);
        mGoalSpinner.setAdapter(adapter);
        mGoalSpinner.setSelection(mGoals.indexOf(mCurrentGoal));
        mGoalSpinner.setOnItemSelectedListener(mSelectGoalListener);

        mAddStepCountAlert = createAddStepCountAlert();
        spm.saveActiveFragmentId(R.id.navigation_home);
    }

    private void rolloverDay(CalendarDay today, int sc) {
        KeepFitViewModel vm = ViewModelProviders.of(this).get(KeepFitViewModel.class);
        vm.insert(new HistoryEntry(activeDay, mCurrentGoal, sc));
        activeDay = today;
        spm.saveCurrentDate(activeDay);
        spm.resetStepCount();
        updateStepCount(0);
    }

    private AlertDialog createAddStepCountAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_step_count_title);
        builder.setMessage(R.string.add_step_count_message);
        builder.setCancelable(true);

        mAddStepCountAlertInput = new EditText(getActivity());
        mAddStepCountAlertInput.setHint(R.string.input_hint_step_count);
        mAddStepCountAlertInput.setGravity(Gravity.CENTER_HORIZONTAL);
        mAddStepCountAlertInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        mAddStepCountAlertInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(7)});
        builder.setView(mAddStepCountAlertInput);
        builder.setPositiveButton(R.string.add_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int val = Integer.parseInt(mAddStepCountAlertInput.getText().toString());
                    spm.addManualStepCount(val);
                    updateStepCount(spm.loadStepCount());
                } catch (NumberFormatException e) {
                    // Probably nothing was entered, so nothing to add.
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        return builder.create();
    }

    @Override
    public void onPause() {
        super.onPause();
        spm.saveCurrentGoal(mCurrentGoal);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void loadStepCount() {
        updateStepCount(spm.loadStepCount());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
