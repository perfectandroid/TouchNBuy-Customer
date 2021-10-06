package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.Adapter.OrderListAdapter;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

public class OrdersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,AdapterView.OnItemSelectedListener {

    ProgressDialog progressDialog;
    RecyclerView rv_orderlist;
    EditText etSearch;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvuser, tvcart, tv_notification, tv_header,tvnoorderlist;
    DBHandler db;
    RelativeLayout rl_notification;
    LinearLayout emptyOrder,llsearch;
    EditText etFromdate,etTodate;
    private int mYear, mMonth, mDay;
    Button btnSearch;
  //  private SwipeRefreshLayout mSwipeData;
    Spinner status_spinner;
    String[] status;
    String reqSubMode, Nodatafound,ok;
    SharedPreferences userid;

    String TAG = "OrdersActivity";
    BottomNavigationView navigation;
    String fromCategory = "from";
    String valueCategory = "orders";
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
        setContentView(R.layout.activity_orders_main);
        initiateViews();
        setRegViews();
        setHomeNavMenu1();
        setBottomBar();

        Log.e(TAG,"Start   129   ");
       /* SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/
        getOrderList();

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF38, 0);
        ImageView imLogo=findViewById(R.id.imLogo);
        String strImagepath1= IMAGEURL + pref4.getString("LogoImageCode", null);
       // PicassoTrustAll.getInstance(this).load(strImagepath1).into(imLogo);
        Glide.with( this ).load( R.drawable.ordernowgif ).into( imLogo );

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
     /*   mSwipeData.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getOrderList();
                mSwipeData.setRefreshing(false);
            }
        });*/


        SharedPreferences allsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF148, 0);
        String all = allsp.getString("all", null);

        SharedPreferences pendingsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF149, 0);
        String pending = pendingsp.getString("pending", null);

        SharedPreferences confirmedsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF150, 0);
        String confirmed = confirmedsp.getString("confirmed", null);

        SharedPreferences packedsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF151, 0);
        String packed = packedsp.getString("packed", null);

        SharedPreferences deliveredsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF152, 0);
        String delivered = deliveredsp.getString("delivered", null);

        status = new String[]{all, pending, confirmed, packed, delivered};

        getStatus();

        SharedPreferences trackorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF188, 0);
        tv_header.setText(trackorder.getString("trackorder", null));

        SharedPreferences fromdate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF154, 0);
        etFromdate.setHint(fromdate.getString("fromdate", null));

        SharedPreferences todate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF155, 0);
        etTodate.setHint(todate.getString("todate", null));

