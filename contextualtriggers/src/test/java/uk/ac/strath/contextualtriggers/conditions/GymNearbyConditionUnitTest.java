package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GymNearbyConditionUnitTest {
    
    /**
     * Tests what happens when there is a gym nearby.
     */
    @Test
    public void testConditionSatisfiedByLikelyGymProximity() {
        MockPlacesDataManager manager = new MockPlacesDataManager();
        GymNearbyCondition condition = new GymNearbyCondition(manager);
        Place gym = manager.getMockPlace(Place.Type.GYM);
        PlaceLikelihood likelihood = manager.getMockLikelihood(gym, 0.25);
        manager.sendMockUpdate(likelihood);
        assertTrue(condition.isSatisfied());
    }

    /**
     * Tests what happens when there is not a gym nearby, just a cafe.
     */
    @Test
    public void testConditionNotSatisfiedWithoutGymProximity() {
        MockPlacesDataManager manager = new MockPlacesDataManager();
        GymNearbyCondition condition = new GymNearbyCondition(manager);
        Place cafe = manager.getMockPlace(Place.Type.CAFE);
        PlaceLikelihood likelihood = manager.getMockLikelihood(cafe, 0.25);
        manager.sendMockUpdate(likelihood);
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when there is not a gym nearby, just multiple other locations.
     */
    @Test
    public void testConditionSatisfiedWithGymProximityMultiple() {
        MockPlacesDataManager manager = new MockPlacesDataManager();
        GymNearbyCondition condition = new GymNearbyCondition(manager);
        Place cafe = manager.getMockPlace(Place.Type.CAFE);
        PlaceLikelihood likelihood_cafe = manager.getMockLikelihood(cafe, 0.25);
        Place gym = manager.getMockPlace(Place.Type.GYM);
        PlaceLikelihood likelihood_gym = manager.getMockLikelihood(gym, 0.25);
        Place gym2 = manager.getMockPlace(Place.Type.GYM);
        PlaceLikelihood likelihood_gym2 = manager.getMockLikelihood(gym2, 0.1);
        Place park = manager.getMockPlace(Place.Type.PARK);
        PlaceLikelihood likelihood_park = manager.getMockLikelihood(park, 0.20);
        manager.sendMockUpdate(likelihood_cafe, likelihood_gym, likelihood_gym2, likelihood_park);
        assertTrue(condition.isSatisfied());
    }

    /**
     * Tests what happens when there is not a gym nearby, just multiple other locations.
     */
    @Test
    public void testConditionNotSatisfiedWithoutGymProximityMultiple() {
        MockPlacesDataManager manager = new MockPlacesDataManager();
        GymNearbyCondition condition = new GymNearbyCondition(manager);
        Place cafe = manager.getMockPlace(Place.Type.CAFE);
        PlaceLikelihood likelihood_cafe = manager.getMockLikelihood(cafe, 0.25);
        Place park = manager.getMockPlace(Place.Type.PARK);
        PlaceLikelihood likelihood_park = manager.getMockLikelihood(park, 0.20);
        Place airport = manager.getMockPlace(Place.Type.AIRPORT);
        PlaceLikelihood likelihood_airport = manager.getMockLikelihood(airport, 0.22);
        manager.sendMockUpdate(likelihood_cafe, likelihood_park, likelihood_airport);
        assertFalse(condition.isSatisfied());
    }

    /**
     * Tests what happens when the user is already in a gym.
     */
    @Test
    public void testConditionNotSatisfiedByTooHighLikelihood() {
        MockPlacesDataManager manager = new MockPlacesDataManager();
        GymNearbyCondition condition = new GymNearbyCondition(manager);
        Place gym = manager.getMockPlace(Place.Type.GYM);
        PlaceLikelihood likelihood = manager.getMockLikelihood(gym, 0.75);
        manager.sendMockUpdate(likelihood);
        assertFalse(condition.isSatisfied());
    }

}
