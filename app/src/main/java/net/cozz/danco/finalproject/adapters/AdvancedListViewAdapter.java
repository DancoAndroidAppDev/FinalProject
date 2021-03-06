package net.cozz.danco.finalproject.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.cozz.danco.finalproject.providers.BeerData;
import net.cozz.danco.finalproject.providers.BeerDataSource;
import net.cozz.danco.finalproject.R;

import java.io.FileNotFoundException;
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


    public AdvancedListViewAdapter(Context context) {//}, ArrayList<State> content) {
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


    public void addItem(BeerData stateObj) {
        Log.d(TAG, "addItem");

        content.add(stateObj);
        notifyDataSetChanged();
    }


    public void removeItem(BeerData stateObj) {
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
        return content.indexOf(content.get(position));
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) { //the first time around, the view is null, so inflate it

            //inflate using the system inflater. This returns a reference to the inflater,
            // which inflates the resource XML to the corresponding view
            LayoutInflater inflater = LayoutInflater.from(context);

            //use the inflate method this way.
            convertView = inflater.inflate(R.layout.beer_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.beer_image);
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

        viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.image.setImageBitmap(getScaledImage(getItem(position).getImageFileUri(),
                        viewHolder.image.getLayoutParams().width,
                        viewHolder.image.getLayoutParams().height));
        viewHolder.name.setText(getItem(position).getName());
        viewHolder.description.setText(getItem(position).getDescription());
        viewHolder.pub.setText(getItem(position).getPubName());

        return convertView;

    }


    /**
     * Need to use ContentResolver to resolve the image from the Uri
     *
     * TODO -- should probably store the scaled image and use stored image to save work
     * @param fileUri - Uri to image
     * @param targetWidth - target width
     * @param targetHeight - target height
     * @return - scaled bitmap
     */
    private Bitmap getScaledImage(final String fileUri, int targetWidth, int targetHeight) {
        Uri uri = Uri.parse(fileUri);

        Bitmap bitmapToScale = null;
        try {
            bitmapToScale =
                    BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

        Bitmap bm = Bitmap.createScaledBitmap(bitmapToScale, targetWidth, targetHeight, false);
        return bm;
    }


    /**
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    /**
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    static class ViewHolder {
        //SmartImageView image;
        ImageView image;
        TextView name;
        TextView description;
        TextView pub;
    }
}
