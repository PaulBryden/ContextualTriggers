package uk.ac.strath.keepfit.view;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import uk.ac.strath.keepfit.model.Goal;
import uk.ac.strath.keepfit.model.HistoryEntry;
import uk.ac.strath.keepfit.R;
import uk.ac.strath.keepfit.model.SharedPreferencesManager;


public class MainActivity extends AppCompatActivity implements SensorEventListener, GoalsFragment.OnFragmentInteractionListener, StatisticsFragment.OnFragmentInteractionListener, HistoryFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener {

    private Fragment currentFragment = null;
    private FragmentTransaction ft;
    private SensorManager sensorManager;
    private SharedPreferencesManager spm;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    switchToFragment(new HomeFragment());
                    return true;
                case R.id.navigation_statistics:
                    switchToFragment(new StatisticsFragment());
                    return true;
                case R.id.navigation_history:
                    switchToFragment(new HistoryFragment());
                    return true;
                case R.id.navigation_goals:
                    switchToFragment(new GoalsFragment());
                    return true;
            }
            return false;
        }
    };

    private void switchToFragment(Fragment fragment) {
        if (fragment != currentFragment) {
            currentFragment = fragment;
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content, currentFragment);
            ft.commit();
        }
    }

    private void showAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    private void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void tempCreateEntry(KeepFitViewModel vm, int year, int month, int day, String name, int target, int count) {
        vm.insert(new HistoryEntry(CalendarDay.from(year, month, day), new Goal(name, target), count));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //https://stackoverflow.com/questions/43104485/how-to-change-fragment-with-the-bottom-navigation-activity
        super.onCreate(savedInstanceState);
        spm = new SharedPreferencesManager(this);
        setContentView(R.layout.activity_main);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        int activeFragmentId = spm.getActiveFragmentId();
        switch (activeFragmentId) {
            case R.id.navigation_statistics:
                currentFragment = new StatisticsFragment();
                navigation.setSelectedItemId(activeFragmentId);
                break;
            case R.id.navigation_history:
                currentFragment = new HistoryFragment();
                navigation.setSelectedItemId(activeFragmentId);
                break;
            case R.id.navigation_goals:
                currentFragment = new GoalsFragment();
                navigation.setSelectedItemId(activeFragmentId);
                break;
            default:
                currentFragment = new HomeFragment();
        }
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, currentFragment);
        ft.commit();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_about:
                showAbout();
                return true;
            case R.id.navigation_settings:
                showSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (spm.isStepCounterEnabled()) {
            Sensor stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            if (stepSensor == null) {
                Toaster.makeMagicToast(this, getResources().getString(R.string.no_step_counter_warning), Toast.LENGTH_LONG);
                spm.setStepCounterEnabled(false);
            } else {
                sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        SharedPreferencesManager spm = new SharedPreferencesManager(this);
        int steps = (int) event.values[0];
        spm.saveAutoStepCount(steps);
        if (currentFragment instanceof HomeFragment) {
            ((HomeFragment) currentFragment).loadStepCount();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Don't need this
    }
}
