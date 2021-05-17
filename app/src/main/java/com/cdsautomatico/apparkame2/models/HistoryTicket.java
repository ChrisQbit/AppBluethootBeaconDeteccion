package com.cdsautomatico.apparkame2.models;

import com.google.gson.annotations.Expose;

import java.util.List;
import java.util.Locale;

public class HistoryTicket
{
	  @Expose
	  private String parkingName;
	  @Expose
	  private String ticketDate;
	  @Expose
	  private int parkingId;
	  @Expose
	  private double total;
	  @Expose
	  private List<TicketPayment> payments;

	  public HistoryTicket (String parkingName, String ticketDate, int parkingId, double total, List<TicketPayment> payments)
	  {
		    this.parkingName = parkingName;
		    this.ticketDate = ticketDate;
		    this.parkingId = parkingId;
		    this.total = total;
		    this.payments = payments;
	  }

	  public HistoryTicket ()
	  {
	  }

	  public String getParkingName ()
	  {
		    return parkingName;
	  }

	  public void setParkingName (String parkingName)
	  {
		    this.parkingName = parkingName;
	  }

	  public String getTicketDate ()
	  {
		    return ticketDate;
	  }

	  public void setTicketDate (String ticketDate)
	  {
		    this.ticketDate = ticketDate;
	  }

	  public int getParkingId ()
	  {
		    return parkingId;
	  }

	  public void setParkingId (int parkingId)
	  {
		    this.parkingId = parkingId;
	  }

	  public double getTotal ()
	  {
		    return total;
	  }

	  public void setTotal (double total)
	  {
		    this.total = total;
	  }

	  public List<TicketPayment> getPayments ()
	  {
		    return payments;
	  }

	  public void setPayments (List<TicketPayment> payments)
	  {
		    this.payments = payments;
	  }

	  public String getFormattedTotal ()
	  {
		    String monto = String.format(Locale.US, "%.2f", total);
		    return "$" + (total < 0 ? "0" + monto : monto);
	  }
}
