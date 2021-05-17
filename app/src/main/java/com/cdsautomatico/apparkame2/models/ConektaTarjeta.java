package com.cdsautomatico.apparkame2.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alangvara on 27/12/17.
 */

public class ConektaTarjeta {

    private String id;
    private Integer usuarioId;
    private String brand;
    private Integer last4;
    private Integer expMonth;
    private Integer expYear;
    private boolean defaultCard;

    public ConektaTarjeta(){

    }

    public ConektaTarjeta(JSONObject json) throws JSONException{
        id = json.getString("id");
        usuarioId = json.getInt("usuario_id");
        brand = json.getString("brand");
        last4 = json.getInt("last4");
        expMonth = json.getInt("exp_month");
        expYear = json.getInt("exp_year");
        defaultCard = json.getInt("default") == 1;
    }

    public String getId() {
        return id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public String getBrand() {
        return brand;
    }

    public Integer getLast4() {
        return last4;
    }

    public Integer getExpMonth() {
        return expMonth;
    }

    public Integer getExpYear() {
        return expYear;
    }

    public boolean isDefaultCard() {
        return defaultCard;
    }
}
