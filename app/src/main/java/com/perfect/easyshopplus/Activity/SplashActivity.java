 package com.perfect.easyshopplus.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.BuildConfig;
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

public class SplashActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    private static int SPLASH_TIME_OUT = 4000;
    String TAG= "SplashActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        ImageView imgSplash=findViewById(R.id.imgSplash);
        Glide.with( this ).load( R.drawable.splashgif ).into( imgSplash );

//        String s = "AIRMTST|ARP1553593909862|WSP20147088948|0|2.00|SP2|555942|03|INR|MDNA|NA|NA|00000000.00|28-07-2021 11:34:57|0399|NA|NA|NA|NA|NA|NA|NA|NA|NA|PBE10002-IPAY0100180 - Authentication not available.|2085500438";
//        Log.e(TAG,"result   6745    "+s);
//        String result = s.replace(s.split("\\|")[25],"");
//        Log.e(TAG,"result   6745    "+result);


        Log.e(TAG,"app_name   79  "+getResources().getString(R.string.app_name));
        if(getResources().getString(R.string.app_name).equals("JZT CART")){
            SharedPreferences Baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
            SharedPreferences.Editor BaseurlprefEditor = Baseurlpref.edit();
            BaseurlprefEditor.putString("BaseURL", "https://202.164.150.65:14001/TouchNBuyAPI/api/");//local
//                BaseurlprefEditor.putString("BaseURL", "https://202.21.32.35:14001/TouchNBuyAPIQA/api/");//local
//                BaseurlprefEditor.putString("BaseURL", "https://40.81.77.56:93/justcartAPI/api/");//justcart live
                BaseurlprefEditor.commit();
                SharedPreferences Imageurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
                SharedPreferences.Editor ImageurlprefEditor = Imageurlpref.edit();
//                ImageurlprefEditor.putString("ImageURL", "https://202.164.150.65:14001/TouchNBuyAPI");//local
                ImageurlprefEditor.putString("ImageURL", "https://202.21.32.35:14001/TouchNBuyAPIQA");//local
//                ImageurlprefEditor.putString("ImageURL", "https://40.81.77.56:93/justcartAPI");//justcart live
                ImageurlprefEditor.commit();

//                getResellerDetails();
            versionCkecking();
        }
        else if(getResources().getString(R.string.app_name).equals("Pulikkottil Hypermarket")){
            SharedPreferences Baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
            SharedPreferences.Editor BaseurlprefEditor = Baseurlpref.edit();
//            BaseurlprefEditor.putString("BaseURL", "https://202.164.150.65:14001/TouchNBuyAPI/api/");
//
            BaseurlprefEditor.putString("BaseURL", "https://103.203.75.124:14002/api/");//Live New 23.08.2021
       //     BaseurlprefEditor.putString("BaseURL", "https://202.164.150.65:14001/TouchNBuyAPI/api/");
//
//            BaseurlprefEditor.putString("BaseURL", "https://103.203.75.124:14002/api/");//Live New 23.08.2021
//            BaseurlprefEditor.putString("BaseURL", "https://shop.pulikkottilhypermarket.in/api/");//Live New 24.08.2021 domain

            BaseurlprefEditor.commit();
            SharedPreferences Imageurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
            SharedPreferences.Editor ImageurlprefEditor = Imageurlpref.edit();
//            ImageurlprefEditor.putString("ImageURL", "https://202.164.150.65:14001/TouchNBuyAPI");

            ImageurlprefEditor.putString("ImageURL", "https://103.203.75.124:14002"); //Live New 23.08.2021
         //   ImageurlprefEditor.putString("ImageURL", "https://202.164.150.65:14001/TouchNBuyAPI");

//            ImageurlprefEditor.putString("ImageURL", "https://103.203.75.124:14002"); //Live New 23.08.2021
//            ImageurlprefEditor.putString("ImageURL", "https://shop.pulikkottilhypermarket.in"); //Live New 24.08.2021 domain

            ImageurlprefEditor.commit();

//            getResellerDetails();
            versionCkecking();
        }
        else if(getResources().getString(R.string.app_name).equals("NeethiMed")){
            SharedPreferences Baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
            SharedPreferences.Editor BaseurlprefEditor = Baseurlpref.edit();
            BaseurlprefEditor.putString("BaseURL", "https://202.164.150.65:14001/TouchNBuyAPI/api/");
//            BaseurlprefEditor.putString("BaseURL", " https://202.164.150.136:14007/TouchnBuyAPI/api/"); // Live
            BaseurlprefEditor.commit();
            SharedPreferences Imageurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
            SharedPreferences.Editor ImageurlprefEditor = Imageurlpref.edit();
            ImageurlprefEditor.putString("ImageURL", "https://202.164.150.65:14001/TouchNBuyAPI");
//            ImageurlprefEditor.putString("ImageURL", "https://202.164.150.136:14007/TouchnBuyAPI"); // Live
            ImageurlprefEditor.commit();

//            getResellerDetails();
            versionCkecking();
        }
        else if(getResources().getString(R.string.app_name).equals("TNB Demo")){
            SharedPreferences Baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
            SharedPreferences.Editor BaseurlprefEditor = Baseurlpref.edit();
            BaseurlprefEditor.putString("BaseURL", "https://202.164.150.65:14001/TouchNBuyAPI/api/");
            BaseurlprefEditor.commit();

            SharedPreferences Imageurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
            SharedPreferences.Editor ImageurlprefEditor = Imageurlpref.edit();
            ImageurlprefEditor.putString("ImageURL", "https://202.164.150.65:14001/TouchNBuyAPI");

            ImageurlprefEditor.commit();

//            getResellerDetails();
            versionCkecking();
        }
        else if(getResources().getString(R.string.app_name).equals("NeethiCoOp")){
            SharedPreferences Baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
            SharedPreferences.Editor BaseurlprefEditor = Baseurlpref.edit();
            BaseurlprefEditor.putString("BaseURL", " https://117.241.72.188:14051/Touchnbuyapi/api/");
            BaseurlprefEditor.commit();

            SharedPreferences Imageurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
            SharedPreferences.Editor ImageurlprefEditor = Imageurlpref.edit();
            ImageurlprefEditor.putString("ImageURL", "https://117.241.72.188:14051/Touchnbuyapi");

            ImageurlprefEditor.commit();

//            getResellerDetails();
            versionCkecking();
        }
        else if(getResources().getString(R.string.app_name).equals("TNB QA")){
            SharedPreferences Baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
            SharedPreferences.Editor BaseurlprefEditor = Baseurlpref.edit();
            BaseurlprefEditor.putString("BaseURL", "https://112.133.227.123:14019/TouchNBuyQAAPI/api/");// https

            BaseurlprefEditor.commit();

            SharedPreferences Imageurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
            SharedPreferences.Editor ImageurlprefEditor = Imageurlpref.edit();
            ImageurlprefEditor.putString("ImageURL", "https://112.133.227.123:14019/TouchNBuyQAAPI"); // https

            ImageurlprefEditor.commit();


//            getResellerDetails();
            versionCkecking();
        }
        else if(getResources().getString(R.string.app_name).equals("ASCB")){
            SharedPreferences Baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
            SharedPreferences.Editor BaseurlprefEditor = Baseurlpref.edit();
            BaseurlprefEditor.putString("BaseURL", "https://117.241.72.68:14002/TouchnbuyAPI/api/");// https

            BaseurlprefEditor.commit();

            SharedPreferences Imageurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
            SharedPreferences.Editor ImageurlprefEditor = Imageurlpref.edit();
            ImageurlprefEditor.putString("ImageURL", "https://117.241.72.68:14002/TouchnbuyAPI"); // https

            ImageurlprefEditor.commit();


//            getResellerDetails();
            versionCkecking();
        }
        else{
            getSplash();
        }
    }

    private void getSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getResources().getString(R.string.app_name).equals("JZT CART") || getResources().getString(R.string.app_name).equals("Pulikkottil Hypermarket") ) {

                    SharedPreferences Loginpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                    if(Loginpref.getString("loginsession", null) == null ){
                        getLabels();
                    } else if(Loginpref.getString("loginsession", null) != null && !Loginpref.getString("loginsession", null).isEmpty()&&Loginpref.getString("loginsession", null).equals("Yes")){
//                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
//                        startActivity(i);
                        SharedPreferences StoreCategorySP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF47, 0);
                        Log.e(TAG,"StoreCategorySP   206    "+StoreCategorySP.getString("StoreCategory", ""));
//                        if (StoreCategorySP.getString("StoreCategory", null) == null){
//                            Log.e(TAG,"StoreCategorySP   208 1   ");
//                            Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
//                            startActivity(i);
//                            finish();
//
//                        }else {
                            Log.e(TAG,"StoreCategorySP   213 2   ");
                            SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF36, 0);
                            SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF37, 0);

                            Log.e(TAG,"StoreCategorySP   216 21   "+pref6.getString("RequiredStore", null) +"   "+pref7.getString("RequiredStoreCategory", null));
                            if(pref6.getString("RequiredStore", null).equals("true")&&
                                    pref7.getString("RequiredStoreCategory", null).equals("true")){
                                SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                Log.e(TAG,"StoreCategorySP   221 3   "+StoreName.getString("StoreName", null));

                                if(StoreName.getString("StoreName", null) == null || StoreName.getString("StoreName", null).isEmpty() || StoreName.getString("StoreName", null).equals("")){
                                    Log.e(TAG,"StoreCategorySP   225 4   ");
                                    Intent i = new Intent(SplashActivity.this, MainCategoryActivity.class);
                                    startActivity(i);
                                    finish();
                                }else {
                                    Log.e(TAG,"StoreCategorySP   229 5   ");
                                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                                    startActivity(i);
                                    finish();
                                }

                            }
                            else if(pref6.getString("RequiredStore", null).equals("false")&&
                                    pref7.getString("RequiredStoreCategory", null).equals("false")){
                                Log.e(TAG,"StoreCategorySP   237 6   ");
                                SharedPreferences Somethingwentwrongsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF72, 0);

//                            singlestore="1";
                                getSinglestoreDetails();
                            }else if(pref6.getString("RequiredStore", null).equals("true")&&
                                    pref7.getString("RequiredStoreCategory", null).equals("false")){
//                            singlestore="1";
//                            getSinglestoreDetails();
                                SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                Log.e(TAG,"StoreCategorySP   221 3   "+StoreName.getString("StoreName", null));
                                if(StoreName.getString("StoreName", null) == null || StoreName.getString("StoreName", null).isEmpty() || StoreName.getString("StoreName", null).equals("")){
                                    Log.e(TAG,"StoreCategorySP   225 4   ");
                                    Intent i = new Intent(SplashActivity.this, StoreActivity.class);
                                    startActivity(i);
                                    finish();
                                }else {
                                    Log.e(TAG,"StoreCategorySP   229 5   ");
                                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                                    startActivity(i);
                                    finish();
                                }

                            }

//                        }
                    } else if(Loginpref.getString("loginsession", null) != null && !Loginpref.getString("loginsession", null).isEmpty()&&Loginpref.getString("loginsession", null).equals("No")){
                        getLabels();
                    }
                }
                else{
                    SharedPreferences Loginpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                    if(Loginpref.getString("loginsession", null) == null ){
                        Intent i = new Intent(SplashActivity.this, LocationActivity.class);
                        startActivity(i);
                        finish();
                    }else if(Loginpref.getString("loginsession", null) != null && !Loginpref.getString("loginsession", null).isEmpty()&&Loginpref.getString("loginsession", null).equals("Yes")){
                        Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(i);
                        finish();
                    }else if(Loginpref.getString("loginsession", null) != null && !Loginpref.getString("loginsession", null).isEmpty()&&Loginpref.getString("loginsession", null).equals("No")){
                        Intent i = new Intent(SplashActivity.this, LocationActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        }, SPLASH_TIME_OUT);
    }

//    private void getSplash1() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                SharedPreferences Loginpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//
//
//                if(Loginpref.getString("loginsession", null) == null ){
//                    getLabels();
//                }else if(Loginpref.getString("loginsession", null) != null && !Loginpref.getString("loginsession", null).isEmpty()&&Loginpref.getString("loginsession", null).equals("Yes")){
//                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
//                    startActivity(i);
//                }else if(Loginpref.getString("loginsession", null) != null && !Loginpref.getString("loginsession", null).isEmpty()&&Loginpref.getString("loginsession", null).equals("No")){
//                    getLabels();
//                }
//            }
//        }, SPLASH_TIME_OUT);
//    }



    private void getSplash1() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                SharedPreferences Loginpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                Log.e(TAG,"getSplash1   2671   "+Loginpref.getString("loginsession", null));
                SharedPreferences StoreCategorySP1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF47, 0);
                Log.e(TAG,"StoreCategorySP   206    "+StoreCategorySP1.getString("StoreCategory", ""));
                if(Loginpref.getString("loginsession", null) == null ){
                    getLabels();
                }else if(Loginpref.getString("loginsession", null) != null && !Loginpref.getString("loginsession", null).isEmpty()&&Loginpref.getString("loginsession", null).equals("Yes")){
//                    Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                    SharedPreferences StoreCategorySP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF47, 0);
                    Log.e(TAG,"StoreCategorySP   206    "+StoreCategorySP.getString("StoreCategory", ""));
//                    if (StoreCategorySP.getString("StoreCategory", null) == null){
//                        Log.e(TAG,"StoreCategorySP   208 1   ");
//                        Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
//                        startActivity(i);
//                        finish();
//
//                    }else {
                        Log.e(TAG,"StoreCategorySP   213 2   ");
                        SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF36, 0);
                        SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF37, 0);

                        Log.e(TAG,"StoreCategorySP   216 21   "+pref6.getString("RequiredStore", null) +"   "+pref7.getString("RequiredStoreCategory", null));
                        if(pref6.getString("RequiredStore", null).equals("true")&&
                                pref7.getString("RequiredStoreCategory", null).equals("true")){
                            SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                            Log.e(TAG,"StoreCategorySP   221 3   "+StoreName.getString("StoreName", null));


                            if(StoreName.getString("StoreName", null) == null || StoreName.getString("StoreName", null).isEmpty() || StoreName.getString("StoreName", null).equals("")){
                                Log.e(TAG,"StoreCategorySP   225 4   ");
                                Intent i = new Intent(SplashActivity.this, MainCategoryActivity.class);
                                startActivity(i);
                                finish();
                            }else {
                                Log.e(TAG,"StoreCategorySP   229 5   ");
                                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            }

                        }
                        else if(pref6.getString("RequiredStore", null).equals("false")&&
                                pref7.getString("RequiredStoreCategory", null).equals("false")){
                            Log.e(TAG,"StoreCategorySP   237 6   ");
                            SharedPreferences Somethingwentwrongsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF72, 0);

//                            singlestore="1";
                            getSinglestoreDetails();
                        }else if(pref6.getString("RequiredStore", null).equals("true")&&
                                pref7.getString("RequiredStoreCategory", null).equals("false")){
//                            singlestore="1";
//                            getSinglestoreDetails();
                            SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                            Log.e(TAG,"StoreCategorySP   221 3   "+StoreName.getString("StoreName", null));
                            if(StoreName.getString("StoreName", null) == null || StoreName.getString("StoreName", null).isEmpty() || StoreName.getString("StoreName", null).equals("")){
                                Log.e(TAG,"StoreCategorySP   225 4   ");
                                Intent i = new Intent(SplashActivity.this, StoreActivity.class);
                                startActivity(i);
                                finish();
                            }else {
                                Log.e(TAG,"StoreCategorySP   229 5   ");
                                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                                startActivity(i);
                                finish();
                            }

                        }

