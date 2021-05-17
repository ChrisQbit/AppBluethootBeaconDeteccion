package com.cdsautomatico.apparkame2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SpotBusy implements Parcelable
{
	  private String spot;

	  public SpotBusy (String spot)
	  {
		    this.spot = spot;
	  }

	  protected SpotBusy (Parcel in)
	  {
		    spot = in.readString();
	  }

	  public static final Creator<SpotBusy> CREATOR = new Creator<SpotBusy>()
	  {
		    @Override
		    public SpotBusy createFromParcel (Parcel in)
		    {
				 return new SpotBusy(in);
		    }

		    @Override
		    public SpotBusy[] newArray (int size)
		    {
				 return new SpotBusy[size];
		    }
	  };

	  public String getSpot ()
	  {
		    return spot;
	  }

	  public void setSpot (String spot)
	  {
		    this.spot = spot;
	  }

	  @Override
	  public int describeContents ()
	  {
		    return 0;
	  }

	  @Override
	  public void writeToParcel (Parcel parcel, int i)
	  {
		    parcel.writeString(spot);
	  }
}
