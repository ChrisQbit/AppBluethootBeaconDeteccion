package com.cdsautomatico.apparkame2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SpotAvailable implements Parcelable
{
	  private String spot;

	  public SpotAvailable (String spot)
	  {
		    this.spot = spot;
	  }

	  protected SpotAvailable (Parcel in)
	  {
		    spot = in.readString();
	  }

	  @Override
	  public void writeToParcel (Parcel dest, int flags)
	  {
		    dest.writeString(spot);
	  }

	  @Override
	  public int describeContents ()
	  {
		    return 0;
	  }

	  public String getSpot ()
	  {
		    return spot;
	  }

	  public void setSpot (String spot)
	  {
		    this.spot = spot;
	  }

	  public static final Creator<SpotAvailable> CREATOR = new Creator<SpotAvailable>()
	  {
		    @Override
		    public SpotAvailable createFromParcel (Parcel in)
		    {
				 return new SpotAvailable(in);
		    }

		    @Override
		    public SpotAvailable[] newArray (int size)
		    {
				 return new SpotAvailable[size];
		    }
	  };
}
