package project.topka.beacon11;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import project.topka.beacon11.connections.ApiConnection;

public class CreateBeacon extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
	protected GoogleApiClient mGoogleApiClient = null;
	protected static final String LOG_TAG = CreateBeacon.class.getSimpleName();
	protected double lat = 0.00, longit = 0.00;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_beacon);

		if(mGoogleApiClient == null)
		{
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();
		}

    }

	protected void onStart()
	{
		mGoogleApiClient.connect();
		super.onStart();
	}

	protected void onStop()
	{
		mGoogleApiClient.disconnect();
		super.onStop();
	}

    public void sendBeacon(View view)
    {
        //TODO:  post Beacon info to server
        //Fields
        EditText mTitleField = (EditText) findViewById(R.id.beacon_name);
        EditText mDescriptionField = (EditText) findViewById(R.id.beacon_description);
//        EditText mLocationField = (EditText) findViewById(R.id.beacon_location);
        EditText mTimeStartField = (EditText) findViewById(R.id.beacon_time_start);
        EditText mTimeEndField = (EditText) findViewById(R.id.beacon_time_end);
        EditText mInterestsField = (EditText) findViewById(R.id.beacon_interests);
        //EditText mLongitudeField = (EditText) findViewById(R.id.beacon_long_coord);
        //EditText mLatitudeField = (EditText) findViewById(R.id.beacon_lat_coord);


        String title = mTitleField.getText().toString();
        String desc = mDescriptionField.getText().toString();
        double latcoord =  -73.52342;//Double.parseDouble(mLatitudeField.getText().toString());
        double longcoord = 43.5324; //Double.parseDouble(mLongitudeField.getText().toString());
        ArrayList<String> interests = new ArrayList<String>(); //use loop to fill
        double duration;
        double range;
//        String location;

		Context context = getApplicationContext();

		SharedPreferences sharedPrefernces = context.getSharedPreferences(context.getString(R.string.user_data_key), Context.MODE_PRIVATE);

		String user = sharedPrefernces.getString("user_id","null");

		new CreateBeaconTask().execute(user,String.valueOf(lat),String.valueOf(longit),title);


        Intent intent = new Intent(this, MapsActivity.class);

        intent.putExtra("lat", latcoord);
        intent.putExtra("longit", longcoord);
        intent.putExtra("title", title);

        startActivity(intent);
        finish();
    }

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		Location mLastLocation = null;

		try
		{
			mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			lat = mLastLocation.getLatitude();
			longit = mLastLocation.getLongitude();
		}
		catch (SecurityException e)
		{

		}

	}

	@Override
	public void onConnectionSuspended(int i)
	{

	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{

	}

	public class CreateBeaconTask extends AsyncTask<String, Void, String>
    {
		private final String LOG_TAG = CreateBeaconTask.class.getSimpleName();

        @Override
        protected String doInBackground(String... params)
        {
            if(params.length <= 2)
                return null;

			final String USER = "user";
			final String LAT = "lat";
			final String LON = "lon";
			final String TITLE = "title";
			final String START = "start";
			final String END = "end";
			final String RANGE = "range";
			final String PLACE = "place";

			Resources resources = getResources();

			ApiConnection connection = new ApiConnection(resources.getString(R.string.api_server));

			connection = connection.buildUpon().appendPath(resources.getString(R.string.beacon_path))
						.appendPath(resources.getString(R.string.create_beacon))
						.appendQuery(USER,params[0])
						.appendQuery(LAT,params[1])
						.appendQuery(LON, params[2]);

			String[] optional = {TITLE, START, END, RANGE, PLACE};

			for(int i=3; i<params.length; i++)
				connection.appendQuery(optional[i-3],params[i]);

			connection.build();

			Log.v(LOG_TAG, "Creating Beacon");
			String resp = connection.connect("GET");

			Log.v(LOG_TAG, "Beacon Response: "+resp);

			return resp;
        }
    }
}