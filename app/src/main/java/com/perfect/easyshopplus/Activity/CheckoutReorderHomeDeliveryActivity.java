package com.perfect.easyshopplus.Activity;

import android.Manifest;
import android.app.Activity;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.billdesk.sdk.PaymentOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Activity.billdesks.SampleCallBack;
import com.perfect.easyshopplus.Adapter.AdapterPaymentOptions;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Servicess.GpsTracker;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.ItemClickListener;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;
import com.perfect.easyshopplus.Utility.Utils;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

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

public class CheckoutReorderHomeDeliveryActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener , ItemClickListener, PaymentResultListener {


    ProgressDialog progressDialog;
    int inExpressdelivery=0;
    EditText etSearch, etremark;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvuser, tvPin, tvcart, tv_mobile, tv_Name, tv_address, tv_subtotal, tv_amountpay, tv_discount, tv_memberdiscount, tv_gst, tv_savedamount, tvitemcount, tv_notification, tvtermsncondition, tv_grandtotal;
    DBHandler db;
    Button btnEdit;
    private int mYear, mMonth, mDay ,mHour, mMinute;;
    ImageView selectTime, selectDate;
    float floatdeliverycharge, subtotal, totalgst, aamountPay, totalMRP, totalRetailPrice, discount, memberdiscount, yousaved;
    String ID_Store, ShopType, ID_CustomerAddress, OrderType, usermobile, finalamount, address, PIN, username, st_oldpassword, st_newpassword, userid, date, time,OrderNo;
    LinearLayout ll_orderconfirm, ll_check, llGST, llAddress, addAddressLl, ll_express;
    RelativeLayout rl_notification;
    CheckBox cbConfirm, cbExpress;
    int OrderBookingPeriod;
    String strDeliveryCharge;
    Date d1, d2, d3, d4, d5;
    Button btAdd;
    RadioButton radioButton, radioButton2, radioButton3;
    EditText etdate, ettime, ettime1;
    TextView tvexpressdeliveryamount, tv_deliverycharges;
    TextView tv_placeorderconfirm, tv_header,tv_paymenttype, tv_deliaddress, tv_emptyshippingaddress, tv_choosedeliverydateandtime, tv_ordersummary;
    TextView discount_tv, memberdiscount_tv,deliverycharges_tv,othercharges_tv, GrandTotal_tv, amountpayable_tv;
    String  OK ,MinimumDeliveryAmount, ThereissometechnicalissuesPleaseuseanotherpaymentoptions, PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdevice, Pleaseselectanypaymentoption, PleaseselectdeliveryStarttimeEndtime, Pleaseselectdeliveryendtime, Pleaseselectdeliverystarttime;
    String LocationNotFound, Somethingwentwrong, GPSisEnabledinyourdevice, HolidayPleaseselectotherdate ;

    String Image, DeliveryCharge,ID_SalesOrder,order_id,Id_order,orderDate,deliveryDate,status,itemcount,storeName;
    LinearLayout llOrdersummary;
    String TAG = "CheckoutReorderHomeDeliveryActivity";

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

    TextView txt_payamount,txt_savedyou;
    LinearLayout lnr_datetime,lnr_redeem,ll_ordersummary,ll_paymenttype;
    int flagDateTime =0;
    int flagRedeem = 0;
    int flagOrderSummary = 0;
    int flagPayType = 0;

    String IsOnlinePay = "false";
    String RedeemRequest = "false";
    RecyclerView recyc_paymenttype;
    JSONArray jsonArrayPay;

    String strPaymentId="1";
    String strPaymenttype="";
    String MerchantID = null;
    String TransactionID = null;
    String SecurityID = null;
    String PaymentSuccessfully, Paymentfailed;
    final int UPI_PAYMENT = 0;

