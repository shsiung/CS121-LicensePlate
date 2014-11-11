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

public class CustomListViewAdapter extends ArrayAdapter<RowItem> implements Filterable {

    private List<RowItem> rowItems;
    Context context;

    public CustomListViewAdapter(Context context, int resourceId,
                                 List<RowItem> items) {
        super(context, resourceId, items);
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
        Button delete;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItem rowItem = rowItems.get(position);

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
                String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                                 + "/License_Plate/";
                RowItem deleteItem = rowItems.get(position);
                File file = new File(dirPath + deleteItem.getAddress());
                Log.d("file", deleteItem.getAddress());
                boolean deleted = file.delete();
                if (deleted) {
                    rowItems.remove(deleteItem);
                    Toast.makeText(context, "Deleted plate", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Could not delete plate", Toast.LENGTH_SHORT).show();
                }
            }


        });
        return convertView;
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.d("Filter","performing filtering");
                Log.d("Filter constraint", constraint.toString());
                FilterResults results = new FilterResults();

                if(constraint==null || constraint.length()==0){
                    results.values = rowItems;
                    results.count = rowItems.size();
                }
                else{
                    List<RowItem> nRowItems = new ArrayList<RowItem>();

                    for(RowItem r : rowItems){
                        Log.d("Title string",r.getTitle().toUpperCase());
                        Log.d("Desc string",r.getDesc().toUpperCase());
                        if(r.getTitle().toUpperCase().startsWith(constraint.toString().toUpperCase()) ||
                                r.getDesc().toUpperCase().startsWith(constraint.toString().toUpperCase())){
                            nRowItems.add(r);
                        }
                        results.values = nRowItems;
                        results.count = nRowItems.size();
                    }
                }
                return results;
            };

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.d("Test", "publishing");
                if(results.count == 0){
                    notifyDataSetInvalidated();
                }
                else {
                    Log.d("results",results.values.toString());
                    rowItems =(ArrayList<RowItem>) results.values;


                    Log.d("Test", "Attempting to change");
                    notifyDataSetChanged();
                }
            }
        };
    }
}
//http://www.survivingwithandroid.com/2012/10/android-listview-custom-filter-and.html