package com.cdsautomatico.apparkame2.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import android.util.Log;

import com.cdsautomatico.apparkame2.api.ApiSession;
import com.cdsautomatico.apparkame2.dataSource.RetrofitErrorHandler;
import com.cdsautomatico.apparkame2.dataSource.ServiceAdapter;
import com.cdsautomatico.apparkame2.models.HistoryTicket;
import com.cdsautomatico.apparkame2.models.webServiceResponse.GetPeymentHistoryResponse;
import com.cdsautomatico.apparkame2.models.webServiceResponse.SimpleResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentViewModel extends AndroidViewModel
{
	  private MutableLiveData<List<HistoryTicket>> tickets;
	  private static final String TAG = "PaymentViewModel";

	  public PaymentViewModel (@NonNull Application application)
	  {
		    super(application);
		    tickets = new MutableLiveData<>();
	  }

	  public MutableLiveData<List<HistoryTicket>> getTickets ()
	  {
		    return tickets;
	  }

	  public void fetchPayments ()
	  {
		    ServiceAdapter.getService(ServiceAdapter.APPARKAME).getPaymentHistory(ApiSession.getAuthToken(), ApiSession.getUsuario().getId())
				.enqueue(new Callback<GetPeymentHistoryResponse>()
				{
					  @Override
					  public void onResponse (@NonNull Call<GetPeymentHistoryResponse> call, @NonNull Response<GetPeymentHistoryResponse> response)
					  {
						    GetPeymentHistoryResponse historyResponse = response.body() == null ?
								new GetPeymentHistoryResponse(response) : response.body();
						    if (historyResponse.isSuccess())
						    {
								 Log.i(TAG, "onResponse: SimpleResponse : OK");
								 tickets.setValue(historyResponse.getTickets());
						    }
						    else
						    {
								 Log.e(TAG, "onResponse: SimpleResponse : " + historyResponse.getMessage());
								 tickets.setValue(null);
						    }
					  }

					  @Override
					  public void onFailure (@NonNull Call<GetPeymentHistoryResponse> call, @NonNull Throwable t)
					  {
						    SimpleResponse response = RetrofitErrorHandler.parseError(getApplication(), t);
						    Log.e(TAG, "onResponse: SimpleResponse : " + response.getMessage());
						    tickets.setValue(null);
					  }
				});
	  }

}
