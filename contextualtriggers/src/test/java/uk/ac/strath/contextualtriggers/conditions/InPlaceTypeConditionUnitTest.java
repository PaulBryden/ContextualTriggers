package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.PlaceLikelihood;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import uk.ac.strath.contextualtriggers.data.PlacesData;

import static com.google.android.libraries.places.api.model.Place.Type.CHURCH;
import static com.google.android.libraries.places.api.model.Place.Type.GYM;
import static com.google.android.libraries.places.api.model.Place.Type.PARK;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InPlaceTypeConditionUnitTest {

    private MockDataManager<PlacesData> manager;
    private InPlaceTypeCondition gymCondition;

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        gymCondition = new InPlaceTypeCondition(GYM, manager);
    }

    @Test
    public void testNoDataReceived() {
        assertFalse(gymCondition.isSatisfied());
    }

    @Test
    public void testConditionSatisfied() {
        PlaceLikelihood pl = new MockPlaceLikelihood(new MockPlace(GYM), 0.75);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        assertTrue(gymCondition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedWithWrongPlaceType() {
        PlaceLikelihood pl = new MockPlaceLikelihood(new MockPlace(PARK), 0.75);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        assertFalse(gymCondition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedWithWrongBuildingType() {
        PlaceLikelihood pl = new MockPlaceLikelihood(new MockPlace(CHURCH), 0.75);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        assertFalse(gymCondition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedWithLowLikelihood() {
        PlaceLikelihood pl = new MockPlaceLikelihood(new MockPlace(GYM), 0.01);
        manager.sendUpdate(new PlacesData(Collections.singletonList(pl)));
        assertFalse(gymCondition.isSatisfied());
    }

}
