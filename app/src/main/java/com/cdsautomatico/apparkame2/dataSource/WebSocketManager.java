package com.cdsautomatico.apparkame2.dataSource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.cdsautomatico.apparkame2.BuildConfig;
import com.cdsautomatico.apparkame2.models.interfaces.OnWebSocketEvent;
import com.cdsautomatico.apparkame2.utils.Constant;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketManager extends WebSocketListener
{
	  private static final String CONNECTION_CONTROLLER = "/api/Apparkame/Connect";
	  private static final String SOCKET_URL = BuildConfig.API_NETCORE.replace("https://", "wss://")
		   .replace("http://", "ws://") + CONNECTION_CONTROLLER;

	  private static final String TAG = "WebSocketManager";

	  private WebSocket ws;
	  private OnWebSocketEvent socketEvent;

	  WebSocketManager (@NonNull OnWebSocketEvent listener)
	  {
		    socketEvent = listener;
	  }

	  void connect (String user, String token)
	  {
		    OkHttpClient httpClient = new OkHttpClient();
		    Request request = new Request.Builder()
				.url(SOCKET_URL)
				.addHeader(Constant.Extra.USER, user)
				.addHeader(Constant.Extra.X_AUTH_TOKEN, token)
				.build();
		    Log.i("url ", SOCKET_URL);
		    Log.i("connect ", request.toString());
		    ws = httpClient.newWebSocket(request, this);
		    httpClient.dispatcher().executorService().shutdown();
	  }

	  void close ()
	  {
		    ws.close(1000, "Sin razón");
	  }

	  boolean send (String message)
	  {
		    return ws.send(message);
	  }

	  @Override
	  public void onOpen (WebSocket webSocket, Response response)
	  {
		    Log.i(TAG, "onOpen() : " + response.message());
	  }

	  @Override
	  public void onMessage (WebSocket webSocket, ByteString bytes)
	  {
		    socketEvent.onMessage(webSocket, bytes.utf8());
	  }

	  @Override
	  public void onMessage (WebSocket webSocket, String text)
	  {
		    socketEvent.onMessage(webSocket, text);
	  }

	  @Override
	  public void onClosed (WebSocket webSocket, int code, String reason)
	  {
		    socketEvent.onClosed(webSocket, code, reason);
	  }

	  @Override
	  public void onFailure (WebSocket webSocket, Throwable throwable, @Nullable Response response)
	  {
		    socketEvent.onFailure(webSocket, throwable, response);
	  }
}