    String privilegeamount ="0";
    String Pc_PrivilageCardEnable = "false";
    String Pc_AccNumber = "";
    String Pc_ID_CustomerAcc = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_address_add_activty);
        initiateViews();
        setRegister();
      //  llOrdersummary.setVisibility(View.GONE);
        setHomeNavMenu1();

     //   radioButton.setVisibility(View.GONE);

        paymentcondition();

       /* SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/
        Log.e(TAG,"Start   123");

        SharedPreferences prefFK_SalesOrder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF404, 0);
        SharedPreferences.Editor prefFK_SalesOrdereditor = prefFK_SalesOrder.edit();
        prefFK_SalesOrdereditor.putString("FK_SalesOrder", "0");

        SharedPreferences prefFK_SalesOrderNew = getApplicationContext().getSharedPreferences(Config.SHARED_PREF405, 0);
        SharedPreferences.Editor prefFK_SalesOrdereditorNew = prefFK_SalesOrderNew.edit();
        prefFK_SalesOrdereditorNew.putString("FK_SalesOrder_new", "0");
        prefFK_SalesOrdereditorNew.commit();

        getSharedPreferences();

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        SharedPreferences PaymentSuccessfullysp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF329, 0);
        PaymentSuccessfully  = PaymentSuccessfullysp.getString("PaymentSuccessfully","");


        SharedPreferences Paymentfailedsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF330, 0);
        Paymentfailed = Paymentfailedsp.getString("Paymentfailed","");


        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
        ImageView imApplogo=findViewById(R.id.imApplogo);
        String strImagepath= IMAGEURL + pref3.getString("AppIconImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath).into(imApplogo);
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
        Image = "";
  //      Image = in.getStringExtra("Image");
//        DeliveryCharge = in.getStringExtra("DeliveryCharge");


        ID_SalesOrder = in.getStringExtra("ID_SalesOrder");
        order_id = in.getStringExtra("order_id");
        Id_order = in.getStringExtra("Id_order");
        orderDate = in.getStringExtra("orderDate");
        OrderNo = in.getStringExtra("OrderNo");
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
        floatdeliverycharge=Float.parseFloat(DeliveryCharge);

        Log.e(TAG,"OrderType   222   "+OrderType);



        SharedPreferences Subtotalsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF162, 0);
        String Subtotal = Subtotalsp.getString("Subtotal", "");
        SharedPreferences itemsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF140, 0);
        String item = itemsp.getString("item", "");
        SharedPreferences Itemssp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF163, 0);
        String Items = Itemssp.getString("Items", "");
        db = new DBHandler(this);
        if (db.selectCartCount() <= 1) {
            tvitemcount.setText(Subtotal+" (" + String.valueOf(db.selectReorderCount()) + " "+item+")");
        } else {
            tvitemcount.setText(Subtotal+" (" + String.valueOf(db.selectReorderCount()) + " "+Items+")");
        }
        setViews();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String currentdate = dateFormat.format(cal.getTime());
        etdate.setText(currentdate);

        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF39, 0);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF40, 0);
        if(pref1.getString("ExpressDelivery", null).equals("true")) {
            cbExpress.setVisibility(View.VISIBLE);
            tvexpressdeliveryamount.setVisibility(View.VISIBLE);
            SharedPreferences extraamountfordelivery = getApplicationContext().getSharedPreferences(Config.SHARED_PREF215, 0);
            tvexpressdeliveryamount.setText("[ "+extraamountfordelivery.getString("extraamountfordelivery", "")+" : "+pref.getString("ExpressDeliveryAmount", null)+"/- ]");
//            tvexpressdeliveryamount.setText("[ Extra Amount For Delivery :  "+pref.getString("ExpressDeliveryAmount", null)+"/- ]");
        }else{
            tvexpressdeliveryamount.setVisibility(View.GONE);
            cbExpress.setVisibility(View.GONE);
        }



        SharedPreferences pref01 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF19, 0);
        String addressDel =pref01.getString("DeliAddress", null);

        if(addressDel==null||addressDel.isEmpty()){
            llAddress.setVisibility(View.GONE);
            addAddressLl.setVisibility(View.VISIBLE);
            tv_address.setVisibility(View.GONE);
        }else{
            llAddress.setVisibility(View.VISIBLE);
            addAddressLl.setVisibility(View.GONE);
            tv_address.setVisibility(View.VISIBLE);
            SharedPreferences pref02 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF18, 0);
            String name =pref02.getString("DeliUsername", null);
            SharedPreferences pref03 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF21, 0);
            String pin =pref03.getString("DeliPincode", null);
            SharedPreferences pref04 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF25, 0);
            String mobNum =pref04.getString("DeliMobNumb", null);
            //   tv_address.setText(name +"\n"+addressDel+"\nPIN : "+pin +"\nMOB NUM : +91"+mobNum);


            if(mobNum.length()!=0){
//                tv_address.setText(name +"\n"+addressDel+"\nPIN : "+pin +"\nMOB NUM : +91"+mobNum);
                tv_address.setText(name +"\n"+addressDel+"\nMOB NUM : +91"+mobNum);
            }
            else {
//                tv_address.setText(name +"\n"+addressDel+"\nPIN : "+pin );
                tv_address.setText(name +"\n"+addressDel);

            }

//        tv_address.setText(username +"\n"+address+"\nPIN : "+PIN +"\nMOB NUM : +91"+usermobile);
        }

        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        // checkLocation(); //check whether location service is
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, GPSisEnabledinyourdevice, Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "GPS is Enabled in your devide", Toast.LENGTH_SHORT).show();
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
                    RedeemRequest = "true";
                    ll_check_redeem.setVisibility(View.VISIBLE);
                    et_your_redeem.setText("");
                    tv_amountpay.setText(/*string+" "+*/finalamountSave+" /-");
                    txt_payamount.setText(/*string+" "+*/finalamountSave+" /-");
                    redeemamount = "0";
                    redeem_tvamnt.setText("0.00");
                    ll_redeemsummary.setVisibility(View.VISIBLE);
                }
                else {
                    RedeemRequest = "false";
                    ll_check_redeem.setVisibility(View.GONE);
                    redeemamount = "0";
                    tv_amountpay.setText(/*string+" "+*/finalamountSave+" /-");
                    txt_payamount.setText(/*string+" "+*/finalamountSave+" /-");
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
                    txt_payamount.setText(/*string+" "+*/finalamountSave+" /-");
                    redeem_tvamnt.setText("0.00");
                }

                Utils.hideKeyboard(CheckoutReorderHomeDeliveryActivity.this);
            }
        });

    }




    private void getSharedPreferences() {
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
        userid = pref1.getString("userid", null);
        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        username = pref2.getString("username", null);
        /*SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF3, 0);
        useremail=pref3.getString("useremail", null);*/
        SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF4, 0);
        usermobile = pref4.getString("userphoneno", null);
        /*SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
        memberid=pref5.getString("memberid", null);*/
        SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF15, 0);
        address = pref6.getString("Address", null);
        SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF16, 0);
        PIN = pref7.getString("PIN", null);









        SharedPreferences selectdate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF206, 0);
        etdate.setHint(selectdate.getString("selectdate", ""));
        SharedPreferences starttime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF207, 0);
        ettime.setHint(starttime.getString("starttime", ""));
        SharedPreferences endtime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF208, 0);
        ettime1.setHint(endtime.getString("endtime", ""));
        SharedPreferences remarks = getApplicationContext().getSharedPreferences(Config.SHARED_PREF209, 0);
        etremark.setHint(remarks.getString("remarks", ""));

        SharedPreferences paymenttype = getApplicationContext().getSharedPreferences(Config.SHARED_PREF213, 0);
        tv_paymenttype.setText(paymenttype.getString("paymenttype", ""));

        SharedPreferences ordersummary = getApplicationContext().getSharedPreferences(Config.SHARED_PREF210, 0);
        tv_ordersummary.setText(ordersummary.getString("ordersummary", ""));

        SharedPreferences choosedeliverydateandtime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF205, 0);
        tv_choosedeliverydateandtime.setText(choosedeliverydateandtime.getString("choosedeliverydateandtime", ""));

        SharedPreferences emptyshippingaddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF202, 0);
        tv_emptyshippingaddress.setText(emptyshippingaddress.getString("emptyshippingaddress", ""));

        SharedPreferences addaddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF203, 0);
        btAdd.setText(addaddress.getString("addaddress", ""));

        SharedPreferences Deliveryaddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF201, 0);
        tv_deliaddress.setText(Deliveryaddress.getString("Deliveryaddress", ""));

        SharedPreferences placeorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF217, 0);
        tv_placeorderconfirm.setText(placeorder.getString("placeorder", ""));
        tv_header.setText(placeorder.getString("placeorder", ""));




        SharedPreferences Discount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF164, 0);
        discount_tv.setText(Discount.getString("Discount", ""));

        SharedPreferences memberdiscount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF165, 0);
        memberdiscount_tv.setText(memberdiscount.getString("memberdiscount", ""));

        SharedPreferences DeliveryCharges = getApplicationContext().getSharedPreferences(Config.SHARED_PREF211, 0);
        deliverycharges_tv.setText(DeliveryCharges.getString("DeliveryCharges", ""));

        SharedPreferences othercharges = getApplicationContext().getSharedPreferences(Config.SHARED_PREF141, 0);
        othercharges_tv.setText(othercharges.getString("othercharges", ""));

        SharedPreferences GrandTotal = getApplicationContext().getSharedPreferences(Config.SHARED_PREF166, 0);
        GrandTotal_tv.setText(GrandTotal.getString("GrandTotal", ""));

        SharedPreferences amountpayable = getApplicationContext().getSharedPreferences(Config.SHARED_PREF142, 0);
        amountpayable_tv.setText(amountpayable.getString("amountpayable", ""));

        SharedPreferences iacceptthetermsAndconditions = getApplicationContext().getSharedPreferences(Config.SHARED_PREF143, 0);
        tvtermsncondition.setText(iacceptthetermsAndconditions.getString("iacceptthetermsAndconditions", ""));



        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        OK = (OKsp.getString("OK", ""));

        SharedPreferences ThereissometechnicalissuesPleaseuseanotherpaymentoptionsSP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF262, 0);
        ThereissometechnicalissuesPleaseuseanotherpaymentoptions = (ThereissometechnicalissuesPleaseuseanotherpaymentoptionsSP.getString("ThereissometechnicalissuesPleaseuseanotherpaymentoptions", ""));

        SharedPreferences PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdeviceSP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF263, 0);
        PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdevice = (PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdeviceSP.getString("PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdevice", ""));

        SharedPreferences Pleaseselectanypaymentoptionsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF264, 0);
        Pleaseselectanypaymentoption = (Pleaseselectanypaymentoptionsp.getString("Pleaseselectanypaymentoption", ""));

        SharedPreferences PleaseselectdeliveryStarttimeEndtimeSP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF265, 0);
        PleaseselectdeliveryStarttimeEndtime = (PleaseselectdeliveryStarttimeEndtimeSP.getString("PleaseselectdeliveryStarttimeEndtime", ""));

        SharedPreferences Pleaseselectdeliveryendtimesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF266, 0);
        Pleaseselectdeliveryendtime = (Pleaseselectdeliveryendtimesp.getString("Pleaseselectdeliveryendtime", ""));

        SharedPreferences Pleaseselectdeliverystarttimesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF267, 0);
        Pleaseselectdeliverystarttime = (Pleaseselectdeliverystarttimesp.getString("Pleaseselectdeliverystarttime", ""));




        SharedPreferences LocationNotFoundsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF75, 0);
        LocationNotFound = LocationNotFoundsp.getString("LocationNotFound", "");

        SharedPreferences GPSisEnabledinyourdevicesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF76, 0);
        GPSisEnabledinyourdevice = GPSisEnabledinyourdevicesp.getString("GPSisEnabledinyourdevice", "");

        SharedPreferences Somethingwentwrongsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF72, 0);
        Somethingwentwrong = Somethingwentwrongsp.getString("Somethingwentwrong", "");

        SharedPreferences HolidayPleaseselectotherdatesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF73, 0);
        HolidayPleaseselectotherdate = HolidayPleaseselectotherdatesp.getString("HolidayPleaseselectotherdate", "");

        SharedPreferences ChangeAddresssp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF260, 0);
        btnEdit.setText(ChangeAddresssp.getString("ChangeAddress", ""));

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
    }

    private void setHomeNavMenu() {
        final String[] menulist = new String[]{"Home", "My Cart", "My Orders", "Favourites", "Favourite Stores",
                "Notifications", "Shipping List", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home, R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,R.drawable.ic_store,
                R.drawable.ic_notifications, R.drawable.ic_list,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(CheckoutReorderHomeDeliveryActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(CheckoutReorderHomeDeliveryActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, AboutUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 8) {
                    doLogout();
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 9) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "\t" + "Invite You  \n\n For Android Users \n http://play.google.com/store/apps/details?id=" + getPackageName() +
                            "\n");
                    startActivity(Intent.createChooser(share, "Invite this App to your friends"));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });


    }

    private void setHomeNavMenu1() {
        final String[] menulist = new String[]{"Home", "My Cart", "Track Orders", "Favourite Items",
                "Notifications",  "About Us", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home, R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(CheckoutReorderHomeDeliveryActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(CheckoutReorderHomeDeliveryActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 5) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, AboutUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    doLogout();
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "\t" + "Invite You  \n\n For Android Users \n http://play.google.com/store/apps/details?id=" + getPackageName() +
                            "\n");
                    startActivity(Intent.createChooser(share, "Invite this App to your friends"));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 8) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 9) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) CheckoutReorderHomeDeliveryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 11) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
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

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) CheckoutReorderHomeDeliveryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this,LocationActivity.class));
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

    private void doChangePassword() {
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
            try {
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
                    requestObject1.put("ReqMode", "4");
                    requestObject1.put("OldPassword", st_oldpassword);
                    requestObject1.put("NewPassword", st_newpassword);
                    requestObject1.put("ID_Customer", userid);
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getChangePassword(body);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jmember = jObject.getJSONObject("UserNameAndPasswordChk");
                            String ResponseCode = jmember.getString("ResponseCode");
                            if (ResponseCode.equals("0")) {
                                Toast.makeText(getApplicationContext(), jmember.getString("ResponseMessage"), Toast.LENGTH_LONG).show();
                            }
                            if (ResponseCode.equals("-1")) {
                                Toast.makeText(getApplicationContext(), jmember.getString("ResponseMessage"), Toast.LENGTH_LONG).show();
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
        } else {
            Intent in = new Intent(this, NoInternetActivity.class);
            startActivity(in);
        }
    }

    private void setRegister() {
        etSearch.setOnClickListener(this);
        imcart.setOnClickListener(this);
        im.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        tv_address.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        ll_orderconfirm.setOnClickListener(this);
        btAdd.setOnClickListener(this);
        etdate.setOnClickListener(this);
        ettime.setOnClickListener(this);
        ettime1.setOnClickListener(this);
        tvtermsncondition.setOnClickListener(this);
        tvtermsncondition.setPaintFlags(tvtermsncondition.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        tv_paymenttype.setOnClickListener(this);
        tv_ordersummary.setOnClickListener(this);
        tv_reddemurreward.setOnClickListener(this);
        tv_choosedeliverydateandtime.setOnClickListener(this);

    }

    private void initiateViews() {
        etSearch = (EditText) findViewById(R.id.etSearch);
        etremark = (EditText) findViewById(R.id.etremark);
        etdate = (EditText) findViewById(R.id.etdate);
        ettime = (EditText) findViewById(R.id.ettime);
        ettime1 = (EditText) findViewById(R.id.ettime1);
        etdate.setKeyListener(null);
        ettime.setKeyListener(null);
        ettime1.setKeyListener(null);
        imcart = (ImageView) findViewById(R.id.imcart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        btnEdit = (Button) findViewById(R.id.btEdit);
        im = (ImageView) findViewById(R.id.im);
        tvPin = (TextView) findViewById(R.id.tvPin);
        tv_mobile = (TextView) findViewById(R.id.tvMobile);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        tv_address = (TextView) findViewById(R.id.tvadrss);
        btAdd = (Button) findViewById(R.id.btAdd);
        tv_subtotal = (TextView) findViewById(R.id.tv_subtotal);
        tv_amountpay = (TextView) findViewById(R.id.tv_amountpay);
        tv_discount = (TextView) findViewById(R.id.tv_discount);
        tv_memberdiscount = (TextView) findViewById(R.id.tv_memberdiscount);
        tv_deliverycharges = (TextView) findViewById(R.id.tv_deliverycharges);
        tv_gst = (TextView) findViewById(R.id.tv_gst);
        tv_Name = (TextView) findViewById(R.id.tvName);
        tv_savedamount = (TextView) findViewById(R.id.tv_savedamount);
        tvitemcount = (TextView) findViewById(R.id.tvitemcount);
        tvtermsncondition = (TextView) findViewById(R.id.tvtermsncondition);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        ll_orderconfirm = (LinearLayout) findViewById(R.id.ll_orderconfirm);
        ll_check = (LinearLayout) findViewById(R.id.ll_check);
        llGST = (LinearLayout) findViewById(R.id.llGST);
        llAddress = (LinearLayout) findViewById(R.id.llAddress);
        selectDate = findViewById(R.id.select_date);
        selectTime = findViewById(R.id.select_time);
        rl_notification = (RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        tv_grandtotal = (TextView) findViewById(R.id.tv_grandtotal);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        cbConfirm = (CheckBox) findViewById(R.id.cbConfirm);
        addAddressLl =  findViewById(R.id.addAddressLl);
        ll_express = (LinearLayout) findViewById(R.id.ll_express);
        llOrdersummary = (LinearLayout) findViewById(R.id.llOrdersummary);
        cbExpress = (CheckBox) findViewById(R.id.cbExpress);
        ll_express = (LinearLayout) findViewById(R.id.ll_express);
        tvexpressdeliveryamount = (TextView) findViewById(R.id.tvexpressdeliveryamount);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        radioButton3 = (RadioButton) findViewById(R.id.radioButton3);
        radioButton2.setVisibility(View.GONE);
        radioButton3.setVisibility(View.GONE);
        radioButton = (RadioButton) findViewById(R.id.radioButton);
        radioButton.setChecked(true);

        ll_redeem = findViewById(R.id.ll_redeem);
        et_your_redeem = findViewById(R.id.et_your_redeem);
        cbRedeem = findViewById(R.id.cbRedeem);
        bt_apply = findViewById(R.id.bt_apply);
        ll_check_redeem = findViewById(R.id.ll_check_redeem);
        ll_balance_red = findViewById(R.id.ll_balance_red);
        tv_your_reward = findViewById(R.id.tv_your_reward);



        discount_tv = findViewById(R.id.discount_tv);
        memberdiscount_tv = findViewById(R.id.memberdiscount_tv);
        othercharges_tv = findViewById(R.id.othercharges_tv);
        deliverycharges_tv = findViewById(R.id.deliverycharges_tv);
        GrandTotal_tv = findViewById(R.id.GrandTotal_tv);
        amountpayable_tv = findViewById(R.id.amountpayable_tv);

        tv_placeorderconfirm = findViewById(R.id.tv_placeorderconfirm);
        tv_header = findViewById(R.id.tv_header);
        tv_deliaddress = findViewById(R.id.tv_deliaddress);
        tv_emptyshippingaddress = findViewById(R.id.tv_emptyshippingaddress);
        tv_choosedeliverydateandtime = findViewById(R.id.tv_choosedeliverydateandtime);
        tv_ordersummary = findViewById(R.id.tv_ordersummary);
        tv_paymenttype = findViewById(R.id.tv_paymenttype);

        ll_redeemsummary = findViewById(R.id.ll_redeemsummary);
        redeem_tv = findViewById(R.id.redeem_tv);
        redeem_tvamnt = findViewById(R.id.redeem_tvamnt);
        tv_Want_to_redeem = findViewById(R.id.tv_Want_to_redeem);
        tv_reddemurreward = findViewById(R.id.tv_reddemurreward);

        lnr_datetime = findViewById(R.id.lnr_datetime);
        lnr_redeem = findViewById(R.id.lnr_redeem);
        ll_ordersummary = findViewById(R.id.ll_ordersummary);
        ll_paymenttype = findViewById(R.id.ll_paymenttype);
        txt_payamount = findViewById(R.id.txt_payamount);
        txt_savedyou = findViewById(R.id.txt_savedyou);

        recyc_paymenttype = findViewById(R.id.recyc_paymenttype);

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
        subtotal=subtotal+floatdeliverycharge;
        yousaved=discount+memberdiscount;
        tv_subtotal.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(totalMRP))));
        tv_grandtotal.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        tv_amountpay.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        txt_payamount.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
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

            SharedPreferences youhavesaved = getApplicationContext().getSharedPreferences(Config.SHARED_PREF212, 0);
            txt_savedyou.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+/*string+" "+*/"0.00"+" )");
        }else{
            tv_discount.setText(/*string+*/" "+f.format(Double.parseDouble(String.valueOf(discount))));

            SharedPreferences youhavesaved = getApplicationContext().getSharedPreferences(Config.SHARED_PREF212, 0);
            txt_savedyou.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(discount)))+" )");
        }
        if(f.format(Double.parseDouble(String.valueOf(memberdiscount))).equals(".00")){
            tv_memberdiscount.setText(/*string+*/" 0.00");
        }else{
            tv_memberdiscount.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(memberdiscount))));}
        if(f.format(Double.parseDouble(String.valueOf(DeliveryCharge))).equals(".00")){
            tv_deliverycharges.setText(/*string+*/" 0.00");
        }else{
            tv_deliverycharges.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(DeliveryCharge))));}
        if(totalgst<=0){
            llGST.setVisibility(View.INVISIBLE);
        }else{
            llGST.setVisibility(View.VISIBLE);
            tv_gst.setVisibility(View.GONE);
        }
        SharedPreferences youhavesaved = getApplicationContext().getSharedPreferences(Config.SHARED_PREF212, 0);
        tv_savedamount.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");

