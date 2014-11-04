package cs121.team5.com.licenseplate;

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
    ArrayList<LatLng> plateLatLng;
    ArrayList<String> plateName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plateLatLng = new ArrayList<LatLng>();
        plateName = new ArrayList<String>();
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

        importPlateLagLng();
        addPlateMarkers();

        return v;
    }

    void importPlateLagLng(){
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/License_Plate";
        File plateDir = new File(dirPath);
        String[] separatedString = new String[5];
        for (File f : plateDir.listFiles()) {
            Log.d("DebugTag", f.getName());
            if (f.isFile())
                separatedString = f.getName().split("_");
                plateLatLng.add(new LatLng(Double.parseDouble(separatedString[3]),
                                           Double.parseDouble(separatedString[4])));
                plateName.add("Plate: " + separatedString[1]);
        }
    }

    void addPlateMarkers(){

        for (int i = 0 ; i <plateLatLng.size(); i++){
            map.addMarker(new MarkerOptions()
                 .position(plateLatLng.get(i))
                 .title(plateName.get(i))
                 .icon(BitmapDescriptorFactory
                         .fromResource(R.drawable.ic_launcher)));
        }
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
