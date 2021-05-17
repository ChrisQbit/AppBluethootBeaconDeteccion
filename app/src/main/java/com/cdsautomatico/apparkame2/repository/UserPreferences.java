package com.cdsautomatico.apparkame2.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.cdsautomatico.apparkame2.models.User;
import com.cdsautomatico.apparkame2.utils.Constant;

public class UserPreferences extends PreferencesManager<User>
{
	  public UserPreferences (Context ctx)
	  {
		    super(ctx);
	  }

	  private String getToken ()
	  {
		    return preferences.getString(Constant.Extra.TOKEN, null);
	  }

	  private String getUser ()
	  {
		    return preferences.getString(Constant.Extra.USER, null);
	  }

	  @Override
	  public void save (User value)
	  {
		    if (value == null)
		    {
				 preferences.edit()
					  .remove(Constant.Extra.TOKEN)
					  .remove(Constant.Extra.USER).apply();
				 return;
		    }
		    SharedPreferences.Editor editor = preferences.edit();
		    editor.putString(Constant.Extra.TOKEN, value.getToken());
		    editor.putString(Constant.Extra.USER, value.getUser());
		    editor.apply();
	  }

	  public boolean showOnboarding ()
	  {
		    return preferences.getBoolean(Constant.Extra.SHOW_ONBOARDING, true);
	  }

	  public void setShowOnboarding (boolean show)
	  {
		    preferences.edit()
				.putBoolean(Constant.Extra.SHOW_ONBOARDING, show)
				.apply();
	  }

	  public void setLastOccupation (String lugar)
	  {
		    SharedPreferences.Editor editor = preferences.edit();
		    editor.putString(Constant.Extra.PLACE, lugar);
		    editor.apply();
	  }

	  public String getLastOccupation ()
	  {
		    return preferences.getString(Constant.Extra.PLACE, null);
	  }

	  public void setLock (boolean lock)
	  {
		    SharedPreferences.Editor editor = preferences.edit();
		    editor.putBoolean(Constant.Extra.LOCK, lock);
		    editor.apply();
	  }

	  public boolean isLocked ()
	  {
		    return preferences.getBoolean(Constant.Extra.LOCK, false);
	  }

	  @Override
	  public User getSaved ()
	  {
		    return getToken() != null && getUser() != null ?
				new User(getUser(), getToken()) : null;
	  }
}
