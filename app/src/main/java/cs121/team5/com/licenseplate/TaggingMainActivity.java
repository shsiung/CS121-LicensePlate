package cs121.team5.com.licenseplate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;

public class TaggingMainActivity extends Activity {
    private AutoCompleteTextView spinnerStates;
    private TextView gpsLocation;
    private PlateStruct currentPlate;
    private ImageView license;
    private EditText licenseNum;
    private CheckBox specialPlate;
    private boolean newPlate = false;

    private String[] state = { "CA", "VA", "NJ", "TN",
            "TA", "WS"};

    private static String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/License_Plate";

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
                    if (newPlate){
                        tesseract(mBitmap);
                        loadGPSLocation();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Update GUI only if it's not new plot
                if(!newPlate) {
                    try {
                        currentPlate.setPlateStruct(plate.getName());
                        updateGui();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else
                    specialPlate.setText("No");
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

    /** Update Photo attributes before saving */
    private void updatePhotoAttribute() {
        String oldName;
        if (newPlate)
            oldName = currentPlate.getPlateName();
        else
            oldName = currentPlate.getPlateAddress();

        currentPlate.setPlateSpecial(specialPlate.isChecked());
        currentPlate.setPlateState(spinnerStates.getText().toString());
        currentPlate.setPlateName(licenseNum.getText().toString());
        String newName = currentPlate.getPlateAddress();
        renamePhoto(dirPath+"/"+oldName,dirPath+"/"+newName);
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

    /** Called when the user clicks the Cancel button */
    public void Cancel(View view) {
        gps.stopUsingGPS();
        finish();
    }

}
