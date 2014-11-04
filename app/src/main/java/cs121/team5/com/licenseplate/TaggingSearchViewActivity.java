package cs121.team5.com.licenseplate;

import android.app.Activity;
import android.app.ListFragment;
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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class TaggingSearchViewActivity extends Fragment {
    ListView listView;
    SearchView searchView;
    Object[] names;
    ArrayAdapter<String> adapter;
    ArrayList<String> mAllList = new ArrayList<String>();

//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_view);
//            initActionbar();
//            names = loadData();
//            listView = (ListView) findViewById(R.id.listview);
//            listView.setAdapter(new ArrayAdapter<Object>(getApplicationContext(),
//                    android.R.layout.simple_expandable_list_item_1, names));
//
//            listView.setTextFilterEnabled(true);
//            searchView.setOnQueryTextListener(this);
//            searchView.setSubmitButtonEnabled(false);
//        }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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


        String[] states = {"CA", "WA", "VA", "HI", "AZ"};

        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        for(int i=0; i<5; i++){
            Map<String,Object> datum = new HashMap<String, Object>(2);
            datum.put("thumbnail",String.valueOf(R.drawable.ic_launcher_plate));
            datum.put("name", states[i]);
            data.add(datum);
        }


        listView.setAdapter(new SimpleAdapter(getActivity(), data, R.layout.list_single, new String[] {"thumbnail","name"}, new int[] {R.id.img, R.id.txt}));

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



