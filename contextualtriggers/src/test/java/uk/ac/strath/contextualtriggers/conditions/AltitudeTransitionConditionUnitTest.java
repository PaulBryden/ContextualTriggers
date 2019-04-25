package uk.ac.strath.contextualtriggers.conditions;

import org.junit.Before;
import org.junit.Test;

import uk.ac.strath.contextualtriggers.data.AltitudeData;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AltitudeTransitionConditionUnitTest {

    private MockDataManager<AltitudeData> manager;
    private AltitudeTransitionCondition condition;

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        condition = new AltitudeTransitionCondition(19, manager);
    }


    @Test
    public void testNoDataReceivedYet() {
        assertFalse(condition.isSatisfied());
        manager.sendUpdate(new AltitudeData(20.0));
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user's altitude increases.
     */
    @Test
    public void testConditionSatisfied() {
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
        manager.sendUpdate(new AltitudeData(0.0));
        assertFalse(condition.isSatisfied());
        manager.sendUpdate(new AltitudeData(10.0));
        assertFalse(condition.isSatisfied());
    }

}
