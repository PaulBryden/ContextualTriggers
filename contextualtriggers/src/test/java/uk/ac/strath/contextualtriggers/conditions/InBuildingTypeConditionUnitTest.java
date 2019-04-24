package uk.ac.strath.contextualtriggers.conditions;

import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.Parcel;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.PlusCode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import uk.ac.strath.contextualtriggers.actions.UnitTestAction;
import uk.ac.strath.contextualtriggers.data.PlacesData;
import uk.ac.strath.contextualtriggers.managers.DataManager;
import uk.ac.strath.contextualtriggers.managers.IDataManager;
import uk.ac.strath.contextualtriggers.triggers.Trigger;

import static org.junit.Assert.assertEquals;

public class InBuildingTypeConditionUnitTest {

    @Test
    public void InBuildingTypeConditionUnitTest() {
        class PlacesMockDataManager extends DataManager<PlacesData> implements IDataManager<PlacesData> {
            boolean firstTime = true;

            @Nullable
            @Override
            public IBinder onBind(Intent intent) {
                return null;
            }

            public void mock() {
                List<PlaceLikelihood> data = new ArrayList<PlaceLikelihood>();
                Place gym = new Place() {
                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel parcel, int i) {

                    }

                    @Override
                    public String getId() {
                        return null;
                    }


                    @Nullable
                    @Override
                    public String getAddress() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public AddressComponents getAddressComponents() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<String> getAttributions() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public OpeningHours getOpeningHours() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getPhoneNumber() {
                        return null;
                    }

                    @Override
                    public LatLng getLatLng() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public String getName() {
                        return null;
                    }

                    @Override
                    public LatLngBounds getViewport() {
                        return null;
                    }

                    @Override
                    public Uri getWebsiteUri() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<PhotoMetadata> getPhotoMetadatas() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public PlusCode getPlusCode() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Integer getPriceLevel() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public Double getRating() {
                        return null;
                    }

                    @Nullable
                    @Override
                    public List<Type> getTypes() {
                        ArrayList<Type> types = new ArrayList<Type>();
                        types.add(Type.CAFE);
                        return types;
                    }

                    @Nullable
                    @Override
                    public Integer getUserRatingsTotal() {
                        return null;
                    }
                };
                PlaceLikelihood likelihood = new PlaceLikelihood() {

                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel parcel, int i) {

                    }

                    @Override
                    public Place getPlace() {
                        return gym;
                    }

                    @Override
                    public double getLikelihood() {
                        return 0.76;
                    }
                };
                firstTime = false;
                data.add(likelihood);
                sendUpdate(new PlacesData(data));
            }
        }
        UnitTestAction action = new UnitTestAction();
        PlacesMockDataManager manager = new PlacesMockDataManager();
        InBuildingTypeCondition altTransCondition = new InBuildingTypeCondition(Place.Type.CAFE, manager);
        Trigger.Builder T = new Trigger.Builder();
        T.setCondition(altTransCondition);
        T.setAction(action);
        Trigger trig = T.build();
        manager.mock();
        assertEquals(true, altTransCondition.isSatisfied());
        System.out.println("InBuildingConditionUnitTest");
    }

}
