package uk.ac.strath.contextualtriggers.triggers;

import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.actions.Action;
import uk.ac.strath.contextualtriggers.actions.SimpleMapNotificationAction;
import uk.ac.strath.contextualtriggers.actions.SimpleNotificationAction;
import uk.ac.strath.contextualtriggers.conditions.AcceptableTimeCondition;
import uk.ac.strath.contextualtriggers.conditions.ActivityPeriodCondition;
import uk.ac.strath.contextualtriggers.conditions.AndCondition;
import uk.ac.strath.contextualtriggers.conditions.ClearWeatherCondition;
import uk.ac.strath.contextualtriggers.conditions.Condition;
import uk.ac.strath.contextualtriggers.conditions.FrequentNotificationPreventionCondition;
import uk.ac.strath.contextualtriggers.conditions.HistoricStepsDaysUnmetCondition;
import uk.ac.strath.contextualtriggers.conditions.StepAndGoalRealCountCondition;
import uk.ac.strath.contextualtriggers.conditions.StepCountCondition;
import uk.ac.strath.contextualtriggers.data.ActivityData;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;
import uk.ac.strath.contextualtriggers.data.TimeOfDayData;
import uk.ac.strath.contextualtriggers.data.VoidData;
import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.ActivityDataManager;
import uk.ac.strath.contextualtriggers.managers.ActualStepAndGoalDataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.managers.IntervalsDataManager;
import uk.ac.strath.contextualtriggers.managers.NotificationDataManager;
import uk.ac.strath.contextualtriggers.managers.WeatherDataManager;

import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_AFTERNOON;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_MORNING;
import static com.google.android.gms.location.DetectedActivity.STILL;
import static uk.ac.strath.contextualtriggers.conditions.StepAndGoalRealCountCondition.LESS_THAN;

