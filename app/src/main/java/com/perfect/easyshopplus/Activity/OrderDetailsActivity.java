package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.billdesk.sdk.PaymentOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Activity.billdesks.SampleCallBack;
import com.perfect.easyshopplus.Activity.billdesks.StatusActivity;
import com.perfect.easyshopplus.Adapter.AdapterPaymentOptions;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.Adapter.OrderDetailsListAdapter;
import com.perfect.easyshopplus.Adapter.OrderListAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.ReorderCartModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.ItemClickListener;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;
import com.perfect.easyshopplus.Utility.Utils;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OrderDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener , ItemClickListener {

    String TAG ="OrderDetailsActivity";
    ProgressDialog progressDialog;
    RecyclerView rv_orderdetaillist;
    EditText etSearch;
    Double dbMinimumDeliveryAmount, dbAmountPayable;
    ImageView im, imcart, im_notification, imStatus;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvuser,tv_Payment_Details, tvcart, tvitemcount, tvOrderId, tvOrderDate, tvDeliveryDate, tvStatus, tv_notification,
            tvamtpayable, tvAddnew, tv_subtotal, tv_discount, tv_memberdiscount, tv_gst, tv_amountpay, tv_grandtotal,tvstoreName,tv_deliverycharge;
    DBHandler db;
    String Somethingwentwrong,OK, Id_order,MinimumDeliveryAmount, orderDate, deliveryDate, status, order_id, ID_CustomerAddress,
            OrderType, AmountPayable, GST, MemberDiscount, Discount, SubTotal, ShopType, ID_Store, itemcount, finalamount, storeName,
            DeliveryCharge;

    RelativeLayout rl_notification, rl_orderdetals;
    LinearLayout llreorder, llGST,ll_Payment_Details,ll_addItem;
    TextView tv_header,tv_items_orderd,tv_order_summary,tvReorder, tvgst,tvincluded,tvlblgrandtotal,tvlblmemberdiscount,
            tvlblothercharges,tvlbldelcharges,tvlbldiscount;

    BottomNavigationView navigation;
    String fromCategory = "from";
    String valueCategory = "home";
    String fromCart = "From";
    String valueCart = "Home";
    String fromHome = "";
    String valueHome = "";
    String fromFavorite = "From";
    String valueFavorite = "Home";

    SharedPreferences Subtotal;
    SharedPreferences item;
    SharedPreferences Items;
    SharedPreferences GSTS;
    LinearLayout ll_redeemsummary;
    TextView redeem_tv,redeem_tvamnt;
    ProgressBar pb_confirmed,pb_packed,pb_delivered;
    ImageView img_conf,img_pack,img_deli;


    TextView tv_track_order,tv_shipping_head,tv_shipping_address,tv_payment_mode,tv_paymenttype,tvDeliveryType;
    TextView txt_payamount,txt_savedyou,tv_paynow;
    LinearLayout ll_track_order,ll_items_orderd,ll_order_summary,ll_shipping_address,ll_paymenttype;
    LinearLayout ll_payNow;
    CardView card_shipping,card_paymenttype;
    int flagTrack = 1;
    int flagItems = 1;
    int flagSummary = 1;
    int flagAddress = 1;
    int flagPayType = 1;
    int flagPayments = 1;


    String strShippingAddress = "";
    String strPaymentStatus ="";
    String strPaymentMode ="";
    String strPaymenttype="";

    RadioGroup radioGroup;
    RadioButton radioButton,radioButton2,radioButton3,radioButton4,radioButton5;

    String OrderNo,FK_SalesOrder,UPIID;
    String onlinepayMethods ;
    String MerchantID = null;
    String TransactionID = null;
    String SecurityID = null;
    String strPaymentId="";
    String OrderNumber_s ="";
    final int UPI_PAYMENT = 0;

    TextView tv_pending,tv_confirmed,tv_packed,tv_delivered,tv_view_purchase,tv_payment_statusmsg;
    String voucherUrl = "";
    String IsOnlinePay = "false";
    RecyclerView recyc_paymenttype;
    JSONArray jsonArrayPay;
    LinearLayout ll_privisummary;
    TextView privi_tv,privi_tvamnt;
    LinearLayout ll_privstatus;
    TextView tv_priv_statusmsg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_order_details_main);

        Log.e(TAG,"Start   115");

        Intent in = getIntent();
        order_id = in.getStringExtra("order_id");
        Id_order = in.getStringExtra("Id_order");
        OrderNo = in.getStringExtra("OrderNo");
        UPIID = in.getStringExtra("UPIID");
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

        Log.e(TAG,"order_id  133   "+order_id);
        Log.e(TAG,"OrderNo  133   "+OrderNo);


        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        OK = (OKsp.getString("OK", ""));
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
        getOrderDetailsList();

        SharedPreferences ordertype = getApplicationContext().getSharedPreferences(Config.SHARED_PREF157, 0);
        SharedPreferences CounterPickup = getApplicationContext().getSharedPreferences(Config.SHARED_PREF305, 0);
        SharedPreferences HomeDeliverys = getApplicationContext().getSharedPreferences(Config.SHARED_PREF300, 0);
        if(OrderType.equals("true")){
            tvDeliveryType.setText(""+ordertype.getString("ordertype",null)+" : "+HomeDeliverys.getString("HomeDeliverys",null));
        }
        else{
            tvDeliveryType.setText(""+ordertype.getString("ordertype",null)+" : "+CounterPickup.getString("CounterPickup",null));
        }

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
        ImageView imApplogo=findViewById(R.id.imApplogo);
        String strImagepath= IMAGEURL + pref3.getString("AppIconImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath).into(imApplogo);


        SharedPreferences OrderDetails = getApplicationContext().getSharedPreferences(Config.SHARED_PREF160, 0);
        SharedPreferences ItemsOrdered = getApplicationContext().getSharedPreferences(Config.SHARED_PREF161, 0);
        SharedPreferences ordersummary = getApplicationContext().getSharedPreferences(Config.SHARED_PREF210, 0);
        SharedPreferences addnewitem = getApplicationContext().getSharedPreferences(Config.SHARED_PREF172, 0);
        SharedPreferences Reorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF168, 0);
        Subtotal = getApplicationContext().getSharedPreferences(Config.SHARED_PREF162, 0);
        item = getApplicationContext().getSharedPreferences(Config.SHARED_PREF140, 0);
        Items = getApplicationContext().getSharedPreferences(Config.SHARED_PREF163, 0);
        GSTS = getApplicationContext().getSharedPreferences(Config.SHARED_PREF322, 0);
        SharedPreferences amountpayable = getApplicationContext().getSharedPreferences(Config.SHARED_PREF142, 0);
        SharedPreferences AmountPaid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF167, 0);
        SharedPreferences GrandTotal = getApplicationContext().getSharedPreferences(Config.SHARED_PREF166, 0);
        SharedPreferences othercharges = getApplicationContext().getSharedPreferences(Config.SHARED_PREF141, 0);
        SharedPreferences DeliveryCharges = getApplicationContext().getSharedPreferences(Config.SHARED_PREF211, 0);
        SharedPreferences memberdiscount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF165, 0);
        SharedPreferences Discount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF164, 0);
        SharedPreferences trackorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF188, 0);


        tv_header.setText(""+OrderDetails.getString("OrderDetails",null));
        tv_items_orderd.setText(""+ItemsOrdered.getString("ItemsOrdered",null));
        tv_order_summary.setText(""+ordersummary.getString("ordersummary",null));
        tvAddnew.setText(""+addnewitem.getString("addnewitem",null));
        tvReorder.setText(""+Reorder.getString("Reorder",null));
        tvgst.setText(""+GSTS.getString("GST",null));
        tvlbldiscount.setText(""+Discount.getString("Discount",null));
        tvlblmemberdiscount.setText(""+memberdiscount.getString("memberdiscount",null));
        tvlbldelcharges.setText(""+DeliveryCharges.getString("DeliveryCharges",null));
        tvlblothercharges.setText(""+othercharges.getString("othercharges",null));
        tvlblgrandtotal.setText(""+GrandTotal.getString("GrandTotal",null));


        SharedPreferences RedeemAmount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF398, 0);
        redeem_tv.setText(""+RedeemAmount.getString("RedeemAmount",""));

        SharedPreferences pending = getApplicationContext().getSharedPreferences(Config.SHARED_PREF149, 0);
        SharedPreferences confirmed = getApplicationContext().getSharedPreferences(Config.SHARED_PREF150, 0);
        SharedPreferences packed = getApplicationContext().getSharedPreferences(Config.SHARED_PREF151, 0);
        SharedPreferences delivered = getApplicationContext().getSharedPreferences(Config.SHARED_PREF152, 0);
        SharedPreferences paymenttype = getApplicationContext().getSharedPreferences(Config.SHARED_PREF213, 0);



        tv_pending.setText(""+pending.getString("pending",""));
        tv_confirmed.setText(""+confirmed.getString("confirmed",""));
        tv_packed.setText(""+packed.getString("packed",""));
        tv_delivered.setText(""+delivered.getString("delivered",""));
        tv_track_order.setText(""+trackorder.getString("trackorder",null));
       // tv_paymenttype.setText(""+paymenttype.getString("paymenttype",null));
        SharedPreferences paymenttype1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF428, 0);
        tv_paymenttype.setText(""+paymenttype1.getString("Changepaymenttype",null));
