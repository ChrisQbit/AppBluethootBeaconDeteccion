package com.cdsautomatico.apparkame2.models;

import android.text.SpannableString;

import com.cdsautomatico.apparkame2.models.enums.CommissionType;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TicketDebit
{
	  private double amount;
	  private double rawAmount;
	  private long time;
	  private List<Commission> commissions;

	  public TicketDebit ()
	  {
	  }

	  public double getAmount ()
	  {
		    return amount;
	  }

	  public double getRawAmount ()
	  {
		    return rawAmount;
	  }

	  public List<Commission> getCommissions ()
	  {
		    return commissions;
	  }

	  public void setAmount (double amount)
	  {
		    rawAmount = amount;
		    this.amount = addCommission(amount);
	  }

	  public long getTime ()
	  {
		    return time;
	  }

	  public String getFormattedTime ()
	  {
		    int time = (int) this.time;
		    long horas = time / 3600;
		    long minutos = (time % 3600) / 60;
		    SpannableString spannableString = new SpannableString(String.format("%s hr %s min", String.format(Locale.US, "%02d", horas), String.format(Locale.US, "%02d", minutos)));
		    return spannableString.toString();
	  }

	  public synchronized void setTime (long time)
	  {
		    this.time = time;
	  }

	  private double addCommission (double parkingFare)
	  {
		    if (commissions == null || commissions.size() == 0)
				 return parkingFare;
		    double totalPercentage = 0;
		    double totalFixedAmount = parkingFare;
		    for (int i = 0; i < commissions.size(); i++)
		    {
				 Commission commission = commissions.get(i);
				 totalPercentage += commission.getPercentage();
				 totalFixedAmount += commission.getFixedAmount();
		    }
		    return totalFixedAmount + (totalFixedAmount * (totalPercentage / 100));
	  }

	  public void setCommissions (List<Commission> commissions)
	  {
		    this.commissions = commissions;
	  }

	  public String getFormattedAmount ()
	  {
		    String monto = String.format(Locale.US, "%.2f", amount);
		    return "$" + (amount < 0 ? "0" + monto : monto);
	  }

	  public String getFormattedRawAmount ()
	  {
		    String monto = String.format(Locale.US, "%.2f", rawAmount);
		    return "$" + (rawAmount < 0 ? "0" + monto : monto);
	  }

	  public List<Commission> getCommissionsAdded (CommissionType commissionType)
	  {
		    double totalPercentage = 0;
		    double totalFixedAmount = rawAmount;
		    double fixedAmounts= 0;
		    for (int i = 0; i < this.commissions.size(); i++)
		    {
				 Commission commission = this.commissions.get(i);
				 totalFixedAmount += commission.getFixedAmount();
				 if (this.commissions.get(i).getType().equals(commissionType))
				 {
					   totalPercentage += commission.getPercentage();
					   fixedAmounts += commission.getFixedAmount();
				 }
		    }
		    List<Commission> commissions = new ArrayList<>();
		    Commission commission = new Commission();
		    commission.setConcept(commissionType.equals(CommissionType.Commission) ? "Uso de plataforma" : "Impuestos");
		    double total = (fixedAmounts) + totalFixedAmount * (totalPercentage / 100);
		    commission.setTotal(total);
		    commissions.add(commission);
		    return commissions;
	  }

	  public List<Commission> getCommissionsSepparated (CommissionType commissionType)
	  {
		    List<Commission> commissions = new ArrayList<>();
		    for (int i = 0; i < this.commissions.size(); i++)
		    {
				 if (this.commissions.get(i).getType().equals(commissionType))
				 {
					   Commission commission = new Commission();
					   commission.setConcept(this.commissions.get(i).getConcept());
					   double total = this.commissions.get(i).getFixedAmount() + (amount * (this.commissions.get(i).getPercentage() / 100));
					   commission.setTotal(total);
					   commissions.add(commission);
				 }
		    }
		    return commissions;
	  }
}
