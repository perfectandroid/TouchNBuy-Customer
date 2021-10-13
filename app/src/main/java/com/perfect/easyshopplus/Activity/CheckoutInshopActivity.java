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
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.perfect.easyshopplus.Adapter.AdapterPaymentOptions;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
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

public class CheckoutInshopActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    String TAG= "CheckoutInshopActivity";
    ProgressDialog progressDialog;
    EditText etSearch, etremark;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvuser, tvcart, tvdate, tvtime, tv_subtotal, tv_amountpay, tv_discount, tv_memberdiscount, tv_gst, tv_savedamount, tvitemcount, tv_notification, tv_grandtotal;
    DBHandler db;
    float subtotal, totalgst, aamountPay, totalMRP, totalRetailPrice, discount, memberdiscount, yousaved;
    LinearLayout ll_orderconfirm, ll_check, llGST;
    RelativeLayout rl_notification;
    CheckBox cbConfirm;
    String strDate, strTime, finalamount, OK;
    TextView discount_tv,tv_header, memberdiscount_tv, othercharges_tv, GrandTotal_tv, amountpayable_tv, tv_placeorderconfirm;

//    String TAG = "CheckoutInshopActivity";
//    BottomNavigationView navigation;
//    String fromCategory = "from";
//    String valueCategory = "home";
//    String fromCart = "From";
//    String valueCart = "Home";
//    String fromHome = "";
//    String valueHome = "";
//    String fromFavorite = "From";
//    String valueFavorite = "Home";

    CheckBox cbRedeem;
    Button bt_apply;
    LinearLayout ll_redeem,ll_check_redeem,ll_balance_red;
    TextView tv_your_reward;
    EditText et_your_redeem;
    Double rewardString = 0.0;
    String redeemamount ="0",finalamountSave;

    String StoreName_s,OrderNumber_s;
    String FK_SalesOrder;

    String IsOnlinePay = "false";
    String RedeemRequest = "false";
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
        setContentView(R.layout.activity_checkout_inshop_main);
        initiateViews();
        setRegViews();
        setHomeNavMenu1();
      //  setBottomBar();

       /* SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/

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

        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        SharedPreferences Subtotalsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF162, 0);
        String Subtotal = Subtotalsp.getString("Subtotal", "");
        SharedPreferences itemsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF140, 0);
        String item = itemsp.getString("item", "");
        SharedPreferences Itemssp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF163, 0);
        String Items = Itemssp.getString("Items", "");

        tvuser.setText("Welcome "+pref2.getString("username", null));
        db=new DBHandler(this);
        tvcart.setText(String.valueOf(db.selectInshopCartCount()));
        if(db.selectCartCount()<=1) {
            tvitemcount.setText(Subtotal+" (" + String.valueOf(db.selectInshopCartCount()) + " "+item+")");
        }else{
            tvitemcount.setText(Subtotal+" (" + String.valueOf(db.selectInshopCartCount()) + " "+Items+")");
        }
        setViews();
        getCurrentdatentime();
        paymentcondition();
        tvtime.setText("Order confirmation at "+strTime);
        tvdate.setText("Order confirmation on "+strDate);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));
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


        SharedPreferences placeorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF217, 0);
        tv_placeorderconfirm.setText(placeorder.getString("placeorder", ""));
        tv_header.setText(placeorder.getString("placeorder", ""));

        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        OK = (OKsp.getString("OK", ""));

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
                    ll_check_redeem.setVisibility(View.VISIBLE);
                    et_your_redeem.setText("");
                    tv_amountpay.setText(/*string+" "+*/finalamountSave+" /-");
                    redeemamount = "0";
                }
                else {
                    ll_check_redeem.setVisibility(View.GONE);
                    redeemamount = "0";
                    tv_amountpay.setText(/*string+" "+*/finalamountSave+" /-");
                }
            }
        });

        bt_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (et_your_redeem.getText().length() != 0 ){
                    try {
                        if (rewardString >= Double.parseDouble(et_your_redeem.getText().toString())){

                            redeemamount = String.valueOf(Double.parseDouble(et_your_redeem.getText().toString()));
                            String finalamountnew = String.valueOf(Double.parseDouble(finalamountSave) - Double.parseDouble(et_your_redeem.getText().toString()));
                            tv_amountpay.setText(/*string+" "+*/finalamountnew+" /-");
                            Log.e(TAG,"4201   finalamountnew       "+finalamountnew);
                        }else {
                            Log.e(TAG,"Exception  42028   Check Amount");
                            Toast.makeText(getApplicationContext(),"Check Amount",Toast.LENGTH_SHORT).show();
                            redeemamount  = "0";
                            tv_amountpay.setText(/*string+" "+*/finalamountSave+" /-");
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
                                tv_your_reward.setText("Your Reward : "+ rewardAmount);
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
        etSearch=(EditText)findViewById(R.id.etSearch);
        etremark=(EditText)findViewById(R.id.etremark);
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
        tvdate = (TextView) findViewById(R.id.tvdate);
        tvtime = (TextView) findViewById(R.id.tvtime);
        tv_savedamount = (TextView) findViewById(R.id.tv_savedamount);
        tvitemcount = (TextView) findViewById(R.id.tvitemcount);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        ll_orderconfirm = (LinearLayout) findViewById(R.id.ll_orderconfirm);
        ll_check = (LinearLayout) findViewById(R.id.ll_check);
        llGST = (LinearLayout) findViewById(R.id.llGST);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        tv_grandtotal = (TextView) findViewById(R.id.tv_grandtotal);
        cbConfirm = (CheckBox) findViewById(R.id.cbConfirm);

        discount_tv = findViewById(R.id.discount_tv);
        memberdiscount_tv = findViewById(R.id.memberdiscount_tv);
        othercharges_tv = findViewById(R.id.othercharges_tv);
        GrandTotal_tv = findViewById(R.id.GrandTotal_tv);
        amountpayable_tv = findViewById(R.id.amountpayable_tv);
        tv_header =  findViewById(R.id.tv_header);
        tv_placeorderconfirm =  findViewById(R.id.tv_placeorderconfirm);

        ll_redeem = findViewById(R.id.ll_redeem);
        et_your_redeem = findViewById(R.id.et_your_redeem);
        cbRedeem = findViewById(R.id.cbRedeem);
        bt_apply = findViewById(R.id.bt_apply);
        ll_check_redeem = findViewById(R.id.ll_check_redeem);
        ll_balance_red = findViewById(R.id.ll_balance_red);
        tv_your_reward = findViewById(R.id.tv_your_reward);

    }

    private void setRegViews() {
        im.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        imcart.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        ll_orderconfirm.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
    }

    private void setViews() {
        DecimalFormat f = new DecimalFormat("##.00");
        String string = "\u20B9";
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {e.printStackTrace();}
        subtotal=db.selectInshopCartTotalActualPrice();
        totalgst=db.InshopCartTotalGST();
        totalMRP=db.InshopCartTotalMRP();
        totalRetailPrice=db.InshopCartTotalRetailPrice();
        discount=totalMRP-totalRetailPrice;
        memberdiscount=   totalRetailPrice-subtotal;
        aamountPay=subtotal+totalgst;
        yousaved=discount+memberdiscount;
        tv_subtotal.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(totalMRP))));
        tv_grandtotal.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        tv_amountpay.setText(/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
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
            finalamountSave= twoStringArray[0];
        }
      //  tv_amountpay.setText(string+" "+finalamount+" /-");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im:

                //  drawer.openDrawer(Gravity.START);
                onBackPressed();                break;
            case R.id.etSearch:
                startActivity(new Intent(CheckoutInshopActivity.this,SearchActivity.class));
                break;
            case R.id.imcart:
                Intent in = new Intent(CheckoutInshopActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.tvcart:
                Intent i = new Intent(CheckoutInshopActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.ll_orderconfirm:
                if (cbRedeem.isChecked()){
                    finalamount = String.valueOf(Double.parseDouble(finalamountSave)-Double.parseDouble(redeemamount));
                }else {
                    finalamount = finalamountSave;
                }
                doOrderConfirm();
                break;
            case R.id.tv_notification:
                startActivity(new Intent(CheckoutInshopActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(CheckoutInshopActivity.this, NotificationActivity.class));
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

    private void setHomeNavMenu() {
        final String[] menulist = new String[]{"Home","My Cart","My Orders", "Favourites","Favourite Stores",
                "Notifications", "Shopping List","My Profile", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,R.drawable.ic_store,
                R.drawable.ic_notifications, R.drawable.ic_list,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(CheckoutInshopActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(CheckoutInshopActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(CheckoutInshopActivity.this, CartActivity.class);
                    i.putExtra("From", "Inshop");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(CheckoutInshopActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(CheckoutInshopActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(CheckoutInshopActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(CheckoutInshopActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(CheckoutInshopActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 7) {
                    startActivity(new Intent(CheckoutInshopActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 8) {
                    startActivity(new Intent(CheckoutInshopActivity.this, AboutUsActivity.class));
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
        NavMenuAdapter adapter = new NavMenuAdapter(CheckoutInshopActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(CheckoutInshopActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(CheckoutInshopActivity.this, CartActivity.class);
                    i.putExtra("From", "Inshop");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(CheckoutInshopActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(CheckoutInshopActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(CheckoutInshopActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(CheckoutInshopActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(CheckoutInshopActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(CheckoutInshopActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 10) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutInshopActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) CheckoutInshopActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivity(new Intent(CheckoutInshopActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(CheckoutInshopActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(CheckoutInshopActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 14) {
                    startActivity(new Intent(CheckoutInshopActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CheckoutInshopActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) CheckoutInshopActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(CheckoutInshopActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(CheckoutInshopActivity.this,LocationActivity.class));
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
                String currentdate=dateFormat.format(cal.getTime());
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
                String strTime = sdf1.format(cal.getTime());
                String DeliveryTime= strTime;
                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF12, 0);
                SharedPreferences pref3= getApplicationContext().getSharedPreferences(Config.SHARED_PREF9, 0);
                requestObject1.put("ID_Customer", pref1.getString("userid", null));
                requestObject1.put("OrderDate", currentdate);
                requestObject1.put("DeliveryDate",currentdate);
                requestObject1.put("DeliveryTime", DeliveryTime);
                requestObject1.put("Remarks", etremark.getText().toString());
                requestObject1.put("ID_Store", pref2.getString("ID_Store_Inshop", null));
                JSONArray jsonArray = new JSONArray();
                DBHandler db=new DBHandler(this);
                Cursor cursor = db.select("Inshop_cart");
                int i = 0;
                if (cursor.moveToFirst()) {
                    do {
                        JSONObject jsonObject1 = new JSONObject();
                        try {
                            jsonObject1.put("ID_Stock", cursor.getString(cursor.getColumnIndex("Inshop_Stock_ID")));
                            jsonObject1.put("ID_Item", cursor.getString(cursor.getColumnIndex("Inshop_ID_Items")));
                            jsonObject1.put("ItemName", cursor.getString(cursor.getColumnIndex("Inshop_ItemName")));
                            jsonObject1.put("MRP",cursor.getString(cursor.getColumnIndex("Inshop_MRP")));
                            jsonObject1.put("SalesPrice", cursor.getString(cursor.getColumnIndex("Inshop_SalesPrice")));
                            jsonObject1.put("Quantity", cursor.getString(cursor.getColumnIndex("Inshop_Count")));
                            jsonObject1.put("RetailPrice",cursor.getString(cursor.getColumnIndex("Inshop_RetailPrice")));
                            jsonObject1.put("VAT", cursor.getString(cursor.getColumnIndex("Inshop_GST")));
                            jsonObject1.put("Cess",cursor.getString(cursor.getColumnIndex("Inshop_CESS")));
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
                requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                SharedPreferences prefFK_SalesOrder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF404, 0);
                Log.e(TAG,"prefFK_SalesOrder   1393    "+prefFK_SalesOrder.getString("FK_SalesOrder","0"));

                requestObject1.put("IsOnlinePay",IsOnlinePay);
                requestObject1.put("RedeemRequest",RedeemRequest);

                requestObject1.put("PrivilageCardEnable",Pc_PrivilageCardEnable);
                requestObject1.put("PrivCardAmount",privilegeamount);
                requestObject1.put("AccNumber",Pc_AccNumber);
                requestObject1.put("ID_CustomerAcc",Pc_ID_CustomerAcc);

                Log.e(TAG,"requestObject1    895  "+requestObject1);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("jsondattaa","BODY  1683 "+requestObject1);
            RequestBody body = RequestBody.create(MediaType.parse("text/plain"), requestObject1.toString());
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
            MultipartBody.Part imageFiles = MultipartBody.Part.createFormData("JsonData", "", requestFile);
            Call<String> call = apiService.getSalesOrderUpdateImgFile(body,imageFiles);

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
                            DBHandler db=new DBHandler(CheckoutInshopActivity.this);
                            db.deleteallInshopCart();
                            OrderNumber_s = jobj.getString("OrderNumber");
                            FK_SalesOrder = jobj.getString("FK_SalesOrder");

                            SharedPreferences prefFK_SalesOrder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF404, 0);
                            SharedPreferences.Editor prefFK_SalesOrdereditor = prefFK_SalesOrder.edit();
                            prefFK_SalesOrdereditor.putString("FK_SalesOrder", FK_SalesOrder);
                            prefFK_SalesOrdereditor.commit();

//                            SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
//                            Intent intent = new Intent(CheckoutInshopActivity.this,ThanksActivity.class);
//                            intent.putExtra("StoreName", pref1.getString("StoreName", null));
//                            intent.putExtra("OrderNumber",jobj.getString("OrderNumber"));
//                            intent.putExtra("strPaymenttype","");
//                            intent.putExtra("finalamount",finalamount);
//                            startActivity(intent);
//                            //startActivity(new Intent(CheckoutInshopActivity.this, ThanksActivity.class));
//                            finish();

                            updatePayments(OrderNumber_s,FK_SalesOrder,strPaymentId,"","0","","0",finalamount,"0");

                        }else if(jObject.getString("StatusCode").equals("10")){
                            AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutInshopActivity.this);
                            builder.setMessage(jobj.getString("ResponseMessage"))
                                    .setCancelable(false)
                                    .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent in = new Intent(CheckoutInshopActivity.this, CartActivity.class);
                                            in.putExtra("From", "Home");
                                            startActivity(in);
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else{
                            AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutInshopActivity.this);
                            builder.setMessage(jObject.getString("EXMessage"))
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
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }

    public void getCurrentdatentime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
        strDate = sdf.format(c.getTime());
        strTime = sdf1.format(c.getTime());
    }

//    private void setBottomBar() {
//        navigation= (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        navigation.getMenu().setGroupCheckable(0, false, true);
//    }
//
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected( MenuItem item) {
//
//            switch (item.getItemId()) {
//                case R.id.navigation_category:
//                    Intent io = new Intent(CheckoutInshopActivity.this, OutShopActivity.class);
//                    io.putExtra(fromCategory, valueCategory);
//                    finish();
//                    startActivity(io);
//                    return true;
//                case R.id.navigation_cart:
//                    Log.e(TAG,"navigation_cart    ");
//                    Intent ic = new Intent(CheckoutInshopActivity.this, CartActivity.class);
//                    ic.putExtra(fromCart, valueCart);
//                    startActivity(ic);
////                    finish();
//                    //   onBackPressed();
//                    return true;
//                case R.id.navigation_home:
//                    Log.e(TAG,"navigation_home    ");
//                    Intent iha = new Intent(CheckoutInshopActivity.this, HomeActivity.class);
//                    iha.putExtra(fromFavorite, valueFavorite);
//                    startActivity(iha);
//                    return true;
//                case R.id.navigation_favorite:
//                    Log.e(TAG,"navigation_favorite    ");
//                    Intent ifa = new Intent(CheckoutInshopActivity.this, FavouriteActivity.class);
//                    ifa.putExtra(fromFavorite, valueFavorite);
//                    startActivity(ifa);
//                    return true;
//
//                case R.id.navigation_quit:
//                    Log.e(TAG,"navigation_quit    ");
//                    //quitapp();
//                    showBackPopup();
//                    return true;
//            }
//
//            //navigation.setSelectedItemId(R.id.navigation_home);
//            return false;
//        }
//    };
//
//
//    private void showBackPopup() {
//        try {
//            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CheckoutInshopActivity.this);
//            LayoutInflater inflater1 = (LayoutInflater) CheckoutInshopActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View layout = inflater1.inflate(R.layout.exit_back_popup, null);
//            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
//            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);
//            builder.setView(layout);
//            final android.app.AlertDialog alertDialog = builder.create();
//            cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    alertDialog.dismiss();
//                    // navigation.getMenu().findItem(R.id.navigation_home).setChecked(false);
//                    navigation.getMenu().setGroupCheckable(0, false, true);
//
//                }
//            });
//            ok.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    alertDialog.dismiss();
//                    finishAffinity();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        finishAffinity();
//                    }
//                }
//            });
//            alertDialog.show();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
                                startActivity(new Intent(CheckoutInshopActivity.this, ThanksActivity.class));
                                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                Intent intent = new Intent(CheckoutInshopActivity.this,ThanksActivity.class);
                                intent.putExtra("StoreName", pref1.getString("StoreName", null));
                                intent.putExtra("OrderNumber",jobj.getString("OrderNumber"));
                                intent.putExtra("strPaymenttype",strPaymenttype);
                                intent.putExtra("finalamount",finalamount);
//
//
                                startActivity(intent);

                                finish();


                            }else {
                                AlertDialog.Builder builder= new AlertDialog.Builder(CheckoutInshopActivity.this);
                                builder.setMessage(jobj.getString("ResponseMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(new Intent(CheckoutInshopActivity.this, HomeActivity.class));
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

}

