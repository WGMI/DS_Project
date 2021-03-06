package com.app.knbs.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.knbs.R;
import com.app.knbs.adapter.SpinnerAdapter;
import com.app.knbs.adapter.model.Report;
import com.app.knbs.adapter.model.Sector_Data;
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.database.sectors.DatabaseAgriculture;
import com.app.knbs.database.sectors.DatabaseBuildingConstruction;
import com.app.knbs.database.sectors.DatabaseCPI;
import com.app.knbs.database.sectors.DatabaseEducation;
import com.app.knbs.database.sectors.DatabaseEnergy;
import com.app.knbs.database.sectors.DatabaseFinance;
import com.app.knbs.database.sectors.DatabaseGovernance;
import com.app.knbs.database.sectors.DatabaseHealth;
import com.app.knbs.database.sectors.DatabaseLabour;
import com.app.knbs.database.sectors.DatabaseLandClimate;
import com.app.knbs.database.sectors.DatabaseManufacturing;
import com.app.knbs.database.sectors.DatabaseMoneyAndBanking;
import com.app.knbs.database.sectors.DatabasePopulation;
import com.app.knbs.database.sectors.DatabaseTourism;
import com.app.knbs.database.sectors.DatabaseTradeCommerce;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Developed by Rodney on 04/10/2017.
 */

public class ReportGraph extends AppCompatActivity {

    private String choice;
    private String county;
    private String report;
    private String selection;

    private String label_1,label_2,label_3,label_4,label_5;
    private TextView yLabel;
    private TextView xLabel;
    private LineChart lineChart;
    private BarChart barChart ;
    private Spinner select;

