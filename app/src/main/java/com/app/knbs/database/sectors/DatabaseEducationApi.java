package com.app.knbs.database.sectors;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.app.knbs.adapter.model.Sector_Data;
import com.app.knbs.database.CountyHelper;
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

public class DatabaseEducationApi {
    private Context context;
    public DatabaseEducationApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);

    public void loadData(final ProgressDialog d){
        //test();
        insertInto_Approved_degree_diploma_programs(d);
        insertInto_education_studentenrollmentbysextechnicalinstitutions(d);
        insertInto_education_studentenrollmentpublicuniversities(d);
        insertInto_education_edstat_kcpe_examination_candidature(d);
        insertInto_education_edstat_kcpe_examination_results_by_subject(d);
        insertInto_education_edstat_kcse_examination_results(d);
        insertInto_education_csa_adulteducationcentresbysubcounty(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_Approved_degree_diploma_programs(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_diploma_degree",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject approvedDegreesObject  = response.getJSONObject(1);
                            JSONObject approvedPrivateDegreesObject  = response.getJSONObject(2);
                            JSONObject validatedDiplomasObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray approvedDegreesArray = approvedDegreesObject.getJSONArray("data");
                            JSONArray approvedPrivateDegreesArray = approvedPrivateDegreesObject.getJSONArray("data");
                            JSONArray validatedDiplomasArray = validatedDiplomasObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<Integer> degrees = new ArrayList<>();
                            List<Integer> privateDegrees = new ArrayList<>();
                            List<Integer> validatedDiplomas = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                degrees.add(approvedDegreesArray.getInt(i));
                                privateDegrees.add(approvedPrivateDegreesArray.getInt(i));
                                validatedDiplomas.add(validatedDiplomasArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_approved_degree_diploma_programs",null,null);

                            for(int i=0;i<yearArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("year",years.get(i));
                                values.put("approved_degree_programmes",degrees.get(i));
                                values.put("approved_private_university_degreeprogrammes",privateDegrees.get(i));
                                values.put("validated_diploma_programmes",validatedDiplomas.get(i));

                                success = db.insertOrThrow("education_approved_degree_diploma_programs",null,values);
                            }

                            db.close();
                            Log.d(TAG, "education_approved_degree_diploma_programs: " + success);

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

    private void insertInto_education_studentenrollmentbysextechnicalinstitutions(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_student_enrollment_sex",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject institutionObject = array.getJSONObject(1);
                            JSONObject maleObject = array.getJSONObject(2);
                            JSONObject femaleObject = array.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray institutionArray = institutionObject.getJSONArray("data");
                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<String> institutions = new ArrayList<>();
                            List<Integer> maleValues = new ArrayList<>();
                            List<Integer> femaleValues = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                institutions.add(institutionArray.getString(i));
                                maleValues.add(maleArray.getInt(i));
                                femaleValues.add(femaleArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_studentenrollmentbysextechnicalinstitutions",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("student_enrollment_id",i);
                                values.put("institution",institutions.get(i));
                                values.put("year",years.get(i));
                                values.put("male",maleValues.get(i));
                                values.put("female",femaleValues.get(i));

                                success = db.insertOrThrow("education_studentenrollmentbysextechnicalinstitutions",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_studentenrollmentbysextechnicalinstitutions: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("test_error", "onResponse: " + volleyError.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_education_studentenrollmentpublicuniversities(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_student_enrollment_public_universities",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject undergraduatesObject = array.getJSONObject(1);
                            JSONObject postgraduatesObject = array.getJSONObject(2);
                            JSONObject otherObject = array.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray undergraduatesArray = undergraduatesObject.getJSONArray("data");
                            JSONArray postgraduatesArray = postgraduatesObject.getJSONArray("data");
                            JSONArray otherArray = otherObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<Integer> undergraduates = new ArrayList<>();
                            List<Integer> postgraduates = new ArrayList<>();
                            List<Integer> others = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                undergraduates.add(undergraduatesArray.getInt(i));
                                postgraduates.add(postgraduatesArray.getInt(i));
                                others.add(otherArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_studentenrollmentpublicuniversities",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("student_enrollment_id",i);
                                values.put("year",years.get(i));
                                values.put("undergraduates",undergraduates.get(i));
                                values.put("postgraduates",postgraduates.get(i));
                                values.put("other",others.get(i));

                                success = db.insertOrThrow("education_studentenrollmentpublicuniversities",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_studentenrollmentpublicuniversities: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("test_error", "onResponse: " + volleyError.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_education_edstat_kcpe_examination_candidature(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_edstat_kcpe_examination_candidature",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject candidatureObject = array.getJSONObject(1);
                            JSONObject genderObject = array.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray candidatureArray = candidatureObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<Integer> candidatures = new ArrayList<>();
                            List<String> genders = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                candidatures.add(candidatureArray.getInt(i));
                                genders.add(genderArray.getString(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_edstat_kcpe_examination_candidature",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("candidature_id",i);
                                values.put("year",years.get(i));
                                values.put("kcpe_candidature",candidatures.get(i));
                                values.put("gender",genders.get(i));

                                success = db.insertOrThrow("education_edstat_kcpe_examination_candidature",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_edstat_kcpe_examination_candidature: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("test_error", "onResponse: " + volleyError.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_education_edstat_kcpe_examination_results_by_subject(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_edstat_kcpe_examination_candidature",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject resultObject = array.getJSONObject(1);
                            JSONObject subjectObject = array.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray resultArray = resultObject.getJSONArray("data");
                            JSONArray subjectArray = subjectObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<Integer> results = new ArrayList<>();
                            List<String> subjects = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                results.add(resultArray.getInt(i));
                                subjects.add(subjectArray.getString(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_edstat_kcpe_examination_results_by_subject",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("kcpe_result_id",i);
                                values.put("year",years.get(i));
                                values.put("kcpe_result",results.get(i));
                                values.put("subject",subjects.get(i));

                                success = db.insertOrThrow("education_edstat_kcpe_examination_results_by_subject",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_edstat_kcpe_examination_results_by_subject: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("test_error", "onResponse: " + volleyError.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_education_edstat_kcse_examination_results(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_edstat_kcse_examination_results",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject candidatesObject = array.getJSONObject(1);
                            JSONObject gradeObject = array.getJSONObject(2);
                            JSONObject sexObject = array.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray candidatesArray = candidatesObject.getJSONArray("data");
                            JSONArray gradeArray = gradeObject.getJSONArray("data");
                            JSONArray sexArray = sexObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<Integer> candidateValues = new ArrayList<>();
                            List<String> grades = new ArrayList<>();
                            List<String> sexes = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                candidateValues.add(candidatesArray.getInt(i));
                                grades.add(gradeArray.getString(i));
                                sexes.add(sexArray.getString(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_edstat_kcse_examination_results",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("kcse_result_id",i);
                                values.put("number_of_candidates",candidateValues.get(i));
                                values.put("year",years.get(i));
                                values.put("kcse_grade",grades.get(i));
                                values.put("sex",sexes.get(i));

                                success = db.insertOrThrow("education_edstat_kcse_examination_results",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_edstat_kcse_examination_results: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("test_error", "onResponse: " + volleyError.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

    private void insertInto_education_csa_adulteducationcentresbysubcounty(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_adult_edu_centres_subcounty",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject countiesObject = array.getJSONObject(1);
                            JSONObject subCountiesObject = array.getJSONObject(2);
                            JSONObject centresObject = array.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countiesArray = countiesObject.getJSONArray("data");
                            JSONArray subCountiesArray = subCountiesObject.getJSONArray("data");
                            JSONArray centresArray = centresObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<String> counties = new ArrayList<>();
                            List<String> subCounties = new ArrayList<>();
                            List<Integer> centres = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                counties.add(countiesArray.getString(i));
                                subCounties.add(subCountiesArray.getString(i));
                                centres.add(centresArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_csa_adulteducationcentresbysubcounty",null,null);

                            CountyHelper countyHelper = new CountyHelper();

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("adult_centre_id",i);
                                values.put("county_id",countyHelper.getCountyId(counties.get(i)));
                                values.put("sub_county_id",countyHelper.getSubCountyId(counties.get(i)));
                                values.put("year",years.get(i));
                                values.put("centres",centres.get(i));
                                //values.put("kcse_grade",grades.get(i));
                                //values.put("sex",sexes.get(i));

                                success = db.insertOrThrow("education_csa_adulteducationcentresbysubcounty",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_csa_adulteducationcentresbysubcounty: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("test_error", "onResponse: " + volleyError.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

}