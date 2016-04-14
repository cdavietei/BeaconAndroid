package project.topka.beacon11.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import project.topka.beacon11.R;

/**
 * Created by Chris on 11/4/16.
 */
public class UpdateLocationReceiver extends BroadcastReceiver
{
	public static final int REQUEST_CODE = 21345;
	public static final String ACTION = "project.topka.beacon11.location.UpdateLocationService";

	@Override
	public void onReceive(Context context, Intent intent)
	{
		SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_data_key), Context.MODE_PRIVATE);

		Intent i = new Intent(context, UpdateLocationService.class);
		i.putExtra("user",sharedPreferences.getString("user_id","null"));

		context.startService(i);
	}
}
