package myfirstapp.example.com.licenseplate;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


public class getPicActivity extends Activity {

    Button getPicButton;
    ImageView imgFavorite;

    private Camera cameraObject;
    private ShowCamera showCamera;
    private ImageView pic;

    public static Camera isCameraAvailiable(){
        Camera object = null;
        try {
            object = Camera.open(0);
           // Log.d("ADebugTag", "Camera open!");
        }
        catch (Exception e){
           // Log.d("ADebugTag","Camera not available!");
        }
        return object;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_pic);

        Intent intent = getIntent();
        pic = (ImageView)findViewById(R.id.imageView1);
        cameraObject = isCameraAvailiable();
        showCamera = new ShowCamera(this, cameraObject);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(showCamera);
    }

    public void snapIt(View view){
        cameraObject.takePicture(null, null, capturedIt);
    }

    private Camera.PictureCallback capturedIt = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if(bitmap==null){
                Toast.makeText(getApplicationContext(), "not taken", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "taken", Toast.LENGTH_SHORT).show();
            }
            cameraObject.release();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bp = (Bitmap) data.getExtras().get("data");
        imgFavorite.setImageBitmap(bp);
    }

}
