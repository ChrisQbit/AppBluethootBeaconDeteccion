package com.cdsautomatico.apparkame2.models.interfaces;

import androidx.annotation.Nullable;

import okhttp3.Response;
import okhttp3.WebSocket;

public interface OnWebSocketEvent
{
	  void onMessage (WebSocket webSocket, String text);
	  void onClosed (WebSocket webSocket, int code, String reason);
	  void onFailure (WebSocket webSocket, Throwable throwable, @Nullable Response response);
}
