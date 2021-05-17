package com.cdsautomatico.apparkame2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class TicketChange implements Parcelable
{
	  private Boleto ticket;

	  public TicketChange (Parcel in)
	  {
		    ticket = in.readParcelable(Boleto.class.getClassLoader());
	  }

	  public TicketChange (Boleto boleto)
	  {
		    ticket = boleto;
	  }

	  @Override
	  public void writeToParcel (Parcel dest, int flags)
	  {
		    dest.writeParcelable(ticket, flags);
	  }

	  @Override
	  public int describeContents ()
	  {
		    return 0;
	  }

	  public static final Creator<TicketChange> CREATOR = new Creator<TicketChange>()
	  {
		    @Override
		    public TicketChange createFromParcel (Parcel in)
		    {
				 return new TicketChange(in);
		    }

		    @Override
		    public TicketChange[] newArray (int size)
		    {
				 return new TicketChange[size];
		    }
	  };

	  public Boleto getTicket ()
	  {
		    return ticket;
	  }
}
