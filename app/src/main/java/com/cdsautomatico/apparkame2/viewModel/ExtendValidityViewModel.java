package com.cdsautomatico.apparkame2.viewModel;

import android.app.Application;

import com.cdsautomatico.apparkame2.api.ApiSession;
import com.cdsautomatico.apparkame2.dataBase.ExtendValidityDao;
import com.cdsautomatico.apparkame2.dataSource.RetrofitErrorHandler;
import com.cdsautomatico.apparkame2.dataSource.ServiceAdapter;
import com.cdsautomatico.apparkame2.models.ExtendValidityModel;
import com.cdsautomatico.apparkame2.models.enums.TimeUnits;
import com.cdsautomatico.apparkame2.models.webServiceResponse.GetPensionValidityResponse;
import com.cdsautomatico.apparkame2.models.webServiceResponse.SimpleResponse;
import com.cdsautomatico.apparkame2.utils.CalendarManager;
import com.cdsautomatico.apparkame2.utils.Constant;

import java.util.HashMap;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExtendValidityViewModel extends AndroidViewModel
{
	  private MutableLiveData<ExtendValidityModel> model;
	  private MutableLiveData<SimpleResponse> payment;

	  private SimpleResponse lastError;

	  private TimeUnits cycleUnit;
	  private CalendarManager calendarManager;
	  private ExtendValidityDao dao;

	  public ExtendValidityViewModel (@NonNull Application application)
	  {
		    super(application);
		    dao = new ExtendValidityDao(application);
		    calendarManager = new CalendarManager();
		    model = new MutableLiveData<>();
		    payment = new MutableLiveData<>();
		    model.setValue(dao.getPensionValidity());
	  }

	  public MutableLiveData<ExtendValidityModel> loadPensionValidity (String pensionId)
	  {
		    refreshModel(pensionId);
		    return model;
	  }


	  public String getNewExpirationDate ()
	  {
		    if (model.getValue() != null)
				 return calendarManager.roll(model.getValue().getActualExpirationDate(), 1, cycleUnit);
		    else
				 return "";
	  }

	  public String getCyclePrice (double cyclePrice)
	  {
		    return String.format(Locale.US, "%.2f", cyclePrice);
	  }

	  private void setUnit (int unit)
	  {
		    cycleUnit = TimeUnits.values()[unit];
	  }

	  public SimpleResponse getLastError ()
	  {
		    return lastError;
	  }

	  private void refreshModel (String id)
	  {
		    HashMap<String, Object> body = new HashMap<>();
		    body.put(Constant.Extra.PENSION_ID, id);
		    ServiceAdapter.getService(ServiceAdapter.APPARKAME).getPensionValidity(ApiSession.getAuthToken(), body)
				.enqueue(new Callback<GetPensionValidityResponse>()
				{
					  @Override
					  public void onResponse (@NonNull Call<GetPensionValidityResponse> call, @NonNull Response<GetPensionValidityResponse> response)
					  {
						    GetPensionValidityResponse validityResponse = response.body() == null ? new GetPensionValidityResponse(response) : response.body();
						    if (validityResponse.isSuccess())
						    {
								 lastError = null;
								 setUnit(validityResponse.getPensionValidity().getUnit());
								 dao.savePensionValidity(validityResponse.getPensionValidity());
								 model.setValue(validityResponse.getPensionValidity());
						    }
						    else
						    {
								 lastError = validityResponse;
								 model.setValue(dao.getPensionValidity());
						    }
					  }

					  @Override
					  public void onFailure (@NonNull Call<GetPensionValidityResponse> call, @NonNull Throwable t)
					  {
						    lastError = RetrofitErrorHandler.parseError(getApplication(), t);
						    model.setValue(null);
					  }
				});
	  }

	  public MutableLiveData<SimpleResponse> pay (String cardId, String pensionId, int userId)
	  {
		    HashMap<String, Object> body = new HashMap<>();

		    body.put(Constant.Extra.ID, userId);
		    body.put(Constant.Extra.CARD_TOKEN, cardId);
		    body.put(Constant.Extra.PENSION_ID, pensionId);

		    ServiceAdapter.getService(ServiceAdapter.APPARKAME).payPension(ApiSession.getAuthToken(), body)
				.enqueue(new Callback<SimpleResponse>()
				{
					  @Override
					  public void onResponse (@NonNull Call<SimpleResponse> call, @NonNull Response<SimpleResponse> response)
					  {
						    SimpleResponse paymentResponse = response.body() == null ?
								new SimpleResponse(response) : response.body();
						    if (paymentResponse.isSuccess())
						    {
								 lastError = null;
								 payment.setValue(paymentResponse);
						    }
						    else
						    {
								 lastError = paymentResponse;
								 payment.setValue(null);
						    }
					  }

					  @Override
					  public void onFailure (@NonNull Call<SimpleResponse> call, @NonNull Throwable t)
					  {
						    lastError = RetrofitErrorHandler.parseError(getApplication(), t);
						    model.setValue(null);
					  }
				});
		    return payment;
	  }
}
