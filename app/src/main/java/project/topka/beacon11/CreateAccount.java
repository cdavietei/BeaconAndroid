package project.topka.beacon11;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CreateAccount extends Activity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

    }

    public void BTN_CREATE_NEW_ACCOUNT(View view)
    {
        EditText EmailField = (EditText) findViewById(R.id.create_email);
        EditText UsernameField = (EditText) findViewById(R.id.create_username);
        EditText PasswordField = (EditText) findViewById(R.id.create_password);
        EditText ConfirmPasswordField = (EditText) findViewById(R.id.confirm_password);

        String email = EmailField.getText().toString();
        String username = UsernameField.getText().toString();
        String password = PasswordField.getText().toString();
        String confirm_password = ConfirmPasswordField.getText().toString();

        //post
        if (checkInput(email,username,password,confirm_password))
        {

        }else
        {
            //toast
            Log.e("input", "invalid");
        }

        //placeholder goto map screen
        Intent intent = new Intent(this,MapsActivity.class);
        startActivity(intent);
        finish();
    }

    public boolean checkInput(String e, String u, String p, String p2) {
        if (p != p2) {
            return false;
        }
        else return true;
    }
}