//        SharedPreferences search = getApplicationContext().getSharedPreferences(Config.SHARED_PREF153, 0);
//        btnSearch.setText(search.getString("search", null));

        SharedPreferences Thereisnoorderlisttoshow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF344, 0);
        tvnoorderlist.setText(Thereisnoorderlisttoshow.getString("Thereisnoorderlisttoshow", null));


        SharedPreferences Nodatafoundsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF281, 0);
        Nodatafound = Nodatafoundsp.getString("Nodatafound", null);
        SharedPreferences oksp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        ok = oksp.getString("OK", null);

    }

    private void getStatus() {
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,status);
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        status_spinner.setAdapter(aa);

    }

    private void initiateViews() {
        rv_orderlist=(RecyclerView)findViewById(R.id.rv_orderlist);
        etSearch=(EditText)findViewById(R.id.etSearch);
        imcart=(ImageView) findViewById(R.id.imcart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        im = (ImageView) findViewById(R.id.im);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        emptyOrder=(LinearLayout) findViewById(R.id.emptyOrder);
        llsearch=(LinearLayout) findViewById(R.id.llsearch);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        etFromdate=(EditText)findViewById(R.id.etFromdate);
        etTodate=(EditText)findViewById(R.id.etTodate);
        btnSearch=(Button) findViewById(R.id.btnSearch);
        etFromdate.setKeyListener(null);
        etTodate.setKeyListener(null);
      //  mSwipeData = findViewById(R.id.swipeData);
        status_spinner = findViewById(R.id.status_spinner);
        tv_header = findViewById(R.id.tv_header);
        tvnoorderlist = findViewById(R.id.tvnoorderlist);
    }

    private void setRegViews() {
        im.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        imcart.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        etFromdate.setOnClickListener(this);
        etTodate.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        status_spinner.setOnItemSelectedListener(this);
    }

    private void getOrderList() {
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
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                requestObject1.put("ReqMode", "7");
                requestObject1.put("ID_Customer", pref.getString("userid", null));
                requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                Log.e(TAG,"requestObject1   2961    "+IDLanguages.getString("ID_Languages", null));

                Log.e(TAG,"requestObject1   296    "+requestObject1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
            Call<String> call = apiService.getOrderList(body);
            call.enqueue(new Callback<String>() {
                @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    try {
                        progressDialog.dismiss();
                        Log.e(TAG,"onResponse   306    "+response.body());
                        JSONObject jObject = new JSONObject(response.body());
                        JSONObject jobj = jObject.getJSONObject("salesOrderStatusDetailsInfo");
                        if (jobj.getString("SalesOrderStatusDetails").equals("null")) {
                            rv_orderlist.setVisibility(View.GONE);
                            emptyOrder.setVisibility(View.VISIBLE);
                            llsearch.setVisibility(View.GONE);
                        }else {
                            JSONArray jarray = jobj.getJSONArray("SalesOrderStatusDetails");
                            rv_orderlist.setVisibility(View.VISIBLE);
                            emptyOrder.setVisibility(View.GONE);
                            GridLayoutManager lLayout = new GridLayoutManager(OrdersActivity.this, 1);
                            rv_orderlist.setLayoutManager(lLayout);
                            rv_orderlist.setHasFixedSize(true);
                            OrderListAdapter adapter = new OrderListAdapter(OrdersActivity.this, jarray);
                            rv_orderlist.setAdapter(adapter);
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
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }

    private void getSearchOrderList(String FromDate, String Todate) {
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
                SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                requestObject1.put("ReqMode", "13");
                requestObject1.put("ID_Customer", pref.getString("userid", null));
              //  requestObject1.put("ID_Customer", "49");
                requestObject1.put("FromDate",FromDate);
                requestObject1.put("Todate", Todate);
                if(reqSubMode!=null){
                    requestObject1.put("ReqSubMode", reqSubMode);
                }
                requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                Log.e(TAG,"requestObject1    388   "+requestObject1);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
            Call<String> call = apiService.getSearchOrderList(body);
            call.enqueue(new Callback<String>() {
                @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    try {
                        Log.e(TAG,"response    398   "+response.body());
                        progressDialog.dismiss();
                        JSONObject jObject = new JSONObject(response.body());
                        JSONObject jobj = jObject.getJSONObject("salesOrderStatusDetailsInfo");
                        if (jobj.getString("SalesOrderStatusDetails").equals("null")) {
                            rv_orderlist.setVisibility(View.GONE);
                            emptyOrder.setVisibility(View.VISIBLE);
                            llsearch.setVisibility(View.GONE);
                           // llsearch.setBackgroundResource(R.color.greylight);
                        }else {
                            llsearch.setVisibility(View.VISIBLE);
                          //  llsearch.setBackgroundResource(R.color.white);
                            JSONArray jarray = jobj.getJSONArray("SalesOrderStatusDetails");
                            rv_orderlist.setVisibility(View.VISIBLE);
                            emptyOrder.setVisibility(View.GONE);
                            GridLayoutManager lLayout = new GridLayoutManager(OrdersActivity.this, 1);
                            rv_orderlist.setLayoutManager(lLayout);
                            rv_orderlist.setHasFixedSize(true);
                            OrderListAdapter adapter = new OrderListAdapter(OrdersActivity.this, jarray);
                            rv_orderlist.setAdapter(adapter);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im:

                //  drawer.openDrawer(Gravity.START);
              //  onBackPressed();
                startActivity(new Intent(OrdersActivity.this,HomeActivity.class));
                  break;
            case R.id.etSearch:
                startActivity(new Intent(OrdersActivity.this,SearchActivity.class));
                break;
            case R.id.imcart:
                Intent i = new Intent(OrdersActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.tvcart:
                Intent in = new Intent(OrdersActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.tv_notification:
                startActivity(new Intent(OrdersActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(OrdersActivity.this, NotificationActivity.class));
                break;
            case R.id.etFromdate:
                dateSelectorfrom();
                break;
            case R.id.etTodate:
                dateSelectorto();
                break;
            case R.id.btnSearch:
                try {
                String fromdate=etFromdate.getText().toString();
                String todate=etTodate.getText().toString();
                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String inputDateStrfrom=fromdate;
                String inputDateStrto=todate;
                Date date1 = inputFormat.parse(inputDateStrfrom);
                Date date2 = inputFormat.parse(inputDateStrto);

                Log.e(TAG,"fromdate  544   "+fromdate+"   "+todate);
                    if ( date1.before(date2)||date1.equals(date2)) {
                        String outputDateStrfrom = outputFormat.format(date1);
                        String outputDateStrto = outputFormat.format(date2);
                        getSearchOrderList(outputDateStrfrom,outputDateStrto);
                    }else if (fromdate.equals("") || todate.equals("")){
                        SharedPreferences pleaseselectcorrectdateintervalsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF258, 0);
                        String pleaseselectcorrectdateinterval = pleaseselectcorrectdateintervalsp.getString("pleaseselectcorrectdateinterval","");
                        AlertDialog.Builder builder= new AlertDialog.Builder(this);
                        builder.setMessage(pleaseselectcorrectdateinterval+".")
                                .setCancelable(false)
                                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else{
                        SharedPreferences pleaseselectcorrectdateintervalsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF258, 0);
                        String pleaseselectcorrectdateinterval = pleaseselectcorrectdateintervalsp.getString("pleaseselectcorrectdateinterval","");
                        AlertDialog.Builder builder= new AlertDialog.Builder(this);
                        builder.setMessage(pleaseselectcorrectdateinterval+".")
                                .setCancelable(false)
                                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG,"ParseException  578   "+e.toString());
                }
                break;
        }
    }

    public void dateSelectorfrom(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(OrdersActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                          etFromdate.setText(dayOfMonth + "-" + (monthOfYear + 1)+ "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        Calendar minDate = Calendar.getInstance();
        minDate.set(Calendar.DAY_OF_MONTH, mDay-(365));
        minDate.set(Calendar.MONTH, mMonth);
        minDate.set(Calendar.YEAR, mYear);
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.show();
    }

    public void dateSelectorto(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(OrdersActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                            etTodate.setText(dayOfMonth + "-" + (monthOfYear + 1)+ "-" + year);
                                    }
                }, mYear, mMonth, mDay);
        Calendar minDate = Calendar.getInstance();
        minDate.set(Calendar.DAY_OF_MONTH, mDay-(365));
        minDate.set(Calendar.MONTH, mMonth);
        minDate.set(Calendar.YEAR, mYear);
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
            startActivity(new Intent(OrdersActivity.this,HomeActivity.class));
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
        final String[] menulist = new String[]{"Home","My Cart", "Favourites","Favourite Stores",
                "Notifications", "Shopping List", "My Profile", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_favorite,R.drawable.ic_store,
                R.drawable.ic_notifications, R.drawable.ic_list,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(OrdersActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(OrdersActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(OrdersActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(OrdersActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(OrdersActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(OrdersActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(OrdersActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(OrdersActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    startActivity(new Intent(OrdersActivity.this, AboutUsActivity.class));
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

    private void setHomeNavMenu1() {
        final String[] menulist = new String[]{"Home","My Cart", "Favourite Items",
                "Notifications", "My Profile", "About Us", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_favorite,
                R.drawable.ic_notifications,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(OrdersActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(OrdersActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(OrdersActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(OrdersActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(OrdersActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(OrdersActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(OrdersActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(OrdersActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 9) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrdersActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) OrdersActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivity(new Intent(OrdersActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 11) {
                    startActivity(new Intent(OrdersActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(OrdersActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(OrdersActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OrdersActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) OrdersActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(OrdersActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(OrdersActivity.this,LocationActivity.class));
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // Toast.makeText(getApplicationContext(),status[position] , Toast.LENGTH_LONG).show();
     //  String mySpinner = (String) parent.getItemAtPosition(position);
      //  Toast.makeText(getApplicationContext(),mySpinner , Toast.LENGTH_LONG).show();
        String item_position = String.valueOf(position);
        int positonInt = Integer.valueOf(item_position);
      //  Toast.makeText(OrdersActivity.this, "value is "+ positonInt, Toast.LENGTH_SHORT).show();
        if(positonInt==0)
        {
            reqSubMode = "0";
        }
        else if(positonInt==1)
        {
            reqSubMode = "-1";
        }
        else if(positonInt==2)
        {
            reqSubMode = "1";
        }
        else if(positonInt==3)
        {
            reqSubMode = "2";
        }
        else if(positonInt==4)
        {
            reqSubMode = "3";
        }
        else
        {
            reqSubMode = "0";
        }
        getStatuslist(reqSubMode);
       /* String s1 =etFromdate.getText().toString();
        String s2 =etTodate.getText().toString();
        if(s1!=null && s2!=null ){
            getSearchOrderList1(s1,s2,reqSubMode);
        }*/
    }

    private void getStatuslist(String reqSubMode) {

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        if (new InternetUtil(this).isInternetOn()) {
          /*  progressDialog = new ProgressDialog(this, R.style.Progress);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(this.getResources()
                    .getDrawable(R.drawable.progress));
            progressDialog.show();*/
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
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    requestObject1.put("ReqMode", "7");
                    requestObject1.put("ID_Customer", pref.getString("userid", null));
                    requestObject1.put("ReqSubMode",reqSubMode);
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1  955   "+requestObject1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getStatuslist(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            //  progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            if(jObject.getString("StatusCode").equals("0")){
                            JSONObject jobj = jObject.getJSONObject("salesOrderStatusDetailsInfo");
                            if (jobj.getString("SalesOrderStatusDetails").equals("null")) {
                                rv_orderlist.setVisibility(View.GONE);
                                emptyOrder.setVisibility(View.VISIBLE);
                                llsearch.setVisibility(View.GONE);
                              //  llsearch.setBackgroundResource(R.color.greylight);
                            } else {
                                llsearch.setVisibility(View.VISIBLE);
                               // llsearch.setBackgroundResource(R.color.white);
                                JSONArray jarray = jobj.getJSONArray("SalesOrderStatusDetails");
                                rv_orderlist.setVisibility(View.VISIBLE);
                                emptyOrder.setVisibility(View.GONE);
                                GridLayoutManager lLayout = new GridLayoutManager(OrdersActivity.this, 1);
                                rv_orderlist.setLayoutManager(lLayout);
                                rv_orderlist.setHasFixedSize(true);
                                OrderListAdapter adapter = new OrderListAdapter(OrdersActivity.this, jarray);
                                rv_orderlist.setAdapter(adapter);
                            }
                        }else{

                                rv_orderlist.setVisibility(View.GONE);
                                emptyOrder.setVisibility(View.VISIBLE);
                                AlertDialog.Builder builder= new AlertDialog.Builder(OrdersActivity.this);
                                builder.setMessage(Nodatafound+".")
                                        .setCancelable(false)
                                        .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                           // progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                     //   progressDialog.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }


    @Override


    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setBottomBar() {
        navigation= (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().setGroupCheckable(0, false, false);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected( MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_category:
                    Intent io = new Intent(OrdersActivity.this, OutShopActivity.class);
                    io.putExtra(fromCategory, valueCategory);
                    finish();
                    startActivity(io);
                    return true;
                case R.id.navigation_cart:
                    Log.e(TAG,"navigation_cart    ");
                    Intent ic = new Intent(OrdersActivity.this, CartActivity.class);
                    ic.putExtra(fromCart, valueCart);
                    startActivity(ic);
                    return true;
                case R.id.navigation_home:
                    Log.e(TAG,"navigation_home    ");
                    Intent ih = new Intent(OrdersActivity.this, HomeActivity.class);
                    startActivity(ih);
                    return true;
                case R.id.navigation_favorite:
                    Log.e(TAG,"navigation_favorite    ");
                    Intent ifa = new Intent(OrdersActivity.this, FavouriteActivity.class);
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OrdersActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) OrdersActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