//        tv_shipping_head.setText(""+);



        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        tvuser.setText("Welcome "+pref2.getString("username", null));
        db=new DBHandler(this);
        tvcart.setText(String.valueOf(db.selectCartCount()+db.selectInshopCartCount()));



        SharedPreferences Somethingwentwrongsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF72, 0);
        Somethingwentwrong = Somethingwentwrongsp.getString("Somethingwentwrong", "");


        if (storeName.length()!=0){
            tvstoreName.setVisibility(View.VISIBLE);
            SharedPreferences Store = getApplicationContext().getSharedPreferences(Config.SHARED_PREF119, 0);
            tvstoreName.setText(""+Store.getString("Store",null)+" : "+storeName);
        }
        else {
            tvstoreName.setVisibility(View.GONE);
        }
        tvOrderId.setText(Id_order);
        tvOrderDate.setText(orderDate);
        if (!deliveryDate.equals(null)){

            if (deliveryDate.length() != 0){
                tvDeliveryDate.setText(deliveryDate);
                tvDeliveryDate.setVisibility(View.VISIBLE);
            }
            else{
                tvDeliveryDate.setVisibility(View.GONE);
            }
        }
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));
        Log.e(TAG,"status    334   "+status);
        if(status.equals("Pending")){
            ll_addItem.setVisibility(View.VISIBLE);
            tvAddnew.setVisibility(View.VISIBLE);
            tvStatus.setText(status);
            tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pending, 0, 0, 0);
            tvStatus.setTextColor(getResources().getColor(R.color.red));
            llreorder.setVisibility(View.GONE);
//            tvamtpayable.setText("Amount Payable");
//            tvamtpayable.setText(""+amountpayable.getString("amountpayable",null));
            PicassoTrustAll.getInstance(this).load(R.drawable.status_pending).into(imStatus);
        }
        else{
            ll_addItem.setVisibility(View.GONE);
            tvAddnew.setVisibility(View.GONE);
            llreorder.setVisibility(View.VISIBLE);}
        if(status.equals("Confirmed")){
            tvStatus.setText(status);
            tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.verifiedtick, 0, 0, 0);
            tvStatus.setTextColor(getResources().getColor(R.color.green));
//            tvamtpayable.setText(""+amountpayable.getString("amountpayable",null));
            PicassoTrustAll.getInstance(this).load(R.drawable.status_verified).into(imStatus);
        }
        if(status.equals("Delivered")){
            tvStatus.setText(status);
            tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.closeddoubletick, 0, 0, 0);
            tvStatus.setTextColor(getResources().getColor(R.color.colorAccent));
//            tvamtpayable.setText("Amount Paid");
//            tvamtpayable.setText(""+AmountPaid.getString("AmountPaid",null));
            PicassoTrustAll.getInstance(this).load(R.drawable.status_deliverd).into(imStatus);
        }
        if(status.equals("Packed")){
            tvStatus.setText(status);
            tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.packed, 0, 0, 0);
            tvStatus.setTextColor(getResources().getColor(R.color.orange));
//            tvamtpayable.setText(""+amountpayable.getString("amountpayable",null));
            PicassoTrustAll.getInstance(this).load(R.drawable.status_packed).into(imStatus);
        }

        setTrackOrder(status);



//        setData();


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButton) {
                    IsOnlinePay = "false";
                    strPaymenttype="COD";
                    getMerchantKeys();
                } else  if (checkedId == R.id.radioButton2) {
                    IsOnlinePay = "true";
                    strPaymenttype="ONLINE";
                    getMerchantKeys();
                } else  if (checkedId == R.id.radioButton3) {
                    IsOnlinePay = "true";
                    strPaymenttype="UPI";
                    getMerchantKeys();
                }else if (checkedId == R.id.radioButton4){
                    IsOnlinePay = "true";
                    strPaymenttype="PAYU Biz";
                    getMerchantKeys();
                }
                else if (checkedId == R.id.radioButton5){
                    IsOnlinePay = "true";
                    strPaymenttype="Bill Desk";
                    getMerchantKeys();
                }

            }
        });

    }

    private void getMerchantKeys() {
        strPaymentId = "0";
//        SharedPreferences OnlinePaymentpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF62, 0);
//        String value = OnlinePaymentpref.getString("OnlinePaymentMethods", null);
        String value = onlinepayMethods;
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
                    strPaymentId = "0";
                    Log.e(TAG,"strPaymentId   5295       "+strPaymentId+"    "+jobjt.getString("ID_PaymentMethod")+"        "+strPaymenttype);
                }


            }



        } catch (Exception e) {
            Log.e(TAG,"Exception   23221    "+e.toString());
        }

        Log.e(TAG,"strPaymentId   529       "+strPaymentId+"    "+strPaymenttype);
    }