public class DefaultTriggers {
    public static ITrigger TimeToWalk(IBinder activityBinder, IBinder intervalBinder, IBinder notifyBinder) {
        IDataManager<ActivityData> activityDataManager;
        IDataManager<VoidData> notificationDataManager;
        IDataManager<TimeOfDayData> intervalDataManager;
        Log.d("Time To Walk Trigger", activityBinder.toString());
        activityDataManager = ((ActivityDataManager.LocalBinder) activityBinder).getInstance();
        intervalDataManager = ((IntervalsDataManager.LocalBinder) intervalBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new ActivityPeriodCondition(60000, STILL,activityDataManager);
        Condition c1 = new FrequentNotificationPreventionCondition(60000,notificationDataManager);
        Condition c2 = new AcceptableTimeCondition(new TimeOfDayData(new int[]{TIME_INTERVAL_MORNING,TIME_INTERVAL_AFTERNOON}), intervalDataManager);
        Action a = new SimpleNotificationAction("You have been inactive for some time. Go for a walk.");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }
    public static ITrigger HalfAndHalf(IBinder stepBinder, IBinder intervalBinder, IBinder notifyBinder)
    {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<VoidData> notificationDataManager;
        IDataManager<TimeOfDayData> intervalDataManager;
        Log.d("HalfAndHalf Trigger","");
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        intervalDataManager = ((IntervalsDataManager.LocalBinder) intervalBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new HistoricStepsDaysUnmetCondition(3,stepDataManager);
        Condition c1 = new FrequentNotificationPreventionCondition(60000,notificationDataManager);
        Condition c2 = new AcceptableTimeCondition(new TimeOfDayData(new int[]{TIME_INTERVAL_MORNING,TIME_INTERVAL_AFTERNOON}), intervalDataManager);
        Condition c3 = new StepAndGoalRealCountCondition(LESS_THAN,stepDataManager);
        Action a = new SimpleNotificationAction("Lets meet today's goal! Let's go for a walk.");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        conditionList.add(c3);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }
    public static ITrigger ButItsSunnyOutside(IBinder stepBinder, IBinder weatherBinder, IBinder notifyBinder) {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<VoidData> notificationDataManager;
        Log.d("Create Weather Trigger", stepBinder.toString());
        IDataManager<WeatherData> weatherDataManager;
        WeatherData targetWeather = new WeatherData();
        targetWeather.TemperatureCelsius=1;
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        weatherDataManager = ((WeatherDataManager.LocalBinder) weatherBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepAndGoalRealCountCondition(StepCountCondition.LESS_THAN, stepDataManager);
        Condition c1 = new ClearWeatherCondition(weatherDataManager);
        Condition c2 = new FrequentNotificationPreventionCondition(10, notificationDataManager);
        Action a = new SimpleMapNotificationAction("Go for a walk ya lazy. It's even sunny ootside!");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }
    public static ITrigger GoingDown(IBinder stepBinder, IBinder weatherBinder, IBinder notifyBinder) {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<VoidData> notificationDataManager;
        Log.d("Create Weather Trigger", stepBinder.toString());
        IDataManager<WeatherData> weatherDataManager;
        WeatherData targetWeather = new WeatherData();
        targetWeather.TemperatureCelsius=1;
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        weatherDataManager = ((WeatherDataManager.LocalBinder) weatherBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepAndGoalRealCountCondition(StepCountCondition.LESS_THAN, stepDataManager);
        Condition c1 = new ClearWeatherCondition(weatherDataManager);
        Condition c2 = new FrequentNotificationPreventionCondition(10, notificationDataManager);
        Action a = new SimpleMapNotificationAction("Go for a walk ya lazy. It's even sunny ootside!");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }
    public static ITrigger WalkAndTalk(IBinder stepBinder, IBinder weatherBinder, IBinder notifyBinder) {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<VoidData> notificationDataManager;
        Log.d("Create Weather Trigger", stepBinder.toString());
        IDataManager<WeatherData> weatherDataManager;
        WeatherData targetWeather = new WeatherData();
        targetWeather.TemperatureCelsius=1;
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        weatherDataManager = ((WeatherDataManager.LocalBinder) weatherBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepAndGoalRealCountCondition(StepCountCondition.LESS_THAN, stepDataManager);
        Condition c1 = new ClearWeatherCondition(weatherDataManager);
        Condition c2 = new FrequentNotificationPreventionCondition(10, notificationDataManager);
        Action a = new SimpleMapNotificationAction("Go for a walk ya lazy. It's even sunny ootside!");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }
    public static ITrigger DanceForYourDinner(IBinder stepBinder, IBinder weatherBinder, IBinder notifyBinder) {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<VoidData> notificationDataManager;
        Log.d("Create Weather Trigger", stepBinder.toString());
        IDataManager<WeatherData> weatherDataManager;
        WeatherData targetWeather = new WeatherData();
        targetWeather.TemperatureCelsius=1;
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        weatherDataManager = ((WeatherDataManager.LocalBinder) weatherBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepAndGoalRealCountCondition(StepCountCondition.LESS_THAN, stepDataManager);
        Condition c1 = new ClearWeatherCondition(weatherDataManager);
        Condition c2 = new FrequentNotificationPreventionCondition(10, notificationDataManager);
        Action a = new SimpleMapNotificationAction("Go for a walk ya lazy. It's even sunny ootside!");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }
    public static ITrigger GoGreen(IBinder stepBinder, IBinder weatherBinder, IBinder notifyBinder) {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<VoidData> notificationDataManager;
        Log.d("Create Weather Trigger", stepBinder.toString());
        IDataManager<WeatherData> weatherDataManager;
        WeatherData targetWeather = new WeatherData();
        targetWeather.TemperatureCelsius=1;
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        weatherDataManager = ((WeatherDataManager.LocalBinder) weatherBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepAndGoalRealCountCondition(StepCountCondition.LESS_THAN, stepDataManager);
        Condition c1 = new ClearWeatherCondition(weatherDataManager);
        Condition c2 = new FrequentNotificationPreventionCondition(10, notificationDataManager);
        Action a = new SimpleMapNotificationAction("Go for a walk ya lazy. It's even sunny ootside!");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }
    public static ITrigger WalkToWorkOnWeekdays(IBinder stepBinder, IBinder weatherBinder, IBinder notifyBinder) {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<VoidData> notificationDataManager;
        Log.d("Create Weather Trigger", stepBinder.toString());
        IDataManager<WeatherData> weatherDataManager;
        WeatherData targetWeather = new WeatherData();
        targetWeather.TemperatureCelsius=1;
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        weatherDataManager = ((WeatherDataManager.LocalBinder) weatherBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepAndGoalRealCountCondition(StepCountCondition.LESS_THAN, stepDataManager);
        Condition c1 = new ClearWeatherCondition(weatherDataManager);
        Condition c2 = new FrequentNotificationPreventionCondition(10, notificationDataManager);
        Action a = new SimpleMapNotificationAction("Go for a walk ya lazy. It's even sunny ootside!");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }
    public static ITrigger Congratulations(IBinder stepBinder, IBinder weatherBinder, IBinder notifyBinder) {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<VoidData> notificationDataManager;
        Log.d("Create Weather Trigger", stepBinder.toString());
        IDataManager<WeatherData> weatherDataManager;
        WeatherData targetWeather = new WeatherData();
        targetWeather.TemperatureCelsius=1;
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        weatherDataManager = ((WeatherDataManager.LocalBinder) weatherBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepAndGoalRealCountCondition(StepCountCondition.LESS_THAN, stepDataManager);
        Condition c1 = new ClearWeatherCondition(weatherDataManager);
        Condition c2 = new FrequentNotificationPreventionCondition(10, notificationDataManager);
        Action a = new SimpleMapNotificationAction("Go for a walk ya lazy. It's even sunny ootside!");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }
}
