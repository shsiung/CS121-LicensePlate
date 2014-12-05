package cs121.team5.com.licenseplate;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
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
import com.google.android.gms.maps.model.Marker;
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
    private static String infoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/License_Plate_Info";
    private ArrayList<PlateStruct> plateInfoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        plateInfoList = new ArrayList<PlateStruct>();
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
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(34.106234, -117.709312),16f));

        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent tagPic = new Intent(getActivity(), TaggingMainActivity.class);
                tagPic.putExtra("NameOfFile", marker.getTitle());
                tagPic.putExtra("NewPlate",false);
                startActivity(tagPic);
            }
        });

        try {
            importPlateMarker();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    void importPlateMarker(){
        File plateInfoDir = new File(infoPath);

        for(File f : plateInfoDir.listFiles()){
            plateInfoList.add(new PlateStruct(f));
        }

        for(int i=0; i< plateInfoList.size();++i ){
            addPlateMarkers(getResizedBitmap(plateInfoList.get(i).getPlateBitmap(),150),
                            plateInfoList.get(i).getPlateLatLng(),
                            plateInfoList.get(i).getPlateName());
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    void addPlateMarkers(Bitmap plate, LatLng latlng, String plateName){
            map.addMarker(new MarkerOptions()
                 .position(latlng)
                 .title(plateName)
                 .icon(BitmapDescriptorFactory
                         .fromBitmap(plate)));
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

    @Override
    public void onPause()
    {
        super.onPause();
        map.clear();
        plateInfoList.clear();
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            importPlateMarker();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
