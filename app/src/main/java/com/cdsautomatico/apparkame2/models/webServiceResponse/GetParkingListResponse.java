package com.cdsautomatico.apparkame2.models.webServiceResponse;

import com.cdsautomatico.apparkame2.models.Parking;
import com.google.gson.annotations.Expose;

import java.util.List;

import retrofit2.Response;

public class GetParkingListResponse extends SimpleResponse
{
	  @Expose
	  private List<Parking> parkingList;

	  public GetParkingListResponse (Response response)
	  {
		    super(response);
	  }

	  public GetParkingListResponse (int status, String message)
	  {
		    super(status, message);
	  }

	  public GetParkingListResponse ()
	  {
	  }

	  public List<Parking> getParkingList ()
	  {
		    return parkingList;
	  }
}
