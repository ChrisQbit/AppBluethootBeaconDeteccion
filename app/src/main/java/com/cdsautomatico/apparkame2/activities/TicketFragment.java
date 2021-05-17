package com.cdsautomatico.apparkame2.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cdsautomatico.apparkame2.BuildConfig;
import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.api.ApiHelper;
import com.cdsautomatico.apparkame2.models.Boleto;
import com.cdsautomatico.apparkame2.models.ConektaTarjeta;
import com.cdsautomatico.apparkame2.models.Parking;
import com.cdsautomatico.apparkame2.models.Terminal;
import com.cdsautomatico.apparkame2.models.enums.TerminalType;
import com.cdsautomatico.apparkame2.viewModel.ParkingViewModel;
import com.cdsautomatico.apparkame2.viewModel.RateViewModel;
import com.cdsautomatico.apparkame2.viewModel.TicketViewModel;
import com.cdsautomatico.apparkame2.views.ApparkameLoadingView;
import com.cdsautomatico.apparkame2.views.ApparkameTimerView;
import com.cdsautomatico.apparkame2.views.DialogoCarril;
import com.cdsautomatico.apparkame2.views.DialogoFormaPago;
import com.cdsautomatico.apparkame2.views.dialogFragment.CommissionsDialogFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import httprequest.HttpRequestCallback;
import httprequest.HttpRequestException;

public class TicketFragment extends Fragment implements /*BeaconConsumer, */View.OnClickListener
{
	  public static final int ETAPA_INACTIVO = 460; // no tiene boleto activo
	  private static final int ETAPA_SALDO = 578; // ingreso pero tiene saldo pendiente
	  private static final int ETAPA_PAGADO = 685; // el boleto esta pagado y puede salir

	  private ForegroundColorSpan redColor;
	  private ForegroundColorSpan greenColor;
	  private StyleSpan fontBold;

	  private static final String TAG = "TicketFragment";
	  private static final int REQUEST_EDIT_PAYMENT_METHODS = 864;

	  private ArrayList<ConektaTarjeta> tarjetas = new ArrayList<>();
	  private HashMap<String, Terminal> terminalesEntrada = new HashMap<>();
	  private HashMap<String, Terminal> terminalesSalida = new HashMap<>();
	  private Parking estacionamientoActivo;
	  private List<String> beaconsUUID = new ArrayList<>();

	  private View rootView;
	  private View btnContainer;
	  private TextView lblEstacionamientoNombre;
	  private TextView estacionamientoEspacios;
	  //private TextView estacionamientoTarifa;
//	  private TextView estacionamientoHorario;
	  private ViewGroup timeTrackingPanel;
	  private TextView boletoTiempo;
	  private TextView boletoTotal;
	  private TextView boletoMoneda;
	  private TextView tvComisiones;
	  //private View btnApparkame;
	  TextView txtHomeMessage;
	  private ApparkameTimerView boletoTimer;
	  private DialogoCarril dialogoCarril;
	  private DialogoFormaPago dialogFormaPago;
	  private ApparkameLoadingView apparkameLoadingView;
	  private View connectionError;

	  private Boleto boletoActivo;
	  private int boletoEtapa = ETAPA_INACTIVO;
	  private boolean activityInBackground = false;
	  private boolean estacionamientoInfoVisible = false;
	  private boolean btnApparkameBlock = false;
	  private View estacionamientoInfo;
	  private ImageView ivParkingImage;

	  private int colorGreen;

	  private TicketViewModel ticketViewModel;
	  private RateViewModel rateViewModel;
	  private ParkingViewModel parkingViewModel;

	  @Override
	  public void onCreate (Bundle savedInstanceState)
	  {
		    super.onCreate(savedInstanceState);
	  }

