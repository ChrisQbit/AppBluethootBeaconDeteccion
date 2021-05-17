package com.cdsautomatico.apparkame2.models;

import com.cdsautomatico.apparkame2.models.enums.CommissionType;
import com.cdsautomatico.apparkame2.models.enums.TerminalType;
import com.google.gson.annotations.Expose;

import java.util.Locale;

public class Commission extends Entity
{
	  @Expose
	  private String concept;
	  @Expose
	  private double fixedAmount;
	  @Expose
	  private double percentage;
	  @Expose
	  private int type;
	  private double total;

	  public Commission (String concept, double fixedAmount, double percentage, int type)
	  {
		    this.concept = concept;
		    this.fixedAmount = fixedAmount;
		    this.percentage = percentage;
		    this.type = type;
	  }

	  public Commission ()
	  {
	  }

	  public String getConcept ()
	  {
		    return concept;
	  }

	  public void setConcept (String concept)
	  {
		    this.concept = concept;
	  }

	  public double getFixedAmount ()
	  {
		    return fixedAmount;
	  }

	  public void setFixedAmount (double fixedAmount)
	  {
		    this.fixedAmount = fixedAmount;
	  }

	  public double getPercentage ()
	  {
		    return percentage;
	  }

	  public void setPercentage (double percentage)
	  {
		    this.percentage = percentage;
	  }

	  public CommissionType getType ()
	  {
		    return CommissionType.values()[type];
	  }

	  public void setType (int type)
	  {
		    this.type =type;
	  }

	  public String getTotal ()
	  {
		    String monto = String.format(Locale.US, "%.2f", total);
		    return "$" + (total < 0 ? "0" + monto : monto);
	  }

	  public void setTotal (double total)
	  {
		    this.total = total;
	  }
}
