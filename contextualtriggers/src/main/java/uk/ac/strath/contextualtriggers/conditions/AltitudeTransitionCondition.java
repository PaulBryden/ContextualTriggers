package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.data.AltitudeData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition that checks whether the user's altitude is increasing.
 */
public class AltitudeTransitionCondition extends DataCondition<AltitudeData> {

    private double AltitudeTransition = 0;
    private AltitudeData oldAltitude;
    private int targetTransition;

    public AltitudeTransitionCondition(int transition, IDataManager<AltitudeData> dataManager) {
        super(dataManager, 30);
        oldAltitude = new AltitudeData();
        this.targetTransition = transition;
    }

    @Override
    public void notifyUpdate(AltitudeData data) {
        // Override since an update always means condition isn't satisfied,
        AltitudeTransition = data.altitude - oldAltitude.altitude;
        oldAltitude = data;
        super.notifyUpdate(data);
    }

    @Override
    public boolean isSatisfied() {
        return AltitudeTransition > targetTransition;
    }

}
