package com.cdsautomatico.apparkame2.models;

import com.cdsautomatico.apparkame2.models.enums.PaymentType;
import com.cdsautomatico.apparkame2.utils.dateTime.Date;
import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Locale;

public class TicketPayment
{
	  @Expose
	  private String date;
	  @Expose
	  private String paymentMethod;
	  @Expose
	  private int paymentType;
	  @Expose
	  private List<Charge> charges;
	  @Expose
	  private double total;

	  public TicketPayment (String date, String paymentMethod, int paymentType, List<Charge> commissionsApplied, double total)
	  {
		    this.date = date;
		    this.paymentMethod = paymentMethod;
		    this.paymentType = paymentType;
		    this.charges = commissionsApplied;
		    this.total = total;
	  }

	  public String getDate ()
	  {
		    return date;
	  }

	  public String getFormattedDate ()
	  {
		    Date formattedDate = new Date(date);
		    return formattedDate .formatted();
	  }

	  public void setDate (String date)
	  {
		    this.date = date;
	  }

	  public String getPaymentMethod ()
	  {
		    return paymentMethod;
	  }

	  public void setPaymentMethod (String paymentMethod)
	  {
		    this.paymentMethod = paymentMethod;
	  }

	  public PaymentType getPaymentType ()
	  {
		    return PaymentType.values()[paymentType];
	  }

	  public void setPaymentType (PaymentType paymentType)
	  {
		    this.paymentType = paymentType.ordinal();
	  }

	  public List<Charge> getCharges ()
	  {
		    return charges;
	  }

	  public void setCharges (List<Charge> charges)
	  {
		    this.charges = charges;
	  }

	  public double getTotal ()
	  {
		    return total;
	  }

	  public String getFormattedTotal ()
	  {
		    String monto = String.format(Locale.US, "%.2f", total);
		    return "$" + (total < 0 ? "0" + monto : monto);
	  }

	  public void setTotal (double total)
	  {
		    this.total = total;
	  }
}
