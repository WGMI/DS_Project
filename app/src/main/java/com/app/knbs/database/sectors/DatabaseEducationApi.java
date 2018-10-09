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

public class DatabaseEducationApi {
    private Context context;
    public DatabaseEducationApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);
    private ReportLoader loader = new ReportLoader(context);

    public void loadData(final ProgressDialog d){
        //test();
        insertInto_Approved_degree_diploma_programs(d);
        insertInto_education_studentenrollmentbysextechnicalinstitutions(d);
        insertInto_education_studentenrollmentpublicuniversities(d);
        insertInto_education_edstat_kcpe_examination_candidature(d);
        insertInto_education_edstat_kcpe_examination_results_by_subject(d);

        /*
        insertInto_education_edstat_kcse_examination_results(d);
        insertInto_education_csa_adulteducationcentresbysubcounty(d);
        insertInto_education_csa_adulteducationproficiencytestresults(d);
        insertInto_education_csa_ecdecentresbycategoryandsubcounty(d);
        insertInto_education_csa_primaryschoolenrollmentbyclasssexandsubcounty(d);

        insertInto_education_csa_youthpolytechnicsbycategoryandsubcounty(d);
        insertInto_education_csa_teachertrainingcolleges(d);
        insertInto_education_csa_studentenrolmentinyouthpolytechnics(d);
        insertInto_education_csa_secondaryschoolenrollmentbyclasssexsubcounty(d);
        */
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
                //"http://156.0.232.97:8000/education/all_diploma_degree",
                loader.getApi("Approved Degree Programmes and Validated Diploma Programmes"),
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
                                values.put("sub_county_id",countyHelper.getSubCountyId(subCounties.get(i)));
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

    private void insertInto_education_csa_adulteducationproficiencytestresults(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_adult_education_proficiency",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject countiesObject = array.getJSONObject(1);
                            JSONObject subCountiesObject = array.getJSONObject(2);
                            JSONObject satObject = array.getJSONObject(3);
                            JSONObject passedObject = array.getJSONObject(4);
                            JSONObject genderObject = array.getJSONObject(5);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countiesArray = countiesObject.getJSONArray("data");
                            JSONArray subCountiesArray = subCountiesObject.getJSONArray("data");
                            JSONArray satArray = satObject.getJSONArray("data");
                            JSONArray passedArray = passedObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_csa_adulteducationproficiencytestresults",null,null);

                            CountyHelper countyHelper = new CountyHelper();

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("adult_proficiency_id",i);
                                values.put("county_id",countyHelper.getCountyId(countiesArray.getString(i)));
                                values.put("sub_county_id",countyHelper.getSubCountyId(subCountiesArray.getString(i)));
                                values.put("no_sat",satArray.getInt(i));
                                values.put("no_passed",passedArray.getInt(i));
                                values.put("gender",genderArray.getString(i));
                                values.put("year",yearArray.getInt(i));
                                //values.put("kcse_grade",grades.get(i));
                                //values.put("sex",sexes.get(i));

                                success = db.insertOrThrow("education_csa_adulteducationproficiencytestresults",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_csa_adulteducationproficiencytestresults: " + success);
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

    private void insertInto_education_csa_ecdecentresbycategoryandsubcounty(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_ecde_centres_category_subcounty",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject countiesObject = array.getJSONObject(1);
                            JSONObject subCountiesObject = array.getJSONObject(2);
                            JSONObject centresObject = array.getJSONObject(3);
                            JSONObject categoryObject = array.getJSONObject(4);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countiesArray = countiesObject.getJSONArray("data");
                            JSONArray subCountiesArray = subCountiesObject.getJSONArray("data");
                            JSONArray centresArray = centresObject.getJSONArray("data");
                            JSONArray categoryArray = categoryObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_csa_ecdecentresbycategoryandsubcounty",null,null);

                            CountyHelper countyHelper = new CountyHelper();

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("ecde_centre_id",i);
                                values.put("county_id",countyHelper.getCountyId(countiesArray.getString(i)));
                                values.put("sub_county_id",countyHelper.getSubCountyId(subCountiesArray.getString(i)));
                                values.put("no_of_centres",centresArray.getInt(i));
                                values.put("category",categoryArray.getString(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("education_csa_ecdecentresbycategoryandsubcounty",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_csa_ecdecentresbycategoryandsubcounty: " + success);
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

    private void insertInto_education_csa_primaryschoolenrollmentbyclasssexandsubcounty(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_primary_enrollment_sex_county",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        //Log.d(TAG, "onResponse: " + array.toString());
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject countiesObject = array.getJSONObject(1);
                            JSONObject subCountiesObject = array.getJSONObject(2);
                            JSONObject oneObject = array.getJSONObject(3);
                            JSONObject twoObject = array.getJSONObject(4);
                            JSONObject threeObject = array.getJSONObject(5);
                            JSONObject fourObject = array.getJSONObject(6);
                            JSONObject fiveObject = array.getJSONObject(7);
                            JSONObject sixObject = array.getJSONObject(8);
                            JSONObject sevenObject = array.getJSONObject(9);
                            JSONObject eightObject = array.getJSONObject(10);
                            JSONObject genderObject = array.getJSONObject(11);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countiesArray = countiesObject.getJSONArray("data");
                            JSONArray subCountiesArray = subCountiesObject.getJSONArray("data");
                            JSONArray oneArray = oneObject.getJSONArray("data");
                            JSONArray twoArray = twoObject.getJSONArray("data");
                            JSONArray threeArray = threeObject.getJSONArray("data");
                            JSONArray fourArray = fourObject.getJSONArray("data");
                            JSONArray fiveArray = fiveObject.getJSONArray("data");
                            JSONArray sixArray = sixObject.getJSONArray("data");
                            JSONArray sevenArray = sevenObject.getJSONArray("data");
                            JSONArray eightArray = eightObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_csa_primaryschoolenrollmentbyclasssexandsubcounty",null,null);

                            CountyHelper countyHelper = new CountyHelper();

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("primary_enrollment_id",i);
                                values.put("county_id",countyHelper.getCountyId(countiesArray.getString(i)));
                                values.put("sub_county_id",countyHelper.getSubCountyId(subCountiesArray.getString(i)));
                                values.put("class_1",oneArray.getInt(i));
                                values.put("class_2",twoArray.getInt(i));
                                values.put("class_3",threeArray.getInt(i));
                                values.put("class_4",fourArray.getInt(i));
                                values.put("class_5",fiveArray.getInt(i));
                                values.put("class_6",sixArray.getInt(i));
                                values.put("class_7",sevenArray.getInt(i));
                                values.put("class_8",eightArray.getInt(i));
                                values.put("gender",genderArray.getString(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("education_csa_primaryschoolenrollmentbyclasssexandsubcounty",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_csa_primaryschoolenrollmentbyclasssexandsubcounty: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
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

    private void insertInto_education_csa_youthpolytechnicsbycategoryandsubcounty(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_polytechnic_category_subcounty",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject countiesObject = array.getJSONObject(1);
                            JSONObject subCountiesObject = array.getJSONObject(2);
                            JSONObject publicObject = array.getJSONObject(3);
                            JSONObject privateObject = array.getJSONObject(4);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countiesArray = countiesObject.getJSONArray("data");
                            JSONArray subCountiesArray = subCountiesObject.getJSONArray("data");
                            JSONArray publicArray = publicObject.getJSONArray("data");
                            JSONArray privateArray = privateObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_csa_youthpolytechnicsbycategoryandsubcounty",null,null);

                            CountyHelper countyHelper = new CountyHelper();

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("youth_poly_id",i);
                                values.put("county_id",countyHelper.getCountyId(countiesArray.getString(i)));
                                values.put("sub_county_id",countyHelper.getSubCountyId(subCountiesArray.getString(i)));
                                values.put("public",publicArray.getInt(i));
                                values.put("private",privateArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("education_csa_youthpolytechnicsbycategoryandsubcounty",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_csa_youthpolytechnicsbycategoryandsubcounty: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
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

    private void insertInto_education_csa_teachertrainingcolleges(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_teachertrainingcolleges",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject countiesObject = array.getJSONObject(1);
                            JSONObject catObject = array.getJSONObject(2);
                            JSONObject preprimaryObject = array.getJSONObject(3);
                            JSONObject primaryObject = array.getJSONObject(4);
                            JSONObject secondaryObject = array.getJSONObject(5);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countiesArray = countiesObject.getJSONArray("data");
                            JSONArray catArray = catObject.getJSONArray("data");
                            JSONArray preprimaryArray = preprimaryObject.getJSONArray("data");
                            JSONArray primaryArray = primaryObject.getJSONArray("data");
                            JSONArray secondaryArray = secondaryObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_csa_teachertrainingcolleges",null,null);

                            CountyHelper countyHelper = new CountyHelper();

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("teacher_colleges_id",i);
                                values.put("county_id",countyHelper.getCountyId(countiesArray.getString(i)));
                                values.put("category",catArray.getString(i));
                                values.put("pre_primary",preprimaryArray.getInt(i));
                                values.put("primary_sc",primaryArray.getInt(i));
                                values.put("secondary",secondaryArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("education_csa_teachertrainingcolleges",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_csa_teachertrainingcolleges: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
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

    private void insertInto_education_csa_studentenrolmentinyouthpolytechnics(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_student_enrollment_polytechnics",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject countiesObject = array.getJSONObject(1);
                            JSONObject subCountiesObject = array.getJSONObject(2);
                            JSONObject nameObject = array.getJSONObject(3);
                            JSONObject catObject = array.getJSONObject(4);
                            JSONObject maleObject = array.getJSONObject(5);
                            JSONObject femaleObject = array.getJSONObject(6);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countiesArray = countiesObject.getJSONArray("data");
                            JSONArray subCountiesArray = subCountiesObject.getJSONArray("data");
                            JSONArray nameArray = nameObject.getJSONArray("data");
                            JSONArray catArray = catObject.getJSONArray("data");
                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_csa_studentenrolmentinyouthpolytechnics",null,null);

                            CountyHelper countyHelper = new CountyHelper();

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("youth_poly_id",i);
                                values.put("county_id",countyHelper.getCountyId(countiesArray.getString(i)));
                                values.put("sub_county_id",countyHelper.getSubCountyId(subCountiesArray.getString(i)));
                                values.put("institution_name",nameArray.getString(i));
                                values.put("category",catArray.getString(i));
                                values.put("male",maleArray.getInt(i));
                                values.put("female",femaleArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("education_csa_studentenrolmentinyouthpolytechnics",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_csa_studentenrolmentinyouthpolytechnics: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
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

    private void insertInto_education_csa_secondaryschoolenrollmentbyclasssexsubcounty(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_education_csa_secondaryschoolenrollmentbyclasssexsubcounty",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        //Log.d(TAG, "onResponse: " + array.toString());
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject countiesObject = array.getJSONObject(1);
                            JSONObject subCountiesObject = array.getJSONObject(2);
                            JSONObject oneObject = array.getJSONObject(3);
                            JSONObject twoObject = array.getJSONObject(4);
                            JSONObject threeObject = array.getJSONObject(5);
                            JSONObject fourObject = array.getJSONObject(6);
                            JSONObject genderObject = array.getJSONObject(7);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countiesArray = countiesObject.getJSONArray("data");
                            JSONArray subCountiesArray = subCountiesObject.getJSONArray("data");
                            JSONArray oneArray = oneObject.getJSONArray("data");
                            JSONArray twoArray = twoObject.getJSONArray("data");
                            JSONArray threeArray = threeObject.getJSONArray("data");
                            JSONArray fourArray = fourObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_csa_secondaryschoolenrollmentbyclasssexsubcounty",null,null);

                            CountyHelper countyHelper = new CountyHelper();

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("enrollment_id",i);
                                values.put("county_id",countyHelper.getCountyId(countiesArray.getString(i)));
                                values.put("sub_county_id",countyHelper.getSubCountyId(subCountiesArray.getString(i)));
                                values.put("form_1",oneArray.getInt(i));
                                values.put("form_2",twoArray.getInt(i));
                                values.put("form_3",threeArray.getInt(i));
                                values.put("form_4",fourArray.getInt(i));
                                values.put("gender",genderArray.getString(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("education_csa_secondaryschoolenrollmentbyclasssexsubcounty",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_csa_secondaryschoolenrollmentbyclasssexsubcounty: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
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

    //Check table
    private void insertInto_education_csa_primaryschoolsbycategoryandsubcounty(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_primary_category_subcounty",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject countiesObject = array.getJSONObject(1);
                            JSONObject subCountiesObject = array.getJSONObject(2);
                            JSONObject schoolsObject = array.getJSONObject(3);
                            JSONObject categoryObject = array.getJSONObject(4);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray countiesArray = countiesObject.getJSONArray("data");
                            JSONArray subCountiesArray = subCountiesObject.getJSONArray("data");
                            JSONArray schoolsArray = schoolsObject.getJSONArray("data");
                            JSONArray catArray = categoryObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_csa_primaryschoolsbycategoryandsubcounty",null,null);

                            CountyHelper countyHelper = new CountyHelper();

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("primary_schools_id",i);
                                values.put("county_id",countyHelper.getCountyId(countiesArray.getString(i)));
                                values.put("sub_county_id",countyHelper.getSubCountyId(subCountiesArray.getString(i)));
                                //values.put("public",oneArray.getInt(i));
                                //values.put("private",twoArray.getInt(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("education_csa_primaryschoolsbycategoryandsubcounty",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "education_csa_primaryschoolsbycategoryandsubcounty: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
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