package project.topka.beacon11;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseTest extends ListActivity {

    private Context context;
    private static String url = "http://localhost:8080/webapi/API/";

    //json nodes
    private static final String BEACON = "beacon";
    private static final String CREATOR = "username";
    private static final String TITLE = "title";
    private static final String LATCOORD = "latCoord";
    private static final String LONGCOORD = "longCoord";
    private static final String STARTTIME = "startTime";
    private static final String ENDTIME = "endTime";
    private static final String PLACENAME = "placeName";
    private static final String ADDRESS = "address";
    private static final String TAGS = "tags";


    ArrayList<HashMap<String,String>> jsonlist = new ArrayList<HashMap<String,String>>();

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);
//        new ProgressTask(DatabaseTest.this).execute();
    }

    public void sendToActivity(View view) {
        Intent intent = new Intent(this, NearbyBeacons.class);

    }

//    private class ProgressTask extends AsyncTask<String, Void, Boolean> {
//        private ProgressDialog dialog;
//        private ListActivity activity;
//        public ProgressTask(ListActivity activity) {
//            context = activity;
//            dialog = new ProgressDialog(context);
//        }
//
//        private Context context;
//
//        protected void onPreExecute() {
//            this.dialog.setMessage("Progress start");
//            this.dialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//
//        }
//    }
}
