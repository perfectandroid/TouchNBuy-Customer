package com.perfect.easyshopplus.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Adapter.AdapterScratched;
import com.perfect.easyshopplus.Adapter.ScratchcardAdapter;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.FullLenghtRecyclerview;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.ItemClickListener;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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

public class GiftVoucherActivity extends AppCompatActivity implements View.OnClickListener,ItemClickListener {

    String TAG = "GiftVoucherActivity";
    TextView tv_header,tv_rewards_amnt,tv_total_rewards;
    TextView tv_MyRewards,tv_GiftCoupons;
    ImageView im;
    FullLenghtRecyclerview rv_rewards,rv_scratch_rewards;
    ProgressDialog progressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_voucher);

        initiateViews();
        setRegViews();


        SharedPreferences BalancetoRedeem = getApplicationContext().getSharedPreferences(Config.SHARED_PREF391, 0);
        tv_total_rewards.setText(""+BalancetoRedeem.getString("BalancetoRedeem",""));

        SharedPreferences GiftCoupons = getApplicationContext().getSharedPreferences(Config.SHARED_PREF389, 0);
        tv_GiftCoupons.setText(""+GiftCoupons.getString("GiftCoupons",""));

        SharedPreferences MyRewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF390, 0);
        tv_MyRewards.setText(""+MyRewards.getString("MyRewards",""));
        SharedPreferences Rewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF392, 0);
        tv_header.setText(""+Rewards.getString("Rewards",""));

        getGiftVoucher();
    }



    private void setRegViews() {

        im.setOnClickListener(this);
    }

    private void initiateViews() {

        tv_header=(TextView) findViewById(R.id.tv_header);
        im = (ImageView) findViewById(R.id.im);

        tv_rewards_amnt=(TextView) findViewById(R.id.tv_rewards_amnt);
        tv_total_rewards=(TextView) findViewById(R.id.tv_total_rewards);

        rv_rewards = (FullLenghtRecyclerview) findViewById(R.id.rv_rewards);
        rv_scratch_rewards = (FullLenghtRecyclerview) findViewById(R.id.rv_scratch_rewards);

        tv_MyRewards=(TextView) findViewById(R.id.tv_MyRewards);
        tv_GiftCoupons=(TextView) findViewById(R.id.tv_GiftCoupons);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.im:

                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
//            progressDialog.show();
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

                    Log.e(TAG,"requestObject1   183  "+requestObject1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getGiftVoucherList(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
//                            progressDialog.dismiss();
                            Log.e(TAG,"response   183  "+response.body());

                            JSONObject jObject = new JSONObject(response.body());
                            if(jObject.getString("StatusCode").equals("0")){
                                progressDialog.dismiss();

                                JSONObject jobj = jObject.getJSONObject("GiftVoucherListInfo");
                                Log.e(TAG,"count    185   "+jobj.getString("count"));
                                tv_rewards_amnt.setText(""+jobj.getString("count"));
                                JSONArray jarray = jobj.getJSONArray("VoucherList");
//                                JSONArray jarray1 = jobj.getJSONArray("VoucherList");
//                                JSONArray jarray2 = jobj.getJSONArray("VoucherList");
                               // Log.e(TAG,"StatusCode  if 183  "+jarray);
                                JSONArray jarray1 = new JSONArray();
                                JSONArray jarray2 = new JSONArray();
                                for (int i=0;i<jarray.length();i++){

                                    JSONObject jsonObject=jarray.getJSONObject(i);
                                    Log.e(TAG,"StatusCode  if 183  "+jsonObject.getBoolean("CRScratch"));
                                    if (jsonObject.getBoolean("CRScratch")==true){
                                        Log.e(TAG,"CRScratch   201    "+true);
                                        Log.e(TAG,"CRScratch   2035    "+jsonObject);
                                       // jarray1.remove(i);
                                        jarray1.put(jsonObject);
                                    }
                                    if (jsonObject.getBoolean("CRScratch")==false){
                                        Log.e(TAG,"CRScratch   201    "+false);
                                        Log.e(TAG,"CRScratch   2045    "+jsonObject);
                                      //  jarray2.remove(i);
                                        jarray2.put(jsonObject);
                                    }
                                }

                                Log.e(TAG,"jarray1   5005    "+jarray1);
                                Log.e(TAG,"jarray2   5006    "+jarray2);

                                if (jarray1.length()>0){
                                    tv_MyRewards.setVisibility(View.VISIBLE);
                                }else {
                                    tv_MyRewards.setVisibility(View.GONE);
                                }
                                if (jarray2.length()>0){

                                    tv_GiftCoupons.setVisibility(View.VISIBLE);
                                }else {
                                    tv_GiftCoupons.setVisibility(View.GONE);
                                }
//
                                GridLayoutManager lLayout = new GridLayoutManager(GiftVoucherActivity.this, 2);
                                rv_rewards.setLayoutManager(lLayout);
                                rv_rewards.setHasFixedSize(true);
                                ScratchcardAdapter adapter = new ScratchcardAdapter(GiftVoucherActivity.this, jarray2);
                                rv_rewards.setAdapter(adapter);
                                adapter.setClickListener(GiftVoucherActivity.this);

                                GridLayoutManager lLayout1 = new GridLayoutManager(GiftVoucherActivity.this, 2);
                                rv_scratch_rewards.setLayoutManager(lLayout1);
                                rv_scratch_rewards.setHasFixedSize(true);
                                AdapterScratched adapter1 = new AdapterScratched(GiftVoucherActivity.this, jarray1);
                                rv_scratch_rewards.setAdapter(adapter1);


                            }
                            else {
//                                progressDialog.dismiss();

                                Log.e(TAG,"StatusCode else  183  ");
                                tv_MyRewards.setVisibility(View.GONE);
                                tv_GiftCoupons.setVisibility(View.GONE);
                                tv_rewards_amnt.setText("0");
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG,"Exception  183  "+e.toString());
                            tv_GiftCoupons.setVisibility(View.GONE);
                            tv_MyRewards.setVisibility(View.GONE);
//                            progressDialog.dismiss();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
//                        progressDialog.dismiss();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
//                progressDialog.dismiss();

            }
        }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
//            progressDialog.dismiss();

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
    public void onClick(int position,String CustomerReward) {

        Log.e(TAG,"318  "+position+ "  "+CustomerReward);
        UpdateScratchCard(CustomerReward);

    }



    private void UpdateScratchCard(String customerReward) {
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
//            progressDialog.show();
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

                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);


                    requestObject1.put("ReqMode","2");
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    requestObject1.put("FK_CustomerReward",customerReward);


                    Log.e(TAG,"requestObject1   183  "+requestObject1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.UpdateScratchCard(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
//                            progressDialog.dismiss();
                            Log.e(TAG,"response   183  "+response.body());

                            JSONObject jObject = new JSONObject(response.body());
                            if(jObject.getString("StatusCode").equals("0")){
                                JSONObject jobj = jObject.getJSONObject("commonCheck");

                                Toast.makeText(getApplicationContext(),""+jobj.getString("ResponseMessage"),Toast.LENGTH_SHORT).show();

                                getGiftVoucher();
                            }else {
                                JSONObject jobj = jObject.getJSONObject("commonCheck");
                                Log.e(TAG,"StatusCode else  183  ");
                                Toast.makeText(getApplicationContext(),""+jobj.getString("ResponseMessage"),Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {
//                            progressDialog.dismiss();

                            e.printStackTrace();
                            Log.e(TAG,"Exception  183  "+e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
//                        progressDialog.dismiss();
                    }
                });

            }
            catch (Exception e) {
                e.printStackTrace();
//                progressDialog.dismiss();

            }
        }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
//            progressDialog.dismiss();

        }
    }}