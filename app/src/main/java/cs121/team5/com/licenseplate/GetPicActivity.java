package cs121.team5.com.licenseplate;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class GetPicActivity extends Activity {

    private Camera cameraObject;
    private ShowCamera showCamera;

    public static Camera isCameraAvailable(){
        Camera object = null;
        try {
            object = Camera.open();
            // Log.d("ADebugTag", "Camera open!");
        }
        catch (Exception e){
            // Log.d("ADebugTag","Camera not available!");
        }
        return object;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pic);
        cameraObject = isCameraAvailable();
        showCamera = new ShowCamera(this, cameraObject);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(showCamera);
    }

    public void snapIt(View view){
        cameraObject.takePicture(null, null, capturedIt);
    }

    private Camera.PictureCallback capturedIt = new Camera.PictureCallback() {

        //        @Override
//        public void onPictureTaken(byte[] data, Camera camera) {
//
//            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//            if(bitmap==null){
//                Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();
//            }
//            cameraObject.release();
//            pic.setImageBitmap(bitmap);
//            //finish();
//        }
        public void onPictureTaken(byte[] data, Camera arg1) {

            int imageNum = 0;
            File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "License_Plate");
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }
            imagesFolder.mkdirs();
            String fileName = "image_" + String.valueOf(imageNum) + ".jpg";
            File output = new File(imagesFolder, fileName);
            while (output.exists()) {
                imageNum++;
                fileName = "image_" + String.valueOf(imageNum) + ".jpg";
                output = new File(imagesFolder, fileName);
            }
            try {
                FileOutputStream imageFileOS = new FileOutputStream(output);
                imageFileOS.write(data);
                imageFileOS.flush();
                imageFileOS.close();

                Toast.makeText(getApplicationContext(), "saved", Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                Log.d("Error", "File not found: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.d("Error", "Error accessing file: " + e.getMessage());
                e.printStackTrace();
            }
            cameraObject.startPreview();
        }

    };

    @Override
    protected void onPause()
    {
        super.onPause();
        if (cameraObject != null) {
            cameraObject.stopPreview();
            cameraObject.release();
            cameraObject = null;
        }
    }

}
