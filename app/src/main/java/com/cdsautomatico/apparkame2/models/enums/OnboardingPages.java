package com.cdsautomatico.apparkame2.models.enums;

import com.cdsautomatico.apparkame2.R;

public enum OnboardingPages
{
	  ENTRY(R.layout.fgmt_onboarding_1),
	  PAYMENT(R.layout.fgmt_onboarding_2),
	  ACCESS_CONTROL(R.layout.fgmt_onboarding_3),
	  SALIDA(R.layout.fgmt_onboarding_4);


	  private int mLayoutResId;

	  OnboardingPages(int layoutResId) {
		    mLayoutResId = layoutResId;
	  }

	  public int getLayoutResId() {
		    return mLayoutResId;
	  }
}
