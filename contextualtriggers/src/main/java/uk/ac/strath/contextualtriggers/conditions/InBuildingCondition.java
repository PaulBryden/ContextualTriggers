package uk.ac.strath.contextualtriggers.conditions;

import com.google.android.libraries.places.api.model.Place;

import java.util.Arrays;
import java.util.List;

import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.managers.IDataManager;

/**
 * A condition satisfied if any place with a likelihood greater than a threshold value matches
 * a place type considered a building.
 */
public class InBuildingCondition extends InPlaceTypeCondition {

    private static final List<Place.Type> BUILDING_PLACE_TYPES = Arrays.asList(Place.Type.ACCOUNTING,
            Place.Type.AQUARIUM, Place.Type.ART_GALLERY, Place.Type.BAKERY, Place.Type.BANK,
            Place.Type.BAR, Place.Type.BEAUTY_SALON, Place.Type.BICYCLE_STORE, Place.Type.BOOK_STORE,
            Place.Type.BOWLING_ALLEY, Place.Type.CAFE, Place.Type.CAR_DEALER, Place.Type.CAR_RENTAL,
            Place.Type.CAR_REPAIR, Place.Type.CASINO, Place.Type.CHURCH, Place.Type.CITY_HALL,
            Place.Type.CLOTHING_STORE, Place.Type.CONVENIENCE_STORE, Place.Type.COURTHOUSE,
            Place.Type.DENTIST, Place.Type.DEPARTMENT_STORE, Place.Type.DOCTOR, Place.Type.ELECTRICIAN,
            Place.Type.ELECTRONICS_STORE, Place.Type.EMBASSY, Place.Type.FIRE_STATION,
            Place.Type.FLORIST, Place.Type.FUNERAL_HOME, Place.Type.FURNITURE_STORE, Place.Type.GYM,
            Place.Type.HAIR_CARE, Place.Type.HARDWARE_STORE, Place.Type.HINDU_TEMPLE,
            Place.Type.HOME_GOODS_STORE, Place.Type.HOSPITAL, Place.Type.INSURANCE_AGENCY,
            Place.Type.JEWELRY_STORE, Place.Type.LAUNDRY, Place.Type.LAWYER, Place.Type.LIBRARY,
            Place.Type.LIQUOR_STORE, Place.Type.LOCAL_GOVERNMENT_OFFICE, Place.Type.LOCKSMITH,
            Place.Type.LODGING, Place.Type.MEAL_DELIVERY, Place.Type.MEAL_TAKEAWAY, Place.Type.MOSQUE,
            Place.Type.MOVIE_RENTAL, Place.Type.MOVIE_THEATER, Place.Type.MOVING_COMPANY,
            Place.Type.MUSEUM, Place.Type.NIGHT_CLUB, Place.Type.PAINTER, Place.Type.PET_STORE,
            Place.Type.PHARMACY, Place.Type.PHYSIOTHERAPIST, Place.Type.PLUMBER, Place.Type.POLICE,
            Place.Type.POST_OFFICE, Place.Type.REAL_ESTATE_AGENCY, Place.Type.RESTAURANT,
            Place.Type.ROOFING_CONTRACTOR, Place.Type.SCHOOL, Place.Type.SHOE_STORE,
            Place.Type.SHOPPING_MALL, Place.Type.SPA, Place.Type.STADIUM, Place.Type.STORAGE,
            Place.Type.STORE, Place.Type.SUPERMARKET, Place.Type.SYNAGOGUE, Place.Type.TRAVEL_AGENCY,
            Place.Type.VETERINARY_CARE);

    public InBuildingCondition(IDataManager<PlacesData> dataManager) {
        this(DEFAULT_THRESHOLD, dataManager);
    }

    public InBuildingCondition(double threshold, IDataManager<PlacesData> dataManager) {
        super(BUILDING_PLACE_TYPES, threshold, dataManager);
    }

}
