package com.cdsautomatico.apparkame2.dataSource;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.cdsautomatico.apparkame2.api.ApiSession;
import com.cdsautomatico.apparkame2.models.interfaces.OnWebSocketEvent;
import com.cdsautomatico.apparkame2.models.manager.SocketMessageHandler;
import com.cdsautomatico.apparkame2.utils.Constant;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Response;
import okhttp3.WebSocket;

public class SocketConsumer extends Service implements OnWebSocketEvent
{
       public static final String TAG = "SocketConsumer";
       public static final String RESTART_SOCKET_CONSUMER = "com.example.pruebas.RESTART_SOCKET_CONSUMER";
       public static final String SOCKET_MESSAGE = "com.example.pruebas.SOCKET_MESSAGE";
//	  private static final String CHANNEL_ID = "Alarma anti-robo";
//	  private static final int NOTIFICATION_ID = 0x1;

       private WebSocketManager socketManager;

       @Override
       public void onCreate ()
       {
              Log.i(TAG, "onCreate");
              if (socketManager == null && ApiSession.getContext() != null)
              {
                     socketManager = new WebSocketManager(this);
                     socketManager.connect(ApiSession.getUsuario().getId().toString(), ApiSession.getAuthToken());
              }
       }

       @Override
       public int onStartCommand (Intent intent, int flags, int startId)
       {
              super.onStartCommand(intent, flags, startId);
              Log.i(TAG, "onStartCommand");
              return START_STICKY;
       }

       @Override
       public boolean onUnbind (Intent intent)
       {
              Log.i(TAG, "onUnbind");
              return super.onUnbind(intent);
       }

       @Override
       public void onDestroy ()
       {
              Log.i(TAG, "onDestroy");
              if (socketManager != null)
                     socketManager.close();
		    /*Intent intent = new Intent(RESTART_SOCKET_CONSUMER);
		    super.onDestroy();
		    sendBroadcast(intent);*/
       }

       @Override
       public void onClosed (WebSocket webSocket, int code, String reason)
       {
              Log.i(TAG, "onClosed: Socket cerrado");
              webSocket.close(code, reason);
       }

       @Override
       public void onFailure (WebSocket webSocket, Throwable throwable, @Nullable Response response)
       {
              Log.e(TAG, "onFailure: " + throwable.getMessage());
              if (socketManager == null)
                     socketManager = new WebSocketManager(this);
              try
              {
                     Thread.sleep(3000);
              }
              catch (InterruptedException e)
              {
                     e.printStackTrace();
              }
              socketManager.connect(ApiSession.getUsuario().getId().toString(), ApiSession.getAuthToken());
       }

       @Override
       public IBinder onBind (Intent intent)
       {
              Log.i(TAG, "onBind");
              return null;
       }

       @Override
       public void onMessage (WebSocket webSocket, String text)
       {
              Log.i(TAG, "onMessage: " + text);

              JsonObject jObject = new JsonParser().parse(text).getAsJsonObject();
              SocketMessageHandler messageHandler = new SocketMessageHandler(jObject);
              Intent intent = new Intent();
              intent.setAction(SOCKET_MESSAGE);
              intent.putExtra(Constant.Extra.TYPE, messageHandler.getType());
              intent.putExtra(Constant.Extra.DATA, messageHandler.getMessage());
              LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
       }
}
