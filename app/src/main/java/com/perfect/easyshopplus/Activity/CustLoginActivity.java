package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.Helper;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

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

public class CustLoginActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG= "CustLoginActivity";
    ProgressDialog progressDialog;
    Button btSignin;
    TextView tvSignup, tv_login, tvfoegetpassword,txtnotamember;
    EditText etpass, etuser, etphonecode;
    CheckBox cbPasswrd;
    String stMobile, stMemberid, strEmailsp, strMsgsp, OK;
    boolean changing = false;
    TextView tv_etuser,tv_etpass;
    TextView tv_welcome,tv_signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_custlogin);
        initiateViews();
        setRegViews();
        showPasswrd();


        if(getResources().getString(R.string.app_name).equals("JZT CART") || getResources().getString(R.string.app_name).equals("Pulikkottil Hypermarket") || getResources().getString(R.string.app_name).equals("NeethiMed")|| getResources().getString(R.string.app_name).equals("Touch n Buy")) {

            SharedPreferences Countrypref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF58, 0);
            SharedPreferences.Editor CountryprefEditor = Countrypref.edit();
            CountryprefEditor.putString("country", "India");
            CountryprefEditor.commit();
        }

            SharedPreferences countrypref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF58, 0);
            if(countrypref.getString("country", null).equals("India")){
                etphonecode.setText("+91");
            }else if(countrypref.getString("country", null).equals("Malaysia")){
                etphonecode.setText("+60");
            }

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
        ImageView imApplogo=findViewById(R.id.imApplogo);
        String strImagepath= IMAGEURL + pref3.getString("AppIconImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath).into(imApplogo);

        SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF89, 0);
//        etuser.setHint(pref4.getString("MobileNo", null));
        tv_etuser.setText(pref4.getString("MobileNo", null));

        SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF90, 0);
