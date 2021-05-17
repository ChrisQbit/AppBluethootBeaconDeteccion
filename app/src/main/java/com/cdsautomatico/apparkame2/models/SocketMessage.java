package com.cdsautomatico.apparkame2.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SocketMessage<T extends Parcelable> implements Parcelable
{
	  private T value;

	  public SocketMessage (T value)
	  {
		    this.value = value;
	  }

	  protected SocketMessage (Parcel in)
	  {
	  }

	  public static final Creator<SocketMessage> CREATOR = new Creator<SocketMessage>()
	  {
		    @Override
		    public SocketMessage createFromParcel (Parcel in)
		    {
				 return new SocketMessage(in);
		    }

		    @Override
		    public SocketMessage[] newArray (int size)
		    {
				 return new SocketMessage[size];
		    }
	  };

	  public T getValue ()
	  {
		    return value;
	  }

	  public void setValue (T value)
	  {
		    this.value = value;
	  }

	  @Override
	  public int describeContents ()
	  {
		    return 0;
	  }

	  @Override
	  public void writeToParcel (Parcel parcel, int i)
	  {
	  }
}
