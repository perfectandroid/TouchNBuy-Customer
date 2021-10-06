package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Adapter.CategoryListInfoAdapter;
import com.perfect.easyshopplus.Adapter.StoreAdapter;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.ItemClickListener;
import com.perfect.easyshopplus.Utility.StoreClickListener;

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

public class MainCategoryActivity extends AppCompatActivity implements View.OnClickListener, StoreClickListener {

    String TAG = "MainCategoryActivity";
    ImageView im,im_change_lang,im_logot;
    RecyclerView rvMaincategory;
    ProgressDialog progressDialog;
    JSONArray Jarray = null;
    RecyclerView rv_recentStoreList ;
    RecyclerView rv_storeist;
    View vSeperator ;
    String StoreCategory;
    TextView tv_header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main_category);

        initiateViews();
        setRegViews();
        SharedPreferences trackorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF188, 0);
        tv_header.setText("");

        getList();

    }




    private void initiateViews() {

        im = (ImageView) findViewById(R.id.im);
        rvMaincategory = (RecyclerView) findViewById(R.id.rvMaincategory);
        tv_header = (TextView)findViewById(R.id.tv_header);

    }

    private void setRegViews() {
        im.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.im:
                showBackPopup();
                break;
        }

    }

    private void getList() {
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
                    requestObject1.put("ReqMode", "21");
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1   154    "+requestObject1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getStoreCategoryDetails(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG,"response   166    "+response.body());
                            JSONObject jObject = new JSONObject(response.body());
                            Log.i("Response",response.body());
                            JSONObject jmember = jObject.getJSONObject("StoreCategoryDetails");
                            JSONObject object = new JSONObject(String.valueOf(jmember));
                            Log.i("First1 ",String.valueOf(object));
                            Jarray  = object.getJSONArray("storeCategoryLists");
                            String jstatus = jObject.getString("StatusCode");
                            if(jstatus.equals("0")){
                                if(Jarray.length()!=0) {
                                    GridLayoutManager lLayout = new GridLayoutManager(getApplicationContext(), 1);
                                    rvMaincategory.setLayoutManager(lLayout);
                                    rvMaincategory.setHasFixedSize(true);
                                    CategoryListInfoAdapter adapter = new CategoryListInfoAdapter(getApplicationContext(), Jarray);
                                    rvMaincategory.setAdapter(adapter);
//                                    adapter.setClickListener(MainCategoryActivity.this);
////                                    rvMaincategory.setOnItemClickListener(new CategoryListInfoAdapter.onItemItemClickListener() {
////                                        @Override
////                                        public void onItemClickListener(View view, int position, MyData data) {
////                                            //do on item click functionality here
////                                        }
////                                    });


                                } else {
                                }
                            }
                            else{
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
            Intent in = new Intent(this, NoInternetActivity.class);
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
      //  super.onBackPressed();
        showBackPopup();
    }

    private void showBackPopup() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainCategoryActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) MainCategoryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.exit_back_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);

            TextView tv_popupdelete = (TextView) layout.findViewById(R.id.tv_popupdelete);
            TextView tv_popupdeleteinfo3 = (TextView) layout.findViewById(R.id.tv_popupdeleteinfo3);
            TextView edit = (TextView) layout.findViewById(R.id.edit);
            TextView tvno = (TextView) layout.findViewById(R.id.tvno);



            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setView(layout, 0, 0, 0, 0);

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
                  //  navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
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
    public void onClick(View view, int position) {
        Log.e(TAG,"position  327   "+position);

        try {
//            JSONObject jsonObject = Jarray.getJSONObject(position);
//
//            SharedPreferences ShopType1 = getApplicationContext().getApplicationContext().getSharedPreferences(Config.SHARED_PREF9, 0);
//            SharedPreferences.Editor ShopTypeeditor1 = ShopType1.edit();
//            ShopTypeeditor1.putString("ShopType", "2");
//            ShopTypeeditor1.commit();
//
//            SharedPreferences StoreCategorySP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF47, 0);
//            SharedPreferences.Editor StoreCategorySPeditor = StoreCategorySP.edit();
//            StoreCategorySPeditor.putString("StoreCategory", jsonObject.getString("StoreCategory"));
//            StoreCategorySPeditor.commit();
//
//
//
//            StoreCategory =  jsonObject.getString("StoreCategory");


           // setStoreList();
        }catch (Exception e){

        }


    }

    private void setStoreList() {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainCategoryActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) MainCategoryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.storelist_popup, null);

            rv_recentStoreList = (RecyclerView) layout.findViewById(R.id.rv_recentStoreList);
            rv_storeist = (RecyclerView) layout.findViewById(R.id.rv_storeist);
            vSeperator = (View) layout.findViewById(R.id.vSeperator);


            getCategories();


            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
