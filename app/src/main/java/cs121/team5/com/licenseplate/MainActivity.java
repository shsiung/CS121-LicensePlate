package cs121.team5.com.licenseplate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends Activity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        //mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

//        mTabHost.addTab(mTabHost.newTabSpec("camera").setIndicator("Camera"),
//                GetPicActivity.class, null);
//
//        mTabHost.addTab(mTabHost.newTabSpec("viewPlate").setIndicator("View Plates"),
//                TaggingSearchViewActivity.class, null);

//        // Tab for Photos
//        TabHost.TabSpec cameraSpec = tabHost.newTabSpec("Camera");
//        // setting Title and Icon for the Tab
//        cameraSpec.setIndicator("Camera", getResources().getDrawable(R.drawable.ic_launcher));
//        Intent cameraIntent = new Intent(this, GetPicActivity.class);
//        cameraSpec.setContent(cameraIntent);
//
//        // Tab for Songs
//        TabHost.TabSpec viewPlatesSpec = tabHost.newTabSpec("View Plates");
//        viewPlatesSpec.setIndicator("View Plates", getResources().getDrawable(R.drawable.ic_launcher));
//        Intent serachViewIntent = new Intent(this, TaggingSearchViewActivity.class);
//        viewPlatesSpec.setContent(serachViewIntent);
//
//        // Adding all TabSpec to TabHost
//        tabHost.addTab(cameraSpec); // Adding photos tab
//        tabHost.addTab(viewPlatesSpec); // Adding songs tab
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

    /** Called when the user clicks the Send button */
    public void getPicture(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, GetPicActivity.class);
        startActivity(intent);
    }

    // Called when the user clicks the view plates button
    public void searchView(View view){
        Intent intent =new Intent(this, TaggingSearchViewActivity.class);
        startActivity(intent);
    }

}
