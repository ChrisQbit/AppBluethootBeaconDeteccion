package com.cdsautomatico.apparkame2.activities;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.cdsautomatico.apparkame2.R;
import com.cdsautomatico.apparkame2.activities.adapters.OnboardingAdapter;
import com.cdsautomatico.apparkame2.repository.UserPreferences;

public class OnboardingActivity extends AppCompatActivity
{
	  @Override
	  protected void onCreate (Bundle savedInstanceState)
	  {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.activity_onboarding);
		    ViewPager pager = findViewById(R.id.pager);
		    TabLayout tabLayout = findViewById(R.id.transition_position);
		    pager.setAdapter(new OnboardingAdapter(this));
		    tabLayout.setupWithViewPager(pager, true);
	  }

	  public void close (View view)
	  {
		    UserPreferences userPreferences = new UserPreferences(this);
		    userPreferences.setShowOnboarding(false);
		    finish();
	  }
}
