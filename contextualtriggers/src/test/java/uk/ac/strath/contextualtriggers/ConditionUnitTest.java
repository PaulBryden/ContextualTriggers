package uk.ac.strath.contextualtriggers;

import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcel;
import android.support.annotation.Nullable;

import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.PlusCode;

import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.ac.strath.contextualtriggers.actions.UnitTestAction;
import uk.ac.strath.contextualtriggers.conditions.AcceptableTimeCondition;
import uk.ac.strath.contextualtriggers.conditions.ActivityPeriodCondition;
import uk.ac.strath.contextualtriggers.conditions.AltitudeTransitionCondition;
import uk.ac.strath.contextualtriggers.conditions.ClearWeatherCondition;
import uk.ac.strath.contextualtriggers.conditions.FrequentNotificationPreventionCondition;
import uk.ac.strath.contextualtriggers.conditions.GymNearbyCondition;
import uk.ac.strath.contextualtriggers.conditions.HistoricStepsDaysUnmetCondition;
import uk.ac.strath.contextualtriggers.conditions.InBuildingCondition;
import uk.ac.strath.contextualtriggers.conditions.InBuildingTypeCondition;
import uk.ac.strath.contextualtriggers.conditions.NoLongerInBuildingTypeCondition;
import uk.ac.strath.contextualtriggers.conditions.NotNotifiedTodayCondition;
import uk.ac.strath.contextualtriggers.conditions.StepAndGoalRealCountCondition;
import uk.ac.strath.contextualtriggers.data.ActivityData;
import uk.ac.strath.contextualtriggers.data.AltitudeData;
import uk.ac.strath.contextualtriggers.data.DayData;
import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.data.StepAndGoalData;
import uk.ac.strath.contextualtriggers.data.TimeOfDayData;
import uk.ac.strath.contextualtriggers.data.VoidData;
import uk.ac.strath.contextualtriggers.data.WeatherData;
import uk.ac.strath.contextualtriggers.managers.DataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.managers.NotificationDataManager;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_AFTERNOON;
import static com.google.android.gms.awareness.fence.TimeFence.TIME_INTERVAL_MORNING;
import static com.google.android.gms.location.DetectedActivity.STILL;
import static org.junit.Assert.*;
import static uk.ac.strath.contextualtriggers.conditions.StepAndGoalRealCountCondition.LESS_THAN;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ConditionUnitTest
{

    @Test
    public void AcceptableTimeConditionUnitTest() {
        class TimeIntervalsDataManager extends DataManager<TimeOfDayData> implements IDataManager<TimeOfDayData>
        {
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {TimeOfDayData test = new TimeOfDayData(new int[]{TIME_INTERVAL_MORNING, TIME_INTERVAL_AFTERNOON});
                sendUpdate(test);
            }
        }

        UnitTestAction action = new UnitTestAction();
        TimeIntervalsDataManager manager = new TimeIntervalsDataManager();
        TimeOfDayData desiredTimeInterval = new TimeOfDayData(new int[]{TIME_INTERVAL_AFTERNOON});
        AcceptableTimeCondition periodCondition = new AcceptableTimeCondition(desiredTimeInterval,manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(periodCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(true,periodCondition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest");
        manager.mock();
        assertEquals(true,periodCondition.isSatisfied());
    }

    @Test
    public void ActivityPeriodConditionUnitTest() {
        class ActivityDataManager extends DataManager<ActivityData> implements IDataManager<ActivityData>
        {
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                sendUpdate(new ActivityData(new DetectedActivity(STILL,100)));
            }
        }

        UnitTestAction action = new UnitTestAction();
        ActivityDataManager manager = new ActivityDataManager();
        ActivityPeriodCondition periodCondition = new ActivityPeriodCondition(10000, STILL,manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(periodCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(false,periodCondition.isSatisfied());
        System.out.println("ActivityPeriodConditionUnitTest");
        try
        {
            Thread.sleep(10500);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        manager.mock();
        assertEquals(true,periodCondition.isSatisfied());
    }


    @Test
    public void AltitudeTransitionConditionUnitTest() {
        class AltitudeMockManager extends DataManager<AltitudeData> implements IDataManager<AltitudeData>
        {
            boolean firstTime=true;
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                AltitudeData data = new AltitudeData();
                if(firstTime)
                {
                    data.altitude = 0;
                    firstTime=false;
                }
                else
                {
                    data.altitude = 20;
                }
                sendUpdate(data);
            }
        }

        UnitTestAction action = new UnitTestAction();
        AltitudeMockManager manager = new AltitudeMockManager();
        AltitudeTransitionCondition altTransCondition = new AltitudeTransitionCondition(19,manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(false,altTransCondition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest");
        manager.mock();
        assertEquals(true,altTransCondition.isSatisfied());
    }


    @Test
    public void ClearWeatherConditionUnitTest() {
        class ClearWeatherMockManager extends DataManager<WeatherData> implements IDataManager<WeatherData>
        {
            boolean firstTime=true;
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                WeatherData data = new WeatherData();
                if(firstTime)
                {
                    data.Conditions = new int[]{Weather.CONDITION_ICY};
                    firstTime=false;
                }
                else
                {
                    data.Conditions = new int[]{Weather.CONDITION_CLEAR};
                }
                sendUpdate(data);
            }
        }

        UnitTestAction action = new UnitTestAction();
        ClearWeatherMockManager manager = new ClearWeatherMockManager();
        ClearWeatherCondition altTransCondition = new ClearWeatherCondition(manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(false,altTransCondition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest");
        manager.mock();
        assertEquals(true,altTransCondition.isSatisfied());
    }

    @Test
    public void FrequentNotificationPreventionConditionUnitTest() {
        class NotificationMockDataManager extends DataManager<VoidData> implements IDataManager<VoidData>
        {
            boolean firstTime=true;
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                sendUpdate(null);
            }
        }

        UnitTestAction action = new UnitTestAction();
        NotificationMockDataManager manager = new NotificationMockDataManager();
        FrequentNotificationPreventionCondition altTransCondition = new FrequentNotificationPreventionCondition(10000,manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        assertEquals(true,altTransCondition.isSatisfied());
        System.out.println("FrequentNotificationPreventionConditionUnitTest");
        manager.mock();
        assertEquals(false,altTransCondition.isSatisfied());
        try
        {
            Thread.sleep(10500);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        assertEquals(true,altTransCondition.isSatisfied());
        manager.mock();
        assertEquals(false,altTransCondition.isSatisfied());

    }

    @Test
    public void GymNearbyConditionUnitTest() {
        class PlacesMockDataManager extends DataManager<PlacesData> implements IDataManager<PlacesData>
        {
            boolean firstTime=true;
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                List<PlaceLikelihood> data = new ArrayList<PlaceLikelihood>();
                Place gym = new Place()
                {
                    @Override
                    public int describeContents()
                    {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel parcel, int i)
                    {

                    }

                    @Override
                    public String getId()
                    {
                        return null;
                    }


                    @Nullable
                    @Override
                    public String getAddress()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public AddressComponents getAddressComponents()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<String> getAttributions()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public OpeningHours getOpeningHours()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getPhoneNumber()
                    {
                        return null;
                    }

                    @Override
                    public LatLng getLatLng()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getName()
                    {
                        return null;
                    }

                    @Override
                    public LatLngBounds getViewport()
                    {
                        return null;
                    }

                    @Override
                    public Uri getWebsiteUri()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<PhotoMetadata> getPhotoMetadatas()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public PlusCode getPlusCode()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Integer getPriceLevel()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Double getRating()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<Type> getTypes()
                    {
                        ArrayList<Type> types = new ArrayList<Type>();
                        types.add(Type.GYM);
                        return types;
                    }

                    @Nullable
                    @Override
                    public Integer getUserRatingsTotal()
                    {
                        return null;
                    }
                };
                    PlaceLikelihood likelihood = new PlaceLikelihood()
                    {

                        @Override
                        public int describeContents()
                        {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel parcel, int i)
                        {

                        }

                        @Override
                        public Place getPlace()
                        {
                            return gym;
                        }

                        @Override
                        public double getLikelihood()
                        {
                            return 0.25;
                        }
                    };
                    firstTime=false;
                    data.add(likelihood);
                    sendUpdate(new PlacesData(data));
            }
        }

        UnitTestAction action = new UnitTestAction();
        PlacesMockDataManager manager = new PlacesMockDataManager();
        GymNearbyCondition altTransCondition = new GymNearbyCondition(manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        System.out.println("GymNearbyConditionUnitTest");
        manager.mock();
        assertEquals(true,altTransCondition.isSatisfied());

    }

    @Test
    public void HistoricStepsDaysUnmetConditionUnitTest() {
        class StepAndGoalMockDataManager extends DataManager<StepAndGoalData> implements IDataManager<StepAndGoalData>
        {
            public StepAndGoalMockDataManager()
            {
                data = new StepAndGoalData();
            }
            boolean firstTime=true;
            StepAndGoalData data;
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                sendUpdate(data);
            }
        }

        UnitTestAction action = new UnitTestAction();
        StepAndGoalMockDataManager manager = new StepAndGoalMockDataManager();
        HistoricStepsDaysUnmetCondition altTransCondition = new HistoricStepsDaysUnmetCondition(3,manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(true,altTransCondition.isSatisfied());
        System.out.println("HistoricStepsDaysUnmetConditionUnitTest");

    }

    @Test
    public void InBuildingConditionUnitTest()
    {
        class PlacesMockDataManager extends DataManager<PlacesData> implements IDataManager<PlacesData>
        {
            boolean firstTime=true;
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                List<PlaceLikelihood> data = new ArrayList<PlaceLikelihood>();
                Place gym = new Place()
                {
                    @Override
                    public int describeContents()
                    {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel parcel, int i)
                    {

                    }

                    @Override
                    public String getId()
                    {
                        return null;
                    }


                    @Nullable
                    @Override
                    public String getAddress()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public AddressComponents getAddressComponents()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<String> getAttributions()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public OpeningHours getOpeningHours()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getPhoneNumber()
                    {
                        return null;
                    }

                    @Override
                    public LatLng getLatLng()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getName()
                    {
                        return null;
                    }

                    @Override
                    public LatLngBounds getViewport()
                    {
                        return null;
                    }

                    @Override
                    public Uri getWebsiteUri()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<PhotoMetadata> getPhotoMetadatas()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public PlusCode getPlusCode()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Integer getPriceLevel()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Double getRating()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<Type> getTypes()
                    {
                        ArrayList<Type> types = new ArrayList<Type>();
                        types.add(Type.GYM);
                        return types;
                    }

                    @Nullable
                    @Override
                    public Integer getUserRatingsTotal()
                    {
                        return null;
                    }
                };
                PlaceLikelihood likelihood = new PlaceLikelihood()
                {

                    @Override
                    public int describeContents()
                    {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel parcel, int i)
                    {

                    }

                    @Override
                    public Place getPlace()
                    {
                        return gym;
                    }

                    @Override
                    public double getLikelihood()
                    {
                        return 0.76;
                    }
                };
                firstTime=false;
                data.add(likelihood);
                sendUpdate(new PlacesData(data));
            }
        }

        UnitTestAction action = new UnitTestAction();
        PlacesMockDataManager manager = new PlacesMockDataManager();
        InBuildingCondition altTransCondition = new InBuildingCondition(manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(true,altTransCondition.isSatisfied());
        System.out.println("InBuildingConditionUnitTest");
    }

    @Test
    public void InBuildingTypeConditionUnitTest()
    {
        class PlacesMockDataManager extends DataManager<PlacesData> implements IDataManager<PlacesData>
        {
            boolean firstTime=true;
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                List<PlaceLikelihood> data = new ArrayList<PlaceLikelihood>();
                Place gym = new Place()
                {
                    @Override
                    public int describeContents()
                    {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel parcel, int i)
                    {

                    }

                    @Override
                    public String getId()
                    {
                        return null;
                    }


                    @Nullable
                    @Override
                    public String getAddress()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public AddressComponents getAddressComponents()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<String> getAttributions()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public OpeningHours getOpeningHours()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getPhoneNumber()
                    {
                        return null;
                    }

                    @Override
                    public LatLng getLatLng()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getName()
                    {
                        return null;
                    }

                    @Override
                    public LatLngBounds getViewport()
                    {
                        return null;
                    }

                    @Override
                    public Uri getWebsiteUri()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<PhotoMetadata> getPhotoMetadatas()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public PlusCode getPlusCode()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Integer getPriceLevel()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Double getRating()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<Type> getTypes()
                    {
                        ArrayList<Type> types = new ArrayList<Type>();
                        types.add(Type.CAFE);
                        return types;
                    }

                    @Nullable
                    @Override
                    public Integer getUserRatingsTotal()
                    {
                        return null;
                    }
                };
                PlaceLikelihood likelihood = new PlaceLikelihood()
                {

                    @Override
                    public int describeContents()
                    {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel parcel, int i)
                    {

                    }

                    @Override
                    public Place getPlace()
                    {
                        return gym;
                    }

                    @Override
                    public double getLikelihood()
                    {
                        return 0.76;
                    }
                };
                firstTime=false;
                data.add(likelihood);
                sendUpdate(new PlacesData(data));
            }
        }
        UnitTestAction action = new UnitTestAction();
        PlacesMockDataManager manager = new PlacesMockDataManager();
        InBuildingTypeCondition altTransCondition = new InBuildingTypeCondition(Place.Type.CAFE,manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(true,altTransCondition.isSatisfied());
        System.out.println("InBuildingConditionUnitTest");
    }


    @Test
    public void NoLongerInBuildingTypeConditionUnitTest()
    {
        class PlacesMockDataManager extends DataManager<PlacesData> implements IDataManager<PlacesData>
        {
            boolean firstTime=true;
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                List<PlaceLikelihood> data = new ArrayList<PlaceLikelihood>();
                Place gym = new Place()
                {
                    @Override
                    public int describeContents()
                    {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel parcel, int i)
                    {

                    }

                    @Override
                    public String getId()
                    {
                        return null;
                    }


                    @Nullable
                    @Override
                    public String getAddress()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public AddressComponents getAddressComponents()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<String> getAttributions()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public OpeningHours getOpeningHours()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getPhoneNumber()
                    {
                        return null;
                    }

                    @Override
                    public LatLng getLatLng()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getName()
                    {
                        return null;
                    }

                    @Override
                    public LatLngBounds getViewport()
                    {
                        return null;
                    }

                    @Override
                    public Uri getWebsiteUri()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<PhotoMetadata> getPhotoMetadatas()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public PlusCode getPlusCode()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Integer getPriceLevel()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Double getRating()
                    {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<Type> getTypes()
                    {
                        ArrayList<Type> types = new ArrayList<Type>();
                        types.add(Type.CAFE);
                        return types;
                    }

                    @Nullable
                    @Override
                    public Integer getUserRatingsTotal()
                    {
                        return null;
                    }
                };
                PlaceLikelihood likelihood = new PlaceLikelihood()
                {

                    @Override
                    public int describeContents()
                    {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel parcel, int i)
                    {

                    }

                    @Override
                    public Place getPlace()
                    {
                        return gym;
                    }

                    @Override
                    public double getLikelihood()
                    {
                        return 0.76;
                    }
                };

                PlaceLikelihood likelihood2 = new PlaceLikelihood()
                {

                    @Override
                    public int describeContents()
                    {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel parcel, int i)
                    {

                    }

                    @Override
                    public Place getPlace()
                    {
                        return gym;
                    }

                    @Override
                    public double getLikelihood()
                    {
                        return 0.1;
                    }
                };
                if(firstTime)
                {
                    data.add(likelihood);
                    firstTime=false;
                }
                else
                {
                    data.remove(likelihood);
                    data.add(likelihood2);
                }
                sendUpdate(new PlacesData(data));
            }
        }
        UnitTestAction action = new UnitTestAction();
        PlacesMockDataManager manager = new PlacesMockDataManager();
        NoLongerInBuildingTypeCondition altTransCondition = new NoLongerInBuildingTypeCondition(Place.Type.CAFE,manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(false,altTransCondition.isSatisfied());
        manager.mock();
        assertEquals(true,altTransCondition.isSatisfied());
        System.out.println("NoLongerInBuildingConditionUnitTest");
    }

    @Test
    public void NotNotifiedTodayConditionUnitTest() {
        class MockNotificationDataManager extends DataManager<VoidData> implements IDataManager<VoidData>
        {
            public MockNotificationDataManager()
            {
            }
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                sendUpdate(new VoidData());
            }
        }

        UnitTestAction action = new UnitTestAction();
        MockNotificationDataManager manager = new MockNotificationDataManager();
        NotNotifiedTodayCondition altTransCondition = new NotNotifiedTodayCondition(manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        assertEquals(true,altTransCondition.isSatisfied());
        manager.mock();
        assertEquals(false,altTransCondition.isSatisfied());
        System.out.println("NotNotifiedTodayConditionUnitTest");

    }


    @Test
    public void StepAndGoalRealCountConditionUnitTest() {
        class MockStepAndGoalCountManager extends DataManager<StepAndGoalData> implements IDataManager<StepAndGoalData>
        {

            public MockStepAndGoalCountManager()
            {
                firstTime=true;
            }
            boolean firstTime;
            @Nullable
            @Override
            public IBinder onBind(Intent intent)
            {
                return null;
            }
            public void mock()
            {
                if(firstTime)
                {
                    sendUpdate(new StepAndGoalData());
                    firstTime=false;
                }
                else
                {
                    StepAndGoalData fulfilled = new StepAndGoalData();
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Calendar cal0 = Calendar.getInstance();
                    Date today = cal0.getTime();
                    try
                    {
                        today = formatter.parse(formatter.format(today));
                    }
                    catch (ParseException e)
                    {
                        e.printStackTrace();
                    }
                    DayData day = fulfilled.getDay(today);
                    day.steps=100000;
                    fulfilled.updateDay(day);
                    sendUpdate(fulfilled);
                }
            }
        }

        UnitTestAction action = new UnitTestAction();
        MockStepAndGoalCountManager manager = new MockStepAndGoalCountManager();
        StepAndGoalRealCountCondition altTransCondition = new StepAndGoalRealCountCondition(LESS_THAN,manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(true,altTransCondition.isSatisfied());
        manager.mock();
        assertEquals(false,altTransCondition.isSatisfied());
        System.out.println("NotNotifiedTodayConditionUnitTest");

    }
}