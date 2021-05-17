package com.cdsautomatico.apparkame2.models.rate;

import com.cdsautomatico.apparkame2.models.Entity;
import com.google.gson.annotations.Expose;

import java.util.List;

public class Category extends Entity
{
	  @Expose
	  private int rateId;
	  @Expose
	  private int startMinute;
	  @Expose
	  private int endMinute;
	  @Expose
	  private List<RateSchedule> schedules;

	  public int getRateId ()
	  {
		    return rateId;
	  }

	  public void setRateId (int rateId)
	  {
		    this.rateId = rateId;
	  }

	  public int getStartMinute ()
	  {
		    return startMinute;
	  }

	  public void setStartMinute (int startMinute)
	  {
		    this.startMinute = startMinute;
	  }

	  public int getEndMinute ()
	  {
		    return endMinute;
	  }

	  public void setEndMinute (int endMinute)
	  {
		    this.endMinute = endMinute;
	  }

	  public List<RateSchedule> getSchedules ()
	  {
		    return schedules;
	  }

	  public void setSchedules (List<RateSchedule> schedules)
	  {
		    this.schedules = schedules;
	  }
}