	  @Override
	  public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	  {
		    rootView = inflater.inflate(R.layout.fragment_ticket, container, false);
		    lblEstacionamientoNombre = rootView.findViewById(R.id.lblEstacionamientoNombre);
		    estacionamientoEspacios = rootView.findViewById(R.id.estacionamientoEspacios);
		    timeTrackingPanel = rootView.findViewById(R.id.timeTrackingPanel);
		    estacionamientoInfo = rootView.findViewById(R.id.estacionamientoInfo);
		    estacionamientoInfo.setVisibility(View.INVISIBLE);
//		    estacionamientoInfoBackColor = rootView.findViewById(R.id.estacionamientoInfoBackColor);
		    ivParkingImage = rootView.findViewById(R.id.parkingImg);

		    /*estacionamientoTarifa = rootView.findViewById(R.id.estacionamientoTarifa);
		    estacionamientoHorario = rootView.findViewById(R.id.estacionamientoHorario);*/
		    btnContainer = rootView.findViewById(R.id.btn_container);


		    redColor = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
		    greenColor = new ForegroundColorSpan(getResources().getColor(R.color.colorAccent));
		    fontBold = new StyleSpan(android.graphics.Typeface.BOLD);

		    txtHomeMessage = rootView.findViewById(R.id.homeMessage);
		    View btnApparkame = rootView.findViewById(R.id.btnApparkame);
		    boletoTimer = rootView.findViewById(R.id.boletoTimer);
		    boletoTiempo = rootView.findViewById(R.id.boletoTiempo);
		    boletoTotal = rootView.findViewById(R.id.boletoTotal);
		    boletoMoneda = rootView.findViewById(R.id.boletoMoneda);
		    dialogoCarril = rootView.findViewById(R.id.dialogoCarril);
		    dialogFormaPago = rootView.findViewById(R.id.dialogFormaPago);
		    tvComisiones = rootView.findViewById(R.id.comisiones);
		    apparkameLoadingView = rootView.findViewById(R.id.apparkameLoading);
		    connectionError = rootView.findViewById(R.id.connectionError);

		    if (getActivity() == null) return null;
		    btnApparkame.setOnClickListener(this);
		    rootView.findViewById(R.id.estacionamientoInfo).setOnClickListener(this);
		    ticketViewModel = ViewModelProviders.of(getActivity()).get(TicketViewModel.class);
		    rateViewModel = ViewModelProviders.of(this).get(RateViewModel.class);
		    parkingViewModel = ViewModelProviders.of(getActivity()).get(ParkingViewModel.class);

		    dialogFormaPago.setOnAgregarFormaPago(() ->
		    {
				 Intent intent = new Intent(getActivity(), FormasPagoAgregarActivity.class);
				 startActivityForResult(intent, REQUEST_EDIT_PAYMENT_METHODS);
		    });

		    loadEstacionamientos();
		    loadTarjetas();

		    initApparkameButtonAnim();
		    setBoletoActivo(null);

		    colorGreen = getResources().getColor(R.color.colorGreen);
		    ticketViewModel.getTicket().observe(getViewLifecycleOwner(), ticket ->
		    {
				 if (ticket != null)
				 {
					   refreshTicket(ticket);
				 }
				 else if (getActivity() != null)
				 {
					   setBoletoActivo(null);
					   btnApparkameBlock = false;
				 }
		    });
		    ticketViewModel.getAlertMessage().observe(getViewLifecycleOwner(), message ->
		    {
				 if (message != null)
				 {
					   btnApparkameBlock = false;
					   if (getActivity() != null)
					   {
							apparkameLoadingView.stopAnimation();
							boletoTimer.setVisibility(View.VISIBLE);
							txtHomeMessage.setVisibility(View.VISIBLE);
					   }
					   showDialogoMensaje("Aviso", message, R.drawable.ic_warning);
					   ticketViewModel.startDebitTimer();
				 }
		    });
		    ticketViewModel.getTtcketDebit().observe(getViewLifecycleOwner(), debit ->
		    {
				 if (debit != null)
				 {
					   showBoletoTotalTiempo(debit.getTime(), debit.getAmount());
				 }
		    });
		    rateViewModel.getActiveRate().observe(getViewLifecycleOwner(), rate ->
		    {
				 if (rate != null)
				 {
					   ticketViewModel.startDebitCounter(rate, rateViewModel.getCommissions());
				 }
		    });
		    parkingViewModel.getParkingList().observe(getViewLifecycleOwner(), parkings ->
		    {
				 if (parkings != null)
				 {
					   fillTerminals(parkings);
				 }
		    });
		    parkingViewModel.getAvailableTerminals().observe(getViewLifecycleOwner(), terminals ->
		    {
				 if (terminals != null)
				 {
					   updateHomeMessage(terminals);
				 }
		    });
		    tvComisiones.setOnClickListener(this);

		    return rootView;
	  }

	  @Override
	  public void onResume ()
	  {
		    super.onResume();
		    activityInBackground = false;
		    if (ticketViewModel != null)
		    {
				 ticketViewModel.register(getContext());
				 ticketViewModel.getActiveTicket();
		    }
		    if (rateViewModel != null)
				 rateViewModel.register(getContext());
	  }

	  @Override
	  public void onPause ()
	  {
		    if (ticketViewModel != null)
				 ticketViewModel.unregister(getContext());
		    if (rateViewModel != null)
				 rateViewModel.unregister(getContext());
		    super.onPause();
		    activityInBackground = true;
	  }

	  public void updateHomeMessage (List<Terminal> availableTerminals)
	  {
		    if (getActivity() == null)
		    {
				 return;
		    }

		    SpannableString spannableString = null;

		    if (ticketViewModel.getTicket().getValue() == null || ticketViewModel.getTicket().getValue().getActivo() != 1)
		    {
				 if (availableTerminals == null || availableTerminals.size() == 0 || (parkingViewModel.hasAntiPassPassBack() && parkingViewModel.getEntryTerminals().size() == 0))
				 {
					   spannableString = showNoEntryNear();
				 }
				 else if (parkingViewModel.getEntryTerminals().size() > 0 && (parkingViewModel.getExitTerminals().size() == 0 || parkingViewModel.hasAntiPassPassBack()))
				 {
					   spannableString = showTapToEnter();
				 }
				 else if (parkingViewModel.getExitTerminals().size() > 0 && (parkingViewModel.hasAntiPassPassBack() || parkingViewModel.getEntryTerminals().size() == 0))
				 {
					   spannableString = showTapToExit();
				 }
				 else
				 {
					   spannableString = showTapToOpen();
				 }
		    }
		    else if (ticketViewModel.getTicket().getValue() != null && ticketViewModel.getTicket().getValue().getSegundosPendientes() > 0 && ((ticketViewModel.getTicket().getValue().getPensionId() != null && ticketViewModel.getTicket().getValue().getPensionId() == 0) || ticketViewModel.getTicket().getValue().getPensionId() == null))
		    {
				 boletoTimer.setVisibility(View.VISIBLE);
				 spannableString = showTapToPay();
		    }
		    else if (boletoEtapa == ETAPA_PAGADO)
		    {
				 boletoTimer.setVisibility(View.VISIBLE);
				 if (availableTerminals == null || availableTerminals.size() == 0)
				 {
					   spannableString = showNoExitNear();
				 }
				 else
				 {
					   spannableString = showTapToExit();
				 }
		    }
		    setEstacionamientoActivo(availableTerminals != null && availableTerminals.size() > 0 ?
				availableTerminals.get(0).getParking() : null);
		    txtHomeMessage.setText(spannableString);
	  }

