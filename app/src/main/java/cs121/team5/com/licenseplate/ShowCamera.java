package cs121.team5.com.licenseplate;

import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
//            int screenWidth = metrics.widthPixels;
//            int screenHeight = (int) (metrics.heightPixels*0.9);
        int screenWidth = getWidth();
        int screenHeight = getHeight();


        //  Set paint options
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.argb(255, 255, 255, 000));

        canvas.drawLine((screenWidth/8)*6,0,(screenWidth/8)*6,screenHeight,paint);
        canvas.drawLine((screenWidth/8)*2,0,(screenWidth/8)*2,screenHeight,paint);
        canvas.drawLine(0,(screenHeight/3)*2,screenWidth,(screenHeight/3)*2,paint);
        canvas.drawLine(0,(screenHeight/3),screenWidth,(screenHeight/3),paint);

    }

}