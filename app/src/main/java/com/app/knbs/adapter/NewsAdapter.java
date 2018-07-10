package com.app.knbs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.knbs.R;
import com.app.knbs.activity.News_Article;
import com.app.knbs.adapter.model.News;
import com.app.knbs.app.MyApplication;
import com.app.knbs.database.DBHandler;
import com.app.knbs.imageLoader.ImageLoader;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Developed by Rodney on 22/10/2016.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

private Context mContext;
private List<News> newsList;
private ImageLoader imageLoader;

public NewsAdapter(Context mContext, List<News> newsList) {
    this.mContext = mContext;
    this.newsList = newsList;

    imageLoader = new ImageLoader(mContext);
}

@Override
public NewsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.cardview_news, parent, false);

    return new NewsAdapter.MyViewHolder(itemView);
}

public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    TextView title,date,content;
    ImageView news_image;

    private MyViewHolder(View view) {
        super(view);
        view.setOnClickListener(this);
        this.title = (TextView) view.findViewById(R.id.textViewTitle);
        this.date = (TextView) view.findViewById(R.id.textViewDate);
        this.content = (TextView) view.findViewById(R.id.textViewContent);

        this.news_image = (ImageView) view.findViewById(R.id.news_image);
    }

    @Override
    public void onClick(final View view) {

        final News news = newsList.get(getPosition());
        // Get tracker.
        Tracker tracker = ((MyApplication) mContext.getApplicationContext()).getDefaultTracker();

        // Build and send an Event.
        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("News_Article")
                .setAction("Views")
                .setLabel(news.getTitle())
                .build());

        Intent intent = new Intent(view.getContext(), News_Article.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("news_title", news.getTitle());
        intent.putExtra("news_image", news.getImage());
        intent.putExtra("news_content", news.getContent());
        mContext.startActivity(intent);
    }
}

    @Override
    public void onBindViewHolder(final NewsAdapter.MyViewHolder holder, int position) {
        News news = newsList.get(position);

        holder.title.setText(news.getTitle());
        holder.date.setText(news.getDate());
        holder.content.setText(news.getContent());

        String day = null;
        try {
            day = date(news.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.date.setText(day);

        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        try {

            imageLoader.DisplayImage(news.getImage(), holder.news_image);
        }catch (Exception e){

            Log.e("Error ", e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public String date(String date) throws ParseException {

        SimpleDateFormat readFormat = new SimpleDateFormat("dd MMMM, yyyy");
        SimpleDateFormat writeFormat = new SimpleDateFormat("dd MMM, yyyy");

        java.util.Date readdate;

        readdate = readFormat.parse(date);
        String writedate = writeFormat.format(readdate);

        return writedate;
    }

    public void searchList(List<News> list){
        newsList = list;
        notifyDataSetChanged();
    }

    public void clear() {
        int size = this.newsList.size();
        this.newsList.clear();
        notifyItemRangeRemoved(0, size);
    }
}