//        etpass.setHint(pref5.getString("Password", null));
        tv_etpass.setText(pref5.getString("Password", null));

        SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF106, 0);
      //  cbPasswrd.setText(pref6.getString("ShowPassword", null));

        SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF107, 0);
        tvfoegetpassword.setText(pref7.getString("ForgotPassword", null));

        SharedPreferences pref8 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF86, 0);
        btSignin.setText(pref8.getString("Login", null));
        tv_login.setText(pref8.getString("Login", null));

        SharedPreferences pref9 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF108, 0);
        txtnotamember.setText(pref9.getString("NotaMember", null));

        SharedPreferences pref10 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF109, 0);
        tvSignup.setText(pref10.getString("RegisterNow", null));


        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        OK = (OKsp.getString("OK", ""));

        SharedPreferences welcome = getApplicationContext().getSharedPreferences(Config.SHARED_PREF200, 0);
        tv_welcome.setText(""+welcome.getString("welcome",""));

        tv_signin.setText("sign in to continue!");

    }

    private void initiateViews() {
        btSignin=(Button)findViewById(R.id.btSignin);
        tvSignup=(TextView) findViewById(R.id.tvSignup);
        tvfoegetpassword=(TextView) findViewById(R.id.tvfoegetpassword);
        txtnotamember=(TextView) findViewById(R.id.txtnotamember);
        tv_login=(TextView) findViewById(R.id.tv_login);
        etpass=(EditText)findViewById(R.id.etpass);
        etuser=(EditText) findViewById(R.id.etuser);
        etphonecode=(EditText) findViewById(R.id.etphonecode);
        cbPasswrd=(CheckBox) findViewById(R.id.cbPasswrd);


        tv_etuser=(TextView) findViewById(R.id.tv_etuser);
        tv_etpass=(TextView) findViewById(R.id.tv_etpass);

        tv_welcome =(TextView) findViewById(R.id.tv_welcome);
        tv_signin =(TextView) findViewById(R.id.tv_signin);

        etuser.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!changing && etuser.getText().toString().startsWith("0")){
                    changing = true;
                    etuser.setText(etuser.getText().toString().replace("0", ""));
                }
                changing = false;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
    }

    private void setRegViews() {
        btSignin.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
        tvfoegetpassword.setOnClickListener(this);
        etphonecode.setKeyListener(null);

    }

    public void showPasswrd(){
        etpass.setTransformationMethod(new PasswordTransformationMethod());
        cbPasswrd.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {
                    etpass.setTransformationMethod(null);
                    cbPasswrd.setBackground(getResources().getDrawable(R.drawable.pass_show));
                } else {
                    etpass.setTransformationMethod(new PasswordTransformationMethod());
                    cbPasswrd.setBackground(getResources().getDrawable(R.drawable.pass_unshow));
                }
            }
        } );
    }

    public void validation() {
        SharedPreferences PleaseProvideyourPhoneNo = getApplicationContext().getSharedPreferences(Config.SHARED_PREF101, 0);
        SharedPreferences PleaseProvideyourPassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF111, 0);

        if (etuser.getText().toString().isEmpty()) {
            etuser.setError(""+PleaseProvideyourPhoneNo.getString("PleaseProvideyourPhoneNo",null));
//            etuser.setError("Please provide your Mobile Number.");
        } else if (etpass.getText().toString().isEmpty()) {
            etpass.setError(""+PleaseProvideyourPassword.getString("PleaseProvideyourPassword",null));

//            etpass.setError("Please provide your Password.");
        }   else {
            getlogin();
        }
    }
    private void getlogin() {
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
                    String phone = etuser.getText().toString();
                    if(phone.contains("+")){
                        phone = phone.replace("+","");
                    }
                    requestObject1.put("ReqMode", "3");
                    requestObject1.put("UserName", phone);
                    requestObject1.put("CusPassword", etpass.getText().toString());
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                    Log.e(TAG,"onResponse  249   "+requestObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getlogin(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG,"onResponse  226   "+response.body());
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jmember = jObject.getJSONObject("CustomerDetailsInfo");
                            String StatusCode = jObject.getString("StatusCode");
                            String ResponseCode = jmember.getString("ResponseCode");
                            if(StatusCode.equals("0")){
                                SharedPreferences Loginpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                                SharedPreferences.Editor logineditor = Loginpref.edit();
                                logineditor.putString("loginsession", "Yes");
                                logineditor.commit();

                                SharedPreferences userid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                                SharedPreferences.Editor userideditor = userid.edit();
                                userideditor.putString("userid", jmember.getString("ID_Customer"));
                                userideditor.commit();
                                SharedPreferences username = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
                                SharedPreferences.Editor usernameeditor = username.edit();
                                usernameeditor.putString("username", jmember.getString("CusName"));
                                usernameeditor.commit();
                                SharedPreferences useremail = getApplicationContext().getSharedPreferences(Config.SHARED_PREF3, 0);
                                SharedPreferences.Editor useremailideditor = useremail.edit();
                                useremailideditor.putString("useremail", jmember.getString("CusEmail"));
                                useremailideditor.commit();
                                SharedPreferences userphoneno = getApplicationContext().getSharedPreferences(Config.SHARED_PREF4, 0);
                                SharedPreferences.Editor userphoneeditor = userphoneno.edit();
                                userphoneeditor.putString("userphoneno", jmember.getString("CusMobile"));
                                userphoneeditor.commit();
                                SharedPreferences memberid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                                SharedPreferences.Editor memberideditor = memberid.edit();
                                memberideditor.putString("memberid", jmember.getString("CusNumber"));
                                memberideditor.commit();
                                SharedPreferences FK_CustomerPlus = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                                SharedPreferences.Editor FK_CustomerPluseditor = FK_CustomerPlus.edit();
                                FK_CustomerPluseditor.putString("FK_CustomerPlus", jmember.getString("FK_CustomerPlus"));
                                FK_CustomerPluseditor.commit();
                                SharedPreferences Address = getApplicationContext().getSharedPreferences(Config.SHARED_PREF15, 0);
                                SharedPreferences.Editor Addresseditor = Address.edit();
                                Addresseditor.putString("Address", jmember.getString("Address"));
                                Addresseditor.commit();
                                SharedPreferences PIN = getApplicationContext().getSharedPreferences(Config.SHARED_PREF16, 0);
                                SharedPreferences.Editor PINeditor = PIN.edit();
                                PINeditor.putString("PIN", jmember.getString("PIN"));
                                PINeditor.commit();
                                SharedPreferences AddressId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF17, 0);
                                SharedPreferences.Editor AddresseditorId = AddressId.edit();
                                AddresseditorId.putString("AddressID", jmember.getString("ID_CustomerAddress"));
                                AddresseditorId.commit();


                                SharedPreferences Areasp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF63, 0);
                                SharedPreferences.Editor Areaeditor = Areasp.edit();
                                Areaeditor.putString("Area", jmember.getString("AreaName"));
                                Areaeditor.commit();

                                SharedPreferences AreaId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF64, 0);
                                SharedPreferences.Editor AreaeditorId = AreaId.edit();
                                AreaeditorId.putString("AreaId", jmember.getString("FK_Area"));
                                AreaeditorId.commit();
