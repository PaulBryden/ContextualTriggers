package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.gms.awareness.state.TimeIntervals;
import com.google.android.gms.awareness.state.Weather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * Condition satisfied if current weather matches a target value. Use constants defined in
 * WeatherService to represent weather states.
 */
public class AcceptableTimeCondition extends DataCondition<int[]> {

    private int[] targetIntervals;

    public AcceptableTimeCondition(int[] targetIntervals, IDataManager<int[]> dataManager)
    {
        super(targetIntervals, dataManager);
        this.targetIntervals = targetIntervals;
    }

    @Override
    public boolean isSatisfied()
    {   boolean equivalent=true;
        System.out.println(Arrays.toString(getData()));
        System.out.println(Arrays.toString(targetIntervals));
        ArrayList<Integer> currentList = new ArrayList<Integer>();
        ArrayList<Integer> targetList = new ArrayList<Integer>();
        for (int i : getData())
        {
            currentList.add(i);
        }
        for (int i : targetIntervals)
        {
            targetList.add(i);
        }

        return Collections.indexOfSubList(currentList,targetList)!=-1;
    }

}
