package project.topka.beacon11;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HTTPTest extends Activity
{
	public int test = 0;
	private String outputStr = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_httptest);

	}

	public void BTN_SUBMIT_TEST(View view)
	{
		FetchBuzzFeed fetch = new FetchBuzzFeed();
		String path = ((EditText) findViewById(R.id.buzzfeedpath)).getText().toString();

		fetch.execute(path);

		TextView output = (TextView) findViewById(R.id.testoutput);
		output.setText(outputStr);
	}

	@Override
	public void onStart()
	{
		super.onStart();

		outputStr = "Hello";
	}

	@Override
	public void onStop()
	{
		super.onStop();
	}


	public class FetchBuzzFeed extends AsyncTask<String, Void, String>
	{
		private final String LOG_TAG = FetchBuzzFeed.class.getSimpleName();


		@Override
		protected String doInBackground(String... params)
		{
			if (params.length == 0)
				return null;

			HttpURLConnection urlConnection = null;
			BufferedReader reader = null;

			String buzzFeedJsonStr = null;

			try {
				final String BASE_URL = "http://www.buzzfeed.com/api/v2/feeds/";

				Uri builder = Uri.parse(BASE_URL).buildUpon()
						.appendPath(params[0]).build();

				URL url = new URL(builder.toString());

				Log.v(LOG_TAG, "Built URI " + builder.toString());

				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.connect();

				InputStream inputStream = urlConnection.getInputStream();
				StringBuffer buffer = new StringBuffer();

				if (inputStream == null)
					return null;

				reader = new BufferedReader(new InputStreamReader(inputStream));

				String line;

				while ((line = reader.readLine()) != null)
					buffer.append(line + "\n");

				if (buffer.length() == 0)
					return null;

				buzzFeedJsonStr = buffer.toString();

				Log.v(LOG_TAG, "Response string: too long" );

			}
			catch (MalformedURLException e) {
				Log.e(LOG_TAG, "Error", e);
				return null;
			}
			catch (IOException e) {
				Log.e(LOG_TAG, "Error", e);
				return null;
			}
			finally {
				if (urlConnection != null)
					urlConnection.disconnect();

				if (reader != null)
					try {
						reader.close();
					}
					catch (IOException e) {
						Log.e(LOG_TAG, "Error closing stream", e);
					}
			}

			return buzzFeedJsonStr;
		}

		protected void onPostExecute(String result)
		{
			if (result != null)
				outputStr = result;
		}
	}


}