	  private SpannableString showNoExitNear ()
	  {
		    SpannableString spannableString = new SpannableString("APROXÍMATE A LA SALIDA DEL ESTACIONAMIENTO");
		    spannableString.setSpan(greenColor, 16, 22, 0);
		    spannableString.setSpan(fontBold, 16, 42, 0);
		    return spannableString;
	  }

	  private SpannableString showTapToPay ()
	  {
		    SpannableString spannableString = new SpannableString("TOCA PARA PAGAR");
		    spannableString.setSpan(redColor, 10, 15, 0);
		    spannableString.setSpan(fontBold, 10, 15, 0);
		    return spannableString;
	  }

	  private SpannableString showTapToExit ()
	  {
		    SpannableString spannableString = new SpannableString("TOCA PARA SALIR");
		    spannableString.setSpan(redColor, 10, 15, 0);
		    spannableString.setSpan(fontBold, 10, 15, 0);
		    return spannableString;
	  }

	  private SpannableString showNoEntryNear ()
	  {
		    SpannableString spannableString = new SpannableString("APROXÍMATE A LA ENTRADA DEL ESTACIONAMIENTO");
		    spannableString.setSpan(redColor, 16, 23, 0);
		    spannableString.setSpan(fontBold, 16, 43, 0);
		    return spannableString;
	  }

	  private SpannableString showTapToEnter ()
	  {
		    SpannableString spannableString = new SpannableString("TOCA PARA ACCESAR");
		    spannableString.setSpan(redColor, 10, 17, 0);
		    spannableString.setSpan(fontBold, 10, 17, 0);
		    return spannableString;
	  }

	  private SpannableString showTapToOpen ()
	  {
		    SpannableString spannableString = new SpannableString("TOCA PARA ABRIR");
		    spannableString.setSpan(redColor, 10, 15, 0);
		    spannableString.setSpan(fontBold, 10, 15, 0);
		    return spannableString;
	  }

	  private void initApparkameButtonAnim ()
	  {
		    FrameLayout button = rootView.findViewById(R.id.btnApparkame);

		    final AnimatorSet animatorSet = new AnimatorSet();

		    ObjectAnimator animator1 = ObjectAnimator.ofFloat(button, "scaleX", 0.95f);
		    animator1.setRepeatCount(0);
		    animator1.setDuration(1000);

		    ObjectAnimator animator2 = ObjectAnimator.ofFloat(button, "scaleX", 1f);
		    animator2.setRepeatCount(0);
		    animator2.setDuration(1000);

		    ObjectAnimator animator3 = ObjectAnimator.ofFloat(button, "scaleY", 0.95f);
		    animator3.setRepeatCount(0);
		    animator3.setDuration(1000);

		    ObjectAnimator animator4 = ObjectAnimator.ofFloat(button, "scaleY", 1f);
		    animator4.setRepeatCount(0);
		    animator4.setDuration(1000);

		    animatorSet.play(animator1).before(animator2);
		    animatorSet.play(animator3).before(animator4);

		    final Timer timer = new Timer();
		    timer.scheduleAtFixedRate(new TimerTask()
		    {
				 @Override
				 public void run ()
				 {
					   if (getActivity() == null)
					   {
							timer.cancel();
					   }
					   else if (!activityInBackground)
					   {
							getActivity().runOnUiThread(animatorSet::start);
					   }
				 }
		    }, 0, 2050);
	  }

	  public void setEstacionamientoActivo (final Parking estacionamiento)
	  {
		    if (getActivity() != null)
		    {
				 getActivity().runOnUiThread(() ->
				 {
					   if (estacionamientoActivo != estacionamiento)
					   {
							if (estacionamiento != null)
							{
								  lblEstacionamientoNombre.setText(estacionamiento.getName());
								  estacionamientoEspacios.setText("0");

								  /*if (estacionamiento.getPrimerTarifa() != null)
								  {
									    EstacionamientoTarifa primerTarifa = estacionamiento.getPrimerTarifa();

									    estacionamientoTarifa.setText((primerTarifa.getMinutos() / 60) + "hr - $" +
											(int) primerTarifa.getTarifa());
								  }
								  else
								  {
									    estacionamientoTarifa.setText("-");
								  }

								  //int weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
								  Schedule horario = estacionamiento.getSchedule();

								  if (horario != null)
								  {
									    estacionamientoHorario.setText(horario.getShowTime());
								  }*/

								  toggleEstacionamientoInfoContainer(true);

							}
							/*else
							{
								  //toggleEstacionamientoInfoContainer(false);
							}*/

							if (estacionamiento != null)
							{
								  Glide.with(getContext())
									   .load(BuildConfig.API_NETCORE + "/api/Apparkame/ParkingImage?parkingId=" + estacionamiento.getId())
									   .fitCenter()
									   .into(ivParkingImage);
							}

							estacionamientoActivo = estacionamiento;
					   }
				 });
		    }
	  }

