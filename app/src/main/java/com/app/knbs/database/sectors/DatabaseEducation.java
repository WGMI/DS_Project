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
import com.android.volley.toolbox.Volley;
import com.app.knbs.adapter.model.Sector_Data;
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

public class DatabaseEducation {
    private Context mContext;
    public DatabaseEducation(Context mContext) {
        this.mContext = mContext;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(mContext);

    public List<Sector_Data> getYouthpolytechnicsbycategoryandsubcounty(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,public,private FROM education_csa_youthpolytechnicsbycategoryandsubcounty e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getTeachertraineesprivateenrolment() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        //choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);
        //String query = "SELECT year,number FROM education_es_teachertraineesprivateenrolment WHERE gender='"+choice+"' GROUP BY year ";

        String query = "SELECT year, SUM(CASE WHEN gender='Male' THEN number ELSE 0 END) AS Male, SUM(CASE WHEN gender='Female' THEN number ELSE 0 END) AS Female " +
                "FROM education_es_teachertraineesprivateenrolment GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getEducationalinstitutions_publictivet_institutions() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(institution) FROM education_es_educationalinstitutions_publictivet ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getEducationalinstitutions_publictivet(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year,number FROM education_es_educationalinstitutions_publictivet WHERE institution='"+choice+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getEducationalinstitutions_schools_institutions() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(institution) FROM education_es_educationalinstitutions_schools ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getEducationalinstitutions_schools(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);

        String query = "SELECT year, SUM(CASE WHEN institution='Pre-Primary' THEN number ELSE 0 END) AS 'Pre-Primary' " +
                " ,SUM(CASE WHEN institution='Primary' THEN number ELSE 0 END) AS 'Primary' " +
                " ,SUM(CASE WHEN institution='Secondary' THEN number ELSE 0 END) AS Secondary " +
                " FROM education_es_educationalinstitutions_schools WHERE category='"+choice+"' GROUP BY year ";

        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            data.setSet_C(cursor.getString(3));

            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getKcpecandidatesandmeansubjectscore_candidates() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year, SUM(CASE WHEN gender='Male' THEN number ELSE 0 END) AS Male, SUM(CASE WHEN gender='Female' THEN number ELSE 0 END) AS Female " +
                "FROM education_es_kcpecandidatesandmeansubjectscore_candidates GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getKcpecandidatesandmeansubjectscore_subject_subject() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(subject) FROM education_es_kcpecandidatesandmeansubjectscore_subject ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getKcpecandidatesandmeansubjectscore_subject( String select) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year,mean_score FROM education_es_kcpecandidatesandmeansubjectscore_subject WHERE subject='"+select+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getNationalgovtdevelopmentandrecurrentexpenditure_expenditure() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(expenditure) FROM education_es_nationalgovtdevelopmentandrecurrentexpenditure ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getNationalgovtdevelopmentandrecurrentexpenditure(String select) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year, SUM(CASE WHEN expenditure_type='Development Expenditure' THEN amount ELSE 0 END) AS 'Development Expenditure'" +
                ", SUM(CASE WHEN expenditure_type='Recurrent Expenditure' THEN amount ELSE 0 END) AS 'Recurrent Expenditure' " +
                "FROM education_es_nationalgovtdevelopmentandrecurrentexpenditure WHERE expenditure='"+select+"' GROUP BY year ";

        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getPrivatecandidatesregisteredforkcpebysex(String county,String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);
        String query = "SELECT proficiency,kcpe FROM education_es_privatecandidatesregisteredforkcpebysex e " +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' AND gender='"+choice+"' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getPublicsecondaryschooltrainedteachers_teachers() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(teachers) FROM education_es_publicsecondaryschooltrainedteachers ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getPublicsecondaryschooltrainedteachers(String select) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year, SUM(CASE WHEN gender='Male' THEN number ELSE 0 END) AS Male, SUM(CASE WHEN gender='Female' THEN number ELSE 0 END) AS Female " +
                "FROM education_es_publicsecondaryschooltrainedteachers WHERE teachers='"+select+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getPublicsecondaryschooluntrainedteachers_teachers() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(teachers) FROM education_es_publicsecondaryschooluntrainedteachers ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getPublicsecondaryschooluntrainedteachers( String select) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year, SUM(CASE WHEN gender='Male' THEN number ELSE 0 END) AS Male, SUM(CASE WHEN gender='Female' THEN number ELSE 0 END) AS Female " +
                "FROM education_es_publicsecondaryschooluntrainedteachers WHERE teachers='"+select+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getTeachertraineesdiplomaenrolments_year() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(diploma_year) FROM education_es_teachertraineesdiplomaenrolment ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getTeachertraineesdiplomaenrolment(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        //choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);
        //String query = "SELECT year,number FROM education_es_teachertraineesdiplomaenrolment WHERE diploma_year='"+select+"' AND gender='"+choice+"' GROUP BY year ";

        String query = "SELECT year, SUM(CASE WHEN gender='Male' THEN number ELSE 0 END) AS Male, SUM(CASE WHEN gender='Female' THEN number ELSE 0 END) AS Female " +
                "FROM education_es_teachertraineesdiplomaenrolment WHERE diploma_year='"+choice+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getTeachertraineespublicenrolment_year() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(primary_year) FROM education_es_teachertraineespublicenrolment ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getTeachertraineespublicenrolment(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        //choice = choice.toLowerCase();
        //String query = "SELECT year,number FROM education_es_teachertraineespublicenrolment WHERE primary_year='"+select+"' AND gender='"+choice+"' GROUP BY year ";

        String query = "SELECT year, SUM(CASE WHEN gender='male' THEN number ELSE 0 END) AS Male, SUM(CASE WHEN gender='female' THEN number ELSE 0 END) AS Female " +
                "FROM education_es_teachertraineespublicenrolment WHERE primary_year='"+choice+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getApproved_degree_diploma_programs() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,approved_degree_programmes,approved_private_university_degreeprogrammes,validated_diploma_programmes FROM education_approved_degree_diploma_programs GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            data.setSet_C(cursor.getString(3));
            list.add(data);
        }
        Log.d(TAG, "getApproved_degree_diploma_programs: " + cursor.getCount());
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getEnrollmentsecondaryschoolsbylevelandsex(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query;
        if(choice.matches("All")) {
            query = "SELECT year,SUM(boys) AS boys ,SUM(girls) AS girls FROM education_enrollmentsecondaryschoolsbylevelandsex GROUP BY year ";
        }else {
            query = "SELECT year,boys,girls FROM education_enrollmentsecondaryschoolsbylevelandsex WHERE level='" + choice + "' GROUP BY year ";
        }
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getNationaltrendskcsecandidatesmeangradebysex_grades() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT grade FROM education_grade ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getNationaltrendskcsecandidatesmeangradebysex(String choice,String selection) {
        choice =  choice.toLowerCase();
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String select = "a_plain,a_minus,e_plain";
        if(selection.matches("Grade A and E")){
            select = "a_plain,a_minus,e_plain";
        }else if(selection.matches("Grade B")){
            select = "b_plus,b_plain,b_minus";
        }else if(selection.matches("Grade C")){
            select = "c_plus,b_plain,b_minus";
        }else if(selection.matches("Grade D")){
            select = "d_plus,d_plain,d_minus";
        }

        String query = "SELECT year,"+select+" FROM education_nationaltrendskcsecandidatesmeangradebysex WHERE gender='"+choice+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            data.setSet_C(cursor.getString(3));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getNumber_educational_institutions() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,schools_primary_secondary,teacher_training_colleges,tivet_institutions,universities FROM education_number_educational_institutions GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            data.setSet_C(cursor.getString(3));
            data.setSet_D(cursor.getString(4));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getStudentenrollmentbysextechnicalinstitutions_institution() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(institution) FROM education_studentenrollmentbysextechnicalinstitutions ";
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getStudentenrollmentbysextechnicalinstitutions(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male,female FROM education_studentenrollmentbysextechnicalinstitutions WHERE institution='"+choice+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getStudentenrollmentpublicuniversities() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,undergraduates,postgraduates,other FROM education_studentenrollmentpublicuniversities GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            data.setSet_C(cursor.getString(3));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getAdulteducationcentresbysubcounty(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,sum(centres) FROM education_csa_adulteducationcentresbysubcounty e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"'  GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getAdulteducationenrolmentbysexandsubcounty(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male,female FROM education_csa_adulteducationenrolmentbysexandsubcounty e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"'  GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getAdulteducationproficiencytestresults(String county, String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        choice = choice.toLowerCase();
        String query = "SELECT year,no_sat,no_passed FROM education_csa_adulteducationproficiencytestresults e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' AND gender='"+choice+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getEcdecentresbycategoryandsubcounty(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year, SUM(CASE WHEN category='public' THEN no_of_centres ELSE 0 END) AS 'Public' " +
                " ,SUM(CASE WHEN category='private' THEN no_of_centres ELSE 0 END) AS 'Private' " +
                " FROM education_csa_ecdecentresbycategoryandsubcounty e  " +
                " JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' GROUP BY year ";

        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getPrimaryenrolmentandaccessindicators(String county, String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        choice = choice.toLowerCase();
        //String query = "SELECT year,"+choice+" FROM education_csa_primaryenrolmentandaccessindicators e " +
        //        "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' AND gender='"+choice+"' GROUP BY year ";

        String query = "SELECT year, SUM(CASE WHEN gender='male' THEN "+choice+" ELSE 0 END) AS Male, SUM(CASE WHEN gender='female' THEN "+choice+"  ELSE 0 END) AS Female " +
                "FROM education_csa_primaryenrolmentandaccessindicators e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' " +
                " GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getSecondaryschoolenrollmentbyclasssexsubcounty(String county, String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        choice = choice.toLowerCase();
        String query = "SELECT year,form_1,form_2,form_3,form_4 FROM education_csa_secondaryschoolenrollmentbyclasssexsubcounty e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' AND gender='"+choice+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            data.setSet_C(cursor.getString(3));
            data.setSet_D(cursor.getString(4));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getSecondaryschoolsbycategoryandsubcounty(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,public,private FROM education_csa_secondaryschoolsbycategoryandsubcounty e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getStudentenrolmentinyouthpolytechnics_institutions() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(institution_name) FROM education_csa_studentenrolmentinyouthpolytechnics ORDER BY institution_name ASC";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getStudentenrolmentinyouthpolytechnics(String county,String choice, String select) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);
        String query = "SELECT year,male,female FROM education_csa_studentenrolmentinyouthpolytechnics e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' AND institution_name='"+select+"' AND category='"+choice+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getTotalsecondaryschoolenrollmentbyyear(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,number_of_students FROM education_totalsecondaryschoolenrollmentbyyear e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getPrimaryschoolenrollmentbyclasssexandsubcounty(String county, String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        choice = choice.toLowerCase();
        String query = "SELECT year,(SUM(class_1)+SUM(class_2)+SUM(class_3)+SUM(class_4)) AS lower_class,(SUM(class_5)+SUM(class_6)+SUM(class_7)+SUM(class_8)) AS upper_class" +
                " FROM `education_csa_primaryschoolenrollmentbyclasssexandsubcounty` e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' AND gender='"+choice+"' GROUP BY gender ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data education = new Sector_Data();
            education.setYear(cursor.getString(0));
            education.setSet_A(cursor.getString(1));
            education.setSet_B(cursor.getString(2));
            list.add(education);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getSecondaryenrolmentandaccessindicators(String county, String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        choice = choice.toLowerCase();
        //String query = "SELECT year,enrolment,ger,ner FROM `education_csa_secondaryenrolmentandaccessindicators` e " +
        //        "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' AND gender='"+choice+"' GROUP BY gender ";

        String query = "SELECT year, SUM(CASE WHEN gender='male' THEN "+choice+" ELSE 0 END) AS Male, SUM(CASE WHEN gender='female' THEN "+choice+"  ELSE 0 END) AS Female " +
                "FROM education_csa_secondaryenrolmentandaccessindicators e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' " +
                " GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data education = new Sector_Data();
            education.setYear(cursor.getString(0));
            education.setSet_A(cursor.getString(1));
            education.setSet_B(cursor.getString(2));
            list.add(education);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getPrimaryschoolsbycategoryandsubcounty(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,SUM(public),SUM(private) FROM education_csa_primaryschoolsbycategoryandsubcounty e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data education = new Sector_Data();
            education.setYear(cursor.getString(0));
            education.setSet_A(cursor.getString(1));
            education.setSet_B(cursor.getString(2));
            list.add(education);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getTeachertrainingcolleges(String county,String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,pre_primary,primary_sc,secondary FROM education_csa_teachertrainingcolleges e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' AND category='"+choice+"' GROUP BY year,category,county_name ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data education = new Sector_Data();
            education.setYear(cursor.getString(0));
            education.setSet_A(cursor.getString(1));
            education.setSet_B(cursor.getString(2));
            education.setSet_C(cursor.getString(3));
            list.add(education);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getKCPE_Examination_Candidature() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,KCPE_candidature from education_edstat_kcpe_examination_candidature where gender = 'Total'";

        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getSubjects() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "select distinct(subject) from education_edstat_kcpe_examination_results_by_subject";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getKCPE_Examination_Results_By_Subject(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select year,kcpe_result from education_edstat_kcpe_examination_results_by_subject where subject = '"+choice+"'";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getKCSE_Grades() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(kcse_grade) FROM education_edstat_kcse_examination_results ORDER BY kcse_grade ASC";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getKCSE_Examination_Results(String choice, String select) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);
        String query = "select year, number_of_candidates from education_edstat_kcse_examination_results where kcse_grade = '"+choice+"' and sex = '"+select+"'";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public void insertInto_Approved_degree_diploma_programs(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(mContext).getQueue();
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

                            Log.d("test_response", "onResponse: " + years.get(0) + degrees.get(0) + privateDegrees.get(0) + validatedDiplomas.get(0));
                            Log.d("test_insert_count", "onResponse: " + success);
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

        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }
}