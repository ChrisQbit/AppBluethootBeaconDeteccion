package com.cdsautomatico.apparkame2.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User
{
	  @Expose
	  @SerializedName("value")
	  private String user;

	  private String passw;

	  @Expose
	  private String token;
	  @Expose
	  private long activeTerminal;

	  public User ()
	  {
	  }

	  public User (String user, String token)
	  {
		    this.user = user;
		    this.token = token;
	  }

	  public long getActiveTerminal ()
	  {
		    return activeTerminal;
	  }

	  public void setActiveTerminal (long activeTerminal)
	  {
		    this.activeTerminal = activeTerminal;
	  }

	  public String getUser ()
	  {
		    return user;
	  }

	  public void setUser (String user)
	  {
		    this.user = user;
	  }

	  public String getPassw ()
	  {
		    return passw;
	  }

	  public void setPassw (String passw)
	  {
		    this.passw = passw;
	  }

	  public String getToken ()
	  {
		    return token;
	  }

	  public void setToken (String token)
	  {
		    this.token = token;
	  }
}
