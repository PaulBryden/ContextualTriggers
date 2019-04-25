package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.PlaceLikelihood;

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

    @Test
    public void testConditionNotSatisfiedWhenNoDataReceived() {
        MockDataManager<PlacesData> manager = new MockDataManager<>();
        GymNearbyCondition condition = new GymNearbyCondition(manager);
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when there is a gym nearby.
     */
    @Test
    public void testConditionSatisfiedByLikelyGymProximity() {
        MockDataManager<PlacesData> manager = new MockDataManager<>();
        GymNearbyCondition condition = new GymNearbyCondition(manager);
        PlaceLikelihood likelihood = new MockPlaceLikelihood(new MockPlace(GYM), 0.25);
        manager.sendUpdate(new PlacesData(Collections.singletonList(likelihood)));
        assertTrue(condition.isSatisfied());
    }

    /**
     * Tests what happens when there is not a gym nearby, just a cafe.
     */
    @Test
    public void testConditionNotSatisfiedWithoutGymProximity() {
        MockDataManager<PlacesData> manager = new MockDataManager<>();
        GymNearbyCondition condition = new GymNearbyCondition(manager);
        PlaceLikelihood likelihood = new MockPlaceLikelihood(new MockPlace(CAFE), 0.25);
        manager.sendUpdate(new PlacesData(Collections.singletonList(likelihood)));
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when there is a gym nearby, amongst multiple other locations.
     */
    @Test
    public void testConditionSatisfiedWithGymProximityMultiple() {
        MockDataManager<PlacesData> manager = new MockDataManager<>();
        GymNearbyCondition condition = new GymNearbyCondition(manager);
        PlaceLikelihood likelihood_cafe = new MockPlaceLikelihood(new MockPlace(CAFE), 0.25);
        PlaceLikelihood likelihood_gym = new MockPlaceLikelihood(new MockPlace(GYM), 0.25);
        PlaceLikelihood likelihood_gym2 = new MockPlaceLikelihood(new MockPlace(GYM), 0.1);
        PlaceLikelihood likelihood_park = new MockPlaceLikelihood(new MockPlace(GYM), 0.20);
        manager.sendUpdate(new PlacesData(Arrays.asList(likelihood_cafe, likelihood_gym, likelihood_gym2, likelihood_park)));
        assertTrue(condition.isSatisfied());
    }

    /**
     * Tests what happens when there is not a gym nearby, just multiple other locations.
     */
    @Test
    public void testConditionNotSatisfiedWithoutGymProximityMultiple() {
        MockDataManager<PlacesData> manager = new MockDataManager<>();
        GymNearbyCondition condition = new GymNearbyCondition(manager);
        PlaceLikelihood likelihood_cafe = new MockPlaceLikelihood(new MockPlace(CAFE), 0.25);
        PlaceLikelihood likelihood_park = new MockPlaceLikelihood(new MockPlace(PARK), 0.2);
        PlaceLikelihood likelihood_airport = new MockPlaceLikelihood(new MockPlace(AIRPORT), 0.22);
        manager.sendUpdate(new PlacesData(Arrays.asList(likelihood_cafe, likelihood_park, likelihood_airport)));
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user is already in a gym.
     */
    @Test
    public void testConditionNotSatisfiedByTooHighLikelihood() {
        MockDataManager<PlacesData> manager = new MockDataManager<>();
        GymNearbyCondition condition = new GymNearbyCondition(manager);
        PlaceLikelihood likelihood = new MockPlaceLikelihood(new MockPlace(GYM), 0.75);
        manager.sendUpdate(new PlacesData(Collections.singletonList(likelihood)));
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user is already in a gym, and there is a gym nearby.
     */
    @Test
    public void testConditionNotSatisfiedByTooHighLikelihoodWhenAlsoNearGym() {
        MockDataManager<PlacesData> manager = new MockDataManager<>();
        GymNearbyCondition condition = new GymNearbyCondition(manager);
        PlaceLikelihood likelihood_gym = new MockPlaceLikelihood(new MockPlace(GYM), 0.1);
        PlaceLikelihood likelihood_gym2 = new MockPlaceLikelihood(new MockPlace(GYM), 0.75);
        manager.sendUpdate(new PlacesData(Arrays.asList(likelihood_gym, likelihood_gym2)));
        assertFalse(condition.isSatisfied());
    }

}
