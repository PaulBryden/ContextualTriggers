package uk.ac.strath.keepfit.view;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.InputType;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import uk.co.daviddunphy.keepfit.model.Goal;
import uk.co.daviddunphy.keepfit.model.HistoryEntry;
import uk.co.daviddunphy.keepfit.model.HistoryEntryObserver;
import uk.co.daviddunphy.keepfit.R;
import uk.co.daviddunphy.keepfit.model.SharedPreferencesManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private TextView dateTextView;
    private TextView stepCountTextView;
    private TextView goalTextView;
    private TextView targetTextView;
    private TextView percentTextView;
    private ImageButton addActivityButton;
    private MaterialCalendarView calendarView;
    private CalendarDay activeDate;
    private ColorStateList defaultTextColours;
    private HistoryEntryObserver heo;
    private SharedPreferencesManager spm;

    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.LONG, Locale.UK);

    private AlertDialog addActivityDialog;
    private AlertDialog addGoalAndActivityDialog;

    private OnDateSelectedListener dateChangeListener = new OnDateSelectedListener() {
        @Override
        public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
            if (selected) {
                updateDisplays(date);
            }
        }
    };

    private View.OnClickListener addActivityOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HistoryEntry entry = heo.getEntry(activeDate);
            if (entry == null) {
                addGoalAndActivityDialog.setMessage(getResources().getString(R.string.add_historic_step_count_and_goal_message, DATE_FORMAT.format(activeDate.getDate())));
                addGoalAndActivityDialog.show();
            } else {
                addActivityDialog.setMessage(getResources().getString(R.string.add_historic_step_count_message, DATE_FORMAT.format(activeDate.getDate())));
                addActivityDialog.show();
            }
        }
    };
    private KeepFitViewModel vm;

    private class TodayDayViewDecorator implements DayViewDecorator {

        private CalendarDay today;

        TodayDayViewDecorator(CalendarDay today) {
            this.today = today;
        }

        @Override
        public boolean shouldDecorate(CalendarDay calendarDay) {
            return calendarDay.equals(today);
        }

        @Override
        public void decorate(DayViewFacade dayViewFacade) {
            dayViewFacade.addSpan(new StyleSpan(Typeface.BOLD));
        }
    }

    private class EntryDayViewDecorator implements DayViewDecorator {

        private int colourId;
        private int minPercent;
        private int maxPercent;

        EntryDayViewDecorator(int colourId, int minPercent, int maxPercent) {
            this.colourId = colourId;
            this.minPercent = minPercent;
            this.maxPercent = maxPercent;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            if (heo.getEntries() == null) {
                return false;
            }
            HistoryEntry entry = heo.getEntries().get(day.hashCode());
            if (entry == null) {
                return false;
            }
            int percent = entry.getPercentage();
            return percent <= maxPercent && percent >= minPercent;
        }

        @Override
        public void decorate(DayViewFacade dayViewFacade) {
            dayViewFacade.addSpan(new DotSpan(8.0f, getResources().getColor(colourId)));
        }
    }

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistoryFragment.
     */
    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
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
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    private void updateDisplays(CalendarDay date) {
        this.activeDate = date;
        HistoryEntry entry = heo.getEntries().get(date.hashCode());
        if (date.equals(CalendarDay.today())) {
            dateTextView.setText(R.string.today_string);
            addActivityButton.setVisibility(View.GONE);
        } else {
            dateTextView.setText(DATE_FORMAT.format(date.getDate()));
            CalendarDay startDate = CalendarDay.from(new Date(CalendarDay.today().getDate().getTime() - 24L * 60*60*1000*spm.getHistoryDuration()));
            if (activeDate.isAfter(startDate)) {
                addActivityButton.setVisibility(View.VISIBLE);
            } else {
                addActivityButton.setVisibility(View.GONE);
            }
        }
        if (entry == null) {
            stepCountTextView.setText("0");
            goalTextView.setText(R.string.no_entry_label);
            targetTextView.setText(R.string.no_entry_label);
            percentTextView.setText(R.string.no_entry_label);
            percentTextView.setTextColor(defaultTextColours);
        } else {
            stepCountTextView.setText(String.format(Locale.UK, "%d", entry.getStepCount()));
            goalTextView.setText(entry.getGoal().getName());
            int target = entry.getGoal().getTarget();
            targetTextView.setText(String.format(Locale.UK, "%d", target));
            int percent = entry.getPercentage();
            percentTextView.setText(String.format(Locale.UK, "%d%%", percent));
            percentTextView.setTextColor(getResources().getColor(GradientColourManager.getColour(percent)));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        final CalendarDay today = CalendarDay.today();
        activeDate = today;

        heo = new HistoryEntryObserver(this) {
            @Override
            public void update() {
                if (getEntries().containsKey(activeDate.hashCode())) {
                    updateDisplays(activeDate);
                }
                calendarView.invalidateDecorators();
            }
        };
        heo.requestEntries();
        spm = new SharedPreferencesManager(getActivity());

        super.onViewCreated(view, savedInstanceState);
        this.dateTextView = view.findViewById(R.id.history_date_display);
        this.stepCountTextView = view.findViewById(R.id.history_step_count_display);
        this.goalTextView = view.findViewById(R.id.history_goal_display);
        this.targetTextView = view.findViewById(R.id.history_target_display);
        this.percentTextView = view.findViewById(R.id.history_percent_display);
        this.calendarView = view.findViewById(R.id.calendarView);
        this.addActivityButton = view.findViewById(R.id.add_historic_activity_button);
        this.addActivityDialog = createAddActivityAlert();
        this.addGoalAndActivityDialog = createAddGoalAndActivityAlert();
        addActivityButton.setOnClickListener(addActivityOnClickListener);
        calendarView.state().edit().setMaximumDate(today).setFirstDayOfWeek(2).commit();
        defaultTextColours = dateTextView.getTextColors();

        vm = ViewModelProviders.of(this).get(KeepFitViewModel.class);
        
        calendarView.setOnDateChangedListener(dateChangeListener);
        for (Map.Entry<Integer, GradientColourManager.Range> e : GradientColourManager.RANGES.entrySet()) {
            calendarView.addDecorator(new EntryDayViewDecorator(e.getKey(), e.getValue().min, e.getValue().max));
        }
        calendarView.addDecorator(new TodayDayViewDecorator(today));
        calendarView.setSelectedDate(activeDate);
        updateDisplays(activeDate);
        spm.saveActiveFragmentId(R.id.navigation_history);
    }

    private AlertDialog createAddGoalAndActivityAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_historic_step_count_title);
        builder.setCancelable(true);

        View v = LayoutInflater.from(getContext()).inflate(R.layout.alert_add_historic_count_and_goal, null, false);
        final EditText stepCountInput = v.findViewById(R.id.add_historic_count_and_goal_count_input);
        stepCountInput.setHint(R.string.input_hint_step_count);
        stepCountInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(7)});
        final Spinner goalSpinner = v.findViewById(R.id.add_historic_count_and_goal_spinner);

        final List<Goal> goals = spm.loadGoals();
        List<String> goalStrings = new ArrayList<>();
        for (Goal g : goals) {
            goalStrings.add(g.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, goalStrings) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView v = (TextView) super.getView(position, convertView, parent);
                v.setGravity(Gravity.CENTER_HORIZONTAL);
                v.setTextSize(18);
                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                TextView v = (TextView) super.getDropDownView(position, convertView, parent);
                v.setGravity(Gravity.CENTER_HORIZONTAL);
                v.setTextSize(18);
                return v;
            }
        };
        goalSpinner.setAdapter(adapter);

        builder.setView(v);
        builder.setPositiveButton(R.string.add_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int count = Integer.parseInt(stepCountInput.getText().toString());
                    String goalName = (String) goalSpinner.getSelectedItem();
                    for (Goal g : goals) {
                        if (g.getName().equals(goalName)) {
                            addStepCountAndGoal(count, g);
                        }
                    }
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

    private AlertDialog createAddActivityAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.add_historic_step_count_title);
        builder.setCancelable(true);
        final EditText stepCountInput = new EditText(getActivity());
        stepCountInput.setGravity(Gravity.CENTER_HORIZONTAL);
        stepCountInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        stepCountInput.setFilters(new InputFilter[] {new InputFilter.LengthFilter(7)});
        stepCountInput.setHint(R.string.input_hint_step_count);
        builder.setView(stepCountInput);
        builder.setPositiveButton(R.string.add_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    int val = Integer.parseInt(stepCountInput.getText().toString());
                    addStepCount(val);
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

    private void addStepCount(int count) {
        HistoryEntry entry = heo.getEntry(activeDate);
        vm.insert(new HistoryEntry(entry.getDate(), entry.getGoal(), entry.getStepCount() + count));
    }

    private void addStepCountAndGoal(int count, Goal goal) {
        vm.insert(new HistoryEntry(activeDate, goal, count));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