//            SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF184, 0);
//            tv_popupdelete.setText(pref4.getString("Confirmexit", null));
//
//            SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF185, 0);
//            tv_popupdeleteinfo3.setText(pref5.getString("areyousureyouwanttoexit", null));
//
//            SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF186, 0);
//            edit.setText(pref6.getString("yes", null));
//
//            SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF187, 0);
//            tvno.setText(pref7.getString("no", null));
//
//
//            cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    alertDialog.dismiss();
//                    navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
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
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getCategories() {

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
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
                        requestObject1.put("ReqMode", "5");
                        requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                        requestObject1.put("StoreCategory", StoreCategory);
                        SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                        requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                        Log.e(TAG,"requestObject1   449    "+requestObject1);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                    Call<String> call = apiService.getStoresList(body);
                    call.enqueue(new Callback<String>() {
                        @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            try {
                                progressDialog.dismiss();
                                JSONArray jarray ;
                                Log.e(TAG,"onResponse   403  "+response.body());
                                JSONObject jObject = new JSONObject(response.body());
                                JSONObject jobj = jObject.getJSONObject("StoreListDetailsInfo");

                                if (jObject.getString("StatusCode").equals("0")) {
                                    if (jobj.getString("OtherStoreListInfo").equals("null")) {

//                                        AlertDialog.Builder builder= new AlertDialog.Builder(StoreActivity.this);
//                                        builder.setMessage(""+Nodatafound.getString("Nodatafound",null))
//                                                .setCancelable(false)
//                                                .setPositiveButton(""+sOK.getString("OK",null), new DialogInterface.OnClickListener() {
//                                                    public void onClick(DialogInterface dialog, int id) {
//                                                        //clearall();
//                                                    }
//                                                });
//                                        AlertDialog alert = builder.create();
//                                        alert.show();
                                    }
                                    else{

                                        // rv_storeist.setVisibility(View.VISIBLE);
                                        jarray = jobj.getJSONArray("OtherStoreListInfo");
                                        Log.e(TAG,"jarray  479 ist  "+jarray.length()+"  "+jarray);
                                        GridLayoutManager lLayout = new GridLayoutManager(MainCategoryActivity.this, 1);
                                        rv_storeist.setLayoutManager(lLayout);
                                        rv_storeist.setHasFixedSize(true);
                                        StoreAdapter adapter = new StoreAdapter(MainCategoryActivity.this, jarray);
                                        rv_storeist.setAdapter(adapter);

//                                        setdataStorewise(jarray);
                                    }

                                    if (jobj.getString("SearchedStoreListInfo").equals("null")){
//                                        rv_recentStoreList.setVisibility(View.GONE);
//                                        vSeperator.setVisibility(View.GONE);
                                        Log.e(TAG,"jarray  479 2nd  ");
                                    }
                                    else {
//                                        rv_recentStoreList.setVisibility(View.VISIBLE);
//                                        vSeperator.setVisibility(View.GONE);
                                        jarray = jobj.getJSONArray("SearchedStoreListInfo");
                                        Log.e(TAG,"jarray  479 3rd  "+jarray.length()+"  "+jarray);
                                        GridLayoutManager lLayout = new GridLayoutManager(MainCategoryActivity.this, 1);
                                        rv_recentStoreList.setLayoutManager(lLayout);
                                        rv_recentStoreList.setHasFixedSize(true);
                                        StoreAdapter adapter = new StoreAdapter(MainCategoryActivity.this, jarray);
                                        rv_recentStoreList.setAdapter(adapter);

                                        //  setdataStorewise(jarray);
                                    }
                                }
                                else if (jObject.getString("StatusCode").equals("2")) {
//                                    rv_storeist.setVisibility(View.GONE);
//                                    AlertDialog.Builder builder= new AlertDialog.Builder(StoreActivity.this);
//                                    builder.setMessage(""+Nodatafound.getString("Nodatafound",null))
//                                            .setCancelable(false)
//                                            .setPositiveButton(""+sOK.getString("OK",null), new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    finish();
//                                                }
//                                            });
//                                    AlertDialog alert = builder.create();
//                                    alert.show();
                                }
                                else  {
//                                    rv_storeist.setVisibility(View.GONE);
//                                    AlertDialog.Builder builder= new AlertDialog.Builder(StoreActivity.this);
//                                    builder.setMessage(""+SomethingwentwrongTryagainlater.getString("SomethingwentwrongTryagainlater",null))
//                                            .setCancelable(false)
//                                            .setPositiveButton(""+sOK.getString("OK",null), new DialogInterface.OnClickListener() {
//                                                public void onClick(DialogInterface dialog, int id) {
//                                                    //clearall();
//                                                }
//                                            });
//                                    AlertDialog alert = builder.create();
//                                    alert.show();
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
            }catch (Exception e) {
                e.printStackTrace();
//                Toast.makeText(getApplicationContext(),""+SomethingwentwrongTryagainlater.getString("SomethingwentwrongTryagainlater",null),Toast.LENGTH_LONG).show();
            }
        }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }
}