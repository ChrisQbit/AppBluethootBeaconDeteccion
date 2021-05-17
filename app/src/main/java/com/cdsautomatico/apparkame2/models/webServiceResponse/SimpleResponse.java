package com.cdsautomatico.apparkame2.models.webServiceResponse;

import com.cdsautomatico.apparkame2.utils.Constant;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.Expose;

import java.io.IOException;

import retrofit2.Response;

public class SimpleResponse
{
	  @Expose
	  private boolean success;
	  @Expose
	  private String message;

	  public SimpleResponse (Response response)
	  {
		    this.success = response.isSuccessful();
		    try
		    {
				 if (!response.isSuccessful() && response.errorBody() != null)
				 {
					   String errorBody = response.errorBody().string();
					   JsonObject json = new JsonParser().parse(errorBody).getAsJsonObject();
					   message = json.get(Constant.Extra.MESSAGE).getAsString();
				 }
				 else
				 {
					   message = response.message();
				 }
		    }
		    catch (IOException e)
		    {
				 e.printStackTrace();
		    }
	  }

	  public SimpleResponse (int status, String message)
	  {
		    this.success = status == 200;
		    this.message = message;
	  }

	  public SimpleResponse ()
	  {
		    this.success = false;
		    this.message = "";
	  }

	  public boolean getStatus ()
	  {
		    return success;
	  }

	  public String getMessage ()
	  {
		    return message;
	  }

	  public void setStatus (boolean status)
	  {
		    this.success = status;
	  }

	  public void setMessage (String message)
	  {
		    this.message = message;
	  }

	  public boolean isSuccess ()
	  {
		    return success;
	  }
}
