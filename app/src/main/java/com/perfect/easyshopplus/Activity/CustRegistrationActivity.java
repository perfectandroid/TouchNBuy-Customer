package com.perfect.easyshopplus.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.goodiebag.pinview.Pinview;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Adapter.AreaListAdapter;
import com.perfect.easyshopplus.Adapter.StoreListAdapter;
import com.perfect.easyshopplus.Model.AreaModel;
import com.perfect.easyshopplus.Model.StoreModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.Helper;
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
import java.util.ArrayList;
import java.util.regex.Pattern;

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

public class CustRegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "CustRegistrationActivity";
    ScrollView sv_registerPage;
    Button btSubmit, btnSendOTP;
    String MemberId, MemMobile, ID_Customer, OTPRefNo, FK_CustomerPlus, CardPrice, Email , MemName , Address, PIN, strEmailsp;
    ProgressDialog progressDialog;
    EditText etname, etphonecode, etemail, etpassword, etconfirmpassword, etLandmark, etaddress, etstore, etarea, etphone, et_otp1, et_otp2, et_otp3, et_otp4;
    ListView list_view;
    EditText etsearch, edt_ageing_month;
    AreaListAdapter sadapter;
    StoreListAdapter sadapter1;
    TextView txt_product, tv_register,tv_popuptitle;
    int textlength = 0;
    ArrayList<StoreModel> storeArrayList = new ArrayList<>();
    public static ArrayList<StoreModel> array_sort1= new ArrayList<>();
    ArrayList<AreaModel> areaArrayList = new ArrayList<>();
    public static ArrayList<AreaModel> array_sort= new ArrayList<>();
    String areaId, idstore="null";
    boolean changing = false;

    TextView tv_etname,tv_etphone,tv_etpassword,tv_etconfirmpassword;
    TextView tv_etemail,tv_etaddress,tv_etLandmark;
    TextView tv_enter_otp;
    Pinview pinview;
    TextView tv_createaccount,tv_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_custregistration);
        initiateViews();
        setRegViews();

      //  openOtpScreenTesing();

        Log.e(TAG,"Start   106 ");

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

        SharedPreferences Emailid = getApplicationContext().getSharedPreferences(Config.SHARED_PREF92, 0);
        SharedPreferences Optional = getApplicationContext().getSharedPreferences(Config.SHARED_PREF98, 0);


        SharedPreferences pref9 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF93, 0);

        SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF50, 0);
        strEmailsp= pref4.getString("OTPEmailSend", null);
        if(strEmailsp.equals("true"))
        {
           // etemail.setHint(""+Emailid.getString("Emailid",null) +"*");
            tv_etemail.setText(""+Emailid.getString("Emailid",null) +"*");
        }
        else
        {
           // etemail.setHint(pref9.getString("Emailid_Optional", null));
            tv_etemail.setText(pref9.getString("Emailid_Optional", null));
        }


        SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF88, 0);
       // etname.setHint(pref5.getString("CustomerName", null));
        tv_etname.setText(pref5.getString("CustomerName", null));

        SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF89, 0);
//        etphone.setHint(pref6.getString("MobileNo", null));
        tv_etphone.setText(pref6.getString("MobileNo", null));

        SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF90, 0);
//        etpassword.setHint(pref7.getString("Password", null));
        tv_etpassword.setText(pref7.getString("Password", null));

        SharedPreferences pref8 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF91, 0);
//        etconfirmpassword.setHint(pref8.getString("confirmPassword", null));
        tv_etconfirmpassword.setText(pref8.getString("confirmPassword", null));


        SharedPreferences pref10 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF95, 0);
//        etaddress.setHint(pref10.getString("Address_Optional", null));
        tv_etaddress.setText(pref10.getString("Address_Optional", null));

        SharedPreferences pref11 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF97, 0);
