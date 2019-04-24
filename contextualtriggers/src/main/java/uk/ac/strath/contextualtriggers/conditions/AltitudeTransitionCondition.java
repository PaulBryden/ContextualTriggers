package uk.ac.strath.contextualtriggers.conditions;

import uk.ac.strath.contextualtriggers.data.AltitudeData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition that checks whether the user's altitude is increasing.
 */
public class AltitudeTransitionCondition extends DataCondition<AltitudeData> {

    private double altitudeTransition;
    private double targetTransition;

    public AltitudeTransitionCondition(int transition, IDataManager<AltitudeData> dataManager) {
        super(dataManager, 30);
        altitudeTransition = 0;
        this.targetTransition = transition;
    }

    @Override
    public void notifyUpdate(AltitudeData data) {
        // Override since an update always means condition isn't satisfied,
        if (getData() != null) {
            altitudeTransition = data.getAltitude() - getData().getAltitude();
        }
        super.notifyUpdate(data);
    }

    @Override
    public boolean isSatisfied() {
        return altitudeTransition > targetTransition;
    }

}
