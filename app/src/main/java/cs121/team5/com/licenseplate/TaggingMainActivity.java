package cs121.team5.com.licenseplate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

public class TaggingMainActivity extends Activity implements OnItemSelectedListener {
    private Spinner spinnerStates;
    private TextView gpsLocation;
    private PhotoAttributes currentPhoto_;
    private ImageView license;
    private EditText licenseNum;
    private CheckBox specialPlate;

    private String[] state = { "CA", "VA", "NJ", "TN",
            "TA", "WS"};

    private static String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/License_Plate";

    private GPSTracker gps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean newPlate = false;
        boolean defaultBool = false;

        currentPhoto_ = new PhotoAttributes();
        gps = new GPSTracker(this);

        // Get the argument from the intent called by parent activity
        Intent argument = getIntent();
        try {
            currentPhoto_.name_ = argument.getStringExtra("NameOfFile");
            newPlate = argument.getBooleanExtra("NewPlate", defaultBool);
            currentPhoto_.directory_ = dirPath;
            Log.d("NameOfFile:","File loaded:" + currentPhoto_.name_.split("_")[1]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_tagging);
        license = (ImageView) findViewById(R.id.licenseView);
        gpsLocation = (TextView) findViewById(R.id.gpsTextView);
        spinnerStates = (Spinner) findViewById(R.id.osversions);
        licenseNum = (EditText) findViewById(R.id.plateNumber);
        specialPlate = (CheckBox) findViewById(R.id.specialCB);

        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStates.setAdapter(adapter_state);
        spinnerStates.setOnItemSelectedListener(this);

        loadLicense(currentPhoto_.name_, newPlate);

        // Only get the local GPS info if it is new plate.
        if (newPlate) {
            loadGPSLocation();
        }


    }

    public void loadGPSLocation(){
        gps.showSettingsAlert();
        double lat = 0;
        double longt = 0;
        if(gps.canGetLocation()) {
            lat = gps.getLatitude();    // returns latitude
            longt = gps.getLongitude(); // returns longitude
            gpsLocation.setText(String.valueOf(lat) + ", " + String.valueOf(longt));
            currentPhoto_.latitude_ = lat;
            currentPhoto_.longtitude_ = longt;
        }
        else{
            gpsLocation.setText("INVALID");
        }

    }

    public void loadLicense(String NameOfFile, Boolean NewPlate) {
        File imagesFolder = new File(dirPath);
        String imagePath = imagesFolder.getAbsolutePath() + "/" + NameOfFile;
        if(imagesFolder.exists()) {
            File plate = new File(imagePath);
            if(plate.exists()) {
                BitmapFactory.Options options;
                try {
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 1;  // Shrink the picture by a factor of 2
                    Bitmap mBitmap = BitmapFactory.decodeFile(imagePath, options);
                    if (mBitmap != null) {
                        license.setImageBitmap(mBitmap);
                    }
                    if (NewPlate){
                        tesseract(mBitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Update GUI only if it's not new plot
                if(!NewPlate) {
                    try {
                        String[] separatedString = plate.getName().split("_");
                        currentPhoto_.longtitude_ = Double.parseDouble(separatedString[4]);
                        currentPhoto_.latitude_ = Double.parseDouble(separatedString[3]);
                        currentPhoto_.special_ = Boolean.parseBoolean(separatedString[2]);
                        currentPhoto_.state_ = separatedString[0];

                        gpsLocation.setText(currentPhoto_.latitude_.toString() +
                                "," + currentPhoto_.longtitude_.toString());
                        licenseNum.setText(separatedString[1]);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void tesseract(Bitmap plate){
        TessBaseAPI baseApi = new TessBaseAPI();
        baseApi.setDebug(true);
        baseApi.init("/mnt/sdcard/tessertact_languages", "eng");
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

    private void updatePhotoAttribute() {
        String oldName = currentPhoto_.name_;
        currentPhoto_.number_ = licenseNum.getText().toString();
        currentPhoto_.state_ = (String) spinnerStates.getSelectedItem();
        currentPhoto_.special_ = specialPlate.isChecked();
        String newName = currentPhoto_.composeName();

        renamePhoto(dirPath+"/"+oldName,dirPath+"/"+newName);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        spinnerStates.setSelection(position);

        if(gps.canGetLocation()) {
            gps.getLatitude(); // returns latitude
            gps.getLongitude(); // returns longitude
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    /** Called when the user clicks the Save button */
    public void SaveTag(View view) {
        updatePhotoAttribute();
        Toast.makeText(getApplicationContext(),
                "License Plate Tags Updated",
                Toast.LENGTH_SHORT).show();
        gps.stopUsingGPS();
        finish();
    }

    /** Called when the user clicks the Save button */
    public void Cancel(View view) {
        gps.stopUsingGPS();
        finish();
    }

}