//        etLandmark.setHint(pref11.getString("Landmark_Optional", null));
        tv_etLandmark.setText(pref11.getString("Landmark_Optional", null));


        SharedPreferences pref12 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF99, 0);
        btnSendOTP.setText(pref12.getString("SendOTP", null));

        SharedPreferences Register = getApplicationContext().getSharedPreferences(Config.SHARED_PREF87, 0);
        tv_register.setText(Register.getString("Register", null));

        tv_createaccount.setText("Create Account");
        tv_signup.setText("signup to get started");

       /* Intent in = getIntent();
        MemberId = in.getStringExtra("MemberId");
        MemMobile = in.getStringExtra("MemMobile");
        FK_CustomerPlus = in.getStringExtra("FK_CustomerPlus");
        CardPrice = in.getStringExtra("CardPrice");
        Email = in.getStringExtra("Email");
        MemName = in.getStringExtra("MemName");
        Address = in.getStringExtra("Address");
        PIN = in.getStringExtra("PIN");
        scrollTopLayout();
        etname.setText(MemName);
        etemail.setText(Email);*/

    }

    private void openOtpScreenTesing() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CustRegistrationActivity.this);
        LayoutInflater inflater1 = (LayoutInflater) CustRegistrationActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater1.inflate(R.layout.otp_popup, null);
        Button ok = (Button) layout.findViewById(R.id.btn_save);
        Button cancel = (Button) layout.findViewById(R.id.pop_cancel);
        TextView tv_popupchange = (TextView) layout.findViewById(R.id.tv_popupchange);
        tv_enter_otp  = (TextView) layout.findViewById(R.id.tv_enter_otp);
        final EditText etotp = (EditText) layout.findViewById(R.id.etotp);
        pinview = (Pinview) layout.findViewById(R.id.pinview) ;
        builder.setView(layout);

        final AlertDialog alertDialog = builder.create();
        SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF103, 0);
        etotp.setHint(pref4.getString("EnterOTP", null));

        tv_enter_otp.setText("Please enter the code was sent in your phone number");

        SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF102, 0);
       // tv_popupchange.setText(pref5.getString("EnterYourOTP", null));
        tv_popupchange.setText("OTP Verification");

        SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF105, 0);
        cancel.setText(pref6.getString("Cancel", null));

        SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        ok.setText(pref7.getString("OK", null));

        pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {

                Log.e(TAG,"Pinview   243   "+pinview.getValue());
                setOTPLogin(pinview.getValue().toString());

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // alertDialog.dismiss();
                if (etotp.getText().toString().isEmpty()) {

                    SharedPreferences Pleaseprovideotpsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF363, 0);
                    String Pleaseprovideotp = Pleaseprovideotpsp.getString("Pleaseprovideotp","");


                    etotp.setError(Pleaseprovideotp+".");
                    Toast.makeText(getApplicationContext(), Pleaseprovideotp+".",Toast.LENGTH_LONG).show();
                }   else {
                    setOTPLogin(etotp.getText().toString());
                }
            }
        });

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void initiateViews() {
        tv_register=findViewById(R.id.tv_register);
        btSubmit=(Button)findViewById(R.id.btSubmit);
        btnSendOTP=(Button)findViewById(R.id.btnSendOTP);
        etname=(EditText)findViewById(R.id.etname);
        etemail=(EditText)findViewById(R.id.etemail);
        etpassword=(EditText)findViewById(R.id.etpassword);
        etconfirmpassword=(EditText)findViewById(R.id.etconfirmpassword);
        etLandmark=(EditText)findViewById(R.id.etLandmark);
        etaddress=(EditText)findViewById(R.id.etaddress);
        etphone=(EditText)findViewById(R.id.etphone);
        etphonecode=(EditText)findViewById(R.id.etphonecode);
        etstore=(EditText)findViewById(R.id.etstore);
        //etstore.setEnabled(false);
        etarea=(EditText)findViewById(R.id.etarea);
       // etarea.setEnabled(false);
        et_otp1=(EditText)findViewById(R.id.et_otp1);
        et_otp2=(EditText)findViewById(R.id.et_otp2);
        et_otp3=(EditText)findViewById(R.id.et_otp3);
        et_otp4=(EditText)findViewById(R.id.et_otp4);
        sv_registerPage=(ScrollView) findViewById(R.id.sv_registerPage);

        tv_etname=(TextView) findViewById(R.id.tv_etname);
        tv_etphone=(TextView) findViewById(R.id.tv_etphone);
        tv_etpassword=(TextView) findViewById(R.id.tv_etpassword);
        tv_etconfirmpassword=(TextView) findViewById(R.id.tv_etconfirmpassword);
        tv_etemail=(TextView) findViewById(R.id.tv_etemail);
        tv_etaddress=(TextView) findViewById(R.id.tv_etaddress);
        tv_etLandmark=(TextView) findViewById(R.id.tv_etLandmark);

        tv_createaccount =(TextView) findViewById(R.id.tv_createaccount);
        tv_signup =(TextView) findViewById(R.id.tv_signup);


        etphone.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (!changing && etphone.getText().toString().startsWith("0")){
                    changing = true;
                    etphone.setText(etphone.getText().toString().replace("0", ""));
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
        btSubmit.setOnClickListener(this);
        btnSendOTP.setOnClickListener(this);
        etarea.setOnClickListener(this);
        etstore.setOnClickListener(this);
        etphone.setOnClickListener(this);
        etarea.setKeyListener(null);
        etstore.setKeyListener(null);
        etphonecode.setKeyListener(null);
    }


    public static void hideKeyboard(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((Activity) mContext).getWindow()
                .getCurrentFocus().getWindowToken(), 0);
    }

    private boolean isValidEmaillId(String email) {
        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

    public void validation() {

        Log.e("validation  ","validation  264   ");
        SharedPreferences PleaseProvideyourname = getApplicationContext().getSharedPreferences(Config.SHARED_PREF100, 0);
        SharedPreferences PleaseProvideyourPhoneNo = getApplicationContext().getSharedPreferences(Config.SHARED_PREF101, 0);
        SharedPreferences PleaseprovideavalidEmailAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF364, 0);
        SharedPreferences YourPasswordandconfirmationpassworddonotmatch = getApplicationContext().getSharedPreferences(Config.SHARED_PREF365, 0);
        SharedPreferences PleaseProvideyourPassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF111, 0);
        SharedPreferences PleaseprovideyourConfirmPassword = getApplicationContext().getSharedPreferences(Config.SHARED_PREF366, 0);

        if (etname.getText().toString().isEmpty()) {
            etname.setError(""+PleaseProvideyourname.getString("PleaseProvideyourname",null));
        }else if (etphone.getText().toString().isEmpty()) {
            etphone.setError(""+PleaseProvideyourPhoneNo.getString("PleaseProvideyourPhoneNo",null));
        }/*else if (etphone.getText().toString().length() != 10) {
            etphone.setError("Please provide 10 digit Phone No.");
        }*//*else if (etstore.getText().toString().isEmpty()) {
            setAlert("Please select store");
        }else if (etarea.getText().toString().isEmpty()) {
            setAlert("Please select area");
        }*/
        else if (etpassword.getText().toString().isEmpty()){
            etpassword.setError(""+PleaseProvideyourPassword.getString("PleaseProvideyourPassword",null));
        }
        else if (!etemail.getText().toString().isEmpty()) {
            if (!isValidEmaillId(etemail.getText().toString().trim())) {
//                etemail.setError("Please provide a valid Email Address.");
                etemail.setError(""+PleaseprovideavalidEmailAddress.getString("PleaseprovideavalidEmailAddress",null));
            } else {
                if(etpassword.getText().toString().equals(etconfirmpassword.getText().toString())) {
                    getRegister();
                }else{
                    setAlert(""+YourPasswordandconfirmationpassworddonotmatch.getString("YourPasswordandconfirmationpassworddonotmatch",""));
//                    setAlert("Your Password and confirmation password do not match.");
                }
            }
        }else if (etemail.getText().toString().isEmpty()) {
            if(strEmailsp.equals("true"))
            {
                etemail.setError(""+PleaseprovideavalidEmailAddress.getString("PleaseprovideavalidEmailAddress",null));
//                etemail.setError("Please provide your Email Address.");
            } else {
                if(etpassword.getText().toString().equals(etconfirmpassword.getText().toString())) {
                    getRegister();
                }else{
                    setAlert(""+YourPasswordandconfirmationpassworddonotmatch.getString("YourPasswordandconfirmationpassworddonotmatch",""));
//                    setAlert("Your Password and confirmation password do not match.");
                }
            }
        }else if (etpassword.getText().toString().isEmpty()) {
            etpassword.setError(""+PleaseProvideyourPassword.getString("PleaseProvideyourPassword",null));

//            etpassword.setError("Please provide your Password.");
        }else if (etconfirmpassword.getText().toString().isEmpty()) {
            etconfirmpassword.setError(""+PleaseprovideyourConfirmPassword.getString("PleaseprovideyourConfirmPassword",null));
//            etconfirmpassword.setError("Please provide your Confirm Password.");
        }
        /*else if (etaddress.getText().toString().isEmpty()) {
            etaddress.setError("Please provide your Address.");
        } */
        else {
            if (etpassword.getText().toString().isEmpty()){
                etpassword.setError(""+PleaseProvideyourPassword.getString("PleaseProvideyourPassword",null));
            }
            else if(etpassword.getText().toString().equals(etconfirmpassword.getText().toString())) {
                getRegister();
            }else{
                setAlert(""+YourPasswordandconfirmationpassworddonotmatch.getString("YourPasswordandconfirmationpassworddonotmatch",""));

//                setAlert("Your Password and confirmation password do not match.");
            }
        }
    }

  /*  public void validation() {
        if (etname.getText().toString().isEmpty()) {
            etname.setError("Please provide your Name.");
        }else if (etphone.getText().toString().isEmpty()) {
            etphone.setError("Please provide your Phone No.");
        }else if (etstore.getText().toString().isEmpty()) {
            setAlert("Please select store");
        }else if (etarea.getText().toString().isEmpty()) {
           setAlert("Please select area");
        }else if (!etemail.getText().toString().isEmpty()) {
            if (!isValidEmaillId(etemail.getText().toString().trim())) {
                etemail.setError("Please provide a valid Email Address.");
            } else {
                if(etpassword.getText().toString().equals(etconfirmpassword.getText().toString())) {
                    getRegister();
                }else{
                    setAlert("Please Enter correct password!");
                }
            }
        }else if (etpassword.getText().toString().isEmpty()) {
            etpassword.setError("Please provide your Password.");
        }   else if (etconfirmpassword.getText().toString().isEmpty()) {
            etconfirmpassword.setError("Please provide your Confirm Password.");
        }   else {
            if(etpassword.getText().toString().equals(etconfirmpassword.getText().toString())) {
                getRegister();
            }else{
                setAlert("Please Enter correct password!");
            }
        }
    }*/

