package com.app.knbs.database.sectors;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.knbs.adapter.model.Sector_Data;
import com.app.knbs.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 27/02/2018.
 */

public class DatabaseHealth {
    private Context mContext;
    public DatabaseHealth(Context mContext) {
        this.mContext = mContext;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(mContext);

    public List<Sector_Data> getCurrent_use_of_contraception_by_county(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT any_modem_method FROM health_current_use_of_contraception_by_county e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getDistributionofoutpatientvisitsbytypeofhealthcareprovider(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT public,private,faith_based,others FROM health_distributionofoutpatientvisitsbytypeofhealthcareprovider e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));

            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getHiv_aids_awareness_and_testing_awareness() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(hiv_awareness) FROM health_hiv_aids_awareness_and_testing ";
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

    public List<Sector_Data> getHiv_aids_awareness_and_testing(String county, String select) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT male,female FROM health_hiv_aids_awareness_and_testing e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' AND hiv_awareness='"+select+"' ";
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

    public List<Sector_Data> getInsurance_coverage_by_counties_and_types(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT insured,nhif,cbhi,private FROM health_insurance_coverage_by_counties_and_types e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2013");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));

            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getMaternal_care(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT percent_receiving_antenatal_care_from_a_skilled_provider,percent_delivered_in_a_health_facility,percent_delivered_by_a_skilled_provider FROM health_maternal_care e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));

            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getNutritional_status_of_children(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT stunted,wasted,under_weight FROM health_nutritional_status_of_children e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));

            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getNutritional_status_of_women(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT normal,overweight,obese FROM health_nutritional_status_of_women e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getRegistered_active_nhif_members_by_county(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year,formal,informal FROM health_registered_active_nhif_members_by_county e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' ";
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

    public List<String> getRegistered_medical_laboratories_by_counties_category() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT category FROM health_registered_medical_laboratories_by_counties_category ";
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

    public List<Sector_Data> getRegistered_medical_laboratories_by_counties(String county, String select) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT "+select+" FROM health_registered_medical_laboratories_by_counties e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getUse_of_mosquito_nets_by_children(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT children_under_five_years_who_slept_under_nets_last_night FROM health_use_of_mosquito_nets_by_children e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getDeaths() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,cancer,hiv_aids,malaria,pneumonia FROM health_death GROUP BY year ";
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

    public List<Sector_Data> getDiseases() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,accidents,diarrhoea,malaria,pneumonia FROM health_diseases GROUP BY year ";
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

    public List<Sector_Data> getMedical_personnel() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,clinical_officers,dentists,doctors,registered_nurse FROM health_medical_personnel GROUP BY year ";
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

    public List<Sector_Data> getNhif_members() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,formal_sector,informal_sector,total FROM health_nhif_members GROUP BY year ";
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

    public List<Sector_Data> getNhif_resources() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,benefits,contributions_net_benefits,receipts FROM health_nhif_resources GROUP BY year ";
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

    public List<String> getCountyoutpatientmorbidity_diseases() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT disease_name FROM health_diseaselist " ;
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

    public List<Sector_Data> getCountyoutpatientmorbidityabovefive(String county,String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,data FROM health_countyoutpatientmorbidityabovefive e " +
                " JOIN counties c ON e.county_id=c.county_id" +
                " JOIN health_diseaselist c ON e.disease_id=c.disease_id" +
                " WHERE county_name='"+county+"' AND disease_name='"+choice+"' " +
                " GROUP BY year,disease_name,county_name ";
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

    public List<Sector_Data> getCounty_outpatient_morbidity_below_five(String county,String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,data FROM health_county_outpatient_morbidity_below_five e " +
                " JOIN counties c ON e.county_id=c.county_id" +
                " JOIN health_diseaselist c ON e.disease_id=c.disease_id" +
                " WHERE county_name='"+county+"' AND disease_name='"+choice+"' " +
                " GROUP BY year,disease_name,county_name ";
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

    public List<Sector_Data> getHealth_facilities(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,facilities FROM health_facilities e " +
                "            JOIN counties c ON e.county_id=c.county_id" +
                "                    WHERE county_name='"+county+"' GROUP BY year,county_name ";
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

    public List<Sector_Data> getHospitalbedsandcots(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,beds,cots FROM health_hospitalbedsandcots e " +
                "            JOIN counties c ON e.county_id=c.county_id" +
                "                    WHERE county_name='"+county+"' GROUP BY year,county_name ";
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

    public List<Sector_Data> getImmunization_rate(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,rate FROM health_immunization_rate e " +
                "            JOIN counties c ON e.county_id=c.county_id" +
                "                    WHERE county_name='"+county+"' GROUP BY year,county_name ";
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

    public List<Sector_Data> getRegisteredmedicalpersonnel(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year, SUM(CASE WHEN gender='male' THEN no_of_personnel ELSE 0 END) AS 'Male' " +
                " ,SUM(CASE WHEN gender='female' THEN no_of_personnel ELSE 0 END) AS 'Female' " +
                " FROM health_registeredmedicalpersonnel e  " +
                " JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' GROUP BY year ";


        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data health = new Sector_Data();
            health.setYear(cursor.getString(0));
            health.setSet_A(cursor.getString(1));
            health.setSet_B(cursor.getString(2));
            list.add(health);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getHealthfacilitiesbyownershipofhealthfacilities_facilities() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT health_facility FROM health_healthfacilitiesbyownershipofhealthfacilities_ids ";
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

    public List<Sector_Data> getHealthfacilitiesbyownershipofhealthfacilities(String county, String select) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year,SUM(no_of_facilities) FROM health_healthfacilitiesbyownershipofhealthfacilities e " +
                "           JOIN counties c ON e.county_id=c.county_id" +
                "           JOIN health_healthfacilitiesbyownershipofhealthfacilities_ids h ON h.health_facility_id=e.health_facility_id" +
                "                    WHERE county_name='"+county+"' AND health_facility='"+select+"' GROUP BY year,county_name ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "Query"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data health = new Sector_Data();
            health.setYear(cursor.getString(0));
            health.setSet_A(cursor.getString(1));
            list.add(health);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getDiseases_For_Incidence() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> diseases = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(disease) FROM health_percentage_incidence_of_diseases_in_kenya";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            diseases.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return diseases;
    }

    public List<Sector_Data> getHealth_percentage_incidence_of_diseases_in_kenya(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select year, percentage_incidence from health_percentage_incidence_of_diseases_in_kenya where disease = '"+choice+"'";
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

    public List<String> getEducationStatuses() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> list = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(education_status) FROM health_fertility_by_education_status ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getFertility_by_Education_Status(String selection) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query =
                "SELECT year,fertility FROM " +
                        "health_fertility_by_education_status WHERE education_status='" + selection + "' group by year";
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

    public List<String> getFertilityAgeGroups() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> list = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(age_group) FROM health_fertility_rate_by_age_and_residence";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getFertility_RatebyAgeandResidence(String selection) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select year, " +
                "avg(case when residence = 'Urban' then fertility_rate else 0 end)," +
                "avg(case when residence = 'Rural' then fertility_rate else 0 end) " +
                "from health_fertility_rate_by_age_and_residence where age_group = '"+selection+"' group by year";
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

    public List<String> getMortalityStatus() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> list = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(status) FROM health_early_childhood_mortality_rates_by_sex";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getEarly_Childhood_Mortality_Rates_by_Sex(String selection) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select year, " +
                "avg(case when gender = 'Male' then mortality_rate else 0 end)," +
                "avg(case when gender = 'Female' then mortality_rate else 0 end) " +
                "from health_early_childhood_mortality_rates_by_sex where status = '"+selection+"' group by year";
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

    public List<String> getDeliveryPlaces() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> list = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(place) FROM health_place_of_delivery";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getPlace_of_Delivery(String selection) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query =
                "SELECT year,number FROM " +
                        "health_place_of_delivery WHERE place='" + selection + "' group by year";
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

    public List<String> getOverweightAgeGroups() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> list = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(age_group) FROM health_prevalence_of_overweight_and_obesity";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getPrevalence_of_Overweight_and_Obesity(String selection) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select " +
                "total " +
                "from health_prevalence_of_overweight_and_obesity where category = 'Number' and age_group = '"+selection+"'";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("As of 2015");
            data.setSet_A(cursor.getString(0));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getTobaccoCategories() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> list = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(category) FROM health_percentage_adults_who_are_current_users";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getPercentage_of_Adults_users_of_tobacco_products(String selection) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT men,women FROM health_percentage_adults_who_are_current_users " +
                "WHERE age_group='Total' AND category='"+selection+"' ";
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

    public List<String> getDrinkerAgeGroups() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> list = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(age) FROM health_percentage_who_drink_alcohol_by_age";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getPercentage_of_Adults_who_drink_alcohol(String selection) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT men,women FROM health_percentage_who_drink_alcohol_by_age " +
                "WHERE age='"+selection+"'";
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

    public List<String> getSupplements() {
        List<String> list = new ArrayList<>();
        list.add("zinc_solution");
        list.add("sugar_salt_solution");
        list.add("other_solutions");
        list.add("none");
        list.add("not_stated");
        return list;
    }

    public List<Sector_Data> getkihibs_children_by_additional_supplement(String county,String selection) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT avg("+selection+") FROM health_kihibs_children_by_additional_supplement e" +
                " JOIN counties c ON e.county_id=c.county_id " +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));

            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getAssistant() {
        List<String> list = new ArrayList<>();
        list.add("doctor");
        list.add("midwife_nurse");
        list.add("tba");
        list.add("ttba");
        list.add("self");
        list.add("other");
        list.add("not_stated");
        return list;
    }

    public List<Sector_Data> getKihibs_children_assisted_at_birth(String county,String selection) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT avg("+selection+") FROM health_kihibs_children_by_who_assisted_at_birth e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));

            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getMeaslesCategory() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> list = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(measles_vaccination) FROM health_kihibs_children_immmunized_against_measles";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            list.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return list;
    }


    public List<Sector_Data> getKihibs_children_immunized(String county, String selection) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT proportion FROM health_kihibs_children_immmunized_against_measles e" +
                " JOIN counties c ON e.county_id=c.county_id " +
                "WHERE county_name='"+county+"' AND measles_vaccination='"+selection+"' GROUP BY county_name ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "Query"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data health = new Sector_Data();
            health.setYear("2014");
            health.setSet_A(cursor.getString(0));
            list.add(health);
        }
        cursor.close();
        db.close();
        return list;
    }

}
