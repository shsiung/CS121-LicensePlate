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

        currentPhoto_ = new PhotoAttributes();
        gps = new GPSTracker(this);

        // Get the argument from the intent called by parent activity
        Intent argument = getIntent();
        try {
            currentPhoto_.name_ = argument.getStringExtra("NameOfFile");
            currentPhoto_.directory_ = dirPath;
            Log.d("NameOfFile:","Name of the file is:" + currentPhoto_.name_);
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

        loadLicensePic(currentPhoto_.name_);
        loadGPSLocation();

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

    public void loadLicensePic(String NameOfFile) {
        File imagesFolder = new File(dirPath);
        if(imagesFolder.exists()) {
            if(new File(imagesFolder.getAbsolutePath() + "/" + NameOfFile).exists()) {
                BitmapFactory.Options options;
                try {
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 1;  // Shrink the picture by a factor of 2
                    Bitmap mBitmap = BitmapFactory.decodeFile(imagesFolder.getAbsolutePath() + "/" + NameOfFile, options);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                    if (mBitmap != null) {
                        license.setImageBitmap(rotatedBitmap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