//                                SharedPreferences name = getApplicationContext().getSharedPreferences(Config.SHARED_PREF18, 0);
//                                SharedPreferences.Editor nameeditor = name.edit();
//                                nameeditor.putString("DeliUsername", jmember.getString("CusName"));
//                                nameeditor.commit();
//                                SharedPreferences DelAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF19, 0);
//                                SharedPreferences.Editor DelAddresseditor = DelAddress.edit();
//                                DelAddresseditor.putString("DeliAddress", jmember.getString("Address"));
//                                DelAddresseditor.commit();
//                                SharedPreferences AddressIdDel = getApplicationContext().getSharedPreferences(Config.SHARED_PREF20, 0);
//                                SharedPreferences.Editor AddresseditorIdDel = AddressIdDel.edit();
//                                AddresseditorIdDel.putString("DeliAddressID", jmember.getString("ID_CustomerAddress"));
//                                AddresseditorIdDel.commit();
//                                SharedPreferences pincode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF21, 0);
//                                SharedPreferences.Editor pincodeeditor = pincode.edit();
//                                pincodeeditor.putString("DeliPincode", jmember.getString("PIN"));
//                                pincodeeditor.commit();
//                                SharedPreferences area = getApplicationContext().getSharedPreferences(Config.SHARED_PREF22, 0);
//                                SharedPreferences.Editor areaeditor = area.edit();
//                                areaeditor.putString("DeliArea", jmember.getString("AreaName"));
//                                areaeditor.commit();
//                                SharedPreferences AreaId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF23, 0);
//                                SharedPreferences.Editor AreaIdeditor = AreaId.edit();
//                                AreaIdeditor.putString("DeliAreaID", jmember.getString("FK_Area"));
//                                AreaIdeditor.commit();
//                                SharedPreferences landmark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF24, 0);
//                                SharedPreferences.Editor landmarkeditor = landmark.edit();
//                                landmarkeditor.putString("DeliLandmark", jmember.getString("LandMark"));
//                                landmarkeditor.commit();
//                                SharedPreferences mobNumb = getApplicationContext().getSharedPreferences(Config.SHARED_PREF25, 0);
//                                SharedPreferences.Editor mobNumbeditor = mobNumb.edit();
//                                mobNumbeditor.putString("DeliMobNumb", jmember.getString("CusMobile"));
//                                mobNumbeditor.commit();
                               /* SharedPreferences ID_Store =getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                                SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                ID_Storeeditor.putString("ID_Store", jmember.getString("FK_Store"));
                                ID_Storeeditor.commit();
                                SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                StoreNameeditor.putString("StoreName", jmember.getString("StoreName"));
                                StoreNameeditor.commit();
                                SharedPreferences StoreImage = getApplicationContext().getSharedPreferences(Config.SHARED_PREF26, 0);
                                SharedPreferences.Editor StoreImageditor = StoreImage.edit();
                                StoreImageditor.putString("StoreImage", jmember.getString("StoreImagePath"));
                                StoreImageditor.commit();*/

                                SharedPreferences Landmark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF28, 0);
                                SharedPreferences.Editor Landmarkeditor = Landmark.edit();
                                Landmarkeditor.putString("Landmark", jmember.getString("LandMark"));
                                Landmarkeditor.commit();

                                SharedPreferences DeliveryCharge = getApplicationContext().getSharedPreferences(Config.SHARED_PREF46, 0);
                                SharedPreferences.Editor DeliveryChargeeditor = DeliveryCharge.edit();
                                DeliveryChargeeditor.putString("DeliveryCharge", jmember.getString("DeliveryCharge"));
                                DeliveryChargeeditor.commit();


