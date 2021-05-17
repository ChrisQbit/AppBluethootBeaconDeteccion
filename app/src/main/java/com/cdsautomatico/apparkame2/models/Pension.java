package com.cdsautomatico.apparkame2.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alangvara on 27/12/17.
 */

public class Pension {

    private String id;
    private Integer usuarioId;
    private Integer estacionamientoId;
    private String fechaInicio;
    private String fechaVencimiento;
    private String fechaContrato;

    private Estacionamiento estacionamiento;

    public Pension(JSONObject json) throws JSONException{
        id = json.getString("id");
        usuarioId = json.getInt("usuario_id");
        estacionamientoId = json.getInt("estacionamiento_id");
        fechaInicio = json.getString("fecha_inicio");
        fechaVencimiento = json.getString("fecha_vencimiento");
        fechaContrato = json.getString("fecha_contrato");

        if(!json.isNull("estacionamiento")){
            estacionamiento = new Estacionamiento(json.getJSONObject("estacionamiento"));
        }
    }

    public String getId() {
        return id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public Integer getEstacionamientoId() {
        return estacionamientoId;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaVencimiento() {
        return fechaVencimiento;
    }

    public String getFechaContrato() {
        return fechaContrato;
    }

    public Estacionamiento getEstacionamiento() {
        return estacionamiento;
    }
}
