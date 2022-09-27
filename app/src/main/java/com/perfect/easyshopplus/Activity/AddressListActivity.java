package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intuz.addresspicker.LocationPickerActivity;
import com.perfect.easyshopplus.Adapter.AddressListAdapter;
import com.perfect.easyshopplus.Adapter.AreaListAdapter;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.Adapter.ReOrderItemListAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.AreaModel;
import com.perfect.easyshopplus.Model.FavModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

public class AddressListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener   {

    String TAG = "AddressListActivity";
    double currentLatitude,currentLongitude;
    private static final int ADDRESS_PICKER_REQUEST = 1020;
    boolean changing = false;
    RecyclerView rv_favlist;
    EditText etSearch, etsearch;
    ImageView im, imcart, im_notification;
    Button btnAddAddress;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvuser, tvcart, tv_notification,tv_address, tv_Thereisnoaddresslisttoshow, tv_popuptitle, tv_PermanentAddress, tv_ShippingAddresslist;
    ArrayList<FavModel> favlist = new ArrayList<>();
    DBHandler db;
    LinearLayout emptyAddress,ll_perAddress,ll_shipAddress;
    RelativeLayout rl_notification;
    ProgressDialog progressDialog;
    BottomNavigationView navigation;
    String fromCategory = "from", valueCategory = "home", fromCart = "From", valueCart = "Home", fromHome = "", valueHome = "", fromFavorite = "From", valueFavorite = "Home";
    ListView list_view;
    AreaListAdapter sadapter;
    int textlength = 0;
    ArrayList<AreaModel> areaArrayList = new ArrayList<>();
    public static ArrayList<AreaModel> array_sort= new ArrayList<>();
    String areaId,MinimumDeliveryAmount, idstore="null", addressDelID ,addressDel ,name ,pin ,mobNumber , From,ID_Store, ShopType,ID_CustomerAddress,OrderType, deliveryCharge ;
    String Image,DeliveryCharge,ID_SalesOrder,order_id,Id_order,orderDate,deliveryDate,status,itemcount,storeName, strAddress,OrderNo;

