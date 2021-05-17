package com.cdsautomatico.apparkame2.viewModel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.util.Log;

import com.cdsautomatico.apparkame2.api.ApiSession;
import com.cdsautomatico.apparkame2.dataSource.RetrofitErrorHandler;
import com.cdsautomatico.apparkame2.dataSource.ServiceAdapter;
import com.cdsautomatico.apparkame2.dataSource.SocketConsumer;
import com.cdsautomatico.apparkame2.models.AlertMessage;
import com.cdsautomatico.apparkame2.models.Boleto;
import com.cdsautomatico.apparkame2.models.Commission;
import com.cdsautomatico.apparkame2.models.SocketMessage;
import com.cdsautomatico.apparkame2.models.TicketChange;
import com.cdsautomatico.apparkame2.models.TicketDebit;
import com.cdsautomatico.apparkame2.models.enums.MessageType;
import com.cdsautomatico.apparkame2.models.enums.TimeUnits;
import com.cdsautomatico.apparkame2.models.rate.Rate;
import com.cdsautomatico.apparkame2.models.webServiceResponse.GetTicketResponse;
import com.cdsautomatico.apparkame2.models.webServiceResponse.SimpleResponse;
import com.cdsautomatico.apparkame2.utils.Constant;
import com.cdsautomatico.apparkame2.utils.MathUtils;
import com.cdsautomatico.apparkame2.utils.dateTime.Date;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketViewModel extends AndroidViewModel
{
       private static final String TAG = "TicketViewModel";
       private MutableLiveData<Boleto> ticket;
       private MutableLiveData<String> alertMessage;
       private MutableLiveData<TicketDebit> ticketDebit;
       private Rate rate;
       private boolean calculateAmount = false;
       private Handler handler = new Handler();
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
                     switch (type)
                     {
                            case Alert:
                                   AlertMessage message = (AlertMessage) socketMessage.getValue();
                                   if (message != null)
                                          alertMessage.setValue(message.getMessage());
                                   break;
                            case TicketChange:
                                   TicketChange change = (TicketChange) socketMessage.getValue();
                                   if (change != null && change.getTicket().getActivo() != -1)
                                          ticket.setValue(change.getTicket());
                                   break;
                            case ApparkameRateUpdated:
                                   //ApparkameRateUpdated rateUpdated = (ApparkameRateUpdated) socketMessage.getValue();
                                   //if (rateUpdated != null)
                                   break;
                     }
              }
       };

       public TicketViewModel (@NonNull Application application)
       {
              super(application);
              ticket = new MutableLiveData<>();
              alertMessage = new MutableLiveData<>();
              ticketDebit = new MutableLiveData<>();
       }

       public void register (Context ctx)
       {
              LocalBroadcastManager.getInstance(ctx).registerReceiver(receiver, new IntentFilter(SocketConsumer.SOCKET_MESSAGE));
       }

       public void unregister (Context ctx)
       {
              LocalBroadcastManager.getInstance(ctx).unregisterReceiver(receiver);
       }

       public MutableLiveData<Boleto> getTicket ()
       {
              return ticket;
       }

       public MutableLiveData<TicketDebit> getTtcketDebit ()
       {
              return ticketDebit;
       }

       public void startDebitTimer ()
       {
              if (rate == null || ticket.getValue() == null)
                     return;
              calculateAmount = true;
              new Thread(() ->
              {
                     while (calculateAmount)
                     {
                            try
                            {
                                   int addSeconds = 2;
                                   Thread.sleep(addSeconds * 1000);
                                   if (ticketDebit.getValue() != null)
                                   {
                                          TicketDebit debit = ticketDebit.getValue();
                                          long newTime = debit.getTime() + addSeconds;
                                          debit.setTime(newTime);
                                          //System.out.println("New Time : " + newTime);
                                          if (newTime > 0 && newTime % 60 == 0)
                                          {
                                                 Log.i(TAG, "startDebitTimer: Going to calculate with " + newTime + " seconds");
                                                 calculateAmount();
                                          }
                                          else
                                          {
                                                 handler.post(() -> ticketDebit.setValue(debit));
                                          }
                                   }
                            }
                            catch (Exception ex)
                            {
                                   ex.printStackTrace();
                            }
                     }
              }).start();
       }

       private void calculateAmount ()
       {
              if (ticket.getValue() == null || ticketDebit.getValue() == null) return;
              Boleto activeTicket = ticket.getValue();
              TicketDebit debit = ticketDebit.getValue();
              final Date startTime = new Date(activeTicket.getHoraEntrada());
              try
              {
                     if (activeTicket.getSegundosPendientes() > 0)
                     {
                            Date endTime = new Date(activeTicket.getHoraEntrada());
                            int minutes = MathUtils.upperRound(debit.getTime() / 60);
                            endTime.addMinutes(minutes);
                            debit.setAmount(rate.calculateCharge(TimeUnits.minutes, minutes, startTime.toString(), endTime.toString()));
                     }
                     else
                     {
                            debit.setAmount(0);
                     }
                     handler.post(() -> ticketDebit.setValue(debit));
              }
              catch (Exception e)
              {
                     e.printStackTrace();
              }
       }

       public void startDebitCounter (@Nullable Rate rate, List<Commission> commissions)
       {
              if (rate != null)
                     this.rate = rate;
              if (ticket.getValue() == null) return;
              Boleto activeTicket = ticket.getValue();
              final Date startTime = new Date(activeTicket.getHoraBalance() == null || activeTicket.getHoraBalance().startsWith("000") ?
                    activeTicket.getHoraEntrada() : activeTicket.getHoraBalance());
              TicketDebit debit = ticketDebit.getValue();
              try
              {
                     if (debit == null || debit.getTime() != activeTicket.getSegundosPendientes())
                     {
                            debit = new TicketDebit();
                            if (commissions != null)
                                   debit.setCommissions(commissions);
                            if (activeTicket.getSegundosPendientes() == 0)
                            {
                                   debit.setAmount(this.rate.getCategories().get(0).getSchedules().get(0).getRules().get(0).getPrice());
                            }
                            else if (activeTicket.getSegundosPendientes() > 0)
                            {
                                   Date endTime = new Date(startTime.toString());
                                   int minutes = MathUtils.upperRound((double) activeTicket.getSegundosPendientes() / 60);
                                   endTime.addMinutes(minutes);
                                   debit.setAmount(this.rate.calculateCharge(TimeUnits.minutes, minutes, startTime.toString(), endTime.toString()));
                            }
                            else
                            {
                                   debit.setAmount(0);
                            }
                            debit.setTime(activeTicket.getSegundosPendientes());
                     }
              }
              catch (Exception e)
              {
                     e.printStackTrace();
              }
              ticketDebit.setValue(debit);
              if (!calculateAmount)
              {
                     calculateAmount = true;
                     handler.post(this::startDebitTimer);
              }
		    /*new Thread(() ->
		    {
				 while (calculateAmount)
				 {
					   try
					   {
							TicketDebit debit = ticketDebit.getValue();
							if (debit == null)
							{
								  debit = new TicketDebit();
								  if (activeTicket.getSegundosPendientes() == 0)
									    debit.setAmount(rate.getCategories().get(0).getSchedules().get(0).getRules().get(0).getPrice());
								  else if (activeTicket.getSegundosPendientes() > 0)
								  {
									    Date endTime = new Date(activeTicket.getHoraEntrada());
									    endTime.addMinutes((int) activeTicket.getSegundosPendientes());
									    debit.setAmount(rate.calculateCharge(TimeUnits.minutes, (int) activeTicket.getSegundosPendientes(), startTime.toString(), endTime.toString()));
								  }
								  else
								  {
									    debit.setAmount(0);
								  }
								  debit.setTime(activeTicket.getSegundosPendientes() * 60);
								  handler.post(this::startDebitTimer);
							}
							else
								  Thread.sleep(60 * 1000);
					   }
					   catch (Exception e)
					   {
							e.printStackTrace();
					   }
				 }
		    }).start();*/
       }

       public MutableLiveData<String> getAlertMessage ()
       {
              return alertMessage;
       }

       public void getActiveTicket ()
       {
              ServiceAdapter.getService(ServiceAdapter.APPARKAME).getActiveTicket(ApiSession.getAuthToken(), ApiSession.getUsuario().getId().toString())
                    .enqueue(new Callback<GetTicketResponse>()
                    {
                           @Override
                           public void onResponse (@NonNull Call<GetTicketResponse> call, @NonNull Response<GetTicketResponse> response)
                           {
                                  GetTicketResponse ticketResponse = response.body() == null ?
                                        new GetTicketResponse(response) : response.body();
                                  if (ticketResponse.isSuccess())
                                  {
                                         Boleto activeTicket = ticketResponse.getTicket();
                                         activeTicket.setSegundosPendientes(ticketResponse.getElapsedMinutes() * 60);
                                         ticket.setValue(activeTicket);
                                  }
                                  else
                                  {
                                         ticket.setValue(null);
                                  }
                           }

                           @Override
                           public void onFailure (@NonNull Call<GetTicketResponse> call, @NonNull Throwable t)
                           {
                                  SimpleResponse response = RetrofitErrorHandler.parseError(getApplication(), t);
                                  Log.e(TAG, "onResponse: SimpleResponse : " + response.getMessage());
                                  ticket.setValue(null);
                           }
                    });
       }

       public void requestEntry (int terminalId)
       {
              HashMap<String, Object> body = new HashMap<>();
              body.put(Constant.Extra.USER, ApiSession.getUsuario().getId());
              body.put(Constant.Extra.TERMINAL, terminalId);
              ServiceAdapter.getService(ServiceAdapter.APPARKAME).entryRequest(ApiSession.getAuthToken(), body)
                    .enqueue(new Callback<SimpleResponse>()
                    {
                           @Override
                           public void onResponse (@NonNull Call<SimpleResponse> call, @NonNull Response<SimpleResponse> response)
                           {
                                  SimpleResponse simpleResponse = response.body() == null ?
                                        new SimpleResponse(response) : response.body();
                                  if (simpleResponse.isSuccess())
                                  {
                                         Log.i(TAG, "onResponse: SimpleResponse : OK");
                                  }
                                  else
                                  {
                                         Log.e(TAG, "onResponse: SimpleResponse : " + simpleResponse.getMessage());
                                  }
                           }

                           @Override
                           public void onFailure (@NonNull Call<SimpleResponse> call, @NonNull Throwable t)
                           {
                                  SimpleResponse response = RetrofitErrorHandler.parseError(getApplication(), t);
                                  Log.e(TAG, "onResponse: SimpleResponse : " + response.getMessage());
                                  ticket.setValue(null);
                           }
                    });
       }

       public void requestExit (int terminalId)
       {
              HashMap<String, Object> body = new HashMap<>();
              body.put(Constant.Extra.USER, ApiSession.getUsuario().getId());
              body.put(Constant.Extra.TERMINAL, terminalId);
              ServiceAdapter.getService(ServiceAdapter.APPARKAME).exitRequest(ApiSession.getAuthToken(), body)
                    .enqueue(new Callback<SimpleResponse>()
                    {
                           @Override
                           public void onResponse (@NonNull Call<SimpleResponse> call, @NonNull Response<SimpleResponse> response)
                           {
                                  SimpleResponse simpleResponse = response.body() == null ?
                                        new SimpleResponse(response) : response.body();
                                  if (simpleResponse.isSuccess())
                                  {
                                         Log.i(TAG, "onResponse: SimpleResponse : OK");
                                  }
                                  else
                                  {
                                         Log.e(TAG, "onResponse: SimpleResponse : " + simpleResponse.getMessage());
                                  }
                           }

                           @Override
                           public void onFailure (@NonNull Call<SimpleResponse> call, @NonNull Throwable t)
                           {
                                  SimpleResponse response = RetrofitErrorHandler.parseError(getApplication(), t);
                                  Log.e(TAG, "onResponse: SimpleResponse : " + response.getMessage());
                                  ticket.setValue(null);
                           }
                    });
       }

       public void requestPayment (String cardId)
       {
              HashMap<String, Object> body = new HashMap<>();
              body.put(Constant.Extra.CARD_ID, cardId);
              body.put(Constant.Extra.USER, ApiSession.getUsuario().getId());
              ServiceAdapter.getService(ServiceAdapter.APPARKAME).payRequest(ApiSession.getAuthToken(), body)
                    .enqueue(new Callback<SimpleResponse>()
                    {
                           @Override
                           public void onResponse (@NonNull Call<SimpleResponse> call, @NonNull Response<SimpleResponse> response)
                           {
                                  SimpleResponse simpleResponse = response.body() == null ?
                                        new SimpleResponse(response) : response.body();
                                  if (simpleResponse.isSuccess())
                                  {
                                         Log.i(TAG, "onResponse: SimpleResponse : OK");
                                  }
                                  else
                                  {
                                         Log.e(TAG, "onResponse: SimpleResponse : " + simpleResponse.getMessage());
                                         getActiveTicket();
                                  }
                           }

                           @Override
                           public void onFailure (@NonNull Call<SimpleResponse> call, @NonNull Throwable t)
                           {
                                  SimpleResponse response = RetrofitErrorHandler.parseError(getApplication(), t);
                                  Log.e(TAG, "onResponse: SimpleResponse : " + response.getMessage());
                                  ticket.setValue(null);
                           }
                    });
       }

       public void stopDebitTimer ()
       {
              calculateAmount = false;
       }

       public void setElapsedTime (long elapsedSeconds)
       {
              TicketDebit debit = ticketDebit.getValue();
              if (debit == null)
              {
                     debit = new TicketDebit();
              }
              debit.setTime(elapsedSeconds);
              Boleto activeTicket = ticket.getValue();
              if (activeTicket != null && (activeTicket.getPensionId() == null || activeTicket.getPensionId() > 0))
              {
                     calculateAmount();
              }
              else if (ticketDebit.getValue() == null)
              {
                     ticketDebit.setValue(debit);
              }
       }
}
