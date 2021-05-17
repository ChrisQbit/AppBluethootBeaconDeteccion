package com.cdsautomatico.apparkame2.activities;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.api.ApiHelper;
import com.cdsautomatico.apparkame2.api.ApiSession;
import com.cdsautomatico.apparkame2.models.ConektaTarjeta;
import com.cdsautomatico.apparkame2.utils.GenericDialogs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import httprequest.HttpRequestCallback;
import httprequest.HttpRequestException;

public class FormasPagoActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int REQUEST_ADD_PAYMENT_METHOD = 217;

    private List<ConektaTarjeta> tarjetas;
    private RecyclerView listTarjetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApiSession.setContext(getBaseContext());
        setContentView(R.layout.activity_formas_pago);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.title_activity_formas_pago);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        listTarjetas = findViewById(R.id.listTarjetas);
        listTarjetas.setHasFixedSize(true);
        listTarjetas.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        loadTarjetas();
    }

    private void loadTarjetas(){
        tarjetas = new ArrayList<>();
        listTarjetas.setAdapter(new FormasPagoAdapter());
        findViewById(R.id.progress).setVisibility(View.VISIBLE);
        findViewById(R.id.noRecords).setVisibility(View.GONE);

        ApiHelper.tarjetas(new HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject json) {
                findViewById(R.id.progress).setVisibility(View.GONE);

                try {
                    if (json.getBoolean("success")) {

                        JSONArray jsonTarjetas = json.getJSONArray("tarjetas");
                        tarjetas = new ArrayList<>();
                        for(int i=0; i<jsonTarjetas.length(); i++){
                            tarjetas.add(new ConektaTarjeta(jsonTarjetas.getJSONObject(i)));
                        }
                        listTarjetas.setAdapter(new FormasPagoAdapter());

                        if(tarjetas.size() == 0){
                            findViewById(R.id.noRecords).setVisibility(View.VISIBLE);
                        }else{
                            findViewById(R.id.noRecords).setVisibility(View.GONE);
                        }

                    } else {
                        Toast.makeText(getBaseContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(HttpRequestException e) {
                e.printStackTrace();
                findViewById(R.id.progress).setVisibility(View.GONE);
                Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private FormasPagoActivity getActivity(){
        return this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnAgregar){
            Intent intent = new Intent(this, FormasPagoAgregarActivity.class);
            startActivityForResult(intent, REQUEST_ADD_PAYMENT_METHOD);
        }
    }

    class FormasPagoAdapter extends RecyclerView.Adapter<FormasPagoAdapter.ViewHolder>{

        public class ViewHolder extends RecyclerView.ViewHolder{

            ImageView icon;
            TextView last4;
            View more;

            public ViewHolder(View view){
                super(view);
                icon = view.findViewById(R.id.icon);
                last4 = view.findViewById(R.id.last4);
                more = view.findViewById(R.id.more);
            }

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.template_list_tarjeta, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final ConektaTarjeta tarjeta = tarjetas.get(position);

            holder.last4.setText("**** " + tarjeta.getLast4().toString());

            switch (tarjeta.getBrand()){
                case "VISA":
                    holder.icon.setImageResource(R.drawable.ic_visa);
                    break;
                case "MC":
                    holder.icon.setImageResource(R.drawable.ic_mastercard);
                    break;
                case "AMERICAN_EXPRESS":
                    holder.icon.setImageResource(R.drawable.ic_amex);
                    break;
            }

            holder.more.setOnClickListener((View view) -> {
                GenericDialogs.confirm(getActivity(), "Eliminar forma de pago",
                        "¿Realmente deseas eliminar la forma de pago?", () -> {
                    tarjetas.remove(tarjeta);
//                    listTarjetas.setAdapter(new FormasPagoAdapter());
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, tarjetas.size());

                    if(tarjetas.size() == 0){
                        findViewById(R.id.noRecords).setVisibility(View.VISIBLE);
                    }

                    ApiHelper.eliminarTarjeta(tarjeta.getId(), new HttpRequestCallback() {
                        @Override
                        public void onSuccess(JSONObject json) {

                        }

                        @Override
                        public void onError(HttpRequestException e) {
                            e.printStackTrace();
                        }
                    });
                });
            });
        }

        @Override
        public int getItemCount() {
            return tarjetas.size();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_ADD_PAYMENT_METHOD){
            loadTarjetas();
        }
    }
}
