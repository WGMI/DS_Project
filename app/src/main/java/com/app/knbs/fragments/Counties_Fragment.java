package com.app.knbs.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.app.knbs.R;
import com.app.knbs.adapter.ReportsCountiesAdapter;
import com.app.knbs.adapter.ReportsNationalAdapter;
import com.app.knbs.adapter.model.Sector;
import com.app.knbs.adapter.model.Report;
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.database.sectors.DatabaseMoneyAndBankingApi;

import java.util.ArrayList;
import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
/**
 * Developed by Rodney on 04/09/2017.
 */

public class Counties_Fragment extends Fragment  {

    DatabaseHelper dbHelper;
    private EditText inputSearch;
    private ReportsCountiesAdapter adapter;
    private List<Report> reportList;
    private String sector,source,api;

    public Counties_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_national, container, false);

        Intent intent = getActivity().getIntent();
        sector = intent.getStringExtra("sector");

        inputSearch = (EditText) view.findViewById(R.id.input_search);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        reportList = new ArrayList<>();

        dbHelper = new DatabaseHelper(getContext());

        dbHelper = new DatabaseHelper(getContext());

        adapter = new ReportsCountiesAdapter(getContext(), reportList);

        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        loadSectors();

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {
                // filter your list from your input
                filter(s.toString());
            }
        });


        inputSearch.setImeOptions(EditorInfo.IME_ACTION_DONE);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);

        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);

                return false;
            }
        });

        return view;
    }

    private void loadSectors() {
        List<Sector> list = dbHelper.getSectorsReports(sector,"county");
        for (int i =0; i< list.size(); i++) {
            Sector sec = list.get(i);

            Report report = new Report();
            report.setSectorID(sec.getSectorID());
            report.setReport(sec.getReport());
            report.setSector(sec.getSector());
            report.setPublisher("Kenya National Bureau of Statistics");
            report.setFavourite(sec.getFavourite());
            report.setSource(sec.getSource());
            report.setTable(sec.getTable());

            reportList.add(report);
            adapter.notifyDataSetChanged();
        }
    }

    void filter(String text){
        List<Report> temp = new ArrayList();
        for(Report d: reportList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getReport().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        adapter.searchList(temp);
    }

}