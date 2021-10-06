package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Adapter.ItemGridAdapter;
import com.perfect.easyshopplus.Adapter.ItemListAdapter;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
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

public class ItemListingActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, CartChangedListener  {

    float subtotal, totalgst, aamountPay, totalMRP, totalRetailPrice, discount, memberdiscount, yousaved;
    ProgressDialog progressDialog;
    String ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst, LayoutOrientation="Portal";
    int intMinpricefilter, intMaxpricefilter, intsortValue;
    TextView tvcat, tvuser, tvcart, iv_headert, tv_notification, tvCart;
    RecyclerView rv_itemtlist;
    EditText etSearch;
    ImageView im, imcart, im_notification, imViewchange,im_rewards;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    DBHandler db;
    public static CartChangedListener cartChangedListener = null;
    RelativeLayout rl_notification;
    Button btFilter, btSort;
    Boolean isChangedLayout=false;
    TextView bttoalamount, bttoalsvdamount;
    double doubleTotalAmt;

    String TAG = "ItemListingActivity";
    BottomNavigationView navigation;
    String fromCategory = "from";
    String valueCategory = "home";
    String fromCart = "From";
    String valueCart = "Home";
    String fromHome = "";
    String valueHome = "";
    String fromFavorite = "From";
    String From = "";
    String valueFavorite = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_item_listing_main);

        Log.e(TAG,"onCreate  Start   126");
        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
        ImageView imApplogo=findViewById(R.id.imApplogo);
        String strImagepath= IMAGEURL + pref3.getString("AppIconImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath).into(imApplogo);

        Intent in = getIntent();
        From = in.getStringExtra("From");
        ID_CategorySecond = in.getStringExtra("ID_CategorySecond");
        SecondCategoryName = in.getStringExtra("SecondCategoryName");
        FirstCategoryName = in.getStringExtra("FirstCategoryName");
        ID_CategoryFirst = in.getStringExtra("ID_CategoryFirst");
        initiateViews();
        setRegViews();
        setBottomBar();

      //  etSearch.setHint("hhhh");
        SharedPreferences SearchForProducts = getApplicationContext().getSharedPreferences(Config.SHARED_PREF118, 0);
        etSearch.setHint(""+SearchForProducts.getString("SearchForProducts",null));

        SharedPreferences sort = getApplicationContext().getSharedPreferences(Config.SHARED_PREF113, 0);
        SharedPreferences filter = getApplicationContext().getSharedPreferences(Config.SHARED_PREF114, 0);
        btSort.setText(""+sort.getString("sort",null));
        btFilter.setText(""+filter.getString("filter",null));

        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
        tvcat.setText(pref1.getString("StoreName", null)+" > "+FirstCategoryName+" > "/*+SecondCategoryName+" > "*/);
        iv_headert.setText(SecondCategoryName);

        SharedPreferences ScratchCard = getApplicationContext().getSharedPreferences(Config.SHARED_PREF382, 0);
        if (ScratchCard.getString("ScratchCard",null).equals("true")){
            im_rewards.setVisibility(View.VISIBLE);
        }else{
            im_rewards.setVisibility(View.GONE);
        }


        getItemList();

        setHomeNavMenu1();



       /* SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/
        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        tvuser.setText("Welcome "+pref2.getString("username", null));
        db=new DBHandler(this);
        tvcart.setText(String.valueOf(db.selectCartCount()));
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));
        onCartChanged();
    }

    private void initiateViews() {
        rv_itemtlist=(RecyclerView)findViewById(R.id.rv_itemtlist);
        tvcat=(TextView) findViewById(R.id.tvcat);
        tvCart=(TextView) findViewById(R.id.tvCart);
        tvuser=(TextView) findViewById(R.id.tvuser);
        tvcart =(TextView) findViewById(R.id.tvcart);
        iv_headert=(TextView) findViewById(R.id.iv_headert);
        etSearch=(EditText)findViewById(R.id.etSearch);
        imcart=(ImageView) findViewById(R.id.imcart);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        im = (ImageView) findViewById(R.id.im);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        im_rewards = (ImageView) findViewById(R.id.im_rewards);
        btFilter = (Button) findViewById(R.id.btFilter);
        btSort = (Button) findViewById(R.id.btSort);
        imViewchange = (ImageView) findViewById(R.id.imViewchange);
        bttoalamount=(TextView)findViewById(R.id.bttoalamount);
        bttoalsvdamount=(TextView)findViewById(R.id.bttoalsvdamount);
    }

    private void setRegViews() {
        im.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        etSearch.setKeyListener(null);
        imcart.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        tvCart.setOnClickListener(this);
        cartChangedListener = this;
        im_notification.setOnClickListener(this);
        im_rewards.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        btFilter.setOnClickListener(this);
        btSort.setOnClickListener(this);
        imViewchange.setOnClickListener(this);
    }
    private void setBottomBar() {
        navigation= (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().setGroupCheckable(0, false, false);
    }

    private void getItemList() {
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
                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                requestObject1.put("ReqMode", "4");
                requestObject1.put("ID_CategoryFirst", ID_CategoryFirst);
                requestObject1.put("ID_CategorySecond", ID_CategorySecond);
                requestObject1.put("ShopType", "2");
                requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                requestObject1.put("MemberId", pref3.getString("memberid", null));
                requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                Log.e(TAG,"requestObject1  228  "+requestObject1);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
            Call<String> call = apiService.getItemList(body);
            call.enqueue(new Callback<String>() {
                @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    try {
                        progressDialog.dismiss();
                        JSONObject jObject = new JSONObject(response.body());
                        if(jObject.getString("StatusCode").equals("0")) {
                            JSONObject jobj = jObject.getJSONObject("ItemListSearchInfo");
                            rv_itemtlist.setVisibility(View.VISIBLE);
                            JSONArray jarray = jobj.getJSONArray("ItemListInfo");
                            if (LayoutOrientation.equals("Landscape")) {
                                GridLayoutManager lLayout = new GridLayoutManager(ItemListingActivity.this, 2);
                                rv_itemtlist.setLayoutManager(lLayout);
                                rv_itemtlist.setHasFixedSize(true);
                                ItemGridAdapter adapter = new ItemGridAdapter(ItemListingActivity.this, jarray, ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst);
                                rv_itemtlist.setAdapter(adapter);
                            }
                            else {
                                GridLayoutManager lLayout = new GridLayoutManager(ItemListingActivity.this, 1);
                                rv_itemtlist.setLayoutManager(lLayout);
                                rv_itemtlist.setHasFixedSize(true);
                                ItemListAdapter adapter = new ItemListAdapter(ItemListingActivity.this, jarray, ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst);
                                rv_itemtlist.setAdapter(adapter);
                            }

                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(ItemListingActivity.this);
                            builder.setMessage("No data found.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
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
                onBackPressed();
                break;
            case R.id.etSearch:
                startActivity(new Intent(ItemListingActivity.this,SearchActivity.class));
                break;
            case R.id.imcart:
                Intent i = new Intent(ItemListingActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.tvcart:
                Intent in = new Intent(ItemListingActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.tvCart:
                Intent intnt = new Intent(ItemListingActivity.this, CartActivity.class);
                intnt.putExtra("From", "Home");
                startActivity(intnt);
                break;
            case R.id.tv_notification:
                startActivity(new Intent(ItemListingActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(ItemListingActivity.this, NotificationActivity.class));
                break;
            case R.id.im_rewards:
                startActivity(new Intent(ItemListingActivity.this, GiftVoucherActivity.class));
                break;
            case R.id.btSort:
                callSort();
                break;
            case R.id.btFilter:
                callFilter();
                break;
            case R.id.imViewchange:
               if(!isChangedLayout){
                   LayoutOrientation="Landscape";
                   imViewchange.setImageResource(R.drawable.list);
                   getItemList();
                   isChangedLayout=true;
               }else{
                   LayoutOrientation="Portal";
                   imViewchange.setImageResource(R.drawable.grid);
                   getItemList();
                   isChangedLayout=false;
               }
               break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF36, 0);
            SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF37, 0);
            SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF52, 0);
            String SubCategoryList = pref1.getString("SubCategoryList", null);
            if (From.equals("HomeItem")){
                Intent i = new Intent(this, HomeActivity.class);
                startActivity(i);
                finish();
            }else if (From.equals("Home")){
                Intent intent = new Intent(this, SubcategoryActivity.class);
                intent.putExtra("ID_CategorySecond", ID_CategorySecond);
                intent.putExtra("SecondCategoryName", SecondCategoryName);
                intent.putExtra("FirstCategoryName", FirstCategoryName);
                intent.putExtra("ID_CategoryFirst", ID_CategoryFirst);
                intent.putExtra("From", From);
                startActivity(intent);
                finish();
            }else if (From.equals("HomeCatItem")){
                Intent intent = new Intent(this, OutShopActivity.class);
                intent.putExtra("from", From);
                startActivity(intent);
                finish();
            }
            else if(pref6.getString("RequiredStore", null).equals("false")&&
                    pref7.getString("RequiredStoreCategory", null).equals("false")){
                Intent intent = new Intent(this, OutShopActivity.class);
                intent.putExtra("from", "home");
                startActivity(intent);
                finish();
            }
            else if (pref1.getString("SubCategoryList", null).equals("true")){
                Intent intent = new Intent(this, SubcategoryActivity.class);
                intent.putExtra("ID_CategorySecond", ID_CategorySecond);
                intent.putExtra("SecondCategoryName", SecondCategoryName);
                intent.putExtra("FirstCategoryName", FirstCategoryName);
                intent.putExtra("ID_CategoryFirst", ID_CategoryFirst);
                intent.putExtra("From", From);
                startActivity(intent);
                finish();
            }else {
                Intent intent = new Intent(this, OutShopActivity.class);
                intent.putExtra("from", "itemlist");
                startActivity(intent);
                finish();
            }
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
                "Notifications", "Shopping List","My Profile", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,R.drawable.ic_store,
                R.drawable.ic_notifications, R.drawable.ic_list,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ItemListingActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ItemListingActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ItemListingActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ItemListingActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ItemListingActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(ItemListingActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 5) {
                    startActivity(new Intent(ItemListingActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(ItemListingActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 7) {
                    startActivity(new Intent(ItemListingActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 8) {
                    startActivity(new Intent(ItemListingActivity.this, AboutUsActivity.class));
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
        final String[] menulist = new String[]{"Home","My Cart", "Track Orders", "Favourite Items",
                "Notifications", "My Profile", "About Us", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ItemListingActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ItemListingActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ItemListingActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ItemListingActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ItemListingActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 4) {
                    startActivity(new Intent(ItemListingActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 5) {
                    startActivity(new Intent(ItemListingActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(ItemListingActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(ItemListingActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 10) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ItemListingActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) ItemListingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivity(new Intent(ItemListingActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(ItemListingActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(ItemListingActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 14) {
                    startActivity(new Intent(ItemListingActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ItemListingActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ItemListingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(ItemListingActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(ItemListingActivity.this,LocationActivity.class));
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
        tvcart.setText(String.valueOf(db.selectCartCount()));


        db=new DBHandler(this);
        String string = "\u20B9";
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {e.printStackTrace();}
        DecimalFormat f = new DecimalFormat("##.00");
        subtotal=db.selectCartTotalActualPrice();
        totalgst=db.CartTotalGST();
        totalMRP=db.CartTotalMRP();
        totalRetailPrice=db.CartTotalRetailPrice();
        discount=totalMRP-totalRetailPrice;
        memberdiscount=   totalRetailPrice-subtotal;
        aamountPay=subtotal+totalgst;
        yousaved=discount+memberdiscount;
        doubleTotalAmt=Double.parseDouble(String.valueOf(subtotal));


        SharedPreferences totalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF131, 0);
        bttoalamount.setText(totalamount.getString("totalamount", "")+" : "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        SharedPreferences youhavesaved = getApplicationContext().getSharedPreferences(Config.SHARED_PREF212, 0);
        bttoalsvdamount.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");

//        bttoalamount.setText("Total Amount : "+string+" "+f.format(Double.parseDouble(String.valueOf(subtotal))));
//        bttoalsvdamount.setText("( You have saved "+string+" "+f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");
    }

    private void callFilter() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ItemListingActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ItemListingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.popup_filter, null);
            LinearLayout ll_clear = (LinearLayout) layout.findViewById(R.id.ll_clear);
            LinearLayout ll_apply = (LinearLayout) layout.findViewById(R.id.ll_apply);
            ImageView imclose = (ImageView) layout.findViewById(R.id.imclose);
            final EditText etmin = (EditText) layout.findViewById(R.id.etmin);
            final EditText etmax = (EditText) layout.findViewById(R.id.etmax);

            TextView txt_MinPrice =  layout.findViewById(R.id.tvmin);
            TextView txt_MaxPrice =  layout.findViewById(R.id.tvmax);
            TextView txtclear =  layout.findViewById(R.id.clear);
            TextView txtapply = (TextView) layout.findViewById(R.id.tvapply);
            TextView txt_filterhead = (TextView) layout.findViewById(R.id.tvtfilter);


            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();

            SharedPreferences minprice = getApplicationContext().getSharedPreferences(Config.SHARED_PREF177, 0);
            SharedPreferences maxprice = getApplicationContext().getSharedPreferences(Config.SHARED_PREF178, 0);
            SharedPreferences clears = getApplicationContext().getSharedPreferences(Config.SHARED_PREF179, 0);
            SharedPreferences applys = getApplicationContext().getSharedPreferences(Config.SHARED_PREF180, 0);
            SharedPreferences filter = getApplicationContext().getSharedPreferences(Config.SHARED_PREF114, 0);
            txt_MinPrice.setText(""+minprice.getString("minprice",null));
            txt_MaxPrice.setText(""+maxprice.getString("maxprice",null));
            txtclear.setText(""+clears.getString("clear",null));
            txtapply.setText(""+applys.getString("apply",null));
            txt_filterhead.setText(""+filter.getString("filter",null));


            ll_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etmin.getText().toString().isEmpty()) {
                        etmin.setError("Please provide Minimum Price.");
                    } else if (etmax.getText().toString().isEmpty()) {
                        etmax.setError("Please provide Maximum Price.");
                    }   else {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        intMinpricefilter=Integer.parseInt(etmin.getText().toString());
                        intMaxpricefilter=Integer.parseInt(etmax.getText().toString());
                        applyFilter(intMinpricefilter,intMaxpricefilter);
                        alertDialog.dismiss();
                    }
                }
            });
            ll_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    etmax.setText("");
                    etmin.setText("");
                }
            });
            imclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callSort() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ItemListingActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ItemListingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.popup_sort, null);
            LinearLayout ll_clear = layout.findViewById(R.id.ll_clear);
            LinearLayout ll_apply = layout.findViewById(R.id.ll_apply);
            ImageView imclose = layout.findViewById(R.id.imclose);
            final RadioGroup rgPrice = layout.findViewById(R.id.rgPrice);

            TextView txt_sortby = layout.findViewById(R.id.tvtsort);
            RadioButton radio0 = layout.findViewById(R.id.radio0);
            RadioButton radio1 = layout.findViewById(R.id.radio1);
            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();

            SharedPreferences pricelowtohigh = getApplicationContext().getSharedPreferences(Config.SHARED_PREF175, 0);
            SharedPreferences pricehightolow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF176, 0);
            SharedPreferences sortby = getApplicationContext().getSharedPreferences(Config.SHARED_PREF174, 0);
            radio0.setText(""+pricelowtohigh.getString("pricelowtohigh",null));
            radio1.setText(""+pricehightolow.getString("pricehightolow",null));
            txt_sortby.setText(""+sortby.getString("sortby",null));

            rgPrice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    View radioButton = rgPrice.findViewById(checkedId);
                    int index = rgPrice.indexOfChild(radioButton);
                    switch (index) {
                        case 0:
                            intsortValue=0;
                            applySort(0);
                            alertDialog.dismiss();
                            break;
                        case 1:
                            intsortValue=1;
                            applySort(1);
                            alertDialog.dismiss();
                            break;
                    }
                }
            });
            ll_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            ll_clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    rgPrice.clearCheck();
                }
            });
            imclose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void applySort(int SortField) {
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
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                    requestObject1.put("ShopType", "2");
                    requestObject1.put("ID_CategoryFirst", ID_CategoryFirst);
                    requestObject1.put("ID_CategorySecond", ID_CategorySecond);
                    requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                    requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("SortField", SortField);
                    requestObject1.put("MemberId", pref3.getString("memberid", null));
                    requestObject1.put("Criteria1", intMinpricefilter);
                    requestObject1.put("Criteria2", intMaxpricefilter);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getSort(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("ItemListSearchInfo");
                            JSONArray jarray = jobj.getJSONArray("ItemListInfo");
                            if(jarray.length()!=0) {
                                if (LayoutOrientation.equals("Landscape")) {
                                    GridLayoutManager lLayout = new GridLayoutManager(ItemListingActivity.this, 2);
                                    rv_itemtlist.setLayoutManager(lLayout);
                                    rv_itemtlist.setHasFixedSize(true);
                                    ItemGridAdapter adapter = new ItemGridAdapter(ItemListingActivity.this, jarray, ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst);
                                    rv_itemtlist.setAdapter(adapter);
                                } else{
                                    GridLayoutManager lLayout = new GridLayoutManager(ItemListingActivity.this, 1);
                                    rv_itemtlist.setLayoutManager(lLayout);
                                    rv_itemtlist.setHasFixedSize(true);
                                    ItemListAdapter adapter = new ItemListAdapter(ItemListingActivity.this, jarray, ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst);
                                    rv_itemtlist.setAdapter(adapter);
                                }
                            }else{}
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

    private void applyFilter(int MinPrice,int MaxPrice) {
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
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                    requestObject1.put("ShopType", "2");
                    requestObject1.put("ID_CategoryFirst", ID_CategoryFirst);
                    requestObject1.put("ID_CategorySecond", ID_CategorySecond);
                    requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                    requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("Criteria1", MinPrice);
                    requestObject1.put("Criteria2", MaxPrice);
                    requestObject1.put("SortField", intsortValue);
                    requestObject1.put("MemberId", pref3.getString("memberid", null));
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getSort(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("ItemListSearchInfo");
                            if (jobj.getString("ItemListInfo").equals("null")) {
                                JSONArray jsnarray = new JSONArray();
                                ItemGridAdapter adapter = new ItemGridAdapter(ItemListingActivity.this, jsnarray, ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst);
                                rv_itemtlist.setAdapter(adapter);
                                AlertDialog.Builder builder = new AlertDialog.Builder(ItemListingActivity.this);
                                builder.setMessage("No Items To Show..")
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                               // finish();
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                JSONArray jarray = jobj.getJSONArray("ItemListInfo");
                                if (jarray.length() != 0) {
                                    if (LayoutOrientation.equals("Landscape")) {
                                        GridLayoutManager lLayout = new GridLayoutManager(ItemListingActivity.this, 2);
                                        rv_itemtlist.setLayoutManager(lLayout);
                                        rv_itemtlist.setHasFixedSize(true);
                                        ItemGridAdapter adapter = new ItemGridAdapter(ItemListingActivity.this, jarray, ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst);
                                        rv_itemtlist.setAdapter(adapter);
                                    } else {
                                        GridLayoutManager lLayout = new GridLayoutManager(ItemListingActivity.this, 1);
                                        rv_itemtlist.setLayoutManager(lLayout);
                                        rv_itemtlist.setHasFixedSize(true);
                                        ItemListAdapter adapter = new ItemListAdapter(ItemListingActivity.this, jarray, ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst);
                                        rv_itemtlist.setAdapter(adapter);
                                    }
                                } else {
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


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected( MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_category:
                    Intent io = new Intent(ItemListingActivity.this, OutShopActivity.class);
                    io.putExtra(fromCategory, valueCategory);
                    finish();
                    startActivity(io);
                    return true;
                case R.id.navigation_cart:
                    Log.e(TAG,"navigation_cart    ");
                    Intent ic = new Intent(ItemListingActivity.this, CartActivity.class);
                    ic.putExtra(fromCart, valueCart);
                    startActivity(ic);
                    return true;
                case R.id.navigation_home:
                    Log.e(TAG,"navigation_home    ");
                    Intent iha = new Intent(ItemListingActivity.this, HomeActivity.class);
                    iha.putExtra(fromFavorite, valueFavorite);
                    startActivity(iha);
                    finish();
                    return true;
                case R.id.navigation_favorite:
                    Log.e(TAG,"navigation_favorite    ");
                    Intent ifa = new Intent(ItemListingActivity.this, FavouriteActivity.class);
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ItemListingActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ItemListingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
