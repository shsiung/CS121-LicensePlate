package cs121.team5.com.licenseplate;

import com.robotium.solo.Solo;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.ViewGroup;



public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity>{

    private Solo solo;

    public MainActivityTest() {
        super(MainActivity.class);
    }



    @Override
    public void setUp() throws Exception {
        //setUp() is run before a test case is started.
        //This is where the solo object is created.
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        //tearDown() is run after a test case has finished.
        //finishOpenedActivities() will finish all the activities that have been opened during the test execution.
        solo.finishOpenedActivities();
    }

    public void test() throws Exception {
        //Unlock the lock screen
        solo.unlockScreen();
        //Assert that Main activity is opened
        solo.assertCurrentActivity("Expected Main activity", "MainActivity");

        // Check on Tab Views
        ViewGroup tabs = (ViewGroup) solo.getView(android.R.id.tabs);
        View tabView = tabs.getChildAt(0); //
        solo.clickOnView(tabView);
        GetPicActivityTest();

        // Wait before click
        solo.sleep(2000);
        tabView = tabs.getChildAt(1); //
        solo.clickOnView(tabView);
        SearchViewActivityTest();

        // Wait before click
        solo.sleep(2000);
        tabView = tabs.getChildAt(2); //
        solo.clickOnView(tabView);

    }

    /*
    *  This test the flow of GetPic activity.
    *  It should takes a photo and then brings up the tagging view activity
    *  and exit back again.
    */
    public void GetPicActivityTest(){

        // Before taking a photo, should give warning
        solo.clickOnText("Save Plate");
        assertTrue(solo.waitForText("You haven't taken a photo yet!"));

        solo.clickOnText("Capture");
        assertTrue(solo.waitForText("Photo Taken"));

        // After taking a photo, should bring out tag view
        solo.clickOnText("Save Plate");
        solo.assertCurrentActivity("Expect Tagging Activity", "TaggingMainActivity");

        // Exit Tagging View
        solo.clickOnText("Cancel");

        // Return to main view
        solo.assertCurrentActivity("Expected Main activity", "MainActivity");

    }

    public void SearchViewActivityTest() {

        // Click the first item
        solo.clickInList(0);

        // Should bring up tagging view activity
        solo.assertCurrentActivity("Expect Tagging Activity", "TaggingMainActivity");

        String nameOfPlate = solo.getEditText(1).getText().toString();

        // Exit Tagging View
        solo.hideSoftKeyboard();
        solo.clickOnText("Cancel");

        // Return to main view
        solo.assertCurrentActivity("Expected Main activity", "MainActivity");

        // Click the first item
        solo.clickInList(0);
//        EditText enText = solo.getEditText(1);
//        assertTrue(enText.getText().toString() == nameOfPlate);

        String testString = "ROBOTEST";
        solo.clearEditText(1);
        solo.enterText(1,testString);

        // Save new plate info
        solo.clickOnText("Save");

        // Return to main view
        solo.assertCurrentActivity("Expected Main activity", "MainActivity");

        solo.clickInList(0);
//        enText = solo.getEditText(1);
//        assertTrue(enText.toString() == testString);

        // Exit Tagging View
        solo.clickOnText("Cancel");

        // Return to main view
        solo.assertCurrentActivity("Expected Main activity", "MainActivity");

    }
}
