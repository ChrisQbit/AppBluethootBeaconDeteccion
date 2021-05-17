package com.cdsautomatico.apparkame2.models;

import com.google.gson.annotations.Expose;

import java.util.List;

public class Parking extends Entity
{
	  @Expose
	  private boolean accessControl;
	  @Expose
	  private String xApiKey;
	  @Expose
	  private String name;
	  @Expose
	  private String timeZone;
	  @Expose
	  private String url;
	  @Expose
	  private Schedule schedule;
	  @Expose
	  private List<Terminal> terminals;

	  public void setAccessControl (boolean accessControl)
	  {
		    this.accessControl = accessControl;
	  }

	  public void setxApiKey (String xApiKey)
	  {
		    this.xApiKey = xApiKey;
	  }

	  public void setName (String name)
	  {
		    this.name = name;
	  }

	  public void setTimeZone (String timeZone)
	  {
		    this.timeZone = timeZone;
	  }

	  public void setUrl (String url)
	  {
		    this.url = url;
	  }

	  public void setSchedule (Schedule schedule)
	  {
		    this.schedule = schedule;
	  }

	  public void setTerminals (List<Terminal> terminals)
	  {
		    this.terminals = terminals;
	  }

	  public boolean isAccessControl ()
	  {
		    return accessControl;
	  }

	  public String getxApiKey ()
	  {
		    return xApiKey;
	  }

	  public String getName ()
	  {
		    return name;
	  }

	  public String getTimeZone ()
	  {
		    return timeZone;
	  }

	  public String getUrl ()
	  {
		    return url;
	  }

	  public Schedule getSchedule ()
	  {
		    return schedule;
	  }

	  public List<Terminal> getTerminals ()
	  {
		    return terminals;
	  }
}