/*    private void getRegister() {
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
            requestObject1.put("ReqMode", "16");
            requestObject1.put("CustomerMobile", etphone.getText().toString());
            requestObject1.put("CustomerEmail", etemail.getText().toString());
            requestObject1.put("CustomerName", etname.getText().toString());
            requestObject1.put("CustomerPassword", etpassword.getText().toString());
            requestObject1.put("LandMark", etLandmark.getText().toString());
            requestObject1.put("CustomerAddress", etaddress.getText().toString());
            requestObject1.put("FK_Area", areaId);
            requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
        Call<String> call = apiService.getCustRegister(body);
        call.enqueue(new Callback<String>() {
            @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jObject = new JSONObject(response.body());
                    JSONObject jmember = jObject.getJSONObject("CustomerRegistrationDetailsInfo");
                    String ResponseCode = jmember.getString("ResponseCode");
                    ID_Customer = jmember.getString("FK_Customer");
                    OTPRefNo = jmember.getString("OTP");
                    if(ResponseCode.equals("0"))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(CustRegistrationActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) CustRegistrationActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater1.inflate(R.layout.otp_popup, null);
                        Button ok = (Button) layout.findViewById(R.id.btn_save);
                        Button cancel = (Button) layout.findViewById(R.id.pop_cancel);
                        final EditText etotp = (EditText) layout.findViewById(R.id.etotp);
                        builder.setView(layout);
                        final AlertDialog alertDialog = builder.create();
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
                                if (etotp.getText().toString().isEmpty()) {


                                            SharedPreferences Pleaseprovideotpsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF363, 0);
                                            String Pleaseprovideotp = Pleaseprovideotpsp.getString("Pleaseprovideotp","");


                                            etotp.setError(Pleaseprovideotp+".");
                                            Toast.makeText(getApplicationContext(), Pleaseprovideotp+".",Toast.LENGTH_LONG).show();
                                    etotp.setError("Please provide OTP.");
                                    Toast.makeText(getApplicationContext(), "Please provide OTP.",Toast.LENGTH_LONG).show();
                                }   else {
                                    setOTPLogin(etotp.getText().toString());
                                }
                            }
                        });
                        alertDialog.show();
                    }
                    if (ResponseCode.equals("1")){
                        AlertDialog.Builder builder= new AlertDialog.Builder(CustRegistrationActivity.this);
                        builder.setMessage(jmember.getString("ResponseMessage")+" Please Login.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        startActivity(new Intent(CustRegistrationActivity.this,CustLoginActivity.class));
                                        finish();
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
        }  }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }*/



    private void getRegister() {
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

                    String phone = etphone.getText().toString();
                    if(phone.contains("+")){
                        phone = phone.replace("+","");
                    }
                   /* String phone = etphone.getText().toString();
                    if(etphone.getText().toString().contains("+")){
                        phone = etphone.getText().toString().replace("+","");
                    }*/

                    requestObject1.put("ReqMode", "16");
                    requestObject1.put("CustomerMobile", phone);
                    requestObject1.put("CustomerEmail", etemail.getText().toString());
                    requestObject1.put("CustomerName", etname.getText().toString());
                    requestObject1.put("CustomerPassword", etpassword.getText().toString());
                    requestObject1.put("LandMark", etLandmark.getText().toString());
                    requestObject1.put("CustomerAddress", etaddress.getText().toString());
                   // requestObject1.put("FK_Area", areaId);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1   644  "+requestObject1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getCustRegister(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG,"onResponse  629   "+response.body());
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jmember = jObject.getJSONObject("CustomerRegistrationDetailsInfo");
//                    String ResponseCode = jmember.getString("ResponseCode");
                            String StatusCode = jObject.getString("StatusCode");
                            ID_Customer = jmember.getString("FK_Customer");
                            OTPRefNo = jmember.getString("OTP");
                            if(StatusCode.equals("0"))
                            {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CustRegistrationActivity.this);
                                LayoutInflater inflater1 = (LayoutInflater) CustRegistrationActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View layout = inflater1.inflate(R.layout.otp_popup, null);
                                Button ok = (Button) layout.findViewById(R.id.btn_save);
                                Button cancel = (Button) layout.findViewById(R.id.pop_cancel);
                                tv_enter_otp  = (TextView) layout.findViewById(R.id.tv_enter_otp);
                                TextView tv_popupchange = (TextView) layout.findViewById(R.id.tv_popupchange);
                                final EditText etotp = (EditText) layout.findViewById(R.id.etotp);
                                pinview = (Pinview) layout.findViewById(R.id.pinview) ;
                                builder.setView(layout);


                                final AlertDialog alertDialog = builder.create();
                                SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF103, 0);
                                etotp.setHint(pref4.getString("EnterOTP", null));

                                tv_enter_otp.setText("Please enter the code was sent in your phone number");

                                SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF102, 0);
                                tv_popupchange.setText(pref5.getString("EnterYourOTP", null));

                                SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF105, 0);
                                cancel.setText(pref6.getString("Cancel", null));

                                SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
                                ok.setText(pref7.getString("OK", null));

                                pinview.setPinViewEventListener(new Pinview.PinViewEventListener() {
                                    @Override
                                    public void onDataEntered(Pinview pinview, boolean fromUser) {

                                        Log.e(TAG,"Pinview   243   "+pinview.getValue());
                                        setOTPLogin(pinview.getValue().toString());

                                    }
                                });

                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog.dismiss();
                                    }
                                });
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                       // alertDialog.dismiss();
                                        if (etotp.getText().toString().isEmpty()) {

                                            SharedPreferences Pleaseprovideotpsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF363, 0);
                                            String Pleaseprovideotp = Pleaseprovideotpsp.getString("Pleaseprovideotp","");


                                            etotp.setError(Pleaseprovideotp+".");
                                            Toast.makeText(getApplicationContext(), Pleaseprovideotp+".",Toast.LENGTH_LONG).show();
                                        }   else {
                                            setOTPLogin(etotp.getText().toString());
                                        }
                                    }
                                });

                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                            }
                            else if (StatusCode.equals("-2")) {
                                SharedPreferences pref16 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
                                new AlertDialog.Builder(CustRegistrationActivity.this)
                                        .setMessage(jmember.getString("ResponseMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(pref16.getString("OK", null), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                            else if (StatusCode.equals("-3")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CustRegistrationActivity.this);
                                LayoutInflater inflater1 = (LayoutInflater) CustRegistrationActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View layout = inflater1.inflate(R.layout.otp_popup, null);



                                Button ok = (Button) layout.findViewById(R.id.btn_save);
                                Button cancel = (Button) layout.findViewById(R.id.pop_cancel);
                                TextView tvheadermsg = (TextView) layout.findViewById(R.id.tvheadermsg);
                                final EditText etotp = (EditText) layout.findViewById(R.id.etotp);

                                builder.setView(layout);





                                final AlertDialog alertDialog = builder.create();

                                SharedPreferences pref13 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF102, 0);
                                tvheadermsg.setText(pref13.getString("EnterYourOTP", null));

                                SharedPreferences pref14 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF103, 0);
                                etotp.setHint(pref14.getString("EnterOTP", null));

                                SharedPreferences pref15 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF105, 0);
                                cancel.setText(pref15.getString("Cancel", null));

                                SharedPreferences pref16 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
                                ok.setText(pref16.getString("OK", null));

                                SharedPreferences OTPissendtoyourregisteredMobilenumber = getApplicationContext().getSharedPreferences(Config.SHARED_PREF367, 0);
                                tvheadermsg.setText(OTPissendtoyourregisteredMobilenumber.getString("OTPissendtoyourregisteredMobilenumber", null));

