package cs121.team5.com.licenseplate;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.File;
import java.net.URI;
import java.util.HashMap;

import static android.app.PendingIntent.getActivity;

public class TaggingMainActivity extends Activity implements OnItemSelectedListener {
    Spinner spinnerStates;
    TextView selState;
    ImageView imageView;
    HashMap<String, PhotoAttributes> photosByName_;
    PhotoAttributes currentPhoto_;
    private String[] state = { "CA", "VA", "NJ", "TN",
            "TA", "WS"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Here we should load the program data saved to file and initiate photosByName_
//        this.loadHashMap();
        this.currentPhoto_ = null;
        setContentView(R.layout.tagging);
        System.out.println(state.length);
        selState = (TextView) findViewById(R.id.selVersion);
        spinnerStates = (Spinner) findViewById(R.id.osversions);
        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, state);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStates.setAdapter(adapter_state);
        spinnerStates.setOnItemSelectedListener(this);

        ////////////
        File imagesFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "License_Plate");
        String path = Environment.getExternalStorageDirectory().toString()+"/Pictures";
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File[] files = f.listFiles();
        Log.d("Files", "Size: "+ files.length);
        File defaultFile = files[0];
        String URIstring = defaultFile.toURI().toString();
        ImageView imgView = (ImageView) findViewById(R.id.image);
        imgView.setImageURI(Uri.parse(URIstring));
        // also load up the photoattribute


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        this.saveHashMap();
    }

//    private void loadHashMap() {
//
//    }

//    private void saveHashMap() {
//        Context context = getActivity();
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//    }

    public void loadPhoto(PhotoAttributes newPhoto) {
        String photoDir = newPhoto.composeName();
        // Load the photo to display in a photo view from currentPhotoDir
        this.imageView.setImageURI(Uri.parse(photoDir));
        this.updatePhotoAttribute(newPhoto);
    }

    private void renamePhoto(String oldname, String newname) {
        File from = new File(oldname);
        File to = new File(newname);
        from.renameTo(to);

        // rename using android FS operation
    }
    // Always call when the photo attribute is updated
    private void updatePhotoAttribute(PhotoAttributes updatedPhoto) {
        String name = updatedPhoto.composeName();
        // It would be better if the entry with the original name is deleted
        this.photosByName_.put(name, updatedPhoto);
        // rename filename of currentphoto to that of updatedphoto
        this.renamePhoto(this.currentPhoto_.composeName(), name);
        // update the new currentphoto. Need to make deep copy in case we are modifying
        // PhotoAttributes retrieved from HashMap
        this.currentPhoto_ = new PhotoAttributes(updatedPhoto);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        spinnerStates.setSelection(position);
        String selState = (String) spinnerStates.getSelectedItem();
        this.selState.setText("Selected State Name:" + selState);

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

}
