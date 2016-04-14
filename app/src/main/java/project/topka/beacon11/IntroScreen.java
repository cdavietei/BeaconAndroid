package project.topka.beacon11;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import project.topka.beacon11.location.UpdateLocationReceiver;

public class IntroScreen extends Activity{

	protected static final String LOG_TAG = IntroScreen.class.getSimpleName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
    }

    protected void onStart()
    {
        scheduleUpdates();

		putUserID();

        super.onStart();
    }

	private void putUserID()
	{
		Context context = getApplicationContext();
		SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.user_data_key), Context.MODE_PRIVATE);

		String uid = sharedPreferences.getString("user_id",null);

		if(uid == null)
		{
			String ts = Context.TELEPHONY_SERVICE;
			TelephonyManager mTelephonyMgr = (TelephonyManager) getSystemService(ts);
			String imei = mTelephonyMgr.getDeviceId();

			SharedPreferences.Editor editor = sharedPreferences.edit();

			editor.putString("user_id",imei);
			editor.commit();
			Log.v(LOG_TAG,"Put user id: "+imei);
		}
	}


	public void BTN_LOGIN_SUBMIT(View view) {
        // check input
        EditText UsernameField = (EditText) findViewById(R.id.login_username);
        EditText PasswordField = (EditText) findViewById(R.id.login_password);
        String username = UsernameField.getText().toString();
        String password = PasswordField.getText().toString();

        //check empty
        if (username == "" || password == "")
		{
            Context context = getApplicationContext();
            CharSequence text = "Please enter username and password";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else
		{
            Log.v("misc","nothing in one field");
        }
        Log.v("Username",username);
        Log.v("Password",password);

    }

    public void BTN_TAKE_TOUR(View view) {
        //demo

        //placeholder (go to map screen)
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void BTN_CREATE_ACCOUNT(View view) {
        Intent intent = new Intent(this, CreateAccount.class);
        startActivity(intent);
    }

    public void BTN_HTTP_TEST(View view)
    {
        Intent intent = new Intent(this, LocationTest.class);
        startActivity(intent);
    }

    public void BTN_LOC_TEST(View view)
    {
        Intent intent = new Intent(this, LocationTest.class);
        startActivity(intent);
    }

    public void scheduleUpdates()
    {
        Intent intent = new Intent(getApplicationContext(), UpdateLocationReceiver.class);

        final PendingIntent pIntent = PendingIntent
                .getBroadcast(this, UpdateLocationReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstMillis = System.currentTimeMillis();

        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        SharedPreferences sharedPref = getApplicationContext()
                .getSharedPreferences(getString(R.string.user_data_key),Context.MODE_PRIVATE);

        long interval = sharedPref.getLong("updateInterval",5*1000*60);

        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, firstMillis, interval ,pIntent);

		Log.v(LOG_TAG,"Starting update location service");
    }
}