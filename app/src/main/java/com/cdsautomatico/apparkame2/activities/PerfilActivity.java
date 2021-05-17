package com.cdsautomatico.apparkame2.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.api.ApiHelper;
import com.cdsautomatico.apparkame2.api.ApiSession;
import com.cdsautomatico.apparkame2.models.Usuario;
import com.cdsautomatico.apparkame2.utils.BitmapManipulation;
import com.cdsautomatico.apparkame2.utils.FormErrors;
import com.cdsautomatico.apparkame2.utils.GenericDialogs;
import com.cdsautomatico.apparkame2.utils.RoundedImageView;
import com.mvc.imagepicker.ImagePicker;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import httprequest.HttpRequestCallback;
import httprequest.HttpRequestException;

public class PerfilActivity extends AppCompatActivity implements View.OnClickListener {

    private RoundedImageView imgProfile;
    private BitmapManipulation selectedBitmap;
    private EditText txtNombre;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtPassword2;
    private CircularProgressButton btnSubmit;

    private FormErrors formErrors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_perfil);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        imgProfile = findViewById(R.id.imgProfile);
        txtNombre = findViewById(R.id.nombre);
        txtEmail = findViewById(R.id.email);
        txtPassword = findViewById(R.id.password);
        txtPassword2 = findViewById(R.id.password2);
        btnSubmit = findViewById(R.id.btnSubmit);

        formErrors = new FormErrors();
        formErrors.setInput("nombre", txtNombre);
        formErrors.setInput("email", txtEmail);
        formErrors.setInput("password", txtPassword);

        if(ApiSession.getUsuario().getFacebookId() != null){
            findViewById(R.id.fieldPassword).setVisibility(View.GONE);
            findViewById(R.id.fieldPassword2).setVisibility(View.GONE);
        }

        Usuario usuario = ApiSession.getUsuario();

        txtNombre.setText(usuario.getNombre());

        if(ApiSession.getUsuario().getEmail() != null) {
            txtEmail.setText(ApiSession.getUsuario().getEmail());
        }

        if(ApiSession.getUsuario().getImagen() != null) {
            Picasso.get().load(ApiSession.getUsuario().getImagen())
                    .resize(200, 200).centerCrop().into(imgProfile);
        }

//        ImagePicker.setMinQuality(200, 200);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void submit(){
        String bitmap64 = null;
        if(selectedBitmap != null){
            bitmap64 = selectedBitmap.getBase64String(85);
        }

        btnSubmit.startAnimation();
        btnSubmit.setEnabled(false);

        ApiHelper.editarPerfil(txtNombre.getText().toString(), txtEmail.getText().toString(),
            txtPassword.getText().toString(), txtPassword2.getText().toString(), bitmap64, new HttpRequestCallback() {
                @Override
                public void onSuccess(JSONObject json) {
                    btnSubmit.revertAnimation();
                    btnSubmit.setEnabled(true);

                    try {
                        if (json.getBoolean("success")) {
                            Usuario usuario = new Usuario(json.getJSONObject("usuario"));
                            ApiSession.setUsuario(usuario);

                            GenericDialogs.success(getActivity(), "Datos actualizados correctamente");
                        }else{
                            if(json.has("errors")){
                                formErrors.renderErrors(json.getJSONObject("errors"));
                            }else {
                                GenericDialogs.error(getActivity(), json.getString("message"));
                            }
                        }
                    }catch (JSONException e){
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(HttpRequestException e) {
                    btnSubmit.revertAnimation();
                    btnSubmit.setEnabled(true);

                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            });
    }

    private PerfilActivity getActivity(){
        return this;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.imgPicker){
            ImagePicker.pickImage(this, "Selecciona imagen:");
        }

        if(view.getId() == R.id.btnSubmit){
            submit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bitmap = ImagePicker.getImageFromResult(this, requestCode, resultCode, data);

        if(bitmap != null) {
            selectedBitmap = new BitmapManipulation(bitmap);
            selectedBitmap.fit(200, 200);

            imgProfile.setImageBitmap(selectedBitmap.getBitmap());
        }
    }
}
