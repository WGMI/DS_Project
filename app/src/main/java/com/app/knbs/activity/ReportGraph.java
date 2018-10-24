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
import com.app.knbs.database.sectors.DatabaseHousing;
import com.app.knbs.database.sectors.DatabaseICT;
import com.app.knbs.database.sectors.DatabaseLabour;
import com.app.knbs.database.sectors.DatabaseLandClimate;
import com.app.knbs.database.sectors.DatabaseManufacturing;
import com.app.knbs.database.sectors.DatabaseMoneyAndBanking;
import com.app.knbs.database.sectors.DatabasePolitical;
import com.app.knbs.database.sectors.DatabasePopulation;
import com.app.knbs.database.sectors.DatabasePoverty;
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
    private DatabasePolitical databasePolitical;
    private DatabaseHousing databaseHousing;
    private DatabaseICT databaseICT;
    private DatabasePoverty databasePoverty;

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
        databasePolitical = new DatabasePolitical(this);
        databaseHousing = new DatabaseHousing(this);
        databaseICT = new DatabaseICT(this);
        databasePoverty = new DatabasePoverty(this);

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
                ||report.matches("Percentage Incidence of Diseases in Kenya by Type and Year")
                ||report.contains("Petroleum Supply and Demand")
                ||report.contains("Net Domestic Sale of Petroleum Fuels by Consumer Category")
                ||report.contains("Installed and Effective Capacity of Electricity")
                ||report.contains("Generation and Imports of Electricity")
                ||report.contains("Participation in Key Decision Making Positions by Sex")
                ||report.contains("Population by Sex and School Attendance (3 Years and Above)")
                ||report.contains("Households by Main Type of Floor Material for the Main Dwelling Unit")
                ||report.contains("Households by Main Source of Water")
                ||report.contains("Percentage of Households by Ownership of Household Assets")
                ||report.contains("Values of Total Exports to European Union")
                ||report.contains("Quarterly Civil Engineering Cost Index")
                ||report.matches("Average Wage Earnings Per Employee in Private Sector")
                ||report.contains("Quantity and Value of Imports, Exports and Re-exports of Petroleum Products")
                ||report.contains("Surface Area by Category")
                ||report.contains("Quantity and Value of Fish Landed")
                ||report.contains("Quantity of Total Minerals")
                ||report.contains("Value of Total Minerals")
                ||report.contains("Population by Sex and Age Groups")
                ||report.contains("Population of Children under 18 by orphanhood")
                ||report.contains("Population Marital Status above 18 years")
                ||report.contains("Experience of Domestic Violence By Age")
                ||report.contains("Experience of Domestic Violence by Marital Success")
                ||report.contains("Experience of Domestic Violence by Residence")
                ||report.contains("Knowledge and Prevalence of Female Circumcision")
                ||report.contains("Members of National Assembly and Senators")
                ||report.contains("Prevalence Female Circumcision and Type")
                ||report.contains("Fertility by Education Status")
                ||report.contains("Fertility Rate by Age and Residence")
                ||report.contains("Early Childhood Mortality Rates by Sex")
                ||report.contains("Place of Delivery")
                ||report.contains("Prevalence of Overweight and Obesity")
                ||report.contains("Percentage of Adults 15+ old who are current users of various smokeless tobacco products by sex")
                ||report.contains("Percentage of Women and Men aged 15-49 who drink alcohol by age, 2014")
                ||report.contains("Kihibs children by additional supplement")
                ||report.contains("Kihibs children assisted at birth")
                ||report.contains("Kihibs children immunized against measles")
                ||report.contains("distribution of households by pointofpurchasedfooditems")
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
                list = databaseGovernance.getCases_handled_by_different_courts_category();
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
            }else if (report.matches("Percentage Incidence of Diseases in Kenya by Type and Year")){
                selectionLabel.setText("Disease");
                list = databaseHealth.getDiseases_For_Incidence();
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
            }else if (report.contains("Values of Total Exports to European Union")){
                selectionLabel.setText("Country");
                list = databaseTradeCommerce.getEuropean_Countries();
            }else if (report.contains("Quarterly Civil Engineering Cost Index")){
                selectionLabel.setText("Material");
                list = databaseBuildingConstruction.getMaterials();
            }else if (report.matches("Average Wage Earnings Per Employee in Private Sector")){
                selectionLabel.setText("Category");
                list = databaseLabour.getPrivate_Sector_Categories();
            }else if (report.contains("Surface Area by Category")){
                selectionLabel.setText("Category");
                Log.d("DatabaseHelper_list", "onCreate: list works");
                list = databaseLandClimate.getSurface_Categories();
            }else if (report.contains("Quantity and Value of Fish Landed")){
                selectionLabel.setText("Category");
                list = databaseLandClimate.getFishCategories();
            }else if (report.contains("Quantity of Total Minerals")){
                selectionLabel.setText("Description");
                list = databaseLandClimate.getMineralDescriptions();
            }else if (report.contains("Value of Total Minerals")){
                selectionLabel.setText("Description");
                list = databaseLandClimate.getMineralDescriptions_Value();
            }else if (report.contains("Population by Sex and Age Groups")){
                selectionLabel.setText("Age Group");
                list = databasePopulation.getAgeGroups();
            }else if (report.contains("Population of Children under 18 by orphanhood")){
                selectionLabel.setText("Category");
                list = databasePopulation.getOrphanCategories();
            }else if (report.contains("Population Marital Status above 18 years")){
                selectionLabel.setText("Category");
                list = databasePopulation.getMaritalCategories();
            }else if (report.contains("Experience of Domestic Violence By Age")){
                selectionLabel.setText("Age Group");
                list = databaseGovernance.getDomesticViolenceAgeGroups();
            }else if (report.contains("Experience of Domestic Violence by Marital Success")){
                selectionLabel.setText("Status");
                list = databaseGovernance.getDomesticViolenceMaritalStatus();
            }else if (report.contains("Experience of Domestic Violence by Residence")){
                selectionLabel.setText("Residence");
                list = databaseGovernance.getDomesticViolenceResidences();
            }else if (report.contains("Knowledge and Prevalence of Female Circumcision")){
                selectionLabel.setText("Age Group");
                list = databaseGovernance.getCircumcisionKnowledgeAgeGroups();
            }else if (report.contains("Members of National Assembly and Senators")){
                selectionLabel.setText("Category");
                list = databaseGovernance.getMNACategories();
            }else if (report.contains("Prevalence Female Circumcision and Type")){
                selectionLabel.setText("Age Group");
                list = databaseGovernance.getFemaleCircumcisionAgeGroups();
            }else if (report.contains("Fertility by Education Status")){
                selectionLabel.setText("Status");
                list = databaseHealth.getEducationStatuses();
            }else if (report.contains("Fertility Rate by Age and Residence")){
                selectionLabel.setText("Age Group");
                list = databaseHealth.getFertilityAgeGroups();
            }else if (report.contains("Early Childhood Mortality Rates by Sex")){
                selectionLabel.setText("Status");
                list = databaseHealth.getMortalityStatus();
            }else if (report.contains("Place of Delivery")){
                selectionLabel.setText("Status");
                list = databaseHealth.getDeliveryPlaces();
            }else if (report.contains("Prevalence of Overweight and Obesity")){
                selectionLabel.setText("Age Groups");
                list = databaseHealth.getOverweightAgeGroups();
            }else if (report.contains("Percentage of Adults 15+ old who are current users of various smokeless tobacco products by sex")){
                selectionLabel.setText("Category");
                list = databaseHealth.getTobaccoCategories();
            }else if (report.contains("Percentage of Women and Men aged 15-49 who drink alcohol by age, 2014")){
                selectionLabel.setText("Age Groups");
                list = databaseHealth.getDrinkerAgeGroups();
            }else if (report.contains("Kihibs children by additional supplement")){
                selectionLabel.setText("Supplement");
                list = databaseHealth.getSupplements();
            }else if (report.contains("Kihibs children assisted at birth")){
                selectionLabel.setText("Assistant");
                list = databaseHealth.getAssistant();
            }else if (report.contains("Kihibs children immunized against measles")){
                selectionLabel.setText("Category");
                list = databaseHealth.getMeaslesCategory();
            }else if (report.contains("distribution of households by pointofpurchasedfooditems")){
                selectionLabel.setText("Point of Purchase");
                list = databasePoverty.getpointofpurchasedfooditems();
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
        }else if(report.contains("Land Potential")){
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

                if(report.contains("Land Potential")){
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
                ||report.contains("Land Potential")
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
                ||report.matches("Percentage Incidence of Diseases in Kenya by Type and Year")
                ||report.matches("Average Retail Prices of Selected Petroleum Products")
                ||report.matches("Petroleum Supply and Demand")
                ||report.matches("Net Domestic Sale of Petroleum Fuels by Consumer Category")
                ||report.contains("Installed and Effective Capacity of Electricity")
                ||report.contains("Generation and Imports of Electricity")
                ||report.contains("Development Expenditure Water")
                ||report.contains("Average Export Prices Ash")
                ||report.contains("Water Purification Points")
                ||report.contains("Number of Financial Institutions")
                ||report.matches("KCSE Examination Results")
                ||report.contains("Households by Main Type of Floor Material for the Main Dwelling Unit")
                ||report.contains("Households by Main Source of Water")
                ||report.contains("Percentage of Households by Ownership of Household Assets")
                ||report.contains("Values of Total Exports to European Union")
                ||report.matches("Average Wage Earnings Per Employee in Private Sector")
                ||report.matches("Proportion of Population That Took A Trip")
                ||report.contains("Quantity and Value of Imports, Exports and Re-exports of Petroleum Products")
                ||report.contains("Rainfall")
                ||report.contains("Surface Area by Category")
                ||report.contains("Temperature")
                ||report.contains("Quantity and Value of Fish Landed")
                ||report.contains("Quantity of Total Minerals")
                ||report.contains("Value of Total Minerals")
                ||report.contains("Population of Children under 18 by orphanhood")
                ||report.contains("Population Marital Status above 18 years")
                ||report.contains("Fertility by Education Status")
                ||report.contains("Place of Delivery")
                ||report.contains("Prevalence of Overweight and Obesity")
                ||report.contains("Kihibs children by additional supplement")
                ||report.contains("Kihibs children assisted at birth")
                ||report.contains("Kihibs children immunized against measles")
                ||report.contains("population with mobile phone and averagesims")
                ||report.contains("distribution of households by pointofpurchasedfooditems")
                ||report.contains("food and non food expenditure per adult equivalent")
                ) {
            if(report.matches("Crimes Reported to Police by Command Stations")){
                label_1 = "Crimes";
                yLabel.setText("Number of Crimes");
                list = databaseGovernance.getCrimes_reported_to_police_by_command_stations(county);
            }else if(report.matches("food and non food expenditure per adult equivalent")){
                label_1 = "Expenditure";
                yLabel.setText("KSH");
                list = databasePoverty.getfoodxpenditureperadult(county);
            }else if(report.matches("Health Facilities by Ownership of Health Facilities")){
                label_1 = "Facilities";
                yLabel.setText("Number of Facilities");
                list = databaseHealth.getHealthfacilitiesbyownershipofhealthfacilities(county,selection);
            }else if(report.matches("distribution of households by pointofpurchasedfooditems")){
                label_1 = "Place";
                yLabel.setText("Percent");
                list = databasePoverty.getdistributionofhouseholdsbypointofpurchasedfooditems(county,selection);
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
            }else if(report.contains("Land Potential")){
                label_1 = choice;
                yLabel.setText("Potential");
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
            }else if (report.matches("Percentage Incidence of Diseases in Kenya by Type and Year")) {
                label_1 = selection;
                yLabel.setText("Value");
                list = databaseHealth.getHealth_percentage_incidence_of_diseases_in_kenya(selection);
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
            }else if (report.contains("Percentage of Households by Ownership")) {
                label_1 = selection;
                yLabel.setText("Number");
                list = databasePopulation.getPercentage_of_Households_by_Ownership_of_Household_Assets(selection);
            }else if (report.contains("Values of Total Exports to European Union")) {
                label_1 = selection;
                yLabel.setText("Number");
                list = databaseTradeCommerce.getValues_of_Total_Exports_to_European_Union(selection);
            }else if (report.matches("Average Wage Earnings Per Employee in Private Sector")) {
                label_1 = selection;
                yLabel.setText("Earnings");
                list = databaseLabour.getAverage_Wage_Earnings_Per_Employee_in_Private_Sector(selection);
            }else if (report.matches("Proportion of Population That Took A Trip")) {
                label_1 = "";
                yLabel.setText("Proportion");
                list = databaseTourism.getProportion_of_Population_That_Took_A_Trip(county);
            }else if (report.contains("Quantity and Value of Imports, Exports and Re-exports of Petroleum Products")) {
                label_1 = "Quantity";
                yLabel.setText("Quantity");
                list = databaseEnergy.getQuantity_and_Value_of_Imports_Exports_and_Reexports_of_Petroleum_Products(selection);
            }else if (report.contains("Rainfall")) {
                label_1 = "Quantity";
                yLabel.setText("Quantity");
                list = databaseLandClimate.getRainfall(county);
            }else if (report.contains("Surface Area by Category")) {
                label_1 = "Area";
                yLabel.setText("Area (SQ KM)");
                list = databaseLandClimate.getSurface_Area_By_Category(county,selection);
            }else if (report.contains("Temperature")) {
                label_1 = "Temperature";
                yLabel.setText("Degrees Celcius");
                list = databaseLandClimate.getTemperature(county);
            }else if (report.contains("Quantity and Value of Fish Landed")) {
                label_1 = "Value";
                yLabel.setText("Value");
                list = databaseLandClimate.getQuantity_and_Value_of_Fish_Landed(selection);
            }else if (report.contains("Quantity of Total Minerals")) {
                label_1 = "Quantity";
                yLabel.setText("Quantity");
                list = databaseLandClimate.getQuantity_of_Total_Minerals(selection);
            }else if (report.contains("Value of Total Minerals")) {
                label_1 = "Value";
                yLabel.setText("Value");
                list = databaseLandClimate.getValue_of_Total_Minerals(selection);
            }else if (report.contains("Population of Children under 18 by orphanhood")){
                label_1 = "Percentage";
                yLabel.setText("Percentage");
                list = databasePopulation.getPopulation_of_Children_under_18_by_orphanhood(county,selection);
            }else if (report.contains("Population Marital Status above 18 years")){
                label_1 = "Percentage";
                yLabel.setText("Percentage");
                list = databasePopulation.getPopulation_Marital_Status_above_18_years(county,selection);
            }else if (report.contains("Fertility by Education Status")){
                label_1 = "Percentage";
                yLabel.setText("Percentage");
                list = databaseHealth.getFertility_by_Education_Status(selection);
            }else if (report.contains("Place of Delivery")){
                label_1 = "Rate";
                yLabel.setText("Rate");
                list = databaseHealth.getPlace_of_Delivery(selection);
            }else if (report.contains("Prevalence of Overweight and Obesity")){
                label_1 = "Number";
                yLabel.setText("Number");
                list = databaseHealth.getPrevalence_of_Overweight_and_Obesity(selection);
            }else if (report.contains("Kihibs children by additional supplement")){
                label_1 = "Percent";
                yLabel.setText("Percent");
                Log.d("DatabaseHelper", "onProgressChanged: We are here");
                list = databaseHealth.getkihibs_children_by_additional_supplement(county,selection);
            }else if (report.contains("Kihibs children assisted at birth")){
                label_1 = "Percent";
                yLabel.setText("Percent");
                list = databaseHealth.getKihibs_children_assisted_at_birth(county,selection);
            }else if (report.contains("Kihibs children immunized against measles")){
                label_1 = "Percent";
                yLabel.setText("Percent");
                list = databaseHealth.getKihibs_children_immunized(county,selection);
            }else if (report.contains("population with mobile phone and averagesims")){
                label_1 = "Percent";
                yLabel.setText("Percent");
                list = databaseICT.getpopulationwithmobilephone(county);
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
                ||report.matches("Average Wage Earnings Per Employee in Public Sector")
                ||report.matches("Memorandum Items in Public Sector")
                ||report.matches("Wage Employment by Industry and Sex")
                ||report.contains("Interest Rates")
                ||report.contains("Production Area and Average Yield of Coffee")
                ||report.contains("Production Area and Average Yield of Tea")
                ||report.contains("Participation in Key Decision Making Positions by Sex")
                ||report.contains("Women Groups Registration Contribution Women Enterprise Fund")
                ||report.contains("Women Groups Registration Contributions Uwezo Funds")
                ||report.contains("Population by Sex and School Attendance (3 Years and Above)")
                ||report.contains("ECDE Enrolment, Gross and Net Enrolment Rates")
                ||report.contains("Number of Candidates by Sex in KCSE")
                ||report.contains("Primary School Enrolment by Sex")
                ||report.contains("Primary School Teachers by Sex")
                ||report.contains("Public Primary Teachers Training College Enrolment by Sex")
                ||report.contains("Public Secondary School Teachers by Sex")
                ||report.contains("Secondary School Enrolment by Year and Sex")
                ||report.contains("Land Topography")
                ||report.contains("Population by Sex and Age Groups")
                ||report.contains("Population Distribution by sex")
                ||report.contains("Population Distribution of households by size")
                ||report.contains("Population by sex according to  household head")
                ||report.contains("Knowledge and Prevalence of Female Circumcision")
                ||report.contains("Members of National Assembly and Senators")
                ||report.contains("Persons Reported to have Committed Defilement")
                ||report.contains("Persons Reported to have Committed Rape")
                ||report.contains("Total Prisoners Committed For Debt by Sex")
                ||report.contains("Fertility Rate by Age and Residence")
                ||report.contains("Early Childhood Mortality Rates by Sex")
                ||report.contains("Percentage of Adults 15+ old who are current users of various smokeless tobacco products by sex")
                ||report.contains("Percentage of Women and Men aged 15-49 who drink alcohol by age, 2014")
                ||report.contains("households owned ict equipment services")
                ||report.contains("population above 18 subscribed mobile money")
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
            }else if (report.matches("households owned ict equipment services")){
                label_1 = "Computers";
                label_2 = "TVs";
                yLabel.setText("Percent");
                list = databaseICT.gethouseholdsownedictequipmentservices(county);
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
            }else if (report.contains("ECDE Enrolment, Gross and Net Enrolment Rates")) {
                label_1 = "Gross";
                label_2 = "Net";
                yLabel.setText("Rate");
                list = databaseEducation.getECDE_Enrolment_Gross_and_Net_Enrolment_Rates(county);
            }else if (report.contains("Number of Candidates by Sex in KCSE")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Number");
                list = databaseEducation.getNumber_of_Candidates_by_Sex_in_KCSE();
            }else if (report.contains("Primary School Enrolment by Sex")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Number");
                list = databaseEducation.getPrimary_school_enrolments_by_sex();
            }else if (report.contains("Primary School Teachers by Sex")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Number");
                list = databaseEducation.getPrimary_School_Teachers_by_Sex();
            }else if (report.contains("Public Primary Teachers Training College Enrolment by Sex")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Number");
                list = databaseEducation.getPublic_Primary_Teachers_Training_College_Enrolment_by_Sex();
            }else if (report.contains("Public Secondary School Teachers by Sex")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Number");
                list = databaseEducation.getPublic_Secondary_School_Teachers_by_Sex();
            }else if (report.contains("Secondary School Enrolment by Year and Sex")) {
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Number");
                list = databaseEducation.getSecondary_School_Enrolment_by_Year_and_Sex();
            }else if (report.contains("Land Topography")) {
                label_1 = "Highest Point";
                label_2 = "Lowest Point";
                yLabel.setText("Meters");
                list = databaseLandClimate.getLand_Topography(county);
            }else if (report.contains("Population by Sex and Age Groups")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Population");
                list = databasePopulation.getPopulation_by_Sex_and_Age_Groups(selection);
            }else if (report.contains("Population Distribution by sex")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Percent");
                list = databasePopulation.getPopulation_Distribution_by_sex(county);
            }else if (report.contains("Population Distribution of households by size")){
                label_1 = "From 1 to 4";
                label_2 = "From 5 to Over 7";
                yLabel.setText("Percent");
                list = databasePopulation.getPopulation_Distribution_of_households_by_size(county);
            }else if (report.contains("Population by sex according to  household head")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Percent");
                list = databasePopulation.getPopulation_by_sex_according_to_household_head(county);
            }else if (report.contains("Knowledge and Prevalence of Female Circumcision")){
                label_1 = "Heard Of";
                label_2 = "Not Heard Of";
                yLabel.setText("Percent");
                list = databaseGovernance.getKnowledge_and_Prevalence_of_Female_Circumcision(selection);
            }else if (report.contains("Members of National Assembly and Senators")){
                label_1 = "Men";
                label_2 = "Women";
                yLabel.setText("Number");
                list = databaseGovernance.getMembers_of_National_Assembly_and_Senators(selection);
            }else if (report.contains("Persons Reported to have Committed Defilement")){
                label_1 = "Men";
                label_2 = "Women";
                yLabel.setText("Number");
                list = databaseGovernance.getPersons_Reported_to_have_Committed_Defilement();
            }else if (report.contains("Persons Reported to have Committed Rape")){
                label_1 = "Men";
                label_2 = "Women";
                yLabel.setText("Number");
                list = databaseGovernance.getPersons_Reported_to_have_Committed_Rape();
            }else if (report.contains("Total Prisoners Committed For Debt by Sex")){
                label_1 = "Men";
                label_2 = "Women";
                yLabel.setText("Number");
                list = databaseGovernance.getTotal_Prisoners_Committed_For_Debt_by_Sex();
            }else if (report.contains("Fertility Rate by Age and Residence")){
                label_1 = "Urban";
                label_2 = "Rural";
                yLabel.setText("Percent");
                list = databaseHealth.getFertility_RatebyAgeandResidence(selection);
            }else if (report.contains("Early Childhood Mortality Rates by Sex")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Percent");
                list = databaseHealth.getEarly_Childhood_Mortality_Rates_by_Sex(selection);
            }else if (report.contains("Percentage of Adults 15+ old who are current users of various smokeless tobacco products by sex")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Percent");
                list = databaseHealth.getPercentage_of_Adults_users_of_tobacco_products(selection);
            }else if (report.contains("Percentage of Women and Men aged 15-49 who drink alcohol by age, 2014")){
                label_1 = "Male";
                label_2 = "Female";
                yLabel.setText("Percent");
                list = databaseHealth.getPercentage_of_Adults_who_drink_alcohol(selection);
            }else if (report.contains("population above 18 subscribed mobile money")){
                label_1 = "Mobile Money";
                label_2 = "Mobile Banking";
                yLabel.setText("Percent");
                list = databaseICT.getpopulationabove18subscribedmobilemoney(county);
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
                ||report.matches("Population by Sex, Households, Density and Census years")
                ||report.matches("NHIF Members")
                ||report.matches("Hotel Occupancy By Residence")
                ||report.matches("Hotel Occupancy By Zone")
                ||report.matches("Domestic Credit")
                ||report.matches("Tourism Visitor To Parks")
                ||report.matches("Tourism Visitor To Museums")
                ||report.matches("NHIF Resources")
                ||report.matches("Approved Degree Diploma Programs")
                ||report.matches("Student Enrolment in Public Universities")
                ||report.matches("Teacher Training Colleges")
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
                ||report.contains("Women Groups Registration Contributions Women Groups")
                ||report.contains("Government Forest")
                ||report.contains("Population by broad age group")
                ||report.contains("Record Sale of Government Products")
                ||report.matches("Population Projections by Special Age Group")
                ||report.matches("Experience of Domestic Violence By Age")
                ||report.matches("Experience of Domestic Violence by Marital Success")
                ||report.matches("Experience of Domestic Violence by Residence")
                ||report.matches("Prevalence Female Circumcision and Type")
                ||report.matches("Nutritional Status of Women")
                ||report.matches("waste disposal method")
                ||report.matches("volume of water used")
                ||report.matches("time taken to fetch drinking water")
                ||report.matches("sharing of toilet")
                ||report.matches("place of washing hands near toilet")
                ||report.matches("owner occupier dwellings")
                ||report.matches("methods used to make water safer")
                ||report.matches("hhholds by habitable rooms")
                ||report.matches("hholds by housing tenure")
                ||report.matches("hholds by type of housing unit")
                ||report.matches("primary type of cooking appliance")
                ||report.matches("hholds in rented dwellings")
                ||report.matches("main floor material")
                ||report.matches("main roofing material")
                ||report.matches("main source of cooking fuel")
                ||report.matches("main source of drinking water")
                ||report.matches("main source of ligthing fuel")
                ||report.matches("main toilet facility")
                ||report.matches("main wall material")
                ||report.matches("Categories of Land")
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
            }else if (report.matches("Population by Sex, Households, Density and Census years")){
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
            }else if (report.matches("Tourism Visitor To Museums")){
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
            }else if(report.contains("Government Forest")){
                label_1 = "Previous Plantation Area";
                label_2 = "Area Planted";
                label_3 = "Area Cleared";
                yLabel.setText("Acres");
                list = databaseLandClimate.getGovernment_Forest();
            }/*else if(report.contains("Land and Climate Surface Area by Category")){
                Log.d("l_and_c", "onProgressChanged: ");
                label_1 = "Employee Compensation";
                label_2 = "Goods and Services";
                label_3 = "Total Expenditure";
                //label_4
                yLabel.setText("Schools");
                list = databaseLandClimate.getLand_and_Climate_Surface_Area_by_Category(county);
            }*/else if(report.contains("Women Groups Registration Contributions Women Groups")){
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
                list = databasePolitical.getAdministrative_unit(county);
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
            }else if(report.contains("Population by broad age group")){
                label_1 = "Independent Ratio";
                label_2 = "Dependent Ratio (Children)";
                label_3 = "Dependent Ratio (Elderly)";
                yLabel.setText("Ratio");
                list = databasePopulation.getPopulation_by_broad_age_group(county);
            }else if (report.contains("Record Sale of Government Products")){
                label_1 = "Soft Wood";
                label_2 = "Fuelwood Charcoal";
                label_3 = "Power and Telegraph";
                yLabel.setText("Value");
                list = databaseLandClimate.getRecord_Sale_of_Government_Products();
            }else if (report.matches("Population Projections by Special Age Group")){
                label_1 = "Range below 18";
                label_2 = "Range 18 plus";
                label_3 = "Range 65 plus";
                list = databasePopulation.getPopulationprojectionsbyspecialagegroups(county,choice);
            }else if (report.matches("Experience of Domestic Violence By Age")){
                label_1 = "Physical Violence";
                label_2 = "Sexual Violence";
                label_3 = "Physical and Sexual Violence";
                list = databaseGovernance.getExperience_of_Domestic_Violence_By_Age(selection);
            }else if (report.matches("Experience of Domestic Violence by Marital Success")){
                label_1 = "Physical Violence";
                label_2 = "Sexual Violence";
                label_3 = "Physical and Sexual Violence";
                list = databaseGovernance.getExperience_of_Domestic_Violence_By_Marital_Success(selection);
            }else if (report.matches("Experience of Domestic Violence by Residence")){
                label_1 = "Physical Violence";
                label_2 = "Sexual Violence";
                label_3 = "Physical and Sexual Violence";
                list = databaseGovernance.getExperience_of_Domestic_Violence_By_Residence(selection);
            }else if (report.matches("Prevalence Female Circumcision and Type")){
                label_1 = "Cut - No Flesh Removed";
                label_2 = "Cut - Flesh Removed";
                label_3 = "Sewn Closed";
                list = databaseGovernance.getPrevalence_Female_Circumcision_and_Type(selection);
            }else if(report.matches("Nutritional Status of Women")) {
                label_1 = "Normal";
                label_2 = "Overweight";
                label_3 = "Obese";
                yLabel.setText("status");
                list = databaseHealth.getNutritional_status_of_women(county);
            }else if(report.matches("waste disposal method")) {
                label_1 = "Government";
                label_2 = "Community";
                label_3 = "Private";
                yLabel.setText("status");
                list = databaseHousing.getwaste_disposal_method(county);
            }else if(report.matches("volume of water used")) {
                label_1 = "0 to 1000 lts";
                label_2 = "1000 to 2000 lts";
                label_3 = "2000 to 3000 lts";
                yLabel.setText("Litres");
                list = databaseHousing.getvolume_of_water_used(county);
            }else if(report.matches("time taken to fetch drinking water")) {
                label_1 = "Zero";
                label_2 = "Less than 30";
                label_3 = "More than 30";
                yLabel.setText("Minutes");
                list = databaseHousing.gettime_taken(county);
            }else if(report.matches("sharing of toilet")) {
                label_1 = "Shared";
                label_2 = "Not Shared";
                label_3 = "Not Stated";
                yLabel.setText("Percent");
                list = databaseHousing.getsharing_of_toilet(county);
            }else if(report.matches("place of washing hands near toilet")) {
                label_1 = "Place";
                label_2 = "No Place";
                label_3 = "Not Stated";
                yLabel.setText("Percent");
                list = databaseHousing.getplace_of_washing_hands(county);
            }else if(report.matches("owner occupier dwellings")) {
                label_1 = "Purchased Cash Loan";
                label_2 = "Constructed Cash Loan";
                label_3 = "Other";
                yLabel.setText("Percent");
                list = databaseHousing.getowner_occupier_dwellings(county);
            }else if(report.matches("methods used to make water safer")) {
                label_1 = "Boil";
                label_2 = "Water Filter";
                label_3 = "Other";
                yLabel.setText("Percent");
                list = databaseHousing.getmethods_used_to_make_water_safer(county);
            }else if(report.matches("hhholds by habitable rooms")) {
                label_1 = "One";
                label_2 = "Two";
                label_3 = "Three";
                yLabel.setText("Percent");
                list = databaseHousing.gethhholds_by_habitable_rooms(county);
            }else if(report.matches("hholds by housing tenure")) {
                label_1 = "Owner Occupier";
                label_2 = "Pays Rent";
                label_3 = "No Rent (consensually)";
                yLabel.setText("Percent");
                list = databaseHousing.gethholds_by_housing_tenure(county);
            }else if(report.matches("hholds by type of housing unit")) {
                label_1 = "Flat";
                label_2 = "Maisonnette";
                label_3 = "Shanty";
                yLabel.setText("Percent");
                list = databaseHousing.gethholds_by_type_of_housing_unit(county);
            }else if(report.matches("primary type of cooking appliance")) {
                label_1 = "Ordinary Jiko";
                label_2 = "Gas";
                label_3 = "Electric cooker";
                yLabel.setText("Percent");
                list = databaseHousing.getprimary_type_of_cooking_appliance(county);
            }else if(report.matches("hholds in rented dwellings")) {
                label_1 = "National Government";
                label_2 = "Company (Direct)";
                label_3 = "Individual (Direct)";
                yLabel.setText("Percent");
                list = databaseHousing.gethholds_in_rented_dwellings(county);
            }else if(report.matches("main floor material")) {
                label_1 = "Tiles";
                label_2 = "Cement";
                label_3 = "Wood";
                yLabel.setText("Percent");
                list = databaseHousing.getmain_floor_material(county);
            }else if(report.matches("main roofing material")) {
                label_1 = "Tiles";
                label_2 = "Cement";
                label_3 = "Mud";
                yLabel.setText("Percent");
                list = databaseHousing.getmain_roofing_material(county);
            }else if(report.matches("main source of cooking fuel")) {
                label_1 = "Firewood";
                label_2 = "Electricity";
                label_3 = "Biogas";
                yLabel.setText("Percent");
                list = databaseHousing.getmain_source_of_cooking_fuel(county);
            }else if(report.matches("main source of drinking water")) {
                label_1 = "Piped Dwelling";
                label_2 = "Rain";
                label_3 = "Truck Vendor";
                yLabel.setText("Percent");
                list = databaseHousing.getmain_source_of_drinking_water(county);
            }else if(report.matches("main source of ligthing fuel")) {
                label_1 = "Electricity";
                label_2 = "Generator";
                label_3 = "Solar Energy";
                yLabel.setText("Percent");
                list = databaseHousing.getmain_source_of_ligthing_fuel(county);
            }else if(report.matches("main toilet facility")) {
                label_1 = "Piped Sewer";
                label_2 = "Septic Tank";
                label_3 = "Latrine";
                yLabel.setText("Percent");
                list = databaseHousing.getmain_toilet_facility(county);
            }else if(report.matches("main wall material")) {
                label_1 = "Lime Stone";
                label_2 = "Brick";
                label_3 = "Cement Blocks";
                yLabel.setText("Percent");
                list = databaseHousing.getmain_wall_material(county);
            }else if(report.matches("Categories of Land")){
                label_1 = "High Potential";
                label_2 = "Medium Potential";
                label_3 = "Low Potential";
                yLabel.setText("Acres");
                list = databaseAgriculture.getCategories_of_Land(county);
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
                //||report.matches("Population Projections by Special Age Group")
                ||report.matches("Death")
                ||report.matches("Diseases 2009 to 2015")
                ||report.matches("Medical Personnel")
                ||report.matches("Number of Educational Institutions")
                ||report.matches("Secondary School Enrollment By Class, Sex and Subcounty")
                ||report.matches("Passports, Work Permits and Foreigners Registered")
                ||report.matches("County Revenue")
                ||report.matches("Distribution of Outpatient Visits by Type of Health Care")
                ||report.matches("Insurance Coverage by Counties and Types")
                ||report.matches("Cases Handled By Various Courts")
                ||report.matches("Quarterly Civil Engineering Cost Index")
                ||report.matches("distribution of household food consumption")
                ||report.matches("food estimates by residence and county")
                ||report.matches("hardcore estimates by residence and county")
                ||report.matches("overall estimates by residence and county")
                ){

            if(report.matches("Population Projections by Selected Age Group")){
                label_1 = "Range 0-19";
                label_2 = "Range 20-39";
                label_3 = "Range 40-59";
                label_4 = "Range 60 plus";
                yLabel.setText("Population");
                list = databasePopulation.getPopulationprojectionsbyselectedagegroup(county);
            }/*else if (report.matches("Population Projections by Special Age Group")){
                label_1 = "Under 1";
                label_2 = "Range below 18";
                label_3 = "Range 18 plus";
                label_4 = "Range 65 plus";
                yLabel.setText("Population");
                list = databasePopulation.getPopulationprojectionsbyspecialagegroups(county,choice);
            }*/else if (report.matches("food estimates by residence and county")){
                label_1 = "Headcount Rate";
                label_2 = "Distribution of Poor";
                label_3 = "Poverty Gap";
                label_4 = "Severity of Poverty";
                yLabel.setText("Percent");
                list = databasePoverty.getfoodestimatesbyresidenceandcounty(county);
            }else if (report.matches("hardcore estimates by residence and county")){
                label_1 = "Headcount Rate";
                label_2 = "Distribution of Poor";
                label_3 = "Poverty Gap";
                label_4 = "Severity of Poverty";
                yLabel.setText("Percent");
                list = databasePoverty.gethardcoreestimatesbyresidenceandcounty(county);
            }else if (report.matches("overall estimates by residence and county")){
                label_1 = "Headcount Rate";
                label_2 = "Distribution of Poor";
                label_3 = "Poverty Gap";
                label_4 = "Severity of Poverty";
                yLabel.setText("Percent");
                list = databasePoverty.geoverallestimatesbyresidenceandcounty(county);
            }else if (report.matches("distribution of household food consumption")){
                label_1 = "Purchases";
                label_2 = "Stock";
                label_3 = "Own Production";
                label_4 = "Gifts";
                yLabel.setText("Percent");
                list = databasePoverty.getdistributionofhouseholdfoodconsumption(county);
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
            }else if(report.matches("Cases Handled By Various Courts")){
                label_1 = "Magistrate Court";
                label_2 = "High Court";
                label_3 = "Court of Appeal";
                label_4 = "Supreme Court";
                yLabel.setText("Cases");
                list = databaseGovernance.getCases_handled_by_different_courts(selection);
            }else if(report.matches("Quarterly Civil Engineering Cost Index")){
                label_1 = "March";
                label_2 = "June";
                label_3 = "September";
                label_4 = "December";
                yLabel.setText("Amount");
                list = databaseBuildingConstruction.getQuarterly_Civil_Engineering_Cost_Index(selection);
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

        if(report.matches("Environmental Natural Resources Trends")
                ||report.matches("Elementary Aggregates Weights in the CPI Baskets")
                ||report.matches("Group Weights for Kenya CPI February Base Period 2009")
                ||report.matches("Group Weights for Kenya CPI October Base Period 1997")
                ||report.matches("households without internet by reason")
                ||report.matches("households with tv")
                ||report.matches("households with internet by type")
                ||report.matches("population above 18 by reason not having phone")
                ||report.matches("population by ictequipment and servicesused")
                ||report.matches("population that didnt use internet byreason")
                ||report.matches("population that used internet by purpose")
                ||report.matches("population who used internet by place")
                ||report.matches("consumption expenditure and quintile distribution")
                ){

            if(report.matches("Environmental Natural Resources Trends")){
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
            }else if (report.matches("households without internet by reason")) {
                label_1 = "Don't Need";
                label_2 = "Lack of Skills";
                label_3 = "No Network";
                label_4 = "Access Available Elsewhere";
                label_5 = "High Cost";
                yLabel.setText("Percent");
                list = databaseICT.gethouseholdswithoutinternetbyreason(county);
            }else if (report.matches("households with tv")) {
                label_1 = "Digital_tv";
                label_2 = "Pay TV";
                label_3 = "Free to Air";
                label_4 = "IP TV";
                label_5 = "None";
                yLabel.setText("Percent");
                list = databaseICT.gethouseholdswithtv(county);
            }else if (report.matches("households with internet by type")) {
                label_1 = "Fixed Wired";
                label_2 = "Fixed Wireless";
                label_3 = "Mobile Broadband";
                label_4 = "Mobile";
                label_5 = "Other";
                yLabel.setText("Percent");
                list = databaseICT.gethouseholdswithinternetbytype(county);
            }else if (report.matches("population above 18 by reason not having phone")) {
                label_1 = "Too young";
                label_2 = "Don't Need";
                label_3 = "Phones Are Restricted";
                label_4 = "No Network";
                label_5 = "No Electricity";
                yLabel.setText("Percent");
                list = databaseICT.getpopulationabove18byreasonnothavingphone(county);
            }else if (report.matches("population by ictequipment and servicesused")) {
                label_1 = "TV";
                label_2 = "Radio";
                label_3 = "Mobile";
                label_4 = "Computer";
                label_5 = "Internet";
                yLabel.setText("Percent");
                list = databaseICT.getpopulationbyictequipmentandservicesused(county);
            }else if (report.matches("population that didnt use internet byreason")) {
                label_1 = "Too Young";
                label_2 = "Don't Need";
                label_3 = "Lack of Skills";
                label_4 = "High Cost";
                label_5 = "No Internet Access";
                yLabel.setText("Percent");
                list = databaseICT.getpopulationthatdidntuseinternetbyreason(county);
            }else if (report.matches("population that used internet by purpose")) {
                label_1 = "Newspaper";
                label_2 = "Banking";
                label_3 = "Research";
                label_4 = "Information";
                label_5 = "Social Networks";
                yLabel.setText("Percent");
                list = databaseICT.getpopulationthatusedinternetbypurpose(county);
            }else if (report.matches("population who used internet by place")) {
                label_1 = "Work";
                label_2 = "Cyber Cafe";
                label_3 = "Educational Center";
                label_4 = "Home";
                label_5 = "Another's Home";
                yLabel.setText("Percent");
                list = databaseICT.getpopulationwhousedinternetbyplace(county);
            }else if (report.matches("consumption expenditure and quintile distribution")) {
                label_1 = "Quintile 1";
                label_2 = "Quintile 2";
                label_3 = "Quintile 3";
                label_4 = "Quintile 4";
                label_5 = "Quintile 5";
                yLabel.setText("Percent");
                list = databasePoverty.getconsumptionexpenditureandquintiledistribution(county);
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