//        tv_savedamount.setText("( You have saved "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");


    }

   /* private void setViews() {
        DecimalFormat f = new DecimalFormat("##.00");
        String string = "\u20B9";
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        subtotal = db.selectCartTotalActualPrice();
        totalgst = db.CartTotalGST();
        totalMRP = db.CartTotalMRP();
        totalRetailPrice = db.CartTotalRetailPrice();
        discount = totalMRP - totalRetailPrice;
        memberdiscount = totalRetailPrice - subtotal;
        aamountPay = subtotal + totalgst;
        yousaved = discount + memberdiscount;
        tv_Name.setText(username);

        SharedPreferences pref01 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF19, 0);
        String addressDel =pref01.getString("DeliAddress", null);

        if(addressDel.isEmpty()||addressDel==null){
            llAddress.setVisibility(View.GONE);
            addAddressLl.setVisibility(View.VISIBLE);
        }else{
            llAddress.setVisibility(View.VISIBLE);
            addAddressLl.setVisibility(View.GONE);
            SharedPreferences pref02 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF18, 0);
            String name =pref02.getString("DeliUsername", null);
            SharedPreferences pref03 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF21, 0);
            String pin =pref03.getString("DeliPincode", null);
            SharedPreferences pref04 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF25, 0);
            String mobNum =pref04.getString("DeliMobNumb", null);
            tv_address.setText(name +"\n"+addressDel+"\nMOB NUM : +91"+mobNum);
//        tv_address.setText(username +"\n"+address+"\nPIN : "+PIN +"\nMOB NUM : +91"+usermobile);
        }
*//*
        if (address.isEmpty() || address == null) {
            llAddress.setVisibility(View.GONE);
            btAdd.setVisibility(View.VISIBLE);
        } else {
            llAddress.setVisibility(View.VISIBLE);
            btAdd.setVisibility(View.GONE);
            tv_address.setText(address);
        }*//*
        //  tvPin.setText(PIN);
        tv_mobile.setText("+91" + usermobile);
        tv_subtotal.setText(string + " " + f.format(Double.parseDouble(String.valueOf(totalMRP))));
        tv_grandtotal.setText(string + " " + f.format(Double.parseDouble(String.valueOf(subtotal))));
        String value = String.valueOf(subtotal);
        String value1 = value.substring(value.lastIndexOf(".") + 1);
        char value2 = value1.charAt(0);
        int checkDecimal = Integer.parseInt(String.valueOf(value2));
        String[] twoStringArray = value.split("\\.", 2);
        if (checkDecimal >= 5) {
            int famount = Integer.parseInt(twoStringArray[0]);
            int roundvalue = famount + 1;
            finalamount = String.valueOf(roundvalue);
        } else {
            finalamount = twoStringArray[0];
        }
        tv_amountpay.setText(string + " " + finalamount + " /-");

        if (f.format(Double.parseDouble(String.valueOf(discount))).equals(".00")) {
            tv_discount.setText(string + " 0.00");
        } else {
            tv_discount.setText(string + " " + f.format(Double.parseDouble(String.valueOf(discount))));
        }
        if (f.format(Double.parseDouble(String.valueOf(memberdiscount))).equals(".00")) {
            tv_memberdiscount.setText(string + " 0.00");
        } else {
            tv_memberdiscount.setText(string + " " + f.format(Double.parseDouble(String.valueOf(memberdiscount))));
        }
        if (totalgst <= 0) {
            llGST.setVisibility(View.INVISIBLE);
        } else {
            llGST.setVisibility(View.VISIBLE);
            tv_gst.setVisibility(View.GONE);
        }
        tv_savedamount.setText("( You have saved " + string + " " + f.format(Double.parseDouble(String.valueOf(yousaved))) + " )");
    }*/

    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
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

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0) {
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
                            if (certs != null && certs.length > 0) {
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


    @Override
    public void onBackPressed() {
//        Intent in=new Intent(this, ReOrderCartActivity.class);
//        in.putExtra("ID_SalesOrder", ID_SalesOrder);
//        in.putExtra("order_id", order_id);
//        in.putExtra("deliveryDate", deliveryDate);
//        in.putExtra("Id_order", Id_order);
//        in.putExtra("orderDate", orderDate);
//        in.putExtra("status", status);
//        in.putExtra("ID_Store", ID_Store);
//        in.putExtra("ShopType", ShopType);
//        in.putExtra("itemcount", itemcount);
//        in.putExtra("ID_CustomerAddress", ID_CustomerAddress);
//        in.putExtra("OrderType", OrderType);
//        in.putExtra("storeName", storeName);
//        in.putExtra("DeliveryCharge",  DeliveryCharge);
//        in.putExtra("MinimumDeliveryAmount",  MinimumDeliveryAmount);
        Intent in = new Intent(this, ReOrderCartActivity.class);
        in.putExtra("ID_SalesOrder", order_id);
        in.putExtra("order_id", order_id);
        in.putExtra("OrderNo",OrderNo);
        in.putExtra("deliveryDate", deliveryDate);
        in.putExtra("Id_order", Id_order);
        in.putExtra("orderDate", orderDate);
        in.putExtra("status", status);
        in.putExtra("ID_Store", ID_Store);
        in.putExtra("ShopType", ShopType);
        in.putExtra("itemcount", itemcount);
        in.putExtra("ID_CustomerAddress", ID_CustomerAddress);
        in.putExtra("OrderType", OrderType);
        in.putExtra("storeName", storeName);
        in.putExtra("DeliveryCharge",  DeliveryCharge);
        in.putExtra("MinimumDeliveryAmount",  MinimumDeliveryAmount);
        startActivity(in);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im:
                //  drawer.openDrawer(Gravity.START);
                onBackPressed();
                break;
            case R.id.etdate:
                dateSelector();
                break;
            case R.id.ettime:
                timeSelector();
                break;
            case R.id.ettime1:
                timeSelector1();
                break;
            case R.id.etSearch:
                startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, SearchActivity.class));
                break;
            case R.id.imcart:
                Intent i = new Intent(CheckoutReorderHomeDeliveryActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.tvcart:
                Intent in = new Intent(CheckoutReorderHomeDeliveryActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.btchangepassword:
                changePassword();
                break;
            case R.id.btlogout:
                doLogout();
                break;
            case R.id.tv_notification:
                startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, NotificationActivity.class));
                break;
            case R.id.tvadrss:
               // startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, AddressListActivity.class));
                Intent intt = new Intent(CheckoutReorderHomeDeliveryActivity.this, AddressListActivity.class);
//                intt.putExtra("From", "reorder");
//                intt.putExtra("ID_Store", "ID_Store");
//                intt.putExtra("ShopType", "ShopType");
//                intt.putExtra("ID_CustomerAddress", "ID_CustomerAddress");
//                intt.putExtra("OrderType", "OrderType");
//                intt.putExtra("Image", "Image");
//                intt.putExtra("DeliveryCharge", "DeliveryCharge");
//                intt.putExtra("MinimumDeliveryAmount", "MinimumDeliveryAmount");

                intt.putExtra("Image", Image);

                intt.putExtra("From", "reorder");
                intt.putExtra("order_id", order_id);
                intt.putExtra("OrderNo",OrderNo);
                intt.putExtra("deliveryDate", deliveryDate);
                intt.putExtra("Id_order", Id_order);
                intt.putExtra("orderDate", orderDate);
                intt.putExtra("status", status);
                intt.putExtra("ID_Store", ID_Store);
                intt.putExtra("ShopType", ShopType);
                intt.putExtra("itemcount", itemcount);
                intt.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                intt.putExtra("OrderType", OrderType);
                intt.putExtra("storeName", storeName);
                intt.putExtra("DeliveryCharge", DeliveryCharge);
                intt.putExtra("MinimumDeliveryAmount", MinimumDeliveryAmount);
                startActivity(intt);
                break;
            case R.id.btEdit:
               // startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, AddressListActivity.class));
                Intent intnt = new Intent(CheckoutReorderHomeDeliveryActivity.this, AddressListActivity.class);
//                intnt.putExtra("From", "reorder");
//                intnt.putExtra("ID_Store", "ID_Store");
//                intnt.putExtra("ShopType", "ShopType");
//                intnt.putExtra("ID_CustomerAddress", "ID_CustomerAddress");
//                intnt.putExtra("OrderType", "OrderType");
//                intnt.putExtra("Image", "Image");
//                intnt.putExtra("DeliveryCharge", "DeliveryCharge");

                intnt.putExtra("Image", Image);
                intnt.putExtra("destination", fileimage);
                intnt.putExtra("From", "reorder");
                intnt.putExtra("order_id", order_id);
                intnt.putExtra("OrderNo",OrderNo);
                intnt.putExtra("deliveryDate", deliveryDate);
                intnt.putExtra("Id_order", Id_order);
                intnt.putExtra("orderDate", orderDate);
                intnt.putExtra("status", status);
                intnt.putExtra("ID_Store", ID_Store);
                intnt.putExtra("ShopType", ShopType);
                intnt.putExtra("itemcount", itemcount);
                intnt.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                intnt.putExtra("OrderType", OrderType);
                intnt.putExtra("storeName", storeName);
                intnt.putExtra("DeliveryCharge", DeliveryCharge);
                intnt.putExtra("MinimumDeliveryAmount", MinimumDeliveryAmount);
                startActivity(intnt);
                break;
            case R.id.btAdd:
               // startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, AddressListActivity.class));
                Intent intent = new Intent(CheckoutReorderHomeDeliveryActivity.this, AddressListActivity.class);
//                intent.putExtra("From", "reorder");
//                intent.putExtra("ID_Store", "ID_Store");
//                intent.putExtra("ShopType", "ShopType");
//                intent.putExtra("ID_CustomerAddress", "ID_CustomerAddress");
//                intent.putExtra("OrderType", "OrderType");
//                intent.putExtra("Image", "Image");
//                intent.putExtra("DeliveryCharge", "DeliveryCharge");


                intent.putExtra("Image", Image);
                intent.putExtra("destination", fileimage);
                intent.putExtra("From", "reorder");
                intent.putExtra("order_id", order_id);
                intent.putExtra("OrderNo",OrderNo);
                intent.putExtra("deliveryDate", deliveryDate);
                intent.putExtra("Id_order", Id_order);
                intent.putExtra("orderDate", orderDate);
                intent.putExtra("status", status);
                intent.putExtra("ID_Store", ID_Store);
                intent.putExtra("ShopType", ShopType);
                intent.putExtra("itemcount", itemcount);
                intent.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                intent.putExtra("OrderType", OrderType);
                intent.putExtra("storeName", storeName);
                intent.putExtra("DeliveryCharge", DeliveryCharge);
                intent.putExtra("MinimumDeliveryAmount", MinimumDeliveryAmount);
                startActivity(intent);
                break;
         /*   case R.id.ll_orderconfirm:
                if (tv_address.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Plaese Add valid Address", Toast.LENGTH_LONG).show();
                } else {
                    doOrderConfirm();
                }
                  break;
                  */


            case R.id.ll_orderconfirm:
                if (cbRedeem.isChecked()){
                    finalamount = String.valueOf(Double.parseDouble(finalamountSave)-Double.parseDouble(redeemamount));
                }else {
                    finalamount = finalamountSave;
                }
                getLocation();
                SharedPreferences pref9 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF61, 0);
                String strTimeSlotCheck=pref9.getString("TimeSlotCheckReorder", null);
                if(strTimeSlotCheck.equals("true")){
                    AsOnDateApplicableChecking(); }
                else{
                    Boolean checkBoxExpress = cbExpress.isChecked();
                    if(checkBoxExpress) {
                        inExpressdelivery=1;
                    }
                    else {
                        inExpressdelivery=0;
                    }
                    if (strLatitude.equals("") && strLongitude.equals("")){
                        Toast.makeText(getApplicationContext(),LocationNotFound,Toast.LENGTH_SHORT).show();
                    }else {
                        holidayCheck();
                    }
                }
                break;
            case R.id.tvtermsncondition:
                SharedPreferences availabilityofthestockpackaging = getApplicationContext().getSharedPreferences(Config.SHARED_PREF65, 0);
                String availabilityofthestockpack = (availabilityofthestockpackaging.getString("availabilityofthestockpackaging", ""));

                SharedPreferences termsandconditionssp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF192, 0);
                String termsandconditions = (termsandconditionssp.getString("termsandconditions", ""));

                AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
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

            case R.id.tv_choosedeliverydateandtime:
                if (flagDateTime == 0){
                    flagDateTime = 1;
                    lnr_datetime.setVisibility(View.GONE);
                    tv_choosedeliverydateandtime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                }else {
                    flagDateTime = 0;
                    lnr_datetime.setVisibility(View.VISIBLE);
                    tv_choosedeliverydateandtime.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);
                }

                break;

            case R.id.tv_reddemurreward:
                if (flagRedeem == 0){
                    flagRedeem = 1;
                    lnr_redeem.setVisibility(View.GONE);
                    tv_reddemurreward.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                }else {
                    flagRedeem = 0;
                    lnr_redeem.setVisibility(View.VISIBLE);
                    tv_reddemurreward.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);
                }

                break;

            case R.id.tv_ordersummary:
                if (flagOrderSummary == 0){
                    flagOrderSummary = 1;
                    ll_ordersummary.setVisibility(View.GONE);
                    tv_ordersummary.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                }else {
                    flagOrderSummary = 0;
                    ll_ordersummary.setVisibility(View.VISIBLE);
                    tv_ordersummary.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);
                }
                break;

            case R.id.tv_paymenttype:
                if (flagPayType == 0){
                    flagPayType = 1;
                    ll_paymenttype.setVisibility(View.GONE);
                    tv_paymenttype.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                }else {
                    flagPayType = 0;
                    ll_paymenttype.setVisibility(View.VISIBLE);
                    tv_paymenttype.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);
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
                    requestObject1.put("DeliveryType",0);
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

                                    if (ettime.getText().toString().isEmpty() && ettime1.getText().toString().isEmpty()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
                                        builder.setMessage(PleaseselectdeliveryStarttimeEndtime+".")
                                                .setCancelable(false)
                                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    } else if (!ettime.getText().toString().isEmpty() && ettime1.getText().toString().isEmpty()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
                                        builder.setMessage(Pleaseselectdeliveryendtime+".")
                                                .setCancelable(false)
                                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    } else if (ettime.getText().toString().isEmpty() && !ettime1.getText().toString().isEmpty()) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
                                        builder.setMessage(Pleaseselectdeliverystarttime+".")
                                                .setCancelable(false)
                                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    } else {


                                        Boolean checkBoxExpress = cbExpress.isChecked();
                                        if(checkBoxExpress) {
                                            inExpressdelivery=1;
                                        }
                                        else {
                                            inExpressdelivery=0;
                                        }
                                        holidayCheck();
                                    }


                                }else{
                                    SharedPreferences sorryonedayprocessdeliciousorderselectnextdaysp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF70, 0);
                                    String sorryonedayprocessdeliciousorderselectnextday = (sorryonedayprocessdeliciousorderselectnextdaysp.getString("sorryonedayprocessdeliciousorderselectnextday", ""));

                                    AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
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
//                    String OrderTypevalue;
//                    if (OrderType== "true"){
//                        OrderTypevalue = "1";
//                    }
//                    else{
//                        OrderTypevalue = "0";
//                    }
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
                            if(jObject.getString("StatusCode").equals("0")){
                                JSONObject jobj = jObject.getJSONObject("HolidayChecking");
                                String strHoliday=jobj.getString("IsHoliday");
                                if(strHoliday.equals("false")){
                                    Boolean checkBoxState = cbConfirm.isChecked();
                                    if(checkBoxState) {
                                        if(tv_address.getText().toString().length()<=0) {
//

                                            SharedPreferences PleaseAddvalidAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF310, 0);

                                            Toast.makeText(getApplicationContext(),PleaseAddvalidAddress.getString("PleaseAddvalidAddress", "")+".",Toast.LENGTH_LONG).show();
                                        }else {
                                            doOrderConfirm();
                                        }
                                    }
                                    else {
                                        SharedPreferences PleaseacceptTermsandConditionssp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF68, 0);
                                        String PleaseacceptTermsandConditions = (PleaseacceptTermsandConditionssp.getString("PleaseacceptTermsandConditions", ""));

                                        AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
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
                                    Toast.makeText(getApplicationContext(),HolidayPleaseselectotherdate+" !",Toast.LENGTH_LONG).show();
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

    public void dateSelector() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(CheckoutReorderHomeDeliveryActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        etdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        Calendar maxDate = Calendar.getInstance();
        maxDate.set(Calendar.DAY_OF_MONTH, mDay + (30));
        maxDate.set(Calendar.MONTH, mMonth);
        maxDate.set(Calendar.YEAR, mYear);
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.show();
    }

    public void timeSelector() {
  /*  TimePickerDialog timePickerDialog = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                    Calendar datetime = Calendar.getInstance();
                    Calendar c = Calendar.getInstance();
                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    datetime.set(Calendar.MINUTE, minute);
                        int hour = hourOfDay % 12;
                        String strDate;
                        strDate = String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                minute, hourOfDay < 12 ? "am" : "pm");
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                        ettime.setText(strDate);


                }
            }, mHour, mMinute, false);
    timePickerDialog.show();*/

        TimePickerDialog Tp = new TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int hour = hourOfDay % 12;
                String strDate;
                strDate = String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                        minute, hourOfDay < 12 ? "am" : "pm");
                SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                ettime.setText(strDate);

            }
        },mHour,mMinute,false);
        Tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tp.show();
    }

    public void timeSelector1() {
   /* TimePickerDialog timePickerDialog = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,
                                      int minute) {
                    Calendar datetime = Calendar.getInstance();
                    Calendar c = Calendar.getInstance();
                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    datetime.set(Calendar.MINUTE, minute);
                        int hour = hourOfDay % 12;
                        String strDate;
                        strDate = String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                                minute, hourOfDay < 12 ? "am" : "pm");
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                        ettime1.setText(strDate);


                }
            }, mHour, mMinute, false);
    timePickerDialog.show();*/

        TimePickerDialog Tp = new TimePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                int hour = hourOfDay % 12;
                String strDate;
                strDate = String.format("%02d:%02d %s", hour == 0 ? 12 : hour,
                        minute, hourOfDay < 12 ? "am" : "pm");
                SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
                ettime1.setText(strDate);

            }
        },mHour,mMinute,false);
        Tp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Tp.show();
    }

  /*  public   void getAddressList(){
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
                        .baseUrl(Config.BASEURL)
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(client)
                        .build();
                ApiInterface apiService = retrofit.create(ApiInterface.class);
                final JSONObject requestObject1 = new JSONObject();
                try {
                    requestObject1.put("ReqMode", "17");
                    requestObject1.put("FK_Customer", "22");
                    requestObject1.put("Bank_Key", "-500");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getCusAddress(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jmember = jObject.getJSONObject("CustomerAddressDetails");
                            String jstatus = jObject.getString("StatusCode");
                            JSONArray CustomerAddressList = jmember.getJSONArray("CustomerAddressList");
                            if(jstatus.equals("0")){

                                    getAddList(CustomerAddressList);
                            }
                            else{

                                Toast.makeText(getApplicationContext(), "Address List is Empty", Toast.LENGTH_SHORT).show();
                                *//*rv_favlist.setVisibility(View.GONE);
                                emptyFvrt.setVisibility(View.VISIBLE);*//*
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }

                    private void getAddList(JSONArray customerAddressList) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddressAddActivty.this);
                        LayoutInflater inflater1 = (LayoutInflater) AddressAddActivty.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater1.inflate(R.layout.addresslist_layout, null);
                        Button btnAddAddress = layout.findViewById(R.id.btnAddAddress);
                        btnAddAddress.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AddressAddActivty.this);
                                    LayoutInflater inflater1 = (LayoutInflater) AddressAddActivty.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View layout = inflater1.inflate(R.layout.add_edit_address_popup, null);
                                    final EditText landmark = (EditText) layout.findViewById(R.id.etLandmark);
                                    final EditText address = (EditText) layout.findViewById(R.id.etaddress);
                                    Button ok = (Button) layout.findViewById(R.id.btn_changesave);
                                    Button cancel = (Button) layout.findViewById(R.id.pop_changecancel);
                                    builder.setView(layout);
                                    final AlertDialog alertDialog = builder.create();
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
                                            addAddress(landmark.getText().toString(),address.getText().toString());
                                        }
                                    });
                                    alertDialog.show();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        RecyclerView mRcyListAddress = layout.findViewById(R.id.rv_favlist);
                        GridLayoutManager lLayout = new GridLayoutManager(getApplicationContext(),1);
                        mRcyListAddress.setLayoutManager(lLayout);
                        mRcyListAddress.setHasFixedSize(true);
                        mRcyListAddress.setItemAnimator(new DefaultItemAnimator());
                        AddressListAdapter adapter = new AddressListAdapter(getApplicationContext(), customerAddressList);
                        mRcyListAddress.setAdapter(adapter);
                        builder.setView(layout);
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.show();
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
    }*/

   /* private void doOrderConfirm() {
        if (new InternetUtil(this).isInternetOn()) {
            progressDialog = new ProgressDialog(this, R.style.Progress);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(this.getResources()
                    .getDrawable(R.drawable.progress));
            progressDialog.show();
            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .sslSocketFactory(getSSLSocketFactory())
                        .hostnameVerifier(getHostnameVerifier())
                        .build();
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(Config.BASEURL)
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
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String currenttime = sdf.format(cal.getTime());
                    String StrDeliveryDate;
                    String DeliveryTime;
                    if (!etdate.getText().toString().isEmpty()) {
                        String DeliveryDate = etdate.getText().toString();
                        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                        DateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
                        Date date = inputFormat.parse(DeliveryDate);
                        StrDeliveryDate = outputFormat.format(date);
                    } else {
                        StrDeliveryDate = currentdate;
                    }
                    if (!ettime.getText().toString().isEmpty() && !ettime1.getText().toString().isEmpty()) {
                        DeliveryTime = ettime.getText().toString() + ettime1.getText().toString();
                    } else if (!ettime.getText().toString().isEmpty() && ettime1.getText().toString().isEmpty()) {
                        DeliveryTime = ettime.getText().toString();
                    } else if (ettime.getText().toString().isEmpty() && !ettime1.getText().toString().isEmpty()) {
                        DeliveryTime = ettime1.getText().toString();
                    } else {
                        DeliveryTime = currenttime;
                    }
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF9, 0);
                    SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF17, 0);


                    requestObject1.put("ID_Customer", pref1.getString("userid", null));
                    requestObject1.put("OrderDate", currentdate);
                    requestObject1.put("DeliveryDate", StrDeliveryDate);
                    requestObject1.put("DeliveryTime", DeliveryTime);
                    requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                    JSONArray jsonArray = new JSONArray();
                    DBHandler db = new DBHandler(this);
                    Cursor cursor = db.select("cart");
                    int i = 0;
                    if (cursor.moveToFirst()) {
                        do {
                            JSONObject jsonObject1 = new JSONObject();
                            try {
                                jsonObject1.put("ID_Stock", cursor.getString(cursor.getColumnIndex("Stock_ID")));
                                jsonObject1.put("ID_Item", cursor.getString(cursor.getColumnIndex("ID_Items")));
                                jsonObject1.put("ItemName", cursor.getString(cursor.getColumnIndex("ItemName")));
                                jsonObject1.put("MRP", cursor.getString(cursor.getColumnIndex("MRP")));
                                jsonObject1.put("SalesPrice", cursor.getString(cursor.getColumnIndex("SalesPrice")));
                                jsonObject1.put("Quantity", cursor.getString(cursor.getColumnIndex("Count")));
                                jsonObject1.put("RetailPrice", cursor.getString(cursor.getColumnIndex("RetailPrice")));
                                jsonObject1.put("VAT", cursor.getString(cursor.getColumnIndex("GST")));
                                jsonObject1.put("Cess", cursor.getString(cursor.getColumnIndex("CESS")));
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
                    requestObject1.put("UserAction", "1");
                    requestObject1.put("ShopType", pref3.getString("ShopType", null));
                    requestObject1.put("DeliveryType", 1);
                    requestObject1.put("ID_CustomerAddress", pref4.getString("AddressID", null));
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.confirmOrder(body);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("SalesOrderDetails");
                            if (jObject.getString("StatusCode").equals("3")) {
                                Toast.makeText(getApplicationContext(), jobj.getString("ResponseMessage"), Toast.LENGTH_LONG).show();
                                DBHandler db = new DBHandler(CheckoutReorderHomeDeliveryActivity.this);
                                db.deleteallCart();
                                startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, ThanksActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Order Submitted Failed !", Toast.LENGTH_LONG).show();
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
        } else {
            Intent in = new Intent(this, NoInternetActivity.class);
            startActivity(in);
        }
    }*/

    private void doOrderConfirm(){
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
                    String currenttime= sdf.format(cal.getTime()) ;
                    String StrDeliveryDate;
                    String DeliveryTime;
                    if (!etdate.getText().toString().isEmpty()) {
                        String DeliveryDate = etdate.getText().toString();
                        DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                        DateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
                        Date date = inputFormat.parse(DeliveryDate);
                        StrDeliveryDate = outputFormat.format(date);
                    }else{
                        StrDeliveryDate =currentdate;
                    }
                    if(!ettime.getText().toString().isEmpty()&&!ettime1.getText().toString().isEmpty()){
                        DeliveryTime = ettime.getText().toString()+ettime1.getText().toString();
                    }
                    else if(!ettime.getText().toString().isEmpty()&&ettime1.getText().toString().isEmpty()){
                        DeliveryTime = ettime.getText().toString();
                    }
                    else if(ettime.getText().toString().isEmpty()&&!ettime1.getText().toString().isEmpty()){
                        DeliveryTime = ettime1.getText().toString();
                    }else{
                        DeliveryTime=currenttime;
                    }

                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    SharedPreferences pref3= getApplicationContext().getSharedPreferences(Config.SHARED_PREF9, 0);
                    requestObject1.put("ID_Customer", pref1.getString("userid", null));
                    requestObject1.put("OrderDate", currentdate);
                    requestObject1.put("DeliveryDate",StrDeliveryDate);
                    requestObject1.put("DeliveryTime", DeliveryTime);
                    requestObject1.put("ID_Store",ID_Store);
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
                    if (OrderType.equals("1")){
                        DeliveryType = 1;
                    }
                    else {
                        DeliveryType = 0;
                    }

                    Log.e(TAG,"OrderType  1965   "+DeliveryType);
                    requestObject1.put("Remarks", etremark.getText().toString());
                    requestObject1.put("Data", jsonArray);
                    requestObject1.put("UserAction", "1");
                    requestObject1.put("ShopType", ShopType);
                    requestObject1.put("DeliveryType", DeliveryType);
                    requestObject1.put("ID_CustomerAddress",ID_CustomerAddress);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    requestObject1.put("ExpressDelivery", inExpressdelivery);
                    requestObject1.put("SOLongitude", strLongitude);
                    requestObject1.put("SOLattitude", strLatitude);

                  /*  byte[] data = Base64.decode(Image, Base64.DEFAULT);
                    requestObject1.put("PrescriptionImage", data);*/
                    if(Image==null||Image.isEmpty()||Image.equals("")||Image.length()<=0) {}else{
                       /* byte[] data = Base64.decode(Image, Base64.DEFAULT);
                        requestObject1.put("PrescriptionImage", data);*/
//                        requestObject1.put("PrescriptionImage", Image);
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

                    Log.e(TAG,"requestObject1   1675   "+requestObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("jsondattaa","fileimage  1995   "+fileimage);
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
//                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
//                Call<String> call = apiService.confirmOrder(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG,"onResponse   1685   "+response.body());
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("SalesOrderDetails");
                            if(jObject.getString("StatusCode").equals("3")){
                                Toast.makeText(getApplicationContext(), jobj.getString("ResponseMessage"),Toast.LENGTH_LONG).show();
                                DBHandler db=new DBHandler(CheckoutReorderHomeDeliveryActivity.this);
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


//                                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
//                                Intent intent = new Intent(CheckoutReorderHomeDeliveryActivity.this,ThanksActivity.class);
//                                intent.putExtra("StoreName", pref1.getString("StoreName", null));
//                                intent.putExtra("OrderNumber",jobj.getString("OrderNumber"));
//                                intent.putExtra("strPaymenttype","COD");
//                                intent.putExtra("finalamount",finalamount);
//                                startActivity(intent);
//
//                                //  startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, ThanksActivity.class));
//                                finish();


                                //                                0 cash
//                                ra 1
//                                2 Bill
//                                3 BillPlus
//                                5 google pay

//                                if(strPaymenttype.equals("COD")){
                                if(strPaymentId.equals("1")){
//                                    startActivity(new Intent(AddressAddActivty.this, ThanksActivity.class));
//                                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
//                                    Intent intent = new Intent(AddressAddActivty.this,ThanksActivity.class);
//                                    intent.putExtra("StoreName", pref1.getString("StoreName", null));
//                                    intent.putExtra("OrderNumber",jobj.getString("OrderNumber"));
//                                    intent.putExtra("strPaymenttype",strPaymenttype);
//                                    intent.putExtra("finalamount",finalamount);
//
//
//                                    startActivity(intent);
//
//                                    finish();

                                    updatePayments(OrderNumber_s,FK_SalesOrder,strPaymentId,"","0","","0",finalamount,"0");
                                }
                                // else if(strPaymenttype.equals("ONLINE")){
                                else if(strPaymentId.equals("1")){




                                    try {
                                        //   doOrderConfirm();
                                        startrazerPayment("perfect solution", "demo testing", 1, "perfectsolution@gmail.com", "9497093212");
                                    }catch (Exception e){

                                        AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
                                        builder.setMessage(ThereissometechnicalissuesPleaseuseanotherpaymentoptions+". ")
                                                .setCancelable(false)
                                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }
//                                else if(strPaymenttype.equals("UPI")){
                                else if(strPaymentId.equals("5")){
                                    try {
                                        //  doOrderConfirm();
                                        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                        SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF59, 0);
                                        payUsingUpi(pref1.getString("StoreName", null), pref6.getString("UPIID", null), "Order Payment", ""+finalamount);
                                    }catch (Exception e){
                                        AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
                                        // builder.setTitle("Terms & Conditions");
                                        builder.setMessage(PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdevice+". ")
                                                .setCancelable(false)
                                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }
//                                else if (strPaymenttype.equals("PAYU Biz")) {
                                else if(strPaymentId.equals("10")){
                                    try {
//                                    startPayubiz(finalamount);
                                    }catch (Exception e){
                                        AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
                                        // builder.setTitle("Terms & Conditions");
                                        builder.setMessage(PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdevice+". ")
                                                .setCancelable(false)
                                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }
//                                else if (strPaymenttype.equals("Bill Desk")) {
                                else if (strPaymentId.equals("2")) {
                                    try {
                                        // orderConfirmation();
                                        startBillDesk(finalamount);
                                    }catch (Exception e){
                                        AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
                                        // builder.setTitle("Terms & Conditions");
                                        builder.setMessage(PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdevice+". ")
                                                .setCancelable(false)
                                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }
                            }
                            else if(jObject.getString("StatusCode").equals("-12")){
                                AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
                                builder.setMessage(jobj.getString("ResponseMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, AddressListActivity.class));
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else if(jObject.getString("StatusCode").equals("-13")){
                                AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
                                builder.setMessage(jobj.getString("ResponseMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else if(jObject.getString("StatusCode").equals("10")){
                                AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
                                builder.setMessage(jobj.getString("ResponseMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent i = new Intent(CheckoutReorderHomeDeliveryActivity.this, ReOrderCartActivity.class);
//                                                i.putExtra("From", "Home");

                                                i.putExtra("ID_SalesOrder", ID_SalesOrder);
                                                i.putExtra("order_id", order_id);
                                                i.putExtra("deliveryDate", deliveryDate);
                                                i.putExtra("Id_order", Id_order);
                                                i.putExtra("orderDate", orderDate);
                                                i.putExtra("status", status);
                                                i.putExtra("ID_Store", ID_Store);
                                                i.putExtra("ShopType", ShopType);
                                                i.putExtra("itemcount", itemcount);
                                                i.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                                                i.putExtra("OrderType", OrderType);
                                                i.putExtra("storeName", storeName);
                                                i.putExtra("DeliveryCharge",  DeliveryCharge);
                                                i.putExtra("MinimumDeliveryAmount",  MinimumDeliveryAmount);
                                                startActivity(i);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else{
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

    private void changePassword() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) CheckoutReorderHomeDeliveryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.change_password_popup, null);

            SharedPreferences changepassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF222, 0);
            TextView tv_popupchange = layout.findViewById(R.id.tv_popupchange);
            tv_popupchange.setText(changepassword.getString("changepassword", null));

            SharedPreferences oldpasswordsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF225, 0);
            final EditText oldpassword = (EditText) layout.findViewById(R.id.oldpassword);
            oldpassword.setHint(oldpasswordsp.getString("oldpassword", null));

            SharedPreferences newpasswordsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF226, 0);
            final EditText newpassword = (EditText) layout.findViewById(R.id.newpassword);
            newpassword.setHint(newpasswordsp.getString("newpassword", null));

            SharedPreferences Cancelsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF105, 0);
            Button pop_changecancel = (Button) layout.findViewById(R.id.pop_changecancel);
            pop_changecancel.setText(Cancelsp.getString("Cancel", null));

            SharedPreferences savesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF227, 0);
            Button btn_changesave = (Button) layout.findViewById(R.id.btn_changesave);
            btn_changesave.setText(savesp.getString("save", null));

            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            pop_changecancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            btn_changesave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (oldpassword.getText().toString().isEmpty()) {
                        //oldpassword.setError("Please provide your Old password.");

                        SharedPreferences EnterOldPasswordsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF353, 0);
                        String EnterOldPassword=EnterOldPasswordsp.getString("EnterOldPassword", null);
                        Toast.makeText(getApplicationContext(), EnterOldPassword+".",Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), "Enter Old Password", Toast.LENGTH_LONG).show();

                    } else if (newpassword.getText().toString().isEmpty()) {
                        //newpassword.setError("Please provide your New Password.");

                        SharedPreferences EnterNewPasswordsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF347, 0);
                        String EnterNewPassword =EnterNewPasswordsp.getString("EnterOldPassword", null);
                        Toast.makeText(getApplicationContext(), EnterNewPassword+".",Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), "Enter New Password", Toast.LENGTH_LONG).show();
                    } else {
                        st_oldpassword = oldpassword.getText().toString();
                        st_newpassword = newpassword.getText().toString();
                        doChangePassword();
                    }
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
        gpsTracker = new GpsTracker(CheckoutReorderHomeDeliveryActivity.this);
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

//    private void showGPSDisabledAlertToUser() {
//
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
//                .setCancelable(false)
//                .setPositiveButton("Goto Settings Page To Enable GPS",
//                        new DialogInterface.OnClickListener(){
//                            public void onClick(DialogInterface dialog, int id){
//                                Intent callGPSSettingIntent = new Intent(
//                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                                startActivity(callGPSSettingIntent);
//                            }
//                        });
//        alertDialogBuilder.setNegativeButton("Cancel",
//                new DialogInterface.OnClickListener(){
//                    public void onClick(DialogInterface dialog, int id){
//                        dialog.cancel();
//                    }
//                });
//        AlertDialog alert = alertDialogBuilder.create();
//        alert.show();
//    }

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

    private void getMerchantKeys() {
        strPaymentId = "1";
        SharedPreferences OnlinePaymentpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF62, 0);
        String value = OnlinePaymentpref.getString("OnlinePaymentMethods", null);
        Log.e(TAG,"OnlinePaymentpref   2293    "+value);
        try {
            JSONArray jsonArray = new JSONArray(value);
            for(int i=0; i<=jsonArray.length(); i++) {
                JSONObject jobjt = jsonArray.getJSONObject(i);
                String id  = jobjt.getString("ID_PaymentMethod");
                String PaymentName = jobjt.getString("PaymentName");

                Log.e("paymentdata ","   485    "+PaymentName);
                Log.e("TransactionID ","   485    "+jobjt.getInt("TransactionID"));
                Log.e("MerchantID ","   485    "+jobjt.getString("MerchantID"));
                Log.e("SecurityID ","   485    "+jobjt.getString("SecurityID"));
                MerchantID = null;
                TransactionID = null;
                SecurityID = null;


                Log.e(TAG,"PaymentName   50111 "+i+"      "+PaymentName+"    "+strPaymenttype);
//                if (PaymentName.equals("UPI")){
                if (PaymentName.equals(strPaymenttype)){
                    MerchantID =  jobjt.getString("MerchantID");
                    TransactionID =  jobjt.getString("TransactionID");
                    SecurityID =  jobjt.getString("SecurityID");
                    strPaymentId = jobjt.getString("ID_PaymentMethod");
                    Log.e(TAG,"strPaymentId   5291       "+strPaymentId+"    "+jobjt.getString("ID_PaymentMethod")+"        "+strPaymenttype);
                }
//                if (PaymentName.equals("PayU")){
                if (PaymentName.equals(strPaymenttype)){
                    MerchantID =  jobjt.getString("MerchantID");
                    TransactionID =  jobjt.getString("TransactionID");
                    SecurityID =  jobjt.getString("SecurityID");
                    strPaymentId = jobjt.getString("ID_PaymentMethod");
                    Log.e(TAG,"strPaymentId   5292       "+strPaymentId+"    "+jobjt.getString("ID_PaymentMethod")+"        "+strPaymenttype);
                }
//                if (PaymentName.equals("Razorpay")){
                if (PaymentName.equals(strPaymenttype)){
                    MerchantID =  jobjt.getString("MerchantID");
                    TransactionID =  jobjt.getString("TransactionID");
                    SecurityID =  jobjt.getString("SecurityID");
                    strPaymentId = jobjt.getString("ID_PaymentMethod");
                    Log.e(TAG,"strPaymentId   5294       "+strPaymentId+"    "+jobjt.getString("ID_PaymentMethod")+"        "+strPaymenttype);
                }
//                if (PaymentName.equals("Bill Desk")){
                if (PaymentName.equals(strPaymenttype)){
                    MerchantID =  jobjt.getString("MerchantID");
                    TransactionID =  jobjt.getString("TransactionID");
                    SecurityID =  jobjt.getString("SecurityID");
                    strPaymentId = jobjt.getString("ID_PaymentMethod");
                    Log.e(TAG,"strPaymentId   5295       "+strPaymentId+"    "+jobjt.getString("ID_PaymentMethod")+"        "+strPaymenttype);
                }
                if (strPaymenttype.equals("COD")){
                    MerchantID =  "";
                    TransactionID =  "";
                    SecurityID =  "";
                    strPaymentId = "1";
                    Log.e(TAG,"strPaymentId   5295       "+strPaymentId+"    "+jobjt.getString("ID_PaymentMethod")+"        "+strPaymenttype);
                }


            }



        } catch (Exception e) {
            Log.e(TAG,"Exception   23221    "+e.toString());
        }

        Log.e(TAG,"strPaymentId   529       "+strPaymentId+"    "+strPaymenttype);
    }

    @Override
    public void onClick(int position, String paymentName) {

        Log.e(TAG,"4616  "+position+ "  "+paymentName);

        try {
            JSONObject jsonObject = jsonArrayPay.getJSONObject(position);
            strPaymenttype=jsonObject.getString("PaymentName");
            IsOnlinePay = "true";
            getMerchantKeys();
        } catch (JSONException e) {
            e.printStackTrace();
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
                                startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, ThanksActivity.class));
                                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                Intent intent = new Intent(CheckoutReorderHomeDeliveryActivity.this,ThanksActivity.class);
                                intent.putExtra("StoreName", pref1.getString("StoreName", null));
                                intent.putExtra("OrderNumber",jobj.getString("OrderNumber"));
                                intent.putExtra("strPaymenttype",strPaymenttype);
                                intent.putExtra("finalamount",finalamount);
//
//
                                startActivity(intent);

                                finish();


                            }else {
                                AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
                                builder.setMessage(jobj.getString("ResponseMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, HomeActivity.class));
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



    public void startrazerPayment(String companyname,String discription,int amount,String email,String contactnumber){
        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", companyname);
            options.put("description", discription);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            int amt = amount*100;
            options.put("currency", "INR");
            options.put("amount", amt);

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", contactnumber);

            options.put("prefill", preFill);

            co.open(activity, options);


        } catch (Exception e) {
            SharedPreferences Errorinpayment = getApplicationContext().getSharedPreferences(Config.SHARED_PREF328, 0);

            Toast.makeText(activity, Errorinpayment.getString("Errorinpayment","")+" : " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }


    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            //holidayCheck();
            Toast.makeText(this, PaymentSuccessfully+": " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, ThanksActivity.class));
            SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
            Intent intent = new Intent(CheckoutReorderHomeDeliveryActivity.this,ThanksActivity.class);
            intent.putExtra("StoreName", pref1.getString("StoreName", null));
            intent.putExtra("OrderNumber",OrderNumber_s);
            intent.putExtra("FK_SalesOrder",FK_SalesOrder);
            intent.putExtra("strPaymenttype",strPaymenttype);
            intent.putExtra("finalamount",finalamount);


            startActivity(intent);

            finish();

        } catch (Exception e) {
            Log.e("error in razor pay", "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, Paymentfailed+": " + code + " " + response, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e) {
            Log.e("error in razor pay", "Exception in onPaymentError", e);
        }
    }


    public void payUsingUpi(  String name,String upiId, String note, String amount) {

        Random rnd = new Random();
        String transNumber  = String.valueOf(rnd.nextInt(999999));
        Log.e(TAG,"transNumber    2553   "+transNumber);
        Uri uri =
                new Uri.Builder()
                        .scheme("upi")
                        .authority("pay")
                        .appendQueryParameter("pa",upiId)
                        .appendQueryParameter("pn", name)
                        .appendQueryParameter("mc", "")
                        .appendQueryParameter("tr", transNumber)
                        .appendQueryParameter("tn", note)
                        .appendQueryParameter("am", amount)
                        .appendQueryParameter("cu", "INR")
//                        .appendQueryParameter("url", "your-transaction-url")
                        .build();
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setData(uri);
//        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
//        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        Intent chooser = Intent.createChooser(upiPayIntent,"Choose Pay");

        if (null != chooser.resolveActivity(getPackageManager())){
            startActivityForResult(chooser,UPI_PAYMENT);
        }else {
            Toast.makeText(getApplicationContext(),"No UPI App found,Please install to continue",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG,"resultCode   2692       "+resultCode+"   "+requestCode);
        Log.e(TAG, "data 2692      "+data );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {

            case 0:


                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                }
                if ((RESULT_OK == resultCode) || (resultCode == 202)) {
                    if (data != null) {

                        Log.e("UPI", "onActivityResult: 2040   " + data);

                    } else {
                        Log.e("UPI", "onActivityResult: 2043    " + data);
                    }
                }

                else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
            case 201:
                Log.e(TAG,"requestCode   2692       "+resultCode+"   "+requestCode);

                if (requestCode == 201) {

                    Log.e(TAG, "data    : 26921    " + data);

//                    Bundle bundle = data.getExtras();
//                    Log.e(TAG, "bundle    : 2692    " + bundle.getString("status").toString().split("\\|"));
//                    Log.e(TAG, "result    : 2692    " + bundle.getString("status"));
                    if (data != null) {
                        Log.e(TAG, "status: 2727    " +  data.getStringExtra("status"));


                    } else {
                        Log.e("UPI", "onActivityResult: 2043    " + data);
                    }
                }else{

                }
                break;
        }

        Log.e(TAG,"resultCode   2061   "+resultCode+"   "+requestCode);
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(CheckoutReorderHomeDeliveryActivity.this)) {
            String str = data.get(0);
            Log.e(TAG,"OrderNumber_s      2737      "+OrderNumber_s);
            Log.e(TAG,"FK_SalesOrder      2737      "+FK_SalesOrder);
            Log.e("UPIPAY", "upiPaymentDataOperation1: " + data);
            Log.e("UPIPAY", "upiPaymentDataOperation2: " + str);
            String[] separated = str. split("&");

            String[] septxnId = separated[0]. split("=");
            String[] septxnRef = separated[1]. split("=");
            String[] sepStatus = separated[2]. split("=");
            String[] sepresponseCode = separated[3]. split("=");

            Log.e("septxnId", "upiPaymentDataOperation31: " + septxnId[1]);
            Log.e("septxnRef", "upiPaymentDataOperation32: " + septxnRef[1]);
            Log.e("sepStatus", "upiPaymentDataOperation33: " + sepStatus[1]);
            Log.e("sepresponseCode", "upiPaymentDataOperation33: " + sepresponseCode[1]);


            String paymentCancel = "";
            if (str == null) str = "discard";

            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }
            SharedPreferences Transactionsuccessful = getApplicationContext().getSharedPreferences(Config.SHARED_PREF325, 0);
            SharedPreferences Paymentcancelledbyuser = getApplicationContext().getSharedPreferences(Config.SHARED_PREF326, 0);
            SharedPreferences TransactionfailedPleasetryagain = getApplicationContext().getSharedPreferences(Config.SHARED_PREF327, 0);


            if (status.equals("success")) {
                //Code to handle successful transaction here.
                // holidayCheck();
//
//                Toast.makeText(AddressAddActivty.this, Transactionsuccessful.getString("Transactionsuccessful", "")+".", Toast.LENGTH_SHORT).show();
//                Log.e("UPI", "payment successfull: " + approvalRefNo);
//                startActivity(new Intent(AddressAddActivty.this, ThanksActivity.class));
//                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
//                Intent intent = new Intent(AddressAddActivty.this,ThanksActivity.class);
//                intent.putExtra("StoreName", pref1.getString("StoreName", null));
//                intent.putExtra("OrderNumber",OrderNumber_s);
//                intent.putExtra("FK_SalesOrder",FK_SalesOrder);
//                intent.putExtra("strPaymenttype",strPaymenttype);
//                intent.putExtra("finalamount",finalamount);
//
//
//                startActivity(intent);
//
//                finish();

                updatePayments(OrderNumber_s,FK_SalesOrder,strPaymentId,septxnId[1],"0","","0",finalamount,"0");
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(CheckoutReorderHomeDeliveryActivity.this, Paymentcancelledbyuser.getString("Paymentcancelledbyuser","")+".", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
                AlertMessage(Paymentcancelledbyuser.getString("Paymentcancelledbyuser",""));

            } else {
                Toast.makeText(CheckoutReorderHomeDeliveryActivity.this, TransactionfailedPleasetryagain.getString("TransactionfailedPleasetryagain","")+".", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
                AlertMessage(TransactionfailedPleasetryagain.getString("TransactionfailedPleasetryagain",""));
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            SharedPreferences Nointernetconnection = getApplicationContext().getSharedPreferences(Config.SHARED_PREF282, 0);
            Toast.makeText(CheckoutReorderHomeDeliveryActivity.this, Nointernetconnection.getString("Nointernetconnection",""), Toast.LENGTH_SHORT).show();
        }
    }

    private void AlertMessage(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutReorderHomeDeliveryActivity.this);
        builder.setMessage(""+msg)
                .setCancelable(false)
                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(CheckoutReorderHomeDeliveryActivity.this, HomeActivity.class));
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void startBillDesk(String finalamount) {




        try {

            DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
            Calendar cal = Calendar.getInstance();
            String currentdate = dateFormat.format(cal.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
            String currenttime= sdf.format(cal.getTime()) ;
            String StrDeliveryDate;
            String DeliveryTime;
            if (!etdate.getText().toString().isEmpty()) {
                String DeliveryDate = etdate.getText().toString();
                DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
                Date date = inputFormat.parse(DeliveryDate);
                StrDeliveryDate = outputFormat.format(date);
            }else{
                StrDeliveryDate =currentdate;
            }
            if(!ettime.getText().toString().isEmpty()&&!ettime1.getText().toString().isEmpty()){
                DeliveryTime = ettime.getText().toString()+ettime1.getText().toString();
            }
            else if(!ettime.getText().toString().isEmpty()&&ettime1.getText().toString().isEmpty()){
                DeliveryTime = ettime.getText().toString();
            }
            else if(ettime.getText().toString().isEmpty()&&!ettime1.getText().toString().isEmpty()){
                DeliveryTime = ettime1.getText().toString();
            }else{
                DeliveryTime=currenttime;
            }

            String strRemark =  etremark.getText().toString();

//            inExpressdelivery,strLongitude,strLatitude

//            requestObject1.put("ExpressDelivery", inExpressdelivery);
//            requestObject1.put("SOLongitude", strLongitude);
//            requestObject1.put("SOLattitude", strLatitude);

            SharedPreferences ShareStrDeliveryDate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF373, 0);
            SharedPreferences.Editor StrDeliveryDateeditor = ShareStrDeliveryDate.edit();
            StrDeliveryDateeditor.putString("StrDeliveryDate", StrDeliveryDate);
            StrDeliveryDateeditor.commit();

            SharedPreferences ShareDeliveryTime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF374, 0);
            SharedPreferences.Editor DeliveryTimeeditor = ShareDeliveryTime.edit();
            DeliveryTimeeditor.putString("DeliveryTime", DeliveryTime);
            DeliveryTimeeditor.commit();


            SharedPreferences SharestrRemark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF375, 0);
            SharedPreferences.Editor strRemarkeditor = SharestrRemark.edit();
            strRemarkeditor.putString("strRemark", strRemark);
            strRemarkeditor.commit();

            SharedPreferences ShareinExpressdelivery = getApplicationContext().getSharedPreferences(Config.SHARED_PREF376, 0);
            SharedPreferences.Editor inExpressdeliveryeditor = ShareinExpressdelivery.edit();
            inExpressdeliveryeditor.putInt("inExpressdelivery", inExpressdelivery);
            inExpressdeliveryeditor.commit();

            SharedPreferences SharestrLongitude = getApplicationContext().getSharedPreferences(Config.SHARED_PREF377, 0);
            SharedPreferences.Editor strLongitudeeditor = SharestrLongitude.edit();
            strLongitudeeditor.putString("strLongitude", strLongitude);
            strLongitudeeditor.commit();

            SharedPreferences SharestrLatitude = getApplicationContext().getSharedPreferences(Config.SHARED_PREF378, 0);
            SharedPreferences.Editor strLatitudeeditor = SharestrLatitude.edit();
            strLatitudeeditor.putString("strLatitude", strLatitude);
            strLatitudeeditor.commit();

            SharedPreferences ConfirmOrderNo = getApplicationContext().getSharedPreferences(Config.SHARED_PREF402, 0);
            SharedPreferences.Editor ConfirmOrderNoeditor = ConfirmOrderNo.edit();
            ConfirmOrderNoeditor.putString("ConfirmOrderNo", OrderNumber_s);
            ConfirmOrderNoeditor.commit();

            SharedPreferences prefstrPaymentId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF403, 0);
            SharedPreferences.Editor prefstrPaymentIdeditor = prefstrPaymentId.edit();
            prefstrPaymentIdeditor.putString("strPaymentId", strPaymentId);
            prefstrPaymentIdeditor.commit();

            SharedPreferences prefFK_SalesOrder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF404, 0);
            SharedPreferences.Editor prefFK_SalesOrdereditor = prefFK_SalesOrder.edit();
            prefFK_SalesOrdereditor.putString("FK_SalesOrder", FK_SalesOrder);
            prefFK_SalesOrdereditor.commit();


            if (fileimage !=null){
                SharedPreferences Sharesfileimage = getApplicationContext().getSharedPreferences(Config.SHARED_PREF379, 0);
                SharedPreferences.Editor fileimageeditor = Sharesfileimage.edit();
                fileimageeditor.putString("fileimage", fileimage.toString());
                fileimageeditor.commit();
            }else {
                SharedPreferences Sharesfileimage = getApplicationContext().getSharedPreferences(Config.SHARED_PREF379, 0);
                SharedPreferences.Editor fileimageeditor = Sharesfileimage.edit();
                fileimageeditor.putString("fileimage", "");
                fileimageeditor.commit();
            }

            Log.e(TAG,"1640      strPaymenttype  "+strPaymenttype+"     finalamount      "+finalamount);

            SharedPreferences SharesstrPaymenttype = getApplicationContext().getSharedPreferences(Config.SHARED_PREF380, 0);
            SharedPreferences.Editor strPaymenttypeeditor = SharesstrPaymenttype.edit();
            strPaymenttypeeditor.putString("strPaymenttype", strPaymenttype);
            strPaymenttypeeditor.commit();

            SharedPreferences Sharesfinalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF381, 0);
            SharedPreferences.Editor finalamounteditor = Sharesfinalamount.edit();
            finalamounteditor.putString("finalamount", finalamount);
            finalamounteditor.commit();

            SharedPreferences redeemAmount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF387, 0);
            SharedPreferences.Editor redeemAmounteditor = redeemAmount.edit();
            redeemAmounteditor.putString("redeemAmount", redeemamount);
            redeemAmounteditor.commit();

