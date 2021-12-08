package com.perfect.easyshopplus.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.perfect.easyshopplus.Activity.AddressAddActivty;
import com.perfect.easyshopplus.Activity.CartActivity;
import com.perfect.easyshopplus.Activity.CheckoutActivity;
import com.perfect.easyshopplus.Activity.FavouriteActivity;
import com.perfect.easyshopplus.Activity.HomeActivity;
import com.perfect.easyshopplus.Activity.OutShopActivity;
import com.perfect.easyshopplus.Activity.PrescriptionUploadActivity;
import com.perfect.easyshopplus.Activity.TermsnConditionActivity;
import com.perfect.easyshopplus.Adapter.OutShopCartAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.CartModel;
import com.perfect.easyshopplus.Model.CheckCartModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

public class OutShopFragment extends Fragment implements CartChangedListener {

    ProgressDialog progressDialog;
    ArrayList<CartModel> cartlist = new ArrayList<>();
    ArrayList<CheckCartModel> checkcartlist = new ArrayList<>();
    DBHandler db;
    RecyclerView rv_cartlist;
    TextView bttoalamount, btcheckout, tvclear, bttoalsvdamount;
    TextView tvCartCount, store_tv,tv_cartisempty, tv_additemstoitnow;
    LinearLayout emptyCart, bottom_navigation, lvTopCartCount;
    Button btShopNow;
    float subtotal, totalgst, aamountPay, totalMRP, totalRetailPrice, discount, memberdiscount, yousaved;
    public static CartChangedListener cartChangedListener = null;
    String strFrom , OK;
    int currentPosition;
    double doubleTotalAmt;

    String TAG = "OutShopFragment";
    BottomNavigationView navigation;
    String fromCategory = "from";
    String valueCategory = "home";
    String fromCart = "From";
    String valueCart = "Home";
    String fromHome = "";
    String valueHome = "";
    String fromFavorite = "From";
    String valueFavorite = "Home";
    private File fileimage = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CartActivity activity = (CartActivity) getActivity();
        strFrom = activity.getMyData();
        currentPosition =activity.getcurrentPosition();
        View v = inflater.inflate(R.layout.activity_outshop, container,
                false);

        Log.e(TAG,"Start   120   ");

        SharedPreferences OKsp = getContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        OK = (OKsp.getString("OK", ""));

        SharedPreferences pref4 = getContext().getApplicationContext().getSharedPreferences(Config.SHARED_PREF38, 0);
        ImageView imLogo=v.findViewById(R.id.imLogo);
        SharedPreferences baseurlpref = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        String strImagepath1= IMAGEURL + pref4.getString("LogoImageCode", null);
        //PicassoTrustAll.getInstance(getContext()).load(strImagepath1).into(imLogo);
        Glide.with( this ).load( R.drawable.emptycartgf ).into( imLogo );

        rv_cartlist=(RecyclerView)v.findViewById(R.id.rv_cartlist);
        bttoalamount=(TextView)v.findViewById(R.id.bttoalamount);
        store_tv=(TextView)v.findViewById(R.id.store_tv);
        tv_additemstoitnow=(TextView)v.findViewById(R.id.tv_additemstoitnow);
        tv_cartisempty=(TextView)v.findViewById(R.id.tv_cartisempty);
        btcheckout=(TextView)v.findViewById(R.id.btcheckout);
        tvclear=(TextView)v.findViewById(R.id.tvclear);
        emptyCart=(LinearLayout)v.findViewById(R.id.emptyCart);
        btShopNow=(Button) v.findViewById(R.id.btShopNow);
        bttoalsvdamount=(TextView)v.findViewById(R.id.bttoalsvdamount);
        tvCartCount=(TextView)v.findViewById(R.id.tvCartCount);
        bottom_navigation=(LinearLayout)v.findViewById(R.id.change_location_bottom_navigation);
        lvTopCartCount=(LinearLayout)v.findViewById(R.id.lvTopCartCount);
        setBottomBar(v);
        getAllcart();


