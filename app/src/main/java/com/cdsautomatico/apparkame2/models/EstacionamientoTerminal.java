package com.cdsautomatico.apparkame2.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alangvara on 07/12/17.
 */

public class EstacionamientoTerminal
{

	  private Integer id;
	  private Integer estacionamientoId;
	  private String nombre;
	  private String beaconUUID;
	  //    private String[] entradas;
//    private String[] salidas;
	  private double detectionIntensity;
	  private Integer type;

	  private Estacionamiento estacionamiento;

	  public EstacionamientoTerminal ()
	  {

	  }

	  public EstacionamientoTerminal (JSONObject json) throws JSONException
	  {
		    id = json.getInt("id");
		    estacionamientoId = json.getInt("estacionamiento_id");
		    nombre = json.getString("nombre");
		    beaconUUID = json.getString("beacon_uuid");
		    type = json.getInt("cds_type");
		    if (!json.isNull("detection_intensity"))
				 detectionIntensity = json.getInt("detection_intensity");
	  }

	  public Integer getId ()
	  {
		    return id;
	  }

	  public Integer getEstacionamientoId ()
	  {
		    return estacionamientoId;
	  }

	  public String getNombre ()
	  {
		    return nombre;
	  }

	  public String getBeaconUUID ()
	  {
		    return beaconUUID;
	  }

	  public Integer getType ()
	  {
		    return type;
	  }

	  public Estacionamiento getEstacionamiento ()
	  {
		    return estacionamiento;
	  }

	  public void setEstacionamiento (Estacionamiento estacionamiento)
	  {
		    this.estacionamiento = estacionamiento;
	  }

	  public double getDetectionIntensity ()
	  {
		    return detectionIntensity;
	  }
}
