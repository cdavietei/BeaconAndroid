package project.topka.beacon11;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.*;

public class LocationTest extends Activity implements ConnectionCallbacks, OnConnectionFailedListener
{
	protected GoogleApiClient mGoogleApiClient = null;

	protected TextView locOutput;
	protected static final String LOG_TAG = LocationTest.class.getSimpleName();


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_test);

		if(mGoogleApiClient == null)
		{
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();
		}

		locOutput = (TextView) findViewById(R.id.loc_test_text);

	}

	protected void onStart()
	{
		Log.v(LOG_TAG, "Starting connection");
		mGoogleApiClient.connect();
		Log.v(LOG_TAG,"Connection established");
		super.onStart();
	}

	protected void onStop()
	{
		mGoogleApiClient.disconnect();
		super.onStop();
	}

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		Location mLastLocation = null;

		Log.v(LOG_TAG,"In onConnected Method");

		try
		{
			mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			if (mLastLocation != null)
			{

				if(locOutput != null)
					locOutput.setText(String.format("%.5f, %.5f",
													mLastLocation.getLatitude(),
													mLastLocation.getLongitude()));
			}
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
}
