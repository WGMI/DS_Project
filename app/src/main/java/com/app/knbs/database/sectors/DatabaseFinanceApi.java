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

public class DatabaseFinanceApi {
    private Context context;
    public DatabaseFinanceApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);
    private ReportLoader loader = new ReportLoader(context);
    private CountyHelper countyHelper = new CountyHelper();

    public void loadData(final ProgressDialog d){
        insertInto_finance_economic_classification_revenue(d);//RECHECK
        insertInto_finance_excise_revenue_commodity(d);
        insertInto_finance_national_government_expenditure(d);
        insertInto_finance_national_government_expenditure_purpose(d);
        insertInto_finance_outstanding_debt_international_organization(d);//RECHECK

        insertInto_finance_outstanding_debt_lending_country(d);//RECHECK
        insertInto_finance_statement_of_national_government_operations(d);
        insertInto_finance_cdf_allocation_by_constituency(d);
        insertInto_finance_county_budget_allocation(d);
        insertInto_finance_county_expenditure(d);

        //insertInto_finance_county_revenue(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_finance_economic_classification_revenue(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Economic Classification of National Government Revenue"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject taxesIPCGObject = array.getJSONObject(0);
                            JSONObject taxesPropertyObject = array.getJSONObject(1);
                            JSONObject taxesVATObject = array.getJSONObject(2);
                            JSONObject taxesOGSObject = array.getJSONObject(3);
                            JSONObject taxesITTObject = array.getJSONObject(4);
                            JSONObject otherTaxesNECObject = array.getJSONObject(5);
                            JSONObject totalTRObject = array.getJSONObject(6);
                            JSONObject socialSecContObject = array.getJSONObject(7);
                            JSONObject proertyIncObject = array.getJSONObject(8);
                            JSONObject salesGandSObject = array.getJSONObject(9);
                            JSONObject finesPandFObject = array.getJSONObject(10);
                            JSONObject repaymentDLObject = array.getJSONObject(11);
                            JSONObject otherRNECObject = array.getJSONObject(12);
                            JSONObject totalNTRCObject = array.getJSONObject(13);
                            JSONObject totalObject = array.getJSONObject(14);
                            JSONObject yearObject = array.getJSONObject(15);

                            JSONArray taxesIPCGArray = taxesIPCGObject.getJSONArray("data");
                            JSONArray taxesPropertyArray = taxesPropertyObject.getJSONArray("data");
                            JSONArray taxesVATArray = taxesVATObject.getJSONArray("data");
                            JSONArray taxesOGSArray = taxesOGSObject.getJSONArray("data");
                            JSONArray taxesITTArray = taxesITTObject.getJSONArray("data");
                            JSONArray otherTaxesNECArray = otherTaxesNECObject.getJSONArray("data");
                            JSONArray totalTRArray = totalTRObject.getJSONArray("data");
                            JSONArray socialSecContArray = socialSecContObject.getJSONArray("data");
                            JSONArray proertyIncArray = proertyIncObject.getJSONArray("data");
                            JSONArray salesGandSArray = salesGandSObject.getJSONArray("data");
                            JSONArray finesPandFArray = finesPandFObject.getJSONArray("data");
                            JSONArray repaymentDLArray = repaymentDLObject.getJSONArray("data");
                            JSONArray otherRNECArray = otherRNECObject.getJSONArray("data");
                            JSONArray totalNTRCArray = totalNTRCObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");
                            JSONArray yearArray = yearObject.getJSONArray("data");

                            List<Integer> taxesIPCGList = new ArrayList<>();
                            List<Integer> taxesPropertyList = new ArrayList<>();
                            List<Integer> taxesVATList = new ArrayList<>();
                            List<Integer> taxesOGSList = new ArrayList<>();
                            List<Integer> taxesITTList = new ArrayList<>();
                            List<Integer> otherTaxesNECList = new ArrayList<>();
                            List<Integer> totalTRList = new ArrayList<>();
                            List<Integer> socialSecContList = new ArrayList<>();
                            List<Integer> proertyIncList = new ArrayList<>();
                            List<Integer> salesGandSList = new ArrayList<>();
                            List<Integer> finesPandFList = new ArrayList<>();
                            List<Integer> repaymentDLList = new ArrayList<>();
                            List<Integer> otherRNECList = new ArrayList<>();
                            List<Integer> totalNTRCList = new ArrayList<>();
                            List<Integer> totalList = new ArrayList<>();
                            List<String> yearList = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                taxesIPCGList.add(taxesIPCGArray.getInt(i));
                                taxesPropertyList.add(taxesPropertyArray.getInt(i));
                                taxesVATList.add(taxesVATArray.getInt(i));
                                taxesOGSList.add(taxesOGSArray.getInt(i));
                                taxesITTList.add(taxesITTArray.getInt(i));
                                otherTaxesNECList.add(otherTaxesNECArray.getInt(i));
                                totalTRList.add(totalTRArray.getInt(i));
                                socialSecContList.add(socialSecContArray.getInt(i));
                                proertyIncList.add(proertyIncArray.getInt(i));
                                salesGandSList.add(salesGandSArray.getInt(i));
                                finesPandFList.add(finesPandFArray.getInt(i));
                                repaymentDLList.add(repaymentDLArray.getInt(i));
                                otherRNECList.add(otherRNECArray.getInt(i));
                                totalNTRCList.add(totalNTRCArray.getInt(i));
                                totalList.add(totalArray.getInt(i));
                                yearList.add(yearArray.getString(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("finance_economic_classification_revenue",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("economicrevenue_id",i);
                                values.put("taxes_income_profits_capitalgains",taxesIPCGList.get(i));
                                values.put("taxes_property",taxesPropertyList.get(i));
                                values.put("taxes_vat",taxesVATList.get(i));
                                values.put("taxes_othergoodsandservices",taxesOGSList.get(i));
                                values.put("taxes_internationaltrade_transactions",taxesITTList.get(i));
                                values.put("other_taxes_notelsewhereclasified",otherTaxesNECList.get(i));
                                values.put("totaltax_revenue",totalTRList.get(i));
                                values.put("socialsecuritycontributions",socialSecContList.get(i));
                                values.put("property_income",taxesPropertyList.get(i));
                                values.put("sale_goodsandservices",salesGandSList.get(i));
                                values.put("fines_penaltiesandforfeitures",finesPandFList.get(i));
                                values.put("repayments_domesticlending",repaymentDLList.get(i));
                                values.put("other_receiptsnotelsewhereclassified",otherRNECList.get(i));
                                values.put("total_nontax_revenue",totalNTRCList.get(i));
                                values.put("total",totalList.get(i));
                                values.put("year",yearList.get(i));

                                success = db.insertOrThrow("finance_economic_classification_revenue",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "finance_economic_classification_revenue: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "finance_economic_classification_revenue: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_finance_excise_revenue_commodity(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Excise Revenue Commodity"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject beerObject = array.getJSONObject(1);
                            JSONObject cigarettesObject = array.getJSONObject(2);
                            JSONObject minWaterObject = array.getJSONObject(3);
                            JSONObject wineSpiritsObject = array.getJSONObject(4);
                            JSONObject othersObject = array.getJSONObject(5);
                            JSONObject totalObject = array.getJSONObject(6);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray beerArray = beerObject.getJSONArray("data");
                            JSONArray cigarettesArray = cigarettesObject.getJSONArray("data");
                            JSONArray minWaterArray = minWaterObject.getJSONArray("data");
                            JSONArray wineSpiritsArray = wineSpiritsObject.getJSONArray("data");
                            JSONArray othersArray = othersObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<Double> beers = new ArrayList<>();
                            List<Double> cigarettes = new ArrayList<>();
                            List<Double> minWaters = new ArrayList<>();
                            List<Double> wineSpirits = new ArrayList<>();
                            List<Double> others = new ArrayList<>();
                            List<Double> totals = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                beers.add(beerArray.getDouble(i));
                                cigarettes.add(cigarettesArray.getDouble(i));
                                minWaters.add(minWaterArray.getDouble(i));
                                wineSpirits.add(wineSpiritsArray.getDouble(i));
                                others.add(othersArray.getDouble(i));
                                totals.add(totalArray.getDouble(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("finance_excise_revenue_commodity",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("excise_id",i);
                                values.put("year",years.get(i));
                                values.put("beer",beers.get(i));
                                values.put("cigarettes",cigarettes.get(i));
                                values.put("mineral_waters",minWaters.get(i));
                                values.put("wines_spirits",wineSpirits.get(i));
                                values.put("other_commodities",others.get(i));
                                values.put("total",totals.get(i));

                                success = db.insertOrThrow("finance_excise_revenue_commodity",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "finance_excise_revenue_commodity: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "finance_excise_revenue_commodity: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_finance_national_government_expenditure(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Economic Classification of National Government Expenditure"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject acquisitionNFAObject = array.getJSONObject(1);
                            JSONObject acquisitionFAObject = array.getJSONObject(2);
                            JSONObject loanRepaymentsObject = array.getJSONObject(3);
                            JSONObject compEmpObject = array.getJSONObject(4);
                            JSONObject gAndSObject = array.getJSONObject(5);
                            JSONObject subsidiesObject = array.getJSONObject(6);
                            JSONObject interestsObject = array.getJSONObject(7);
                            JSONObject grantsObject = array.getJSONObject(8);
                            JSONObject otherExpObject = array.getJSONObject(9);
                            JSONObject socialBenObject = array.getJSONObject(10);
                            JSONObject capitalGrantsObject = array.getJSONObject(11);
                            JSONObject totalObject = array.getJSONObject(12);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray acquisitionNFAArray = acquisitionNFAObject.getJSONArray("data");
                            JSONArray acquisitionFAArray = acquisitionFAObject.getJSONArray("data");
                            JSONArray loanRepaymentsArray = loanRepaymentsObject.getJSONArray("data");
                            JSONArray compEmpArray = compEmpObject.getJSONArray("data");
                            JSONArray gAndSArray = gAndSObject.getJSONArray("data");
                            JSONArray subsidiesArray = subsidiesObject.getJSONArray("data");
                            JSONArray interestsArray = interestsObject.getJSONArray("data");
                            JSONArray grantsArray = grantsObject.getJSONArray("data");
                            JSONArray otherExpArray = otherExpObject.getJSONArray("data");
                            JSONArray socialBenArray = socialBenObject.getJSONArray("data");
                            JSONArray capitalGrantsArray = capitalGrantsObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");

                            List<String> years = new ArrayList<>();
                            List<Double> acquisitionNFAList = new ArrayList<>();
                            List<Double> acquisitionFAList = new ArrayList<>();
                            List<Double> loanRepaymentsList = new ArrayList<>();
                            List<Double> compEmpList = new ArrayList<>();
                            List<Double> gAndSList = new ArrayList<>();
                            List<Double> subsidiesList = new ArrayList<>();
                            List<Double> interestsList = new ArrayList<>();
                            List<Double> grantsList = new ArrayList<>();
                            List<Double> otherExpList = new ArrayList<>();
                            List<Double> socialBenList = new ArrayList<>();
                            List<Double> capitalGrantsList = new ArrayList<>();
                            List<Double> totals = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getString(i));
                                acquisitionNFAList.add(acquisitionNFAArray.getDouble(i));
                                acquisitionFAList.add(acquisitionFAArray.getDouble(i));
                                loanRepaymentsList.add(loanRepaymentsArray.getDouble(i));
                                compEmpList.add(compEmpArray.getDouble(i));
                                gAndSList.add(gAndSArray.getDouble(i));
                                subsidiesList.add(subsidiesArray.getDouble(i));
                                interestsList.add(interestsArray.getDouble(i));
                                grantsList.add(grantsArray.getDouble(i));
                                otherExpList.add(otherExpArray.getDouble(i));
                                socialBenList.add(socialBenArray.getDouble(i));
                                capitalGrantsList.add(capitalGrantsArray.getDouble(i));
                                totals.add(totalArray.getDouble(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("finance_national_government_expenditure",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("government_expenditure_id",i);
                                values.put("acquisition_nonfinancial_assets",acquisitionNFAList.get(i));
                                values.put("acquisition_financial_assets",acquisitionFAList.get(i));
                                values.put("loans_repayments",loanRepaymentsList.get(i));
                                values.put("compensation_employees",compEmpList.get(i));
                                values.put("goods_services",gAndSList.get(i));
                                values.put("subsidies",subsidiesList.get(i));
                                values.put("interest",interestsList.get(i));
                                values.put("grants",grantsList.get(i));
                                values.put("other_expense",otherExpList.get(i));
                                values.put("social_benefits",socialBenList.get(i));
                                values.put("capital_grants",capitalGrantsList.get(i));
                                values.put("total",totals.get(i));
                                values.put("year",years.get(i));

                                success = db.insertOrThrow("finance_national_government_expenditure",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "finance_national_government_expenditure: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "finance_national_government_expenditure: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_finance_national_government_expenditure_purpose(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("National Government Expenditure Purpose"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject genPubServObject = array.getJSONObject(1);
                            JSONObject pubDebtTransObject = array.getJSONObject(2);
                            JSONObject transfersObject = array.getJSONObject(3);
                            JSONObject defenseObject = array.getJSONObject(4);
                            JSONObject orderSafetyObject = array.getJSONObject(5);
                            JSONObject econCommLaborObject = array.getJSONObject(6);
                            JSONObject agricultureObject = array.getJSONObject(7);
                            JSONObject fuelEnergyObject = array.getJSONObject(8);
                            JSONObject miningManuConObject = array.getJSONObject(9);
                            JSONObject transportObject = array.getJSONObject(10);
                            JSONObject communicationsObject = array.getJSONObject(11);
                            JSONObject otherIndObject = array.getJSONObject(12);
                            JSONObject envProtObject = array.getJSONObject(13);
                            JSONObject housingCommAmenObject = array.getJSONObject(14);
                            JSONObject healthObject = array.getJSONObject(15);
                            JSONObject recCulReligionObject = array.getJSONObject(16);
                            JSONObject educationObject = array.getJSONObject(17);
                            JSONObject socialProtObject = array.getJSONObject(18);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray genPubServArray = genPubServObject.getJSONArray("data");
                            JSONArray pubDebtTransArray = pubDebtTransObject.getJSONArray("data");
                            JSONArray transfersArray = transfersObject.getJSONArray("data");
                            JSONArray defenseArray = defenseObject.getJSONArray("data");
                            JSONArray orderSafetyArray = orderSafetyObject.getJSONArray("data");
                            JSONArray econCommLaborArray = econCommLaborObject.getJSONArray("data");
                            JSONArray agricultureArray = agricultureObject.getJSONArray("data");
                            JSONArray fuelEnergyArray = fuelEnergyObject.getJSONArray("data");
                            JSONArray miningManuConArray = miningManuConObject.getJSONArray("data");
                            JSONArray transportArray = transportObject.getJSONArray("data");
                            JSONArray communicationsArray = communicationsObject.getJSONArray("data");
                            JSONArray otherIndArray = otherIndObject.getJSONArray("data");
                            JSONArray envProtArray = envProtObject.getJSONArray("data");
                            JSONArray housingCommAmenObjectArray = housingCommAmenObject.getJSONArray("data");
                            JSONArray healthArray = healthObject.getJSONArray("data");
                            JSONArray recCulReligionArray = recCulReligionObject.getJSONArray("data");
                            JSONArray educationArray = educationObject.getJSONArray("data");
                            JSONArray socialProtArray = socialProtObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<Double> genPubServList = new ArrayList<>();
                            List<Double> pubDebtTransList = new ArrayList<>();
                            List<Double> transfers = new ArrayList<>();
                            List<Double> defenseList = new ArrayList<>();
                            List<Double> orderSafetyList = new ArrayList<>();
                            List<Double> econCommLaborList = new ArrayList<>();
                            List<Double> agricultureList = new ArrayList<>();
                            List<Double> fuelEnergyList = new ArrayList<>();
                            List<Double> miningManuConList = new ArrayList<>();
                            List<Double> transportList = new ArrayList<>();
                            List<Double> communicationsList = new ArrayList<>();
                            List<Double> otherIndList = new ArrayList<>();
                            List<Double> envProtList = new ArrayList<>();
                            List<Double> housingCommAmenObjectList = new ArrayList<>();
                            List<Double> healthList = new ArrayList<>();
                            List<Double> recCulReligionList = new ArrayList<>();
                            List<Double> educationList = new ArrayList<>();
                            List<Double> socialProtList = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                genPubServList.add(genPubServArray.getDouble(i));
                                pubDebtTransList.add(pubDebtTransArray.getDouble(i));
                                transfers.add(transfersArray.getDouble(i));
                                defenseList.add(defenseArray.getDouble(i));
                                orderSafetyList.add(orderSafetyArray.getDouble(i));
                                econCommLaborList.add(econCommLaborArray.getDouble(i));
                                agricultureList.add(agricultureArray.getDouble(i));
                                fuelEnergyList.add(fuelEnergyArray.getDouble(i));
                                miningManuConList.add(miningManuConArray.getDouble(i));
                                transportList.add(transportArray.getDouble(i));
                                communicationsList.add(communicationsArray.getDouble(i));
                                otherIndList.add(otherIndArray.getDouble(i));
                                envProtList.add(envProtArray.getDouble(i));
                                housingCommAmenObjectList.add(housingCommAmenObjectArray.getDouble(i));
                                healthList.add(healthArray.getDouble(i));
                                recCulReligionList.add(recCulReligionArray.getDouble(i));
                                educationList.add(educationArray.getDouble(i));
                                socialProtList.add(socialProtArray.getDouble(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("finance_national_government_expenditure_purpose",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("purpose_id",i+1);
                                values.put("year",years.get(i));
                                values.put("general_publicservices",genPubServList.get(i));
                                values.put("public_debttransactions",pubDebtTransList.get(i));
                                values.put("transfers",transfers.get(i));
                                values.put("defense",defenseList.get(i));
                                values.put("order_safety",orderSafetyList.get(i));
                                values.put("economic_commercial_labor",econCommLaborList.get(i));
                                values.put("agriculture",agricultureList.get(i));
                                values.put("fuel_energy",fuelEnergyList.get(i));
                                values.put("mining_manufacturing_construction",miningManuConList.get(i));
                                values.put("transport",transportList.get(i));
                                values.put("communication",communicationsList.get(i));
                                values.put("other_industries",otherIndList.get(i));
                                values.put("environmental_protection",envProtList.get(i));
                                values.put("housing_communityamenities",housingCommAmenObjectList.get(i));
                                values.put("health",healthList.get(i));
                                values.put("recreation_culture_religion",recCulReligionList.get(i));
                                values.put("education",educationList.get(i));
                                values.put("socialprotection",socialProtList.get(i));

                                success = db.insertOrThrow("finance_national_government_expenditure_purpose",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "finance_national_government_expenditure_purpose: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "finance_national_government_expenditure: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_finance_outstanding_debt_international_organization(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Outstanding Debt International Organization"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject IDAObject = array.getJSONObject(1);
                            JSONObject EEC_EIBObject = array.getJSONObject(2);
                            JSONObject IMFObject = array.getJSONObject(3);
                            JSONObject Adf_adbObject = array.getJSONObject(4);
                            JSONObject commBanksObject = array.getJSONObject(5);
                            JSONObject othersObject = array.getJSONObject(6);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray IDAArray = IDAObject.getJSONArray("data");
                            JSONArray EEC_EIBArray = EEC_EIBObject.getJSONArray("data");
                            JSONArray IMFArray = IMFObject.getJSONArray("data");
                            JSONArray Adf_adbArray = Adf_adbObject.getJSONArray("data");
                            JSONArray commBanksArray = commBanksObject.getJSONArray("data");
                            JSONArray othersArray = othersObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("finance_outstanding_debt_international_organization",null,null);

                            for(int i=0;i<IDAArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("outstanding_debt",i+1);
                                values.put("year",yearArray.getInt(i));
                                values.put("ida",IDAArray.getDouble(i));
                                values.put("eec_eib",EEC_EIBArray.getDouble(i));
                                values.put("imf",IMFArray.getDouble(i));
                                values.put("adf_adb",Adf_adbArray.getDouble(i));
                                values.put("commercial_banks",commBanksArray.getDouble(i));
                                values.put("others",othersArray.getDouble(i));

                                success = db.insertOrThrow("finance_outstanding_debt_international_organization",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "finance_outstanding_debt_international_organization: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "finance_outstanding_debt_international_organization: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_finance_outstanding_debt_lending_country(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Outstanding Debt Lending Country"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject GermanyObject = array.getJSONObject(1);
                            JSONObject JapanObject = array.getJSONObject(2);
                            JSONObject FranceObject = array.getJSONObject(3);
                            JSONObject USAObject = array.getJSONObject(4);
                            JSONObject NetherlandsObject = array.getJSONObject(5);
                            JSONObject DenmarkObject = array.getJSONObject(6);
                            JSONObject FinlandObject = array.getJSONObject(7);
                            JSONObject ChinaObject = array.getJSONObject(8);
                            JSONObject BelgiumObject = array.getJSONObject(9);
                            JSONObject OtherObject = array.getJSONObject(10);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray GermanyArray = GermanyObject.getJSONArray("data");
                            JSONArray JapanArray = JapanObject.getJSONArray("data");
                            JSONArray FranceArray = FranceObject.getJSONArray("data");
                            JSONArray USAArray = USAObject.getJSONArray("data");
                            JSONArray NetherlandsArray = NetherlandsObject.getJSONArray("data");
                            JSONArray DenmarkArray = DenmarkObject.getJSONArray("data");
                            JSONArray FinlandArray = FinlandObject.getJSONArray("data");
                            JSONArray ChinaArray = ChinaObject.getJSONArray("data");
                            JSONArray BelgiumArray = BelgiumObject.getJSONArray("data");
                            JSONArray OtherArray = OtherObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<Double> Germany = new ArrayList<>();
                            List<Double> Japan = new ArrayList<>();
                            List<Double> France = new ArrayList<>();
                            List<Double> USA = new ArrayList<>();
                            List<Double> Netherlands = new ArrayList<>();
                            List<Double> Denmark = new ArrayList<>();
                            List<Double> Finland = new ArrayList<>();
                            List<Double> China = new ArrayList<>();
                            List<Double> Belgium = new ArrayList<>();
                            List<Double> Other = new ArrayList<>();

                            for(int i=0;i<GermanyArray.length();i++){
                                years.add(yearArray.getInt(i));
                                Germany.add(GermanyArray.getDouble(i));
                                Japan.add(JapanArray.getDouble(i));
                                France.add(FranceArray.getDouble(i));
                                USA.add(USAArray.getDouble(i));
                                Netherlands.add(NetherlandsArray.getDouble(i));
                                Denmark.add(DenmarkArray.getDouble(i));
                                Finland.add(FinlandArray.getDouble(i));
                                China.add(ChinaArray.getDouble(i));
                                Belgium.add(BelgiumArray.getDouble(i));
                                Other.add(OtherArray.getDouble(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("finance_outstanding_debt_lending_country",null,null);

                            for(int i=0;i<GermanyArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("lending_country_id",i+1);
                                values.put("germany",100000);
                                //values.put("germany",Germany.get(i));
                                values.put("japan",Japan.get(i));
                                values.put("france",France.get(i));
                                values.put("usa",USA.get(i));
                                values.put("netherlands",Netherlands.get(i));
                                values.put("denmark",Denmark.get(i));
                                values.put("finland",Finland.get(i));
                                values.put("china",China.get(i));
                                values.put("belgium",Belgium.get(i));
                                values.put("other",Other.get(i));
                                values.put("year",years.get(i));

                                success = db.insertOrThrow("finance_outstanding_debt_lending_country",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "finance_outstanding_debt_lending_country: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "finance_outstanding_debt_lending_country: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_finance_statement_of_national_government_operations(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Statement of National Government Operations"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject natGovOpsObject = array.getJSONObject(1);
                            JSONObject amountInMillObject = array.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray natGovOpsArray = natGovOpsObject.getJSONArray("data");
                            JSONArray amountInMillArray = amountInMillObject.getJSONArray("data");

                            List<String> years = new ArrayList<>();
                            List<String> natGovOps = new ArrayList<>();
                            List<Float> amountInMills = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getString(i));
                                natGovOps.add(natGovOpsArray.getString(i));
                                amountInMills.add(Float.parseFloat(String.valueOf(amountInMillArray.get(i))));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("finance_statement_of_national_government_operations",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("operation_id",i+1);
                                values.put("year",years.get(i));
                                values.put("national_government_operation",natGovOps.get(i));
                                values.put("amount_in_millions",amountInMills.get(i));

                                success = db.insertOrThrow("finance_statement_of_national_government_operations",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "finance_statement_of_national_government_operations: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "finance_national_government_expenditure: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_finance_cdf_allocation_by_constituency(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("CDF Allocation"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject subCountyObject = array.getJSONObject(1);
                            JSONObject cdfObject = array.getJSONObject(2);
                            JSONObject yearObject = array.getJSONObject(3);

                            JSONArray countiesArray = countyObject.getJSONArray("data");
                            JSONArray subCountiesArray = subCountyObject.getJSONArray("data");
                            JSONArray cdfArray = cdfObject.getJSONArray("data");
                            JSONArray yearArray = yearObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("finance_cdf_allocation",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("cdfallocation_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countiesArray.getString(i)));
                                values.put("subcounty_id",countyHelper.getSubCountyId(subCountiesArray.getString(i)));
                                values.put("cdfallocation",cdfArray.getInt(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("finance_cdf_allocation",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "finance_cdf_allocation: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "finance_national_government_expenditure: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_finance_county_budget_allocation(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("County Budget Allocation"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countyObject = array.getJSONObject(0);
                            JSONObject reccurentObject = array.getJSONObject(1);
                            JSONObject devObject = array.getJSONObject(2);
                            JSONObject totalObject = array.getJSONObject(3);
                            JSONObject yearObject = array.getJSONObject(4);

                            JSONArray countiesArray = countyObject.getJSONArray("data");
                            JSONArray reccArray = reccurentObject.getJSONArray("data");
                            JSONArray devArray = devObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");
                            JSONArray yearArray = yearObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("finance_county_budget_allocation",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("budget_allocation_ID",i+1);
                                values.put("county_id",countyHelper.getCountyId(countiesArray.getString(i)));
                                values.put("recurrent",reccArray.getDouble(i));
                                values.put("development",devArray.getDouble(i));
                                values.put("total",totalArray.getDouble(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("finance_county_budget_allocation",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "finance_county_budget_allocation: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "finance_national_government_expenditure: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_finance_county_expenditure(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("County Expenditure"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countiesObject = array.getJSONObject(0);
                            JSONObject compObject = array.getJSONObject(1);
                            JSONObject goodsObject = array.getJSONObject(2);
                            JSONObject totalObject = array.getJSONObject(16);
                            JSONObject yearObject = array.getJSONObject(17);

                            JSONArray countiesArray = countiesObject.getJSONArray("data");
                            JSONArray compArray = compObject.getJSONArray("data");
                            JSONArray goodsArray = goodsObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");
                            JSONArray yearArray = yearObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("finance_county_expenditure",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("countyexpenditure_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countiesArray.getString(i)));
                                values.put("compensation_employees",compArray.getDouble(i));
                                values.put("goods_services",goodsArray.getDouble(i));
                                values.put("subsidies",goodsArray.getDouble(i));
                                values.put("grants_internationalorganisation",goodsArray.getDouble(i));
                                values.put("grants_governmentunits",goodsArray.getDouble(i));
                                values.put("othergrants",goodsArray.getDouble(i));
                                values.put("capitalgrants",goodsArray.getDouble(i));
                                values.put("socialbenefits",goodsArray.getDouble(i));
                                values.put("otherexpense",goodsArray.getDouble(i));
                                values.put("buildingstructures",goodsArray.getDouble(i));
                                values.put("plantmachinery_equipment",goodsArray.getDouble(i));
                                values.put("inventories",goodsArray.getDouble(i));
                                values.put("otherassets",goodsArray.getDouble(i));
                                values.put("acquisition_financialassets",goodsArray.getDouble(i));
                                values.put("interest_debt",goodsArray.getDouble(i));
                                values.put("total",totalArray.getDouble(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("finance_county_expenditure",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "finance_county_expenditure: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "finance_national_government_expenditure: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    private void insertInto_finance_county_revenue(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("County Revenue"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject countiesObject = array.getJSONObject(0);
                            JSONObject estObject = array.getJSONObject(1);
                            JSONObject grantObject = array.getJSONObject(2);
                            JSONObject shareObject = array.getJSONObject(3);
                            JSONObject totalObject = array.getJSONObject(4);
                            JSONObject yearObject = array.getJSONObject(5);

                            JSONArray countiesArray = countiesObject.getJSONArray("data");
                            JSONArray estArray = estObject.getJSONArray("data");
                            JSONArray grantArray = grantObject.getJSONArray("data");
                            JSONArray shareArray = shareObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");
                            JSONArray yearArray = yearObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("finance_county_revenue",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("county_revenue_id",i+1);
                                values.put("revenue_estimates",estArray.getInt(i));
                                values.put("conditional_grant",grantArray.getInt(i));
                                values.put("county_id",countyHelper.getCountyId(countiesArray.getString(i)));
                                values.put("equitable_share",shareArray.getInt(i));
                                values.put("total_revenue",totalArray.getInt(i));
                                values.put("year",yearArray.getString(i));

                                success = db.insertOrThrow("finance_county_revenue",null,values);
                            }

                            db.close();
                            d.dismiss();
                            Log.d(TAG, "finance_county_revenue: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "Error: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

}