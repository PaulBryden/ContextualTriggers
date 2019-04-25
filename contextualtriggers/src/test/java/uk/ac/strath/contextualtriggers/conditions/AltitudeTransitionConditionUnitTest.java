package uk.ac.strath.contextualtriggers.conditions;

import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.AltitudeData;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AltitudeTransitionConditionUnitTest {

    @Test
    public void testConditionNotSatisfiedBeforeReceivingData() {
        MockDataManager<AltitudeData> manager = new MockDataManager<>();
        AltitudeTransitionCondition condition = new AltitudeTransitionCondition(19, manager);
        assertFalse(condition.isSatisfied());
        manager.sendUpdate(new AltitudeData(20.0));
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user's altitude increases.
     */
    @Test
    public void testConditionSatisfied() {
        MockDataManager<AltitudeData> manager = new MockDataManager<>();
        AltitudeTransitionCondition condition = new AltitudeTransitionCondition(19, manager);
        manager.sendUpdate(new AltitudeData(0.0));
        assertFalse(condition.isSatisfied());
        manager.sendUpdate(new AltitudeData(20.0));
        assertTrue(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user's altitude decreases.
     */
    @Test
    public void testConditionNotSatisfiedWithIncorrectSign() {
        MockDataManager<AltitudeData> manager = new MockDataManager<>();
        AltitudeTransitionCondition condition = new AltitudeTransitionCondition(19, manager);
        manager.sendUpdate(new AltitudeData(0.0));
        assertFalse(condition.isSatisfied());
        manager.sendUpdate(new AltitudeData(-20.0));
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user's altitude increases too slowly.
     */
    @Test
    public void testConditionNotSatisfiedByInsuffcientIncrease() {
        MockDataManager<AltitudeData> manager = new MockDataManager<>();
        AltitudeTransitionCondition condition = new AltitudeTransitionCondition(19, manager);
        manager.sendUpdate(new AltitudeData(0.0));
        assertFalse(condition.isSatisfied());
        manager.sendUpdate(new AltitudeData(10.0));
        assertFalse(condition.isSatisfied());
    }

}
