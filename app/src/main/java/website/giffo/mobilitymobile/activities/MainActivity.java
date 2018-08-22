package website.giffo.mobilitymobile.activities;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import website.giffo.mobilitymobile.R;
import website.giffo.mobilitymobile.adapters.TaxiListAdapter;
import website.giffo.mobilitymobile.objects.Taxi;

public class MainActivity extends AppCompatActivity {

    // final Variables
    private static final int MAX_MINUTES = 301;
    private static final int UPDATE_DELAY = 5000;

    // Variables

    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private  ArrayList<Taxi> mTaxis;
    private TaxiListAdapter taxiListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find recyclerview view
        RecyclerView mRecyclerView = findViewById(R.id.taxi_recycler_view);

        // get taxi list
        try {
            // get json database from assets
            JSONObject jsonDatabase = new JSONObject(getJsonDatabase("database.json", this));
            // get the taxi list array
            JSONArray taxiList = jsonDatabase.getJSONArray("taxi_list");


            // create new taxi object
            mTaxis = new ArrayList<>();

            // get random numbers for eta
            Random r = new Random();

            // set taxi object data from jason
            for (int i = 0; i < taxiList.length(); i++) {
                // get json obj from json array
                JSONObject taxiJson = (JSONObject) taxiList.get(i);

                // set data to obj
                Taxi taxi = new Taxi();
                taxi.setmTaxiName(taxiJson.getString("station_name"));
                taxi.setmTaxiIcon(taxiJson.getInt("ic_id"));
                taxi.setmEta(r.nextInt(MAX_MINUTES));
                // add taxi obj to list
                mTaxis.add(taxi);
            }

            // sort list after first time
            sortList();

            // create the taxis adapter
             taxiListAdapter = new TaxiListAdapter(this, mTaxis);

            // set adapter to recycler view
            if (mRecyclerView != null) {
                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                mRecyclerView.setAdapter(taxiListAdapter);
                taxiListAdapter.notifyDataSetChanged();
            }

            // set random numbers
            getRandomEta();

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }


    }




    public static String getJsonDatabase (String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }

    public void getRandomEta() {


        mHandler.postDelayed( mRunnable = new Runnable() {
            public void run() {

                for (int i = 0; i < mTaxis.size(); i++) {

                    Random random = new Random();
                    mTaxis.get(i).setmEta(random.nextInt(MAX_MINUTES));

                    taxiListAdapter.notifyDataSetChanged();
                }

                // compare and sort by eta
               sortList();
                mHandler.postDelayed(mRunnable, UPDATE_DELAY);
            }
        }, UPDATE_DELAY);

    }



    @Override
    protected void onPause() {
        mHandler.removeCallbacks(mRunnable); //stop handler when activity not visible
        mRunnable = null;
        super.onPause();
    }

    @Override
    protected void onResume() {
        // start to get random numbers again on resume
        if (mRunnable == null) {
            getRandomEta();
        }
        super.onResume();
    }


    private void sortList(){

        Collections.sort(mTaxis, new Comparator< Taxi >() {
            @Override
            public int compare(Taxi o1, Taxi o2) {
                return o1.getmEta()- o2.getmEta(); // Ascending
            }

        });
    }
}
