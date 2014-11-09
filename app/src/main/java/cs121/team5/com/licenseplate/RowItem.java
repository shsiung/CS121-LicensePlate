package cs121.team5.com.licenseplate;

import android.graphics.Bitmap;

/**
 * Created by perry_000 on 11/3/2014.
 */
public class RowItem {
    private Bitmap imageBitmap;
    private String title;
    private String desc;

    public RowItem(PlateStruct plate){
        this.imageBitmap = plate.getPlateBitmap();
        this.title = plate.getPlateState();
        this.desc = plate.getPlateName();
    }

    public RowItem(Bitmap imageBitmap, String title, String desc) {
        this.imageBitmap = imageBitmap;
        this.title = title;
        this.desc = desc;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }
    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title + "\n" + desc;
    }
}