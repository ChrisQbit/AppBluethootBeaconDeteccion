package com.cdsautomatico.apparkame2.dataSource;

import com.cdsautomatico.apparkame2.models.webServiceResponse.GetParkingListResponse;
import com.cdsautomatico.apparkame2.models.webServiceResponse.GetPensionValidityResponse;
import com.cdsautomatico.apparkame2.models.webServiceResponse.GetPeymentHistoryResponse;
import com.cdsautomatico.apparkame2.models.webServiceResponse.GetRateResponse;
import com.cdsautomatico.apparkame2.models.webServiceResponse.GetTicketResponse;
import com.cdsautomatico.apparkame2.models.webServiceResponse.SimpleResponse;
import com.cdsautomatico.apparkame2.utils.Constant;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Services
{
	  String PARKING_CONTROLLER = "/api/Parking";
	  String APPARKAME_CONTROLLER = "/api/Apparkame";

	  @POST(APPARKAME_CONTROLLER + "/GetPensionValidity")
	  Call<GetPensionValidityResponse> getPensionValidity (@Header(Constant.Headers.XAuthToken) String XAuthToken, @Body HashMap<String, Object> body);

	  @POST(APPARKAME_CONTROLLER + "/PayPension")
	  Call<SimpleResponse> payPension (@Header(Constant.Headers.XAuthToken) String xAuthToken, @Body HashMap<String, Object> body);

	  @POST(APPARKAME_CONTROLLER + "/EntryRequest")
	  Call<SimpleResponse> entryRequest (@Header(Constant.Headers.XAuthToken) String xAuthToken, @Body HashMap<String, Object> body);

	  @POST(APPARKAME_CONTROLLER + "/ExitRequest")
	  Call<SimpleResponse> exitRequest (@Header(Constant.Headers.XAuthToken) String xAuthToken, @Body HashMap<String, Object> body);

	  @POST(APPARKAME_CONTROLLER + "/PayRequest")
	  Call<SimpleResponse> payRequest (@Header(Constant.Headers.XAuthToken) String xAuthToken, @Body HashMap<String, Object> body);

	  @GET(APPARKAME_CONTROLLER + "/Rate")
	  Call<GetRateResponse> getRate (@Header(Constant.Headers.XAuthToken) String xAuthToken, @Query(Constant.Extra.CDS_RATE_ID) int cdsRateId, @Query(Constant.Extra.PARKING_ID) int parkingId);

	  @GET(APPARKAME_CONTROLLER + "/ActiveTicket")
	  Call<GetTicketResponse> getActiveTicket (@Header(Constant.Headers.XAuthToken) String xAuthToken, @Header(Constant.Extra.USER) String userId);

	  @GET(APPARKAME_CONTROLLER + "/Parkings")
	  Call<GetParkingListResponse> getParkingList (@Header(Constant.Headers.XAuthToken) String xAuthToken, @Header(Constant.Extra.USER) int userId);

	  @GET(APPARKAME_CONTROLLER + "/PaymentHistory")
	  Call<GetPeymentHistoryResponse> getPaymentHistory (@Header(Constant.Headers.XAuthToken) String xAuthToken, @Query(Constant.Extra.USER_ID) int userId);
}