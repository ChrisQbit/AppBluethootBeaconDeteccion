package com.cdsautomatico.apparkame2.models.webServiceResponse;

import com.cdsautomatico.apparkame2.models.Boleto;
import com.google.gson.annotations.Expose;

import retrofit2.Response;

public class GetTicketResponse extends SimpleResponse
{
	  @Expose
	  private Boleto ticket;
	  @Expose
	  private int elapsedMinutes;

	  public GetTicketResponse (Response response)
	  {
		    super(response);
	  }

	  public GetTicketResponse (int status, String message)
	  {
		    super(status, message);
	  }

	  public GetTicketResponse ()
	  {
	  }

	  public int getElapsedMinutes ()
	  {
		    return elapsedMinutes;
	  }

	  public Boleto getTicket ()
	  {
		    return ticket;
	  }
}
