package com.cdsautomatico.apparkame2.models.rate;

import com.cdsautomatico.apparkame2.models.Entity;
import com.cdsautomatico.apparkame2.models.enums.TimeUnits;
import com.cdsautomatico.apparkame2.utils.MathUtils;
import com.google.gson.annotations.Expose;

import static com.cdsautomatico.apparkame2.utils.TimeUtils.toDays;
import static com.cdsautomatico.apparkame2.utils.TimeUtils.toHours;
import static com.cdsautomatico.apparkame2.utils.TimeUtils.toMinutes;
import static com.cdsautomatico.apparkame2.utils.TimeUtils.toMonths;
import static com.cdsautomatico.apparkame2.utils.TimeUtils.toWeeks;

public class Rule extends Entity
{
	  @Expose
	  private int scheduleId;
	  @Expose
	  private int quantity;
	  @Expose
	  private int unit;
	  @Expose
	  private int times;
	  @Expose
	  private double price;

	  int maxMinutes ()
	  {
		    return times == -1 ?
				times :
				toMinutes(getUnit(), times * quantity);
	  }

	  public double calculateCharge (TimeUnits unit, int quantity, int from)
	  {
		    int proportionality = 0;
		    try
		    {
				 if (times != -1 && quantity >= toMinutes(getUnit(), this.quantity * times))
					   return price * times;
				 int realTime = GetTimeOnMyUnit(unit, getUnit(), quantity - from);
				 double raw = (double) realTime / (double) this.quantity;
				 proportionality = MathUtils.upperRound(raw);
		    }
		    catch (Exception e)
		    {
				 e.printStackTrace();
		    }
		    return proportionality * price;
	  }

	  public double calculateCharge (TimeUnits unit, int quantity)
	  {
		    int proportionality = 0;
		    try
		    {
				 if (times != -1 && quantity >= toMinutes(getUnit(), this.quantity * times))
					   return price * times;
				 int realTime = GetTimeOnMyUnit(unit, getUnit(), quantity);
				 double raw = (double) realTime / (double) this.quantity;
				 proportionality = MathUtils.upperRound(raw);
		    }
		    catch (Exception e)
		    {
				 e.printStackTrace();
		    }
		    return proportionality * price;
	  }

	  int getMinutesProportionality (int quantity)
	  {
		    int realQuantity = toMinutes(getUnit(), this.quantity);
		    double raw = (double) quantity / (double) realQuantity;
		    int proportionality = MathUtils.upperRound(raw);
		    return proportionality * realQuantity;
	  }

	  private int GetTimeOnMyUnit (TimeUnits from, TimeUnits to, int quantity) throws Exception
	  {
		    if (from == to) return quantity;
		    switch (to)
		    {
				 case minutes:
					   return toMinutes(from, quantity);
				 case hours:
					   return toHours(from, quantity);
				 case days:
					   return toDays(from, quantity);
				 case weeks:
					   return toWeeks(from, quantity);
				 case months:
					   return toMonths(from, quantity);
				 default:
					   throw new Exception("Conversión de unidad de medida no soportada");
		    }
	  }

	  public int getScheduleId ()
	  {
		    return scheduleId;
	  }

	  public void setScheduleId (int scheduleId)
	  {
		    this.scheduleId = scheduleId;
	  }

	  public int getQuantity ()
	  {
		    return quantity;
	  }

	  public void setQuantity (int quantity)
	  {
		    this.quantity = quantity;
	  }

	  public TimeUnits getUnit ()
	  {
		    return TimeUnits.values()[unit];
	  }

	  public void setUnit (TimeUnits unit)
	  {
		    this.unit = unit.ordinal();
	  }

	  public int getTimes ()
	  {
		    return times;
	  }

	  public void setTimes (int times)
	  {
		    this.times = times;
	  }

	  public double getPrice ()
	  {
		    return price;
	  }

	  public void setPrice (double price)
	  {
		    this.price = price;
	  }
}
