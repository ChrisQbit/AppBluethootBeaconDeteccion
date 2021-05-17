package com.cdsautomatico.apparkame2.activities.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdsautomatico.apparkame2.models.enums.OnboardingPages;

public class OnboardingAdapter extends PagerAdapter
{
	  private Context ctx;

	  public OnboardingAdapter (Context ctx)
	  {
		    this.ctx = ctx;
	  }

	  @NonNull
	  @Override
	  public Object instantiateItem (@NonNull ViewGroup container, int position)
	  {
		    LayoutInflater inflater = LayoutInflater.from(ctx);
		    OnboardingPages page = OnboardingPages.values()[position];
		    View view = inflater.inflate(page.getLayoutResId(), container, false);
		    container.addView(view);
		    return view;
	  }


	  @Override
	  public void destroyItem(ViewGroup collection, int position, @NonNull Object view) {
		    collection.removeView((View) view);
	  }

	  @Override
	  public int getCount ()
	  {
		    return OnboardingPages.values().length;
	  }

	  @Override
	  public CharSequence getPageTitle(int position) {
		    OnboardingPages customPagerEnum = OnboardingPages.values()[position];
		    return  null;//customPagerEnum.getTitleResId();
	  }

	  @Override
	  public boolean isViewFromObject (@NonNull View view, @NonNull Object object)
	  {
		    return view == object;
	  }
}
