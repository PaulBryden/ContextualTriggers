package uk.ac.strath.contextualtriggers.services;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import uk.ac.strath.contextualtriggers.ContextualTriggersService;

public class AbstractServiceConnection implements ServiceConnection {

    private ContextualTriggersService mainService;

    private IBinder dataManager;
    private boolean connected;

    public AbstractServiceConnection(ContextualTriggersService mainService) {
        this.mainService = mainService;
        connected = false;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d("AbstractService", "Are we here?");
        dataManager = service;
        connected = true;
        mainService.notifyDataManagerOnline();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        connected = false;
    }

    public boolean isConnected() {
        return connected;
    }

    public IBinder getDataManager(){
        return dataManager;
    }
}
