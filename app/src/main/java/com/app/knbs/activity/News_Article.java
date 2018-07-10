package com.app.knbs.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.knbs.MainActivity;
import com.app.knbs.R;
import com.app.knbs.adapter.model.Sector_Data;
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.imageLoader.ImageLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;

public class News_Article extends AppCompatActivity{
    private String image,content,title;
    private Spanned Text;
    private ImageLoader imageLoader;

    private DatabaseHelper dbHelper = new DatabaseHelper(News_Article.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        //getSupportActionBar().setLogo(R.drawable.logo_toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        title = intent.getStringExtra("news_title");
        image = intent.getStringExtra("news_image");
        content = intent.getStringExtra("news_content");
        getSupportActionBar().setTitle(title);

        imageLoader = new ImageLoader(this);

        TextView textViewAbout = (TextView) findViewById(R.id.textNews);

        Text = Html.fromHtml(content);
        textViewAbout.setMovementMethod(LinkMovementMethod.getInstance());
        textViewAbout.setText(Text);

        ImageView image_news =(ImageView)findViewById(R.id.newsImageView);
        /*String sector_image = "sector_agriculture";
        int resource_id = getResources().getIdentifier(sector_image , "drawable", getPackageName());

        final int sdk = Build.VERSION.SDK_INT;
        if(sdk < Build.VERSION_CODES.JELLY_BEAN) {
            layout.setBackgroundDrawable( getResources().getDrawable(resource_id) );
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                layout.setBackground( getResources().getDrawable(resource_id));
            }
        }
        */
        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class
        try {
            imageLoader.DisplayImage(image, image_news);
        }catch (Exception e){

            Log.e("Error ", e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
