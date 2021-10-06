package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Activity.InShopActivity;
import com.perfect.easyshopplus.Activity.NoInternetActivity;
import com.perfect.easyshopplus.Activity.StoreInShopActivity;
import com.perfect.easyshopplus.DB.DBHandler;
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

public class StoreInShopAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    ProgressDialog progressDialog;

    public StoreInShopAdapter(Context context, JSONArray jsonArray) {
        this.context=context;
        this.jsonArray=jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.store_instore_list, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            jsonObject=jsonArray.getJSONObject(position);
            if (holder instanceof MainViewHolder) {
                SharedPreferences MinDeliveryAmt = context.getSharedPreferences(Config.SHARED_PREF277, 0);

                String string = "\u20B9";
                ((MainViewHolder)holder).tvPrdName.setText(jsonObject.getString("StoreName"));
                ((MainViewHolder)holder).tvAddress.setText(jsonObject.getString("Address"));
                DecimalFormat f = new DecimalFormat("##.00");
//                ((MainViewHolder)holder).tvMinamt.setText("Min. Delivery Amt : "+/*string+" "+*/f.format(Double.parseDouble(jsonObject.getString("MinimumDeliveryAmount"))));
                ((MainViewHolder) holder).tvMinamt.setText(""+MinDeliveryAmt.getString("MinDeliveryAmt", null)+" : "  + /*string + " " + */f.format(Double.parseDouble(jsonObject.getString("MinimumDeliveryAmount"))));

                ((MainViewHolder)holder).tvtime.setText(jsonObject.getString("WorkingHours"));
                if(jsonObject.getString("StoreRate").isEmpty()){
                    ((MainViewHolder) holder).txtvRating.setVisibility(View.GONE);
                }else{
                    ((MainViewHolder)holder).txtvRating.setText(jsonObject.getString("StoreRate"));
                }
                if(jsonObject.getString("DeliveryCriteria").isEmpty()){
                    ((MainViewHolder) holder).tvnote.setVisibility(View.GONE);
                }else{
                    ((MainViewHolder) holder).tvnote.setText(jsonObject.getString("DeliveryCriteria"));
                }
                if(jsonObject.getString("Mobile").isEmpty()){}else {
                    SharedPreferences PhoneNo = context.getSharedPreferences(Config.SHARED_PREF276, 0);
//                    ((MainViewHolder) holder).tvMob.setText("Phone No. : "+jsonObject.getString("Mobile"));
                    ((MainViewHolder) holder).tvMob.setText(""+PhoneNo.getString("PhoneNo", null)+" : "+jsonObject.getString("Mobile"));
                }
           /*     if(jsonObject.getString("HomeDelivery").equals("false")){
                    SharedPreferences homedeliverySP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF27, 0);
                    SharedPreferences.Editor homedeliverySPeditor = homedeliverySP.edit();
                    homedeliverySPeditor.putString("homedelivery", jsonObject.getString("HomeDelivery"));
                    homedeliverySPeditor.commit();
                    ((MainViewHolder)holder).ivHomedelivery.setVisibility(View.INVISIBLE);
                }else {
                    SharedPreferences homedeliverySP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF27, 0);
                    SharedPreferences.Editor homedeliverySPeditor = homedeliverySP.edit();
                    homedeliverySPeditor.putString("homedelivery", jsonObject.getString("HomeDelivery"));
                    homedeliverySPeditor.commit();
                    ((MainViewHolder)holder).ivHomedelivery.setVisibility(View.VISIBLE);
                }*/
                if(jsonObject.getString("HomeDelivery").equals("false")){
                    ((MainViewHolder) holder).tvMinamt.setVisibility(View.GONE);
                    ((MainViewHolder)holder).ivHomedelivery.setVisibility(View.INVISIBLE);
                }else {
//                    ((MainViewHolder) holder).tvMinamt.setText("Min. Delivery Amt : " + /*string + " " + */f.format(Double.parseDouble(jsonObject.getString("MinimumDeliveryAmount"))));
                    ((MainViewHolder) holder).tvMinamt.setText(""+MinDeliveryAmt.getString("MinDeliveryAmt", null)+" : "  + /*string + " " + */f.format(Double.parseDouble(jsonObject.getString("MinimumDeliveryAmount"))));
                    ((MainViewHolder)holder).ivHomedelivery.setVisibility(View.VISIBLE);
                }
                SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
                SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
                String BASEURL = baseurlpref.getString("BaseURL", null);
                String IMAGEURL = imgpref.getString("ImageURL", null);
                String imagepath=IMAGEURL + jsonObject.getString("ImagePath");


                PicassoTrustAll.getInstance(context).load(imagepath).error(R.drawable.store).into(((MainViewHolder) holder).iv_itemimage);
               /* Glide.with(context)
                        .load(imagepath)
                        .placeholder(R.drawable.store)
                        .error(R.drawable.store)
                        .into(((MainViewHolder) holder).iv_itemimage);*/

                ((MainViewHolder)holder).lnLayout.setTag(position);
                ((MainViewHolder)holder).lnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences pref3 = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF10, 0);
                        SharedPreferences pref4 = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF11, 0);
                        SharedPreferences GPSisdisabledinyourdeviceYouShouldenableittoproceedyourpurchase = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF293, 0);
                        SharedPreferences OKsp = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
                        String OK = OKsp.getString("OK","");
                        SharedPreferences sCancel = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF105, 0);
                        String Cancel = sCancel.getString("Cancel","");

                        if((pref3.getString("Latitude", null)== null&&pref4.getString("Longitude", null)==null)||(pref3.getString("Latitude", null).equals("")&&pref4.getString("Longitude", null).equals(""))){
                            AlertDialog.Builder builder= new AlertDialog.Builder(context);
                            builder.setMessage(GPSisdisabledinyourdeviceYouShouldenableittoproceedyourpurchase.getString("GPSisdisabledinyourdeviceYouShouldenableittoproceedyourpurchase","")+"?")
//                            builder.setMessage("GPS is disabled in your device. You Should enable it to proceed your purchase?")
                                    .setCancelable(false)
                                    .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            context.startActivity(new Intent(context, StoreInShopActivity.class));
                                            ((Activity)context).finish();
                                           dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton(Cancel, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }else{
                            try {
                                jsonObject = jsonArray.getJSONObject(position);
                                final SharedPreferences ID_Store = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF12, 0);
                                final String selectedStore = jsonObject.getString("ID_Store");
                                if(ID_Store.getString("ID_Store_Inshop", null) == null ||ID_Store.getString("ID_Store_Inshop", null).equals("")||  ID_Store.getString( "ID_Store_Inshop", null).equals(selectedStore))  {
                                    getLocation(selectedStore,ID_Store, "false");
                                }
                                else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    View layout = inflater1.inflate(R.layout.warning_popup, null);
                                    LinearLayout ll_cancel = (LinearLayout) layout.findViewById(R.id.ll_cancel);
                                    LinearLayout ll_ok = (LinearLayout) layout.findViewById(R.id.ll_ok);
                                    builder.setView(layout);
                                    final AlertDialog alertDialog = builder.create();
                                    ll_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            alertDialog.dismiss();
                                        }
                                    });
                                    ll_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            getLocation(selectedStore,ID_Store,"true");
                                            DBHandler db = new DBHandler(context);
                                            db.deleteallInshopCart();
                                            alertDialog.dismiss();
                                        }
                                    });
                                    alertDialog.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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
        TextView tvPrdName,tvAddress,tvMinamt,tvtime,tvMob, tvnote, txtvRating;
        ImageView iv_itemimage, ivFavourates, ivHomedelivery;
        public MainViewHolder(View v) {
            super(v);
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            iv_itemimage=(ImageView)v.findViewById(R.id.iv_itemimage);
            ivHomedelivery=(ImageView)v.findViewById(R.id.ivHomedelivery);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            tvAddress=(TextView)v.findViewById(R.id.tvAddress);
            tvMinamt=(TextView)v.findViewById(R.id.tvMinamt);
            tvtime=(TextView)v.findViewById(R.id.tvtime);
            tvMob=(TextView)v.findViewById(R.id.tvMob);
            tvnote=(TextView)v.findViewById(R.id.tvnote);
            txtvRating=(TextView)v.findViewById(R.id.txtvRating);
        }
    }

    private void getLocation(String storeID, final SharedPreferences ID_Store , final String clearDB ) {
        SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        SharedPreferences SomethingwentwrongTryagainlater = context.getSharedPreferences(Config.SHARED_PREF280, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(context).isInternetOn()) {
        try {
            progressDialog = new ProgressDialog(context, R.style.Progress);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(context.getApplicationContext().getResources()
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
                    requestObject1.put("ReqMode", "9");
                    requestObject1.put("ID_Store", storeID);
                    requestObject1.put("Bank_Key", context.getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = context.getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getStoresLocation(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("StoreLocationInfo");
                            String strLattitude = jobj.getString("Lattitude");
                            String strLongitude = jobj.getString("Longitude");
                            if((strLattitude.equals("null")&&strLongitude.equals("null"))||(strLattitude.equals("")&&strLongitude.equals(""))){

                                SharedPreferences Storelocationnotfoundsp = context.getSharedPreferences(Config.SHARED_PREF286, 0);
                                Toast.makeText(context,Storelocationnotfoundsp.getString("Storelocationnotfound", null),Toast.LENGTH_LONG).show();
//                                Toast.makeText(context,"Store location not found",Toast.LENGTH_LONG).show();
                            }else{
                            SharedPreferences preflt = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF10, 0);
                            SharedPreferences preflg = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF11, 0);
                            String stlat2=preflt.getString("Latitude", null);
                            String stlng2=preflg.getString("Longitude", null);
                                double lat2 = Double.parseDouble(preflt.getString("Latitude", null));
                                double lng2 = Double.parseDouble(preflg.getString("Longitude", null));
                                double lat1 = new Double(strLattitude);
                                double lng1 = new Double(strLongitude);
                                if (distance(lat1, lng1, lat2, lng2) < 1) {
                                    if (clearDB.equals("true")) {
                                        SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                        try {
                                            ID_Storeeditor.putString("ID_Store_Inshop", jsonObject.getString("ID_Store"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        ID_Storeeditor.commit();
                                        SharedPreferences StoreName = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF13, 0);
                                        SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                        try {
                                            StoreNameeditor.putString("StoreName_Inshop", jsonObject.getString("StoreName"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        StoreNameeditor.commit();
                                        Intent intent = new Intent(context, InShopActivity.class);
                                        context.startActivity(intent);
                                    } else {
                                        SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                        ID_Storeeditor.putString("ID_Store_Inshop", jsonObject.getString("ID_Store"));
                                        ID_Storeeditor.commit();
                                        SharedPreferences StoreName = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF13, 0);
                                        SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                        StoreNameeditor.putString("StoreName_Inshop", jsonObject.getString("StoreName"));
                                        StoreNameeditor.commit();
                                        Intent intent = new Intent(context, InShopActivity.class);
                                        context.startActivity(intent);
                                        ((Activity)context).finish();
                                    }
                                }
                                else {
                                    SharedPreferences OKsp = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
                                    String OK = OKsp.getString("OK","");
                                    SharedPreferences Youarenotinsideornearthestoresoyoucantselectinshoppleaseselectoutshopforpurchasesp = context.getSharedPreferences(Config.SHARED_PREF285, 0);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    builder.setMessage(Youarenotinsideornearthestoresoyoucantselectinshoppleaseselectoutshopforpurchasesp.getString("Youarenotinsideornearthestoresoyoucantselectinshoppleaseselectoutshopforpurchase", null)+".")
                                            .setCancelable(false)
                                            .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                }
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();

                            Toast.makeText(context.getApplicationContext(),""+SomethingwentwrongTryagainlater.getString("SomethingwentwrongTryagainlater", null),Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context.getApplicationContext(),""+SomethingwentwrongTryagainlater.getString("SomethingwentwrongTryagainlater", null),Toast.LENGTH_LONG).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context.getApplicationContext(),""+SomethingwentwrongTryagainlater.getString("SomethingwentwrongTryagainlater", null),Toast.LENGTH_LONG).show();
            }
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context.getApplicationContext(),""+SomethingwentwrongTryagainlater.getString("SomethingwentwrongTryagainlater", null),Toast.LENGTH_LONG).show();
        }
    }else {
        Intent in = new Intent(context, NoInternetActivity.class);
        context.startActivity(in);
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
        InputStream caInput = context.getApplicationContext().getResources().openRawResource(R.raw.my_cert); // File path: app\src\main\res\raw\your_cert.cer
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

    private double distance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;
        return dist;
    }

}
