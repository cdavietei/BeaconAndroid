package project.topka.beacon11;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends Activity {

    //timer
    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                Intent intent = new Intent(SplashScreen.this, IntroScreen.class);
                startActivity(intent);

                // close this activity
                finish();
            }

        }, SPLASH_TIME_OUT);
    }
}
