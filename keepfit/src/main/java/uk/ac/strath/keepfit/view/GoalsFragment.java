package uk.ac.strath.keepfit.view;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import uk.co.daviddunphy.keepfit.model.Goal;
import uk.co.daviddunphy.keepfit.R;
import uk.co.daviddunphy.keepfit.model.SharedPreferencesManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalsFragment extends Fragment {

    private List<Goal> mGoals;

    private OnFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private FloatingActionButton mAddGoalFAB;
    private GoalAlertDialog mAddNewGoalAlert;

    private FloatingActionButton.OnClickListener mAddGoalListener = new FloatingActionButton.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAddNewGoalAlert.show(getActivity(), mAdapter, mGoals);
        }
    };

    public GoalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GoalsFragment.
     */
    public static GoalsFragment newInstance() {
        GoalsFragment fragment = new GoalsFragment();
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
        return inflater.inflate(R.layout.fragment_goals, container, false);
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SharedPreferencesManager spm = new SharedPreferencesManager(getActivity());
        mGoals = spm.loadGoals();
        mRecyclerView = view.findViewById(R.id.goalListRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GoalAdapter(getActivity(), mGoals, spm.currentGoal(mGoals), spm.isGoalEditingEnabled());
        mRecyclerView.setAdapter(mAdapter);
        mAddGoalFAB = view.findViewById(R.id.addGoalFAB);
        mAddGoalFAB.setOnClickListener(mAddGoalListener);
        mAddNewGoalAlert = new GoalAlertDialog(getActivity(), R.string.add_goal_dialog_title);
        spm.saveActiveFragmentId(R.id.navigation_goals);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferencesManager spm = new SharedPreferencesManager(getActivity());
        spm.saveGoals(mGoals);
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
