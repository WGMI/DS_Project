package com.app.knbs;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.knbs.activity.About;
import com.app.knbs.activity.DataRequest;
import com.app.knbs.activity.Favourites;
import com.app.knbs.activity.Feedback;
import com.app.knbs.activity.Sector;
import com.app.knbs.adapter.NewsAdapter;
import com.app.knbs.adapter.model.News;
import com.app.knbs.adapter.model.Sector_Data;
import com.app.knbs.app.PrefManager;
import com.app.knbs.database.DBHandler;
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.database.sectors.DatabaseEducation;
import com.app.knbs.database.sectors.DatabaseEducationApi;
import com.app.knbs.database.sectors.DatabaseFinanceApi;
import com.app.knbs.database.sectors.DatabaseGovernanceApi;
import com.app.knbs.database.sectors.DatabaseHealthApi;
import com.app.knbs.database.sectors.DatabasePopulationApi;
import com.app.knbs.services.ReportLoader;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener {

    private Context context;

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<News> newsList;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String check=null;
    private TextView textViewMessage;
    private EditText searchNews;
    private CoordinatorLayout coordinatorLayout;
    public ImageView facebook,twitter,whatsapp;

    private ReportLoader loader;
    private DatabaseEducationApi databaseEducationApi;
    private DatabaseFinanceApi databaseFinanceApi;
    private DatabaseHealthApi databaseHealthApi;
    private DatabasePopulationApi databasePopulationApi;
    private DatabaseGovernanceApi databaseGovernanceApi;

    private ProgressDialog dataFetchingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = this;

        loader = new ReportLoader(context);
        databaseEducationApi = new DatabaseEducationApi(context);
        databaseFinanceApi = new DatabaseFinanceApi(context);
        databaseHealthApi = new DatabaseHealthApi(context);
        databasePopulationApi = new DatabasePopulationApi(context);
        databaseGovernanceApi = new DatabaseGovernanceApi(context);

        dataFetchingDialog = new ProgressDialog(MainActivity.this);
        dataFetchingDialog.setTitle("Fetching Data");
        dataFetchingDialog.setMessage("Please wait...");
        dataFetchingDialog.setCanceledOnTouchOutside(false);
        dataFetchingDialog.show();

        //loader.loadReports();
        databaseEducationApi.loadData(dataFetchingDialog);
        databaseFinanceApi.loadData(dataFetchingDialog);
        databaseHealthApi.loadData(dataFetchingDialog);
        databasePopulationApi.loadData(dataFetchingDialog);
        databaseGovernanceApi.loadData(dataFetchingDialog);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        textViewMessage = (TextView) findViewById(R.id.textViewMessage);
        searchNews = (EditText) findViewById(R.id.seachNews);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        PrefManager pref = new PrefManager(getApplicationContext());
        swipeRefreshLayout.setOnRefreshListener(this);

        checkPermissions();

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(false);
                                        //loadOffers();
                                    }
                                }
        );

        newsList = new ArrayList<>();
        adapter = new NewsAdapter(getApplicationContext(), newsList);

        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Initializing NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        DatabaseHelper dbHelper = new DatabaseHelper(this );

        final List<String> list;
        list = dbHelper.getSectors();

        //Spinner in the Navigation Menu
        Spinner spinner = (Spinner) navigationView.getMenu().findItem(R.id.navigation_spinner).getActionView();
        spinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,list));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!String.valueOf(list.get(position)).matches("Select Sector")) {
                    String sector_name = String.valueOf(list.get(position));
                    Intent sector = new Intent(getApplicationContext(), Sector.class);
                    sector.putExtra("sector", sector_name);
                    if (
                            sector_name.matches("CPI") ||
                            sector_name.matches("Tourism")
                            ) {
                        sector.putExtra("tab", "national");
                    } else{
                        sector.putExtra("tab", "county");
                    }
                    startActivity(sector);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        News news = new News();
        news.setTitle("Quarterly Gross Domestic Product Report Second Quarter 2017");
        news.setDate("1 October, 2017");
        news.setContent("Real Quarterly Gross Domestic Product (QGDP) is estimated to have slowed down to 5.0 per cent in the second quarter of 2017 compared to 6.3 per cent in the corresponding quarter of 2016. The quarter in review was characterized by sharp increases in food prices as a result of adverse weather conditions and a notable rise in international oil prices. This led to a surge in inflationary pressures with the average inflation rate increasing more than two-fold from 5.36 per cent in the second quarter of 2016 to 10.80 per cent in the review quarter. The current account deficit widened to KSh 134.8 billion in the quarter under review from a deficit of KSh 114.1 billion in the corresponding quarter of 2016 on account of significant increase in the value of imports.\n" +
                "\n" +
                "The slowed but robust growth was supported by activities of Transport and Storage, Real Estate, Information and Communication Technology, Accommodation and Food services and a slightly improved growth in Wholesale and Retail Trade. On the other hand, the growth was somewhat constrained by subdued performances in Agriculture Forestry and Fishing (1.4 per cent), Manufacturing (2.3 per cent), Electricity (6.1 per cent) and Financial intermediation (4.3 per cent) thereby dampened the overall growth momentum during the quarter in review.\n" +
                "\n" +
                "Performance of the agriculture sector was exacerbated by widespread drought experienced during the fourth quarter of 2016 and somewhat suppressed long rains in 2017 that considerably affected crop production and rearing of animals. This led to a notable slowdown in the manufacture of food as agro-processing was negatively affected by constrained supply of food products. Electricity generation was also greatly affected by reduced rains thereby necessitating increased use of thermal sources whose intermediate inputs are more expensive compared to other sources. Growth in Financial Intermediation was also dampened by the effect of continued slow uptake of credit.\n" +
                "\n" +
                "During the quarter, the Kenyan Shilling marginally depreciated against the US dollar but appreciated slightly against the sterling pound. Performance of the Shilling against the Euro and the Japanese Yen remained largely unchanged during the quarter. In the regional front, the Shilling depreciated against the South African Rand and Tanzanian Shilling but appreciated slightly against the Ugandan Shilling. Commercial bank lending rates dropped from an average of 18.15 per cent in the second quarter of 2016 to 13.63 per cent in the quarter under review. ");
        news.setImage("https://www.knbs.or.ke/wp-content/uploads/2017/09/QGDP022017.png");

        newsList.add(news);

        News news0 = new News();
        news0.setTitle("Quarterly Balance of Payments Second Quarter 2017");
        news0.setDate("29 September, 2017");
        news0.setContent("In the second quarter of 2017, Imports valued on f.o.b increased by 15.5 per cent to KSh 410.9 billion from KSh 355.9 billion in the second quarter of 2016, mainly on account of increased bill on imports of food and petroleum products. On the other hand, exports increased by 2.9 per cent to KSh 150.2 billion in the same period. This resulted into widening of the merchandise trade deficit by 24.2 per cent to KSh 260.8 billion. Receipts from international trade in services expanded by 20.1 per cent to KSh 128.1 billion in the second quarter of 2017 mainly supported by increased travel receipts which grew by 38.0 per cent to KSh 23.5 billion. These developments resulted into 6.4 per cent growth in international trade in services inflows to register a surplus of KSh 38.9 billion in the quarter under review compared to a surplus of KSh 36.5 billion in the corresponding quarter of 2016. In the second quarter of 2017, diaspora remittances remained resilient increasing by 5.1 per cent to KSh 47.6 billion from KSh 45.3 billion in the second quarter of 2016. The surplus in the services account could not offset the deficit in merchandise trade balance leading into an expansion of 18.1 per cent in the current account deficit to KSh 134.8 billion in the quarter under review.\n" +
                "\n" +
                "Net financial inflows declined marginally to a surplus of KSh 133.4 billion in the second quarter of 2017 from KSh 134.1 billion in the second quarter of 2016. Gross official reserves increased by KSh 20.8 billion to stand at KSh 889.7 billion as at the end of the second quarter of 2017 compared to an increase of KSh 46.5 billion in the second quarter of 2016. ");
        news0.setImage("https://www.knbs.or.ke/wp-content/uploads/2017/09/BOP022017.png");

        newsList.add(news0);


        News news1 = new News();
        news1.setTitle("CPI and rates of inflation for September 2017");
        news1.setDate("29 September, 2017");
        news1.setContent("In line with the Kenya National Bureau of Statistics commitment to release data for the monthly Consumer Price Indices (CPI) and rates of inflation, we hereby release the numbers for September, 2017. These numbers were generated by conducting a survey of retail prices for a basket of goods and services, during the second and third weeks of the month under review. The prices were obtained from selected retail outlets in 25 data collection zones spread across Nairobi and 13 other urban centers.\n" +
                "\n" +
                "The CPI decreased by 0.57 per cent from 184.72 in August to 183.66 in September 2017. The overall year on year inflation stood at 7.06 per cent in September 2017.");
        news1.setImage("https://www.knbs.or.ke/wp-content/uploads/2017/09/cpi092017.png");

        newsList.add(news1);


        News news2 = new News();
        news2.setTitle("CPI and rates of inflation for August 2017");
        news2.setDate("31 August, 2017");
        news2.setContent("In line with the Kenya National Bureau of Statistics commitment to release data for the monthly Consumer Price Indices (CPI) and rates of inflation on the last working day of the month, we hereby release the numbers for August, 2017. These numbers are generated by surveying prices of a basket of goods and services.\n" +
                "\n" +
                "The survey was undertaken as normal, during the second and third weeks of the month under review. The prices are obtained from selected retail outlets in 25 data collection zones spread across Nairobi and 13 other urban centers.\n" +
                "\n" +
                "The CPI increased by 0.61 per cent from 183.78 in July to 184.72 in August 2017. The overall year on year inflation stood at 8.04 per cent in August 2017. ");
        news2.setImage("https://www.knbs.or.ke/wp-content/uploads/2017/08/cpi082017.png");

        newsList.add(news2);

        adapter.notifyDataSetChanged();

        searchNews.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {

                // filter your list from your input
                filter(s.toString());

                //you can use runnable postDelayed like 500 ms to delay search text
            }
        });


        searchNews.setImeOptions(EditorInfo.IME_ACTION_DONE);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchNews.getWindowToken(), 0);

                return false;
            }
        });

    }


    void filter(String text){
        List<News> temp = new ArrayList();
        for(News d: newsList){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(d.getTitle().toLowerCase().contains(text.toLowerCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        adapter.searchList(temp);
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        // showing refresh animation before making http call
        swipeRefreshLayout.setRefreshing(true);

        //handler.deleteAllOffers();

        //Intent grapprIntent = new Intent(getApplicationContext(), LoadNews.class);
        //startService(grapprIntent);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                //loadNews();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }

    private void loadNews() {

        newsList = new ArrayList<>();

        /*
        List<News_Article> nw = handler.getAllNews();

        for (News_Article news : nw) {
            check = news.getNewsID();
            newsList.add(news);
            adapter = new NewsAdapter(getApplicationContext(), newsList);
            recyclerView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }
        */

        Log.d("Check",check+" ok ");
        if(check==null){
            textViewMessage.setText(R.string.news_message);
            textViewMessage.setVisibility(View.VISIBLE);
        }else{
            textViewMessage.setVisibility(View.GONE);
        }
        swipeRefreshLayout.setRefreshing(false);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_favourite) {
            Intent fav = new Intent(getApplicationContext(), Favourites.class);
            fav.putExtra("sector", "Favourites");
            startActivity(fav);
        }else if (id == R.id.agiculture) {
            Intent sector = new Intent(getApplicationContext(), Sector.class);
            sector.putExtra("sector", "Agriculture");
            startActivity(sector);
        }else if (id == R.id.education) {
            Intent sector = new Intent(getApplicationContext(), Sector.class);
            sector.putExtra("sector", "Education");
            startActivity(sector);
        }else if (id == R.id.governance) {
            Intent sector = new Intent(getApplicationContext(), Sector.class);
            sector.putExtra("sector", "Governance");
            startActivity(sector);
        }else if (id == R.id.finance) {
            Intent sector = new Intent(getApplicationContext(), Sector.class);
            sector.putExtra("sector", "Public Finance");
            startActivity(sector);
        }else if (id == R.id.health) {
            Intent sector = new Intent(getApplicationContext(), Sector.class);
            sector.putExtra("sector", "Public Health");
            startActivity(sector);
        }else if (id == R.id.population) {
            Intent sector = new Intent(getApplicationContext(), Sector.class);
            sector.putExtra("sector", "Population and Vital Statistics");
            sector.putExtra("tab", "county");
            startActivity(sector);
        }else if (id == R.id.energy) {
            Intent sector = new Intent(getApplicationContext(), Sector.class);
            sector.putExtra("sector", "Energy");
            sector.putExtra("tab", "national");
            startActivity(sector);
        }else if (id == R.id.labour) {
            Intent sector = new Intent(getApplicationContext(), Sector.class);
            sector.putExtra("sector", "Labour");
            sector.putExtra("tab", "county");
            startActivity(sector);
        }else if (id == R.id.cpi) {
            Intent sector = new Intent(getApplicationContext(), Sector.class);
            sector.putExtra("sector", "CPI");
            sector.putExtra("tab", "county");
            startActivity(sector);
        }else if (id == R.id.manufacturing) {
            Intent sector = new Intent(getApplicationContext(), Sector.class);
            sector.putExtra("sector", "Manufacturing");
            sector.putExtra("tab", "national");
            startActivity(sector);
        }else if (id == R.id.tradeandcommerce) {
            Intent sector = new Intent(getApplicationContext(), Sector.class);
            sector.putExtra("sector", "Trade and Commerce");
            sector.putExtra("tab", "national");
            startActivity(sector);
        }else if (id == R.id.nav_about) {
            Intent about = new Intent(this, About.class);
            startActivity(about);
        }else if (id == R.id.nav_data) {
            Intent data = new Intent(this, DataRequest.class);
            startActivity(data);
        }  else if (id == R.id.nav_help) {
            Intent feedback = new Intent(this, Feedback.class);
            startActivity(feedback);
        }else if (id == R.id.nav_share) {
            share();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void share(){
        //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(getAc(), R.style.AppTheme));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View dialogView = inflater.inflate(R.layout.dialog, null);
        alertDialogBuilder.setMessage("Share ?");

        facebook = (ImageView) dialogView.findViewById(R.id.imageViewFacebook);
        twitter = (ImageView) dialogView.findViewById(R.id.imageViewTwitter);
        whatsapp = (ImageView) dialogView.findViewById(R.id.imageViewWhatsapp);

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgToShare = "Want more insights on Kenya Data, Download the KNBS Application from the PlayStore ";
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                // intent.putExtra(Intent.EXTRA_SUBJECT, "Foo bar"); // NB: has no effect!
                intent.putExtra(Intent.EXTRA_TEXT, msgToShare);

                // See if official Facebook app is found
                boolean facebookAppFound = false;
                List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                        intent.setPackage(info.activityInfo.packageName);
                        facebookAppFound = true;
                        break;
                    }
                }

                // As fallback, launch sharer.php in a browser
                if (!facebookAppFound) {
                    Toast.makeText(getApplicationContext(), "Application not found, opening browser", Toast.LENGTH_LONG).show();
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + msgToShare;
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }

                startActivity(intent);
            }

        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //share("twitter","your comment");
                Intent tweetIntent = new Intent(Intent.ACTION_SEND);
                tweetIntent.putExtra(Intent.EXTRA_TEXT, "Want more insights on Kenya Data, Download the KNBS Application from the PlayStore ");
                tweetIntent.setType("text/plain");

                PackageManager packManager = getPackageManager();
                List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

                boolean resolved = false;
                for (ResolveInfo resolveInfo : resolvedInfoList) {
                    if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                        tweetIntent.setClassName(
                                resolveInfo.activityInfo.packageName,
                                resolveInfo.activityInfo.name);
                        resolved = true;
                        break;
                    }
                }
                if (resolved) {
                    startActivity(tweetIntent);
                } else {
                    Intent i = new Intent();
                    i.putExtra(Intent.EXTRA_TEXT, "Want more insights on Kenya Data, Download the KNBS Application from the PlayStore ");
                    i.setAction(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode("Full of terror")));
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Twitter app isn't found", Toast.LENGTH_LONG).show();
                }

            }

        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PackageManager pm = getPackageManager();
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    String text = "Want more insights on Kenya Data, Download the KNBS Application from the PlayStore ";
                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    intent.setPackage("com.whatsapp");
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    //startActivity(Intent.createChooser(intent, "Share with"));
                    startActivity(intent);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "WhatsApp not Installed", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        alertDialogBuilder.setView(dialogView).setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //Dismiss
                arg0.dismiss();
            }
        });

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
            }
        });
        alertDialog.show();
    }


    private String urlEncode(String msg){
        try {
            return URLEncoder.encode(msg, "UTF-8");
        }catch (UnsupportedEncodingException e) {
            Log.wtf("UTF Issue", "UTF-8 should always be supported", e);
        }
        return "";
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void checkPermissions(){
        if (    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            List<String> permissions = new ArrayList<>();
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions( permissions.toArray( new String[permissions.size()] ), 101 );
            }
            //return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch ( requestCode ) {
            case 101: {
                View view;
                for( int i = 0; i < permissions.length; i++ ) {
                    if( grantResults[i] == PackageManager.PERMISSION_GRANTED ) {
                        Log.d( "Permissions", "Permission Granted: " + permissions[i] );
                    } else if( grantResults[i] == PackageManager.PERMISSION_DENIED ) {
                        Log.d( "Permissions", "Permission Denied: " + permissions[i] );
                        //Toast.makeText(getActivity(), "LoyaltyClub won't work well unless you allow requested permissions to be granted", Toast.LENGTH_LONG).show();
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, permissions[i]+ " : Permission Denied", Snackbar.LENGTH_INDEFINITE);
                        snackbar.show();
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

}
