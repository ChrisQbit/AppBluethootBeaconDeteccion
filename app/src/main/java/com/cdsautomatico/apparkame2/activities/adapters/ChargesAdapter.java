package com.cdsautomatico.apparkame2.activities.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.models.Charge;

import java.util.List;

public class ChargesAdapter extends RecyclerView.Adapter<ChargesAdapter.ChargesViewHolder>
{
	  private List<Charge> commissions;
	  private Context ctx;

	  ChargesAdapter (List<Charge> commissions, Context ctx)
	  {
		    this.commissions = commissions;
		    this.ctx = ctx;
	  }

	  @NonNull
	  @Override
	  public ChargesViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
	  {
		    LayoutInflater inflater = LayoutInflater.from(ctx);
		    View view = inflater.inflate(R.layout.commissions_item_list, parent, false);
		    return new ChargesViewHolder(view);
	  }

	  @Override
	  public void onBindViewHolder (@NonNull ChargesViewHolder holder, int position)
	  {
		    Charge commission = commissions.get(position);
		    holder.tvAmount.setText(commission.getFormattedAmount());
		    holder.tvConcept.setText(commission.getConcept());
	  }

	  @Override
	  public int getItemCount ()
	  {
		    return commissions.size();
	  }

	  class ChargesViewHolder extends RecyclerView.ViewHolder
	  {
		    private TextView tvConcept, tvAmount;

		    ChargesViewHolder (View itemView)
		    {
				 super(itemView);
				 tvConcept = itemView.findViewById(R.id.concept);
				 tvAmount = itemView.findViewById(R.id.amount);
		    }
	  }
}

