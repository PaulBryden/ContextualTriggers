package uk.ac.strath.contextualtriggers.triggers;


import android.os.IBinder;

import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.actions.Action;
import uk.ac.strath.contextualtriggers.actions.CustomMapNotificationAction;
import uk.ac.strath.contextualtriggers.actions.SimpleMapNotificationAction;
import uk.ac.strath.contextualtriggers.actions.SimpleNotificationAction;
import uk.ac.strath.contextualtriggers.conditions.AcceptableTimeCondition;
import uk.ac.strath.contextualtriggers.conditions.ActivityPeriodCondition;
import uk.ac.strath.contextualtriggers.conditions.AltitudeTransitionCondition;
import uk.ac.strath.contextualtriggers.conditions.AndCondition;
import uk.ac.strath.contextualtriggers.conditions.ClearWeatherCondition;
import uk.ac.strath.contextualtriggers.conditions.Condition;
import uk.ac.strath.contextualtriggers.conditions.FrequentNotificationPreventionCondition;
import uk.ac.strath.contextualtriggers.conditions.GymNearbyCondition;
import uk.ac.strath.contextualtriggers.conditions.HistoricStepsDaysUnmetCondition;
import uk.ac.strath.contextualtriggers.conditions.InBuildingCondition;
import uk.ac.strath.contextualtriggers.conditions.InPlaceTypeCondition;
import uk.ac.strath.contextualtriggers.conditions.MeetingCondition;
import uk.ac.strath.contextualtriggers.conditions.NoLongerInPlaceTypeCondition;
import uk.ac.strath.contextualtriggers.conditions.NotNotifiedTodayCondition;
import uk.ac.strath.contextualtriggers.conditions.StepAndGoalRealCountCondition;
import uk.ac.strath.contextualtriggers.conditions.StepCountCondition;
import uk.ac.strath.contextualtriggers.data.ActivityData;
import uk.ac.strath.contextualtriggers.data.AltitudeData;
import uk.ac.strath.contextualtriggers.data.CalendarData;
import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;
import uk.ac.strath.contextualtriggers.data.TimeOfDayData;
import uk.ac.strath.contextualtriggers.data.VoidData;
import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.ActivityDataManager;
import uk.ac.strath.contextualtriggers.managers.ActualStepAndGoalDataManager;
import uk.ac.strath.contextualtriggers.managers.AltitudeDataManager;
import uk.ac.strath.contextualtriggers.managers.CalendarDataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.managers.IntervalsDataManager;
import uk.ac.strath.contextualtriggers.managers.NotificationDataManager;
import uk.ac.strath.contextualtriggers.managers.PlacesDataManager;
import uk.ac.strath.contextualtriggers.managers.WeatherDataManager;

import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_AFTERNOON;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_EVENING;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_MORNING;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_WEEKDAY;
import static com.google.android.gms.location.DetectedActivity.STILL;
import static uk.ac.strath.contextualtriggers.conditions.StepAndGoalRealCountCondition.LESS_THAN;

public class DefaultTriggers {

