package project.topka.beacon11.connections;

import android.net.Uri.Builder;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Chris on 9/4/16.
 */
public class ApiConnection
{
	protected Builder builder = null;
	protected Uri uri = null;
	protected final String BASE_URL;
	protected final String LOG_TAG;

	public ApiConnection(String base)
	{
		BASE_URL = base;
		LOG_TAG = this.getClass().getSimpleName();
	}

	public ApiConnection(String base, String logTag)
	{
		BASE_URL = base;
		LOG_TAG = logTag;
	}

	public ApiConnection buildUpon()
	{
		builder = Uri.parse(BASE_URL).buildUpon();
		return this;
	}


	public ApiConnection appendPath(String path)
	{
		builder = builder.appendPath(path);
		return this;
	}

	public ApiConnection appendQuery(String key, String value)
	{
		builder = builder.appendQueryParameter(key,value);
		return this;
	}

	public ApiConnection build()
	{
		uri = builder.build();

		Log.v(LOG_TAG, "Built URI "+builder.toString());
		return this;
	}

	public String connect(String method)
	{

		if(builder == null)
			return null;

		BufferedReader reader = null;
		HttpURLConnection urlConnection = null;

		String retval = null;

		try
		{
			URL url = new URL(builder.toString());

			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod(method);
			urlConnection.connect();

			InputStream inputStream = urlConnection.getInputStream();
			StringBuffer buffer = new StringBuffer();

			if(inputStream == null)
				return null;

			reader = new BufferedReader(new InputStreamReader(inputStream));

			String line;

			while((line = reader.readLine()) != null)
				buffer.append(line + "\n");

			if(buffer.length() == 0)
				return null;

			retval = buffer.toString();
		}
		catch(IOException e)
		{
			Log.e(LOG_TAG, "Error", e);
			return null;
		}
		finally
		{
			if(urlConnection != null)
				urlConnection.disconnect();

			if (reader != null)
				try
				{
					reader.close();
				}
				catch (IOException e)
				{
					Log.e(LOG_TAG, "Error closing stream", e);
				}
		}

		return retval;
	}
}
