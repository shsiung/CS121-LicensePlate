package myfirstapp.example.com.myfirstapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    TextView mainTextView;
    Button mainButton;
    ListView mainListView;
    ArrayAdapter mArrayAdapter;
    ArrayList mNameList = new ArrayList();
    EditText mainEditText;
    android.widget.ShareActionProvider mShareActionProvider;

    private static final String PREFS = "prefs";
    private static final String PREF_NAME = "name";
    SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        mainTextView = (TextView) findViewById(R.id.main_textview);
        mainTextView.setText("Set in Java!");

        mainButton = (Button) findViewById(R.id.main_button);
        mainButton.setOnClickListener(this);

        // 4. Access the ListView
        mainListView = (ListView) findViewById(R.id.main_listview);

        mainEditText = (EditText) findViewById(R.id.mainEditText);

        // Create an ArrayAdapter for the ListView
        mArrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                mNameList);

        // Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mArrayAdapter);

        // 7. Greet the user, or ask for their name if new
        displayWelcome();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu.
        // Adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);

        // Access the Share Item defined in menu XML
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        // Access the object responsible for
        // putting together the sharing submenu
        if (shareItem != null) {
            mShareActionProvider = (android.widget.ShareActionProvider)shareItem.getActionProvider();
        }

        // Create an Intent to share your content
        setShareIntent();

        return true;
    }

    private void setShareIntent() {

        if (mShareActionProvider != null) {

            // create an Intent with the contents of the TextView
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Android Development");
            shareIntent.putExtra(Intent.EXTRA_TEXT, mainTextView.getText());

            // Make sure the provider knows
            // it should work with that Intent
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    /** Called when the user clicks the Send button
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }*/


    @Override
    public void onClick(View view) {
        mainTextView.setText("Button Pressed!");

        // Also add that value to the list shown in the ListView
        mNameList.add(mainEditText.getText().toString());
        mArrayAdapter.notifyDataSetChanged();

        // 5. Set this activity to react to list items being pressed
        mainListView.setOnItemClickListener(this);

        // 6. The text you'd like to share has changed,
        // and you need to update
        setShareIntent();
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {
        // Log the item's position and contents
        // to the console in Debug
        Log.d("omg android", position + ": " + mNameList.get(position));
    }

    public void displayWelcome() {

        // Access the device's key-value storage
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        // Read the user's name,
        // or an empty string if nothing found
        String name = mSharedPreferences.getString(PREF_NAME, "");

        if (name.length() > 0) {

            // If the name is valid, display a Toast welcoming them
            Toast.makeText(this, "Welcome back, " + name + "!", Toast.LENGTH_LONG).show();
        }
    }
}
