package com.cdsautomatico.apparkame2.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.api.ApiHelper;
import com.cdsautomatico.apparkame2.utils.FormErrors;
import com.cdsautomatico.apparkame2.utils.GenericDialogs;

import org.json.JSONObject;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import httprequest.HttpRequestCallback;
import httprequest.HttpRequestException;

public class RecuperarContraseniaActivity extends AppCompatActivity implements View.OnClickListener{

    private CircularProgressButton btnSubmit;
    private EditText txtEmail;
    private FormErrors formErrors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_contrasenia);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_recuperar_contrasenia);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        txtEmail = findViewById(R.id.email);
        btnSubmit = findViewById(R.id.btnSubmit);

        formErrors = new FormErrors();
        formErrors.setInput("email", txtEmail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void submit(){
        btnSubmit.startAnimation();
        btnSubmit.setEnabled(false);

        ApiHelper.recuperarContrasenia(txtEmail.getText().toString(), new HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject json) {
                btnSubmit.revertAnimation();
                btnSubmit.setEnabled(true);

                try {
                    if (json.getBoolean("success")) {
                        GenericDialogs.success(getActivity(), json.getString("message"));
                    }else{
                        if(!json.isNull("errors")){
                            formErrors.renderErrors(json.getJSONObject("errors"));
                        }else {
                            GenericDialogs.error(getActivity(), json.getString("message"));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(HttpRequestException e) {
                e.printStackTrace();
                btnSubmit.revertAnimation();
                btnSubmit.setEnabled(true);
                Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private RecuperarContraseniaActivity getActivity(){
        return this;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSubmit){
            submit();
        }
    }
}
