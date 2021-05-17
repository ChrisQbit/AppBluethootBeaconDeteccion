package com.cdsautomatico.apparkame2.models.webServiceResponse;

import com.cdsautomatico.apparkame2.models.HistoryTicket;
import com.google.gson.annotations.Expose;

import java.util.List;

import retrofit2.Response;

public class GetPeymentHistoryResponse extends SimpleResponse
{
	  @Expose
	  private List<HistoryTicket> tickets;

	  public GetPeymentHistoryResponse (Response response)
	  {
		    super(response);
	  }

	  public GetPeymentHistoryResponse (int status, String message)
	  {
		    super(status, message);
	  }

	  public GetPeymentHistoryResponse ()
	  {
	  }

	  public List<HistoryTicket> getTickets ()
	  {
		    return tickets;
	  }

	  public void setTickets (List<HistoryTicket> tickets)
	  {
		    this.tickets = tickets;
	  }
}
