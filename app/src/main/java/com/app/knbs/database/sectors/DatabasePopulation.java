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

public class DatabasePopulation {
    private Context mContext;
    public DatabasePopulation(Context mContext) {
        this.mContext = mContext;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(mContext);

    public List<Sector_Data> getVital_statistics_births_and_deaths_by_sex(String county, String gender) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        gender = gender.toLowerCase();
        String query = "SELECT year,births,deaths FROM population_and_vs_births_and_deaths_by_sex e " +
                "                JOIN counties c ON e.county_id=c.county_id" +
                "                    WHERE county_name='"+county+"' and gender='"+gender+"' GROUP BY gender,year,county_name ";
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

    public List<Sector_Data> getVital_statistics_expectedandregisteredbirthsanddeaths(String county,String coverage) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,births,deaths FROM population_and_vs_expectedandregisteredbirthsanddeaths e " +
                "                JOIN counties c ON e.county_id=c.county_id" +
                "                    WHERE county_name='"+county+"' and coverage='"+coverage+"' GROUP BY coverage,year,county_name ";
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

    public List<Sector_Data> getPopulationprojectionsbyselectedagegroup(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT county_name,year,\n" +
                "(SUM(range_0_4)+SUM(range_5_9)+SUM(range_10_14)+SUM(range_15_19)) AS range_0_19,\n" +
                "(SUM(range_20_24)+SUM(range_25_29)+SUM(range_30_34)+SUM(range_35_39)) AS range_20_39,\n" +
                "(SUM(range_40_44)+SUM(range_45_49)+SUM(range_50_54)+SUM(range_55_59)) AS range_40_59,\n" +
                "(SUM(range_60_64)+SUM(range_65_69)+SUM(range_70_74)+SUM(range_75_79)+SUM(range_80_plus)) AS range_60_plus\n" +
                "FROM population_and_vs_populationprojectionsbyselectedagegroup e\n" +
                "JOIN counties c ON e.county_id=c.county_id\n" +
                "WHERE county_name='"+county+"' GROUP BY year,county_name";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setCounty(cursor.getString(0));
            data.setYear(cursor.getString(1));
            data.setSet_A(cursor.getString(2));
            data.setSet_B(cursor.getString(3));
            data.setSet_C(cursor.getString(4));
            data.setSet_D(cursor.getString(5));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getPopulationprojectionsbyspecialagegroups(String county, String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        //choice = choice.toLowerCase();
        String query = "SELECT year,\n" +
                "SUM(range_less_18) AS range_below_18,\n" +
                "SUM(range_18_plus) AS range_18_plus,\n" +
                "SUM(range_65_plus) AS range_65_plus \n" +
                "FROM population_and_vs_populationprojectionsbyspecialagegroups e\n" +
                "JOIN counties c ON e.county_id=c.county_id\n" +
                "WHERE county_name='"+county+"' AND gender LIKE '"+choice+"' GROUP BY year";
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

    public List<String> getVital_statistics_top_ten_death_causes() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT cause FROM population_and_vs_death_causes" ;
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

    public List<Sector_Data> getVital_statistics_top_ten_death_causes_2014(String county, String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,total FROM population_and_vs_top_ten_death_causes_2014 e " +
                "JOIN counties c ON e.county_id=c.county_id " +
                "JOIN population_and_vs_death_causes f ON f.cause_id=e.cause_id" +
                " WHERE county_name='"+county+"' AND cause='"+choice+"' GROUP BY year,county_name ";
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

    public List<Sector_Data> getPopulationbysexhouseholdsdensityandcensusyears() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT census_year,male,female,total FROM population_and_vs_populationbysexhouseholdsdensityandcensusyears GROUP BY census_year ";
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

    public List<String> getEducation_levels() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "select distinct(education_level) from population_by_sex_and_school_attendance";
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

    public List<Sector_Data> getPopulation_by_Sex_and_School_Attendance_3_Years_and_Above(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select male,female from population_by_sex_and_school_attendance where education_level = '"+choice+"'";
        Log.d(TAG, "rows "+query);
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(choice);
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getFloor_Type() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "select distinct(material) from population_households_type_floor_material_main_dwelling_unit";
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

    public List<Sector_Data> getHouseholds_by_Main_Type_of_Floor_Material_for_the_Main_Dwelling_Unit(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select households from population_households_type_floor_material_main_dwelling_unit where material = '"+choice+"'";
        Log.d(TAG, "rows "+query);
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(choice);
            data.setSet_A(cursor.getString(0));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getWater_Sources() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "select distinct(source) from population_households_by_main_source_of_water";
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

    public List<Sector_Data> getHouseholds_by_Main_Source_of_Water(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select total from population_households_by_main_source_of_water where source = '"+choice+"'";
        Log.d(TAG, "rows "+query);
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(choice);
            data.setSet_A(cursor.getString(0));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getAssets() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "select distinct(asset) from population_percentage_households_ownership_household_assets";
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

    public List<Sector_Data> getPercentage_of_Households_by_Ownership_of_Household_Assets(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "select percentage from population_percentage_households_ownership_household_assets where asset = '"+choice+"'";
        Log.d(TAG, "rows "+query);
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(choice);
            data.setSet_A(cursor.getString(0));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getAgeGroups() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "select distinct(age_group) from population_by_sex_and_age_groups";
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

    public List<Sector_Data> getPopulation_by_Sex_and_Age_Groups(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT male,female FROM population_by_sex_and_age_groups WHERE age_group='"+choice+"'";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
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

    public List<Sector_Data> getPopulation_by_broad_age_group(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT age_depend_ratio,child_depend_ratio,old_age_depend_ratio FROM population_kihibs_by_broad_age_group e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='" + county + "' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows " + cursor.getCount() + "\n" + query);
        List<Sector_Data> list = new ArrayList<>();
        while (cursor.moveToNext()) {
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

    public List<String> getOrphanCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("living_with_both");
        categories.add("father_alive");
        categories.add("father_deceased");
        categories.add("mother_alive");
        categories.add("mother_deceased");
        categories.add("both_alive");
        categories.add("only_father_alive");
        categories.add("only_mother_alive");
        categories.add("both_parents_deceased");
        categories.add("missing_info");
        categories.add("orphanhood");
        return categories;
    }

    public List<Sector_Data> getPopulation_of_Children_under_18_by_orphanhood(String county,String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query =
                "SELECT "+choice+" FROM " +
                        "population_kihibs_children_under_18_by_orphanhood e " +
                        "JOIN counties c ON e.county_id=c.county_id WHERE county_name='" + county + "' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
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

    public List<Sector_Data> getPopulation_Distribution_by_sex(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT male_per_cent,female_per_cent FROM population_kihibs_distribution_by_sex e " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='" + county + "' ";;;
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

    public List<Sector_Data> getPopulation_Distribution_of_households_by_size(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT range_1_2_persons + range_3_4_persons,range_5_6_persons + over_7_persons FROM population_kihibs_distribution_of_households_by_size e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='" + county + "' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows " + cursor.getCount() + "\n" + query);
        List<Sector_Data> list = new ArrayList<>();
        while (cursor.moveToNext()) {
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

    public List<String> getMaritalCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("monogamous");
        categories.add("polygamous");
        categories.add("living_together");
        categories.add("seperated");
        categories.add("divorced");
        categories.add("widow_widower");
        return categories;
    }

    public List<Sector_Data> getPopulation_Marital_Status_above_18_years(String county,String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT "+choice+" FROM population_kihibs_marital_status_above_18_years " +
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='" + county + "' ";;
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
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

    public List<Sector_Data> getPopulation_by_sex_according_to_household_head(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT male,female FROM population_kihibs_hholds_by_sex_of_household_head e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='" + county + "' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows " + cursor.getCount() + "\n" + query);
        List<Sector_Data> list = new ArrayList<>();
        while (cursor.moveToNext()) {
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

}
