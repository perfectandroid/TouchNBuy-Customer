package com.perfect.easyshopplus.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Adapter.SearchListAdapter;
import com.perfect.easyshopplus.Model.SearchModel;
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
import java.util.ArrayList;
import java.util.Locale;

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

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG= "SearchActivity",OK;
    ProgressDialog progressDialog;
    EditText etsearch;
    ListView list_view;
    int textlength = 0;
    SearchListAdapter sadapter;
    ImageView imsearch, imClear;
    ArrayList<SearchModel> searchNamesArrayList = new ArrayList<>();
    public static ArrayList<SearchModel> array_sort= new ArrayList<>();

    public static final Integer RecordAudioRequestCode = 1;
    private SpeechRecognizer speechRecognizer;
    private ImageView micButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
//            checkPermission();
//        }
        setInitialise();

        Log.e(TAG,"SearchActivity  Start    84   ");

        SharedPreferences SearchForProducts = getApplicationContext().getSharedPreferences(Config.SHARED_PREF118, 0);
        etsearch.setHint(""+SearchForProducts.getString("SearchForProducts",null));
        etsearch.requestFocus();
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
        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {

            }

            @Override
            public void onBeginningOfSpeech() {
                etsearch.setText("");
                etsearch.setHint("Listening...");
            }

            @Override
            public void onRmsChanged(float v) {

            }

            @Override
            public void onBufferReceived(byte[] bytes) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int i) {

            }

            @Override
            public void onResults(Bundle bundle) {
                micButton.setImageResource(R.drawable.ic_mic_black_off);
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                etsearch.setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {

            }

            @Override
            public void onEvent(int i, Bundle bundle) {

            }
        });


        micButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP){
                    speechRecognizer.stopListening();
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    micButton.setImageResource(R.drawable.ic_mic_black_24dp);
                    speechRecognizer.startListening(speechRecognizerIntent);
                }
                return false;
            }
        });



        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        OK = (OKsp.getString("OK", ""));

    }

    private void setInitialise() {
        list_view = (ListView) findViewById(R.id.list_view);
        etsearch = (EditText) findViewById(R.id.etsearch);
        imsearch = (ImageView) findViewById(R.id.imsearch);
        imsearch.setOnClickListener(this);
        imClear = (ImageView) findViewById(R.id.imClear);
        imClear.setOnClickListener(this);

        micButton = findViewById(R.id.imSpeak);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
    }

    public void searchList(String Keywrd){
        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        final String IMAGEURL = imgpref.getString("ImageURL", null);
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
                SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                requestObject1.put("ReqMode", "4");
                requestObject1.put("ShopType", "2");
                requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                requestObject1.put("MemberId", pref3.getString("memberid", null));
                requestObject1.put("ItemName",Keywrd);
                requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                Log.e(TAG,"requestObject1   263    "+requestObject1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
            Call<String> call = apiService.getItemList(body);
            call.enqueue(new Callback<String>() {
                @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    try {
                        Log.e(TAG,"response   272    "+response.body());
                        JSONObject jObject = new JSONObject(response.body());
                        JSONObject jobj = jObject.getJSONObject("ItemListSearchInfo");
                        if (jobj.getString("ItemListInfo").equals("null")) {
                        } else {


                        JSONArray jarray = jobj.getJSONArray("ItemListInfo");
                        array_sort = new ArrayList<>();
                        for (int k = 0; k < jarray.length(); k++) {
                            JSONObject jsonObject = jarray.getJSONObject(k);
                            searchNamesArrayList.add(new SearchModel(jsonObject.getString("ItemName"), jsonObject.getString("MRP"),
                                    jsonObject.getString("SalesPrice"), jsonObject.getString("ID_Items"),
                                    jsonObject.getString("ID_Stock"), jsonObject.getString("CurrentStock"),
                                    jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"),
                                    jsonObject.getString("WholesalePrice"), jsonObject.getString("GST"),
                                    jsonObject.getString("CESS"), jsonObject.getString("Description"),
                                    IMAGEURL + jsonObject.getString("ImageName"), jsonObject.getString("Packed"),
                                    jsonObject.getString("ItemMalayalamName")

                            ));
                            array_sort.add(new SearchModel(jsonObject.getString("ItemName"), jsonObject.getString("MRP"),
                                    jsonObject.getString("SalesPrice"), jsonObject.getString("ID_Items"),
                                    jsonObject.getString("ID_Stock"), jsonObject.getString("CurrentStock"),
                                    jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"),
                                    jsonObject.getString("WholesalePrice"), jsonObject.getString("GST"),
                                    jsonObject.getString("CESS"), jsonObject.getString("Description"),
                                    IMAGEURL + jsonObject.getString("ImageName"), jsonObject.getString("Packed"),
                                    jsonObject.getString("ItemMalayalamName")
                            ));
                        }

                            Log.e(TAG,"response   303    "+array_sort);
                        sadapter = new SearchListAdapter(SearchActivity.this, array_sort);
                        list_view.setAdapter(sadapter);
                        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                Log.e(TAG,"ImageName   309   "+array_sort.get(position).getImageName());
                                Intent i = new Intent(SearchActivity.this, ItemDetailsActivity.class);
                                i.putExtra("ItemName", array_sort.get(position).getItemName());
                                i.putExtra("MRP", array_sort.get(position).getMRP());
                                i.putExtra("SalesPrice", array_sort.get(position).getSalesPrice());
                                i.putExtra("Id_Item", array_sort.get(position).getID_Items());
                                i.putExtra("ID_Stock", array_sort.get(position).getID_Stock());
                                i.putExtra("CurrentStock", array_sort.get(position).getCurrentStock());
                                i.putExtra("RetailPrice", array_sort.get(position).getRetailPrice());
                                i.putExtra("PrivilagePrice", array_sort.get(position).getPrivilagePrice());
                                i.putExtra("WholesalePrice", array_sort.get(position).getWholesalePrice());
                                i.putExtra("GST", array_sort.get(position).getGST());
                                i.putExtra("CESS", array_sort.get(position).getCESS());
                                i.putExtra("Description", array_sort.get(position).getDescription());
                                i.putExtra("ImageName", array_sort.get(position).getImageName());
                                i.putExtra("Packed", array_sort.get(position).getPacked());
                                i.putExtra("from", "Search");
                                i.putExtra("ItemMalayalamName", array_sort.get(position).getItemMalayalamName());
                                startActivity(i);
                                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(etsearch.getWindowToken(), 0);
                            }
                        });
                    }
//                        etsearch.addTextChangedListener(new TextWatcher() {
//                            public void afterTextChanged(Editable s) {
//                            }
//                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                            }
//                            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                                list_view.setVisibility(View.VISIBLE);
//                                textlength = etsearch.getText().length();
//                                array_sort.clear();
//                                for (int i = 0; i < searchNamesArrayList.size(); i++) {
//                                    if (textlength <= searchNamesArrayList.get(i).getItemName().length()) {
//                                        if (searchNamesArrayList.get(i).getItemName().toLowerCase().trim().contains(
//                                                etsearch.getText().toString().toLowerCase().trim())) {
//                                            array_sort.add(searchNamesArrayList.get(i));
//                                        }
//                                    }
//                                }
//                                Log.e(TAG,"array_sort   349    "+array_sort);
//                                sadapter = new SearchListAdapter(SearchActivity.this, array_sort);
//                                list_view.setAdapter(sadapter);
//                            }
//                        });
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
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                    requestObject1.put("ReqMode", "4");
                    requestObject1.put("ShopType", "2");
                    requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                    requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("MemberId", pref3.getString("memberid", null));
                    requestObject1.put("ItemName",Keywrd);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1    406    "+requestObject1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getItemList(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            Log.e(TAG,"onResponse    416    "+response.body());
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("ItemListSearchInfo");
                            if (jobj.getString("ItemListInfo").equals("null")) {
                                array_sort = new ArrayList<>();
                                sadapter = new SearchListAdapter(SearchActivity.this, array_sort);
                                list_view.setAdapter(sadapter);
                                SharedPreferences Noitemfound = getApplicationContext().getSharedPreferences(Config.SHARED_PREF259, 0);
                                String Noitemfoun = Noitemfound.getString("Noitemfound","");
                                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                            builder.setMessage(Noitemfoun+"..")
                                    .setCancelable(false)
                                    .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                            } else {
                                JSONArray jarray = jobj.getJSONArray("ItemListInfo");
                                array_sort = new ArrayList<>();
                                for (int k = 0; k < jarray.length(); k++) {
                                    JSONObject jsonObject = jarray.getJSONObject(k);
//                                    searchNamesArrayList.add(new SearchModel(jsonObject.getString("ItemName"), jsonObject.getString("MRP"),
//                                            jsonObject.getString("SalesPrice"), jsonObject.getString("ID_Items"),
//                                            jsonObject.getString("ID_Stock"), jsonObject.getString("CurrentStock"),
//                                            jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"),
//                                            jsonObject.getString("WholesalePrice"), jsonObject.getString("GST"),
//                                            jsonObject.getString("CESS"), jsonObject.getString("Description"),
//                                            IMAGEURL + jsonObject.getString("ImageName"),
//                                            jsonObject.getString("Packed"), jsonObject.getString("ItemMalayalamName")
//
//                                    ));
//                                    array_sort.add(new SearchModel(jsonObject.getString("ItemName"), jsonObject.getString("MRP"),
//                                            jsonObject.getString("SalesPrice"), jsonObject.getString("ID_Items"),
//                                            jsonObject.getString("ID_Stock"), jsonObject.getString("CurrentStock"),
//                                            jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"),
//                                            jsonObject.getString("WholesalePrice"), jsonObject.getString("GST"),
//                                            jsonObject.getString("CESS"), jsonObject.getString("Description"),
//                                            IMAGEURL + jsonObject.getString("ImageName"), jsonObject.getString("Packed"),
//                                            jsonObject.getString("ItemMalayalamName")
//                                    ));

                                    searchNamesArrayList.add(new SearchModel(jsonObject.getString("ItemName"), jsonObject.getString("MRP"),
                                            jsonObject.getString("SalesPrice"), jsonObject.getString("ID_Items"),
                                            jsonObject.getString("ID_Stock"), jsonObject.getString("CurrentStock"),
                                            jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"),
                                            jsonObject.getString("WholesalePrice"), jsonObject.getString("GST"),
                                            jsonObject.getString("CESS"), jsonObject.getString("Description"),
                                            IMAGEURL + jsonObject.getString("ImageName"), jsonObject.getString("Packed"),
                                            jsonObject.getString("ItemMalayalamName")

                                    ));
                                    array_sort.add(new SearchModel(jsonObject.getString("ItemName"), jsonObject.getString("MRP"),
                                            jsonObject.getString("SalesPrice"), jsonObject.getString("ID_Items"),
                                            jsonObject.getString("ID_Stock"), jsonObject.getString("CurrentStock"),
                                            jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"),
                                            jsonObject.getString("WholesalePrice"), jsonObject.getString("GST"),
                                            jsonObject.getString("CESS"), jsonObject.getString("Description"),
                                            IMAGEURL + jsonObject.getString("ImageName"), jsonObject.getString("Packed"),
                                            jsonObject.getString("ItemMalayalamName")
                                    ));
                                }
                                sadapter = new SearchListAdapter(SearchActivity.this, array_sort);
                                list_view.setAdapter(sadapter);
                                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent i = new Intent(SearchActivity.this, ItemDetailsActivity.class);
                                        i.putExtra("ItemName", array_sort.get(position).getItemName());
                                        i.putExtra("MRP", array_sort.get(position).getMRP());
                                        i.putExtra("SalesPrice", array_sort.get(position).getSalesPrice());
                                        i.putExtra("Id_Item", array_sort.get(position).getID_Items());
                                        i.putExtra("ID_Stock", array_sort.get(position).getID_Stock());
                                        i.putExtra("CurrentStock", array_sort.get(position).getCurrentStock());
                                        i.putExtra("RetailPrice", array_sort.get(position).getRetailPrice());
                                        i.putExtra("PrivilagePrice", array_sort.get(position).getPrivilagePrice());
                                        i.putExtra("WholesalePrice", array_sort.get(position).getWholesalePrice());
                                        i.putExtra("GST", array_sort.get(position).getGST());
                                        i.putExtra("CESS", array_sort.get(position).getCESS());
                                        i.putExtra("Description", array_sort.get(position).getDescription());
                                        i.putExtra("ImageName", array_sort.get(position).getImageName());
                                        i.putExtra("Packed", array_sort.get(position).getPacked());
                                        i.putExtra("from", "Search");
                                        i.putExtra("ItemMalayalamName", array_sort.get(position).getItemMalayalamName());
                                        startActivity(i);
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(etsearch.getWindowToken(), 0);
                                    }
                                });
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imsearch:
                if (etsearch.getText().toString().isEmpty()) {
                    etsearch.setError("Please provide Keyword for search.");
                }else {
                    getSearchList(etsearch.getText().toString());
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                break;
            case R.id.imClear:
                etsearch.setText("");
                imClear.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speechRecognizer.destroy();
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.RECORD_AUDIO},RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0 ){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SharedPreferences PermissionGranted = getApplicationContext().getSharedPreferences(Config.SHARED_PREF372, 0);
                Toast.makeText(this, PermissionGranted.getString("PermissionGranted", null) + ".", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
