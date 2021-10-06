package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Activity.NoInternetActivity;
import com.perfect.easyshopplus.Activity.NotificationDetailActivity;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

public class NotificationAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    int notificationcount=0;
    private Date date, time;

    public NotificationAdapter(Context context, JSONArray jsonArray) {
        this.context=context;
        this.jsonArray=jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_not_list, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {

            SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
            SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
            final String BASEURL = baseurlpref.getString("BaseURL", null);
            String IMAGEURL = imgpref.getString("ImageURL", null);
            jsonObject=jsonArray.getJSONObject(position);
            if (holder instanceof MainViewHolder) {
                String IsRead=jsonObject.getString("IsRead");
                if(IsRead.equals("false")){
                  //  ((MainViewHolder)holder).lnLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                    ((MainViewHolder)holder).tvnew.setVisibility(View.VISIBLE);
                }else{
                  //  ((MainViewHolder)holder).lnLayout.setBackgroundColor(context.getResources().getColor(R.color.greylight1));
                    ((MainViewHolder)holder).tvnew.setVisibility(View.INVISIBLE);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm a");
                try {
                    date=new SimpleDateFormat("dd-MM-yyyy").parse(jsonObject.getString("EntryDate"));
                    time=new SimpleDateFormat("hh:mm:ss").parse(jsonObject.getString("EntryTime"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final String strDate = sdf.format(date);
                final String strTime = sdf1.format(time);
                ((MainViewHolder)holder).tvDate.setText(strDate);
                ((MainViewHolder)holder).tvHeader.setText(jsonObject.getString("Title"));
                ((MainViewHolder)holder).lnLayout.setTag(position);
                ((MainViewHolder)holder).lnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                        jsonObject = jsonArray.getJSONObject(position);
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
                                        SharedPreferences pref = context.getSharedPreferences(Config.SHARED_PREF1, 0);
                                        requestObject1.put("ReqMode", "6");
                                        requestObject1.put("ID_Customer", pref.getString("userid", null));
                                        requestObject1.put("UserAction", "1");
                                        requestObject1.put("ID_Notification", jsonObject.getString("ID_Notification"));
                                        requestObject1.put("Bank_Key", context.getResources().getString(R.string.BankKey));
                                        SharedPreferences IDLanguages = context.getSharedPreferences(Config.SHARED_PREF80, 0);
                                        requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                                    Call<String> call = apiService.getNotification(body);
                                    call.enqueue(new Callback<String>() {
                                        @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                                            try {
                                                JSONObject jObject = new JSONObject(response.body());
                                                JSONObject jmember = jObject.getJSONObject("NotificationDetailsInfo");
                                                String ResponseCode = jmember.getString("ResponseCode");
                                                if(ResponseCode.equals("0")) {
                                                  //  ((MainViewHolder) holder).lnLayout.setBackgroundColor(context.getResources().getColor(R.color.greylight1));
                                                    ((MainViewHolder) holder).tvnew.setVisibility(View.INVISIBLE);
                                                    setNptifications();
                                                }else{
                                                  //  ((MainViewHolder) holder).lnLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                                                    ((MainViewHolder) holder).tvnew.setVisibility(View.VISIBLE);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<String> call, Throwable t) {
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            Intent i = new Intent(context, NotificationDetailActivity.class);
                            i.putExtra("NotificationTitle", jsonObject.getString("Title"));
                            i.putExtra("NotificationDetail", jsonObject.getString("Description"));
                            i.putExtra("NotificationDate",strDate);
                            i.putExtra("NotificationTime",strTime);
                            context.startActivity(i);
                            ((Activity)context).finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.length();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return position % 2;
    }

    private class MainViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lnLayout;
        TextView tvHeader, tvnew, tvDate;
        public MainViewHolder(View v) {
            super(v);
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            tvHeader=(TextView)v.findViewById(R.id.tvHeader);
            tvnew=(TextView)v.findViewById(R.id.tvnew);
            tvDate=(TextView)v.findViewById(R.id.tvDate);
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
        InputStream caInput = context.getResources().openRawResource(R.raw.my_cert); // File path: app\src\main\res\raw\your_cert.cer
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

    private void setNptifications() {
        SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(context).isInternetOn()) {
        try {
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
                    SharedPreferences pref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                    requestObject1.put("ReqMode", "6");
                    requestObject1.put("ID_Customer", pref.getString("userid", null));
                    requestObject1.put("UserAction", "0");
                    requestObject1.put("Bank_Key", context.getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = context.getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getNotification(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("NotificationDetailsInfo");
                            if (jobj.getString("NotificationDetails").equals("null")) {
                            }else {
                                JSONArray jarray = jobj.getJSONArray("NotificationDetails");
                                for(int i=0; i<=jarray.length(); i++){
                                    JSONObject jobjt = jarray.getJSONObject(i);
                                    if(jobjt.getString("IsRead").equals("false")){
                                        notificationcount += 1;
                                    }
                                    SharedPreferences spnotificationcount = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
                                    SharedPreferences.Editor notificationcounteditor1 = spnotificationcount.edit();
                                    notificationcounteditor1.putString("notificationcount", String.valueOf(notificationcount));
                                    notificationcounteditor1.commit();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();

            SharedPreferences Somethingwentwrongsp = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF72, 0);
            String Somethingwentwrong = Somethingwentwrongsp.getString("Somethingwentwrong", "");
            Toast.makeText(context.getApplicationContext(),Somethingwentwrong+"!",Toast.LENGTH_LONG).show();
        }
        }else {
            Intent in = new Intent(context, NoInternetActivity.class);
            context.startActivity(in);
        }
    }

}
