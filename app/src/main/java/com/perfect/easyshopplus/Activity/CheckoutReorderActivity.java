package com.perfect.easyshopplus.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Servicess.GpsTracker;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;
import com.perfect.easyshopplus.Utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CheckoutReorderActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    int inExpressdelivery=0;
    ProgressDialog progressDialog;
    EditText etSearch, etdate,ettime, etremark;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvuser, tvcart, tv_subtotal, tv_amountpay, tv_discount, tv_memberdiscount, tv_gst, tv_savedamount, tvitemcount, tv_notification, tvtermsncondition, tv_grandtotal;
    DBHandler db;
    Button btnDatePicker, btnTimePicker, proceed;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ImageView selectTime, selectDate;
    Button confirmOrder;
    float subtotal, totalgst, aamountPay, totalMRP, totalRetailPrice, discount, memberdiscount, yousaved;
    String BranchWorkingHoursFrom,ID_SalesOrder,order_id,Id_order,orderDate,deliveryDate,status,itemcount,storeName,DeliveryCharge, BranchWorkingHoursTo, OrderType, ID_CustomerAddress,  MinimumTime, CurrentTime,ID_Store, ShopType, CurrentDate, WorkingHoursFrom, WorkingHoursTo, CustomerNumber,finalamount,OrderNo;
    LinearLayout ll_orderconfirm, ll_check, llGST;
    RelativeLayout rl_notification;
    CheckBox cbConfirm, cbExpress;
    int OrderBookingPeriod;
    String Image, OK, Somethingwentwrong;
    TextView tvexpressdeliveryamount, tv_chooseyourpickupdateandtime, tv_pricedetail, tv_header, discount_tv, tv_placeorderconfirm, memberdiscount_tv, othercharges_tv, GrandTotal_tv, amountpayable_tv;;

    String TAG = "CheckoutReorderActivity";
    BottomNavigationView navigation;
    String fromCategory = "from";
    String valueCategory = "home";
    String fromCart = "From";
    String valueCart = "Home";
    String fromHome = "";
    String valueHome = "";
    String fromFavorite = "From";
    String valueFavorite = "Home";

    private GpsTracker gpsTracker;
    private GoogleApiClient mGoogleApiClient;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    List<Address> addresses = null;
    private Location mLocation;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 1000; /* 1 sec */

    String strLongitude = "";
    String strLatitude = "";
    String strAdrressLine = "";
    private File fileimage = null;
    CheckBox cbRedeem;
    Button bt_apply;
    LinearLayout ll_redeem,ll_check_redeem,ll_balance_red;
    TextView tv_your_reward;
    EditText et_your_redeem;
    Double rewardString = 0.0;
    String redeemamount ="0",finalamountSave;
    LinearLayout ll_redeemsummary;
    TextView redeem_tv,redeem_tvamnt;
    TextView tv_Want_to_redeem,tv_reddemurreward;
    String StoreName_s,OrderNumber_s;
    String FK_SalesOrder;
    String IsOnlinePay = "false";
    String RedeemRequest = "false";

    LinearLayout ll_datetime,ll_redeemrew,ll_ordersummary;
    int flagDateTime = 0;
    int flagRedeem  = 0;
    int flagOrderSummary = 0;
    TextView txt_payamount,txt_savedyou;

    String Pc_PrivilageCardEnable = "false";
    String Pc_AccNumber = "";
    String Pc_ID_CustomerAcc = "0";
    String privilegeamount ="0";
    String strPaymenttype ="";
    String strPaymentId = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_checkout_main);
        Intent in = getIntent();
//        ID_Store = in.getStringExtra("ID_Store");
//        ShopType = in.getStringExtra("ShopType");
//        ID_CustomerAddress = in.getStringExtra("ID_CustomerAddress");
//        OrderType = in.getStringExtra("OrderType");
//        if (getIntent().getStringExtra("destination")!=null) {
        if ((File)in.getExtras().get("destination")!=null) {

//            Log.e(TAG,"PrescriptionImage  120   "+in.getStringExtra("destination"));
//            fileimage = new File(in.getStringExtra("destination"));
            fileimage = (File)in.getExtras().get("destination");
            Log.e(TAG,"PrescriptionImage  124   "+fileimage);
        }
//        Image = in.getStringExtra("Image");
        Image = "";

