package com.cdsautomatico.apparkame2.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.activities.adapters.PensionesAdapter;
import com.cdsautomatico.apparkame2.api.ApiHelper;
import com.cdsautomatico.apparkame2.api.ApiSession;
import com.cdsautomatico.apparkame2.models.Pension;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import httprequest.HttpRequestCallback;
import httprequest.HttpRequestException;

public class PensionesActivity extends AppCompatActivity implements View.OnClickListener
{

       private List<Pension> pensiones;
       private RecyclerView listPensiones;

       @Override
       protected void onCreate (Bundle savedInstanceState)
       {
              super.onCreate(savedInstanceState);
              ApiSession.setContext(getBaseContext());
              setContentView(R.layout.activity_pensiones);

              Toolbar toolbar = findViewById(R.id.toolbar);
              toolbar.setTitle(R.string.title_activity_pensiones);
              setSupportActionBar(toolbar);

              if (getSupportActionBar() != null)
              {
                     getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                     getSupportActionBar().setHomeButtonEnabled(true);
              }

              listPensiones = findViewById(R.id.listPensiones);
              listPensiones.setHasFixedSize(true);
              listPensiones.setLayoutManager(new LinearLayoutManager(getBaseContext()));
       }

       @Override
       protected void onStart ()
       {
              super.onStart();
              loadPensiones();
       }

       private void loadPensiones ()
       {
              pensiones = new ArrayList<>();
              //listPensiones.setAdapter(new PensionesAdapter(pensiones));
              findViewById(R.id.progress).setVisibility(View.VISIBLE);
              findViewById(R.id.noRecords).setVisibility(View.GONE);

              ApiHelper.pensiones(new HttpRequestCallback()
              {
                     @Override
                     public void onSuccess (JSONObject json)
                     {
                            findViewById(R.id.progress).setVisibility(View.GONE);

                            try
                            {
                                   if (json.getBoolean("success"))
                                   {

                                          JSONArray jsonPensiones = json.getJSONArray("pensiones");
                                          pensiones = new ArrayList<>();
                                          for (int i = 0; i < jsonPensiones.length(); i++)
                                          {
                                                 pensiones.add(new Pension(jsonPensiones.getJSONObject(i)));
                                          }
                                          listPensiones.setAdapter(new PensionesAdapter(pensiones));

                                          if (pensiones.size() == 0)
                                          {
                                                 findViewById(R.id.noRecords).setVisibility(View.VISIBLE);
                                          }
                                          else
                                          {
                                                 findViewById(R.id.noRecords).setVisibility(View.GONE);
                                          }

                                   }
                                   else
                                   {
                                          Toast.makeText(getBaseContext(), json.getString("message"), Toast.LENGTH_SHORT).show();
                                   }
                            }
                            catch (JSONException e)
                            {
                                   e.printStackTrace();
                                   Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                            }
                     }

                     @Override
                     public void onError (HttpRequestException e)
                     {
                            e.printStackTrace();
                            findViewById(R.id.progress).setVisibility(View.GONE);
                            Toast.makeText(getBaseContext(), R.string.error_msg, Toast.LENGTH_SHORT).show();
                     }
              });
       }

       @Override
       public void onClick (View view)
       {

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

}