//                    }
//
                }else if(Loginpref.getString("loginsession", null) != null && !Loginpref.getString("loginsession", null).isEmpty()&&Loginpref.getString("loginsession", null).equals("No")){
                    getLabels();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void getLabels() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = pref.getString("BaseURL", null);
        String imageurl = pref1.getString("ImageURL", null);

        Log.e(TAG,"BaseURL  265   "+BASEURL);

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
                    requestObject1.put("ReqMode","34");

                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    if (IDLanguages.getString("ID_Languages", null) == null){
                        requestObject1.put("ID_Languages","1");
                    }
                    else{
                        Log.e(TAG,"ID_Languages  299  "+IDLanguages.getString("ID_Languages", null));
                        requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG,"requestObject1   306   "+requestObject1);
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getLabels(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {


                        try {
                            Log.e(TAG,"onResponse  314  "+response.body());
                            JSONObject jObject = new JSONObject(response.body());
                            if(jObject.getString("StatusCode").equals("0")) {

                                JSONObject jobj = jObject.getJSONObject("LabelListInfo");
                                JSONObject jobj1 = jobj.getJSONObject("Labels");
                                Log.e(TAG,"strJ  320  "+jobj1);
                                Log.e(TAG,"strJ  321  "+jobj1.getString("Changelanguage"));


                                SharedPreferences availabilityofthestockpackaging = getApplicationContext().getSharedPreferences(Config.SHARED_PREF65, 0);
                                SharedPreferences.Editor availabilityofthestockpackagingeditor = availabilityofthestockpackaging.edit();
                                availabilityofthestockpackagingeditor.putString("availabilityofthestockpackaging", jobj1.getString("availabilityofthestockpackaging"));
                                availabilityofthestockpackagingeditor.commit();

                                SharedPreferences GPSisdisabledinyourdevice = getApplicationContext().getSharedPreferences(Config.SHARED_PREF66, 0);
                                SharedPreferences.Editor GPSisdisabledinyourdeviceeditor = GPSisdisabledinyourdevice.edit();
                                GPSisdisabledinyourdeviceeditor.putString("GPSisdisabledinyourdevice", jobj1.getString("GPSisdisabledinyourdevice"));
                                GPSisdisabledinyourdeviceeditor.commit();

                                SharedPreferences GotoSettingsPageToEnableGPS = getApplicationContext().getSharedPreferences(Config.SHARED_PREF67, 0);
                                SharedPreferences.Editor GotoSettingsPageToEnableGPSeditor = GotoSettingsPageToEnableGPS.edit();
                                GotoSettingsPageToEnableGPSeditor.putString("GotoSettingsPageToEnableGPS", jobj1.getString("GotoSettingsPageToEnableGPS"));
                                GotoSettingsPageToEnableGPSeditor.commit();

                                SharedPreferences PleaseacceptTermsandConditions = getApplicationContext().getSharedPreferences(Config.SHARED_PREF68, 0);
                                SharedPreferences.Editor PleaseacceptTermsandConditionseditor = PleaseacceptTermsandConditions.edit();
                                PleaseacceptTermsandConditionseditor.putString("PleaseacceptTermsandConditions", jobj1.getString("PleaseacceptTermsandConditions"));
                                PleaseacceptTermsandConditionseditor.commit();

                                SharedPreferences Pleaseselectdeliverytime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF69, 0);
                                SharedPreferences.Editor Pleaseselectdeliverytimeeditor = Pleaseselectdeliverytime.edit();
                                Pleaseselectdeliverytimeeditor.putString("Pleaseselectdeliverytime", jobj1.getString("Pleaseselectdeliverytime"));
                                Pleaseselectdeliverytimeeditor.commit();

                                SharedPreferences sorryonedayprocessdeliciousorderselectnextday = getApplicationContext().getSharedPreferences(Config.SHARED_PREF70, 0);
                                SharedPreferences.Editor sorryonedayprocessdeliciousorderselectnextdayeditor = sorryonedayprocessdeliciousorderselectnextday.edit();
                                sorryonedayprocessdeliciousorderselectnextdayeditor.putString("sorryonedayprocessdeliciousorderselectnextday", jobj1.getString("sorryonedayprocessdeliciousorderselectnextday"));
                                sorryonedayprocessdeliciousorderselectnextdayeditor.commit();

                                SharedPreferences PleaseselectdeliveryDate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF71, 0);
                                SharedPreferences.Editor PleaseselectdeliveryDateeditor = PleaseselectdeliveryDate.edit();
                                PleaseselectdeliveryDateeditor.putString("PleaseselectdeliveryDate", jobj1.getString("PleaseselectdeliveryDate"));
                                PleaseselectdeliveryDateeditor.commit();


                                SharedPreferences Somethingwentwrong = getApplicationContext().getSharedPreferences(Config.SHARED_PREF72, 0);
                                SharedPreferences.Editor Somethingwentwrongeditor = Somethingwentwrong.edit();
                                Somethingwentwrongeditor.putString("Somethingwentwrong", jobj1.getString("Somethingwentwrong"));
                                Somethingwentwrongeditor.commit();

                                SharedPreferences HolidayPleaseselectotherdate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF73, 0);
                                SharedPreferences.Editor HolidayPleaseselectotherdateeditor = HolidayPleaseselectotherdate.edit();
                                HolidayPleaseselectotherdateeditor.putString("HolidayPleaseselectotherdate", jobj1.getString("HolidayPleaseselectotherdate"));
                                HolidayPleaseselectotherdateeditor.commit();

                                SharedPreferences LocationnotDetected = getApplicationContext().getSharedPreferences(Config.SHARED_PREF74, 0);
                                SharedPreferences.Editor LocationnotDetectededitor = LocationnotDetected.edit();
                                LocationnotDetectededitor.putString("LocationnotDetected", jobj1.getString("LocationnotDetected"));
                                LocationnotDetectededitor.commit();

                                SharedPreferences LocationNotFound = getApplicationContext().getSharedPreferences(Config.SHARED_PREF75, 0);
                                SharedPreferences.Editor LocationNotFoundeditor = LocationNotFound.edit();
                                LocationNotFoundeditor.putString("LocationNotFound", jobj1.getString("LocationNotFound"));
                                LocationNotFoundeditor.commit();

                                SharedPreferences GPSisEnabledinyourdevice = getApplicationContext().getSharedPreferences(Config.SHARED_PREF76, 0);
                                SharedPreferences.Editor GPSisEnabledinyourdeviceeditor = GPSisEnabledinyourdevice.edit();
                                GPSisEnabledinyourdeviceeditor.putString("GPSisEnabledinyourdevice", jobj1.getString("GPSisEnabledinyourdevice"));
                                GPSisEnabledinyourdeviceeditor.commit();
                                SharedPreferences SelectYourCountry = getApplicationContext().getSharedPreferences(Config.SHARED_PREF83, 0);
                                SharedPreferences.Editor SelectYourCountryeditor = SelectYourCountry.edit();
                                SelectYourCountryeditor.putString("SelectYourCountry", jobj1.getString("SelectYourCountry"));
                                SelectYourCountryeditor.commit();

                                Log.e(TAG,"strJ  389  "+jobj1.getString("Changelanguage"));

                                SharedPreferences GetStarted = getApplicationContext().getSharedPreferences(Config.SHARED_PREF84, 0);
                                SharedPreferences.Editor GetStartededitor = GetStarted.edit();
                                GetStartededitor.putString("GetStarted", jobj1.getString("GetStarted"));
                                GetStartededitor.commit();

                                SharedPreferences Signup = getApplicationContext().getSharedPreferences(Config.SHARED_PREF85, 0);
                                SharedPreferences.Editor Signupeditor = Signup.edit();
                                Signupeditor.putString("Signup", jobj1.getString("Signup"));
                                Signupeditor.commit();

                                SharedPreferences Login = getApplicationContext().getSharedPreferences(Config.SHARED_PREF86, 0);
                                SharedPreferences.Editor Logineditor = Login.edit();
                                Logineditor.putString("Login", jobj1.getString("Login"));
                                Logineditor.commit();

                                SharedPreferences Register = getApplicationContext().getSharedPreferences(Config.SHARED_PREF87, 0);
                                SharedPreferences.Editor Registereditor = Register.edit();
                                Registereditor.putString("Register", jobj1.getString("Register"));
                                Registereditor.commit();

                                SharedPreferences CustomerName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF88, 0);
                                SharedPreferences.Editor CustomerNameeditor = CustomerName.edit();
                                CustomerNameeditor.putString("CustomerName", jobj1.getString("CustomerName"));
                                CustomerNameeditor.commit();

                                SharedPreferences MobileNo = getApplicationContext().getSharedPreferences(Config.SHARED_PREF89, 0);
                                SharedPreferences.Editor MobileNoeditor = MobileNo.edit();
                                MobileNoeditor.putString("MobileNo", jobj1.getString("MobileNo"));
                                MobileNoeditor.commit();

                                SharedPreferences Password = getApplicationContext().getSharedPreferences(Config.SHARED_PREF90, 0);
                                SharedPreferences.Editor Passwordeditor = Password.edit();
                                Passwordeditor.putString("Password", jobj1.getString("Password"));
                                Passwordeditor.commit();

                                SharedPreferences confirmPassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF91, 0);
                                SharedPreferences.Editor confirmPasswordeditor = confirmPassword.edit();
                                confirmPasswordeditor.putString("confirmPassword", jobj1.getString("confirmPassword"));
                                confirmPasswordeditor.commit();

                                SharedPreferences Emailid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF92, 0);
                                SharedPreferences.Editor Emailideditor = Emailid.edit();
                                Emailideditor.putString("Emailid", jobj1.getString("Emailid"));
                                Emailideditor.commit();


                                SharedPreferences Emailid_Optional = getApplicationContext().getSharedPreferences(Config.SHARED_PREF93, 0);
                                SharedPreferences.Editor Emailid_Optionaleditor = Emailid_Optional.edit();
                                Emailid_Optionaleditor.putString("Emailid_Optional", jobj1.getString("Emailid_Optional"));
                                Emailid_Optionaleditor.commit();

                                SharedPreferences address = getApplicationContext().getSharedPreferences(Config.SHARED_PREF94, 0);
                                SharedPreferences.Editor addresseditor = address.edit();
                                addresseditor.putString("address", jobj1.getString("address"));
                                addresseditor.commit();

                                SharedPreferences Address_Optional = getApplicationContext().getSharedPreferences(Config.SHARED_PREF95, 0);
                                SharedPreferences.Editor Address_Optionaleditor = Address_Optional.edit();
                                Address_Optionaleditor.putString("Address_Optional", jobj1.getString("Address_Optional"));
                                Address_Optionaleditor.commit();

                                SharedPreferences Landmark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF96, 0);
                                SharedPreferences.Editor Landmarkeditor = Landmark.edit();
                                Landmarkeditor.putString("LandmarkS", jobj1.getString("Landmark"));
                                Landmarkeditor.commit();

                                SharedPreferences Landmark_Optional = getApplicationContext().getSharedPreferences(Config.SHARED_PREF97, 0);
                                SharedPreferences.Editor Landmark_Optionaleditor = Landmark_Optional.edit();
                                Landmark_Optionaleditor.putString("Landmark_Optional", jobj1.getString("Landmark_Optional"));
                                Landmark_Optionaleditor.commit();


                                SharedPreferences Optional = getApplicationContext().getSharedPreferences(Config.SHARED_PREF98, 0);
                                SharedPreferences.Editor Optionaleditor = Optional.edit();
                                Optionaleditor.putString("Optional", jobj1.getString("Optional"));
                                Optionaleditor.commit();

                                SharedPreferences SendOTP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF99, 0);
                                SharedPreferences.Editor SendOTPeditor = SendOTP.edit();
                                SendOTPeditor.putString("SendOTP", jobj1.getString("SendOTP"));
                                SendOTPeditor.commit();

                                SharedPreferences PleaseProvideyourname = getApplicationContext().getSharedPreferences(Config.SHARED_PREF100, 0);
                                SharedPreferences.Editor PleaseProvideyournameeditor = PleaseProvideyourname.edit();
                                PleaseProvideyournameeditor.putString("PleaseProvideyourname", jobj1.getString("PleaseProvideyourname"));
                                PleaseProvideyournameeditor.commit();

                                SharedPreferences PleaseProvideyourPhoneNo = getApplicationContext().getSharedPreferences(Config.SHARED_PREF101, 0);
                                SharedPreferences.Editor PleaseProvideyourPhoneNoeditor = PleaseProvideyourPhoneNo.edit();
                                PleaseProvideyourPhoneNoeditor.putString("PleaseProvideyourPhoneNo", jobj1.getString("PleaseProvideyourPhoneNo"));
                                PleaseProvideyourPhoneNoeditor.commit();

                                SharedPreferences EnterYourOTP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF102, 0);
                                SharedPreferences.Editor EnterYourOTPeditor = EnterYourOTP.edit();
                                EnterYourOTPeditor.putString("EnterYourOTP", jobj1.getString("EnterYourOTP"));
                                EnterYourOTPeditor.commit();

                                SharedPreferences EnterOTP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF103, 0);
                                SharedPreferences.Editor EnterOTPeditor = EnterOTP.edit();
                                EnterOTPeditor.putString("EnterOTP", jobj1.getString("EnterOTP"));
                                EnterOTPeditor.commit();

                                SharedPreferences OK = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
                                SharedPreferences.Editor OKeditor = OK.edit();
                                OKeditor.putString("OK", jobj1.getString("OK"));
                                OKeditor.commit();

                                SharedPreferences Cancel = getApplicationContext().getSharedPreferences(Config.SHARED_PREF105, 0);
                                SharedPreferences.Editor Canceleditor = Cancel.edit();
                                Canceleditor.putString("Cancel", jobj1.getString("Cancel"));
                                Canceleditor.commit();

                                SharedPreferences ShowPassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF106, 0);
                                SharedPreferences.Editor ShowPasswordeditor = ShowPassword.edit();
                                ShowPasswordeditor.putString("ShowPassword", jobj1.getString("ShowPassword"));
                                ShowPasswordeditor.commit();

                                SharedPreferences ForgotPassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF107, 0);
                                SharedPreferences.Editor ForgotPasswordeditor = ForgotPassword.edit();
                                ForgotPasswordeditor.putString("ForgotPassword", jobj1.getString("ForgotPassword"));
                                ForgotPasswordeditor.commit();

                                SharedPreferences NotaMember = getApplicationContext().getSharedPreferences(Config.SHARED_PREF108, 0);
                                SharedPreferences.Editor NotaMembereditor = NotaMember.edit();
                                NotaMembereditor.putString("NotaMember", jobj1.getString("NotaMember"));
                                NotaMembereditor.commit();

                                SharedPreferences RegisterNow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF109, 0);
                                SharedPreferences.Editor RegisterNoweditor = RegisterNow.edit();
                                RegisterNoweditor.putString("RegisterNow", jobj1.getString("RegisterNow"));
                                RegisterNoweditor.commit();

                                SharedPreferences PleaseProvideyourMobilenumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF110, 0);
                                SharedPreferences.Editor PleaseProvideyourMobilenumbereditor = PleaseProvideyourMobilenumber.edit();
                                PleaseProvideyourMobilenumbereditor.putString("PleaseProvideyourMobilenumber", jobj1.getString("PleaseProvideyourMobilenumber"));
                                PleaseProvideyourMobilenumbereditor.commit();

                                SharedPreferences PleaseProvideyourPassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF111, 0);
                                SharedPreferences.Editor PleaseProvideyourPasswordeditor = PleaseProvideyourPassword.edit();
                                PleaseProvideyourPasswordeditor.putString("PleaseProvideyourPassword", jobj1.getString("PleaseProvideyourPassword"));
                                PleaseProvideyourPasswordeditor.commit();

                                SharedPreferences LetsGo = getApplicationContext().getSharedPreferences(Config.SHARED_PREF112, 0);
                                SharedPreferences.Editor LetsGoeditor = LetsGo.edit();
                                LetsGoeditor.putString("LetsGo", jobj1.getString("LetsGo"));
                                LetsGoeditor.commit();

                                SharedPreferences sort = getApplicationContext().getSharedPreferences(Config.SHARED_PREF113, 0);
                                SharedPreferences.Editor sorteditor = sort.edit();
                                sorteditor.putString("sort", jobj1.getString("sort"));
                                sorteditor.commit();

                                SharedPreferences filter = getApplicationContext().getSharedPreferences(Config.SHARED_PREF114, 0);
                                SharedPreferences.Editor filtereditor = filter.edit();
                                filtereditor.putString("filter", jobj1.getString("filter"));
                                filtereditor.commit();

                                SharedPreferences MRP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF115, 0);
                                SharedPreferences.Editor MRPditor = MRP.edit();
                                MRPditor.putString("MRP", jobj1.getString("MRP"));
                                MRPditor.commit();

                                SharedPreferences Outofstock = getApplicationContext().getSharedPreferences(Config.SHARED_PREF116, 0);
                                SharedPreferences.Editor Outofstockeditor = Outofstock.edit();
                                Outofstockeditor.putString("Outofstock", jobj1.getString("Outofstock"));
                                Outofstockeditor.commit();

                                SharedPreferences yousaved = getApplicationContext().getSharedPreferences(Config.SHARED_PREF117, 0);
                                SharedPreferences.Editor yousavededitor = yousaved.edit();
                                yousavededitor.putString("yousaved", jobj1.getString("yousaved"));
                                yousavededitor.commit();

                                SharedPreferences SearchForProducts = getApplicationContext().getSharedPreferences(Config.SHARED_PREF118, 0);
                                SharedPreferences.Editor SearchForProductseditor = SearchForProducts.edit();
                                SearchForProductseditor.putString("SearchForProducts", jobj1.getString("SearchForProducts"));
                                SearchForProductseditor.commit();

                                SharedPreferences Store = getApplicationContext().getSharedPreferences(Config.SHARED_PREF119, 0);
                                SharedPreferences.Editor Storeeditor = Store.edit();
                                Storeeditor.putString("Store", jobj1.getString("Store"));
                                Storeeditor.commit();


                                SharedPreferences similarproducts = getApplicationContext().getSharedPreferences(Config.SHARED_PREF120, 0);
                                SharedPreferences.Editor similarproductseditor = similarproducts.edit();
                                similarproductseditor.putString("similarproducts", jobj1.getString("similarproducts"));
                                similarproductseditor.commit();

                                SharedPreferences addtocart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF121, 0);
                                SharedPreferences.Editor addtocarteditor = addtocart.edit();
                                addtocarteditor.putString("addtocart", jobj1.getString("addtocart"));
                                addtocarteditor.commit();

                                SharedPreferences quantity = getApplicationContext().getSharedPreferences(Config.SHARED_PREF122, 0);
                                SharedPreferences.Editor quantityeditor = quantity.edit();
                                quantityeditor.putString("quantity", jobj1.getString("quantity"));
                                quantityeditor.commit();

                                SharedPreferences mycart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF123, 0);
                                SharedPreferences.Editor mycarteditor = mycart.edit();
                                mycarteditor.putString("mycart", jobj1.getString("mycart"));
                                mycarteditor.commit();

                                SharedPreferences continueshopping = getApplicationContext().getSharedPreferences(Config.SHARED_PREF124, 0);
                                SharedPreferences.Editor continueshoppingeditor = continueshopping.edit();
                                continueshoppingeditor.putString("continueshopping", jobj1.getString("continueshopping"));
                                continueshoppingeditor.commit();

                                SharedPreferences yourcartisempty = getApplicationContext().getSharedPreferences(Config.SHARED_PREF125, 0);
                                SharedPreferences.Editor yourcartisemptyeditor = yourcartisempty.edit();
                                yourcartisemptyeditor.putString("yourcartisempty", jobj1.getString("yourcartisempty"));
                                yourcartisemptyeditor.commit();

                                SharedPreferences additemstoitnow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF126, 0);
                                SharedPreferences.Editor additemstoitnoweditor = additemstoitnow.edit();
                                additemstoitnoweditor.putString("additemstoitnow", jobj1.getString("additemstoitnow"));
                                additemstoitnoweditor.commit();

                                SharedPreferences shopnow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF127, 0);
                                SharedPreferences.Editor shopnoweditor = shopnow.edit();
                                shopnoweditor.putString("shopnow", jobj1.getString("shopnow"));
                                shopnoweditor.commit();

                                SharedPreferences clearall = getApplicationContext().getSharedPreferences(Config.SHARED_PREF128, 0);
                                SharedPreferences.Editor clearalleditor = clearall.edit();
                                clearalleditor.putString("clearall", jobj1.getString("clearall"));
                                clearalleditor.commit();

                                SharedPreferences totalitems = getApplicationContext().getSharedPreferences(Config.SHARED_PREF129, 0);
                                SharedPreferences.Editor totalitemseditor = totalitems.edit();
                                totalitemseditor.putString("totalitems", jobj1.getString("totalitems"));
                                totalitemseditor.commit();

                                SharedPreferences qty = getApplicationContext().getSharedPreferences(Config.SHARED_PREF130, 0);
                                SharedPreferences.Editor qtyeditor = qty.edit();
                                qtyeditor.putString("qty", jobj1.getString("qty"));
                                qtyeditor.commit();

                                SharedPreferences totalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF131, 0);
                                SharedPreferences.Editor totalamounteditor = totalamount.edit();
                                totalamounteditor.putString("totalamount", jobj1.getString("totalamount"));
                                totalamounteditor.commit();

                                SharedPreferences proceed = getApplicationContext().getSharedPreferences(Config.SHARED_PREF132, 0);
                                SharedPreferences.Editor proceededitor = proceed.edit();
                                proceededitor.putString("proceed", jobj1.getString("proceed"));
                                proceededitor.commit();

                                SharedPreferences chooseyourdeliveryoptions = getApplicationContext().getSharedPreferences(Config.SHARED_PREF133, 0);
                                SharedPreferences.Editor chooseyourdeliveryoptionseditor = chooseyourdeliveryoptions.edit();
                                chooseyourdeliveryoptionseditor.putString("chooseyourdeliveryoptions", jobj1.getString("chooseyourdeliveryoptions"));
                                chooseyourdeliveryoptionseditor.commit();

                                SharedPreferences storepickup = getApplicationContext().getSharedPreferences(Config.SHARED_PREF134, 0);
                                SharedPreferences.Editor storepickupeditor = storepickup.edit();
                                storepickupeditor.putString("storepickup", jobj1.getString("storepickup"));
                                storepickupeditor.commit();

                                SharedPreferences doordelivery = getApplicationContext().getSharedPreferences(Config.SHARED_PREF135, 0);
                                SharedPreferences.Editor doordeliveryeditor = doordelivery.edit();
                                doordeliveryeditor.putString("doordelivery", jobj1.getString("doordelivery"));
                                doordeliveryeditor.commit();



                                SharedPreferences favoritesitems = getApplicationContext().getSharedPreferences(Config.SHARED_PREF136, 0);
                                SharedPreferences.Editor favoritesitemseditor = favoritesitems.edit();
                                if (jobj1.has("favoritesitems")) {
                                    favoritesitemseditor.putString("favoritesitems", jobj1.getString("favoritesitems"));
                                }else {
                                    favoritesitemseditor.putString("favoritesitems", " ");
                                }
                                favoritesitemseditor.commit();

                                SharedPreferences chooseyourpickupdateandtime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF137, 0);
                                SharedPreferences.Editor chooseyourpickupdateandtimeeditor = chooseyourpickupdateandtime.edit();
                                chooseyourpickupdateandtimeeditor.putString("chooseyourpickupdateandtime", jobj1.getString("chooseyourpickupdateandtime"));
                                chooseyourpickupdateandtimeeditor.commit();

                                SharedPreferences selecttime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF138, 0);
                                SharedPreferences.Editor selecttimeeditor = selecttime.edit();
                                selecttimeeditor.putString("selecttime", jobj1.getString("selecttime"));
                                selecttimeeditor.commit();

                                SharedPreferences pricedetail = getApplicationContext().getSharedPreferences(Config.SHARED_PREF139, 0);
                                SharedPreferences.Editor pricedetaileditor = pricedetail.edit();
                                pricedetaileditor.putString("pricedetail", jobj1.getString("pricedetail"));
                                pricedetaileditor.commit();

                                SharedPreferences item = getApplicationContext().getSharedPreferences(Config.SHARED_PREF140, 0);
                                SharedPreferences.Editor itemeditor = item.edit();
                                itemeditor.putString("item", jobj1.getString("item"));
                                itemeditor.commit();

                                SharedPreferences othercharges = getApplicationContext().getSharedPreferences(Config.SHARED_PREF141, 0);
                                SharedPreferences.Editor otherchargeseditor = othercharges.edit();
                                otherchargeseditor.putString("othercharges", jobj1.getString("othercharges"));
                                otherchargeseditor.commit();

                                SharedPreferences amountpayable = getApplicationContext().getSharedPreferences(Config.SHARED_PREF142, 0);
                                SharedPreferences.Editor amountpayableeditor = amountpayable.edit();
                                amountpayableeditor.putString("amountpayable", jobj1.getString("amountpayable"));
                                amountpayableeditor.commit();

                                SharedPreferences iacceptthetermsAndconditions = getApplicationContext().getSharedPreferences(Config.SHARED_PREF143, 0);
                                SharedPreferences.Editor iacceptthetermsAndconditionseditor = iacceptthetermsAndconditions.edit();
                                iacceptthetermsAndconditionseditor.putString("iacceptthetermsAndconditions", jobj1.getString("iacceptthetermsAndconditions"));
                                iacceptthetermsAndconditionseditor.commit();

                                SharedPreferences thankyouforpurchasingfromus = getApplicationContext().getSharedPreferences(Config.SHARED_PREF144, 0);
                                SharedPreferences.Editor thankyouforpurchasingfromuseditor = thankyouforpurchasingfromus.edit();
                                thankyouforpurchasingfromuseditor.putString("thankyouforpurchasingfromus", jobj1.getString("thankyouforpurchasingfromus"));
                                thankyouforpurchasingfromuseditor.commit();

                                SharedPreferences orderconfirmed = getApplicationContext().getSharedPreferences(Config.SHARED_PREF145, 0);
                                SharedPreferences.Editor orderconfirmededitor = orderconfirmed.edit();
                                orderconfirmededitor.putString("orderconfirmed", jobj1.getString("orderconfirmed"));
                                orderconfirmededitor.commit();

                                SharedPreferences backtohome = getApplicationContext().getSharedPreferences(Config.SHARED_PREF146, 0);
                                SharedPreferences.Editor backtohomeeditor = backtohome.edit();
                                backtohomeeditor.putString("backtohome", jobj1.getString("backtohome"));
                                backtohomeeditor.commit();

                                SharedPreferences checkyourorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF147, 0);
                                SharedPreferences.Editor checkyourordereditor = checkyourorder.edit();
                                checkyourordereditor.putString("checkyourorder", jobj1.getString("checkyourorder"));
                                checkyourordereditor.commit();

                                SharedPreferences all = getApplicationContext().getSharedPreferences(Config.SHARED_PREF148, 0);
                                SharedPreferences.Editor alleditor = all.edit();
                                alleditor.putString("all", jobj1.getString("all"));
                                alleditor.commit();

                                SharedPreferences pending = getApplicationContext().getSharedPreferences(Config.SHARED_PREF149, 0);
                                SharedPreferences.Editor pendingeditor = pending.edit();
                                pendingeditor.putString("pending", jobj1.getString("pending"));
                                pendingeditor.commit();


                                SharedPreferences confirmed = getApplicationContext().getSharedPreferences(Config.SHARED_PREF150, 0);
                                SharedPreferences.Editor confirmededitor = confirmed.edit();
                                confirmededitor.putString("confirmed", jobj1.getString("confirmed"));
                                confirmededitor.commit();

                                SharedPreferences packed = getApplicationContext().getSharedPreferences(Config.SHARED_PREF151, 0);
                                SharedPreferences.Editor packededitor = packed.edit();
                                packededitor.putString("packed", jobj1.getString("packed"));
                                packededitor.commit();

                                SharedPreferences delivered = getApplicationContext().getSharedPreferences(Config.SHARED_PREF152, 0);
                                SharedPreferences.Editor deliverededitor = delivered.edit();
                                deliverededitor.putString("delivered", jobj1.getString("delivered"));
                                deliverededitor.commit();

                                SharedPreferences search = getApplicationContext().getSharedPreferences(Config.SHARED_PREF153, 0);
                                SharedPreferences.Editor searcheditor = search.edit();
                                searcheditor.putString("search", jobj1.getString("search"));
                                searcheditor.commit();

                                SharedPreferences fromdate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF154, 0);
                                SharedPreferences.Editor fromdateeditor = fromdate.edit();
                                fromdateeditor.putString("fromdate", jobj1.getString("fromdate"));
                                fromdateeditor.commit();

                                SharedPreferences todate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF155, 0);
                                SharedPreferences.Editor todateeditor = todate.edit();
                                todateeditor.putString("todate", jobj1.getString("todate"));
                                todateeditor.commit();

                                SharedPreferences ordernumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF156, 0);
                                SharedPreferences.Editor ordernumbereditor = ordernumber.edit();
                                ordernumbereditor.putString("ordernumber", jobj1.getString("ordernumber"));
                                ordernumbereditor.commit();

                                SharedPreferences ordertype = getApplicationContext().getSharedPreferences(Config.SHARED_PREF157, 0);
                                SharedPreferences.Editor ordertypeeditor = ordertype.edit();
                                ordertypeeditor.putString("ordertype", jobj1.getString("ordertype"));
                                ordertypeeditor.commit();

                                SharedPreferences orderedon = getApplicationContext().getSharedPreferences(Config.SHARED_PREF158, 0);
                                SharedPreferences.Editor orderedoneditor = orderedon.edit();
                                orderedoneditor.putString("orderedon", jobj1.getString("orderedon"));
                                orderedoneditor.commit();

                                SharedPreferences closedon = getApplicationContext().getSharedPreferences(Config.SHARED_PREF159, 0);
                                SharedPreferences.Editor closedoneditor = closedon.edit();
                                closedoneditor.putString("closedon", jobj1.getString("closedon"));
                                closedoneditor.commit();

                                SharedPreferences OrderDetails = getApplicationContext().getSharedPreferences(Config.SHARED_PREF160, 0);
                                SharedPreferences.Editor OrderDetailseditor = OrderDetails.edit();
                                OrderDetailseditor.putString("OrderDetails", jobj1.getString("OrderDetails"));
                                OrderDetailseditor.commit();

                                SharedPreferences ItemsOrdered = getApplicationContext().getSharedPreferences(Config.SHARED_PREF161, 0);
                                SharedPreferences.Editor ItemsOrderededitor = ItemsOrdered.edit();
                                ItemsOrderededitor.putString("ItemsOrdered", jobj1.getString("ItemsOrdered"));
                                ItemsOrderededitor.commit();

                                SharedPreferences Subtotal = getApplicationContext().getSharedPreferences(Config.SHARED_PREF162, 0);
                                SharedPreferences.Editor Subtotaleditor = Subtotal.edit();
                                Subtotaleditor.putString("Subtotal", jobj1.getString("Subtotal"));
                                Subtotaleditor.commit();

                                SharedPreferences Items = getApplicationContext().getSharedPreferences(Config.SHARED_PREF163, 0);
                                SharedPreferences.Editor Itemseditor = Items.edit();
                                Itemseditor.putString("Items", jobj1.getString("Items"));
                                Itemseditor.commit();

                                SharedPreferences Discount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF164, 0);
                                SharedPreferences.Editor Discounteditor = Discount.edit();
                                Discounteditor.putString("Discount", jobj1.getString("Discount"));
                                Discounteditor.commit();

                                SharedPreferences memberdiscount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF165, 0);
                                SharedPreferences.Editor memberdiscounteditor = memberdiscount.edit();
                                memberdiscounteditor.putString("memberdiscount", jobj1.getString("memberdiscount"));
                                memberdiscounteditor.commit();

                                SharedPreferences GrandTotal = getApplicationContext().getSharedPreferences(Config.SHARED_PREF166, 0);
                                SharedPreferences.Editor GrandTotaleditor = GrandTotal.edit();
                                GrandTotaleditor.putString("GrandTotal", jobj1.getString("GrandTotal"));
                                GrandTotaleditor.commit();

                                SharedPreferences AmountPaid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF167, 0);
                                SharedPreferences.Editor AmountPaideditor = AmountPaid.edit();
                                AmountPaideditor.putString("AmountPaid", jobj1.getString("AmountPaid"));
                                AmountPaideditor.commit();

                                SharedPreferences Reorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF168, 0);
                                SharedPreferences.Editor Reordereditor = Reorder.edit();
                                Reordereditor.putString("Reorder", jobj1.getString("Reorder"));
                                Reordereditor.commit();

                                SharedPreferences OrderedItems = getApplicationContext().getSharedPreferences(Config.SHARED_PREF169, 0);
                                SharedPreferences.Editor OrderedItemseditor = OrderedItems.edit();
                                OrderedItemseditor.putString("OrderedItems", jobj1.getString("OrderedItems"));
                                OrderedItemseditor.commit();

                                SharedPreferences paidamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF170, 0);
                                SharedPreferences.Editor paidamounteditor = paidamount.edit();
                                paidamounteditor.putString("paidamount", jobj1.getString("paidamount"));
                                paidamounteditor.commit();

                                SharedPreferences Ordereddate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF171, 0);
                                SharedPreferences.Editor Ordereddateeditor = Ordereddate.edit();
                                Ordereddateeditor.putString("Ordereddate", jobj1.getString("Ordereddate"));
                                Ordereddateeditor.commit();

                                SharedPreferences addnewitem = getApplicationContext().getSharedPreferences(Config.SHARED_PREF172, 0);
                                SharedPreferences.Editor addnewitemeditor = addnewitem.edit();
                                addnewitemeditor.putString("addnewitem", jobj1.getString("addnewitem"));
                                addnewitemeditor.commit();

                                SharedPreferences addtoorderlist = getApplicationContext().getSharedPreferences(Config.SHARED_PREF173, 0);
                                SharedPreferences.Editor addtoorderlisteditor = addtoorderlist.edit();
                                addtoorderlisteditor.putString("addtoorderlist", jobj1.getString("addtoorderlist"));
                                addtoorderlisteditor.commit();

                                SharedPreferences sortby = getApplicationContext().getSharedPreferences(Config.SHARED_PREF174, 0);
                                SharedPreferences.Editor sortbyeditor = sortby.edit();
                                sortbyeditor.putString("sortby", jobj1.getString("sortby"));
                                sortbyeditor.commit();

                                SharedPreferences pricelowtohigh = getApplicationContext().getSharedPreferences(Config.SHARED_PREF175, 0);
                                SharedPreferences.Editor pricelowtohigheditor = pricelowtohigh.edit();
                                pricelowtohigheditor.putString("pricelowtohigh", jobj1.getString("pricelowtohigh"));
                                pricelowtohigheditor.commit();

                                SharedPreferences pricehightolow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF176, 0);
                                SharedPreferences.Editor pricehightoloweditor = pricehightolow.edit();
                                pricehightoloweditor.putString("pricehightolow", jobj1.getString("pricehightolow"));
                                pricehightoloweditor.commit();

                                SharedPreferences minprice = getApplicationContext().getSharedPreferences(Config.SHARED_PREF177, 0);
                                SharedPreferences.Editor minpriceeditor = minprice.edit();
                                minpriceeditor.putString("minprice", jobj1.getString("minprice"));
                                minpriceeditor.commit();

                                SharedPreferences maxprice = getApplicationContext().getSharedPreferences(Config.SHARED_PREF178, 0);
                                SharedPreferences.Editor maxpriceeditor = maxprice.edit();
                                maxpriceeditor.putString("maxprice", jobj1.getString("maxprice"));
                                maxpriceeditor.commit();

                                SharedPreferences clear = getApplicationContext().getSharedPreferences(Config.SHARED_PREF179, 0);
                                SharedPreferences.Editor cleareditor = clear.edit();
                                cleareditor.putString("clear", jobj1.getString("clear"));
                                cleareditor.commit();

                                SharedPreferences apply = getApplicationContext().getSharedPreferences(Config.SHARED_PREF180, 0);
                                SharedPreferences.Editor applyeditor = apply.edit();
                                applyeditor.putString("apply", jobj1.getString("apply"));
                                applyeditor.commit();

                                SharedPreferences shopbycategory = getApplicationContext().getSharedPreferences(Config.SHARED_PREF181, 0);
                                SharedPreferences.Editor shopbycategoryeditor = shopbycategory.edit();
                                shopbycategoryeditor.putString("shopbycategory", jobj1.getString("shopbycategory"));
                                shopbycategoryeditor.commit();

                                SharedPreferences gotocart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF182, 0);
                                SharedPreferences.Editor gotocarteditor = gotocart.edit();
                                gotocarteditor.putString("gotocart", jobj1.getString("gotocart"));
                                gotocarteditor.commit();

                                SharedPreferences categoryname = getApplicationContext().getSharedPreferences(Config.SHARED_PREF183, 0);
                                SharedPreferences.Editor categorynameeditor = categoryname.edit();
                                categorynameeditor.putString("categoryname", jobj1.getString("categoryname"));
                                categorynameeditor.commit();

                                SharedPreferences Confirmexit = getApplicationContext().getSharedPreferences(Config.SHARED_PREF184, 0);
                                SharedPreferences.Editor Confirmexiteditor = Confirmexit.edit();
                                Confirmexiteditor.putString("Confirmexit", jobj1.getString("Confirmexit"));
                                Confirmexiteditor.commit();

                                SharedPreferences areyousureyouwanttoexit = getApplicationContext().getSharedPreferences(Config.SHARED_PREF185, 0);
                                SharedPreferences.Editor areyousureyouwanttoexiteditor = areyousureyouwanttoexit.edit();
                                areyousureyouwanttoexiteditor.putString("areyousureyouwanttoexit", jobj1.getString("areyousureyouwanttoexit"));
                                areyousureyouwanttoexiteditor.commit();

                                SharedPreferences yes = getApplicationContext().getSharedPreferences(Config.SHARED_PREF186, 0);
                                SharedPreferences.Editor yeseditor = yes.edit();
                                yeseditor.putString("yes", jobj1.getString("yes"));
                                yeseditor.commit();

                                SharedPreferences no = getApplicationContext().getSharedPreferences(Config.SHARED_PREF187, 0);
                                SharedPreferences.Editor noeditor = no.edit();
                                noeditor.putString("no", jobj1.getString("no"));
                                noeditor.commit();

                                SharedPreferences trackorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF188, 0);
                                SharedPreferences.Editor trackordereditor = trackorder.edit();
                                trackordereditor.putString("trackorder", jobj1.getString("trackorder"));
                                trackordereditor.commit();

                                SharedPreferences favouriteitems = getApplicationContext().getSharedPreferences(Config.SHARED_PREF189, 0);
                                SharedPreferences.Editor favouriteitemseditor = favouriteitems.edit();
                                favouriteitemseditor.putString("favouriteitems", jobj1.getString("favouriteitems"));
                                favouriteitemseditor.commit();

                                SharedPreferences notifications = getApplicationContext().getSharedPreferences(Config.SHARED_PREF190, 0);
                                SharedPreferences.Editor notificationseditor = notifications.edit();
                                notificationseditor.putString("notifications", jobj1.getString("notifications"));
                                notificationseditor.commit();

                                SharedPreferences aboutus = getApplicationContext().getSharedPreferences(Config.SHARED_PREF191, 0);
                                SharedPreferences.Editor aboutuseditor = aboutus.edit();
                                aboutuseditor.putString("aboutus", jobj1.getString("aboutus"));
                                aboutuseditor.commit();

                                SharedPreferences termsandconditions = getApplicationContext().getSharedPreferences(Config.SHARED_PREF192, 0);
                                SharedPreferences.Editor termsandconditionseditor = termsandconditions.edit();
                                termsandconditionseditor.putString("termsandconditions", jobj1.getString("termsandconditions"));
                                termsandconditionseditor.commit();

                                SharedPreferences Privacypolicies = getApplicationContext().getSharedPreferences(Config.SHARED_PREF193, 0);
                                SharedPreferences.Editor Privacypolicieseditor = Privacypolicies.edit();
                                Privacypolicieseditor.putString("Privacypolicies", jobj1.getString("Privacypolicies"));
                                Privacypolicieseditor.commit();

                                SharedPreferences suggetions = getApplicationContext().getSharedPreferences(Config.SHARED_PREF194, 0);
                                SharedPreferences.Editor suggetionseditor = suggetions.edit();
                                suggetionseditor.putString("suggetions", jobj1.getString("suggetions"));
                                suggetionseditor.commit();

                                SharedPreferences faq = getApplicationContext().getSharedPreferences(Config.SHARED_PREF195, 0);
                                SharedPreferences.Editor faqeditor = faq.edit();
                                faqeditor.putString("faq", jobj1.getString("faq"));
                                faqeditor.commit();


                                SharedPreferences rateus = getApplicationContext().getSharedPreferences(Config.SHARED_PREF196, 0);
                                SharedPreferences.Editor rateuseditor = rateus.edit();
                                rateuseditor.putString("rateus", jobj1.getString("rateus"));
                                rateuseditor.commit();


                                SharedPreferences share = getApplicationContext().getSharedPreferences(Config.SHARED_PREF197, 0);
                                SharedPreferences.Editor shareeditor = share.edit();
                                shareeditor.putString("share", jobj1.getString("share"));
                                shareeditor.commit();

