package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.PlaceLikelihood;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import uk.ac.strath.contextualtriggers.data.PlacesData;

import static com.google.android.libraries.places.api.model.Place.Type.AIRPORT;
import static com.google.android.libraries.places.api.model.Place.Type.CAFE;
import static com.google.android.libraries.places.api.model.Place.Type.GYM;
import static com.google.android.libraries.places.api.model.Place.Type.PARK;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GymNearbyConditionUnitTest {

    private MockDataManager<PlacesData> manager;
    private GymNearbyCondition condition;

    @Before
    public void setup() {
        manager = new MockDataManager<>();
        condition = new GymNearbyCondition(manager);
    }

    @Test
    public void testConditionNotSatisfiedWhenNoDataReceived() {
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionSatisfiedByLikelyGymProximity() {
        PlaceLikelihood likelihood = new MockPlaceLikelihood(new MockPlace(GYM), 0.25);
        manager.sendUpdate(new PlacesData(Collections.singletonList(likelihood)));
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedWithoutGymProximity() {
        PlaceLikelihood likelihood = new MockPlaceLikelihood(new MockPlace(CAFE), 0.25);
        manager.sendUpdate(new PlacesData(Collections.singletonList(likelihood)));
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionSatisfiedWithGymProximityMultiple() {
        PlaceLikelihood likelihood_cafe = new MockPlaceLikelihood(new MockPlace(CAFE), 0.25);
        PlaceLikelihood likelihood_gym = new MockPlaceLikelihood(new MockPlace(GYM), 0.25);
        PlaceLikelihood likelihood_gym2 = new MockPlaceLikelihood(new MockPlace(GYM), 0.1);
        PlaceLikelihood likelihood_park = new MockPlaceLikelihood(new MockPlace(GYM), 0.20);
        manager.sendUpdate(new PlacesData(Arrays.asList(likelihood_cafe, likelihood_gym, likelihood_gym2, likelihood_park)));
        assertTrue(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedWithoutGymProximityMultiple() {
        PlaceLikelihood likelihood_cafe = new MockPlaceLikelihood(new MockPlace(CAFE), 0.25);
        PlaceLikelihood likelihood_park = new MockPlaceLikelihood(new MockPlace(PARK), 0.2);
        PlaceLikelihood likelihood_airport = new MockPlaceLikelihood(new MockPlace(AIRPORT), 0.22);
        manager.sendUpdate(new PlacesData(Arrays.asList(likelihood_cafe, likelihood_park, likelihood_airport)));
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedByTooHighLikelihood() {
        PlaceLikelihood likelihood = new MockPlaceLikelihood(new MockPlace(GYM), 0.75);
        manager.sendUpdate(new PlacesData(Collections.singletonList(likelihood)));
        assertFalse(condition.isSatisfied());
    }

    @Test
    public void testConditionNotSatisfiedByTooHighLikelihoodWhenAlsoNearGym() {
        PlaceLikelihood likelihood_gym = new MockPlaceLikelihood(new MockPlace(GYM), 0.1);
        PlaceLikelihood likelihood_gym2 = new MockPlaceLikelihood(new MockPlace(GYM), 0.75);
        manager.sendUpdate(new PlacesData(Arrays.asList(likelihood_gym, likelihood_gym2)));
        assertFalse(condition.isSatisfied());
    }

}
