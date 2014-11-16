package cs121.team5.com.licenseplate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by perry_000 on 11/9/2014.
 */
public class PlateStruct {

    private static String plateInfoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/License_Plate_Info";
    private static String plateImagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/License_Plate";

    private String plateName;
    private String plateState;
    private LatLng plateLatLng;
    private Bitmap plateBitmap;
    private Boolean plateSpecial;
    private ArrayList<String> plateInfo;

    public PlateStruct(){
        // Do Nothing
    }

    public PlateStruct(String plateName, String plateState, LatLng plateLatLng,
                       Bitmap plateBitmap, Boolean plateSpecial){
        this.plateName = plateName;
        this.plateState = plateState;
        this.plateLatLng = plateLatLng;
        this.plateBitmap = plateBitmap;
        this.plateSpecial = plateSpecial;
    }

    public PlateStruct(File f){

        int fileNamePos = f.getName().lastIndexOf(".");

        setPlateStruct(f.getName());
        setPlateBitmap(f.getName().substring(0,fileNamePos)+".jpg");

    }


    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public void setPlateState(String plateState) {
        this.plateState = plateState;
    }

    public void setPlateLatLng(LatLng plateLatLng) {
        this.plateLatLng = plateLatLng;
    }

    public void setPlateBitmap(Bitmap plateBitmap) {
        this.plateBitmap = plateBitmap;
    }

    public void setPlateBitmap(String fileName) {
        File imagesFolder = new File(plateImagePath);
        String imagePath = imagesFolder.getAbsolutePath() + "/" + fileName;

        if (imagesFolder.exists()) {
            File plate = new File(imagePath);
            if (plate.exists()) {
                BitmapFactory.Options options;
                try {
                    // Set Thumbnail image
                    options = new BitmapFactory.Options();
                    options.inSampleSize = 1;  // Shrink the picture by a factor of 2
                    setPlateBitmap(BitmapFactory.decodeFile(imagePath, options));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setPlateSpecial(Boolean plateSpecial) {
        this.plateSpecial = plateSpecial;
    }

    public String getPlateName() {
        return plateName;
    }

    public String getPlateState() {
        return plateState;
    }

    public LatLng getPlateLatLng() {
        return plateLatLng;
    }

    public Bitmap getPlateBitmap() {
        return plateBitmap;
    }

    public Boolean getPlateSpecial() {
        return plateSpecial;
    }

    public void setPlateStruct(String fileInfoName){

        File infoFolder = new File(plateInfoPath);
        File output_text = new File(infoFolder, fileInfoName);
        plateInfo = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(output_text));
            String line;

            while ((line = br.readLine()) != null) {
                plateInfo.add(line);
            }
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            e.printStackTrace();
        }

        try {
            this.setPlateState(plateInfo.get(0));
            this.setPlateName(plateInfo.get(1));
            this.setPlateSpecial(Boolean.valueOf(plateInfo.get(2)));
            this.setPlateLatLng(new LatLng(Double.parseDouble(plateInfo.get(3)),
                    Double.parseDouble(plateInfo.get(4))));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getPlateInfo(){
        String nameOfFile =
                this.getPlateState() + "\r\n"+
                this.getPlateName() + "\r\n" +
                this.getPlateSpecial().toString()+"\r\n"+
                this.getPlateLatLng().latitude+"\r\n"+
                this.getPlateLatLng().longitude;
        return nameOfFile;
    }
}
