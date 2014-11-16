package cs121.team5.com.licenseplate;

import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TaggingSearchViewActivity extends Fragment {
    ListView listView;
    List<RowItem> rowItems;
    ArrayList<PlateStruct> plateInfoList;
    EditText editSearch;

    SearchView searchView;
    Object[] names;
    CustomListViewAdapter adapter;
    ArrayList<String> mAllList = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plateInfoList = new ArrayList<PlateStruct>();
        rowItems = new ArrayList<RowItem>();

    }


       //Takes each picture and puts its information into the arraylist
    public void updatePlateInfo(){
        String infoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/License_Plate_Info";

        File infoDir = new File(infoPath);
        plateInfoList.clear();

        for(File f : infoDir.listFiles()){
            plateInfoList.add(new PlateStruct(f));
        }

        for(int i=0; i< plateInfoList.size();++i ){
            RowItem item = new RowItem(plateInfoList.get(i));
            rowItems.add(item);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_view, container, false);

        listView = (ListView) v
                .findViewById(R.id.list);

        EditText searchBar = (EditText) v.findViewById(R.id.queryContent);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rowItems.clear();
                String constraint = s.toString().toUpperCase();
                //Filter
                for (PlateStruct plate : plateInfoList) {
                    if (plate.getPlateState().toUpperCase().startsWith(constraint) ||
                            plate.getPlateName().toUpperCase().startsWith(constraint)) {
                        rowItems.add(new RowItem(plate));
                    }
                }
                displayRows();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Intent tagPic = new Intent(getActivity(), TaggingMainActivity.class);
                String nameOfFile = plateInfoList.get(position).getPlateName();
                tagPic.putExtra("NameOfFile", nameOfFile);
                tagPic.putExtra("NewPlate",false);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                Bitmap plate = plateInfoList.get(position).getPlateBitmap();
                plate.compress(Bitmap.CompressFormat.PNG, 100, stream);
                tagPic.putExtra("Plate",stream.toByteArray());
                
                startActivity(tagPic);
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updatePlateInfo();
        displayRows();


    }



    private void displayRows() {

        //Create and set the adapter
        adapter = new CustomListViewAdapter(getActivity(), R.layout.list_single, rowItems);
        listView.setAdapter(adapter);
    }


}



