//package project.topka.beacon11;
//
//import android.app.Activity;
//import android.app.Fragment;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Message;
//import android.text.Layout;
//import android.util.JsonReader;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.net.ssl.HttpsURLConnection;
//
///**
// * Created by atopk on 4/3/2016.
// */
//public class UpdateBeacon extends Activity {
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manage_beacon);
//    }
//
//    public void BTN_TEST_DATABASE(View view) {
//
//        ArrayList<Double[]> myList = mUpdateBeacon.getList();
//        Intent intent = new Intent(this, MapsActivity.class);
//        intent.putExtra("List", List);
//    }
//}
//
//
////    @Override
////    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
////    {
////        return inflater.inflate(R.layout.activity_manage_beacon, container, false);
////    }
//
//
//    class mUpdateBeacon extends AsyncTask<ArrayList<Double[]>,Void,ArrayList<Double[]>> {
//        ArrayList<Double[]> List = new ArrayList<Double[]>();
//
//        public ArrayList<Double[]> getList() {
//            return List;
//        }
//
//
//    private ArrayList<Double[]> getBeaconDataFromJson(String jsonStr) throws JSONException {
//
//        //json nodes
//        final String BEACON = "beacons";
//        final String CREATOR = "username";
//        final String TITLE = "title";
//        final String LATCOORD = "latCoord";
//        final String LONGCOORD = "longCoord";
//        final String STARTTIME = "startTime";
//        final String ENDTIME = "endTime";
//        final String PLACENAME = "placeName";
//        final String ADDRESS = "address";
//        final String TAGS = "tags";
//
//        JSONObject beaconJson = new JSONObject(jsonStr);
//        JSONArray beaconArray = beaconJson.getJSONArray(BEACON);
//
//        for (int i = 0; i < beaconArray.length(); i++) {
//            JSONObject c = beaconArray.getJSONObject(i);
//            JSONObject location = c.getJSONObject("location");
//
//            JSONArray coords = c.getJSONArray("coordinates");
//            Double[] coordPair = new Double[2];
//
//            coordPair[0] = coords.getDouble(0);
//            coordPair[1] = coords.getDouble(1);
//
//            List.add(coordPair);
//        }
//
//        return List;
//    }
//
//
//    @Override
//    protected ArrayList<Double[]> doInBackground(ArrayList<Double[]>... params) {
//        HttpsURLConnection urlConnection = null;
//        BufferedReader reader = null;
//        String JsonResponse = null;
//
//        String format = "json";
//
//        try {
//            final String BEACON_BASE_URL = "http://ec2-52-90-59-17.compute-1.amazonaws.com/NearbyBeacons";
//            final String LAT_PARAM = "lat";
//            final String LONG_PARAM = "long";
//            final String MAX_PARAM = "max";
//            final String DIST_PARAM = "dist";
//
//            Uri builtUri = Uri.parse(BEACON_BASE_URL).buildUpon()
//                    .appendQueryParameter(LAT_PARAM, "40.729446")
//                    .appendQueryParameter(LONG_PARAM, "-73.996212")
//                    .appendQueryParameter(MAX_PARAM, "5")
//                    .appendQueryParameter(DIST_PARAM, "0.0001")
//                    .build();
//            URL url = new URL(builtUri.toString());
//            urlConnection = (HttpsURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.connect();
//            InputStream inputStream = urlConnection.getInputStream();
//            StringBuffer buffer = new StringBuffer();
//            if (inputStream == null) {
//                // Nothing to do.
//                return null;
//            }
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                // But it does make debugging a *lot* easier if you print out the completed
//                // buffer for debugging.
//                buffer.append(line + "\n");
//            }
//
//            if (buffer.length() == 0) {
//                // Stream was empty.  No point in parsing.
//                return null;
//            }
//            JsonResponse = buffer.toString();
//
//        } catch (Exception e) {
//            Log.e("request", "failed");
//            return null;
//        } finally {
//            if (urlConnection != null) {
//                urlConnection.disconnect();
//            }
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (final IOException e) {
//                    Log.e("test", "Error closing stream", e);
//                }
//            }
//        }
//        try {
//            return getBeaconDataFromJson(JsonResponse);
//        } catch (JSONException e) {
//            Log.e("test", e.getMessage(), e);
//            e.printStackTrace();
//        }
//
//        // This will only happen if there was an error getting or parsing the forecast.
//        return null;
//    }
//
//    public void sendToCreateBeacon(View view) {
//
//    }
//}
////    //Need to close connection
////    //Shouldn't this return something other than null?
////    public static String[] getData() {
////        HttpsURLConnection urlConnection = null;
////        BufferedReader reader = null;
////        String jsonstr = null;
////        String format = "json";
////
////        try {
////            final String BASE_URL = "http://localhost:8080/webapi/API/";
////            URL url = new URL(BASE_URL);
////
////            //Create request
////            urlConnection = (HttpsURLConnection) url.openConnection();
////            urlConnection.setRequestMethod("GET");
////            urlConnection.connect();
////
////            // Read the input stream into a String
////            InputStream inputStream = urlConnection.getInputStream();
////            StringBuffer buffer = new StringBuffer();
////            if (inputStream == null) {
////                // Nothing to do.
////                return null;
////            }
////            reader = new BufferedReader(new InputStreamReader(inputStream));
////            if (buffer.length() == 0) {
////                // Stream was empty.  No point in parsing.
////                return null;
////            }
////        } catch (IOException e) {
////            Log.e("url", "error");
////            return null;
////        } finally {
////            if (urlConnection != null) {
////                urlConnection.disconnect();
////            }
////            if (reader != null) {
////                try {
////                    reader.close();
////                } catch (final IOException e) {
////                    Log.e("stream", "Error closing stream", e);
////                }
////            }
////        }
////        return null;
////
////    }
////
////    // JSON READER //
////    public List readJsonStream(InputStream in) throws IOException {
////        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
////        try {
////            return readBeaconArray(reader);
////        }finally {
////            reader.close();
////        }
////    }
////
////    public List readBeaconArray (JsonReader reader) throws IOException {
////        List beacons = new ArrayList();
////
////        reader.beginArray();
////        while (reader.hasNext()) {
////            beacons.add(readMessage(reader));
////        }
////        reader.endArray();
////        return beacons;
////    }
////
////    public ArrayList<String> readStringArray(JsonReader reader) throws IOException {
////        ArrayList<String> strings = new ArrayList<String>();
////        reader.beginArray();
////        while(reader.hasNext()) {
////            strings.add(reader.nextString());
////        }
////        reader.endArray();
////        return strings;
////    }
////
////    public Message readMessage(JsonReader reader) throws IOException {
////        int length;
////        String title = null;
////        String creator = null;
////        String desc = null;
////        Double latcoord = null;
////        Double longcoord = null;
////        ArrayList<String> interests = new ArrayList<String>();
////        Double duration = null;
////        Double range = null;
////        String location = null;
////
////        reader.beginObject();
////        while (reader.hasNext()) {
////            String name = reader.nextName();
////            if (name.equals("title")) {
////                title = reader.nextString();
////            }
////            else if (name.equals("creator")) {
////                creator = reader.nextString();
////            }
////            else if (name.equals("desc")) {
////                desc = reader.nextString();
////            }
////            else if (name.equals("latcoord")) {
////                latcoord = reader.nextDouble();
////            }
////            else if (name.equals("longcoord")) {
////                longcoord = reader.nextDouble();
////            }
////            else if (name.equals("interests")) {
////                interests = readStringArray(reader);
////            }
////            else if (name.equals("duration")) {
////                duration = reader.nextDouble();
////            }
////            else if (name.equals("range")) {
////                range = reader.nextDouble();
////            }
////            else if (name.equals("location")) {
////                location = reader.nextString();
////            }
////            else{
////                reader.skipValue();
////            }
////        }
////        reader.endObject();
////        //return new Message(title,desc,latcoord,longcoord,interests,duration,range,location);
////        return new Message();
////    }
////
////
////
////}