//    private void setData(){
//        TextView tv_header = findViewById(R.id.tv_header);
//        TextView tv_items_orderd = findViewById(R.id.tv_items_orderd);
//        TextView tv_order_summary = findViewById(R.id.tv_order_summary);
//
//        SharedPreferences spOrderDetails = getApplicationContext().getSharedPreferences(Config.SHARED_PREF65, 0);
//        tv_header.setText(spOrderDetails.getString("OrderDetails", null));
//
////        SharedPreferences spOrderNumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF66, 0);
////        tvOrderId.setText(spOrderNumber.getString("OrderNumber", null) + Id_order);
//
//        SharedPreferences spStore = getApplicationContext().getSharedPreferences(Config.SHARED_PREF67, 0);
//        if (storeName.length()!=0){
//            tvstoreName.setVisibility(View.VISIBLE);
//            tvstoreName.setText(spStore.getString("Store",null)+" : "+storeName);
//        }
//        else {
//            tvstoreName.setVisibility(View.GONE);
//        }
//
////        SharedPreferences spOrderedDate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF68, 0);
////        tvOrderDate.setText(spOrderedDate.getString("OrderedDate", null) + orderDate);
//
//        SharedPreferences spOrderedItems = getApplicationContext().getSharedPreferences(Config.SHARED_PREF69, 0);
//        tv_items_orderd.setText(spOrderedItems.getString("OrderedItems", null));
//
//        SharedPreferences spOrderSummary = getApplicationContext().getSharedPreferences(Config.SHARED_PREF70, 0);
//        tv_order_summary.setText(spOrderSummary.getString("OrderSummary", null));
//    }

    private void initiateViews() {

        ll_addItem = findViewById(R.id.ll_addItem) ;
        tvDeliveryType = findViewById(R.id.tvDeliveryType) ;
        tv_Payment_Details = findViewById(R.id.tv_Payment_Details) ;
        ll_Payment_Details = findViewById(R.id.ll_Payment_Details) ;
        tv_header = (TextView)findViewById(R.id.tv_header) ;
        tv_items_orderd =  findViewById(R.id.tv_items_orderd);
        tv_order_summary = findViewById(R.id.tv_order_summary);
        tvReorder = findViewById(R.id.tvReorder);

        rv_orderdetaillist=(RecyclerView)findViewById(R.id.rv_orderdetaillist);
        etSearch=(EditText)findViewById(R.id.etSearch);
        imcart=(ImageView) findViewById(R.id.imcart);
        imStatus=(ImageView) findViewById(R.id.imStatus);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        im = (ImageView) findViewById(R.id.im);
        tvAddnew = (TextView) findViewById(R.id.tvAddnew);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        tvOrderId = (TextView) findViewById(R.id.tvOrderId);
        tvOrderDate = (TextView) findViewById(R.id.tvOrderDate);
        tvDeliveryDate = (TextView) findViewById(R.id.tvDeliveryDate);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        rl_orderdetals=(RelativeLayout) findViewById(R.id.rl_orderdetals);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        tv_subtotal = (TextView) findViewById(R.id.tv_subtotal);
        tv_discount = (TextView) findViewById(R.id.tv_discount);
        tv_memberdiscount = (TextView) findViewById(R.id.tv_memberdiscount);
        tv_deliverycharge = (TextView) findViewById(R.id.tv_deliverycharge);
        tv_gst = (TextView) findViewById(R.id.tv_gst);
        tv_amountpay = (TextView) findViewById(R.id.tv_amountpay);
        tvitemcount = (TextView) findViewById(R.id.tvitemcount);
        tvamtpayable = (TextView) findViewById(R.id.tvamtpayable);
        tv_grandtotal = (TextView) findViewById(R.id.tv_grandtotal);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        llreorder = (LinearLayout) findViewById(R.id.llreorder);
        llGST = (LinearLayout) findViewById(R.id.llGST);
        tvstoreName =  findViewById(R.id.tvstoreName);


        tvgst = (TextView) findViewById(R.id.tvgst);
        tvincluded = (TextView) findViewById(R.id.tvincluded);
        tvlblgrandtotal = (TextView) findViewById(R.id.tvlblgrandtotal);
        tvlblmemberdiscount = (TextView) findViewById(R.id.tvlblmemberdiscount);
        tvlblothercharges = (TextView) findViewById(R.id.tvlblothercharges);
        tvlbldelcharges = (TextView) findViewById(R.id.tvlbldelcharges);
        tvlbldiscount = (TextView) findViewById(R.id.tvlbldiscount);

        ll_redeemsummary = (LinearLayout) findViewById(R.id.ll_redeemsummary);
        redeem_tv = (TextView) findViewById(R.id.redeem_tv);
        redeem_tvamnt = (TextView) findViewById(R.id.redeem_tvamnt);


        ll_track_order = (LinearLayout)findViewById(R.id.ll_track_order);
        ll_items_orderd = (LinearLayout)findViewById(R.id.ll_items_orderd);
        ll_order_summary = (LinearLayout)findViewById(R.id.ll_order_summary);
        tv_track_order = (TextView) findViewById(R.id.tv_track_order);

        card_shipping = (CardView)findViewById(R.id.card_shipping);
        tv_shipping_address = (TextView) findViewById(R.id.tv_shipping_address);
        tv_shipping_head = (TextView) findViewById(R.id.tv_shipping_head);
        ll_shipping_address = (LinearLayout)findViewById(R.id.ll_shipping_address);

        tv_payment_mode  = (TextView) findViewById(R.id.tv_payment_mode);

        radioGroup =  findViewById(R.id.radioGroup);
        radioButton =  findViewById(R.id.radioButton);
        radioButton2 =  findViewById(R.id.radioButton2);
        radioButton3 =  findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);
        radioButton5 = findViewById(R.id.radioButton5);

        tv_paymenttype  = (TextView) findViewById(R.id.tv_paymenttype);
        ll_paymenttype  = (LinearLayout) findViewById(R.id.ll_paymenttype);
        card_paymenttype  = (CardView) findViewById(R.id.card_paymenttype);

        txt_payamount =  (TextView) findViewById(R.id.txt_payamount);
        txt_savedyou = (TextView) findViewById(R.id.txt_savedyou);

        tv_paynow =  (TextView) findViewById(R.id.tv_paynow);
        ll_payNow =  (LinearLayout) findViewById(R.id.ll_payNow);


        tv_pending =  (TextView) findViewById(R.id.tv_pending);
        tv_confirmed =  (TextView) findViewById(R.id.tv_confirmed);
        tv_packed =  (TextView) findViewById(R.id.tv_packed);
        tv_delivered =  (TextView) findViewById(R.id.tv_delivered);
        tv_view_purchase =  (TextView) findViewById(R.id.tv_view_purchase);
        tv_payment_statusmsg = (TextView) findViewById(R.id.tv_payment_statusmsg);

        recyc_paymenttype = (RecyclerView) findViewById(R.id.recyc_paymenttype);

        ll_privisummary = (LinearLayout) findViewById(R.id.ll_privisummary);
        privi_tv = (TextView) findViewById(R.id.privi_tv);
        privi_tvamnt = (TextView) findViewById(R.id.privi_tvamnt);

        ll_privstatus = (LinearLayout) findViewById(R.id.ll_privstatus);
        tv_priv_statusmsg= (TextView) findViewById(R.id.tv_priv_statusmsg);



    }

    private void setRegViews() {
        im.setOnClickListener(this);
        tv_Payment_Details.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        imcart.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        tvAddnew.setOnClickListener(this);
        llreorder.setOnClickListener(this);
        tv_track_order.setOnClickListener(this);
        tv_items_orderd.setOnClickListener(this);
        tv_order_summary.setOnClickListener(this);
        tv_shipping_head.setOnClickListener(this);
        tv_paymenttype.setOnClickListener(this);
        ll_payNow.setOnClickListener(this);
        tv_view_purchase.setOnClickListener(this);
        tv_paynow.setOnClickListener(this);

    }

    private void getOrderDetailsList() {
        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(this).isInternetOn()) {
        progressDialog = new ProgressDialog(this, R.style.Progress);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setIndeterminateDrawable(this.getResources().getDrawable(R.drawable.progress));
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
                    requestObject1.put("ReqMode", "8");
                    requestObject1.put("ID_SalesOrder",order_id );
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1  325   "+requestObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getOrderDetailList(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            Log.e(TAG,"onResponse  336   "+response.body());
                            if (jObject.getString("StatusCode").equals("0")){

                                JSONObject jobj = jObject.getJSONObject("SalesOrderItemsDetailsInfo");
                                DecimalFormat f = new DecimalFormat("##.00");
                                String string = "\u20B9";
                                byte[] utf8 = new byte[0];
                                try {
                                    utf8 = string.getBytes("UTF-8");
                                    string = new String(utf8, "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                tv_subtotal.setText(/*string + " " + */f.format(jobj.getDouble("SubTotal")));
                                if (f.format(jobj.getDouble("Discount")).equals(".00")) {
                                    tv_discount.setText(/*string + */" 0.00");
                                    SharedPreferences youhavesaved = getApplicationContext().getSharedPreferences(Config.SHARED_PREF212, 0);
                                    txt_savedyou.setText("( "+youhavesaved.getString("youhavesaved", "")+" 0.00 )");
                                } else {
                                    tv_discount.setText(/*string + " " + */f.format(jobj.getDouble("Discount")));
                                    SharedPreferences youhavesaved = getApplicationContext().getSharedPreferences(Config.SHARED_PREF212, 0);
                                    txt_savedyou.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+f.format(jobj.getDouble("Discount"))+" )");
                                }
                                if (f.format(jobj.getDouble("MemberDiscount")).equals(".00")) {
                                    tv_memberdiscount.setText(/*string +*/ " 0.00");
                                } else {
                                    tv_memberdiscount.setText(/*string + " " + */f.format(jobj.getDouble("MemberDiscount")));
                                }
                                if (f.format(jobj.getDouble("DeliveryCharge")).equals(".00")) {
                                    tv_deliverycharge.setText(/*string +*/ " 0.00");
                                } else {
                                    tv_deliverycharge.setText(/*string + " " +*/ f.format(jobj.getDouble("DeliveryCharge")));
                                }
                                if (jobj.getDouble("GST") <= 0) {
                                    llGST.setVisibility(View.INVISIBLE);
                                } else {
                                    llGST.setVisibility(View.VISIBLE);
                                    tv_gst.setVisibility(View.GONE);
                                }
                                // tv_grandtotal.setText(/*string + " " +*/ f.format(jobj.getDouble("AmountPayable")));
                                tv_amountpay.setText(/*string + " " +*/ jobj.getString("AmountPayable"));
                                SharedPreferences totalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF131, 0);
                                txt_payamount.setText(totalamount.getString("totalamount", "")+" : "+f.format(jobj.getDouble("AmountPayable")));

                                Float RedeemAmnts = Float.parseFloat(jobj.getString("RedeemAmnt"));
                                Float PrivCardAmount = Float.parseFloat(jobj.getString("PrivCardAmount"));
                                Log.e(TAG,"RedeemAmnt  450  "+RedeemAmnts);
                                String amntPayable ="";
//                                if (RedeemAmnts>0 && PrivCardAmount >0){
//                                    Log.e(TAG,"RedeemAmnt  PrivCardAmount 4531  "+RedeemAmnts+"  "+PrivCardAmount);
//                                    amntPayable = String.valueOf(jobj.getDouble("AmountPayable") + RedeemAmnts+PrivCardAmount);
//                                    Log.e(TAG,"RedeemAmnt  PrivCardAmount   amntPayable 4532  "+amntPayable);
//                                    tv_grandtotal.setText(/*string + " " +*/ f.format(Double.parseDouble(amntPayable)));
//                                    ll_redeemsummary.setVisibility(View.VISIBLE);
//                                    ll_privisummary.setVisibility(View.VISIBLE);
//                                    redeem_tvamnt.setText(""+f.format(Double.parseDouble(String.valueOf(RedeemAmnts))));
//                                    privi_tvamnt.setText(""+f.format(Double.parseDouble(String.valueOf(PrivCardAmount))));
//
//                                }
//                                else if (RedeemAmnts>0){
//                                    Log.e(TAG,"RedeemAmnt  4533  "+RedeemAmnts);
//                                    amntPayable = String.valueOf(jobj.getDouble("AmountPayable") + RedeemAmnts);
//                                    Log.e(TAG,"RedeemAmnt   amntPayable 4534  "+amntPayable);
//                                    tv_grandtotal.setText(/*string + " " +*/ f.format(Double.parseDouble(amntPayable)));
////                                    tv_grandtotal.setText(/*string + " " +*/ f.format(jobj.getDouble("AmountPayable")));
//                                    ll_redeemsummary.setVisibility(View.VISIBLE);
//                                    ll_privisummary.setVisibility(View.GONE);
//                                    redeem_tvamnt.setText(""+f.format(Double.parseDouble(String.valueOf(RedeemAmnts))));
//                                    privi_tvamnt.setText("0.00");
//                                }
//
//                                else if (PrivCardAmount>0){
//                                    Log.e(TAG,"PrivCardAmount  4535  "+PrivCardAmount);
//                                    amntPayable = String.valueOf(jobj.getDouble("AmountPayable") + PrivCardAmount);
//                                    Log.e(TAG,"  PrivCardAmount   amntPayable 4534  "+amntPayable);
//                                    tv_grandtotal.setText(/*string + " " +*/ f.format(Double.parseDouble(amntPayable)));
////                                    tv_grandtotal.setText(/*string + " " +*/ f.format(jobj.getDouble("AmountPayable")));
//                                    ll_redeemsummary.setVisibility(View.GONE);
//                                    ll_privisummary.setVisibility(View.VISIBLE);
//                                    redeem_tvamnt.setText("0.00");
//                                    privi_tvamnt.setText(""+f.format(Double.parseDouble(String.valueOf(PrivCardAmount))));
//                                }
//
//                                else{
//                                    Log.e(TAG,"PrivCardAmount  457  "+PrivCardAmount);
//                                    tv_grandtotal.setText(/*string + " " +*/ f.format(jobj.getDouble("AmountPayable")));
////                                    tv_grandtotal.setText(/*string + " " +*/ f.format(jobj.getDouble("AmountPayable")));
//                                    ll_redeemsummary.setVisibility(View.GONE);
//                                    ll_privisummary.setVisibility(View.GONE);
//                                    redeem_tvamnt.setText("0.00");
//                                    privi_tvamnt.setText("0.00");
//                                }


                                if (jobj.getString("PrivilageCardEnable").equals("true")){
                                    //ll_privstatus.setVisibility(View.VISIBLE);
                                    ll_privisummary.setVisibility(View.VISIBLE);
                                  //  tv_priv_statusmsg.setText("Card Status : "+jobj.getString("PrivCardStatus"));
                                    if (PrivCardAmount>0){
                                        privi_tvamnt.setText(""+f.format(Double.parseDouble(String.valueOf(PrivCardAmount))));
                                    }else {
                                        ll_privisummary.setVisibility(View.GONE);
                                        privi_tvamnt.setText("0.00");
                                    }
                                }else {
                                   // ll_privstatus.setVisibility(View.GONE);
                                    ll_privisummary.setVisibility(View.GONE);
                                 //   tv_priv_statusmsg.setText("Card Status : "+jobj.getString("PrivCardStatus"));
                                    privi_tvamnt.setText("0.00");
                                }


                                if (jobj.getString("GiftVoucher").equals("true")){
                                    ll_redeemsummary.setVisibility(View.VISIBLE);
                                    if (RedeemAmnts>0){
                                        redeem_tvamnt.setText(""+f.format(Double.parseDouble(String.valueOf(RedeemAmnts))));
                                    }else {
                                        ll_redeemsummary.setVisibility(View.GONE);
                                        redeem_tvamnt.setText("0.00");
                                    }

                                }else {
                                    ll_redeemsummary.setVisibility(View.GONE);
                                    redeem_tvamnt.setText("0.00");
                                }

                                if (jobj.getString("PrivilageCard").equals("true")){
                                    ll_privstatus.setVisibility(View.VISIBLE);
                                    tv_priv_statusmsg.setText("Card Status : "+jobj.getString("PrivCardStatus"));
                                }else {
                                    ll_privstatus.setVisibility(View.GONE);
                                    tv_priv_statusmsg.setText("Card Status : "+jobj.getString("PrivCardStatus"));
                                }


                                dbMinimumDeliveryAmount = jobj.getDouble("MinimumDeliveryAmount");



                                dbAmountPayable = jobj.getDouble("AmountPayable");
                                String value = jobj.getString("AmountPayable");
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

                                if (jobj.getDouble("SalesPrice")>0){
                                    tv_grandtotal.setText(f.format(Double.parseDouble(jobj.getString("SalesPrice"))));
                                }else {
                                    tv_grandtotal.setText("0.00");
                                }


                                strShippingAddress = jobj.getString("DeliverAddress");
                                if(strShippingAddress.equals("null") ||strShippingAddress.isEmpty()){
                                    Log.e(TAG,"strShippingAddress   533 "+strShippingAddress);
                                    card_shipping.setVisibility(View.GONE);
                                }else {
                                    Log.e(TAG,"strShippingAddress  536  "+strShippingAddress);
                                    card_shipping.setVisibility(View.VISIBLE);
                                    tv_shipping_address.setText(""+strShippingAddress);
                                }

                                strPaymentStatus= jobj.getString("PaymentStatus");
                                Log.e(TAG,"strPaymentStatus   824 "+strPaymentStatus+"  "+status+"  "+jobj.getString("PrivCardStatus"));
//                                if (strPaymentStatus.equals("Paid")){
                                if (strPaymentStatus.equals("Paid") || status.equals("Delivered")){

                                    if (jobj.getString("PrivCardStatus").equals("Sucess")){
                                        SharedPreferences AmountPaid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF167, 0);
                                        tvamtpayable.setText(""+AmountPaid.getString("AmountPaid",null));
                                        card_paymenttype.setVisibility(View.GONE);
                                    }else {
                                        SharedPreferences AmountPaid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF167, 0);
                                        tvamtpayable.setText(""+AmountPaid.getString("AmountPaid",null));
                                        card_paymenttype.setVisibility(View.VISIBLE);
                                    }

                                }else {
                                    SharedPreferences amountpayable = getApplicationContext().getSharedPreferences(Config.SHARED_PREF142, 0);
                                    tvamtpayable.setText(""+amountpayable.getString("amountpayable",null));
                                    card_paymenttype.setVisibility(View.VISIBLE);
                                }


                                tv_payment_statusmsg.setText("Payment Status : "+jobj.getString("Transactionmessage"));





