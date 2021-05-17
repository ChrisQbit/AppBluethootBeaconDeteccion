package com.cdsautomatico.apparkame2.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cdsautomatico.apparkame2.BuildConfig;
import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.api.ApiHelper;
import com.cdsautomatico.apparkame2.api.ApiSession;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import httprequest.HttpRequestCallback;
import httprequest.HttpRequestException;
import io.conekta.conektasdk.Card;
import io.conekta.conektasdk.Conekta;
import io.conekta.conektasdk.Token;

public class FormasPagoAgregarActivity extends AppCompatActivity implements View.OnClickListener
{

       EditText txtTitular;
       EditText txtTarjeta;
       EditText txtFechaExpiracion;
       EditText txtCvv;
       CircularProgressButton btnSubmit;

       @Override
       protected void onCreate (Bundle savedInstanceState)
       {
              super.onCreate(savedInstanceState);
              ApiSession.setContext(getBaseContext());
              setContentView(R.layout.activity_formas_pago_agregar);

              Toolbar toolbar = findViewById(R.id.toolbar);
              toolbar.setTitle(R.string.title_activity_formas_pago_agregar);
              setSupportActionBar(toolbar);

              getSupportActionBar().setDisplayHomeAsUpEnabled(true);
              getSupportActionBar().setHomeButtonEnabled(true);

              txtTitular = findViewById(R.id.titular);
              txtTarjeta = findViewById(R.id.tarjeta);
              txtFechaExpiracion = findViewById(R.id.fechaExpiracion);
              txtCvv = findViewById(R.id.cvv);
              btnSubmit = findViewById(R.id.btnSubmit);


              txtFechaExpiracion.addTextChangedListener(new TextWatcher()
              {
                     @Override
                     public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2)
                     {

                     }

                     @Override
                     public void onTextChanged (CharSequence charSequence, int i, int i1, int i2)
                     {
                            if (charSequence.length() == 3 && charSequence.charAt(2) != '/')
                            {
                                   String newText = charSequence.subSequence(0, 2) + "/" + charSequence.charAt(2);
                                   txtFechaExpiracion.setText(newText);
                                   txtFechaExpiracion.setSelection(4);
                            }
                     }

                     @Override
                     public void afterTextChanged (Editable editable)
                     {

                     }
              });


       }

