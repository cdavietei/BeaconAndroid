package project.topka.beacon11;

import android.content.Intent;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.google.*;

import javax.net.ssl.HttpsURLConnection;

import project.topka.beacon11.MapsActivity;


public class CreateBeacon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_beacon);

    }

        public void sendBeacon(View view) {
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

        intent.putExtra("lat",latcoord);
        intent.putExtra("longit",longcoord);
        intent.putExtra("title",title);

        intent2.putExtra("lat",latcoord);
        intent2.putExtra("longit",longcoord);
        intent2.putExtra("title",title);

        startActivity(intent2);
        finish();
    }



//        HttpsURLConnection urlConnection = null;
//        BufferedReader reader = null;
//
//        String forecastJsonStr = null;
//
//        String format = "json";

//        try{
//            String path = "http://localhost:8080/webapi/API/";
//            Uri builtUri = Uri.parse(path).buildUpon();
//        }catch{
//
//        }


//        HttpResponse response = null;
//        try {
//            HttpClient client = new DefaultHttpClient();
//            HttpGet request = new HttpGet();
//            request.setURI(new URI("https://www.googleapis.com/shopping/search/v1/public/products/?key={my_key}&country=&q=t-shirts&alt=json&rankByrelevancy="));
//            response = client.execute(request);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return response;
    }
//        HttpClient client = new DefaultHttpClient();
//        HttpConnectionParams.setConnectionTimeout(client.getParams(),100000);
//        HttpResponse response;


//        JSONObject json = new JSONObject();
//        try {
//
//        } catch (Exception e) {
//            Object n=e.getStackTrace();
//            Toast.makeText(getApplicationContext(),n.toString(), Toast.LENGTH_SHORT).show();
//        }


   // }

    // JSON READER, should go in ManageBeacons