//                                SharedPreferences ShortName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF198, 0);
//                                SharedPreferences.Editor ShortNameeditor = ShortName.edit();
//                                ShortNameeditor.putString("ShortName", jobj1.getString("ShortName"));
//                                ShortNameeditor.commit();

                                SharedPreferences logout = getApplicationContext().getSharedPreferences(Config.SHARED_PREF199, 0);
                                SharedPreferences.Editor logouteditor = logout.edit();
                                logouteditor.putString("logout", jobj1.getString("logout"));
                                logouteditor.commit();

                                SharedPreferences welcome = getApplicationContext().getSharedPreferences(Config.SHARED_PREF200, 0);
                                SharedPreferences.Editor welcomeeditor = welcome.edit();
                                welcomeeditor.putString("welcome", jobj1.getString("welcome"));
                                welcomeeditor.commit();

                                SharedPreferences Deliveryaddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF201, 0);
                                SharedPreferences.Editor Deliveryaddresseditor = Deliveryaddress.edit();
                                Deliveryaddresseditor.putString("Deliveryaddress", jobj1.getString("Deliveryaddress"));
                                Deliveryaddresseditor.commit();



                                SharedPreferences emptyshippingaddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF202, 0);
                                SharedPreferences.Editor emptyshippingaddresseditor = emptyshippingaddress.edit();
                                emptyshippingaddresseditor.putString("emptyshippingaddress", jobj1.getString("emptyshippingaddress"));
                                emptyshippingaddresseditor.commit();

                                SharedPreferences addaddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF203, 0);
                                SharedPreferences.Editor addaddresseditor = addaddress.edit();
                                addaddresseditor.putString("addaddress", jobj1.getString("addaddress"));
                                addaddresseditor.commit();

                                SharedPreferences editadress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF204, 0);
                                SharedPreferences.Editor editadresseditor = editadress.edit();
                                editadresseditor.putString("editadress", jobj1.getString("editadress"));
                                editadresseditor.commit();

                                SharedPreferences choosedeliverydateandtime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF205, 0);
                                SharedPreferences.Editor choosedeliverydateandtimeeditor = choosedeliverydateandtime.edit();
                                choosedeliverydateandtimeeditor.putString("choosedeliverydateandtime", jobj1.getString("choosedeliverydateandtime"));
                                choosedeliverydateandtimeeditor.commit();

                                SharedPreferences selectdate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF206, 0);
                                SharedPreferences.Editor selectdateeditor = selectdate.edit();
                                selectdateeditor.putString("selectdate", jobj1.getString("selectdate"));
                                selectdateeditor.commit();

                                SharedPreferences starttime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF207, 0);
                                SharedPreferences.Editor starttimeeditor = starttime.edit();
                                starttimeeditor.putString("starttime", jobj1.getString("starttime"));
                                starttimeeditor.commit();

                                SharedPreferences endtime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF208, 0);
                                SharedPreferences.Editor endtimeeditor = endtime.edit();
                                endtimeeditor.putString("endtime", jobj1.getString("endtime"));
                                endtimeeditor.commit();

                                SharedPreferences remarks = getApplicationContext().getSharedPreferences(Config.SHARED_PREF209, 0);
                                SharedPreferences.Editor remarkseditor = remarks.edit();
                                remarkseditor.putString("remarks", jobj1.getString("remarks"));
                                remarkseditor.commit();

                                SharedPreferences ordersummary = getApplicationContext().getSharedPreferences(Config.SHARED_PREF210, 0);
                                SharedPreferences.Editor ordersummaryeditor = ordersummary.edit();
                                ordersummaryeditor.putString("ordersummary", jobj1.getString("ordersummary"));
                                ordersummaryeditor.commit();

                                SharedPreferences DeliveryCharges = getApplicationContext().getSharedPreferences(Config.SHARED_PREF211, 0);
                                SharedPreferences.Editor DeliveryChargeseditor = DeliveryCharges.edit();
                                DeliveryChargeseditor.putString("DeliveryCharges", jobj1.getString("DeliveryCharges"));
                                DeliveryChargeseditor.commit();

                                SharedPreferences youhavesaved = getApplicationContext().getSharedPreferences(Config.SHARED_PREF212, 0);
                                SharedPreferences.Editor youhavesavededitor = youhavesaved.edit();
                                youhavesavededitor.putString("youhavesaved", jobj1.getString("youhavesaved"));
                                youhavesavededitor.commit();

                                SharedPreferences paymenttype = getApplicationContext().getSharedPreferences(Config.SHARED_PREF213, 0);
                                SharedPreferences.Editor paymenttypeeditor = paymenttype.edit();
                                paymenttypeeditor.putString("paymenttype", jobj1.getString("paymenttype"));
                                paymenttypeeditor.commit();

                                SharedPreferences expressdelivery = getApplicationContext().getSharedPreferences(Config.SHARED_PREF214, 0);
                                SharedPreferences.Editor expressdeliveryeditor = expressdelivery.edit();
                                expressdeliveryeditor.putString("expressdelivery", jobj1.getString("expressdelivery"));
                                expressdeliveryeditor.commit();

                                SharedPreferences extraamountfordelivery = getApplicationContext().getSharedPreferences(Config.SHARED_PREF215, 0);
                                SharedPreferences.Editor extraamountfordeliveryeditor = extraamountfordelivery.edit();
                                extraamountfordeliveryeditor.putString("extraamountfordelivery", jobj1.getString("extraamountfordelivery"));
                                extraamountfordeliveryeditor.commit();

                                SharedPreferences iaccepttermsAndconditions = getApplicationContext().getSharedPreferences(Config.SHARED_PREF216, 0);
                                SharedPreferences.Editor iaccepttermsAndconditionseditor = iaccepttermsAndconditions.edit();
                                iaccepttermsAndconditionseditor.putString("iaccepttermsAndconditions", jobj1.getString("iaccepttermsAndconditions"));
                                iaccepttermsAndconditionseditor.commit();

                                SharedPreferences placeorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF217, 0);
                                SharedPreferences.Editor placeordereditor = placeorder.edit();
                                placeordereditor.putString("placeorder", jobj1.getString("placeorder"));
                                placeordereditor.commit();

                                SharedPreferences markallasread = getApplicationContext().getSharedPreferences(Config.SHARED_PREF218, 0);
                                SharedPreferences.Editor markallasreadeditor = markallasread.edit();
                                markallasreadeditor.putString("markallasread", jobj1.getString("markallasread"));
                                markallasreadeditor.commit();

                                SharedPreferences orderno = getApplicationContext().getSharedPreferences(Config.SHARED_PREF219, 0);
                                SharedPreferences.Editor ordernoeditor = orderno.edit();
                                ordernoeditor.putString("orderno", jobj1.getString("orderno"));
                                ordernoeditor.commit();

                                SharedPreferences notificationdetails = getApplicationContext().getSharedPreferences(Config.SHARED_PREF220, 0);
                                SharedPreferences.Editor notificationdetailseditor = notificationdetails.edit();
                                notificationdetailseditor.putString("notificationdetails", jobj1.getString("notificationdetails"));
                                notificationdetailseditor.commit();

                                SharedPreferences myprofile = getApplicationContext().getSharedPreferences(Config.SHARED_PREF221, 0);
                                SharedPreferences.Editor myprofileeditor = myprofile.edit();
                                myprofileeditor.putString("myprofile", jobj1.getString("myprofile"));
                                myprofileeditor.commit();

                                SharedPreferences changepassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF222, 0);
                                SharedPreferences.Editor changepasswordeditor = changepassword.edit();
                                changepasswordeditor.putString("changepassword", jobj1.getString("changepassword"));
                                changepasswordeditor.commit();

                                SharedPreferences area = getApplicationContext().getSharedPreferences(Config.SHARED_PREF223, 0);
                                SharedPreferences.Editor areaeditor = area.edit();
                                areaeditor.putString("area", jobj1.getString("area"));
                                areaeditor.commit();

                                SharedPreferences selectarea = getApplicationContext().getSharedPreferences(Config.SHARED_PREF224, 0);
                                SharedPreferences.Editor selectareaeditor = selectarea.edit();
                                selectareaeditor.putString("selectarea", jobj1.getString("selectarea"));
                                selectareaeditor.commit();

                                SharedPreferences oldpassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF225, 0);
                                SharedPreferences.Editor oldpasswordeditor = oldpassword.edit();
                                oldpasswordeditor.putString("oldpassword", jobj1.getString("oldpassword"));
                                oldpasswordeditor.commit();

                                SharedPreferences newpassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF226, 0);
                                SharedPreferences.Editor newpasswordeditor = newpassword.edit();
                                newpasswordeditor.putString("newpassword", jobj1.getString("newpassword"));
                                newpasswordeditor.commit();

                                SharedPreferences save = getApplicationContext().getSharedPreferences(Config.SHARED_PREF227, 0);
                                SharedPreferences.Editor saveeditor = save.edit();
                                saveeditor.putString("save", jobj1.getString("save"));
                                saveeditor.commit();



                                SharedPreferences versioncode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF228, 0);
                                SharedPreferences.Editor versioncodeeditor = versioncode.edit();
                                versioncodeeditor.putString("versioncode", jobj1.getString("versioncode"));
                                versioncodeeditor.commit();

                                SharedPreferences technologypartner = getApplicationContext().getSharedPreferences(Config.SHARED_PREF229, 0);
                                SharedPreferences.Editor technologypartnereditor = technologypartner.edit();
                                technologypartnereditor.putString("technologypartner", jobj1.getString("technologypartner"));
                                technologypartnereditor.commit();

                                SharedPreferences contactus = getApplicationContext().getSharedPreferences(Config.SHARED_PREF230, 0);
                                SharedPreferences.Editor contactuseditor = contactus.edit();
                                contactuseditor.putString("contactus", jobj1.getString("contactus"));
                                contactuseditor.commit();

                                SharedPreferences privacypolicy = getApplicationContext().getSharedPreferences(Config.SHARED_PREF231, 0);
                                SharedPreferences.Editor privacypolicyeditor = privacypolicy.edit();
                                privacypolicyeditor.putString("privacypolicy", jobj1.getString("privacypolicy"));
                                privacypolicyeditor.commit();

                                SharedPreferences feedback = getApplicationContext().getSharedPreferences(Config.SHARED_PREF232, 0);
                                SharedPreferences.Editor feedbackeditor = feedback.edit();
                                feedbackeditor.putString("feedback", jobj1.getString("feedback"));
                                feedbackeditor.commit();

                                SharedPreferences reportanerror = getApplicationContext().getSharedPreferences(Config.SHARED_PREF233, 0);
                                SharedPreferences.Editor reportanerroreditor = reportanerror.edit();
                                reportanerroreditor.putString("reportanerror", jobj1.getString("reportanerror"));
                                reportanerroreditor.commit();

                                SharedPreferences giveasuggestion = getApplicationContext().getSharedPreferences(Config.SHARED_PREF234, 0);
                                SharedPreferences.Editor giveasuggestioneditor = giveasuggestion.edit();
                                giveasuggestioneditor.putString("giveasuggestion", jobj1.getString("giveasuggestion"));
                                giveasuggestioneditor.commit();

                                SharedPreferences anythingelse = getApplicationContext().getSharedPreferences(Config.SHARED_PREF235, 0);
                                SharedPreferences.Editor anythingelseeditor = anythingelse.edit();
                                anythingelseeditor.putString("anythingelse", jobj1.getString("anythingelse"));
                                anythingelseeditor.commit();

                                SharedPreferences submit = getApplicationContext().getSharedPreferences(Config.SHARED_PREF236, 0);
                                SharedPreferences.Editor submiteditor = submit.edit();
                                submiteditor.putString("submit", jobj1.getString("submit"));
                                submiteditor.commit();

                                SharedPreferences frequentlyaskedquestions = getApplicationContext().getSharedPreferences(Config.SHARED_PREF237, 0);
                                SharedPreferences.Editor frequentlyaskedquestionseditor = frequentlyaskedquestions.edit();
                                frequentlyaskedquestionseditor.putString("frequentlyaskedquestions", jobj1.getString("frequentlyaskedquestions"));
                                frequentlyaskedquestionseditor.commit();

                                SharedPreferences wewantyourfeedback = getApplicationContext().getSharedPreferences(Config.SHARED_PREF238, 0);
                                SharedPreferences.Editor wewantyourfeedbackeditor = wewantyourfeedback.edit();
                                wewantyourfeedbackeditor.putString("wewantyourfeedback", jobj1.getString("wewantyourfeedback"));
                                wewantyourfeedbackeditor.commit();

                                SharedPreferences lovethisapprateus = getApplicationContext().getSharedPreferences(Config.SHARED_PREF239, 0);
                                SharedPreferences.Editor lovethisapprateuseditor = lovethisapprateus.edit();
                                lovethisapprateuseditor.putString("lovethisapprateus", jobj1.getString("lovethisapprateus"));
                                lovethisapprateuseditor.commit();

                                SharedPreferences ratenow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF240, 0);
                                SharedPreferences.Editor ratenoweditor = ratenow.edit();
                                ratenoweditor.putString("ratenow", jobj1.getString("ratenow"));
                                ratenoweditor.commit();

                                SharedPreferences Logoutaccount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF241, 0);
                                SharedPreferences.Editor Logoutaccounteditor = Logoutaccount.edit();
                                Logoutaccounteditor.putString("Logoutaccount", jobj1.getString("Logoutaccount"));
                                Logoutaccounteditor.commit();

                                SharedPreferences areyousureyouwanttologout = getApplicationContext().getSharedPreferences(Config.SHARED_PREF242, 0);
                                SharedPreferences.Editor areyousureyouwanttologouteditor = areyousureyouwanttologout.edit();
                                areyousureyouwanttologouteditor.putString("areyousureyouwanttologout", jobj1.getString("areyousureyouwanttologout"));
                                areyousureyouwanttologouteditor.commit();

                                SharedPreferences Changelanguage = getApplicationContext().getSharedPreferences(Config.SHARED_PREF243, 0);
                                SharedPreferences.Editor Changelanguageeditor = Changelanguage.edit();
                                Changelanguageeditor.putString("Changelanguage", jobj1.getString("Changelanguage"));
                                Changelanguageeditor.commit();

                                SharedPreferences favoriteStore = getApplicationContext().getSharedPreferences(Config.SHARED_PREF244, 0);
                                SharedPreferences.Editor favoriteStoreeditor = favoriteStore.edit();
                                favoriteStoreeditor.putString("favoriteStore", jobj1.getString("favoriteStore"));
                                favoriteStoreeditor.commit();

                                ////////
                                SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF245, 0);
                                SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                StoreNameeditor.putString("Store_Name", jobj1.getString("Store_Name"));
                                StoreNameeditor.commit();

                                SharedPreferences AreaName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF246, 0);
                                SharedPreferences.Editor AreaNameeditor = AreaName.edit();
                                AreaNameeditor.putString("AreaName", jobj1.getString("AreaName"));
                                AreaNameeditor.commit();

                                SharedPreferences Pincode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF247, 0);
                                SharedPreferences.Editor Pincodeeditor = Pincode.edit();
                                Pincodeeditor.putString("Pincode", jobj1.getString("Pincode"));
                                Pincodeeditor.commit();

                                ////////
                                SharedPreferences EnterStore = getApplicationContext().getSharedPreferences(Config.SHARED_PREF248, 0);
                                SharedPreferences.Editor EnterStoreeditor = EnterStore.edit();
                                EnterStoreeditor.putString("EnterStore", jobj1.getString("EnterStore"));
                                EnterStoreeditor.commit();

                                SharedPreferences EnterArea = getApplicationContext().getSharedPreferences(Config.SHARED_PREF249, 0);
                                SharedPreferences.Editor EnterAreaeditor = EnterArea.edit();
                                EnterAreaeditor.putString("EnterArea", jobj1.getString("EnterArea"));
                                EnterAreaeditor.commit();

                                SharedPreferences EnterPincode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF250, 0);
                                SharedPreferences.Editor EnterPincodeeditor = EnterPincode.edit();
                                EnterPincodeeditor.putString("EnterPincode", jobj1.getString("EnterPincode"));
                                EnterPincodeeditor.commit();

                                SharedPreferences StoreLocations = getApplicationContext().getSharedPreferences(Config.SHARED_PREF251, 0);
                                SharedPreferences.Editor StoreLocationseditor = StoreLocations.edit();
                                StoreLocationseditor.putString("StoreLocations", jobj1.getString("StoreLocations"));
                                StoreLocationseditor.commit();


                                SharedPreferences Thereisnonotificationstoshow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF252, 0);
                                SharedPreferences.Editor Thereisnonotificationstoshoweditor = Thereisnonotificationstoshow.edit();
                                Thereisnonotificationstoshoweditor.putString("Thereisnonotificationstoshow", jobj1.getString("Nonotifications"));
                                Thereisnonotificationstoshoweditor.commit();

                                SharedPreferences Homedeliveryoptionwillstartshortly = getApplicationContext().getSharedPreferences(Config.SHARED_PREF253, 0);
                                SharedPreferences.Editor Homedeliveryoptionwillstartshortlyeditor = Homedeliveryoptionwillstartshortly.edit();
                                Homedeliveryoptionwillstartshortlyeditor.putString("Homedeliveryoptionwillstartshortly", jobj1.getString("Homedeliveryoptionwillstartshortly"));
                                Homedeliveryoptionwillstartshortlyeditor.commit();

                                SharedPreferences MinimumamountHomedelivery = getApplicationContext().getSharedPreferences(Config.SHARED_PREF254, 0);
                                SharedPreferences.Editor MinimumamountHomedeliveryeditor = MinimumamountHomedelivery.edit();
                                MinimumamountHomedeliveryeditor.putString("MinimumamountHomedelivery", jobj1.getString("MinimumamountHomedelivery"));
                                MinimumamountHomedeliveryeditor.commit();

                                SharedPreferences Homeshortlypleasedocounterpickupdeliveryoption = getApplicationContext().getSharedPreferences(Config.SHARED_PREF255, 0);
                                SharedPreferences.Editor Homeshortlypleasedocounterpickupdeliveryoptioneditor = Homeshortlypleasedocounterpickupdeliveryoption.edit();
                                Homeshortlypleasedocounterpickupdeliveryoptioneditor.putString("Homeshortlypleasedocounterpickupdeliveryoption", jobj1.getString("Homeshortlypleasedocounterpickupdeliveryoption"));
                                Homeshortlypleasedocounterpickupdeliveryoptioneditor.commit();

                                SharedPreferences Pleaseupdateallitemquantity = getApplicationContext().getSharedPreferences(Config.SHARED_PREF256, 0);
                                SharedPreferences.Editor Pleaseupdateallitemquantityeditor = Pleaseupdateallitemquantity.edit();
                                Pleaseupdateallitemquantityeditor.putString("Pleaseupdateallitemquantity", jobj1.getString("Pleaseupdateallitemquantity"));
                                Pleaseupdateallitemquantityeditor.commit();




