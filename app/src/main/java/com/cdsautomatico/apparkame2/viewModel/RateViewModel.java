package com.cdsautomatico.apparkame2.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.cdsautomatico.apparkame2.api.ApiSession;
import com.cdsautomatico.apparkame2.dataSource.RetrofitErrorHandler;
import com.cdsautomatico.apparkame2.dataSource.ServiceAdapter;
import com.cdsautomatico.apparkame2.dataSource.SocketConsumer;
import com.cdsautomatico.apparkame2.models.ApparkameRateUpdated;
import com.cdsautomatico.apparkame2.models.Commission;
import com.cdsautomatico.apparkame2.models.SocketMessage;
import com.cdsautomatico.apparkame2.models.enums.MessageType;
import com.cdsautomatico.apparkame2.models.rate.Rate;
import com.cdsautomatico.apparkame2.models.webServiceResponse.GetRateResponse;
import com.cdsautomatico.apparkame2.utils.Constant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateViewModel extends AndroidViewModel
{
	  public static final String TAG = "RateViewModel";
	  private MutableLiveData<Rate> activeRate;
	  private List<Commission> commissions;
	  private BroadcastReceiver receiver = new BroadcastReceiver()
	  {
		    @Override
		    public void onReceive (Context context, Intent intent)
		    {
				 Bundle extras = intent.getExtras();
				 if (extras == null)
					   return;
				 MessageType type = (MessageType) extras.getSerializable(Constant.Extra.TYPE);
				 Object dataObject = extras.get(Constant.Extra.DATA);
				 SocketMessage<?> socketMessage = null;
				 if (dataObject instanceof Parcelable)
					   socketMessage = extras.getParcelable(Constant.Extra.DATA);
				 if (type == null || socketMessage == null)
					   return;
				 if (type == MessageType.ApparkameRateUpdated)
				 {
					   ApparkameRateUpdated rateUpdated = (ApparkameRateUpdated) socketMessage.getValue();
					   if (rateUpdated != null)
							activeRate.setValue(rateUpdated.getRate());
				 }
		    }
	  };

	  public RateViewModel (@NonNull Application application)
	  {
		    super(application);
		    activeRate = new MutableLiveData<>();
	  }

	  public void register (Context ctx)
	  {
		    LocalBroadcastManager.getInstance(ctx).registerReceiver(receiver, new IntentFilter(SocketConsumer.SOCKET_MESSAGE));
	  }

	  public void unregister (Context ctx)
	  {
		    LocalBroadcastManager.getInstance(ctx).unregisterReceiver(receiver);
	  }

	  public MutableLiveData<Rate> getActiveRate ()
	  {
		    return activeRate;
	  }

	  public List<Commission> getCommissions ()
	  {
		    return commissions;
	  }

	  public void loadRate (int cdsRateId, int parkingId)
	  {
		    ServiceAdapter.getService(ServiceAdapter.APPARKAME).getRate(ApiSession.getAuthToken(), cdsRateId, parkingId)
				.enqueue(new Callback<GetRateResponse>()
				{
					  @Override
					  public void onResponse (@NonNull Call<GetRateResponse> call, @NonNull Response<GetRateResponse> response)
					  {
						    GetRateResponse rateResponse = response.body() == null ?
								new GetRateResponse(response) : response.body();
						    commissions = rateResponse.getCommissions();
						    activeRate.setValue(rateResponse.getRate());
						    if (rateResponse.isSuccess())
						    {
								 Log.i(TAG, "onResponse: SimpleResponse : OK");
						    }
						    else
						    {
								 Log.e(TAG, "onResponse: SimpleResponse : " + rateResponse.getMessage());
						    }
					  }

					  @Override
					  public void onFailure (@NonNull Call<GetRateResponse> call, @NonNull Throwable t)
					  {
						    GetRateResponse response = (GetRateResponse) RetrofitErrorHandler.parseError(getApplication(), t, new GetRateResponse());
						    Log.e(TAG, "onResponse: GetRateResponse : " + response.getMessage());
						    activeRate.setValue(null);
					  }
				});
	  }
}
