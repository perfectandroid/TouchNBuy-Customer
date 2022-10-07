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

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

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
import com.perfect.easyshopplus.Adapter.AreaListAdapter;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
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

public class ProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    EditText etSearch;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView name,tvname,tvemailid, tvphone, tvmemberid, btlogout, btchangepassword, tvuser, tvcart, tv_notification,tvstore,tvarea, tvadrss,tvlandmark,tv_header;
    ProgressDialog progressDialog;
    String st_oldpassword, st_newpassword;
    String userid, username, useremail, usermobile, memberid, address, PIN, addressId, Landmark/*,Store,AddressID*/,lan,add/*,Customer*/, AreaID, Area;
    DBHandler db;
    RelativeLayout rl_notification;
    LinearLayout lladrss,llstore,llarea,llandmark;

    String TAG = "ProfileActivity";
    BottomNavigationView navigation;
    String fromCategory = "from";
    String valueCategory = "home";
    String fromCart = "From";
    String valueCart = "Home";
    String fromHome = "";
    String valueHome = "";
    String fromFavorite = "From";
    String valueFavorite = "Home";







    double currentLatitude,currentLongitude;
    private static final int ADDRESS_PICKER_REQUEST = 1020;
    ArrayList<FavModel> favlist = new ArrayList<>();
    ListView list_view;
    EditText etsearch;
    AreaListAdapter sadapter;
    TextView tv_popuptitle;
    int textlength = 0;
    ArrayList<AreaModel> areaArrayList = new ArrayList<>();
    public static ArrayList<AreaModel> array_sort= new ArrayList<>();
    String idstore="null";







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_profile_main);

        ImageView imbanner=findViewById(R.id.iv_profilepic);
        Glide.with( this ).load( R.drawable.profilegif ).into( imbanner );
        setInitialise();

        setRegister();
        setBottomBar();
        setHomeNavMenu1();

       /* SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/
        getSharedPreferences();
        getProfileDetails();


        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
        ImageView imApplogo=findViewById(R.id.imApplogo);
        String strImagepath= IMAGEURL + pref3.getString("AppIconImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath).into(imApplogo);

        db=new DBHandler(this);
        tvcart.setText(String.valueOf(db.selectCartCount()+db.selectInshopCartCount()));
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));

        SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF221, 0);
        tv_header.setText(pref4.getString("myprofile", null));

        SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        tvname.setText(pref5.getString("username", null));

        SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF222, 0);
        btchangepassword.setText(pref6.getString("changepassword", null));

        SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF15, 0);
        tvadrss.setText(pref7.getString("Address", null));
    }

    private void setInitialise() {
        etSearch=(EditText)findViewById(R.id.etSearch);
        imcart=(ImageView) findViewById(R.id.imcart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        im = (ImageView) findViewById(R.id.im);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        tvname = (TextView) findViewById(R.id.tvname);
        tvemailid = (TextView) findViewById(R.id.tvemailid);
        tvphone = (TextView) findViewById(R.id.tvphone);
        tvmemberid = (TextView) findViewById(R.id.tvmemberid);
        btlogout = (TextView) findViewById(R.id.btlogout);
        btchangepassword = (TextView) findViewById(R.id.btchangepassword);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        tvadrss = (TextView) findViewById(R.id.tvadrss);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        tv_header= (TextView) findViewById(R.id.tv_header);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        lladrss = (LinearLayout) findViewById(R.id.lladrss);

        llstore = (LinearLayout) findViewById(R.id.llstore);
        llarea = (LinearLayout) findViewById(R.id.llarea);
        llandmark = (LinearLayout) findViewById(R.id.llandmark);

        tvarea = (TextView) findViewById(R.id.tvarea);
        tvlandmark = (TextView) findViewById(R.id.tvlandmark);
        tvstore = (TextView) findViewById(R.id.tvstore);
        name = (TextView) findViewById(R.id.name);
    }

    private void setRegister() {
        etSearch.setOnClickListener(this);
        imcart.setOnClickListener(this);
        im.setOnClickListener(this);
        btchangepassword.setOnClickListener(this);
        btlogout.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        name.setOnClickListener(this);
    }

    private void getSharedPreferences() {
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
        userid=pref1.getString("userid", null);
        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        username=pref2.getString("username", null);
        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF3, 0);
        useremail=pref3.getString("useremail", null);
        SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF4, 0);
        usermobile=pref4.getString("userphoneno", null);
        SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
        memberid=pref5.getString("memberid", null);
        SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF15, 0);
        address=pref6.getString("Address", null);
        Log.e(TAG,"address   243     "+address);
        SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF16, 0);
        PIN=pref7.getString("PIN", null);

        SharedPreferences pref8 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF17, 0);
        addressId=pref8.getString("AddressID", null);

        SharedPreferences pref9 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF28, 0);
        Landmark=pref9.getString("Landmark", null);



        SharedPreferences pref10 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF63, 0);
        Area =pref10.getString("Area", null);

        SharedPreferences pref11 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF64, 0);
        AreaID =pref11.getString("AreaID", null);

      /*  SharedPreferences pref10 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
        Store=pref10.getString("Store", null);

        SharedPreferences pref11 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF17, 0);
        AddressID=pref11.getString("AddressID", null);*/

       /* SharedPreferences pref12 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF30, 0);
        Customer=pref12.getString("FK_Customer", null);
        Log.i("Cust",Customer);*/
    }

    private void getProfileDetails() {
        tvuser.setText("Welcome "+username);
        tvname.setText(username);
        tvemailid.setText(useremail);
        tvphone.setText(usermobile);
        tvmemberid.setText(memberid);
        if(address.equals("")){
            lladrss.setVisibility(View.GONE);
        }else {
            lladrss.setVisibility(View.VISIBLE);
            if(PIN.equals(""))
            {
                tvadrss.setText(address);
            }
            else {
                tvadrss.setText(address + "\n Pin Code : " + PIN);
            }
        }

      /*  if(AreaId2.equals("")){
            llarea.setVisibility(View.GONE);
        }else {
            llarea.setVisibility(View.VISIBLE);
            tvarea.setText(AreaId2);
        }*/


     /*   if(Landmark!=null){
            llandmark.setVisibility(View.VISIBLE);
            tvlandmark.setText(Landmark);

        }else {
            llandmark.setVisibility(View.GONE);
        }*/

      /*  if(llstore.equals("")){
            llstore.setVisibility(View.GONE);
        }else {
            llstore.setVisibility(View.VISIBLE);
            tvstore.setText(Store);
        }*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im:

                //  drawer.openDrawer(Gravity.START);
                onBackPressed();                break;
            case R.id.etSearch:
                startActivity(new Intent(ProfileActivity.this,SearchActivity.class));
                break;
            case R.id.imcart:
                Intent i = new Intent(ProfileActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.tvcart:
                Intent in = new Intent(ProfileActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.btchangepassword:
                changePassword();
                break;
            case R.id.btlogout:
                doLogout();
                break;
            case R.id.name:
//                 editAddressPopup();

                editAddressPup();
                break;
            case R.id.tv_notification:
                startActivity(new Intent(ProfileActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(ProfileActivity.this, NotificationActivity.class));
                break;
        }
    }

    private void editAddressPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        LayoutInflater inflater1 = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater1.inflate(R.layout.add_edit_address_popup1, null);
        final TextView header = (TextView) layout.findViewById(R.id.tv_popupchange);
        header.setText("EDIT ADDRESS");
        final EditText landmark = (EditText) layout.findViewById(R.id.etLandmark);
        landmark.setText(Landmark);
        final EditText addrs = (EditText) layout.findViewById(R.id.etaddress);
        addrs.setText(address);
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
                add = addrs.getText().toString();
                lan = landmark.getText().toString();
                editAddress(lan,add);

                }
        });
        alertDialog.show();
    }

    private void editAddress(String lan, String add) {

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
                        requestObject1.put("ReqSubMode", "4");
                        requestObject1.put("ID_CustomerAddress", addressId);
                        requestObject1.put("CustomerAddress",add);
                        requestObject1.put("LandMark", lan);
                        requestObject1.put( "FK_Customer",userid);
                        requestObject1.put("FK_Area", "1");
                        requestObject1.put("PinCode", "56");
                        requestObject1.put("ContactName", "");
                        requestObject1.put("ContactNumber", "");
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
                                Log.i("Response",jObject.toString());

                                JSONObject jmember = jObject.getJSONObject("CustomerAddressDetails");
                                Log.i("First ",String.valueOf(jmember));
                                JSONObject object = new JSONObject(String.valueOf(jmember));
                                Log.i("First1 ",String.valueOf(object));
                                String jstatus = jObject.getString("StatusCode");
                                JSONArray Jarray  = object.getJSONArray("CustomerAddressList");
                                if(jstatus.equals("0")){
                                    for(int i=0; i<Jarray.length(); i++) {
                                        JSONObject jsonObj = Jarray.getJSONObject(i);

                                        // getting inner array Ingredients
                                        String id = jsonObj.getString("Address");
                                        String name=jsonObj.getString("LandMark");
                                        Log.i("Test",id+"\n"+name);
                                         tvadrss.setText(id);
                                         tvlandmark.setText(name);

                                    }
                                }

                               /* if(ResponseCode.equals("0")){
                                    setAlert( jmember.getString("ResponseMessage"));
                                    SharedPreferences Loginpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                    SharedPreferences.Editor logineditor = Loginpref.edit();
                                    logineditor.putString("loginsession", "Yes");
                                    logineditor.commit();


                                    SharedPreferences userid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                                    SharedPreferences.Editor userideditor = userid.edit();
                                    userideditor.putString("userid", jmember.getString("ID_Customer"));
                                    userideditor.commit();
                                    SharedPreferences username = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
                                    SharedPreferences.Editor usernameeditor = username.edit();
                                    usernameeditor.putString("username", jmember.getString("CusName"));
                                    usernameeditor.commit();
                                    SharedPreferences useremail = getApplicationContext().getSharedPreferences(Config.SHARED_PREF3, 0);
                                    SharedPreferences.Editor useremailideditor = useremail.edit();
                                    useremailideditor.putString("useremail", jmember.getString("CusEmail"));
                                    useremailideditor.commit();
                                    SharedPreferences userphoneno = getApplicationContext().getSharedPreferences(Config.SHARED_PREF4, 0);
                                    SharedPreferences.Editor userphoneeditor = userphoneno.edit();
                                    userphoneeditor.putString("userphoneno", jmember.getString("CusMobile"));
                                    userphoneeditor.commit();
                                    SharedPreferences memberid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                                    SharedPreferences.Editor memberideditor = memberid.edit();
                                    memberideditor.putString("memberid", jmember.getString("CusNumber"));
                                    memberideditor.commit();
                                    SharedPreferences FK_CustomerPlus = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                                    SharedPreferences.Editor FK_CustomerPluseditor = FK_CustomerPlus.edit();
                                    FK_CustomerPluseditor.putString("FK_CustomerPlus", jmember.getString("FK_Customer"));
                                    FK_CustomerPluseditor.commit();
                                    SharedPreferences Address = getApplicationContext().getSharedPreferences(Config.SHARED_PREF15, 0);
                                    SharedPreferences.Editor Addresseditor = Address.edit();
                                    Addresseditor.putString("Address", jmember.getString("Address"));
                                    Addresseditor.commit();
                                    SharedPreferences AddressId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF17, 0);
                                    SharedPreferences.Editor AddresseditorId = AddressId.edit();
                                    AddresseditorId.putString("AddressID", jmember.getString("ID_CustomerAddress"));
                                    AddresseditorId.commit();

                                    SharedPreferences AreaId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF18, 0);
                                    SharedPreferences.Editor AreaeditorId = AreaId.edit();
                                    AreaeditorId.putString("AreaId", jmember.getString("AreaName"));
                                    AreaeditorId.commit();

                                    SharedPreferences Landmark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF19, 0);
                                    SharedPreferences.Editor LandmarkeditorId = Landmark.edit();
                                    LandmarkeditorId.putString("Landmark", jmember.getString("LandMark"));
                                    LandmarkeditorId.commit();

                                    SharedPreferences Store = getApplicationContext().getSharedPreferences(Config.SHARED_PREF20, 0);
                                    SharedPreferences.Editor StoreId = Store.edit();
                                    StoreId.putString("Store", jmember.getString("StoreName"));
                                    StoreId.commit();


                                    startActivity(new Intent(CustRegistrationActivity.this,HomeActivity.class));
                                }*/
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
        final String[] menulist = new String[]{"Home","My Cart", "Track Orders", "Favourite Itemss",
                "Notifications","Shopping List", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications,R.drawable.ic_list,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ProfileActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ProfileActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ProfileActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ProfileActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(ProfileActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(ProfileActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 6) {
                    startActivity(new Intent(ProfileActivity.this, AboutUsActivity.class));
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
        NavMenuAdapter adapter = new NavMenuAdapter(ProfileActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ProfileActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ProfileActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ProfileActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(ProfileActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 5) {
                    startActivity(new Intent(ProfileActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(ProfileActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 9) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) ProfileActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater1.inflate(R.layout.rate_us_pop_up, null);
                        final Button rateUs = layout.findViewById(R.id.rate_us);
                        final TextView feedbk =layout.findViewById(R.id.wantFeedback);
                        final TextView lovethis =layout.findViewById(R.id.tv_lovethis);
                        builder.setView(layout);

                        final AlertDialog alertDialog = builder.create();
                        SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF238, 0);
                        feedbk.setText(pref4.getString("wewantyourfeedback", null));

                        SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF239, 0);
                        lovethis.setText(pref5.getString("lovethisapprateus", null));

                        SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF240, 0);
                        rateUs.setText(pref6.getString("ratenow", null));

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
                    startActivity(new Intent(ProfileActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 11) {
                    startActivity(new Intent(ProfileActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(ProfileActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(ProfileActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ProfileActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(ProfileActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(ProfileActivity.this,LocationActivity.class));
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

    private void changePassword(){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ProfileActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                       // Toast.makeText(getApplicationContext(), "Enter Old Password",Toast.LENGTH_LONG).show();

                    } else if (newpassword.getText().toString().isEmpty()) {
                        //newpassword.setError("Please provide your New Password.");

                        SharedPreferences EnterNewPasswordsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF347, 0);
                        String EnterNewPassword =EnterNewPasswordsp.getString("EnterNewPassword", null);
                        Toast.makeText(getApplicationContext(), EnterNewPassword+"",Toast.LENGTH_LONG).show();
//                        Toast.makeText(getApplicationContext(), "Enter New Password",Toast.LENGTH_LONG).show();
                    }   else {
                        alertDialog.dismiss();
                        st_oldpassword=oldpassword.getText().toString();
                        st_newpassword=newpassword.getText().toString();
                        doChangePassword();
                    }

                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                Log.e(TAG,"requestObject1  9061   "+requestObject1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
            Call<String> call = apiService.getChangePassword(body);
            call.enqueue(new Callback<String>() {
                @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    try {
                        progressDialog.dismiss();
                        Log.e(TAG,"response  9062   "+response.body());
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

    private void dologoutchanges(){
        Config.logOut(this);
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
                    Intent io = new Intent(ProfileActivity.this, OutShopActivity.class);
                    io.putExtra(fromCategory, valueCategory);
                    finish();
                    startActivity(io);
                    return true;
                case R.id.navigation_cart:
                    Log.e(TAG,"navigation_cart    ");
                    Intent ic = new Intent(ProfileActivity.this, CartActivity.class);
                    ic.putExtra(fromCart, valueCart);
                    startActivity(ic);
                    finish();
                    //   onBackPressed();
                    return true;
                case R.id.navigation_home:
                    Log.e(TAG,"navigation_home    ");
                    Intent iha = new Intent(ProfileActivity.this, HomeActivity.class);
                    iha.putExtra(fromFavorite, valueFavorite);
                    startActivity(iha);
                    return true;
                case R.id.navigation_favorite:
                    Log.e(TAG,"navigation_favorite    ");
                    Intent ifa = new Intent(ProfileActivity.this, FavouriteActivity.class);
                    ifa.putExtra(fromFavorite, valueFavorite);
                    startActivity(ifa);
                    finish();
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ProfileActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ProfileActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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












    private  void editAddressPup(){

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ProfileActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.edit_address_popup, null);
            final TextView tv_popupchange = (TextView) layout.findViewById(R.id.tv_popupchange);
            builder.setCancelable(false);

            final EditText addres = (EditText) layout.findViewById(R.id.etaddress);
            final EditText landmark = (EditText) layout.findViewById(R.id.etLandmark);
            final EditText area = (EditText) layout.findViewById(R.id.etArea);

            SharedPreferences editadress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF204, 0);
            SharedPreferences addressSp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF94, 0);
            SharedPreferences areaSp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF223, 0);
            SharedPreferences LandmarkSp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF96, 0);
            SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
            SharedPreferences CancelSp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF105, 0);
            SharedPreferences AreaIdSp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF64, 0);

            tv_popupchange.setText(editadress.getString("editadress",""));
            addres.setHint(addressSp.getString("address",""));
            area.setHint(areaSp.getString("area",""));
            landmark.setHint(LandmarkSp.getString("LandmarkS",""));

            Log.e(TAG,"Landmark   1149   "+LandmarkSp.getString("LandmarkS",""));
            AreaID = AreaIdSp.getString("AreaId","");
            Log.e(TAG,"AreaIdSp   11492   "+AreaIdSp.getString("AreaId",""));

            landmark.setText(Landmark);
            addres.setText(address);
            area.setText(Area);
            area.setKeyListener(null);
            Button pop_getlocation = (Button) layout.findViewById(R.id.pop_getlocation);
            pop_getlocation.setVisibility(View.GONE);
            Button ok = (Button) layout.findViewById(R.id.btn_changesave);
            Button cancel = (Button) layout.findViewById(R.id.pop_changecancel);
            ok.setText(OKsp.getString("OK",""));
            cancel.setText(CancelSp.getString("Cancel",""));



        //    ok.setText();
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
                    Intent intent = new Intent(ProfileActivity.this, LocationPickerActivity.class);
                    startActivityForResult(intent, ADDRESS_PICKER_REQUEST);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(addres.getText().toString().length()!=0){
                        if(area.getText().toString().length()!=0){
                            if(!AreaID.equals("0")){
                                if(landmark.getText().toString().length()!=0){
                                    alertDialog.dismiss();
                                    addAddress(addres.getText().toString(), landmark.getText().toString(), AreaID);
                                }
                                else{
                                    landmark.setError("Please enter landmark.");
                                }
                            }
                            else{
                                area.setError("Please select area.");
                            }
                        }
                        else{
                            area.setError("Please select area.");
                        }
                    }
                    else{
                        addres.setError("Please enter address.");
                    }
                }
            });
            alertDialog.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAddress(String address,String landmark,String areaId){
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



//                    requestObject1.put("ReqMode", "18");
//                    requestObject1.put("ReqSubMode", "4");
//                    requestObject1.put("ID_CustomerAddress", addressId);
//                    requestObject1.put("CustomerAddress",add);
//                    requestObject1.put("LandMark", lan);
//                    requestObject1.put( "FK_Customer",userid);
//                    requestObject1.put("FK_Area", "1");
//                    requestObject1.put("PinCode", "56");
//                    requestObject1.put("ContactName", "");
//                    requestObject1.put("ContactNumber", "");
//                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));

                    requestObject1.put("ReqMode", "18");
                    requestObject1.put("ReqSubMode", "4");
                    requestObject1.put("ID_CustomerAddress", addressId);
                    requestObject1.put("CustomerAddress", address);
                    requestObject1.put("LandMark", landmark);
                    requestObject1.put("FK_Customer", userid);
                    requestObject1.put("FK_Area",areaId);
                    requestObject1.put("ContactName","");
                    requestObject1.put("ContactNumber","");
                    requestObject1.put("PinCode","56");
                    requestObject1.put("CALattitude",currentLatitude);
                    requestObject1.put("CALongitude",currentLongitude);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1   1279    "+requestObject1);


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
                            Log.e(TAG,"Response  1290   "+response.body());


                            JSONObject jmember = jObject.getJSONObject("CustomerAddressDetails");
                            JSONObject object = new JSONObject(String.valueOf(jmember));
                            String jstatus = jObject.getString("StatusCode");
                            JSONArray Jarray  = object.getJSONArray("CustomerAddressList");
                            if(jstatus.equals("0")){
                                for(int i=0; i<Jarray.length(); i++) {
                                    JSONObject jsonObj = Jarray.getJSONObject(i);

                                    // getting inner array Ingredients
                                    String id = jsonObj.getString("Address");
                                    String name=jsonObj.getString("LandMark");
                                    tvadrss.setText(id);
                                    tvlandmark.setText(name);


                                    SharedPreferences Address = getApplicationContext().getSharedPreferences(Config.SHARED_PREF15, 0);
                                    SharedPreferences.Editor AddressEditor = Address.edit();
                                    AddressEditor.putString("Address", jsonObj.getString("Address"));
                                    AddressEditor.commit();

                                    SharedPreferences Areasp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF63, 0);
                                    SharedPreferences.Editor Areaeditor = Areasp.edit();
                                    Areaeditor.putString("Area", jsonObj.getString("AreaName"));
                                    Areaeditor.commit();

                                    SharedPreferences AreaId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF64, 0);
                                    SharedPreferences.Editor AreaeditorId = AreaId.edit();
                                    AreaeditorId.putString("AreaId", jsonObj.getString("FK_Area"));
                                    AreaeditorId.commit();

                                    SharedPreferences Landmark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF28, 0);
                                    SharedPreferences.Editor LandmarkeditorId = Landmark.edit();
                                    LandmarkeditorId.putString("Landmark", jsonObj.getString("LandMark"));
                                    LandmarkeditorId.commit();

                                    getSharedPreferences();


                                    Toast.makeText(getApplicationContext(),""+object.getString("ResponseMessage"),Toast.LENGTH_SHORT).show();


                                }
                            }

                               /* if(ResponseCode.equals("0")){
                                    setAlert( jmember.getString("ResponseMessage"));
                                    SharedPreferences Loginpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                    SharedPreferences.Editor logineditor = Loginpref.edit();
                                    logineditor.putString("loginsession", "Yes");
                                    logineditor.commit();


                                    SharedPreferences userid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                                    SharedPreferences.Editor userideditor = userid.edit();
                                    userideditor.putString("userid", jmember.getString("ID_Customer"));
                                    userideditor.commit();
                                    SharedPreferences username = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
                                    SharedPreferences.Editor usernameeditor = username.edit();
                                    usernameeditor.putString("username", jmember.getString("CusName"));
                                    usernameeditor.commit();
                                    SharedPreferences useremail = getApplicationContext().getSharedPreferences(Config.SHARED_PREF3, 0);
                                    SharedPreferences.Editor useremailideditor = useremail.edit();
                                    useremailideditor.putString("useremail", jmember.getString("CusEmail"));
                                    useremailideditor.commit();
                                    SharedPreferences userphoneno = getApplicationContext().getSharedPreferences(Config.SHARED_PREF4, 0);
                                    SharedPreferences.Editor userphoneeditor = userphoneno.edit();
                                    userphoneeditor.putString("userphoneno", jmember.getString("CusMobile"));
                                    userphoneeditor.commit();
                                    SharedPreferences memberid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                                    SharedPreferences.Editor memberideditor = memberid.edit();
                                    memberideditor.putString("memberid", jmember.getString("CusNumber"));
                                    memberideditor.commit();
                                    SharedPreferences FK_CustomerPlus = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                                    SharedPreferences.Editor FK_CustomerPluseditor = FK_CustomerPlus.edit();
                                    FK_CustomerPluseditor.putString("FK_CustomerPlus", jmember.getString("FK_Customer"));
                                    FK_CustomerPluseditor.commit();
                                    SharedPreferences Address = getApplicationContext().getSharedPreferences(Config.SHARED_PREF15, 0);
                                    SharedPreferences.Editor Addresseditor = Address.edit();
                                    Addresseditor.putString("Address", jmember.getString("Address"));
                                    Addresseditor.commit();
                                    SharedPreferences AddressId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF17, 0);
                                    SharedPreferences.Editor AddresseditorId = AddressId.edit();
                                    AddresseditorId.putString("AddressID", jmember.getString("ID_CustomerAddress"));
                                    AddresseditorId.commit();

                                    SharedPreferences AreaId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF18, 0);
                                    SharedPreferences.Editor AreaeditorId = AreaId.edit();
                                    AreaeditorId.putString("AreaId", jmember.getString("AreaName"));
                                    AreaeditorId.commit();

                                    SharedPreferences Landmark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF19, 0);
                                    SharedPreferences.Editor LandmarkeditorId = Landmark.edit();
                                    LandmarkeditorId.putString("Landmark", jmember.getString("LandMark"));
                                    LandmarkeditorId.commit();

                                    SharedPreferences Store = getApplicationContext().getSharedPreferences(Config.SHARED_PREF20, 0);
                                    SharedPreferences.Editor StoreId = Store.edit();
                                    StoreId.putString("Store", jmember.getString("StoreName"));
                                    StoreId.commit();


                                    startActivity(new Intent(CustRegistrationActivity.this,HomeActivity.class));
                                }*/
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
            tv_popuptitle.setText("Select Area");
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
                                sadapter = new AreaListAdapter(ProfileActivity.this, array_sort);
                                list_view.setAdapter(sadapter);
                                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                     area.setText(array_sort.get(position).getAreaName());
                                     AreaID =array_sort.get(position).getID_Area();
                                     alertDialog.dismiss();
                                    }
                                }
                                /*.OnItemClickListener() {
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
                                    sadapter = new AreaListAdapter(ProfileActivity.this, array_sort);
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
            SharedPreferences Nointernetconnection = getApplicationContext().getSharedPreferences(Config.SHARED_PREF282, 0);
            Toast.makeText(this, Nointernetconnection.getString("Nointernetconnection",""), Toast.LENGTH_SHORT).show();

//            Toast.makeText(this,"No internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADDRESS_PICKER_REQUEST) {
            try {
                if (data != null && data.getStringExtra("address") != null) {
                    // strAddress = data.getStringExtra("address");
                    currentLatitude = data.getDoubleExtra("lat", 0.0);
                    currentLongitude = data.getDoubleExtra("long", 0.0);
                    // Toast.makeText(getApplicationContext(),"Latitude : "+currentLatitude+"\n"+"Longitude :"+currentLongitude,Toast.LENGTH_LONG).show();
                    //  txtAddress.setText("Address: "+address);
                    //  txtLatLong.setText("Lat:"+currentLatitude+"  Long:"+currentLongitude);

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
