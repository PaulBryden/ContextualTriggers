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
import java.util.Locale;

import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.permissions.RequestCalendarPermission;
import uk.ac.strath.contextualtriggers.data.CalendarData;
import uk.ac.strath.contextualtriggers.data.EventData;

import static android.Manifest.permission.READ_CALENDAR;

public class CalendarDataManager extends AlarmDataManager<CalendarData> {
    private final IBinder binder = new CalendarDataManager.LocalBinder();
    // Projection array. Creating indices for this array instead of doing
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.NAME,                          // 1
            CalendarContract.Calendars.ACCOUNT_NAME,         // 2
            CalendarContract.Calendars.ACCOUNT_TYPE                  // 3
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        setup();
        return binder;
    }

    public class LocalBinder extends Binder {
        public IDataManager<CalendarData> getInstance() {
            return CalendarDataManager.this;
        }
    }

    public CalendarDataManager() {
        super(60, 600);
        setup();
    }

    private void setup() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        monitor();
        alarm();
        return START_STICKY;
    }

    private void monitor() {
        if (ContextCompat.checkSelfPermission(this,
                READ_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
                Intent i = new Intent(this, RequestCalendarPermission.class);
                startActivity(i);
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
            for (int i = 0; i < CNames.length; i++) {

                nameOfEvent.add(cursor.getString(1));
                startDates.add(getDate(Long.parseLong(cursor.getString(3))));
                CNames[i] = cursor.getString(1);
                cursor.moveToNext();

            }
            List<EventData> cd = new ArrayList<>();
            for(int i = 0; i < nameOfEvent.size();i++){
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                try {
                    EventData c = new EventData(nameOfEvent.get(i), dateFormat.parse(startDates.get(i)));
                    cd.add(c);
                    Log.d("Calendar Event",c.name+c.time);
                } catch (ParseException e ){
                    Log.e("Calendar","Error parsing date in Calendar");
                }

            }
            sendUpdate(new CalendarData(cd));
        }
    }


    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}

