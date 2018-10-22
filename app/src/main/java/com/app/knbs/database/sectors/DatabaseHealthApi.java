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

public class DatabaseHealthApi {
    private Context context;
    public DatabaseHealthApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);
    private ReportLoader loader = new ReportLoader(context);
    private CountyHelper countyHelper = new CountyHelper();

    public void loadData(final ProgressDialog d){
        /*insertInto_health_registeredmedicalpersonnel(d);
        insertInto_health_immunization_rate(d);
        insertInto_health_nhif_members(d);
        insertInto_health_percentage_incidence_of_diseases_in_kenya(d);
        insertInto_health_nhif_resources(d);

        insertInto_health_current_use_of_contraception_by_county(d);
        insertInto_health_distributionofoutpatientvisitsbytypeofhealthcareprovider(d);
        insertInto_health_hiv_aids_awareness_and_testing(d);
        insertInto_health_insurance_coverage_by_counties_and_types(d);
        insertInto_health_maternal_care(d);

        insertInto_health_nutritional_status_of_children(d);
        insertInto_health_nutritional_status_of_women(d);
        insertInto_health_registered_active_nhif_members_by_county(d);
        insertInto_health_registered_medical_laboratories_by_counties(d);
        insertInto_health_use_of_mosquito_nets_by_children(d);

        insertInto_health_fertility_by_education_status(d);
        insertInto_health_fertility_rate_by_age_and_residence(d);
        insertInto_health_early_childhood_mortality_rates_by_sex(d);
        insertInto_health_place_of_delivery(d);
        insertInto_health_prevalence_of_overweight_and_obesity(d);

        insertInto_health_percentage_adults_who_are_current_users(d);
        insertInto_health_percentage_who_drink_alcohol_by_age(d);
        insertInto_health_kihibs_children_by_additional_supplement(d);
        insertInto_health_kihibs_children_by_who_assisted_at_birth(d);
        insertInto_health_kihibs_children_immmunized_against_measles(d);*/

    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_health_percentage_incidence_of_diseases_in_kenya(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Percentage Incidence of Diseases in Kenya by Type and Year"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject percentageIncedenceObject = array.getJSONObject(1);
                            JSONObject diseaseObject = array.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray percentageIncedenceArray = percentageIncedenceObject.getJSONArray("data");
                            JSONArray diseaseArray = diseaseObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<Double> percentageIncedences = new ArrayList<>();
                            List<String> diseases = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                percentageIncedences.add(percentageIncedenceArray.getDouble(i));
                                diseases.add(diseaseArray.getString(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_percentage_incidence_of_diseases_in_kenya",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("incidence_id",i+1);
                                values.put("year",years.get(i));
                                values.put("percentage_incidence",percentageIncedences.get(i));
                                values.put("disease",diseases.get(i));

                                success = db.insertOrThrow("health_percentage_incidence_of_diseases_in_kenya",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_percentage_incidence_of_diseases_in_kenya: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "health_percentage_incidence_of_diseases_in_kenya: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_registeredmedicalpersonnel(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Health Registered Medical Personnel"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject countyObject = array.getJSONObject(1);
                            JSONObject medObject = array.getJSONObject(2);
                            JSONObject noObject = array.getJSONObject(3);
                            JSONObject genderObject = array.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray medArray = medObject.getJSONArray("data");
                            JSONArray noArray = noObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_registeredmedicalpersonnel",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("registered_medical_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("medical_personnel_id",/*medArray.getString(i)*/"");
                                values.put("no_of_personnel",noArray.getInt(i));
                                values.put("gender",genderArray.getString(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("health_registeredmedicalpersonnel",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_registeredmedicalpersonnel: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "health_percentage_incidence_of_diseases_in_kenya: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_nhif_members(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("NHIF Members"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject forObject = array.getJSONObject(1);
                            JSONObject infObject = array.getJSONObject(2);
                            JSONObject totalObject = array.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray forArray = forObject.getJSONArray("data");
                            JSONArray infArray = infObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_nhif_members",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("nhif_member_id",i+1);
                                values.put("formal_sector",forArray.getInt(i));
                                values.put("informal_sector",infArray.getInt(i));
                                values.put("total",totalArray.getInt(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("health_nhif_members",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_nhif_members: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "health_percentage_incidence_of_diseases_in_kenya: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_nhif_resources(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("NHIF Resources"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject benObject = array.getJSONObject(1);
                            JSONObject contObject = array.getJSONObject(2);
                            JSONObject recObject = array.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray benArray = benObject.getJSONArray("data");
                            JSONArray contArray = contObject.getJSONArray("data");
                            JSONArray recArray = recObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_nhif_resources",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("resource_id",i+1);
                                values.put("benefits",benArray.getInt(i));
                                values.put("contributions_net_benefits",contArray.getInt(i));
                                values.put("receipts",recArray.getInt(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("health_nhif_resources",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_nhif_resources: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "health_percentage_incidence_of_diseases_in_kenya: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_immunization_rate(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Full Immunization Coverage Rates for Children Under One Year"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject countyObject = array.getJSONObject(1);
                            JSONObject rateObject = array.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray rateArray = rateObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_immunization_rate",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("immunization_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("rate",rateArray.getDouble(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("health_immunization_rate",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_immunization_rate: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "health_percentage_incidence_of_diseases_in_kenya: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_current_use_of_contraception_by_county(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Current Use of Contraception by County 2014"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject rateObject = array.getJSONObject(1);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray rateArray = rateObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_current_use_of_contraception_by_county",null,null);

                            for(int i=0;i<rateArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("contraception_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("any_modem_method",rateArray.getDouble(i));

                                success = db.insertOrThrow("health_current_use_of_contraception_by_county",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_current_use_of_contraception_by_county: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "health_percentage_incidence_of_diseases_in_kenya: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_distributionofoutpatientvisitsbytypeofhealthcareprovider(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Health Distribution of Out Patient Visits by Type of Health Care Provider"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject pubObject = array.getJSONObject(1);
                            JSONObject prvObject = array.getJSONObject(2);
                            JSONObject fbObject = array.getJSONObject(3);
                            JSONObject othObject = array.getJSONObject(4);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray pubArray = pubObject.getJSONArray("data");
                            JSONArray prvArray = prvObject.getJSONArray("data");
                            JSONArray fbArray = fbObject.getJSONArray("data");
                            JSONArray othArray = othObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_distributionofoutpatientvisitsbytypeofhealthcareprovider",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("health_facility_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("public",pubArray.getDouble(i));
                                values.put("private",prvArray.getDouble(i));
                                values.put("faith_based",fbArray.getDouble(i));
                                values.put("others",othArray.getDouble(i));

                                success = db.insertOrThrow("health_distributionofoutpatientvisitsbytypeofhealthcareprovider",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_distributionofoutpatientvisitsbytypeofhealthcareprovider: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_hiv_aids_awareness_and_testing(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("HIV/AIDS Awareness and Testing 2014"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject maleObject = array.getJSONObject(1);
                            JSONObject femaleObject = array.getJSONObject(2);
                            JSONObject awareObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");
                            JSONArray awArray = awareObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_hiv_aids_awareness_and_testing",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("awareness_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("male",maleArray.getDouble(i));
                                values.put("female",femaleArray.getDouble(i));
                                values.put("hiv_awareness",awArray.getString(i));

                                success = db.insertOrThrow("health_hiv_aids_awareness_and_testing",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_hiv_aids_awareness_and_testing: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_maternal_care(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Health Maternal Care 2014"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject skillObject = array.getJSONObject(1);
                            JSONObject facObject = array.getJSONObject(2);
                            JSONObject proObject = array.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray skArray = skillObject.getJSONArray("data");
                            JSONArray facArray = facObject.getJSONArray("data");
                            JSONArray proArray = proObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_maternal_care",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("maternal_care_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("percent_receiving_antenatal_care_from_a_skilled_provider",skArray.getDouble(i));
                                values.put("percent_delivered_in_a_health_facility",facArray.getDouble(i));
                                values.put("percent_delivered_by_a_skilled_provider",proArray.getDouble(i));

                                success = db.insertOrThrow("health_maternal_care",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_maternal_care: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_insurance_coverage_by_counties_and_types(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Insurance Coverage By Counties and Types in 2013"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject insObject = array.getJSONObject(1);
                            JSONObject nhifObject = array.getJSONObject(2);
                            JSONObject cbhiObject = array.getJSONObject(3);
                            JSONObject prvObject = array.getJSONObject(4);
                            JSONObject othObject = array.getJSONObject(5);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray insArray = insObject.getJSONArray("data");
                            JSONArray nhifArray = nhifObject.getJSONArray("data");
                            JSONArray cbhiArray = cbhiObject.getJSONArray("data");
                            JSONArray prvArray = prvObject.getJSONArray("data");
                            JSONArray othArray = othObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_insurance_coverage_by_counties_and_types",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("insurance_coverage_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("insured",insArray.getDouble(i));
                                values.put("nhif",nhifArray.getDouble(i));
                                values.put("cbhi",cbhiArray.getDouble(i));
                                values.put("private",prvArray.getDouble(i));
                                values.put("others",othArray.getDouble(i));

                                success = db.insertOrThrow("health_insurance_coverage_by_counties_and_types",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_insurance_coverage_by_counties_and_types: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_nutritional_status_of_children(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Health Nutritional Status of Children 2014"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject stObject = array.getJSONObject(1);
                            JSONObject wsObject = array.getJSONObject(2);
                            JSONObject uwObject = array.getJSONObject(3);;

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray stArray = stObject.getJSONArray("data");
                            JSONArray wsArray = wsObject.getJSONArray("data");
                            JSONArray uwArray = uwObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_nutritional_status_of_children",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("nutrition_child_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("stunted",stArray.getDouble(i));
                                values.put("wasted",wsArray.getDouble(i));
                                values.put("under_weight",uwArray.getDouble(i));

                                success = db.insertOrThrow("health_nutritional_status_of_children",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_nutritional_status_of_children: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_nutritional_status_of_women(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Nutritional Status of Women 2014"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject norObject = array.getJSONObject(1);
                            JSONObject owObject = array.getJSONObject(2);
                            JSONObject obObject = array.getJSONObject(3);;

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray norArray = norObject.getJSONArray("data");
                            JSONArray owArray = owObject.getJSONArray("data");
                            JSONArray obArray = obObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_nutritional_status_of_women",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("nutrition_adult_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("undernutrition",0);
                                values.put("normal",norArray.getDouble(i));
                                values.put("overweight",owArray.getDouble(i));
                                values.put("obese",obArray.getDouble(i));

                                success = db.insertOrThrow("health_nutritional_status_of_women",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_nutritional_status_of_women: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_registered_active_nhif_members_by_county(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Registered Active NHIF Members by County"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject countyObject = array.getJSONObject(1);
                            JSONObject forObject = array.getJSONObject(2);
                            JSONObject infObject = array.getJSONObject(3);;

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray forArray = forObject.getJSONArray("data");
                            JSONArray infArray = infObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_registered_active_nhif_members_by_county",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("member_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("formal",forArray.getInt(i));
                                values.put("informal",infArray.getInt(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("health_registered_active_nhif_members_by_county",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_registered_active_nhif_members_by_county: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_registered_medical_laboratories_by_counties(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Registered Medical Laboratories by Counties"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject aObject = array.getJSONObject(1);
                            JSONObject bObject = array.getJSONObject(2);;
                            JSONObject cObject = array.getJSONObject(3);;
                            JSONObject dObject = array.getJSONObject(4);;
                            JSONObject eObject = array.getJSONObject(5);;
                            JSONObject fObject = array.getJSONObject(6);;
                            JSONObject unObject = array.getJSONObject(7);;

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray aArray = aObject.getJSONArray("data");
                            JSONArray bArray = bObject.getJSONArray("data");
                            JSONArray cArray = cObject.getJSONArray("data");
                            JSONArray dArray = dObject.getJSONArray("data");
                            JSONArray eArray = eObject.getJSONArray("data");
                            JSONArray fArray = fObject.getJSONArray("data");
                            JSONArray unArray = unObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_registered_medical_laboratories_by_counties",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("reg_med_lab_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("class_a",aArray.getInt(i));
                                values.put("class_b",bArray.getInt(i));
                                values.put("class_c",cArray.getInt(i));
                                values.put("class_d",dArray.getInt(i));
                                values.put("class_e",eArray.getInt(i));
                                values.put("class_f",fArray.getInt(i));
                                values.put("unknown",unArray.getInt(i));

                                success = db.insertOrThrow("health_registered_medical_laboratories_by_counties",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_registered_medical_laboratories_by_counties: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_use_of_mosquito_nets_by_children(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Health Use of Mosquito Nets by Children 2014"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject childObject = array.getJSONObject(1);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray chArray = childObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_use_of_mosquito_nets_by_children",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("use_mosquito_net_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("children_under_five_years_who_slept_under_nets_last_night",chArray.getInt(i));

                                success = db.insertOrThrow("health_use_of_mosquito_nets_by_children",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_use_of_mosquito_nets_by_children: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_fertility_by_education_status(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Fertility by Education Status"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject ferObject = array.getJSONObject(1);
                            JSONObject eduObject = array.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray ferArray = ferObject.getJSONArray("data");
                            JSONArray eduArray = eduObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_fertility_by_education_status",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("fertility_id",i+1);
                                values.put("fertility",ferArray.getDouble(i));
                                values.put("education_status",eduArray.getString(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("health_fertility_by_education_status",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_fertility_by_education_status: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_fertility_rate_by_age_and_residence(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Fertility Rate by Age and Residence"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject ferObject = array.getJSONObject(1);
                            JSONObject ageObject = array.getJSONObject(2);
                            JSONObject resObject = array.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray ferArray = ferObject.getJSONArray("data");
                            JSONArray ageArray = ageObject.getJSONArray("data");
                            JSONArray resArray = resObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_fertility_rate_by_age_and_residence",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("fertility_id",i+1);
                                values.put("fertility_rate",ferArray.getDouble(i));
                                values.put("age_group",ageArray.getString(i));
                                values.put("residence",resArray.getString(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("health_fertility_rate_by_age_and_residence",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_fertility_rate_by_age_and_residence: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_early_childhood_mortality_rates_by_sex(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Early Childhood Mortality Rates by Sex"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject rateObject = array.getJSONObject(1);
                            JSONObject statusObject = array.getJSONObject(2);
                            JSONObject genderObject = array.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray rateArray = rateObject.getJSONArray("data");
                            JSONArray stArray = statusObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_early_childhood_mortality_rates_by_sex",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("rate_id",i+1);
                                values.put("mortality_rate",rateArray.getDouble(i));
                                values.put("status",stArray.getString(i));
                                values.put("gender",genderArray.getString(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("health_early_childhood_mortality_rates_by_sex",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_early_childhood_mortality_rates_by_sex: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_place_of_delivery(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Place of Delivery"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject rateObject = array.getJSONObject(1);
                            JSONObject placeObject = array.getJSONObject(2);;

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray rateArray = rateObject.getJSONArray("data");
                            JSONArray plArray = placeObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_place_of_delivery",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("place_id",i+1);
                                values.put("number",rateArray.getDouble(i));
                                values.put("place",plArray.getString(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("health_place_of_delivery",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_place_of_delivery: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_prevalence_of_overweight_and_obesity(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Prevalence of Overweight and Obesity"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject ageObject = array.getJSONObject(0);
                            JSONObject womenObject = array.getJSONObject(1);
                            JSONObject menObject = array.getJSONObject(2);;
                            JSONObject totalObject = array.getJSONObject(3);;
                            JSONObject catObject = array.getJSONObject(4);;

                            JSONArray ageArray = ageObject.getJSONArray("data");
                            JSONArray womenArray = womenObject.getJSONArray("data");
                            JSONArray menArray = menObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");
                            JSONArray catArray = catObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_prevalence_of_overweight_and_obesity",null,null);

                            for(int i=0;i<ageArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("prevalence_id",i+1);
                                values.put("age_group",ageArray.getString(i));
                                values.put("women",womenArray.getInt(i));
                                values.put("men",menArray.getInt(i));
                                values.put("total",totalArray.getInt(i));
                                values.put("category",catArray.getString(i));

                                success = db.insertOrThrow("health_prevalence_of_overweight_and_obesity",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_prevalence_of_overweight_and_obesity: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_percentage_adults_who_are_current_users(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Percentage of Adults 15+ old who are current users of various smokeless tobacco products by sex"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject ageObject = array.getJSONObject(0);
                            JSONObject womenObject = array.getJSONObject(1);
                            JSONObject menObject = array.getJSONObject(2);;
                            JSONObject catObject = array.getJSONObject(3);;

                            JSONArray ageArray = ageObject.getJSONArray("data");
                            JSONArray womenArray = womenObject.getJSONArray("data");
                            JSONArray menArray = menObject.getJSONArray("data");
                            JSONArray catArray = catObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_percentage_adults_who_are_current_users",null,null);

                            for(int i=0;i<ageArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("user_id",i+1);
                                values.put("age_group",ageArray.getString(i));
                                values.put("women",womenArray.getDouble(i));
                                values.put("men",menArray.getDouble(i));
                                values.put("category",catArray.getString(i));

                                success = db.insertOrThrow("health_percentage_adults_who_are_current_users",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_percentage_adults_who_are_current_users: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_percentage_who_drink_alcohol_by_age(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Percentage of Women and Men aged 15-49 who drink alcohol by age, 2014"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject ageObject = array.getJSONObject(0);
                            JSONObject womenObject = array.getJSONObject(1);
                            JSONObject menObject = array.getJSONObject(2);;

                            JSONArray ageArray = ageObject.getJSONArray("data");
                            JSONArray womenArray = womenObject.getJSONArray("data");
                            JSONArray menArray = menObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_percentage_who_drink_alcohol_by_age",null,null);

                            for(int i=0;i<ageArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("percentage_id",i+1);
                                values.put("age",ageArray.getString(i));
                                values.put("women",womenArray.getDouble(i));
                                values.put("men",menArray.getDouble(i));

                                success = db.insertOrThrow("health_percentage_who_drink_alcohol_by_age",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_percentage_who_drink_alcohol_by_age: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_kihibs_children_by_additional_supplement(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("kihibs children by additional supplement"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject znObject = array.getJSONObject(0);
                            JSONObject sugObject = array.getJSONObject(1);
                            JSONObject othObject = array.getJSONObject(2);
                            JSONObject noneObject = array.getJSONObject(3);
                            JSONObject nsObject = array.getJSONObject(4);
                            JSONObject indObject = array.getJSONObject(5);
                            JSONObject countyObject = array.getJSONObject(6);

                            JSONArray znArray = znObject.getJSONArray("data");
                            JSONArray sugArray = sugObject.getJSONArray("data");
                            JSONArray othArray = othObject.getJSONArray("data");
                            JSONArray noneArray = noneObject.getJSONArray("data");
                            JSONArray nsArray = nsObject.getJSONArray("data");
                            JSONArray indArray = indObject.getJSONArray("data");
                            JSONArray countyArray = countyObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_kihibs_children_by_additional_supplement",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("distribution_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("zinc_solution",znArray.getDouble(i));
                                values.put("sugar_salt_solution",sugArray.getDouble(i));
                                values.put("other_solutions",othArray.getDouble(i));
                                values.put("none",noneArray.getDouble(i));
                                values.put("not_stated",nsArray.getDouble(i));
                                values.put("no_of_individuals",indArray.getInt(i));

                                success = db.insertOrThrow("health_kihibs_children_by_additional_supplement",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_kihibs_children_by_additional_supplement: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_kihibs_children_by_who_assisted_at_birth(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("kihibs children assisted at birth"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject docObject = array.getJSONObject(0);
                            JSONObject midObject = array.getJSONObject(1);
                            JSONObject tbaObject = array.getJSONObject(2);
                            JSONObject ttbaObject = array.getJSONObject(3);
                            JSONObject selfObject = array.getJSONObject(4);
                            JSONObject othObject = array.getJSONObject(5);
                            JSONObject nsObject = array.getJSONObject(6);
                            JSONObject indObject = array.getJSONObject(7);
                            JSONObject countyObject = array.getJSONObject(8);

                            JSONArray docArray = docObject.getJSONArray("data");
                            JSONArray midArray = midObject.getJSONArray("data");
                            JSONArray tbaArray = tbaObject.getJSONArray("data");
                            JSONArray ttbaArray = ttbaObject.getJSONArray("data");
                            JSONArray selfArray = selfObject.getJSONArray("data");
                            JSONArray othArray = othObject.getJSONArray("data");
                            JSONArray nsArray = nsObject.getJSONArray("data");
                            JSONArray indArray = indObject.getJSONArray("data");
                            JSONArray countyArray = countyObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_kihibs_children_by_who_assisted_at_birth",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("proportion_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("doctor",docArray.getDouble(i));
                                values.put("midwife_nurse",midArray.getDouble(i));
                                values.put("tba",tbaArray.getDouble(i));
                                values.put("ttba",ttbaArray.getDouble(i));
                                values.put("self",selfArray.getDouble(i));
                                values.put("other",othArray.getDouble(i));
                                values.put("not_stated",nsArray.getDouble(i));
                                values.put("no_of_individuals",indArray.getInt(i));

                                success = db.insertOrThrow("health_kihibs_children_by_who_assisted_at_birth",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_kihibs_children_by_who_assisted_at_birth: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_health_kihibs_children_immmunized_against_measles(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("kihibs children immunized against measles"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject msObject = array.getJSONObject(0);
                            JSONObject proObject = array.getJSONObject(1);
                            JSONObject countyObject = array.getJSONObject(2);

                            JSONArray msArray = msObject.getJSONArray("data");
                            JSONArray proArray = proObject.getJSONArray("data");
                            JSONArray countyArray = countyObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_kihibs_children_immmunized_against_measles",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("proportion_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("measles_vaccination",msArray.getString(i));
                                values.put("proportion",proArray.getDouble(i));

                                success = db.insertOrThrow("health_kihibs_children_immmunized_against_measles",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_kihibs_children_immmunized_against_measles: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Errot: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

}