package com.perfect.easyshopplus.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Adapter.ReOrderItemListAdapter;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;

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

public class ReOrderItemListingActivity extends AppCompatActivity implements View.OnClickListener{

    String TAG = "ReOrderItemListingActivity";
    ProgressDialog progressDialog;
    String ID_CategorySecond, SecondCategoryName,MinimumDeliveryAmount, FirstCategoryName, ID_CategoryFirst, ID_Store, ShopType, ID_SalesOrder,ID_CustomerAddress,OrderType,DeliveryCharge,Id_order, orderDate, deliveryDate, status, order_id , itemcount, storeName;
    TextView tvcat;
    RecyclerView rv_itemtlist;
    Button btFilter, btSort,go_to_cart;

    EditText etsearch;
    ImageView imsearch, imClear,imback;

    BottomNavigationView navigation;
    String fromCategory = "from";
    String valueCategory = "home";
    String fromCart = "From";
    String valueCart = "Home";
    String fromHome = "";
    String valueHome = "";
    String fromFavorite = "From";
    String valueFavorite = "Home";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_listing); Intent in = getIntent();
        // ID_CategorySecond = in.getStringExtra("ID_CategorySecond");
        //SecondCategoryName = in.getStringExtra("SecondCategoryName");
//        FirstCategoryName = in.getStringExtra("FirstCategoryName");
//        ID_CategoryFirst = in.getStringExtra("ID_CategoryFirst");
//        ID_Store = in.getStringExtra("ID_Store");
//        ShopType = in.getStringExtra("ShopType");
//        ID_SalesOrder = in.getStringExtra("ID_SalesOrder");
//        ID_CustomerAddress = in.getStringExtra("ID_CustomerAddress");
//        OrderType = in.getStringExtra("OrderType");
//        DeliveryCharge = in.getStringExtra("DeliveryCharge");


        FirstCategoryName = in.getStringExtra("FirstCategoryName");
        ID_CategoryFirst = in.getStringExtra("ID_CategoryFirst");

        ID_SalesOrder = in.getStringExtra("ID_SalesOrder");

        order_id = in.getStringExtra("order_id");
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
        initiateViews();
        setRegViews();
        tvcat.setText(FirstCategoryName/*+" > "+SecondCategoryName+" > "*/);
        getItemList();

        etsearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.equals("") ) {
                    imClear.setVisibility(View.VISIBLE);
                }else{
                    imClear.setVisibility(View.GONE);}
                if(etsearch.getText().toString().isEmpty()){ imClear.setVisibility(View.GONE);}
                try {
                    searchList(etsearch.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            public void beforeTextChanged(CharSequence s, int start, int count,  int after) {
            }
            public void afterTextChanged(Editable s) {
            }
        });
        etsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                    getSearchList(etsearch.getText().toString());
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return false;
            }
        });
        setBottomBar();

        SharedPreferences SearchForProducts = getSharedPreferences(Config.SHARED_PREF118, 0);
        String strsearch=SearchForProducts.getString("SearchForProducts","");
        etsearch.setHint(strsearch);

        SharedPreferences sort = getSharedPreferences(Config.SHARED_PREF113, 0);
        String strsort=sort.getString("sort","");
        btSort.setText(strsort);

        SharedPreferences filter = getSharedPreferences(Config.SHARED_PREF114, 0);
        String strfilter=filter.getString("filter","");
        btFilter.setText(strfilter);
    }

    private void initiateViews() {
        rv_itemtlist=(RecyclerView)findViewById(R.id.rv_itemtlist);
        tvcat=(TextView) findViewById(R.id.tvcat);
        btFilter = (Button) findViewById(R.id.btFilter);
        btSort = (Button) findViewById(R.id.btSort);


        etsearch =  findViewById(R.id.etsearch);
        imsearch =  findViewById(R.id.imsearch);
        imClear = findViewById(R.id.imClear);
        go_to_cart = findViewById(R.id.go_to_cart);
        imback = findViewById(R.id.imback);
    }

    private void setRegViews() {
        btFilter.setOnClickListener(this);
        btSort.setOnClickListener(this);
        imClear.setOnClickListener(this);
        imsearch.setOnClickListener(this);
        go_to_cart.setOnClickListener(this);
        imback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imback:
                onBackPressed();
                break;
            case R.id.btSort:
                callSort();
                break;
            case R.id.btFilter:
                callFilter();
                break;
            case R.id.go_to_cart:
                Intent i=new Intent(this, ReOrderCartActivity.class);
//                    in.putExtra("ID_Store", ID_Store);
//                    in.putExtra("ShopType", ShopType);
//                    in.putExtra("ID_SalesOrder", ID_SalesOrder);
//                    in.putExtra("ID_CustomerAddress", ID_CustomerAddress);
//                    in.putExtra("OrderType", OrderType);
//                    in.putExtra("DeliveryCharge", DeliveryCharge);

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
                    finish();
                break;
        }
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
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                    requestObject1.put("ReqMode", "4");
                    requestObject1.put("ID_CategoryFirst", ID_CategoryFirst);
                    // requestObject1.put("ID_CategorySecond", ID_CategorySecond);
                    requestObject1.put("ShopType", ShopType);
                    requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                    requestObject1.put("ID_Store", ID_Store);
                    requestObject1.put("MemberId", pref3.getString("memberid", null));
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getItemList(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try
                        {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("ItemListSearchInfo");
                            if (!jobj.getString("ItemListInfo").equals("null")){
                                JSONArray jarray = jobj.getJSONArray("ItemListInfo");
                                if(jarray.length()!=0) {
                                    rv_itemtlist.setVisibility(View.VISIBLE);
                                    GridLayoutManager lLayout = new GridLayoutManager(ReOrderItemListingActivity.this, 1);
                                    rv_itemtlist.setLayoutManager(lLayout);
                                    rv_itemtlist.setHasFixedSize(true);
//                                    ReOrderItemListAdapter adapter = new ReOrderItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_Store, ShopType, ID_SalesOrder,ID_CustomerAddress,OrderType, DeliveryCharge);
                                    ReOrderItemListAdapter adapter = new ReOrderItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_SalesOrder, order_id, deliveryDate, Id_order, orderDate, status, ID_Store, ShopType,itemcount,ID_CustomerAddress,OrderType, storeName,DeliveryCharge,MinimumDeliveryAmount);

                                    rv_itemtlist.setAdapter(adapter);
                                }else{}
                            }
                            else {

                                SharedPreferences Noitemfound = getApplicationContext().getSharedPreferences(Config.SHARED_PREF259, 0);
                                String Noitemfoun = Noitemfound.getString("Noitemfound","");
                                Toast.makeText(ReOrderItemListingActivity.this, Noitemfoun+".", Toast.LENGTH_SHORT).show();
                                rv_itemtlist.setVisibility(View.GONE);

                            }
                        }catch (JSONException e) {
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
    public void onBackPressed() {
        Intent i=new Intent(this, ReOrderCategoryActivity.class);
//        in.putExtra("ID_Store", ID_Store);
//        in.putExtra("ShopType", ShopType);
//        in.putExtra("ID_SalesOrder", ID_SalesOrder);
//        in.putExtra("OrderType", OrderType);
//        in.putExtra("ID_CustomerAddress", ID_CustomerAddress);
//        in.putExtra("DeliveryCharge", DeliveryCharge);
        // in.putExtra("ID_CategoryFirst", ID_CategoryFirst);
        // in.putExtra("FirstCategoryName", FirstCategoryName);



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
        i.putExtra("DeliveryCharge", DeliveryCharge);
        i.putExtra("MinimumDeliveryAmount", MinimumDeliveryAmount);
        startActivity(i);
        finish();
    }

    private void callFilter() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReOrderItemListingActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ReOrderItemListingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.popup_filter, null);
            LinearLayout ll_clear = (LinearLayout) layout.findViewById(R.id.ll_clear);
            LinearLayout ll_apply = (LinearLayout) layout.findViewById(R.id.ll_apply);
            ImageView imclose = (ImageView) layout.findViewById(R.id.imclose);
            final EditText etmin = (EditText) layout.findViewById(R.id.etmin);
            final EditText etmax = (EditText) layout.findViewById(R.id.etmax);

            final TextView tvtfilter = (TextView) layout.findViewById(R.id.tvtfilter);
            final TextView tvmin = (TextView) layout.findViewById(R.id.tvmin);
            final TextView tvmax = (TextView) layout.findViewById(R.id.tvmax);
            final TextView clear = (TextView) layout.findViewById(R.id.clear);
            final TextView tvapply = (TextView) layout.findViewById(R.id.tvapply);

            SharedPreferences filter = getSharedPreferences(Config.SHARED_PREF114, 0);
            String strfilter=filter.getString("filter","");
            tvtfilter.setText(strfilter);
            SharedPreferences minprice = getSharedPreferences(Config.SHARED_PREF177, 0);
            tvmin.setText(minprice.getString("minprice",""));
            SharedPreferences maxprice = getSharedPreferences(Config.SHARED_PREF178, 0);
            tvmax.setText(maxprice.getString("maxprice",""));

            SharedPreferences clearsp = getSharedPreferences(Config.SHARED_PREF179, 0);
            clear.setText(clearsp.getString("clear",""));
            SharedPreferences apply = getSharedPreferences(Config.SHARED_PREF180, 0);
            tvapply.setText(apply.getString("apply",""));


            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();
            ll_apply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (etmin.getText().toString().isEmpty()) {
                        SharedPreferences PleaseprovideMinimumPrice = getSharedPreferences(Config.SHARED_PREF291, 0);
                       String strminprice=PleaseprovideMinimumPrice.getString("PleaseprovideMinimumPrice","");
                        etmin.setError(strminprice);
                    } else if (etmax.getText().toString().isEmpty()) {
                        SharedPreferences PleaseprovideMaximumPrice = getSharedPreferences(Config.SHARED_PREF292, 0);
                        String strmaxprice=PleaseprovideMaximumPrice.getString("PleaseprovideMaximumPrice","");
                        etmax.setError(strmaxprice);
                    }   else {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        applyFilter(Integer.parseInt(etmin.getText().toString()),Integer.parseInt(etmax.getText().toString()));
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReOrderItemListingActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ReOrderItemListingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.popup_sort, null);
            LinearLayout ll_clear = (LinearLayout) layout.findViewById(R.id.ll_clear);
            LinearLayout ll_apply = (LinearLayout) layout.findViewById(R.id.ll_apply);
            ImageView imclose = (ImageView) layout.findViewById(R.id.imclose);

            TextView tvtsort = (TextView) layout.findViewById(R.id.tvtsort);
            SharedPreferences sortby = getSharedPreferences(Config.SHARED_PREF174, 0);
            String strsort=sortby.getString("sortby","");
            tvtsort.setText(strsort);

            final RadioGroup rgPrice = (RadioGroup) layout.findViewById(R.id.rgPrice);
            final RadioButton radio0 = (RadioButton) layout.findViewById(R.id.radio0);
            final RadioButton radio1 = (RadioButton) layout.findViewById(R.id.radio1);

            SharedPreferences pricelowtohigh = getSharedPreferences(Config.SHARED_PREF175, 0);
            String strrb1=pricelowtohigh.getString("pricelowtohigh","");
            radio0.setText(strrb1);
            SharedPreferences pricehightolow = getSharedPreferences(Config.SHARED_PREF176, 0);
            String strrb2=pricehightolow.getString("pricehightolow","");
            radio1.setText(strrb2);

            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();
            rgPrice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    View radioButton = rgPrice.findViewById(checkedId);
                    int index = rgPrice.indexOfChild(radioButton);
                    switch (index) {
                        case 0:
                            applySort(0);
                            alertDialog.dismiss();
                            break;
                        case 1:
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
                    SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    requestObject1.put("ShopType", "2");
                    requestObject1.put("ID_CategoryFirst", ID_CategoryFirst);
                    //  requestObject1.put("ID_CategorySecond", ID_CategorySecond);
                    requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                    // requestObject1.put("FK_CustomerPlus", pref4.getString("userid", null));
                    requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("SortField", SortField);
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
                            JSONArray jarray = jobj.getJSONArray("ItemListInfo");
                            if(jarray.length()!=0) {
                                GridLayoutManager lLayout = new GridLayoutManager(ReOrderItemListingActivity.this, 1);
                                rv_itemtlist.setLayoutManager(lLayout);
                                rv_itemtlist.setHasFixedSize(true);
                                //  ItemListAdapter adapter = new ItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst);
                                ReOrderItemListAdapter adapter = new ReOrderItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_SalesOrder, order_id, deliveryDate, Id_order, orderDate, status, ID_Store, ShopType,itemcount,ID_CustomerAddress,OrderType, storeName,DeliveryCharge,MinimumDeliveryAmount);
                                rv_itemtlist.setAdapter(adapter);
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
                    // requestObject1.put("ID_CategorySecond", ID_CategorySecond);
                    requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                    requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("Criteria1", MinPrice);
                    requestObject1.put("Criteria2", MaxPrice);
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
                            JSONArray jarray = jobj.getJSONArray("ItemListInfo");
                            if(jarray.length()!=0) {
                                GridLayoutManager lLayout = new GridLayoutManager(ReOrderItemListingActivity.this, 1);
                                rv_itemtlist.setLayoutManager(lLayout);
                                rv_itemtlist.setHasFixedSize(true);
                                //  ItemListAdapter adapter = new ItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst);
                                ReOrderItemListAdapter adapter = new ReOrderItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_SalesOrder, order_id, deliveryDate, Id_order, orderDate, status, ID_Store, ShopType,itemcount,ID_CustomerAddress,OrderType, storeName,DeliveryCharge,MinimumDeliveryAmount);
                                rv_itemtlist.setAdapter(adapter);
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

    public void searchList(String Keywrd){
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
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                    requestObject1.put("ReqMode", "4");
                    requestObject1.put("ShopType", ShopType);
                    requestObject1.put("ID_CategoryFirst", ID_CategoryFirst);
//                    requestObject1.put("ID_CategorySecond", ID_CategorySecond);
                    requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                    requestObject1.put("ID_Store", ID_Store);
                    requestObject1.put("MemberId", pref3.getString("memberid", null));
                    requestObject1.put("ItemName",Keywrd);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG," requestObject1 774    "+requestObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getItemList(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try
                        {
                            Log.e(TAG," response 784    "+response.body());
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("ItemListSearchInfo");
                            if (!jobj.getString("ItemListInfo").equals("null")){
                                JSONArray jarray = jobj.getJSONArray("ItemListInfo");
                                if(jarray.length()!=0) {
                                    rv_itemtlist.setVisibility(View.VISIBLE);
                                    GridLayoutManager lLayout = new GridLayoutManager(ReOrderItemListingActivity.this, 1);
                                    rv_itemtlist.setLayoutManager(lLayout);
                                    rv_itemtlist.setHasFixedSize(true);
                                    ReOrderItemListAdapter adapter = new ReOrderItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_SalesOrder, order_id, deliveryDate, Id_order, orderDate, status, ID_Store, ShopType,itemcount,ID_CustomerAddress,OrderType, storeName,DeliveryCharge,MinimumDeliveryAmount);
                                    rv_itemtlist.setAdapter(adapter);
                                }else{}
                            }
                            else {

                                SharedPreferences Noitemfound = getApplicationContext().getSharedPreferences(Config.SHARED_PREF259, 0);
                                String Noitemfoun = Noitemfound.getString("Noitemfound","");
                                Toast.makeText(ReOrderItemListingActivity.this, Noitemfoun+".", Toast.LENGTH_SHORT).show();
                                rv_itemtlist.setVisibility(View.GONE);

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
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

    private void getSearchList(String Keywrd) {
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
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                    requestObject1.put("ReqMode", "4");
                    requestObject1.put("ShopType", ShopType);
                    requestObject1.put("ID_CategoryFirst", ID_CategoryFirst);
//                    requestObject1.put("ID_CategorySecond", ID_CategorySecond);
                    requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                    requestObject1.put("ID_Store", ID_Store);
                    requestObject1.put("MemberId", pref3.getString("memberid", null));
                    requestObject1.put("ItemName",Keywrd);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getItemList(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try
                        {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("ItemListSearchInfo");
                            if (!jobj.getString("ItemListInfo").equals("null")){
                                JSONArray jarray = jobj.getJSONArray("ItemListInfo");
                                if(jarray.length()!=0) {
                                    rv_itemtlist.setVisibility(View.VISIBLE);
                                    GridLayoutManager lLayout = new GridLayoutManager(ReOrderItemListingActivity.this, 1);
                                    rv_itemtlist.setLayoutManager(lLayout);
                                    rv_itemtlist.setHasFixedSize(true);
                                    ReOrderItemListAdapter adapter = new ReOrderItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_SalesOrder, order_id, deliveryDate, Id_order, orderDate, status, ID_Store, ShopType,itemcount,ID_CustomerAddress,OrderType, storeName,DeliveryCharge,MinimumDeliveryAmount);
                                    rv_itemtlist.setAdapter(adapter);
                                }else{}
                            }
                            else {
                                SharedPreferences Noitemfound = getApplicationContext().getSharedPreferences(Config.SHARED_PREF259, 0);
                                String Noitemfoun = Noitemfound.getString("Noitemfound","");
                                Toast.makeText(ReOrderItemListingActivity.this, Noitemfoun+".", Toast.LENGTH_SHORT).show();
                                rv_itemtlist.setVisibility(View.GONE);

                            }
                        }catch (JSONException e) {
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



    public void hideKeyboard(){

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etsearch.getWindowToken(), 0);
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
                    Intent io = new Intent(ReOrderItemListingActivity.this, OutShopActivity.class);
                    io.putExtra(fromCategory, valueCategory);
                    finish();
                    startActivity(io);
                    return true;
//                case R.id.navigation_cart:
//                    Intent ic = new Intent(ReOrderItemListingActivity.this, CartActivity.class);
//                    ic.putExtra(fromCart, valueCart);
//                    startActivity(ic);
//                    finish();
//                    //   onBackPressed();
//                    return true;
                case R.id.navigation_home:
                    Intent iha = new Intent(ReOrderItemListingActivity.this, HomeActivity.class);
                    iha.putExtra(fromFavorite, valueFavorite);
                    startActivity(iha);
                    return true;
                case R.id.navigation_favorite:
                    Intent ifa = new Intent(ReOrderItemListingActivity.this, FavouriteActivity.class);
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReOrderItemListingActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ReOrderItemListingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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



/*
package com.perfect.easyshopplus.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Adapter.ItemListAdapter;
import com.perfect.easyshopplus.Adapter.ReOrderItemListAdapter;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;

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

public class ReOrderItemListingActivity extends AppCompatActivity implements View.OnClickListener{

    ProgressDialog progressDialog;
    String ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst, ID_Store, ShopType, ID_SalesOrder,ID_CustomerAddress,OrderType,DeliveryCharge;
    TextView tvcat;
    RecyclerView rv_itemtlist;
    Button btFilter, btSort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_listing); Intent in = getIntent();
       // ID_CategorySecond = in.getStringExtra("ID_CategorySecond");
        //SecondCategoryName = in.getStringExtra("SecondCategoryName");
        FirstCategoryName = in.getStringExtra("FirstCategoryName");
        ID_CategoryFirst = in.getStringExtra("ID_CategoryFirst");
        ID_Store = in.getStringExtra("ID_Store");
        ShopType = in.getStringExtra("ShopType");
        ID_SalesOrder = in.getStringExtra("ID_SalesOrder");
        ID_CustomerAddress = in.getStringExtra("ID_CustomerAddress");
        OrderType = in.getStringExtra("OrderType");
        DeliveryCharge = in.getStringExtra("DeliveryCharge");
        initiateViews();
        setRegViews();
        tvcat.setText(FirstCategoryName+" > "*/
/*+SecondCategoryName+" > "*//*
);
        getItemList();
    }

    private void initiateViews() {
        rv_itemtlist=(RecyclerView)findViewById(R.id.rv_itemtlist);
        tvcat=(TextView) findViewById(R.id.tvcat);
        btFilter = (Button) findViewById(R.id.btFilter);
        btSort = (Button) findViewById(R.id.btSort);
    }

    private void setRegViews() {
        btFilter.setOnClickListener(this);
        btSort.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btSort:
                callSort();
                break;
            case R.id.btFilter:
                callFilter();
                break;
        }
    }
    private void getItemList() {
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
                SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                requestObject1.put("ReqMode", "4");
                requestObject1.put("ID_CategoryFirst", ID_CategoryFirst);
               // requestObject1.put("ID_CategorySecond", ID_CategorySecond);
                requestObject1.put("ShopType", ShopType);
                requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                requestObject1.put("ID_Store", ID_Store);
                requestObject1.put("MemberId", pref3.getString("memberid", null));
                requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
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
                        JSONObject jobj = jObject.getJSONObject("ItemListSearchInfo");
                        JSONArray jarray = jobj.getJSONArray("ItemListInfo");
                        if(jarray.length()!=0) {
                            GridLayoutManager lLayout = new GridLayoutManager(ReOrderItemListingActivity.this, 1);
                            rv_itemtlist.setLayoutManager(lLayout);
                            rv_itemtlist.setHasFixedSize(true);
                            ReOrderItemListAdapter adapter = new ReOrderItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_Store, ShopType, ID_SalesOrder,ID_CustomerAddress,OrderType, DeliveryCharge);
                            rv_itemtlist.setAdapter(adapter);
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
        Intent in=new Intent(this, ReOrderCategoryActivity.class);
        in.putExtra("ID_Store", ID_Store);
        in.putExtra("ShopType", ShopType);
        in.putExtra("ID_SalesOrder", ID_SalesOrder);
        in.putExtra("OrderType", OrderType);
        in.putExtra("ID_CustomerAddress", ID_CustomerAddress);
        in.putExtra("DeliveryCharge", DeliveryCharge);
        // in.putExtra("ID_CategoryFirst", ID_CategoryFirst);
        // in.putExtra("FirstCategoryName", FirstCategoryName);
        startActivity(in);
        finish();
    }

    private void callFilter() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReOrderItemListingActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ReOrderItemListingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.popup_filter, null);
            LinearLayout ll_clear = (LinearLayout) layout.findViewById(R.id.ll_clear);
            LinearLayout ll_apply = (LinearLayout) layout.findViewById(R.id.ll_apply);
            ImageView imclose = (ImageView) layout.findViewById(R.id.imclose);
            final EditText etmin = (EditText) layout.findViewById(R.id.etmin);
            final EditText etmax = (EditText) layout.findViewById(R.id.etmax);
            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();
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
                        applyFilter(Integer.parseInt(etmin.getText().toString()),Integer.parseInt(etmax.getText().toString()));
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReOrderItemListingActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ReOrderItemListingActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.popup_sort, null);
            LinearLayout ll_clear = (LinearLayout) layout.findViewById(R.id.ll_clear);
            LinearLayout ll_apply = (LinearLayout) layout.findViewById(R.id.ll_apply);
            ImageView imclose = (ImageView) layout.findViewById(R.id.imclose);
            final RadioGroup rgPrice = (RadioGroup) layout.findViewById(R.id.rgPrice);
            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();
            rgPrice.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    View radioButton = rgPrice.findViewById(checkedId);
                    int index = rgPrice.indexOfChild(radioButton);
                    switch (index) {
                        case 0:
                            applySort(0);
                            alertDialog.dismiss();
                            break;
                        case 1:
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
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                    SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    requestObject1.put("ShopType", "2");
                    requestObject1.put("ID_CategoryFirst", ID_CategoryFirst);
                  //  requestObject1.put("ID_CategorySecond", ID_CategorySecond);
                    requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                   // requestObject1.put("FK_CustomerPlus", pref4.getString("userid", null));
                    requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("SortField", SortField);
                    requestObject1.put("MemberId", pref3.getString("memberid", null));
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
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
                                GridLayoutManager lLayout = new GridLayoutManager(ReOrderItemListingActivity.this, 1);
                                rv_itemtlist.setLayoutManager(lLayout);
                                rv_itemtlist.setHasFixedSize(true);
                              //  ItemListAdapter adapter = new ItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst);
                                ReOrderItemListAdapter adapter = new ReOrderItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_Store, ShopType, ID_SalesOrder,ID_CustomerAddress,OrderType,DeliveryCharge);
                                rv_itemtlist.setAdapter(adapter);
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
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                    requestObject1.put("ShopType", "2");
                    requestObject1.put("ID_CategoryFirst", ID_CategoryFirst);
                   // requestObject1.put("ID_CategorySecond", ID_CategorySecond);
                    requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                    requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("Criteria1", MinPrice);
                    requestObject1.put("Criteria2", MaxPrice);
                    requestObject1.put("MemberId", pref3.getString("memberid", null));
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
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
                                GridLayoutManager lLayout = new GridLayoutManager(ReOrderItemListingActivity.this, 1);
                                rv_itemtlist.setLayoutManager(lLayout);
                                rv_itemtlist.setHasFixedSize(true);
                              //  ItemListAdapter adapter = new ItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst);
                                ReOrderItemListAdapter adapter = new ReOrderItemListAdapter(ReOrderItemListingActivity.this, jarray, ID_Store, ShopType, ID_SalesOrder,ID_CustomerAddress,OrderType,DeliveryCharge);
                                rv_itemtlist.setAdapter(adapter);
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

}
*/