//                                tvheadermsg.setText("OTP is send to your registered Mobile number");
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog.dismiss();
                                    }
                                });
                                ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // alertDialog.dismiss();
                                        if (etotp.getText().toString().isEmpty()) {


                                            SharedPreferences Pleaseprovideotpsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF363, 0);
                                            String Pleaseprovideotp = Pleaseprovideotpsp.getString("Pleaseprovideotp","");


                                            etotp.setError(Pleaseprovideotp+".");
                                            Toast.makeText(getApplicationContext(), Pleaseprovideotp+".",Toast.LENGTH_LONG).show();
//                                            etotp.setError("Please provide OTP.");
//                                            Toast.makeText(getApplicationContext(), "Please provide OTP.",Toast.LENGTH_LONG).show();
                                        }   else {
                                            setOTPLogin(etotp.getText().toString());
                                        }
                                    }
                                });
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                            }
                            else if (StatusCode.equals("1")) {
//                        AlertDialog.Builder builder= new AlertDialog.Builder(CustRegistrationActivity.this);
//                        builder.setMessage(jmember.getString("ResponseMessage")+" Please Login.")
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        startActivity(new Intent(CustRegistrationActivity.this,CustLoginActivity.class));
//                                        finish();
//                                    }
//                                });
//                        AlertDialog alert = builder.create();
//                        alert.show();
                                SharedPreferences pref16 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
                                new AlertDialog.Builder(CustRegistrationActivity.this)
                                        .setMessage(jmember.getString("ResponseMessage")+" Please Login.")
                                        .setCancelable(false)
                                        .setPositiveButton(""+pref16.getString("OK",null), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                startActivity(new Intent(CustRegistrationActivity.this,CustLoginActivity.class));
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                            else {
//                        AlertDialog.Builder builder= new AlertDialog.Builder(CustRegistrationActivity.this);
//                        final AlertDialog alert = builder.create();
//                        builder.setMessage(jmember.getString("ResponseMessage"))
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        alert.dismiss();
//                                    }
//                                });
//                        alert.show();
                                SharedPreferences pref16 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);

                                new AlertDialog.Builder(CustRegistrationActivity.this)
                                        .setMessage(jmember.getString("ResponseMessage"))
                                        .setCancelable(false)
                                        .setPositiveButton(""+pref16.getString("OK",null), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
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
            }  }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }


 /*   private void setOTPLogin(String otp) {
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
            requestObject1.put("ReqMode", "2");
            requestObject1.put("ID_Customer", ID_Customer);
            requestObject1.put("OTPRefNo", OTPRefNo);
            requestObject1.put("OTP", otp);
            requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
        Call<String> call = apiService.getOTPVerification(body);
        call.enqueue(new Callback<String>() {
            @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                try {
                    progressDialog.dismiss();
                    JSONObject jObject = new JSONObject(response.body());
                    JSONObject jmember = jObject.getJSONObject("OTPverifyInfo");
                    String ResponseCode = jmember.getString("ResponseCode");
                    if(ResponseCode.equals("0")){
                        setAlert( jmember.getString("ResponseMessage"));
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
                        FK_CustomerPluseditor.putString("FK_CustomerPlus", jmember.getString("FK_Customer"));
                        FK_CustomerPluseditor.commit();
                        SharedPreferences Address = getApplicationContext().getSharedPreferences(Config.SHARED_PREF15, 0);
                        SharedPreferences.Editor Addresseditor = Address.edit();
                        Addresseditor.putString("Address", jmember.getString("Address"));
                        Addresseditor.commit();
                        SharedPreferences AddressId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF17, 0);
                        SharedPreferences.Editor AddresseditorId = AddressId.edit();
                        AddresseditorId.putString("AddressID", jmember.getString("ID_CustomerAddress"));
                        AddresseditorId.commit();

                        startActivity(new Intent(CustRegistrationActivity.this,HomeActivity.class));
                    }
                    if (ResponseCode.equals("-1")){
                        setAlert(jmember.getString("ResponseMessage"));
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
    }  }else {
                Intent in = new Intent(this,NoInternetActivity.class);
                startActivity(in);
            }
    }*/


    private void setOTPLogin(String otp) {
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
                    requestObject1.put("ReqMode", "2");
                    requestObject1.put("ID_Customer", ID_Customer);
                    requestObject1.put("OTPRefNo", OTPRefNo);
                    requestObject1.put("OTP", otp);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1   1019   "+requestObject1);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getOTPVerification(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            if (jObject.getString("StatusCode").equals("0")){
                                JSONObject jmember = jObject.getJSONObject("OTPverifyInfo");
                                String ResponseCode = jmember.getString("ResponseCode");
                                if(ResponseCode.equals("0")){
                                  //  setAlert( jmember.getString("ResponseMessage"));
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
                                    FK_CustomerPluseditor.putString("FK_CustomerPlus", jmember.getString("FK_Customer"));
                                    FK_CustomerPluseditor.commit();
                                    SharedPreferences Address = getApplicationContext().getSharedPreferences(Config.SHARED_PREF15, 0);
                                    SharedPreferences.Editor Addresseditor = Address.edit();
                                    Addresseditor.putString("Address", jmember.getString("Address"));
                                    Addresseditor.commit();
                                    SharedPreferences AddressId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF17, 0);
                                    SharedPreferences.Editor AddresseditorId = AddressId.edit();
                                    AddresseditorId.putString("AddressID", jmember.getString("ID_CustomerAddress"));
                                    AddresseditorId.commit();
                                    SharedPreferences name = getApplicationContext().getSharedPreferences(Config.SHARED_PREF18, 0);
                                    SharedPreferences.Editor nameeditor = name.edit();
                                    nameeditor.putString("DeliUsername", jmember.getString("CusName"));
                                    nameeditor.commit();
                                    SharedPreferences DelAddress = getApplicationContext().getSharedPreferences(Config.SHARED_PREF19, 0);
                                    SharedPreferences.Editor DelAddresseditor = DelAddress.edit();
                                    DelAddresseditor.putString("DeliAddress", jmember.getString("Address"));
                                    DelAddresseditor.commit();
                                    SharedPreferences AddressIdDel = getApplicationContext().getSharedPreferences(Config.SHARED_PREF20, 0);
                                    SharedPreferences.Editor AddresseditorIdDel = AddressIdDel.edit();
                                    AddresseditorIdDel.putString("DeliAddressID", jmember.getString("ID_CustomerAddress"));
                                    AddresseditorIdDel.commit();
                               /* SharedPreferences pincode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF21, 0);
                                SharedPreferences.Editor pincodeeditor = pincode.edit();
                                pincodeeditor.putString("DeliPincode", jmember.getString("PIN"));
                                pincodeeditor.commit();*/
                                    SharedPreferences area = getApplicationContext().getSharedPreferences(Config.SHARED_PREF22, 0);
                                    SharedPreferences.Editor areaeditor = area.edit();
                                    areaeditor.putString("DeliArea", jmember.getString("AreaName"));
                                    areaeditor.commit();
                                    SharedPreferences AreaId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF23, 0);
                                    SharedPreferences.Editor AreaIdeditor = AreaId.edit();
                                    AreaIdeditor.putString("DeliAreaID", jmember.getString("FK_Area"));
                                    AreaIdeditor.commit();
                                    SharedPreferences landmark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF24, 0);
                                    SharedPreferences.Editor landmarkeditor = landmark.edit();
                                    landmarkeditor.putString("DeliLandmark", jmember.getString("LandMark"));
                                    landmarkeditor.commit();
                                    SharedPreferences mobNumb = getApplicationContext().getSharedPreferences(Config.SHARED_PREF25, 0);
                                    SharedPreferences.Editor mobNumbeditor = mobNumb.edit();
                                    mobNumbeditor.putString("DeliMobNumb", jmember.getString("CusMobile"));
                                    mobNumbeditor.commit();
                                    SharedPreferences Landmark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF28, 0);
                                    SharedPreferences.Editor Landmarkeditor = Landmark.edit();
                                    Landmarkeditor.putString("Landmark", jmember.getString("LandMark"));
                                    Landmarkeditor.commit();

                                    SharedPreferences DeliveryCharge = getApplicationContext().getSharedPreferences(Config.SHARED_PREF46, 0);
                                    SharedPreferences.Editor DeliveryChargeeditor = DeliveryCharge.edit();
                                    DeliveryChargeeditor.putString("DeliveryCharge", jmember.getString("DeliveryCharge"));
                                    DeliveryChargeeditor.commit();

                              /*  SharedPreferences ID_Store =getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
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
                                    //  startActivity(new Intent(CustRegistrationActivity.this,HomeActivity.class));

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
                                            Intent i = new Intent(CustRegistrationActivity.this, MainCategoryActivity.class);
                                            startActivity(i);
                                            finish();
                                        }else {
                                            Intent i = new Intent(CustRegistrationActivity.this, HomeActivity.class);
                                            startActivity(i);
                                            finish();
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
                                            Intent i = new Intent(CustRegistrationActivity.this, StoreActivity.class);
                                            startActivity(i);
                                            finish();
                                        }else {
                                            Intent i = new Intent(CustRegistrationActivity.this, HomeActivity.class);
                                            startActivity(i);
                                            finish();
                                        }

                                    }
                                }
                                if (ResponseCode.equals("-1")){

                                   // setAlert(jmember.getString("ResponseMessage"));
                                    pinview.requestPinEntryFocus();
                                    Toast.makeText(getApplicationContext(),""+jmember.getString("ResponseMessage"),Toast.LENGTH_SHORT).show();
                                }
                            }else {
                              //  setAlert(jObject.getString("EXMessage"));
                                Toast.makeText(getApplicationContext(),""+jObject.getString("EXMessage"),Toast.LENGTH_SHORT).show();
                                pinview.requestPinEntryFocus();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            pinview.requestPinEntryFocus();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        progressDialog.dismiss();
                        pinview.requestPinEntryFocus();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                pinview.requestPinEntryFocus();
            }  }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }

    private void getStore() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater1 = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.area_popup, null);
            list_view = (ListView) layout.findViewById(R.id.list_view);
            etsearch = (EditText) layout.findViewById(R.id.etsearch);
            tv_popuptitle = (TextView) layout.findViewById(R.id.tv_popuptitle);

            SharedPreferences SelectStoresp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF356, 0);
            String SelectStore = SelectStoresp.getString("SelectStore","");

            tv_popuptitle.setText(SelectStore+"");
            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            getStoreList(alertDialog);
            alertDialog.show();
        }catch (Exception e){e.printStackTrace();}
    }

    public void getStoreList(final AlertDialog alertDialog){
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
                    requestObject1.put("ReqMode", "5");
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getStores(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("StoreBindListInfo");
                            if (jobj.getString("storeListInfos").equals("null")) {
                            } else {
                                JSONArray jarray = jobj.getJSONArray("storeListInfos");
                                array_sort1 = new ArrayList<>();
                                storeArrayList = new ArrayList<>();
                                for (int k = 0; k < jarray.length(); k++) {
                                    JSONObject jsonObject = jarray.getJSONObject(k);

                                    storeArrayList.add(new StoreModel(jsonObject.getString("ID_Store"),jsonObject.getString("StoreName")));
                                    array_sort1.add(new StoreModel(jsonObject.getString("ID_Store"),jsonObject.getString("StoreName")));
                                }
                                sadapter1 = new StoreListAdapter(CustRegistrationActivity.this, array_sort1);
                                list_view.setAdapter(sadapter1);
                                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        etstore.setText(array_sort1.get(position).getStoreName());
                                        idstore=array_sort1.get(position).getID_Store();
                                        alertDialog.dismiss();
                                    }
                                }/*.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    }*/
                                );

                            }
                            etsearch.addTextChangedListener(new TextWatcher() {
                                public void afterTextChanged(Editable s) {
                                }
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    list_view.setVisibility(View.VISIBLE);
                                    textlength = etsearch.getText().length();
                                    array_sort1.clear();
                                    for (int i = 0; i < storeArrayList.size(); i++) {
                                        if (textlength <= storeArrayList.get(i).getStoreName().length()) {
                                            if (storeArrayList.get(i).getStoreName().toLowerCase().trim().contains(
                                                    etsearch.getText().toString().toLowerCase().trim())) {
                                                array_sort1.add(storeArrayList.get(i));
                                            }
                                        }
                                    }
                                    sadapter1 = new StoreListAdapter(CustRegistrationActivity.this, array_sort1);
                                    list_view.setAdapter(sadapter1);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {}
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            SharedPreferences Nointernetconnection = getApplicationContext().getSharedPreferences(Config.SHARED_PREF282, 0);
            Toast.makeText(this,""+Nointernetconnection.getString("Nointernetconnection",null),Toast.LENGTH_SHORT).show();
        }
    }

    private void getArea() {
        try {
            SharedPreferences selectarea = getApplicationContext().getSharedPreferences(Config.SHARED_PREF224, 0);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater1 = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.area_popup, null);
            list_view = (ListView) layout.findViewById(R.id.list_view);
            etsearch = (EditText) layout.findViewById(R.id.etsearch);
            tv_popuptitle = (TextView) layout.findViewById(R.id.tv_popuptitle);
            tv_popuptitle.setText(""+selectarea.getString("selectarea",null));
            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            getAreaList(alertDialog);
            alertDialog.show();
        }catch (Exception e){e.printStackTrace();}
    }

    public void getAreaList(final AlertDialog alertDialog){
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
                    requestObject1.put("ReqMode", "15");
                    requestObject1.put("FK_Store", idstore);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getArea(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("AreaDetails");
                            if (jobj.getString("AreaDetailsList").equals("null")) {
                            } else {
                                JSONArray jarray = jobj.getJSONArray("AreaDetailsList");
                                array_sort = new ArrayList<>();
                                areaArrayList=new ArrayList<>();
                                for (int k = 0; k < jarray.length(); k++) {
                                    JSONObject jsonObject = jarray.getJSONObject(k);

                                    areaArrayList.add(new AreaModel(jsonObject.getString("FK_Area"),jsonObject.getString("AreaName")));
                                    array_sort.add(new AreaModel(jsonObject.getString("FK_Area"),jsonObject.getString("AreaName")));
                                }
                                sadapter = new AreaListAdapter(CustRegistrationActivity.this, array_sort);
                                list_view.setAdapter(sadapter);
                                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        etarea.setText(array_sort.get(position).getAreaName());
                                        areaId=array_sort.get(position).getID_Area();
                                        alertDialog.dismiss();
                                    }
                                }/*.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    }*/
                                );

                            }
                            etsearch.addTextChangedListener(new TextWatcher() {
                                public void afterTextChanged(Editable s) {
                                }
                                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                }
                                public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    list_view.setVisibility(View.VISIBLE);
                                    textlength = etsearch.getText().length();
                                    array_sort.clear();
                                  //  areaArrayList=new ArrayList<>();
                                 //   array_sort = new ArrayList<>();
                                    for (int i = 0; i < areaArrayList.size(); i++) {
                                        if (textlength <= areaArrayList.get(i).getAreaName().length()) {
                                            if (areaArrayList.get(i).getAreaName().toLowerCase().trim().contains(
                                                    etsearch.getText().toString().toLowerCase().trim())) {
                                                array_sort.add(areaArrayList.get(i));
                                            }
                                        }
                                    }
                                    sadapter = new AreaListAdapter(CustRegistrationActivity.this, array_sort);
                                    list_view.setAdapter(sadapter);
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {}
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {

            SharedPreferences Nointernetconnection = getApplicationContext().getSharedPreferences(Config.SHARED_PREF282, 0);
            Toast.makeText(this,""+Nointernetconnection.getString("Nointernetconnection",null),Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onClick(View v){
            switch (v.getId()) {
                case R.id.btnSendOTP:
                   // hideKeyboard(this);
                        validation();
                    break;
                case R.id.etarea:
                    hideKeyboard(this);
                    if(idstore.equals("null")){
                        setAlert("Please select Store");
                    }
                    else {
                        getArea();
                    }
                    break;
                case R.id.etstore:
                    hideKeyboard(this);
                    getStore();
                    etarea.setText("");
                    break;
                /*case R.id.etphone:
                    etphone.setText("+91 ");
                    etphone.setSelection(3);

                    break;*/
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

        Helper.setErrorAlert(CustRegistrationActivity.this,strMsg);
//        SharedPreferences pref16 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
//        AlertDialog.Builder builder= new AlertDialog.Builder(this);
//        builder.setMessage(strMsg)
//                .setCancelable(false)
//                .setPositiveButton(""+pref16.getString("OK",null), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        //clearall();
//                    }
//                });
//        AlertDialog alert = builder.create();
//        alert.show();


    }

    public void scrollTopLayout() {
        et_otp1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sv_registerPage.scrollBy(0, 120);
                    }
                }, 200);
            }
        });
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


                                // if(singlestore.equals("1")) {
                                Intent intent = new Intent(CustRegistrationActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
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