        SharedPreferences proceed = getContext().getSharedPreferences(Config.SHARED_PREF132, 0);
        btcheckout.setText(proceed.getString("proceed",""));
        SharedPreferences clearall = getContext().getSharedPreferences(Config.SHARED_PREF128, 0);
        tvclear.setText(clearall.getString("clearall",""));
        SharedPreferences Store = getContext().getSharedPreferences(Config.SHARED_PREF119, 0);
        SharedPreferences pref1 = getContext().getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
        if(pref1.getString("StoreName", "") != null) {
            if(!pref1.getString("StoreName", "").isEmpty()) {
                store_tv.setText(Store.getString("Store", "") + " : " + pref1.getString("StoreName", null));
            }
        }

        SharedPreferences yourcartisempty = getContext().getSharedPreferences(Config.SHARED_PREF125, 0);
        tv_cartisempty.setText(yourcartisempty.getString("yourcartisempty", ""));

        Log.e(TAG,"yourcartisempty   "+yourcartisempty.getString("yourcartisempty", ""));


        SharedPreferences additemstoitnow = getContext().getSharedPreferences(Config.SHARED_PREF126, 0);
        tv_additemstoitnow.setText(additemstoitnow.getString("additemstoitnow", ""));
        SharedPreferences shopnow = getContext().getSharedPreferences(Config.SHARED_PREF127, 0);
        btShopNow.setText(shopnow.getString("shopnow", ""));



        db = new DBHandler(getContext());
        String string = "\u20B9";
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {e.printStackTrace();}
        float totalAmount=db.selectCartTotalActualPrice();
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

