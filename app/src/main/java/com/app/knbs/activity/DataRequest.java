package com.app.knbs.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.knbs.R;

/**
 * Mobile application created by Rodney on 15/07/2016.
 */
public class DataRequest extends AppCompatActivity {

    private EditText nameText;
    private EditText emailText;
    private EditText datasetText;
    private EditText yearText;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_request);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        //getSupportActionBar().setLogo(R.drawable.logo_toolbar);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }


    public void submit(View view) {
        //Toast.makeText(getApplicationContext(),"Thank you for your request", Toast.LENGTH_SHORT).show();
        String name = nameText.getText().toString();
        String dataset = datasetText.getText().toString();
        String email = emailText.getText().toString();
        String year = yearText.getText().toString();

        if (    name.isEmpty() || name.length() < 3
                ||email.isEmpty() || email.length() < 3
                ||dataset.isEmpty() || dataset.length() < 3
                ||year.isEmpty() || year.length() < 3) {
            message = "Please enter at least 3 characters for all fields";
        }else{
            Log.d("Data",name+" "+dataset+" "+email+" "+year);
        }

        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}

