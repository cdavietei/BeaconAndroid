package project.topka.beacon11;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;



public class MapsActivity extends FragmentActivity implements LocationProvider.LocationCallback {

    private GoogleMap mMap;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private LocationProvider mLocationProvider;
    public double currentLongitude = 0.0;
    public double currentLatitude = 0.0;

    //SWIPE
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_DISTANCE = 200;
    private static final int SWIPE_VELOCITY_THRESHOLD = 200;
    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        mLocationProvider = new LocationProvider(this, this);

        // GESTURE DETECTION //
        gestureDetector = new GestureDetector(this, new SwipeGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            double testLat = extras.getDouble("lat");
            double testLong = extras.getDouble("longit");
            Log.e("lat", String.valueOf(testLat));
            Log.e("long", String.valueOf(testLong));

            String title = extras.getString("title");
            LatLng testCoord = new LatLng(testLat, testLong);
            mMap.addMarker(new MarkerOptions().position(testCoord).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(testCoord,15));
        }
    } // end onCreate


    // SWIPE DETECTION //

    private void onLeftSwipe() {
        Intent intent = new Intent(this, FriendList.class);
        startActivity(intent);
    }

    private void onRightSwipe(){
        Intent intent = new Intent(this, NearbyBeacons.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    public class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY){
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_DISTANCE) {
                    return false;
                }

                //right to left swipe
                if (e1.getX() - e2.getX()  > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    MapsActivity.this.onLeftSwipe();
                    Log.e("MapActivity","left swipe");
                }
                //left to right FIX
                else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    MapsActivity.this.onRightSwipe();
                    Log.e("MapActivity","right swipe");
                }
            } catch (Exception e) {
                Log.e("MapActivity", "Error with gesture");
            }
            return false;
        }

    }

    // LOCATION SERVICES //

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        mLocationProvider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }

    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        mMap.addMarker(new MarkerOptions().position(new LatLng(currentLatitude, currentLongitude)).title("Current Location"));
        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("I am here!");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private void setUpMap() {
//        LatLng hofstra = new LatLng(40.714111,-73.6027117);
//        mMap.addMarker(new MarkerOptions().position(hofstra).title("My Beacon"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hofstra,15));

    }

    // BUTTON CONTROLS //

    public void BTN_CREATE_BEACON(View view){
        Intent intent = new Intent(this, CreateBeacon.class);
        startActivity(intent);
    }

    public void testLocation(View view) {
        Log.e("long", String.valueOf(currentLongitude));
        Log.e("lat", String.valueOf(currentLatitude));
    }

}
