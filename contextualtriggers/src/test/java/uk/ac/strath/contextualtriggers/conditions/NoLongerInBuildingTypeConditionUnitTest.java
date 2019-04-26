package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.PlaceLikelihood;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import uk.ac.strath.contextualtriggers.data.PlacesData;

import static com.google.android.libraries.places.api.model.Place.Type.GYM;
import static com.google.android.libraries.places.api.model.Place.Type.PARK;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NoLongerInBuildingTypeConditionUnitTest {

    private MockDataManager<PlacesData> manager;
    private NoLongerInPlaceTypeCondition condition;

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        condition = new NoLongerInPlaceTypeCondition(GYM, manager);
    }

    @Test
    public void testNoDataReceived() {
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedWhenInBuilding() {
        PlaceLikelihood pl = new MockPlaceLikelihood(new MockPlace(GYM), 0.8);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionSatisfiedWhenLeftBuilding() {
        PlaceLikelihood pl = new MockPlaceLikelihood(new MockPlace(GYM), 0.8);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        pl = new MockPlaceLikelihood(new MockPlace(GYM), 0.01);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedWithWrongPlaceType() {
        PlaceLikelihood pl = new MockPlaceLikelihood(new MockPlace(PARK), 0.8);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        pl = new MockPlaceLikelihood(new MockPlace(PARK), 0.01);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedAfterTimeout() {
        PlaceLikelihood pl = new MockPlaceLikelihood(new MockPlace(GYM), 0.8);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl), System.currentTimeMillis() - 5 * 60 * 1000));
        pl = new MockPlaceLikelihood(new MockPlace(GYM), 0.01);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl), System.currentTimeMillis() - 4 * 60 * 1000));
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedWithLowLikelihood() {
        PlaceLikelihood pl = new MockPlaceLikelihood(new MockPlace(GYM), 0.15);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        pl = new MockPlaceLikelihood(new MockPlace(GYM), 0.01);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        assertFalse(condition.isSatisfied());
    }

}
