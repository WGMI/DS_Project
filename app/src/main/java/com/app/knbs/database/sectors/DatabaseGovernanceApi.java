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

import java.util.ArrayList;
import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 28/02/2018.
 */

public class DatabaseGovernanceApi {
    private Context context;
    public DatabaseGovernanceApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);

    public void loadData(final ProgressDialog d){
        insertInto_governance_cases_forwarded_and_action_taken(d);
        insertInto_governance_cases_handled_by_different_courts(d);
        insertInto_governance_convicted_prisoners_by_type_of_offence_and_sex(d);
        insertInto_governance_convicted_prison_population_by_age_and_sex(d);
        insertInto_governance_daily_average_population_of_prisoners_by_sex(d);
        insertInto_governance_environmental_crimes_reported_to_nema(d);
        insertInto_governance_magistrates_judges_and_practicing_lawyers(d);
        insertInto_governance_murder_cases_and_convictions_obtained_by_high_court(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_governance_cases_forwarded_and_action_taken(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/governance/all_governance_cases_forwarded_and_action_taken",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject actionObject  = response.getJSONObject(1);
                            JSONObject noOfRecsObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray actionArray = actionObject.getJSONArray("data");
                            JSONArray noOfRecsArray = noOfRecsObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<String> actions = new ArrayList<>();
                            List<Integer> noOfRecs = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                actions.add(actionArray.getString(i));
                                noOfRecs.add(noOfRecsArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_cases_forwarded_and_action_taken",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("action_id",i+1);
                                values.put("action_taken",actions.get(i));
                                values.put("no_of_recommendations",noOfRecs.get(i));
                                values.put("year",years.get(i));

                                success = db.insertOrThrow("governance_cases_forwarded_and_action_taken",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_cases_forwarded_and_action_taken: " + success);

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

    private void insertInto_governance_cases_handled_by_different_courts(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/governance/all_governance_cases_handled_by_various_courts",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject categoryObject  = response.getJSONObject(1);
                            JSONObject kadhisObject  = response.getJSONObject(2);
                            JSONObject magistrateObject  = response.getJSONObject(3);
                            JSONObject highObject  = response.getJSONObject(4);
                            JSONObject appealObject  = response.getJSONObject(5);
                            JSONObject supremeObject  = response.getJSONObject(6);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray categoryArray = categoryObject.getJSONArray("data");
                            JSONArray kadhisArray = kadhisObject.getJSONArray("data");
                            JSONArray magistrateArray = magistrateObject.getJSONArray("data");
                            JSONArray highArray = highObject.getJSONArray("data");
                            JSONArray appealArray = appealObject.getJSONArray("data");
                            JSONArray supremeArray = supremeObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<String> categories = new ArrayList<>();
                            List<Integer> kadhisList = new ArrayList<>();
                            List<Integer> magistrateList = new ArrayList<>();
                            List<Integer> highList = new ArrayList<>();
                            List<Integer> appealList = new ArrayList<>();
                            List<Integer> supremeList = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                categories.add(categoryArray.getString(i));
                                kadhisList.add(kadhisArray.getInt(i));
                                magistrateList.add(magistrateArray.getInt(i));
                                highList.add(highArray.getInt(i));
                                appealList.add(appealArray.getInt(i));
                                supremeList.add(supremeArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_cases_handled_by_different_courts",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("court_id",i+1);
                                values.put("category",categories.get(i));
                                values.put("kadhis_court",kadhisList.get(i));
                                values.put("magistrate_court",magistrateList.get(i));
                                values.put("high_court",highList.get(i));
                                values.put("court_of_appeal",appealList.get(i));
                                values.put("supreme_court",supremeList.get(i));
                                values.put("year",years.get(i));

                                success = db.insertOrThrow("governance_cases_handled_by_different_courts",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_cases_handled_by_different_courts: " + success);

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

    private void insertInto_governance_convicted_prisoners_by_type_of_offence_and_sex(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/governance/all_governance_convicted_prisoners_by_type_of_offence_and_sex",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject offenceObject  = response.getJSONObject(1);
                            JSONObject maleObject  = response.getJSONObject(2);
                            JSONObject femaleObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray offenceArray = offenceObject.getJSONArray("data");
                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<String> offences = new ArrayList<>();
                            List<Integer> males = new ArrayList<>();
                            List<Integer> females = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                offences.add(offenceArray.getString(i));
                                males.add(maleArray.getInt(i));
                                females.add(femaleArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_convicted_prisoners_by_type_of_offence_and_sex",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("convicted_offence_type",i+1);
                                values.put("offence",offences.get(i));
                                values.put("male",males.get(i));
                                values.put("female",females.get(i));
                                values.put("year",years.get(i));

                                success = db.insertOrThrow("governance_convicted_prisoners_by_type_of_offence_and_sex",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_convicted_prisoners_by_type_of_offence_and_sex: " + success);

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

    private void insertInto_governance_convicted_prison_population_by_age_and_sex(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/governance/all_governance_convicted_prison_population_by_age_and_sex",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject categoryObject  = response.getJSONObject(1);
                            JSONObject maleObject  = response.getJSONObject(2);
                            JSONObject femaleObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray categoryArray = categoryObject.getJSONArray("data");
                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<String> categories = new ArrayList<>();
                            List<Integer> males = new ArrayList<>();
                            List<Integer> females = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                categories.add(categoryArray.getString(i));
                                males.add(maleArray.getInt(i));
                                females.add(femaleArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_convicted_prison_population_by_age_and_sex",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("convict_population",i+1);
                                values.put("category",categories.get(i));
                                values.put("male",males.get(i));
                                values.put("female",females.get(i));
                                values.put("year",years.get(i));

                                success = db.insertOrThrow("governance_convicted_prison_population_by_age_and_sex",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_convicted_prison_population_by_age_and_sex: " + success);

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

    private void insertInto_governance_daily_average_population_of_prisoners_by_sex(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/governance/all_governance_daily_average_population_of_prisoners_by_sex",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject categoryObject  = response.getJSONObject(1);
                            JSONObject maleObject  = response.getJSONObject(2);
                            JSONObject femaleObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray categoryArray = categoryObject.getJSONArray("data");
                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<String> categories = new ArrayList<>();
                            List<Integer> males = new ArrayList<>();
                            List<Integer> females = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                categories.add(categoryArray.getString(i));
                                males.add(maleArray.getInt(i));
                                females.add(femaleArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_daily_average_population_of_prisoners_by_sex",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("daily_population_prisoners",i+1);
                                values.put("category",categories.get(i));
                                values.put("male",males.get(i));
                                values.put("female",females.get(i));
                                values.put("year",years.get(i));

                                success = db.insertOrThrow("governance_daily_average_population_of_prisoners_by_sex",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_daily_average_population_of_prisoners_by_sex: " + success);

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

    private void insertInto_governance_environmental_crimes_reported_to_nema(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/governance/all_governance_environmental_crimes_reported_to_nema",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject typeObject  = response.getJSONObject(1);
                            JSONObject noOfCasesObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray typeArray = typeObject.getJSONArray("data");
                            JSONArray noOfCasesArray = noOfCasesObject.getJSONArray("data");

                            /*List<Integer> years = new ArrayList<>();
                            List<String> types = new ArrayList<>();
                            List<Integer> noOfCasesList = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                types.add(typeArray.getString(i));
                                noOfCasesList.add(noOfCasesArray.getInt(i));
                            }*/

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_environmental_crimes_reported_to_nema",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("crime_id",i+1);
                                values.put("type_of_case",typeArray.getString(i));
                                values.put("no_of_cases",noOfCasesArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_environmental_crimes_reported_to_nema",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_environmental_crimes_reported_to_nema: " + success);

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

    private void insertInto_governance_magistrates_judges_and_practicing_lawyers(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/governance/all_governance_magistrates_judges_and_practicing_lawyers",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject categoryObject  = response.getJSONObject(1);
                            JSONObject maleObject  = response.getJSONObject(2);
                            JSONObject femaleObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray categoryArray = categoryObject.getJSONArray("data");
                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_magistrates_judges_and_practicing_lawyers",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("category_id",i+1);
                                values.put("category",categoryArray.getString(i));
                                values.put("male",maleArray.getInt(i));
                                values.put("female",femaleArray.getInt(i));
                                values.put("total",maleArray.getInt(i) + femaleArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_magistrates_judges_and_practicing_lawyers",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_magistrates_judges_and_practicing_lawyers: " + success);

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

    private void insertInto_governance_murder_cases_and_convictions_obtained_by_high_court(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/governance/all_governance_murder_cases_and_convictions_obtained_by_high_court",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject courtObject  = response.getJSONObject(1);
                            JSONObject regCasesObject  = response.getJSONObject(2);
                            JSONObject obtainedObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray courtArray = courtObject.getJSONArray("data");
                            JSONArray regCasesArray = regCasesObject.getJSONArray("data");
                            JSONArray obtainedArray = obtainedObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_murder_cases_and_convictions_obtained_by_high_court_s",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("reg_murder_convictions_obtained_id",i+1);
                                values.put("court_station",courtArray.getString(i));
                                values.put("registered_murder_cases",regCasesArray.getInt(i));
                                values.put("murder_convictions_obtained",obtainedArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_murder_cases_and_convictions_obtained_by_high_court_s",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_murder_cases_and_convictions_obtained_by_high_court_s: " + success);

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

    //Bad API
    private void insertInto_governance_firearms_and_ammunition_recovered_or_surrendered(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/governance/all_governance_firearms_and_ammunition_recovered_or_surrendered",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject typeObject  = response.getJSONObject(1);
                            JSONObject noOfCasesObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray typeArray = typeObject.getJSONArray("data");
                            JSONArray noOfCasesArray = noOfCasesObject.getJSONArray("data");

                            /*List<Integer> years = new ArrayList<>();
                            List<String> types = new ArrayList<>();
                            List<Integer> noOfCasesList = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                types.add(typeArray.getString(i));
                                noOfCasesList.add(noOfCasesArray.getInt(i));
                            }*/

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_environmental_crimes_reported_to_nema",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("crime_id",i+1);
                                values.put("type_of_case",typeArray.getString(i));
                                values.put("no_of_cases",noOfCasesArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_environmental_crimes_reported_to_nema",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_environmental_crimes_reported_to_nema: " + success);

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

    //API: Sorry, the page you are looking for could not be found.
    private void insertInto_governance_cases_handled_by_ethics_commision(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);


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