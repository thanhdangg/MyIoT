package com.example.myiot.ui;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CircleProgressBar extends View{

    private Paint paint;
    private int progress;

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public int getProgress() {
        return progress;
    }

    public CircleProgressBar(Context context) {
        super(context);
    }
    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
//        progress = 80;

    }
    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public void setProgress(int progress) {
        this.progress = progress;
//        invalidate();
//        postInvalidate();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int radius = (int)(Math.min(width, height) / 2* 0.8);
        int cx = width / 2;
        int cy = height / 2;

        // Draw the background circle
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(10); // Increase the stroke width
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx, cy, radius, paint);

        // Calculate the sweep angle based on progress
        float sweepAngle = (float) progress / 100 * 360;

        // Set the stroke width of the progress bar
        paint.setStrokeWidth(30); // Increase the stroke width

        // Create a gradient from light blue to dark blue
        paint.setShader(new LinearGradient(0, 0, width, height, Color.GREEN, Color.DKGRAY, Shader.TileMode.CLAMP));

        // Draw the progress arc
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
}
