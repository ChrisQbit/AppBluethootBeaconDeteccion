package com.cdsautomatico.apparkame2.activities;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.activities.adapters.HistoryAdapter;
import com.cdsautomatico.apparkame2.models.HistoryTicket;
import com.cdsautomatico.apparkame2.viewModel.PaymentViewModel;

import java.util.List;

public class HistoryActivity extends AppCompatActivity
{
	  private TextView tvMessage;
	  private RecyclerView recyclerView;

	  @Override
	  protected void onCreate (Bundle savedInstanceState)
	  {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_history);

		    Toolbar toolbar = findViewById(R.id.toolbar);
		    toolbar.setTitle(R.string.title_activity_payment_history);
		    setSupportActionBar(toolbar);
		    if (getSupportActionBar() != null)
		    {
				 getSupportActionBar().setDisplayHomeAsUpEnabled(true);
				 getSupportActionBar().setHomeButtonEnabled(true);
		    }

		    PaymentViewModel paymentViewModel = ViewModelProviders.of(this).get(PaymentViewModel.class);
		    tvMessage = findViewById(R.id.message);
		    recyclerView = findViewById(R.id.tickets);
		    paymentViewModel.getTickets().observe(this, historyTickets ->
		    {
				 if (historyTickets != null)
				 {
					   fillList(historyTickets);
				 }
				 else
				 {
					   tvMessage.setVisibility(View.VISIBLE);
					   recyclerView.setAdapter(null);
				 }
		    });
		    paymentViewModel.fetchPayments();
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

	  private void fillList (List<HistoryTicket> historyTickets)
	  {
		    HistoryAdapter adapter = new HistoryAdapter(historyTickets, this);
		    recyclerView.setAdapter(adapter);
	  }
}
