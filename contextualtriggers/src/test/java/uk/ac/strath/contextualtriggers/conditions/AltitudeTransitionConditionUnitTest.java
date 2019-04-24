package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.AltitudeData;
import uk.ac.strath.contextualtriggers.managers.DataManager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AltitudeTransitionConditionUnitTest {

    private class MockAltitudeDataManager extends DataManager<AltitudeData> {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        public void sendMockUpdate(double altitude) {
            AltitudeData data = new AltitudeData(altitude);
            sendUpdate(data);
        }
    }

    /**
     * Tests what happens when the user's altitude increases.
     */
    @Test
    public void AltitudeTransitionConditionUnitTest() {
        MockAltitudeDataManager manager = new MockAltitudeDataManager();
        AltitudeTransitionCondition condition = new AltitudeTransitionCondition(19, manager);
        manager.sendMockUpdate(0.0);
        assertFalse(condition.isSatisfied());
        manager.sendMockUpdate(20.0);
        assertTrue(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user's altitude decreases.
     */
    @Test
    public void AltitudeTransitionConditionUnitTest2() {
        MockAltitudeDataManager manager = new MockAltitudeDataManager();
        AltitudeTransitionCondition condition = new AltitudeTransitionCondition(19, manager);
        manager.sendMockUpdate(0.0);
        assertFalse(condition.isSatisfied());
        manager.sendMockUpdate(-20.0);
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user's altitude increases too slowly.
     */
    @Test
    public void AltitudeTransitionConditionUnitTest3() {
        MockAltitudeDataManager manager = new MockAltitudeDataManager();
        AltitudeTransitionCondition condition = new AltitudeTransitionCondition(19, manager);
        manager.sendMockUpdate(0.0);
        assertFalse(condition.isSatisfied());
        System.out.println("AcceptableTimeConditionUnitTest3");
        manager.sendMockUpdate(10.0);
        assertFalse(condition.isSatisfied());
    }

}
