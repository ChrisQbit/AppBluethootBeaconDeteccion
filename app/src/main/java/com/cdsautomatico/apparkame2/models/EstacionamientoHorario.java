package com.cdsautomatico.apparkame2.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alangvara on 07/12/17.
 */

public class EstacionamientoHorario {

    private Integer id;
    private Integer estacionamientoId;
    private Integer dia;
    private String horaInicio;
    private String horaFin;

    public EstacionamientoHorario(){

    }

    public EstacionamientoHorario(JSONObject json) throws JSONException{
        id = json.getInt("id");
        estacionamientoId = json.getInt("estacionamiento_id");
        dia = json.getInt("dia");
        horaInicio = json.getString("hora_inicio");
        horaFin = json.getString("hora_fin");
    }

    public Integer getId() {
        return id;
    }

    public Integer getEstacionamientoId() {
        return estacionamientoId;
    }

    public Integer getDia() {
        return dia;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }
}
