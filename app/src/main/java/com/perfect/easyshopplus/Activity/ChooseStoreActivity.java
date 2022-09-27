package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
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
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.Adapter.StoreSearchListAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.FavModel;
import com.perfect.easyshopplus.Model.StoreselectModel;
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

public class ChooseStoreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener, CartChangedListener   {

    RecyclerView rv_favlist;
    EditText etSearch;
    ImageView im, imcart, im_notification, iv_itemimage;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvuser, tvcart, tv_notification, tvPrdName;
    ArrayList<FavModel> favlist = new ArrayList<>();
    DBHandler db;
    public static CartChangedListener cartChangedListener = null;
    LinearLayout emptyFvrt;
    RelativeLayout rl_notification;
    EditText etstore, etarea, etpin;
    Button btsearch;
    ListView list_view;
    EditText etsearch;
    StoreSearchListAdapter sadapter1;
    TextView tv_popuptitle;
    int textlength = 0;
    ArrayList<StoreselectModel> storeArrayList = new ArrayList<>();
    public static ArrayList<StoreselectModel> array_sort1= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_choose_store_main);
        initiateViews();
        setRegViews();
        setHomeNavMenu1();

       /* SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/
       // getAllfav();

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
        onCartChanged();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));
        SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
        SharedPreferences StoreImage = getApplicationContext().getSharedPreferences(Config.SHARED_PREF26, 0);
        String strStorename=StoreName.getString("StoreName", null);
        tvPrdName.setText(strStorename);
        String imagepath= IMAGEURL + StoreImage.getString("StoreImage", null);
       /* Glide.with(ChooseStoreActivity.this)
                .load(imagepath)
                .placeholder(R.drawable.items)
                .error(R.drawable.items)
                .into(iv_itemimage);*/

        PicassoTrustAll.getInstance(ChooseStoreActivity.this).load(imagepath).fit().centerCrop().into(iv_itemimage);
    }

    private void initiateViews() {

        btsearch=(Button) findViewById(R.id.btsearch);
        etSearch=(EditText)findViewById(R.id.etSearch);
        etstore=(EditText)findViewById(R.id.etstore);
        etarea=(EditText)findViewById(R.id.etarea);
        etpin=(EditText)findViewById(R.id.etpin);
        imcart=(ImageView) findViewById(R.id.imcart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        im = (ImageView) findViewById(R.id.im);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        tvPrdName = (TextView) findViewById(R.id.tvPrdName);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        emptyFvrt=(LinearLayout) findViewById(R.id.emptyFvrt);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        iv_itemimage = (ImageView) findViewById(R.id.iv_itemimage);
    }

    private void setRegViews() {
        im.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        imcart.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        cartChangedListener = this;
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        btsearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.im:

                //  drawer.openDrawer(Gravity.START);
                onBackPressed();                break;
            case R.id.etSearch:
                startActivity(new Intent(ChooseStoreActivity.this,SearchActivity.class));
                break;
            case R.id.imcart:
                Intent i = new Intent(ChooseStoreActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.tvcart:
                Intent in = new Intent(ChooseStoreActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.tv_notification:
                startActivity(new Intent(ChooseStoreActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(ChooseStoreActivity.this, NotificationActivity.class));
                break;
            case R.id.btsearch:
                getStore();
                break;
        }
    }

    private void getStore() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            LayoutInflater inflater1 = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.area_popup, null);
            list_view = (ListView) layout.findViewById(R.id.list_view);
            etsearch = (EditText) layout.findViewById(R.id.etsearch);
            tv_popuptitle = (TextView) layout.findViewById(R.id.tv_popuptitle);

            SharedPreferences SelectStoresp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF356, 0);
            String SelectStore = SelectStoresp.getString("SelectStore","");

            tv_popuptitle.setText(SelectStore+"");
            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();
            getStoreList(alertDialog);
            alertDialog.show();
        }catch (Exception e){e.printStackTrace();}
    }

    public void getStoreList(final AlertDialog alertDialog){

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        final String IMAGEURL = imgpref.getString("ImageURL", null);
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
                    requestObject1.put("StoreName",etstore.getText().toString());
                    requestObject1.put("AreaName",etarea.getText().toString());
                    requestObject1.put("PinCode", etpin.getText().toString());
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getStores(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("StoreBindListInfo");
                            if (jobj.getString("storeListInfos").equals("null")) {
                            } else {
                                JSONArray jarray = jobj.getJSONArray("storeListInfos");
                                array_sort1 = new ArrayList<>();
                                storeArrayList = new ArrayList<>();
                                for (int k = 0; k < jarray.length(); k++) {
                                    JSONObject jsonObject = jarray.getJSONObject(k);

                                    storeArrayList.add(new StoreselectModel(jsonObject.getString("ID_Store"),jsonObject.getString("StoreName"),jsonObject.getString("ImagePath")));
                                    array_sort1.add(new StoreselectModel(jsonObject.getString("ID_Store"),jsonObject.getString("StoreName"),jsonObject.getString("ImagePath")));
                                }
                                sadapter1 = new StoreSearchListAdapter(ChooseStoreActivity.this, array_sort1);
                                list_view.setAdapter(sadapter1);
                                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                     @Override
                                                                     public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                                                                   /*      tvPrdName.setText(array_sort1.get(position).getStoreName());
                                                                         String imagepath= Config.IMAGEURL + array_sort1.get(position).getImagePath();
                                                                         Glide.with(ChooseStoreActivity.this)
                                                                                 .load(imagepath)
                                                                                 .placeholder(R.drawable.items)
                                                                                 .error(R.drawable.items)
                                                                                 .into(iv_itemimage);
                                SharedPreferences ID_Store =getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                                SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                ID_Storeeditor.putString("ID_Store", array_sort1.get(position).getID_Store());
                                ID_Storeeditor.commit();
                                SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                StoreNameeditor.putString("StoreName", array_sort1.get(position).getStoreName());
                                StoreNameeditor.commit();
                                SharedPreferences StoreImage = getApplicationContext().getSharedPreferences(Config.SHARED_PREF26, 0);
                                SharedPreferences.Editor StoreImageditor = StoreImage.edit();
                                StoreImageditor.putString("StoreImage", array_sort1.get(position).getImagePath());
                                StoreImageditor.commit();
                                alertDialog.dismiss();*/


                                                                         final SharedPreferences ID_Store = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                                                                         if(ID_Store.getString("ID_Store", null) == null ||ID_Store.getString("ID_Store", null).equals("") ||
                                                                                 ID_Store.getString( "ID_Store", null).equals(array_sort1.get(position).getID_Store()))  {
                                                                             tvPrdName.setText(array_sort1.get(position).getStoreName());
                                                                             String imagepath= IMAGEURL + array_sort1.get(position).getImagePath();
//                                                                             Glide.with(ChooseStoreActivity.this)
//                                                                                     .load(imagepath)
//                                                                                     .placeholder(R.drawable.items)
//                                                                                     .error(R.drawable.items)
//                                                                                     .into(iv_itemimage);
                                                                             PicassoTrustAll.getInstance(ChooseStoreActivity.this).load(imagepath).fit().centerCrop().into(iv_itemimage);

                                                                             // SharedPreferences ID_Store =getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                                                                             SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                                                             ID_Storeeditor.putString("ID_Store", array_sort1.get(position).getID_Store());
                                                                             ID_Storeeditor.commit();
                                                                             SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                                                             SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                                                             StoreNameeditor.putString("StoreName", array_sort1.get(position).getStoreName());
                                                                             StoreNameeditor.commit();
                                                                             SharedPreferences StoreImage = getApplicationContext().getSharedPreferences(Config.SHARED_PREF26, 0);
                                                                             SharedPreferences.Editor StoreImageditor = StoreImage.edit();
                                                                             StoreImageditor.putString("StoreImage", array_sort1.get(position).getImagePath());
                                                                             StoreImageditor.commit();
                                                                             alertDialog.dismiss();
                                                                         }
                                                                         else {
                                                                             android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChooseStoreActivity.this);
                                                                             LayoutInflater inflater1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                                                             View layout = inflater1.inflate(R.layout.warning_popup, null);
                                                                             LinearLayout ll_cancel = (LinearLayout) layout.findViewById(R.id.ll_cancel);
                                                                             LinearLayout ll_ok = (LinearLayout) layout.findViewById(R.id.ll_ok);
                                                                             builder.setView(layout);
                                                                             final android.app.AlertDialog alertDialog1 = builder.create();
                                                                             ll_cancel.setOnClickListener(new View.OnClickListener() {
                                                                                 @Override
                                                                                 public void onClick(View view) {
                                                                                     alertDialog.dismiss();
                                                                                     alertDialog1.dismiss();
                                                                                 }
                                                                             });
                                                                             ll_ok.setOnClickListener(new View.OnClickListener() {
                                                                                 @Override
                                                                                 public void onClick(View view) {
                                                                                     DBHandler db = new DBHandler(ChooseStoreActivity.this);
                                                                                     db.deleteallCart();
                                                                                     tvPrdName.setText(array_sort1.get(position).getStoreName());
                                                                                     String imagepath= IMAGEURL + array_sort1.get(position).getImagePath();
//                                                                                     Glide.with(ChooseStoreActivity.this)
//                                                                                             .load(imagepath)
//                                                                                             .placeholder(R.drawable.items)
//                                                                                             .error(R.drawable.items)
//                                                                                             .into(iv_itemimage);

                                                                                     PicassoTrustAll.getInstance(ChooseStoreActivity.this).load(imagepath).fit().centerCrop().into(iv_itemimage);
                                                                                     // SharedPreferences ID_Store =getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                                                                                     SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                                                                     ID_Storeeditor.putString("ID_Store", array_sort1.get(position).getID_Store());
                                                                                     ID_Storeeditor.commit();
                                                                                     SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                                                                     SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                                                                     StoreNameeditor.putString("StoreName", array_sort1.get(position).getStoreName());
                                                                                     StoreNameeditor.commit();
                                                                                     SharedPreferences StoreImage = getApplicationContext().getSharedPreferences(Config.SHARED_PREF26, 0);
                                                                                     SharedPreferences.Editor StoreImageditor = StoreImage.edit();
                                                                                     StoreImageditor.putString("StoreImage", array_sort1.get(position).getImagePath());
                                                                                     StoreImageditor.commit();
                                                                                     alertDialog.dismiss();
                                                                                     alertDialog1.dismiss();
                                                                                 }
                                                                             });
                                                                             alertDialog1.show();
                                                                         }



                                }
                               });
                            }
                            etsearch.addTextChangedListener(new TextWatcher() {
                                public void afterTextChanged(Editable s) {
                                }
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    list_view.setVisibility(View.VISIBLE);
                                    textlength = etsearch.getText().length();
                                    array_sort1.clear();
                                    for (int i = 0; i < storeArrayList.size(); i++) {
                                        if (textlength <= storeArrayList.get(i).getStoreName().length()) {
                                            if (storeArrayList.get(i).getStoreName().toLowerCase().trim().contains(
                                                    etsearch.getText().toString().toLowerCase().trim())) {
                                                array_sort1.add(storeArrayList.get(i));
                                            }
                                        }
                                    }
                                    sadapter1 = new StoreSearchListAdapter(ChooseStoreActivity.this, array_sort1);
                                    list_view.setAdapter(sadapter1);
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
            Toast.makeText(this,Nointernetconnection.getString("Nointernetconnection",""),Toast.LENGTH_SHORT).show();
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
        final String[] menulist = new String[]{"Home","My Cart", "My Orders",
                "Notifications", "Shopping List","My Profile", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order,
                R.drawable.ic_notifications, R.drawable.ic_list,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ChooseStoreActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ChooseStoreActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ChooseStoreActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ChooseStoreActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ChooseStoreActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(ChooseStoreActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 5) {
                    startActivity(new Intent(ChooseStoreActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(ChooseStoreActivity.this, AboutUsActivity.class));
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
        final String[] menulist = new String[]{"Home","My Cart", "Track Orders",
                "Notifications", "My Profile", "About Us", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order,
                R.drawable.ic_notifications,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ChooseStoreActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ChooseStoreActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ChooseStoreActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ChooseStoreActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ChooseStoreActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 4) {
                    startActivity(new Intent(ChooseStoreActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(ChooseStoreActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(ChooseStoreActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 9) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ChooseStoreActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) ChooseStoreActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivity(new Intent(ChooseStoreActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 11) {
                    startActivity(new Intent(ChooseStoreActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(ChooseStoreActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(ChooseStoreActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ChooseStoreActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ChooseStoreActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(ChooseStoreActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(ChooseStoreActivity.this,LocationActivity.class));
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
    public void onCartChanged() {
        db=new DBHandler(this);
        tvcart.setText(String.valueOf(db.selectCartCount()+db.selectInshopCartCount()));
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