        SharedPreferences totalamount = getContext().getSharedPreferences(Config.SHARED_PREF131, 0);
        bttoalamount.setText(totalamount.getString("totalamount", "")+" : "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        SharedPreferences youhavesaved = getContext().getSharedPreferences(Config.SHARED_PREF212, 0);
        bttoalsvdamount.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");
        btcheckout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (db.checkCartCount() == true){

                    SharedPreferences Pleaseupdateallitemquantity = getContext().getSharedPreferences(Config.SHARED_PREF256, 0);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(Pleaseupdateallitemquantity.getString("Pleaseupdateallitemquantity","")+" !")
                            .setCancelable(false)
                            .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                    // Toast.makeText(getContext().getApplicationContext(),"Please update all item quantity!",Toast.LENGTH_LONG).show();
                }
                else{
                    getprescription(doubleTotalAmt);
                }
               /* SharedPreferences pref3 = getContext().getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                String strMinimumDeliveryAmount = pref3.getString("MinimumDeliveryAmount", null);
                if (Double.parseDouble(strMinimumDeliveryAmount) > OutShopFragment.this.doubleTotalAmt) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Minimum amount required for Home delivery is " + strMinimumDeliveryAmount)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    Intent intent = new Intent(getContext(), AddressAddActivty.class);
                    startActivity(intent);
                }*/


            }
        });
        tvclear.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHandler db=new DBHandler(getContext());
                db.deleteallCart();
                onCartChanged();
                getAllcart();
            }
        });
        btShopNow.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences pref6 = getContext().getApplicationContext().getSharedPreferences(Config.SHARED_PREF36, 0);
                SharedPreferences pref7 = getContext().getApplicationContext().getSharedPreferences(Config.SHARED_PREF37, 0);
                if(pref6.getString("RequiredStore", null).equals("false")&&
                        pref7.getString("RequiredStoreCategory", null).equals("false")){
                    Intent intent = new Intent(getContext(), OutShopActivity.class);
                    intent.putExtra("from", "home");
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), OutShopActivity.class);
                    intent.putExtra("from", "cart");
                    startActivity(intent);
                }
            }
        });
        if(db.selectCartCount()==0){
            rv_cartlist.setVisibility(View.GONE);
            emptyCart.setVisibility(View.VISIBLE);
        }else{
            rv_cartlist.setVisibility(View.VISIBLE);
            emptyCart.setVisibility(View.GONE);
        }
        SharedPreferences totalitems = getContext().getSharedPreferences(Config.SHARED_PREF129, 0);
        tvCartCount.setText(totalitems.getString("totalitems", "") + " : " + (db.selectCartCount()));
        onCartChanged();
        cartChangedListener = this;
        return v;
    }

    private void setBottomBar(View v) {

        navigation= (BottomNavigationView) v.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().findItem(R.id.navigation_cart).setChecked(true);
    }

    public void getAllcart(){
        db = new DBHandler(getContext());
        if(db.selectCartCount()==0){
            rv_cartlist.setVisibility(View.GONE);
            emptyCart.setVisibility(View.VISIBLE);
            bottom_navigation.setVisibility(View.GONE);
            lvTopCartCount.setVisibility(View.GONE);
        }else{
            rv_cartlist.setVisibility(View.VISIBLE);
            emptyCart.setVisibility(View.GONE);
            bottom_navigation.setVisibility(View.VISIBLE);
            lvTopCartCount.setVisibility(View.VISIBLE);
        }
        cartlist = new ArrayList<>(db.getAllCart());
        try {
            Gson gson = new Gson();
            String listString = gson.toJson(cartlist,new TypeToken<ArrayList<CartModel>>() {}.getType());
            JSONArray jarray =  new JSONArray(listString);
            GridLayoutManager lLayout = new GridLayoutManager(getContext(), 1);
            rv_cartlist.setLayoutManager(lLayout);
            rv_cartlist.setHasFixedSize(true);
            OutShopCartAdapter adapter = new OutShopCartAdapter(getContext(), jarray, this);
            rv_cartlist.setAdapter(adapter);
        }catch (Exception e){
        }
    }

    @Override
    public void onCartChanged() {
        db=new DBHandler(getContext());
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
        SharedPreferences totalamount = getContext().getSharedPreferences(Config.SHARED_PREF131, 0);
        bttoalamount.setText(totalamount.getString("totalamount", "")+" : "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        SharedPreferences youhavesaved = getContext().getSharedPreferences(Config.SHARED_PREF212, 0);

        bttoalsvdamount.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");

//        bttoalsvdamount.setText("( You have saved "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");
        TextView textview = (TextView)getActivity().findViewById(R.id.tvcart);
        if(strFrom.equals("Home")){
            textview.setText(String.valueOf(db.selectCartCount()));}

        SharedPreferences totalitems = getContext().getSharedPreferences(Config.SHARED_PREF129, 0);
        tvCartCount.setText(totalitems.getString("totalitems", "") + " : " + (db.selectCartCount()));
//        tvCartCount.setText("Total items : " + (db.selectCartCount()));
    }

    private void getprescription(final double doubleTotalAmt) {
        SharedPreferences baseurlpref = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(getActivity()).isInternetOn()) {
            progressDialog = new ProgressDialog(getActivity(), R.style.Progress);
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
                    requestObject1.put("ReqMode", "22");
                    JSONArray jsonArray = new JSONArray();
                    DBHandler db=new DBHandler(getActivity());
                    Cursor cursor = db.select("cart");
                    int i = 0;
                    if (cursor.moveToFirst()) {
                        do {
                            JSONObject jsonObject1 = new JSONObject();
                            try {
                                jsonObject1.put("ID_Item", cursor.getString(cursor.getColumnIndex("ID_Items")));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            try {
                                jsonArray.put(i, jsonObject1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            i++;
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                    requestObject1.put("Data", jsonArray);
                    requestObject1.put("Bank_Key","-500");
                    SharedPreferences IDLanguages = getContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getPrescription(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();

                            JSONObject jObject = new JSONObject(response.body());
                            Log.i("Response",response.body());
                            if(jObject.getString("StatusCode").equals("0")) {
                                JSONObject jobj = jObject.getJSONObject("PrescriptionList");
                                final String checkcase = jobj.getString("CheckCase");
                                SharedPreferences pref2 = getContext().getApplicationContext().getSharedPreferences(Config.SHARED_PREF27, 0);
                                String strhomedelivery = pref2.getString("homedelivery", null);
                                if (checkcase.equals("true")) {
                                    //  startActivity(new Intent(getContext(), PrescriptionUploadActivity.class));
                                    Intent i = new Intent(getActivity(),PrescriptionUploadActivity.class);
                                    i.putExtra("doubleTotalAmt", String.valueOf(doubleTotalAmt));
                                    startActivity(i);
                                } else {
                                    SharedPreferences Counterpickuppref = getContext().getApplicationContext().getSharedPreferences(Config.SHARED_PREF44, 0);
                                    if(Counterpickuppref.getString("Requiredcounterpickup", null).equals("false") && strhomedelivery.equals("true")){

                                        if (strhomedelivery.equals("false")) {

                                            SharedPreferences Homedeliveryoptionwillstartshortly = getContext().getSharedPreferences(Config.SHARED_PREF253, 0);
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage(Homedeliveryoptionwillstartshortly.getString("Homedeliveryoptionwillstartshortly","")+".")
//                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                                    builder.setMessage("Home delivery option will start shortly.")
                                                    .setCancelable(false)
                                                    .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            //  startActivity(new Intent(getContext(), CheckoutActivity.class));
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }
                                        else {
                                            SharedPreferences pref3 = getContext().getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                                            String strMinimumDeliveryAmount = pref3.getString("MinimumDeliveryAmount", null);
                                            if (Double.parseDouble(strMinimumDeliveryAmount) > OutShopFragment.this.doubleTotalAmt) {


                                                SharedPreferences MinimumamountHomedelivery = getContext().getSharedPreferences(Config.SHARED_PREF254, 0);
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                builder.setMessage(MinimumamountHomedelivery.getString("MinimumamountHomedelivery","")+" "+strMinimumDeliveryAmount)
//                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                                        builder.setMessage("Minimum amount required for Home delivery is " + strMinimumDeliveryAmount)
                                                        .setCancelable(false)
                                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                            }
                                                        });
                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            } else {
                                                Intent intent = new Intent(getContext(), AddressAddActivty.class);
                                                intent.putExtra("destination", fileimage);
                                                startActivity(intent);
                                            }
                                        }

                                    }

                                    else if(Counterpickuppref.getString("Requiredcounterpickup", null).equals("true") && strhomedelivery.equals("false")){

                                        Intent intent = new Intent(getContext(), CheckoutActivity.class);
                                        intent.putExtra("destination", fileimage);
                                        startActivity(intent);
                                    }
                                    else{
                                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                        final View customLayout = getLayoutInflater().inflate(R.layout.orderconfirm_dialog, null);

                                        builder.setView(customLayout);

                                        TextView txt_dia = customLayout.findViewById(R.id.txt_dia);
                                        TextView tv_storepickup = customLayout.findViewById(R.id.tv_storepickup);
                                        TextView tv_doordelivery = customLayout.findViewById(R.id.tv_doordelivery);

                                        SharedPreferences chooseyourdeliveryoptions = getContext().getSharedPreferences(Config.SHARED_PREF133, 0);
                                        txt_dia.setText(chooseyourdeliveryoptions.getString("chooseyourdeliveryoptions",""));
                                        SharedPreferences storepickup = getContext().getSharedPreferences(Config.SHARED_PREF134, 0);
                                        tv_storepickup.setText(storepickup.getString("storepickup",""));
                                        SharedPreferences doordelivery = getContext().getSharedPreferences(Config.SHARED_PREF135, 0);
                                        tv_doordelivery.setText(doordelivery.getString("doordelivery",""));


                                        CardView crdPickUp = customLayout.findViewById(R.id.crdPickUp);
                                        CardView crdHomeDelivery = customLayout.findViewById(R.id.crdHomeDelivery);
                                        crdPickUp.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getContext(), CheckoutActivity.class);
                                                intent.putExtra("destination", fileimage);
                                                startActivity(intent);
//                                                startActivity(new Intent(getContext(), CheckoutActivity.class));
                                            }
                                        });
                                        crdHomeDelivery.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {

                                                SharedPreferences pref2 = getContext().getApplicationContext().getSharedPreferences(Config.SHARED_PREF27, 0);
                                                String strhomedelivery = pref2.getString("homedelivery", null);


                                                if (strhomedelivery.equals("false")) {
                                                    SharedPreferences Homeshortlypleasedocounterpickupdeliveryoption = getContext().getSharedPreferences(Config.SHARED_PREF255, 0);
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                    builder.setMessage(Homeshortlypleasedocounterpickupdeliveryoption.getString("Homeshortlypleasedocounterpickupdeliveryoption","")+" .")
//                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                                    builder.setMessage("Home delivery option will start shortly, please do counter pickup delivery option.")
                                                            .setCancelable(false)
                                                            .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    // startActivity(new Intent(getContext(), CheckoutActivity.class));
                                                                    Intent intent = new Intent(getContext(), CheckoutActivity.class);
                                                                    intent.putExtra("destination", fileimage);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                    AlertDialog alert = builder.create();
                                                    alert.show();
                                                } else {
                                                    SharedPreferences pref3 = getContext().getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                                                    String strMinimumDeliveryAmount = pref3.getString("MinimumDeliveryAmount", null);
                                                    if (Double.parseDouble(strMinimumDeliveryAmount) > OutShopFragment.this.doubleTotalAmt) {

                                                        SharedPreferences MinimumamountHomedelivery = getContext().getSharedPreferences(Config.SHARED_PREF254, 0);
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                                        builder.setMessage(MinimumamountHomedelivery.getString("MinimumamountHomedelivery","")+" "+strMinimumDeliveryAmount)
//                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                                        builder.setMessage("Minimum amount required for Home delivery is " + strMinimumDeliveryAmount)
                                                                .setCancelable(false)
                                                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                                                    public void onClick(DialogInterface dialog, int id) {

                                                                    }
                                                                });
                                                        AlertDialog alert = builder.create();
                                                        alert.show();
                                                    }
                                                    else {
                                                        Intent intent = new Intent(getContext(), AddressAddActivty.class);
                                                        intent.putExtra("destination", fileimage);
                                                        startActivity(intent);
                                                    }
                                                }
                                            }
                                        });
                                        AlertDialog dialog = builder.create();
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        dialog.setView(customLayout, 0, 0, 0, 0);
                                        dialog.show();
                                    }
                                }
                            }else{
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setMessage("Some thing went wrong.")
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
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
            //  Intent in = new Intent(this, NoInternetActivity.class);
            //  startActivity(in);
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected( MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_category:
                    Intent io = new Intent(getContext(), OutShopActivity.class);
                    io.putExtra(fromCategory, valueCategory);
                    //finish();
                    startActivity(io);
                    return true;
                case R.id.navigation_cart:
                    Log.e(TAG,"navigation_cart    ");
//                    Intent ic = new Intent(HomeActivity.this, CartActivity.class);
//                    ic.putExtra(fromCart, valueCart);
//                    startActivity(ic);
                    return true;
                case R.id.navigation_home:
                    Log.e(TAG,"navigation_home    ");
                    Intent iha = new Intent(getContext(), HomeActivity.class);
                    iha.putExtra(fromFavorite, valueFavorite);
                    startActivity(iha);
                    return true;
                case R.id.navigation_favorite:
                    Log.e(TAG,"navigation_favorite    ");
                    Intent ifa = new Intent(getContext(), FavouriteActivity.class);
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
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
            LayoutInflater inflater1 = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.exit_back_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);

            TextView tv_popupdelete = (TextView) layout.findViewById(R.id.tv_popupdelete);
            TextView tv_popupdeleteinfo3 = (TextView) layout.findViewById(R.id.tv_popupdeleteinfo3);
            TextView edit = (TextView) layout.findViewById(R.id.edit);
            TextView tvno = (TextView) layout.findViewById(R.id.tvno);



            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();
            SharedPreferences pref4 = getContext().getSharedPreferences(Config.SHARED_PREF184, 0);
            tv_popupdelete.setText(pref4.getString("Confirmexit", null));

            SharedPreferences pref5 = getContext().getSharedPreferences(Config.SHARED_PREF185, 0);
            tv_popupdeleteinfo3.setText(pref5.getString("areyousureyouwanttoexit", null));

            SharedPreferences pref6 = getContext().getSharedPreferences(Config.SHARED_PREF186, 0);
            edit.setText(pref6.getString("yes", null));

            SharedPreferences pref7 = getContext().getSharedPreferences(Config.SHARED_PREF187, 0);
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
                    getActivity().finishAffinity();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        getActivity().finishAffinity();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


