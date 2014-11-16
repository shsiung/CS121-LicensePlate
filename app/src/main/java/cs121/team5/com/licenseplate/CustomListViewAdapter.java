package cs121.team5.com.licenseplate;

/**
 * Created by perry_000 on 11/3/2014.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomListViewAdapter extends ArrayAdapter<RowItem> {

    Context context;

    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
        Button delete;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        final RowItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_single, null);
            holder = new ViewHolder();
            holder.txtDesc = (TextView) convertView.findViewById(R.id.desc);
            holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
            holder.imageView = (ImageView) convertView.findViewById(R.id.icon);
            holder.delete = (Button) convertView.findViewById(R.id.btnDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtDesc.setText(rowItem.getDesc());
        holder.txtTitle.setText(rowItem.getTitle());
        holder.imageView.setImageBitmap(rowItem.getImageBitmap());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String infoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                 + "/License_Plate_info/";
                String imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/License_Plate/";
                File file = new File(imagePath + rowItem.getDesc()+".jpg");
                File infoFile = new File(infoPath + rowItem.getDesc()+".txt");
                //Log.d("file", rowItem.getDesc()+".jpg");
                boolean deleted = file.delete();
                boolean deletedInfo = infoFile.delete();
                if (deleted && deletedInfo) {
                    remove(rowItem);
                    Toast.makeText(context, "Deleted plate", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Could not delete plate", Toast.LENGTH_SHORT).show();
                }
            }


        });
        return convertView;
    }

}