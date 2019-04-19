package uk.ac.strath.contextualtriggers.triggers;


import android.content.Context;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.Action;
import uk.ac.strath.contextualtriggers.Condition;
import uk.ac.strath.contextualtriggers.actions.NotificationAction;
import uk.ac.strath.contextualtriggers.conditions.AndCondition;
import uk.ac.strath.contextualtriggers.conditions.NotificationHistoryCondition;
import uk.ac.strath.contextualtriggers.conditions.StepCountCondition;
import uk.ac.strath.contextualtriggers.conditions.WeatherSunnyCondition;
import uk.ac.strath.contextualtriggers.data.StepData;
import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.ActualStepAndGoalDataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.managers.NotificationDataManager;
import uk.ac.strath.contextualtriggers.managers.SimulatedStepDataManager;
import uk.ac.strath.contextualtriggers.managers.WeatherDataManager;

public class DefaultTriggers {

    public static ITrigger createWeatherWithNotifyLimitTrigger(IBinder stepBinder, IBinder weatherBinder, IBinder notifyBinder, Context t) throws ClassCastException{
        IDataManager<StepData> stepDataManager;
        IDataManager<Boolean> notificationDataManager;
        Log.d("Create Weather Trigger", stepBinder.toString());
        IDataManager<WeatherData> weatherDataManager;
        WeatherData targetWeather = new WeatherData();
        targetWeather.TemperatureCelsius=1;
        stepDataManager = ((SimulatedStepDataManager.LocalBinder) stepBinder).getInstance();
        weatherDataManager = ((WeatherDataManager.LocalBinder) weatherBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepCountCondition(StepCountCondition.LESS_THAN, 10000, stepDataManager);
        Condition c1 = new WeatherSunnyCondition(targetWeather, weatherDataManager);
        Condition c2 = new NotificationHistoryCondition(10, notificationDataManager);
        Action a = new NotificationAction("Go for a walk ya lazy. It's even sunny ootside!",t);
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();

    }
    //This is a POINTLESS TRIGGER DO NOT USE THIS
    //should probably throw an exception on casting rather than deal with it here
    @SuppressWarnings("unchecked")
    public static ITrigger createStepMonitorTrigger(IBinder binder, Context t) throws ClassCastException{
        IDataManager<StepData> dataManager;
            dataManager = ((ActualStepAndGoalDataManager.LocalBinder) binder).getInstance();
            Trigger.Builder builder = new Trigger.Builder();
            Condition c = new StepCountCondition(StepCountCondition.LESS_THAN, 10000, dataManager);
            Action a = new NotificationAction("Go for a walk ya lazy. It's even sunny ootside!", t);
            builder.setCondition(c);
            builder.setAction(a);
            return builder.build();
    }

    @SuppressWarnings("unchecked")
    public static ITrigger createWeatherTrigger(IBinder stepBinder, IBinder weatherBinder, Context t) throws ClassCastException{
        IDataManager<StepData> stepDataManager;
        Log.d("Create Weather Trigger", stepBinder.toString());
        IDataManager<WeatherData> weatherDataManager;
        WeatherData targetWeather = new WeatherData();
        targetWeather.TemperatureCelsius=1;
        stepDataManager = ((SimulatedStepDataManager.LocalBinder) stepBinder).getInstance();
        weatherDataManager = ((WeatherDataManager.LocalBinder) weatherBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepCountCondition(StepCountCondition.LESS_THAN, 10000, stepDataManager);
        Condition c1 = new WeatherSunnyCondition(targetWeather, weatherDataManager);
        Action a = new NotificationAction("Go for a walk ya lazy. It's even sunny ootside!", t);
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();

    }

    @SuppressWarnings("unchecked")
    public static ITrigger createWalkIdleTrigger(IBinder stepBinder, Context t) throws ClassCastException{
        IDataManager<StepData> stepDataManager;
        Log.d("Create Weather Trigger", stepBinder.toString());

        stepDataManager = ((SimulatedStepDataManager.LocalBinder) stepBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepCountCondition(StepCountCondition.LESS_THAN, 5000, stepDataManager);
        Action a = new NotificationAction("Go for a walk ya lazy. Not sure if its sunny outside?! Go Anyway! You're less than half your goal!", t);
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();

    }
}
