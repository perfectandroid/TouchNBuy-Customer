package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Fragment.InShopFragment;
import com.perfect.easyshopplus.Fragment.OutShopFragment;
import com.perfect.easyshopplus.Model.CheckCartModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener , CartChangedListener{


    String TAG = "CartActivity";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    // EditText etSearch;
    TextView tvuser, tvcart, tv_notification, tv_continueshopping, tv_mycart;
    ArrayList<CheckCartModel> checkcartlist = new ArrayList<>();
    DBHandler db;
    RelativeLayout rl_notification;
    public static CartChangedListener cartChangedListener = null;
    String StrFrom;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_cart_list_main);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        initiateViews();
        setRegViews();
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        Log.e(TAG,"Start   74   ");

        setHomeNavMenu1();
       /* SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/
        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
        ImageView imApplogo=findViewById(R.id.imApplogo);
        String strImagepath= IMAGEURL + pref3.getString("AppIconImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath).into(imApplogo);

        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        tvuser.setText("Welcome "+pref2.getString("username", null));
        db=new DBHandler(this);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));
        tabSelection();
        Intent in = getIntent();
        if(in.getStringExtra("From").equals("Inshop")){
            viewPager.setCurrentItem(1);
            tvcart.setText(String.valueOf(db.selectInshopCartCount()));
        }if(in.getStringExtra("From").equals("Home")){
            viewPager.setCurrentItem(0);
            tvcart.setText(String.valueOf(db.selectCartCount()));
        }
        StrFrom=in.getStringExtra("From");

        SharedPreferences continueshopping = getApplicationContext().getSharedPreferences(Config.SHARED_PREF124, 0);
        tv_continueshopping.setText(continueshopping.getString("continueshopping", ""));
        SharedPreferences mycart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF123, 0);
        tv_mycart.setText(mycart.getString("mycart", ""));
    }

    public String getMyData() {
        return StrFrom;
    }

    public int getcurrentPosition() {
        return currentPosition;
    }

    public void tabSelection(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                currentPosition=viewPager.getCurrentItem();
                int position = tab.getPosition();
                if(position == 0){
                    tvcart.setText(String.valueOf(db.selectCartCount()));
                }else{
                    tvcart.setText(String.valueOf(db.selectInshopCartCount()));
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if(position == 0){
                    tvcart.setText(String.valueOf(db.selectCartCount()));
                }else{
                    tvcart.setText(String.valueOf(db.selectInshopCartCount())); }
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        CartActivity.ViewPagerAdapter adapter = new CartActivity.ViewPagerAdapter(getSupportFragmentManager());
        SharedPreferences Inshoppref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF43, 0);
        if(Inshoppref.getString("RequiredInshop", null).equals("false")){
            adapter.addFragment(new OutShopFragment(), "Order Online");
            tabLayout.setVisibility(View.GONE);
        }else{


            SharedPreferences storepickup = getApplicationContext().getSharedPreferences(Config.SHARED_PREF134, 0);
            String StrStorepickup = storepickup.getString("storepickup","");
            SharedPreferences doordelivery = getApplicationContext().getSharedPreferences(Config.SHARED_PREF135, 0);
            String StrDoordelivery = doordelivery.getString("doordelivery","");

            adapter.addFragment(new OutShopFragment(), StrDoordelivery);
            adapter.addFragment(new InShopFragment(), StrStorepickup);
        }
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

    private void initiateViews() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        im = (ImageView) findViewById(R.id.im);
        // etSearch=(EditText)findViewById(R.id.etSearch);
        imcart=(ImageView) findViewById(R.id.imcart);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tv_mycart = (TextView) findViewById(R.id.tv_mycart);
        tvcart = (TextView) findViewById(R.id.tvcart);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        tv_continueshopping = (TextView) findViewById(R.id.tv_continueshopping);
        im_notification = (ImageView) findViewById(R.id.im_notification);
    }

    private void setRegViews() {
        im.setOnClickListener(this);
        //  etSearch.setOnClickListener(this);
        imcart.setOnClickListener(this);
        cartChangedListener = this;
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        tv_continueshopping.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im:

                //  drawer.openDrawer(Gravity.START);
                onBackPressed();                break;
            /*case R.id.etSearch:
                startActivity(new Intent(CartActivity.this,SearchActivity.class));
                break;*/
            case R.id.tv_continueshopping:
                SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF36, 0);
                SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF37, 0);
                if(pref6.getString("RequiredStore", null).equals("false")&&
                        pref7.getString("RequiredStoreCategory", null).equals("false")){
                    Intent intent = new Intent(this, OutShopActivity.class);
                    intent.putExtra("from", "home");
                    startActivity(intent);
                    //    finish();
                }else {
                    Intent intent = new Intent(this, OutShopActivity.class);
                    intent.putExtra("from", "cart");
                    startActivity(intent);
                    //   finish();
                }


                break;
            case R.id.tv_notification:
                startActivity(new Intent(CartActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(CartActivity.this, NotificationActivity.class));
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

/*
    private void setHomeNavMenu() {
        final String[] menulist = new String[]{"Home", "My Orders", "Favourites","Favourite Stores",
                "Notifications", "Shopping List","My Profile", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_store, R.drawable.ic_notifications,R.drawable.ic_list, R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout,  R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(CartActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(CartActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    startActivity(new Intent(CartActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(CartActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 3) {
                    startActivity(new Intent(CartActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(CartActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(CartActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(CartActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    startActivity(new Intent(CartActivity.this, AboutUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 8) {
                    doLogout();
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 9) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "\t" + "Invite You  \n\n For Android Users \n http://play.google.com/store/apps/details?id=" + getPackageName() +
                            "\n" );
                    startActivity(Intent.createChooser(share, "Invite this App to your friends"));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }
*/

    private void setHomeNavMenu1() {
        final String[] menulist = new String[]{"Home", "Track Orders", "Favourite Items",
                "Notifications", "My Profile", "About Us", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout,  R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(CartActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(CartActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    startActivity(new Intent(CartActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(CartActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 3) {
                    startActivity(new Intent(CartActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(CartActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(CartActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(CartActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 9) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) CartActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater1.inflate(R.layout.rate_us_pop_up, null);
                        final Button rateUs = layout.findViewById(R.id.rate_us);
                        builder.setView(layout);

                        final AlertDialog alertDialog = builder.create();
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
                    startActivity(new Intent(CartActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 11) {
                    startActivity(new Intent(CartActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(CartActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(CartActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CartActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) CartActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(CartActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(CartActivity.this,LocationActivity.class));
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

    @Override
    public void onCartChanged() {
        Intent in = getIntent();
        if(in.getStringExtra("From").equals("Inshop")){
            viewPager.setCurrentItem(1);
            tvcart.setText(String.valueOf(db.selectInshopCartCount()));
        }if(in.getStringExtra("From").equals("Home")){
            viewPager.setCurrentItem(0);
            tvcart.setText(String.valueOf(db.selectCartCount()));
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
       try {
           SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
           tv_notification.setText(pref.getString("notificationcount", null));
       }catch (Exception e){

       }
    }
}