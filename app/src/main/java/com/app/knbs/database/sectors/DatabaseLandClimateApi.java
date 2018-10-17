package com.app.knbs.database.sectors;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.knbs.database.CountyHelper;
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.services.ReportLoader;
import com.app.knbs.services.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 28/02/2018.
 */

public class DatabaseLandClimateApi {
    private Context context;
    public DatabaseLandClimateApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);
    private ReportLoader loader = new ReportLoader(context);
    CountyHelper countyHelper = new CountyHelper();

    public void loadData(final ProgressDialog d){
        insertInto_land_and_climate_rainfall(d);
        insertInto_land_and_climate_surface_area_by_category(d);
        insertInto_land_and_climate_temperature(d);
        insertInto_land_and_climate_topography_altitude(d);
        insertInto_environment_and_natural_resources_environmental_natural_resources_trends(d);

        insertInto_environment_and_natural_resources_average_export_prices_ash(d);
        insertInto_environment_and_natural_resources_development_expenditure_water(d);
        insertInto_environment_and_natural_resources_government_forest(d);
        insertInto_environment_and_natural_resources_water_purification_points(d);
        insertInto_environment_and_natural_resources_population_wildlife(d);

        insertInto_environment_and_natural_resources_quantity_and_value_of_fish_landed(d);
        insertInto_environment_and_natural_resources_quantity_of_total_minerals(d);
        insertInto_environment_and_natural_resources_record_sale_of_government_products(d);
        insertInto_environment_and_natural_resources_value_of_total_minerals(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_land_and_climate_rainfall(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Land and Climate Rainfall"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject countyObject  = response.getJSONObject(1);
                            JSONObject rainfallObject  = response.getJSONObject(2);
                            JSONObject rainInMMObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray rainArray = rainfallObject.getJSONArray("data");
                            JSONArray mmArray = rainInMMObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("land_and_climate_rainfall",null,null);

                            for(int i=0;i<yearArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("climate_rainfall_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("rainfall_id",i);
                                values.put("rainfall_in_mm",mmArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_rainfall",null,values);
                            }

                            db.close();
                            Log.d(TAG, "land_and_climate_rainfall: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_land_and_climate_surface_area_by_category(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Land and Climate Surface Area by Category"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject countyObject  = response.getJSONObject(0);
                            JSONObject catObject  = response.getJSONObject(1);
                            JSONObject areaObject  = response.getJSONObject(2);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray catArray = catObject.getJSONArray("data");
                            JSONArray areaArray = areaObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("land_and_climate_surface_area_by_category",null,null);

                            for(int i=0;i<areaArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("surface_area_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("category",catArray.getString(i));
                                values.put("area_sq_km",areaArray.getDouble(i));

                                success = db.insertOrThrow("land_and_climate_surface_area_by_category",null,values);
                            }

                            db.close();
                            Log.d(TAG, "land_and_climate_surface_area_by_category: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_land_and_climate_temperature(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Land and Climate Temperature"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject countyObject  = response.getJSONObject(1);
                            JSONObject tempObject  = response.getJSONObject(2);
                            JSONObject degreesObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray tempArray = tempObject.getJSONArray("data");
                            JSONArray degArray = degreesObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("land_and_climate_temperature",null,null);

                            for(int i=0;i<yearArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("climate_temperature_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("temperature_id",i);
                                values.put("temp_celsius_degrees",degArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_temperature",null,values);
                            }

                            db.close();
                            Log.d(TAG, "land_and_climate_temperature: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_land_and_climate_topography_altitude(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Land and Climate Topography Altitude"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject countyObject  = response.getJSONObject(1);
                            JSONObject typeObject  = response.getJSONObject(2);
                            JSONObject degreesObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray typeArray = typeObject.getJSONArray("data");
                            JSONArray degArray = degreesObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("land_and_climate_topography_altitude",null,null);

                            for(int i=0;i<yearArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("altitude_topography_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("geography_type",typeArray.getString(i));
                                values.put("altitude_in_metres",degArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_topography_altitude",null,values);
                            }

                            db.close();
                            Log.d(TAG, "land_and_climate_topography_altitude: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_environment_and_natural_resources_environmental_natural_resources_trends(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Environmental Natural Resources Trends"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject forObject  = response.getJSONObject(1);
                            JSONObject fishObject  = response.getJSONObject(2);
                            JSONObject mineObject  = response.getJSONObject(3);
                            JSONObject waterObject  = response.getJSONObject(4);
                            JSONObject gdpObject  = response.getJSONObject(5);
                            JSONObject resObject  = response.getJSONObject(6);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray forArray = forObject.getJSONArray("data");
                            JSONArray fishArray = fishObject.getJSONArray("data");
                            JSONArray mineArray = mineObject.getJSONArray("data");
                            JSONArray waterArray = waterObject.getJSONArray("data");
                            JSONArray gdpArray = gdpObject.getJSONArray("data");
                            JSONArray resArray = resObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("land_and_climate_trends_in_environment_and_natural_resources",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("industry_id",i+1);
                                values.put("ForestryandLogging",forArray.getInt(i));
                                values.put("FishingandAquaculture",fishArray.getInt(i));
                                values.put("Miningandquarrying",mineArray.getInt(i));
                                values.put("WaterSupply",waterArray.getInt(i));
                                values.put("GDPatCurrentPrices",gdpArray.getInt(i));
                                values.put("ResourceaspercentofGDP",resArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_trends_in_environment_and_natural_resources",null,values);
                            }

                            db.close();
                            Log.d(TAG, "land_and_climate_trends_in_environment_and_natural_resources: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_environment_and_natural_resources_average_export_prices_ash(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Average Export Prices Ash"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject sodaObject  = response.getJSONObject(1);
                            JSONObject fluorObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray sodaArray = sodaObject.getJSONArray("data");
                            JSONArray fluorArray = fluorObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("environment_and_natural_resources_average_export_prices_ash",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("average_id",i+1);
                                values.put("soda_ash",sodaArray.getInt(i));
                                values.put("fluorspar",fluorArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("environment_and_natural_resources_average_export_prices_ash",null,values);
                            }

                            db.close();
                            Log.d(TAG, "environment_and_natural_resources_average_export_prices_ash: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_environment_and_natural_resources_population_wildlife(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Population Wildlife"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject buffaloObject  = response.getJSONObject(1);
                            JSONObject zebraObject  = response.getJSONObject(2);
                            JSONObject elandObject  = response.getJSONObject(3);
                            JSONObject elephantObject  = response.getJSONObject(4);
                            JSONObject gerenukObject  = response.getJSONObject(5);
                            JSONObject giraffeObject  = response.getJSONObject(6);
                            JSONObject grantgazelleObject  = response.getJSONObject(7);
                            JSONObject grevyzebraObject  = response.getJSONObject(8);
                            JSONObject huntershartebeestObject  = response.getJSONObject(9);
                            JSONObject impalaObject  = response.getJSONObject(10);
                            JSONObject kongoniObject  = response.getJSONObject(11);
                            JSONObject kuduObject  = response.getJSONObject(12);
                            JSONObject oryxObject  = response.getJSONObject(13);
                            JSONObject ostrichObject  = response.getJSONObject(14);
                            JSONObject thomsongazelleObject  = response.getJSONObject(15);
                            JSONObject topiObject  = response.getJSONObject(16);
                            JSONObject warthogObject  = response.getJSONObject(17);
                            JSONObject waterbuckObject  = response.getJSONObject(18);
                            JSONObject wildebeestObject  = response.getJSONObject(19);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray buffArray = buffaloObject.getJSONArray("data");
                            JSONArray zebArray = zebraObject.getJSONArray("data");
                            JSONArray elArray = elandObject.getJSONArray("data");
                            JSONArray elephArray = elephantObject.getJSONArray("data");
                            JSONArray gerArray = gerenukObject.getJSONArray("data");
                            JSONArray girArray = giraffeObject.getJSONArray("data");
                            JSONArray graArray = grantgazelleObject.getJSONArray("data");
                            JSONArray grevyArray = grevyzebraObject.getJSONArray("data");
                            JSONArray hunterArray = huntershartebeestObject.getJSONArray("data");
                            JSONArray imArray = impalaObject.getJSONArray("data");
                            JSONArray konArray = kongoniObject.getJSONArray("data");
                            JSONArray kuduArray = kuduObject.getJSONArray("data");
                            JSONArray oryxArray = oryxObject.getJSONArray("data");
                            JSONArray ostArray = ostrichObject.getJSONArray("data");
                            JSONArray thomArray = thomsongazelleObject.getJSONArray("data");
                            JSONArray topiArray = topiObject.getJSONArray("data");
                            JSONArray wartArray = warthogObject.getJSONArray("data");
                            JSONArray watArray = waterbuckObject.getJSONArray("data");
                            JSONArray wildArray = wildebeestObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",i+1);
                                values.put("animal","Buffalo");
                                values.put("no_estimate",buffArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",2*(i+1));
                                values.put("animal","Burchells's Zebra");
                                values.put("no_estimate",zebArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",3*(i+1));
                                values.put("animal","Eland");
                                values.put("no_estimate",elArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",4*(i+1));
                                values.put("animal","Elephant");
                                values.put("no_estimate",elephArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",5*(i+1));
                                values.put("animal","Gerenuk");
                                values.put("no_estimate",gerArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",6*(i+1));
                                values.put("animal","Giraffe");
                                values.put("no_estimate",girArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",7*(i+1));
                                values.put("animal","Grant's Gazellle");
                                values.put("no_estimate",graArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",8*(i+1));
                                values.put("animal","Grevy's Zebra");
                                values.put("no_estimate",grevyArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",9*(i+1));
                                values.put("animal","Hunters Hartebeest");
                                values.put("no_estimate",hunterArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",10*(i+1));
                                values.put("animal","Hunters Hartebeest");
                                values.put("no_estimate",hunterArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",10*(i+1));
                                values.put("animal","Impala");
                                values.put("no_estimate",imArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",11*(i+1));
                                values.put("animal","Kongoni");
                                values.put("no_estimate",konArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",13*(i+1));
                                values.put("animal","Kudu");
                                values.put("no_estimate",kuduArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",14*(i+1));
                                values.put("animal","Oryx");
                                values.put("no_estimate",oryxArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",15*(i+1));
                                values.put("animal","Ostrich");
                                values.put("no_estimate",ostArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",16*(i+1));
                                values.put("animal","Thomson's Gazelle");
                                values.put("no_estimate",thomArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",16*(i+1));
                                values.put("animal","Topi");
                                values.put("no_estimate",topiArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",17*(i+1));
                                values.put("animal","Warthog");
                                values.put("no_estimate",wartArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",18*(i+1));
                                values.put("animal","Waterbuck");
                                values.put("no_estimate",watArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("species_id",19*(i+1));
                                values.put("animal","Wildebeest");
                                values.put("no_estimate",wildArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("land_and_climate_wildlife_population_estimates_kenya_rangelands",null,values);
                            }

                            db.close();
                            Log.d(TAG, "land_and_climate_wildlife_population_estimates_kenya_rangelands: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_environment_and_natural_resources_development_expenditure_water(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Development Expenditure Water"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject staffObject  = response.getJSONObject(1);
                            JSONObject ruralObject  = response.getJSONObject(2);
                            JSONObject miscObject  = response.getJSONObject(3);
                            JSONObject nationalconservationObject  = response.getJSONObject(4);
                            JSONObject irrObject  = response.getJSONObject(5);
                            JSONObject natirrObject  = response.getJSONObject(6);
                            JSONObject waterdevObject  = response.getJSONObject(7);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray staffArray = staffObject.getJSONArray("data");
                            JSONArray rurArray = ruralObject.getJSONArray("data");
                            JSONArray miscArray = miscObject.getJSONArray("data");
                            JSONArray natconArray = nationalconservationObject.getJSONArray("data");
                            JSONArray irrArray = irrObject.getJSONArray("data");
                            JSONArray natirrArray = natirrObject.getJSONArray("data");
                            JSONArray waterdevArray = waterdevObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("environment_and_natural_resources_development_expenditure_water",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("development_id",i+1);
                                values.put("water_development",waterdevArray.getDouble(i));
                                values.put("training_of_water_development_staff",staffArray.getDouble(i));
                                values.put("rural_water_supplies",rurArray.getDouble(i));
                                values.put("miscellaneous_and_special_water_programmes",miscArray.getDouble(i));
                                values.put("national_water_conservation_and_pipeline_corporation",natconArray.getDouble(i));
                                values.put("irrigation_development",irrArray.getDouble(i));
                                values.put("national_irrigation_board",natirrArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("environment_and_natural_resources_development_expenditure_water",null,values);
                            }

                            db.close();
                            Log.d(TAG, "environment_and_natural_resources_development_expenditure_water: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_environment_and_natural_resources_government_forest(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Government Forest"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject prevObject  = response.getJSONObject(1);
                            JSONObject plantedObject  = response.getJSONObject(2);
                            JSONObject clearObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray prevArray = prevObject.getJSONArray("data");
                            JSONArray plantedArray = plantedObject.getJSONArray("data");
                            JSONArray clearArray = clearObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("environment_and_natural_resources_government_forest",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("govt_id",i+1);
                                values.put("previous_plantation_area",prevArray.getDouble(i));
                                values.put("area_planted",plantedArray.getDouble(i));
                                values.put("area_clear_felled",clearArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("environment_and_natural_resources_government_forest",null,values);
                            }

                            db.close();
                            Log.d(TAG, "environment_and_natural_resources_government_forest: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_environment_and_natural_resources_quantity_and_value_of_fish_landed(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Quantity and Value of Fish Landed"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject typeObject  = response.getJSONObject(1);
                            JSONObject catObject  = response.getJSONObject(2);
                            JSONObject valObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray typeArray = typeObject.getJSONArray("data");
                            JSONArray catArray = catObject.getJSONArray("data");
                            JSONArray valArray = valObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("environment_and_natural_resources_quantity_value_fish_landed",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("quantity_id",i+1);
                                values.put("category",catArray.getString(i));
                                values.put("type",typeArray.getString(i));
                                values.put("value",valArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("environment_and_natural_resources_quantity_value_fish_landed",null,values);
                            }

                            db.close();
                            Log.d(TAG, "environment_and_natural_resources_quantity_value_fish_landed: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_environment_and_natural_resources_quantity_of_total_minerals(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Quantity of Total Minerals"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject catObject  = response.getJSONObject(1);
                            JSONObject descObject  = response.getJSONObject(2);
                            JSONObject amountObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray catArray = catObject.getJSONArray("data");
                            JSONArray descArray = descObject.getJSONArray("data");
                            JSONArray amountArray = amountObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("environment_and_natural_resources_quantity_of_total_mineral",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("quantity_id",i+1);
                                values.put("category",catArray.getString(i));
                                values.put("description",descArray.getString(i));
                                values.put("amount",amountArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("environment_and_natural_resources_quantity_of_total_mineral",null,values);
                            }

                            db.close();
                            Log.d(TAG, "environment_and_natural_resources_quantity_of_total_mineral: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_environment_and_natural_resources_value_of_total_minerals(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Value of Total Minerals"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject catObject  = response.getJSONObject(1);
                            JSONObject descObject  = response.getJSONObject(2);
                            JSONObject amountObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray catArray = catObject.getJSONArray("data");
                            JSONArray descArray = descObject.getJSONArray("data");
                            JSONArray amountArray = amountObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("environment_and_natural_resources_value_of_total_mineral",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("value_id",i+1);
                                values.put("category",catArray.getString(i));
                                values.put("description",descArray.getString(i));
                                values.put("amount",amountArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("environment_and_natural_resources_value_of_total_mineral",null,values);
                            }

                            db.close();
                            Log.d(TAG, "environment_and_natural_resources_value_of_total_mineral: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_environment_and_natural_resources_water_purification_points(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Water Purification Points"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject pointsObject  = response.getJSONObject(1);
                            JSONObject holesObject  = response.getJSONObject(2);
                            JSONObject publicObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray pointsArray = pointsObject.getJSONArray("data");
                            JSONArray holesArray = holesObject.getJSONArray("data");
                            JSONArray publicArray = publicObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("environment_and_natural_resources_water_purification_points",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("water_id",i+1);
                                values.put("water_purification_points",pointsArray.getDouble(i));
                                values.put("boreholes_total",holesArray.getDouble(i));
                                values.put("public",publicArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("environment_and_natural_resources_water_purification_points",null,values);
                            }

                            db.close();
                            Log.d(TAG, "environment_and_natural_resources_water_purification_points: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_environment_and_natural_resources_record_sale_of_government_products(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Record Sale of Government Products"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject woodObject  = response.getJSONObject(1);
                            JSONObject charObject  = response.getJSONObject(2);
                            JSONObject powerObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray woodArray = woodObject.getJSONArray("data");
                            JSONArray charArray = charObject.getJSONArray("data");
                            JSONArray powerArray = powerObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("environment_and_natural_resources_record_sale_goverment_products",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("record_id",i+1);
                                values.put("soft_wood",woodArray.getDouble(i));
                                values.put("fuelwood_charcoal",charArray.getDouble(i));
                                values.put("power_and_telegraph",powerArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("environment_and_natural_resources_record_sale_goverment_products",null,values);
                            }

                            db.close();
                            Log.d(TAG, "environment_and_natural_resources_record_sale_goverment_products: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

}