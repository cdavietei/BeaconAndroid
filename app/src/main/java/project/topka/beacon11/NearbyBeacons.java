package project.topka.beacon11;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import project.topka.beacon11.beacons.Beacon;

public class NearbyBeacons extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_beacons);

        //TODO: Get Array from MapsActivity

        //Array to populate ListView
        ArrayList<String> beaconTitles = new ArrayList<String>();

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, beaconTitles);
        ListView beaconList = (ListView) findViewById(R.id.beacon_list_view);
        beaconList.setAdapter(itemsAdapter);

        beaconList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = ((TextView) view).getText().toString();
                double latCoord = 0.0, longCoord = 0.0;
                //ON CLICK
                goBack(view,latCoord,longCoord,item);
            }

        });

        } //end onCreate

    public void goBack(View view, double lat, double longit,String title) {
        //TODO: Change this to send creator of Beacon tapped back to MapsActivity
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("lat",lat);
        intent.putExtra("longit",longit);
        intent.putExtra("title",title);
        startActivity(intent);
        finish();
    }
    public void BTN_TEST_DATABASE(View view){
        //display json string
    }



}
