package com.cdsautomatico.apparkame2.models;

import com.cdsautomatico.apparkame2.models.enums.TerminalType;
import com.cdsautomatico.apparkame2.utils.dateTime.Date;
import com.google.gson.annotations.Expose;

public class Terminal extends Entity
{
	  @Expose
	  private String name;
	  @Expose
	  private int terminalType;
	  @Expose
	  private String creationDate;
	  @Expose
	  private String beaconUUID;
	  @Expose
	  private int cdsTerminalId;
	  @Expose
	  private String deletedAt;
	  @Expose
	  private double detectionIntensity;
	  @Expose
	  private boolean antiPassBack;
	  private Parking parking;
	  private double actualIntensity;

	  public String getName ()
	  {
		    return name;
	  }

	  public TerminalType getTerminalType ()
	  {
		    return TerminalType.values()[terminalType];
	  }

	  public Date getCreationDate ()
	  {
		    return new Date(creationDate);
	  }

	  public String getBeaconUUID ()
	  {
		    return beaconUUID;
	  }

	  public int getCdsTerminalId ()
	  {
		    return cdsTerminalId;
	  }

	  public Date getDeletedAt ()
	  {
		    return new Date(deletedAt);
	  }

	  public double getDetectionIntensity ()
	  {
		    return detectionIntensity;
	  }

	  public void setName (String name)
	  {
		    this.name = name;
	  }

	  public void setTerminalType (TerminalType terminalType)
	  {
		    this.terminalType = terminalType.ordinal();
	  }

	  public void setCreationDate (String creationDate)
	  {
		    this.creationDate = creationDate;
	  }

	  public void setBeaconUUID (String beaconUUID)
	  {
		    this.beaconUUID = beaconUUID;
	  }

	  public void setCdsTerminalId (int cdsTerminalId)
	  {
		    this.cdsTerminalId = cdsTerminalId;
	  }

	  public void setDeletedAt (String deletedAt)
	  {
		    this.deletedAt = deletedAt;
	  }

	  public void setDetectionIntensity (double detectionIntensity)
	  {
		    this.detectionIntensity = detectionIntensity;
	  }

	  public Parking getParking ()
	  {
		    return parking;
	  }

	  public void setParking (Parking parking)
	  {
		    this.parking = parking;
	  }

	  public boolean isAntiPassBack ()
	  {
		    return antiPassBack;
	  }

	  public void setAntiPassBack (boolean antiPassBack)
	  {
		    this.antiPassBack = antiPassBack;
	  }

	  public double getActualIntensity ()
	  {
		    return actualIntensity;
	  }

	  public void setActualIntensity (double actualIntensity)
	  {
		    this.actualIntensity = actualIntensity;
	  }
}
