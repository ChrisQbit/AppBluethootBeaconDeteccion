package com.cdsautomatico.apparkame2.activities.adapters;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.activities.ExtendValidityActivity;
import com.cdsautomatico.apparkame2.models.Pension;
import com.cdsautomatico.apparkame2.utils.Constant;
import com.cdsautomatico.apparkame2.utils.RoundedImageView;

import java.util.List;

public class PensionesAdapter extends RecyclerView.Adapter<PensionesAdapter.ViewHolder>
{
       private List<Pension> pensiones;

       public PensionesAdapter (List<Pension> pensiones)
       {
              this.pensiones = pensiones;
       }

       @NonNull
       @Override
       public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType)
       {
              View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.template_list_pension, parent, false);
              return new ViewHolder(view);
       }

       @Override
       public void onBindViewHolder (@NonNull ViewHolder holder, int position)
       {
              final Pension pension = pensiones.get(position);
              holder.estacionamiento.setText(pension.getEstacionamiento().getNombre());
              String vigencia = pension.getFechaInicio().substring(0, 10) + " - " +
                    pension.getFechaVencimiento().substring(0, 10);
              holder.fechas.setText(vigencia);
       }

       @Override
       public int getItemCount ()
       {
              return pensiones.size();
       }


       class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
       {

              RoundedImageView image;
              TextView estacionamiento;
              TextView fechas;
              Button btnAmpliarVigencia;

              ViewHolder (View view)
              {
                     super(view);
                     image = view.findViewById(R.id.image);
                     estacionamiento = view.findViewById(R.id.estacionamiento);
                     fechas = view.findViewById(R.id.fechas);
                     btnAmpliarVigencia = view.findViewById(R.id.ampliarVigencia);
                     btnAmpliarVigencia.setOnClickListener(this);
              }

              @Override
              public void onClick (View view)
              {
                     if (view.getId() == R.id.ampliarVigencia)
                     {
                            Intent intent = new Intent(itemView.getContext(), ExtendValidityActivity.class);
                            intent.putExtra(Constant.Extra.PENSION_ID, pensiones.get(this.getAdapterPosition()).getId());
                            itemView.getContext().startActivity(intent);
                     }
              }
       }
}
