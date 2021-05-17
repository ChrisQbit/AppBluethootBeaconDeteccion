package com.cdsautomatico.apparkame2.models;

public abstract class Entity<T>
{
	  public T value;
	  public long id;
	  public char status = 1;

	  @Override
	  public String toString ()
	  {
		    return value.toString();
	  }

	  public T getValue ()
	  {
		    return value;
	  }

	  public void setValue (T value)
	  {
		    this.value = value;
	  }

	  public long getId ()
	  {
		    return id;
	  }

	  public void setId (long id)
	  {
		    this.id = id;
	  }

	  public char getStatus ()
	  {
		    return status;
	  }

	  public void setStatus (char status)
	  {
		    this.status = status;
	  }
}