	  private void toggleEstacionamientoInfoContainer (boolean visible)
	  {
		    if (estacionamientoInfoVisible == visible)
		    {
				 return;
		    }

		    estacionamientoInfo.setVisibility(View.VISIBLE);
		    DisplayMetrics metrics = getResources().getDisplayMetrics();
		    int hidePosition = (int) (metrics.density * 300f) * -1;

		    ObjectAnimator animator;
		    ObjectAnimator animator2;

		    if (visible)
		    {
				 animator = ObjectAnimator.ofFloat(estacionamientoInfo, "translationY", hidePosition, 0);
				 animator2 = ObjectAnimator.ofFloat(btnContainer, "translationY", 0, hidePosition / 2 * -1);
				 animator2.addListener(new Animator.AnimatorListener()
				 {
					   @Override
					   public void onAnimationStart (Animator animator)
					   {}

					   @Override
					   public void onAnimationEnd (Animator animator)
					   {
							btnContainer.setY(hidePosition / 2 * -1);
					   }

					   @Override
					   public void onAnimationCancel (Animator animator)
					   {

					   }

					   @Override
					   public void onAnimationRepeat (Animator animator)
					   {

					   }
				 });
				 estacionamientoInfoVisible = visible;
		    }
		    else
		    {
				 animator = ObjectAnimator.ofFloat(estacionamientoInfo, "translationY", 0, hidePosition);
				 animator2 = ObjectAnimator.ofFloat(btnContainer, "translationY", hidePosition / 2 * -1, 0);
				 animator2.addListener(new Animator.AnimatorListener()
				 {
					   @Override
					   public void onAnimationStart (Animator animator)
					   {}

					   @Override
					   public void onAnimationEnd (Animator animator)
					   {
							btnContainer.setY(0);
					   }

					   @Override
					   public void onAnimationCancel (Animator animator)
					   {

					   }

					   @Override
					   public void onAnimationRepeat (Animator animator)
					   {

					   }
				 });
				 estacionamientoInfoVisible = visible;
		    }

		    animator.setDuration(200);
		    animator.start();
		    animator2.setDuration(200);
		    animator2.start();
	  }