//                                ll_privstatus = (LinearLayout) findViewById(R.id.ll_privstatus);
//                                tv_priv_statusmsg

                                FK_SalesOrder =  jobj.getString("FK_SalesOrder");
                                UPIID =  jobj.getString("UPIID");
                                onlinepayMethods =jobj.getString("OnlinePaymentMethodDetails");
                                OrderNumber_s =jobj.getString("OrderNo");
                                voucherUrl = jObject.getString("VoucherUrl");

                               if (jobj.getDouble("AmountPayable")>0){
                                   paymentcondition(onlinepayMethods);
                               }else {
                                   card_paymenttype.setVisibility(View.GONE);
                               }


                                strPaymentMode = jobj.getString("PaymentHint");
//                                if (strPaymentMode.equals("Online")){
//                                    tv_payment_mode.setText("Payment Mode : "+strPaymentMode+" , "+jobj.getString("PayTransactionDetail"));
//                                }else {
//                                    tv_payment_mode.setText("Payment Mode : "+"Cash On Delivery [COD]");
//                                }
                                tv_payment_mode.setText("Payment Mode : "+strPaymentMode+" , "+jobj.getString("PayTransactionDetail"));

//                                if (strPaymentMode.equals("Cash")){
//                                    tv_payment_statusmsg.setVisibility(View.GONE);
//                                }



                                // tv_amountpay.setText(string+" "+finalamount+" /-");
                                if (jobj.getString("SalesOrderItemsDetails").equals("null")) {
                                    rl_orderdetals.setVisibility(View.GONE);

                                    SharedPreferences Pleasewaityourorderisunderprocessingsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF257, 0);
                                    String Pleasewaityourorderisunderprocessing = Pleasewaityourorderisunderprocessingsp.getString("Pleasewaityourorderisunderprocessing","");

                                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
                                    builder.setMessage(Pleasewaityourorderisunderprocessing+".")
                                            .setCancelable(false)
                                            .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    finish();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }else{
                                    JSONArray jarray = jobj.getJSONArray("SalesOrderItemsDetails");
                                    rl_orderdetals.setVisibility(View.VISIBLE);
                                    if (jarray.length() <= 1) {

                                        tvitemcount.setText(""+Subtotal.getString("Subtotal",null)+" ( " + jarray.length() + ""+item.getString("item",null)+")");
                                    } else {
                                        tvitemcount.setText(""+Subtotal.getString("Subtotal",null)+" ( " + jarray.length() +""+Items.getString("Items",null)+")");
                                    }
                                    Log.e(TAG,"jarray  407   "+jarray);
                                    if (jarray.length() != 0) {
                                        GridLayoutManager lLayout = new GridLayoutManager(OrderDetailsActivity.this, 1);
                                        rv_orderdetaillist.setLayoutManager(lLayout);
                                        rv_orderdetaillist.setHasFixedSize(true);
                                        OrderDetailsListAdapter adapter = new OrderDetailsListAdapter(OrderDetailsActivity.this, jarray,
                                                status, order_id,
                                                Id_order, orderDate, deliveryDate, ShopType, ID_Store,
                                                itemcount, storeName, dbMinimumDeliveryAmount, dbAmountPayable,
                                                OrderType, ID_CustomerAddress, DeliveryCharge,OrderNo);

                                        rv_orderdetaillist.setAdapter(adapter);
                                    } else {

                                    }
                                }

                            }else {
                                AlertDialog.Builder builder= new AlertDialog.Builder(OrderDetailsActivity.this);
                                JSONObject jobj = jObject.getJSONObject("SalesOrderItemsDetailsInfo");
                                SharedPreferences Pleaseselectanypaymentoptionsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF264, 0);
                                String Pleaseselectanypaymentoption = (jobj.getString("ResponseMessage"));
                                builder.setMessage(Pleaseselectanypaymentoption+". ")
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
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }

    private void paymentcondition(String onlinepayMethods) {

//        SharedPreferences OnlinePaymentmeth1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF62, 0);
//        String BASEURL = OnlinePaymentmeth1.getString("OnlinePaymentMethods", null);
//        Log.e(TAG,"BASEURLSSSSS   2283    "+BASEURL);
//
//        SharedPreferences OnlinePaymentpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF62, 0);
//        String value = OnlinePaymentpref.getString("OnlinePaymentMethods", null);



        String value = onlinepayMethods;
        Log.e(TAG,"OnlinePaymentpref   2293    "+value);
        try {

            jsonArrayPay = new JSONArray(value);


            AdapterPaymentOptions adapter = new AdapterPaymentOptions(OrderDetailsActivity.this, jsonArrayPay);
            LinearLayoutManager horizontalLayoutManagaer
                    = new LinearLayoutManager(OrderDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
            recyc_paymenttype.setLayoutManager(horizontalLayoutManagaer);
            recyc_paymenttype.setAdapter(adapter);
            adapter.setClickListener(OrderDetailsActivity.this);


//            JSONArray jsonArray = new JSONArray(value);
//            for(int i=0; i<=jsonArray.length(); i++) {
//                JSONObject jobjt = jsonArray.getJSONObject(i);
//                String id  = jobjt.getString("ID_PaymentMethod");
//                String PaymentName = jobjt.getString("PaymentName");
//
//                Log.e("paymentdata ",""+PaymentName);
//
//
//                if (PaymentName.equals("UPI")){
//                    radioButton3.setVisibility(View.VISIBLE);
//                    radioButton3.setText(PaymentName);
//                }
//                if (PaymentName.equals("PayU")){
//                    radioButton4.setVisibility(View.VISIBLE);
//                    radioButton4.setText(PaymentName);
//                }
//                if (PaymentName.equals("Razorpay")){
//                    radioButton2.setVisibility(View.VISIBLE);
//                    radioButton2.setText(PaymentName);
//                }
//                if (PaymentName.equals("Bill Desk")){
//                    radioButton5.setVisibility(View.VISIBLE);
//                    radioButton5.setText(PaymentName);
//                }
//            }

        } catch (Exception e) {
            Log.e(TAG,"Exception   2322    "+e.toString());
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
                onBackPressed();                break;
            case R.id.etSearch:
                startActivity(new Intent(OrderDetailsActivity.this,SearchActivity.class));
                break;
            case R.id.imcart:
                Intent intnt = new Intent(OrderDetailsActivity.this, CartActivity.class);
                intnt.putExtra("From", "Home");
                startActivity(intnt);
                break;
            case R.id.tvcart:
                Intent intent = new Intent(OrderDetailsActivity.this, CartActivity.class);
                intent.putExtra("From", "Home");
                startActivity(intent);
                break;
            case R.id.tv_notification:
                startActivity(new Intent(OrderDetailsActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(OrderDetailsActivity.this, NotificationActivity.class));
                break;
            case R.id.llreorder:
                db.deleteallReOrderCart();
                getReOrderList();
                break;
            case R.id.tvAddnew:
                Intent in = new Intent(this, NewOrderCategoryActivity.class);
                in.putExtra("ID_SalesOrder", order_id);
                in.putExtra("order_id", order_id);
                in.putExtra("OrderNo", OrderNo);
                in.putExtra("FK_SalesOrder",FK_SalesOrder);
                in.putExtra("voucherUrl",voucherUrl);
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
                in.putExtra("DeliveryCharge", DeliveryCharge);
                startActivity(in);
                break;

            case R.id.tv_track_order:
                if (flagTrack == 0){
                    flagTrack = 1;
                    ll_track_order.setVisibility(View.GONE);
                    tv_track_order.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                }else {
                    flagTrack = 0;
                    ll_track_order.setVisibility(View.VISIBLE);
                    tv_track_order.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);
                    setTrackOrder(status);
                }
                break;

            case R.id.tv_items_orderd:
                if (flagItems == 0){
                    flagItems = 1;
                    ll_items_orderd.setVisibility(View.GONE);
                    tv_items_orderd.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                }else {
                    flagItems = 0;
                    ll_items_orderd.setVisibility(View.VISIBLE);
                    tv_track_order.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);

                }
                break;
            case R.id.tv_Payment_Details:
                if (flagPayments == 0){
                    flagPayments = 1;
                    ll_Payment_Details.setVisibility(View.GONE);
                    tv_Payment_Details.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                }else {
                    flagPayments = 0;
                    ll_Payment_Details.setVisibility(View.VISIBLE);
                    tv_Payment_Details.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);

                }
                break;
            case R.id.tv_order_summary:
                if (flagSummary == 0){
                    flagSummary = 1;
                    ll_order_summary.setVisibility(View.GONE);
                    tv_order_summary.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                }else {
                    flagSummary = 0;
                    ll_order_summary.setVisibility(View.VISIBLE);
                    tv_order_summary.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);

                }
                break;

            case R.id.tv_shipping_head:
                if (flagAddress == 0){
                    flagAddress = 1;
                    ll_shipping_address.setVisibility(View.GONE);
                    tv_shipping_head.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_down, 0);
                }else {
                    flagAddress = 0;
                    ll_shipping_address.setVisibility(View.VISIBLE);
                    tv_shipping_head.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.arrow_top, 0);

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