//            if (MerchantID.equals("null")){
//                Log.e(TAG,"MerchantID   1614   "+MerchantID);
//            }else {
//                VerifyChecksum();
//            }

            Log.e("TransactionID ","   1622    "+TransactionID);
            Log.e("MerchantID ","   1622    "+MerchantID);
            Log.e("SecurityID ","   1622    "+SecurityID);
            VerifyChecksum();


////            String strPGMsg = "BDSKUATY|ARP10234|NA|2.00|NA|NA|NA|INR|NA|R|bdskuaty|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|http://www.domain.com/response.jsp|892409133";
//            String strPGMsg = "|ARP1553593909862|NA|2|NA|NA|NA|INR|NA|R|airmtst|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|https://uat.billdesk.com/pgidsk/pgmerc/pg_dump.jsp|892409133";
//            String strTokenMsg = null;
//            SampleCallBack objSampleCallBack = new SampleCallBack();
//AIRMTST
//            Intent sdkIntent = new Intent(this, PaymentOptions.class);
//            sdkIntent.putExtra("msg",strPGMsg);
//            if(strTokenMsg != null && strTokenMsg.length() > strPGMsg.length()) {
//
//                sdkIntent.putExtra("token",strTokenMsg);
//            }
//            sdkIntent.putExtra("user-email","test@bd.com");
//            sdkIntent.putExtra("user-mobile","9800000000");
//            sdkIntent.putExtra("callback",objSampleCallBack);
//
//            startActivity(sdkIntent);


        }catch (Exception e){

            Log.e(TAG,"Exception   37   "+e.toString());
        }



