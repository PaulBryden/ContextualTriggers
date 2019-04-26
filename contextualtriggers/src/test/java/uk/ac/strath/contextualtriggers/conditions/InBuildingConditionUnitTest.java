package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.PlaceLikelihood;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.managers.DataManager;

import static com.google.android.libraries.places.api.model.Place.Type.GYM;
import static com.google.android.libraries.places.api.model.Place.Type.PARK;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InBuildingConditionUnitTest {

    private MockDataManager<PlacesData> manager;
    private InBuildingCondition condition;

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        condition = new InBuildingCondition(manager);
    }

    @Test
    public void testNoDataReceived() {
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user is in a gym.
     */
    @Test
    public void testConditionSatisfied() {
        PlaceLikelihood pl = new MockPlaceLikelihood(new MockPlace(GYM), 0.75);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        assertTrue(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user is in a park.
     */
    @Test
    public void testConditionNotSatisfiedWithWrongPlaceType() {
        PlaceLikelihood pl = new MockPlaceLikelihood(new MockPlace(PARK), 0.75);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedWithLowLikelihood() {
        PlaceLikelihood pl = new MockPlaceLikelihood(new MockPlace(GYM), 0.01);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        assertFalse(condition.isSatisfied());
    }

}
