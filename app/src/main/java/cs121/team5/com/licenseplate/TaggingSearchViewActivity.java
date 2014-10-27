package cs121.team5.com.licenseplate;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by Huameng on 2014/10/14.
 */
//public class TaggingSearchViewActivity {
//

//    }


import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

public class TaggingSearchViewActivity extends Activity {
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

//        public void initActionbar() {
//            // 自定义标题栏
//            getActionBar().setDisplayShowHomeEnabled(false);
//            getActionBar().setDisplayShowTitleEnabled(false);
//            getActionBar().setDisplayShowCustomEnabled(true);
//            LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View mTitleView = mInflater.inflate(R.layout.custom_action_bar_layout,
//                    null);
//            getActionBar().setCustomView(
//                    mTitleView,
//                    new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT,
//                            LayoutParams.WRAP_CONTENT));
//            searchView = (SearchView) mTitleView.findViewById(R.id.search_view);
//        }
//
//        public Object[] loadData() {
//            mAllList.add("aa");
//            mAllList.add("ddfa");
//            mAllList.add("qw");
//            mAllList.add("sd");
//            mAllList.add("fd");
//            mAllList.add("cf");
//            mAllList.add("re");
//            return mAllList.toArray();
//        }
//
//        @Override
//        public boolean onQueryTextChange(String newText) {
//            if (TextUtils.isEmpty(newText)) {
//                // Clear the text filter.
//                listView.clearTextFilter();
//            } else {
//                // Sets the initial value for the text filter.
//                listView.setFilterText(newText.toString());
//            }
//            return false;
//        }
//
//        @Override
//        public boolean onQueryTextSubmit(String query) {
//            // TODO Auto-generated method stub
//            return false;
//        }
}

