package com.cdsautomatico.apparkame2.activities.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.models.Commission;

import java.util.List;

public class CommissionsAdapter extends RecyclerView.Adapter<CommissionsAdapter.CommissionsViewHolder>
{
	  private List<Commission> commissions;
	  private Context ctx;

	  public CommissionsAdapter (List<Commission> commissions, Context ctx)
	  {
		    this.commissions = commissions;
		    this.ctx = ctx;
	  }

	  @NonNull
	  @Override
	  public CommissionsViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
	  {
		    LayoutInflater inflater = LayoutInflater.from(ctx);
		    View view = inflater.inflate(R.layout.commissions_item_list, parent, false);
		    return new CommissionsViewHolder(view);
	  }

	  @Override
	  public void onBindViewHolder (@NonNull CommissionsViewHolder holder, int position)
	  {
		    Commission commission = commissions.get(position);
		    holder.tvAmount.setText(commission.getTotal());
		    holder.tvConcept.setText(commission.getConcept());
	  }

	  @Override
	  public int getItemCount ()
	  {
		    return commissions.size();
	  }

	  class CommissionsViewHolder extends RecyclerView.ViewHolder
	  {
		    private TextView tvConcept, tvAmount;

		    CommissionsViewHolder (View itemView)
		    {
				 super(itemView);
				 tvConcept = itemView.findViewById(R.id.concept);
				 tvAmount = itemView.findViewById(R.id.amount);
		    }
	  }
}
