package com.app.knbs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.app.knbs.R;

import agency.tango.materialintroscreen.SlideFragment;
/**
 * Developed by Rodney on 10/10/2017.
 */

public class Terms_Conditions  extends SlideFragment {
        private CheckBox checkBox;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.fragment_intro, container, false);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            return view;
        }

        @Override
        public int backgroundColor() {
            return R.color.custom_slide_background;
        }

        @Override
        public int buttonsColor() {
            return R.color.custom_slide_buttons;
        }

        @Override
        public boolean canMoveFurther() {
            return checkBox.isChecked();
        }

        @Override
        public String cantMoveFurtherErrorMessage() {
            return getString(R.string.error_message);
        }
    }
