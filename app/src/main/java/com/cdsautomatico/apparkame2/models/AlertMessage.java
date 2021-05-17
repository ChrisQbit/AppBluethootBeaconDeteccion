package com.cdsautomatico.apparkame2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class AlertMessage implements Parcelable
{
	  private String message;

	  public AlertMessage(String message)
	  {
	  	  this.message = message;
	  }

	  public String getMessage ()
	  {
		    return message;
	  }

	  protected AlertMessage (Parcel in)
	  {
		    message = in.readString();
	  }

	  public static final Creator<AlertMessage> CREATOR = new Creator<AlertMessage>()
	  {
		    @Override
		    public AlertMessage createFromParcel (Parcel in)
		    {
				 return new AlertMessage(in);
		    }

		    @Override
		    public AlertMessage[] newArray (int size)
		    {
				 return new AlertMessage[size];
		    }
	  };

	  @Override
	  public int describeContents ()
	  {
		    return 0;
	  }

	  @Override
	  public void writeToParcel (Parcel parcel, int i)
	  {
		    parcel.writeString(message);
	  }
}
