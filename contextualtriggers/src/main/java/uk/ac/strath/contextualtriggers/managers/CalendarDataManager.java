package uk.ac.strath.contextualtriggers.managers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.RequestCalendarPermission;
import uk.ac.strath.contextualtriggers.RequestLocationPermission;
import uk.ac.strath.contextualtriggers.data.CalendarData;
import uk.ac.strath.contextualtriggers.data.ListCalendarData;

import static android.Manifest.permission.READ_CALENDAR;
//import static com.google.android.gms.internal.zzs.TAG;

public class CalendarDataManager extends AlarmDataManager<ListCalendarData> {
    Logger logger;
    private final IBinder binder = new CalendarDataManager.LocalBinder();
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS;
    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.NAME,                          // 1
            CalendarContract.Calendars.ACCOUNT_NAME,         // 2
            CalendarContract.Calendars.ACCOUNT_TYPE                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        setup();
        return binder;
    }

    public class LocalBinder extends Binder {
        public IDataManager getInstance() {
            return CalendarDataManager.this;
        }
    }

    public CalendarDataManager() {
        super(60, 600);
        setup();
    }

    private void setup() {
        logger = Logger.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor();
        alarm();
        return START_STICKY;
    }

    /*This Could be setup to fire on a transition, instead of a poll*/
    private void monitor() {
        if (ContextCompat.checkSelfPermission(this,
                READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainApplication.getAppActivity(),
                    READ_CALENDAR)) {
                Intent i = new Intent(this, RequestCalendarPermission.class);
                startActivity(i);
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(MainApplication.getAppActivity(),
                        new String[]{READ_CALENDAR},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            }
        } else {

            ArrayList<String> nameOfEvent = new ArrayList<String>();
            ArrayList<String> startDates = new ArrayList<String>();
            ArrayList<String> endDates = new ArrayList<String>();
            ArrayList<String> descriptions = new ArrayList<String>();
            Cursor cursor = getContentResolver()
                    .query(
                            Uri.parse("content://com.android.calendar/events"),
                            new String[]{"calendar_id", "title", "description",
                                    "dtstart", "dtend", "eventLocation"}, null,
                            null, null);
            cursor.moveToFirst();
            // fetching calendars name
            String CNames[] = new String[cursor.getCount()];

            // fetching calendars id
            nameOfEvent.clear();
            startDates.clear();
            endDates.clear();
            descriptions.clear();
            for (int i = 0; i < CNames.length; i++) {

                nameOfEvent.add(cursor.getString(1));
                startDates.add(getDate(Long.parseLong(cursor.getString(3))));
                endDates.add(getDate(Long.parseLong(cursor.getString(4))));
                descriptions.add(cursor.getString(2));
                CNames[i] = cursor.getString(1);
                cursor.moveToNext();

            }
            List<CalendarData> cd = new ArrayList<>();
            for (int i = 0; i < nameOfEvent.size(); i++) {
                // Log.d("CALENDAREVENT", nameOfEvent.get(i));
                //  Log.d("CALENDARTIME", startDates.get(i));
                SimpleDateFormat dateFormat = new SimpleDateFormat("DD/MM/yyyy hh:mm:ss a");
                try {
                    CalendarData c = new CalendarData(nameOfEvent.get(i), dateFormat.parse(startDates.get(i)));
                    cd.add(c);
                } catch (ParseException e) {
                    Log.e("Calendar", "Error parsing date in Calendar");
                }

            }
            sendUpdate(new ListCalendarData(cd));
        }
    }


    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}

