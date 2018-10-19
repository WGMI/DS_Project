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
 * Developed by Rodney on 28/02/2018.
 */

public class DatabaseGovernance {
    private Context mContext;
    public DatabaseGovernance(Context mContext) {
        this.mContext = mContext;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(mContext);

    public List<String> getCases_forwarded_and_action_taken_action() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(action_taken) FROM governance_cases_forwarded_and_action_taken ";
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

    public List<Sector_Data> getCases_forwarded_and_action_taken(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,no_of_recommendations FROM governance_cases_forwarded_and_action_taken WHERE action_taken='"+choice+"' GROUP BY year,action_taken ";
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

    public List<String> getCases_handled_by_ethics_commision_action() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(action) FROM governance_cases_handled_by_ethics_commision ";
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

    public List<Sector_Data> getCases_handled_by_ethics_commision(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,no_cases FROM governance_cases_handled_by_ethics_commision WHERE action='"+choice+"' GROUP BY year ";
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

    public List<String> getCases_handled_by_various_courts_category() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(court) FROM governance_cases_handled_by_various_courts ";
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

    public List<String> getCases_handled_by_different_courts_category() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(category) FROM governance_cases_handled_by_different_courts ";
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

    public List<Sector_Data> getCases_handled_by_various_courts(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,filled,pending,disposed_of FROM governance_cases_handled_by_various_courts WHERE court='"+choice+"' GROUP BY year ";
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

    public List<Sector_Data> getCases_handled_by_different_courts(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select " +
                "year," +
                "magistrate_court,high_court," +
                "court_of_appeal,supreme_court " +
                "from governance_cases_handled_by_different_courts " +
                "where category = '"+choice+"' " +
                "GROUP BY year ";
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

    public List<String> getConvicted_prisoners_by_type_of_offence_and_sex_category() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(offence) FROM governance_convicted_prisoners_by_type_of_offence_and_sex ";
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

    public List<Sector_Data> getConvicted_prisoners_by_type_of_offence_and_sex(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male,female FROM governance_convicted_prisoners_by_type_of_offence_and_sex WHERE offence='"+choice+"' GROUP BY year,offence ";
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

    public List<String> getDaily_average_population_of_prisoners_by_sex_category() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(category) FROM governance_daily_average_population_of_prisoners_by_sex ";
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

    public List<Sector_Data> getDaily_average_population_of_prisoners_by_sex(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male,female FROM governance_daily_average_population_of_prisoners_by_sex WHERE category='"+choice+"' GROUP BY year,category ";
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

    public List<String> getEnvironmental_crimes_reported_to_nema_category() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(type_of_case) FROM governance_environmental_crimes_reported_to_nema ";
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

    public List<Sector_Data> getEnvironmental_crimes_reported_to_nema(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,no_of_cases FROM governance_environmental_crimes_reported_to_nema WHERE type_of_case='"+choice+"' GROUP BY year ";
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

    public List<String> getFirearms_and_ammunition_recovered_or_surrendered_category() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(category) FROM governance_firearms_and_ammunition_recovered_or_surrendered ";
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

    public List<Sector_Data> getFirearms_and_ammunition_recovered_or_surrendered(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,recovered,surrendered FROM governance_firearms_and_ammunition_recovered_or_surrendered WHERE category='"+choice+"' GROUP BY year,category ";
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

    public List<Sector_Data> getIdentity_cards_made_processed_and_collected(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,npr_apps_made,npr_ids_prod,npr_ids_collected FROM governance_identity_cards_made_processed_and_collected e " +
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
            data.setSet_C(cursor.getString(3));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getMagistrates_judges_and_practicing_lawyers_category() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(category) FROM governance_magistrates_judges_and_practicing_lawyers ";
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

    public List<Sector_Data> getMagistrates_judges_and_practicing_lawyers(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male,female FROM governance_magistrates_judges_and_practicing_lawyers WHERE category='"+choice+"' GROUP BY year,category ";
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

    public List<String> getMurder_cases_and_convictions_obtained_by_high_court_stations() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(court_station) FROM governance_murder_cases_and_convictions_obtained_by_high_court_s ";
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

    public List<Sector_Data> getMurder_cases_and_convictions_obtained_by_high_court_s(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,registered_murder_cases,murder_convictions_obtained FROM governance_murder_cases_and_convictions_obtained_by_high_court_s WHERE court_station='"+choice+"' GROUP BY year,court_station ";
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

    public List<String> getNumber_of_police_prisons_and_probation_officers_category() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(category) FROM governance_number_of_police_prisons_and_probation_officers ";
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

    public List<Sector_Data> getNumber_of_police_prisons_and_probation_officers(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male + female FROM governance_number_of_police_prisons_and_probation_officers WHERE category='"+choice+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            //data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getOffences_committed_against_morality_category() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(category_id) FROM governance_offences_committed_against_morality ";
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

    public List<Sector_Data> getOffences_committed_against_morality(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male,female FROM governance_offences_committed_against_morality WHERE category_id='"+choice+"' GROUP BY year,category_id ";
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

    public List<String> getOffenders_serving_offence() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(offence) FROM governance_offenders_serving ";
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

    public List<Sector_Data> getOffenders_serving(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male,female FROM governance_offenders_serving WHERE offence='"+choice+"' GROUP BY year,offence ";
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

    public List<Sector_Data> getPassports_work_permits_and_foreigners_registered() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,passport_issued,foreign_nat_reg,work_permit_issued,work_permit_ren FROM governance_passports_work_permits_and_foreigners_registered GROUP BY year ";
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

    public List<String> getPeople_reported_to_have_committed_offence_related_to_drugs_offence() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> offence = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(offence) FROM governance_people_reported_to_have_committed_offence_related_to_ ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            offence.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return offence;
    }

    public List<Sector_Data> getPeople_reported_to_have_committed_offence_related_to_(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male,female FROM governance_people_reported_to_have_committed_offence_related_to_ WHERE offence='"+choice+"' GROUP BY year,offence ";
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

    public List<String> getPersons_reported_to_have_committed_homicide_by_sex_offence() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> offence = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(offence) FROM governance_persons_reported_to_have_committed_homicide_by_sex ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            offence.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return offence;
    }

    public List<Sector_Data> getPersons_reported_to_have_committed_homicide_by_sex(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male,female FROM governance_persons_reported_to_have_committed_homicide_by_sex WHERE offence='"+choice+"' GROUP BY year,offence ";
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

    public List<String> getPersons_reported_to_have_committed_robbery_and_theft_offence() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> offence = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(offence) FROM governance_persons_reported_to_have_committed_robbery_and_theft ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            offence.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return offence;
    }

    public List<Sector_Data> getPersons_reported_to_have_committed_robbery_and_theft(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male,female FROM governance_persons_reported_to_have_committed_robbery_and_theft WHERE offence ='"+choice+"' GROUP BY year,offence ";
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

    public List<String> getPrison_population_by_sentence_duration_and_sex_category() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(category) FROM governance_prison_population_by_sentence_duration_and_sex ";
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

    public List<Sector_Data> getPrison_population_by_sentence_duration_and_sex(String select) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male,female FROM governance_prison_population_by_sentence_duration_and_sex WHERE category='"+select+"' GROUP BY year,female,male,category ";
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

    public List<Sector_Data> getPublic_assets_traced_recovered_and_loss_averted() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,public_assets_traced,public_assets_recovered,loss_averted FROM governance_public_assets_traced_recovered_and_loss_averted GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getConvicted_prison_population_by_age_and_sex_category() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(category) FROM governance_convicted_prison_population_by_age_and_sex ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getConvicted_prison_population_by_age_and_sex(String select) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year,male,female FROM governance_convicted_prison_population_by_age_and_sex WHERE category='"+select+"' GROUP BY year ";
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

    public List<Sector_Data> getNumber_of_refugees_by_age_and_sex(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);
        String query = "SELECT year,children,adult FROM governance_number_of_refugees_by_age_and_sex WHERE gender='"+choice+"' GROUP BY year ";
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

    public List<Sector_Data> getCrimes_reported_to_police_by_command_stations(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,crimes FROM governance_crimes_reported_to_police_by_command_stations e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' GROUP BY year,county_name ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data governance = new Sector_Data();
            governance.setYear(cursor.getString(0));
            governance.setSet_A(cursor.getString(1));
            list.add(governance);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getRegistered_voters_by_county_and_by_sex(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        //choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);
        //String query = "SELECT reg_voters FROM governance_registered_voters_by_county_and_by_sex e " +
        //        "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' AND gender='"+choice+"' GROUP BY county_name ";

        String query = "SELECT  SUM(CASE WHEN gender LIKE 'Male' THEN reg_voters ELSE 0 END) AS Male, SUM(CASE WHEN gender='Female' THEN reg_voters ELSE 0 END) AS Female " +
                ", SUM(CASE WHEN gender='Total' THEN reg_voters ELSE 0 END) AS Total FROM governance_registered_voters_by_county_and_by_sex e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"'";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data education = new Sector_Data();
            education.setYear("2014");
            education.setSet_A(cursor.getString(0));
            education.setSet_B(cursor.getString(1));
            education.setSet_C(cursor.getString(2));
            list.add(education);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getOffence_by_sex_and_command_stations(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,male,female FROM governance_offence_by_sex_and_command_stations e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' GROUP BY year,county_name ";
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

    public List<String> getDecision_making_positions() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> offence = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(category) FROM governance_participation_in_key_decision_making_positions_by_sex ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            offence.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return offence;
    }

    public List<Sector_Data> getParticipation_in_Key_Decision_Making_Positions_by_Sex(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        //String query = "SELECT year,male,female FROM governance_persons_reported_to_have_committed_homicide_by_sex WHERE offence='"+choice+"' GROUP BY year,offence ";
        String query = "select year,male,female from governance_participation_in_key_decision_making_positions_by_sex where category ='"+choice+"'";
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

    public List<Sector_Data> getWomen_Groups_Registration_Contribution_Women_Enterprise_Fund() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select year,no_of_beneficiaries,women_enterprise_fund from governance_women_groups_registration_cont_women_enterprise_fund";
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

    public List<Sector_Data> getWomen_Groups_Registration_Contributions_Uwezo_Funds() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select year,no_of_beneficiaries,uwezo_fund_disbursed from governance_women_groups_registration_contributions_uwezo_funds";
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

    public List<Sector_Data> getWomen_Groups_Registration_Contributions_Women_Groups() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select year,number,membership,group_contributions from governance_women_groups_registration_contributions_women_groups";
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

    public List<String> getDomesticViolenceAgeGroups() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> offence = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(age) FROM governance_experienceof_domestic_violence_by_age ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            offence.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return offence;
    }

    public List<Sector_Data> getExperience_of_Domestic_Violence_By_Age(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select sum(percentage_experienced_physical_violence)," +
                "sum(percentage_experienced_sexual_violence)," +
                "sum(percentage_experienced_physical_and_sexual_violence) " +
                "from governance_experienceof_domestic_violence_by_age where age ='"+choice+"'";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("Untimed");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getDomesticViolenceMaritalStatus() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> offence = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(marital_status) FROM governance_experienceof_domestic_violence_by_marital_success ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            offence.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return offence;
    }

    public List<Sector_Data> getExperience_of_Domestic_Violence_By_Marital_Success(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select sum(percentage_experienced_physical_violence)," +
                "sum(percentage_experienced_sexual_violence)," +
                "sum(percentage_experienced_physical_and_sexual_violence) " +
                "from governance_experienceof_domestic_violence_by_marital_success where marital_status ='"+choice+"'";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("Untimed");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getDomesticViolenceResidences() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> offence = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(residence) FROM governance_experienceof_domestic_violence_by_residence ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            offence.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return offence;
    }

    public List<Sector_Data> getExperience_of_Domestic_Violence_By_Residence(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select sum(percentage_experienced_physical_violence)," +
                "sum(percentage_experienced_sexual_violence)," +
                "sum(percentage_experienced_physical_and_sexual_violence) " +
                "from governance_experienceof_domestic_violence_by_residence where residence ='"+choice+"'";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("Untimed");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getCircumcisionKnowledgeAgeGroups() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> list = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(age) FROM governance_knowledge_and_prevalence_of_female_circumcision ";
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

    public List<Sector_Data> getKnowledge_and_Prevalence_of_Female_Circumcision(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select AVG(percentage_women_heard_of_FC)," +
                "AVG(percentage_women_not_heard_of_FC) " +
                "from governance_knowledge_and_prevalence_of_female_circumcision where age ='"+choice+"'";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("Average as of 2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getMNACategories() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> list = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(status) FROM governance_members_of_nationalassembly_and_senators ";
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

    public List<Sector_Data> getMembers_of_National_Assembly_and_Senators(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select men,women " +
                "from governance_members_of_nationalassembly_and_senators where status ='"+choice+"'";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("As of 2015");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getPersons_Reported_to_have_Committed_Defilement() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select year, " +
                "sum(case when gender = 'male' then number else 0 end)," +
                "sum(case when gender = 'female' then number else 0 end) " +
                "from governance_persons_reported_tohave_committed_defilement group by year";
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

    public List<Sector_Data> getPersons_Reported_to_have_Committed_Rape() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select year, " +
                "sum(case when gender = 'male' then number else 0 end)," +
                "sum(case when gender = 'female' then number else 0 end) " +
                "from governance_persons_reported_tohave_committed_rape group by year";
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

    public List<Sector_Data> getTotal_Prisoners_Committed_For_Debt_by_Sex() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select year, " +
                "sum(case when gender = 'male' then number else 0 end)," +
                "sum(case when gender = 'female' then number else 0 end) " +
                "from governance_total_prisoners_committed_for_debt_bysex group by year";
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

    public List<String> getFemaleCircumcisionAgeGroups() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> list = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(age) FROM governance_prevalence_female_circumcision_and_type ";
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

    public List<Sector_Data> getPrevalence_Female_Circumcision_and_Type(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select cut_no_flesh_removed, " +
                "cut_flesh_removed," +
                "sewn_closed " +
                "from governance_prevalence_female_circumcision_and_type where age = '"+choice+"'";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("As of 2015");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }
}
