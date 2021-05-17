package com.cdsautomatico.apparkame2.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alangvara on 07/12/17.
 */

public class EstacionamientoTarifa {

    private Integer id;
    private Integer estacionamientoId;
    private int minutos;
    private double tarifa;

    public EstacionamientoTarifa(){

    }

    public EstacionamientoTarifa(JSONObject json) throws JSONException{
        id = json.getInt("id");
        estacionamientoId = json.getInt("estacionamiento_id");
        minutos = json.getInt("minutos");
        tarifa = json.getDouble("tarifa");
    }

    public Integer getId() {
        return id;
    }

    public Integer getEstacionamientoId() {
        return estacionamientoId;
    }

    public int getMinutos() {
        return minutos;
    }

    public double getTarifa() {
        return tarifa;
    }
}
