package project.topka.beacon11.location;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.*;

import project.topka.beacon11.R;
import project.topka.beacon11.connections.ApiConnection;


/**
 * Created by Chris on 11/4/16.
 */
public class UpdateLocationService extends IntentService implements ConnectionCallbacks, OnConnectionFailedListener
{
	private GoogleApiClient mGoogleApiClient = null;


	private final String LOG_TAG = UpdateLocationService.class.getSimpleName();
	protected double lat = 0.000, longit = 0.000;

	public UpdateLocationService()
	{
		super("UpdateLocationService");
	}

	public void onCreate()
	{
		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();
		}
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		String user = intent.getStringExtra("user");

		mGoogleApiClient.connect();

		final String USER = "user";
		final String LAT = "lat";
		final String LON = "lon";

		Resources resources = getResources();

		ApiConnection connection = new ApiConnection(resources.getString(R.string.api_server));

		connection = connection.buildUpon()
					.appendPath(resources.getString(R.string.update_location))
					.appendQuery(USER,user)
					.appendQuery(LAT,String.valueOf(lat))
					.appendQuery(LON,String.valueOf(longit)).build();

		Log.v(LOG_TAG, "Sending Update Request");

		String resp = connection.connect("GET");
		//TODO Handle failed update
	}

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		Location mLastLocation = null;

		try
		{
			mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
			if (mLastLocation != null)
			{
				lat = mLastLocation.getLatitude();
				longit = mLastLocation.getLongitude();
			}
		}
		catch (SecurityException e)
		{
			Log.e(LOG_TAG, "Not Sufficient Permission" + e.getLocalizedMessage());
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

	public void onDestroy()
	{
		mGoogleApiClient.disconnect();
		super.onDestroy();

	}
}
