package com.cdsautomatico.apparkame2.views.dialogFragment;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.activities.adapters.CommissionsAdapter;
import com.cdsautomatico.apparkame2.models.TicketDebit;
import com.cdsautomatico.apparkame2.models.enums.CommissionType;
import com.cdsautomatico.apparkame2.viewModel.TicketViewModel;

public class CommissionsDialogFragment extends DialogFragment
{
	  public static final String TAG = "CommissionsDialogFragme";
	  private TicketViewModel ticketViewModel;
	  private RecyclerView commissionsRecycler, taxesRecycler;
	  private TextView tvTime, tvParkingFare, tvTotal;

	  @Override
	  public void onCreate (@Nullable Bundle savedInstanceState)
	  {
		    super.onCreate(savedInstanceState);
		    if (getActivity() == null)
				 return;
		    ticketViewModel = ViewModelProviders.of(getActivity()).get(TicketViewModel.class);
	  }

	  @Nullable
	  @Override
	  public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	  {
		    View view = inflater.inflate(R.layout.commissions_dialog_fragment, container, false);
		    tvTime = view.findViewById(R.id.timeElapsed);
		    tvParkingFare = view.findViewById(R.id.amount);
		    tvTotal = view.findViewById(R.id.total);
		    commissionsRecycler = view.findViewById(R.id.commissions);
		    taxesRecycler = view.findViewById(R.id.taxes);
		    return view;
	  }

	  @Override
	  public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState)
	  {
		    super.onViewCreated(view, savedInstanceState);
		    ticketViewModel.getTtcketDebit().observe(this, ticketDebit ->
		    {
				 if (ticketDebit != null)
				 {
					   fillData(ticketDebit);
				 }
		    });
	  }

	  private void fillData (@NonNull TicketDebit ticketDebit)
	  {
		    tvTime.setText(ticketDebit.getFormattedTime());
		    tvTotal.setText(ticketDebit.getFormattedAmount());
		    tvParkingFare.setText(ticketDebit.getFormattedRawAmount());
		    CommissionsAdapter commissionsAdapter = new CommissionsAdapter(ticketDebit.getCommissionsAdded(CommissionType.Commission), getContext());
		    CommissionsAdapter taxesAdapter = new CommissionsAdapter(ticketDebit.getCommissionsAdded(CommissionType.Tax), getContext());
		    LinearLayoutManager taxesLayoutManager = new LinearLayoutManager(getContext());
		    LinearLayoutManager commissionsLayoutManager = new LinearLayoutManager(getContext());
		    commissionsRecycler.setLayoutManager(taxesLayoutManager);
		    taxesRecycler.setLayoutManager(commissionsLayoutManager);
		    commissionsRecycler.setAdapter(commissionsAdapter);
		    taxesRecycler.setAdapter(taxesAdapter);
		    commissionsRecycler.setVisibility(View.VISIBLE);
		    taxesRecycler.setVisibility(View.VISIBLE);
	  }
}
