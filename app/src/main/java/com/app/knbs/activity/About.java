package com.app.knbs.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import com.app.knbs.R;
import com.app.knbs.database.DatabaseHelper;

/**
 * Mobile application created by Rodney on 15/07/2016.
 */
public class About extends AppCompatActivity {

    String about = "\n" +
            "<h5>Vision Statement</h5>\n" +
            "<p>To be a centre of excellence in production and management of quality statistics.</p>\n" +
            "<h5>Mission Statement</h5>\n" +
            "<p>To be a centre of excellence in production and management of quality statistics.</p>\n" +
            "<p>To develop, provide and promote quality statistical information for evidence-based decision making.</p>";
    Spanned Text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        //getSupportActionBar().setLogo(R.drawable.logo_toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textViewAbout = (TextView) findViewById(R.id.about_text);

        Text = Html.fromHtml(about);
        textViewAbout.setMovementMethod(LinkMovementMethod.getInstance());
        textViewAbout.setText(Text);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

}

