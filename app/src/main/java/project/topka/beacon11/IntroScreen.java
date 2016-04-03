package project.topka.beacon11;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class IntroScreen extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_screen);
    }

    public void BTN_LOGIN_SUBMIT(View view) {
        // check input
        EditText UsernameField = (EditText) findViewById(R.id.login_username);
        EditText PasswordField = (EditText) findViewById(R.id.login_password);
        String username = UsernameField.getText().toString();
        String password = PasswordField.getText().toString();

        //check empty
        if (username == "" || password == "") {
            Context context = getApplicationContext();
            CharSequence text = "Please enter username and password";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else{
            // user authentication

            //post

            //placeholder (go to map screen)
            Log.e("misc","nothing in one field");
//            Intent intent = new Intent(this, MapsActivity.class);
//            startActivity(intent);
//            finish();
        }
        Log.e("Username",username);
        Log.e("Password",password);

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

}
