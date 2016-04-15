package project.topka.beacon11;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
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

import java.util.ArrayList;

import project.topka.beacon11.beacons.Beacon;
import project.topka.beacon11.connections.ApiConnection;


public class MapsActivity extends FragmentActivity implements LocationProvider.LocationCallback {

    private GoogleMap mMap;
    public static final String TAG = MapsActivity.class.getSimpleName();
    private LocationProvider mLocationProvider;
    public double currentLongitude = 0.0;
    public double currentLatitude = 0.0;

    //TODO: Get Beacons from server and add them to this array (use getBeacon function)
    public ArrayList<Beacon> currentBeacons = new ArrayList<Beacon>();

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

        getBeacons(); // make sure this is first
        setUpMapIfNeeded();

        mLocationProvider = new LocationProvider(this, this);

        // GESTURE DETECTION //
        gestureDetector = new GestureDetector(this, new SwipeGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };
    } // end onCreate

    // BEACON OBJECT //
    public class Beacon {
        String creator;
        String title;
        double latitude;
        double longitude;

        public Beacon(String c, String t, double lat, double longit) {
            this.creator = c;
            this.title = t;
            this.latitude = lat;
            this.longitude = longit;
        }

    }

    // GET JSON STRING, PARSE INTO BEACON OBJECT, ADD TO currentBeacons
    public void getBeacons() {

    }

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


    // ON MAP LOAD //
    private void setUpMap() {
        //Moves camera
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hofstra,15));

        //TODO: get current location (replace myLocation's values)
        LatLng myLocation = new LatLng(40.713819, -73.599657);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,15));

        Beacon curr;

        // Show nearby Beacons, getBeacons must first be called
        if (currentBeacons.size() != 0) {
            for(int i=0;i<currentBeacons.size();i++) {
                curr = currentBeacons.get(i);
                LatLng coord = new LatLng(curr.latitude,curr.longitude);
                mMap.addMarker(new MarkerOptions().position(coord).title(curr.title));
            }
        }

    }

    // BUTTON CONTROLS //

    public void BTN_CREATE_BEACON(View view){
        Intent intent = new Intent(this, CreateBeacon.class);
        startActivity(intent);
    }

    public void testLocation(View view) {
        Log.v("long", String.valueOf(currentLongitude));
        Log.v("lat", String.valueOf(currentLatitude));
    }

    public class NearbyBeacon extends AsyncTask<String, Void, ArrayList<Beacon>>
    {
		private final String LOG_TAG = NearbyBeacon.class.getSimpleName();

		@Override
		protected ArrayList<Beacon> doInBackground(String... params)
		{
			if(params.length <= 1)
				return null;

			final String LAT = "lat";
			final String LON = "lon";
			final String DIST = "dist";

			Resources resources = getResources();

			ApiConnection connection = new ApiConnection(resources.getString(R.string.api_server));

			connection = connection.buildUpon().appendPath(resources.getString(R.string.create_beacon))
						.appendQuery(LAT,params[0])
						.appendQuery(LON,params[1]);

			if(params.length == 3)
				connection.appendQuery(DIST,params[2]);

			connection = connection.build();

			String resp = connection.connect("GET");
			Log.v(LOG_TAG, "Requested nearby Beacons");

			return null;
		}

		protected ArrayList<Beacon> beaconsFromJson(String json)
		{
			return null;
		}
	}

}
