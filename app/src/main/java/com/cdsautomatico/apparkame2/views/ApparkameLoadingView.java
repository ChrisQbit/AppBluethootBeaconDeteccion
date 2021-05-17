package com.cdsautomatico.apparkame2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.cdsautomatico.apparkame2.R;

/**
 * Created by alangvara on 02/05/18.
 */

public class ApparkameLoadingView extends View
{

	  static private float FPS = 60f;

	  private static final String TAG = "ApparkameLoadingView";

	  static
	  {
		    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
		    {
				 FPS = 30;
		    }
	  }

	  private Paint paintArc1;
	  private Paint paintArc2;
	  private float paddingTop = 0;
	  private float animationDuration = 1000;
	  private float dpi;
	  private boolean animating = false;
	  private float elapsedTime = 0;


	  // CIRCLES ANIMATION

	  private Paint paintCircles;
	  private float circle1BaseRadius;
	  private float circle2BaseRadius;
	  private float circle3BaseRadius;
	  private float circle1GrowRadius;

	  // ARCS ANIMATION

	  private float arc1BaseRadius;


	  public ApparkameLoadingView (Context context)
	  {
		    super(context);
		    init(null);
	  }

	  public ApparkameLoadingView (Context context, AttributeSet attrs)
	  {
		    super(context, attrs);
		    init(attrs);
	  }

	  public ApparkameLoadingView (Context context, AttributeSet attrs, int defStyle)
	  {
		    super(context, attrs, defStyle);
		    init(attrs);
	  }

	  private void init (AttributeSet attrs)
	  {
		    if (attrs != null)
		    {

				 TypedArray typedArray = getContext().getTheme()
					  .obtainStyledAttributes(attrs, R.styleable.ApparkameLoadingView, 0, 0);

				 try
				 {
					   paddingTop = typedArray.getDimension(R.styleable.ApparkameLoadingView_paddingTop, 0);
				 }
				 catch (Exception e)
				 {

				 }
				 finally
				 {
					   typedArray.recycle();
				 }
		    }

		    dpi = getResources().getDisplayMetrics().density;

		    // CIRCLES ANIMATION

		    paintCircles = new Paint();
		    paintCircles.setStyle(Paint.Style.FILL);
		    paintCircles.setColor(getContext().getResources().getColor(R.color.colorAccent));
		    paintCircles.setAlpha(60);

		    circle1BaseRadius = (50f * dpi);
		    circle2BaseRadius = (75f * dpi);
		    circle3BaseRadius = (100f * dpi);

		    circle1GrowRadius = (90f * dpi);

		    // ARCS ANIMATION

		    paintArc1 = new Paint();
		    paintArc1.setColor(getContext().getResources().getColor(R.color.colorAccent));
		    paintArc1.setStrokeWidth(5f * dpi);
		    paintArc1.setAlpha(100);
		    paintArc1.setStyle(Paint.Style.FILL);
		    paintArc1.setStyle(Paint.Style.STROKE);

		    arc1BaseRadius = (100f * dpi);
	  }

	  public void startAnimation ()
	  {
		    animating = true;
		    invalidate();
	  }

	  public void stopAnimation ()
	  {
		    animating = false;
		    invalidate();
	  }

	  @Override
	  protected void onDraw (Canvas canvas)
	  {
		    super.onDraw(canvas);

		    if (!animating)
		    {
				 elapsedTime = 0;
				 return;
		    }

		    if (elapsedTime >= animationDuration)
		    {
				 elapsedTime = 0;
		    }

		    drawCiclesAnimation(canvas);
		    drawArcAnimation(canvas);

		    this.postInvalidateDelayed((long) (1000.0f / FPS));

		    elapsedTime += (1000.0f / FPS);

	  }

	  private void drawCiclesAnimation (Canvas canvas)
	  {
		    float circle1Radius;
		    float circle2Radius;
		    float circle3Radius;

		    if (elapsedTime < (animationDuration * 0.7f))
		    {
				 float progress = elapsedTime / (animationDuration * 0.7f);
				 circle1Radius = circle1BaseRadius + (circle1GrowRadius * progress);
				 circle2Radius = circle2BaseRadius + (circle1GrowRadius * progress);
				 circle3Radius = circle3BaseRadius + (circle1GrowRadius * progress);

				 paintCircles.setAlpha(75);

//            Log.v(TAG, "progress start: " + Float.toString(progress));
		    }
		    else
		    {
				 float progress = (elapsedTime - (animationDuration * 0.7f)) / (animationDuration * 0.3f);

				 circle1Radius = circle1BaseRadius + circle1GrowRadius - (circle1GrowRadius * (progress * 0.5f));
				 circle2Radius = circle2BaseRadius + circle1GrowRadius - (circle1GrowRadius * (progress * 0.5f));
				 circle3Radius = circle3BaseRadius + circle1GrowRadius - (circle1GrowRadius * (progress * 0.5f));

				 paintCircles.setAlpha(75);

//            Log.v(TAG, "progress end: " + Float.toString(progress));
		    }


		    float xPos = getWidth() / 2f;
		    float yPos = ((getHeight() - paddingTop) / 2f) + paddingTop;

		    canvas.drawCircle(xPos, yPos, circle3Radius, paintCircles);
		    canvas.drawCircle(xPos, yPos, circle2Radius, paintCircles);
		    canvas.drawCircle(xPos, yPos, circle1Radius, paintCircles);
	  }

	  private void drawArcAnimation (Canvas canvas)
	  {
//        float maxArcRadius = (getHeight() > getWidth() ? getHeight() / 2 : getWidth() / 2);
		    float maxArcRadius = (320f * dpi);
		    float center_x = getWidth() / 2f;
		    float center_y = ((getHeight() - paddingTop) / 2f) + paddingTop;
		    float progress = elapsedTime / animationDuration;
		    float radius = arc1BaseRadius + ((maxArcRadius - arc1BaseRadius) * progress);
		    float radius2 = arc1BaseRadius + ((maxArcRadius - arc1BaseRadius) * progress * 0.75f);

		    paintArc1.setAlpha(75);

		    final RectF oval = new RectF();
		    oval.set(center_x - radius,
				center_y - radius,
				center_x + radius,
				center_y + radius);

		    canvas.drawArc(oval, 0, 360, true, paintArc1);

		    final RectF oval2 = new RectF();
		    oval2.set(center_x - radius2,
				center_y - radius2,
				center_x + radius2,
				center_y + radius2);

		    canvas.drawArc(oval2, 0, 360, true, paintArc1);
	  }

	  public boolean isRunning ()
	  {
		    return animating;
	  }
}
