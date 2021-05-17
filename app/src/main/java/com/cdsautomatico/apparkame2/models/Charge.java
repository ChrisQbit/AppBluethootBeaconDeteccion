package com.cdsautomatico.apparkame2.models;

import com.google.gson.annotations.Expose;

import java.util.Locale;

public class Charge
{
	  @Expose
	  private String concept;
	  @Expose
	  private double amount;

	  public Charge (String concept, double amount)
	  {
		    this.concept = concept;
		    this.amount = amount;
	  }

	  public Charge ()
	  {
	  }

	  public String getConcept ()
	  {
		    return concept;
	  }

	  public String getFormattedAmount ()
	  {
		    String monto = String.format(Locale.US, "%.2f", amount);
		    return "$" + (amount < 0 ? "0" + monto : monto);
	  }

	  public void setConcept (String concept)
	  {
		    this.concept = concept;
	  }

	  public double getAmount ()
	  {
		    return amount;
	  }

	  public void setAmount (double amount)
	  {
		    this.amount = amount;
	  }
}
