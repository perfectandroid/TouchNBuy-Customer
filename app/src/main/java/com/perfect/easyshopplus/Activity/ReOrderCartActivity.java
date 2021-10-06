package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.Adapter.ReOrderCartAdapter;
import com.perfect.easyshopplus.Adapter.ReOrderItemListAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.ReorderCartModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ReOrderCartActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener  , CartChangedListener {

    String TAG = "ReOrderCartActivity";
    ProgressDialog progressDialog;
    ArrayList<ReorderCartModel> cartlist = new ArrayList<>();
    TextView tvuser, tvcart, tv_notification, tvtitle, bttoalamount, bttoalsvdamount, btcheckout;
    EditText etSearch;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    DBHandler db;
    String item, OK, ID_SalesOrder,MinimumDeliveryAmount, ID_Store, ShopType, ID_CustomerAddress, OrderType, DeliveryCharge,order_id,Id_order,orderDate,deliveryDate,status,itemcount,storeName,OrderNo;
    RelativeLayout rl_notification;
    FloatingActionButton fab;
    RecyclerView rv_reorder;
    float subtotal, totalgst, aamountPay, totalMRP, totalRetailPrice, discount, memberdiscount, yousaved;
    public static CartChangedListener cartChangedListener = null;
    double doubleTotalAmt;
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
        setContentView(R.layout.activity_reorder_main);
        Intent in = getIntent();
//        ID_Store = in.getStringExtra("ID_Store");
//        ShopType = in.getStringExtra("ShopType");
//        ID_SalesOrder = in.getStringExtra("ID_SalesOrder");
//        DeliveryCharge = in.getStringExtra("DeliveryCharge");
//
//        ID_CustomerAddress = in.getStringExtra("ID_CustomerAddress");
//        OrderType = in.getStringExtra("OrderType");

        Log.e(TAG,"Start   132" );

        ID_SalesOrder = in.getStringExtra("ID_SalesOrder");
        order_id = in.getStringExtra("order_id");
        Id_order = in.getStringExtra("Id_order");
        OrderNo = in.getStringExtra("OrderNo");
        orderDate = in.getStringExtra("orderDate");
        deliveryDate = in.getStringExtra("deliveryDate");
        status = in.getStringExtra("status");
        ID_Store = in.getStringExtra("ID_Store");
        ShopType = in.getStringExtra("ShopType");
        itemcount = in.getStringExtra("itemcount");
        ID_CustomerAddress = in.getStringExtra("ID_CustomerAddress");
        OrderType = in.getStringExtra("OrderType");
        storeName = in.getStringExtra("storeName");
        DeliveryCharge = in.getStringExtra("DeliveryCharge");
        MinimumDeliveryAmount = in.getStringExtra("MinimumDeliveryAmount");

        initiateViews();
        setRegViews();
        setHomeNavMenu1();

        /*SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
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
        tvcart.setText(String.valueOf(db.selectCartCount()+db.selectInshopCartCount()));
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));
        setView();

        setBottomBar();

        SharedPreferences Reorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF168, 0);
        tvtitle.setText(Reorder.getString("Reorder", ""));

        SharedPreferences proceed = getSharedPreferences(Config.SHARED_PREF132, 0);
        btcheckout.setText(proceed.getString("proceed",""));

        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        OK = (OKsp.getString("OK", ""));

    }

    private void initiateViews() {
        etSearch = findViewById(R.id.etSearch);
        imcart = findViewById(R.id.imcart);
        drawer = findViewById(R.id.drawer_layout);
        im = findViewById(R.id.im);
        lvNavMenu = findViewById(R.id.lvNavMenu);
        tvuser = findViewById(R.id.tvuser);
        tvcart = findViewById(R.id.tvcart);
        rl_notification= findViewById(R.id.rl_notification);
        tv_notification = findViewById(R.id.tv_notification);
        im_notification = findViewById(R.id.im_notification);
        rv_reorder = findViewById(R.id.rv_reorder);
        fab = findViewById(R.id.fab);
        tvtitle = findViewById(R.id.tvtitle);
        bttoalamount  = findViewById(R.id.bttoalamount);
        bttoalsvdamount = findViewById(R.id.bttoalsvdamount);
        btcheckout = findViewById(R.id.btcheckout);
    }

    private void setRegViews() {
        im.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        imcart.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        btcheckout.setOnClickListener(this);
        fab.setOnClickListener(this);
        cartChangedListener = this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im:
                //  drawer.openDrawer(Gravity.START);
                onBackPressed();
                break;
            case R.id.etSearch:
                startActivity(new Intent(ReOrderCartActivity.this,SearchActivity.class));
                break;
            case R.id.imcart:
                Intent intnt = new Intent(ReOrderCartActivity.this, CartActivity.class);
                intnt.putExtra("From", "Home");
                startActivity(intnt);
                break;
            case R.id.tvcart:
                Intent in = new Intent(ReOrderCartActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.tv_notification:
                startActivity(new Intent(ReOrderCartActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(ReOrderCartActivity.this, NotificationActivity.class));
                break;
            case R.id.btcheckout:
                if (db.checkReOrderCartCount() == true){
                   // Toast.makeText(getApplicationContext(),"Please update all item quantity!",Toast.LENGTH_LONG).show();
                    SharedPreferences Pleaseupdateallitemquantity = getSharedPreferences(Config.SHARED_PREF256, 0);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(Pleaseupdateallitemquantity.getString("Pleaseupdateallitemquantity","")+" !")
                            .setCancelable(false)
                            .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else{
                    getprescription();
                }
                /*if(OrderType.equals("true")) {
                    Intent intn = new Intent(this, CheckoutReorderHomeDeliveryActivity.class);
                    intn.putExtra("ID_Store", ID_Store);
                    intn.putExtra("ShopType", ShopType);
                    intn.putExtra("OrderType", "1");
                    intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                    startActivity(intn);
                    finish();
                }else{
                    Intent intn = new Intent(this, CheckoutReorderActivity.class);
                    intn.putExtra("ID_Store", ID_Store);
                    intn.putExtra("ShopType", ShopType);
                    intn.putExtra("OrderType", "0");
                    intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                    startActivity(intn);
                    finish();
                }*/
                break;
            case R.id.fab:
                Intent i = new Intent(this, ReOrderCategoryActivity.class);
                    i.putExtra("ID_SalesOrder", ID_SalesOrder);
                    i.putExtra("order_id", order_id);
                    i.putExtra("deliveryDate", deliveryDate);
                    i.putExtra("Id_order", Id_order);
                    i.putExtra("OrderNo", OrderNo);
                    i.putExtra("orderDate", orderDate);
                    i.putExtra("status", status);
                    i.putExtra("ID_Store", ID_Store);
                    i.putExtra("ShopType", ShopType);
                    i.putExtra("itemcount", itemcount);
                    i.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                    i.putExtra("OrderType", OrderType);
                    i.putExtra("storeName", storeName);
                    i.putExtra("DeliveryCharge", DeliveryCharge);
                    i.putExtra("MinimumDeliveryAmount", MinimumDeliveryAmount);
//                i.putExtra("ID_Store", ID_Store);
//                i.putExtra("ShopType", ShopType);
//                i.putExtra("OrderType", OrderType);
//                i.putExtra("ID_CustomerAddress", ID_CustomerAddress);
//                i.putExtra("DeliveryCharge", DeliveryCharge);
                startActivity(i);
                finish();
                break;
        }
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    public void onBackPressed() {
        Intent in=new Intent(this, OrderDetailsActivity.class);
            in.putExtra("order_id", order_id);
            in.putExtra("deliveryDate", deliveryDate);
            in.putExtra("Id_order", Id_order);
            in.putExtra("OrderNo", OrderNo);
            in.putExtra("orderDate", orderDate);
            in.putExtra("status", status);
            in.putExtra("ID_Store", ID_Store);
            in.putExtra("ShopType", ShopType);
            in.putExtra("itemcount", itemcount);
            in.putExtra("ID_CustomerAddress", ID_CustomerAddress);
            in.putExtra("OrderType", OrderType);
            in.putExtra("storeName", storeName);
            in.putExtra("DeliveryCharge", DeliveryCharge);
            in.putExtra("MinimumDeliveryAmount", MinimumDeliveryAmount);
        startActivity(in);
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setHomeNavMenu() {
        final String[] menulist = new String[]{"Home","My Cart", "My Orders", "Favourites","Favourite Stores",
                "Notifications", "Shopping List","My Profile", "About Us","Logout","Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,R.drawable.ic_store,
                R.drawable.ic_notifications, R.drawable.ic_list,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ReOrderCartActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ReOrderCartActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ReOrderCartActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ReOrderCartActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ReOrderCartActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(ReOrderCartActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(ReOrderCartActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(ReOrderCartActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 7) {
                    startActivity(new Intent(ReOrderCartActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 8) {
                    startActivity(new Intent(ReOrderCartActivity.this, AboutUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 9) {
                    doLogout();
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 10) {
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

    private void setHomeNavMenu1() {
        final String[] menulist = new String[]{"Home","My Cart", "Track Orders", "Favourite Items",
                "Notifications", "My Profile", "About Us","Logout","Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ReOrderCartActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ReOrderCartActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ReOrderCartActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ReOrderCartActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ReOrderCartActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 4) {
                    startActivity(new Intent(ReOrderCartActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 5) {
                    startActivity(new Intent(ReOrderCartActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 6) {
                    startActivity(new Intent(ReOrderCartActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(ReOrderCartActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 10) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ReOrderCartActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) ReOrderCartActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                else if (position == 11) {
                    startActivity(new Intent(ReOrderCartActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(ReOrderCartActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(ReOrderCartActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 14) {
                    startActivity(new Intent(ReOrderCartActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReOrderCartActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ReOrderCartActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(ReOrderCartActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(ReOrderCartActivity.this,LocationActivity.class));
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

    public void setView(){
        db = new DBHandler(this);
        cartlist = new ArrayList<>(db.getAllReOrderCart());
        try {
            Gson gson = new Gson();
            String listString = gson.toJson(cartlist,new TypeToken<ArrayList<ReorderCartModel>>() {}.getType());
            JSONArray jarray =  new JSONArray(listString);
            GridLayoutManager lLayout = new GridLayoutManager(this, 1);
            rv_reorder.setLayoutManager(lLayout);
            rv_reorder.setHasFixedSize(true);

//            ReOrderCartAdapter adapter = new ReOrderCartAdapter(this, jarray,
//                    ID_Store, ShopType,
//                    ID_SalesOrder, ID_CustomerAddress,OrderType,DeliveryCharge);

            Log.e(TAG,"jarray   549    "+jarray);

            ReOrderCartAdapter adapter = new ReOrderCartAdapter(this, jarray, ID_SalesOrder, order_id, deliveryDate, Id_order, orderDate, status, ID_Store, ShopType,itemcount,ID_CustomerAddress,OrderType, storeName,DeliveryCharge,MinimumDeliveryAmount,OrderNo);

            rv_reorder.setAdapter(adapter);
        }
        catch (Exception e){
        }
    }

    @Override
    public void onCartChanged() {
        db = new DBHandler(this);
        String string = "\u20B9";
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {e.printStackTrace();}

        DecimalFormat f = new DecimalFormat("##.00");
        subtotal=db.selectReorderTotalActualPrice();
        totalgst=db.ReorderTotalGST();
        totalMRP=db.ReorderTotalMRP();
        totalRetailPrice=db.ReorderTotalRetailPrice();
        discount=totalMRP-totalRetailPrice;
        memberdiscount=   totalRetailPrice-subtotal;
        aamountPay=subtotal;
        yousaved=discount+memberdiscount;
        doubleTotalAmt=Double.parseDouble(String.valueOf(subtotal));
//        bttoalamount.setText("Total Amount : "+string+" "+f.format(Double.parseDouble(String.valueOf(subtotal))));
//        bttoalsvdamount.setText("( You have saved "+string+" "+f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");



        SharedPreferences totalamount = getSharedPreferences(Config.SHARED_PREF131, 0);
        bttoalamount.setText(totalamount.getString("totalamount", "")+" : "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        SharedPreferences youhavesaved = getSharedPreferences(Config.SHARED_PREF212, 0);
        bttoalsvdamount.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");

    }

    private void getprescription() {
        Log.e(TAG,"doubleTotalAmt  717   "+doubleTotalAmt);
        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(this).isInternetOn()) {
            progressDialog = new ProgressDialog(this, R.style.Progress);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(this.getResources()
                    .getDrawable(R.drawable.progress));
            progressDialog.show();
            try{
                OkHttpClient client = new OkHttpClient.Builder()
                        .sslSocketFactory(getSSLSocketFactory())
                        .hostnameVerifier(getHostnameVerifier())
                        .build();
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASEURL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(client)
                        .build();
                ApiInterface apiService = retrofit.create(ApiInterface.class);
                final JSONObject requestObject1 = new JSONObject();
                try {
                    requestObject1.put("ReqMode", "22");
                    JSONArray jsonArray = new JSONArray();
                    DBHandler db=new DBHandler(this);
                    Cursor cursor = db.select("reordercart");
                    int i = 0;
                    if (cursor.moveToFirst()) {
                        do {
                            JSONObject jsonObject1 = new JSONObject();
                            try {
                                jsonObject1.put("ID_Item", cursor.getString(cursor.getColumnIndex("ID_Items")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                jsonArray.put(i, jsonObject1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            i++;
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    requestObject1.put("Data", jsonArray);
                    requestObject1.put("Bank_Key","-500");
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getPrescription(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jObject = new JSONObject(response.body());
                            Log.e(TAG,"Response  646    "+response.body());

                            if(jObject.getString("StatusCode").equals("0")) {
                                JSONObject jobj = jObject.getJSONObject("PrescriptionList");
                                final String checkcase = jobj.getString("CheckCase");
                                if (checkcase.equals("true")) {
                                    //  startActivity(new Intent(getContext(), PrescriptionUploadActivity.class));
                                    Intent i = new Intent(ReOrderCartActivity.this,ReorderPrescriptionUploadActivity.class);

//                                    i.putExtra("ID_Store", ID_Store);
//                                    i.putExtra("ShopType", ShopType);
//                                    i.putExtra("OrderType", OrderType);
//                                    i.putExtra("ID_CustomerAddress", ID_CustomerAddress);.

                                    Log.e(TAG,"doubleTotalAmt  717   "+doubleTotalAmt);
                                    i.putExtra("doubleTotalAmt", String.valueOf(doubleTotalAmt));



                                    i.putExtra("order_id", order_id);
                                    i.putExtra("deliveryDate", deliveryDate);
                                    i.putExtra("Id_order", Id_order);
                                    i.putExtra("OrderNo", OrderNo);
                                    i.putExtra("orderDate", orderDate);
                                    i.putExtra("status", status);
                                    i.putExtra("ID_Store", ID_Store);
                                    i.putExtra("ShopType", ShopType);
                                    i.putExtra("itemcount", itemcount);
                                    i.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                                    i.putExtra("OrderType", OrderType);
                                    i.putExtra("storeName", storeName);
                                    i.putExtra("DeliveryCharge", DeliveryCharge);
                                    i.putExtra("MinimumDeliveryAmount", MinimumDeliveryAmount);
                                    startActivity(i);
                                } else {
                                   /* if(OrderType.equals("true")) {
                                        Intent intn = new Intent(ReOrderCartActivity.this, CheckoutReorderHomeDeliveryActivity.class);
                                        intn.putExtra("ID_Store", ID_Store);
                                        intn.putExtra("ShopType", ShopType);
                                        intn.putExtra("OrderType", "1");
                                        intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                                        intn.putExtra("DeliveryCharge", DeliveryCharge);
                                        startActivity(intn);
                                        finish();
                                    }
                                    else{
                                        Intent intn = new Intent(ReOrderCartActivity.this, CheckoutReorderActivity.class);
                                        intn.putExtra("ID_Store", ID_Store);
                                        intn.putExtra("ShopType", ShopType);
                                        intn.putExtra("OrderType", "0");
                                        intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                                        startActivity(intn);
                                        finish();
                                    }*/
                                    if(OrderType.equals("true")) {
                                        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                                        String strMinimumDeliveryAmount = pref3.getString("MinimumDeliveryAmount", "");

//                                        if (Double.parseDouble(strMinimumDeliveryAmount) > doubleTotalAmt) {
                                        if (Double.parseDouble(MinimumDeliveryAmount) > doubleTotalAmt) {
                                            SharedPreferences MinimumamountHomedelivery = getSharedPreferences(Config.SHARED_PREF254, 0);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ReOrderCartActivity.this);
                                            builder.setMessage(MinimumamountHomedelivery.getString("MinimumamountHomedelivery","")+" "+strMinimumDeliveryAmount)
                                                    .setCancelable(false)
                                                    .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }
                                        else {
                                            Log.e(TAG,"CheckoutReorderHomeDeliveryActivity   711");
                                            Intent intn = new Intent(ReOrderCartActivity.this, CheckoutReorderHomeDeliveryActivity.class);
//                                            intn.putExtra("ID_Store", ID_Store);
//                                            intn.putExtra("ShopType", ShopType);
//                                            intn.putExtra("OrderType", "1");
//                                            intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);
//                                            intn.putExtra("DeliveryCharge", DeliveryCharge);
                                                intn.putExtra("order_id", order_id);
                                                intn.putExtra("deliveryDate", deliveryDate);
                                                intn.putExtra("Id_order", Id_order);
                                                intn.putExtra("OrderNo", OrderNo);
                                                intn.putExtra("orderDate", orderDate);
                                                intn.putExtra("status", status);
                                                intn.putExtra("ID_Store", ID_Store);
                                                intn.putExtra("ShopType", ShopType);
                                                intn.putExtra("itemcount", itemcount);
                                                intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                                                intn.putExtra("OrderType", OrderType);
                                                intn.putExtra("storeName", storeName);
                                                intn.putExtra("DeliveryCharge", DeliveryCharge);
                                                intn.putExtra("MinimumDeliveryAmount", MinimumDeliveryAmount);
                                            startActivity(intn);
                                            finish();
                                        }
                                    }
                                    else{

                                        Log.e(TAG,"CheckoutReorderActivity   738");
                                        Intent intn = new Intent(ReOrderCartActivity.this, CheckoutReorderActivity.class);
                                            intn.putExtra("order_id", order_id);
                                            intn.putExtra("deliveryDate", deliveryDate);
                                            intn.putExtra("Id_order", Id_order);
                                            intn.putExtra("OrderNo", OrderNo);
                                            intn.putExtra("OrderNo", OrderNo);
                                            intn.putExtra("orderDate", orderDate);
                                            intn.putExtra("status", status);
                                            intn.putExtra("ID_Store", ID_Store);
                                            intn.putExtra("ShopType", ShopType);
                                            intn.putExtra("itemcount", itemcount);
                                            intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                                            intn.putExtra("OrderType", OrderType);
                                            intn.putExtra("storeName", storeName);
                                            intn.putExtra("DeliveryCharge", DeliveryCharge);
                                            intn.putExtra("MinimumDeliveryAmount", MinimumDeliveryAmount);
                                        startActivity(intn);
                                        finish();
                                    }

                                }
                            }else{
                                SharedPreferences Somethingwentwrong = getSharedPreferences(Config.SHARED_PREF72, 0);
                                AlertDialog.Builder builder = new AlertDialog.Builder(ReOrderCartActivity.this);
                                builder.setMessage(Somethingwentwrong.getString("Somethingwentwrong",""))

                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progressDialog.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            //  Intent in = new Intent(this, NoInternetActivity.class);
            //  startActivity(in);
        }
    }

    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkClientTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkClientTrusted", e.toString());
                        }
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkServerTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkServerTrusted", e.toString());
                        }
                    }
                }
        };
    }

    private SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException,
            NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = getResources().openRawResource(R.raw.my_cert); // File path: app\src\main\res\raw\your_cert.cer
        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();
        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);
        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);
        return sslContext.getSocketFactory();
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
                    Intent io = new Intent(ReOrderCartActivity.this, OutShopActivity.class);
                    io.putExtra(fromCategory, valueCategory);
                    finish();
                    startActivity(io);
                    return true;
//                case R.id.navigation_cart:
//                    Intent ic = new Intent(ReOrderCartActivity.this, CartActivity.class);
//                    ic.putExtra(fromCart, valueCart);
//                    startActivity(ic);
//                    finish();
//                    //   onBackPressed();
//                    return true;
                case R.id.navigation_home:
                    Intent iha = new Intent(ReOrderCartActivity.this, HomeActivity.class);
                    iha.putExtra(fromFavorite, valueFavorite);
                    startActivity(iha);
                    return true;
                case R.id.navigation_favorite:
                    Intent ifa = new Intent(ReOrderCartActivity.this, FavouriteActivity.class);
                    ifa.putExtra(fromFavorite, valueFavorite);
                    startActivity(ifa);
                    finish();
                    return true;

                case R.id.navigation_quit:
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReOrderCartActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ReOrderCartActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
