package cs121.team5.com.licenseplate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class GetPicActivity extends Fragment {

    private Camera cameraObject;
    private ShowCamera showCamera;
    private boolean PictureTaken;
    private int CurrentLicenseNum;
    private FrameLayout camPreview;
    private byte[] image;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraObject = isCameraAvailable();
        showCamera = new ShowCamera(getActivity(), cameraObject);
        PictureTaken = false;
        CurrentLicenseNum = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_get_pic, container, false);

        if (showCamera == null){
            cameraObject = isCameraAvailable();
            showCamera = new ShowCamera(getActivity(), cameraObject);
        }
        camPreview = (FrameLayout) v.findViewById(R.id.camera_preview);
        camPreview.addView(showCamera);

        Button captureBtn = (Button) v.findViewById(R.id.button_capture);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnapIt(v);
            }
        });

        Button doneBtn = (Button) v.findViewById(R.id.submit);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagPicture(v);
            }
        });

        return v;
    }

    public void SnapIt(View view){
        cameraObject.takePicture(null, null, capturedIt);
    }

    public void TagPicture(View view){
        if(PictureTaken) {
            Intent tagPic = new Intent(getActivity(), TaggingMainActivity.class);
            tagPic.putExtra("NameOfFile", "newPic.jpg");
            tagPic.putExtra("NewPlate",true);
            tagPic.putExtra("Plate",image);
            onDestroy();
            startActivity(tagPic);
        }
        else {
            Toast.makeText(getActivity(),
                    "You haven't taken a photo yet!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private Camera.PictureCallback capturedIt = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera arg1) {

            if (data != null) {
                PictureTaken = true;

                //Extract the bitmap from data
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

                // Only select the region we want
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth()/9, bitmap.getHeight()/3);

                //Rotate the bitmap
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bitmap = Bitmap.createBitmap(bitmap, 0,0,
                                            bitmap.getWidth(),bitmap.getHeight(),
                                            matrix, true);

                // Convert it back to byte array data
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bmArray = stream.toByteArray(); // Get the underlying pixel bytes array
                image = bmArray;
                if (image != null) {
                    Toast.makeText(getActivity(),
                            "Photo Taken",
                            Toast.LENGTH_SHORT).show();
                }
            }
            cameraObject.startPreview();
        }

    };

    @Override
    public void onPause()
    {
        super.onPause();
        if (cameraObject != null) {
            cameraObject.stopPreview();
            camPreview.removeView(showCamera);
            cameraObject.release();
            cameraObject = null;
        }
        PictureTaken = false;
        showCamera = null;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (cameraObject != null) {
            cameraObject.stopPreview();
            camPreview.removeView(showCamera);
            cameraObject.release();
            cameraObject = null;
            showCamera = null;
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (showCamera == null){
            cameraObject = isCameraAvailable();
            showCamera = new ShowCamera(getActivity(), cameraObject);
        }
    }
}


//            // Get the path to the directory
//            File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "License_Plate");
//            Log.d("ADebugTag",imagesFolder.getAbsolutePath());
//
//            // If the directory doesn't exit, create one
//            if (!imagesFolder.exists()) {
//                imagesFolder.mkdirs();
//            }
//
//            // The current license name
//            String fileName = "license_" + String.valueOf(CurrentLicenseNum) + ".jpg";
//            String fileTextName = "license_" + String.valueOf(CurrentLicenseNum) + ".txt";
//            File output = new File(imagesFolder, fileName);
//            File output_text = new File(imagesFolder, fileTextName);
//
//            // Check if the photo name exist and photo being taken already
//            while (output.exists() && !PictureTaken) {
//                CurrentLicenseNum++;
//                fileName = "license_" + String.valueOf(CurrentLicenseNum) + ".jpg";
//                fileTextName = "license_" + String.valueOf(CurrentLicenseNum) + ".txt";
//                output = new File(imagesFolder, fileName);
//                output_text = new File(imagesFolder, fileTextName);
//            }
//            try {
//                // Extract the bitmap from data
//                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//
//                // Only select the region we want
//                bitmap = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth()/9, bitmap.getHeight()/3);
//
//                //Rotate the bitmap
//                Matrix matrix = new Matrix();
//                matrix.postRotate(90);
//                bitmap = Bitmap.createBitmap(bitmap, 0,0,
//                                            bitmap.getWidth(),bitmap.getHeight(),
//                                            matrix, true);
//
//                // Convert it back to byte array data
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byte[] bmArray = stream.toByteArray(); // Get the underlying pixel bytes array
//                image = bmArray;
//
//                FileOutputStream imageFileOS = new FileOutputStream(output);
//                imageFileOS.write(bmArray);
//                imageFileOS.flush();
//                imageFileOS.close();
//
//                FileOutputStream textFileOS = new FileOutputStream(output_text);
//                textFileOS.write("TEST".getBytes());
//                textFileOS.flush();
//                textFileOS.close();
//
//                Toast.makeText(getActivity(),
//                               "License " + String.valueOf(CurrentLicenseNum)+ " Saved",
//                               Toast.LENGTH_SHORT).show();
//                PictureTaken = true;
//
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                Log.d("Error", "File not found: " + e.getMessage());
//                e.printStackTrace();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                Log.d("Error", "Error accessing file: " + e.getMessage());
//                e.printStackTrace();
//            }
