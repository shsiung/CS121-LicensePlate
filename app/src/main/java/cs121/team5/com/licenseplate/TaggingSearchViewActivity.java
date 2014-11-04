package cs121.team5.com.licenseplate;

import android.app.Activity;
import android.app.ListFragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TaggingSearchViewActivity extends Fragment {
    ListView listView;
    List<RowItem> rowItems;
    ArrayList<String> plateName;
    ArrayList<String> plateState;
    ArrayList<LatLng> plateLatLng;
    ArrayList<Bitmap> platePic;

    SearchView searchView;
    Object[] names;
    ArrayAdapter<String> adapter;
    ArrayList<String> mAllList = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plateLatLng = new ArrayList<LatLng>();
        plateName = new ArrayList<String>();
        plateState = new ArrayList<String>();
        platePic = new ArrayList<Bitmap>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_view, container, false);

        listView = (ListView) v
                .findViewById(R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {

                // TODO: Fill in the actual photo name clicked
                Intent tagPic = new Intent(getActivity(), TaggingMainActivity.class);
                tagPic.putExtra("NameOfFile", "license_" + String.valueOf(0) + ".jpg");
                startActivity(tagPic);
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Display_Rows();

    }



    private void Display_Rows() {

        plateState.clear();
        plateLatLng.clear();
        plateName.clear();
        platePic.clear();

        rowItems = new ArrayList<RowItem>();
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/License_Plate";
        File plateDir = new File(dirPath);
        String[] separatedString;

        //Pull info from files to local arraylists
        for(File f : plateDir.listFiles()){

            separatedString = f.getName().split("_");

            plateState.add("State: " + separatedString[0]);
            plateLatLng.add(new LatLng(Double.parseDouble(separatedString[3]),
                                       Double.parseDouble(separatedString[4])));
            plateName.add("Plate: " + separatedString[1]);

            Bitmap plateBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
            platePic.add(plateBitmap);
        }

        //Create a row item for each plate file
        rowItems = new ArrayList<RowItem>();
        for(int i=0; i< plateState.size();++i ){
            RowItem item = new RowItem(platePic.get(i), plateState.get(i), plateName.get(i));
            rowItems.add(item);
        }

        //Create and set the adapter
        CustomListViewAdapter adapter = new CustomListViewAdapter(getActivity(), R.layout.list_single, rowItems);
        listView.setAdapter(adapter);

    }

    public File[] Search(String keyword) {
        String myDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();
        File f = new File(myDirectory);
        if (f.exists() && f.isDirectory()) {
            // the regex has to be written
            final Pattern p = Pattern.compile("*" + keyword + "*");

            File[] flists = f.listFiles(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return p.matcher(file.getName()).matches();
                }
            });
            return flists;
        } else {
            return null;
        }
    }
}



