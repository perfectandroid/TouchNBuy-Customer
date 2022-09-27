package com.perfect.easyshopplus.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
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

import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.notbytes.barcode_reader.BarcodeReaderActivity;
import com.notbytes.barcode_reader.BarcodeReaderFragment;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.CartModel;
import com.perfect.easyshopplus.Model.InshopCartModel;
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
import java.util.List;

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

public class InShopActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, BarcodeReaderFragment.BarcodeReaderListener, CartChangedListener {

    String TAG = "InShopActivity",OK;
    private static final int BARCODE_READER_ACTIVITY_REQUEST = 1208;
    private TextView mTvResult;
    ProgressDialog progressDialog;
    EditText etSearch;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvuser, tvcart, tv_notification, tv_title_hd;
    DBHandler db;
    RelativeLayout rl_notification;
    String scanValue;
    int counter= 1;
    boolean isCart;
    Boolean isInCart;
    static CartChangedListener cartChangedListener;
    ArrayList<InshopCartModel> inshopcartlist = new ArrayList<>();
    LinearLayout llinsert, llscan;
    Button btn_insertcode_activity,btn_activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_shop_main);
        initiateViews();
        setRegViews();
        setHomeNavMenu();

        Log.e(TAG,"Start   119   ");

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
        tvcart.setText(String.valueOf(db.selectInshopCartCount()));
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));
        onCartChanged();

        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        OK = OKsp.getString("OK","");

        SharedPreferences Scanorinsertbarcodeontheitemandpleaseaddittoyourcart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF348, 0);
        tv_title_hd.setText(Scanorinsertbarcodeontheitemandpleaseaddittoyourcart.getString("Scanorinsertbarcodeontheitemandpleaseaddittoyourcart",""));

        SharedPreferences InsertCode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF349, 0);
        btn_insertcode_activity.setText(InsertCode.getString("InsertCode",""));

        SharedPreferences ScanCode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF350, 0);
        btn_activity.setText(ScanCode.getString("ScanCode",""));
    }

    private void initiateViews() {
        etSearch=(EditText)findViewById(R.id.etSearch);
        imcart=(ImageView) findViewById(R.id.imcart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        im = (ImageView) findViewById(R.id.im);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        llinsert = (LinearLayout) findViewById(R.id.llinsert);
        llscan = (LinearLayout) findViewById(R.id.llscan);
        btn_activity = findViewById(R.id.btn_activity);
        mTvResult = findViewById(R.id.tv_result);
        tv_title_hd = findViewById(R.id.tv_title_hd);
        btn_insertcode_activity = findViewById(R.id.btn_insertcode_activity);
    }

    private void setRegViews() {
        im.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        imcart.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        llinsert.setOnClickListener(this);
        llscan.setOnClickListener(this);
        btn_activity.setOnClickListener(this);
        btn_insertcode_activity.setOnClickListener(this);
        cartChangedListener = this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llscan:
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                Fragment fragmentById = supportFragmentManager.findFragmentById(R.id.fm_container);
                if (fragmentById != null) {
                    fragmentTransaction.remove(fragmentById);
                }
                fragmentTransaction.commitAllowingStateLoss();
                launchBarCodeActivity();
                break;
            case R.id.btn_activity:
                FragmentManager supportFragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = supportFragmentManager1.beginTransaction();
                Fragment fragmentById1 = supportFragmentManager1.findFragmentById(R.id.fm_container);
                if (fragmentById1 != null) {
                    fragmentTransaction1.remove(fragmentById1);
                }
                fragmentTransaction1.commitAllowingStateLoss();
                launchBarCodeActivity();
                break;
            case R.id.btn_insertcode_activity:
                InsertCode();
                break;
            case R.id.llinsert:
                InsertCode();
                break;
            case R.id.tv_notification:
                startActivity(new Intent(InShopActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(InShopActivity.this, NotificationActivity.class));
                break; case R.id.im:

                //  drawer.openDrawer(Gravity.START);
                onBackPressed();                break;
            case R.id.etSearch:
                startActivity(new Intent(InShopActivity.this,SearchActivity.class));
                break;
            case R.id.imcart:
                Intent i = new Intent(InShopActivity.this, CartActivity.class);
                i.putExtra("From", "Inshop");
                startActivity(i);
                break;
            case R.id.tvcart:
                Intent in = new Intent(InShopActivity.this, CartActivity.class);
                in.putExtra("From", "Inshop");
                startActivity(in);
                break;
        }
    }


    private void launchBarCodeActivity() {
        Intent launchIntent = BarcodeReaderActivity.getLaunchIntent(this, true, false);
        startActivityForResult(launchIntent, BARCODE_READER_ACTIVITY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {

            SharedPreferences errorinscanningsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF368, 0);
            String errorinscanning = errorinscanningsp.getString("errorinscanning","");

            Toast.makeText(this, errorinscanning+".", Toast.LENGTH_SHORT).show();
            return;
        }
        if (requestCode == BARCODE_READER_ACTIVITY_REQUEST && data != null) {
            Barcode barcode = data.getParcelableExtra(BarcodeReaderActivity.KEY_CAPTURED_BARCODE);
            mTvResult.setText(barcode.rawValue);
            doScanItems(barcode.rawValue);
        }
    }

    @Override
    public void onScanned(Barcode barcode) {
    }

    @Override
    public void onScannedMultiple(List<Barcode> barcodes) {
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
    }

    @Override
    public void onScanError(String errorMessage) {
    }

    @Override
    public void onCameraPermissionDenied() {
        SharedPreferences Camerapermissiondeniedsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF369, 0);
        String Camerapermissiondenied = Camerapermissiondeniedsp.getString("Camerapermissiondenied","");
        Toast.makeText(this, Camerapermissiondenied+"!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent intent = new Intent(this, StoreInShopActivity.class);
            startActivity(intent);
            finish();
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
        NavMenuAdapter adapter = new NavMenuAdapter(InShopActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(InShopActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(InShopActivity.this, CartActivity.class);
                    i.putExtra("From", "Inshop");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(InShopActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(InShopActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(InShopActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(InShopActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(InShopActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(InShopActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 10) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(InShopActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) InShopActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivity(new Intent(InShopActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(InShopActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(InShopActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 14) {
                    startActivity(new Intent(InShopActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(InShopActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) InShopActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(InShopActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(InShopActivity.this,LocationActivity.class));
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
    private void doScanItems(String barcode) {
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
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF12, 0);
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                    requestObject1.put("ReqMode", "4");
                    requestObject1.put("ShopType", "1");
                    requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                    requestObject1.put("ID_Store", pref2.getString("ID_Store_Inshop", null));
                    requestObject1.put("MemberId", pref3.getString("memberid", null));
                    requestObject1.put("Barcode", barcode);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.scanBarcode(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            if(jObject.getString("StatusCode").equals("0")){
                                JSONObject jobjt = jObject.getJSONObject("itemListInfo");
                                String ID_Items = jobjt.getString("ID_Items");
                                String ItemName = jobjt.getString("ItemName");
                                String ID_Stock = jobjt.getString("ID_Stock");
                                String MRP = jobjt.getString("MRP");
                                String SalesPrice = jobjt.getString("SalesPrice");
                                String CurrentStock = jobjt.getString("CurrentStock");
                                String RetailPrice = jobjt.getString("RetailPrice");
                                String PrivilagePrice = jobjt.getString("PrivilagePrice");
                                String WholesalePrice = jobjt.getString("WholesalePrice");
                                String GST = jobjt.getString("GST");
                                String CESS = jobjt.getString("CESS");
                                String Packed = jobjt.getString("Packed");
                                String Description = jobjt.getString("Description");
                                String ImageName = IMAGEURL + jobjt.getString("ImageName");
                                getProduct(ItemName,SalesPrice, MRP, ID_Items,ID_Stock,CurrentStock,RetailPrice,PrivilagePrice,WholesalePrice,GST,CESS,Packed,Description,ImageName);
                            }else{
                                SharedPreferences Noitemfoundwhilescanningsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF351, 0);
                                String Noitemfoundwhilescanning = Noitemfoundwhilescanningsp.getString("Noitemfoundwhilescanning","");

                                AlertDialog.Builder builder= new AlertDialog.Builder(InShopActivity.this);
                                builder.setMessage(Noitemfoundwhilescanning+".")
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();}
                        }
                        catch (JSONException e) {
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
            SharedPreferences SomethingwentwrongTryagainlater = getApplicationContext().getSharedPreferences(Config.SHARED_PREF280, 0);

            Toast.makeText(getApplicationContext(),""+SomethingwentwrongTryagainlater.getString("SomethingwentwrongTryagainlater", null),Toast.LENGTH_LONG).show();
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

    public void
    getProduct(final String ItemName, final String SalesPrice, final String MRP, final String ID_Items,
                           final String ID_Stock,final String CurrentStock, final String RetailPrice,
                           final String PrivilagePrice, final String WholesalePrice, final String GST,
                           final String CESS, final String Packed, final String Description, final String ImageName){
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(InShopActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) InShopActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.productdetails_popup, null);
            final TextView tvPrdName = (TextView) layout.findViewById(R.id.tvPrdName);
            final TextView tvPrdAmount = (TextView) layout.findViewById(R.id.tvPrdAmount);
            final TextView tvPrdMRPAmount = (TextView) layout.findViewById(R.id.tvPrdMRPAmount);
            final TextView tvPrdMRPAmountsaved = (TextView) layout.findViewById(R.id.tvPrdMRPAmountsaved);
            final TextView tvQtyValue = (TextView) layout.findViewById(R.id.tvQtyValue);
            final TextView tvaddcart = (TextView) layout.findViewById(R.id.tvaddcart);
            final ImageView ivQtyMinus = (ImageView) layout.findViewById(R.id.ivQtyMinus);
            final ImageView ivQtyAdd = (ImageView) layout.findViewById(R.id.ivQtyAdd);
            final ImageView imaddcart = (ImageView) layout.findViewById(R.id.imaddcart);
            final LinearLayout llcancel = (LinearLayout) layout.findViewById(R.id.llcancel);
            final LinearLayout lladdcart = (LinearLayout) layout.findViewById(R.id.lladdcart);
            final TextView tvdescptntitle = (TextView) layout.findViewById(R.id.tvdescptntitle);
            final TextView tvdescptn = (TextView) layout.findViewById(R.id.tvdescptn);
            final ImageView iv_itemimage = (ImageView) layout.findViewById(R.id.iv_itemimage);
            final LinearLayout llQty = (LinearLayout) layout.findViewById(R.id.llQty);
            final LinearLayout lladdQty = (LinearLayout) layout.findViewById(R.id.lladdQty);
            final EditText tvQtyValueloose = (EditText) layout.findViewById(R.id.tvQtyValueloose);
//            Glide.with(this)
//                    .load(ImageName)
//                    .placeholder(R.drawable.items)
//                    .error(R.drawable.items)
//                    .into(iv_itemimage);
            if(Packed.equals("false")){
                llQty.setVisibility(View.VISIBLE);
                lladdQty.setVisibility(View.GONE);
            }else{
                llQty.setVisibility(View.GONE);
                lladdQty.setVisibility(View.VISIBLE);
            }
            PicassoTrustAll.getInstance(this).load(ImageName).fit().centerCrop().into(iv_itemimage);
         /*   if(!ImageName.isEmpty()) {
                byte[] decodedString = Base64.decode(ImageName, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                decodedByte.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Glide.with(this)
                        .load(stream.toByteArray())
                        .placeholder(R.drawable.items)
                        .error(R.drawable.items)
                        .into(iv_itemimage);
            }else {
                iv_itemimage.setImageResource(R.drawable.items);
            }*/
            tvPrdName.setText(ItemName);
            DecimalFormat f = new DecimalFormat("##.00");
            String string = "\u20B9";
            byte[] utf8 = new byte[0];
            try {
                utf8 = string.getBytes("UTF-8");
                string = new String(utf8, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            tvPrdAmount.setText(string+" "+f.format(Double.parseDouble(SalesPrice))+" /-");
            double yousaved=Float.parseFloat(MRP)-Float.parseFloat(SalesPrice);
            if(yousaved<=0){
                tvPrdMRPAmount.setVisibility(View.GONE);
                tvPrdMRPAmountsaved.setVisibility(View.GONE);
            }else {
                tvPrdMRPAmount.setVisibility(View.VISIBLE);
                tvPrdMRPAmountsaved.setVisibility(View.VISIBLE);
            }

            SharedPreferences MRPS = getApplicationContext().getSharedPreferences(Config.SHARED_PREF115, 0);
            SharedPreferences yousaveds = getApplicationContext().getSharedPreferences(Config.SHARED_PREF117, 0);


            tvPrdMRPAmount.setText(MRPS.getString("MRP","")+" "+string+" "+f.format(Double.parseDouble(MRP))+" /-");
            tvPrdMRPAmount.setPaintFlags(tvPrdMRPAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvPrdMRPAmountsaved.setText(yousaveds.getString("yousaved","")+" "+string+" "+f.format(yousaved));
            if (Description.isEmpty()){
                tvdescptntitle.setVisibility(View.GONE);
                tvdescptn.setVisibility(View.GONE);
            }else{
                tvdescptntitle.setVisibility(View.VISIBLE);
                tvdescptn.setVisibility(View.VISIBLE);
                tvdescptn.setText(Description);
            }
            try {
                inshopcartlist = new ArrayList<>(db.getAllInshopCart());
                Gson gson = new Gson();
                String listString = gson.toJson(inshopcartlist,new TypeToken<ArrayList<CartModel>>() {}.getType());
                JSONArray jarray =  new JSONArray(listString);
                for(int i=0; i<=jarray.length();i++){
                    JSONObject jobjt =  jarray.getJSONObject(i);
                    if(jobjt.getString("ID_Items").equals(ID_Items)){
                        int count=jobjt.getInt("Count");
                        tvQtyValue.setText(String.valueOf(count));
                        tvQtyValueloose.setText(String.valueOf(count));
                        break;
                    }
                }
            } catch (JSONException e) {e.printStackTrace();}
            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();
            llcancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            ivQtyAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    counter = Integer.valueOf(tvQtyValue.getText().toString()) + 1;
                    tvQtyValue.setText("" + counter);
                }
            });
            ivQtyMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Integer.valueOf(tvQtyValue.getText().toString())>=2) {
                        counter = Integer.valueOf(tvQtyValue.getText().toString()) - 1;
                        tvQtyValue.setText("" + counter);
                    }
                }
            });
            lladdcart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(tvQtyValueloose.getText().toString().equals("")){
                        SharedPreferences PleaseEnterQuantitysp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF370, 0);
                        String PleaseEnterQuantity = PleaseEnterQuantitysp.getString("PleaseEnterQuantity","");

                        AlertDialog.Builder builder = new AlertDialog.Builder(InShopActivity.this);
                        builder.setMessage(PleaseEnterQuantity+".");
                        builder.setNegativeButton(OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    } else{
                        isInCart = db.doesRecordExist(ID_Items);
                        String strQty;
                        if (Packed.equals("false")) {
                            strQty = tvQtyValueloose.getText().toString();
                        } else {
                            strQty = tvQtyValue.getText().toString();
                        }
                        if (!isInCart) {
                            db.addtoInshopCart(new InshopCartModel(ID_Items, ItemName, SalesPrice, MRP, ID_Stock,
                                    CurrentStock, RetailPrice, PrivilagePrice, WholesalePrice, GST, CESS, strQty, Packed, ImageName));

                            SharedPreferences Itemaddedtothecartsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF337, 0);
                            String Itemaddedtothecart = Itemaddedtothecartsp.getString("Itemaddedtothecart","");

                            Toast.makeText(getApplicationContext(), Itemaddedtothecart+".", Toast.LENGTH_SHORT).show();
                            cartChangedListener.onCartChanged();
                        } else {
                            db.updateInshopCart(Integer.valueOf(ID_Items), strQty);

                            SharedPreferences Itemupdatedtothereordersp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF309, 0);
                            String Itemupdatedtothereorder = Itemupdatedtothereordersp.getString("Itemupdatedtothereorder","");

                            Toast.makeText(getApplicationContext(), Itemupdatedtothereorder+".", Toast.LENGTH_SHORT).show();
                            cartChangedListener.onCartChanged();
                        }
                        alertDialog.dismiss();
                }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCartChanged() {
        db=new DBHandler(this);
        tvcart.setText(String.valueOf(db.selectInshopCartCount()));
    }

    private void InsertCode(){
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(InShopActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) InShopActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.insert_barcode_popup, null);
            final EditText etbarcode = (EditText) layout.findViewById(R.id.etbarcode);
            Button pop_barcodecancel = (Button) layout.findViewById(R.id.pop_barcodecancel);
            Button btn_barcodeok = (Button) layout.findViewById(R.id.btn_barcodeok);
            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();
            pop_barcodecancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            btn_barcodeok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etbarcode.getText().toString().isEmpty()) {

                        SharedPreferences Pleaseprovidebarcodevalue = getApplicationContext().getSharedPreferences(Config.SHARED_PREF352, 0);
                        etbarcode.setError(Pleaseprovidebarcodevalue.getString("Pleaseprovidebarcodevalue",""));
//                        etbarcode.setError("Please provide barcode value.");
                    }  else {
                        doScanItems(etbarcode.getText().toString());
                        alertDialog.dismiss();
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
