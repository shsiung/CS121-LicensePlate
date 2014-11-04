package cs121.team5.com.licenseplate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;

/*
    Adapted from http://mobiforge.com/design-development/developing-with-google-maps-v2-android
    To make it fragment, I follow:
    http://stackoverflow.com/questions/19353255/how-to-put-google-maps-v2-on-a-fragment-using-viewpager
    not the solution but the answer from user3898069
 */
public class GPSMapLocator extends Fragment{

    GoogleMap map;
    private static String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/License_Plate";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_gpsmap_locator, container, false);

        MapView mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try{
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        map = mMapView.getMap();
        if (map == null) {
            Toast.makeText(getActivity(), "Google Maps not available",
                    Toast.LENGTH_LONG).show();
        }

        // Make the view centering at U.S
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.850033,-87.6500523),3.5f));

        try {
            importPlateMarker();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    void importPlateMarker(){
        File plateDir = new File(dirPath);
        String[] separatedString = new String[5];
        String plateName;
        LatLng latlng;
        for (File f : plateDir.listFiles()) {
            Log.d("DebugTag", f.getName());
            if (f.isFile())
                separatedString = f.getName().split("_");
                latlng = new LatLng(Double.parseDouble(separatedString[3]), Double.parseDouble(separatedString[4]));
                plateName = "Plate: " + separatedString[1];
                addPlateMarkers(loadLicensePic(f.getName()),
                                latlng,
                                plateName);
        }
    }

    void addPlateMarkers(Bitmap plate, LatLng latlng, String plateName){
            map.addMarker(new MarkerOptions()
                 .position(latlng)
                 .title(plateName)
                 .icon(BitmapDescriptorFactory
                         .fromBitmap(plate)));
    }

    public Bitmap loadLicensePic(String NameOfFile) {
        File imagesFolder = new File(dirPath);
        if(imagesFolder.exists()) {
            if(new File(imagesFolder.getAbsolutePath() + "/" + NameOfFile).exists()) {
                BitmapFactory.Options options;
                try {
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 6;  // Shrink the picture by a factor of 2
                    Bitmap mBitmap = BitmapFactory.decodeFile(imagesFolder.getAbsolutePath() + "/" + NameOfFile, options);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                    return rotatedBitmap;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
