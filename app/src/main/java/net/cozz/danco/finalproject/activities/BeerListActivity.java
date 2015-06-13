package net.cozz.danco.finalproject.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import net.cozz.danco.finalproject.adapters.AdvancedListViewAdapter;
import net.cozz.danco.finalproject.R;


public class BeerListActivity extends AppCompatActivity {

    private AdvancedListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));

        setTitle("My Beers");

        adapter = new AdvancedListViewAdapter(this);
        ListView listView = (ListView) findViewById(R.id.list_view_beers);
        listView.setAdapter(adapter);

        Button btnAddAnother = (Button) findViewById(R.id.add_another);
        btnAddAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddBeerActivity.class);
                startActivity(intent);
            }
        });

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_beer_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_another) {
            Intent intent = new Intent(getApplicationContext(), AddBeerActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
