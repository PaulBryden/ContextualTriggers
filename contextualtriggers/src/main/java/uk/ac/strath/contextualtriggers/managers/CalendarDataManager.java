package uk.ac.strath.contextualtriggers.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.ContextualTriggersService;
import uk.ac.strath.contextualtriggers.Logger;
import uk.ac.strath.contextualtriggers.MainApplication;
import uk.ac.strath.contextualtriggers.data.WeatherData;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CALENDAR;
//import static com.google.android.gms.internal.zzs.TAG;

public class CalendarDataManager extends DataManager<DetectedActivity> implements IDataManager<DetectedActivity> {
    Logger logger;
    private final IBinder binder = new CalendarDataManager.LocalBinder();
    int MY_PERMISSIONS_REQUEST_READ_CONTACTS;

    // Projection array. Creating indices for this array instead of doing
// dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[] {
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

    public CalendarDataManager()
    {
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

    private void alarm(){

        AlarmManager alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        Intent ic = new Intent(this, CalendarDataManager.class);
        PendingIntent alarmIntent = PendingIntent.getService(this, 0, ic, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + 5000,
                alarmIntent);
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
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
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
                                new String[] { "calendar_id", "title", "description",
                                        "dtstart", "dtend", "eventLocation" }, null,
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
                for(int i = 0; i < nameOfEvent.size();i++){
                    Log.d("CALENDAR", nameOfEvent.get(i));
                }
            }


        }
            /*Cursor calCursor =
                    getContentResolver().
                            query(CalendarContract.Calendars.CONTENT_URI,
                                    EVENT_PROJECTION,
                                    CalendarContract.Calendars.VISIBLE + " = 1",
                                    null,
                                    CalendarContract.Calendars._ID + " ASC");
           while(calCursor.moveToNext()){
               calCursor.
               List<ContactsContract.CommonDataKinds.Event> calendars = calendarProvider.getEvents(calendar.id).getList();
               calCursor.
               long selectedEventId;// the event-id;
                       String[] proj =
                       new String[]{
                               CalendarContract.Events._ID,
                               CalendarContract.Events.DTSTART,
                               CalendarContract.Events.DTEND,
                               CalendarContract.Events.RRULE,
                               CalendarContract.Events.TITLE};
               Cursor cursor =
                       getContentResolver().
                               query(
                                       CalendarContract.Events.CONTENT_URI,
                                       proj,
                                       CalendarContract.Events._ID + " = ? ",
                                       new String[]{Long.toString(selectedEventId)},
                                       null);
               if (cursor.moveToFirst()) {
                   // read event data
               }

            }*/

       /* // Run query
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
        String[] selectionArgs = new String[] {"hera@example.com", "com.example",
                "hera@example.com"};
// Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);


            Log.d("CALENDAR", "" + calID + displayName + accountName + ownerName);
            // Do something with the values...
            }}}
*/


public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat(
        "dd/MM/yyyy hh:mm:ss a");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
        }

}