    public static ITrigger TimeToWalk(IBinder activityBinder, IBinder intervalBinder, IBinder notifyBinder) {
        IDataManager<ActivityData> activityDataManager;
        IDataManager<VoidData> notificationDataManager;
        IDataManager<TimeOfDayData> intervalDataManager;
        activityDataManager = ((ActivityDataManager.LocalBinder) activityBinder).getInstance();
        intervalDataManager = ((IntervalsDataManager.LocalBinder) intervalBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new ActivityPeriodCondition(60, STILL,activityDataManager);
        Condition c1 = new FrequentNotificationPreventionCondition(60,notificationDataManager);
        Condition c2 = new AcceptableTimeCondition(new TimeOfDayData(new int[]{TIME_INTERVAL_MORNING,TIME_INTERVAL_AFTERNOON,TIME_INTERVAL_EVENING}), intervalDataManager);
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

    public static ITrigger GyminyCricket(IBinder placesBinder, IBinder notifyBinder)
    {
        IDataManager<PlacesData> placeDataManager;
        IDataManager<VoidData> notificationDataManager;
        placeDataManager = ((PlacesDataManager.LocalBinder) placesBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new GymNearbyCondition(placeDataManager);
        Condition c1 = new NotNotifiedTodayCondition(notificationDataManager);
        Action a = new CustomMapNotificationAction("There is a gym nearby. Let's go on the treadmill.","gym");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
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
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        intervalDataManager = ((IntervalsDataManager.LocalBinder) intervalBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new HistoricStepsDaysUnmetCondition(3,stepDataManager);
        Condition c1 = new FrequentNotificationPreventionCondition(60,notificationDataManager);
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

    public static ITrigger ButItsSunnyOutside(IBinder stepBinder, IBinder weatherBinder, IBinder activityBinder, IBinder notifyBinder) {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<VoidData> notificationDataManager;
        IDataManager<WeatherData> weatherDataManager;
        IDataManager<ActivityData> activityDataManager;
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        weatherDataManager = ((WeatherDataManager.LocalBinder) weatherBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        activityDataManager = ((ActivityDataManager.LocalBinder) activityBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepAndGoalRealCountCondition(StepCountCondition.LESS_THAN, stepDataManager);
        Condition c1 = new ClearWeatherCondition(weatherDataManager);
        Condition c2 = new FrequentNotificationPreventionCondition(60, notificationDataManager);
        Condition c3 = new ActivityPeriodCondition(60, STILL,activityDataManager);
        Action a = new SimpleMapNotificationAction("The weather is clear. Would you like to go for a walk?");
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

    public static ITrigger GoingDown(IBinder stepBinder, IBinder placesBinder, IBinder altitudeBinder,  IBinder notifyBinder) {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<VoidData> notificationDataManager;
        IDataManager<AltitudeData> altitudeDataManager;
        IDataManager<PlacesData> placesDataManager;
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        placesDataManager = ((PlacesDataManager.LocalBinder) placesBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        altitudeDataManager = ((AltitudeDataManager.LocalBinder) altitudeBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new HistoricStepsDaysUnmetCondition(3, stepDataManager);
        Condition c1 = new InBuildingCondition(placesDataManager);
        Condition c2 = new StepAndGoalRealCountCondition(LESS_THAN, stepDataManager);
        Condition c3 = new FrequentNotificationPreventionCondition(60, notificationDataManager);
        Condition c4 = new AltitudeTransitionCondition(20,altitudeDataManager);
        Action a = new SimpleNotificationAction("You haven't met your step goal. Would you like to walk down?");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        conditionList.add(c3);
        conditionList.add(c4);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }

    public static ITrigger WalkAndTalk(IBinder stepBinder, IBinder calendarBinder, IBinder notifyBinder) {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<CalendarData> calendarDataManager;
        IDataManager<VoidData> notificationDataManager;
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        calendarDataManager = ((CalendarDataManager.LocalBinder) calendarBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepAndGoalRealCountCondition(StepCountCondition.LESS_THAN, stepDataManager);
        Condition c1 = new MeetingCondition(calendarDataManager);
        Condition c2 = new FrequentNotificationPreventionCondition(30, notificationDataManager);
        Action a = new SimpleMapNotificationAction("Would you like to have a walking meeting?");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }

    public static ITrigger DanceForYourDinner(IBinder stepBinder, IBinder placesBinder, IBinder notifyBinder) {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<VoidData> notificationDataManager;
        IDataManager<PlacesData> placesDataManager;
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        placesDataManager = ((PlacesDataManager.LocalBinder) placesBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepAndGoalRealCountCondition(StepCountCondition.LESS_THAN, stepDataManager);
        Condition c1 = new InPlaceTypeCondition(Place.Type.FOOD, placesDataManager);
        Condition c2 = new FrequentNotificationPreventionCondition(30, notificationDataManager);
        Action a = new SimpleMapNotificationAction("We hope you enjoyed your lunch. Let's walk it off.");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        conditionList.add(c2);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }

    public static ITrigger WalkToWorkOnWeekdays(IBinder stepBinder, IBinder intervalBinder, IBinder notifyBinder) {
        IDataManager<StepAndGoalData> stepDataManager;
        IDataManager<VoidData> notificationDataManager;
        IDataManager<TimeOfDayData> timeOfDayDataManager;
        stepDataManager = ((ActualStepAndGoalDataManager.LocalBinder) stepBinder).getInstance();
        timeOfDayDataManager = ((IntervalsDataManager.LocalBinder) intervalBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new StepAndGoalRealCountCondition(StepCountCondition.LESS_THAN, stepDataManager);
        Condition c1 = new AcceptableTimeCondition(new TimeOfDayData(new int[]{TIME_INTERVAL_WEEKDAY,TIME_INTERVAL_MORNING}),timeOfDayDataManager);
        Condition c2 = new FrequentNotificationPreventionCondition(60, notificationDataManager);
        Condition c3 = new HistoricStepsDaysUnmetCondition(3,stepDataManager);
        Action a = new CustomMapNotificationAction("Lets walk to work today!","work");
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

    public static ITrigger Congratulations(IBinder placesBinder, IBinder notifyBinder) {
        IDataManager<VoidData> notificationDataManager;
        IDataManager<PlacesData> placesDataManager;
        placesDataManager = ((PlacesDataManager.LocalBinder) placesBinder).getInstance();
        notificationDataManager = ((NotificationDataManager.LocalBinder) notifyBinder).getInstance();
        Trigger.Builder builder = new Trigger.Builder();
        Condition c = new NoLongerInPlaceTypeCondition(Place.Type.GYM, placesDataManager);
        Condition c1 = new FrequentNotificationPreventionCondition(60, notificationDataManager);
        Action a = new SimpleNotificationAction("You went to the gym! Congratulations.");
        List<Condition> conditionList = new ArrayList<>();
        conditionList.add(c);
        conditionList.add(c1);
        Condition and = new AndCondition(conditionList);
        builder.setCondition(and);
        builder.setAction(a);
        return builder.build();
    }
}
