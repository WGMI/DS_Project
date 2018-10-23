
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

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 28/02/2018.
 */

public class DatabaseICTApi {
    private Context context;
    public DatabaseICTApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);
    private ReportLoader loader = new ReportLoader(context);
    private CountyHelper countyHelper = new CountyHelper();

    public void loadData(final ProgressDialog d){
        insertInto_ict_kihibs_households_owned_ict_equipment_services(d);
        insertInto_ict_kihibs_households_without_internet_by_reason(d);
        insertInto_ict_kihibs_households_with_tv(d);
        insertInto_ict_kihibs_households_with_internet_by_type(d);
        insertInto_ict_kihibs_population_above18by_reasonnothaving_phone(d);

        insertInto_ict_kihibs_population_above18subscribed_mobilemoney(d);
        insertInto_ict_kihibs_population_by_ictequipment_and_servicesused(d);
        insertInto_ict_kihibs_population_that_didntuseinternet_by_reason(d);
        insertInto_ict_kihibs_population_that_used_internet_by_purpose(d);
        insertInto_ict_kihibs_population_who_used_internet_by_place(d);

        insertInto_ict_kihibs_population_withmobilephone_andaveragesims(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_ict_kihibs_households_owned_ict_equipment_services(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("households owned ict equipment services"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject compObject = array.getJSONObject(1);
                            JSONObject tvObject = array.getJSONObject(2);
                            JSONObject hhObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray compArray = compObject.getJSONArray("data");
                            JSONArray tvArray = tvObject.getJSONArray("data");
                            JSONArray hhArray = hhObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("ict_kihibs_households_owned_ict_equipment_services",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("household_id",i+1);
                                values.put("computer",compArray.getDouble(i));
                                values.put("television",tvArray.getDouble(i));
                                values.put("households",hhArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("ict_kihibs_households_owned_ict_equipment_services",null,values);
                            }

                            db.close();
                            Log.d(TAG, "ict_kihibs_households_owned_ict_equipment_services: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_ict_kihibs_households_without_internet_by_reason(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("households without internet by reason"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject dnObject = array.getJSONObject(1);
                            JSONObject lsObject = array.getJSONObject(2);
                            JSONObject nnObject = array.getJSONObject(3);
                            JSONObject aewObject = array.getJSONObject(4);
                            JSONObject schObject = array.getJSONObject(6);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray dnArray = dnObject.getJSONArray("data");
                            JSONArray lsArray = lsObject.getJSONArray("data");
                            JSONArray nnArray = nnObject.getJSONArray("data");
                            JSONArray aewArray = aewObject.getJSONArray("data");
                            JSONArray schArray = schObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("ict_kihibs_households_without_internet_by_reason",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("distribution_id",i+1);
                                values.put("dont_need",dnArray.getDouble(i));
                                values.put("lack_skills",lsArray.getDouble(i));
                                values.put("no_network",nnArray.getDouble(i));
                                values.put("access_elsewhere",aewArray.getDouble(i));
                                values.put("service_cost_high",schArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("ict_kihibs_households_without_internet_by_reason",null,values);
                            }

                            db.close();
                            Log.d(TAG, "ict_kihibs_households_without_internet_by_reason: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_ict_kihibs_households_with_tv(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("households with tv"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject dtObject = array.getJSONObject(1);
                            JSONObject ptObject = array.getJSONObject(2);
                            JSONObject frObject = array.getJSONObject(3);
                            JSONObject ipObject = array.getJSONObject(4);
                            JSONObject noneObject = array.getJSONObject(5);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray dtArray = dtObject.getJSONArray("data");
                            JSONArray ptArray = ptObject.getJSONArray("data");
                            JSONArray frArray = frObject.getJSONArray("data");
                            JSONArray ipArray = ipObject.getJSONArray("data");
                            JSONArray noneArray = noneObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("ict_kihibs_households_with_tv",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("household_id",i+1);
                                values.put("digital_tv",dtArray.getDouble(i));
                                values.put("pay_tv",ptArray.getDouble(i));
                                values.put("free_to_air",frArray.getDouble(i));
                                values.put("ip_tv",ipArray.getDouble(i));
                                values.put("none",noneArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("ict_kihibs_households_with_tv",null,values);
                            }

                            db.close();
                            Log.d(TAG, "ict_kihibs_households_with_tv: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_ict_kihibs_households_with_internet_by_type(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("households with internet by type"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject fwObject = array.getJSONObject(1);
                            JSONObject fwlObject = array.getJSONObject(2);
                            JSONObject mbObject = array.getJSONObject(3);
                            JSONObject moObject = array.getJSONObject(4);
                            JSONObject othObject = array.getJSONObject(5);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray fwArray = fwObject.getJSONArray("data");
                            JSONArray fwlArray = fwlObject.getJSONArray("data");
                            JSONArray mbArray = mbObject.getJSONArray("data");
                            JSONArray moArray = moObject.getJSONArray("data");
                            JSONArray othArray = othObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("ict_kihibs_households_with_internet_by_type",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("distribution_id",i+1);
                                values.put("fixed_wired",fwArray.getDouble(i));
                                values.put("fixed_wireless",fwlArray.getDouble(i));
                                values.put("mobile_broadband",mbArray.getDouble(i));
                                values.put("mobile",moArray.getDouble(i));
                                values.put("other",othArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("ict_kihibs_households_with_internet_by_type",null,values);
                            }

                            db.close();
                            Log.d(TAG, "ict_kihibs_households_with_internet_by_type: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_ict_kihibs_population_above18by_reasonnothaving_phone(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("population above 18 by reason not having phone"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject tyObject = array.getJSONObject(2);
                            JSONObject dnObject = array.getJSONObject(3);
                            JSONObject reObject = array.getJSONObject(4);
                            JSONObject nnObject = array.getJSONObject(5);
                            JSONObject neObject = array.getJSONObject(7);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray tyArray = tyObject.getJSONArray("data");
                            JSONArray dnArray = dnObject.getJSONArray("data");
                            JSONArray reArray = reObject.getJSONArray("data");
                            JSONArray nnArray = nnObject.getJSONArray("data");
                            JSONArray neArray = neObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("ict_kihibs_population_above18by_reasonnothaving_phone",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("population_id",i+1);
                                values.put("too_young",tyArray.getDouble(i));
                                values.put("dont_need",dnArray.getDouble(i));
                                values.put("restricted",reArray.getDouble(i));
                                values.put("no_network",nnArray.getDouble(i));
                                values.put("no_electricity",neArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("ict_kihibs_population_above18by_reasonnothaving_phone",null,values);
                            }

                            db.close();
                            Log.d(TAG, "ict_kihibs_population_above18by_reasonnothaving_phone: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_ict_kihibs_population_above18subscribed_mobilemoney(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("population above 18 subscribed mobile money"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject mmObject = array.getJSONObject(1);
                            JSONObject mbObject = array.getJSONObject(2);
                            JSONObject poObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray mmArray = mmObject.getJSONArray("data");
                            JSONArray mbArray = mbObject.getJSONArray("data");
                            JSONArray poArray = poObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("ict_kihibs_population_above18subscribed_mobilemoney",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("proportion_id",i+1);
                                values.put("mobile_money",mmArray.getDouble(i));
                                values.put("mobile_banking",mbArray.getDouble(i));
                                values.put("population",poArray.getInt(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("ict_kihibs_population_above18subscribed_mobilemoney",null,values);
                            }

                            db.close();
                            Log.d(TAG, "ict_kihibs_population_above18subscribed_mobilemoney: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_ict_kihibs_population_by_ictequipment_and_servicesused(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("population by ictequipment and servicesused"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject tvObject = array.getJSONObject(1);
                            JSONObject rdObject = array.getJSONObject(2);
                            JSONObject moObject = array.getJSONObject(3);
                            JSONObject coObject = array.getJSONObject(4);
                            JSONObject inObject = array.getJSONObject(5);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray tvArray = tvObject.getJSONArray("data");
                            JSONArray rdArray = rdObject.getJSONArray("data");
                            JSONArray moArray = moObject.getJSONArray("data");
                            JSONArray coArray = coObject.getJSONArray("data");
                            JSONArray inArray = inObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("ict_kihibs_population_by_ictequipment_and_servicesused",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("proportion_id",i+1);
                                values.put("television",tvArray.getDouble(i));
                                values.put("radio",rdArray.getDouble(i));
                                values.put("mobile",moArray.getInt(i));
                                values.put("computer",coArray.getInt(i));
                                values.put("internet",inArray.getInt(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("ict_kihibs_population_by_ictequipment_and_servicesused",null,values);
                            }

                            db.close();
                            Log.d(TAG, "ict_kihibs_population_by_ictequipment_and_servicesused: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_ict_kihibs_population_that_didntuseinternet_by_reason(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("population that didnt use internet byreason"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject tyObject = array.getJSONObject(1);
                            JSONObject dnObject = array.getJSONObject(2);
                            JSONObject lsObject = array.getJSONObject(3);
                            JSONObject exObject = array.getJSONObject(4);
                            JSONObject niObject = array.getJSONObject(5);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray tyArray = tyObject.getJSONArray("data");
                            JSONArray dnArray = dnObject.getJSONArray("data");
                            JSONArray lsArray = lsObject.getJSONArray("data");
                            JSONArray exArray = exObject.getJSONArray("data");
                            JSONArray niArray = niObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("ict_kihibs_population_that_didntuseinternet_by_reason",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("proportion_id",i+1);
                                values.put("too_young",tyArray.getDouble(i));
                                values.put("dont_need",dnArray.getDouble(i));
                                values.put("lack_skill",lsArray.getInt(i));
                                values.put("expensive",exArray.getInt(i));
                                values.put("no_internet",niArray.getInt(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("ict_kihibs_population_that_didntuseinternet_by_reason",null,values);
                            }

                            db.close();
                            Log.d(TAG, "ict_kihibs_population_that_didntuseinternet_by_reason: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_ict_kihibs_population_that_used_internet_by_purpose(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("population that used internet by purpose"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject nsObject = array.getJSONObject(4);
                            JSONObject bnObject = array.getJSONObject(5);
                            JSONObject rsObject = array.getJSONObject(10);
                            JSONObject inObject = array.getJSONObject(11);
                            JSONObject snObject = array.getJSONObject(13);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray nsArray = nsObject.getJSONArray("data");
                            JSONArray bnArray = bnObject.getJSONArray("data");
                            JSONArray rsArray = rsObject.getJSONArray("data");
                            JSONArray inArray = inObject.getJSONArray("data");
                            JSONArray snArray = snObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("ict_kihibs_population_that_used_internet_by_purpose",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("distribution_id",i+1);
                                values.put("newspaper",nsArray.getDouble(i));
                                values.put("banking",bnArray.getDouble(i));
                                values.put("research",rsArray.getInt(i));
                                values.put("informative",inArray.getInt(i));
                                values.put("social_nets",snArray.getInt(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("ict_kihibs_population_that_used_internet_by_purpose",null,values);
                            }

                            db.close();
                            Log.d(TAG, "ict_kihibs_population_that_used_internet_by_purpose: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_ict_kihibs_population_who_used_internet_by_place(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("population who used internet by place"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject wrObject = array.getJSONObject(2);
                            JSONObject cyObject = array.getJSONObject(3);
                            JSONObject edObject = array.getJSONObject(4);
                            JSONObject ahObject = array.getJSONObject(6);
                            JSONObject hoObject = array.getJSONObject(7);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray wrArray = wrObject.getJSONArray("data");
                            JSONArray cyArray = cyObject.getJSONArray("data");
                            JSONArray edArray = edObject.getJSONArray("data");
                            JSONArray ahArray = ahObject.getJSONArray("data");
                            JSONArray hoArray = hoObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("ict_kihibs_population_who_used_internet_by_place",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("proportion_id",i+1);
                                values.put("work",wrArray.getDouble(i));
                                values.put("cyber",cyArray.getDouble(i));
                                values.put("ed_centre",edArray.getInt(i));
                                values.put("another_home",ahArray.getInt(i));
                                values.put("at_home",hoArray.getInt(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("ict_kihibs_population_who_used_internet_by_place",null,values);
                            }

                            db.close();
                            Log.d(TAG, "ict_kihibs_population_who_used_internet_by_place: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_ict_kihibs_population_withmobilephone_andaveragesims(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("population with mobile phone and averagesims"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject phObject = array.getJSONObject(1);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray phArray = phObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("ict_kihibs_population_withmobilephone_andaveragesims",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("proportion_id",i+1);
                                values.put("phone",phArray.getDouble(i));
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));

                                success = db.insertOrThrow("ict_kihibs_population_withmobilephone_andaveragesims",null,values);
                            }

                            db.close();
                            Log.d(TAG, "ict_kihibs_population_withmobilephone_andaveragesims: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

}