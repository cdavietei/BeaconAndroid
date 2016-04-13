package project.topka.beacon11;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.util.Log;

import java.util.ArrayList;

import project.topka.beacon11.connections.ApiConnection;

public class CreateBeacon extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_beacon);

    }

    public void sendBeacon(View view)
    {
        //post Beaon info to server
        //Fields
        EditText mTitleField = (EditText) findViewById(R.id.beacon_name);
        EditText mDescriptionField = (EditText) findViewById(R.id.beacon_description);
//        EditText mLocationField = (EditText) findViewById(R.id.beacon_location);
        EditText mTimeStartField = (EditText) findViewById(R.id.beacon_time_start);
        EditText mTimeEndField = (EditText) findViewById(R.id.beacon_time_end);
        EditText mInterestsField = (EditText) findViewById(R.id.beacon_interests);
        EditText mLongitudeField = (EditText) findViewById(R.id.beacon_long_coord);
        EditText mLatitudeField = (EditText) findViewById(R.id.beacon_lat_coord);


        String title = mTitleField.getText().toString();
        String desc = mDescriptionField.getText().toString();
        double latcoord = Double.parseDouble(mLatitudeField.getText().toString());
        double longcoord = Double.parseDouble(mLongitudeField.getText().toString());
        ArrayList<String> interests = new ArrayList<String>(); //use loop to fill
        double duration;
        double range;
//        String location;

        Intent intent = new Intent(this, MapsActivity.class);
        Intent intent2 = new Intent(this, NearbyBeacons.class);

        intent.putExtra("lat", latcoord);
        intent.putExtra("longit", longcoord);
        intent.putExtra("title", title);

        intent2.putExtra("lat", latcoord);
        intent2.putExtra("longit", longcoord);
        intent2.putExtra("title", title);

        startActivity(intent2);
        finish();
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

			connection = connection.buildUpon().appendPath(resources.getString(R.string.create_beacon))
						.appendQuery(USER,params[0])
						.appendQuery(LAT,params[1])
						.appendQuery(LON, params[2]);

			String[] optional = {TITLE, START, END, RANGE, PLACE};

			for(int i=3; i<params.length; i++)
				connection.appendQuery(optional[i-3],params[i]);

			connection.build();

			Log.v(LOG_TAG, "Creating Beacon");
			String resp = connection.connect("GET");

			return resp;
        }
    }
}