package com.cdsautomatico.apparkame2.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import android.content.Intent;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.cdsautomatico.apparkame2.api.ApiSession;
import com.cdsautomatico.apparkame2.dataSource.RetrofitErrorHandler;
import com.cdsautomatico.apparkame2.dataSource.ServiceAdapter;
import com.cdsautomatico.apparkame2.dataSource.SocketConsumer;
import com.cdsautomatico.apparkame2.models.Parking;
import com.cdsautomatico.apparkame2.models.Terminal;
import com.cdsautomatico.apparkame2.models.enums.MessageType;
import com.cdsautomatico.apparkame2.models.enums.TerminalType;
import com.cdsautomatico.apparkame2.models.webServiceResponse.GetParkingListResponse;
import com.cdsautomatico.apparkame2.models.webServiceResponse.SimpleResponse;
import com.cdsautomatico.apparkame2.utils.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ParkingViewModel extends AndroidViewModel
{
	  private MutableLiveData<Parking> activeParking;
	  private MutableLiveData<List<Parking>> parkingList;
	  private MutableLiveData<List<Terminal>> availableTerminals;
	  private List<Terminal> accessControlTerminals;
	  private List<Terminal> entryTerminals;
	  private List<Terminal> exitTerminals;
	  private Handler handler = new Handler();

	  public ParkingViewModel (@NonNull Application application)
	  {
		    super(application);
		    parkingList = new MutableLiveData<>();
		    activeParking = new MutableLiveData<>();
		    availableTerminals = new MutableLiveData<>();
	  }

	  public MutableLiveData<Parking> getActiveParking ()
	  {
		    return activeParking;
	  }

	  public MutableLiveData<List<Terminal>> getAvailableTerminals ()
	  {
		    return availableTerminals;
	  }

	  public MutableLiveData<List<Parking>> getParkingList ()
	  {
		    return parkingList;
	  }

	  public List<Terminal> getAccessControlTerminals ()
	  {
		    return accessControlTerminals;
	  }

	  public List<Terminal> getEntryTerminals ()
	  {
		    return entryTerminals;
	  }

	  public List<Terminal> getExitTerminals ()
	  {
		    return exitTerminals;
	  }

	  public boolean hasAntiPassPassBack ()
	  {
		    if (accessControlTerminals == null || accessControlTerminals.size() == 0)
				 return true;
		    for (int i = 0; i < accessControlTerminals.size(); i++)
		    {
				 if (accessControlTerminals.get(i).isAntiPassBack())
					   return true;
		    }
		    return false;
	  }

	  public void loadParkingList ()
	  {
		    ServiceAdapter.getService(ServiceAdapter.APPARKAME).getParkingList(ApiSession.getAuthToken(), ApiSession.getUsuario().getId())
				.enqueue(new Callback<GetParkingListResponse>()
				{
					  @Override
					  public void onResponse (@NonNull Call<GetParkingListResponse> call, @NonNull Response<GetParkingListResponse> response)
					  {
						    GetParkingListResponse getParkingListResponse = response.body() != null ? response.body() :
								new GetParkingListResponse(response);
						    if (getParkingListResponse.isSuccess())
						    {
								 parkingList.setValue(getParkingListResponse.getParkingList());
						    }
						    else
						    {
								 parkingList.setValue(null);
								 sendAlert(getParkingListResponse.getMessage());
						    }
					  }

					  @Override
					  public void onFailure (@NonNull Call<GetParkingListResponse> call, @NonNull Throwable t)
					  {
						    SimpleResponse simpleResponse = RetrofitErrorHandler.parseError(getApplication(), t);
						    sendAlert(simpleResponse.getMessage());
					  }
				});
	  }

	  private void sendAlert (String alert)
	  {
		    Intent intent = new Intent();
		    intent.setAction(SocketConsumer.SOCKET_MESSAGE);
		    intent.putExtra(Constant.Extra.TYPE, MessageType.Alert);
		    intent.putExtra(Constant.Extra.DATA, alert);
		    LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(intent);
	  }

	  public void setDataFromBeaconScann (List<Terminal> availableTerminals)
	  {
		    if(availableTerminals!=null){
	  	    List<Terminal> filteredTerminals = new ArrayList<>();
		    accessControlTerminals = new ArrayList<>();
		    entryTerminals = new ArrayList<>();
		    exitTerminals = new ArrayList<>();
		    for (int i = 0; i < availableTerminals.size(); i++)
		    {
				 Terminal terminal = availableTerminals.get(i);
				 if (terminal.getParking().isAccessControl())//|| !terminal.isAntiPassBack())
				 {
					   accessControlTerminals.add(terminal);
				 }
				 if (terminal.getTerminalType().equals(TerminalType.Salida))
				 {
					   filteredTerminals.add(terminal);
					   exitTerminals.add(terminal);
				 }
				 else if (terminal.getTerminalType().equals(TerminalType.Entrada))
				 {
					   filteredTerminals.add(terminal);
					   entryTerminals.add(terminal);
				 }
		    }
		    handler.post(() -> this.availableTerminals.setValue(filteredTerminals));
		    }else{
		    	  if(entryTerminals!=null)
		    	  	entryTerminals.clear();
		    	  if(exitTerminals!=null)
		    	  	  exitTerminals.clear();
		    	  if(accessControlTerminals!=null)
		    	  	  accessControlTerminals.clear();
		    }
	  }
}