//                                SharedPreferences SearchforProducts = getApplicationContext().getSharedPreferences(Config.SHARED_PREF258, 0);
//                                SharedPreferences.Editor SearchforProductseditor = SearchforProducts.edit();
//                                SearchforProductseditor.putString("SearchforProducts", jobj1.getString("SearchforProducts"));
//                                SearchforProductseditor.commit();
//
//                                SharedPreferences SearchforProducts = getApplicationContext().getSharedPreferences(Config.SHARED_PREF259, 0);
//                                SharedPreferences.Editor SearchforProductseditor = SearchforProducts.edit();
//                                SearchforProductseditor.putString("SearchforProducts", jobj1.getString("SearchforProducts"));
//                                SearchforProductseditor.commit();

                                SharedPreferences ChangeAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF260, 0);
                                SharedPreferences.Editor ChangeAddresseditor = ChangeAddress.edit();
                                ChangeAddresseditor.putString("ChangeAddress", jobj1.getString("ChangeAddress"));
                                ChangeAddresseditor.commit();

                                SharedPreferences usernameandpasswordareverified = getApplicationContext().getSharedPreferences(Config.SHARED_PREF261, 0);
                                SharedPreferences.Editor usernameandpasswordareverifiededitor = usernameandpasswordareverified.edit();
                                usernameandpasswordareverifiededitor.putString("usernameandpasswordareverified", jobj1.getString("usernameandpasswordareverified"));
                                usernameandpasswordareverifiededitor.commit();

                                SharedPreferences ThereissometechnicalissuesPleaseuseanotherpaymentoptions = getApplicationContext().getSharedPreferences(Config.SHARED_PREF262, 0);
                                SharedPreferences.Editor ThereissometechnicalissuesPleaseuseanotherpaymentoptionseditor = ThereissometechnicalissuesPleaseuseanotherpaymentoptions.edit();
                                ThereissometechnicalissuesPleaseuseanotherpaymentoptionseditor.putString("ThereissometechnicalissuesPleaseuseanotherpaymentoptions", jobj1.getString("ThereissometechnicalissuesPleaseuseanotherpaymentoptions"));
                                ThereissometechnicalissuesPleaseuseanotherpaymentoptionseditor.commit();

                                SharedPreferences PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdevice = getApplicationContext().getSharedPreferences(Config.SHARED_PREF263, 0);
                                SharedPreferences.Editor PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdeviceeditor = PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdevice.edit();
                                PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdeviceeditor.putString("PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdevice", jobj1.getString("PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdevice"));
                                PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdeviceeditor.commit();

                                SharedPreferences Pleaseselectanypaymentoption = getApplicationContext().getSharedPreferences(Config.SHARED_PREF264, 0);
                                SharedPreferences.Editor Pleaseselectanypaymentoptioneditor = Pleaseselectanypaymentoption.edit();
                                Pleaseselectanypaymentoptioneditor.putString("Pleaseselectanypaymentoption", jobj1.getString("Pleaseselectanypaymentoption"));
                                Pleaseselectanypaymentoptioneditor.commit();

                                SharedPreferences PleaseselectdeliveryStarttimeEndtime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF265, 0);
                                SharedPreferences.Editor PleaseselectdeliveryStarttimeEndtimeeditor = PleaseselectdeliveryStarttimeEndtime.edit();
                                PleaseselectdeliveryStarttimeEndtimeeditor.putString("PleaseselectdeliveryStarttimeEndtime", jobj1.getString("PleaseselectdeliveryStarttimeEndtime"));
                                PleaseselectdeliveryStarttimeEndtimeeditor.commit();

                                SharedPreferences Pleaseselectdeliveryendtime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF266, 0);
                                SharedPreferences.Editor Pleaseselectdeliveryendtimeeditor = Pleaseselectdeliveryendtime.edit();
                                Pleaseselectdeliveryendtimeeditor.putString("Pleaseselectdeliveryendtime", jobj1.getString("Pleaseselectdeliveryendtime"));
                                Pleaseselectdeliveryendtimeeditor.commit();

                                SharedPreferences Pleaseselectdeliverystarttime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF267, 0);
                                SharedPreferences.Editor Pleaseselectdeliverystarttimeeditor = Pleaseselectdeliverystarttime.edit();
                                Pleaseselectdeliverystarttimeeditor.putString("Pleaseselectdeliverystarttime", jobj1.getString("Pleaseselectdeliverystarttime"));
                                Pleaseselectdeliverystarttimeeditor.commit();

                                SharedPreferences Androidversionneedsanupgrade = getApplicationContext().getSharedPreferences(Config.SHARED_PREF268, 0);
                                SharedPreferences.Editor Androidversionneedsanupgradeeditor = Androidversionneedsanupgrade.edit();
                                Androidversionneedsanupgradeeditor.putString("Androidversionneedsanupgrade", jobj1.getString("Androidversionneedsanupgrade"));
                                Androidversionneedsanupgradeeditor.commit();

                                SharedPreferences Thisappisntcompatiblewithyourdevice = getApplicationContext().getSharedPreferences(Config.SHARED_PREF269, 0);
                                SharedPreferences.Editor Thisappisntcompatiblewithyourdeviceeditor = Thisappisntcompatiblewithyourdevice.edit();
                                Thisappisntcompatiblewithyourdeviceeditor.putString("Thisappisntcompatiblewithyourdevice", jobj1.getString("Thisappisntcompatiblewithyourdevice"));
                                Thisappisntcompatiblewithyourdeviceeditor.commit();

                                SharedPreferences Description = getApplicationContext().getSharedPreferences(Config.SHARED_PREF270, 0);
                                SharedPreferences.Editor Descriptioneditor = Description.edit();
                                Descriptioneditor.putString("Description", jobj1.getString("Description"));
                                Descriptioneditor.commit();

                                SharedPreferences AddressList = getApplicationContext().getSharedPreferences(Config.SHARED_PREF271, 0);
                                SharedPreferences.Editor AddressListeditor = AddressList.edit();
                                AddressListeditor.putString("AddressList", jobj1.getString("AddressList"));
                                AddressListeditor.commit();

                                SharedPreferences Thereisnoaddresslisttoshow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF272, 0);
                                SharedPreferences.Editor Thereisnoaddresslisttoshoweditor = Thereisnoaddresslisttoshow.edit();
                                Thereisnoaddresslisttoshoweditor.putString("Thereisnoaddresslisttoshow", jobj1.getString("Thereisnoaddresslisttoshow"));
                                Thereisnoaddresslisttoshoweditor.commit();

                                SharedPreferences Getlocation = getApplicationContext().getSharedPreferences(Config.SHARED_PREF273, 0);
                                SharedPreferences.Editor Getlocationeditor = Getlocation.edit();
                                Getlocationeditor.putString("Getlocation", jobj1.getString("Getlocation"));
                                Getlocationeditor.commit();

                                SharedPreferences PermanentAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF274, 0);
                                SharedPreferences.Editor PermanentAddresseditor = PermanentAddress.edit();
                                PermanentAddresseditor.putString("PermanentAddress", jobj1.getString("PermanentAddress"));
                                PermanentAddresseditor.commit();

                                SharedPreferences ShippingAddresslist = getApplicationContext().getSharedPreferences(Config.SHARED_PREF275, 0);
                                SharedPreferences.Editor ShippingAddresslisteditor = ShippingAddresslist.edit();
                                ShippingAddresslisteditor.putString("ShippingAddresslist", jobj1.getString("ShippingAddresslist"));
                                ShippingAddresslisteditor.commit();

                                SharedPreferences PhoneNo = getApplicationContext().getSharedPreferences(Config.SHARED_PREF276, 0);
                                SharedPreferences.Editor PhoneNoeditor = PhoneNo.edit();
                                PhoneNoeditor.putString("PhoneNo", jobj1.getString("PhoneNo"));
                                PhoneNoeditor.commit();

                                SharedPreferences MinDeliveryAmt = getApplicationContext().getSharedPreferences(Config.SHARED_PREF277, 0);
                                SharedPreferences.Editor MinDeliveryAmteditor = MinDeliveryAmt.edit();
                                MinDeliveryAmteditor.putString("MinDeliveryAmt", jobj1.getString("MinDeliveryAmt"));
                                MinDeliveryAmteditor.commit();

                                SharedPreferences Storeaddedtothefavourites = getApplicationContext().getSharedPreferences(Config.SHARED_PREF278, 0);
                                SharedPreferences.Editor Storeaddedtothefavouriteseditor = Storeaddedtothefavourites.edit();
                                Storeaddedtothefavouriteseditor.putString("Storeaddedtothefavourites", jobj1.getString("Storeaddedtothefavourites"));
                                Storeaddedtothefavouriteseditor.commit();

                                SharedPreferences Storeremovedfromthefavourites = getApplicationContext().getSharedPreferences(Config.SHARED_PREF279, 0);
                                SharedPreferences.Editor Storeremovedfromthefavouriteseditor = Storeremovedfromthefavourites.edit();
                                Storeremovedfromthefavouriteseditor.putString("Storeremovedfromthefavourites", jobj1.getString("Storeremovedfromthefavourites"));
                                Storeremovedfromthefavouriteseditor.commit();

                                SharedPreferences SomethingwentwrongTryagainlater = getApplicationContext().getSharedPreferences(Config.SHARED_PREF280, 0);
                                SharedPreferences.Editor SomethingwentwrongTryagainlatereditor = SomethingwentwrongTryagainlater.edit();
                                SomethingwentwrongTryagainlatereditor.putString("SomethingwentwrongTryagainlater", jobj1.getString("SomethingwentwrongTryagainlater"));
                                SomethingwentwrongTryagainlatereditor.commit();

                                SharedPreferences Nodatafound = getApplicationContext().getSharedPreferences(Config.SHARED_PREF281, 0);
                                SharedPreferences.Editor Nodatafoundeditor = Nodatafound.edit();
                                Nodatafoundeditor.putString("Nodatafound", jobj1.getString("Nodatafound"));
                                Nodatafoundeditor.commit();

                                SharedPreferences Nointernetconnection = getApplicationContext().getSharedPreferences(Config.SHARED_PREF282, 0);
                                SharedPreferences.Editor Nointernetconnectioneditor = Nointernetconnection.edit();
                                Nointernetconnectioneditor.putString("Nointernetconnection", jobj1.getString("Nointernetconnection"));
                                Nointernetconnectioneditor.commit();

                                SharedPreferences AddToOrder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF283, 0);
                                SharedPreferences.Editor AddToOrdereditor = AddToOrder.edit();
                                AddToOrdereditor.putString("AddToOrder", jobj1.getString("AddToOrder"));
                                AddToOrdereditor.commit();

                                SharedPreferences ItemaddedtotheReorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF284, 0);
                                SharedPreferences.Editor ItemaddedtotheReordereditor = ItemaddedtotheReorder.edit();
                                ItemaddedtotheReordereditor.putString("ItemaddedtotheReorder", jobj1.getString("ItemaddedtotheReorder"));
                                ItemaddedtotheReordereditor.commit();




                                SharedPreferences Youarenotinsideornearthestoresoyoucantselectinshoppleaseselectoutshopforpurchase = getApplicationContext().getSharedPreferences(Config.SHARED_PREF285, 0);
                                SharedPreferences.Editor Youarenotinsideornearthestoresoyoucantselectinshoppleaseselectoutshopforpurchaseeditor = Youarenotinsideornearthestoresoyoucantselectinshoppleaseselectoutshopforpurchase.edit();
                                Youarenotinsideornearthestoresoyoucantselectinshoppleaseselectoutshopforpurchaseeditor.putString("Youarenotinsideornearthestoresoyoucantselectinshoppleaseselectoutshopforpurchase", jobj1.getString("Youarenotinsideornearthestoresoyoucantselectinshoppleaseselectoutshopforpurchase"));
                                Youarenotinsideornearthestoresoyoucantselectinshoppleaseselectoutshopforpurchaseeditor.commit();

                                SharedPreferences Storelocationnotfound = getApplicationContext().getSharedPreferences(Config.SHARED_PREF286, 0);
                                SharedPreferences.Editor Storelocationnotfoundeditor = Storelocationnotfound.edit();
                                Storelocationnotfoundeditor.putString("Storelocationnotfound", jobj1.getString("Storelocationnotfound"));
                                Storelocationnotfoundeditor.commit();

                                SharedPreferences LocationsettingsareinadequateandcannotbefixedhereFixinSettings = getApplicationContext().getSharedPreferences(Config.SHARED_PREF287, 0);
                                SharedPreferences.Editor LocationsettingsareinadequateandcannotbefixedhereFixinSettingseditor = LocationsettingsareinadequateandcannotbefixedhereFixinSettings.edit();
                                LocationsettingsareinadequateandcannotbefixedhereFixinSettingseditor.putString("LocationsettingsareinadequateandcannotbefixedhereFixinSettings", jobj1.getString("LocationsettingsareinadequateandcannotbefixedhereFixinSettings"));
                                LocationsettingsareinadequateandcannotbefixedhereFixinSettingseditor.commit();


                                SharedPreferences InviteYou = getApplicationContext().getSharedPreferences(Config.SHARED_PREF288, 0);
                                SharedPreferences.Editor InviteYoueditor = InviteYou.edit();
                                InviteYoueditor.putString("InviteYou", jobj1.getString("InviteYou"));
                                InviteYoueditor.commit();



                                SharedPreferences ForAndroidUsers = getApplicationContext().getSharedPreferences(Config.SHARED_PREF289, 0);
                                SharedPreferences.Editor ForAndroidUserseditor = ForAndroidUsers.edit();
                                ForAndroidUserseditor.putString("ForAndroidUsers", jobj1.getString("ForAndroidUsers"));
                                ForAndroidUserseditor.commit();

                                SharedPreferences Youwillgetaccessonlyifyouareinsideornearthestore = getApplicationContext().getSharedPreferences(Config.SHARED_PREF290, 0);
                                SharedPreferences.Editor Youwillgetaccessonlyifyouareinsideornearthestoreeditor = Youwillgetaccessonlyifyouareinsideornearthestore.edit();
                                Youwillgetaccessonlyifyouareinsideornearthestoreeditor.putString("Youwillgetaccessonlyifyouareinsideornearthestore", jobj1.getString("Youwillgetaccessonlyifyouareinsideornearthestore"));
                                Youwillgetaccessonlyifyouareinsideornearthestoreeditor.commit();

                                SharedPreferences PleaseprovideMinimumPrice = getApplicationContext().getSharedPreferences(Config.SHARED_PREF291, 0);
                                SharedPreferences.Editor PleaseprovideMinimumPriceeeditor = PleaseprovideMinimumPrice.edit();
                                PleaseprovideMinimumPriceeeditor.putString("PleaseprovideMinimumPrice", jobj1.getString("PleaseprovideMinimumPrice"));
                                PleaseprovideMinimumPriceeeditor.commit();

                                SharedPreferences PleaseprovideMaximumPrice = getApplicationContext().getSharedPreferences(Config.SHARED_PREF292, 0);
                                SharedPreferences.Editor PleaseprovideMaximumPriceeeditor = PleaseprovideMaximumPrice.edit();
                                PleaseprovideMaximumPriceeeditor.putString("PleaseprovideMaximumPrice", jobj1.getString("PleaseprovideMaximumPrice"));
                                PleaseprovideMaximumPriceeeditor.commit();

                                SharedPreferences GPSisdisabledinyourdeviceYouShouldenableittoproceedyourpurchase = getApplicationContext().getSharedPreferences(Config.SHARED_PREF293, 0);
                                SharedPreferences.Editor GPSisdisabledinyourdeviceYouShouldenableittoproceedyourpurchaseeeditor = GPSisdisabledinyourdeviceYouShouldenableittoproceedyourpurchase.edit();
                                GPSisdisabledinyourdeviceYouShouldenableittoproceedyourpurchaseeeditor.putString("GPSisdisabledinyourdeviceYouShouldenableittoproceedyourpurchase", jobj1.getString("GPSisdisabledinyourdeviceYouShouldenableittoproceedyourpurchase"));
                                GPSisdisabledinyourdeviceYouShouldenableittoproceedyourpurchaseeeditor.commit();


                                SharedPreferences Pleaseentername = getApplicationContext().getSharedPreferences(Config.SHARED_PREF294, 0);
                                SharedPreferences.Editor Pleaseenternameeditor = Pleaseentername.edit();
                                Pleaseenternameeditor.putString("Pleaseentername", jobj1.getString("Pleaseentername"));
                                Pleaseenternameeditor.commit();


                                SharedPreferences Pleaseenteraddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF295, 0);
                                SharedPreferences.Editor Pleaseenteraddresseditor = Pleaseenteraddress.edit();
                                Pleaseenteraddresseditor.putString("Pleaseenteraddress", jobj1.getString("Pleaseenteraddress"));
                                Pleaseenteraddresseditor.commit();


                                SharedPreferences Pleaseselectarea = getApplicationContext().getSharedPreferences(Config.SHARED_PREF296, 0);
                                SharedPreferences.Editor Pleaseselectareaeditor = Pleaseselectarea.edit();
                                Pleaseselectareaeditor.putString("Pleaseselectarea", jobj1.getString("Pleaseselectarea"));
                                Pleaseselectareaeditor.commit();


                                SharedPreferences Pleaseenterlandmark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF297, 0);
                                SharedPreferences.Editor Pleaseenterlandmarkeditor = Pleaseenterlandmark.edit();
                                Pleaseenterlandmarkeditor.putString("Pleaseenterlandmark", jobj1.getString("Pleaseenterlandmark"));
                                Pleaseenterlandmarkeditor.commit();

                                SharedPreferences OrderhasbeenRemovedSuccessfully = getApplicationContext().getSharedPreferences(Config.SHARED_PREF298, 0);
                                SharedPreferences.Editor OrderhasbeenRemovedSuccessfullyeditor = OrderhasbeenRemovedSuccessfully.edit();
                                OrderhasbeenRemovedSuccessfullyeditor.putString("ItemaddedtotheReorder", jobj1.getString("OrderhasbeenRemovedSuccessfully"));
                                OrderhasbeenRemovedSuccessfullyeditor.commit();

                                SharedPreferences Thankyouforyoursupport = getApplicationContext().getSharedPreferences(Config.SHARED_PREF299, 0);
                                SharedPreferences.Editor Thankyouforyoursupporteditor = Thankyouforyoursupport.edit();
                                Thankyouforyoursupporteditor.putString("Thankyouforyoursupport", jobj1.getString("Thankyouforyoursupport"));
                                Thankyouforyoursupporteditor.commit();

                                SharedPreferences HomeDeliverys = getApplicationContext().getSharedPreferences(Config.SHARED_PREF300, 0);
                                SharedPreferences.Editor HomeDeliveryseditor = HomeDeliverys.edit();
                                HomeDeliveryseditor.putString("HomeDeliverys", jobj1.getString("HomeDelivery"));
                                HomeDeliveryseditor.commit();

                                SharedPreferences Verifiedon = getApplicationContext().getSharedPreferences(Config.SHARED_PREF301, 0);
                                SharedPreferences.Editor Verifiedoneditor = Verifiedon.edit();
                                Verifiedoneditor.putString("Verifiedon", jobj1.getString("Verifiedon"));
                                Verifiedoneditor.commit();

                                SharedPreferences Packedon = getApplicationContext().getSharedPreferences(Config.SHARED_PREF302, 0);
                                SharedPreferences.Editor Packedoneditor = Packedon.edit();
                                Packedoneditor.putString("Packedon", jobj1.getString("Packedon"));
                                Packedoneditor.commit();

                                SharedPreferences Deliveredon = getApplicationContext().getSharedPreferences(Config.SHARED_PREF303, 0);
                                SharedPreferences.Editor Deliveredoneditor = Deliveredon.edit();
                                Deliveredoneditor.putString("Deliveredon", jobj1.getString("Deliveredon"));
                                Deliveredoneditor.commit();

                                SharedPreferences at = getApplicationContext().getSharedPreferences(Config.SHARED_PREF304, 0);
                                SharedPreferences.Editor ateditor = at.edit();
                                ateditor.putString("at", jobj1.getString("at"));
                                ateditor.commit();

                                SharedPreferences CounterPickup = getApplicationContext().getSharedPreferences(Config.SHARED_PREF305, 0);
                                SharedPreferences.Editor CounterPickupeditor = CounterPickup.edit();
                                CounterPickupeditor.putString("CounterPickup", jobj1.getString("CounterPickup"));
                                CounterPickupeditor.commit();

                                SharedPreferences AreyousureDoyouwanttodeletethisorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF306, 0);
                                SharedPreferences.Editor AreyousureDoyouwanttodeletethisordereditor = AreyousureDoyouwanttodeletethisorder.edit();
                                AreyousureDoyouwanttodeletethisordereditor.putString("AreyousureDoyouwanttodeletethisorder", jobj1.getString("AreyousureDoyouwanttodeletethisorder"));
                                AreyousureDoyouwanttodeletethisordereditor.commit();

                                SharedPreferences OrderhasbeenRemoved = getApplicationContext().getSharedPreferences(Config.SHARED_PREF307, 0);
                                SharedPreferences.Editor OrderhasbeenRemovededitor = OrderhasbeenRemoved.edit();
                                OrderhasbeenRemovededitor.putString("OrderhasbeenRemoved", jobj1.getString("OrderhasbeenRemoved"));
                                OrderhasbeenRemovededitor.commit();

                                SharedPreferences Howwasyourexperiencewiththisstore = getApplicationContext().getSharedPreferences(Config.SHARED_PREF308, 0);
                                SharedPreferences.Editor Howwasyourexperiencewiththisstoreeditor = Howwasyourexperiencewiththisstore.edit();
                                Howwasyourexperiencewiththisstoreeditor.putString("Howwasyourexperiencewiththisstore", jobj1.getString("Howwasyourexperiencewiththisstore"));
                                Howwasyourexperiencewiththisstoreeditor.commit();

                                SharedPreferences Itemupdatedtothereorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF309, 0);
                                SharedPreferences.Editor Itemupdatedtothereordereditor = Itemupdatedtothereorder.edit();
                                Itemupdatedtothereordereditor.putString("Itemupdatedtothereorder", jobj1.getString("Itemupdatedtothereorder"));
                                Itemupdatedtothereordereditor.commit();

                                SharedPreferences PleaseAddvalidAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF310, 0);
                                SharedPreferences.Editor PleaseAddvalidAddresseditor = PleaseAddvalidAddress.edit();
                                PleaseAddvalidAddresseditor.putString("PleaseAddvalidAddress", jobj1.getString("PleaseAddvalidAddress"));
                                PleaseAddvalidAddresseditor.commit();

                                SharedPreferences OrderSubmittedFailed = getApplicationContext().getSharedPreferences(Config.SHARED_PREF311, 0);
                                SharedPreferences.Editor OrderSubmittedFailededitor = OrderSubmittedFailed.edit();
                                OrderSubmittedFailededitor.putString("OrderSubmittedFailed", jobj1.getString("OrderSubmittedFailed"));
                                OrderSubmittedFailededitor.commit();