//                                Toast.makeText(getApplicationContext(), jmember.getString("ResponseMessage"),Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(CustLoginActivity.this,HomeActivity.class));

                                SharedPreferences StoreCategorySP = getApplicationContext().getSharedPreferences(Config.SHARED_PREF47, 0);
                                Log.e(TAG,"StoreCategorySP   388    "+StoreCategorySP.getString("StoreCategory", null));
//                                if (StoreCategorySP.getString("StoreCategory", "").equals("")){
//                                    Log.e(TAG,"StoreCategorySP   390    "+StoreCategorySP.getString("StoreCategory", null));
//                                    Intent i = new Intent(CustLoginActivity.this, MainCategoryActivity.class);
//                                    startActivity(i);
//
//                                }else {
                                    Log.e(TAG,"StoreCategorySP   395    "+StoreCategorySP.getString("StoreCategory", null));
                                    SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF36, 0);
                                    SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF37, 0);
                                    if(pref6.getString("RequiredStore", null).equals("true")&&
                                            pref7.getString("RequiredStoreCategory", null).equals("true")){
//                                        Intent i = new Intent(CustLoginActivity.this, HomeActivity.class);
//                                        startActivity(i);
                                        SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                        Log.e(TAG,"StoreName  384 399    "+StoreName.getString("StoreName", null));
                                        if(StoreName.getString("StoreName", null) == null || StoreName.getString("StoreName", null).isEmpty() || StoreName.getString("StoreName", null).equals("")){
                                            Intent i = new Intent(CustLoginActivity.this, MainCategoryActivity.class);
                                            startActivity(i);
                                            finish();
                                        }else {
                                            Intent i = new Intent(CustLoginActivity.this, HomeActivity.class);
                                            startActivity(i);

                                        }
                                    }
                                    else if(pref6.getString("RequiredStore", null).equals("false")&&
                                            pref7.getString("RequiredStoreCategory", null).equals("false")){


                                        getSinglestoreDetails();
                                    }
                                    else if(pref6.getString("RequiredStore", null).equals("true")&&
                                            pref7.getString("RequiredStoreCategory", null).equals("false")){
//                                      singlestore="1";
                                       // getSinglestoreDetails();

                                        SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                        Log.e(TAG,"StoreName  384 399    "+StoreName.getString("StoreName", null));
                                        if(StoreName.getString("StoreName", null) == null || StoreName.getString("StoreName", null).isEmpty() || StoreName.getString("StoreName", null).equals("")){
                                            Intent i = new Intent(CustLoginActivity.this, StoreActivity.class);
                                            startActivity(i);
                                            finish();
                                        }else {
                                            Intent i = new Intent(CustLoginActivity.this, HomeActivity.class);
                                            startActivity(i);
                                        }

                                    }

                                }
