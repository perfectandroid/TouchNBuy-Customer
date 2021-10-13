package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class CheckoutShoppinglistAddressAddActivty extends AppCompatActivity implements View.OnClickListener{
    ProgressDialog progressDialog;
    int inExpressdelivery=0;
    EditText etSearch, etremark;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvuser, tvPin,tvcart,tv_mobile,tv_Name, tv_address,tv_subtotal, tv_amountpay, btlogout, btchangepassword, tv_discount, tv_memberdiscount, tv_gst, tv_savedamount, tvitemcount, tv_notification, tvtermsncondition,tv_grandtotal;
    DBHandler db;
    Button btnEdit, proceed, btnAddAddress;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ImageView selectTime, selectDate;
    float subtotal, totalgst, aamountPay, totalMRP, totalRetailPrice, discount, memberdiscount, yousaved;
    String usermobile, finalamount,address,PIN,username, st_oldpassword, st_newpassword,userid,date,time;
    LinearLayout ll_orderconfirm, ll_check, llGST, llAddress, addAddressLl, ll_express;
    RelativeLayout rl_notification;
    CheckBox cbConfirm, cbExpress;
    int OrderBookingPeriod;
    Date d1, d2, d3, d4, d5 ;
    Button btAdd;
    EditText etdate,ettime, ettime1;
    TextView tvstorenote, tvexpressdeliveryamount;
    String PrescriptionImage;
    String TAG = "CheckoutShoppinglistAddressAddActivty";

    String strPaymenttype="", strTimeSlotCheck, LocationNotFound, GPSisEnabledinyourdevice, HolidayPleaseselectotherdate ;
    String  OK , Somethingwentwrong,  ThereissometechnicalissuesPleaseuseanotherpaymentoptions, PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdevice, Pleaseselectanypaymentoption, PleaseselectdeliveryStarttimeEndtime, Pleaseselectdeliveryendtime, Pleaseselectdeliverystarttime;
    private File fileimage = null;
    String StoreName_s,OrderNumber_s;
    String FK_SalesOrder;

    TextView tv_addaddresshead,tv_deliaddresshead,tv_Want_to_redeem;
    TextView tv_choosedeliverydateandtime,tv_reddemurreward,tv_paymenttype;
    LinearLayout lnr_datetime,lnr_redeem,ll_paymenttype,ll_redeem;

    int flagDateTime =0;
    int flagRedeem = 0;
    int flagOrderSummary = 0;
    int flagPayType = 0;

    String IsOnlinePay = "false";
    String RedeemRequest = "false";
    CheckBox cbRedeem;
    TextView tv_header,tvaddcart;
    String Pc_PrivilageCardEnable = "false";
    String Pc_AccNumber = "";
    String Pc_ID_CustomerAcc = "0";
    String privilegeamount ="0";
    String strPaymentId = "1";
    RadioButton radioButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_shopping_address_add_activty);

        try{



        Intent in = getIntent();
//        if (getIntent().getStringExtra("destination")!=null) {
            if ((File)in.getExtras().get("destination")!=null) {

            PrescriptionImage = in.getStringExtra("Image");
            Log.e(TAG, "PrescriptionImage  123   " + PrescriptionImage);
            Log.e(TAG, "PrescriptionImage  120   " + in.getStringExtra("destination"));
//            fileimage = new File(in.getStringExtra("destination"));
                fileimage = (File)in.getExtras().get("destination");
            Log.e(TAG, "PrescriptionImage  124   " + fileimage);
        }
        initiateViews();
        setRegister();
        setHomeNavMenu1();

        Log.e(TAG,"Start   123");

            SharedPreferences prefFK_SalesOrderNew = getApplicationContext().getSharedPreferences(Config.SHARED_PREF405, 0);
            SharedPreferences.Editor prefFK_SalesOrdereditorNew = prefFK_SalesOrderNew.edit();
            prefFK_SalesOrdereditorNew.putString("FK_SalesOrder_new", "0");
            prefFK_SalesOrdereditorNew.commit();

       /* SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/
        getSharedPreferences();
            SharedPreferences prefFK_SalesOrder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF404, 0);
            SharedPreferences.Editor prefFK_SalesOrdereditor = prefFK_SalesOrder.edit();
            prefFK_SalesOrdereditor.putString("FK_SalesOrder", "0");

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
        ImageView imApplogo=findViewById(R.id.imApplogo);
        String strImagepath= IMAGEURL + pref3.getString("AppIconImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath).into(imApplogo);
       /* Bundle  bundle = getIntent().getExtras();
        assert bundle != null;
        date = *//*bundle.getString("order_date","")*//*"2/04/20";
        time = *//*bundle.getString("order_time","")*//*"12:30";*/

        SharedPreferences Subtotalsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF162, 0);
        String Subtotal = Subtotalsp.getString("Subtotal", "");
        SharedPreferences itemsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF140, 0);
        String item = itemsp.getString("item", "");
        SharedPreferences Itemssp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF163, 0);
        String Items = Itemssp.getString("Items", "");
        db=new DBHandler(this);
        if(db.selectCartCount()<=1) {
            tvitemcount.setText(Subtotal+" (" + String.valueOf(db.selectCartCount()) + " "+item+")");
        }else {
            tvitemcount.setText(Subtotal + " (" + String.valueOf(db.selectCartCount()) + " " + Items + ")");
        }
        setViews();
        paymentcondition();
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Calendar cal = Calendar.getInstance();
        String currentdate = dateFormat.format(cal.getTime());
        etdate.setText(currentdate);



        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF39, 0);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF40, 0);
        Log.e(TAG,"186    "+pref1.getString("ExpressDelivery", null));
        if(pref1.getString("ExpressDelivery", null).equals("true")) {
            cbExpress.setVisibility(View.VISIBLE);
            tvexpressdeliveryamount.setVisibility(View.VISIBLE);
            SharedPreferences extraamountfordelivery = getApplicationContext().getSharedPreferences(Config.SHARED_PREF215, 0);
            tvexpressdeliveryamount.setText("[ "+extraamountfordelivery.getString("extraamountfordelivery", "")+" :  "+pref.getString("ExpressDeliveryAmount", null)+"/- ]");
            Log.e(TAG,"1861    "+extraamountfordelivery.getString("extraamountfordelivery", "")+" :  "+pref.getString("ExpressDeliveryAmount", null));

        }else{
            tvexpressdeliveryamount.setVisibility(View.GONE);
            cbExpress.setVisibility(View.GONE);
        }

        }catch (Exception e){
            Log.e(TAG,"Exception  197   "+e.toString());
        }

        SharedPreferences ScratchCar = getApplicationContext().getSharedPreferences(Config.SHARED_PREF382, 0);
        if (ScratchCar.getString("ScratchCard",null).equals("true")){
           // getGiftVoucher();
            ll_redeem.setVisibility(View.VISIBLE);
        }else{
            ll_redeem.setVisibility(View.GONE);
        }
        cbRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbRedeem.isChecked()){
                    RedeemRequest = "true";
                }
                else {
                    RedeemRequest = "false";

                }
            }
        });



    }
    private void getSharedPreferences() {
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
        userid=pref1.getString("userid", null);
        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        username=pref2.getString("username", null);
        /*SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF3, 0);
        useremail=pref3.getString("useremail", null);*/
        SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF4, 0);
        usermobile=pref4.getString("userphoneno", null);
        /*SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
        memberid=pref5.getString("memberid", null);*/
        SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF15, 0);
        address=pref6.getString("Address", null);
        SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF16, 0);
        PIN=pref7.getString("PIN", null);
        SharedPreferences pref8 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF30, 0);
        tvstorenote.setText(pref8.getString("DeliveryCriteria", null));



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

        SharedPreferences Deliveryaddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF201, 0);
        tv_deliaddresshead.setText(""+Deliveryaddress.getString("Deliveryaddress",""));

        tv_addaddresshead.setText("Select an address to place the order");

        btAdd.setText("ADD NEW");

        SharedPreferences ChangeAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF260, 0);
        btnEdit.setText(""+ChangeAddress.getString("ChangeAddress",""));

        SharedPreferences choosedeliverydateandtime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF205, 0);
        tv_choosedeliverydateandtime.setText(choosedeliverydateandtime.getString("choosedeliverydateandtime", ""));

        SharedPreferences RedeemYourRewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF396, 0);
        tv_reddemurreward.setText(""+RedeemYourRewards.getString("RedeemYourRewards",""));

        SharedPreferences paymenttype = getApplicationContext().getSharedPreferences(Config.SHARED_PREF213, 0);
        tv_paymenttype.setText(paymenttype.getString("paymenttype", ""));

        SharedPreferences selectdate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF206, 0);
        etdate.setHint(selectdate.getString("selectdate", ""));
        SharedPreferences starttime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF207, 0);
        ettime.setHint(starttime.getString("starttime", ""));
        SharedPreferences endtime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF208, 0);
        ettime1.setHint(endtime.getString("endtime", ""));
        SharedPreferences remarks = getApplicationContext().getSharedPreferences(Config.SHARED_PREF209, 0);
        etremark.setHint(remarks.getString("remarks", ""));

        SharedPreferences Doyouwanttoreddemfromyourrewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF393, 0);
        tv_Want_to_redeem.setText(""+Doyouwanttoreddemfromyourrewards.getString("Doyouwanttoreddemfromyourrewards",""));

        SharedPreferences placeorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF217, 0);
        tvaddcart.setText(placeorder.getString("placeorder", ""));
        tv_header.setText(placeorder.getString("placeorder", ""));


    }
    private void setHomeNavMenu() {
        final String[] menulist = new String[]{"Home","My Cart", "My Orders", "Favourites","Favourite Stores",
                "Notifications", "Shopping List","About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,R.drawable.ic_store,
                R.drawable.ic_notifications,R.drawable.ic_list,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(CheckoutShoppinglistAddressAddActivty.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(CheckoutShoppinglistAddressAddActivty.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 7) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, AboutUsActivity.class));
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
        final String[] menulist = new String[]{"Home","My Cart", "My Orders", "Favourites",
                "Notifications", "About Us", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(CheckoutShoppinglistAddressAddActivty.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(CheckoutShoppinglistAddressAddActivty.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 5) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, AboutUsActivity.class));
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
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 9) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutShoppinglistAddressAddActivty.this);
                        LayoutInflater inflater1 = (LayoutInflater) CheckoutShoppinglistAddressAddActivty.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 11) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutShoppinglistAddressAddActivty.this);
            LayoutInflater inflater1 = (LayoutInflater) CheckoutShoppinglistAddressAddActivty.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.logout_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);
            TextView tv_popupdelete= (TextView) layout.findViewById(R.id.tv_popupdelete);
            TextView tv_popupdeleteinfo3= (TextView) layout.findViewById(R.id.tv_popupdeleteinfo3);
            TextView edit= (TextView) layout.findViewById(R.id.edit);
            TextView tv_no= (TextView) layout.findViewById(R.id.tv_no);



            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();

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

                        startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this,LocationActivity.class));
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
                    requestObject1.put("ReqMode", "4");
                    requestObject1.put("OldPassword", st_oldpassword);
                    requestObject1.put("NewPassword", st_newpassword);
                    requestObject1.put("ID_Customer", userid);
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getChangePassword(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jmember = jObject.getJSONObject("UserNameAndPasswordChk");
                            String ResponseCode = jmember.getString("ResponseCode");
                            if(ResponseCode.equals("0")){
                                Toast.makeText(getApplicationContext(), jmember.getString("ResponseMessage"),Toast.LENGTH_LONG).show();
                            }
                            if (ResponseCode.equals("-1")){
                                Toast.makeText(getApplicationContext(), jmember.getString("ResponseMessage"),Toast.LENGTH_LONG).show();
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
            }  }else {
            Intent in = new Intent(this,NoInternetActivity.class);
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

        tv_choosedeliverydateandtime.setOnClickListener(this);
        tv_reddemurreward.setOnClickListener(this);
        tv_paymenttype.setOnClickListener(this);
    }

    private void initiateViews() {

        tv_header = (TextView)findViewById(R.id.tv_header);
        tvaddcart = (TextView)findViewById(R.id.tvaddcart);
        etSearch=(EditText)findViewById(R.id.etSearch);
        etremark=(EditText)findViewById(R.id.etremark);
        etdate=(EditText)findViewById(R.id.etdate);
        ettime=(EditText)findViewById(R.id.ettime);
        ettime1=(EditText)findViewById(R.id.ettime1);
        etdate.setKeyListener(null);
        ettime.setKeyListener(null);
        ettime1.setKeyListener(null);
        imcart=(ImageView) findViewById(R.id.imcart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        btnEdit = (Button) findViewById(R.id.btEdit);
        im = (ImageView) findViewById(R.id.im);
        tvstorenote = (TextView)findViewById(R.id.tvstorenote);
        tvPin = (TextView)findViewById(R.id.tvPin);
        tv_mobile = (TextView)findViewById(R.id.tvMobile);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        tv_address = (TextView) findViewById(R.id.tvadrss);
        btAdd = (Button) findViewById(R.id.btAdd);
        tv_subtotal = (TextView) findViewById(R.id.tv_subtotal);
        tv_amountpay = (TextView) findViewById(R.id.tv_amountpay);
        tv_discount = (TextView) findViewById(R.id.tv_discount);
        tv_memberdiscount = (TextView) findViewById(R.id.tv_memberdiscount);
        tv_gst = (TextView) findViewById(R.id.tv_gst);
        tv_Name = (TextView)findViewById(R.id.tvName);
        tv_savedamount = (TextView) findViewById(R.id.tv_savedamount);
        tvitemcount = (TextView) findViewById(R.id.tvitemcount);
        tvtermsncondition = (TextView) findViewById(R.id.tvtermsncondition);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        ll_orderconfirm = (LinearLayout) findViewById(R.id.ll_orderconfirm);
        ll_check = (LinearLayout) findViewById(R.id.ll_check);
        ll_express = (LinearLayout) findViewById(R.id.ll_express);
        llGST = (LinearLayout) findViewById(R.id.llGST);
        llAddress = (LinearLayout) findViewById(R.id.llAddress);
        selectDate= findViewById(R.id.select_date);
        selectTime = findViewById(R.id.select_time);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        tv_grandtotal = (TextView) findViewById(R.id.tv_grandtotal);
        tvexpressdeliveryamount = (TextView) findViewById(R.id.tvexpressdeliveryamount);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        cbConfirm = (CheckBox) findViewById(R.id.cbConfirm);
        cbExpress = (CheckBox) findViewById(R.id.cbExpress);
        addAddressLl =  findViewById(R.id.addAddressLl);

        tv_addaddresshead = findViewById(R.id.tv_addaddresshead);
        tv_deliaddresshead = findViewById(R.id.tv_deliaddresshead);

        tv_reddemurreward = findViewById(R.id.tv_reddemurreward);
        tv_choosedeliverydateandtime = findViewById(R.id.tv_choosedeliverydateandtime);
        tv_paymenttype = findViewById(R.id.tv_paymenttype);

        lnr_datetime = findViewById(R.id.lnr_datetime);
        lnr_redeem = findViewById(R.id.lnr_redeem);
        ll_paymenttype= findViewById(R.id.ll_paymenttype);
        ll_redeem= findViewById(R.id.ll_redeem);

        cbRedeem = findViewById(R.id.cbRedeem);
        tv_Want_to_redeem = findViewById(R.id.tv_Want_to_redeem);

        radioButton = findViewById(R.id.radioButton);



    }

    private void setViews() {
        DecimalFormat f = new DecimalFormat("##.00");
        String string = "\u20B9";
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {e.printStackTrace();}
        subtotal=db.selectCartTotalActualPrice();
        totalgst=db.CartTotalGST();
        totalMRP=db.CartTotalMRP();
        totalRetailPrice=db.CartTotalRetailPrice();
        discount=totalMRP-totalRetailPrice;
        memberdiscount=   totalRetailPrice-subtotal;
        aamountPay=subtotal+totalgst;
        yousaved=discount+memberdiscount;
        tv_Name.setText(username);

        SharedPreferences pref01 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF19, 0);
        String addressDel =pref01.getString("DeliAddress", null);

        if(addressDel.isEmpty()||addressDel==null){
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
   /*     if(address.isEmpty()||address==null){
            llAddress.setVisibility(View.GONE);
            btAdd.setVisibility(View.VISIBLE);
        }else{
            llAddress.setVisibility(View.VISIBLE);
            btAdd.setVisibility(View.GONE);
        tv_address.setText(address);
        }*/
      //  tvPin.setText(PIN);
        tv_mobile.setText("+91"+usermobile);
        tv_subtotal.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(totalMRP))));
        tv_grandtotal.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        String value=String.valueOf(subtotal);
        String value1=value.substring(value.lastIndexOf(".") + 1);
        char value2 = value1.charAt(0);
        int checkDecimal=Integer.parseInt(String.valueOf(value2));
        String [] twoStringArray= value.split("\\.", 2);
        if(checkDecimal>=5){
            int famount=Integer.parseInt(twoStringArray[0]);
            int roundvalue=famount+1;
            finalamount= String.valueOf(roundvalue);
        }else{
            finalamount =twoStringArray[0];
        }
        tv_amountpay.setText(/*string+" "+*/finalamount+" /-");

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
        tv_savedamount.setText("( You have saved "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");
    }
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.im:

                //  drawer.openDrawer(Gravity.START);
                onBackPressed();                break;
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
                startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, SearchActivity.class));
                break;
            case R.id.imcart:
                Intent i = new Intent(CheckoutShoppinglistAddressAddActivty.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.tvcart:
                Intent in = new Intent(CheckoutShoppinglistAddressAddActivty.this, CartActivity.class);
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
                startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, NotificationActivity.class));
                break;
            case R.id.tvadrss:
                startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, AddressListActivity.class));
                break;
           /* case R.id.btEdit:
                startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, AddressListActivity.class));
                break;
            case R.id.btAdd:
                startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, AddressListActivity.class));
                break;*/
            case R.id.btEdit:
                Log.e(TAG, "PrescriptionImage  831   " + fileimage);
                Intent inten = new Intent(CheckoutShoppinglistAddressAddActivty.this, AddressListActivity.class);
                inten.putExtra("From", "shopping");
                inten.putExtra("Image", PrescriptionImage);
                inten.putExtra("destination", fileimage);
                startActivity(inten);
                break;
            case R.id.btAdd:
                Intent inte = new Intent(CheckoutShoppinglistAddressAddActivty.this, AddressListActivity.class);
                inte.putExtra("From", "shopping");
                inte.putExtra("Image", PrescriptionImage);
                inte.putExtra("destination", fileimage);
                startActivity(inte);
                break;
            case R.id.ll_orderconfirm:
                Boolean checkBoxExpress = cbExpress.isChecked();
                if(checkBoxExpress) {
                    inExpressdelivery=1;
                }
                else {
                    inExpressdelivery=0;
                }
                holidayCheck();
             /*   Boolean checkBoxState = cbConfirm.isChecked();
                if(checkBoxState) {
                    if(tv_address.getText().toString().length()<=0) {
                        Toast.makeText(getApplicationContext(),"Plaese Add valid Address",Toast.LENGTH_LONG).show();
                    }else {
                        doOrderConfirm();
                    }
                }
                else {
                    AlertDialog.Builder builder= new AlertDialog.Builder(AddressAddActivty.this);
                    builder.setMessage("Please accept Terms & Conditions.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }*/
                break;
            case R.id.tvtermsncondition:

                SharedPreferences availabilityofthestockpackaging = getApplicationContext().getSharedPreferences(Config.SHARED_PREF65, 0);
                String availabilityofthestockpack = (availabilityofthestockpackaging.getString("availabilityofthestockpackaging", ""));

                SharedPreferences termsandconditionssp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF192, 0);
                String termsandconditions = (termsandconditionssp.getString("termsandconditions", ""));

                AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutShoppinglistAddressAddActivty.this);
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

    public void dateSelector(){
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(CheckoutShoppinglistAddressAddActivty.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        etdate.setText(dayOfMonth + "-" + (monthOfYear + 1)+ "-" + year);
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
    private void doOrderConfirm(){
        if (new InternetUtil(this).isInternetOn()) {
            SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
            SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
            String BASEURL = baseurlpref.getString("BaseURL", null);
            String IMAGEURL = imgpref.getString("ImageURL", null);
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
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                    SharedPreferences pref3= getApplicationContext().getSharedPreferences(Config.SHARED_PREF9, 0);
                    SharedPreferences pref4= getApplicationContext().getSharedPreferences(Config.SHARED_PREF20, 0);

                    requestObject1.put("ID_Customer", pref1.getString("userid", null));
                    requestObject1.put("OrderDate", currentdate);
                    requestObject1.put("DeliveryDate",StrDeliveryDate);
                    requestObject1.put("DeliveryTime", DeliveryTime);
                    requestObject1.put("Remarks", etremark.getText().toString());
                    requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("CheckDate", 1);
                    requestObject1.put("UserAction", "1");
                    requestObject1.put("ShopType", pref3.getString("ShopType", null));
                    requestObject1.put("DeliveryType", 1);
                    requestObject1.put("ID_CustomerAddress", pref4.getString("DeliAddressID", null));
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    requestObject1.put("ExpressDelivery", inExpressdelivery);
                    JSONArray jsonArray = new JSONArray();
                    requestObject1.put("Data", jsonArray);
                   /* byte[] data = Base64.decode(PrescriptionImage, Base64.DEFAULT);
                    requestObject1.put("PrescriptionImage", data);*/
//                    if(PrescriptionImage==null||PrescriptionImage.isEmpty()||PrescriptionImage.equals("")||PrescriptionImage.length()<=0) {}else{
//                       /* byte[] data = Base64.decode(PrescriptionImage, Base64.DEFAULT);
//                        requestObject1.put("PrescriptionImage", data);*/
//                        requestObject1.put("PrescriptionImage", PrescriptionImage);
//                    }

                    requestObject1.put("PrescriptionImage", "");
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                    requestObject1.put("Redeem","0");

                    SharedPreferences prefFK_SalesOrderNew = getApplicationContext().getSharedPreferences(Config.SHARED_PREF405, 0);

                    requestObject1.put("IsOnlinePay",IsOnlinePay);
                    requestObject1.put("ID_SalesOrder",prefFK_SalesOrderNew.getString("FK_SalesOrder_new","0"));
                    requestObject1.put("RedeemRequest",RedeemRequest);

                    requestObject1.put("PrivilageCardEnable",Pc_PrivilageCardEnable);
                    requestObject1.put("PrivCardAmount",privilegeamount);
                    requestObject1.put("AccNumber",Pc_AccNumber);
                    requestObject1.put("ID_CustomerAcc",Pc_ID_CustomerAcc);

                    Log.e(TAG,"requestObject1       "+requestObject1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e(TAG,"requestObject1   1042   "+requestObject1);
                Log.e(TAG,"fileimage  1043   "+fileimage);

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
                    Log.e("jsondattaa","BODY  1079 "+body);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
                    imageFiles = MultipartBody.Part.createFormData("JsonData", "", requestFile);
                    call = apiService.getSalesOrderUpdateImgFile(body,imageFiles);

                }


                //crete
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileimage);
//            MultipartBody.Part body = MultipartBody.Part.createFormData("ImgFile", fileimage.getName(), requestFile);
//            RequestBody JsonData = RequestBody.create(MediaType.parse("text/plain"), requestObject1.toString());
//            Call<String> call = apiService.getUploadMedicalItems(JsonData,body);

//                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileimage);
//                MultipartBody.Part imageFiles = MultipartBody.Part.createFormData("JsonData", fileimage.getName(), requestFile);
//                RequestBody body = RequestBody.create(MediaType.parse("text/plain"), requestObject1.toString());
//                //   RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
//                Log.e("jsondattaa","BODY  1054 "+body);
//
//
//                Call<String> call = apiService.getUploadMedicalItemsImgFile(body,imageFiles);

//                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
//                Log.e(TAG,"body    1047  "+body  );
//                Call<String> call = apiService.confirmOrder(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            Log.e(TAG,"response  1104  "+response.body());
                            if(jObject.getString("StatusCode").equals("3")){
                                JSONObject jobj = jObject.getJSONObject("SalesOrderDetails");
                                Toast.makeText(getApplicationContext(), jobj.getString("ResponseMessage"),Toast.LENGTH_LONG).show();
                                DBHandler db=new DBHandler(CheckoutShoppinglistAddressAddActivty.this);
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
//                                Intent intent = new Intent(CheckoutShoppinglistAddressAddActivty.this,ThanksActivity.class);
//                                intent.putExtra("StoreName", pref1.getString("StoreName", null));
//                                intent.putExtra("OrderNumber",jobj.getString("OrderNumber"));
//                                intent.putExtra("strPaymenttype","COD");
//                                intent.putExtra("finalamount",finalamount);
//                                startActivity(intent);
//
//                                //  startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, ThanksActivity.class));
//                                finish();

                                updatePayments(OrderNumber_s,FK_SalesOrder,strPaymentId,"","0","","0",finalamount,"0");

                            }else if(jObject.getString("StatusCode").equals("10")){
                                JSONObject jobj = jObject.getJSONObject("SalesOrderDetails");
                                AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutShoppinglistAddressAddActivty.this);
                                builder.setMessage(jobj.getString("ResponseMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                Intent i = new Intent(CheckoutShoppinglistAddressAddActivty.this, CartActivity.class);
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
                    requestObject1.put("DeliveryType","1");
                    requestObject1.put("FK_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getHolidayChecking(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
/*
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jmember = jObject.getJSONObject("HolidayChecking");
                            String ResponseCode = jmember.getString("ResponseCode");
*/
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("HolidayChecking");
                            if(jObject.getString("StatusCode").equals("0")){
                                String strHoliday=jobj.getString("IsHoliday");
                            if(strHoliday.equals("false")){
                                    Boolean checkBoxState = cbConfirm.isChecked();
                                    if(checkBoxState) {
                                        if(tv_address.getText().toString().length()<=0) {
                                            SharedPreferences PleaseAddvalidAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF310, 0);
                                            Toast.makeText(getApplicationContext(),PleaseAddvalidAddress.getString("PleaseAddvalidAddress", "")+".",Toast.LENGTH_LONG).show();
                                        }else {
                                            doOrderConfirm();
                                        }
                                    }
                                    else {
                                        SharedPreferences PleaseacceptTermsandConditionssp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF68, 0);
                                        String PleaseacceptTermsandConditions = (PleaseacceptTermsandConditionssp.getString("PleaseacceptTermsandConditions", ""));

                                        AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutShoppinglistAddressAddActivty.this);
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
                                Toast.makeText(getApplicationContext(),HolidayPleaseselectotherdate+"!",Toast.LENGTH_LONG).show();
//                                Toast.makeText(getApplicationContext(),"Your selected date is a Holiday, Please select any other date!",Toast.LENGTH_LONG).show();
                                }


                            }else{
                                Toast.makeText(getApplicationContext(),Somethingwentwrong+"!!",Toast.LENGTH_LONG).show();
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



    private void changePassword(){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutShoppinglistAddressAddActivty.this);
            LayoutInflater inflater1 = (LayoutInflater) CheckoutShoppinglistAddressAddActivty.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.change_password_popup, null);
            final EditText oldpassword = (EditText) layout.findViewById(R.id.oldpassword);
            final EditText newpassword = (EditText) layout.findViewById(R.id.newpassword);
            Button pop_changecancel = (Button) layout.findViewById(R.id.pop_changecancel);
            Button btn_changesave = (Button) layout.findViewById(R.id.btn_changesave);
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
                        Toast.makeText(getApplicationContext(), "Enter Old Password",Toast.LENGTH_LONG).show();

                    } else if (newpassword.getText().toString().isEmpty()) {
                        //newpassword.setError("Please provide your New Password.");

                        SharedPreferences EnterNewPasswordsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF347, 0);
                        String EnterNewPassword =EnterNewPasswordsp.getString("EnterOldPassword", null);
                        Toast.makeText(getApplicationContext(), EnterNewPassword+".",Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), "Enter New Password",Toast.LENGTH_LONG).show();
                    }   else {
                        st_oldpassword=oldpassword.getText().toString();
                        st_newpassword=newpassword.getText().toString();
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
                                startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, ThanksActivity.class));
                                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                Intent intent = new Intent(CheckoutShoppinglistAddressAddActivty.this,ThanksActivity.class);
                                intent.putExtra("StoreName", pref1.getString("StoreName", null));
                                intent.putExtra("OrderNumber",jobj.getString("OrderNumber"));
                                intent.putExtra("strPaymenttype",strPaymenttype);
                                intent.putExtra("finalamount",finalamount);
//
//
                                startActivity(intent);

                                finish();


                            }else {
                                AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutShoppinglistAddressAddActivty.this);
                                builder.setMessage(jobj.getString("ResponseMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(new Intent(CheckoutShoppinglistAddressAddActivty.this, HomeActivity.class));
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
