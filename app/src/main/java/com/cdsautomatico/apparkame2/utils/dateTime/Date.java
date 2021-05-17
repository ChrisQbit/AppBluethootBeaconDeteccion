package com.cdsautomatico.apparkame2.utils.dateTime;

import androidx.annotation.NonNull;

import com.cdsautomatico.apparkame2.utils.MathUtils;

import java.util.Calendar;

public class Date implements Comparable<Date>
{
	  private int day, month, year;
	  private TimeSpan timeSpan;

	  public Date (int day, int month, int year, TimeSpan timeSpan)
	  {
		    this.day = day;
		    this.month = month;
		    this.year = year;
		    this.timeSpan = timeSpan;
	  }

	  public Date (String dateString)
	  {
		    setFrom(dateString);
	  }

	  public void setFrom (String dateString)
	  {
		    String[] splittedDate = dateString.split(" ");
		    if (splittedDate.length < 2)
		    {
				 splittedDate = dateString.split("T");
				 String[] date = splittedDate[0].split("-");
				 day = Integer.parseInt(date[2]);
				 month = Integer.parseInt(date[1]);
				 year = Integer.parseInt(date[0]);
				 timeSpan = new TimeSpan(splittedDate.length < 2 ?
					  TimeSpan.MinValue.getShowTime() : splittedDate[1]);
		    }
		    else
		    {
				 String[] date = splittedDate[0].split("-");
				 day = Integer.parseInt(date[2]);
				 month = Integer.parseInt(date[1]);
				 year = Integer.parseInt(date[0]);
				 timeSpan = new TimeSpan(splittedDate[1]);
		    }
	  }

	  public static Date parse (String dateString)
	  {
		    return new Date(dateString);
	  }

	  public long totalSeconds ()
	  {
		    Calendar date = Calendar.getInstance();
		    date.set(year, month - 1, day, timeSpan.getHour(), timeSpan.getMinute(), timeSpan.getSecond());
		    return date.getTimeInMillis() / 1000;
	  }

	  public Date substract (Date otherDate)
	  {
		    long time = (totalSeconds() * 1000) - (otherDate.totalSeconds() * 1000);
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTimeInMillis(time);
		    String dateString = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " +
				calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
		    return new Date(dateString);
	  }

	  @NonNull
	  @Override
	  public String toString ()
	  {
		    return getDate() + " " + getTime().getShowTime();
	  }

	  @Override
	  public int compareTo (@NonNull Date date)
	  {
		    return Long.compare(totalSeconds(), date.totalSeconds());
	  }

	  public TimeSpan getTime ()
	  {
		    return timeSpan;
	  }

	  public String getDate ()
	  {
		    return year + "-" + month + "-" + day;
	  }

	  public int totalDays ()
	  {
		    Calendar date = Calendar.getInstance();
		    date.setTimeInMillis(totalSeconds() * 1000);
		    return MathUtils.upperRound(date.getTimeInMillis() / (double) 1000 / (double) 60 / (double) 60 / (double) 24);
	  }

	  public Date addDays (int days)
	  {
		    Calendar calendar = Calendar.getInstance();
		    long timeInMillis = (totalSeconds() * 1000) + (days * 24 * 60 * 60 * 1000);
		    calendar.setTimeInMillis(timeInMillis);
		    String dateString = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " +
				calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
		    return new Date(dateString);
	  }

	  public void addMinutes (int minutes)
	  {
		    Calendar calendar = Calendar.getInstance();
		    long timeInMillis = (totalSeconds() * 1000) + (minutes * 60 * 1000);
		    calendar.setTimeInMillis(timeInMillis);
		    String dateString = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH) + " " +
				calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
		    setFrom(dateString);
	  }

	  public String formatted ()
	  {
		    Calendar calendar = Calendar.getInstance();
		    long timeInMillis = (totalSeconds() * 1000);
		    calendar.setTimeInMillis(timeInMillis);
		    return null;
	  }
}
