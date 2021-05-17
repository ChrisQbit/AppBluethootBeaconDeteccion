package com.cdsautomatico.apparkame2.activities;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.api.ApiHelper;
import com.cdsautomatico.apparkame2.api.ApiSession;
import com.cdsautomatico.apparkame2.models.ConektaTarjeta;
import com.cdsautomatico.apparkame2.utils.Constant;
import com.cdsautomatico.apparkame2.viewModel.ExtendValidityViewModel;
import com.cdsautomatico.apparkame2.views.ApparkameLoadingView;
import com.cdsautomatico.apparkame2.views.DialogoFormaPago;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import httprequest.HttpRequestCallback;
import httprequest.HttpRequestException;

public class ExtendValidityActivity extends AppCompatActivity implements View.OnClickListener
{
	  private View btnPagar;
	  private ApparkameLoadingView apparkameLoading;
	  private ExtendValidityViewModel validityViewModel;
	  private ArrayList<ConektaTarjeta> tarjetas = new ArrayList<>();
	  private DialogoFormaPago dialogFormaPago;
	  private Timer timer;
	  private String pensionId;

	  private TextView tvUserName, tvParking, tvDateRange, tvPrice;

	  @Override
	  protected void onCreate (Bundle savedInstanceState)
	  {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_extend_validity);
		    Bundle extras = getIntent().getExtras();

		    if (extras == null)
		    {
				 Toast.makeText(this, R.string.parameter_error, Toast.LENGTH_SHORT).show();
				 finish();
				 return;
		    }

		    btnPagar = findViewById(R.id.constraintLayout);
		    apparkameLoading = findViewById(R.id.apparkameLoading);
		    tvUserName = findViewById(R.id.userName);
		    tvParking = findViewById(R.id.paymentDate);
		    tvDateRange = findViewById(R.id.ampliacionVigencia);
		    tvPrice = findViewById(R.id.price);
		    dialogFormaPago = findViewById(R.id.dialogFormaPago);

		    pensionId = extras.getString(Constant.Extra.PENSION_ID);

		    validityViewModel = ViewModelProviders.of(this).get(ExtendValidityViewModel.class);
		    validityViewModel.loadPensionValidity(pensionId).observe(this, extendValidityModel ->
		    {
				 if (extendValidityModel == null)
				 {
					   if (validityViewModel.getLastError() != null)
					   {
							String error = validityViewModel.getLastError().getMessage();
							if (apparkameLoading.isRunning())
								  apparkameLoading.stopAnimation();
							Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
					   }
					   return;
				 }
				 tvUserName.setText(extendValidityModel.getPensionName());
				 tvParking.setText(extendValidityModel.getParkingName());
				 String validityMessage = String.format(getString(R.string.ampliacion_vigencia),
					  extendValidityModel.getFormatterExpirationDate(), validityViewModel.getNewExpirationDate());
				 tvDateRange.setText(validityMessage);
				 tvPrice.setText(validityViewModel.getCyclePrice(extendValidityModel.getCyclePrice()));
				 if (apparkameLoading.isRunning())
					   apparkameLoading.stopAnimation();
		    });
		    dialogFormaPago.setOnAgregarFormaPago(() ->
		    {
				 Intent intent = new Intent(this, FormasPagoAgregarActivity.class);
				 startActivityForResult(intent, DialogoFormaPago.REQUEST_EDIT_PAYMENT_METHODS);
		    });
		    loadTarjetas();
		    btnPagar.setOnClickListener(this);
	  }

	  @Override
	  protected void onStart ()
	  {
		    super.onStart();
		    startButtonBouncing();
	  }

	  @Override
	  protected void onStop ()
	  {
		    timer.cancel();
		    super.onStop();
	  }

	  private void startButtonBouncing ()
	  {
		    final AnimatorSet animatorSet = new AnimatorSet();

		    ObjectAnimator animator1 = ObjectAnimator.ofFloat(btnPagar, "scaleX", 0.95f);
		    animator1.setRepeatCount(0);
		    animator1.setDuration(1000);

		    ObjectAnimator animator2 = ObjectAnimator.ofFloat(btnPagar, "scaleX", 1f);
		    animator2.setRepeatCount(0);
		    animator2.setDuration(1000);

		    ObjectAnimator animator3 = ObjectAnimator.ofFloat(btnPagar, "scaleY", 0.95f);
		    animator3.setRepeatCount(0);
		    animator3.setDuration(1000);

		    ObjectAnimator animator4 = ObjectAnimator.ofFloat(btnPagar, "scaleY", 1f);
		    animator4.setRepeatCount(0);
		    animator4.setDuration(1000);

		    animatorSet.play(animator1).before(animator2);
		    animatorSet.play(animator3).before(animator4);

		    timer = new Timer();
		    timer.scheduleAtFixedRate(new TimerTask()
		    {
				 @Override
				 public void run ()
				 {
					   runOnUiThread(animatorSet::start);
				 }
		    }, 0, 2050);
	  }

	  @Override
	  public void onBackPressed ()
	  {
		    if (apparkameLoading.isRunning())
				 apparkameLoading.stopAnimation();
		    else
				 super.onBackPressed();
	  }

	  @Override
	  public void onClick (View view)
	  {
		    if (view.getId() == R.id.constraintLayout)
		    {
				 dialogFormaPago.show();
				 dialogFormaPago.setOnConfirm(() ->
				 {
					   apparkameLoading.startAnimation();
					   ConektaTarjeta tarjeta = dialogFormaPago.getTarjetaSeleccionada();
					   validityViewModel.pay(tarjeta.getId(), pensionId, ApiSession.getUsuario().getId()).observe(this, paymentResponse ->
					   {
							String message;
							String title;
							int ress;
							if (paymentResponse == null || validityViewModel.getLastError() != null)
							{
								  message = validityViewModel.getLastError().getMessage();
								  title = "Error al procesar pago";
								  ress = R.drawable.ic_warning;
								  if (apparkameLoading.isRunning())
									    apparkameLoading.stopAnimation();
								  MessageActivity.showMessage(this, title, message, ress);
							}
							else
							{
								  if (paymentResponse.isSuccess())
								  {
									    ress = R.drawable.ic_logo;
									    title = "Pago realizado con éxito";
									    message = paymentResponse.getMessage();
								  }
								  else
								  {
									    ress = R.drawable.ic_warning;
									    title = "Algo salió mal";
									    message = "No se realizó ningún cobro\n" + paymentResponse.getMessage();
									    if (apparkameLoading.isRunning())
											 apparkameLoading.stopAnimation();
								  }
								  MessageActivity.showMessage(this, title, message, ress);
								  if (paymentResponse.isSuccess())
									    finish();
							}
					   });
				 });
		    }
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
}
