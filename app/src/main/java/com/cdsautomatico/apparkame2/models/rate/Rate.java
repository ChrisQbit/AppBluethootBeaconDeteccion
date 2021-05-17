package com.cdsautomatico.apparkame2.models.rate;

import android.util.LongSparseArray;

import com.cdsautomatico.apparkame2.models.Entity;
import com.cdsautomatico.apparkame2.models.enums.TimeUnits;
import com.cdsautomatico.apparkame2.utils.TimeUtils;
import com.cdsautomatico.apparkame2.utils.dateTime.Date;
import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Rate extends Entity<Double>
{
	  @Expose
	  private String name;
	  @Expose
	  private long parkingId;
	  @Expose
	  private long cdsrateId;
	  @Expose
	  private int type;
	  @Expose
	  private int paymentTolerance;
	  @Expose
	  private double maxAmount;
	  @Expose
	  private boolean appliesAntiPassback;
	  @Expose
	  private List<Category> categories;

	  public double calculateCharge (TimeUnits unit, int quantity, String start, String end) throws Exception
	  {
		    if (categories.size() != 1)
				 throw new Exception("Solo se puede calcular la tarifa con una sola categoría.");
		    double charge = 0;
		    Category category = categories.get(0);
		    Date startDate = new Date(start);
		    Date endDate = new Date(end);
		    HashMap<String, Integer> minutesPerDay = TimeUtils.splitMinutesPerDay(start, end);
		    List<String> days = Arrays.asList(minutesPerDay.keySet().toArray(new String[] { }));
		    Collections.sort(days);
		    int realTime = TimeUtils.toMinutes(unit, quantity);
		    int elapsed = 0;
		    Log("Minutos reales a calcular {" + realTime + "}");
		    LongSparseArray<LongSparseArray<Integer>> elapsedMinutesPerRate = new LongSparseArray<>();
		    for (int i = 0; i < days.size(); i++)
		    {
				 String day = days.get(i);
				 for (int j = 0; j < category.getSchedules().size() && elapsed < realTime; j++)
				 {
					   RateSchedule rateSchedule = category.getSchedules().get(j);
					   if ((startDate.getDate().equals(day) && startDate.getTime().compareTo(rateSchedule.getEndTime()) >= 0) ||
						    (endDate.getDate().equals(day) && endDate.getTime().compareTo(rateSchedule.getStartTime()) <= 0))
							continue;
					   int minutesOnSchedule =
						    startDate.getDate().equals(day) ?
								endDate.getDate().equals(day) ?
									 endDate.getTime().compareTo(rateSchedule.getEndTime()) >= 0 ?
										  rateSchedule.getEndTime().substract(startDate.getTime()).totalMinutes() :
										  rateSchedule.getEndTime().substract(startDate.getTime()).totalMinutes() - rateSchedule.getEndTime().substract(endDate.getTime()).totalMinutes() :
									 rateSchedule.getEndTime().substract(startDate.getTime()).totalMinutes() :
								endDate.getDate().equals(day) ?
									 endDate.getTime().compareTo(rateSchedule.getEndTime()) >= 0 ?
										  rateSchedule.getEndTime().totalMinutes() :
										  endDate.getTime().totalMinutes() - rateSchedule.getStartTime().totalMinutes() :
									 rateSchedule.getEndTime().totalMinutes() - rateSchedule.getStartTime().totalMinutes();
					   Log("Minutos para horario [" + j + "] {" + minutesOnSchedule + "}");
					   for (int k = rateSchedule.getRuleIndex(elapsed); k < rateSchedule.getRules().size() && minutesOnSchedule > 0 && elapsed < realTime; k++)
					   {
							Rule rule = rateSchedule.getRules().get(k);
							if (k == 0 && realTime <= TimeUtils.toMinutes(rule.getUnit(), rule.getQuantity()))
							{
								  charge = rule.calculateCharge(TimeUnits.minutes, realTime);
								  return charge;
							}
							int lastEndingMinutePaid = 0;
							if (rule.getTimes() > 0 && elapsedMinutesPerRate.get(rateSchedule.getId()) != null && elapsedMinutesPerRate.get(rateSchedule.getId()).get(rule.getId()) != null)
							{
								  lastEndingMinutePaid = elapsedMinutesPerRate.get(rateSchedule.getId()).get(rule.getId());
							}
							int minutesToPayOnRule = (minutesOnSchedule > rule.maxMinutes() && rule.getTimes() > 0 ?
								 rule.maxMinutes() :
								 minutesOnSchedule) - (lastEndingMinutePaid > 0 ? lastEndingMinutePaid : 0);
							Log("Minutos para regla {" + minutesToPayOnRule + "}");
							if (minutesToPayOnRule <= 0)
							{
								  //minutesOnSchedule = 0;
								  //Log($"No hay más minutos por cobrar");
								  continue;
							}
							double partial = rule.calculateCharge(TimeUnits.minutes, minutesToPayOnRule);
							charge += partial;
							Log("Cargo de regla {" + k + "} ->> {" + partial + "}");
							minutesOnSchedule -= minutesToPayOnRule;
							elapsed += minutesToPayOnRule;
							if (elapsedMinutesPerRate.get(rateSchedule.getId()) == null)
							{
								  elapsedMinutesPerRate.put(rateSchedule.getId(), new LongSparseArray<>());
							}
							if (elapsedMinutesPerRate.get(rateSchedule.getId()).get(rule.getId()) == null)
							{
								  elapsedMinutesPerRate.get(rateSchedule.getId()).put(rule.getId(), rule.getMinutesProportionality(minutesToPayOnRule));
							}
					   }
				 }
		    }
		    return charge > maxAmount ? maxAmount : charge;
	  }

	  private void Log (String message)
	  {
		    System.out.println("Rate->CalculateCharge : " + message);
	  }

	  public String getName ()
	  {
		    return name;
	  }

	  public void setName (String name)
	  {
		    this.name = name;
	  }

	  public List<Category> getCategories ()
	  {
		    return categories;
	  }

	  public long getParkingId ()
	  {
		    return parkingId;
	  }

	  public void setParkingId (long parkingId)
	  {
		    this.parkingId = parkingId;
	  }

	  public long getCdsrateId ()
	  {
		    return cdsrateId;
	  }

	  public void setCdsrateId (long cdsrateId)
	  {
		    this.cdsrateId = cdsrateId;
	  }

	  public int getType ()
	  {
		    return type;
	  }

	  public void setType (int type)
	  {
		    this.type = type;
	  }

	  public int getPaymentTolerance ()
	  {
		    return paymentTolerance;
	  }

	  public void setPaymentTolerance (int paymentTolerance)
	  {
		    this.paymentTolerance = paymentTolerance;
	  }

	  public double getMaxAmount ()
	  {
		    return maxAmount;
	  }

	  public void setMaxAmount (double maxAmount)
	  {
		    this.maxAmount = maxAmount;
	  }

	  public boolean isAppliesAntiPassback ()
	  {
		    return appliesAntiPassback;
	  }

	  public void setAppliesAntiPassback (boolean appliesAntiPassback)
	  {
		    this.appliesAntiPassback = appliesAntiPassback;
	  }

	  public void setCategories (List<Category> categories)
	  {
		    this.categories = categories;
	  }
}
