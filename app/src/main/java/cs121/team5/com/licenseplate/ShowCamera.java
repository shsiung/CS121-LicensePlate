package cs121.team5.com.licenseplate;

import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.hardware.Camera;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class ShowCamera extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holdMe;
    private Camera theCamera;

    public ShowCamera(Context context,Camera camera) {
        super(context);
        theCamera = camera;
        holdMe = getHolder();
        holdMe.addCallback(this);
        this.setWillNotDraw(false);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try   {
            theCamera.setPreviewDisplay(holder);
            theCamera.startPreview();
            theCamera.setDisplayOrientation(90);
        } catch (IOException e) {
            Log.d("ADebugTag", "Uh ohhhhhh!");
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        //  Find Screen size first
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        int screenWidth = getWidth();
        int screenHeight = getHeight();

        // Color to use
        int outerFillColor = 0x77000000;

        //  Set paint options
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(outerFillColor);

        //  Set paint options
        Paint paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setARGB(200,0,200,0);
        paintText.setStrokeWidth(10);
        //Fill the screen
        canvas.drawPaint(paint);

        //Empty a rectangle in the center
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        //Adjust the dimension values as needed
        canvas.drawRect((screenWidth/9),(screenHeight/3),8*(screenWidth/9),2*(screenHeight/3), paint);

        // Draw corner guidelines
        canvas.drawLine((screenWidth/9),(screenHeight/3),(screenWidth/9),(screenHeight/3)+50,paintText);
        canvas.drawLine((screenWidth/9),(screenHeight/3),(screenWidth/9)+50,(screenHeight/3),paintText);
        canvas.drawLine(8*(screenWidth/9),(screenHeight/3),8*(screenWidth/9),(screenHeight/3)+50,paintText);
        canvas.drawLine(8*(screenWidth/9),(screenHeight/3),8*(screenWidth/9)-50,(screenHeight/3),paintText);
        canvas.drawLine((screenWidth/9),2*(screenHeight/3),(screenWidth/9),2*(screenHeight/3)-50,paintText);
        canvas.drawLine((screenWidth/9),2*(screenHeight/3),(screenWidth/9)+50,2*(screenHeight/3),paintText);
        canvas.drawLine(8*(screenWidth/9),2*(screenHeight/3),8*(screenWidth/9),2*(screenHeight/3)-50,paintText);
        canvas.drawLine(8*(screenWidth/9),2*(screenHeight/3),8*(screenWidth/9)-50,2*(screenHeight/3),paintText);
    }

}