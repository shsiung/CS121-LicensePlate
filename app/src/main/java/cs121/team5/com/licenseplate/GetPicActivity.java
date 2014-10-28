package cs121.team5.com.licenseplate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Picture;
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
    private boolean PictureTaken;
    private int CurrentLicenseNum;

    public static Camera isCameraAvailable(){
        Camera object = null;
        try {
            object = Camera.open();
        }
        catch (Exception e){
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
        PictureTaken = false;
        CurrentLicenseNum = 0;
    }

    public void SnapIt(View view){
        cameraObject.takePicture(null, null, capturedIt);
    }

    public void TagPicture(View view){
        Intent tagPic = new Intent(this, TaggingMainActivity.class);
        tagPic.putExtra("NameOfFile","license_" + String.valueOf(CurrentLicenseNum) + ".jpg");
        startActivity(tagPic);
    }

    private Camera.PictureCallback capturedIt = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera arg1) {

            File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "License_Plate");
            Log.d("ADebugTag",imagesFolder.getAbsolutePath());
            if (!imagesFolder.exists()) {
                imagesFolder.mkdirs();
            }
            imagesFolder.mkdirs();
            String fileName = "license_" + String.valueOf(CurrentLicenseNum) + ".jpg";
            File output = new File(imagesFolder, fileName);
            while (output.exists() && !PictureTaken) {
                CurrentLicenseNum++;
                fileName = "license_" + String.valueOf(CurrentLicenseNum) + ".jpg";
                output = new File(imagesFolder, fileName);
            }
            try {

                FileOutputStream imageFileOS = new FileOutputStream(output);
                imageFileOS.write(data);
                imageFileOS.flush();
                imageFileOS.close();

                Toast.makeText(getApplicationContext(),
                               "License " + String.valueOf(CurrentLicenseNum)+ " Saved",
                               Toast.LENGTH_SHORT).show();
                PictureTaken = true;

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
