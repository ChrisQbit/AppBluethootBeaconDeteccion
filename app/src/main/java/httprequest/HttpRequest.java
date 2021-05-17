package httprequest;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by alangvara on 27/06/17.
 */

public class HttpRequest
{

	  private static String authToken;

	  static public void setAuthToken (String authToken)
	  {
		    HttpRequest.authToken = authToken;
	  }

	  static public void sendGet (String url, final HttpRequestCallback callback)
	  {
		    SendGetCallback sendGet = new SendGetCallback(callback);
		    sendGet.execute(url);
	  }

	  static public void sendPost (String url, final HashMap<String, String> data, final HttpRequestCallback callback)
	  {
		    SendPostAsync sendPost = new SendPostAsync(callback, data);
		    sendPost.execute(url);
	  }

	  static public void sendDelete (String url, HttpRequestCallback callback)
	  {

	  }


	  static private class SendGetCallback extends AsyncTask<String, Void, JSONObject>
	  {
		    private HttpRequestException exception;
		    private HttpRequestCallback callback;

		    public SendGetCallback (HttpRequestCallback callback)
		    {
				 this.callback = callback;
		    }

		    @Override
		    protected JSONObject doInBackground (String... params)
		    {
				 try
				 {
					   URL url = new URL(params[0]);
					   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

					   if (authToken != null)
					   {
							urlConnection.setRequestProperty("x-auth-token", authToken);
					   }

					   try
					   {
							int httpStatusCode = urlConnection.getResponseCode();

							InputStream in;
							if (httpStatusCode == 200)
							{
								  in = urlConnection.getInputStream();
							}
							else
							{
								  in = urlConnection.getErrorStream();
							}

							StringBuilder stringBuilderResponse = new StringBuilder();

							int nextChar;
							while ((nextChar = in.read()) > 0)
							{
								  stringBuilderResponse.append((char) nextChar);
							}

							String strResponse = stringBuilderResponse.toString();

							try
							{
								  return new JSONObject(strResponse);
							}
							catch (JSONException e)
							{
								  exception = new HttpRequestException(e.getMessage(), e.getCause());
								  exception.setHttpStatusCode(httpStatusCode);
								  exception.setHttpResponseText(strResponse);
							}

					   }
					   finally
					   {
							urlConnection.disconnect();
					   }
				 }
				 catch (IOException e)
				 {
					   exception = new HttpRequestException(e.getMessage(), e.getCause());
				 }
				 return null;
		    }

		    //        @Override
		    protected void onPostExecute (JSONObject json)
		    {
				 super.onPostExecute(json);

				 if (exception != null)
				 {
					   callback.onError(exception);
				 }
				 else
				 {
					   callback.onSuccess(json);
				 }
		    }

	  }

	  static private class SendPostAsync extends AsyncTask<String, Void, JSONObject>
	  {
		    private HttpRequestException exception;
		    private HttpRequestCallback callback;
		    private HashMap<String, String> postData;

		    public SendPostAsync (HttpRequestCallback callback, HashMap<String, String> postData)
		    {
				 this.callback = callback;
				 this.postData = postData;
		    }

		    @Override
		    protected JSONObject doInBackground (String... params)
		    {
				 int httpStatusCode = 0;

				 try
				 {
					   URL url = new URL(params[0]);
					   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					   conn.setConnectTimeout(30000);
					   conn.setRequestMethod("POST");
					   conn.setDoOutput(true);

					   if (authToken != null)
					   {
							conn.setRequestProperty("x-auth-token", authToken);
					   }

					   OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

					   StringBuilder stringBuilderPostData = new StringBuilder();
					   for (String key : postData.keySet())
					   {
							if (postData.get(key) != null)
							{
								  stringBuilderPostData.append(URLEncoder.encode(key, "UTF-8"));
								  stringBuilderPostData.append('=');
								  stringBuilderPostData.append(URLEncoder.encode(postData.get(key), "UTF-8"));
								  stringBuilderPostData.append('&');
							}
					   }

					   wr.write(stringBuilderPostData.toString());
					   stringBuilderPostData.setLength(0); //liberar memoria
					   wr.flush();

					   try
					   {
							httpStatusCode = conn.getResponseCode();

							InputStream in;
							if (httpStatusCode == 200)
							{
								  in = conn.getInputStream();
							}
							else
							{
								  in = conn.getErrorStream();
							}

							StringBuilder stringBuilderResponse = new StringBuilder();

							int nextChar;
							while ((nextChar = in.read()) > 0)
							{
								  stringBuilderResponse.append((char) nextChar);
							}

							String strResponse = stringBuilderResponse.toString();

							try
							{
								  return new JSONObject(strResponse);
							}
							catch (JSONException e)
							{
								  this.exception = new HttpRequestException(e.getMessage(), e.getCause());
								  this.exception.setHttpStatusCode(httpStatusCode);
								  this.exception.setHttpResponseText(strResponse);
							}

					   }
					   finally
					   {
							conn.disconnect();
					   }
				 }
				 catch (IOException e)
				 {
					   this.exception = new HttpRequestException(e.getMessage(), e.getCause());
					   this.exception.setHttpStatusCode(httpStatusCode);
				 }
				 return null;
		    }

		    @Override
		    protected void onPostExecute (JSONObject json)
		    {
				 if (callback != null)
				 {
					   if (exception != null)
					   {
							callback.onError(exception);
					   }
					   else
					   {
							super.onPostExecute(json);
							callback.onSuccess(json);
					   }
				 }
		    }
	  }
}
