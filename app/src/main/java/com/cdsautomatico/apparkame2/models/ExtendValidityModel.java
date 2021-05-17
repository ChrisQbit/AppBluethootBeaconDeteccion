package com.cdsautomatico.apparkame2.models;

import com.cdsautomatico.apparkame2.utils.CalendarManager;
import com.google.gson.annotations.Expose;

public class ExtendValidityModel
{
	  @Expose
	  private int parkingId;
	  @Expose
	  private String parkingName;
	  @Expose
	  private String pensionName;
	  @Expose
	  private String actualExpirationDate;
	  @Expose
	  private double cyclePrice;
	  @Expose
	  private int unit;

	  public ExtendValidityModel ()
	  {
	  }

	  public ExtendValidityModel (int parkingId, String parkingName, String pensionName, String actualExpirationDate, double ciclePrice, int unit)
	  {
		    this.parkingId = parkingId;
		    this.parkingName = parkingName;
		    this.pensionName = pensionName;
		    this.actualExpirationDate = actualExpirationDate;
		    this.cyclePrice = ciclePrice;
		    this.unit = unit;
	  }

	  public int getParkingId ()
	  {
		    return parkingId;
	  }

	  public void setParkingId (int parkingId)
	  {
		    this.parkingId = parkingId;
	  }

	  public String getParkingName ()
	  {
		    return parkingName;
	  }

	  public void setParkingName (String parkingName)
	  {
		    this.parkingName = parkingName;
	  }

	  public String getActualExpirationDate ()
	  {
		    return actualExpirationDate;
	  }

	  public String getFormatterExpirationDate ()
	  {
		    return CalendarManager.format(actualExpirationDate);
	  }

	  public void setActualExpirationDate (String actualExpirationDate)
	  {
		    this.actualExpirationDate = actualExpirationDate;
	  }

	  public double getCyclePrice ()
	  {
		    return cyclePrice;
	  }

	  public void setCyclePrice (double cyclePrice)
	  {
		    this.cyclePrice = cyclePrice;
	  }

	  public int getUnit ()
	  {
		    return unit;
	  }

	  public void setUnit (int unit)
	  {
		    this.unit = unit;
	  }

	  public String getPensionName ()
	  {
		    return pensionName;
	  }

	  public void setPensionName (String pensionName)
	  {
		    this.pensionName = pensionName;
	  }
}
