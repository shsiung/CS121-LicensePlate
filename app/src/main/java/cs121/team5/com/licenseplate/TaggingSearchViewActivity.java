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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaggingSearchViewActivity extends Fragment {
    ListView listView;
    List<RowItem> rowItems;
    ArrayList<PlateStruct> plateInfoList;
    EditText editSearch;

    SearchView searchView;
    Object[] names;
    CustomListViewAdapter adapter;
    ArrayList<String> mAllList = new ArrayList<String>();


    public static final HashMap<String,String> STATE_MAP;
    static{
        STATE_MAP = new HashMap<String, String>();
        STATE_MAP.put("Alabama","AL");
        STATE_MAP.put("Alaska","AK");
        STATE_MAP.put("Alberta","AB");
        STATE_MAP.put("American Samoa","AS");
        STATE_MAP.put("Arizona","AZ");
        STATE_MAP.put("Arkansas","AR");
        STATE_MAP.put("Armed Forces (AE)","AE");
        STATE_MAP.put("Armed Forces Americas","AA");
        STATE_MAP.put("Armed Forces Pacific","AP");
        STATE_MAP.put("British Columbia","BC");
        STATE_MAP.put("California","CA");
        STATE_MAP.put("Colorado","CO");
        STATE_MAP.put("Connecticut","CT");
        STATE_MAP.put("Delaware","DE");
        STATE_MAP.put("District Of Columbia","DC");
        STATE_MAP.put("Florida","FL");
        STATE_MAP.put("Georgia","GA");
        STATE_MAP.put("Guam","GU");
        STATE_MAP.put("Hawaii","HI");
        STATE_MAP.put("Idaho","ID");
        STATE_MAP.put("Illinois","IL");
        STATE_MAP.put("Indiana","IN");
        STATE_MAP.put("Iowa","IA");
        STATE_MAP.put("Kansas","KS");
        STATE_MAP.put("Kentucky","KY");
        STATE_MAP.put("Louisiana","LA");
        STATE_MAP.put("Maine","ME");
        STATE_MAP.put("Manitoba","MB");
        STATE_MAP.put("Maryland","MD");
        STATE_MAP.put("Massachusetts","MA");
        STATE_MAP.put("Michigan","MI");
        STATE_MAP.put("Minnesota","MN");
        STATE_MAP.put("Mississippi","MS");
        STATE_MAP.put("Missouri","MO");
        STATE_MAP.put("Montana","MT");
        STATE_MAP.put("Nebraska","NE");
        STATE_MAP.put("Nevada","NV");
        STATE_MAP.put("New Brunswick","NB");
        STATE_MAP.put("New Hampshire","NH");
        STATE_MAP.put("New Jersey","NJ");
        STATE_MAP.put("New Mexico","NM");
        STATE_MAP.put("New York","NY");
        STATE_MAP.put("Newfoundland","NF");
        STATE_MAP.put("North Carolina","NC");
        STATE_MAP.put("North Dakota","ND");
        STATE_MAP.put("Northwest Territories","NT");
        STATE_MAP.put("Nova Scotia","NS");
        STATE_MAP.put("Nunavut","NU");
        STATE_MAP.put("Ohio","OH");
        STATE_MAP.put("Oklahoma","OK");
        STATE_MAP.put("Ontario","ON");
        STATE_MAP.put("Oregon","OR");
        STATE_MAP.put("Pennsylvania","PA");
        STATE_MAP.put("Prince Edward Island","PE");
        STATE_MAP.put("Puerto Rico","PR");
        STATE_MAP.put("Quebec","PQ");
        STATE_MAP.put("Rhode Island","RI");
        STATE_MAP.put("Saskatchewan","SK");
        STATE_MAP.put("South Carolina","SC");
        STATE_MAP.put("South Dakota","SD");
        STATE_MAP.put("Tennessee","TN");
        STATE_MAP.put("Texas","TX");
        STATE_MAP.put("Utah","UT");
        STATE_MAP.put("Vermont","VT");
        STATE_MAP.put("Virgin Islands","VI");
        STATE_MAP.put("Virginia","VA");
        STATE_MAP.put("Washington","WA");
        STATE_MAP.put("West Virginia","WV");
        STATE_MAP.put("Wisconsin","WI");
        STATE_MAP.put("Wyoming","WY");
        STATE_MAP.put("Yukon Territory","YT");
    }



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
        rowItems.clear();

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
                Set<String> stateKeys = STATE_MAP.keySet();
                ArrayList<String> abbreviations = new ArrayList<String>();

                //find applicable state abbreviations
                for(String key : stateKeys){
                    if(key.toUpperCase().startsWith(constraint)){
                        abbreviations.add(STATE_MAP.get(key));
                    }
                }

                //Filter
                for (PlateStruct plate : plateInfoList) {
                    if (plate.getPlateInfo().toUpperCase().contains(constraint)){
                        rowItems.add(new RowItem(plate));
                    }
                    for(String abbreviation : abbreviations){
                        if(plate.getPlateState().toUpperCase().startsWith(abbreviation)){
                            rowItems.add(new RowItem(plate));
                        }
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

    @Override
    public void onResume(){
        super.onResume();
        updatePlateInfo();
        displayRows();
    }


    private void displayRows() {

        //Create and set the adapter
        adapter = new CustomListViewAdapter(getActivity(), R.layout.list_single, rowItems);
        listView.setAdapter(adapter);
    }


}



