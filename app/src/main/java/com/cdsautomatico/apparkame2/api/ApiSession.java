package com.cdsautomatico.apparkame2.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.cdsautomatico.apparkame2.models.Usuario;
import com.google.gson.Gson;

/**
 * Created by alanguevara on 19/08/16.
 */
public class ApiSession
{

    private static Context context;
    private static final String TAG = "ApiSession";
    private static String authToken;
    private static Usuario usuario;

    public static Context getContext(){
        return context;
    }

    public static void setContext(Context context){
        ApiSession.context = context;
    }

    public static String getAuthToken(){
        if(authToken != null){
            return authToken;
        }

        SharedPreferences settings = getSharedPreferences();
        authToken = settings.getString("auth_token", null);

        return authToken;
    }

    public static void setAuthToken(String authToken){
        SharedPreferences settings = getSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("auth_token", authToken);
        editor.apply();
        ApiSession.authToken = authToken;
    }

    public static Usuario getUsuario(){
        if(usuario != null){
            return usuario;
        }

        SharedPreferences settings = getSharedPreferences();
        usuario = new Gson().fromJson(settings.getString("usuario", null), Usuario.class);

        return usuario;
    }

    public static void setUsuario(Usuario usuario){
        SharedPreferences settings = getSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("usuario", new Gson().toJson(usuario));
        editor.apply();
        ApiSession.usuario = usuario;
    }

    public static void clear(){
        SharedPreferences settings = getSharedPreferences();
        SharedPreferences.Editor editor = settings.edit();
        authToken = null;
        usuario = null;
        editor.putString("auth_token", null);
        editor.putString("usuario", null);
        editor.apply();
    }

    private static SharedPreferences getSharedPreferences(){
        return context.getSharedPreferences("session", Context.MODE_PRIVATE);
    }
}