//        startActivityForResult(sdkIntent,202);

//        Intent i = new Intent(this,TestActivity.class);
//        startActivityForResult(i,202);

    }

    private void VerifyChecksum() {
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

                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);

                    // requestObject1.put("TransactionID", "2563");
//                    requestObject1.put("MerchantID", "AIRMTST");
//                    requestObject1.put("Amount","2");
//                    requestObject1.put("SecurityID", "airmtst");


                    SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF4, 0);
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);

                    requestObject1.put("MerchantID", Utils.encryptStart(MerchantID));
                    requestObject1.put("Amount",Utils.encryptStart(finalamount));
                    requestObject1.put("SecurityID", Utils.encryptStart(SecurityID));
                    requestObject1.put("MobileNumber", Utils.encryptStart(pref4.getString("userphoneno", null)));
                    requestObject1.put("UserID", Utils.encryptStart(pref1.getString("userid", null)));
                    requestObject1.put("CustName", Utils.encryptStart(pref3.getString("username", null)));
                    requestObject1.put("StoreName", Utils.encryptStart(pref5.getString("StoreName", null)));

                    requestObject1.put("Fk_PaymentMethod", Utils.encryptStart(strPaymentId));
                    requestObject1.put("ID_SalesOrder", Utils.encryptStart(FK_SalesOrder));
