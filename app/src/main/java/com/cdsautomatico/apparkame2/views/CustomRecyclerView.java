package com.cdsautomatico.apparkame2.views;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

public class CustomRecyclerView extends RecyclerView
{

	  public CustomRecyclerView (Context context, AttributeSet attrs)
	  {
		    super(context, attrs);
	  }

	  public CustomRecyclerView (Context context)
	  {
		    super(context);
	  }

	  public CustomRecyclerView (Context context, AttributeSet attrs, int defStyle)
	  {
		    super(context, attrs, defStyle);
	  }

	  @Override
	  public void onMeasure (int widthMeasureSpec, int heightMeasureSpec)
	  {
		    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		    super.onMeasure(widthMeasureSpec, expandSpec);
	  }

}