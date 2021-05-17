package com.cdsautomatico.apparkame2.models;

import com.cdsautomatico.apparkame2.models.rate.Rate;
import com.google.gson.annotations.Expose;

public class ApparkameRateUpdated
{
	  @Expose
	  private Rate rate;

	  public Rate getRate ()
	  {
		    return rate;
	  }

	  public void setRate (Rate rate)
	  {
		    this.rate = rate;
	  }
}
