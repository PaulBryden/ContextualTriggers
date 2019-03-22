package uk.ac.strath.keepfit.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import uk.co.daviddunphy.keepfit.R;
import uk.co.daviddunphy.keepfit.model.SharedPreferencesManager;
import uk.co.daviddunphy.keepfit.view.KeepFitViewModel;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        getFragmentManager().beginTransaction().replace(R.id.settings_content,
                new PrefsFragment()).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferencesManager spm = new SharedPreferencesManager(this);
        KeepFitViewModel vm = ViewModelProviders.of(this).get(KeepFitViewModel.class);
        if (spm.getDeleteHistory()) {
            vm.deleteAllEntries();
            spm.setDeleteHistory(false);
        } else {
            vm.deleteOldEntries();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);
        }
    }

}