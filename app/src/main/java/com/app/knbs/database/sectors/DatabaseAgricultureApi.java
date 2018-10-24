package com.app.knbs.database.sectors;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

public class DatabaseAgricultureApi {
    private Context context;
    public DatabaseAgricultureApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);
    private ReportLoader loader = new ReportLoader(context);
    CountyHelper countyHelper = new CountyHelper();

    public void loadData(final ProgressDialog d){
        insertInto_agriculture_land_potential_by_area(d);
        insertInto_agriculture_chemical_med_feed_input(d);
        insertInto_agriculture_cooperatives(d);
        insertInto_agriculture_valueofagriculturalinputs(d);
        insertInto_agriculture_gross_market_production(d);

        insertInto_agriculture_irrigation_schemes(d);
        insertInto_agriculture_pricetoproducersformeatmilk(d);
        //insertInto_agriculture_totalsharecapital(d);
        insertInto_agriculture_area_under_sugarcane_harvested_production_avg_yield(d);
        insertInto_agriculture_production_area_average_yield_tea_type_grower(d);

        insertInto_agriculture_production_area_average_yield_coffee_type_of_grower(d);
        insertInto_categories_of_land(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_agriculture_land_potential_by_area(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Land Potential by Area"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject countyObject  = response.getJSONObject(0);
                            JSONObject subCountyObject  = response.getJSONObject(1);
                            JSONObject valObject  = response.getJSONObject(2);
                            JSONObject lpObject  = response.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray subCountyArray = subCountyObject.getJSONArray("data");
                            JSONArray valArray = valObject.getJSONArray("data");
                            JSONArray lpArray = lpObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("agriculture_land_potential",null,null);

                            for(int i=0;i<countyArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("land_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("subcounty_id",countyHelper.getSubCountyId(subCountyArray.getString(i)));
                                values.put("potential_id",lpArray.getString(i));
                                values.put("value",valArray.getDouble(i));
                                values.put("year","2014");

                                success = db.insertOrThrow("agriculture_land_potential",null,values);
                            }

                            db.close();
                            Log.d(TAG, "agriculture_land_potential: " + success);

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

    private void insertInto_agriculture_chemical_med_feed_input(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Chemical Medicinal and Farm Inputs"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject cfObject  = response.getJSONObject(1);
                            JSONObject inObject  = response.getJSONObject(5);
                            JSONObject oldObject  = response.getJSONObject(7);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray cfArray = cfObject.getJSONArray("data");
                            JSONArray inArray = inObject.getJSONArray("data");
                            JSONArray oldArray = oldObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("agriculture_chemical_med_feed_input",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("chemical_med_feed_inputs_id",i+1);
                                values.put("cattle_feed",cfArray.getInt(i));
                                values.put("insecticides",inArray.getInt(i));
                                values.put("other_livestock_drugs",oldArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("agriculture_chemical_med_feed_input",null,values);
                            }

                            db.close();
                            Log.d(TAG, "agriculture_chemical_med_feed_input: " + success);

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

    private void insertInto_agriculture_cooperatives(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Cooperatives"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject cfObject  = response.getJSONObject(1);
                            JSONObject saObject  = response.getJSONObject(10);
                            JSONObject unObject  = response.getJSONObject(15);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray cfArray = cfObject.getJSONArray("data");
                            JSONArray saArray = saObject.getJSONArray("data");
                            JSONArray unArray = unObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("agriculture_cooperatives",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("cooperatives_id",i+1);
                                values.put("coffee",cfArray.getInt(i));
                                values.put("saccos",saArray.getInt(i));
                                values.put("unions",unArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("agriculture_cooperatives",null,values);
                            }

                            db.close();
                            Log.d(TAG, "agriculture_cooperatives: " + success);

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

    private void insertInto_agriculture_valueofagriculturalinputs(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Value of Agricultural Inputs"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject frObject  = response.getJSONObject(6);
                            JSONObject fuObject  = response.getJSONObject(7);
                            JSONObject seObject  = response.getJSONObject(20);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray frArray = frObject.getJSONArray("data");
                            JSONArray fuArray = fuObject.getJSONArray("data");
                            JSONArray seArray = seObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("agriculture_valueofagriculturalinputs",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("value_of_agricultural_inputs_id",i+1);
                                values.put("fertilizers",frArray.getInt(i));
                                values.put("fuel",fuArray.getInt(i));
                                values.put("seeds",seArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("agriculture_valueofagriculturalinputs",null,values);
                            }

                            db.close();
                            Log.d(TAG, "agriculture_valueofagriculturalinputs: " + success);

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

    private void insertInto_agriculture_gross_market_production(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Gross Market Production"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject caObject  = response.getJSONObject(1);
                            JSONObject peObject  = response.getJSONObject(7);
                            JSONObject tcObject  = response.getJSONObject(25);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray caArray = caObject.getJSONArray("data");
                            JSONArray peArray = peObject.getJSONArray("data");
                            JSONArray tcArray = tcObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("agriculture_gross_market_production",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("gross_market_production_at_constant_id",i+1);
                                values.put("cattle_and_calves_for_slaughter",caArray.getInt(i));
                                values.put("poultry_and_eggs",peArray.getInt(i));
                                values.put("total_crops",tcArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("agriculture_gross_market_production",null,values);
                            }

                            db.close();
                            Log.d(TAG, "agriculture_gross_market_production: " + success);

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

    private void insertInto_agriculture_irrigation_schemes(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Irrigation Schemes"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject gvObject  = response.getJSONObject(6);
                            JSONObject ppObject  = response.getJSONObject(10);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray gvArray = gvObject.getJSONArray("data");
                            JSONArray ppArray = ppObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("agriculture_irrigation_schemes",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("irrigation_schemes_id",i+1);
                                values.put("all_schemes_gross_value_of_crop_millions",gvArray.getInt(i));
                                values.put("all_schemes_payments_to_plot_holders_millions",ppArray.getInt(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("agriculture_irrigation_schemes",null,values);
                            }

                            db.close();
                            Log.d(TAG, "agriculture_irrigation_schemes: " + success);

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

    private void insertInto_agriculture_pricetoproducersformeatmilk(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Price to producers for meat and milk"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject bfObject  = response.getJSONObject(1);
                            JSONObject piObject  = response.getJSONObject(2);
                            JSONObject whObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray bfArray = bfObject.getJSONArray("data");
                            JSONArray piArray = piObject.getJSONArray("data");
                            JSONArray whArray = whObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("agriculture_pricetoproducersformeatmilk",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("price_to_producers_for_meat_milk_id",i+1);
                                values.put("beef_third_grade_per_kg",bfArray.getInt(i));
                                values.put("pig_meat_per_kg",piArray.getInt(i));
                                values.put("whole_milk_per_litre",whArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("agriculture_pricetoproducersformeatmilk",null,values);
                            }

                            db.close();
                            Log.d(TAG, "agriculture_pricetoproducersformeatmilk: " + success);

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

    private void insertInto_agriculture_totalsharecapital(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Total Share capital"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject cfObject  = response.getJSONObject(1);
                            JSONObject saObject  = response.getJSONObject(10);
                            JSONObject unObject  = response.getJSONObject(15);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray cfArray = cfObject.getJSONArray("data");
                            JSONArray saArray = saObject.getJSONArray("data");
                            JSONArray unArray = unObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("agriculture_totalsharecapital",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("total_share_capital_id",i+1);
                                values.put("coffee",cfArray.getInt(i));
                                values.put("saccos",saArray.getInt(i));
                                values.put("unions",unArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("agriculture_totalsharecapital",null,values);
                            }

                            db.close();
                            Log.d(TAG, "agriculture_totalsharecapital: " + success);

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

    private void insertInto_agriculture_area_under_sugarcane_harvested_production_avg_yield(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Sugarcane Harvested, Production and Average Yield"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject unObject  = response.getJSONObject(1);
                            JSONObject haObject  = response.getJSONObject(2);
                            JSONObject prObject  = response.getJSONObject(3);
                            JSONObject yiObject  = response.getJSONObject(4);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray unArray = unObject.getJSONArray("data");
                            JSONArray haArray = haObject.getJSONArray("data");
                            JSONArray prArray = prObject.getJSONArray("data");
                            JSONArray yiArray = yiObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("agriculture_area_under_sugarcane_harvested_production_avg_yield",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("area_id",i+1);
                                values.put("area_under_cane_ha",unArray.getInt(i));
                                values.put("area_harvested_ha",haArray.getInt(i));
                                values.put("production_tonnes",prArray.getInt(i));
                                values.put("average_yield_tonnes_per_ha",yiArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("agriculture_area_under_sugarcane_harvested_production_avg_yield",null,values);
                            }

                            db.close();
                            Log.d(TAG, "agriculture_area_under_sugarcane_harvested_production_avg_yield: " + success);

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

    private void insertInto_agriculture_production_area_average_yield_tea_type_grower(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Production, Area and Average Yield Tea by type of Grower"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject caObject  = response.getJSONObject(1);
                            JSONObject shObject  = response.getJSONObject(2);
                            JSONObject esObject  = response.getJSONObject(3);
                            JSONObject unObject  = response.getJSONObject(4);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray caArray = caObject.getJSONArray("data");
                            JSONArray shArray = shObject.getJSONArray("data");
                            JSONArray esArray = esObject.getJSONArray("data");
                            JSONArray unArray = unObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("agriculture_production_area_average_yield_tea_type_grower",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("category_id",i+1);
                                values.put("category",caArray.getString(i));
                                values.put("smallholders",shArray.getDouble(i));
                                values.put("estates",esArray.getDouble(i));
                                values.put("unit",unArray.getString(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("agriculture_production_area_average_yield_tea_type_grower",null,values);
                            }

                            db.close();
                            Log.d(TAG, "agriculture_production_area_average_yield_tea_type_grower: " + success);

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

    private void insertInto_agriculture_production_area_average_yield_coffee_type_of_grower(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Production, Area and Average Yield by Coffee Type Grower"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject caObject  = response.getJSONObject(1);
                            JSONObject coObject  = response.getJSONObject(2);
                            JSONObject esObject  = response.getJSONObject(3);
                            JSONObject unObject  = response.getJSONObject(4);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray caArray = caObject.getJSONArray("data");
                            JSONArray coArray = coObject.getJSONArray("data");
                            JSONArray esArray = esObject.getJSONArray("data");
                            JSONArray unArray = unObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("agriculture_production_area_average_yield_coffee_type_of_grower",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("category_id",i+1);
                                values.put("category",caArray.getString(i));
                                values.put("cooperatives",coArray.getDouble(i));
                                values.put("estates",esArray.getDouble(i));
                                values.put("unit",unArray.getString(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("agriculture_production_area_average_yield_coffee_type_of_grower",null,values);
                            }

                            db.close();
                            Log.d(TAG, "agriculture_production_area_average_yield_coffee_type_of_grower: " + success);

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

    private void insertInto_categories_of_land(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Categories of Land"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject countyObject  = response.getJSONObject(0);
                            JSONObject hpObject  = response.getJSONObject(1);
                            JSONObject mpObject  = response.getJSONObject(2);
                            JSONObject lpObject  = response.getJSONObject(3);
                            JSONObject tlObject  = response.getJSONObject(4);
                            JSONObject alObject  = response.getJSONObject(5);
                            JSONObject tlaObject  = response.getJSONObject(6);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray hpArray = hpObject.getJSONArray("data");
                            JSONArray mpArray = mpObject.getJSONArray("data");
                            JSONArray lpArray = lpObject.getJSONArray("data");
                            JSONArray tlArray = tlObject.getJSONArray("data");
                            JSONArray alArray = alObject.getJSONArray("data");
                            JSONArray tlaArray = tlaObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("categories_of_land",null,null);

                            for(int i=0;i<countyArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("land_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("high_potential",hpArray.getInt(i));
                                values.put("medium_potential",mpArray.getInt(i));
                                values.put("low_potential",lpArray.getInt(i));
                                values.put("total_land",tlArray.getInt(i));
                                values.put("all_other_land",alArray.getInt(i));
                                values.put("total_land_area",tlaArray.getInt(i));

                                success = db.insertOrThrow("categories_of_land",null,values);
                            }

                            db.close();
                            Log.d(TAG, "categories_of_land: " + success);

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