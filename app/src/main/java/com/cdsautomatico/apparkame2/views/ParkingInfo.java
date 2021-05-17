package com.cdsautomatico.apparkame2.views;

import android.view.ViewGroup;
import android.widget.TextView;

import com.cdsautomatico.apparkame2.R;

public class ParkingInfo
{
	  private ViewGroup root;
	  private TextView tvParkingName;
	  private TextView tvRate;
	  private TextView tvSchedule;

	  public ParkingInfo (ViewGroup root)
	  {
		    this.root = root;
		    tvParkingName = root.findViewById(R.id.lblEstacionamientoNombre);
		    /*tvRate = root.findViewById(R.id.estacionamientoTarifa);
		    tvSchedule = root.findViewById(R.id.estacionamientoHorario);*/
	  }

	  public ViewGroup getRoot ()
	  {
		    return root;
	  }

	  public void setTvParkingName (String tvParkingName)
	  {
		    this.tvParkingName.setText(tvParkingName);
	  }

	  public void setTvRate (String tvRate)
	  {
		    this.tvRate.setText(tvRate);
	  }

	  public void setTvSchedule (String tvSchedule)
	  {
		    this.tvSchedule.setText(tvSchedule);
	  }
}