//            case R.id.ll_payNow:
            case R.id.tv_paynow:

                Log.e(TAG,"IsOnlinePay   1375     "+IsOnlinePay);
                Log.e(TAG,"MerchantID   1375     "+MerchantID);
                Log.e(TAG,"TransactionID  1375   "+TransactionID);
                Log.e(TAG,"SecurityID   1375     "+SecurityID);
                Log.e(TAG,"strPaymentId   1375     "+strPaymentId);
                Log.e(TAG,"UPIID   1375     "+UPIID);
                Log.e(TAG,"FK_SalesOrder   1375     "+FK_SalesOrder);
                Log.e(TAG,"strPaymenttype   1375     "+strPaymenttype);
                Log.e(TAG,"finalamount   1375     "+finalamount);
                Log.e(TAG,"storeName   1375     "+storeName);
                Log.e(TAG,"OrderNumber_s   1375     "+OrderNumber_s);
                //

//                if (strPaymenttype.equals("COD") || strPaymenttype.equals("UPI") || strPaymenttype.equals("PAYU Biz") || strPaymenttype.equals("Bill Desk")){
                if (!strPaymentId.equals("")){
                    passPaymentMethod();
                }else {
                    AlertDialog.Builder builder= new AlertDialog.Builder(OrderDetailsActivity.this);
                    // builder.setTitle("Terms & Conditions");
                    SharedPreferences Pleaseselectanypaymentoptionsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF264, 0);
                    String Pleaseselectanypaymentoption = (Pleaseselectanypaymentoptionsp.getString("Pleaseselectanypaymentoption", ""));
                    builder.setMessage(Pleaseselectanypaymentoption+". ")
                            .setCancelable(false)
                            .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                


