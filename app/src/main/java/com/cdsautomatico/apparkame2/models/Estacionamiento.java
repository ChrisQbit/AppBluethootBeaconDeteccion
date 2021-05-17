package com.cdsautomatico.apparkame2.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alangvara on 07/12/17.
 */

public class Estacionamiento {

    private Integer id;
    private String nombre;
    private String direccion;
    private float lat;
    private float lng;
    private int espacios;
    private int minutosGracia;
    private String zonaHoraria;
    private String imagen;
    private EstacionamientoTarifa primerTarifa;

    private ArrayList<EstacionamientoTerminal> terminales = new ArrayList<>();
    private ArrayList<EstacionamientoTarifa> tarifas = new ArrayList<>();
    private HashMap<Integer, EstacionamientoHorario> horarios = new HashMap<>();

    public Estacionamiento(){

    }

    public Estacionamiento(JSONObject json) throws JSONException{
        id = json.getInt("id");
        nombre = json.getString("nombre");
        direccion = json.getString("direccion");
        espacios = json.getInt("espacios");
        minutosGracia = json.getInt("minutos_gracia_pago");
        zonaHoraria = json.getString("zona_horaria");

        if(!json.isNull("lat")){
            lat = (float) json.getDouble("lat");
        }

        if(!json.isNull("lng")){
            lng = (float) json.getDouble("lng");
        }

        if(!json.isNull("imagen_url")){
            imagen = json.getString("imagen_url");
        }

        if(!json.isNull("terminales")){
            JSONArray jsonAccesos = json.getJSONArray("terminales");
            for(int i=0; i<jsonAccesos.length(); i++){
                EstacionamientoTerminal terminal =
                        new EstacionamientoTerminal(jsonAccesos.getJSONObject(i));
                terminal.setEstacionamiento(this);
                terminales.add(terminal);
            }
        }

        if(!json.isNull("primer_tarifa")){
            primerTarifa = new EstacionamientoTarifa(json.getJSONObject("primer_tarifa"));
        }

        if(!json.isNull("horarios")){
            JSONArray jsonHorario = json.getJSONArray("horarios");
            for(int i=0; i<jsonHorario.length(); i++){
                EstacionamientoHorario horario = new EstacionamientoHorario(jsonHorario.getJSONObject(i));
                horarios.put(horario.getDia(), horario);
            }
        }
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public int getEspacios() {
        return espacios;
    }

    public int getMinutosGracia() {
        return minutosGracia;
    }

    public String getZonaHoraria() {
        return zonaHoraria;
    }

    public String getImagen() {
        return imagen;
    }

    public EstacionamientoTarifa getPrimerTarifa() {
        return primerTarifa;
    }

    public ArrayList<EstacionamientoTerminal> getTerminales() {
        return terminales;
    }

    public ArrayList<EstacionamientoTarifa> getTarifas() {
        return tarifas;
    }

    public HashMap<Integer, EstacionamientoHorario> getHorarios() {
        return horarios;
    }
}