    EditText address;
    private File fileimage = null;
    String strfileimage = "null";
    TextView tv_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_addresslist_main);


        Intent in = getIntent();
        From = in.getStringExtra("From");
        Log.e(TAG, "PrescriptionImage  from  129   " + From);

        if (From.equals("reorder")) {
//            ID_Store = in.getStringExtra("ID_Store");
//            ShopType = in.getStringExtra("ShopType");
//            ID_CustomerAddress = in.getStringExtra("ID_CustomerAddress");
//            OrderType = in.getStringExtra("OrderType");
//            DeliveryCharge = in.getStringExtra("DeliveryCharge");
            if (in.getStringExtra("destination")!=null) {

                fileimage = (File)in.getExtras().get("destination");
             //   fileimage = new File(in.getStringExtra("destination"));
               // strfileimage = fileimage.toString();
            }

            Image = in.getStringExtra("Image");

            ID_SalesOrder = in.getStringExtra("ID_SalesOrder");
            order_id = in.getStringExtra("order_id");
            OrderNo = in.getStringExtra("OrderNo");
            Id_order = in.getStringExtra("Id_order");
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
        }
        else if (From.equals("shopping")){
            Image = in.getStringExtra("Image");
            if ((File)in.getExtras().get("destination")!=null) {
//                fileimage = new File(in.getStringExtra("destination"));
//                strfileimage = fileimage.toString();
                fileimage = (File)in.getExtras().get("destination");
            }else {
                Log.e(TAG, "PrescriptionImage  163   " + fileimage);
            }
        }

        if ((File)in.getExtras().get("destination")!=null) {
//            fileimage = new File(in.getStringExtra("destination"));
//            strfileimage = fileimage.toString();
            fileimage = (File)in.getExtras().get("destination");
        }
        Log.e(TAG, "PrescriptionImage  165   " + fileimage);
        //Log.e(TAG, "PrescriptionImage  165   " + strfileimage);

        initiateViews();
        setRegViews();
        setHomeNavMenu1();

       /* SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/
        getAddressList();

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF38, 0);
        ImageView imLogo=findViewById(R.id.imLogo);
        String strImagepath1= IMAGEURL + pref4.getString("LogoImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath1).into(imLogo);

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
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
        idstore = pref1.getString("ID_Store", null);

        SharedPreferences pref00 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF20, 0);
        addressDelID =pref00.getString("DeliAddressID", null);

        SharedPreferences dcpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF46, 0);
        deliveryCharge =dcpref.getString("DeliveryCharge", null);

        SharedPreferences AddressList = getApplicationContext().getSharedPreferences(Config.SHARED_PREF271, 0);
        tv_header.setText(""+AddressList.getString("AddressList",null));

        SharedPreferences pref01 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF19, 0);
        addressDel =pref01.getString("DeliAddress", null);
        if(addressDel==null||addressDel.isEmpty()){
            ll_perAddress.setVisibility(View.GONE);
        }
        else{
            ll_perAddress.setVisibility(View.VISIBLE);
            SharedPreferences pref02 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF18, 0);
            name =pref02.getString("DeliUsername", null);
            SharedPreferences pref03 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF21, 0);
            pin =pref03.getString("DeliPincode", null);
            SharedPreferences pref04 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF25, 0);
            mobNumber =pref04.getString("DeliMobNumb", null);
          //  tv_address.setText(name +"\n"+addressDel+"\nPIN : "+pin +"\nMOB NUM : +91"+mobNumber);
            if (mobNumber.length()!=0){
//                tv_address.setText(name +"\n"+addressDel+"\nPIN : "+pin +"\nMOB NUM : +91"+mobNumber);
                tv_address.setText(name +"\n"+addressDel+"\nMOB NUM : +91"+mobNumber);
            }
            else{
//                tv_address.setText(name +"\n"+addressDel+"\nPIN : "+pin);
                tv_address.setText(name +"\n"+addressDel);
            }


        }
        spValues();

        setBottomBar();

    }

    private void initiateViews() {
        rv_favlist=(RecyclerView)findViewById(R.id.rv_favlist);
        etSearch=(EditText)findViewById(R.id.etSearch);
        imcart=(ImageView) findViewById(R.id.imcart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        im = (ImageView) findViewById(R.id.im);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        tv_address = (TextView) findViewById(R.id.tv_address);
        emptyAddress=(LinearLayout) findViewById(R.id.emptyAddress);
        ll_perAddress=(LinearLayout) findViewById(R.id.ll_perAddress);
        ll_shipAddress=(LinearLayout) findViewById(R.id.ll_shipAddress);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        btnAddAddress = (Button) findViewById(R.id.btnAddAddress);

        tv_PermanentAddress =  findViewById(R.id.tv_PermanentAddress);
        tv_ShippingAddresslist =  findViewById(R.id.tv_ShippingAddresslist);
        tv_Thereisnoaddresslisttoshow =  findViewById(R.id.tv_Thereisnoaddresslisttoshow);

        tv_header = findViewById(R.id.tv_header);
    }

    private void setRegViews() {
        im.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        imcart.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        btnAddAddress.setOnClickListener(this);
        tv_address.setOnClickListener(this);
    }

    private void spValues(){

        SharedPreferences addaddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF203, 0);
        btnAddAddress.setText(addaddress.getString("addaddress", ""));

        SharedPreferences PermanentAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF274, 0);
        tv_PermanentAddress.setText(PermanentAddress.getString("PermanentAddress", ""));

        SharedPreferences ShippingAddresslist = getApplicationContext().getSharedPreferences(Config.SHARED_PREF275, 0);
        tv_ShippingAddresslist.setText(ShippingAddresslist.getString("ShippingAddresslist", ""));

        SharedPreferences Thereisnoaddresslisttoshow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF272, 0);
        tv_Thereisnoaddresslisttoshow.setText(Thereisnoaddresslisttoshow.getString("Thereisnoaddresslisttoshow", ""));
    }

    public void getAddressList(){
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
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    requestObject1.put("FK_Customer", pref1.getString("userid", null));
                    requestObject1.put("ReqMode", "17");
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1  347  "+requestObject1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getCusAddress(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            Log.e(TAG,"response  347  "+response.body());
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jmember = jObject.getJSONObject("CustomerAddressDetails");
                            String jstatus = jObject.getString("StatusCode");
                            if(jstatus.equals("0")){

                                ll_shipAddress.setVisibility(View.VISIBLE);
                                emptyAddress.setVisibility(View.GONE);

                                JSONArray CustomerAddressList = jmember.getJSONArray("CustomerAddressList");
                                GridLayoutManager lLayout = new GridLayoutManager(getApplicationContext(),1);
                                lLayout.setOrientation(GridLayoutManager.VERTICAL);
                                rv_favlist.setLayoutManager(lLayout);
                                rv_favlist.setHasFixedSize(true);
//                                AddressListAdapter adapter = new AddressListAdapter(AddressListActivity.this, CustomerAddressList, From,
//                                        ID_Store, ShopType,ID_CustomerAddress,OrderType,Image,DeliveryCharge);
                                AddressListAdapter adapter = new AddressListAdapter(AddressListActivity.this, CustomerAddressList,From, ID_SalesOrder, order_id, deliveryDate, Id_order, orderDate, status, ID_Store, ShopType,itemcount,ID_CustomerAddress,OrderType, storeName,DeliveryCharge,fileimage);

                                rv_favlist.setAdapter(adapter);
                            }
                            else{

                                ll_shipAddress.setVisibility(View.GONE);
                                SharedPreferences pref01 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF19, 0);
                                addressDel =pref01.getString("DeliAddress", null);
                                if(addressDel==null||addressDel.isEmpty()){
                                    emptyAddress.setVisibility(View.VISIBLE);
                                }
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
                onBackPressed();                break;
            case R.id.etSearch:
                startActivity(new Intent(AddressListActivity.this,SearchActivity.class));
                break;
            case R.id.imcart:
                Intent i = new Intent(AddressListActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.tvcart:
                Intent in = new Intent(AddressListActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.tv_notification:
                startActivity(new Intent(AddressListActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(AddressListActivity.this, NotificationActivity.class));
                break;
            case R.id.tv_address:
                setSPAddress();

                if(From.equals("reorder")) {
                    Log.e(TAG,"PrescriptionImage   471   ");
                    //startActivity(new Intent(AddressListActivity.this, CheckoutReorderHomeDeliveryActivity.class));
                    Intent intn = new Intent(this, CheckoutReorderHomeDeliveryActivity.class);
//                    intn.putExtra("ID_Store", ID_Store);
//                    intn.putExtra("ShopType", ShopType);
//                    intn.putExtra("OrderType", OrderType);
//                    intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);
//                    intn.putExtra("DeliveryCharge", DeliveryCharge);


                    intn.putExtra("order_id", order_id);
                    intn.putExtra("deliveryDate", deliveryDate);
                    intn.putExtra("OrderNo",OrderNo);
                    intn.putExtra("Id_order", Id_order);
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

                    intn.putExtra("destination", fileimage);
                    intn.putExtra("Image", Image);
                    startActivity(intn);
                }else{
                    Log.e(TAG,"PrescriptionImage   497   ");
                   // startActivity(new Intent(AddressListActivity.this, AddressAddActivty.class));
                    Intent intn = new Intent(this, AddressAddActivty.class);
                    intn.putExtra("destination", fileimage);
                    startActivity(intn);
                }
                break;
            case R.id.btnAddAddress:
                try {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddressListActivity.this);
                    LayoutInflater inflater1 = (LayoutInflater) AddressListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View layout = inflater1.inflate(R.layout.add_edit_address_popup, null);


                    SharedPreferences addaddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF203, 0);
                    SharedPreferences addressSP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF94, 0);
                    SharedPreferences CustomerNamesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF88, 0);
                    SharedPreferences Landmarksp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF96, 0);
                    SharedPreferences areasp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF223, 0);
                    SharedPreferences PhoneNosp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF276, 0);

                    SharedPreferences Getlocation = getApplicationContext().getSharedPreferences(Config.SHARED_PREF273, 0);
                    SharedPreferences OK = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
                    SharedPreferences Cancel = getApplicationContext().getSharedPreferences(Config.SHARED_PREF105, 0);


                    final TextView tv_popupchange = layout.findViewById(R.id.tv_popupchange);
                    tv_popupchange.setText(addaddress.getString("addaddress", null));

                    final EditText name = (EditText) layout.findViewById(R.id.etName);
                    name.setHint(CustomerNamesp.getString("CustomerName", null));

                    address = (EditText) layout.findViewById(R.id.etaddress);
                    address.setHint(addressSP.getString("address", null));

                    final EditText pinCode = (EditText) layout.findViewById(R.id.etPincode);

                    final EditText landmark = (EditText) layout.findViewById(R.id.etLandmark);
//                    String Landmark = Landmarksp.getString("Landmark", null);
                    landmark.setHint(Landmarksp.getString("LandmarkS", null));

                    final EditText area = (EditText) layout.findViewById(R.id.etArea);
                    area.setHint(areasp.getString("area", null));

                    final EditText phoneNumber = (EditText) layout.findViewById(R.id.etPhoneNumber);
                    phoneNumber.setHint(PhoneNosp.getString("PhoneNo", null));

                    final TextView pop_getlocation = layout.findViewById(R.id.pop_getlocation);
                    pop_getlocation.setText(Getlocation.getString("Getlocation", null));

                    final Button pop_changecancel = layout.findViewById(R.id.pop_changecancel);
                    pop_changecancel.setText(Cancel.getString("Cancel", null));

                    final Button btn_changesave = layout.findViewById(R.id.btn_changesave);
                    btn_changesave.setText(OK.getString("OK", null));



                    phoneNumber.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void afterTextChanged(Editable s) {
                            if (!changing && phoneNumber.getText().toString().startsWith("0")){
                                changing = true;
                                phoneNumber.setText(phoneNumber.getText().toString().replace("0", ""));
                            }
                            changing = false;
                        }
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                    });
                    area.setKeyListener(null);

                    pop_getlocation.setVisibility(View.VISIBLE);
//                    Button ok = (Button) layout.findViewById(R.id.btn_changesave);
//                    Button cancel = (Button) layout.findViewById(R.id.pop_changecancel);
                    builder.setView(layout);
                    final AlertDialog alertDialog = builder.create();
                    builder.setCancelable(false);
                    area.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getArea(area);
                        }
                    });
                    pop_getlocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(AddressListActivity.this, LocationPickerActivity.class);
                            startActivityForResult(intent, ADDRESS_PICKER_REQUEST);
                        }
                    });

                  //  Button ok = (Button) layout.findViewById(R.id.btn_changesave);
                  //  Button cancel = (Button) layout.findViewById(R.id.pop_changecancel);
                 //   builder.setView(layout);
                   // final AlertDialog alertDialog = builder.create();
                    //builder.setCancelable(false);
                  /*  area.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getArea(area);
                        }
                    });*/
                    pop_changecancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();
                        }
                    });
                    btn_changesave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(name.getText().toString().length()!=0){
                                if(address.getText().toString().length()!=0){
                                   // if(pinCode.getText().toString().length()==6){
                                        if(area.getText().toString().length()!=0){
                                            if(landmark.getText().toString().length()!=0){
                                                if(phoneNumber.getText().toString().length()!=0){
                                                  //  if(phoneNumber.getText().toString().length()<10){
                                                        alertDialog.dismiss();
                                                        String phone = phoneNumber.getText().toString();
                                                        if(phone.contains("+")){
                                                            phone = phone.replace("+","");
                                                        }
                                                        addAddress(name.getText().toString(),address.getText().toString(),pinCode.getText().toString(),
                                                                landmark.getText().toString(),areaId,phone);
                                                   /* }
                                                    else{
                                                        phoneNumber.setError("Please enter valid mobile number.");
                                                    }*/
                                                }
                                                else{
                                                    alertDialog.dismiss();
                                                    String phone = phoneNumber.getText().toString();
                                                    if(phone.contains("+")){
                                                        phone = phone.replace("+","");
                                                    }
                                                    addAddress(name.getText().toString(),address.getText().toString(),pinCode.getText().toString(),
                                                            landmark.getText().toString(),areaId,phone);                                                }
                                            }
                                            else{
                                                SharedPreferences Pleaseenterlandmark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF297, 0);
                                                landmark.setError(""+Pleaseenterlandmark.getString("Pleaseenterlandmark",null));
                                            }
                                        }
                                        else{
                                            SharedPreferences Pleaseselectarea = getApplicationContext().getSharedPreferences(Config.SHARED_PREF296, 0);
                                            area.setError(""+Pleaseselectarea.getString("Pleaseselectarea",null));
                                        }
                                 /*   } else{
                                        pinCode.setError("Please enter valid pin code.");
                                    }*/
                                }
                                else{
                                    SharedPreferences Pleaseenteraddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF295, 0);
                                    address.setError(""+Pleaseenteraddress.getString("Pleaseenteraddress",null));
                                }
                            }
                            else{
                                SharedPreferences Pleaseentername = getApplicationContext().getSharedPreferences(Config.SHARED_PREF294, 0);
                                name.setError(""+Pleaseentername.getString("Pleaseentername",null));
                            }
                         /*   alertDialog.dismiss();
                            addAddress(name.getText().toString(),address.getText().toString(),pinCode.getText().toString(),
                                    landmark.getText().toString(),areaId,phoneNumber.getText().toString());
                  */
                        }
                    });
                    alertDialog.show();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void setSPAddress() {
        SharedPreferences nameDel = getApplicationContext().getSharedPreferences(Config.SHARED_PREF18, 0);
        SharedPreferences.Editor nameeditor = nameDel.edit();
        nameeditor.putString("DeliUsername", name);
        nameeditor.commit();
        SharedPreferences Address = getApplicationContext().getSharedPreferences(Config.SHARED_PREF19, 0);
        SharedPreferences.Editor Addresseditor = Address.edit();
        Addresseditor.putString("DeliAddress", addressDel);
        Addresseditor.commit();
        SharedPreferences AddressId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF20, 0);
        SharedPreferences.Editor AddresseditorId = AddressId.edit();
        AddresseditorId.putString("DeliAddressID",addressDelID);
        AddresseditorId.commit();
        SharedPreferences pincode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF21, 0);
        SharedPreferences.Editor pincodeeditor = pincode.edit();
        pincodeeditor.putString("DeliPincode", pin);
        pincodeeditor.commit();
        SharedPreferences area = getApplicationContext().getSharedPreferences(Config.SHARED_PREF22, 0);
        SharedPreferences.Editor areaeditor = area.edit();
        areaeditor.putString("DeliArea", "");
        areaeditor.commit();
        SharedPreferences AreaId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF23, 0);
        SharedPreferences.Editor AreaIdeditor = AreaId.edit();
        AreaIdeditor.putString("DeliAreaID", "");
        AreaIdeditor.commit();
        SharedPreferences landmark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF24, 0);
        SharedPreferences.Editor landmarkeditor = landmark.edit();
        landmarkeditor.putString("DeliLandmark", "");
        landmarkeditor.commit();
        SharedPreferences mobNumb = getApplicationContext().getSharedPreferences(Config.SHARED_PREF25, 0);
        SharedPreferences.Editor mobNumbeditor = mobNumb.edit();
        mobNumbeditor.putString("DeliMobNumb", mobNumber);
        mobNumbeditor.commit();
        SharedPreferences DeliveryCharge = getApplicationContext().getSharedPreferences(Config.SHARED_PREF46, 0);
        SharedPreferences.Editor DeliveryChargeeditor = DeliveryCharge.edit();
        DeliveryChargeeditor.putString("DeliveryCharge", deliveryCharge);

        DeliveryChargeeditor.commit();
    }


    private void addAddress(String name,String address,String pincode,String landmark,String areaId,String phoneNum){
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
                    requestObject1.put("ReqMode", "18");
                    requestObject1.put("ReqSubMode", "3");
                    requestObject1.put("CustomerAddress", address);
                    requestObject1.put("LandMark", landmark);
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    requestObject1.put("FK_Customer", pref1.getString("userid", null));
                    requestObject1.put("FK_Area",areaId);
                    requestObject1.put("ContactName",name);
                    requestObject1.put("ContactNumber",phoneNum);
                    requestObject1.put("PinCode",pincode);
                    requestObject1.put("CALattitude",currentLatitude);
                    requestObject1.put("CALongitude",currentLongitude);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getCustmerAddressChangeActions(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jmember = jObject.getJSONObject("CustomerAddressDetails");
                            String jstatus = jObject.getString("StatusCode");
                            JSONArray CustomerAddressList = jmember.getJSONArray("CustomerAddressList");
                            if(jstatus.equals("0")){
                                getAddressList();

                            }
                            else {
                                getAddressList();
                            }
//                            if (ResponseCode.equals("-1")){
//                                setAlert(jmember.getString("ResponseMessage"));
//                                etuser.setText("");
//                                etpass.setText("");
//                            }
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



    private void getArea(EditText area) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater1 = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.area_popup, null);
            list_view = (ListView) layout.findViewById(R.id.list_view);
            etsearch = (EditText) layout.findViewById(R.id.etsearch);
            tv_popuptitle = (TextView) layout.findViewById(R.id.tv_popuptitle);
            SharedPreferences selectarea = getApplicationContext().getSharedPreferences(Config.SHARED_PREF224, 0);
            tv_popuptitle.setText(selectarea.getString("selectarea", null));
//            tv_popuptitle.setText("Select Area");
            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            getAreaList(alertDialog, area);
            alertDialog.show();
        }catch (Exception e){e.printStackTrace();}
    }

    public void getAreaList(final AlertDialog alertDialog, final EditText area){
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
                    requestObject1.put("ReqMode", "15");
                    requestObject1.put("FK_Store", idstore);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1  837  "+requestObject1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getArea(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("AreaDetails");
                            if (jobj.getString("AreaDetailsList").equals("null")) {
                            } else {
                                JSONArray jarray = jobj.getJSONArray("AreaDetailsList");
                                array_sort = new ArrayList<>();
                                areaArrayList=new ArrayList<>();
                                for (int k = 0; k < jarray.length(); k++) {
                                    JSONObject jsonObject = jarray.getJSONObject(k);

                                    areaArrayList.add(new AreaModel(jsonObject.getString("FK_Area"),jsonObject.getString("AreaName")));
                                    array_sort.add(new AreaModel(jsonObject.getString("FK_Area"),jsonObject.getString("AreaName")));
                                }
                                sadapter = new AreaListAdapter(AddressListActivity.this, array_sort);
                                list_view.setAdapter(sadapter);
                                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                     @Override
                                                                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                         area.setText(array_sort.get(position).getAreaName());
                                                                         areaId=array_sort.get(position).getID_Area();
                                                                         alertDialog.dismiss();
                                                                     }
                                                                 }/*.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    }*/
                                );

                            }
                            etsearch.addTextChangedListener(new TextWatcher() {
                                public void afterTextChanged(Editable s) {
                                }
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    list_view.setVisibility(View.VISIBLE);
                                    textlength = etsearch.getText().length();
                                    array_sort.clear();
                                    //  areaArrayList=new ArrayList<>();
                                    //   array_sort = new ArrayList<>();
                                    for (int i = 0; i < areaArrayList.size(); i++) {
                                        if (textlength <= areaArrayList.get(i).getAreaName().length()) {
                                            if (areaArrayList.get(i).getAreaName().toLowerCase().trim().contains(
                                                    etsearch.getText().toString().toLowerCase().trim())) {
                                                array_sort.add(areaArrayList.get(i));
                                            }
                                        }
                                    }
                                    sadapter = new AreaListAdapter(AddressListActivity.this, array_sort);
                                    list_view.setAdapter(sadapter);
                                }
                            });
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
            Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
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
        final String[] menulist = new String[]{"Home","My Cart", "My Orders", "Favourites","Favourite Stores",
                "Notifications", "Shopping List","About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_store,R.drawable.ic_notifications,R.drawable.ic_list,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(AddressListActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(AddressListActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(AddressListActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(AddressListActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(AddressListActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(AddressListActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(AddressListActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(AddressListActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    startActivity(new Intent(AddressListActivity.this, AboutUsActivity.class));
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
        final String[] menulist = new String[]{"Home","My Cart", "Track Orders", "Favourite Items",
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
        NavMenuAdapter adapter = new NavMenuAdapter(AddressListActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(AddressListActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(AddressListActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(AddressListActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(AddressListActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(AddressListActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(AddressListActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(AddressListActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 9) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddressListActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) AddressListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivity(new Intent(AddressListActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 11) {
                    startActivity(new Intent(AddressListActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(AddressListActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(AddressListActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddressListActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) AddressListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.logout_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);
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
                    dologoutchanges();
                    if(getResources().getString(R.string.app_name).equals("JZT CART") || getResources().getString(R.string.app_name).equals("Pulikkottil Hypermarket") || getResources().getString(R.string.app_name).equals("NeethiMed")|| getResources().getString(R.string.app_name).equals("Touch n Buy")) {

                        startActivity(new Intent(AddressListActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(AddressListActivity.this,LocationActivity.class));
                        finish();}
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }
//                    startActivity(new Intent(AddressListActivity.this,LocationActivity.class));
//                    finish();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        finishAffinity();
//                    }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra("address") != null) {
                    strAddress = data.getStringExtra("address");
                    currentLatitude = data.getDoubleExtra("lat", 0.0);
                    currentLongitude = data.getDoubleExtra("long", 0.0);
                    Log.e(TAG,"1141     "+strAddress);

                    address.setText(""+strAddress);

                   // Toast.makeText(getApplicationContext(),"Latitude : "+currentLatitude+"\n"+"Longitude :"+currentLongitude,Toast.LENGTH_LONG).show();
                    //  txtAddress.setText("Address: "+address);
                    //  txtLatLong.setText("Lat:"+currentLatitude+"  Long:"+currentLongitude);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
                    Intent io = new Intent(AddressListActivity.this, OutShopActivity.class);
                    io.putExtra(fromCategory, valueCategory);
                    finish();
                    startActivity(io);
                    return true;
                case R.id.navigation_cart:
                    Intent ic = new Intent(AddressListActivity.this, CartActivity.class);
                    ic.putExtra(fromCart, valueCart);
                    startActivity(ic);
                    finish();
                    //   onBackPressed();
                    return true;
                case R.id.navigation_home:
                    Intent iha = new Intent(AddressListActivity.this, HomeActivity.class);
                    iha.putExtra(fromFavorite, valueFavorite);
                    startActivity(iha);
                    return true;
                case R.id.navigation_favorite:
                    Intent ifa = new Intent(AddressListActivity.this, FavouriteActivity.class);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(AddressListActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) AddressListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.exit_back_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);
            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    // navigation.getMenu().findItem(R.id.navigation_home).setChecked(false);
                    navigation.getMenu().setGroupCheckable(0, false, true);

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

