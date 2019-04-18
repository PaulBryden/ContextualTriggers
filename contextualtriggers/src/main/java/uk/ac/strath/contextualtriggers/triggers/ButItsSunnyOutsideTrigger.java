//package uk.ac.strath.contextualtriggers.triggers;
//
//import android.app.Service;
//import android.content.Intent;
//import android.content.ServiceConnection;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//
//import java.util.ArrayList;
//
//import uk.ac.strath.contextualtriggers.Condition;
//import uk.ac.strath.contextualtriggers.actions.NotificationAction;
//import uk.ac.strath.contextualtriggers.conditions.AndCondition;
//import uk.ac.strath.contextualtriggers.conditions.NotificationHistoryCondition;
//import uk.ac.strath.contextualtriggers.conditions.StepCountCondition;
//import uk.ac.strath.contextualtriggers.conditions.WeatherSunnyCondition;
//import uk.ac.strath.contextualtriggers.data.WeatherData;
//import uk.ac.strath.contextualtriggers.managers.SimulatedStepDataManager;
//import uk.ac.strath.contextualtriggers.managers.WeatherDataManager;
//
//public class ButItsSunnyOutsideTrigger implements ITrigger
//{
//    AndCondition conditions;
//    NotificationHistoryCondition notifyCondition;
//    public ButItsSunnyOutsideTrigger()
//    {
//        ArrayList<Condition> conditionList = new ArrayList<Condition>();
//        /*Setup Conditions*/
//        StepCountCondition stepCondition = new StepCountCondition(StepCountCondition.LESS_THAN,10000);
//        WeatherData targetWeather = new WeatherData();
//        targetWeather.TemperatureCelsius=14;
//        WeatherSunnyCondition sunnyCondition = new WeatherSunnyCondition(targetWeather);
//        notifyCondition = new NotificationHistoryCondition(30);
//        /*Link Conditions to Data Manager*/
//        SimulatedStepDataManager stepDataManager = SimulatedStepDataManager.getInstance();
//        WeatherDataManager weatherDataManager = WeatherDataManager.getInstance();
//        stepDataManager.register(stepCondition);
//        weatherDataManager.register(sunnyCondition);
//        conditionList.add(stepCondition);
//        conditionList.add(sunnyCondition);
//        conditionList.add(notifyCondition);
//        conditions = new AndCondition(conditionList);
//        conditions.attachTrigger(this);
//        stepDataManager.start();
//        weatherDataManager.start();
//
//        /*Link Conditions to Data Manager*/
//    }
//
//    @Override
//    public void notifyChange()
//    {
//        if(conditions.isSatisfied())
//        {
//            notifyCondition.notifyUpdate(null);
//            NotificationAction newAction = new NotificationAction("Go for a walk ya lazy. It's even sunny ootside!");
//            newAction.execute();
//
//        }
//    }
//}
