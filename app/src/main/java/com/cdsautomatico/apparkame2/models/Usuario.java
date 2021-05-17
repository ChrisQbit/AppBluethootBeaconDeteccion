package com.cdsautomatico.apparkame2.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alangvara on 07/12/17.
 */

public class Usuario {

    private Integer id;
    private String perfil;
    private String email;
    private String facebookId;
    private String nombre;
    private String imagen;


    public Usuario(){

    }

    public Usuario(JSONObject json) throws JSONException{
        id = json.getInt("id");
        perfil = json.getString("perfil");
        nombre = json.getString("nombre");

        if(!json.isNull("email")){
            email = json.getString("email");
        }

        if(!json.isNull("imagen_url")){
            imagen = json.getString("imagen_url");
        }

        if(!json.isNull("facebook_id")){
            facebookId = json.getString("facebook_id");
        }
    }

    public Integer getId() {
        return id;
    }

    public String getPerfil() {
        return perfil;
    }

    public String getEmail() {
        return email;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }
}