//                                SharedPreferences ExtraAmountForDelivery = getApplicationContext().getSharedPreferences(Config.SHARED_PREF312, 0);
//                                SharedPreferences.Editor ExtraAmountForDeliveryeditor = ExtraAmountForDelivery.edit();
//                                ExtraAmountForDeliveryeditor.putString("ExtraAmountForDelivery", jobj1.getString("ExtraAmountForDelivery"));
//                                ExtraAmountForDeliveryeditor.commit();

                                SharedPreferences UploadPrescription = getApplicationContext().getSharedPreferences(Config.SHARED_PREF313, 0);
                                SharedPreferences.Editor UploadPrescriptioneditor = UploadPrescription.edit();
                                UploadPrescriptioneditor.putString("UploadPrescription", jobj1.getString("UploadPrescription"));
                                UploadPrescriptioneditor.commit();

                                SharedPreferences Browse = getApplicationContext().getSharedPreferences(Config.SHARED_PREF314, 0);
                                SharedPreferences.Editor Browseeditor = Browse.edit();
                                Browseeditor.putString("Browse", jobj1.getString("Browse"));
                                Browseeditor.commit();

                                SharedPreferences Pleaseuploadimage = getApplicationContext().getSharedPreferences(Config.SHARED_PREF315, 0);
                                SharedPreferences.Editor Pleaseuploadimageeditor = Pleaseuploadimage.edit();
                                Pleaseuploadimageeditor.putString("Pleaseuploadimage", jobj1.getString("Pleaseuploadimage"));
                                Pleaseuploadimageeditor.commit();

                                SharedPreferences CameraPermissionerror = getApplicationContext().getSharedPreferences(Config.SHARED_PREF316, 0);
                                SharedPreferences.Editor CameraPermissionerroreditor = CameraPermissionerror.edit();
                                CameraPermissionerroreditor.putString("CameraPermissionerror", jobj1.getString("CameraPermissionerror"));
                                CameraPermissionerroreditor.commit();

                                SharedPreferences Price = getApplicationContext().getSharedPreferences(Config.SHARED_PREF317, 0);
                                SharedPreferences.Editor Priceeditor = Price.edit();
                                Priceeditor.putString("Price", jobj1.getString("Price"));
                                Priceeditor.commit();

                                SharedPreferences amountpayablelessthanminimumDeliveryamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF318, 0);
                                SharedPreferences.Editor amountpayablelessthanminimumDeliveryamounteditor = amountpayablelessthanminimumDeliveryamount.edit();
                                amountpayablelessthanminimumDeliveryamounteditor.putString("amountpayablelessthanminimumDeliveryamount", jobj1.getString("amountpayablelessthanminimumDeliveryamount"));
                                amountpayablelessthanminimumDeliveryamounteditor.commit();

                                SharedPreferences lastproductoforder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF319, 0);
                                SharedPreferences.Editor lastproductofordereditor = lastproductoforder.edit();
                                lastproductofordereditor.putString("lastproductoforder", jobj1.getString("lastproductoforder"));
                                lastproductofordereditor.commit();

                                SharedPreferences AreyousureDoyouwanttodeletethisproductfromorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF320, 0);
                                SharedPreferences.Editor AreyousureDoyouwanttodeletethisproductfromordereditor = AreyousureDoyouwanttodeletethisproductfromorder.edit();
                                AreyousureDoyouwanttodeletethisproductfromordereditor.putString("AreyousureDoyouwanttodeletethisproductfromorder", jobj1.getString("AreyousureDoyouwanttodeletethisproductfromorder"));
                                AreyousureDoyouwanttodeletethisproductfromordereditor.commit();

                                SharedPreferences EditQuantity = getApplicationContext().getSharedPreferences(Config.SHARED_PREF321, 0);
                                SharedPreferences.Editor EditQuantityeditor = EditQuantity.edit();
                                EditQuantityeditor.putString("EditQuantity", jobj1.getString("EditQuantity"));
                                EditQuantityeditor.commit();

                                SharedPreferences GST = getApplicationContext().getSharedPreferences(Config.SHARED_PREF322, 0);
                                SharedPreferences.Editor GSTeditor = GST.edit();
                                GSTeditor.putString("GST", jobj1.getString("GST"));
                                GSTeditor.commit();

                                SharedPreferences Included = getApplicationContext().getSharedPreferences(Config.SHARED_PREF323, 0);
                                SharedPreferences.Editor Includededitor = Included.edit();
                                Includededitor.putString("Included", jobj1.getString("Included"));
                                Includededitor.commit();

                                SharedPreferences Upload = getApplicationContext().getSharedPreferences(Config.SHARED_PREF324, 0);
                                SharedPreferences.Editor Uploadeditor = Upload.edit();
                                Uploadeditor.putString("Upload", jobj1.getString("Upload"));
                                Uploadeditor.commit();

                                SharedPreferences Transactionsuccessful = getApplicationContext().getSharedPreferences(Config.SHARED_PREF325, 0);
                                SharedPreferences.Editor Transactionsuccessfuleditor = Transactionsuccessful.edit();
                                Transactionsuccessfuleditor.putString("Transactionsuccessful", jobj1.getString("Transactionsuccessful"));
                                Transactionsuccessfuleditor.commit();

                                SharedPreferences Paymentcancelledbyuser = getApplicationContext().getSharedPreferences(Config.SHARED_PREF326, 0);
                                SharedPreferences.Editor Paymentcancelledbyusereditor = Paymentcancelledbyuser.edit();
                                Paymentcancelledbyusereditor.putString("Paymentcancelledbyuser", jobj1.getString("Paymentcancelledbyuser"));
                                Paymentcancelledbyusereditor.commit();

                                SharedPreferences TransactionfailedPleasetryagain = getApplicationContext().getSharedPreferences(Config.SHARED_PREF327, 0);
                                SharedPreferences.Editor TransactionfailedPleasetryagaineditor = TransactionfailedPleasetryagain.edit();
                                TransactionfailedPleasetryagaineditor.putString("TransactionfailedPleasetryagain", jobj1.getString("TransactionfailedPleasetryagain"));
                                TransactionfailedPleasetryagaineditor.commit();

                                SharedPreferences Errorinpayment = getApplicationContext().getSharedPreferences(Config.SHARED_PREF328, 0);
                                SharedPreferences.Editor Errorinpaymenteditor = Errorinpayment.edit();
                                Errorinpaymenteditor.putString("Errorinpayment", jobj1.getString("Errorinpayment"));
                                Errorinpaymenteditor.commit();

                                SharedPreferences PaymentSuccessfully = getApplicationContext().getSharedPreferences(Config.SHARED_PREF329, 0);
                                SharedPreferences.Editor PaymentSuccessfullyeditor = PaymentSuccessfully.edit();
                                PaymentSuccessfullyeditor.putString("PaymentSuccessfully", jobj1.getString("PaymentSuccessfully"));
                                PaymentSuccessfullyeditor.commit();

                                SharedPreferences Paymentfailed = getApplicationContext().getSharedPreferences(Config.SHARED_PREF330, 0);
                                SharedPreferences.Editor Paymentfailededitor = Paymentfailed.edit();
                                Paymentfailededitor.putString("Paymentfailed", jobj1.getString("Paymentfailed"));
                                Paymentfailededitor.commit();

                                SharedPreferences PleaseprovideKeywordforsearch = getApplicationContext().getSharedPreferences(Config.SHARED_PREF331, 0);
                                SharedPreferences.Editor PleaseprovideKeywordforsearcheditor = PleaseprovideKeywordforsearch.edit();
                                PleaseprovideKeywordforsearcheditor.putString("PleaseprovideKeywordforsearch", jobj1.getString("PleaseprovideKeywordforsearch"));
                                PleaseprovideKeywordforsearcheditor.commit();

                                SharedPreferences Itemaddedtothefavourites = getApplicationContext().getSharedPreferences(Config.SHARED_PREF332, 0);
                                SharedPreferences.Editor Itemaddedtothefavouriteseditor = Itemaddedtothefavourites.edit();
                                Itemaddedtothefavouriteseditor.putString("Itemaddedtothefavourites", jobj1.getString("Itemaddedtothefavourites"));
                                Itemaddedtothefavouriteseditor.commit();

                                SharedPreferences ItemAddedToYourOrderList = getApplicationContext().getSharedPreferences(Config.SHARED_PREF333, 0);
                                SharedPreferences.Editor ItemAddedToYourOrderListeditor = ItemAddedToYourOrderList.edit();
                                ItemAddedToYourOrderListeditor.putString("ItemAddedToYourOrderList", jobj1.getString("ItemAddedToYourOrderList"));
                                ItemAddedToYourOrderListeditor.commit();

                                SharedPreferences ItemAddedToYourOrderListFailed = getApplicationContext().getSharedPreferences(Config.SHARED_PREF334, 0);
                                SharedPreferences.Editor ItemAddedToYourOrderListFailededitor = ItemAddedToYourOrderListFailed.edit();
                                ItemAddedToYourOrderListFailededitor.putString("ItemAddedToYourOrderListFailed", jobj1.getString("ItemAddedToYourOrderListFailed"));
                                ItemAddedToYourOrderListFailededitor.commit();

                                SharedPreferences Itemquantityupdatedtothecart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF335, 0);
                                SharedPreferences.Editor Itemquantityupdatedtothecarteditor = Itemquantityupdatedtothecart.edit();
                                Itemquantityupdatedtothecarteditor.putString("Itemquantityupdatedtothecart", jobj1.getString("Itemquantityupdatedtothecart"));
                                Itemquantityupdatedtothecarteditor.commit();

                                SharedPreferences Pleaseupdatequantitytothecart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF336, 0);
                                SharedPreferences.Editor Pleaseupdatequantitytothecarteditor = Pleaseupdatequantitytothecart.edit();
                                Pleaseupdatequantitytothecarteditor.putString("Pleaseupdatequantitytothecart", jobj1.getString("Pleaseupdatequantitytothecart"));
                                Pleaseupdatequantitytothecarteditor.commit();

                                SharedPreferences Itemaddedtothecart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF337, 0);
                                SharedPreferences.Editor Itemaddedtothecarteditor = Itemaddedtothecart.edit();
                                Itemaddedtothecarteditor.putString("Itemaddedtothecart", jobj1.getString("Itemaddedtothecart"));
                                Itemaddedtothecarteditor.commit();

                                SharedPreferences Itemremovedfromthefavourites = getApplicationContext().getSharedPreferences(Config.SHARED_PREF338, 0);
                                SharedPreferences.Editor Itemremovedfromthefavouriteseditor = Itemremovedfromthefavourites.edit();
                                Itemremovedfromthefavouriteseditor.putString("Itemremovedfromthefavourites", jobj1.getString("Itemremovedfromthefavourites"));
                                Itemremovedfromthefavouriteseditor.commit();




                                Log.e(TAG,"8322    "+Itemremovedfromthefavourites.getString("Itemremovedfromthefavourites",""));
                                Log.e(TAG,"8322    "+jobj1.getString("Itemremovedfromthefavourites"));





                                SharedPreferences Itemremovedfromthecart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF339, 0);
                                SharedPreferences.Editor Itemremovedfromthecarteditor = Itemremovedfromthecart.edit();
                                Itemremovedfromthecarteditor.putString("Itemremovedfromthecart", jobj1.getString("Itemremovedfromthecart"));
                                Itemremovedfromthecarteditor.commit();


                                SharedPreferences Thereisnoorderlisttoshow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF344, 0);
                                SharedPreferences.Editor Thereisnoorderlisttoshoweditor = Thereisnoorderlisttoshow.edit();
                                Thereisnoorderlisttoshoweditor.putString("Thereisnoorderlisttoshow", jobj1.getString("Thereisnoorderlisttoshow"));
                                Thereisnoorderlisttoshoweditor.commit();

                                SharedPreferences Thereisnofavouriteitemstoshow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF345, 0);
                                SharedPreferences.Editor Thereisnofavouriteitemstoshoweditor = Thereisnofavouriteitemstoshow.edit();
                                Thereisnofavouriteitemstoshoweditor.putString("Thereisnofavouriteitemstoshow", jobj1.getString("Thereisnofavouriteitemstoshow"));
                                Thereisnofavouriteitemstoshoweditor.commit();

                                SharedPreferences Thereisnofavouritestoretoshow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF346, 0);
                                SharedPreferences.Editor Thereisnofavouritestoretoshoweditor = Thereisnofavouritestoretoshow.edit();
                                Thereisnofavouritestoretoshoweditor.putString("Thereisnofavouritestoretoshow", jobj1.getString("Thereisnofavouritestoretoshow"));
                                Thereisnofavouritestoretoshoweditor.commit();


                                Log.e(TAG,"EnterNewPassword  "+jobj1.getString("EnterNewPassword"));
                                SharedPreferences EnterNewPassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF347, 0);
                                SharedPreferences.Editor EnterNewPasswordeditor = EnterNewPassword.edit();
                                EnterNewPasswordeditor.putString("EnterNewPassword", jobj1.getString("EnterNewPassword"));
                                EnterNewPasswordeditor.commit();

                                SharedPreferences Scanorinsertbarcodeontheitemandpleaseaddittoyourcart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF348, 0);
                                SharedPreferences.Editor Scanorinsertbarcodeontheitemandpleaseaddittoyourcarteditor = Scanorinsertbarcodeontheitemandpleaseaddittoyourcart.edit();
                                Scanorinsertbarcodeontheitemandpleaseaddittoyourcarteditor.putString("Scanorinsertbarcodeontheitemandpleaseaddittoyourcart", jobj1.getString("Scanorinsertbarcodeontheitemandpleaseaddittoyourcart"));
                                Scanorinsertbarcodeontheitemandpleaseaddittoyourcarteditor.commit();

                                SharedPreferences InsertCode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF349, 0);
                                SharedPreferences.Editor InsertCodeeditor = InsertCode.edit();
                                InsertCodeeditor.putString("InsertCode", jobj1.getString("InsertCode"));
                                InsertCodeeditor.commit();

                                SharedPreferences ScanCode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF350, 0);
                                SharedPreferences.Editor ScanCodeeditor = ScanCode.edit();
                                ScanCodeeditor.putString("ScanCode", jobj1.getString("ScanCode"));
                                ScanCodeeditor.commit();

                                SharedPreferences Noitemfoundwhilescanning = getApplicationContext().getSharedPreferences(Config.SHARED_PREF351, 0);
                                SharedPreferences.Editor Noitemfoundwhilescanningeditor = Noitemfoundwhilescanning.edit();
                                Noitemfoundwhilescanningeditor.putString("Noitemfoundwhilescanning", jobj1.getString("Noitemfoundwhilescanning"));
                                Noitemfoundwhilescanningeditor.commit();


                                SharedPreferences Pleaseprovidebarcodevalue = getApplicationContext().getSharedPreferences(Config.SHARED_PREF352, 0);
                                SharedPreferences.Editor Pleaseprovidebarcodevalueeditor = Pleaseprovidebarcodevalue.edit();
                                Pleaseprovidebarcodevalueeditor.putString("Pleaseprovidebarcodevalue", jobj1.getString("Pleaseprovidebarcodevalue"));
                                Pleaseprovidebarcodevalueeditor.commit();

                                SharedPreferences EnterOldPassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF353, 0);
                                SharedPreferences.Editor EnterOldPasswordeditor = EnterOldPassword.edit();
                                EnterOldPasswordeditor.putString("EnterOldPassword", jobj1.getString("EnterOldPassword"));
                                EnterOldPasswordeditor.commit();

                                SharedPreferences Invalidtime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF354, 0);
                                SharedPreferences.Editor Invalidtimeeditor = Invalidtime.edit();
                                Invalidtimeeditor.putString("Invalidtime", jobj1.getString("Invalidtime"));
                                Invalidtimeeditor.commit();

                                SharedPreferences Selecttimegreaterthancurrenttime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF355, 0);
                                SharedPreferences.Editor Selecttimegreaterthancurrenttimeeditor = Selecttimegreaterthancurrenttime.edit();
                                Selecttimegreaterthancurrenttimeeditor.putString("Selecttimegreaterthancurrenttime", jobj1.getString("Selecttimegreaterthancurrenttime"));
                                Selecttimegreaterthancurrenttimeeditor.commit();

                                SharedPreferences SelectStore = getApplicationContext().getSharedPreferences(Config.SHARED_PREF356, 0);
                                SharedPreferences.Editor SelectStoreeditor = SelectStore.edit();
                                SelectStoreeditor.putString("SelectStore", jobj1.getString("SelectStore"));
                                SelectStoreeditor.commit();

                                SharedPreferences Sendemail = getApplicationContext().getSharedPreferences(Config.SHARED_PREF357, 0);
                                SharedPreferences.Editor Sendemaileditor = Sendemail.edit();
                                Sendemaileditor.putString("Sendemail", jobj1.getString("Sendemail"));
                                Sendemaileditor.commit();

                                SharedPreferences EmailAddressMobileNumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF358, 0);
                                SharedPreferences.Editor EmailAddressMobileNumbereditor = EmailAddressMobileNumber.edit();
                                EmailAddressMobileNumbereditor.putString("EmailAddressMobileNumber", jobj1.getString("EmailAddressMobileNumber"));
                                EmailAddressMobileNumbereditor.commit();

                                SharedPreferences EnterEmailAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF359, 0);
                                SharedPreferences.Editor EnterEmailAddresseditor = EnterEmailAddress.edit();
                                EnterEmailAddresseditor.putString("EnterEmailAddress", jobj1.getString("EnterEmailAddress"));
                                EnterEmailAddresseditor.commit();

                                SharedPreferences EnterMobileNumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF360, 0);
                                SharedPreferences.Editor EnterMobileNumbereditor = EnterMobileNumber.edit();
                                EnterMobileNumbereditor.putString("EnterMobileNumber", jobj1.getString("EnterMobileNumber"));
                                EnterMobileNumbereditor.commit();

                                SharedPreferences NewPasswordissendtoyournumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF361, 0);
                                SharedPreferences.Editor NewPasswordissendtoyournumbereditor = NewPasswordissendtoyournumber.edit();
                                NewPasswordissendtoyournumbereditor.putString("NewPasswordissendtoyournumber", jobj1.getString("NewPasswordissendtoyournumber"));
                                NewPasswordissendtoyournumbereditor.commit();

                                SharedPreferences EnterValidMobileNumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF362, 0);
                                SharedPreferences.Editor EnterValidMobileNumbereditor = EnterValidMobileNumber.edit();
                                EnterValidMobileNumbereditor.putString("EnterValidMobileNumber", jobj1.getString("EnterValidMobileNumber"));
                                EnterValidMobileNumbereditor.commit();

                                SharedPreferences Pleaseprovideotp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF363, 0);
                                SharedPreferences.Editor Pleaseprovideotpeditor = Pleaseprovideotp.edit();
                                Pleaseprovideotpeditor.putString("Pleaseprovideotp", jobj1.getString("Pleaseprovideotp"));
                                Pleaseprovideotpeditor.commit();

                                SharedPreferences PleaseprovideavalidEmailAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF364, 0);
                                SharedPreferences.Editor PleaseprovideavalidEmailAddresseditor = PleaseprovideavalidEmailAddress.edit();
                                PleaseprovideavalidEmailAddresseditor.putString("PleaseprovideavalidEmailAddress", jobj1.getString("PleaseprovideavalidEmailAddress"));
                                PleaseprovideavalidEmailAddresseditor.commit();

                                SharedPreferences YourPasswordandconfirmationpassworddonotmatch = getApplicationContext().getSharedPreferences(Config.SHARED_PREF365, 0);
                                SharedPreferences.Editor YourPasswordandconfirmationpassworddonotmatcheditor = YourPasswordandconfirmationpassworddonotmatch.edit();
                                YourPasswordandconfirmationpassworddonotmatcheditor.putString("YourPasswordandconfirmationpassworddonotmatch", jobj1.getString("YourPasswordandconfirmationpassworddonotmatch"));
                                YourPasswordandconfirmationpassworddonotmatcheditor.commit();

                                SharedPreferences PleaseprovideyourConfirmPassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF366, 0);
                                SharedPreferences.Editor PleaseprovideyourConfirmPasswordeditor = PleaseprovideyourConfirmPassword.edit();
                                PleaseprovideyourConfirmPasswordeditor.putString("PleaseprovideyourConfirmPassword", jobj1.getString("PleaseprovideyourConfirmPassword"));
                                PleaseprovideyourConfirmPasswordeditor.commit();

                                SharedPreferences OTPissendtoyourregisteredMobilenumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF367, 0);
                                SharedPreferences.Editor OTPissendtoyourregisteredMobilenumbereditor = OTPissendtoyourregisteredMobilenumber.edit();
                                OTPissendtoyourregisteredMobilenumbereditor.putString("OTPissendtoyourregisteredMobilenumber", jobj1.getString("OTPsendtoyouregisteredMobilenumber"));
                                OTPissendtoyourregisteredMobilenumbereditor.commit();

                                SharedPreferences errorinscanning = getApplicationContext().getSharedPreferences(Config.SHARED_PREF368, 0);
                                SharedPreferences.Editor errorinscanningeditor = errorinscanning.edit();
                                errorinscanningeditor.putString("errorinscanning", jobj1.getString("errorinscanning"));
                                errorinscanningeditor.commit();

                                SharedPreferences Camerapermissiondenied = getApplicationContext().getSharedPreferences(Config.SHARED_PREF369, 0);
                                SharedPreferences.Editor Camerapermissiondeniededitor = Camerapermissiondenied.edit();
                                Camerapermissiondeniededitor.putString("Camerapermissiondenied", jobj1.getString("Camerapermissiondenied"));
                                Camerapermissiondeniededitor.commit();

                                SharedPreferences PleaseEnterQuantity = getApplicationContext().getSharedPreferences(Config.SHARED_PREF370, 0);
                                SharedPreferences.Editor PleaseEnterQuantityeditor = PleaseEnterQuantity.edit();
                                PleaseEnterQuantityeditor.putString("PleaseEnterQuantity", jobj1.getString("PleaseEnterQuantity"));
                                PleaseEnterQuantityeditor.commit();

                                SharedPreferences Selected = getApplicationContext().getSharedPreferences(Config.SHARED_PREF371, 0);
                                SharedPreferences.Editor Selectededitor = Selected.edit();
                                Selectededitor.putString("Selected", jobj1.getString("Selected"));
                                Selectededitor.commit();

                                SharedPreferences PermissionGranted = getApplicationContext().getSharedPreferences(Config.SHARED_PREF372, 0);
                                SharedPreferences.Editor PermissionGrantededitor = PermissionGranted.edit();
                                PermissionGrantededitor.putString("PermissionGranted", jobj1.getString("PermissionGranted"));
                                PermissionGrantededitor.commit();





                                SharedPreferences permissiondenied = getApplicationContext().getSharedPreferences(Config.SHARED_PREF77, 0);
                                SharedPreferences.Editor permissiondeniededitor = permissiondenied.edit();
                                permissiondeniededitor.putString("permissiondenied", jobj1.getString("permissiondenied"));
                                permissiondeniededitor.commit();

                                SharedPreferences Servicesforyourselectedcountryisnotavailable = getApplicationContext().getSharedPreferences(Config.SHARED_PREF78, 0);
                                SharedPreferences.Editor Servicesforyourselectedcountryisnotavailableeditor = Servicesforyourselectedcountryisnotavailable.edit();
                                Servicesforyourselectedcountryisnotavailableeditor.putString("Servicesforyourselectedcountryisnotavailable", jobj1.getString("Servicesforyourselectedcountryisnotavailable"));
                                Servicesforyourselectedcountryisnotavailableeditor.commit();

                                SharedPreferences ReloadingFailedPleasecheckyourinternetconnection = getApplicationContext().getSharedPreferences(Config.SHARED_PREF79, 0);
                                SharedPreferences.Editor ReloadingFailedPleasecheckyourinternetconnectioneditor = ReloadingFailedPleasecheckyourinternetconnection.edit();
                                ReloadingFailedPleasecheckyourinternetconnectioneditor.putString("ReloadingFailedPleasecheckyourinternetconnection", jobj1.getString("ReloadingFailedPleasecheckyourinternetconnection"));
                                ReloadingFailedPleasecheckyourinternetconnectioneditor.commit();

                                SharedPreferences Pleasewaityourorderisunderprocessing = getApplicationContext().getSharedPreferences(Config.SHARED_PREF257, 0);
                                SharedPreferences.Editor Pleasewaityourorderisunderprocessingeditor = Pleasewaityourorderisunderprocessing.edit();
                                Pleasewaityourorderisunderprocessingeditor.putString("Pleasewaityourorderisunderprocessing", jobj1.getString("Pleasewaityourorderisunderprocessing"));
                                Pleasewaityourorderisunderprocessingeditor.commit();

                                SharedPreferences pleaseselectcorrectdateinterval = getApplicationContext().getSharedPreferences(Config.SHARED_PREF258, 0);
                                SharedPreferences.Editor pleaseselectcorrectdateintervaleditor = pleaseselectcorrectdateinterval.edit();
                                pleaseselectcorrectdateintervaleditor.putString("pleaseselectcorrectdateinterval", jobj1.getString("pleaseselectcorrectdateinterval"));
                                pleaseselectcorrectdateintervaleditor.commit();

                                SharedPreferences Noitemfound = getApplicationContext().getSharedPreferences(Config.SHARED_PREF259, 0);
                                SharedPreferences.Editor Noitemfoundeditor = Noitemfound.edit();
                                Noitemfoundeditor.putString("Noitemfound", jobj1.getString("Noitemfound"));
                                Noitemfoundeditor.commit();

                                SharedPreferences JustUploadyourPrescriptiontoplaceorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF383, 0);
                                SharedPreferences.Editor JustUploadyourPrescriptiontoplaceordereditor = JustUploadyourPrescriptiontoplaceorder.edit();
                                JustUploadyourPrescriptiontoplaceordereditor.putString("JustUploadyourPrescriptiontoplaceorder", jobj1.getString("JustUploadyourPrescriptiontoplaceorder"));
                                JustUploadyourPrescriptiontoplaceordereditor.commit();

                                SharedPreferences GiftVoucher = getApplicationContext().getSharedPreferences(Config.SHARED_PREF384, 0);
                                SharedPreferences.Editor GiftVouchereditor = GiftVoucher.edit();
                                GiftVouchereditor.putString("GiftVoucher", jobj1.getString("GiftVoucher"));
                                GiftVouchereditor.commit();

                                SharedPreferences Earnyourrewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF385, 0);
                                SharedPreferences.Editor Earnyourrewardseditor = Earnyourrewards.edit();
                                Earnyourrewardseditor.putString("Earnyourrewards", jobj1.getString("Earnyourrewards"));
                                Earnyourrewardseditor.commit();

                                SharedPreferences TotalRewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF386, 0);
                                SharedPreferences.Editor TotalRewardseditor = Noitemfound.edit();
                                TotalRewardseditor.putString("TotalRewards", jobj1.getString("TotalRewards"));
                                TotalRewardseditor.commit();

                                // 04.08.2021
                                SharedPreferences YourRewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF388, 0);
                                SharedPreferences.Editor YourRewardseditor = YourRewards.edit();
                                YourRewardseditor.putString("YourRewards", jobj1.getString("YourRewards"));
                                YourRewardseditor.commit();

                                SharedPreferences GiftCoupons = getApplicationContext().getSharedPreferences(Config.SHARED_PREF389, 0);
                                SharedPreferences.Editor GiftCouponseditor = GiftCoupons.edit();
                                GiftCouponseditor.putString("GiftCoupons", jobj1.getString("GiftCoupons"));
                                GiftCouponseditor.commit();

                                SharedPreferences MyRewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF390, 0);
                                SharedPreferences.Editor MyRewardseditor = MyRewards.edit();
                                MyRewardseditor.putString("MyRewards", jobj1.getString("MyRewards"));
                                MyRewardseditor.commit();

                                SharedPreferences BalancetoRedeem = getApplicationContext().getSharedPreferences(Config.SHARED_PREF391, 0);
                                SharedPreferences.Editor BalancetoRedeemeditor = BalancetoRedeem.edit();
                                BalancetoRedeemeditor.putString("BalancetoRedeem", jobj1.getString("BalancetoRedeem"));
                                BalancetoRedeemeditor.commit();

                                SharedPreferences Rewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF392, 0);
                                SharedPreferences.Editor Rewardseditor = Rewards.edit();
                                Rewardseditor.putString("Rewards", jobj1.getString("Rewards"));
                                Rewardseditor.commit();

                                SharedPreferences Doyouwanttoreddemfromyourrewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF393, 0);
                                SharedPreferences.Editor Doyouwanttoreddemfromyourrewardseditor = Doyouwanttoreddemfromyourrewards.edit();
                                Doyouwanttoreddemfromyourrewardseditor.putString("Doyouwanttoreddemfromyourrewards", jobj1.getString("Doyouwanttoreddemfromyourrewards"));
                                Doyouwanttoreddemfromyourrewardseditor.commit();

                                SharedPreferences Yourreward = getApplicationContext().getSharedPreferences(Config.SHARED_PREF394, 0);
                                SharedPreferences.Editor Yourrewardeditor = Yourreward.edit();
                                Yourrewardeditor.putString("Yourreward", jobj1.getString("Yourreward"));
                                Yourrewardeditor.commit();

                                SharedPreferences Apply = getApplicationContext().getSharedPreferences(Config.SHARED_PREF395, 0);
                                SharedPreferences.Editor Applyeditor = Apply.edit();
                                Applyeditor.putString("Apply", jobj1.getString("Apply"));
                                Applyeditor.commit();

                                SharedPreferences RedeemYourRewards = getApplicationContext().getSharedPreferences(Config.SHARED_PREF396, 0);
                                SharedPreferences.Editor RedeemYourRewardseditor = RedeemYourRewards.edit();
                                RedeemYourRewardseditor.putString("RedeemYourRewards", jobj1.getString("RedeemYourRewards"));
                                RedeemYourRewardseditor.commit();

                                SharedPreferences ApplyGiftCoupon = getApplicationContext().getSharedPreferences(Config.SHARED_PREF397, 0);
                                SharedPreferences.Editor ApplyGiftCouponeditor = ApplyGiftCoupon.edit();
                                ApplyGiftCouponeditor.putString("ApplyGiftCoupon", jobj1.getString("ApplyGiftCoupon"));
                                ApplyGiftCouponeditor.commit();

                                SharedPreferences RedeemAmount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF398, 0);
                                SharedPreferences.Editor RedeemAmounteditor = RedeemAmount.edit();
                                RedeemAmounteditor.putString("RedeemAmount", jobj1.getString("RedeemAmount"));
                                RedeemAmounteditor.commit();

                                SharedPreferences EnterRedeemAmount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF399, 0);
                                SharedPreferences.Editor EnterRedeemAmounteditor = EnterRedeemAmount.edit();
                                EnterRedeemAmounteditor.putString("EnterRedeemAmount", jobj1.getString("EnterRedeemAmount"));
                                EnterRedeemAmounteditor.commit();

                                SharedPreferences betterlucknexttime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF400, 0);
                                SharedPreferences.Editor betterlucknexttimeeditor = betterlucknexttime.edit();
                                betterlucknexttimeeditor.putString("betterlucknexttime", jobj1.getString("betterlucknexttime"));
                                betterlucknexttimeeditor.commit();

                                SharedPreferences ScratchWin = getApplicationContext().getSharedPreferences(Config.SHARED_PREF401, 0);
                                SharedPreferences.Editor ScratchWineditor = ScratchWin.edit();
                                ScratchWineditor.putString("ScratchWin", jobj1.getString("ScratchWin"));
                                ScratchWineditor.commit();


                                SharedPreferences CreateAccount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF406, 0);
                                SharedPreferences.Editor CreateAccounteditor = CreateAccount.edit();
                                CreateAccounteditor.putString("CreateAccount", jobj1.getString("CreateAccount"));
                                CreateAccounteditor.commit();

                                SharedPreferences signuptogetstarted = getApplicationContext().getSharedPreferences(Config.SHARED_PREF407, 0);
                                SharedPreferences.Editor signuptogetstartededitor = signuptogetstarted.edit();
                                signuptogetstartededitor.putString("signuptogetstarted", jobj1.getString("signuptogetstarted"));
                                signuptogetstartededitor.commit();

                                SharedPreferences PleseProvideavalidemailaddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF408, 0);
                                SharedPreferences.Editor PleseProvideavalidemailaddresseditor = PleseProvideavalidemailaddress.edit();
                                PleseProvideavalidemailaddresseditor.putString("PleseProvideavalidemailaddress", jobj1.getString("PleseProvideavalidemailaddress"));
                                PleseProvideavalidemailaddresseditor.commit();

                                SharedPreferences Pleaseenterthecodewassentinyourphonenumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF409, 0);
                                SharedPreferences.Editor Pleaseenterthecodewassentinyourphonenumbereditor = Pleaseenterthecodewassentinyourphonenumber.edit();
                                Pleaseenterthecodewassentinyourphonenumbereditor.putString("Pleaseenterthecodewassentinyourphonenumber", jobj1.getString("Pleaseenterthecodewassentinyourphonenumber"));
                                Pleaseenterthecodewassentinyourphonenumbereditor.commit();

                                Log.e(TAG,"signintocontinue  2086   "+jobj1.getString("signintocontinue!"));

                                SharedPreferences signintocontinue = getApplicationContext().getSharedPreferences(Config.SHARED_PREF410, 0);
                                SharedPreferences.Editor signintocontinueeditor = signintocontinue.edit();
                                signintocontinueeditor.putString("signintocontinue!", jobj1.getString("signintocontinue!"));
                                signintocontinueeditor.commit();


                                SharedPreferences Continue = getApplicationContext().getSharedPreferences(Config.SHARED_PREF411, 0);
                                SharedPreferences.Editor Continueeditor = Continue.edit();
                                Continueeditor.putString("Continue", jobj1.getString("Continue"));
                                Continueeditor.commit();

                                SharedPreferences Saveforlater = getApplicationContext().getSharedPreferences(Config.SHARED_PREF412, 0);
                                SharedPreferences.Editor Saveforlatereditor = Saveforlater.edit();
                                Saveforlatereditor.putString("Saveforlater", jobj1.getString("Saveforlater"));
                                Saveforlatereditor.commit();

                                SharedPreferences Remove = getApplicationContext().getSharedPreferences(Config.SHARED_PREF413, 0);
                                SharedPreferences.Editor Removeeditor = Remove.edit();
                                Removeeditor.putString("Remove", jobj1.getString("Remove"));
                                Removeeditor.commit();

                                SharedPreferences PaymentMode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF414, 0);
                                SharedPreferences.Editor PaymentModeeditor = PaymentMode.edit();
                                PaymentModeeditor.putString("PaymentMode", jobj1.getString("PaymentMode"));
                                PaymentModeeditor.commit();

                                SharedPreferences PayNow = getApplicationContext().getSharedPreferences(Config.SHARED_PREF415, 0);
                                SharedPreferences.Editor PayNoweditor = PayNow.edit();
                                PayNoweditor.putString("PayNow", jobj1.getString("PayNow"));
                                PayNoweditor.commit();

                                SharedPreferences ShippingAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF416, 0);
                                SharedPreferences.Editor ShippingAddresseditor = ShippingAddress.edit();
                                ShippingAddresseditor.putString("ShippingAddress", jobj1.getString("ShippingAddress"));
                                ShippingAddresseditor.commit();

                                SharedPreferences SecurePaymentGenuineProducts = getApplicationContext().getSharedPreferences(Config.SHARED_PREF417, 0);
                                SharedPreferences.Editor SecurePaymentGenuineProductseditor = SecurePaymentGenuineProducts.edit();
                                SecurePaymentGenuineProductseditor.putString("SecurePaymentGenuineProducts", jobj1.getString("SecurePayment|GenuineProducts"));
                                SecurePaymentGenuineProductseditor.commit();

                                SharedPreferences Selectanaddresstoplacetheorder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF418, 0);
                                SharedPreferences.Editor Selectanaddresstoplacetheordereditor = Selectanaddresstoplacetheorder.edit();
                                Selectanaddresstoplacetheordereditor.putString("Selectanaddresstoplacetheorder", jobj1.getString("Selectanaddresstoplacetheorder"));
                                Selectanaddresstoplacetheordereditor.commit();

                                SharedPreferences ADDNEW = getApplicationContext().getSharedPreferences(Config.SHARED_PREF419, 0);
                                SharedPreferences.Editor ADDNEWeditor = ADDNEW.edit();
                                ADDNEWeditor.putString("ADDNEW", jobj1.getString("ADDNEW"));
                                ADDNEWeditor.commit();

                                SharedPreferences ProductPrice = getApplicationContext().getSharedPreferences(Config.SHARED_PREF420, 0);
                                SharedPreferences.Editor ProductPriceeditor = ProductPrice.edit();
                                ProductPriceeditor.putString("ProductPrice", jobj1.getString("ProductPrice"));
                                ProductPriceeditor.commit();

                                SharedPreferences CASHONDELIVERYAVAILABLE = getApplicationContext().getSharedPreferences(Config.SHARED_PREF421, 0);
                                SharedPreferences.Editor CASHONDELIVERYAVAILABLEeditor = CASHONDELIVERYAVAILABLE.edit();
                                CASHONDELIVERYAVAILABLEeditor.putString("CASHONDELIVERYAVAILABLE", jobj1.getString("CASHONDELIVERYAVAILABLE"));
                                CASHONDELIVERYAVAILABLEeditor.commit();

                                SharedPreferences SOLDBY = getApplicationContext().getSharedPreferences(Config.SHARED_PREF422, 0);
                                SharedPreferences.Editor SOLDBYeditor = SOLDBY.edit();
                                SOLDBYeditor.putString("SOLDBY", jobj1.getString("SOLDBY"));
                                SOLDBYeditor.commit();

                                SharedPreferences GenuineProduct = getApplicationContext().getSharedPreferences(Config.SHARED_PREF423, 0);
                                SharedPreferences.Editor GenuineProducteditor = GenuineProduct.edit();
                                GenuineProducteditor.putString("GenuineProduct", jobj1.getString("GenuineProduct"));
                                GenuineProducteditor.commit();

                                SharedPreferences Qualitychecked = getApplicationContext().getSharedPreferences(Config.SHARED_PREF424, 0);
                                SharedPreferences.Editor Qualitycheckededitor = Qualitychecked.edit();
                                Qualitycheckededitor.putString("Qualitychecked", jobj1.getString("Qualitychecked"));
                                Qualitycheckededitor.commit();

                                SharedPreferences Greatesavings = getApplicationContext().getSharedPreferences(Config.SHARED_PREF425, 0);
                                SharedPreferences.Editor Greatesavingseditor = Greatesavings.edit();
                                Greatesavingseditor.putString("Greatesavings", jobj1.getString("Greatesavings"));
                                Greatesavingseditor.commit();

                                SharedPreferences PaymentStatus = getApplicationContext().getSharedPreferences(Config.SHARED_PREF426, 0);
                                SharedPreferences.Editor PaymentStatuseditor = PaymentStatus.edit();
                                PaymentStatuseditor.putString("PaymentStatus", jobj1.getString("PaymentStatus"));
                                PaymentStatuseditor.commit();

                              /*  SharedPreferences CartView = getApplicationContext().getSharedPreferences(Config.SHARED_PREF427, 0);
                                SharedPreferences.Editor CartVieweditor = CartView.edit();
                                CartVieweditor.putString("CartView", jobj1.getString("CartView"));
                                CartVieweditor.commit();*/

                                SharedPreferences changepaymenttype = getApplicationContext().getSharedPreferences(Config.SHARED_PREF428, 0);
                                SharedPreferences.Editor changepaymenttypeeditor = changepaymenttype.edit();
                                changepaymenttypeeditor.putString("Changepaymenttype", jobj1.getString("Changepaymenttype"));
                                changepaymenttypeeditor.commit();





                                if(getResources().getString(R.string.app_name).equals("JZT CART") || getResources().getString(R.string.app_name).equals("Pulikkottil Hypermarket") || getResources().getString(R.string.app_name).equals("NeethiMed") || getResources().getString(R.string.app_name).equals("TNB Demo") || getResources().getString(R.string.app_name).equals("NeethiCoOp") ) {

                                    Intent i = new Intent(SplashActivity.this, WelcomeActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                                else{
                                    Intent i = new Intent(SplashActivity.this, LocationActivity.class);
                                    startActivity(i);
                                    finish();
                                }

                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),""+jObject.getString("EXMessage"),Toast.LENGTH_SHORT).show();
                            }


                        }
                        catch (JSONException e) {
                            progressDialog.dismiss();
                            e.printStackTrace();
                            Log.e(TAG,"printStackTrace   2003   "+e.toString());
                        }




                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        progressDialog.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
        }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }

    private void getResellerDetails() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = pref.getString("BaseURL", null);
        String imageurl = pref1.getString("ImageURL", null);

        Log.e(TAG,"BaseURL  2033   "+BASEURL);

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
                    requestObject1.put("ReqMode","23");
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG,"requestObject1   2067   "+requestObject1);
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getResellerDetails(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("ResellerDetails");
                            Log.e(TAG,"onResponse   2076   "+response.body());
                            if(jObject.getString("StatusCode").equals("0")){

                              /*  SharedPreferences Requiredcounterpickuppref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF44, 0);
                                SharedPreferences.Editor Requiredcounterpickupeditor = Requiredcounterpickuppref.edit();
                                Requiredcounterpickupeditor.putString("Requiredcounterpickup", "true");
                                Requiredcounterpickupeditor.commit();*/

                                //ImageView imSplash=findViewById(R.id.imSplash);
                                //String strimagepath= Config.IMAGEURL + jobj.getString("SplashImageCode");
                                // PicassoTrustAll.getInstance(LocationActivity.this).load(strimagepath).into(imSplash);
                                SharedPreferences ResellerNamepref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF31, 0);
                                SharedPreferences.Editor ResellerNameeditor = ResellerNamepref.edit();
                                ResellerNameeditor.putString("ResellerName", jobj.getString("ResellerName"));
                                ResellerNameeditor.commit();
                                SharedPreferences SplashImageCodepref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF32, 0);
                                SharedPreferences.Editor SplashImageCodeeditor = SplashImageCodepref.edit();
                                SplashImageCodeeditor.putString("SplashImageCode", jobj.getString("SplashImageCode"));
                                SplashImageCodeeditor.commit();
                                SharedPreferences AppIconImageCodepref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
                                SharedPreferences.Editor AppIconImageCodeeditor = AppIconImageCodepref.edit();
                                AppIconImageCodeeditor.putString("AppIconImageCode", jobj.getString("AppIconImageCode"));
                                AppIconImageCodeeditor.commit();
                                SharedPreferences CompanyLogoImageCodepref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF34, 0);
                                SharedPreferences.Editor CompanyLogoImageCodeeditor = CompanyLogoImageCodepref.edit();
                                CompanyLogoImageCodeeditor.putString("CompanyLogoImageCode", jobj.getString("CompanyLogoImageCode"));
                                CompanyLogoImageCodeeditor.commit();
                                SharedPreferences LogoImageCodepref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF38, 0);
                                SharedPreferences.Editor LogoImageCodeeditor = LogoImageCodepref.edit();
                                LogoImageCodeeditor.putString("LogoImageCode", jobj.getString("LogoImageCode"));
                                LogoImageCodeeditor.commit();
                                SharedPreferences ProductNamepref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF35, 0);
                                SharedPreferences.Editor ProductNameeditor = ProductNamepref.edit();
                                ProductNameeditor.putString("ProductName", jobj.getString("ProductName"));

                                ProductNameeditor.commit();
                                SharedPreferences RequiredStorepref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF36, 0);
                                SharedPreferences.Editor RequiredStoreeditor = RequiredStorepref.edit();
                                RequiredStoreeditor.putString("RequiredStore", jobj.getString("RequiredStore"));
                                RequiredStoreeditor.commit();


                                SharedPreferences RequiredStoreCategorypref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF37, 0);
                                SharedPreferences.Editor RequiredStoreCategoryeditor = RequiredStoreCategorypref.edit();
                                RequiredStoreCategoryeditor.putString("RequiredStoreCategory", jobj.getString("RequiredStoreCategory"));
                                RequiredStoreCategoryeditor.commit();
                                SharedPreferences ExpressDeliverypref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF39, 0);
                                SharedPreferences.Editor ExpressDeliveryeditor = ExpressDeliverypref.edit();
                                ExpressDeliveryeditor.putString("ExpressDelivery", jobj.getString("ExpressDelivery"));
                                ExpressDeliveryeditor.commit();
                                SharedPreferences ExpressDeliveryAmountpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF40, 0);
                                SharedPreferences.Editor ExpressDeliveryAmounteditor = ExpressDeliveryAmountpref.edit();
                                ExpressDeliveryAmounteditor.putString("ExpressDeliveryAmount", jobj.getString("ExpressDeliveryAmount"));
                                ExpressDeliveryAmounteditor.commit();
                                SharedPreferences HomeIconImageCodepref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF41, 0);
                                SharedPreferences.Editor HomeIconImageCodeeditor = HomeIconImageCodepref.edit();
                                HomeIconImageCodeeditor.putString("HomeIconImageCode", jobj.getString("HomeIconImageCode"));
                                HomeIconImageCodeeditor.commit();
                                SharedPreferences RequiredBranchpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF42, 0);
                                SharedPreferences.Editor RequiredBrancheditor = RequiredBranchpref.edit();
                                RequiredBrancheditor.putString("RequiredBranch", jobj.getString("RequiredBranch"));
                                RequiredBrancheditor.commit();
                                SharedPreferences RequiredInshoppref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF43, 0);
                                SharedPreferences.Editor RequiredInshopeditor = RequiredInshoppref.edit();
                                RequiredInshopeditor.putString("RequiredInshop", jobj.getString("RequiredInshop"));
                                RequiredInshopeditor.commit();
                                Log.e(TAG,"RequiredShoppinglist   2139   "+jobj.getString("RequiredShopingList"));
                                SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
                                SharedPreferences.Editor RequiredShoppinglisteditor = RequiredShoppinglistpref.edit();
                                RequiredShoppinglisteditor.putString("RequiredShoppinglist", jobj.getString("RequiredShopingList") );
                                RequiredShoppinglisteditor.commit();
                                SharedPreferences PlayStoreLinkpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF48, 0);
                                SharedPreferences.Editor PlayStoreLinkeditor = PlayStoreLinkpref.edit();
                                PlayStoreLinkeditor.putString("PlayStoreLink", jobj.getString("PlayStoreLink") );
                                PlayStoreLinkeditor.commit();
                                SharedPreferences HomeBgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF49, 0);
                                SharedPreferences.Editor HomeBgeditor = HomeBgpref.edit();
                                HomeBgeditor.putString("HomeImage", jobj.getString("HomeImage") );
                                HomeBgeditor.commit();
                                SharedPreferences OtpEmailpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF50, 0);
                                SharedPreferences.Editor OtpEmaileditor = OtpEmailpref.edit();
                                OtpEmaileditor.putString("OTPEmailSend", jobj.getString("OTPEmailSend"));
                                OtpEmaileditor.commit();
                                SharedPreferences OTPMsgSendpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF55, 0);
                                SharedPreferences.Editor OTPMsgSendeditor = OTPMsgSendpref.edit();
                                OTPMsgSendeditor.putString("OTPMsgSend", jobj.getString("OTPMsgSend"));
                                OTPMsgSendeditor.commit();

                                //todo privacy policy
                                SharedPreferences privacypolicy = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_PRIVACYPOLICY, 0);
                                SharedPreferences.Editor privacypolicyy = privacypolicy.edit();
                                privacypolicyy.putString("privacypolicy", jobj.getString("PrivacyPolicy"));
                                privacypolicyy.commit();

                                //todo terms and condition
                                SharedPreferences contactDetails = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_CONTACT_US, 0);
                                SharedPreferences.Editor contactDetailss = contactDetails.edit();
                                contactDetailss.putString("ContactDetails", jobj.getString("ContactDetails"));
                                contactDetailss.commit();


                                //todo terms and condition
                                SharedPreferences termsco = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_TERMSCO, 0);
                                SharedPreferences.Editor termscon = termsco.edit();
                                termscon.putString("TermsAndCondition", jobj.getString("TermsAndCondition"));
                                termscon.commit();

                                //todo faq
                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("localpref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                JSONArray jsonfaq = jobj.getJSONArray("MerchantFAQDetails");
                                Log.e("faqqqqq",""+jsonfaq);
                                if (jsonfaq.length() != 0 && !jsonfaq.equals(null)) {
                                    JSONArray FAQARRAY = new JSONArray();
                                    for (int i = 0; i <= jsonfaq.length(); i++) {
                                        try {
                                            JSONObject jobjt = jsonfaq.getJSONObject(i);
                                            String ID_FAQ = jobjt.getString("ID_FAQ");
                                            String Question = jobjt.getString("Question");
                                            String Answer = jobjt.getString("Answer");
                                            try {
                                                JSONObject jsonObject = new JSONObject();
                                                jsonObject.put("ID_FAQ", ID_FAQ);
                                                jsonObject.put("Question", Question);
                                                jsonObject.put("Answer", Answer);
                                                FAQARRAY.put(jsonObject);
                                                Log.e("response1234567", "" + FAQARRAY.toString());
                                                editor.putString("pref_faq", FAQARRAY.toString()).commit();
                                            } catch (JSONException json) {
                                                Log.e("mmmm",""+json);
                                            }
                                        }
                                        catch (Exception e) {
                                            Log.e("mmmm",""+e);
                                        }
                                    }
                                }

                                //todo about us
                                SharedPreferences aboutus = getApplicationContext().getSharedPreferences(Config.SHARED_PREF_ABOUT_US, 0);
                                SharedPreferences.Editor aboutuss = aboutus.edit();
                                aboutuss.putString("AboutUs", jobj.getString("AboutUs"));
                                aboutuss.commit();
                                Log.e(TAG,"Exception   22291   ");
                               // getSplash1();
                                getLanguages();


                                /*startActivity(new Intent(SplashActivity.this,WelcomeActivity.class));
                                finish();*/
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Some technical issues !",Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e) {
                            Log.e(TAG,"Exception   22292   "+e.toString());
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
//                        catch (JSONException e) {
//                            Log.e(TAG,"JSONException   323   "+response.body());
//                            e.printStackTrace();
//                            progressDialog.dismiss();
//                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG,"onFailure   2241   "+t.getMessage());
                        progressDialog.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG,"onFailure   2247   "+e.toString());
            }
        }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }

    private void getLanguages() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = pref.getString("BaseURL", null);
        String imageurl = pref1.getString("ImageURL", null);

        Log.e(TAG,"BaseURL  2262   "+BASEURL);

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
                    requestObject1.put("ReqMode","33");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e(TAG,"requestObject1   2294   "+requestObject1);
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getLanguageList(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        progressDialog.dismiss();

                        try {
                            Log.e(TAG,"onResponse  2302  "+response.body());
                            JSONObject jObject = new JSONObject(response.body());
                            if(jObject.getString("StatusCode").equals("0")) {

                                JSONObject jobj = jObject.getJSONObject("LanguageListInfo");
                                JSONArray jarray = jobj.getJSONArray("LanguageList");

                                Log.e(TAG,"onResponse  2309  "+jarray);

                                SharedPreferences ID_Languages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                                if(ID_Languages.getString("ID_Languages", null) == null){
                                    JSONObject jsonObject=jarray.getJSONObject(0);
                                    SharedPreferences.Editor ID_Languageseditor = ID_Languages.edit();
                                    ID_Languageseditor.putString("ID_Languages", jsonObject.getString("ID_Languages"));
                                    ID_Languageseditor.commit();

                                    Log.e(TAG,"onResponse  2318  "+jsonObject.getString("ID_Languages"));
                                }

                                getSplash1();

                            }else {
                                Toast.makeText(getApplicationContext(),""+jObject.getString("EXMessage"),Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                        progressDialog.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
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

    private SSLSocketFactory getSSLSocketFactory() throws CertificateException, KeyStoreException, IOException,
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

    private void versionCkecking() {
        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        Log.e(TAG,"BASEURL   2417    "+BASEURL);
        Log.e(TAG,"IMAGEURL   2417    "+IMAGEURL);
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
                    requestObject1.put("ReqMode", "24");
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    requestObject1.put("OsType", 0);
                    requestObject1.put("VersionNo", BuildConfig.VERSION_CODE);
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"BASEURL   24440    "+BASEURL);
                    Log.e(TAG,"requestObject1   24441   "+requestObject1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getAppVersionCheck(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            Log.e(TAG,"response   24442    "+response.body());

                            JSONObject jObject = new JSONObject(response.body());
                            String StatusCode = jObject.getString("StatusCode");
                            if(StatusCode.equals("0"))
                            {
                                getResellerDetails();
                            }else{
                                versionUpdation();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(TAG,"JSONException  2467    "+e.toString());
                        }

                    }
                    @Override public void onFailure(Call<String> call, Throwable t) {
                        Log.e(TAG,"onFailure  2472    "+t.getMessage());
                        versionUpdation();


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                versionUpdation();

            }
        }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }

    private void versionUpdation() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SplashActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) SplashActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.version_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.llok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.llcancel);
            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    finish();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }

                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF48, 0);
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(pref.getString("PlayStoreLink", null)));
                    startActivity(intent);
                }
            });
            alertDialog.setCancelable(false);
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getSinglestoreDetails() {
        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(this).isInternetOn()) {
         /*   progressDialog = new ProgressDialog(this, R.style.Progress);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(this.getResources()
                    .getDrawable(R.drawable.progress));
            progressDialog.show();*/
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
                    requestObject1.put("ReqMode","5");
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1  2559  "+requestObject1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getSingleStoreDetails(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            //   progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            Log.e(TAG,"getSinglestoreDetails  onResponse  2571   "+response.body());
                            if(jObject.getString("StatusCode").equals("0")){

                                JSONObject jobj = jObject.getJSONObject("SingleStoreDetails");
                                SharedPreferences homedeliverySP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF27, 0);
                                SharedPreferences.Editor homedeliverySPeditor = homedeliverySP.edit();
                                homedeliverySPeditor.putString("homedelivery", jobj.getString("HomeDelivery"));
                                homedeliverySPeditor.commit();

                                SharedPreferences MinimumDeliveryAmountSP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                                SharedPreferences.Editor MinimumDeliveryAmounteditor = MinimumDeliveryAmountSP.edit();
                                MinimumDeliveryAmounteditor.putString("MinimumDeliveryAmount", jobj.getString("MinimumDeliveryAmount"));
                                MinimumDeliveryAmounteditor.commit();

                                SharedPreferences DeliveryCriteriaSP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                                SharedPreferences.Editor DeliveryCriteriaeditor = DeliveryCriteriaSP.edit();
                                DeliveryCriteriaeditor.putString("DeliveryCriteria", jobj.getString("DeliveryCriteria"));
                                DeliveryCriteriaeditor.commit();

                                SharedPreferences ID_Store = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                                SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                ID_Storeeditor.putString("ID_Store", jobj.getString("ID_Store"));
                                ID_Storeeditor.commit();

                                SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                StoreNameeditor.putString("StoreName", jobj.getString("StoreName"));
                                StoreNameeditor.commit();

                                SharedPreferences ExpressDeliverypref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF39, 0);
                                SharedPreferences.Editor ExpressDeliveryeditor = ExpressDeliverypref.edit();
                                ExpressDeliveryeditor.putString("ExpressDelivery", jobj.getString("ExpressDelivery"));
                                ExpressDeliveryeditor.commit();

                                SharedPreferences ExpressDeliveryAmountpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF40, 0);
                                SharedPreferences.Editor ExpressDeliveryAmounteditor = ExpressDeliveryAmountpref.edit();
                                ExpressDeliveryAmounteditor.putString("ExpressDeliveryAmount", jobj.getString("ExpressDeliveryAmount"));
                                ExpressDeliveryAmounteditor.commit();

                                SharedPreferences Requiredcounterpickuppref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF44, 0);
                                SharedPreferences.Editor Requiredcounterpickupeditor = Requiredcounterpickuppref.edit();
                                Requiredcounterpickupeditor.putString("Requiredcounterpickup", jobj.getString("CounterDelivery"));
                                Requiredcounterpickupeditor.commit();

                                SharedPreferences CategoryListpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF51, 0);
                                SharedPreferences.Editor CategoryListeditor = CategoryListpref.edit();
                                CategoryListeditor.putString("CategoryList", jobj.getString("CategoryList"));
                                CategoryListeditor.commit();

                                SharedPreferences SubCategoryListpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF52, 0);
                                SharedPreferences.Editor SubCategoryListeditor = SubCategoryListpref.edit();
                                SubCategoryListeditor.putString("SubCategoryList", jobj.getString("SubCategoryList"));
                                SubCategoryListeditor.commit();

                                SharedPreferences CashOnDeliverypref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF53, 0);
                                SharedPreferences.Editor CashOnDeliveryeditor = CashOnDeliverypref.edit();
                                CashOnDeliveryeditor.putString("CashOnDelivery", jobj.getString("CashOnDelivery"));
                                CashOnDeliveryeditor.commit();

                                SharedPreferences OnlinePaymentpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF54, 0);
                                SharedPreferences.Editor OnlinePaymenteditor = OnlinePaymentpref.edit();
                                OnlinePaymenteditor.putString("OnlinePayment", jobj.getString("OnlinePayment"));
                                OnlinePaymenteditor.commit();

                                Log.e(TAG,"OnlinePayment  2635    "+ jobj.getString("OnlinePayment"));

                                SharedPreferences OnlinePaymentmeth = getApplicationContext().getSharedPreferences(Config.SHARED_PREF62, 0);
                                SharedPreferences.Editor OnlinePaymentmetheditor = OnlinePaymentmeth.edit();
                                OnlinePaymentmetheditor.putString("OnlinePaymentMethods", jobj.getString("OnlinePaymentMethods"));
                                OnlinePaymentmetheditor.commit();


                                SharedPreferences TimeSlotCheckpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF60, 0);
                                SharedPreferences.Editor TimeSlotCheckeditor = TimeSlotCheckpref.edit();
                                TimeSlotCheckeditor.putString("TimeSlotCheck", jobj.getString("TimeSlotCheck"));
                                TimeSlotCheckeditor.commit();

                                SharedPreferences UPIIDpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF59, 0);
                                SharedPreferences.Editor UPIIDeditor = UPIIDpref.edit();
                                UPIIDeditor.putString("UPIID", jobj.getString("UPIID"));
                                UPIIDeditor.commit();


                                SharedPreferences MerchantKey = getApplicationContext().getSharedPreferences(Config.SHARED_PREF340, 0);
                                SharedPreferences.Editor MerchantKeyeditor = MerchantKey.edit();
                                MerchantKeyeditor.putString("MerchantKey", jobj.getString("MerchantKey"));
                                MerchantKeyeditor.commit();

                                SharedPreferences SaltKey = getApplicationContext().getSharedPreferences(Config.SHARED_PREF341, 0);
                                SharedPreferences.Editor SaltKeyeditor = SaltKey.edit();
                                SaltKeyeditor.putString("SaltKey", jobj.getString("SaltKey"));
                                SaltKeyeditor.commit();

                                SharedPreferences sMobile = getApplicationContext().getSharedPreferences(Config.SHARED_PREF342, 0);
                                SharedPreferences.Editor sMobileeditor = sMobile.edit();
                                sMobileeditor.putString("Mobile", jobj.getString("Mobile"));
                                sMobileeditor.commit();

                                SharedPreferences BranchEmail = getApplicationContext().getSharedPreferences(Config.SHARED_PREF343, 0);
                                SharedPreferences.Editor BranchEmaileditor = BranchEmail.edit();
                                BranchEmaileditor.putString("BranchEmail", jobj.getString("BranchEmail"));
                                BranchEmaileditor.commit();


                                SharedPreferences ScratchCard = getApplicationContext().getSharedPreferences(Config.SHARED_PREF382, 0);
                                SharedPreferences.Editor ScratchCardeditor = ScratchCard.edit();
                                ScratchCardeditor.putString("ScratchCard", jobj.getString("GiftVoucher"));
                                ScratchCardeditor.commit();

                                SharedPreferences PrivilageCardEnable = getApplicationContext().getSharedPreferences(Config.SHARED_PREF429, 0);
                                SharedPreferences.Editor PrivilageCardEnableeditor = PrivilageCardEnable.edit();
                                PrivilageCardEnableeditor.putString("PrivilageCardEnable", jobj.getString("PrivilageCardEnable"));
                                PrivilageCardEnableeditor.commit();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("localpref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                JSONArray jsonpayment = jobj.getJSONArray("OnlinePaymentMethods");

                                if (jsonpayment.length() != 0 && !jsonpayment.equals(null)) {
                                    JSONArray PAYARRAY = new JSONArray();
                                    for (int i = 0; i <= jsonpayment.length(); i++) {
                                        try {
                                            JSONObject jobjt = jsonpayment.getJSONObject(i);
                                            String id = jobjt.getString("ID_PaymentMethod");
                                            String PaymentName = jobjt.getString("PaymentName");
                                            try {
                                                JSONObject jsonObject = new JSONObject();
                                                jsonObject.put("id", id);
                                                jsonObject.put("PaymentName", PaymentName);
                                                PAYARRAY.put(jsonObject);
                                                Log.e("response1234567", "" + PAYARRAY.toString());
                                                editor.putString("pref_data", PAYARRAY.toString()).commit();
                                            } catch (JSONException json) {
                                                Log.e("mmmm",""+json);
                                            }
                                        } catch (Exception e) {
                                            Log.e("mmmm",""+e);
                                        }

                                    }
                                }

//                                SharedPreferences OnlinePaymentpref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF54, 0);
//                                SharedPreferences OnlinePaymentmeth1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF62, 0);
//                                String BASEURL = OnlinePaymentmeth1.getString("OnlinePaymentMethods", null);
//                                Log.e(TAG,"BASEURL   2282    "+BASEURL + "    "+OnlinePaymentpref1.getString("OnlinePayment", null));


//                                Log.e(TAG,"2514    "+singlestore);
//                                if(singlestore.equals("1")) {
                                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                                    startActivity(intent);
                               // }
                            }
                            else{
                                SharedPreferences Somethingwentwrongsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF72, 0);
                                String Somethingwentwrong = Somethingwentwrongsp.getString("Somethingwentwrong", "");
                                Toast.makeText(getApplicationContext(),Somethingwentwrong+" !",Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //  progressDialog.dismiss();
                            Log.e(TAG,"Exception    2694    "+e.toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        //    progressDialog.dismiss();
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

}