//,ID_SalesOrder,order_id,Id_order,orderDate,deliveryDate,status,itemcount,storeName,DeliveryCharge
        Log.e(TAG,"Start   140");

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

        initiateViews();
        setRegViews();
        setHomeNavMenu1();
        setBottomBar();

       /* SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/

        SharedPreferences prefFK_SalesOrder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF404, 0);
        SharedPreferences.Editor prefFK_SalesOrdereditor = prefFK_SalesOrder.edit();
        prefFK_SalesOrdereditor.putString("FK_SalesOrder", "0");

        SharedPreferences prefFK_SalesOrderNew = getApplicationContext().getSharedPreferences(Config.SHARED_PREF405, 0);
        SharedPreferences.Editor prefFK_SalesOrdereditorNew = prefFK_SalesOrderNew.edit();
        prefFK_SalesOrdereditorNew.putString("FK_SalesOrder_new", "0");
        prefFK_SalesOrdereditorNew.commit();

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

        SharedPreferences Subtotalsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF162, 0);
        String Subtotal = Subtotalsp.getString("Subtotal", "");
        SharedPreferences itemsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF140, 0);
        String item = itemsp.getString("item", "");
        SharedPreferences Itemssp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF163, 0);
        String Items = Itemssp.getString("Items", "");


        if(db.selectCartCount()<=1) {
            tvitemcount.setText(Subtotal+ " (" + String.valueOf(db.selectReorderCount())+" " +item+ " )");
        }else{
            tvitemcount.setText(Subtotal+ " (" + String.valueOf(db.selectReorderCount()) +" " +Items+ " )");
        }

//        if(db.selectReorderCount()<=1) {
//            tvitemcount.setText("SubTotal (" + String.valueOf(db.selectReorderCount()) + " Item)");
//        }else{
//            tvitemcount.setText("SubTotal (" + String.valueOf(db.selectReorderCount()) + " Items)");
//        }


        SharedPreferences Discount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF164, 0);
        discount_tv.setText(Discount.getString("Discount", ""));

        SharedPreferences memberdiscount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF165, 0);
        memberdiscount_tv.setText(memberdiscount.getString("memberdiscount", ""));

        SharedPreferences othercharges = getApplicationContext().getSharedPreferences(Config.SHARED_PREF141, 0);
        othercharges_tv.setText(othercharges.getString("othercharges", ""));

        SharedPreferences GrandTotal = getApplicationContext().getSharedPreferences(Config.SHARED_PREF166, 0);
        GrandTotal_tv.setText(GrandTotal.getString("GrandTotal", ""));

        SharedPreferences amountpayable = getApplicationContext().getSharedPreferences(Config.SHARED_PREF142, 0);
        amountpayable_tv.setText(amountpayable.getString("amountpayable", ""));

        SharedPreferences iacceptthetermsAndconditions = getApplicationContext().getSharedPreferences(Config.SHARED_PREF143, 0);
        tvtermsncondition.setText(iacceptthetermsAndconditions.getString("iacceptthetermsAndconditions", ""));

        SharedPreferences placeorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF217, 0);
        tv_placeorderconfirm.setText(placeorder.getString("placeorder", ""));
        tv_header.setText(placeorder.getString("placeorder", ""));

        SharedPreferences Somethingwentwrongsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF72, 0);
        Somethingwentwrong = Somethingwentwrongsp.getString("Somethingwentwrong", "");

        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        OK = (OKsp.getString("OK", ""));

        SharedPreferences chooseyourpickupdateandtime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF137, 0);
        tv_chooseyourpickupdateandtime.setText(chooseyourpickupdateandtime.getString("chooseyourpickupdateandtime", ""));
        SharedPreferences selectdate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF206, 0);
        etdate.setHint(selectdate.getString("selectdate", ""));
        SharedPreferences selecttime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF138, 0);
        ettime.setHint(selecttime.getString("selecttime", ""));
        SharedPreferences remarks = getApplicationContext().getSharedPreferences(Config.SHARED_PREF209, 0);
        etremark.setHint(remarks.getString("remarks", ""));
        SharedPreferences pricedetail = getApplicationContext().getSharedPreferences(Config.SHARED_PREF139, 0);
        tv_pricedetail.setText(pricedetail.getString("pricedetail", "")+" :");


        setViews();
        getCurrentdatentime();
        paymentcondition();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));

        SharedPreferences RedeemYourRewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF396, 0);
        tv_reddemurreward.setText(""+RedeemYourRewards.getString("RedeemYourRewards",""));

        SharedPreferences Doyouwanttoreddemfromyourrewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF393, 0);
        tv_Want_to_redeem.setText(""+Doyouwanttoreddemfromyourrewards.getString("Doyouwanttoreddemfromyourrewards",""));

        SharedPreferences EnterRedeemAmount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF399, 0);
        et_your_redeem.setHint(""+EnterRedeemAmount.getString("EnterRedeemAmount",""));

        SharedPreferences Apply = getApplicationContext().getSharedPreferences(Config.SHARED_PREF395, 0);
        bt_apply.setText(""+Apply.getString("Apply",""));

        SharedPreferences RedeemAmount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF398, 0);
        redeem_tv.setText(""+RedeemAmount.getString("RedeemAmount",""));
        getTimenDateLimit();

        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF39, 0);
        SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF40, 0);
       /* if(pref1.getString("ExpressDelivery", null).equals("true")) {
            cbExpress.setVisibility(View.VISIBLE);
            tvexpressdeliveryamount.setVisibility(View.VISIBLE);
            tvexpressdeliveryamount.setText("[ Extra Amount For Delivery : "+pref4.getString("ExpressDeliveryAmount", null)+"/- ]");
        }else{
            tvexpressdeliveryamount.setVisibility(View.GONE);
            cbExpress.setVisibility(View.GONE);
        }*/

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        // checkLocation(); //check whether location service is
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){


            SharedPreferences GPSisEnabledinyourdevicesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF76, 0);
            String GPSisEnabledinyourdevice = GPSisEnabledinyourdevicesp.getString("GPSisEnabledinyourdevice", "");

            Toast.makeText(this, GPSisEnabledinyourdevice, Toast.LENGTH_SHORT).show();
        }else{
            showGPSDisabledAlertToUser();
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }


        SharedPreferences ScratchCar = getApplicationContext().getSharedPreferences(Config.SHARED_PREF382, 0);
        if (ScratchCar.getString("ScratchCard",null).equals("true")){
            getGiftVoucher();
            ll_redeem.setVisibility(View.VISIBLE);
        }else{
            ll_redeem.setVisibility(View.GONE);
        }
        cbRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbRedeem.isChecked()){
                    RedeemRequest ="true";
                    ll_check_redeem.setVisibility(View.VISIBLE);
                    et_your_redeem.setText("");
                    SharedPreferences totalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF131, 0);
                    tv_amountpay.setText(/*string+" "+*/finalamountSave+" /-");
                    txt_payamount.setText(totalamount.getString("totalamount", "")+" : "+finalamountSave+" /-");
                    redeemamount = "0";
                    redeem_tvamnt.setText("0.00");
                    ll_redeemsummary.setVisibility(View.VISIBLE);
                }
                else {
                    RedeemRequest = "false";
                    ll_check_redeem.setVisibility(View.GONE);
                    redeemamount = "0";
                    SharedPreferences totalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF131, 0);
                    tv_amountpay.setText(/*string+" "+*/finalamountSave+" /-");
                    txt_payamount.setText(totalamount.getString("totalamount", "")+" : "+finalamountSave+" /-");
                    redeem_tvamnt.setText("0.00");
                    ll_redeemsummary.setVisibility(View.GONE);
                }
            }
        });

        bt_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (et_your_redeem.getText().length() != 0 ){
                    try {
                        Log.e(TAG,"finalamtchkRedeem   474   "+finalamountSave+"   "+finalamountSave);
                        if (Double.parseDouble(et_your_redeem.getText().toString())<Double.parseDouble(finalamountSave)){
                            Log.e(TAG,"finalamtchkRedeem   4741   "+finalamountSave+"   "+finalamountSave);
                            if (rewardString >= Double.parseDouble(et_your_redeem.getText().toString())){

                                redeemamount = String.valueOf(Double.parseDouble(et_your_redeem.getText().toString()));
                                String finalamountnew = String.valueOf(Double.parseDouble(finalamountSave) - Double.parseDouble(et_your_redeem.getText().toString()));
                                tv_amountpay.setText(/*string+" "+*/finalamountnew+" /-");
                                SharedPreferences totalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF131, 0);
                                txt_payamount.setText(totalamount.getString("totalamount", "")+" : "+Utils.getDecimelFormate(Double.parseDouble(finalamountnew)));
                                Log.e(TAG,"4201   finalamountnew       "+finalamountnew);
                                DecimalFormat f = new DecimalFormat("##.00");
                                redeem_tvamnt.setText(""+f.format(Double.parseDouble(redeemamount)));
                            }else {
                                Log.e(TAG,"Exception  42028   Check Amount");
                                Toast.makeText(getApplicationContext(),"Check Amount",Toast.LENGTH_SHORT).show();
                                redeemamount  = "0";
                                tv_amountpay.setText(/*string+" "+*/finalamountSave+" /-");
                                SharedPreferences totalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF131, 0);
                                txt_payamount.setText(totalamount.getString("totalamount", "")+" : "+Utils.getDecimelFormate(Double.parseDouble(finalamountSave)));
                                redeem_tvamnt.setText("0.00");
                            }
                        }else {
                            Log.e(TAG,"finalamtchkRedeem   4742   "+finalamountSave+"   "+finalamountSave);
                            Toast.makeText(getApplicationContext(),"Redeem Amount should be less than Payment amount",Toast.LENGTH_SHORT).show();
                            redeemamount  = "0";
                            tv_amountpay.setText(/*string+" "+*/finalamountSave+" /-");
                            SharedPreferences totalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF131, 0);
                            txt_payamount.setText(totalamount.getString("totalamount", "")+" : "+Utils.getDecimelFormate(Double.parseDouble(finalamountSave)));
                            redeem_tvamnt.setText("0.00");

                        }



                    }catch (Exception e){
                        Log.e(TAG,"Exception  42032   "+e.toString());
                    }
                    //
                }else {
                    Log.e(TAG,"Exception  42036   Check Amount");
                    Toast.makeText(getApplicationContext(),"Check Amount",Toast.LENGTH_SHORT).show();
                    redeemamount  = "0";
                    tv_amountpay.setText(/*string+" "+*/finalamountSave+" /-");
                    SharedPreferences totalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF131, 0);
                    txt_payamount.setText(totalamount.getString("totalamount", "")+" : "+finalamountSave+" /-");
                    redeem_tvamnt.setText("0.00");
                }

                Utils.hideKeyboard(CheckoutReorderActivity.this);
            }
        });
    }

    private void getGiftVoucher() {
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
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Calendar cal = Calendar.getInstance();
                    String currentdate = dateFormat.format(cal.getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                    String currenttime= sdf.format(cal.getTime()) ;
                    String StrDeliveryDate;
                    String DeliveryTime;

                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);


                    requestObject1.put("ReqMode","1");
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    requestObject1.put("FK_Customer", pref1.getString("userid",null));
                    requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("FK_CustomerReward","0");
                    requestObject1.put("FromDate",currentdate);
                    requestObject1.put("ToDate",currentdate);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getGiftVoucherList(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            if(jObject.getString("StatusCode").equals("0")){
                                JSONObject jobj = jObject.getJSONObject("GiftVoucherListInfo");

                                String rewardAmount = jobj.getString("count");
                                SharedPreferences YourRewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF388, 0);
                                tv_your_reward.setText(""+YourRewards.getString("YourRewards","")+" : "+ rewardAmount);
                                rewardString = Double.parseDouble(rewardAmount);

                                if (rewardString > 0.0){
                                    ll_balance_red.setVisibility(View.VISIBLE);
                                }
                                else{
                                    ll_balance_red.setVisibility(View.GONE);
                                }

                            }else{
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG,"Exception  183  "+e.toString());
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

    private void initiateViews() {
        llGST = (LinearLayout) findViewById(R.id.llGST);
        etSearch=(EditText)findViewById(R.id.etSearch);
        etdate=(EditText)findViewById(R.id.etdate);
        ettime=(EditText)findViewById(R.id.ettime);
        etremark = (EditText) findViewById(R.id.etremark);
        etdate.setKeyListener(null);
        ettime.setKeyListener(null);
        imcart=(ImageView) findViewById(R.id.imcart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        im = (ImageView) findViewById(R.id.im);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        tv_subtotal = (TextView) findViewById(R.id.tv_subtotal);
        tv_amountpay = (TextView) findViewById(R.id.tv_amountpay);
        tv_discount = (TextView) findViewById(R.id.tv_discount);
        tv_memberdiscount = (TextView) findViewById(R.id.tv_memberdiscount);
        tv_gst = (TextView) findViewById(R.id.tv_gst);
        tv_savedamount = (TextView) findViewById(R.id.tv_savedamount);
        tvitemcount = (TextView) findViewById(R.id.tvitemcount);
        tvtermsncondition = (TextView) findViewById(R.id.tvtermsncondition);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        ll_orderconfirm = (LinearLayout) findViewById(R.id.ll_orderconfirm);
        ll_check = (LinearLayout) findViewById(R.id.ll_check);
        selectDate= findViewById(R.id.select_date);
        selectTime = findViewById(R.id.select_time);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        cbConfirm = (CheckBox) findViewById(R.id.cbConfirm);
        tv_grandtotal = (TextView) findViewById(R.id.tv_grandtotal);
        tvexpressdeliveryamount = (TextView) findViewById(R.id.tvexpressdeliveryamount);
        cbExpress = (CheckBox) findViewById(R.id.cbExpress);


        discount_tv = findViewById(R.id.discount_tv);
        tv_placeorderconfirm = findViewById(R.id.tv_placeorderconfirm);
        memberdiscount_tv = findViewById(R.id.memberdiscount_tv);
        othercharges_tv = findViewById(R.id.othercharges_tv);
        GrandTotal_tv = findViewById(R.id.GrandTotal_tv);
        amountpayable_tv = findViewById(R.id.amountpayable_tv);
        tv_header =  findViewById(R.id.tv_header);
        tv_chooseyourpickupdateandtime = findViewById(R.id.tv_chooseyourpickupdateandtime);
        tv_pricedetail =  findViewById(R.id.tv_pricedetail);

        ll_redeem = findViewById(R.id.ll_redeem);
        et_your_redeem = findViewById(R.id.et_your_redeem);
        cbRedeem = findViewById(R.id.cbRedeem);
        bt_apply = findViewById(R.id.bt_apply);
        ll_check_redeem = findViewById(R.id.ll_check_redeem);
        ll_balance_red = findViewById(R.id.ll_balance_red);
        tv_your_reward = findViewById(R.id.tv_your_reward);
        ll_redeemsummary = findViewById(R.id.ll_redeemsummary);
        redeem_tv = findViewById(R.id.redeem_tv);
        redeem_tvamnt = findViewById(R.id.redeem_tvamnt);



        tv_Want_to_redeem = findViewById(R.id.tv_Want_to_redeem);
        tv_reddemurreward = findViewById(R.id.tv_reddemurreward);

        ll_datetime = findViewById(R.id.ll_datetime);
        ll_redeemrew = findViewById(R.id.ll_redeemrew);
        ll_ordersummary = findViewById(R.id.ll_ordersummary);

        txt_payamount = findViewById(R.id.txt_payamount);
        txt_savedyou = findViewById(R.id.txt_savedyou);
    }

    private void setRegViews() {
        im.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        imcart.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        etdate.setOnClickListener(this);
        ettime.setOnClickListener(this);
        selectTime.setOnClickListener(this);
        selectDate.setOnClickListener(this);
        ll_orderconfirm.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        tvtermsncondition.setOnClickListener(this);
        tvtermsncondition.setPaintFlags(tvtermsncondition.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tv_chooseyourpickupdateandtime.setOnClickListener(this);
        tv_reddemurreward.setOnClickListener(this);
        tv_pricedetail.setOnClickListener(this);
    }

    private void setViews() {
        DecimalFormat f = new DecimalFormat("##.00");
        String string = "\u20B9";
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {e.printStackTrace();}
        subtotal=db.selectReorderTotalActualPrice();
        totalgst=db.ReorderTotalGST();
        totalMRP=db.ReorderTotalMRP();
        totalRetailPrice=db.ReorderTotalRetailPrice();
        discount=totalMRP-totalRetailPrice;
        memberdiscount=   totalRetailPrice-subtotal;
        aamountPay=subtotal+totalgst;
        yousaved=discount+memberdiscount;
        tv_subtotal.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(totalMRP))));
        tv_grandtotal.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        tv_amountpay.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        SharedPreferences totalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF131, 0);
        txt_payamount.setText(totalamount.getString("totalamount", "")+" : "+f.format(Double.parseDouble(String.valueOf(subtotal))));
        String value=String.valueOf(subtotal);
        String value1=value.substring(value.lastIndexOf(".") + 1);
        char value2 = value1.charAt(0);
        int checkDecimal=Integer.parseInt(String.valueOf(value2));
        String [] twoStringArray= value.split("\\.", 2);
        if(checkDecimal>=5){
            int famount=Integer.parseInt(twoStringArray[0]);
            int roundvalue=famount+1;
            finalamount= String.valueOf(roundvalue);
            finalamountSave= String.valueOf(roundvalue);
        }else{
            finalamount =twoStringArray[0];
            finalamountSave =twoStringArray[0];
        }
        // tv_amountpay.setText(string+" "+finalamount+" /-");
        if(f.format(Double.parseDouble(String.valueOf(discount))).equals(".00")){
            tv_discount.setText(/*string+*/" 0.00");
        }else{
            tv_discount.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(discount))));}
        if(f.format(Double.parseDouble(String.valueOf(memberdiscount))).equals(".00")){
            tv_memberdiscount.setText(/*string+*/" 0.00");
        }else{
            tv_memberdiscount.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(memberdiscount))));}
        if(totalgst<=0){
            llGST.setVisibility(View.INVISIBLE);
        }else{
            llGST.setVisibility(View.VISIBLE);
            tv_gst.setVisibility(View.GONE);
        }
