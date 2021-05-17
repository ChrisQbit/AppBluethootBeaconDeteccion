package com.cdsautomatico.apparkame2.models.rate;

import com.cdsautomatico.apparkame2.models.Entity;
import com.cdsautomatico.apparkame2.models.Schedule;
import com.cdsautomatico.apparkame2.utils.dateTime.TimeSpan;
import com.google.gson.annotations.Expose;

import java.util.List;

public class RateSchedule extends Schedule
{
	  @Expose
	  private int categoryId;
	  @Expose
	  private List<Rule> rules;

	  public int getRuleIndex (int fromMinute)
	  {
		    int elapsed = 0;
		    for (int i = 0; i < rules.size(); i++)
		    {
				 if (rules.get(i).maxMinutes() == -1 || rules.get(i).maxMinutes() + elapsed > fromMinute)
					   return i;
				 elapsed += rules.get(i).maxMinutes();
		    }
		    return rules.size() - 1;
	  }

	  public int getCategoryId ()
	  {
		    return categoryId;
	  }

	  public void setCategoryId (int categoryId)
	  {
		    this.categoryId = categoryId;
	  }

	  public String getStart ()
	  {
		    return startTime;
	  }

	  public void setStartTime (String startTime)
	  {
		    this.startTime = startTime;
	  }

	  public TimeSpan getStartTime ()
	  {
		    return new TimeSpan(startTime);
	  }

	  public TimeSpan getEndTime ()
	  {
		    return new TimeSpan(endTime);
	  }

	  public String getEnd ()
	  {
		    return endTime;
	  }

	  public void setEndTime (String endTime)
	  {
		    this.endTime = endTime;
	  }

	  public List<Rule> getRules ()
	  {
		    return rules;
	  }

	  public void setRules (List<Rule> rules)
	  {
		    this.rules = rules;
	  }
}
