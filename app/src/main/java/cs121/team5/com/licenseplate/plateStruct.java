package cs121.team5.com.licenseplate;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

/**
 * Created by perry_000 on 11/9/2014.
 */
public class plateStruct {
    private String plateName;
    private String plateState;
    private LatLng plateLatLng;
    private Bitmap plateBitmap;
    private Boolean plateSpecial;


    public plateStruct(String plateName, String plateState, LatLng plateLatLng,
                        Bitmap plateBitmap, Boolean plateSpecial){
        this.plateName = plateName;
        this.plateState = plateState;
        this.plateLatLng = plateLatLng;
        this.plateBitmap = plateBitmap;
        this.plateSpecial = plateSpecial;
    }

    public plateStruct(File f){
        String[] separatedString = f.getName().split("_");

        this.plateState = separatedString[0];
        this.plateName = separatedString[1];
        this.plateLatLng = new LatLng(Double.parseDouble(separatedString[3]),
                Double.parseDouble(separatedString[4]));
        this.plateSpecial = Boolean.parseBoolean(separatedString[2]);
        this.plateBitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
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
}
