package net.cozz.danco.finalproject.activities;

import android.content.Intent;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import net.cozz.danco.finalproject.providers.BeerData;
import net.cozz.danco.finalproject.providers.BeerDataSource;
import net.cozz.danco.finalproject.R;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
    I started with trying to access the Camera object, then the camera2 API and finally settled on
    the Intent method since it was simpler.
 */
public class AddBeerActivity extends AppCompatActivity {
    private static final String TAG = AddBeerActivity.class.getCanonicalName();
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";

    private Uri fileUri;

    private EditText beerName;
    private EditText description;
    private EditText pubName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_beer);

        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                        fileUri, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
                Toast.makeText(this, "Dude, something went wrong", Toast.LENGTH_LONG).show();
            }

            ImageView image = (ImageView)findViewById(R.id.beer_to_add);
            image.setImageURI(fileUri);
            // I'd like to add an on click here that will bring up the location where the photo was
            // taken in a map view, but again, I'm out of time to finish that part

            beerName = (EditText) findViewById(R.id.edit_txt_beer_name);
            description = (EditText) findViewById(R.id.edit_txt_description);
            pubName = (EditText) findViewById(R.id.edit_txt_pub_name);

            Button btnOk = (Button) findViewById(R.id.btnOk);
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strBeerName = beerName.getText().toString();
                    String strDescription = description.getText().toString();
                    String strPubName = pubName.getText().toString();

                    doSaveBeer(strBeerName, strDescription, strPubName);
                }
            });

            Button btnCancel = (Button) findViewById(R.id.btnCancel);
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }
    }


    private void doSaveBeer(final String beerName, final String desc, final String pubName) {
        BeerDataSource datasource = new BeerDataSource(this);
        try {
            datasource.open();
            int i = 0;
            BeerData beer = new BeerData(beerName);
            beer.setImageFileUri(fileUri.toString());
            beer.setDescription(desc);
            beer.setPubName(pubName);

            ExifInterface exif = new ExifInterface(fileUri.toString());
            if (isLocationAvailable(exif)) {
                Location location = new Location("");
                location.setLatitude(Double.parseDouble(
                        exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)));
                location.setLongitude(Double.parseDouble(
                        exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)));
                beer.setLocation(location);
            }
            datasource.addBeerData(beer);

            Intent intent = new Intent(getApplicationContext(), BeerListActivity.class);
            startActivity(intent);
        } catch (SQLException | IOException e) {
            Log.d(TAG, e.getMessage());
            e.printStackTrace();
        } finally {
            datasource.close();
        }
    }


    private boolean isLocationAvailable(ExifInterface exif) {
        return exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE) != null &&
                exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE) != null;
    }


    /** Create a file Uri for saving an image or video */
    private Uri getOutputMediaFileUri(int type){
        File outputFile = getOutputMediaFile(type);
        if (outputFile == null) {
            // throw error, cancel operation
            finish();
        }
        return Uri.fromFile(outputFile);
    }


    private File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        if (!isExternalStorageWritable()) {
            return null;
        }

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), getResources().getString(R.string.app_name));
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d(getResources().getString(R.string.app_name), "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_beer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
