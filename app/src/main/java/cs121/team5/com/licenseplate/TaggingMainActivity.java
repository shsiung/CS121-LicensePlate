package cs121.team5.com.licenseplate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class TaggingMainActivity extends Activity {
    private AutoCompleteTextView spinnerStates;
    private TextView gpsLocation;
    private PlateStruct currentPlate;
    private ImageView license;
    private EditText licenseNum;
    private CheckBox specialPlate;
    private boolean newPlate = false;
    private byte[] plateThumbnail;

    private String[] state = { "CA", "VA", "NJ", "TN",
            "TA", "WS"};

    private static String platePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/License_Plate";
    private static String plateInfoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/License_Plate_Info";

    private GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean defaultBool = false;

        currentPlate = new PlateStruct();
        gps = new GPSTracker(this);

        setContentView(R.layout.activity_tagging);
        license = (ImageView) findViewById(R.id.licenseView);
        gpsLocation = (TextView) findViewById(R.id.gpsTextView);
        spinnerStates = (AutoCompleteTextView) findViewById(R.id.osversions);
        spinnerStates.setThreshold(1);
        licenseNum = (EditText) findViewById(R.id.plateNumber);
        specialPlate = (CheckBox) findViewById(R.id.specialCB);
        specialPlate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(specialPlate.isChecked()){
                    specialPlate.setText("Yes");
                }else{
                    specialPlate.setText("No");
                }
            }
        });

        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                R.layout.state_dropdown, state);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStates.setAdapter(adapter_state);

        // Get the argument from the intent called by parent activity
        Intent argument = getIntent();
        try {
            currentPlate.setPlateName(argument.getStringExtra("NameOfFile"));
            newPlate = argument.getBooleanExtra("NewPlate", defaultBool);
            plateThumbnail = argument.getByteArrayExtra("Plate");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        loadLicense(currentPlate.getPlateName());
    }

    public void loadGPSLocation(){
        gps.showSettingsAlert();
        double lat;
        double lng;
        if(gps.canGetLocation()) {
            lat = gps.getLatitude();    // returns latitude
            lng = gps.getLongitude(); // returns longitude
            gpsLocation.setText(String.valueOf(lat) + ", " + String.valueOf(lng));
            currentPlate.setPlateLatLng(new LatLng(lat,lng));
        }
        else{
            gpsLocation.setText("INVALID");
        }

    }

    public void loadLicense(String NameOfFile) {

        Bitmap mBitmap;
        if (newPlate) {
            mBitmap = BitmapFactory.decodeByteArray(plateThumbnail, 0, plateThumbnail.length);
            if (mBitmap != null) {
                license.setImageBitmap(mBitmap);
            }
            //tesseract(mBitmap);
            loadGPSLocation();
            specialPlate.setText("No");
        }
        else
        {
            File imagesFolder = new File(platePath);
            String imagePath = imagesFolder.getAbsolutePath() + "/" + NameOfFile + ".jpg";

            if (imagesFolder.exists()) {
                File plate = new File(imagePath);
                if (plate.exists()) {
                    BitmapFactory.Options options;
                    try {
                        // Set Thumbnail image
                        options = new BitmapFactory.Options();
                        options.inSampleSize = 1;  // Shrink the picture by a factor of 2
                        mBitmap = BitmapFactory.decodeFile(imagePath, options);
                        if (mBitmap != null) {
                            license.setImageBitmap(mBitmap);
                        }
                        // Load plate info and update GUI
                        currentPlate.setPlateStruct(NameOfFile+".txt");
                        updateGui();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /** Update GUI */
    public void updateGui()
    {
        gpsLocation.setText(currentPlate.getPlateLatLng().latitude + "," +
        currentPlate.getPlateLatLng().longitude );
        licenseNum.setText(currentPlate.getPlateName());
        spinnerStates.setText(currentPlate.getPlateState());
        specialPlate.setChecked(currentPlate.getPlateSpecial());
        if (specialPlate.isChecked())
            specialPlate.setText("Yes");
        else
            specialPlate.setText("No");
    }

    /** Text recognition library */
    public void tesseract(Bitmap plate){
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init(Environment.getExternalStorageDirectory().getPath()+"/tessertact_languages", "eng");
        baseApi.setImage(plate);
        String recognizedText = baseApi.getUTF8Text();
        baseApi.end();

        if (recognizedText.length() != 0) {
            licenseNum.setText(recognizedText);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void renamePhoto(String oldName, String newName) {
        File from = new File(oldName);
        File to = new File(newName);
        from.renameTo(to);
    }

    private void saveNewPhotoInfo(String newName) {
        File infoFolder = new File(plateInfoPath);
        if (!infoFolder.exists()) {
            infoFolder.mkdir();
        }

        String fileTextName = newName + ".txt";
        File output_text = new File(infoFolder, fileTextName);

        try {
            FileOutputStream textFileOS = new FileOutputStream(output_text);
            textFileOS.write(currentPlate.getPlateInfo().getBytes());
            textFileOS.flush();
            textFileOS.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.d("Error", "Error accessing file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updatePhotoInfo(String oldName, String newName)
    {
        File infoFolder = new File(plateInfoPath);
        String oldFileTextName = oldName + ".txt";
        String fileTextName = newName + ".txt";
        File output_new = new File(infoFolder, fileTextName);
        File output_old = new File(infoFolder, oldFileTextName);
        output_old.delete();

        try {
            FileOutputStream textFileOS = new FileOutputStream(output_new);
            textFileOS.write(currentPlate.getPlateInfo().getBytes());
            textFileOS.flush();
            textFileOS.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.d("Error", "Error accessing file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Update Photo attributes before saving */
    private void saveNewPhoto(String newName)
    {
        File imagesFolder = new File(platePath);

        // If the directory doesn't exit, create one
        if (!imagesFolder.exists()) {
            imagesFolder.mkdirs();
        }

        // The current license name
        String fileName = newName + ".jpg";
        File output = new File(imagesFolder, fileName);

        // Check if the photo name exist and photo being taken already
        while (output.exists()) {
            fileName = newName + ".jpg";
            output = new File(imagesFolder, fileName);
        }
        try {
            FileOutputStream imageFileOS = new FileOutputStream(output);
            imageFileOS.write(plateThumbnail);
            imageFileOS.flush();
            imageFileOS.close();

            Toast.makeText(getApplicationContext(),
                    "License Saved",
                    Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.d("Error", "Error accessing file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void updatePhotoAttribute(){
        currentPlate.setPlateSpecial(specialPlate.isChecked());
        currentPlate.setPlateState(spinnerStates.getText().toString());
        currentPlate.setPlateName(licenseNum.getText().toString());
    }

    private boolean checkFormComplete() {
        if (spinnerStates.getText().toString().matches("") || licenseNum.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(),
                        "Info contains empty field(s)",
                        Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Arrays.asList(state).contains(spinnerStates.getText().toString()))
        {
            Toast.makeText(getApplicationContext(),
                    "Illegal state name",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (licenseNum.getText().length() > 10) {
            Toast.makeText(getApplicationContext(),
                    "Illegal license plate number",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    /** Called when the user clicks the Save button */
    public void SaveTag(View view) {

        String oldName;
        oldName = currentPlate.getPlateName();
        updatePhotoAttribute();
        String newName = currentPlate.getPlateName();
        if (checkFormComplete())
        {
            if (newPlate) {
                saveNewPhoto(newName);
                saveNewPhotoInfo(newName);
                Toast.makeText(getApplicationContext(),
                        "Plate saved",
                        Toast.LENGTH_SHORT).show();
            } else {
                renamePhoto(platePath + "/" + oldName + ".jpg", platePath + "/" + newName + ".jpg");
                updatePhotoInfo(oldName, newName);
                Toast.makeText(getApplicationContext(),
                        "License plate tags updated",
                        Toast.LENGTH_SHORT).show();
            }

            gps.stopUsingGPS();
            finish();
        }
    }

    /** Called when the user clicks the Cancel button */
    public void Cancel(View view) {
        gps.stopUsingGPS();
        finish();
    }

}
