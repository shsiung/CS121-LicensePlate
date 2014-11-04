package cs121.team5.com.licenseplate;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(setIndicator(MainActivity.this,mTabHost.newTabSpec("camera"),
                R.drawable.tab_indicator_gen,"Camera",R.drawable.camera), GetPicActivity.class,null);

        mTabHost.addTab(setIndicator(MainActivity.this,mTabHost.newTabSpec("viewPlate"),
                R.drawable.tab_indicator_gen,"View Plates",R.drawable.serach_view_ic), TaggingSearchViewActivity.class,null);

        mTabHost.addTab(setIndicator(MainActivity.this,mTabHost.newTabSpec("mapView"),
                R.drawable.tab_indicator_gen,"Map View",R.drawable.map_ic), GPSMapLocator.class,null);
    }


    /*
        Example from http://androidcodeblogspot.blogspot.com/2014/02/android-fragment-tab-example-bottom.html
     */
    private TabHost.TabSpec setIndicator(Context ctx, TabHost.TabSpec spec,
                                 int resid, String string, int genresIcon) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.tab_item, null);
        v.setBackgroundResource(resid);
        TextView tv = (TextView)v.findViewById(R.id.txt_tabtxt);
        ImageView img = (ImageView)v.findViewById(R.id.img_tabtxt);

        tv.setText(string);
        img.setBackgroundResource(genresIcon);
        return spec.setIndicator(v);
    }

    // Default functions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Default functions
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