//                            }
                            else if (StatusCode.equals("-1")){
                                setAlert(jmember.getString("ResponseMessage"));
                                etuser.setText("");
                                etpass.setText("");
                            }
                            else{
                                setAlert(jObject.getString("EXMessage"));
                                etuser.setText("");
                                etpass.setText("");
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

 /*   private void getlogin() {
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
                requestObject1.put("ReqMode", "3");
                requestObject1.put("UserName", etuser.getText().toString());
                requestObject1.put("CusPassword", etpass.getText().toString());
                requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
            Call<String> call = apiService.getlogin(body);
            call.enqueue(new Callback<String>() {
                @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    try {
                        progressDialog.dismiss();
                        JSONObject jObject = new JSONObject(response.body());
                        JSONObject jmember = jObject.getJSONObject("CustomerDetailsInfo");
                        String ResponseCode = jmember.getString("ResponseCode");
                        if(ResponseCode.equals("0")){
                            SharedPreferences Loginpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                            SharedPreferences.Editor logineditor = Loginpref.edit();
                            logineditor.putString("loginsession", "Yes");
                            logineditor.commit();


                            SharedPreferences userid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
                            SharedPreferences.Editor userideditor = userid.edit();
                            userideditor.putString("userid", jmember.getString("ID_Customer"));
                            userideditor.commit();
                            SharedPreferences username = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
                            SharedPreferences.Editor usernameeditor = username.edit();
                            usernameeditor.putString("username", jmember.getString("CusName"));
                            usernameeditor.commit();
                            SharedPreferences useremail = getApplicationContext().getSharedPreferences(Config.SHARED_PREF3, 0);
                            SharedPreferences.Editor useremailideditor = useremail.edit();
                            useremailideditor.putString("useremail", jmember.getString("CusEmail"));
                            useremailideditor.commit();
                            SharedPreferences userphoneno = getApplicationContext().getSharedPreferences(Config.SHARED_PREF4, 0);
                            SharedPreferences.Editor userphoneeditor = userphoneno.edit();
                            userphoneeditor.putString("userphoneno", jmember.getString("CusMobile"));
                            userphoneeditor.commit();
                            SharedPreferences memberid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                            SharedPreferences.Editor memberideditor = memberid.edit();
                            memberideditor.putString("memberid", jmember.getString("CusNumber"));
                            memberideditor.commit();
                            SharedPreferences FK_CustomerPlus = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                            SharedPreferences.Editor FK_CustomerPluseditor = FK_CustomerPlus.edit();
                            FK_CustomerPluseditor.putString("FK_CustomerPlus", jmember.getString("FK_CustomerPlus"));
                            FK_CustomerPluseditor.commit();
                            SharedPreferences Address = getApplicationContext().getSharedPreferences(Config.SHARED_PREF15, 0);
                            SharedPreferences.Editor Addresseditor = Address.edit();
                            Addresseditor.putString("Address", jmember.getString("Address"));
                            Addresseditor.commit();
                            SharedPreferences PIN = getApplicationContext().getSharedPreferences(Config.SHARED_PREF16, 0);
                            SharedPreferences.Editor PINeditor = PIN.edit();
                            PINeditor.putString("PIN", jmember.getString("PIN"));
                            PINeditor.commit();
                            SharedPreferences AddressId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF17, 0);
                            SharedPreferences.Editor AddresseditorId = AddressId.edit();
                            AddresseditorId.putString("AddressID", jmember.getString("ID_CustomerAddress"));
                            AddresseditorId.commit();

                            Toast.makeText(getApplicationContext(), jmember.getString("ResponseMessage"),Toast.LENGTH_LONG).show();
                            startActivity(new Intent(CustLoginActivity.this,HomeActivity.class));
                        }
                        if (ResponseCode.equals("-1")){
                            setAlert(jmember.getString("ResponseMessage"));
                            etuser.setText("");
                            etpass.setText("");
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
    }*/

    private void doForgetPassword(final AlertDialog alertDialog) {
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
            SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
            final JSONObject requestObject1 = new JSONObject();
            try {
                requestObject1.put("ReqMode", "5");
                requestObject1.put("MemMobile", stMobile );
                requestObject1.put("CusEmail", stMemberid );
                requestObject1.put("ID_Customer", pref1.getString("userid", null));
                requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                Log.e(TAG,"requestObject1   622   "+requestObject1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
            Call<String> call = apiService.getforgetpasswrd(body);
            call.enqueue(new Callback<String>() {
                @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    try {
                        progressDialog.dismiss();
                        JSONObject jObject = new JSONObject(response.body());
                        Log.e("ForgotPasswordInfo ","524    "+response.body());
                        JSONObject jmember = jObject.getJSONObject("ForgotPasswordInfo");
                        String ResponseCode = jmember.getString("ResponseCode");
                        if(ResponseCode.equals("2")){


                            SharedPreferences NewPasswordissendtoyournumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF361, 0);
                         //   setAlert(NewPasswordissendtoyournumber.getString("NewPasswordissendtoyournumber", null));

                            setAlert1(""+jmember.getString("ResponseMessage"));
//                            setAlert("New Password is send to your number.");
                            alertDialog.dismiss();
                        }
                        else{
                            SharedPreferences EnterValidMobileNumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF362, 0);
//                            setAlert(EnterValidMobileNumber.getString("EnterValidMobileNumber", null));
                            setAlert(jmember.getString("ResponseMessage"));
//                            setAlert("Enter Valid Mobile Number.");
                        }
                    } catch (JSONException e) {

                        SharedPreferences Somethingwentwrong = getApplicationContext().getSharedPreferences(Config.SHARED_PREF72, 0);
                        Toast.makeText(getApplicationContext(),""+Somethingwentwrong.getString("Somethingwentwrong",null),Toast.LENGTH_LONG).show();
//                        setAlert("Enter Valid Mobile Number.");
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



    private void foegetpassword(){
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(CustLoginActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) CustLoginActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.forget_password_popup, null);

            TextView tv_popupchange = layout.findViewById(R.id.tv_popupchange);
            SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF107, 0);
            tv_popupchange.setText(pref7.getString("ForgotPassword", null));

            final EditText etmobileno = (EditText) layout.findViewById(R.id.etmobileno);
            SharedPreferences EnterMobileNumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF360, 0);
            etmobileno.setHint(EnterMobileNumber.getString("EnterMobileNumber", null));


            final EditText etemailid = (EditText) layout.findViewById(R.id.etemailid);
            SharedPreferences EnterEmailAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF359, 0);
            etemailid.setHint(EnterEmailAddress.getString("EnterEmailAddress", null));

            final TextView tvor = (TextView) layout.findViewById(R.id.tvor);
            TextView tv_email_mob = (TextView) layout.findViewById(R.id.tv_email_mob);


            Button pop_changecancel = (Button) layout.findViewById(R.id.pop_changecancel);
            SharedPreferences Cancel = getApplicationContext().getSharedPreferences(Config.SHARED_PREF105, 0);
            pop_changecancel.setText(Cancel.getString("Cancel", null));


            Button btn_changesave = (Button) layout.findViewById(R.id.btn_changesave);
            SharedPreferences OK = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
            SharedPreferences submit = getApplicationContext().getSharedPreferences(Config.SHARED_PREF236, 0);
//            btn_changesave.setHint(OK.getString("OK", null));
            btn_changesave.setText(submit.getString("submit",null));


            SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF50, 0);
            strEmailsp= pref4.getString("OTPEmailSend", null);
            SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF55, 0);
            strMsgsp= pref5.getString("OTPMsgSend", null);

            if(strEmailsp.equals("true")&&strMsgsp.equals("true")) {
                etmobileno.setVisibility(View.VISIBLE);
                etemailid.setVisibility(View.VISIBLE);
                tv_email_mob.setText("");
                tvor.setVisibility(View.VISIBLE);
            } else if(strEmailsp.equals("true")&&strMsgsp.equals("false")) {
                etemailid.setVisibility(View.VISIBLE);
            } else if(strEmailsp.equals("false")&&strMsgsp.equals("true")) {
                etmobileno.setVisibility(View.VISIBLE);
            }
            etmobileno.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    if (!changing && etmobileno.getText().toString().startsWith("0")){
                        changing = true;
                        etmobileno.setText(etmobileno.getText().toString().replace("0", ""));
                    }
                    changing = false;
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

            });

            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            pop_changecancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            btn_changesave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF50, 0);
                    strEmailsp= pref4.getString("OTPEmailSend", null);
                    SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF55, 0);
                    strMsgsp= pref5.getString("OTPMsgSend", null);
                    if(strEmailsp.equals("true")&&strMsgsp.equals("true")) {
                        if (etmobileno.getText().toString().isEmpty()&&etemailid.getText().toString().isEmpty()) {

                            SharedPreferences EmailAddressMobileNumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF358, 0);
                            setAlert(EmailAddressMobileNumber.getString("EmailAddressMobileNumber", null));
//                            setAlert("Enter Email Address Or Mobile Number");
                        } else {
                            String phone = etmobileno.getText().toString();
                            if(phone.contains("+")){
                                phone = phone.replace("+","");
                            }
                            stMobile =phone;
                            stMemberid=etemailid.getText().toString();
                            doForgetPassword(alertDialog);
                        }
                    } else if(strEmailsp.equals("true")&&strMsgsp.equals("false")) {
                        if (etemailid.getText().toString().isEmpty()) {


                            SharedPreferences EnterEmailAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF359, 0);
                            setAlert(EnterEmailAddress.getString("EnterEmailAddress", null));
//                            setAlert("Enter Email Address");
                        } else {
                            String phone = etmobileno.getText().toString();
                            if(phone.contains("+")){
                                phone = phone.replace("+","");
                            }
                            stMobile =phone;
                            stMemberid=etemailid.getText().toString();
                            doForgetPassword(alertDialog);
                        }
                    } else if(strEmailsp.equals("false")&&strMsgsp.equals("true")) {
                        if (etmobileno.getText().toString().isEmpty()) {

                            SharedPreferences EnterMobileNumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF360, 0);
                            setAlert(EnterMobileNumber.getString("EnterMobileNumber", null));
//                            setAlert("Enter Mobile Number");
                        } else {
                            String phone = etmobileno.getText().toString();
                            if(phone.contains("+")){
                                phone = phone.replace("+","");
                            }
                            stMobile =phone;
                            stMemberid=etemailid.getText().toString();
                            doForgetPassword(alertDialog);
                        }
                    }String MobilePattern = "[0-9]{10}";
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btSignin:
                validation();
                break;
            case R.id.tvSignup:
                startActivity(new Intent(this,CustRegistrationActivity.class));
                finish();
                break;
            case R.id.tvfoegetpassword:
                foegetpassword();
                break;
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

    public void setAlert(String strMsg){

       // Helper.setErrorAlert(CustLoginActivity.this,strMsg);

        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        String OKs = (OKsp.getString("OK", ""));

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage(strMsg)
                .setCancelable(false)
                .setPositiveButton(OKs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //clearall();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void setAlert1(String strMsg) {

        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        String OKs = (OKsp.getString("OK", ""));

        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage(strMsg)
                .setCancelable(false)
                .setPositiveButton(OKs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //clearall();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
                            Log.e(TAG,"getSinglestoreDetails  onResponse  1150   "+response.body());
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

                                Log.e(TAG,"OnlinePayment    "+ jobj.getString("OnlinePayment"));

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

//                                SharedPreferences OnlinePaymentpref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF54, 0);
//                                SharedPreferences OnlinePaymentmeth1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF62, 0);
//                                String BASEURL = OnlinePaymentmeth1.getString("OnlinePaymentMethods", null);
//                                Log.e(TAG,"BASEURL   2282    "+BASEURL + "    "+OnlinePaymentpref1.getString("OnlinePayment", null));


                               // if(singlestore.equals("1")) {
                                    Intent intent = new Intent(CustLoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                              //  }
                            }
                            else{
                                SharedPreferences Somethingwentwrongsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF72, 0);
                                String Somethingwentwrong = Somethingwentwrongsp.getString("Somethingwentwrong", "");
                                Toast.makeText(getApplicationContext(),Somethingwentwrong+" !",Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            //  progressDialog.dismiss();
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