//                if(strPaymenttype.equals("COD")){
//                    try {
//
//                    }catch (Exception e){
//                        Log.e(TAG,"Exception   1019  "+e.toString());
//                    }
//                }
//                else if(strPaymenttype.equals("UPI")){
//                    try {
//                        payUsingUpi(storeName, UPIID, "Order Payment", ""+finalamount);
//                    }catch (Exception e){
//                        Log.e(TAG,"Exception  1026   "+e.toString());
//                    }
//                }
//                else if (strPaymenttype.equals("PAYU Biz")) {
//                    try {
//
//                    }catch (Exception e){
//                        Log.e(TAG,"Exception   1033  "+e.toString());
//                    }
//                }
//                else if (strPaymenttype.equals("Bill Desk")) {
//                    try {
//                        startBillDesk(finalamount);
////                        VerifyChecksum(finalamount);
//                    }catch (Exception e){
//                        Log.e(TAG,"Exception  1040   "+e.toString());
//                    }
//                }
//                else {
//                    AlertDialog.Builder builder= new AlertDialog.Builder(OrderDetailsActivity.this);
//                    // builder.setTitle("Terms & Conditions");
//                    SharedPreferences Pleaseselectanypaymentoptionsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF264, 0);
//                    String Pleaseselectanypaymentoption = (Pleaseselectanypaymentoptionsp.getString("Pleaseselectanypaymentoption", ""));
//                    builder.setMessage(Pleaseselectanypaymentoption+". ")
//                            .setCancelable(false)
//                            .setPositiveButton(OK, new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                }
//                            });
//                    AlertDialog alert = builder.create();
//                    alert.show();
//                }

                break;

            case R.id.tv_view_purchase:

                Intent vp = new Intent(this,PurchaseDetailActivity.class);
                vp.putExtra("voucherUrl",voucherUrl);
                startActivity(vp);
                break;

        }
    }

    private void passPaymentMethod() {

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

                    requestObject1.put("ID_SalesOrder", FK_SalesOrder);
                    requestObject1.put("IsOnlinePay", IsOnlinePay);
                    requestObject1.put("FK_PaymentMethod",strPaymentId);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));

                    Log.e(TAG,"requestObject1   516    "+requestObject1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getPaymentMethod(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG,"response   1675  "+response.body());

                            JSONObject jObject = new JSONObject(response.body());
                            Log.e(TAG,"response   16753  "+jObject.getString("StatusCode"));
                            if(jObject.getString("StatusCode").equals("3")){
//                                JSONObject jobj = jObject.getJSONObject("SalesOrderDetails");
//                                Log.e(TAG,"ResponseMessage   1270   "+jobj.getString("ResponseMessage"));
                                if(strPaymentId.equals("1")){
                                    try {

                                    }catch (Exception e){
                                        Log.e(TAG,"Exception   1019  "+e.toString());
                                    }
                                }
                                else if(strPaymentId.equals("4")){
                                      try {

                                    }catch (Exception e){
                                        Log.e(TAG,"Exception   1019  "+e.toString());
                                    }
                                }
                                else if(strPaymentId.equals("5")){
                                    try {
                                        payUsingUpi(storeName, UPIID, "Order Payment", ""+finalamount);
                                    }catch (Exception e){
                                        Log.e(TAG,"Exception  1026   "+e.toString());
                                    }
                                }
                                else if (strPaymentId.equals("10")) {
                                    try {

                                    }catch (Exception e){
                                        Log.e(TAG,"Exception   1033  "+e.toString());
                                    }
                                }
                                else if (strPaymentId.equals("2")) {
                                    try {
                                        startBillDesk(finalamount);
                                    }catch (Exception e){
                                        Log.e(TAG,"Exception  1040   "+e.toString());
                                    }
                                }

                            }else {
                              //  JSONObject jobj = jObject.getJSONObject("SalesOrderDetails");
                                AlertDialog.Builder builder= new AlertDialog.Builder(OrderDetailsActivity.this);
                                // builder.setTitle("Terms & Conditions");
                               // SharedPreferences Pleaseselectanypaymentoptionsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF264, 0);
//                                String Pleaseselectanypaymentoption = (jobj.getString("ResponseMessage"));
                                String EXMessage = (jObject.getString("EXMessage"));
                                builder.setMessage(EXMessage)
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            startActivity(new Intent(OrderDetailsActivity.this, OrdersActivity.class));

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
        final String[] menulist = new String[]{"Home","My Cart", "My Orders","Favourites","Favourite Stores",
                "Notifications", "Shopping List","My Profile", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart,R.drawable.ic_order, R.drawable.ic_favorite,R.drawable.ic_store,
                R.drawable.ic_notifications, R.drawable.ic_list,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(OrderDetailsActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(OrderDetailsActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(OrderDetailsActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(OrderDetailsActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 3) {
                    startActivity(new Intent(OrderDetailsActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(OrderDetailsActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(OrderDetailsActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(OrderDetailsActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    startActivity(new Intent(OrderDetailsActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 8) {
                    startActivity(new Intent(OrderDetailsActivity.this, AboutUsActivity.class));
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
                R.drawable.ic_home,R.drawable.ic_cart,R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications, R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(OrderDetailsActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(OrderDetailsActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(OrderDetailsActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(OrderDetailsActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 3) {
                    startActivity(new Intent(OrderDetailsActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(OrderDetailsActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(OrderDetailsActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(OrderDetailsActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(OrderDetailsActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 10) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) OrderDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivity(new Intent(OrderDetailsActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(OrderDetailsActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(OrderDetailsActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 14) {
                    startActivity(new Intent(OrderDetailsActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) OrderDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(OrderDetailsActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(OrderDetailsActivity.this,LocationActivity.class));
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

    private void getReOrderList() {

        Log.e(TAG,"getReOrderList  780   ");
        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        final String IMAGEURL = imgpref.getString("ImageURL", null);
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
                    requestObject1.put("ReqMode", "11");
                    requestObject1.put("ID_SalesOrder", order_id);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1  816   "+requestObject1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getReOrderDetails(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG,"onResponse  827   "+response.body());
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("ReSalesOrderDetails");
                            if (jObject.getString("StatusCode").equals("0")){

                                JSONArray jarray = jobj.getJSONArray("ReSalesOrderDetailsInfo");

                                Log.e(TAG,"jarray   838   "+jarray.length());
                                for(int i=0; i<jarray.length();i++){
                                    JSONObject jobjt =  jarray.getJSONObject(i);
                                    Log.e(TAG,"jarray   838   "+ jobjt.getString("ItemName"));
                                    DBHandler db=new DBHandler(OrderDetailsActivity.this);
                                    db.addtoReorderCart(new ReorderCartModel(jobjt.getString("ID_Items"),
                                            jobjt.getString("ItemName"),
                                            jobjt.getString("SalesPrice"),
                                            jobjt.getString("MRP"),
                                            jobjt.getString("ID_Stock"),
                                            jobjt.getString("CurrentStock"),
                                            jobjt.getString("RetailPrice"),
                                            jobjt.getString("PrivilagePrice"),
                                            jobjt.getString("WholesalePrice"),
                                            jobjt.getString("GST"),
                                            jobjt.getString("CESS"),
                                            jobjt.getString("OrderQty"),
                                            jobjt.getString("Packed"),
                                            jobjt.getString("Description"),
                                            IMAGEURL+jobjt.getString("ImageName")
                                    ));
                                }

                                SharedPreferences DeliveryChargepref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF46, 0);
                                Intent in = new Intent(OrderDetailsActivity.this, ReOrderCartActivity.class);


                                in.putExtra("ID_SalesOrder", order_id);
                                in.putExtra("order_id", order_id);
                                in.putExtra("OrderNo", OrderNo);
                                in.putExtra("FK_SalesOrder","FK_SalesOrder");
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
                                in.putExtra("MinimumDeliveryAmount", MinimumDeliveryAmount);
                                in.putExtra("DeliveryCharge",  DeliveryChargepref.getString("DeliveryCharge", null));



//                            in.putExtra("ID_Store", ID_Store);
//                            in.putExtra("ShopType", ShopType);
//                            in.putExtra("ID_SalesOrder", order_id);
//                            in.putExtra("ID_CustomerAddress", ID_CustomerAddress);
//                            in.putExtra("OrderType", OrderType);
//                            in.putExtra("DeliveryCharge", DeliveryChargepref.getString("DeliveryCharge", null));
                                startActivity(in);
                            }else {

                                Log.e(TAG,"884    "+jobj.getString("ResponseMessage"));
                                Toast.makeText(getApplicationContext(),""+jobj.getString("ResponseMessage"),Toast.LENGTH_SHORT).show();
                            }



                        } catch (JSONException e) {
                            Log.e(TAG,"JSONException  881   "+e);
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
            Toast.makeText(getApplicationContext(),Somethingwentwrong+"!",Toast.LENGTH_LONG).show();
        }
        }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
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
                    Intent io = new Intent(OrderDetailsActivity.this, OutShopActivity.class);
                    io.putExtra(fromCategory, valueCategory);
                    finish();
                    startActivity(io);
                    return true;
                case R.id.navigation_cart:
                    //Log.e(TAG,"navigation_cart    ");
                    Intent ic = new Intent(OrderDetailsActivity.this, CartActivity.class);
                    ic.putExtra(fromCart, valueCart);
                    startActivity(ic);
                    finish();
                    //   onBackPressed();
                    return true;
                case R.id.navigation_home:
                    //Log.e(TAG,"navigation_home    ");
                    Intent iha = new Intent(OrderDetailsActivity.this, HomeActivity.class);
                    iha.putExtra(fromFavorite, valueFavorite);
                    startActivity(iha);
                    return true;
                case R.id.navigation_favorite:
                   // Log.e(TAG,"navigation_favorite    ");
                    Intent ifa = new Intent(OrderDetailsActivity.this, FavouriteActivity.class);
                    ifa.putExtra(fromFavorite, valueFavorite);
                    startActivity(ifa);
                    finish();
                    return true;

                case R.id.navigation_quit:
                //    Log.e(TAG,"navigation_quit    ");
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
            AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) OrderDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.exit_back_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);

            TextView tv_popupdelete = (TextView) layout.findViewById(R.id.tv_popupdelete);
            TextView tv_popupdeleteinfo3 = (TextView) layout.findViewById(R.id.tv_popupdeleteinfo3);
            TextView edit = (TextView) layout.findViewById(R.id.edit);
            TextView tvno = (TextView) layout.findViewById(R.id.tvno);



            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
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



    private void setProgress() {

//        final int[] progress = {0};
//        final Handler mHandler = new Handler();
//
//        Runnable runnable = new Runnable() {
//            public void run() {
//
//                progress[0] = progressBar.getProgress() + 1;
//                progressBar.setProgress(progress[0]);
//                if (progress[0] > 10) {
//                  //  progressDeterminate.setProgress(0)
//                }
//                mHandler.postDelayed(this, 10);
//            }
//        };
//
//        mHandler.post(runnable);




    }


    private void setTrackOrder(String status) {

        Log.e(TAG,"status  1259  "+status);
        pb_confirmed = (ProgressBar)findViewById(R.id.pb_confirmed) ;
        pb_packed = (ProgressBar)findViewById(R.id.pb_packed) ;
        pb_delivered = (ProgressBar)findViewById(R.id.pb_delivered) ;


        img_conf = (ImageView) findViewById(R.id.img_conf) ;
        img_pack = (ImageView) findViewById(R.id.img_pack) ;
        img_deli = (ImageView) findViewById(R.id.img_deli) ;

        if (status.equals("Confirmed") || status.equals("Packed") || status.equals("Delivered")){

            setProgressConfirm(status);
        }

    }

    private void setProgressConfirm(String status) {

        final int[] progress = {0};
        final Handler mHandler = new Handler();

        Runnable runnable = new Runnable() {
            public void run() {

                progress[0] = pb_confirmed.getProgress() + 1;
                pb_confirmed.setProgress(progress[0]);
                if (progress[0] == 11) {
                    //  progressDeterminate.setProgress(0)
                    img_conf.setBackground(getDrawable(R.drawable.img_confirm));
//                    Log.e(TAG,"12661    "+pb_confirmed.getProgress()  );
                    if (status.equals("Packed") || status.equals("Delivered")){

                        setProgressPacked(status);
                    }
                    mHandler.removeCallbacksAndMessages(null);
                }
                mHandler.postDelayed(this, 25);
            }
        };

        mHandler.post(runnable);

    }

    private void setProgressPacked(String status) {

        final int[] progress1 = {0};
        final Handler mHandler = new Handler();

        Runnable runnable = new Runnable() {
            public void run() {
//                Log.e(TAG,"12662    "+pb_packed.getProgress()  );
                progress1[0] = pb_packed.getProgress() + 1;
                pb_packed.setProgress(progress1[0]);
                if (progress1[0] == 11) {
                    img_pack.setBackground(getDrawable(R.drawable.img_pack));
                    //  progressDeterminate.setProgress(0)
                    if (status.equals("Delivered")){

                        setProgressDelivered(status);
                    }
                    mHandler.removeCallbacksAndMessages(null);
                }
                mHandler.postDelayed(this, 25);
            }
        };

        mHandler.post(runnable);
    }

    private void setProgressDelivered(String status) {

        final int[] progress2 = {0};
        final Handler mHandler = new Handler();

        Runnable runnable = new Runnable() {
            public void run() {

                progress2[0] = pb_delivered.getProgress() + 1;
                pb_delivered.setProgress(progress2[0]);
                if (progress2[0] == 11) {
                    //  progressDeterminate.setProgress(0)
                  //  if (status.equals("Delivered")){
                        img_deli.setBackground(getDrawable(R.drawable.img_del));
                     //   setProgressDelivered(status);
                    mHandler.removeCallbacksAndMessages(null);
                  //  }
                }
                mHandler.postDelayed(this, 25);
            }
        };

        mHandler.post(runnable);
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

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(OrderDetailsActivity.this)) {
            String str = data.get(0);
          //  Log.e(TAG,"OrderNumber_s      2737      "+OrderNumber_s);
            Log.e(TAG,"FK_SalesOrder      2737      "+FK_SalesOrder);
            Log.e("UPIPAY", "upiPaymentDataOperation1: " + data);
            Log.e("UPIPAY", "upiPaymentDataOperation2: " + str);
            String[] separated = str. split("&");



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

//                Toast.makeText(OrderDetailsActivity.this, Transactionsuccessful.getString("Transactionsuccessful", "")+".", Toast.LENGTH_SHORT).show();
//                Log.e("UPI", "payment successfull: " + approvalRefNo);
//                startActivity(new Intent(OrderDetailsActivity.this, ThanksActivity.class));
//                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
//                Intent intent = new Intent(OrderDetailsActivity.this,ThanksActivity.class);
//                intent.putExtra("StoreName", storeName);
//                intent.putExtra("OrderNumber",OrderNumber_s);
//                intent.putExtra("FK_SalesOrder",FK_SalesOrder);
//                intent.putExtra("strPaymenttype",strPaymenttype);
//                intent.putExtra("finalamount",finalamount);
//
//
//                startActivity(intent);
//
//                finish();

                String[] septxnId = separated[0]. split("=");
                String[] septxnRef = separated[1]. split("=");
                String[] sepStatus = separated[2]. split("=");
                String[] sepresponseCode = separated[3]. split("=");

                Log.e("septxnId", "upiPaymentDataOperation31: " + septxnId[1]);
                Log.e("septxnRef", "upiPaymentDataOperation32: " + septxnRef[1]);
                Log.e("sepStatus", "upiPaymentDataOperation33: " + sepStatus[1]);
                Log.e("sepresponseCode", "upiPaymentDataOperation33: " + sepresponseCode[1]);


                updatePayments(OrderNumber_s,FK_SalesOrder,strPaymentId,septxnId[1],"0","","0",finalamount,"0");

            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(OrderDetailsActivity.this, Paymentcancelledbyuser.getString("Paymentcancelledbyuser","")+".", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: " + approvalRefNo);
                AlertMessage(Paymentcancelledbyuser.getString("Paymentcancelledbyuser",""));

            } else {
                Toast.makeText(OrderDetailsActivity.this, TransactionfailedPleasetryagain.getString("TransactionfailedPleasetryagain","")+".", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: " + approvalRefNo);
                AlertMessage(TransactionfailedPleasetryagain.getString("TransactionfailedPleasetryagain",""));
            }
        } else {
            Log.e("UPI", "Internet issue: ");
            SharedPreferences Nointernetconnection = getApplicationContext().getSharedPreferences(Config.SHARED_PREF282, 0);
            Toast.makeText(OrderDetailsActivity.this, Nointernetconnection.getString("Nointernetconnection",""), Toast.LENGTH_SHORT).show();
        }
    }


    private void AlertMessage(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetailsActivity.this);
        builder.setMessage(""+msg)
                .setCancelable(false)
                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(OrderDetailsActivity.this, HomeActivity.class));
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void startBillDesk(String finalamount) {




        try {


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


            Log.e(TAG,"1640      strPaymenttype  "+strPaymenttype+"     finalamount      "+finalamount);

            SharedPreferences SharesstrPaymenttype = getApplicationContext().getSharedPreferences(Config.SHARED_PREF380, 0);
            SharedPreferences.Editor strPaymenttypeeditor = SharesstrPaymenttype.edit();
            strPaymenttypeeditor.putString("strPaymenttype", strPaymenttype);
            strPaymenttypeeditor.commit();

            SharedPreferences Sharesfinalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF381, 0);
            SharedPreferences.Editor finalamounteditor = Sharesfinalamount.edit();
            finalamounteditor.putString("finalamount", finalamount);
            finalamounteditor.commit();


//            if (MerchantID.equals("null")){
//                Log.e(TAG,"MerchantID   1614   "+MerchantID);
//            }else {
//                VerifyChecksum();
//            }

            Log.e("TransactionID ","   1622    "+TransactionID);
            Log.e("MerchantID ","   1622    "+MerchantID);
            Log.e("SecurityID ","   1622    "+SecurityID);
            VerifyChecksum(finalamount);


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

    private void VerifyChecksum(String finalamount) {
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

                    SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF4, 0);
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);

                    requestObject1.put("MerchantID", Utils.encryptStart(MerchantID));
                    requestObject1.put("Amount",Utils.encryptStart(finalamount));
                    requestObject1.put("SecurityID", Utils.encryptStart(SecurityID));
                    requestObject1.put("MobileNumber", Utils.encryptStart(pref4.getString("userphoneno", null)));
                    requestObject1.put("UserID", Utils.encryptStart(pref1.getString("userid", null)));
                    requestObject1.put("CustName", Utils.encryptStart(pref3.getString("username", null)));
                    requestObject1.put("StoreName", Utils.encryptStart(storeName));

                    requestObject1.put("Fk_PaymentMethod", Utils.encryptStart(strPaymentId));
                    requestObject1.put("ID_SalesOrder", Utils.encryptStart(FK_SalesOrder));

                    Log.e(TAG,"requestObject1   1681    "+strPaymentId);

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
                                Intent sdkIntent = new Intent(OrderDetailsActivity.this, PaymentOptions.class);
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


                    Log.e(TAG,"requestObject1   2587    "+requestObject1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.PaymentDetailUpdate(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG,"response   16752  "+response.body());


//							{"GateWayResult":{"Status":true,"TransDateTime":"25-08-2021 11:14:31","TransType":"01","TransactionId":"BDSK11112","TranStatus":"0300",
//									"TranAmnt":"00000002.00","ResponseCode":"0","ResponseMessage":"Transaction Verified"},"StatusCode":0,"EXMessage":null}


                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("SalesOrderDetails");
                            if(jObject.getString("StatusCode").equals("0")){

                                Log.e(TAG,"authStatus   567 1   "+authStatuss+"   ");
                                startActivity(new Intent(OrderDetailsActivity.this, ThanksActivity.class));
                                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                Intent intent = new Intent(OrderDetailsActivity.this,ThanksActivity.class);
                                intent.putExtra("StoreName", pref1.getString("StoreName", null));
                                intent.putExtra("OrderNumber",jobj.getString("OrderNumber"));
                                intent.putExtra("strPaymenttype",strPaymenttype);
                                intent.putExtra("finalamount",finalamount);
//
//
                                startActivity(intent);

                                finish();


                            }else {
                                AlertDialog.Builder builder= new AlertDialog.Builder(OrderDetailsActivity.this);
                                builder.setMessage(jobj.getString("ResponseMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                               // startActivity(new Intent(OrderDetailsActivity.this, HomeActivity.class));
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


    @Override
    public void onClick(int position, String paymentName) {

        Log.e(TAG,"4616  "+position+ "  "+paymentName);

        try {
            JSONObject jsonObject = jsonArrayPay.getJSONObject(position);
            strPaymenttype=jsonObject.getString("PaymentName");
            // strPaymentId=jsonObject.getString("ID_PaymentMethod");
            IsOnlinePay = jsonObject.getString("IsOnlinePay");
            MerchantID =  jsonObject.getString("MerchantID");
            TransactionID =  jsonObject.getString("TransactionID");
            SecurityID =  jsonObject.getString("SecurityID");
            strPaymentId = jsonObject.getString("ID_PaymentMethod");
            //  getMerchantKeys();
        } catch (JSONException e) {
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





