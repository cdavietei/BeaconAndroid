package project.topka.beacon11;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FriendList extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        String[] testValues = {"Chuck","Gerda","Xiang","Gretchen"};
        ArrayList<String> testArray = new ArrayList<String>();

        //populate arraylist
        for (int i=0; i<testValues.length; i++)
        {
            testArray.add(testValues[i]);
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, testArray);
        ListView friendList = (ListView) findViewById(R.id.friend_list_view);
        friendList.setAdapter(itemsAdapter);
    }
}
