package com.cdsautomatico.apparkame2.models.webServiceResponse;

import com.cdsautomatico.apparkame2.models.Commission;
import com.cdsautomatico.apparkame2.models.rate.Rate;
import com.google.gson.annotations.Expose;

import java.util.List;

import retrofit2.Response;

public class GetRateResponse extends SimpleResponse
{
	  @Expose
	  private Rate rate;
	  @Expose

	  private List<Commission> commissions;

	  public GetRateResponse (Response response)
	  {
		    super(response);
	  }

	  public GetRateResponse (int status, String message)
	  {
		    super(status, message);
	  }

	  public GetRateResponse ()
	  {
	  }

	  public Rate getRate ()
	  {
		    return rate;
	  }

	  public void setRate (Rate rate)
	  {
		    this.rate = rate;
	  }

	  public List<Commission> getCommissions ()
	  {
		    return commissions;
	  }

	  public void setCommissions (List<Commission> commissions)
	  {
		    this.commissions = commissions;
	  }
}
