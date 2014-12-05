package cs121.team5.com.licenseplate;

/**
 * Created by perry_000 on 11/3/2014.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListViewAdapter extends ArrayAdapter<PlateStruct> {

    List<PlateStruct> rowItems;
    Context context;

    public CustomListViewAdapter(Context context, int resourceId,
                                 List<PlateStruct> items) {
        super(context, resourceId, items);
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
        ImageView special;
        Button delete;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final PlateStruct rowItem = rowItems.get(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_single, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.special = (ImageView) convertView.findViewById(R.id.star);
            holder.delete = (Button) convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //Set what appears in each field
        holder.txtDesc.setText(rowItem.getPlateName());
        holder.txtTitle.setText(rowItem.getPlateState());
        holder.imageView.setImageBitmap(rowItem.getPlateBitmap());

        //Special Plate Star
        if(rowItem.getPlateSpecial()){
            holder.special.setVisibility(View.VISIBLE);
        }
        else{
            holder.special.setVisibility(View.GONE);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String infoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                 + "/License_Plate_info/";
                String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/License_Plate/";
                File file = new File(imagePath + rowItem.getPlateName()+".jpg");
                File infoFile = new File(infoPath + rowItem.getPlateName()+".txt");
                //Log.d("file", rowItem.getDesc()+".jpg");
                boolean deleted = file.delete();
                boolean deletedInfo = infoFile.delete();
                if (deleted && deletedInfo) {
                    remove(rowItem);
                    Toast.makeText(context, "Deleted plate", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Could not delete plate", Toast.LENGTH_SHORT).show();
                }
            }


        });
        return convertView;
    }


}
