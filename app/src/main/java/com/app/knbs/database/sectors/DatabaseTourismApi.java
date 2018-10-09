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
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.services.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 28/02/2018.
 */

public class DatabaseTourismApi {
    private Context context;
    public DatabaseTourismApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);

    public void loadData(final ProgressDialog d){
        insertInto_tourism_tourist_arrivals(d);
        insertInto_tourism_conferences(d);
        insertInto_tourism_departures(d);
        insertInto_tourism_earnings(d);
        insertInto_tourism_hotel_occupancy_by_residence(d);
        insertInto_tourism_hotel_occupancy_by_zone(d);
        insertInto_tourism_visitors_to_parks(d);
        insertInto_tourism_visitors_to_museums(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_tourism_tourist_arrivals(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/tourism/tourism_arrivals",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject quarterObject  = response.getJSONObject(1);
                            JSONObject holidayObject  = response.getJSONObject(2);
                            JSONObject businessObject  = response.getJSONObject(3);
                            JSONObject transitObject  = response.getJSONObject(4);
                            JSONObject otherObject  = response.getJSONObject(5);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray quarterArray = quarterObject.getJSONArray("data");
                            JSONArray holidayArray = holidayObject.getJSONArray("data");
                            JSONArray businessArray = businessObject.getJSONArray("data");
                            JSONArray transitArray = transitObject.getJSONArray("data");
                            JSONArray otherArray = otherObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("tourism_arrivals",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("arrivals_id",i+1);
                                values.put("quarter",quarterArray.getString(i));
                                values.put("holiday",holidayArray.getDouble(i));
                                values.put("business",businessArray.getDouble(i));
                                values.put("transit",transitArray.getDouble(i));
                                values.put("other",otherArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("tourism_arrivals",null,values);
                            }

                            db.close();
                            Log.d(TAG, "tourism_arrivals: " + success);

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

    private void insertInto_tourism_conferences(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/tourism/all_tourism_conferences",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject categoryObject  = response.getJSONObject(1);
                            JSONObject conferencesObject  = response.getJSONObject(2);
                            JSONObject delegatesObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray categoryArray = categoryObject.getJSONArray("data");
                            JSONArray conferencesArray = conferencesObject.getJSONArray("data");
                            JSONArray delegatesArray = delegatesObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("tourism_conferences",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("conference_id",i+1);
                                values.put("category",categoryArray.getString(i));
                                values.put("number_of_conferences",conferencesArray.getDouble(i));
                                values.put("number_of_delegates",delegatesArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("tourism_conferences",null,values);
                            }

                            db.close();
                            Log.d(TAG, "tourism_conferences: " + success);

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

    private void insertInto_tourism_departures(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/tourism/all_tourism_departures",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject quarterObject  = response.getJSONObject(1);
                            JSONObject holidayObject  = response.getJSONObject(2);
                            JSONObject businessObject  = response.getJSONObject(3);
                            JSONObject transitObject  = response.getJSONObject(4);
                            JSONObject otherObject  = response.getJSONObject(5);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray quarterArray = quarterObject.getJSONArray("data");
                            JSONArray holidayArray = holidayObject.getJSONArray("data");
                            JSONArray businessArray = businessObject.getJSONArray("data");
                            JSONArray transitArray = transitObject.getJSONArray("data");
                            JSONArray otherArray = otherObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("tourism_departures",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("departures_id",i+1);
                                values.put("quarter",quarterArray.getString(i));
                                values.put("holiday",holidayArray.getDouble(i));
                                values.put("business",businessArray.getDouble(i));
                                values.put("transit",transitArray.getDouble(i));
                                values.put("other",otherArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("tourism_departures",null,values);
                            }

                            db.close();
                            Log.d(TAG, "tourism_departures: " + success);

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

    private void insertInto_tourism_earnings(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/tourism/all_tourism_earnings",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject earningsObject  = response.getJSONObject(1);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray earningsArray = earningsObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("tourism_earnings",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("earnings_id",i+1);
                                values.put("tourism_earnings_billions",earningsArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("tourism_earnings",null,values);
                            }

                            db.close();
                            Log.d(TAG, "tourism_earnings: " + success);

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

    private void insertInto_tourism_hotel_occupancy_by_residence(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/tourism/all_tourism_hotel_occupancy_by_residence",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);

                            JSONObject poObject  = response.getJSONObject(1);
                            JSONObject germanyObject  = response.getJSONObject(2);
                            JSONObject switzObject  = response.getJSONObject(3);
                            JSONObject ukObject  = response.getJSONObject(4);
                            JSONObject italyObject  = response.getJSONObject(5);

                            JSONObject franceObject  = response.getJSONObject(6);
                            JSONObject scandObject  = response.getJSONObject(7);
                            JSONObject opeObject  = response.getJSONObject(8);
                            JSONObject europeObject  = response.getJSONObject(9);
                            JSONObject krObject  = response.getJSONObject(10);

                            JSONObject ugObject  = response.getJSONObject(11);
                            JSONObject tzObject  = response.getJSONObject(12);
                            JSONObject eacaObject  = response.getJSONObject(13);
                            JSONObject waObject  = response.getJSONObject(14);
                            JSONObject naObject  = response.getJSONObject(15);

                            JSONObject saObject  = response.getJSONObject(16);
                            JSONObject opaObject  = response.getJSONObject(17);
                            JSONObject africaObject  = response.getJSONObject(18);
                            JSONObject usaObject  = response.getJSONObject(19);
                            JSONObject canadaObject  = response.getJSONObject(20);

                            JSONObject opusaObject  = response.getJSONObject(21);
                            JSONObject americaObject  = response.getJSONObject(22);
                            JSONObject japanObject  = response.getJSONObject(23);
                            JSONObject indiaObject  = response.getJSONObject(24);
                            JSONObject meObject  = response.getJSONObject(25);

                            JSONObject opasiaObject  = response.getJSONObject(26);
                            JSONObject asiaObject  = response.getJSONObject(27);
                            JSONObject ausObject  = response.getJSONObject(28);
                            JSONObject allotherObject  = response.getJSONObject(29);
                            JSONObject totalOccObject  = response.getJSONObject(30);

                            JSONObject totalAvObject  = response.getJSONObject(31);
                            JSONObject occpercentObject  = response.getJSONObject(32);


                            JSONArray yearArray = yearObject.getJSONArray("data");

                            JSONArray poArray = poObject.getJSONArray("data");
                            JSONArray gerArray = germanyObject.getJSONArray("data");
                            JSONArray switzArray = switzObject.getJSONArray("data");
                            JSONArray ukArray = ukObject.getJSONArray("data");
                            JSONArray itArray = italyObject.getJSONArray("data");

                            JSONArray frArray = franceObject.getJSONArray("data");
                            JSONArray scArray = scandObject.getJSONArray("data");
                            JSONArray opeArray = opeObject.getJSONArray("data");
                            JSONArray eurArray = europeObject.getJSONArray("data");
                            JSONArray krArray = krObject.getJSONArray("data");

                            JSONArray ugArray = ugObject.getJSONArray("data");
                            JSONArray tzArray = tzObject.getJSONArray("data");
                            JSONArray eacaArray = eacaObject.getJSONArray("data");
                            JSONArray waArray = waObject.getJSONArray("data");
                            JSONArray naArray = naObject.getJSONArray("data");

                            JSONArray saArray = saObject.getJSONArray("data");
                            JSONArray opaArray = opaObject.getJSONArray("data");
                            JSONArray afArray = africaObject.getJSONArray("data");
                            JSONArray usaArray = usaObject.getJSONArray("data");
                            JSONArray canArray = canadaObject.getJSONArray("data");

                            JSONArray opusaArray = opusaObject.getJSONArray("data");
                            JSONArray amArray = americaObject.getJSONArray("data");
                            JSONArray japArray = japanObject.getJSONArray("data");
                            JSONArray indArray = indiaObject.getJSONArray("data");
                            JSONArray meArray = meObject.getJSONArray("data");

                            JSONArray opasiaArray = opasiaObject.getJSONArray("data");
                            JSONArray asiaArray = asiaObject.getJSONArray("data");
                            JSONArray ausArray = ausObject.getJSONArray("data");
                            JSONArray allotherArray = allotherObject.getJSONArray("data");
                            JSONArray totoccArray = totalOccObject.getJSONArray("data");

                            JSONArray totavArray = totalAvObject.getJSONArray("data");
                            JSONArray occpercentArray = occpercentObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("tourism_hotel_occupancy_by_residence",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("hotel_occupancy_id",i+1);

                                values.put("year",yearArray.getInt(i));
                                values.put("permanent_occupants",Float.parseFloat(String.valueOf(poArray.get(i))));
                                values.put("germany",Float.parseFloat(String.valueOf(gerArray.get(i))));
                                values.put("switzerland",Float.parseFloat(String.valueOf(switzArray.get(i))));
                                values.put("united_kingdom",Float.parseFloat(String.valueOf(ukArray.get(i))));

                                values.put("italy",Float.parseFloat(String.valueOf(itArray.get(i))));
                                values.put("france",Float.parseFloat(String.valueOf(frArray.get(i))));
                                values.put("scandinavia",Float.parseFloat(String.valueOf(scArray.get(i))));
                                values.put("other_europe",Float.parseFloat(String.valueOf(opeArray.get(i))));
                                values.put("europe",Float.parseFloat(String.valueOf(eurArray.get(i))));

                                values.put("kenya_residents",Float.parseFloat(String.valueOf(krArray.get(i))));
                                values.put("uganda",Float.parseFloat(String.valueOf(ugArray.get(i))));
                                values.put("tanzania",Float.parseFloat(String.valueOf(tzArray.get(i))));
                                values.put("east_and_central_africa",Float.parseFloat(String.valueOf(eacaArray.get(i))));
                                values.put("west_africa",Float.parseFloat(String.valueOf(waArray.get(i))));

                                values.put("north_africa",Float.parseFloat(String.valueOf(naArray.get(i))));
                                values.put("south_africa",Float.parseFloat(String.valueOf(saArray.get(i))));
                                values.put("other_africa",Float.parseFloat(String.valueOf(opaArray.get(i))));
                                values.put("africa",Float.parseFloat(String.valueOf(afArray.get(i))));
                                values.put("usa",Float.parseFloat(String.valueOf(usaArray.get(i))));

                                values.put("canada",Float.parseFloat(String.valueOf(canArray.get(i))));
                                values.put("other_america",Float.parseFloat(String.valueOf(opusaArray.get(i))));
                                values.put("america",Float.parseFloat(String.valueOf(amArray.get(i))));
                                values.put("japan",Float.parseFloat(String.valueOf(japArray.get(i))));
                                values.put("india",Float.parseFloat(String.valueOf(indArray.get(i))));

                                values.put("middle_east",Float.parseFloat(String.valueOf(meArray.get(i))));
                                values.put("other_asia",Float.parseFloat(String.valueOf(opasiaArray.get(i))));
                                values.put("asia",Float.parseFloat(String.valueOf(asiaArray.get(i))));
                                values.put("australia_and_new_zealand",Float.parseFloat(String.valueOf(ausArray.get(i))));
                                values.put("all_other_countries",Float.parseFloat(String.valueOf(allotherArray.get(i))));

                                values.put("total_occupied",Float.parseFloat(String.valueOf(totoccArray.get(i))));
                                values.put("total_available",Float.parseFloat(String.valueOf(totavArray.get(i))));
                                values.put("occupancy_percentage_rate",Float.parseFloat(String.valueOf(occpercentArray.get(i))));

                                success = db.insertOrThrow("tourism_hotel_occupancy_by_residence",null,values);
                            }

                            db.close();
                            Log.d(TAG, "tourism_hotel_occupancy_by_residence: " + success);

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

    private void insertInto_tourism_hotel_occupancy_by_zone(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/tourism/all_tourism_hotel_occupancy_by_zone",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);

                            JSONObject cbObject  = response.getJSONObject(1);
                            JSONObject ocaObject  = response.getJSONObject(2);
                            JSONObject chObject  = response.getJSONObject(3);
                            JSONObject nhcObject  = response.getJSONObject(4);
                            JSONObject noaObject  = response.getJSONObject(5);

                            JSONObject cenObject  = response.getJSONObject(6);
                            JSONObject maaObject  = response.getJSONObject(7);
                            JSONObject nbObject  = response.getJSONObject(8);
                            JSONObject wesObject  = response.getJSONObject(9);
                            JSONObject norObject  = response.getJSONObject(10);

                            JSONObject toObject  = response.getJSONObject(11);
                            JSONObject taObject  = response.getJSONObject(12);


                            JSONArray yearArray = yearObject.getJSONArray("data");

                            JSONArray cbArray = cbObject.getJSONArray("data");
                            JSONArray ocaArray = ocaObject.getJSONArray("data");
                            JSONArray chArray = chObject.getJSONArray("data");
                            JSONArray nhcArray = nhcObject.getJSONArray("data");
                            JSONArray noaArray = noaObject.getJSONArray("data");

                            JSONArray cenArray = cenObject.getJSONArray("data");
                            JSONArray maaArray = maaObject.getJSONArray("data");
                            JSONArray nbArray = nbObject.getJSONArray("data");
                            JSONArray wesArray = wesObject.getJSONArray("data");
                            JSONArray norArray = norObject.getJSONArray("data");

                            JSONArray toArray = toObject.getJSONArray("data");
                            JSONArray taArray = taObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("tourism_hotel_occupancy_by_zone",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("id",i+1);
                                values.put("year",yearArray.getInt(i));

                                values.put("coastal_beach",Float.parseFloat(String.valueOf(cbArray.get(i))));
                                values.put("coastal_other",Float.parseFloat(String.valueOf(ocaArray.get(i))));
                                values.put("coastal_hinterland",Float.parseFloat(String.valueOf(chArray.get(i))));
                                values.put("nairobi_high_class",Float.parseFloat(String.valueOf(nhcArray.get(i))));
                                values.put("nairobi_other",Float.parseFloat(String.valueOf(noaArray.get(i))));

                                values.put("central",Float.parseFloat(String.valueOf(cenArray.get(i))));
                                values.put("masailand",Float.parseFloat(String.valueOf(maaArray.get(i))));
                                values.put("nyanza_basin",Float.parseFloat(String.valueOf(nbArray.get(i))));
                                values.put("western",Float.parseFloat(String.valueOf(wesArray.get(i))));
                                values.put("northern",Float.parseFloat(String.valueOf(norArray.get(i))));

                                values.put("total_occupied",Float.parseFloat(String.valueOf(toArray.get(i))));
                                values.put("total_available",Float.parseFloat(String.valueOf(taArray.get(i))));

                                success = db.insertOrThrow("tourism_hotel_occupancy_by_zone",null,values);
                            }

                            db.close();
                            Log.d(TAG, "tourism_hotel_occupancy_by_zone: " + success);

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

    //Incomplete
    private void insertInto_tourism_visitors_to_parks(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/tourism/all_tourism_visitor_to_parks",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);

                            JSONObject nairobiObject  = response.getJSONObject(1);
                            JSONObject nswObject  = response.getJSONObject(2);
                            JSONObject amboseliObject  = response.getJSONObject(4);

                            JSONArray yearArray = yearObject.getJSONArray("data");

                            JSONArray nairobiArray = nairobiObject.getJSONArray("data");
                            JSONArray nswArray = nswObject.getJSONArray("data");
                            JSONArray amboseliArray = amboseliObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("tourism_hotel_occupancy_by_zone",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("visitor_parks_id",i+1);
                                values.put("year",yearArray.getInt(i));

                                values.put("nairobi",Float.parseFloat(String.valueOf(nairobiArray.get(i))));
                                values.put("nairobi_safari_walk",Float.parseFloat(String.valueOf(nswArray.get(i))));
                                values.put("nairobi_mini_orphanage",Float.parseFloat(String.valueOf(nswArray.get(i))));
                                values.put("amboseli",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("tsavo_west",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("tsavo_east",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("aberdare",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("lake_nakuru",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("masai_mara",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("hallers_park",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("malindi_marine",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("lake_bogoria",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("meru",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("shimba_hills",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("mt_kenya",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("samburu",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("kisite_mpunguti",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("mombasa_marine",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("watamu_marine",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("hells_gate",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("impala_sanctuary_kisumu",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("mt_longonot",Float.parseFloat(String.valueOf(amboseliArray.get(i))));
                                values.put("other",Float.parseFloat(String.valueOf(amboseliArray.get(i))));

                                success = db.insertOrThrow("tourism_visitor_to_parks",null,values);
                            }

                            db.close();
                            Log.d(TAG, "tourism_visitor_to_parks: " + success);

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

    //Incomplete
    private void insertInto_tourism_visitors_to_museums(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/tourism/all_tourism_visitors_to_museums",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);

                            JSONObject nspObject  = response.getJSONObject(1);
                            JSONObject fjObject  = response.getJSONObject(2);
                            JSONObject kbObject  = response.getJSONObject(11);

                            JSONArray yearArray = yearObject.getJSONArray("data");

                            JSONArray nspArray = nspObject.getJSONArray("data");
                            JSONArray fjArray = fjObject.getJSONArray("data");
                            JSONArray kbArray = kbObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("tourism_visitors_to_museums",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("visitor_museums_id",i+1);
                                values.put("year",yearArray.getInt(i));

                                values.put("nairobi_snake_park",Float.parseFloat(String.valueOf(nspArray.get(i))));
                                values.put("fort_jesus",Float.parseFloat(String.valueOf(fjArray.get(i))));
                                values.put("kisumu",Float.parseFloat(String.valueOf(fjArray.get(i))));
                                values.put("kitale",Float.parseFloat(String.valueOf(fjArray.get(i))));
                                values.put("gede",Float.parseFloat(String.valueOf(fjArray.get(i))));
                                values.put("meru",Float.parseFloat(String.valueOf(fjArray.get(i))));
                                values.put("lamu",Float.parseFloat(String.valueOf(fjArray.get(i))));
                                values.put("jumba_la_mtwana",Float.parseFloat(String.valueOf(fjArray.get(i))));
                                values.put("kariandusi",Float.parseFloat(String.valueOf(fjArray.get(i))));
                                values.put("hyrax_hill",Float.parseFloat(String.valueOf(fjArray.get(i))));
                                values.put("karen_blixen",Float.parseFloat(String.valueOf(kbArray.get(i))));
                                values.put("malindi",Float.parseFloat(String.valueOf(kbArray.get(i))));
                                values.put("kilifi_mnarani",Float.parseFloat(String.valueOf(kbArray.get(i))));
                                values.put("kabarnet",Float.parseFloat(String.valueOf(kbArray.get(i))));
                                values.put("kapenguria",Float.parseFloat(String.valueOf(kbArray.get(i))));
                                values.put("swahili_house",Float.parseFloat(String.valueOf(kbArray.get(i))));
                                values.put("narok",Float.parseFloat(String.valueOf(kbArray.get(i))));
                                values.put("german_post",Float.parseFloat(String.valueOf(kbArray.get(i))));
                                values.put("takwa_ruins",Float.parseFloat(String.valueOf(kbArray.get(i))));

                                success = db.insertOrThrow("tourism_visitors_to_museums",null,values);
                            }

                            db.close();
                            Log.d(TAG, "tourism_visitors_to_museums: " + success);

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