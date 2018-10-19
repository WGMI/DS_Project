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

public class DatabaseGovernanceApi {
    private Context context;
    public DatabaseGovernanceApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);
    private ReportLoader loader = new ReportLoader(context);
    private CountyHelper countyHelper = new CountyHelper();

    public void loadData(final ProgressDialog d){
        /*insertInto_governance_cases_forwarded_and_action_taken(d);
        insertInto_governance_cases_handled_by_different_courts(d);
        insertInto_governance_convicted_prisoners_by_type_of_offence_and_sex(d);
        insertInto_governance_convicted_prison_population_by_age_and_sex(d);
        insertInto_governance_daily_average_population_of_prisoners_by_sex(d);

        insertInto_governance_environmental_crimes_reported_to_nema(d);
        insertInto_governance_magistrates_judges_and_practicing_lawyers(d);
        insertInto_governance_murder_cases_and_convictions_obtained_by_high_court(d);
        insertInto_governance_number_of_police_prisons_and_probation_officers(d);
        insertInto_governance_number_of_refugees_by_age_and_sex(d);

        insertInto_governance_offences_committed_against_morality(d);
        insertInto_governance_offenders_serving(d);
        insertInto_governance_passports_work_permits_and_foreigners_registered(d);
        insertInto_governance_persons_reported_committed_offences_related_to_drugs(d);
        insertInto_governance_persons_reported_to_have_committed_homicide_by_sex(d);

        insertInto_governance_persons_reported_to_have_committed_robbery_and_theft(d);
        insertInto_governance_prison_population_by_sentence_duration_and_sex(d);
        insertInto_governance_public_assets_traced_recovered_and_loss_averted(d);*/
        insertInto_governance_cases_handled_by_ethics_commision(d);
        insertInto_governance_crimes_reported_to_police_by_command_stations(d);

        insertInto_governance_identity_cards_made_processed_and_collected(d);
        insertInto_governance_offence_by_sex_and_command_stations(d);
        insertInto_governance_registered_voters_by_county_and_by_sex(d);
        insertInto_governance_experienceof_domestic_violence_by_age(d);
        insertInto_governance_experienceof_domestic_violence_by_marital_success(d);

        insertInto_governance_experienceof_domestic_violence_by_residence(d);
        insertInto_governance_knowledge_and_prevalence_of_female_circumcision(d);
        insertInto_governance_members_of_nationalassembly_and_senators(d);
        insertInto_governance_persons_reported_tohave_committed_defilement(d);
        insertInto_governance_persons_reported_tohave_committed_rape(d);

        insertInto_governance_total_prisoners_committed_for_debt_bysex(d);
        insertInto_governance_prevalence_female_circumcision_and_type(d);
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
                loader.getApi("Cases forwarded to the Office of the Director of Public Prosecution and Action Taken"),
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
                loader.getApi("Cases Handled by Various Courts"),
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
                loader.getApi("Convicted Prisoners by Type Of Offence and Sex"),
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
                loader.getApi("Convicted Prison Population by Age and Sex"),
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
                loader.getApi("Daily Average Population of Prisoners by Sex"),
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
                loader.getApi("Environmental Crimes Reported to NEMA"),
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
                loader.getApi("Magistrates, Judges and Practicing Lawyers"),
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
                loader.getApi("Murder Cases and Convictions Obtained by High Court"),
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

    private void insertInto_governance_number_of_police_prisons_and_probation_officers(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Number of Police Prisons and Probation Officers"),
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
                            db.delete("governance_number_of_police_prisons_and_probation_officers",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("category_id",i+1);
                                values.put("category",categoryArray.getString(i));
                                values.put("male",maleArray.getInt(i));
                                values.put("female",femaleArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_number_of_police_prisons_and_probation_officers",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_number_of_police_prisons_and_probation_officers: " + success);

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

    private void insertInto_governance_number_of_refugees_by_age_and_sex(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Number of Refugees by Age and Sex"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject childrenObject  = response.getJSONObject(1);
                            JSONObject adultObject  = response.getJSONObject(2);
                            JSONObject genderObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray childrenArray = childrenObject.getJSONArray("data");
                            JSONArray adultArray = adultObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_number_of_refugees_by_age_and_sex",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("category_id",i+1);
                                values.put("children",childrenArray.getInt(i));
                                values.put("adult",adultArray.getInt(i));
                                values.put("gender",genderArray.getString(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_number_of_refugees_by_age_and_sex",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_number_of_refugees_by_age_and_sex: " + success);

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

    private void insertInto_governance_offences_committed_against_morality(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Offences Committed Against Morality"),
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
                            db.delete("governance_offences_committed_against_morality",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("offences_commiited_against_morality_id",i+1);
                                values.put("category_id",categoryArray.getString(i));
                                values.put("male",maleArray.getInt(i));
                                values.put("female",femaleArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_offences_committed_against_morality",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_offences_committed_against_morality: " + success);

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

    private void insertInto_governance_offenders_serving(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Offenders Serving"),
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

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_offenders_serving",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("offence_id",i+1);
                                values.put("offence",offenceArray.getString(i));
                                values.put("male",maleArray.getInt(i));
                                values.put("female",femaleArray.getInt(i));
                                values.put("category","");
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_offenders_serving",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_offenders_serving: " + success);

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

    private void insertInto_governance_passports_work_permits_and_foreigners_registered(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Passports Work Permits and Foreigners Registered"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject passIssuedObject  = response.getJSONObject(1);
                            JSONObject forNatRegObject  = response.getJSONObject(2);
                            JSONObject workPerIssuedObject  = response.getJSONObject(3);
                            JSONObject workPerRenObject  = response.getJSONObject(4);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray passIssuedArray = passIssuedObject.getJSONArray("data");
                            JSONArray forNatRegArray = forNatRegObject.getJSONArray("data");
                            JSONArray workPerIssuedArray = workPerIssuedObject.getJSONArray("data");
                            JSONArray workPerRenArray = workPerRenObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_passports_work_permits_and_foreigners_registered",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("passports_permits_registered_foreigners",i+1);
                                values.put("passport_issued",passIssuedArray.getInt(i));
                                values.put("foreign_nat_reg",forNatRegArray.getInt(i));
                                values.put("work_permit_issued",workPerIssuedArray.getInt(i));
                                values.put("work_permit_ren",workPerRenArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_passports_work_permits_and_foreigners_registered",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_passports_work_permits_and_foreigners_registered: " + success);

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

    private void insertInto_governance_persons_reported_committed_offences_related_to_drugs(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("People Reported to have Committed Offence Related to Drugs"),
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

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_people_reported_to_have_committed_offence_related_to_",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("people_offence_related_drugs_id",i+1);
                                values.put("offence",offenceArray.getString(i));
                                values.put("male",maleArray.getInt(i));
                                values.put("female",femaleArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_people_reported_to_have_committed_offence_related_to_",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_people_reported_to_have_committed_offence_related_to_: " + success);

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

    private void insertInto_governance_persons_reported_to_have_committed_homicide_by_sex(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Persons Reported to have Committed Homicide by Sex"),
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

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_persons_reported_to_have_committed_homicide_by_sex",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("offence_id",i+1);
                                values.put("offence",offenceArray.getString(i));
                                values.put("male",maleArray.getInt(i));
                                values.put("female",femaleArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_persons_reported_to_have_committed_homicide_by_sex",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_persons_reported_to_have_committed_homicide_by_sex: " + success);

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

    private void insertInto_governance_persons_reported_to_have_committed_robbery_and_theft(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Persons Reported to have Committed Robbery and Theft"),
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

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_persons_reported_to_have_committed_robbery_and_theft",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("offence_id",i+1);
                                values.put("offence",offenceArray.getString(i));
                                values.put("male",maleArray.getInt(i));
                                values.put("female",femaleArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_persons_reported_to_have_committed_robbery_and_theft",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_persons_reported_to_have_committed_robbery_and_theft: " + success);

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

    private void insertInto_governance_prison_population_by_sentence_duration_and_sex(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Prison Population by Sentence Duration and Sex"),
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
                            db.delete("governance_prison_population_by_sentence_duration_and_sex",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("category_id",i+1);
                                values.put("category",categoryArray.getString(i));
                                values.put("male",maleArray.getInt(i));
                                values.put("female",femaleArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_prison_population_by_sentence_duration_and_sex",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_prison_population_by_sentence_duration_and_sex: " + success);

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

    private void insertInto_governance_public_assets_traced_recovered_and_loss_averted(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Public Assets Traced Recovered and Loss Averted"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject tracedObject  = response.getJSONObject(1);
                            JSONObject recoveredObject  = response.getJSONObject(2);
                            JSONObject avertedObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray tracedArray = tracedObject.getJSONArray("data");
                            JSONArray recoveredArray = recoveredObject.getJSONArray("data");
                            JSONArray avertedArray = avertedObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_public_assets_traced_recovered_and_loss_averted",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("category_id",i+1);
                                values.put("public_assets_traced",tracedArray.getInt(i));
                                values.put("public_assets_recovered",recoveredArray.getInt(i));
                                values.put("loss_averted",avertedArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_public_assets_traced_recovered_and_loss_averted",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_public_assets_traced_recovered_and_loss_averted: " + success);

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
                loader.getApi("Firearms and Ammunition Recovered or Surrendered"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject tracedObject  = response.getJSONObject(1);
                            JSONObject recoveredObject  = response.getJSONObject(2);
                            JSONObject avertedObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray tracedArray = tracedObject.getJSONArray("data");
                            JSONArray recoveredArray = recoveredObject.getJSONArray("data");
                            JSONArray avertedArray = avertedObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_public_assets_traced_recovered_and_loss_averted",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("category_id",i+1);
                                values.put("public_assets_traced",tracedArray.getInt(i));
                                values.put("public_assets_recovered",recoveredArray.getInt(i));
                                values.put("loss_averted",avertedArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_public_assets_traced_recovered_and_loss_averted",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_public_assets_traced_recovered_and_loss_averted: " + success);

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

    private void insertInto_governance_cases_handled_by_ethics_commision(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Cases Handled by Ethics Commision"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject actionObject  = response.getJSONObject(1);
                            JSONObject noObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray actArray = actionObject.getJSONArray("data");
                            JSONArray noArray = noObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_cases_handled_by_ethics_commision",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("cases_handled_id",i+1);
                                values.put("action",actArray.getString(i));
                                values.put("no_cases",noArray.getInt(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("governance_cases_handled_by_ethics_commision",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_cases_handled_by_ethics_commision: " + success);

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

    private void insertInto_governance_crimes_reported_to_police_by_command_stations(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Crimes Reported to Police by Command Stations"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject countyObject  = response.getJSONObject(1);
                            JSONObject crimeObject  = response.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray crArray = crimeObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_crimes_reported_to_police_by_command_stations",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("crime_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("crimes",crArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_crimes_reported_to_police_by_command_stations",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_crimes_reported_to_police_by_command_stations: " + success);

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

    private void insertInto_governance_identity_cards_made_processed_and_collected(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Identity Cards Made Processed and Collected"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject countyObject  = response.getJSONObject(1);
                            JSONObject appsObject  = response.getJSONObject(2);
                            JSONObject prodObject  = response.getJSONObject(3);
                            JSONObject collObject  = response.getJSONObject(4);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray apArray = appsObject.getJSONArray("data");
                            JSONArray prArray = prodObject.getJSONArray("data");
                            JSONArray collArray = collObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_identity_cards_made_processed_and_collected",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("nprs_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("npr_apps_made",apArray.getInt(i));
                                values.put("npr_ids_prod",prArray.getInt(i));
                                values.put("npr_ids_collected",collArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_identity_cards_made_processed_and_collected",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_identity_cards_made_processed_and_collected: " + success);

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

    private void insertInto_governance_offence_by_sex_and_command_stations(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Offence by Sex and Command Stations"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject countyObject  = response.getJSONObject(1);
                            JSONObject maleObject  = response.getJSONObject(2);
                            JSONObject femaleObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_offence_by_sex_and_command_stations",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("offence_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("male",maleArray.getInt(i));
                                values.put("female",femaleArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_offence_by_sex_and_command_stations",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_offence_by_sex_and_command_stations: " + success);

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

    private void insertInto_governance_registered_voters_by_county_and_by_sex(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Registered Voters by County and Sex"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject countyObject  = response.getJSONObject(0);
                            JSONObject subCountyObject  = response.getJSONObject(1);
                            JSONObject regObject  = response.getJSONObject(2);
                            JSONObject genderObject  = response.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray subCountyArray = subCountyObject.getJSONArray("data");
                            JSONArray regArray = regObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_registered_voters_by_county_and_by_sex",null,null);

                            for(int i=0;i<genderArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("voters_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("sub_counties_id",countyHelper.getSubCountyId(subCountyArray.getString(i)));
                                values.put("reg_voters",regArray.getInt(i));
                                values.put("gender",genderArray.getString(i).trim());

                                success = db.insertOrThrow("governance_registered_voters_by_county_and_by_sex",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_registered_voters_by_county_and_by_sex: " + success);

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

    private void insertInto_governance_experienceof_domestic_violence_by_age(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Experience of Domestic Violence By Age"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject ageObject  = response.getJSONObject(0);
                            JSONObject physObject  = response.getJSONObject(1);
                            JSONObject sexObject  = response.getJSONObject(2);
                            JSONObject bothObject  = response.getJSONObject(3);
                            JSONObject genderObject  = response.getJSONObject(4);

                            JSONArray ageArray = ageObject.getJSONArray("data");
                            JSONArray physArray = physObject.getJSONArray("data");
                            JSONArray sexArray = sexObject.getJSONArray("data");
                            JSONArray bothArray = bothObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_experienceof_domestic_violence_by_age",null,null);

                            for(int i=0;i<genderArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("woman_id",i+1);
                                values.put("age",ageArray.getString(i));
                                values.put("percentage_experienced_physical_violence",physArray.getDouble(i));
                                values.put("percentage_experienced_sexual_violence",sexArray.getDouble(i));
                                values.put("percentage_experienced_physical_and_sexual_violence",bothArray.getDouble(i));
                                values.put("gender",genderArray.getString(i));

                                success = db.insertOrThrow("governance_experienceof_domestic_violence_by_age",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_experienceof_domestic_violence_by_age: " + success);

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

    private void insertInto_governance_experienceof_domestic_violence_by_marital_success(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Experience of Domestic Violence by Marital Success"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject statusObject  = response.getJSONObject(0);
                            JSONObject physObject  = response.getJSONObject(1);
                            JSONObject sexObject  = response.getJSONObject(2);
                            JSONObject bothObject  = response.getJSONObject(3);
                            JSONObject genderObject  = response.getJSONObject(4);

                            JSONArray statusArray = statusObject.getJSONArray("data");
                            JSONArray physArray = physObject.getJSONArray("data");
                            JSONArray sexArray = sexObject.getJSONArray("data");
                            JSONArray bothArray = bothObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_experienceof_domestic_violence_by_marital_success",null,null);

                            for(int i=0;i<genderArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("woman_id",i+1);
                                values.put("marital_status",statusArray.getString(i));
                                values.put("percentage_experienced_physical_violence",physArray.getDouble(i));
                                values.put("percentage_experienced_sexual_violence",sexArray.getDouble(i));
                                values.put("percentage_experienced_physical_and_sexual_violence",bothArray.getDouble(i));
                                values.put("gender",genderArray.getString(i));

                                success = db.insertOrThrow("governance_experienceof_domestic_violence_by_marital_success",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_experienceof_domestic_violence_by_marital_success: " + success);

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

    private void insertInto_governance_experienceof_domestic_violence_by_residence(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Experience of Domestic Violence by Residence"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject resObject  = response.getJSONObject(0);
                            JSONObject physObject  = response.getJSONObject(1);
                            JSONObject sexObject  = response.getJSONObject(2);
                            JSONObject bothObject  = response.getJSONObject(3);
                            JSONObject genderObject  = response.getJSONObject(4);

                            JSONArray resArray = resObject.getJSONArray("data");
                            JSONArray physArray = physObject.getJSONArray("data");
                            JSONArray sexArray = sexObject.getJSONArray("data");
                            JSONArray bothArray = bothObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_experienceof_domestic_violence_by_residence",null,null);

                            for(int i=0;i<genderArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("woman_id",i+1);
                                values.put("residence",resArray.getString(i));
                                values.put("percentage_experienced_physical_violence",physArray.getDouble(i));
                                values.put("percentage_experienced_sexual_violence",sexArray.getDouble(i));
                                values.put("percentage_experienced_physical_and_sexual_violence",bothArray.getDouble(i));
                                values.put("gender",genderArray.getString(i));

                                success = db.insertOrThrow("governance_experienceof_domestic_violence_by_residence",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_experienceof_domestic_violence_by_residence: " + success);

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

    private void insertInto_governance_knowledge_and_prevalence_of_female_circumcision(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Knowledge and Prevalence of Female Circumcision"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject ageObject  = response.getJSONObject(0);
                            JSONObject heardObject  = response.getJSONObject(1);
                            JSONObject unheardObject  = response.getJSONObject(2);

                            JSONArray ageArray = ageObject.getJSONArray("data");
                            JSONArray heardArray = heardObject.getJSONArray("data");
                            JSONArray unheardArray = unheardObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_knowledge_and_prevalence_of_female_circumcision",null,null);

                            for(int i=0;i<ageArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("woman_id",i+1);
                                values.put("age",ageArray.getString(i));
                                values.put("percentage_women_heard_of_FC",heardArray.getDouble(i));
                                values.put("percentage_women_not_heard_of_FC",unheardArray.getDouble(i));
                                values.put("year","");

                                success = db.insertOrThrow("governance_knowledge_and_prevalence_of_female_circumcision",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_knowledge_and_prevalence_of_female_circumcision: " + success);

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

    private void insertInto_governance_members_of_nationalassembly_and_senators(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Members of National Assembly and Senators"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject statusObject  = response.getJSONObject(0);
                            JSONObject menObject  = response.getJSONObject(1);
                            JSONObject womenObject  = response.getJSONObject(2);
                            JSONObject totalObject  = response.getJSONObject(3);
                            JSONObject perWomenObject  = response.getJSONObject(4);

                            JSONArray stArray = statusObject.getJSONArray("data");
                            JSONArray menArray = menObject.getJSONArray("data");
                            JSONArray womenArray = womenObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");
                            JSONArray pwArray = perWomenObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_members_of_nationalassembly_and_senators",null,null);

                            for(int i=0;i<stArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("woman_id",i+1);
                                values.put("status",stArray.getString(i));
                                values.put("women",womenArray.getInt(i));
                                values.put("men",menArray.getInt(i));
                                values.put("total",totalArray.getInt(i));
                                values.put("women_percentage",pwArray.getInt(i));

                                success = db.insertOrThrow("governance_members_of_nationalassembly_and_senators",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_members_of_nationalassembly_and_senators: " + success);

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

    private void insertInto_governance_persons_reported_tohave_committed_defilement(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Persons Reported to have Committed Defilement"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject noObject  = response.getJSONObject(1);
                            JSONObject propObject  = response.getJSONObject(2);
                            JSONObject genderObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray noArray = noObject.getJSONArray("data");
                            JSONArray propArray = propObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_persons_reported_tohave_committed_defilement",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("persons_id",i+1);
                                values.put("number",noArray.getInt(i));
                                values.put("proportion",propArray.getDouble(i));
                                values.put("gender",genderArray.getString(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_persons_reported_tohave_committed_defilement",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_persons_reported_tohave_committed_defilement: " + success);

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

    private void insertInto_governance_persons_reported_tohave_committed_rape(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Persons Reported to have Committed Rape"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject noObject  = response.getJSONObject(1);
                            JSONObject propObject  = response.getJSONObject(2);
                            JSONObject genderObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray noArray = noObject.getJSONArray("data");
                            JSONArray propArray = propObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_persons_reported_tohave_committed_rape",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("persons_id",i+1);
                                values.put("number",noArray.getInt(i));
                                values.put("proportion",propArray.getDouble(i));
                                values.put("gender",genderArray.getString(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_persons_reported_tohave_committed_rape",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_persons_reported_tohave_committed_rape: " + success);

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

    private void insertInto_governance_total_prisoners_committed_for_debt_bysex(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Total Prisoners Committed For Debt by Sex"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject noObject  = response.getJSONObject(1);
                            JSONObject propObject  = response.getJSONObject(2);
                            JSONObject genderObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray noArray = noObject.getJSONArray("data");
                            JSONArray propArray = propObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_total_prisoners_committed_for_debt_bysex",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("persons_id",i+1);
                                values.put("number",noArray.getInt(i));
                                values.put("proportion",propArray.getDouble(i));
                                values.put("gender",genderArray.getString(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("governance_total_prisoners_committed_for_debt_bysex",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_total_prisoners_committed_for_debt_bysex: " + success);

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

    private void insertInto_governance_prevalence_female_circumcision_and_type(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Prevalence Female Circumcision and Type"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject ageObject  = response.getJSONObject(0);
                            JSONObject nofleshObject  = response.getJSONObject(1);
                            JSONObject fleshObject  = response.getJSONObject(2);
                            JSONObject sewnObject  = response.getJSONObject(3);
                            JSONObject othersObject  = response.getJSONObject(3);
                            JSONObject percObject  = response.getJSONObject(3);

                            JSONArray ageArray = ageObject.getJSONArray("data");
                            JSONArray nfArray = nofleshObject.getJSONArray("data");
                            JSONArray flArray = fleshObject.getJSONArray("data");
                            JSONArray swArray = sewnObject.getJSONArray("data");
                            JSONArray othArray = othersObject.getJSONArray("data");
                            JSONArray pcArray = percObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("governance_prevalence_female_circumcision_and_type",null,null);

                            for(int i=0;i<ageArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("persons_id",i+1);
                                values.put("age",ageArray.getString(i));
                                values.put("cut_no_flesh_removed",nfArray.getDouble(i));
                                values.put("cut_flesh_removed",flArray.getDouble(i));
                                values.put("sewn_closed",swArray.getDouble(i));
                                values.put("others",othArray.getDouble(i));
                                values.put("percentage_women_circumcised",pcArray.getDouble(i));

                                success = db.insertOrThrow("governance_prevalence_female_circumcision_and_type",null,values);
                            }

                            db.close();
                            Log.d(TAG, "governance_prevalence_female_circumcision_and_type: " + success);

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