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

import com.bumptech.glide.Glide;
import com.cdsautomatico.apparkame2.BuildConfig;
import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.models.HistoryTicket;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>
{
	  private List<HistoryTicket> tickets;
	  private Context ctx;

	  public HistoryAdapter (List<HistoryTicket> tickets, Context ctx)
	  {
		    this.tickets = tickets;
		    this.ctx = ctx;
	  }

	  @NonNull
	  @Override
	  public HistoryViewHolder onCreateViewHolder (@NonNull ViewGroup viewGroup, int i)
	  {
		    LayoutInflater inflater = LayoutInflater.from(ctx);
		    View view = inflater.inflate(R.layout.history_item_list, viewGroup, false);
		    return new HistoryViewHolder(view);
	  }

	  @Override
	  public void onBindViewHolder (@NonNull HistoryViewHolder holder, int position)
	  {
		    HistoryTicket ticket = tickets.get(holder.getAdapterPosition());
		    //holder.ticket = ticket;
		    holder.tvAmount.setText(ticket.getFormattedTotal());
		    holder.tvDate.setText(ticket.getTicketDate());
		    holder.tvParkingName.setText(ticket.getParkingName());
		    Glide.with(ctx)
				.load(BuildConfig.API_NETCORE + "/api/Apparkame/ParkingImage?parkingId=" + ticket.getParkingId())
				.fitCenter()
				.into(holder.ivParkingImage);
		    holder.itemView.setOnClickListener(view ->
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
		    return tickets.size();
	  }

	  class HistoryViewHolder extends RecyclerView.ViewHolder
	  {
		    private ImageView ivParkingImage;
		    private TextView tvParkingName;
		    private TextView tvDate;
		    private TextView tvAmount;
		    private RecyclerView recyclerView;
		    private int page = 1;

		    HistoryViewHolder (@NonNull View itemView)
		    {
				 super(itemView);
				 ivParkingImage = itemView.findViewById(R.id.parkingImg);
				 tvParkingName = itemView.findViewById(R.id.parkingName);
				 tvAmount = itemView.findViewById(R.id.total);
				 tvDate = itemView.findViewById(R.id.date);
				 recyclerView = itemView.findViewById(R.id.ticketPayments);
		    }

		    void open (Context ctx)
		    {
				 ConstraintSet set = new ConstraintSet();
				 set.setVisibility(R.id.ticketPayments, View.VISIBLE);
				 set.applyTo((ConstraintLayout) itemView);
				 set.clone(ctx, R.layout.history_item_list_second);
				 TransitionManager.beginDelayedTransition((ViewGroup) itemView);
				 set.applyTo((ConstraintLayout) itemView);
				 TicketPaymentAdapter adapter = new TicketPaymentAdapter(tickets.get(getLayoutPosition()).getPayments(), ctx);
				 recyclerView.setAdapter(adapter);
				 page = 2;
		    }

		    void close (Context ctx)
		    {
				 ConstraintSet set = new ConstraintSet();
				 set.setVisibility(R.id.ticketPayments, View.INVISIBLE);
				 set.applyTo((ConstraintLayout) itemView);
				 set.clone(ctx, R.layout.history_item_list);
				 set.setVisibility(R.id.ticketPayments, View.GONE);
				 TransitionManager.beginDelayedTransition((ViewGroup) itemView);
				 set.applyTo((ConstraintLayout) itemView);
				 page = 1;
		    }
	  }
}
