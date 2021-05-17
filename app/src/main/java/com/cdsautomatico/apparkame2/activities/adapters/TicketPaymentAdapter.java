package com.cdsautomatico.apparkame2.activities.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.models.TicketPayment;

import java.util.List;

public class TicketPaymentAdapter extends RecyclerView.Adapter<TicketPaymentAdapter.TicketPaymentViewHolder>
{
	  private List<TicketPayment> payments;
	  private Context ctx;

	  public TicketPaymentAdapter (List<TicketPayment> payments, Context ctx)
	  {
		    this.payments = payments;
		    this.ctx = ctx;
	  }

	  @NonNull
	  @Override
	  public TicketPaymentViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i)
	  {
		    LayoutInflater inflater = LayoutInflater.from(ctx);
		    return new TicketPaymentViewHolder(inflater.inflate(R.layout.payment_item_list, viewGroup, false));
	  }

	  @Override
	  public void onBindViewHolder (@NonNull TicketPaymentViewHolder holder, int position)
	  {
		    TicketPayment payment = payments.get(holder.getAdapterPosition());
		    //holder.payment = payment;
		    holder.tvPaymentMethod.setText(payment.getPaymentMethod());
		    holder.tvDate.setText(payment.getDate());
		    holder.tvAmount.setText(payment.getFormattedTotal());
		    holder.tvPaymentMethod.setText(payment.getPaymentMethod());
		    holder.ivIcon.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_credit_card));
		    holder.itemView.setOnClickListener((view) ->
		    {
				 if (holder.page == 1)
				 {
					   holder.open(ctx);
				 }
				 else if (holder.page == 2)
				 {
					   holder.close(ctx);
				 }
		    });
		    if (holder.page == 2)
		    {
				 holder.close(ctx);
		    }
	  }

	  @Override
	  public int getItemCount ()
	  {
		    return payments.size();
	  }

	  class TicketPaymentViewHolder extends RecyclerView.ViewHolder
	  {
		    private TextView tvDate;
		    private TextView tvPaymentMethod;
		    private TextView tvAmount;
		    private ImageView ivIcon;
		    private RecyclerView recyclerView;
		    private int page = 1;

		    TicketPaymentViewHolder (@NonNull View itemView)
		    {
				 super(itemView);
				 tvDate = itemView.findViewById(R.id.paymentDate);
				 tvPaymentMethod = itemView.findViewById(R.id.paymentMethod);
				 tvAmount = itemView.findViewById(R.id.total);
				 ivIcon = itemView.findViewById(R.id.icon);
				 recyclerView = itemView.findViewById(R.id.charges);
		    }

		    void open (Context ctx)
		    {
				 ConstraintSet set = new ConstraintSet();
				 set.setVisibility(R.id.charges, View.VISIBLE);
				 set.applyTo((ConstraintLayout) itemView);
				 set.clone(ctx, R.layout.payment_item_list_second);
				 TransitionManager.beginDelayedTransition((ViewGroup) itemView);
				 set.applyTo((ConstraintLayout) itemView);
				 ChargesAdapter adapter = new ChargesAdapter(payments.get(getLayoutPosition()).getCharges(), ctx);
				 recyclerView.setAdapter(adapter);
				 page = 2;
		    }

		    void close (Context ctx)
		    {
				 ConstraintSet set = new ConstraintSet();
				 set.setVisibility(R.id.charges, View.INVISIBLE);
				 set.applyTo((ConstraintLayout) itemView);
				 set.clone(ctx, R.layout.payment_item_list);
				 set.setVisibility(R.id.charges, View.GONE);
				 TransitionManager.beginDelayedTransition((ViewGroup) itemView);
				 set.applyTo((ConstraintLayout) itemView);
				 page = 1;
		    }

	  }
}
