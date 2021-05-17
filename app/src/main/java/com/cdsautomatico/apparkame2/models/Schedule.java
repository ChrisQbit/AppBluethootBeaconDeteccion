package com.cdsautomatico.apparkame2.models;

import com.cdsautomatico.apparkame2.utils.dateTime.TimeSpan;
import com.google.gson.annotations.Expose;

public class Schedule extends Entity
{
	  @Expose
	  protected String name;
	  @Expose
	  protected String startTime;
	  @Expose
	  protected String endTime;

	  public TimeSpan getStartTime ()
	  {
		    return new TimeSpan(startTime);
	  }

	  public TimeSpan getEndTime ()
	  {
		    return new TimeSpan(endTime);
	  }

	  public String getShowTime ()
	  {
		    return getStartTime().getShowTime() + " - " + getEndTime().getShowTime();
	  }
}
