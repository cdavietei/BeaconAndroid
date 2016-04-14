package project.topka.beacon11.location;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.GoogleApiClient.*;

import project.topka.beacon11.LocationTest;
import project.topka.beacon11.MapsActivity;
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

		mGoogleApiClient.connect();
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		Log.v(LOG_TAG,"Started Update Service");

		String user = intent.getStringExtra("user");

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

		Log.v(LOG_TAG,"Server response: "+resp);

		putLocation();

		if(resp.equals("true"))
		{
			scheduleNotification();
		}

		//TODO Handle failed update
	}

	protected void putLocation()
	{
		Context context = getApplicationContext();
		SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_data_key), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();

		editor.putFloat("lat",(float)lat);
		editor.putFloat("longit",(float)longit);
		editor.commit();

		Log.v(LOG_TAG, "Updated user location in SharedPrefs: "+lat+", "+longit);
	}

	protected void scheduleNotification()
	{
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.drawable.notification_icon)
						.setContentTitle("Beacons Near By")
						.setContentText("Tap to see Beacons near you");

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MapsActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MapsActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
				stackBuilder.getPendingIntent(
						0,
						PendingIntent.FLAG_UPDATE_CURRENT
				);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		int mId = 2721;
		mNotificationManager.notify(mId, mBuilder.build());
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
