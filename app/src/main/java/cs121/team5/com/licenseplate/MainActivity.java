package cs121.team5.com.licenseplate;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;


public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tabHost = getTabHost();

        // Tab for Photos
        TabHost.TabSpec cameraSpec = tabHost.newTabSpec("Camera");
        // setting Title and Icon for the Tab
        cameraSpec.setIndicator("Camera", getResources().getDrawable(R.drawable.ic_launcher));
        Intent photosIntent = new Intent(this, GetPicActivity.class);
        cameraSpec.setContent(photosIntent);

        // Tab for Songs
        TabHost.TabSpec viewPlatesSpec = tabHost.newTabSpec("View Plates");
        viewPlatesSpec.setIndicator("View Plates", getResources().getDrawable(R.drawable.ic_launcher));
        Intent songsIntent = new Intent(this, TaggingSearchViewActivity.class);
        viewPlatesSpec.setContent(songsIntent);

        // Adding all TabSpec to TabHost
        tabHost.addTab(cameraSpec); // Adding photos tab
        tabHost.addTab(viewPlatesSpec); // Adding songs tab
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
