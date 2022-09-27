package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Adapter.BannerDeatilsAdapter;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.Adapter.StoreAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import me.relex.circleindicator.CircleIndicator;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class StoreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    String TAG = "StoreActivity";
    ProgressDialog progressDialog;
    RecyclerView rv_storeist,rv_recentStoreList;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvuser, tvcart, tv_notification;
    DBHandler db;
    RelativeLayout rl_notification, rl_location, rl_fav;
    static  TextView tv_check;
    static  Context mcontext;
  //  private SwipeRefreshLayout mSwipeData;
    Spinner search_spinner;
    String[] searchType=new String[]{};
    String   strStorename, strAreaName, strpinNumber;
    EditText search;
    int intPosition;
    ImageButton ib_search;
    private static ViewPager mPager;
    private static int currentPage = 0;
    View vSeperator;
    String StoreCategory;
    LinearLayout store_list_title_container;
    TextView tv_header;


    BottomNavigationView navigation;
    String fromCategory = "from";
    String valueCategory = "store";
    String fromCart = "From";
    String valueCart = "Home";
    String fromHome = "";
    String valueHome = "";
    String fromFavorite = "From";
    String valueFavorite = "Home";

    SharedPreferences Nointernetconnection;
    SharedPreferences SomethingwentwrongTryagainlater;
    SharedPreferences Nodatafound;
    SharedPreferences sOK;
    SharedPreferences sCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_main);

        Intent in = getIntent();
        StoreCategory = in.getStringExtra("StoreCategory");
        initiateViews();
        setRegViews();
        setHomeNavMenu1();
        setBottomBar();
        /*SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/
        Log.e(TAG,"Start   131  ");
        getCategories();
        Nointernetconnection = getApplicationContext().getSharedPreferences(Config.SHARED_PREF282, 0);
        SomethingwentwrongTryagainlater = getApplicationContext().getSharedPreferences(Config.SHARED_PREF280, 0);
        Nodatafound = getApplicationContext().getSharedPreferences(Config.SHARED_PREF281, 0);
        sOK = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        sCancel = getApplicationContext().getSharedPreferences(Config.SHARED_PREF105, 0);
        SharedPreferences Store = getApplicationContext().getSharedPreferences(Config.SHARED_PREF119, 0);
        tv_header.setText(""+Store.getString("Store",""));

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
        ImageView imApplogo=findViewById(R.id.imApplogo);
        String strImagepath= IMAGEURL + pref3.getString("AppIconImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath).into(imApplogo);

        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tvuser.setText("Welcome "+pref2.getString("username", null));
        db=new DBHandler(this);
        tvcart.setText(String.valueOf(db.selectCartCount()));
        tv_notification.setText(pref.getString("notificationcount", null));
      /*  mSwipeData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCategories();
                mSwipeData.setRefreshing(false);
            }
        });*/



        detailsShowing();
        getBannerImage();
    }


    private void setBottomBar() {
        navigation= (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().setGroupCheckable(0, false, false);
    }


    private void initiateViews() {

        tv_header = (TextView)findViewById(R.id.tv_header) ;
        rv_storeist=(RecyclerView)findViewById(R.id.rv_storeist);
        rv_recentStoreList=(RecyclerView)findViewById(R.id.rv_recentStoreList);
        vSeperator=findViewById(R.id.vSeperator);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        im = (ImageView) findViewById(R.id.im);
        imcart = (ImageView) findViewById(R.id.imcart);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        store_list_title_container=(LinearLayout) findViewById(R.id.store_list_title_container);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        tv_check = (TextView) findViewById(R.id.select_store_tv);
       // mSwipeData = findViewById(R.id.swipeData);
        search_spinner = findViewById(R.id.search_spinner);
        search = findViewById(R.id.etSearch);
        ib_search = findViewById(R.id.ib_search);
        rl_location = findViewById(R.id.rl_location);
        rl_fav = findViewById(R.id.rl_fav);
    }

    private void setRegViews() {
        im.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        imcart.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        ib_search.setOnClickListener(this);
        rl_location.setOnClickListener(this);
        rl_fav.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im:

                //  drawer.openDrawer(Gravity.START);
                onBackPressed();                break;
            case R.id.tvcart:
                Intent i = new Intent(StoreActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.imcart:
                Intent in = new Intent(StoreActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.rl_fav:
                startActivity(new Intent(StoreActivity.this, FavouriteStoreActivity.class));
                break;
            case R.id.tv_notification:
                startActivity(new Intent(StoreActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(StoreActivity.this, NotificationActivity.class));
                break;
            case R.id.rl_location:
                startActivity(new Intent(StoreActivity.this, MapsActivity.class));
                break;
            case R.id.ib_search:
                hideKeyboard();
                if(intPosition==0){
                    if(search.getText().toString().length()<=0){
                        SharedPreferences EnterStore = getApplicationContext().getSharedPreferences(Config.SHARED_PREF248, 0);
                            search.setError(""+EnterStore.getString("EnterStore",""));
                    }else {
                        strStorename = search.getText().toString();
                        getStoreList(strStorename, strAreaName, strpinNumber);
                    }
                }
                if(intPosition==1){
                    if(search.getText().toString().length()<=0){
                        SharedPreferences EnterArea = getApplicationContext().getSharedPreferences(Config.SHARED_PREF249, 0);
//                            search.setError("Enter Area");
                        search.setError(""+EnterArea.getString("EnterArea",""));

                    }else {
                        strAreaName = search.getText().toString();
                        getStoreList(strStorename, strAreaName, strpinNumber);
                    }
                }
                if(intPosition==2){
                    if(search.getText().toString().length()<=0){
//                            search.setError("Enter Pincode");
                        SharedPreferences EnterPincode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF250, 0);
                        search.setError(""+EnterPincode.getString("EnterPincode",""));

                    }else {
                        strpinNumber = search.getText().toString();
                        getStoreList(strStorename, strAreaName, strpinNumber);
                    }
                }
                break;
        }
    }

    public void getStoreList(String storename, String areaname, String pin){
        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(this).isInternetOn()) {
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
                    requestObject1.put("ReqMode", "8");
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    requestObject1.put("StoreName",storename);
                    requestObject1.put("AreaName",areaname);
                    requestObject1.put("PinCode", pin);
                    requestObject1.put("StoreCategory", StoreCategory);
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1   336   "+requestObject1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getStores(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            Log.e(TAG,"onResponse    345   "+response.body());
//                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("StoreBindListInfo");
                          /*  if(jarray.length()==1){
                                JSONObject jobjt = jarray.getJSONObject(0);
                                String strID_Store = jobjt.getString("ID_Store");
                                String strStoreName = jobjt.getString("StoreName");
                                SharedPreferences ID_Store = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                                SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                ID_Storeeditor.putString("ID_Store", strID_Store);
                                ID_Storeeditor.commit();
                                SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                StoreNameeditor.putString("StoreName", strStoreName);
                                StoreNameeditor.commit();
                                Intent intent = new Intent(StoreActivity.this, OutShopActivity.class);
                                startActivity(intent);
                            }else {
                                GridLayoutManager lLayout = new GridLayoutManager(StoreActivity.this, 1);
                                rv_storeist.setLayoutManager(lLayout);
                                rv_storeist.setHasFixedSize(true);
                                StoreAdapter adapter = new StoreAdapter(StoreActivity.this, jarray);
                                rv_storeist.setAdapter(adapter);
                            }*/
                            if (jobj.getString("storeListInfos").equals("null")) {
                                rv_storeist.setVisibility(View.GONE);
                                AlertDialog.Builder builder= new AlertDialog.Builder(StoreActivity.this);
                                builder.setMessage(""+Nodatafound.getString("Nodatafound",null))
                                        .setCancelable(false)
                                        .setPositiveButton(""+sOK.getString("OK",null), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //clearall();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }else{
                                rv_storeist.setVisibility(View.VISIBLE);
                                rv_recentStoreList.setVisibility(View.GONE);
                                vSeperator.setVisibility(View.GONE);
                                JSONArray jarray = jobj.getJSONArray("storeListInfos");
                                GridLayoutManager lLayout = new GridLayoutManager(StoreActivity.this, 1);
                            rv_storeist.setLayoutManager(lLayout);
                            rv_storeist.setHasFixedSize(true);
                            StoreAdapter adapter = new StoreAdapter(StoreActivity.this, jarray);
                            rv_storeist.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {}
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this,""+Nointernetconnection.getString("Nointernetconnection",null),Toast.LENGTH_SHORT).show();
        }
    }

    public void hideKeyboard(){

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
    }

    private void getCategories() {

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(this).isInternetOn()) {
        try {
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
                    requestObject1.put("ReqMode", "5");
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    requestObject1.put("StoreCategory", StoreCategory);
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"BASEURL   4490    "+BASEURL);
                    Log.e(TAG,"requestObject1   4491    "+requestObject1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getStoresList(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG,"onResponse   4492  "+response.body());
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("StoreListDetailsInfo");

                            if (jObject.getString("StatusCode").equals("0")) {
                                if (jobj.getString("OtherStoreListInfo").equals("null")) {
                                    rv_storeist.setVisibility(View.GONE);
                                    AlertDialog.Builder builder= new AlertDialog.Builder(StoreActivity.this);
                                    builder.setMessage(""+Nodatafound.getString("Nodatafound",null))
                                            .setCancelable(false)
                                            .setPositiveButton(""+sOK.getString("OK",null), new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    //clearall();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                                else{

                                    rv_storeist.setVisibility(View.VISIBLE);
                                    JSONArray jarray = jobj.getJSONArray("OtherStoreListInfo");
                                    Log.e(TAG,"jarray  479 ist  "+jarray.length());
                                    GridLayoutManager lLayout = new GridLayoutManager(StoreActivity.this, 1);
                                    rv_storeist.setLayoutManager(lLayout);
                                    rv_storeist.setHasFixedSize(true);
                                    StoreAdapter adapter = new StoreAdapter(StoreActivity.this, jarray);
                                    rv_storeist.setAdapter(adapter);
                                }

                                if (jobj.getString("SearchedStoreListInfo").equals("null")){
                                    rv_recentStoreList.setVisibility(View.GONE);
                                    vSeperator.setVisibility(View.GONE);
                                    Log.e(TAG,"jarray  479 2nd  ");
                                }
                                else {
                                    rv_recentStoreList.setVisibility(View.VISIBLE);
                                    vSeperator.setVisibility(View.GONE);
                                    JSONArray jarray = jobj.getJSONArray("SearchedStoreListInfo");
                                    Log.e(TAG,"jarray  479 3rd  "+jarray.length());
                                    GridLayoutManager lLayout = new GridLayoutManager(StoreActivity.this, 1);
                                    rv_recentStoreList.setLayoutManager(lLayout);
                                    rv_recentStoreList.setHasFixedSize(true);
                                    StoreAdapter adapter = new StoreAdapter(StoreActivity.this, jarray);
                                    rv_recentStoreList.setAdapter(adapter);
                                }
                            }
                            else if (jObject.getString("StatusCode").equals("2")) {
                                rv_storeist.setVisibility(View.GONE);
                                AlertDialog.Builder builder= new AlertDialog.Builder(StoreActivity.this);
                                builder.setMessage(""+Nodatafound.getString("Nodatafound",null))
                                        .setCancelable(false)
                                        .setPositiveButton(""+sOK.getString("OK",null), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else  {
                                rv_storeist.setVisibility(View.GONE);
                                AlertDialog.Builder builder= new AlertDialog.Builder(StoreActivity.this);
                                builder.setMessage(""+SomethingwentwrongTryagainlater.getString("SomethingwentwrongTryagainlater",null))
                                        .setCancelable(false)
                                        .setPositiveButton(""+sOK.getString("OK",null), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //clearall();
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
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),""+SomethingwentwrongTryagainlater.getString("SomethingwentwrongTryagainlater",null),Toast.LENGTH_LONG).show();
        }
    }else {
        Intent in = new Intent(this,NoInternetActivity.class);
        startActivity(in);
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

    private void setHomeNavMenu() {
        final String[] menulist = new String[]{"Home","My Cart", "My Orders", "Favourites","Favourite Stores",
                "Notifications", "Shopping List", "My Profile", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,R.drawable.ic_store,
                R.drawable.ic_notifications, R.drawable.ic_list,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(StoreActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(StoreActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(StoreActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(StoreActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(StoreActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(StoreActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position ==5) {
                    startActivity(new Intent(StoreActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(StoreActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    startActivity(new Intent(StoreActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 8) {
                    startActivity(new Intent(StoreActivity.this, AboutUsActivity.class));
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
                "Notifications",  "My Profile", "About Us", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications, R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(StoreActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(StoreActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(StoreActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(StoreActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(StoreActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position ==4) {
                    startActivity(new Intent(StoreActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(StoreActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(StoreActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(StoreActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 10) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StoreActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) StoreActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivity(new Intent(StoreActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(StoreActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(StoreActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 14) {
                    startActivity(new Intent(StoreActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StoreActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) StoreActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(StoreActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(StoreActivity.this,LocationActivity.class));
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

    public void detailsShowing(){
      //  searchType = getResources().getStringArray(R.array.array_search);

        SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF245, 0);
        SharedPreferences AreaName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF246, 0);
        SharedPreferences Pincode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF247, 0);

        searchType =  new String[]{StoreName.getString("Store_Name", ""),AreaName.getString("AreaName", ""),Pincode.getString("Pincode", "")};
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,searchType);
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        search_spinner.setAdapter(aa);
        search_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int position, long arg3) {
                // TODO Auto-generated method stub
                strStorename="";
                strAreaName="";
                strpinNumber="";
                if(position==0){
                    search.setText("");
                    search.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(20)});
                    search.setInputType(InputType.TYPE_CLASS_TEXT);
                    intPosition=position;
                }
                if(position==1){
                    search.setText("");
                    search.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(20)});
                    search.setInputType(InputType.TYPE_CLASS_TEXT);
                    intPosition=position;
                }
                if(position==2){
                    search.setText("");
                    search.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(6)});
                    search.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                    search.setTransformationMethod(null);
                    intPosition=position;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


    }

    InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; ++i)
            {
                if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*").matcher(String.valueOf(source.charAt(i))).matches())
                {return "";}}
            return null;
        }
    };

    private void getBannerImage() {

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        final String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(this).isInternetOn()) {
            try {
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
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                        String idStore = pref.getString("ID_Store","");
                        requestObject1.put("ReqMode", "19");
                    //    requestObject1.put("FK_Store",/*idStore*/"1");
                        requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                        SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                        requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                        Log.e(TAG,"requestObject1  9981   "+requestObject1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                    Call<String> call = apiService.getBanner(body);
                    call.enqueue(new Callback<String>() {
                        @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            try {
                                Log.e(TAG,"onResponse  9982   "+response.body());
                                JSONObject jObject = new JSONObject(response.body());
                                if(jObject.getString("StatusCode").equals("0")) {
                                    store_list_title_container.setVisibility(View.VISIBLE);
                                JSONObject jobj = jObject.getJSONObject("BannerImageDetails");
                                final JSONArray jarray = jobj.getJSONArray("BannerImageDetailsInfo");
                                ArrayList<String> ImgDetails = new ArrayList<>();
                                int size = jarray.length();
                                if (size <= 0) {
                                    store_list_title_container.setVisibility(View.GONE);
                                } else {

                                    store_list_title_container.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject object = jarray.getJSONObject(i);
                                        ImgDetails.add(IMAGEURL + object.getString("ImagePath"));
                                    }
                                    String[] strArray = ImgDetails.toArray(new String[ImgDetails.size()]);
                                    getInitView(strArray, size);
                                }
                            }else{
                                    store_list_title_container.setVisibility(View.GONE);
                            }
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),""+SomethingwentwrongTryagainlater.getString("SomethingwentwrongTryagainlater",null),Toast.LENGTH_LONG).show();
            }
        }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }

    }

    private void getInitView(String [] imageDetails, final int size) {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new BannerDeatilsAdapter(StoreActivity.this,imageDetails));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == size) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 4000);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected( MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_category:
                    Intent io = new Intent(StoreActivity.this, OutShopActivity.class);
                    io.putExtra(fromCategory, valueCategory);
                    finish();
                    startActivity(io);
                    return true;
                case R.id.navigation_cart:
                    Log.e(TAG,"navigation_cart    ");
                    Intent ic = new Intent(StoreActivity.this, CartActivity.class);
                    ic.putExtra(fromCart, valueCart);
                    startActivity(ic);
                    return true;
                case R.id.navigation_home:
                    Log.e(TAG,"navigation_home    ");
                    Intent ih = new Intent(StoreActivity.this, HomeActivity.class);
                    startActivity(ih);
                    return true;
                case R.id.navigation_favorite:
                    Log.e(TAG,"navigation_favorite    ");
                    Intent ifa = new Intent(StoreActivity.this, FavouriteActivity.class);
                    ifa.putExtra(fromFavorite, valueFavorite);
                    startActivity(ifa);
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(StoreActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) StoreActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
