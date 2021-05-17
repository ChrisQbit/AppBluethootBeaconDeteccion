package com.cdsautomatico.apparkame2.utils.dateTime;

import androidx.annotation.NonNull;

import com.cdsautomatico.apparkame2.utils.MathUtils;

public class TimeSpan implements Comparable<TimeSpan>
{
	  public static TimeSpan MaxValue = new TimeSpan("23:59:59");
	  public static TimeSpan MinValue = new TimeSpan("00:00:00");
	  private int hour, minute, second;

	  public TimeSpan (String timeSpan)
	  {
		    String[] splittedTimeSpan = timeSpan.split(":");
		    hour = Integer.parseInt(splittedTimeSpan[0]);
		    minute = Integer.parseInt(splittedTimeSpan[1]);
		    String seconds = splittedTimeSpan[2].split("\\.").length < 2 ?
				splittedTimeSpan[2] : splittedTimeSpan[2].split("\\.")[0];
		    second = Integer.parseInt(seconds);
	  }

	  public long totalSeconds ()
	  {
		    return hour * 3600 + minute * 60 + second;
	  }

	  public int getHour ()
	  {
		    return hour;
	  }

	  public void setHour (int hour)
	  {
		    this.hour = hour;
	  }

	  public int getMinute ()
	  {
		    return minute;
	  }

	  public void setMinute (int minute)
	  {
		    this.minute = minute;
	  }

	  public int getSecond ()
	  {
		    return second;
	  }

	  public void setSecond (int second)
	  {
		    this.second = second;
	  }

	  @Override
	  public int compareTo (@NonNull TimeSpan timeSpan)
	  {
		    return Long.compare(totalSeconds(), timeSpan.totalSeconds());
	  }

	  public TimeSpan substract (TimeSpan otherTime)
	  {
		    long seconds = totalSeconds() - otherTime.totalSeconds();
		    long hours = seconds / 3600;
		    long minutes = (seconds % 3600) / 60;
		    long finalSeconds = seconds - ((hours * 3600) + (minutes * 60));
		    String hourString = hours + ":" + minutes + ":" + finalSeconds;
		    return new TimeSpan(hourString);
	  }

	  public Integer totalMinutes ()
	  {
		    return MathUtils.upperRound(((double) totalSeconds()) / 60);
	  }

	  public String getShowTime ()
	  {
		    return hour + ":" + minute + ":" + second;
	  }

	  @Override
	  public String toString ()
	  {
		    return getShowTime();
	  }
}