    private DatabaseHelper dbHelper;
    private DatabaseManufacturing databaseManufacturing;
    private DatabaseCPI databaseCPI;
    private DatabaseLandClimate databaseLandClimate;
    private DatabaseLabour databaseLabour;
    private DatabaseHealth databaseHealth;
    private DatabaseBuildingConstruction databaseBuildingConstruction;
    private DatabaseEducation databaseEducation;
    private DatabaseGovernance databaseGovernance;
    private DatabaseTradeCommerce databaseTradeCommerce;
    private DatabaseFinance databaseFinance;
    private DatabaseAgriculture databaseAgriculture;
    private DatabasePopulation databasePopulation;
    private DatabaseEnergy databaseEnergy;
    private DatabaseTourism databaseTourism;
    private DatabaseMoneyAndBanking databaseMoneyAndBanking;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_report);

        dbHelper = new DatabaseHelper(this);
        databaseEducation = new DatabaseEducation(this);
        databaseManufacturing = new DatabaseManufacturing(this);
        databaseCPI = new DatabaseCPI(this);
        databaseLandClimate = new DatabaseLandClimate(this);
        databaseLabour = new DatabaseLabour(this);
        databaseHealth = new DatabaseHealth(this);
        databaseBuildingConstruction = new DatabaseBuildingConstruction(this);
        databaseGovernance = new DatabaseGovernance(this);
        databaseTradeCommerce = new DatabaseTradeCommerce(this);
        databaseFinance =  new DatabaseFinance(this);
        databaseAgriculture = new DatabaseAgriculture(this);
        databasePopulation =  new DatabasePopulation(this);
        databaseEnergy =  new DatabaseEnergy(this);
        databaseTourism =  new DatabaseTourism(this);
        databaseMoneyAndBanking =  new DatabaseMoneyAndBanking(this);

        Intent intent = getIntent();
        String sector = intent.getStringExtra("sector");
        report = intent.getStringExtra("report");
        county = intent.getStringExtra("county");
        String source = intent.getStringExtra("source");
        String table = intent.getStringExtra("table");
        String api = intent.getStringExtra("api");
        Log.d("Report",report);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(sector);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView txtReport = (TextView)findViewById(R.id.textViewReport);
        TextView txtSource = (TextView)findViewById(R.id.source);
        yLabel = (TextView)findViewById(R.id.yLabel);
        xLabel = (TextView)findViewById(R.id.xlabel);
        TextView selectionTitle = (TextView) findViewById(R.id.title);
        TextView selectionLabel = (TextView) findViewById(R.id.label);

        txtReport.setText(report);
        txtSource.setText(source);

        if(county!=null) {
            TextView txtCounty = (TextView) findViewById(R.id.textCounty);
            txtCounty.setVisibility(View.VISIBLE);
            txtCounty.setText(county);
        }

        final String[] select_qualification = {
                "Select Qualification", "10th Below", "12th", "Diploma", "UG",
                "PG", "Phd"};
        Spinner spinner = (Spinner) findViewById(R.id.spinnerCheckbox);

        ArrayList<Report> listVOs = new ArrayList<>();

        for (String aSelect_qualification : select_qualification) {
            Report spinnerDetails = new Report();
            spinnerDetails.setTitle(aSelect_qualification);
            spinnerDetails.setSector(sector);
            spinnerDetails.setReport(report);
            spinnerDetails.setCounty(county);
            spinnerDetails.setSource(source);
            spinnerDetails.setTable(table);
            spinnerDetails.setApi(api);
            spinnerDetails.setSelected(false);

            listVOs.add(spinnerDetails);
        }


        SpinnerAdapter myAdapter = new SpinnerAdapter(getApplicationContext(), 0, listVOs);
        spinner.setAdapter(myAdapter);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms

                Log.d("Checked Values",SpinnerAdapter.listString);
                handler.postDelayed(this, 2000);
            }
        }, 1500);


        select = (Spinner) findViewById(R.id.selectSpinner);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        final RadioButton choice_one = (RadioButton) findViewById(R.id.choice_one);
        final RadioButton choice_two = (RadioButton) findViewById(R.id.choice_two);
        final RadioButton choice_three = (RadioButton) findViewById(R.id.choice_three);
        final RadioButton choice_four = (RadioButton) findViewById(R.id.choice_four);
        final RadioButton choice_five = (RadioButton) findViewById(R.id.choice_five);

        if(report.matches("Prison Population by Sentence Duration and Sex")
                ||report.matches("Persons Reported to have Committed Robbery and Theft")
                ||report.matches("Magistrates Judges and Practicing Lawyers")
                ||report.matches("Persons Reported to Have Committed Homicide by Sex")
                ||report.matches("People Reported to Have Committed Offence Related to Drugs")
                ||report.matches("Offences Committed Against Morality")
                ||report.matches("Offenders Serving")
                ||report.matches("Murder Cases and Convictions Obtained By High Court Station")
                ||report.matches("Firearms and Ammunition Recovered or Surrendered")
                ||report.matches("Environmental Crimes Reported to NEMA")
                ||report.matches("Convicted Prisoners by Type of Offence and Sex")
                ||report.matches("Daily Average Population of Prisoners by Sex")
                ||report.matches("Cases Forwarded And Action Taken")
                ||report.matches("Cases Handled By Ethics Commision")
                ||report.matches("Cases Handled By Various Courts")
                ||report.matches("Financial Institutions by Subcounty")
                ||report.matches("County Outpatient Morbidity Above Five")
                ||report.matches("County Outpatient Morbidity Below Five")
                ||report.matches("Number of Police Prisons and Probation Officers")
                ||report.matches("Top Ten Death Causes")
                ||report.matches("National Trends KCSE Candidates Mean Grade by Sex")
                ||report.matches("Student Enrolment by Sex, Technical Institutions")
                ||report.matches("Public TIVET Education Institutions")
                ||report.matches("KCPE Mean Subject Score")
                ||report.matches("National Government Development and Recurrent Expenditure")
                ||report.matches("Public Secondary School Trained Teachers")
                ||report.matches("Public Secondary School Untrained Teachers")
                ||report.matches("Convicted Prison Population by Age and Sex")
                ||report.matches("Revenue Collection by Amount")
                ||report.matches("Trading Centres")
                ||report.matches("Student Enrolment in Youth Polytechnics")
                ||report.matches("KCSE Examination Results")
                ||report.matches("Health Facilities by Ownership of Health Facilities")
                ||report.matches("Reported Completion of Buildings for Private Ownership")
                ||report.matches("HIV/AIDS Awareness and Testing")
                ||report.matches("Registered Medical Laboratories by Counties")
                ||report.matches("Wage Employment by Industry and Sex")
                ||report.matches("Wage Employment by Industry in Private Sector")
                ||report.matches("Wage Employment by Industry in Public Sector")
                ||report.matches("Environment Impact Assessments by Sector")
                ||report.matches("Wildlife Population Estimates Kenya Rangelands")
                ||report.matches("Quantum Indices of Manufacturing Production")
                ||report.matches("Percentage Change in Quantum Indices of Manufacturing Production")
                ||report.matches("Annual Average Retail Prices of Certain Consumer Goods in Kenya")
                ||report.matches("Consumer Price Index")
                ||report.matches("Elementary Aggregates Weights in the CPI Baskets")
                ||report.matches("Group Weights for Kenya CPI February Base Period 2009")
                ||report.matches("Group Weights for Kenya CPI October Base Period 1997")
                ||report.matches("Balance of Trade")
                ||report.matches("Quantities of Principle Imports")
                ||report.matches("Quantities of Principle Domestic Exports")
                ||report.matches("Values of Principal Domestic Exports")
                ||report.matches("Values of Principal Imports")
                ||report.matches("Values of Total Exports to East African Communities")
                ||report.matches("Values of Total Exports to All Destinations")
                ||report.matches("Trade Imports with African Counrties")
                ||report.matches("Production Area and Average Yield of Coffee")
                ||report.matches("Production Area and Average Yield of Tea")
                ||report.matches("Average Retail Prices of Selected Petroleum Products")
                ||report.matches("Electricity Demand and Supply")
                ||report.matches("KCPE Examination Results by Subject")
                ||report.matches("Statement of National Government Operations")
                ||report.contains("Quantity and Value of Imports, Exports and Re-exports of Petroleum Products")
                ||report.contains("Petroleum Supply and Demand")
                ||report.contains("Net Domestic Sale of Petroleum Fuels by Consumer Category")
                ||report.contains("Installed and Effective Capacity of Electricity")
                ||report.contains("Generation and Imports of Electricity")
                ||report.contains("Participation in Key Decision Making Positions by Sex")
                ||report.contains("Population by Sex and School Attendance (3 Years and Above)")
                ||report.contains("Households by Main Type of Floor Material for the Main Dwelling Unit")
                ||report.contains("Households by Main Source of Water")
                ||report.contains("Percentage of Households by Ownership of Household Assets")
                ) {
            List<String> list = null;
            selectionLabel.setVisibility(View.VISIBLE);
            if(report.matches("Prison Population by Sentence Duration and Sex")) {
                selectionLabel.setText("Category");
                list = databaseGovernance.getPrison_population_by_sentence_duration_and_sex_category();
            }else if(report.matches("Persons Reported to have Committed Robbery and Theft")){
                selectionLabel.setText("Offence");
                list = databaseGovernance.getPersons_reported_to_have_committed_robbery_and_theft_offence();
            }else if (report.matches("Magistrates Judges and Practicing Lawyers")){
                selectionLabel.setText("Category");
                list = databaseGovernance.getMagistrates_judges_and_practicing_lawyers_category();
            }else if (report.matches("Persons Reported to Have Committed Homicide by Sex")){
                selectionLabel.setText("Offence");
                list = databaseGovernance.getPersons_reported_to_have_committed_homicide_by_sex_offence();
            }else if (report.matches("Participation in Key Decision Making Positions by Sex")){
                selectionLabel.setText("Offence");
                list = databaseGovernance.getDecision_making_positions();
            }else if (report.matches("People Reported to Have Committed Offence Related to Drugs")){
                selectionLabel.setText("Offence");
                list = databaseGovernance.getPeople_reported_to_have_committed_offence_related_to_drugs_offence();
            }else if (report.matches("Offences Committed Against Morality")){
                selectionLabel.setText("Category");
                list = databaseGovernance.getOffences_committed_against_morality_category();
            }else if (report.matches("Offenders Serving")){
                selectionLabel.setText("Offence");
                list = databaseGovernance.getOffenders_serving_offence();
            }else if (report.matches("Murder Cases and Convictions Obtained By High Court Station")){
                selectionLabel.setText("Court Station");
                list = databaseGovernance.getMurder_cases_and_convictions_obtained_by_high_court_stations();
            }else if(report.matches("Firearms and Ammunition Recovered or Surrendered")){
                selectionLabel.setText("Category");
                list = databaseGovernance.getFirearms_and_ammunition_recovered_or_surrendered_category();
            }else if(report.matches("Environmental Crimes Reported to NEMA")){
                selectionLabel.setText("Category");
                list = databaseGovernance.getEnvironmental_crimes_reported_to_nema_category();
            }else if(report.matches("Convicted Prisoners by Type of Offence and Sex")){
                selectionLabel.setText("Category");
                list = databaseGovernance.getConvicted_prisoners_by_type_of_offence_and_sex_category();
            }else if(report.matches("Daily Average Population of Prisoners by Sex")){
                selectionLabel.setText("Category");
                list = databaseGovernance.getDaily_average_population_of_prisoners_by_sex_category();
            }else if(report.matches("Cases Forwarded And Action Taken")){
                selectionLabel.setText("Action Taken");
                list = databaseGovernance.getCases_forwarded_and_action_taken_action();
            }else if (report.matches("Cases Handled By Ethics Commision")){
                selectionLabel.setText("Cases");
                list = databaseGovernance.getCases_handled_by_ethics_commision_action();
            }else if (report.matches("Financial Institutions by Subcounty")) {
                selectionLabel.setText("Institution");
                list = databaseFinance.getMoney_banking_institutions_institution();
            }else if(report.matches("County Outpatient Morbidity Above Five")){
                selectionLabel.setText("Disease");
                list = databaseHealth.getCountyoutpatientmorbidity_diseases();
            }else if(report.matches("County Outpatient Morbidity Below Five")) {
                selectionLabel.setText("Disease");
                list = databaseHealth.getCountyoutpatientmorbidity_diseases();
            }else if (report.matches("Number of Police Prisons and Probation Officers")){
                selectionLabel.setText("Category");
                list =databaseGovernance.getNumber_of_police_prisons_and_probation_officers_category();
            }else if(report.matches("Top Ten Death Causes")){
                selectionLabel.setText("Death Causes");
                list = databasePopulation.getVital_statistics_top_ten_death_causes();
            }else if (report.matches("Cases Handled By Various Courts")){
                selectionLabel.setText("Category");
                list = databaseGovernance.getCases_handled_by_various_courts_category();
            }else if (report.matches("National Trends KCSE Candidates Mean Grade by Sex")){
                selectionLabel.setText("Grades");
                list = databaseEducation.getNationaltrendskcsecandidatesmeangradebysex_grades();
            }else if(report.matches("Student Enrolment by Sex, Technical Institutions")){
                selectionLabel.setText("Institution");
                list = databaseEducation.getStudentenrollmentbysextechnicalinstitutions_institution();
            }else if(report.matches("Public TIVET Education Institutions")){
                selectionLabel.setText("Institution");
                list = databaseEducation.getEducationalinstitutions_publictivet_institutions();
            }else if(report.matches("KCPE Mean Subject Score")){
                selectionLabel.setText("Subject");
                list  = databaseEducation.getKcpecandidatesandmeansubjectscore_subject_subject();
            }else if(report.matches("National Government Development and Recurrent Expenditure")){
                selectionLabel.setText("Expenditure");
                list  = databaseEducation.getNationalgovtdevelopmentandrecurrentexpenditure_expenditure();
            }else if(report.matches("Public Secondary School Trained Teachers")){
                selectionLabel.setText("Teacher");
                list = databaseEducation.getPublicsecondaryschooltrainedteachers_teachers();
            }else if(report.matches("Public Secondary School Untrained Teachers")){
                selectionLabel.setText("Teacher");
                list = databaseEducation.getPublicsecondaryschooluntrainedteachers_teachers();
            }else if(report.matches("Convicted Prison Population by Age and Sex")){
                selectionLabel.setText("Category");
                list = databaseGovernance.getConvicted_prison_population_by_age_and_sex_category();
            }else if(report.matches("Revenue Collection by Amount")){
                selectionLabel.setText("Category");
                list = databaseTradeCommerce.getRevenue_collection_ids();
            }else if(report.matches("Trading Centres")){
                selectionLabel.setText("Centres");
                list = databaseTradeCommerce.getTrading_centres_ids();
            }else if(report.matches("Student Enrolment in Youth Polytechnics")){
                selectionLabel.setText("Institution");
                list = databaseEducation.getStudentenrolmentinyouthpolytechnics_institutions();
            }else if(report.matches("KCSE Examination Results")){
                selectionLabel.setText("Grade");
                list = databaseEducation.getKCSE_Grades();
            }else if (report.matches("Health Facilities by Ownership of Health Facilities")){
                selectionLabel.setText("Facility");
                list = databaseHealth.getHealthfacilitiesbyownershipofhealthfacilities_facilities();
            }else if(report.matches("Reported Completion of Buildings for Private Ownership")){
                selectionLabel.setText("Industry");
                list = databaseBuildingConstruction.getBuilding_and_construction_ids();
            }else if (report.matches("HIV/AIDS Awareness and Testing")){
                selectionLabel.setText("HIV Awareness");
                list = databaseHealth.getHiv_aids_awareness_and_testing_awareness();
            }else if (report.matches("Registered Medical Laboratories by Counties")){
                selectionLabel.setText("Class");
                list = databaseHealth.getRegistered_medical_laboratories_by_counties_category();
            }else if (report.matches("Wage Employment by Industry and Sex")){
                selectionLabel.setText("Employment Wage");
                list = databaseLabour.getWage_employment_by_industry_and_sex_industry();
            }else if (report.matches("Wage Employment by Industry in Private Sector")){
                selectionLabel.setText("Employment Wage");
                list = databaseLabour.getWage_employment_by_industry_in_private_sector_industry();
            }else if (report.matches("Wage Employment by Industry in Public Sector")){
                selectionLabel.setText("Employment Wage");
                list = databaseLabour.getWage_employment_by_industry_in_public_sector_industry();
            }else if (report.matches("Environment Impact Assessments by Sector")){
                selectionLabel.setText("Sector");
                list = databaseLandClimate.getLand_and_climate_environment_impact_assessments_by_sector();
            }else if(report.matches("Wildlife Population Estimates Kenya Rangelands")){
                selectionLabel.setText("Trends");
                list = databaseLandClimate.getLand_and_climate_wildlife_population_estimates_kenya_rangelands();
            }else if(report.matches("Quantum Indices of Manufacturing Production")){
                selectionLabel.setText("Commodity");
                list = databaseManufacturing.getManufacturing_quantum_indices_of_manufacturing_production_commodities();
            }else if(report.matches("Percentage Change in Quantum Indices of Manufacturing Production")){
                selectionLabel.setText("Commodity");
                list = databaseManufacturing.getManufacturing_per_change_in_quantum_indices_of_man_production_commodities();
            }else if(report.matches("Annual Average Retail Prices of Certain Consumer Goods in Kenya")){
                selectionLabel.setText("Item");
                list = databaseCPI.getCpi_annual_avg_retail_prices_of_certain_consumer_goods_in_kenya_item();
            }else if(report.contains("Consumer Price Index")){
                Log.d("DatabaseHelper", "getCpi_Income_Group");
                selectionLabel.setText("Income Group");
                list = databaseCPI.getCpi_Income_Group();
            }else if(report.matches("Elementary Aggregates Weights in the CPI Baskets")){
                selectionLabel.setText("Item");
                list = databaseCPI.getCpi_elementary_aggregates_weights_in_the_cpi_baskets();
            }else if (report.matches("Group Weights for Kenya CPI February Base Period 2009")){
                selectionLabel.setText("Item");
                list = databaseCPI.getCpi_group_weights_for_kenya_cpi_febuary_base_2009();
            }else if (report.matches("Group Weights for Kenya CPI October Base Period 1997")){
                selectionLabel.setText("Item");
                list = databaseCPI.getCpi_group_weights_for_kenya_cpi_october_base_1997();
            }else if (report.matches("Balance of Trade")){
                selectionLabel.setText("Description");
                list = databaseTradeCommerce.getBalance_of_Trade_Description();
            }else if (report.matches("Quantities of Principle Imports")){
                selectionLabel.setText("Commodity");
                list = databaseTradeCommerce.getPrincipal_Imports_Commodity_Names();
            }else if (report.matches("Quantities of Principle Domestic Exports")){
                selectionLabel.setText("Description");
                list = databaseTradeCommerce.getDomestic_Exports();
            }else if (report.matches("Values of Principal Domestic Exports")){
                selectionLabel.setText("Description");
                list = databaseTradeCommerce.getDomestic_Exports_From_Val();
            }else if (report.matches("Values of Principal Imports")){
                selectionLabel.setText("Description");
                list = databaseTradeCommerce.getPrincipal_Imports_Commodity_Names_From_Val();
            }else if (report.matches("Values of Total Exports to East African Communities")){
                selectionLabel.setText("Country");
                list = databaseTradeCommerce.getEastAfrican_Countries();
            }else if (report.matches("Values of Total Exports to All Destinations")){
                selectionLabel.setText("Country");
                list = databaseTradeCommerce.getAll_Destinations_Countries();
            }else if (report.matches("Trade Imports with African Counrties")){
                selectionLabel.setText("Country");
                list = databaseTradeCommerce.getAfrican_Countries();
            }else if (report.matches("Production Area and Average Yield of Coffee")){
                selectionLabel.setText("Category");
                list = databaseAgriculture.getCategories();
            }else if (report.matches("Production Area and Average Yield of Tea")){
                selectionLabel.setText("Category");
                list = databaseAgriculture.getCategoriesForTea();
            }else if (report.matches("Average Retail Prices of Selected Petroleum Products")){
                selectionLabel.setText("Product");
                list = databaseEnergy.getSelectedProducts();
            }else if (report.matches("Electricity Demand and Supply")){
                selectionLabel.setText("Demand/Supply");
                list = databaseEnergy.getDemandSupply();
            }else if (report.matches("KCPE Examination Results by Subject")){
                selectionLabel.setText("Subject");
                list = databaseEducation.getSubjects();
            }else if (report.matches("Statement of National Government Operations")){
                selectionLabel.setText("Operation");
                list = databaseFinance.getNational_Government_Operations();
            }else if (report.contains("Quantity and Value of Imports, Exports and Re-exports of Petroleum Products")){
                selectionLabel.setText("Type");
                list = databaseEnergy.getImport_Export_Type();
            }else if (report.contains("Petroleum Supply and Demand")){
                selectionLabel.setText("Product");
                list = databaseEnergy.getPetroleumProduct();
            }else if (report.contains("Net Domestic Sale of Petroleum Fuels by Consumer Category")){
                selectionLabel.setText("User");
                list = databaseEnergy.getPetroleumUser();
            }else if (report.contains("Installed and Effective Capacity of Electricity")){
                selectionLabel.setText("Source");
                list = databaseEnergy.getElectricity_Source();
            }else if (report.contains("Generation and Imports of Electricity")){
                selectionLabel.setText("Source");
                list = databaseEnergy.getElectricity_Source_For_Gen_and_Imports();
            }else if (report.contains("Population by Sex and School Attendance (3 Years and Above)")){
                selectionLabel.setText("Level");
                list = databasePopulation.getEducation_levels();
            }else if (report.contains("Households by Main Type of Floor Material for the Main Dwelling Unit")){
                selectionLabel.setText("Material");
                list = databasePopulation.getFloor_Type();
            }else if (report.contains("Households by Main Source of Water")){
                selectionLabel.setText("Source");
                list = databasePopulation.getWater_Sources();
            }else if (report.contains("Percentage of Households by Ownership of Household Assets")){
                selectionLabel.setText("Asset");
                list = databasePopulation.getAssets();
            }

            select.setVisibility(View.VISIBLE);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<String> adapterSpinner = null;
            if (list != null) {
                adapterSpinner = new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, list);
            }
            // Specify the layout to use when the list of choices appears
            if (adapterSpinner != null) {
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
            // Apply the adapter to the spinner
            select.setAdapter(adapterSpinner);
            select.setOnItemSelectedListener(new ItemSelectedListener());
        }

        if(report.matches("Births and Deaths by Sex")
                ||report.matches("Population Projections by Special Age Group")
                ||report.matches("National Trends KCSE Candidates Mean Grade by Sex")
                ||report.matches("Primary School Enrollment By Class, Sex and Subcounty")
                ||report.matches("Adult Education Proficiency Test Results")
                ||report.matches("Secondary School Enrollment By Class, Sex and Subcounty")
                ||report.matches("Private Candidates Registered for KCPE by Sex")
                ||report.matches("Number of Refugees By Age and Sex")
                ||report.matches("KCSE Examination Results")
                ){
            radioGroup.setVisibility(View.VISIBLE);
            selectionTitle.setVisibility(View.VISIBLE);
            selectionTitle.setText("Gender");
            choice= (report.matches("KCSE Examination Results")) ? "Male":"male";
            choice_one.setText(R.string.male);
            choice_two.setText(R.string.female);

        }else if (
                report.matches("Secondary Enrolment and Access Indicators")
                ||report.matches("Primary Enrolment and Access Indicators")){
            radioGroup.setVisibility(View.VISIBLE);
            selectionTitle.setVisibility(View.VISIBLE);
            selectionTitle.setText("Enrolment and Access Indicators");
            choice_three.setVisibility(View.VISIBLE);

            choice="Enrolment";
            choice_one.setText(R.string.enrolment);
            choice_two.setText(R.string.ger);
            choice_three.setText(R.string.ner);

        }else if(
                report.contains("Installed and Effective Capacity of Electricity")
                ){
            radioGroup.setVisibility(View.VISIBLE);
            selectionTitle.setVisibility(View.VISIBLE);
            selectionTitle.setText("Installed/Effective");
            //choice_three.setVisibility(View.VISIBLE);

            choice="Installed";
            choice_one.setText("Installed");
            choice_two.setText("Effective");
            //choice_three.setText(R.string.ner);
        }else if(
                report.contains("Generation and Imports of Electricity")
                ){
            radioGroup.setVisibility(View.VISIBLE);
            selectionTitle.setVisibility(View.VISIBLE);
            selectionTitle.setText("Generation/Import");
            //choice_three.setVisibility(View.VISIBLE);

            choice="Generation";
            choice_one.setText("Generation");
            choice_two.setText("Imports");
            //choice_three.setText(R.string.ner);
        }else if(report.matches("Teacher Training Colleges")
                ||report.matches("Education Institutions")
                ||report.matches("Student Enrolment in Youth Polytechnics")
                ){
            radioGroup.setVisibility(View.VISIBLE);
            selectionTitle.setVisibility(View.VISIBLE);
            selectionTitle.setText("Sector");
            choice="public";
            choice_one.setText(R.string.public_sector);
            choice_two.setText(R.string.private_sector);
        }else if(report.matches("Expected and Registered Births and Deaths")){
            radioGroup.setVisibility(View.VISIBLE);
            choice_three.setVisibility(View.VISIBLE);

            choice="Expected";
            choice_one.setText(R.string.expected);
            choice_two.setText(R.string.registered);
            choice_three.setText(R.string.coverage);

        }else if(report.matches("Secondary School Enrollment by Level and Sex")){
            radioGroup.setVisibility(View.VISIBLE);
            choice_three.setVisibility(View.VISIBLE);
            choice_four.setVisibility(View.VISIBLE);
            choice_five.setVisibility(View.VISIBLE);

            selectionTitle.setVisibility(View.VISIBLE);
            selectionTitle.setText("Level");
            choice="Form 1";
            choice_one.setText(R.string.form1);
            choice_two.setText(R.string.form2);
            choice_three.setText(R.string.form3);
            choice_four.setText(R.string.form4);
            choice_five.setText(R.string.all_levels);
        }else if(report.matches("Land Potential")){
            radioGroup.setVisibility(View.VISIBLE);
            choice_three.setVisibility(View.VISIBLE);
            choice_four.setVisibility(View.VISIBLE);

            selectionTitle.setVisibility(View.VISIBLE);
            selectionTitle.setText("Potential");
            choice="High Potential";
            choice_one.setText(R.string.high);
            choice_two.setText(R.string.medium);
            choice_three.setText(R.string.low);
            choice_four.setText(R.string.other);
        }else if(report.matches("Teacher Trainees Diploma Enrolment")
                ||report.matches("Teacher Trainees Public Enrolment")){
            radioGroup.setVisibility(View.VISIBLE);

            selectionTitle.setVisibility(View.VISIBLE);
            selectionTitle.setText("Year");
            choice="1st Year";
            choice_one.setText(R.string.year1);
            choice_two.setText(R.string.year2);

            if(report.matches("Teacher Trainees Diploma Enrolment")) {
                choice_three.setVisibility(View.VISIBLE);
                choice_three.setText(R.string.year3);
            }
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(report.matches("Births and Deaths by Sex")
                        ||report.matches("Population Projections by Special Age Group")
                        ||report.matches("National Trends KCSE Candidates Mean Grade by Sex")
                        ||report.matches("Adult Education Proficiency Test Results")
                        ||report.matches("Primary School Enrollment By Class, Sex and Subcounty")
                        ||report.matches("Secondary School Enrollment By Class, Sex and Subcounty")
                        ||report.matches("Private Candidates Registered for KCPE by Sex")
                        ||report.matches("Number of Refugees By Age and Sex")
                        ||report.matches("KCSE Examination Results")
                        ) {

                    if (checkedId == R.id.choice_one) {
                        choice = "Male";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_two) {
                        choice = "Female";
                        onProgressChanged();
                    }

                }

                if(report.contains("Installed and Effective Capacity of Electricity")){
                    if (checkedId == R.id.choice_one) {
                        choice = "Installed";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_two) {
                        choice = "Effective";
                        onProgressChanged();
                    }
                }

                if(report.contains("Generation and Imports of Electricity")){
                    if (checkedId == R.id.choice_one) {
                        choice = "Generation";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_two) {
                        choice = "Imports";
                        onProgressChanged();
                    }
                }

                if (report.matches("Secondary Enrolment and Access Indicators")
                    ||report.matches("Primary Enrolment and Access Indicators")){

                    if (checkedId == R.id.choice_one) {
                        choice = "Enrolment";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_two) {
                        choice = "Ger";
                        onProgressChanged();
                    }else if (checkedId == R.id.choice_three) {
                        choice = "Ner";
                        onProgressChanged();
                    }
                }

                if(report.matches("Teacher Training Colleges")
                        ||report.matches("Education Institutions")
                        ||report.matches("Student Enrolment in Youth Polytechnics")
                        ){
                    if (checkedId == R.id.choice_one) {
                        choice = "public";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_two) {
                        choice = "private";
                        onProgressChanged();
                    }
                }

                if(report.matches("Expected and Registered Births and Deaths")) {
                    if (checkedId == R.id.choice_one) {
                        choice = "Expected";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_two) {
                        choice = "Registered";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_three) {
                        choice = "Coverage";
                        onProgressChanged();
                    }
                }

                if(report.matches("KCSE Examination Results")) {
                    if (checkedId == R.id.choice_one) {
                        choice = "Male";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_two) {
                        choice = "Female";
                        onProgressChanged();
                    }
                }

                if(report.matches("Secondary School Enrollment by Level and Sex")){
                    if (checkedId == R.id.choice_one) {
                        choice = "Form 1";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_two) {
                        choice = "Form 2";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_three) {
                        choice = "Form 3";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_four) {
                        choice = "Form 4";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_five) {
                        choice = "All";
                        onProgressChanged();
                    }
                }

                if(report.matches("Land Potential")){
                    if (checkedId == R.id.choice_one) {
                        choice = "High Potential";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_two) {
                        choice = "Medium Potential";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_three) {
                        choice = "Low Potential";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_four) {
                        choice = "All Other Land";
                        onProgressChanged();
                    }
                }

                if(report.matches("Teacher Trainees Diploma Enrolment")
                        ||report.matches("Teacher Trainees Public Enrolment")){

                    if (checkedId == R.id.choice_one) {
                        choice = "1st Year";
                        onProgressChanged();
                    } else if (checkedId == R.id.choice_two) {
                        choice = "2nd Year";
                        onProgressChanged();
                    }

                    if(report.matches("Teacher Trainees Diploma Enrolment")){
                        if (checkedId == R.id.choice_three) {
                            choice = "3rd Year";
                            onProgressChanged();
                        }
                    }
                }

            }
        });

        try {
            onProgressChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    public void onProgressChanged(){

        List<Sector_Data> list = null ;

        lineChart = (LineChart) findViewById(R.id.chartLine);
        barChart = (BarChart) findViewById(R.id.chartBar);

        ArrayList<String> labels = new ArrayList<>();

        ArrayList<Entry> group1 = new ArrayList<>();
        ArrayList<Entry> group2 = new ArrayList<>();
        ArrayList<Entry> group3 = new ArrayList<>();
        ArrayList<Entry> group4 = new ArrayList<>();
        ArrayList<Entry> group5 = new ArrayList<>();

        ArrayList<BarEntry> group11 = new ArrayList<>();
        ArrayList<BarEntry> group12 = new ArrayList<>();
        ArrayList<BarEntry> group13 = new ArrayList<>();
        ArrayList<BarEntry> group14 = new ArrayList<>();
        ArrayList<BarEntry> group15 = new ArrayList<>();

        /*
        labels.add("2010");
        group1.add(new Entry(4f, 0));
        group11.add(new BarEntry(4f, 0));
        group2.add(new Entry(4f, 0));
        group12.add(new BarEntry(4f, 0));
        */

        /*Start*/

        if (report.matches("Crimes Reported to Police by Command Stations")
                ||report.matches("Health Facilities by Ownership of Health Facilities")
                ||report.matches("CDF Allocation")
                ||report.matches("Building and Construction Amount")
                ||report.matches("Quarterly Civil Engineering Cost")
                ||report.matches("Quarterly Residential Building Cost")
                ||report.matches("Quarterly Non Residential Building Cost")
                ||report.matches("Quarterly Overall Construction Cost")
                ||report.matches("Health Facilities")
                ||report.matches("Immunization Rate")
                ||report.matches("Adult Education Centres by Subcounty")
                ||report.matches("Total Secondary School Enrolment by Year")
                ||report.matches("Environmental Crimes Reported to NEMA")
                ||report.matches("Cases Forwarded And Action Taken")
                ||report.matches("Cases Handled By Ethics Commision")
                ||report.matches("Financial Institutions by Subcounty")
                ||report.matches("Number of Police Prisons and Probation Officers")
                ||report.matches("Top Ten Death Causes")
                ||report.matches("County Outpatient Morbidity Above Five")
                ||report.matches("County Outpatient Morbidity Below Five")
                ||report.matches("Public TIVET Education Institutions")
                ||report.matches("KCPE Mean Subject Score")
                ||report.matches("Land Potential")
                ||report.matches("Revenue Collection by Amount")
                ||report.matches("Trading Centres")
                ||report.matches("Reported Completion of Buildings for Private Ownership")
                ||report.matches("Current Use of Contraception by County")
                ||report.matches("Registered Medical Laboratories by Counties")
                ||report.matches("Use of Mosquito Nets by Children")
                ||report.matches("Wage Employment by Industry in Private Sector")
                ||report.matches("Wage Employment by Industry in Public Sector")
                ||report.matches("Environment Impact Assessments by Sector")
                ||report.matches("Wildlife Population Estimates Kenya Rangelands")
                ||report.matches("Quantum Indices of Manufacturing Production")
                ||report.matches("Percentage Change in Quantum Indices of Manufacturing Production")
                ||report.matches("Annual Average Retail Prices of Certain Consumer Goods in Kenya")
                ||report.contains("Consumer Price Index")
                ||report.contains("Nairobi Securities")
                ||report.contains("Broad Money Supply")
                ||report.contains("Inflation Rates")
                ||report.contains("Commercial Banks")
                ||report.matches("Arrivals")
                ||report.matches("Conferences")
                ||report.matches("Departures")
                ||report.matches("Earnings")
                ||report.matches("Tourism Visitor To Parks")
                ||report.matches("Balance of Trade")
                ||report.matches("Quantities of Principle Imports")
                ||report.matches("Quantities of Principle Domestic Exports")
                ||report.matches("Values of Principal Domestic Exports")
                ||report.matches("Values of Principal Imports")
                ||report.matches("Values of Total Exports to East African Communities")
                ||report.matches("Values of Total Exports to All Destinations")
                ||report.matches("Trade Imports with African Counrties")
                ||report.matches("Electricity Demand and Supply")
                ||report.matches("KCPE Examination Candidature")
                ||report.matches("KCPE Examination Results by Subject")
                ||report.matches("Statement of National Government Operations")
                ||report.matches("Average Retail Prices of Selected Petroleum Products")
                ||report.matches("Petroleum Supply and Demand")
                ||report.matches("Net Domestic Sale of Petroleum Fuels by Consumer Category")
                ||report.contains("Installed and Effective Capacity of Electricity")
                ||report.contains("Generation and Imports of Electricity")
                ||report.contains("Development Expenditure Water")
                ||report.contains("Average Export Prices Ash")
                ||report.contains("Government Forest")
                ||report.contains("Water Purification Points")
                ||report.contains("Number of Financial Institutions")
                ||report.matches("KCSE Examination Results")
                ||report.contains("Households by Main Type of Floor Material for the Main Dwelling Unit")
                ||report.contains("Households by Main Source of Water")
                ||report.contains("Percentage of Households by Ownership of Household Assets ")
                ) {
            if(report.matches("Crimes Reported to Police by Command Stations")){
                label_1 = "Crimes";
                yLabel.setText("Number of Crimes");
                list = databaseGovernance.getCrimes_reported_to_police_by_command_stations(county);
            }else if(report.matches("Health Facilities by Ownership of Health Facilities")){
                label_1 = "Facilities";
                yLabel.setText("Number of Facilities");
                list = databaseHealth.getHealthfacilitiesbyownershipofhealthfacilities(county,selection);
            }else if(report.matches("CDF Allocation")){
                label_1 = "CDF allocation";
                yLabel.setText("Allocation");
                list = databaseFinance.getCdf_allocation(county);
            }else if(report.matches("Building and Construction Amount")){
                label_1 = "Building and Construction Amount";
                yLabel.setText("Amount");
                list = databaseBuildingConstruction.getBuilding_and_construction_amount(county);
            }else if(report.matches("Quarterly Civil Engineering Cost")){
                label_1 = "Quarterly Civil Engineering Cost";
                yLabel.setText("Amount");
                list = databaseBuildingConstruction.getQuarterly_Civil_Engineering_Cost();
            }else if(report.matches("Quarterly Residential Building Cost")){
                label_1 = "Quarterly Residential Building Cost";
                yLabel.setText("Amount");
                list = databaseBuildingConstruction.getQuarterly_Residential_Building_Cost();
            }else if(report.matches("Quarterly Non Residential Building Cost")){
                label_1 = "Quarterly Non Residential Building Cost";
                yLabel.setText("Amount");
                list = databaseBuildingConstruction.getQuarterly_Non_Residential_Building_Cost();
            }else if(report.matches("Quarterly Overall Construction Cost")){
                label_1 = "Quarterly Overall Construction Cost";
                yLabel.setText("Amount");
                list = databaseBuildingConstruction.get_Quarterly_Overall_Construction_Cost();
            }else if (report.matches("Health Facilities")){
                label_1 = "Facilities";
                yLabel.setText("Number of Facilities");
                list = databaseHealth.getHealth_facilities(county);
            }else if (report.matches("Immunization Rate")){
                label_1 = "Immunization Rate";
                yLabel.setText("Rate");
                list = databaseHealth.getImmunization_rate(county);
            }else if (report.matches("Adult Education Centres by Subcounty")){
                label_1 = "Adult education centers";
                yLabel.setText("Centers");
                list = databaseEducation.getAdulteducationcentresbysubcounty(county);
            }else if (report.matches("Total Secondary School Enrolment by Year")){
                label_1 = "Secondary School Enrolment";
                yLabel.setText("Enrolment");
                list = databaseEducation.getTotalsecondaryschoolenrollmentbyyear(county);
            }else if(report.matches("Environmental Crimes Reported to NEMA")) {
                label_1 = selection;
                yLabel.setText("Crimes");
                list = databaseGovernance.getEnvironmental_crimes_reported_to_nema(selection);
            }else if (report.matches("Cases Forwarded And Action Taken")) {
                label_1 = selection;
                yLabel.setText("Cases");
                list = databaseGovernance.getCases_forwarded_and_action_taken(selection);
            }else if (report.matches("Cases Handled By Ethics Commision")) {
                label_1 = selection;
                yLabel.setText("Cases");
                list = databaseGovernance.getCases_handled_by_ethics_commision(selection);
            }else if (report.matches("Financial Institutions by Subcounty")) {
                label_1 = selection;
                yLabel.setText("Institutions");
                list = databaseFinance.getMoney_banking_institutions(county,selection);
            }else if(report.matches("County Outpatient Morbidity Above Five")){
                label_1 = selection;
                yLabel.setText("Patients");
                list = databaseHealth.getCountyoutpatientmorbidityabovefive(county,selection);
            }else if(report.matches("County Outpatient Morbidity Below Five")) {
                label_1 = selection;
                yLabel.setText("Patients");
                list = databaseHealth.getCounty_outpatient_morbidity_below_five(county,selection);
            }else if(report.matches("Number of Police Prisons and Probation Officers")) {
                label_1 = selection;
                yLabel.setText("Officers");
                list = databaseGovernance.getNumber_of_police_prisons_and_probation_officers(selection);
            }else if (report.matches("Top Ten Death Causes")) {
                label_1 = selection;
                yLabel.setText("Officers");
                list = databasePopulation.getVital_statistics_top_ten_death_causes_2014(county,selection);
            }else if (report.matches("Public TIVET Education Institutions")){
                label_1 = selection;
                yLabel.setText("polytechnics");
                list = databaseEducation.getEducationalinstitutions_publictivet(selection);
            }else if (report.matches("KCPE Mean Subject Score")){
                label_1 = selection;
                yLabel.setText("mean score");
                list = databaseEducation.getKcpecandidatesandmeansubjectscore_subject(selection);
            }else if(report.matches("Land Potential")){
                label_1 = choice;
                yLabel.setText("potential");
                list = databaseAgriculture.getLand_potential(county,choice);
            }else if(report.matches("Revenue Collection by Amount")) {
                label_1 = selection;
                yLabel.setText("revenue");
                list = databaseTradeCommerce.getRevenue_collection_by_amount(county,selection);
            }else if(report.matches("Trading Centres")) {
                label_1 = selection;
                yLabel.setText("centres");
                list = databaseTradeCommerce.getTrading_centres(county,selection);
            }else if (report.matches("Reported Completion of Buildings for Private Ownership")){
                label_1 = selection;
                yLabel.setText("centres");
                list = databaseBuildingConstruction.getBuilding_and_construction_amount(county,selection);
            }else if (report.matches("Current Use of Contraception by County")){
                label_1 = "Contraceptions";
                yLabel.setText("contraceptions");
                list = databaseHealth.getCurrent_use_of_contraception_by_county(county);
            }else if (report.matches("Registered Medical Laboratories by Counties")){
                label_1 = selection;
                yLabel.setText("laboratories");
                list = databaseHealth.getRegistered_medical_laboratories_by_counties(county,selection);
            }else if (report.matches("Use of Mosquito Nets by Children")){
                label_1 = "Mosquito Nets";
                yLabel.setText("nets");
                list = databaseHealth.getUse_of_mosquito_nets_by_children(county);
            }else if (report.matches("Wage Employment by Industry in Private Sector")){
                label_1 = selection;
                yLabel.setText("Wage Employment");
                list = databaseLabour.getWage_employment_by_industry_in_private_sector(selection);
            }else if (report.matches("Wage Employment by Industry in Public Sector")){
                label_1 = selection;
                yLabel.setText("Wage Employment");
                list = databaseLabour.getWage_employment_by_industry_in_public_sector(selection);
            }else if (report.matches("Environment Impact Assessments by Sector")){
                label_1 = selection;
                yLabel.setText("Envoronmantal Impact");
                list = databaseLandClimate.getLand_and_climate_environment_impact_assessments_by_sector(selection);
            }else if(report.matches("Wildlife Population Estimates Kenya Rangelands")){
                label_1 = selection;
                yLabel.setText("Value Added");
                list = databaseLandClimate.getLand_and_climate_wildlife_population_estimates_kenya_rangelands(selection);
            }else if (report.matches("Quantum Indices of Manufacturing Production")){
                label_1 = selection;
                yLabel.setText("Quantum Indice");
                list = databaseManufacturing.getManufacturing_quantum_indices_of_manufacturing_production(selection);
            }else if(report.matches("Percentage Change in Quantum Indices of Manufacturing Production")){
                label_1 = selection;
                yLabel.setText("Percentage Change");
                list = databaseManufacturing.getManufacturing_per_change_in_quantum_indices_of_man_production(selection);
            }else if(report.matches("Annual Average Retail Prices of Certain Consumer Goods in Kenya")){
                label_1 = selection;
                yLabel.setText("Retail Price");
                list = databaseCPI.getCpi_annual_avg_retail_prices_of_certain_consumer_goods_in_kenya(selection);
            }else if(report.contains("Price Ind")){
                Log.d("DatabaseHelper", "getCpi_Consumer_Price_Index ");
                label_1 = selection;
                yLabel.setText("CPI");
                list = databaseCPI.getCpi_Consumer_Price_Index(selection);
            }else if(report.contains("Nairobi Securities")){
                label_1 = selection;
                yLabel.setText("Share Index");
                list = databaseMoneyAndBanking.getNSE_Share_Index();
            }else if(report.contains("Broad Money Supply")){
                label_1 = selection;
                yLabel.setText("Broad Money Supply");
                list = databaseMoneyAndBanking.getBroad_Money_Supply();
            }else if(report.contains("Inflation Rates")){
                label_1 = selection;
                yLabel.setText("Inflation Rate");
                list = databaseMoneyAndBanking.getInflation_Rates();
            }else if(report.contains("Commercial Banks")){
                label_1 = selection;
                yLabel.setText("Amount");
                list = databaseMoneyAndBanking.getInterest_Rates_OR_Commercial_Banks_B_L_And_A(1);
            }else if(report.matches("Arrivals")){
                label_1 = selection;
                yLabel.setText("Arrivals");
                list = databaseTourism.getYearly_Tourism_Arrivals();
            }else if (report.matches("Conferences")){
                label_1 = "Number Of Conferences";
                label_2 = "Number Of Delegates";
                //label_3 = "Unions";
                yLabel.setText("Cooperatives");
                list = databaseTourism.getConferences();
            }else if (report.matches("Departures")){
                label_1 = "Business";
                label_2 = "Holiday";
                label_3 = "Transit";
                yLabel.setText("Travellers");
                list = databaseTourism.getDepartures();
            }else if (report.matches("Earnings")){
                label_1 = selection;
                yLabel.setText("Amount");
                list = databaseTourism.getEarnings();
            }else if (report.matches("Development Expenditure Water")){
                label_1 = "Water Development";
                label_2 = "Rural Water Supplies";
                label_3 = "Irrigation Development";
                yLabel.setText("Amount");
                list = databaseLandClimate.getDevelopment_Expenditure_Water();
            }else if (report.matches("Balance of Trade")) {
                label_1 = selection;
                yLabel.setText("Value");
                list = databaseTradeCommerce.getBalance_of_Trade(selection);
            }else if (report.matches("Quantities of Principle Imports")) {
                label_1 = selection;
                yLabel.setText("Value");
                list = databaseTradeCommerce.getQuantities_of_Principle_Imports(selection);
            }else if (report.matches("Quantities of Principle Domestic Exports")) {
                label_1 = selection;
                yLabel.setText("Value");
                list = databaseTradeCommerce.getQuantities_of_Principle_Domestic_Exports(selection);
            }else if (report.matches("Values of Principal Domestic Exports")) {
                label_1 = selection;
                yLabel.setText("Value");
                list = databaseTradeCommerce.getValues_of_Principle_Domestic_Exports(selection);
            }else if (report.matches("Values of Principal Imports")) {
                label_1 = selection;
                yLabel.setText("Value");
                list = databaseTradeCommerce.getValues_of_Principle_Imports(selection);
            }else if (report.matches("Values of Total Exports to East African Communities")) {
                label_1 = selection;
                yLabel.setText("Value");
                list = databaseTradeCommerce.getValues_of_Total_Exports_to_East_African_Communities(selection);
            }else if (report.matches("Values of Total Exports to All Destinations")) {
                label_1 = selection;
                yLabel.setText("Value");
                list = databaseTradeCommerce.getValues_of_Total_Exports_to_All_Destinations(selection);
            }else if (report.matches("Trade Imports with African Counrties")) {
                label_1 = selection;
                yLabel.setText("Value");
                list = databaseTradeCommerce.getTrade_Imports_with_African_Counrties(selection);
            }else if (report.matches("Average Retail Prices of Selected Petroleum Products")) {
                Log.d("We_are_here", "onProgressChanged: ");
                label_1 = selection;
                yLabel.setText("Price");
                list = databaseEnergy.getAverage_Retail_Prices_of_Selected_Petroleum_Products(selection);
            }else if (report.matches("Electricity Demand and Supply")) {
                label_1 = selection;
                yLabel.setText("Capacity (Megawatts)");
                //list = databaseEnergy.getAverage_Retail_Prices_of_Selected_Petroleum_Products(selection);
                list = databaseEnergy.getElectricity_Demand_and_Supply(selection);
            }else if (report.matches("KCPE Examination Candidature")) {
                label_1 = selection;
                yLabel.setText("Candidature");
                list = databaseEducation.getKCPE_Examination_Candidature();
            }else if (report.matches("KCPE Examination Results by Subject")) {
                label_1 = selection;
                yLabel.setText("Result");
                list = databaseEducation.getKCPE_Examination_Results_By_Subject(selection);
            }else if (report.matches("Statement of National Government Operations")) {
                label_1 = selection;
                yLabel.setText("Amount");
                list = databaseFinance.getStatement_of_National_Government_Operations(selection);
            }else if (report.matches("Petroleum Supply and Demand")) {
                label_1 = selection;
                list = databaseEnergy.getPetroleum_Supply_and_Demand(selection);
                yLabel.setText("Quantity (Supply)");
                if(list.get(0).getSet_B().contains("Demand")){
                    yLabel.setText("Quantity (Demand)");
                }
            }else if (report.matches("Net Domestic Sale of Petroleum Fuels by Consumer Category")) {
                label_1 = selection;
                yLabel.setText("Amount");
                list = databaseEnergy.getNet_Domestic_Sale_of_Petroleum_Fuels_by_Consumer_Category(selection);
            }else if (report.contains("Installed and Effective Capacity of Electricity")) {
                label_1 = selection;
                yLabel.setText("Megawatts");
                list = databaseEnergy.getInstalled_and_Effective_Capacity_of_Electricity(choice,selection);
            }else if (report.contains("Generation and Imports of Electricity")) {
                label_1 = selection;
                yLabel.setText("Megawatts");
                list = databaseEnergy.getGeneration_and_Imports_of_Electricity(choice,selection);
            }else if (report.contains("Average Export Prices Ash")) {
                label_1 = selection;
                yLabel.setText("Price");
                list = databaseLandClimate.getAverage_Export_Prices_Ash();
            }else if (report.contains("Government Forest")) {
                label_1 = selection;
                yLabel.setText("Acres (Thousands)");
                list = databaseLandClimate.getGovernment_Forest();
            }else if (report.contains("Water Purification Points")) {
                label_1 = selection;
                yLabel.setText("Value");
                list = databaseLandClimate.getWater_Purification_Points();
            }else if (report.contains("Number of Financial Institutions")) {
                label_1 = selection;
                yLabel.setText("Value");
                list = databaseMoneyAndBanking.getFinacial_Institutions(county);
            }else if (report.matches("KCSE Examination Results")) {
                label_1 = selection;
                yLabel.setText("Value");
                list = databaseEducation.getKCSE_Examination_Results(selection,choice);
            }else if (report.contains("Households by Main Type of Floor Material for the Main Dwelling Unit")) {
                label_1 = selection;
                yLabel.setText("Number");
                list = databasePopulation.getHouseholds_by_Main_Type_of_Floor_Material_for_the_Main_Dwelling_Unit(selection);
            }else if (report.contains("Households by Main Source of Water")) {
                label_1 = selection;
                yLabel.setText("Number");
                list = databasePopulation.getHouseholds_by_Main_Source_of_Water(selection);
            }else if (report.contains("Percentage of Households by Ownership of Household Assets")) {
                label_1 = selection;
                yLabel.setText("Number");
                list = databasePopulation.getPercentage_of_Households_by_Ownership_of_Household_Assets(selection);
            }

            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Sector_Data data = list.get(i);
                    if (labels.contains(data.getYear())) {
                        Log.d("Year ", data.getYear());
                    } else {
                        labels.add(data.getYear());
                    }

                    try{
                        Log.d("Data ", data.getSet_A()+" Year "+data.getYear());
                        group1.add(new Entry(Float.parseFloat(data.getSet_A()), i));
                        group11.add(new BarEntry(Float.parseFloat(data.getSet_A()), i));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }


        if(report.matches("Expected and Registered Births and Deaths")
                ||report.matches("Births and Deaths by Sex")
                ||report.matches("Secondary School Enrollment by Level and Sex")
                ||report.matches("Student Enrolment by Sex, Technical Institutions")
                ||report.matches("Adult Education Enrolment by Sex and Subcounty")
                ||report.matches("Adult Education Proficiency Test Results")
                ||report.matches("Primary School Enrollment By Class, Sex and Subcounty")
                ||report.matches("Secondary Schools by Category and Subcounty")
                ||report.matches("Prison Population by Sentence Duration and Sex")
                ||report.contains("Primary Schools by Category")
                ||report.matches("Offence by Sex and Command Stations")
                ||report.matches("Irrigation Schemes")
                ||report.matches("Hospital Beds and Cots")
                ||report.matches("Persons Reported to have Committed Robbery and Theft")
                ||report.matches("Magistrates Judges and Practicing Lawyers")
                ||report.matches("Persons Reported to Have Committed Homicide by Sex")
                ||report.matches("People Reported to Have Committed Offence Related to Drugs")
                ||report.matches("Offences Committed Against Morality")
                ||report.matches("Offenders Serving")
                ||report.matches("Firearms and Ammunition Recovered or Surrendered")
                ||report.matches("Convicted Prisoners by Type of Offence and Sex")
                ||report.matches("Daily Average Population of Prisoners by Sex")
                ||report.matches("Murder Cases and Convictions Obtained By High Court Station")
                ||report.matches("Private Candidates Registered for KCPE by Sex")
                ||report.matches("Convicted Prison Population by Age and Sex")
                ||report.matches("Number of Refugees By Age and Sex")
                ||report.matches("KCPE Candidates")
                ||report.matches("Public Secondary School Trained Teachers")
                ||report.matches("Public Secondary School Untrained Teachers")
                ||report.matches("National Government Development and Recurrent Expenditure")
                ||report.matches("Teacher Trainees Diploma Enrolment")
                ||report.matches("Teacher Trainees Public Enrolment")
                ||report.matches("Teacher Trainees Private Enrolment")
                ||report.matches("ECDE Centres by Category and Subcounty")
                ||report.matches("Student Enrolment in Youth Polytechnics")
                ||report.matches("Youth Polytechnics by Category and Subcounty")
                ||report.matches("Registered Medical Personnel")
                ||report.matches("HIV/AIDS Awareness and Testing")
                ||report.matches("Registered Active NHIF Member by County")
                ||report.matches("Primary Enrolment and Access Indicators")
                ||report.matches("Secondary Enrolment and Access Indicators")
                ||report.matches("Total Recorded Employment")
                ||report.matches("Average Wage Earnings Per Employee in Private Sector")
                ||report.matches("Average Wage Earnings Per Employee in Public Sector")
                ||report.matches("Memorandum Items in Public Sector")
                ||report.matches("Wage Employment by Industry and Sex")
                ||report.contains("Interest Rates")
                ||report.contains("Production Area and Average Yield of Coffee")
                ||report.contains("Production Area and Average Yield of Tea")
                ||report.contains("Quantity and Value of Imports, Exports and Re-exports of Petroleum Products")
                ||report.contains("Participation in Key Decision Making Positions by Sex")
                ||report.contains("Women Groups Registration Contribution Women Enterprise Fund")
                ||report.contains("Women Groups Registration Contributions Uwezo Funds")
                ||report.contains("Population by Sex and School Attendance (3 Years and Above)")
                ){

            if(report.matches("Births and Deaths by Sex")){
                label_1 = "Births";
                label_2 = "Deaths";
                yLabel.setText("People");
                list = databasePopulation.getVital_statistics_births_and_deaths_by_sex(county,choice);
            }else if (report.matches("Expected and Registered Births and Deaths")){
                label_1 = "Births";
                label_2 = "Deaths";
                yLabel.setText("People");
                list = databasePopulation.getVital_statistics_expectedandregisteredbirthsanddeaths(county, choice);
            }else if (report.matches("Secondary School Enrollment by Level and Sex")){
                label_1 = "Boys";
                label_2 = "Girls";
                yLabel.setText("Pupils");
                list = databaseEducation.getEnrollmentsecondaryschoolsbylevelandsex(choice);
            }else if (report.matches("Student Enrolment by Sex, Technical Institutions")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Students");
                list = databaseEducation.getStudentenrollmentbysextechnicalinstitutions(selection);
            }else if  (report.matches("Adult Education Enrolment by Sex and Subcounty")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Students");
                list = databaseEducation.getAdulteducationenrolmentbysexandsubcounty(county);
            }else if (report.matches("Adult Education Proficiency Test Results")){
                label_1 = "Number sat";
                label_2 = "Number Passed";
                yLabel.setText("Students");
                list = databaseEducation.getAdulteducationproficiencytestresults(county,choice);
            }else if(report.matches("Primary School Enrollment By Class, Sex and Subcounty")) {
                label_1 = "Lower Class";
                label_2 = "Upper Class";
                yLabel.setText("Students");
                list = databaseEducation.getPrimaryschoolenrollmentbyclasssexandsubcounty(county,choice);
            }else if (report.matches("Secondary Schools by Category and Subcounty")){
                label_1 = "Public";
                label_2 = "Private";
                yLabel.setText("Schools");
                list = databaseEducation.getSecondaryschoolsbycategoryandsubcounty(county);
            }else if (report.matches("Prison Population by Sentence Duration and Sex")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Population");
                list = databaseGovernance.getPrison_population_by_sentence_duration_and_sex(selection);
            }else if (report.matches("Offence by Sex and Command Stations")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Population");
                list = databaseGovernance.getOffence_by_sex_and_command_stations(county);
            } else if (report.matches("Irrigation Schemes")) {
                label_1 = "All schemes gross value of crop in millions";
                label_2 = "All schemes payments to plot holders in millions";
                yLabel.setText("Data");
                list = databaseAgriculture.getIrrigation_schemes();
            } else if (report.contains("Primary Schools by Category")){
                label_1 = "Public";
                label_2 = "Private";
                yLabel.setText("Schools");
                Log.d("match_test", "onProgressChanged: " + "Primary Schools by Category and Subcounty");
                list = databaseEducation.getPrimaryschoolsbycategoryandsubcounty(county);
            }else if(report.matches("Hospital Beds and Cots")){
                label_1 = "Beds";
                label_2 = "Cots";
                yLabel.setText("Beds & Cots");
                list = databaseHealth.getHospitalbedsandcots(county);
            }else if(report.matches("Persons Reported to have Committed Robbery and Theft")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Criminals");
                list = databaseGovernance.getPersons_reported_to_have_committed_robbery_and_theft(selection);
            }else if(report.matches("Magistrates Judges and Practicing Lawyers")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Legal Professionals");
                list = databaseGovernance.getMagistrates_judges_and_practicing_lawyers(selection);
            }else if(report.matches("Persons Reported to Have Committed Homicide by Sex")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Criminals");
                list = databaseGovernance.getPersons_reported_to_have_committed_homicide_by_sex(selection);
            }else if(report.matches("Participation in Key Decision Making Positions by Sex")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Positions");
                list = databaseGovernance.getParticipation_in_Key_Decision_Making_Positions_by_Sex(selection);
            }else if(report.matches("People Reported to Have Committed Offence Related to Drugs")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Suspects");
                list = databaseGovernance.getPeople_reported_to_have_committed_offence_related_to_(selection);
            }else if(report.matches("Offences Committed Against Morality")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Offences");
                list = databaseGovernance.getOffences_committed_against_morality(selection);
            }else if(report.matches("Offenders Serving")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Criminals");
                list = databaseGovernance.getOffenders_serving(selection);
            }else if(report.matches("Murder Cases and Convictions Obtained By High Court Station")) {
                label_1 = "Registered Murder Cases";
                label_2 = "Murder Convictions Obtained";
                yLabel.setText("Cases");
                list = databaseGovernance.getMurder_cases_and_convictions_obtained_by_high_court_s(selection);
            }else if(report.matches("Firearms and Ammunition Recovered or Surrendered")) {
                label_1 = "Recovered";
                label_2 = "Surrendered";
                yLabel.setText("Cases");
                list = databaseGovernance.getFirearms_and_ammunition_recovered_or_surrendered(selection);
            }else if(report.matches("Convicted Prisoners by Type of Offence and Sex")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Criminals");
                list = databaseGovernance.getConvicted_prisoners_by_type_of_offence_and_sex(selection);
            }else if(report.matches("Daily Average Population of Prisoners by Sex")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Prisoners");
                list = databaseGovernance.getDaily_average_population_of_prisoners_by_sex(selection);
            }else if(report.matches("Private Candidates Registered for KCPE by Sex")){
                label_1 = "Proficiency";
                label_2 = "KCPE";
                yLabel.setText("Candidates");
                list = databaseEducation.getPrivatecandidatesregisteredforkcpebysex(county,choice);
            }else if(report.matches("Convicted Prison Population by Age and Sex")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Prisoners");
                list = databaseGovernance.getConvicted_prison_population_by_age_and_sex(selection);
            }else if(report.matches("Number of Refugees By Age and Sex")){
                label_1 = "Children";
                label_2 = "Adult";
                yLabel.setText("Refugees");
                list = databaseGovernance.getNumber_of_refugees_by_age_and_sex(choice);
            }else if(report.matches("KCPE Candidates")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("candidates");
                list = databaseEducation.getKcpecandidatesandmeansubjectscore_candidates();
            }else if(report.matches("Public Secondary School Trained Teachers")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("teachers");
                list = databaseEducation.getPublicsecondaryschooltrainedteachers(selection);
            }else if(report.matches("Public Secondary School Untrained Teachers")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("teachers");
                list = databaseEducation.getPublicsecondaryschooluntrainedteachers(selection);
            }else if (report.matches("National Government Development and Recurrent Expenditure")){
                label_1 = "Development Expenditure";
                label_2 = "Recurrent Expenditure";
                yLabel.setText("amount");
                list = databaseEducation.getNationalgovtdevelopmentandrecurrentexpenditure(selection);
            }else if(report.matches("Teacher Trainees Diploma Enrolment")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("teachers");
                list = databaseEducation.getTeachertraineesdiplomaenrolment(choice);
            }else if(report.matches("Teacher Trainees Public Enrolment")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("teachers");
                list = databaseEducation.getTeachertraineespublicenrolment(choice);
            }else if(report.matches("Teacher Trainees Private Enrolment")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("teachers");
                list = databaseEducation.getTeachertraineesprivateenrolment();
            }else if (report.matches("ECDE Centres by Category and Subcounty")){
                label_1 = "Public";
                label_2 = "Private";
                yLabel.setText("Number of Centers");
                list = databaseEducation.getEcdecentresbycategoryandsubcounty(county);
            }else if (report.matches("Student Enrolment in Youth Polytechnics")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("teachers");
                list = databaseEducation.getStudentenrolmentinyouthpolytechnics(county,choice,selection);
                //list = databaseEducation.getPrimaryschoolsbycategoryandsubcounty("Nairobi");
            }else if (report.matches("Youth Polytechnics by Category and Subcounty")){
                label_1 = "Public";
                label_2 = "Private";
                yLabel.setText("Polytechnics");
                list = databaseEducation.getYouthpolytechnicsbycategoryandsubcounty(county);
            }else if (report.matches("Registered Medical Personnel")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Number of personnel");
                list = databaseHealth.getRegisteredmedicalpersonnel(county);
            }else if (report.matches("HIV/AIDS Awareness and Testing")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Awareness");
                list = databaseHealth.getHiv_aids_awareness_and_testing(county,selection);
            }else if (report.matches("Registered Active NHIF Member by County")){
                label_1 = "Formal";
                label_2 = "Informal";
                yLabel.setText("active members");
                list = databaseHealth.getRegistered_active_nhif_members_by_county(county);
            }else if (report.matches("Primary Enrolment and Access Indicators")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Enrolment & Access Indicators");
                list = databaseEducation.getPrimaryenrolmentandaccessindicators(county,choice);
            }else if (report.matches("Secondary Enrolment and Access Indicators")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Enrolment & Access Indicators");
                list = databaseEducation.getSecondaryenrolmentandaccessindicators(county,choice);
            }else if (report.matches("Average Wage Earnings Per Employee in Private Sector")) {
                label_1 = "Modern Sector";
                label_2 = "Informal Sector";
                yLabel.setText("Total Employment");
                list = databaseLabour.getTotal_recorded_employment();
            }else if (report.matches("Average Wage Earnings Per Employee in Public Sector")) {
                label_1 = "Agriculture";
                label_2 = "Manufacturing";
                yLabel.setText("Total Employment");
                list = databaseLabour.getTotal_recorded_employment_public_sector();
            }else if (report.matches("Memorandum Items in Public Sector")) {
                label_1 = "Ministries";
                label_2 = "County Governments";
                yLabel.setText("Total Earnings");
                list = databaseLabour.getMemorandum_Items_in_Public_Sector();
            }else if (report.matches("Total Recorded Employment")) {
                label_1 = "Modern Sector";
                label_2 = "Informal Sector";
                yLabel.setText("Total Employment");
                list = databaseLabour.getTotal_recorded_employment();
            }else if (report.matches("Wage Employment by Industry and Sex")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Wage Employment");
                list = databaseLabour.getWage_employment_by_industry_and_sex(selection);
            }else if (report.contains("Interest Rates")) {
                label_1 = "Average Rates";
                label_2 = "Deposit Rate";
                yLabel.setText("Interest Rates");
                list = databaseMoneyAndBanking.getInterest_Rates_OR_Commercial_Banks_B_L_And_A(0);
            }else if (report.contains("Production Area and Average Yield of Coffee")) {
                label_1 = "Cooperatives";
                label_2 = "Estates";
                yLabel.setText("Yield");
                list = databaseAgriculture.getProduction_Area_and_Average_Yield_of_Coffee(selection);
            }else if (report.contains("Production Area and Average Yield of Tea")) {
                label_1 = "Smallholders";
                label_2 = "Estates";
                yLabel.setText("Yield");
                list = databaseAgriculture.getProduction_Area_and_Average_Yield_of_Tea(selection);
            }else if (report.contains("Quantity and Value of Imports, Exports and Re-exports of Petroleum Products")) {
                label_1 = "Quantity (Tonnes)";
                label_2 = "Value";
                yLabel.setText("Amount");
                list = databaseEnergy.getQuantity_and_Value_of_Imports_Exports_and_Reexports_of_Petroleum_Products(selection);
            }else if (report.contains("Women Groups Registration Contribution Women Enterprise Fund")) {
                label_1 = "Number of Beneficiaries";
                label_2 = "Enterprise Fund";
                yLabel.setText("Value");
                list = databaseGovernance.getWomen_Groups_Registration_Contribution_Women_Enterprise_Fund();
            }else if (report.contains("Women Groups Registration Contributions Uwezo Funds")) {
                label_1 = "Number of Beneficiaries";
                label_2 = "Uwezo Fund Disbursed";
                yLabel.setText("Value");
                list = databaseGovernance.getWomen_Groups_Registration_Contributions_Uwezo_Funds();
            }else if (report.contains("Population by Sex and School Attendance (3 Years and Above)")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Number");
                list = databasePopulation.getPopulation_by_Sex_and_School_Attendance_3_Years_and_Above(selection);
            }

            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Sector_Data data = list.get(i);
                    if(labels.contains(data.getYear())){
                        Log.d("Year ",data.getYear());
                    }else {
                        labels.add(data.getYear());
                    }
                    try {
                        Log.d("Set A ",data.getSet_A()+"");
                        Log.d("Set B ",data.getSet_B()+"");
                        group1.add(new Entry(Float.parseFloat(data.getSet_A()), i));
                        group2.add(new Entry(Float.parseFloat(data.getSet_B()), i));

                        group11.add(new BarEntry(Float.parseFloat(data.getSet_A()), i));
                        group12.add(new BarEntry(Float.parseFloat(data.getSet_B()), i));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        if (report.matches("County Budget Allocation")
                ||report.matches("Chemical Med Feed Input ")
                ||report.matches("Area Under Sugarcane Harvested")
                ||report.matches("Cooperatives")
                ||report.matches("Gross Market Production at Constant")
                ||report.matches("Price to Producers for Meat, Milk")
                ||report.matches("Total Share Capital")
                ||report.matches("Value of Agricultural Inputs")
                ||report.matches("Economic Classification Revenue")
                ||report.matches("Excise Revenue Commodity")
                ||report.matches("National Government Expenditure")
                ||report.matches("National Government Expenditure By Purpose")
                ||report.matches("Outstanding Debt International Organization")
                ||report.matches("Outstanding Debt Lending Country")
                ||report.matches("Population by Sex, Households, Density and Census Years")
                ||report.matches("NHIF Members")
                ||report.matches("Hotel Occupancy By Residence")
                ||report.matches("Hotel Occupancy By Zone")
                ||report.matches("Domestic Credit")
                ||report.matches("Tourism Visitor To Parks")
                ||report.matches("Tourism Visitors To Museums")
                ||report.matches("NHIF Resources")
                ||report.matches("Approved Degree Diploma Programs")
                ||report.matches("Student Enrolment in Public Universities")
                ||report.matches("Teacher Training Colleges")
                ||report.matches("Cases Handled By Various Courts")
                ||report.matches("Identity Cards Made Processed and Collected")
                ||report.matches("Public Assets Traced, Recovered and Loss Averted")
                ||report.matches("County Expenditure")
                ||report.matches("National Trends KCSE Candidates Mean Grade by Sex")
                ||report.matches("Average Monthly Pump Prices for Fuel by Category")
                ||report.matches("Registered Voters by County and by Sex")
                ||report.matches("Education Institutions")
                ||report.matches("Administrative Units")
                ||report.matches("Maternal Care")
                ||report.matches("Nutritional Status of Children")
                ||report.contains("Land and Climate Surface Area by Category")
                ||report.contains("Women Groups Registration Contributions Women Groups")
                ) {

            if(report.matches("Chemical Med Feed Input ")){
                label_1 = "Cattle Feed";
                label_2 = "Insecticides";
                label_3 = "Other Livestock Drugs";
                yLabel.setText("Feed Input");
                list = databaseAgriculture.getChemical_med_feed_input();
            }else if(report.matches("Area Under Sugarcane Harvested")){
                label_1 = "Area Under Cane";
                label_2 = "Area Harvested";
                label_3 = "Average Yield Per Hectare (Tonnes)";
                yLabel.setText("Yield");
                list = databaseAgriculture.getSugarcane_Harvested_Production_and_Average_Yield();
            }else if (report.matches("County Budget Allocation")){
                Log.d("l_and_c", "onProgressChanged: ");
                label_1 = "Recurrent";
                label_2 = "Development";
                label_3 = "Total";
                yLabel.setText("Allocation");
                list = databaseFinance.getCounty_budget_allocation(county);
            }else if (report.matches("Cooperatives")){
                label_1 = "Coffee";
                label_2 = "Saccos";
                label_3 = "Unions";
                yLabel.setText("Cooperatives");
                list = databaseAgriculture.getCooperatives();
            }else if (report.matches("Gross Market Production at Constant")){
                label_1 = "Cattle and calves for slaughter";
                label_2 = "Poultry and eggs";
                label_3 = "Total Crops";
                yLabel.setText("Gross market production");
                list = databaseAgriculture.getGross_market_production();
            }else if (report.matches("Price to Producers for Meat, Milk")){
                label_1 = "Beef third grade per kg";
                label_2 = "Pig meat per kg";
                label_3 = "Whole milk per litre";
                yLabel.setText("Quantity");
                list = databaseAgriculture.getPricetoproducersformeatmilk();
            }else if (report.matches("Total Share Capital")){
                label_1 = "Coffee";
                label_2 = "Saccos";
                label_3 = "Unions";
                yLabel.setText("Share Capital");
                list = databaseAgriculture.getTotalsharecapital();
            }else if (report.matches("Value of Agricultural Inputs")){
                label_1 = "Fertilizers";
                label_2 = "Fuel";
                label_3 = "Seeds";
                yLabel.setText("Input Value");
                list = databaseAgriculture.getValueofagriculturalinputs();
            }else if (report.matches("Economic Classification Revenue")){
                label_1 = "Total Tax revenue";
                label_2 = "Total Nontax revenue";
                label_3 = "Total";
                yLabel.setText("Revenue");
                list = databaseFinance.getEconomic_classification_revenue();
            }else if (report.matches("Excise Revenue Commodity")){
                label_1 = "Beer";
                label_2 = "Cigarettes";
                label_3 = "Wines and spirits";
                yLabel.setText("Revenue");
                list = databaseFinance.getExcise_revenue_commodity();
            }else if (report.matches("National Government Expenditure")){
                label_1 = "Loans Repayments";
                label_2 = "Compensation for Employees";
                label_3 = "Goods and Services";
                yLabel.setText("Expenditure");
                list = databaseFinance.getNational_government_expenditure();
            }else if (report.matches("National Government Expenditure By Purpose")){
                label_1 = "Transport";
                label_2 = "Health";
                label_3 = "Education";
                yLabel.setText("Expenditure");
                list = databaseFinance.getNational_government_expenditure_purpose();
            }else if (report.matches("Outstanding Debt International Organization")){
                label_1 = "EEC EIB";
                label_2 = "IMF";
                label_3 = "ADF ADB";
                yLabel.setText("Debt");
                list = databaseFinance.getOutstanding_debt_international_organization();
            }else if (report.matches("Outstanding Debt Lending Country")){
                label_1 = "Germany";
                label_2 = "France";
                label_3 = "China";
                yLabel.setText("Debt");
                list = databaseFinance.getOutstanding_debt_lending_country();
            }else if (report.matches("Population by Sex, Households, Density and Census Years")){
                label_1 = "Male";
                label_2 = "Female";
                label_3 = "Total";
                yLabel.setText("Population");
                list = databasePopulation.getPopulationbysexhouseholdsdensityandcensusyears();
            }else if (report.matches("NHIF Members")){
                label_1 = "Formal sector";
                label_2 = "Informal sector";
                label_3 = "Total";
                yLabel.setText("Members");
                list = databaseHealth.getNhif_members();
            }else if (report.matches("Hotel Occupancy By Residence")){
                label_1 = "Germany";
                label_2 = "France";
                label_3 = "switzerland";
                yLabel.setText("Residents");
                list = databaseTourism.getHotel_Occupancy_By_Residence();
            }else if (report.matches("Hotel Occupancy By Zone")){
                label_1 = "Germany";
                label_2 = "France";
                label_3 = "switzerland";
                yLabel.setText("Residents");
                list = databaseTourism.getHotel_Occupancy_By_Zone();
            }else if (report.matches("Domestic Credit")){
                label_1 = "Private/Public Bodies";
                label_2 = "Government";
                label_3 = "Total";
                yLabel.setText("amount");
                list = databaseMoneyAndBanking.getDomestic_Credit();
            }else if (report.matches("Tourism Visitor To Parks")){
                label_1 = "Nairobi";
                label_2 = "Nairobi Safari Walk";
                label_3 = "Amboseli";
                yLabel.setText("Visitors");
                list = databaseTourism.getTourism_visitor_to_parks();
            }else if (report.matches("Tourism Visitors To Museums")){
                label_1 = "Nairobi Snake Park";
                label_2 = "Fort Jesus";
                label_3 = "Karen Blixen";
                yLabel.setText("Visitors");
                list = databaseTourism.getTourism_visitor_to_museums();
            }else if(report.matches("NHIF Resources")){
                label_1 = "Benefits";
                label_2 = "Contributions net benefits";
                label_3 = "Receipts";
                yLabel.setText("Resources");
                list = databaseHealth.getNhif_resources();
            }else if(report.matches("Approved Degree Diploma Programs")){
                label_1 = "Approved Degree Programmes";
                label_2 = "Approved Private University Degree Programmes";
                label_3 = "Validated Diploma Programmes";
                yLabel.setText("Programmes");
                list = databaseEducation.getApproved_degree_diploma_programs();
            }else if (report.matches("Student Enrolment in Public Universities")){
                label_1 = "Undergraduates";
                label_2 = "Postgraduates";
                label_3 = "Other";
                yLabel.setText("Enrolment");
                list = databaseEducation.getStudentenrollmentpublicuniversities();
            }else if (report.matches("Teacher Training Colleges")) {
                label_1 = "Pre primary";
                label_2 = "Primary";
                label_3 = "Secondary";
                yLabel.setText("Teachers");
                list = databaseEducation.getTeachertrainingcolleges(county,choice);
            }else if(report.matches("Cases Handled By Various Courts")){
                label_1 = "Filled";
                label_2 = "Pending";
                label_3 = "Disposed of";
                yLabel.setText("Cases");
                list = databaseGovernance.getCases_handled_by_various_courts(selection);
            }else if(report.matches("Identity Cards Made Processed and Collected")){
                label_1 = "Applications Made";
                label_2 = "IDs produced";
                label_3 = "IDs Collected";
                yLabel.setText("Teachers");
                list = databaseGovernance.getIdentity_cards_made_processed_and_collected(county);
            }else if(report.matches("Public Assets Traced, Recovered and Loss Averted")){
                label_1 ="Public Assets Traced";
                label_2 = "Public Assets Recovered";
                label_3 = "Loss Averted";
                yLabel.setText("Public Assets");
                list = databaseGovernance.getPublic_assets_traced_recovered_and_loss_averted();
            }else if(report.matches("County Expenditure")){
                label_1 = "Employee Compensation";
                label_2 = "Goods and Services";
                label_3 = "Total Expenditure";
                yLabel.setText("Schools");
                list = databaseFinance.getCounty_expenditure(county);
            }else if(report.contains("Land and Climate Surface Area by Category")){
                Log.d("l_and_c", "onProgressChanged: ");
                label_1 = "Employee Compensation";
                label_2 = "Goods and Services";
                label_3 = "Total Expenditure";
                //label_4
                yLabel.setText("Schools");
                list = databaseLandClimate.getLand_and_Climate_Surface_Area_by_Category(county);
            }else if(report.contains("Women Groups Registration Contributions Women Groups")){
                Log.d("l_and_c", "onProgressChanged: ");
                label_1 = "Number of Groups";
                label_2 = "Membership";
                label_3 = "Contributions";
                //label_4
                yLabel.setText("Value");
                list = databaseGovernance.getWomen_Groups_Registration_Contributions_Women_Groups();
            }else if (report.matches("National Trends KCSE Candidates Mean Grade by Sex")){
                if(selection.matches("Grade A and E")){
                    label_1 = "Grade A plain";
                    label_2 = "Grade A minus";
                    label_3 = "Grade E plain";
                }else if(selection.matches("Grade B")){
                    label_1 = "Grade B plus";
                    label_2 = "Grade B plain";
                    label_3 = "Grade B minus";
                }else if(selection.matches("Grade C")){
                    label_1 = "Grade C plus";
                    label_2 = "Grade C plain";
                    label_3 = "Grade C minus";
                }else if(selection.matches("Grade D")){
                    label_1 = "Grade D plus";
                    label_2 = "Grade D plain";
                    label_3 = "Grade D minus";
                }

                yLabel.setText("Mean Grade");
                list = databaseEducation.getNationaltrendskcsecandidatesmeangradebysex(choice,selection);
            }else if (report.matches("Average Monthly Pump Prices for Fuel by Category")){
                label_1 = "Super Petrol";
                label_2 = "Diesel";
                label_3 = "Kerosene";
                yLabel.setText("Petrol");
                list = databaseEnergy.getEnergy_averagemonthlypumppricesforfuelbycategory(county);
            }else if (report.matches("Registered Voters by County and by Sex")) {
                label_1 = "Male";
                label_2 = "Female";
                label_3 = "Total";
                yLabel.setText("Voters");
                list = databaseGovernance.getRegistered_voters_by_county_and_by_sex(county);
            }else if(report.matches("Education Institutions")){
                label_1 = "Pre-Primary";
                label_2 = "Primary";
                label_3 = "Secondary";
                yLabel.setText("institutions");
                list = databaseEducation.getEducationalinstitutions_schools(choice);
            }else if(report.matches("Administrative Units")){
                label_1 = "Divisions";
                label_2 = "Locations";
                label_3 = "Sub Locations";
                yLabel.setText("institutions");
                list = dbHelper.getAdministrative_unit(county);
            }else if(report.matches("Maternal Care")){
                label_1 = "Receiving Antenatal Care From a Skilled Provider";
                label_2 = "Delivered in a Health Facility";
                label_3 = "Delivered by a Skilled Provider";
                yLabel.setText("percentage");
                list = databaseHealth.getMaternal_care(county);
            }else if(report.matches("Nutritional Status of Children")){
                label_1 = "Stunted";
                label_2 = "Wasted";
                label_3 = "Under Weight";
                yLabel.setText("percentage");
                list = databaseHealth.getNutritional_status_of_children(county);
            }


            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Sector_Data data = list.get(i);
                    if(!labels.contains(data.getYear())){
                        labels.add(data.getYear());
                    }

                    try {
                        group1.add(new Entry(Float.parseFloat(data.getSet_A()), i));
                        group2.add(new Entry(Float.parseFloat(data.getSet_B()), i));
                        group3.add(new Entry(Float.parseFloat(data.getSet_C()), i));

                        group11.add(new BarEntry(Float.parseFloat(data.getSet_A()), i));
                        group12.add(new BarEntry(Float.parseFloat(data.getSet_B()), i));
                        group13.add(new BarEntry(Float.parseFloat(data.getSet_C()), i));
                        Log.d("Data ", data.getSet_A() + "," + data.getSet_A());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }

        if(report.matches("Population Projections by Selected Age Group")
                ||report.matches("Population Projections by Special Age Group")
                ||report.matches("Death")
                ||report.matches("Diseases 2009 to 2015")
                ||report.matches("Medical Personnel")
                ||report.matches("Number of Educational Institutions")
                ||report.matches("Secondary School Enrollment By Class, Sex and Subcounty")
                ||report.matches("Passports, Work Permits and Foreigners Registered")
                ||report.matches("County Revenue")
                ||report.matches("Distribution of Outpatient Visits by Type of Health Care")
                ||report.matches("Insurance Coverage by Counties and Types")
                ||report.matches("Nutritional Status of Women")
                ){

            if(report.matches("Population Projections by Selected Age Group")){
                label_1 = "Range 0-19";
                label_2 = "Range 20-39";
                label_3 = "Range 40-59";
                label_4 = "Range 60 plus";
                yLabel.setText("Population");
                list = databasePopulation.getPopulationprojectionsbyselectedagegroup(county);
            }else if (report.matches("Population Projections by Special Age Group")){
                label_1 = "Range 0-13";
                label_2 = "Range 14-34";
                label_3 = "Range 15-64";
                label_4 = "Range 65 plus";
                yLabel.setText("Population");
                list = databasePopulation.getPopulationprojectionsbyspecialagegroups(county,choice);
            }else if (report.matches("Death")){
                label_1 = "Cancer";
                label_2 = "HIV/AIDS";
                label_3 = "Malaria";
                label_4 = "Pneumonia";
                yLabel.setText("People");
                list = databaseHealth.getDeaths();
            }else if(report.matches("Diseases 2009 to 2015")){
                label_1 = "Accidents";
                label_2 = "Diarrhoea";
                label_3 = "Malaria";
                label_4 = "Pneumonia";
                yLabel.setText("People");
                list = databaseHealth.getDiseases();
            }else if (report.matches("Medical Personnel")){
                label_1 = "Clinical Officers";
                label_2 = "Dentists";
                label_3 = "Doctors";
                label_4 = "Registered Nurses";
                yLabel.setText("Medics");
                list = databaseHealth.getMedical_personnel();
            }else if (report.matches("Number of Educational Institutions")){
                label_1 = "Primary & Secondary Schools";
                label_2 = "Teacher Training Colleges";
                label_3 = "Tivet Institutions";
                label_4 = "Universities";
                yLabel.setText("Institutions");
                list = databaseEducation.getNumber_educational_institutions();
            }else if (report.matches("Secondary School Enrollment By Class, Sex and Subcounty")){
                label_1 = "Form 1";
                label_2 = "Form 2";
                label_3 = "Form 3";
                label_4 = "Form 4";
                yLabel.setText("Enrolment");
                list = databaseEducation.getSecondaryschoolenrollmentbyclasssexsubcounty(county,choice);
            }else if (report.matches("Passports, Work Permits and Foreigners Registered")){
                label_1 = "Passport Issued";
                label_2 = "Foreign National Registered";
                label_3 = "Work permit Issued";
                label_4 = "Work permit Renewed";
                yLabel.setText("Enrolment");
                list = databaseGovernance.getPassports_work_permits_and_foreigners_registered();
            } else if(report.matches("County Revenue")) {
                label_1 = "Revenue Estimates";
                label_2 = "Conditional Grants";
                label_3 = "Equitable Share";
                label_4 = "Total revenue";
                yLabel.setText("revenue");
                list = databaseFinance.getCounty_revenue(county);
            } else if(report.matches("Distribution of Outpatient Visits by Type of Health Care")) {
                label_1 = "Public";
                label_2 = "Private";
                label_3 = "Faith Based";
                label_4 = "Others";
                yLabel.setText("visits");
                list = databaseHealth.getDistributionofoutpatientvisitsbytypeofhealthcareprovider(county);
            } else if(report.matches("Insurance Coverage by Counties and Types")) {
                label_1 = "Insured";
                label_2 = "NHIF";
                label_3 = "CBHI";
                label_4 = "Private";
                yLabel.setText("coverage");
                list = databaseHealth.getInsurance_coverage_by_counties_and_types(county);
            }else if(report.matches("Nutritional Status of Women")) {
                label_1 = "Under Nutrition";
                label_2 = "Normal";
                label_3 = "Overweight";
                label_4 = "Obese";
                yLabel.setText("status");
                list = databaseHealth.getNutritional_status_of_women(county);
            }

            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Sector_Data data = list.get(i);
                    labels.add(data.getYear());

                    try {

                            group1.add(new Entry(Float.parseFloat(data.getSet_A()), i));
                            group2.add(new Entry(Float.parseFloat(data.getSet_B()), i));
                            group3.add(new Entry(Float.parseFloat(data.getSet_C()), i));
                            group4.add(new Entry(Float.parseFloat(data.getSet_D()), i));

                            group11.add(new BarEntry(Float.parseFloat(data.getSet_A()), i));
                            group12.add(new BarEntry(Float.parseFloat(data.getSet_B()), i));
                            group13.add(new BarEntry(Float.parseFloat(data.getSet_C()), i));
                            group14.add(new BarEntry(Float.parseFloat(data.getSet_D()), i));

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }

        if(report.matches("Trends in Environment and Natural Resources")
                ||report.matches("Elementary Aggregates Weights in the CPI Baskets")
                ||report.matches("Group Weights for Kenya CPI February Base Period 2009")
                ||report.matches("Group Weights for Kenya CPI October Base Period 1997")
                ){

            if(report.matches("Trends in Environment and Natural Resources")){
                label_1 = "Forestry and Logging";
                label_2 = "Fishing and Aquaculture";
                label_3 = "Mining and quarrying";
                label_4 = "Water Supply";
                label_5 = "GDP at Current Prices";
                yLabel.setText("Population");
                list = databaseLandClimate.getLand_and_trends_in_environment_and_natural_resources();
            }else if (report.matches("Elementary Aggregates Weights in the CPI Baskets")) {
                label_1 = "Nairobi Lower";
                label_2 = "Nairobi Middle";
                label_3 = "Nairobi Upper";
                label_4 = "Rest of Urban Areas";
                label_5 = "Kenya";
                yLabel.setText("CPI Basket");
                list = databaseCPI.getCpi_elementary_aggregates_weights_in_the_cpi_baskets(selection);
            }else if (report.matches("Group Weights for Kenya CPI February Base Period 2009")) {
                label_1 = "Nairobi Lower";
                label_2 = "Nairobi Middle";
                label_3 = "Nairobi Upper";
                label_4 = "Rest of Urban Areas";
                label_5 = "Kenya";
                yLabel.setText("CPI Basket");
                list = databaseCPI.getCpi_group_weights_for_kenya_cpi_febuary_base_2009(selection);
            }else if (report.matches("Group Weights for Kenya CPI October Base Period 1997")) {
                label_1 = "Nairobi Lower";
                label_2 = "Nairobi Middle";
                label_3 = "Nairobi Upper";
                label_4 = "Rest of Urban Areas";
                label_5 = "Kenya";
                yLabel.setText("CPI Basket");
                list = databaseCPI.getCpi_group_weights_for_kenya_cpi_october_base_1997(selection);
            }

            if (list != null) {
                for (int i = 0; i < list.size(); i++) {
                    Sector_Data data = list.get(i);
                    labels.add(data.getYear());

                    try {

                        group1.add(new Entry(Float.parseFloat(data.getSet_A()), i));
                        group2.add(new Entry(Float.parseFloat(data.getSet_B()), i));
                        group3.add(new Entry(Float.parseFloat(data.getSet_C()), i));
                        group4.add(new Entry(Float.parseFloat(data.getSet_D()), i));
                        group5.add(new Entry(Float.parseFloat(data.getTotal()), i));

                        group11.add(new BarEntry(Float.parseFloat(data.getSet_A()), i));
                        group12.add(new BarEntry(Float.parseFloat(data.getSet_B()), i));
                        group13.add(new BarEntry(Float.parseFloat(data.getSet_C()), i));
                        group14.add(new BarEntry(Float.parseFloat(data.getSet_D()), i));
                        group15.add(new BarEntry(Float.parseFloat(data.getTotal()), i));

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        }

        /*Stop*/

         /*Group 1*/
        LineDataSet lineDataSet1 = new LineDataSet(group1, label_1);
        lineDataSet1.setColor(Color.rgb(0, 155, 0));

        BarDataSet barDataSet1 = new BarDataSet(group11, label_1);
        barDataSet1.setColor(Color.rgb(0, 155, 0));

        /*Group 2*/
        BarDataSet barDataSet2 = new BarDataSet(group12, label_2);
        barDataSet2.setColor(Color.rgb(255, 0, 0));

        LineDataSet lineDataSet2 = new LineDataSet(group2, label_2);
        lineDataSet2.setColor(Color.rgb(255, 0, 0));

        /*Group 3*/
        BarDataSet barDataSet3 = new BarDataSet(group13, label_3);
        barDataSet3.setColor(Color.rgb(0, 0, 255));

        LineDataSet lineDataSet3 = new LineDataSet(group3, label_3);
        lineDataSet3.setColor(Color.rgb(0, 0, 255));

        /*Group 4*/
        BarDataSet barDataSet4 = new BarDataSet(group14, label_4);
        barDataSet4.setColor(Color.rgb(102, 0, 102));

        LineDataSet lineDataSet4 = new LineDataSet(group4, label_4);
        lineDataSet4.setColor(Color.rgb(102, 0, 102));

        /*Group 5*/
        BarDataSet barDataSet5 = new BarDataSet(group15, label_5);
        barDataSet5.setColor(Color.rgb(70, 50, 102));

        LineDataSet lineDataSet5 = new LineDataSet(group4, label_5);
        lineDataSet5.setColor(Color.rgb(70, 50, 102));

        ArrayList<BarDataSet> dataset_bar = new ArrayList<>();
        ArrayList<LineDataSet> dataset_line = new ArrayList<>();

        dataset_line.add(lineDataSet1);
        dataset_line.add(lineDataSet2);
        dataset_line.add(lineDataSet3);
        dataset_line.add(lineDataSet4);
        dataset_line.add(lineDataSet5);

        dataset_bar.add(barDataSet1);
        dataset_bar.add(barDataSet2);
        dataset_bar.add(barDataSet3);
        dataset_bar.add(barDataSet4);
        dataset_bar.add(barDataSet5);

        //////////////////////////////////////////

        /*Line Graph*/
        // XAxis settings
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getXAxis().setDrawAxisLine(true);
        lineChart.setExtraBottomOffset(10);
        lineChart.getXAxis().setDrawGridLines(false);
        Legend l = lineChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setYOffset(5f);
        l.setXOffset(15f);
        l.setYEntrySpace(0f);
        l.setTextSize(8f);


        lineChart.getAxisRight().setEnabled(false);
        lineChart.getAxisLeft().setStartAtZero(false);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setAxisMinValue(0); // start at zero
        yAxis.setValueFormatter(new LargeValueFormatter());

        LineData data_line = new LineData(labels, dataset_line);
        data_line.setValueFormatter(new LargeValueFormatter());
        lineChart.setData(data_line);
        lineChart.setDescription("");
        lineChart.animateY(3000);

       /*Bar Graph*/
        // XAxis settings
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawAxisLine(true);
        barChart.setExtraBottomOffset(10);

        Legend lb = barChart.getLegend();
        lb.setWordWrapEnabled(true);
        lb.setYOffset(5f);
        lb.setXOffset(15f);
        lb.setYEntrySpace(0f);
        lb.setTextSize(8f);

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setStartAtZero(false);
        YAxis yAxisBar = barChart.getAxisLeft();
        yAxisBar.setDrawGridLines(false);
        yAxisBar.setAxisMinValue(0); // start at zero
        yAxisBar.setValueFormatter(new LargeValueFormatter());

        BarData data_bar = new BarData(labels, dataset_bar);
        data_bar.setValueFormatter(new LargeValueFormatter());
        barChart.setData(data_bar);
        barChart.setDescription("");
        barChart.animateY(3000);

    }

    public void showLine(View view) {
        lineChart.setVisibility(View.VISIBLE);
        barChart.setVisibility(View.GONE);
    }

    public void showBar(View view) {
        lineChart.setVisibility(View.GONE);
        barChart.setVisibility(View.VISIBLE);
    }


    private class ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        //get strings of first item
        private String firstItem = String.valueOf(select.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            if (firstItem.equals(String.valueOf(select.getSelectedItem()))) {
                // ToDo when first item is selected
                selection = parent.getItemAtPosition(0).toString();
                onProgressChanged();

            } else {
                //Toast.makeText(parent.getContext(), parent.getItemAtPosition(pos).toString()+" County selected", Toast.LENGTH_LONG).show();
                selection = parent.getItemAtPosition(pos).toString();
                onProgressChanged();
                // Todo when item is selected by the user
            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }


    private void exportTheDB() throws IOException
    {
        File myFile;
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
        String TimeStampDB = sdf.format(cal.getTime());

        String pathToSaveDBFile = "data/data/com.app.knbs/databases";
        SQLiteDatabase KNBS_db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        try {

            myFile = new File(android.os.Environment.getExternalStorageDirectory(),
                    "KNBS"+"/Export_"+TimeStampDB+".csv");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append("Start time;End time;Elapse;Sports type");
            myOutWriter.append("\n");

            Cursor c = KNBS_db.rawQuery("SELECT * FROM  sectors ", null);

            if (c != null) {
                if (c.moveToFirst()) {
                    do {

                        String start_Time = c.getString(c.getColumnIndex("startTimeDate"));
                        String end_Time = c.getString(c.getColumnIndex("endTimeDate"));
                        String sport_Name = c.getString(c.getColumnIndex("label"));

                        myOutWriter.append(start_Time).append(";").append(end_Time).append(";").append(sport_Name);
                        myOutWriter.append("\n");
                    }

                    while (c.moveToNext());
                }

                c.close();
                myOutWriter.close();
                fOut.close();

            }
        } catch (SQLiteException se)
        {
            Log.e(getClass().getSimpleName(),"Could not create or Open the database");
        }

        finally {
            KNBS_db.close();
        }

    }


}

