package com.cdsautomatico.apparkame2.utils;

import com.cdsautomatico.apparkame2.models.enums.TimeUnits;

import java.util.Calendar;

public class CalendarManager
{
	  private Calendar calendar;

	  public String roll (String actualExpirationDate, int quantity, TimeUnits cycleUnit)
	  {
		    calendar = Calendar.getInstance();
		    String[] dateSplit = actualExpirationDate.replace('T', ' ').split(" ")[0]
				.replace("/", "-").split("-");
		    int year = Integer.parseInt(dateSplit[0]);
		    int month = Integer.parseInt(dateSplit[1]);
		    int day = Integer.parseInt(dateSplit[2]);
		    calendar.set(year, month, day);

		    int field = 0;

		    if (cycleUnit == TimeUnits.days)
		    {
				 field = Calendar.DAY_OF_MONTH;
		    }
		    else if (cycleUnit == TimeUnits.months)
		    {
				 field = Calendar.MONTH;
		    }
		    else if (cycleUnit == TimeUnits.minutes)
		    {
				 field = Calendar.MINUTE;
		    }
		    else if (cycleUnit == TimeUnits.weeks)
		    {
				 field = Calendar.WEEK_OF_YEAR;
		    }

		    calendar.roll(field, quantity);
		    return toUserDate();
	  }


	  public static String format (String date)
	  {
		    String[] dateSplit = date.replace('T', ' ').split(" ")[0]
				.replace("/", "-").split("-");
		    int year = Integer.parseInt(dateSplit[0]);
		    int month = Integer.parseInt(dateSplit[1]);
		    int day = Integer.parseInt(dateSplit[2]);
		    return day + "/" + month + "/" + year;
	  }

	  public String toUserDate ()
	  {
		    return calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
	  }
}
