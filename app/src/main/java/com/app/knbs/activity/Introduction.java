package com.app.knbs.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.app.knbs.MainActivity;
import com.app.knbs.R;
import com.app.knbs.app.PrefManager;
import com.app.knbs.database.DatabaseHelper;

import java.io.IOException;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import agency.tango.materialintroscreen.animations.IViewTranslation;

/**
 * Developed by Rodney on 10/10/2017.
 */

public class Introduction extends MaterialIntroActivity {

    PrefManager pref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableLastSlideAlphaExitTransition(true);

        pref = new PrefManager(getApplicationContext());
        loadMainActivity();

        getBackButtonTranslationWrapper()
                .setEnterTranslation(new IViewTranslation() {
                    @Override
                    public void translate(View view, @FloatRange(from = 0, to = 1.0) float percentage) {
                        view.setAlpha(percentage);
                    }
                });

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.first_slide_background)
                        .buttonsColor(R.color.first_slide_buttons)
                        .image(R.drawable.intro)
                        .title("Welcome to the Kenya National Bureau of Statistics")
                        .description("Application")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("KNBS acts as the principal agency of the Government for collecting, analysing and disseminating statistical data in Kenya.");
                    }
                }, "Welcome"));


        addSlide(new Terms_Conditions());

        addSlide(new SlideFragmentBuilder()
                        .backgroundColor(R.color.third_slide_background)
                        .buttonsColor(R.color.third_slide_buttons)
                        //.possiblePermissions(new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_SMS})
                        .neededPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                        .image(R.drawable.terms)
                        .title("We require the following permissions(s)")
                        .description("These will help the application run smoothly")
                        .build(),
                new MessageButtonBehaviour(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMessage("Thank you allowing required permissions!");
                    }
                }, "Message"));



        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.green)
                .buttonsColor(R.color.third_slide_buttons)
                .image(R.drawable.done)
                .title("That's it, You're all done")
                .description("Proceed to Use the application")
                .build());
    }

    @Override
    public void onFinish() {
        super.onFinish();
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        try {
            dbHelper.prepareDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        pref.setFirstTimeLaunch(false);
        Toast.makeText(this, "Welcome to the KNBS Application! :)", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);

    }

    private void loadMainActivity(){


        if(!pref.isFirstTimeLaunch()) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
