package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Activity.HomeActivity;
import com.perfect.easyshopplus.Activity.LocationActivity;
import com.perfect.easyshopplus.Activity.NoInternetActivity;
import com.perfect.easyshopplus.Activity.NotificationDetailActivity;
import com.perfect.easyshopplus.Activity.SplashActivity;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.ItemClickListener;
import com.perfect.easyshopplus.Utility.ScratchCard;

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
import java.text.DecimalFormat;
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

public class ScratchcardAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    int notificationcount=0;
    private Date date, time;
    private ItemClickListener clickListener;



    public ScratchcardAdapter(Context context, JSONArray jsonArray) {
        this.context=context;
        this.jsonArray=jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_scratched_not, parent, false);
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

                SharedPreferences ScratchWin = context.getSharedPreferences(Config.SHARED_PREF401, 0);
                ((MainViewHolder) holder).tv_scratchwin.setText(ScratchWin.getString("ScratchWin",""));

                if (jsonObject.getString("CRAmount").equals("0.00")){
                    SharedPreferences betterlucknexttime = context.getSharedPreferences(Config.SHARED_PREF400, 0);
                    ((MainViewHolder)holder).codeTxt.setText(""+betterlucknexttime.getString("betterlucknexttime",""));
                    ((MainViewHolder)holder).img_scratch.setBackground(context.getResources().getDrawable(R.drawable.betterluck));
                }else {
                    ((MainViewHolder)holder).codeTxt.setText(jsonObject.getString("CRAmount"));
                    ((MainViewHolder)holder).img_scratch.setBackground(context.getResources().getDrawable(R.drawable.trophy));
                }

//                ((MainViewHolder)holder).codeTxt.setText(jsonObject.getString("CRAmount"));
                ((MainViewHolder)holder).lnLayout.setTag(position);
                ((MainViewHolder)holder).lnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                        jsonObject = jsonArray.getJSONObject(position);

                        String CRAmount = jsonObject.getString("CRAmount");
                        String ID_CustomerReward = jsonObject.getString("ID_CustomerReward");
                         //   Toast.makeText(context.getApplicationContext(), "Do call API"+ position,Toast.LENGTH_LONG).show();
                            ScratchPop(CRAmount,ID_CustomerReward,position);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                ((MainViewHolder)holder).mScratchCard.setOnScratchListener(new ScratchCard.OnScratchListener() {
                    @Override
                    public void onScratch(ScratchCard scratchCard, float visiblePercent) {



                        if (visiblePercent>=0.5) {
                            try {
                                jsonObject=jsonArray.getJSONObject(position);
                                Log.e("ScratchCard","ScratchCard  114    "+scratchCard+"   "+visiblePercent ) ;
                                Log.e("Reveal Percentage", "114 onRevealPercentChangedListener: " + String.valueOf(visiblePercent));
                                Log.e("ScratchCard","ScratchCard  114    "+jsonObject.getString("CRAmount") );
                                clickListener.onClick(position,jsonObject.getString("ID_CustomerReward"));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        ;

                        }
                    }
                });


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void ScratchPop(String CRAmount,String ID_CustomerReward, int position) {

        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.pop_scratched_not, null);

            TextView srt_codeTxt =  (TextView) layout.findViewById(R.id.codeTxt);
            TextView srt_tv_scratchwin =  (TextView) layout.findViewById(R.id.tv_scratchwin);
            ScratchCard srt_mScratchCard =  (ScratchCard) layout.findViewById(R.id.scratchCard);
            ImageView srt_img_scratch =  (ImageView) layout.findViewById(R.id.img_scratch);


            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.setView(layout, 0, 0, 0, 0);

            if (CRAmount.equals("0.00")){
                SharedPreferences betterlucknexttime = context.getSharedPreferences(Config.SHARED_PREF400, 0);
                srt_codeTxt.setText(""+betterlucknexttime.getString("betterlucknexttime",""));
                srt_img_scratch.setBackground(context.getResources().getDrawable(R.drawable.betterluck));
            }else {
                DecimalFormat f = new DecimalFormat("##.00");
                srt_codeTxt.setText(f.format(Double.parseDouble(jsonObject.getString("CRAmount"))));
                srt_img_scratch.setBackground(context.getResources().getDrawable(R.drawable.trophy));
            }

            srt_mScratchCard.setOnScratchListener(new ScratchCard.OnScratchListener() {
                @Override
                public void onScratch(ScratchCard scratchCard, float visiblePercent) {



                    if (visiblePercent>=0.5) {
                        try {
                           // jsonObject=jsonArray.getJSONObject(position);
                            Log.e("ScratchCard","ScratchCard  114    "+scratchCard+"   "+visiblePercent ) ;
                            Log.e("Reveal Percentage", "114 onRevealPercentChangedListener: " + String.valueOf(visiblePercent));
                          //  Log.e("ScratchCard","ScratchCard  114    "+jsonObject.getString("CRAmount") );
                            clickListener.onClick(position,ID_CustomerReward);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ;

                    }
                }
            });

            alertDialog.show();
        } catch (Exception e) {
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


    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    private class MainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout lnLayout;

        TextView codeTxt,tv_scratchwin;
        ScratchCard mScratchCard;
        ImageView img_scratch;
        public MainViewHolder(View v) {
            super(v);
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            codeTxt=(TextView)v.findViewById(R.id.codeTxt);
            mScratchCard = v.findViewById(R.id.scratchCard);
            img_scratch = v.findViewById(R.id.img_scratch);
            tv_scratchwin = (TextView)v.findViewById(R.id.tv_scratchwin);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

          // if (clickListener != null) clickListener.onClick(getAdapterPosition());

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


}
