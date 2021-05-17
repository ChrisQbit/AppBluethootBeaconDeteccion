package com.cdsautomatico.apparkame2.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.cdsautomatico.apparkame2.R;

/**
 * Created by alangvara on 26/04/18.
 */

public class ApparkameTimerView extends View {

    private float startAngle;
    private float progress;
    private boolean clockwise;
    private float strokeWidth;
    private int strokeColor;
    private float strokeShadowWidth;
    private int strokeShadowColor;
    private float dpi;

    public ApparkameTimerView(Context context){
        super(context);
        init(null);
    }

    public ApparkameTimerView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(attrs);
    }

    public ApparkameTimerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        if(attrs == null){
            return;
        }

        TypedArray typedArray = getContext().getTheme()
                .obtainStyledAttributes(attrs, R.styleable.ApparkameTimerView, 0, 0);

        try {
            startAngle = typedArray.getFloat(R.styleable.ApparkameTimerView_startAngle, 0);
            progress = typedArray.getFloat(R.styleable.ApparkameTimerView_progress, 0);
            clockwise = typedArray.getBoolean(R.styleable.ApparkameTimerView_clockwise, true);
            strokeWidth = typedArray.getDimensionPixelSize(R.styleable.ApparkameTimerView_strokeWidth, 2);
            strokeColor = typedArray.getColor(R.styleable.ApparkameTimerView_strokeColor, Color.BLACK);
            strokeShadowWidth = typedArray.getDimensionPixelSize(R.styleable.ApparkameTimerView_strokeShadowWidth, 2);
            strokeShadowColor = typedArray.getColor(R.styleable.ApparkameTimerView_strokeShadowColor, Color.rgb(210, 210, 210));
        } finally {
            typedArray.recycle();
        }

        dpi = getResources().getDisplayMetrics().density;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float width = (float) getWidth();
        float height = (float) getHeight();
        float radius;

        if (width > height) {
            radius = ((height - strokeWidth) / 2);
        } else {
            radius = ((height - strokeWidth) / 2);
        }

        Path path = new Path();
        path.addCircle(width / 2,
                height / 2, radius,
                Path.Direction.CW);

        Paint paint = new Paint();
        paint.setColor(strokeColor);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.FILL);
        paint.setStyle(Paint.Style.STROKE);

        float center_x, center_y;
        final RectF oval = new RectF();

        center_x = width / 2;
        center_y = height / 2;

        oval.set(center_x - radius,
                center_y - radius,
                center_x + radius,
                center_y + radius);

        float endAngle = progress * 360f * (clockwise ? 1 : -1);

        Paint paint2 = new Paint();
        paint2.setColor(strokeShadowColor);
        paint2.setStrokeWidth(strokeShadowWidth);
        paint2.setStyle(Paint.Style.FILL);
        paint2.setStyle(Paint.Style.STROKE);

        canvas.drawArc(oval, startAngle, 360, false, paint2);

        canvas.drawArc(oval, startAngle, endAngle, false, paint);


//        DIBUJAR AGUJA DE RELOJ

        canvas.save();
        canvas.rotate(startAngle + endAngle, center_x, center_y);
//        canvas.rotate(endAngle, center_x, center_y);
        canvas.drawLine(center_x + radius - (30 * dpi), center_y, center_x + radius, center_y, paint);
        canvas.restore();

//        canvas.drawLine();
//
//        getResources().get
//
//        Matrix matrix = new Matrix();
//        matrix.reset();
//        matrix.postTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2); // Centers image
//        matrix.postRotate(angle);
//        matrix.postTranslate(px, py);
//        canvas.drawBitmap(bitmap, matrix, null);
    }

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle;
        invalidate();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress){
        this.progress = progress;
        invalidate();
    }

    public boolean isClockwise() {
        return clockwise;
    }

    public void setClockwise(boolean clockwise) {
        this.clockwise = clockwise;
        invalidate();
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        invalidate();
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        invalidate();
    }
}
