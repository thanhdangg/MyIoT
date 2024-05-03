package com.example.myiot.ui;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CircleProgressBar extends View {

    private int progress = 0;
    private int maxProgress = 100;
    private int progressBarWidth = 30; // Độ rộng của thanh tiến trình

    public CircleProgressBar(Context context) {
        super(context);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int viewWidth = getWidth();
        int viewHeight = getHeight();

        int centerX = viewWidth / 2;
        int centerY = viewHeight / 2;
        int radius = Math.min(centerX, centerY) - progressBarWidth / 2 - 10; // Khoảng trống xung quanh và điều chỉnh độ rộng của thanh tiến trình

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(progressBarWidth);
        paint.setColor(Color.GRAY);
        canvas.drawCircle(centerX, centerY, radius, paint); // Vẽ vòng tròn ngoài

        RectF oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

        // Tạo một gradient cho thanh tiến trình
        LinearGradient gradient = new LinearGradient(centerX - radius, centerY, centerX + radius, centerY,
                Color.BLUE, Color.RED, Shader.TileMode.CLAMP);

        paint.setShader(gradient);

        float sweepAngle = 360 * progress / maxProgress;
        canvas.drawArc(oval, -90, sweepAngle, false, paint); // Vẽ vòng tròn thể hiện tiến trình

        // Vẽ số phần trăm ở giữa
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLACK);
        paint.setTextSize(100);
        String text = progress + "%";
        float textWidth = paint.measureText(text);
        canvas.drawText(text, centerX - textWidth / 2, centerY + 50, paint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate(); // Yêu cầu vẽ lại khi tiến trình thay đổi
    }
}