	  private void refreshTicket (Boleto ticket)
	  {
		    ApiHelper.boletoActivo(new HttpRequestCallback()
		    {
				 @Override
				 public void onSuccess (JSONObject json)
				 {
					   try
					   {
							connectionError.setVisibility(View.GONE);
							Log.i(TAG, "onSuccess PHP: " + json);
							if (json.getBoolean("success"))
							{
								  if (!json.isNull("boleto"))
								  {
									    Boleto boleto = new Boleto(json.getJSONObject("boleto"));
									    if (ticket != null)
									    {
											 ticket.setParking(boleto.getParking());
											 ticket.setEstacionamientoId(boleto.getEstacionamientoId());
									    }
									    setBoletoActivo(ticket == null ? boleto : ticket);
									    if (ticket != null && (ticket.getPensionId() == null || ticket.getPensionId() == 0))
									    {
											 ticketViewModel.setElapsedTime(ticket.getSegundosPendientes());
									    }
									    else
									    {
											 ticketViewModel.setElapsedTime(0);
											 showBoletoTotalTiempo(0, 0);
									    }
								  }
								  else
								  {
									    setBoletoActivo(null);
								  }
							}
							else
							{
								  Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_SHORT).show();
							}
					   }
					   catch (Exception e)
					   {
							if (getActivity() != null)
							{
								  connectionError.setVisibility(View.VISIBLE);
								  e.printStackTrace();
							}
					   }
				 }

				 @Override
				 public void onError (HttpRequestException e)
				 {
					   if (connectionError != null)
					   {
							e.printStackTrace();
							if (e.getHttpResponseText() != null)
							{
								  Log.e(TAG, e.getHttpResponseText());
							}
							connectionError.setVisibility(View.VISIBLE);
					   }
				 }
		    });
	  }

	  /*private void initBoletoActivoListener ()
	  {
		    new Timer().scheduleAtFixedRate(new TimerTask()
		    {
				 @Override
				 public void run ()
				 {

					   ApiHelper.boletoActivo(new HttpRequestCallback()
					   {
							@Override
							public void onSuccess (JSONObject json)
							{

								  try
								  {
									    connectionError.setVisibility(View.GONE);

									    if (json.getBoolean("success"))
									    {
											 if (!json.isNull("boleto"))
											 {
												   setBoletoActivo(new Boleto(json.getJSONObject("boleto")));
											 }
											 else
											 {
												   setBoletoActivo(null);
											 }
									    }
									    else
									    {
											 Toast.makeText(getActivity(), json.getString("message"), Toast.LENGTH_SHORT).show();
									    }
								  }
								  catch (Exception e)
								  {
									    if (getActivity() != null)
									    {
											 connectionError.setVisibility(View.VISIBLE);
											 e.printStackTrace();
									    }
								  }
							}

							@Override
							public void onError (HttpRequestException e)
							{
								  if (connectionError != null)
								  {
									    e.printStackTrace();
									    if (e.getHttpResponseText() != null)
									    {
											 Log.e(TAG, e.getHttpResponseText());
									    }
									    connectionError.setVisibility(View.VISIBLE);
								  }
							}
					   });

				 }
		    }, 0, 4000);
	  }*/

	  private void setBoletoActivo (Boleto boleto)
	  {
		    Boleto ultimoBoleto = boletoActivo;
		    boletoActivo = boleto;

		    btnApparkameBlock = false;
		    apparkameLoadingView.stopAnimation();
		    txtHomeMessage.setVisibility(View.VISIBLE);

		    if (boleto != null)
		    {
				 setEstacionamientoActivo(boleto.getParking());
				 timeTrackingPanel.setVisibility(View.VISIBLE);
				 rateViewModel.loadRate(boleto.getCdsRateId(), boleto.getEstacionamientoId());
				 try
				 {
					   if (boleto.getSegundosPendientes() >= 0)
					   {
							if (boleto.getPensionId() != null && boleto.getPensionId() != 0)
							{
								  boletoEtapa = ETAPA_PAGADO;
							}
							else
							{
								  boletoEtapa = ETAPA_SALDO;
							}
					   }
					   else
					   {
							boletoEtapa = ETAPA_PAGADO;
					   }

				 }
				 catch (Exception e)
				 {
					   e.printStackTrace();
				 }
		    }
		    else
		    {
				 boletoEtapa = ETAPA_INACTIVO;

				 timeTrackingPanel.setVisibility(View.GONE);

				 if (ultimoBoleto != null)
				 {
					   showDialogoMensaje("Boleto finalizado", "¡Vuelve pronto!", R.drawable.ic_logo_completo_claro);
				 }
		    }

		    //updateHomeMessage();
	  }

	  @SuppressLint("DefaultLocale")
	  private void showBoletoTotalTiempo (long time, double amount)
	  {
		    if (boletoActivo == null)
		    {
				 ticketViewModel.stopDebitTimer();
				 return;
		    }
		    try
		    {
				 if (time > 0)
				 {

					   if (boletoActivo.getPensionId() != null && boletoActivo.getPensionId() != 0)
					   {
							boletoTotal.setTextColor(colorGreen);
							boletoMoneda.setTextColor(colorGreen);
							boletoTimer.setStrokeColor(colorGreen);
							tvComisiones.setVisibility(View.GONE);

							boletoTotal.setText("0");
							boletoTimer.setProgress(0);
					   }
					   else
					   {
							tvComisiones.setVisibility(View.VISIBLE);
							float residuo = ((float) time) % 3600f;
							float progress = residuo / 3600f;
							boletoTimer.setClockwise(true);
							boletoTimer.setProgress(progress);

							int color = getResources().getColor(R.color.colorAccent);
							boletoTotal.setTextColor(color);
							boletoMoneda.setTextColor(color);
							boletoTimer.setStrokeColor(color);
							String monto = String.format("%.2f", amount);
							boletoTotal.setText(monto);
					   }
				 }
				 else if (time < 0 || (boletoActivo.getPensionId() != null && boletoActivo.getPensionId() > 0))
				 {
					   time = Math.abs(time);
					   //float segundosGracia = boletoActivo.getParking().getMinutosGracia() * 60;
					   float progress = time / 3600f;
					   tvComisiones.setVisibility(View.GONE);

					   boletoTimer.setClockwise(true);
					   boletoTimer.setProgress(progress);

					   boletoTotal.setTextColor(colorGreen);
					   boletoMoneda.setTextColor(colorGreen);
					   boletoTimer.setStrokeColor(colorGreen);
					   boletoTotal.setText("0");
				 }
				 else
				 {
					   ticketViewModel.getActiveTicket();
					   return;
				 }

				 long horas = time / 3600;
				 long minutos = (time % 3600) / 60;

				 final StyleSpan fontBold = new StyleSpan(android.graphics.Typeface.BOLD);
				 final StyleSpan fontBold2 = new StyleSpan(android.graphics.Typeface.BOLD);

				 SpannableString spannableString = new SpannableString(String.format("%02d", horas) + " hr " + String.format("%02d", minutos) + " min");
				 spannableString.setSpan(fontBold, 3, 5, 0);
				 spannableString.setSpan(fontBold2, 9, 12, 0);

				 boletoTiempo.setText(spannableString);
				 updateHomeMessage(parkingViewModel.getAvailableTerminals().getValue());
		    }
		    catch (Exception e)
		    {
				 e.printStackTrace();
		    }
	  }

	  private void fillTerminals (List<Parking> parkingList)
	  {
		    for (int i = 0; i < parkingList.size(); i++)
		    {
				 Parking parking = parkingList.get(i);

				 for (int j = 0; j < parking.getTerminals().size(); j++)
				 {
					   Terminal terminal = parking.getTerminals().get(j);
					   terminal.setParking(parking);
					   String cleanBeaconUUID = terminal.getBeaconUUID()
						    .replaceAll("-", "").toLowerCase();

					   if (terminal.getTerminalType().equals(TerminalType.Entrada))
					   {
							Log.v(TAG, "-- terminal entrada: " + terminal.getBeaconUUID());
							terminalesEntrada.put(cleanBeaconUUID, terminal);
					   }
					   else if (terminal.getTerminalType().equals(TerminalType.Salida))
					   {
							Log.v(TAG, "-- terminal salida: " + terminal.getBeaconUUID());
							terminalesSalida.put(cleanBeaconUUID, terminal);
					   }
				 }
		    }
	  }

	  private void loadEstacionamientos ()
	  {
		    Log.v(TAG, "-- estacionamientos solicitados -- (Deprecado)");
		    Log.v(TAG, "Cargando desde el poderosísimo C#");
		    parkingViewModel.loadParkingList();
		    /*ApiHelper.estacionamientos(new HttpRequestCallback()
		    {
				 @Override
				 public void onSuccess (JSONObject json)
				 {
					   try
					   {
							if (json.getBoolean("success"))
							{
								  Log.v(TAG, "-- estacionamientos cargados --");
								  JSONArray jsonEstacionamientos = json.getJSONArray("estacionamientos");

								  for (int i = 0; i < jsonEstacionamientos.length(); i++)
								  {
									    Estacionamiento estac = new Estacionamiento(jsonEstacionamientos.getJSONObject(i));

									    Log.d(TAG, "-- " + estac.getNombre() + "--");

									    for (EstacionamientoTerminal terminal : estac.getTerminales())
									    {
											 String cleanBeaconUUID = terminal.getBeaconUUID()
												  .replaceAll("-", "").toLowerCase();

											 if (terminal.getType() == 1)
											 { //entrada
												   Log.v(TAG, "-- terminal entrada: " + terminal.getBeaconUUID());
												   terminalesEntrada.put(cleanBeaconUUID, terminal);
											 }
											 else if (terminal.getType() == 2)
											 {
												   Log.v(TAG, "-- terminal salida: " + terminal.getBeaconUUID());
												   terminalesSalida.put(cleanBeaconUUID, terminal);
											 }
									    }
								  }
							}
							else
							{
								  showDialogoMensaje(getString(R.string.error_title),
									   json.getString("message"), R.drawable.ic_warning);
							}
					   }
					   catch (JSONException e)
					   {
							Log.e(TAG, "error al cargar estacionamientos: " + e.getMessage());
							e.printStackTrace();
							showDialogoMensaje(getString(R.string.error_title),
								 getText(R.string.error_msg).toString(), R.drawable.ic_warning);
					   }
				 }

				 @Override
				 public void onError (HttpRequestException e)
				 {
					   e.printStackTrace();
					   Toast.makeText(getContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
				 }
		    });*/
	  }

	  public void loadTarjetas ()
	  {
		    ApiHelper.tarjetas(new HttpRequestCallback()
		    {
				 @Override
				 public void onSuccess (JSONObject json)
				 {
					   try
					   {
							if (json.getBoolean("success"))
							{
								  JSONArray jsonTarjetas = json.getJSONArray("tarjetas");
								  tarjetas = new ArrayList<>();
								  for (int i = 0; i < jsonTarjetas.length(); i++)
								  {
									    tarjetas.add(new ConektaTarjeta(jsonTarjetas.getJSONObject(i)));
								  }

								  dialogFormaPago.setFormasPago(tarjetas);
							}
					   }
					   catch (JSONException e)
					   {
							e.printStackTrace();
					   }
				 }

				 @Override
				 public void onError (HttpRequestException e)
				 {
					   e.printStackTrace();
				 }
		    });
	  }

	  private List<Terminal> getEntradasCercanas ()
	  {
		    List<Terminal> entradas = new ArrayList<>();

		    for (String UUID : beaconsUUID)
		    {
				 Terminal terminal = terminalesEntrada.get(UUID.replaceAll("-", "").toLowerCase());

				 if (terminal != null)
				 {
					   entradas.add(terminal);
				 }
		    }

		    return entradas;
	  }

	  private List<Terminal> getSalidasCercanas ()
	  {
		    List<Terminal> salidas = new ArrayList<>();

		    for (String UUID : beaconsUUID)
		    {
				 Terminal terminal = terminalesSalida
					  .get(UUID.replaceAll("-", "").toLowerCase());

				 if (terminal != null)
				 {
					   salidas.add(terminal);
				 }
		    }

		    return salidas;
	  }

	  public void verifyGPS ()
	  {
		    if (getContext() == null || getActivity() == null) return;
		    LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

		    if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		    {
				 final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				 builder.setTitle("GPS inactivo");
				 builder.setMessage("Es necesario activar tu GPS para la detección de estacionamientos");
				 builder.setPositiveButton(android.R.string.ok, null);
				 builder.setOnDismissListener(null);
				 builder.show();
		    }
	  }

	  private void seleccionarEntrada ()
	  {
	  		List<Terminal> availableTerminals = parkingViewModel.getAvailableTerminals().getValue();
		  Terminal terminalEntrada = availableTerminals.get(0);
		  if (terminalEntrada == null && availableTerminals == null)
		    {
				 Toast.makeText(getActivity(), "No hay terminal de entrada cercana", Toast.LENGTH_SHORT).show();
				 return;
		    }
		  if (availableTerminals.size() >= 1 && terminalEntrada.getTerminalType().equals(TerminalType.Entrada))
		    {
				ingresarEstacionamiento((int) terminalEntrada.getId());
				 return;
		    }
		  /*  dialogoCarril.setTerminales(availableTerminals);

		    List<Terminal> entries = new ArrayList<>();
		    for (int i = 0; i < availableTerminals.size(); i++)
		    {
				 Terminal terminal = availableTerminals.get(i);
				 if (terminal.getTerminalType().equals(TerminalType.Entrada) || !terminal.isAntiPassBack())
					   entries.add(terminal);
		    }
		    if (entries.size() > 0)
		    {
				 dialogoCarril.setTerminales(entries);
		    }
		    else
		    {
				 showDialogoMensaje("Sin entradas cercanas", "Aproxímate a una entrada", R.drawable.ic_warning);
				 return;
		    }

		    dialogoCarril.setOnConfirm(() ->
		    {
				 Terminal selectedTerminal = dialogoCarril.getCarrilSeleccionado();
				 if (selectedTerminal.getTerminalType().equals(TerminalType.Salida))
					   salirEstacionamiento((int) selectedTerminal.getId());
				 else if (selectedTerminal.getTerminalType().equals(TerminalType.Entrada))
					   ingresarEstacionamiento((int) selectedTerminal.getId());
		    });

		    dialogoCarril.show();*/
	  }

	  private void ingresarEstacionamiento (Integer terminalEntradaId)
	  {
		    btnApparkameBlock = true;
		    apparkameLoadingView.startAnimation();
		    txtHomeMessage.setVisibility(View.INVISIBLE);
		    toggleEstacionamientoInfoContainer(false);
		    ticketViewModel.requestEntry(terminalEntradaId);
		    /*ApiHelper.ingresarEstacionamiento(terminalEntradaId, new HttpRequestCallback()
		    {
				 @Override
				 public void onSuccess (JSONObject json)
				 {
					   Log.i(TAG, "onSuccess: Petición de entrada registrada");
					   toggleEstacionamientoInfoContainer(true);
					   try
					   {
							if (json.getBoolean("success"))
							{
								  setBoletoActivo(new Boleto(json.getJSONObject("boleto")));
							}
							else
							{
								  showDialogoMensaje(getString(R.string.error_title),
									   json.getString("message"), R.drawable.ic_warning);
							}
					   }
					   catch (Exception e)
					   {
							e.printStackTrace();
							showDialogoMensaje(getString(R.string.error_title),
								 getText(R.string.error_msg).toString(), R.drawable.ic_warning);
					   }
					   finally
					   {
							btnApparkameBlock = false;
							apparkameLoadingView.stopAnimation();
							txtHomeMessage.setVisibility(View.VISIBLE);
					   }
				 }

				 @Override
				 public void onError (HttpRequestException e)
				 {
					   toggleEstacionamientoInfoContainer(true);
					   btnApparkameBlock = false;
					   apparkameLoadingView.stopAnimation();
					   e.printStackTrace();
					   txtHomeMessage.setVisibility(View.VISIBLE);
					   showDialogoMensaje(getString(R.string.error_title),
						    getText(R.string.error_msg).toString(), R.drawable.ic_warning);
				 }
		    });*/
	  }

	  private void pagarEstacionamiento ()
	  {
		    dialogFormaPago.show();
		    ticketViewModel.stopDebitTimer();
		    dialogFormaPago.setOnConfirm(() ->
		    {
				 ConektaTarjeta tarjeta = dialogFormaPago.getTarjetaSeleccionada();
				 if (tarjeta == null)
					   return;
				 btnApparkameBlock = true;
				 apparkameLoadingView.startAnimation();
				 boletoTimer.setVisibility(View.INVISIBLE);
				 txtHomeMessage.setVisibility(View.INVISIBLE);
				 ticketViewModel.requestPayment(tarjeta.getId());
		    });
	  }

	  private void seleccionarSalida ()
	  {
		    List<Terminal> availableTerminals = parkingViewModel.getAvailableTerminals().getValue();
		    Terminal terminalSalida = availableTerminals.get(0);
		    if (availableTerminals == null && terminalSalida == null)
		    {
				 Toast.makeText(getActivity(), "No hay terminal de salida cercana", Toast.LENGTH_SHORT).show();
				 return;
		    }
		    if (terminalSalida.getTerminalType().equals(TerminalType.Salida) && availableTerminals.size() >= 1)
		    {
				 Terminal selectedTerminal = availableTerminals.get(0);
				 if (selectedTerminal.getTerminalType().equals(TerminalType.Salida) || selectedTerminal.isAntiPassBack())
					   salirEstacionamiento((int) selectedTerminal.getId());
				 return;
		    }
		    /*dialogoCarril.setTerminales(availableTerminals);
		    List<Terminal> exits = new ArrayList<>();
		    for (int i = 0; i < availableTerminals.size(); i++)
		    {
				 Terminal terminal = availableTerminals.get(i);
				 if (terminal.getTerminalType().equals(TerminalType.Salida) || !terminal.isAntiPassBack())
					   exits.add(terminal);
		    }
		    if (exits.size() > 0)
		    {
				 dialogoCarril.setTerminales(exits);
		    }
		    else
		    {
				 showDialogoMensaje("Sin salidas cercanas",
					  "Aproxímate a una salida", R.drawable.ic_warning);
				 return;
		    }
		    dialogoCarril.setOnConfirm(() ->
		    {
				 Terminal selectedTerminal = dialogoCarril.getCarrilSeleccionado();
				 if (selectedTerminal.getTerminalType().equals(TerminalType.Salida) || selectedTerminal.isAntiPassBack())
					   salirEstacionamiento((int) selectedTerminal.getId());
				 else if (selectedTerminal.getTerminalType().equals(TerminalType.Entrada))
					   ingresarEstacionamiento((int) selectedTerminal.getId());
		    });

		    dialogoCarril.show();*/
	  }

	  private void salirEstacionamiento (Integer terminalId)
	  {
		    toggleEstacionamientoInfoContainer(false);
		    btnApparkameBlock = true;
		    apparkameLoadingView.startAnimation();
		    boletoTimer.setVisibility(View.INVISIBLE);
		    txtHomeMessage.setVisibility(View.INVISIBLE);
		    ticketViewModel.requestExit(terminalId);
		    /*ApiHelper.salirEstacionamiento(terminalId, new HttpRequestCallback()
		    {
				 @Override
				 public void onSuccess (JSONObject json)
				 {
					   toggleEstacionamientoInfoContainer(true);
					   try
					   {
							Log.e(TAG, "onSuccess:" + json.toString());
							if (json.getBoolean("success"))
							{
								  setBoletoActivo(new Boleto(json.getJSONObject("boleto")));
							}
							else
							{
								  showDialogoMensaje(getString(R.string.error_title),
									   json.getString("message"), R.drawable.ic_warning);
							}
					   }
					   catch (Exception e)
					   {
							e.printStackTrace();
							showDialogoMensaje(getString(R.string.error_title),
								 getText(R.string.error_msg).toString(), R.drawable.ic_warning);
					   }
					   finally
					   {
							btnApparkameBlock = false;
							apparkameLoadingView.stopAnimation();
							boletoTimer.setVisibility(View.VISIBLE);
							txtHomeMessage.setVisibility(View.VISIBLE);
					   }
				 }

				 @Override
				 public void onError (HttpRequestException e)
				 {
					   toggleEstacionamientoInfoContainer(true);
					   btnApparkameBlock = false;
					   apparkameLoadingView.stopAnimation();
					   boletoTimer.setVisibility(View.VISIBLE);
					   txtHomeMessage.setVisibility(View.VISIBLE);
					   e.printStackTrace();
					   showDialogoMensaje(getString(R.string.error_title),
						    getText(R.string.error_msg).toString(), R.drawable.ic_warning);
				 }
		    });*/
	  }

	  private void showDialogoEstacionamiento ()
	  {
		    if (getActivity() == null) return;
		    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		    LayoutInflater inflater = getActivity().getLayoutInflater();

		    View viewRoot = inflater.inflate(R.layout.dialogo_estacionamiento, null);


		    Parking estacionamiento;

		    if (boletoActivo != null)
		    {
				 estacionamiento = boletoActivo.getParking();
		    }
		    else if (estacionamientoActivo != null)
		    {
				 estacionamiento = estacionamientoActivo;
		    }
		    else
		    {
				 return;
		    }

		    /*((TextView) viewRoot.findViewById(R.id.minutosTolerancia)).setText(Integer.toString(estacionamiento.getMinutosGracia()));
		    ((TextView) viewRoot.findViewById(R.id.espacios)).setText(Integer.toString(estacionamiento.getEspacios()));*/
		    //((TextView) viewRoot.findViewById(R.id.direccion)).setText(estacionamiento.get());

		    builder.setView(viewRoot).setPositiveButton(R.string.ok, null);
		    AlertDialog alertDialog = builder.create();

		    alertDialog.show();
	  }

	  @Override
	  public void onClick (View view)
	  {
		    if (view.getId() == R.id.estacionamientoInfo)
		    {
				 //showDialogoEstacionamiento();
		    }
		    else if (view.getId() == R.id.btnApparkame)
		    {
				 if (btnApparkameBlock)
				 {
					   return;
				 }

				 if (boletoEtapa == ETAPA_INACTIVO)
				 {
					   seleccionarEntrada();
				 }
				 else if (boletoEtapa == ETAPA_SALDO)
				 {
					   pagarEstacionamiento();
				 }
				 else if (boletoEtapa == ETAPA_PAGADO)
				 {
					   seleccionarSalida();
				 }
		    }
		    else if (view.getId() == R.id.comisiones)
		    {
				 showCommissionsDialog();
		    }
	  }

	  private void showCommissionsDialog ()
	  {
		    FragmentManager fragmentManager = getChildFragmentManager();
		    CommissionsDialogFragment commissionsDialogFragment = (CommissionsDialogFragment) fragmentManager.findFragmentByTag(CommissionsDialogFragment.TAG);
		    if (commissionsDialogFragment == null)
				 commissionsDialogFragment = new CommissionsDialogFragment();
		    if (!commissionsDialogFragment.isAdded())
				 commissionsDialogFragment.show(fragmentManager, CommissionsDialogFragment.TAG);
	  }

	  private void showDialogoMensaje (String titulo, String mensaje, int resIcono)
	  {
		    if (getActivity() != null)
		    {
				 getMainActivity().showDialogoMensaje(titulo, mensaje, resIcono);
		    }
	  }

	  private MainActivity getMainActivity ()
	  {
		    return (MainActivity) getActivity();
	  }

	  public Boleto getBoletoActivo ()
	  {
		    return boletoActivo;
	  }

	  public HashMap<String, Terminal> getTerminalesSalida ()
	  {
		    return terminalesSalida;
	  }

	  public HashMap<String, Terminal> getTerminalesEntrada ()
	  {
		    return terminalesEntrada;
	  }

	  public List<String> getBeaconsUUID ()
	  {
		    return beaconsUUID;
	  }

	  public Parking getEstacionamientoActivo ()
	  {
		    return estacionamientoActivo;
	  }

	  /*public void setEstacionamientoNombre (String distancia)
	  {
		    String estacionamientoNombre = estacionamientoActivo.getNombre();
		    lblEstacionamientoNombre.setText(estacionamientoNombre + "(" + distancia + ")");
	  }

	  public int getEtapaBoleto ()
	  {
		    return boletoEtapa;
	  }*/
}
