package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.perfect.easyshopplus.Adapter.FaqsAdapter;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class FAQActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener {

    TextView aboutUsDescription,privacyPolicyDescription,
            termsAndConditionDescription,tvuser, tvcart, tv_notification,tv_header,txtvfaq1;
  //  TextView termsAndCondition,rateUs,aboutUs,privacyPolicy,feedBack,faq,faq_description;
    EditText feedbackText, etSearch;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    DBHandler db;
    String item;
    RelativeLayout rl_notification;
    RecyclerView faq_recyclerview;

    String TAG = "FAQActivity";
    BottomNavigationView navigation;
    String fromCategory = "from";
    String valueCategory = "home";
    String fromCart = "From";
    String valueCart = "Home";
    String fromHome = "";
    String valueHome = "";
    String fromFavorite = "From";
    String valueFavorite = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_faq_main);

        ImageView imbanner=findViewById(R.id.imbanner);
        Glide.with( this ).load( R.drawable.faqgif ).into( imbanner );
        initiateViews();
        setRegViews();
        setBottomBar();

        Log.e(TAG,"Start    86   ");

        SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        SharedPreferences RequiredStorepref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF36, 0);

        SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF195, 0);
        tv_header.setText(pref5.getString("faq", null));

        SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF237, 0);
        txtvfaq1.setText(pref6.getString("frequentlyaskedquestions", null));
       /* if(RequiredStorepref.getString("RequiredStore", null).equals("false")&&RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else if(RequiredStorepref.getString("RequiredStore", null).equals("true")&&RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu2();
        }else if(RequiredStorepref.getString("RequiredStore", null).equals("false")&&RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("true")){
            setHomeNavMenu3();
        }else if(RequiredStorepref.getString("RequiredStore", null).equals("true")&&RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("true")){
            setHomeNavMenu();
        }*/

        if(RequiredStorepref.getString("RequiredStore", null).equals("false")){
            setHomeNavMenu1();
        }else if(RequiredStorepref.getString("RequiredStore", null).equals("true")){
            setHomeNavMenu2();
        }