//    private void generarTokenTarjetaOpenPay(){
//
//        boolean errors = false;
//
//        if(txtTitular.getText().toString().equals("")){
//            txtTitular.setError("Indica títular de la tarjeta");
//            errors = true;
//        }
//
//        if(txtTarjeta.getText().toString().equals("")){
//            txtTarjeta.setError("Indica número de tarjeta");
//            errors = true;
//        }
//
//        String[] fechaExp = txtFechaExpiracion.getText().toString().split("/");
//        if(fechaExp.length < 2){
//            txtFechaExpiracion.setError("Fecha de expiración inválida");
//            errors = true;
//        }
//
//        if(txtCvv.getText().toString().equals("")){
//            txtCvv.setError("Indica código de seguridad CVV");
//            errors = true;
//        }
//
//        if(errors){
//            return;
//        }
//
//
//        Openpay openpay = new Openpay(BuildConfig.OPENPAY_MERCHANT_ID, BuildConfig.OPENPAY_PUBLIC, BuildConfig.OPENPAY_PRODUCTION);
//
//        mx.openpay.android.model.Card card = new mx.openpay.android.model.Card();
//        card.holderName(txtTitular.getText().toString());
//        card.cardNumber(txtTarjeta.getText().toString());
//        card.expirationMonth(Integer.parseInt(fechaExp[0]));
//        card.expirationYear(Integer.parseInt(fechaExp[1]));
//        card.cvv2(txtCvv.getText().toString());
//
//        btnSubmit.startAnimation();
//        btnSubmit.setEnabled(false);
//
//        openpay.createToken(card, new OperationCallBack<mx.openpay.android.model.Token>() {
//
//            @Override
//            public void onSuccess(OperationResult arg0) {
//                mx.openpay.android.model.Token token = (mx.openpay.android.model.Token) arg0.getResult();
//                Log.d("TOKEN", token.getId());
//
//                guardarTarjeta(token.getId());
//            }
//
//            @Override
//            public void onError(OpenpayServiceException arg0) {
//                Toast.makeText(getActivity(), R.string.error_msg, Toast.LENGTH_SHORT).show();
//                btnSubmit.revertAnimation();
//                btnSubmit.setEnabled(true);
//            }
//
//            @Override
//            public void onCommunicationError(ServiceUnavailableException arg0) {
//                Toast.makeText(getActivity(), R.string.error_msg, Toast.LENGTH_SHORT).show();
//                btnSubmit.revertAnimation();
//                btnSubmit.setEnabled(true);
//            }
//        });
//    }

       private void generarTokenTarjeta ()
       {
              Conekta.setPublicKey(BuildConfig.CONEKTA_PUBLIC);
              Conekta.setApiVersion(BuildConfig.CONEKTA_VERSION);
              Conekta.collectDevice(this);


              boolean errors = false;

              if (txtTitular.getText().toString().equals(""))
              {
                     txtTitular.setError("Indica títular de la tarjeta");
                     errors = true;
              }

              if (txtTarjeta.getText().toString().equals(""))
              {
                     txtTarjeta.setError("Indica número de tarjeta");
                     errors = true;
              }

              String[] fechaExp = txtFechaExpiracion.getText().toString().split("/");
              if (fechaExp.length < 2)
              {
                     txtFechaExpiracion.setError("Fecha de expiración inválida");
                     errors = true;
              }

              if (txtCvv.getText().toString().equals(""))
              {
                     txtCvv.setError("Indica código de seguridad CVV");
                     errors = true;
              }

              if (Integer.parseInt(fechaExp[0]) > 12 || Integer.parseInt(fechaExp[0]) == 0)
              {
                     txtFechaExpiracion.setError("Mes no válido");
                     errors = true;
              }

              if (errors)
              {
                     return;
              }

              Card card = new Card(txtTitular.getText().toString(), txtTarjeta.getText().toString(), txtCvv.getText().toString(), fechaExp[0], "20" + fechaExp[1]);
              Token token = new Token(this);

              token.onCreateTokenListener((JSONObject data) ->
              {

                     try
                     {
                            if (data.getString("object").equals("error"))
                            {
                                   Toast.makeText(getActivity(), data.getString("message_to_purchaser"), Toast.LENGTH_SHORT).show();
                                   btnSubmit.revertAnimation();
                                   btnSubmit.setEnabled(true);
                                   return;
                            }

                            guardarTarjeta(data.getString("id"));
                     }
                     catch (Exception err)
                     {
                            err.printStackTrace();
                            btnSubmit.revertAnimation();
                            btnSubmit.setEnabled(true);
                     }
              });

              token.create(card);
              //btnSubmit.startAnimation();
              btnSubmit.setEnabled(false);
       }

       private void guardarTarjeta (String cardToken)
       {
              ApiHelper.guardarTarjeta(cardToken, new HttpRequestCallback()
              {
                     @Override
                     public void onSuccess (JSONObject json)
                     {
                            try
                            {
                                   if (json.getBoolean("success"))
                                   {
                                          finish();
                                   }
                                   else
                                   {
                                          Toast.makeText(getBaseContext(), json.getString("message"),
                                                Toast.LENGTH_SHORT).show();

                                          btnSubmit.revertAnimation();
                                          btnSubmit.setEnabled(true);
                                   }
                            }
                            catch (JSONException e)
                            {
                                   btnSubmit.revertAnimation();
                                   btnSubmit.setEnabled(true);

                                   e.printStackTrace();
                                   Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                            }
                     }

                     @Override
                     public void onError (HttpRequestException e)
                     {
                            btnSubmit.revertAnimation();
                            btnSubmit.setEnabled(true);

                            e.printStackTrace();
                            Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                     }
              });
       }

       private FormasPagoAgregarActivity getActivity ()
       {
              return this;
       }

       @Override
       public boolean onOptionsItemSelected (MenuItem item)
       {
              if (item.getItemId() == android.R.id.home)
              {
                     finish();
              }

              return super.onOptionsItemSelected(item);
       }

       @Override
       public void onClick (View view)
       {
              if (view.getId() == R.id.btnSubmit)
              {
                     generarTokenTarjeta();
              }
       }
}