//        tv_savedamount.setText("( You have saved "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");
        SharedPreferences youhavesaved = getApplicationContext().getSharedPreferences(Config.SHARED_PREF212, 0);
        tv_savedamount.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");
        txt_savedyou.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im:

                //  drawer.openDrawer(Gravity.START);
                onBackPressed();                break;
            case R.id.etSearch:
                startActivity(new Intent(CheckoutReorderActivity.this,SearchActivity.class));
                break;
            case R.id.imcart:
                Intent i = new Intent(CheckoutReorderActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.tvcart:
                Intent in = new Intent(CheckoutReorderActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.etdate:
                dateSelector();
                break;
            case R.id.ettime:
                timeSelector();
                break;
            case R.id.select_time:
                timeSelector();
                break;
            case R.id.select_date:
                dateSelector();
                break;
            case R.id.ll_orderconfirm:
              /*  Boolean checkBoxExpress = cbExpress.isChecked();
                if(checkBoxExpress) {
                    inExpressdelivery=1;
                }
                else {
                    inExpressdelivery=0;
                }
                holidayCheck();*/
/*
                Boolean checkBoxExpress = cbExpress.isChecked();
                if(checkBoxExpress) {
                    inExpressdelivery=1;
                }
                else {
                    inExpressdelivery=0;
                }
*/
                if (cbRedeem.isChecked()){
                    finalamount = String.valueOf(Double.parseDouble(finalamountSave)-Double.parseDouble(redeemamount));
                }else {
                    finalamount = finalamountSave;
                }
                getLocation();
                if (strLatitude.equals("") && strLongitude.equals("")){


                    SharedPreferences LocationNotFoundsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF75, 0);
                    String LocationNotFound = LocationNotFoundsp.getString("LocationNotFound", "");

                    Toast.makeText(getApplicationContext(),LocationNotFound,Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferences pref9 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF61, 0);
                    String strTimeSlotCheck=pref9.getString("TimeSlotCheckReorder", null);
                    if(strTimeSlotCheck.equals("true")){
                        AsOnDateApplicableChecking(); }
                    else{
                        holidayCheck();
                    }
                }



                break;
            case R.id.tv_notification:
                startActivity(new Intent(CheckoutReorderActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(CheckoutReorderActivity.this, NotificationActivity.class));
                break;
            case R.id.tvtermsncondition:

                SharedPreferences availabilityofthestockpackaging = getApplicationContext().getSharedPreferences(Config.SHARED_PREF65, 0);
                String availabilityofthestockpack = (availabilityofthestockpackaging.getString("availabilityofthestockpackaging", ""));

                SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
                String OK = (OKsp.getString("OK", ""));
                SharedPreferences termsandconditionssp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF192, 0);
                String termsandconditions = (termsandconditionssp.getString("termsandconditions", ""));

                AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderActivity.this);
                builder.setTitle(termsandconditions);
                builder.setMessage(availabilityofthestockpack)
                        .setCancelable(false)
                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
                break;

            case R.id.tv_chooseyourpickupdateandtime:

                if (flagDateTime == 0){
                    flagDateTime = 1;
                    ll_datetime.setVisibility(View.GONE);
                    tv_chooseyourpickupdateandtime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                }else {
                    flagDateTime = 0;
                    ll_datetime.setVisibility(View.VISIBLE);
                    tv_chooseyourpickupdateandtime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);
                }

                break;

            case R.id.tv_reddemurreward:

                if (flagRedeem == 0){
                    flagRedeem = 1;
                    ll_redeemrew.setVisibility(View.GONE);
                    tv_reddemurreward.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                }else {
                    flagRedeem = 0;
                    ll_redeemrew.setVisibility(View.VISIBLE);
                    tv_reddemurreward.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);
                }

                break;

            case R.id.tv_pricedetail:

                if (flagOrderSummary == 0){
                    flagOrderSummary = 1;
                    ll_ordersummary.setVisibility(View.GONE);
                    tv_pricedetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                }else {
                    flagOrderSummary = 0;
                    ll_ordersummary.setVisibility(View.VISIBLE);
                    tv_pricedetail.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);
                }

                break;
        }
    }


    private void AsOnDateApplicableChecking(){
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
                    String StrDeliveryDate = null;
                    if (!etdate.getText().toString().isEmpty()) {
                        String DeliveryDate = etdate.getText().toString();
                        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                        DateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
                        Date date = inputFormat.parse(DeliveryDate);
                        StrDeliveryDate = outputFormat.format(date);
                    }

                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);

                    requestObject1.put("ReqMode", "29");
                    requestObject1.put("DeliveryDate", StrDeliveryDate);
                    requestObject1.put("DeliveryType",OrderType);
                    requestObject1.put("FK_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getAsOnDateApplicableChecking(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("AsOnDateApplicableChecking");
                            if(jObject.getString("StatusCode").equals("0")){
                                String strHoliday=jobj.getString("IsAsOnDateApplicable");
                                if(strHoliday.equals("true")){

                                    if(ettime.getText().toString().isEmpty()){

                                        SharedPreferences Pleaseselectdeliverytimesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF69, 0);
                                        String Pleaseselectdeliverytime = (Pleaseselectdeliverytimesp.getString("Pleaseselectdeliverytime", ""));

                                        AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderActivity.this);
                                        builder.setMessage(Pleaseselectdeliverytime)
                                                .setCancelable(false)
                                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                    else{
                                        holidayCheck();}
                                }else{
                                    SharedPreferences sorryonedayprocessdeliciousorderselectnextdaysp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF70, 0);
                                    String sorryonedayprocessdeliciousorderselectnextday = (sorryonedayprocessdeliciousorderselectnextdaysp.getString("sorryonedayprocessdeliciousorderselectnextday", ""));

                                    AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderActivity.this);
                                    //  builder.setTitle("Terms & Conditions");
                                    builder.setMessage(sorryonedayprocessdeliciousorderselectnextday)
                                            .setCancelable(false)
                                            .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    //Toast.makeText(getApplicationContext(),"Order for Your selected date is a closed, Please select any other date!",Toast.LENGTH_LONG).show();
                                }


                            }else{
                                Toast.makeText(getApplicationContext(),Somethingwentwrong+" !!",Toast.LENGTH_LONG).show();
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
    private void holidayCheck(){
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
                    String StrDeliveryDate = null;
                    if (!etdate.getText().toString().isEmpty()) {
                        String DeliveryDate = etdate.getText().toString();
                        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                        DateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
                        Date date = inputFormat.parse(DeliveryDate);
                        StrDeliveryDate = outputFormat.format(date);
                    }

                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);

                    requestObject1.put("ReqMode", "20");
                    requestObject1.put("OrderDate", StrDeliveryDate);
                    requestObject1.put("DeliveryType",OrderType);
                    requestObject1.put("FK_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getHolidayChecking(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("HolidayChecking");
                            if(jObject.getString("StatusCode").equals("0")){
                                String strHoliday=jobj.getString("IsHoliday");
                                if(strHoliday.equals("false")){

                                    Boolean checkBoxState = cbConfirm.isChecked();
                                    if(checkBoxState) {
                                        checkingMinimumTimeIntervel();
                                    }
                                    else {

                                        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
                                        String OK = (OKsp.getString("OK", ""));
                                        SharedPreferences PleaseacceptTermsandConditionssp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF68, 0);
                                        String PleaseacceptTermsandConditions = (PleaseacceptTermsandConditionssp.getString("PleaseacceptTermsandConditions", ""));

                                        AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderActivity.this);
                                        builder.setMessage(PleaseacceptTermsandConditions)
                                                .setCancelable(false)
                                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }else{

                                    SharedPreferences HolidayPleaseselectotherdatesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF73, 0);
                                    String HolidayPleaseselectotherdate = HolidayPleaseselectotherdatesp.getString("HolidayPleaseselectotherdate", "");
                                    Toast.makeText(getApplicationContext(),HolidayPleaseselectotherdate + " !",Toast.LENGTH_LONG).show();
                                }


                            }else{
                                Toast.makeText(getApplicationContext(),Somethingwentwrong+" !!",Toast.LENGTH_LONG).show();
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
        final String[] menulist = new String[]{"Home","My Cart","My Orders", "Favourites","Favourite Stores",
                "Notifications", "Shopping List","My Profile", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,R.drawable.ic_store,
                R.drawable.ic_notifications, R.drawable.ic_list,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(CheckoutReorderActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(CheckoutReorderActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(CheckoutReorderActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(CheckoutReorderActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(CheckoutReorderActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(CheckoutReorderActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(CheckoutReorderActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(CheckoutReorderActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    startActivity(new Intent(CheckoutReorderActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 8) {
                    startActivity(new Intent(CheckoutReorderActivity.this, AboutUsActivity.class));
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
        final String[] menulist = new String[]{"Home","My Cart","Track Orders", "Favourite Items",
                "Notifications", "My Profile", "About Us", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications, R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(CheckoutReorderActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(CheckoutReorderActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(CheckoutReorderActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(CheckoutReorderActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(CheckoutReorderActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(CheckoutReorderActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(CheckoutReorderActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(CheckoutReorderActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(CheckoutReorderActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 10) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutReorderActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) CheckoutReorderActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivity(new Intent(CheckoutReorderActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(CheckoutReorderActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(CheckoutReorderActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 14) {
                    startActivity(new Intent(CheckoutReorderActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CheckoutReorderActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) CheckoutReorderActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(CheckoutReorderActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(CheckoutReorderActivity.this,LocationActivity.class));
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

    public void dateSelector(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(CheckoutReorderActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        etdate.setText(dayOfMonth + "-" + (monthOfYear + 1)+ "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.DAY_OF_MONTH, mDay + (OrderBookingPeriod-1));
        maxDate.set(Calendar.MONTH, mMonth);
        maxDate.set(Calendar.YEAR, mYear);
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.show();
    }

    public void timeSelector() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        Calendar datetime = Calendar.getInstance();
                        Calendar c = Calendar.getInstance();
                        datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        datetime.set(Calendar.MINUTE, minute);
                        if (CurrentDate.equals(etdate.getText().toString())) {
                            if (datetime.getTimeInMillis() >= c.getTimeInMillis()) {
                                try {
                                    int hour = hourOfDay % 12;
                                    String strdate, strDate;
                                    strdate = CurrentTime;
                                    strDate = String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                            minute, hourOfDay < 12 ? "am" : "pm");
                                    SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                                    Date date1 = format.parse(strdate);
                                    Date date2 = format.parse(strDate);
                                    long mills = date2.getTime() - date1.getTime();
                                    int hours = (int) (mills / (1000 * 60 * 60));
                                    int mins = (int) (mills / (1000 * 60)) % 60;
                                    String differenceTime = hours + ":" + mins;
                                    String minTime = MinimumTime.replaceAll("\\.", ":");
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                    if (dateFormat.parse(minTime).after(dateFormat.parse(differenceTime))) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutReorderActivity.this);
                                        builder.setMessage("Please select greater time, we need minimum "+minTime+" hour for packing.")
                                                .setCancelable(false)
                                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    } else {
                                        ettime.setText(strDate);
                                    }
                                } catch (ParseException e) {e.printStackTrace();}
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutReorderActivity.this);
                                builder.setMessage("Invalid time! \nPlease Select time greater than current time")
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } else {
                            try {
                                int hour = hourOfDay % 12;
                                String strdate, strDate;
                                strDate = String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                        minute, hourOfDay < 12 ? "am" : "pm");
                                Date datecurrent = new SimpleDateFormat("hh:mm aa").parse(strDate);
                                SimpleDateFormat tf24 = new SimpleDateFormat("HH:mm");
                                SimpleDateFormat tf12 = new SimpleDateFormat("hh:mm aa");
                                Date datefrom = tf24.parse(WorkingHoursFrom);
                                Date fromdate = new SimpleDateFormat("hh:mm aa").parse(tf12.format(datefrom));
                                Date dateto = tf24.parse(WorkingHoursTo);
                                Date todate=new SimpleDateFormat("hh:mm aa").parse(tf12.format(dateto));
                                if((datecurrent.after(fromdate))&&(datecurrent.before(todate))||datecurrent.equals(fromdate)||datecurrent.equals(todate) ) {
                                    SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                                    strdate = String.valueOf(tf12.format(datefrom));
                                    Date date2 = format.parse(strDate);
                                    Date date1 = format.parse(strdate);
                                    long mills = date2.getTime() - date1.getTime();
                                    int hours = (int) (mills / (1000 * 60 * 60));
                                    int mins = (int) (mills / (1000 * 60)) % 60;
                                    String differenceTime = hours + ":" + mins;
                                    String minTime = MinimumTime.replaceAll("\\.", ":");
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                    if (dateFormat.parse(minTime).after(dateFormat.parse(differenceTime))) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutReorderActivity.this);
                                        builder.setMessage("Please select greater time, we need minimum "+minTime+" hour for packing.")
                                                .setCancelable(false)
                                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    } else {
                                        ettime.setText(strDate);
                                    }
                                }else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutReorderActivity.this);
                                    builder.setMessage("Working hours are from "+WorkingHoursFrom+" to "+WorkingHoursTo+
                                            "\nPlease select pickup time according to this time interval." )
                                            .setCancelable(false)
                                            .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            } catch (ParseException e) {e.printStackTrace();}
                        }
                    }
                }, mHour, mMinute, false);
        timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timePickerDialog.show();
    }

    private void getWorkingTime() {
        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
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
                        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                        DateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
                        Date date = inputFormat.parse(etdate.getText().toString());
                        String StrCheckDate = outputFormat.format(date);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                        requestObject1.put("ID_Store",  pref.getString("ID_Store", null));
                        requestObject1.put("CheckDate",StrCheckDate);
                        requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                        SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                        requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                    Call<String> call = apiService.getWorkingtime(body);
                    call.enqueue(new Callback<String>() {
                        @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            try {
                                JSONObject jObject = new JSONObject(response.body());
                                JSONObject jobj = jObject.getJSONObject("storeTimedetails");
                                BranchWorkingHoursFrom = jobj.getString("WorkingHoursFrom");
                                BranchWorkingHoursTo = jobj.getString("WorkingHoursTo");
                                try {
                                    String string1 = ettime.getText().toString();
                                    Date date1 = new SimpleDateFormat("hh:mm aa").parse(string1);
                                    String string2 = BranchWorkingHoursFrom;
                                    Date date2 = new SimpleDateFormat("hh:mm aa").parse(string2);
                                    String someRandomTime = BranchWorkingHoursTo;
                                    Date date3 = new SimpleDateFormat("hh:mm aa").parse(someRandomTime);
                                    if((date1.after(date2))&&(date1.before(date3))||date1.equals(date2) ||date1.equals(date3) ) {
                                        doOrderConfirm(etdate.getText().toString());
                                    }else {
                                        Toast.makeText(getApplicationContext(),"Working hours are from "+BranchWorkingHoursFrom+" to "+BranchWorkingHoursTo,Toast.LENGTH_LONG).show();
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } catch (JSONException e) {
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
                Toast.makeText(getApplicationContext(),Somethingwentwrong+" !!",Toast.LENGTH_LONG).show();

//            Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
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

    private void doOrderConfirm(String selectedDate){
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
                    DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String currentdate=dateFormat.format(cal.getTime());
                    String DeliveryDate=selectedDate;
                    String DeliveryTime= ettime.getText().toString();
                    DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
                    Date date = inputFormat.parse(DeliveryDate);
                    String StrDeliveryDate = outputFormat.format(date);
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    SharedPreferences pref3= getApplicationContext().getSharedPreferences(Config.SHARED_PREF9, 0);
                    requestObject1.put("ID_Customer", pref1.getString("userid", null));
                    requestObject1.put("OrderDate", currentdate);
                    requestObject1.put("DeliveryDate",StrDeliveryDate);
                    requestObject1.put("DeliveryTime", DeliveryTime);
                    requestObject1.put("ID_Store",ID_Store);
                    requestObject1.put("Remarks", etremark.getText().toString());
                    JSONArray jsonArray = new JSONArray();
                    DBHandler db=new DBHandler(this);
                    Cursor cursor = db.select("reordercart");
                    int i = 0;
                    if (cursor.moveToFirst()) {
                        do {
                            JSONObject jsonObject1 = new JSONObject();
                            try {
                                jsonObject1.put("ID_Stock", cursor.getString(cursor.getColumnIndex("Stock_ID")));
                                jsonObject1.put("ID_Item", cursor.getString(cursor.getColumnIndex("ID_Items")));
                                jsonObject1.put("ItemName", cursor.getString(cursor.getColumnIndex("ItemName")));
                                jsonObject1.put("MRP",cursor.getString(cursor.getColumnIndex("MRP")));
                                jsonObject1.put("SalesPrice", cursor.getString(cursor.getColumnIndex("SalesPrice")));
                                jsonObject1.put("Quantity", cursor.getString(cursor.getColumnIndex("Count")));
                                jsonObject1.put("RetailPrice",cursor.getString(cursor.getColumnIndex("RetailPrice")));
                                jsonObject1.put("VAT", cursor.getString(cursor.getColumnIndex("GST")));
                                jsonObject1.put("Cess",cursor.getString(cursor.getColumnIndex("CESS")));
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

                    Integer DeliveryType;
                    if (OrderType .equals("1")){
                        DeliveryType = 1;
                    }
                    else {
                        DeliveryType = 0;
                    }
                    requestObject1.put("Data", jsonArray);
                    requestObject1.put("UserAction", "1");
                    requestObject1.put("ShopType", ShopType);
                    requestObject1.put("DeliveryType", DeliveryType);
                    requestObject1.put("ID_CustomerAddress",ID_CustomerAddress);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    requestObject1.put("ExpressDelivery", inExpressdelivery);
                    requestObject1.put("SOLongitude", strLongitude);
                    requestObject1.put("SOLattitude", strLatitude);

                    if(Image==null||Image.isEmpty()||Image.equals("")||Image.length()<=0) {}else{
                   /* byte[] data = Base64.decode(Image, Base64.DEFAULT);
                    requestObject1.put("PrescriptionImage", data);*/
//                    requestObject1.put("PrescriptionImage", Image);
                    }
                    requestObject1.put("PrescriptionImage", "");
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                    requestObject1.put("Redeem",redeemamount);

                    SharedPreferences prefFK_SalesOrderNew = getApplicationContext().getSharedPreferences(Config.SHARED_PREF405, 0);

                    requestObject1.put("IsOnlinePay",IsOnlinePay);
                    requestObject1.put("ID_SalesOrder",prefFK_SalesOrderNew.getString("FK_SalesOrder_new","0"));
                    requestObject1.put("RedeemRequest",RedeemRequest);

                    requestObject1.put("PrivilageCardEnable",Pc_PrivilageCardEnable);
                    requestObject1.put("PrivCardAmount",privilegeamount);
                    requestObject1.put("AccNumber",Pc_AccNumber);
                    requestObject1.put("ID_CustomerAcc",Pc_ID_CustomerAcc);

                    Log.e(TAG,"requestObject1   1252   "+requestObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("jsondattaa","fileimage  1422   "+fileimage);
                MultipartBody.Part imageFiles = null;
                Call<String> call = null;
                if (fileimage !=null){
                    Log.e("jsondattaa","fileimage  1618   "+fileimage);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileimage);
                    imageFiles = MultipartBody.Part.createFormData("JsonData", fileimage.getName(), requestFile);
                    RequestBody body = RequestBody.create(MediaType.parse("text/plain"), requestObject1.toString());
                    Log.e("jsondattaa","BODY  1676 "+body);
                    Log.e("jsondattaa","BODY  1677 "+imageFiles);

                    call = apiService.getSalesOrderUpdateImgFile(body,imageFiles);
                }
                else {

                    Log.e("jsondattaa","BODY  1683 "+requestObject1);
                    RequestBody body = RequestBody.create(MediaType.parse("text/plain"), requestObject1.toString());
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                    imageFiles = MultipartBody.Part.createFormData("JsonData", "", requestFile);
                    call = apiService.getSalesOrderUpdateImgFile(body,imageFiles);

                }

//            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
//            Call<String> call = apiService.confirmOrder(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("SalesOrderDetails");
                            if(jObject.getString("StatusCode").equals("3")){
                                Toast.makeText(getApplicationContext(), jobj.getString("ResponseMessage"),Toast.LENGTH_LONG).show();
                                DBHandler db=new DBHandler(CheckoutReorderActivity.this);
                                db.deleteallCart();

                                OrderNumber_s = jobj.getString("OrderNumber");
                                FK_SalesOrder = jobj.getString("FK_SalesOrder");

                                SharedPreferences prefFK_SalesOrder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF404, 0);
                                SharedPreferences.Editor prefFK_SalesOrdereditor = prefFK_SalesOrder.edit();
                                prefFK_SalesOrdereditor.putString("FK_SalesOrder", FK_SalesOrder);
                                prefFK_SalesOrdereditor.commit();

                                SharedPreferences prefFK_SalesOrderNew = getApplicationContext().getSharedPreferences(Config.SHARED_PREF405, 0);
                                SharedPreferences.Editor prefFK_SalesOrdereditorNew = prefFK_SalesOrderNew.edit();
                                prefFK_SalesOrdereditorNew.putString("FK_SalesOrder_new", jobj.getString("FK_SalesOrder"));
                                prefFK_SalesOrdereditorNew.commit();
//
//                                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
//                                Intent intent = new Intent(CheckoutReorderActivity.this,ThanksActivity.class);
//                                intent.putExtra("StoreName", pref1.getString("StoreName", null));
//                                intent.putExtra("OrderNumber",jobj.getString("OrderNumber"));
//                                intent.putExtra("strPaymenttype","COD");
//                                intent.putExtra("finalamount",finalamount);
//                                startActivity(intent);
////                            startActivity(new Intent(CheckoutReorderActivity.this, ThanksActivity.class));
//                                finish();

                                updatePayments(OrderNumber_s,FK_SalesOrder,strPaymentId,"","0","","0",finalamount,"0");

                            }else if(jObject.getString("StatusCode").equals("10")){
                                AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderActivity.this);
                                builder.setMessage(jobj.getString("ResponseMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent i = new Intent(CheckoutReorderActivity.this, CartActivity.class);
                                                i.putExtra("From", "Home");
                                                startActivity(i);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }else{
                                SharedPreferences OrderSubmittedFailed = getApplicationContext().getSharedPreferences(Config.SHARED_PREF311, 0);
                                Toast.makeText(getApplicationContext(),OrderSubmittedFailed.getString("OrderSubmittedFailed", "")+ " !",Toast.LENGTH_LONG).show();
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

    public void validation() {
        if (etdate.getText().toString().isEmpty()) {

            SharedPreferences PleaseselectdeliveryDatesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF71, 0);
            String PleaseselectdeliveryDate = (PleaseselectdeliveryDatesp.getString("PleaseselectdeliveryDate", ""));

            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setMessage(PleaseselectdeliveryDate)
                    .setCancelable(false)
                    .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (ettime.getText().toString().isEmpty()) {

            SharedPreferences Pleaseselectdeliverytimesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF69, 0);
            String Pleaseselectdeliverytime = (Pleaseselectdeliverytimesp.getString("Pleaseselectdeliverytime", ""));

            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setMessage(Pleaseselectdeliverytime)
                    .setCancelable(false)
                    .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }   else {
            getWorkingTime();
        }
    }

    public void getCurrentdatentime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
        CurrentDate = sdf.format(c.getTime());
        CurrentTime = sdf1.format(c.getTime());
        etdate.setText(CurrentDate);
    }

    private void getTimenDateLimit(){
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
                    String DeliveryDate=etdate.getText().toString();
                    DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                    DateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
                    Date date = inputFormat.parse(DeliveryDate);
                    String StrDeliveryDate = outputFormat.format(date);
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                    requestObject1.put("CheckDate",StrDeliveryDate);
                    requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getStoreTimeDate(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            JSONObject jObject = new JSONObject(response.body());
                            if(jObject.getString("StatusCode").equals("0")){
                                JSONObject jobj = jObject.getJSONObject("storeTimedetails");
                                OrderBookingPeriod=jobj.getInt("OrderBookingPeriod");
                                MinimumTime=jobj.getString("MinimumTime");
                                WorkingHoursFrom =jobj.getString("WorkingHoursFrom");
                                WorkingHoursTo=jobj.getString("WorkingHoursTo");
                                CustomerNumber=jobj.getString("CustomerNumber");
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
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }

    public void checkingMinimumTimeIntervel(){
        try {
            String strdate, strDate;
            if (ettime.getText().toString().isEmpty()) {
                strdate = CurrentTime;
            }else {
                strdate= ettime.getText().toString();
            }
            String wht=WorkingHoursTo;
            if(/*WorkingHoursTo.equals("")||WorkingHoursTo.isEmpty()||
                    WorkingHoursTo==null||WorkingHoursTo.length()==0||*/
                    WorkingHoursTo.equals("null")) {
                Toast.makeText(getApplicationContext(),Somethingwentwrong+"..",Toast.LENGTH_SHORT).show();
            }else {
                SimpleDateFormat tf24 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat tf12 = new SimpleDateFormat("hh:mm aa");
                Date date = tf24.parse(WorkingHoursTo);
                strDate = String.valueOf(tf12.format(date));
                SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                Date date1 = format.parse(strdate);
                Date date2 = format.parse(strDate);
                long mills = date2.getTime() - date1.getTime();
                int hours = (int) (mills / (1000 * 60 * 60));
                int mins = (int) (mills / (1000 * 60)) % 60;
                String differenceTime = hours + ":" + mins;
                String minTime = MinimumTime.replaceAll("\\.", ":");
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                if (dateFormat.parse(minTime).after(dateFormat.parse(differenceTime))) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
                    LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater1.inflate(R.layout.warning_timeintervel_popup, null);
                    LinearLayout ll_call = (LinearLayout) layout.findViewById(R.id.ll_call);
                    LinearLayout ll_ok = (LinearLayout) layout.findViewById(R.id.ll_ok);
                    ImageView imclose = (ImageView) layout.findViewById(R.id.imclose);
                    View view = (View) layout.findViewById(R.id.view);
                    if (CustomerNumber.isEmpty()) {
                        ll_call.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                    } else {
                        ll_call.setVisibility(View.VISIBLE);
                        view.setVisibility(View.VISIBLE);
                    }
                    builder.setView(layout);
                    final android.app.AlertDialog alertDialog = builder.create();
                    imclose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    ll_call.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", CustomerNumber, null));
                            startActivity(intent);
                            alertDialog.dismiss();
                        }
                    });
                    ll_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                            Calendar c = Calendar.getInstance();
                            try {
                                c.setTime(sdf.parse(etdate.getText().toString()));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            c.add(Calendar.DATE, 1);
                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                            String output = sdf1.format(c.getTime());
                            //Toast.makeText(getApplicationContext(),output,Toast.LENGTH_LONG).show();
                            doOrderConfirm(output);
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                } else {
                    doOrderConfirm(etdate.getText().toString());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
                    Intent io = new Intent(CheckoutReorderActivity.this, OutShopActivity.class);
                    io.putExtra(fromCategory, valueCategory);
                    finish();
                    startActivity(io);
                    return true;
                case R.id.navigation_cart:
                    Log.e(TAG,"navigation_cart    ");
                    Intent ic = new Intent(CheckoutReorderActivity.this, CartActivity.class);
                    ic.putExtra(fromCart, valueCart);
                    startActivity(ic);
//                    finish();
                    //   onBackPressed();
                    return true;
                case R.id.navigation_home:
                    Log.e(TAG,"navigation_home    ");
                    Intent iha = new Intent(CheckoutReorderActivity.this, HomeActivity.class);
                    iha.putExtra(fromFavorite, valueFavorite);
                    startActivity(iha);
                    return true;
                case R.id.navigation_favorite:
                    Log.e(TAG,"navigation_favorite    ");
                    Intent ifa = new Intent(CheckoutReorderActivity.this, FavouriteActivity.class);
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CheckoutReorderActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) CheckoutReorderActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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


    /////////////////////////////////////////


    /////////////////////////////////////


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
            // mLatitudeTextView.setText(String.valueOf(mLocation.getLatitude()));
            //mLongitudeTextView.setText(String.valueOf(mLocation.getLongitude()));
        } else {


            SharedPreferences LocationnotDetectedsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF74, 0);
            String LocationnotDetected = LocationnotDetectedsp.getString("LocationnotDetected", "");
            Toast.makeText(this, LocationnotDetected, Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.e(TAG, "Connection Suspended");
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    }

    @Override
    public void onLocationChanged(Location location) {

        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());

        //     Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Geocoder geocoder;
        geocoder = new Geocoder(this, Locale.getDefault());

        strLongitude = String.valueOf(location.getLongitude());
        strLatitude = String.valueOf(location.getLatitude());

        try {
            Log.e(TAG,"1617    "+location.getLatitude()+"   "+ location.getLongitude());
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            setLocationAddress();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }

    private void setLocationAddress() {


        if (addresses != null) {
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
            Double latitudee = addresses.get(0).getLatitude();
            Double longitudee = addresses.get(0).getLongitude();

            Log.e(TAG,"1667    "+city);


            if (knownName == null){
                knownName = "";
            }
            if (city == null){
                city = "";
            }

            if (postalCode == null){
                postalCode = "";
            }




        }
    }

    public void getLocation(){
        gpsTracker = new GpsTracker(CheckoutReorderActivity.this);
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            //  tvLatitude.setText(gpsTracker.getAddress());
            // tvLongitude.setText(String.valueOf(longitude));

            strAdrressLine  =gpsTracker.getAddress();
            strLongitude = String.valueOf(longitude);
            strLatitude = String.valueOf(latitude);




            Log.e(TAG,"GETLOCATION    "+gpsTracker.getAddress());


        }else{
            gpsTracker.showSettingsAlert();
        }
    }

    private void showGPSDisabledAlertToUser() {
        SharedPreferences GPSisdisabledinyourdevicesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF66, 0);
        String GPSisdisabledinyourdevice = (GPSisdisabledinyourdevicesp.getString("GPSisdisabledinyourdevice", ""));
        SharedPreferences GotoSettingsPageToEnableGPSsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF67, 0);
        String GotoSettingsPageToEnableGPS = (GotoSettingsPageToEnableGPSsp.getString("GotoSettingsPageToEnableGPS", ""));
        SharedPreferences Cancelsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF105, 0);
        String Cancel = (Cancelsp.getString("Cancel", ""));

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(GPSisdisabledinyourdevice )
                .setCancelable(false)
                .setPositiveButton(GotoSettingsPageToEnableGPS,
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton(Cancel,
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void paymentcondition(){


        SharedPreferences OnlinePaymentmeth1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF62, 0);
        String BASEURL = OnlinePaymentmeth1.getString("OnlinePaymentMethods", null);
        Log.e(TAG,"BASEURLSSSSS   2283    "+BASEURL);



        SharedPreferences OnlinePaymentpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF62, 0);
        String value = OnlinePaymentpref.getString("OnlinePaymentMethods", null);
        Log.e(TAG,"OnlinePaymentpref   2293    "+value);
        try {
            JSONArray jsonArrayPay = new JSONArray(value);
            for (int i=0;i<jsonArrayPay.length();i++){
                JSONObject jsonObject = jsonArrayPay.getJSONObject(i);
                if (jsonObject.getString("ID_PaymentMethod").equals("1")){
                    strPaymenttype = jsonObject.getString("PaymentName");
                }

            }



        } catch (Exception e) {
            Log.e(TAG,"Exception   2322    "+e.toString());
        }
    }

    private void updatePayments(String id_salesOrder, String FK_SalesOrder,String fk_paymentMethod, String uniqueTxnID, String payDescription,
                                String authStatuss, String payResponseId, String txnAmount, String txnType) {

        Log.e(TAG,"455   id_salesOrder       "+id_salesOrder);
        Log.e(TAG,"455   FK_SalesOrder       "+FK_SalesOrder);
        Log.e(TAG,"455   fk_paymentMethod    "+fk_paymentMethod);
        Log.e(TAG,"455   uniqueTxnID         "+uniqueTxnID);
        Log.e(TAG,"455   payDescription      "+payDescription);
        Log.e(TAG,"455   authStatus          "+authStatuss);
        Log.e(TAG,"455   payResponseId       "+payResponseId);
        Log.e(TAG,"455   txnAmount           "+txnAmount);
        Log.e(TAG,"455   txnType             "+txnType);
        Log.e(TAG,"455   txnType             "+txnType);
        Log.e(TAG,"455   strPaymenttype             "+strPaymenttype);

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
                    DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
                    Calendar cal = Calendar.getInstance();
                    String currentdate = dateFormat.format(cal.getTime());
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");


                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                    SharedPreferences pref3= getApplicationContext().getSharedPreferences(Config.SHARED_PREF9, 0);
                    SharedPreferences pref4= getApplicationContext().getSharedPreferences(Config.SHARED_PREF20, 0);


//					uniqueTxnID = "";
//					authStatus = "";
//					txnAmount = "";
//					txnType  ="";

                    requestObject1.put("ID_SalesOrder", FK_SalesOrder);
                    requestObject1.put("FK_PaymentMethod", fk_paymentMethod);
                    requestObject1.put("PayTransactionID",uniqueTxnID);
                    requestObject1.put("PayDescription", payDescription);
                    requestObject1.put("PayStatus", authStatuss);
                    requestObject1.put("PayResponseId",payResponseId );
                    requestObject1.put("Amount",txnAmount );
                    requestObject1.put("TransType", txnType);


                    Log.e(TAG,"requestObject1   516    "+requestObject1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.PaymentDetailUpdate(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG,"response   1675  "+response.body());


//							{"GateWayResult":{"Status":true,"TransDateTime":"25-08-2021 11:14:31","TransType":"01","TransactionId":"BDSK11112","TranStatus":"0300",
//									"TranAmnt":"00000002.00","ResponseCode":"0","ResponseMessage":"Transaction Verified"},"StatusCode":0,"EXMessage":null}


                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("SalesOrderDetails");
                            if(jObject.getString("StatusCode").equals("0")){

                                Log.e(TAG,"authStatus   567 1   "+authStatuss+"   ");
                                startActivity(new Intent(CheckoutReorderActivity.this, ThanksActivity.class));
                                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                Intent intent = new Intent(CheckoutReorderActivity.this,ThanksActivity.class);
                                intent.putExtra("StoreName", pref1.getString("StoreName", null));
                                intent.putExtra("OrderNumber",jobj.getString("OrderNumber"));
                                intent.putExtra("strPaymenttype",strPaymenttype);
                                intent.putExtra("finalamount",finalamount);
//
//
                                startActivity(intent);

                                finish();


                            }else {
                                AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderActivity.this);
                                builder.setMessage(jobj.getString("ResponseMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(new Intent(CheckoutReorderActivity.this, HomeActivity.class));
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }

//

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
            Intent in = new Intent(this, NoInternetActivity.class);
            startActivity(in);
        }



    }

}

