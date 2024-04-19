package com.example.myiot.ui;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressBar extends View{

    private Paint paint;
    private int progress;

    public CircleProgressBar(Context context) {
        super(context);
    }
    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        progress = 0;
    }
    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;
        int cx = width / 2;
        int cy = height / 2;

        // Draw the background circle
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx, cy, radius, paint);

        // Calculate the sweep angle based on progress
        float sweepAngle = (float) progress / 100 * 360;

        // Draw the progress arc
        paint.setColor(Color.BLUE);
        canvas.drawArc(cx - radius, cy - radius, cx + radius, cy + radius, -90, sweepAngle, false, paint);

        // Draw the progress text in the center
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setStyle(Paint.Style.FILL);
        String progressText = progress + "%";
        float textWidth = paint.measureText(progressText);
        float textHeight = paint.descent() - paint.ascent();
        float textOffset = (textHeight / 2) - paint.descent();
        canvas.drawText(progressText, cx - (textWidth / 2), cy + textOffset, paint);
    }
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate(); // Redraw the view
    }
    public void setMax(int max) {
    }
    public void setProgressColor(int color) {
    }
    public void setProgressWidth(int width) {
    }
    public void setProgressBackgroundColor(int color) {
    }
    public void setProgressBackgroundWidth(int width) {
    }
    public void setProgressTextSize(int size) {
    }
    public void setProgressTextColor(int color) {
    }
    public void setProgressText(String text) {
    }
    public void setProgressTextVisibility(boolean visibility) {
    }
    public void setProgressTextTypeface(Typeface typeface) {
    }
    public void setProgressTextTypeface(String typeface) {
    }
    public void setProgressTextTypeface(int typeface) {
    }
    public void setProgressTextTypeface(Typeface typeface, int style) {
    }
    public void setProgressTextTypeface(String typeface, int style) {
    }
    public void setProgressTextTypeface(int typeface, int style) {
    }
    public void setProgressTextTypeface(Typeface typeface, int style, int size) {
    }
    public void setProgressTextTypeface(String typeface, int style, int size) {
    }
    public void setProgressTextTypeface(int typeface, int style, int size) {
    }
    public void setProgressTextTypeface(Typeface typeface, int style, int size, int color) {
    }
    public void setProgressTextTypeface(String typeface, int style, int size, int color) {
    }
    public void setProgressTextTypeface(int typeface, int style, int size, int color) {
    }
    public void setProgressTextTypeface(Typeface typeface, int style, int size, int color, int shadowColor) {
    }
    public void setProgressTextTypeface(String typeface, int style, int size, int color, int shadow){

    }
}