//
//                    Log.e(TAG,"requestObject1   1681    "+strPaymentId);
                    Log.e(TAG,"requestObject1   1680    "+requestObject1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getVerifychecksum(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG,"response   1680    "+response.body());
                            JSONObject jObject = new JSONObject(response.body());

                            if(jObject.getString("StatusCode").equals("0")){
                                JSONObject jobj = jObject.getJSONObject("AccountTransferStatus");
                                Log.e(TAG,"msg   1697   "+jobj.getString("msg"));

//                                String strPGMsg = "BDSKUATY|BDSK1004|NA|2|NA|NA|NA|INR|NA|R|bdskuaty|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|http://localhost:51953/Home/PaymentGateWayResult|984BABE1F1214D9DED3FA8151D7C002EC675E44A376D16580E4805D119776FDB";
                                // String strPGMsg = "AIRMTST|ARP1553593909862|NA|2|NA|NA|NA|INR|NA|R|airmtst|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|https://uat.billdesk.com/pgidsk/pgmerc/pg_dump.jsp|892409133";
                                String strPGMsg = jobj.getString("msg");
                                String strTokenMsg = null;
                                SampleCallBack objSampleCallBack = new SampleCallBack();
                                Intent sdkIntent = new Intent(CheckoutReorderHomeDeliveryActivity.this, PaymentOptions.class);
                                sdkIntent.putExtra("msg",strPGMsg);
                                if(strTokenMsg != null && strTokenMsg.length() > strPGMsg.length()) {

                                    sdkIntent.putExtra("token",strTokenMsg);
                                }
                                SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF3, 0);
                                // String useremail=pref3.getString("useremail", null);
                                SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF4, 0);

                                sdkIntent.putExtra("user-email",pref3.getString("useremail", null));
                                sdkIntent.putExtra("user-mobile",pref4.getString("userphoneno", null));
                                sdkIntent.putExtra("callback",objSampleCallBack);

                                startActivity(sdkIntent);
                                //  startActivityForResult(sdkIntent,201);

                            }else{
                                JSONObject jobj = jObject.getJSONObject("AccountTransferStatus");
                                Log.e(TAG,"msg   1697   "+jobj.getString("msg"));
                                Toast.makeText(getApplicationContext(),jobj.getString("ResponseMessage")+ "!!",Toast.LENGTH_LONG).show();
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

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
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
                    radioButton.setText(""+jsonObject.getString("PaymentName"));
                }

            }



        } catch (Exception e) {
            Log.e(TAG,"Exception   2322    "+e.toString());
        }
    }

}