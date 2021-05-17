package com.cdsautomatico.apparkame2.utils;

import com.cdsautomatico.apparkame2.models.enums.TimeUnits;
import com.cdsautomatico.apparkame2.utils.dateTime.Date;
import com.cdsautomatico.apparkame2.utils.dateTime.TimeSpan;

import java.util.HashMap;

public class TimeUtils
{
	  public static HashMap<String, Integer> splitMinutesPerDay (String start, String end)
	  {
		    HashMap<String, Integer> minutesPerDay = new HashMap<>();
		    Date startDate = new Date(start);
		    Date endDate = new Date(end);
		    int days = totalDays(startDate, endDate);
		    TimeSpan lastHourOfDay = TimeSpan.MaxValue;
		    TimeSpan firstDayMinutes = lastHourOfDay.substract(startDate.getTime());
		    minutesPerDay.put(startDate.getDate(), firstDayMinutes.totalMinutes());
		    Date tmpDate = startDate;
		    for (int i = 0; i < days - 2; i++)
		    {
				 tmpDate = tmpDate.addDays(1);
				 minutesPerDay.put(tmpDate.getDate(), 1440);
		    }
		    if (days > 1)
				 minutesPerDay.put(endDate.getDate(), endDate.getTime().totalMinutes());
		    return minutesPerDay;
	  }

	  private static int totalDays (Date startDate, Date endDate)
	  {
		    int days = 1;
		    Date tmpStart = new Date(startDate.getDate());
		    Date tmpEnd = new Date(endDate.getDate());
		    while (!tmpEnd.getDate().equals(tmpStart.getDate()))
		    {
				 days++;
				 tmpStart = tmpStart.addDays(1);
		    }
		    return days;
	  }

	  public static int toMinutes (TimeUnits timeUnits, int quantity)
	  {
		    int minutes = 0;
		    switch (timeUnits)
		    {
				 case seconds:
				 {
					   double raw = (double) quantity / 60.0;
					   minutes = MathUtils.upperRound(raw);
				 }
				 break;
				 case minutes:
				 {
					   minutes = quantity;
				 }
				 break;
				 case hours:
				 {
					   minutes = quantity * 60;
				 }
				 break;
				 case days:
				 {
					   minutes = quantity * 60 * 24;
				 }
				 break;
				 case weeks:
				 {
					   minutes = quantity * 60 * 24 * 7;
				 }
				 break;
				 case months:
				 {
					   minutes = (int) (quantity * 60 * 24 * 30.5);//(DateTime.Now.DaysFrom(DateTime.Now.Month)));
				 }
				 break;
				 case years:
				 {
					   minutes = quantity * 60 * 24 * 365;
				 }
				 break;
		    }
		    return minutes;
	  }

	  public static int toHours (TimeUnits timeUnits, int quantity)
	  {
		    int hours = 0;
		    switch (timeUnits)
		    {
				 case seconds:
				 {
					   double raw = (double) quantity / 60.0 / 60.0;
					   hours = MathUtils.upperRound(raw);
				 }
				 break;
				 case minutes:
				 {
					   double raw = (double) quantity / 60.0;
					   hours = MathUtils.upperRound(raw);
				 }
				 break;
				 case hours:
				 {
					   hours = quantity;
				 }
				 break;
				 case days:
				 {
					   hours = quantity * 24;
				 }
				 break;
				 case weeks:
				 {
					   hours = quantity * 24 * 7;
				 }
				 break;
				 case months:
				 {
					   hours = (int) (quantity * 24 * 30.5);//(DateTime.Now.DaysFrom(DateTime.Now.Month)));
				 }
				 break;
				 case years:
				 {
					   hours = quantity * 365;
				 }
				 break;
		    }
		    return hours;
	  }

	  public static int toDays (TimeUnits timeUnits, int quantity)
	  {
		    int days = 0;
		    switch (timeUnits)
		    {
				 case seconds:
				 {
					   double raw = (double) quantity / 24.0 / 60.0 / 60.0;
					   days = MathUtils.upperRound(raw);
				 }
				 break;
				 case minutes:
				 {
					   double raw = (double) quantity / 60.0 / 60.0;
					   days = MathUtils.upperRound(raw);
				 }
				 break;
				 case hours:
				 {
					   double raw = (double) quantity / 24.0;
					   days = MathUtils.upperRound(raw);
				 }
				 break;
				 case days:
				 {
					   days = quantity;
				 }
				 break;
				 case weeks:
				 {
					   days = quantity * 7;
				 }
				 break;
				 case months:
				 {
					   days = (int) (quantity * 30.5);//(DateTime.Now.DaysFrom(DateTime.Now.Month)));
				 }
				 break;
				 case years:
				 {
					   days = quantity * 365;
				 }
				 break;
		    }
		    return days;
	  }

	  public static int toWeeks (TimeUnits timeUnits, int quantity)
	  {
		    int weeks = 0;
		    switch (timeUnits)
		    {
				 case seconds:
				 {
					   double raw = (double) quantity / 7.0 / 24.0 / 60.0 / 60.0;
					   weeks = MathUtils.upperRound(raw);
				 }
				 break;
				 case minutes:
				 {
					   double raw = (double) quantity / 7.0 / 24.0 / 60.0;
					   weeks = MathUtils.upperRound(raw);
				 }
				 break;
				 case hours:
				 {
					   double raw = (double) quantity / 7.0 / 24.0;
					   weeks = MathUtils.upperRound(raw);
				 }
				 break;
				 case days:
				 {
					   double raw = (double) quantity / 7.0;
					   weeks = MathUtils.upperRound(raw);
				 }
				 break;
				 case weeks:
				 {
					   weeks = quantity;
				 }
				 break;
				 case months:
				 {
					   weeks = quantity * 4;
				 }
				 break;
				 case years:
				 {
					   weeks = quantity * 52;
				 }
				 break;
		    }
		    return weeks;
	  }

	  public static int toMonths (TimeUnits timeUnits, int quantity)
	  {
		    int months = 0;
		    switch (timeUnits)
		    {
				 case seconds:
				 {
					   double raw = (double) quantity / 30.5 / 24.0 / 60.0 / 60.0;
					   months = MathUtils.upperRound(raw);
				 }
				 break;
				 case minutes:
				 {
					   double raw = (double) quantity / 30.5 / 24.0 / 60.0;
					   months = MathUtils.upperRound(raw);
				 }
				 break;
				 case hours:
				 {
					   double raw = (double) quantity / 24.0 / 30.5;
					   months = MathUtils.upperRound(raw);
				 }
				 break;
				 case days:
				 {
					   double raw = (double) quantity / 30.5;
					   months = MathUtils.upperRound(raw);
				 }
				 break;
				 case weeks:
				 {
					   double raw = (double) quantity / 4.0;
					   months = MathUtils.upperRound(raw);
				 }
				 break;
				 case months:
				 {
					   months = quantity;
				 }
				 break;
				 case years:
				 {
					   months = quantity * 52;
				 }
				 break;
		    }
		    return months;
	  }
}
