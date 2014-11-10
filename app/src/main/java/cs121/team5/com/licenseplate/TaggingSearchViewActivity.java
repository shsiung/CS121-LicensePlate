package cs121.team5.com.licenseplate;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class TaggingSearchViewActivity extends Fragment {
    ListView listView;
    List<RowItem> rowItems;
    ArrayList<PlateStruct> plateInfoList;
    EditText editSearch;

    SearchView searchView;
    Object[] names;
    ArrayAdapter<String> adapter;
    ArrayList<String> mAllList = new ArrayList<String>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        plateInfoList = new ArrayList<PlateStruct>();


    }


       //Takes each picture and puts its information into the arraylist
    public void updatePlateInfo(){
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/License_Plate";
        File plateDir = new File(dirPath);
        plateInfoList.clear();

        for(File f : plateDir.listFiles()){
            plateInfoList.add(new PlateStruct(f));
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_view, container, false);

        listView = (ListView) v
                .findViewById(R.id.list);

        editSearch = (EditText) v.findViewById(R.id.queryContent);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = editSearch.getText().toString().toLowerCase(Locale.getDefault());
                adapter.getFilter().filter(text);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Intent tagPic = new Intent(getActivity(), TaggingMainActivity.class);
                String nameOfFile = plateInfoList.get(position).getPlateAddress();
                tagPic.putExtra("NameOfFile", nameOfFile);
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

        rowItems = new ArrayList<RowItem>();
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+ "/License_Plate";


        //Create a row item for each plate file
        rowItems = new ArrayList<RowItem>();
        for(int i=0; i< plateInfoList.size();++i ){
            RowItem item = new RowItem(plateInfoList.get(i));
            rowItems.add(item);
        }

        //Create and set the adapter
        CustomListViewAdapter adapter = new CustomListViewAdapter(getActivity(), R.layout.list_single, rowItems);
        listView.setAdapter(adapter);

    }


}



