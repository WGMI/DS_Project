package com.app.knbs.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.app.knbs.MainActivity;
import com.app.knbs.R;
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.fragments.Counties_Fragment;
import com.app.knbs.fragments.National_Fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Developed by Rodney on 04/09/2017.
 */

public class Sector extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ImageView facebook,twitter,whatsapp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_national_county);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String sector = intent.getStringExtra("sector");
        String tab = intent.getStringExtra("tab");
        getSupportActionBar().setTitle(sector);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        Log.d("Tab",tab+"");
        if(tab!=null&&tab.matches("county")) {
            viewPager.setCurrentItem(1);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#007F00"));
        tabLayout.setSelectedTabIndicatorHeight((int) (4 * getResources().getDisplayMetrics().density));

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

        Spinner spinner = (Spinner) navigationView.getMenu().findItem(R.id.navigation_spinner).getActionView();
        spinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,list));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!String.valueOf(list.get(position)).matches("Select Sector")) {
                    Intent sector = new Intent(getApplicationContext(), Sector.class);
                    sector.putExtra("sector", String.valueOf(list.get(position)));
                    sector.putExtra("tab", "county");
                    startActivity(sector);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new National_Fragment(), "National");
        adapter.addFragment(new Counties_Fragment(), "County");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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

        if (id == R.id.nav_home) {
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
        }else if (id == R.id.nav_favourite) {
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
}
