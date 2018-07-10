package com.app.knbs.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.knbs.activity.Sector;
import com.app.knbs.app.EndPoints;
import com.app.knbs.app.MyApplication;
import com.app.knbs.database.DBHandler;
import com.app.knbs.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Application Created by Rodney on 14-Sep-16.
 */
public class FavouriteService extends IntentService {

    public FavouriteService() {
        super(FavouriteService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final DatabaseHelper dbHelper = new DatabaseHelper(this);
        //getFilesDir().getAbsolutePath()

        if (intent != null) {
            String status = intent.getStringExtra("status");
            String sector = intent.getStringExtra("sector");
            String tab = intent.getStringExtra("tab");
            String sector_id = intent.getStringExtra("sector_id");

            dbHelper.addFavourite(sector_id,status);
            dbHelper.getSectorData();

            Intent sector_intent = new Intent(getApplicationContext(), Sector.class);
            sector_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            sector_intent.putExtra("sector",sector );
            sector_intent.putExtra("tab",tab );
            startActivity(sector_intent);
        }else{
            Toast.makeText(getApplicationContext(), "Unable to add to favourite", Toast.LENGTH_LONG).show();
        }
    }

}
