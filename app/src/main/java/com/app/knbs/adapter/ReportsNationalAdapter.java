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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.knbs.R;
import com.app.knbs.activity.ReportGraph;
import com.app.knbs.adapter.model.Report;
import com.app.knbs.adapter.model.Sector_Data;
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.services.FavouriteService;

import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 10/10/2017.
 */

public class ReportsNationalAdapter extends RecyclerView.Adapter<ReportsNationalAdapter.MyViewHolder> {

    private Context mContext;
    private List<Report> reportList;

    public ReportsNationalAdapter(Context mContext, List<Report> reportList) {
        this.mContext = mContext;
        this.reportList = reportList;

    }

    @Override
    public ReportsNationalAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_report, parent, false);

        return new ReportsNationalAdapter.MyViewHolder(itemView);
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

            Intent graph = new Intent(view.getContext(), ReportGraph.class);
            graph.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            graph.putExtra("report", reports.getReport());
            graph.putExtra("sector", reports.getSector());
            graph.putExtra("source", reports.getSource());
            graph.putExtra("table", reports.getTable());
            graph.putExtra("api", reports.getApi());
            Log.d(TAG, "onClick: " + reports.getReport());
            mContext.startActivity(graph);

            }
    }

    @Override
    public void onBindViewHolder(final ReportsNationalAdapter.MyViewHolder holder, int position) {
       final Report reports = reportList.get(position);

        holder.report.setText(reports.getReport());
        holder.source.setText(reports.getSource());
        holder.publisher.setText(reports.getPublisher());

        String start_year ="";
        String end_year = "";
        DatabaseHelper dbHelper = new DatabaseHelper(mContext.getApplicationContext());
        List<Sector_Data> list ;

        try {
            list = dbHelper.getYears(reports.getTable());
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
                grapprIntent.putExtra("tab", "national");
                grapprIntent.putExtra("sector", reports.getSector());
                grapprIntent.putExtra("sector_id", reports.getSectorID());
                mContext.getApplicationContext().startService(grapprIntent);

            }else if(finalStatus.matches("0")){
                holder.favourite.setImageDrawable(active);
                Intent grapprIntent = new Intent(mContext.getApplicationContext(), FavouriteService.class);
                grapprIntent.putExtra("status", "1");
                grapprIntent.putExtra("tab", "national");
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
}