//        SharedPreferences Inshoppref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF43, 0);
//        if(Inshoppref.getString("RequiredInshop", null).equals("false")){
//            faq_description.setText(R.string.faq1);
//        }else{
//            faq_description.setText(R.string.faq
//            );
//        }
        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
        ImageView imApplogo=findViewById(R.id.imApplogo);
        String strImagepath= IMAGEURL + pref3.getString("AppIconImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath).into(imApplogo);

//        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF34, 0);
//        ImageView imCompanylogo=findViewById(R.id.imCompanylogo);
//        String strimagepath= IMAGEURL + pref1.getString("CompanyLogoImageCode", null);
//        PicassoTrustAll.getInstance(this).load(strimagepath).into(imCompanylogo);

        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        tvuser.setText("Welcome "+pref2.getString("username", null));
        db=new DBHandler(this);
        tvcart.setText(String.valueOf(db.selectCartCount()+db.selectInshopCartCount()));
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));

        faqlisting();
    }
    public void faqlisting() {
        SharedPreferences sharedPreferences = getSharedPreferences("localpref", MODE_PRIVATE);
        String value = sharedPreferences.getString("pref_faq", "");
        Log.e(TAG,"Start    86   "+value);
        try {
            JSONArray jsonArray = new JSONArray(value);
            if (jsonArray.length() != 0 && !jsonArray.equals(null)) {
                GridLayoutManager lLayout = new GridLayoutManager(this, 1);
                faq_recyclerview.setLayoutManager(lLayout);
                faq_recyclerview.setHasFixedSize(true);
                FaqsAdapter adapter = new FaqsAdapter(this, jsonArray);
                faq_recyclerview.setAdapter(adapter);
            }
        } catch(Exception e){

        }

    }

    private void initiateViews() {
        etSearch=(EditText)findViewById(R.id.etSearch);
        imcart=(ImageView) findViewById(R.id.imcart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        im = (ImageView) findViewById(R.id.im);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        tv_header= (TextView) findViewById(R.id.tv_header);
        txtvfaq1= (TextView) findViewById(R.id.txtvfaq1);
//        feedBack = findViewById(R.id.feedback);
//        rateUs = findViewById(R.id.rate_us);
//        aboutUs = findViewById(R.id.about_us);
//        privacyPolicy= findViewById(R.id.privacy_policy);
       // termsAndCondition  = findViewById(R.id.terms_and_condition);
//        faq = (TextView) findViewById(R.id.faq);
        aboutUsDescription = findViewById(R.id.about_us_description);
        privacyPolicyDescription = findViewById(R.id.privacy_policy_description);
        termsAndConditionDescription = findViewById(R.id.terms_and_condition_description);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
     //   faq_description = (TextView) findViewById(R.id.faq_description);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        faq_recyclerview = findViewById(R.id.faq_recyclerview);
    }

    private void setRegViews() {
        im.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        imcart.setOnClickListener(this);
        tvcart.setOnClickListener(this);
//        termsAndCondition.setOnClickListener(this);
//        rateUs.setOnClickListener(this);
//        aboutUs.setOnClickListener(this);
//        privacyPolicy.setOnClickListener(this);
//        feedBack.setOnClickListener(this);
//        faq.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
//
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im:

                //  drawer.openDrawer(Gravity.START);
                onBackPressed();
                break;
            case R.id.etSearch:
                startActivity(new Intent(FAQActivity.this,SearchActivity.class));
                break;
            case R.id.imcart:
                Intent in = new Intent(FAQActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.tvcart:
                Intent i = new Intent(FAQActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.tv_notification:
                startActivity(new Intent(FAQActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(FAQActivity.this, NotificationActivity.class));
                break;
            case R.id.feedback:
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FAQActivity.this);
                    LayoutInflater inflater1 = (LayoutInflater) FAQActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater1.inflate(R.layout.feedback_pop_up_layout, null);
                    final Spinner spinner = layout.findViewById(R.id.spinner);
                    feedbackText = layout.findViewById(R.id.feedbackText);
                    final TextView submitFeedback = layout.findViewById(R.id.submitFeedback);
                    final TextView cancelFeedback = layout.findViewById(R.id.cancelFeedback);
                    builder.setView(layout);
                    spinner.setOnItemSelectedListener(this);
                    List<String> categories = new ArrayList<String>();
                    categories.add("Report an error");
                    categories.add("Give a suggestion");
                    categories.add("Anything else");
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(FAQActivity.this, android.R.layout.simple_spinner_item, categories);
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(dataAdapter);
                    final AlertDialog alertDialog = builder.create();
                    cancelFeedback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    submitFeedback.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            sendEmail();
                        }
                    });
                    alertDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.privacy_policy:
                if (privacyPolicyDescription.getVisibility() == View.VISIBLE){
                    privacyPolicyDescription.setVisibility(View.GONE);
                } else {
                    privacyPolicyDescription.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.about_us:
                if (aboutUsDescription.getVisibility() == View.VISIBLE){
                    aboutUsDescription.setVisibility(View.GONE);
                }else {
                    aboutUsDescription.setVisibility(View.VISIBLE);
                }
                break;
//            case R.id.faq:
//                if (faq_description.getVisibility() == View.VISIBLE){
//                    faq_description.setVisibility(View.GONE);
//                }else {
//                    faq_description.setVisibility(View.VISIBLE);
//                }
//                break;
            case R.id.rate_us:
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FAQActivity.this);
                    LayoutInflater inflater1 = (LayoutInflater) FAQActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater1.inflate(R.layout.rate_us_pop_up, null);
                    final Button rateUs = layout.findViewById(R.id.rate_us);
                    final TextView feedbk =layout.findViewById(R.id.wantFeedback);
                    final TextView lovethis =layout.findViewById(R.id.tv_lovethis);

                    builder.setView(layout);

                    final AlertDialog alertDialog = builder.create();
                    SharedPreferences pref44 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF238, 0);
                    feedbk.setText(pref44.getString("wewantyourfeedback", null));

                    SharedPreferences pref55 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF239, 0);
                    lovethis.setText(pref55.getString("lovethisapprateus", null));

                    SharedPreferences pref66 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF240, 0);
                    rateUs.setText(pref66.getString("ratenow", null));
                    rateUs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                            Uri uri = Uri.parse("market://details?id=" + getPackageName());
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            try {
                                startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                            }
                        }
                    });
                    alertDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.terms_and_condition:
                if (termsAndConditionDescription.getVisibility() == View.VISIBLE){
                    termsAndConditionDescription.setVisibility(View.GONE);
                }
                else {
                    termsAndConditionDescription.setVisibility(View.VISIBLE);
                }
                break;
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void setHomeNavMenu2() {
        final String[] menulist = new String[]{"Home","My Cart", "Track Orders", "Favourite Items","Favourite Stores",
                "Notifications", "My Profile", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.navmycart, R.drawable.navmyorder, R.drawable.navfvrts,
                R.drawable.navmyorder,R.drawable.navntfn, R.drawable.navmyprofile,
                R.drawable.navlgt, R.drawable.navshare,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
       };
        NavMenuAdapter adapter = new NavMenuAdapter(FAQActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(FAQActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(FAQActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(FAQActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(FAQActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(FAQActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(FAQActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 6) {
                    startActivity(new Intent(FAQActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    doLogout();
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 8) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "\t" + "Invite You  \n\n For Android Users \n http://play.google.com/store/apps/details?id=" + getPackageName() +
                            "\n" );
                    startActivity(Intent.createChooser(share, "Invite this App to your friends"));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 9) {
                    startActivity(new Intent(FAQActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 10) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FAQActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) FAQActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater1.inflate(R.layout.rate_us_pop_up, null);
                        final Button rateUs = layout.findViewById(R.id.rate_us);
                        final TextView feedbk =layout.findViewById(R.id.wantFeedback);
                        final TextView lovethis =layout.findViewById(R.id.tv_lovethis);
                        builder.setView(layout);

                        final AlertDialog alertDialog = builder.create();
                        SharedPreferences pref44 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF238, 0);
                        feedbk.setText(pref44.getString("wewantyourfeedback", null));

                        SharedPreferences pref55 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF239, 0);
                        lovethis.setText(pref55.getString("lovethisapprateus", null));

                        SharedPreferences pref66 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF240, 0);
                        rateUs.setText(pref66.getString("ratenow", null));
                        rateUs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                                }
                            }
                        });
                        alertDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (position == 11) {
                    startActivity(new Intent(FAQActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(FAQActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(FAQActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 14) {
                    startActivity(new Intent(FAQActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void setHomeNavMenu1() {
        final String[] menulist = new String[]{"Home","My Cart", "Track Orders", "Favourite Items",
                "Notifications", "My Profile", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications, R.drawable.ic_member, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(FAQActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(FAQActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(FAQActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(FAQActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(FAQActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(FAQActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(FAQActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    doLogout();
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 7) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "\t" + "Invite You  \n\n For Android Users \n http://play.google.com/store/apps/details?id=" + getPackageName() +
                            "\n" );
                    startActivity(Intent.createChooser(share, "Invite this App to your friends"));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 8) {
                    startActivity(new Intent(FAQActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 9) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FAQActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) FAQActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater1.inflate(R.layout.rate_us_pop_up, null);
                        final Button rateUs = layout.findViewById(R.id.rate_us);
                        final TextView feedbk =layout.findViewById(R.id.wantFeedback);
                        final TextView lovethis =layout.findViewById(R.id.tv_lovethis);
                        builder.setView(layout);

                        final AlertDialog alertDialog = builder.create();
                        SharedPreferences pref44 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF238, 0);
                        feedbk.setText(pref44.getString("wewantyourfeedback", null));

                        SharedPreferences pref55 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF239, 0);
                        lovethis.setText(pref55.getString("lovethisapprateus", null));

                        SharedPreferences pref66 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF240, 0);
                        rateUs.setText(pref66.getString("ratenow", null));
                        rateUs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                                }
                            }
                        });
                        alertDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (position == 10) {
                    startActivity(new Intent(FAQActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 11) {
                    startActivity(new Intent(FAQActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(FAQActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(FAQActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FAQActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) FAQActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.logout_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);
            TextView tv_popupdelete= (TextView) layout.findViewById(R.id.tv_popupdelete);
            TextView tv_popupdeleteinfo3= (TextView) layout.findViewById(R.id.tv_popupdeleteinfo3);
            TextView edit= (TextView) layout.findViewById(R.id.edit);
            TextView tv_no= (TextView) layout.findViewById(R.id.tv_no);

            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();

            SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF241, 0);
            tv_popupdelete.setText(pref4.getString("Logoutaccount", null));

            SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF242, 0);
            tv_popupdeleteinfo3.setText(pref5.getString("areyousureyouwanttologout", null));

            SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF186, 0);
            edit.setText(pref6.getString("yes", null));

            SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF187, 0);
            tv_no.setText(pref7.getString("no", null));

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    dologoutchanges();


                    if(getResources().getString(R.string.app_name).equals("JZT CART") || getResources().getString(R.string.app_name).equals("Pulikkottil Hypermarket") || getResources().getString(R.string.app_name).equals("NeethiMed")|| getResources().getString(R.string.app_name).equals("Touch n Buy")) {

                        startActivity(new Intent(FAQActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(FAQActivity.this,LocationActivity.class));
                        finish();}
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }

                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dologoutchanges(){
        Config.logOut(this);
    }
    protected void sendEmail() {

        SharedPreferences Sendemail = getApplicationContext().getSharedPreferences(Config.SHARED_PREF357, 0);
        String Sendmail = Sendemail.getString("Sendemail","");
        Toast.makeText(FAQActivity.this, Sendmail+"", Toast.LENGTH_SHORT).show();        String[] TO = {"touchnbuysupport@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, item);
        emailIntent.putExtra(Intent.EXTRA_TEXT, feedbackText.getText().toString());
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (ActivityNotFoundException ex) {
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }


    private void setBottomBar() {
        navigation= (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().setGroupCheckable(0, false, true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected( MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_category:
                    Intent io = new Intent(FAQActivity.this, OutShopActivity.class);
                    io.putExtra(fromCategory, valueCategory);
                    finish();
                    startActivity(io);
                    return true;
                case R.id.navigation_cart:
                    Log.e(TAG,"navigation_cart    ");
                    Intent ic = new Intent(FAQActivity.this, CartActivity.class);
                    ic.putExtra(fromCart, valueCart);
                    startActivity(ic);
                    finish();
                    //   onBackPressed();
                    return true;
                case R.id.navigation_home:
                    Log.e(TAG,"navigation_home    ");
                    Intent iha = new Intent(FAQActivity.this, HomeActivity.class);
                    iha.putExtra(fromFavorite, valueFavorite);
                    startActivity(iha);
                    return true;
                case R.id.navigation_favorite:
                    Log.e(TAG,"navigation_favorite    ");
                    Intent ifa = new Intent(FAQActivity.this, FavouriteActivity.class);
                    ifa.putExtra(fromFavorite, valueFavorite);
                    startActivity(ifa);
                    finish();
                    return true;

                case R.id.navigation_quit:
                    Log.e(TAG,"navigation_quit    ");
                    //quitapp();
                    showBackPopup();
                    return true;
            }

            //navigation.setSelectedItemId(R.id.navigation_home);
            return false;
        }



    };


    private void showBackPopup() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(FAQActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) FAQActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.exit_back_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);

            TextView tv_popupdelete = (TextView) layout.findViewById(R.id.tv_popupdelete);
            TextView tv_popupdeleteinfo3 = (TextView) layout.findViewById(R.id.tv_popupdeleteinfo3);
            TextView edit = (TextView) layout.findViewById(R.id.edit);
            TextView tvno = (TextView) layout.findViewById(R.id.tvno);



            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();
            SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF184, 0);
            tv_popupdelete.setText(pref4.getString("Confirmexit", null));

            SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF185, 0);
            tv_popupdeleteinfo3.setText(pref5.getString("areyousureyouwanttoexit", null));

            SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF186, 0);
            edit.setText(pref6.getString("yes", null));

            SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF187, 0);
            tvno.setText(pref7.getString("no", null));


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    finishAffinity();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
