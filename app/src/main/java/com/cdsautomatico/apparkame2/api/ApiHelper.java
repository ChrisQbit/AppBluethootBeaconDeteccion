package com.cdsautomatico.apparkame2.api;

import com.cdsautomatico.apparkame2.BuildConfig;

import java.util.HashMap;

import httprequest.HttpRequest;
import httprequest.HttpRequestCallback;

public class ApiHelper
{

    static private final String API_URL = BuildConfig.API_URL;

    static public void setAuthToken(String authToken){
        HttpRequest.setAuthToken(authToken);
    }

    static public void login(String username, String password, HttpRequestCallback callback){
        HashMap<String, String> data = new HashMap<>();
        data.put("email", username);
        data.put("password", password);

        HttpRequest.sendPost(API_URL + "/api/login", data, callback);
    }

    static public void loginFacebook(String facebookToken, HttpRequestCallback callback){
        HashMap<String, String> data = new HashMap<>();
        data.put("facebook_token", facebookToken);

        HttpRequest.sendPost(API_URL + "/api/login-facebook", data, callback);
    }

    static public void logout(HttpRequestCallback callback){
        HashMap<String, String> data = new HashMap<>();
        data.put("auth_token", ApiSession.getAuthToken());

        HttpRequest.sendPost(API_URL + "/api/logout", data, callback);
    }

    static public void registro(String nombre, String email, String password, String password2, HttpRequestCallback callback){
        HashMap<String, String> data = new HashMap<>();
        data.put("nombre", nombre);
        data.put("email", email);
        data.put("password", password);
        data.put("password_confirmation", password2);

        HttpRequest.sendPost(API_URL + "/api/registro", data, callback);
    }

    static public void recuperarContrasenia(String email, HttpRequestCallback callback){
        HashMap<String, String> data = new HashMap<>();
        data.put("email", email);

        HttpRequest.sendPost(API_URL + "/api/restablecer-contrasenia", data, callback);
    }

    static public void editarPerfil(String nombre, String email, String password, String password2,
                                    String imagen, HttpRequestCallback callback){

        HashMap<String, String> data = new HashMap<>();
        data.put("nombre", nombre);
        data.put("email", email);
        data.put("password", password);
        data.put("password_confirmation", password2);
        data.put("imagen", imagen);

        HttpRequest.setAuthToken(ApiSession.getAuthToken());
        HttpRequest.sendPost(API_URL + "/api/editar-perfil", data, callback);
    }

    static public void estacionamientos(HttpRequestCallback callback){
        String url = API_URL + "/api/estacionamientos";

        HttpRequest.setAuthToken(ApiSession.getAuthToken());
        HttpRequest.sendGet(url, callback);
    }

    static public void tarjetas(HttpRequestCallback callback){
        String url = API_URL + "/api/tarjetas";

        HttpRequest.setAuthToken(ApiSession.getAuthToken());
        HttpRequest.sendGet(url, callback);
    }

    static public void guardarTarjeta(String cardToken, HttpRequestCallback callback){
        String url = API_URL + "/api/guardar-tarjeta";

        HashMap<String, String> data = new HashMap<>();
        data.put("card_token", cardToken);

        HttpRequest.setAuthToken(ApiSession.getAuthToken());
        HttpRequest.sendPost(url, data, callback);
    }

    static public void eliminarTarjeta(String cardId, HttpRequestCallback callback){
        String url = API_URL + "/api/eliminar-tarjeta";

        HashMap<String, String> data = new HashMap<>();
        data.put("card_id", cardId);

        HttpRequest.setAuthToken(ApiSession.getAuthToken());
        HttpRequest.sendPost(url, data, callback);
    }

    static public void pensiones(HttpRequestCallback callback){
        String url = API_URL + "/api/pensiones";

        HttpRequest.setAuthToken(ApiSession.getAuthToken());
        HttpRequest.sendGet(url, callback);
    }

    static public void ingresarEstacionamiento(Integer terminalId, HttpRequestCallback callback){
        String url = API_URL + "/api/estacionamientos/ingresar";
        HashMap<String, String> data = new HashMap<>();
        data.put("terminal_id", terminalId.toString());

        HttpRequest.setAuthToken(ApiSession.getAuthToken());
        HttpRequest.sendPost(url, data, callback);
    }

    static public void boletoActivo(HttpRequestCallback callback){
        String url = API_URL + "/api/boleto-activo";

        HttpRequest.setAuthToken(ApiSession.getAuthToken());
        HttpRequest.sendGet(url, callback);
    }

    static public void pagarEstacionamiento(String cardId, HttpRequestCallback callback){
        String url = API_URL + "/api/estacionamientos/pagar";

        HashMap<String, String> data = new HashMap<>();
        data.put("card_id", cardId);

        HttpRequest.setAuthToken(ApiSession.getAuthToken());
        HttpRequest.sendPost(url, data, callback);
    }

    static public void salirEstacionamiento(Integer terminalId, HttpRequestCallback callback){
        String url = API_URL + "/api/estacionamientos/salir";

        HashMap<String, String> data = new HashMap<>();
        data.put("terminal_id", terminalId.toString());

        HttpRequest.setAuthToken(ApiSession.getAuthToken());
        HttpRequest.sendPost(url, data, callback);
    }

}
