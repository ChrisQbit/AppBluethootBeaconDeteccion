package com.cdsautomatico.apparkame2.repository;

import android.content.Context;
import android.content.SharedPreferences;

abstract class PreferencesManager<T>
{
	  private static final String PREFERENCES_NAME = "ApparkameMap";

	  SharedPreferences preferences;

	  PreferencesManager (Context ctx)
	  {
		    this.preferences = ctx.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
	  }

	  public abstract void save(T value);

	  public abstract T getSaved();

}
