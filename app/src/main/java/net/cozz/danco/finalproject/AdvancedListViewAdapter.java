package net.cozz.danco.finalproject;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class AdvancedListViewAdapter extends BaseAdapter {
    private final static String TAG = AdvancedListViewAdapter.class.getCanonicalName();
    private List<BeerData> content;
    private Context context;
    private AdvancedListViewAdapter adapter;


    public AdvancedListViewAdapter(Context context){//}, ArrayList<State> content) {
        super();
        Log.i(TAG, "AdvancedListViewAdapter - constructor...");

        this.context = context;

        BeerDataSource datasource = new BeerDataSource(context);
        try {
            datasource.open();
            content = datasource.getBeerDataList();
            Log.d("", content.toString());
        } catch (SQLException e) {
            Log.d("MyActivity", "unable to open datasource");
        } finally {
            datasource.close();
        }

    }

    public void setListItems(ArrayList<BeerData> newList) {
        Log.d(TAG, "setListItems");

        content = newList;
        notifyDataSetChanged();
    }

    public void addItem(BeerData stateObj){
        Log.d(TAG, "addItem");

        content.add(stateObj);
        notifyDataSetChanged();
    }

    public void removeItem(BeerData stateObj){
        Log.d(TAG, "addItem");

        content.remove(stateObj);
        notifyDataSetChanged();
    }

    public void clear() {
        Log.d(TAG, "clear");

        content.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return content.size();
    }

    @Override
    public BeerData getItem(int position) {
        return content.get(position);
    }

    @Override
    public long getItemId(int position) {
        return content.indexOf( content.get(position) );
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){ //the first time around, the view is null, so inflate it

            //inflate using the system inflater. This returns a reference to the inflater,
            // which inflates the resource XML to the corresponding view
            LayoutInflater inflater = LayoutInflater.from(context);

            //use the inflate method this way.
            convertView = inflater.inflate(R.layout.beer_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.my_beers);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.description = (TextView) convertView.findViewById(R.id.description);
            viewHolder.pub = (TextView) convertView.findViewById(R.id.pub);
            convertView.setTag(viewHolder);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(getItem(position));
                }
            });

        }

        ((ViewHolder) convertView.getTag()).image.setImageURI(
                Uri.parse(getItem(position).getImageFileUri()));
        ((ViewHolder) convertView.getTag()).name.setText(getItem(position).getName());
        ((ViewHolder) convertView.getTag()).description.setText(getItem(position).getDescription());
        ((ViewHolder) convertView.getTag()).pub.setText(getItem(position).getPubName());

        return convertView;

    }

    static class ViewHolder {
        //SmartImageView image;
        ImageView image;
        TextView name;
        TextView description;
        TextView pub;
    }
}
