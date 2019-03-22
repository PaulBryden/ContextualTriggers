package uk.ac.strath.keepfit.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import uk.co.daviddunphy.keepfit.model.HistoryEntry;
import uk.co.daviddunphy.keepfit.model.HistoryEntryObserver;
import uk.co.daviddunphy.keepfit.R;
import uk.co.daviddunphy.keepfit.model.SharedPreferencesManager;
import uk.co.daviddunphy.keepfit.model.StatisticsCalculator;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private GraphView graphView;
    private TextView averageStepCountDisplay;
    private TextView totalStepCountDisplay;
    private TextView goalCompletionDisplay;
    private TextView currentStreakDisplay;
    private TextView longestStreakDisplay;
    private TextView mostActiveDayDisplay;
    private TextView recordsStartDisplay;
    private CalendarDay lastDay;
    private HistoryEntryObserver heo;
    private int daysInGraph;
    private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance(DateFormat.LONG, Locale.UK);
    private LineGraphSeries<DataPoint> stepSeries;
    private LineGraphSeries<DataPoint> percentSeries;
    private static final long MS_IN_DAY = 24L * 60 * 60 * 1000;
    private LabelFormatter dayLabelFormatter = new DefaultLabelFormatter() {
        @Override
        public String formatLabel(double value, boolean isValueX) {
            if (isValueX) {
                // X value, so show day name or date
                if (value == daysInGraph -1) {
                    return "Now";
                }
                int daysBefore = daysInGraph-1 - (int) value;
                CalendarDay day = CalendarDay.from(new Date(lastDay.getDate().getTime() - MS_IN_DAY * daysBefore));
                if (daysBefore < 7) {
                    String[] day_names = new String[] {"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
                    return day_names[day.getCalendar().get(Calendar.DAY_OF_WEEK)];
                }
                return String.format(Locale.UK, "%d/%d", day.getDay(), day.getMonth() + 1);
            } else {
                // Y value, so just do the usual thing
                return super.formatLabel(value, isValueX);
            }
        }
    };

    public StatisticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HistoryFragment.
     */
    public static StatisticsFragment newInstance(String param1, String param2) {
        StatisticsFragment fragment = new StatisticsFragment();
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
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    private void createGraph() {
        stepSeries = new LineGraphSeries<>();
        percentSeries = new LineGraphSeries<>();
        graphView.addSeries(stepSeries);
        graphView.getSecondScale().addSeries(percentSeries);
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setScalable(true);
        graphView.getViewport().setMaxY(10000);
        graphView.getViewport().setMinY(0);
        graphView.getSecondScale().setMaxY(100);
        graphView.getSecondScale().setMinY(0);
        stepSeries.setColor(getResources().getColor(R.color.graphStepSeries));
        graphView.getGridLabelRenderer().setVerticalLabelsColor(getResources().getColor(R.color.graphStepSeries));
        percentSeries.setColor(getResources().getColor(R.color.graphPercentSeries));
        graphView.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(getResources().getColor(R.color.graphPercentSeries));
        graphView.getGridLabelRenderer().setLabelFormatter(dayLabelFormatter);
        graphView.getSecondScale().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                return String.format(Locale.UK, "%d%%", (int) value);
            }
        });
    }

    private void setGraphData(DataPoint[] stepData, DataPoint[] percentData, int maxSteps, int maxPercent) {
        stepSeries.resetData(stepData);
        percentSeries.resetData(percentData);
        graphView.getViewport().setMaxY(roundUpToPowerOfTen(maxSteps, 3));
        graphView.getSecondScale().setMaxY(Math.max(100, roundUpToPowerOfTen(maxPercent, 1)));
    }

    private static int roundUpToPowerOfTen(int n, int e) {
        int f = (int) Math.pow(10, e);
        return ((n + f) / f) * f;
    }

    private CalendarDay[] lastNDays(int n, CalendarDay day) {
        CalendarDay[] days = new CalendarDay[n];
        for (int i = daysInGraph -1; i >= 0; i--) {
            days[i] = day;
            day = CalendarDay.from(new Date(day.getDate().getTime() - MS_IN_DAY));
        }
        return days;
    }

    private void generateGraph() {
        CalendarDay firstDay = HistoryEntry.convertFromDateCode(Collections.min(heo.getEntries().keySet()));
        long ms = lastDay.getDate().getTime() - firstDay.getDate().getTime();
        daysInGraph = Math.max(10, (int) (1 + (ms / (1000 * 60 * 60 * 24))));
        CalendarDay[] days = lastNDays(daysInGraph, lastDay);
        DataPoint[] stepData = new DataPoint[daysInGraph];
        DataPoint[] percentData = new DataPoint[daysInGraph];
        HistoryEntry entry;
        int maxSteps = 0;
        int maxPercent = 0;
        int sc, pc;
        for (int i = 0; i < daysInGraph; i++) {
            entry = heo.getEntry(days[i]);
            if (entry == null) {
                stepData[i] = new DataPoint(i, 0);
                percentData[i] = new DataPoint(i, 0);
            } else {
                sc = entry.getStepCount();
                pc = entry.getPercentage();
                stepData[i] = new DataPoint(i, sc);
                percentData[i] = new DataPoint(i, pc);
                maxSteps = Math.max(maxSteps, sc);
                maxPercent = Math.max(maxPercent, pc);
            }
        }
        setGraphData(stepData, percentData, maxSteps, maxPercent);
        graphView.getViewport().setMinX(daysInGraph - 8);
        graphView.getViewport().setMaxX(daysInGraph - 1);
    }

    public void updateDisplay(StatisticsCalculator sc) {
        averageStepCountDisplay.setText(String.format(Locale.UK, "%d", (int) Math.round(sc.getAverageStepCount())));
        totalStepCountDisplay.setText(String.format(Locale.UK, "%d", sc.getTotalStepCount()));
        goalCompletionDisplay.setText(String.format(Locale.UK, "%d%%", (int) Math.round(sc.getGoalCompletion() * 100)));
        currentStreakDisplay.setText(String.format(Locale.UK, "%d", sc.getCurrentStreak()));
        longestStreakDisplay.setText(String.format(Locale.UK, "%d", sc.getLongestStreak()));
        String[] day_names = new String[] {"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        mostActiveDayDisplay.setText(day_names[sc.getMostActiveDay()]);
        recordsStartDisplay.setText(DATE_FORMAT.format(sc.getRecordsStart().getDate()));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        averageStepCountDisplay = view.findViewById(R.id.average_step_count_display);
        totalStepCountDisplay = view.findViewById(R.id.total_step_count_display);
        goalCompletionDisplay = view.findViewById(R.id.goal_completion_display);
        currentStreakDisplay = view.findViewById(R.id.current_streak_display);
        longestStreakDisplay = view.findViewById(R.id.longest_streak_display);
        mostActiveDayDisplay = view.findViewById(R.id.most_active_day_display);
        recordsStartDisplay = view.findViewById(R.id.records_start_display);

        SharedPreferencesManager spm = new SharedPreferencesManager(getActivity());
        CalendarDay d = spm.loadCurrentDate();
        HistoryEntry todaysEntry;
        if (d.equals(CalendarDay.today())) {
            todaysEntry = new HistoryEntry(d, spm.currentGoal(), spm.loadStepCount());
        } else {
            todaysEntry = new HistoryEntry(CalendarDay.today(), spm.currentGoal(), 0);
        }
        KeepFitViewModel vm = ViewModelProviders.of(this).get(KeepFitViewModel.class);
        new StatisticsCalculator(this, vm, todaysEntry);

        lastDay = CalendarDay.today();
        daysInGraph = 10;
        graphView = view.findViewById(R.id.graph);
        createGraph();
        heo = new HistoryEntryObserver(this) {
            @Override
            public void update() {
                generateGraph();
            }
        };
        heo.requestEntries();
        spm.saveActiveFragmentId(R.id.navigation_statistics);
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
