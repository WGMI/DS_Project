package com.app.knbs.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.knbs.R;
import com.app.knbs.activity.ReportGraph;
import com.app.knbs.adapter.model.Report;
import com.app.knbs.adapter.model.Sector_Data;
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.services.FavouriteService;

import java.util.List;

/**
 * Developed by Rodney on 10/10/2017.
 */

public class ReportsCountiesAdapter extends RecyclerView.Adapter<ReportsCountiesAdapter.MyViewHolder> {

    private Context mContext;
    private List<Report> reportList;
    private DatabaseHelper dbHelper;
    private String  county = "Select County";
    private Spinner counties;

    public ReportsCountiesAdapter(Context mContext, List<Report> reportList) {
        this.mContext = mContext;
        this.reportList = reportList;

        dbHelper = new DatabaseHelper(mContext.getApplicationContext());
    }

    @Override
    public ReportsCountiesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_report, parent, false);

        return new ReportsCountiesAdapter.MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView report;
        TextView source;
        TextView years;
        TextView publisher;
        ImageView favourite;
        

        private MyViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.report = (TextView) view.findViewById(R.id.report);
            this.source = (TextView) view.findViewById(R.id.source);
            this.years = (TextView) view.findViewById(R.id.years);
            this.publisher = (TextView) view.findViewById(R.id.publisher);

            this.favourite = (ImageView) view.findViewById(R.id.imageFavourite);
        }

        @Override
        public void onClick(final View view) {

            final Report reports = reportList.get(getPosition());
            dbHelper = new DatabaseHelper(mContext.getApplicationContext());

            Log.d("table_name", "onClick: " + reports.getTable());

            List<String> countylist ;
            mContext = itemView.getContext();

            final Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.dialog_county);
            dialog.setTitle("Select County");

            dbHelper.getCounties(reports.getTable());

            countylist = dbHelper.getCounties(reports.getTable());

            counties = (Spinner) dialog.findViewById(R.id.countySpinner);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(mContext.getApplicationContext(),R.layout.simple_spinner_item, countylist);
            //ArrayAdapter<CharSequence> adapterSpinner  = ArrayAdapter.createFromResource(mContext.getApplicationContext(), R.array.counties, R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            counties.setAdapter(adapterSpinner);
            counties.setOnItemSelectedListener(new ItemSelectedListener());

            Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        int count = Integer.parseInt(dbHelper.checkSectorsReports(reports.getTable(),county));

                        if(county.matches("Select County")) {
                            Snackbar snackbar = Snackbar
                                    .make(view, "Please select county ", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }else{
                            if (count > 0) {
                                Intent graph = new Intent(mContext.getApplicationContext(), ReportGraph.class);
                                graph.putExtra("report", reports.getReport());
                                graph.putExtra("sector", reports.getSector());
                                graph.putExtra("county", county);
                                graph.putExtra("source", reports.getSource());
                                graph.putExtra("table", reports.getTable());
                                graph.putExtra("api", reports.getApi());
                                mContext.startActivity(graph);
                            } else {
                                Snackbar snackbar = Snackbar
                                        .make(view, county + " County currently has no data for " + reports.getReport(), Snackbar.LENGTH_LONG);
                                snackbar.show();
                            }
                        }
                        dialog.dismiss();


                }
            });

            dialog.show();

        }
    }

    @Override
    public void onBindViewHolder(final ReportsCountiesAdapter.MyViewHolder holder, int position) {
        final Report reports = reportList.get(position);

        Log.d("report_table", "onBindViewHolder: " + reports.getTable());

        holder.report.setText(reports.getReport());
        holder.source.setText(reports.getSource());
        holder.publisher.setText(reports.getPublisher());

        String start_year ="";
        String end_year = "";
        List<Sector_Data> list ;

        try {
            String table = reports.getTable();
            if(table.contains("land_and_climate_surface_area_by_category")){
                table = "land_and_climate_environment_impact_assessments_by_sector";
            }
            list = dbHelper.getYears(table);
            for (int i = 0; i < list.size(); i++) {
                Sector_Data data = list.get(i);
                end_year = data.getYear();
            }
            String s_year ="";
            for (int i = list.size() - 1; i >= 0; i--) {
                Sector_Data data = list.get(i);
                s_year = data.getYear();
                start_year = data.getYear() + " - ";
            }

            if(s_year.matches(end_year)){
                holder.years.setText(end_year);
            }else {
                holder.years.setText(start_year + end_year);
            }
            Log.d("Start Year", start_year);
            Log.d("End Year", end_year);



        }catch (Exception e){
            e.printStackTrace();
        }

        String active_fav = "@drawable/favourite";  // where myresource (without the extension) is the file
        String inactive_fav = "@drawable/unfavourite";  // where myresource (without the extension) is the file

        int imageResource1 = mContext.getResources().getIdentifier(active_fav, null, mContext.getPackageName());
        int imageResource2 = mContext.getResources().getIdentifier(inactive_fav, null, mContext.getPackageName());

        final Drawable active = mContext.getResources().getDrawable(imageResource1);
        final Drawable inactive= mContext.getResources().getDrawable(imageResource2);
        String status = "";

        if (reports.getFavourite().matches("1")){
            holder.favourite.setImageDrawable(active);
            status = "1";
        }else if(reports.getFavourite().matches("0")){
            holder.favourite.setImageDrawable(inactive);
            status = "0";
        }

        final String finalStatus = status;
        holder.favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View convertView) {
            // if image source equals this drawable then...
            // else change the image source to this drawable...
            if (finalStatus.matches("1")){
                holder.favourite.setImageDrawable(inactive);
                Intent grapprIntent = new Intent(mContext.getApplicationContext(), FavouriteService.class);
                grapprIntent.putExtra("status", "0");
                grapprIntent.putExtra("tab", "county");
                grapprIntent.putExtra("sector", reports.getSector());
                grapprIntent.putExtra("sector_id", reports.getSectorID());
                mContext.getApplicationContext().startService(grapprIntent);
            }else if(finalStatus.matches("0")){
                holder.favourite.setImageDrawable(active);
                Intent grapprIntent = new Intent(mContext.getApplicationContext(), FavouriteService.class);
                grapprIntent.putExtra("status", "1");
                grapprIntent.putExtra("tab", "county");
                grapprIntent.putExtra("sector", reports.getSector());
                grapprIntent.putExtra("sector_id", reports.getSectorID());
                mContext.getApplicationContext().startService(grapprIntent);
            }

            }
        });

    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public void searchList(List<Report> list){
        reportList = list;
        notifyDataSetChanged();
    }

    public void clear() {
        int size = this.reportList.size();
        this.reportList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        //get strings of first item
        private String firstItem = String.valueOf(counties.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            // An item was selected. You can retrieve the selected item using
            if (firstItem.equals(String.valueOf(counties.getSelectedItem()))) {
                // ToDo when first item is selected

            } else {
                Toast.makeText(parent.getContext(),parent.getItemAtPosition(pos).toString()+" County selected", Toast.LENGTH_LONG).show();
                county = parent.getItemAtPosition(pos).toString();
                // Todo when item is selected by the user
            }

        }

        public void onNothingSelected(AdapterView<?> parent) {
            // Another interface callback
        }
    }
}


