package com.perfect.easyshopplus.Activity;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Adapter.AreaListAdapter;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.AreaModel;
import com.perfect.easyshopplus.Model.ArealistModel;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, View.OnClickListener , NavigationView.OnNavigationItemSelectedListener{
    ProgressDialog progressDialog;
    private GoogleMap mMap;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;
    ImageButton ib_search;
    EditText etSearch,etsearch;
    String userid, Somethingwentwrong;
    Spinner search_spinner;
    Marker marker;
    int intPosition;
    ListView list_view;
    double lattitude,longitude;
     ArrayList<ArealistModel> areaModelArrayList;
     ArrayList<String> areaNames = new ArrayList<String>();
    TextView  tv_popuptitle,tvcart, tv_notification;
    ArrayList<AreaModel> areaArrayList = new ArrayList<>();
    int textlength = 0;
    AreaListAdapter sadapter;
    String areaId ="null";
    private ListView lvNavMenu;
    DrawerLayout drawer;
    ImageView im, imcart, im_notification;
    public static ArrayList<AreaModel> array_sort= new ArrayList<>();
    List<Marker> markers = new ArrayList<Marker>();
    String[] searchType=new String[]{};
    String   strStorename, strAreaName, strpinNumber;
    TextView tv_header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_main);



        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
        ImageView imApplogo=findViewById(R.id.imApplogo);
        String strImagepath= IMAGEURL + pref3.getString("AppIconImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath).into(imApplogo);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager()
                        .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);


        initiateViews();
        setRegViews();
        setHomeNavMenu1();

        SharedPreferences StoreLocations = getApplicationContext().getSharedPreferences(Config.SHARED_PREF251, 0);
        tv_header.setText(""+StoreLocations.getString("StoreLocations",""));


        SharedPreferences Somethingwentwrongsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF72, 0);
        Somethingwentwrong = Somethingwentwrongsp.getString("Somethingwentwrong", "");
     /*   SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/


        String value = etSearch.getText().toString();

        if(value.equals(""))
        {
            //Toast.makeText(getApplicationContext(),"yes",Toast.LENGTH_LONG).show();
            getStoreSearchList();
        }
        else
        {
          // Toast.makeText(getApplicationContext(),"no",Toast.LENGTH_LONG).show();
        }
        detailsShowing();
     //   Share.dPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
      //  String username=pref2.getString("username", null);
    }
    InputFilter filter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; ++i)
            {
                if (!Pattern.compile("[ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890]*").matcher(String.valueOf(source.charAt(i))).matches())
                {return "";}}
            return null;
        }
    };
    private void detailsShowing() {


          //  searchType = getResources().getStringArray(R.array.array_search);

            SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF245, 0);
            SharedPreferences AreaName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF246, 0);
            SharedPreferences Pincode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF247, 0);

            searchType =  new String[]{StoreName.getString("Store_Name", ""),AreaName.getString("AreaName", ""),Pincode.getString("Pincode", "")};
            ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,searchType);
            aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            search_spinner.setAdapter(aa);
            search_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View v,
                                           int position, long arg3) {
                    // TODO Auto-generated method stub
                    strStorename="";
                    strAreaName="";
                    strpinNumber="";
                    if(position==0){
                        etSearch.setText("");
                        etSearch.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(20)});
                        etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                        intPosition=position;
                    }
                    if(position==1){
                        etSearch.setText("");
                        etSearch.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(20)});
                        etSearch.setInputType(InputType.TYPE_CLASS_TEXT);
                        intPosition=position;
                    }
                    if(position==2){
                        etSearch.setText("");
                        etSearch.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(6)});
                        etSearch.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                        etSearch.setTransformationMethod(null);
                        intPosition=position;
                    }

                }
                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                    // TODO Auto-generated method stub

                }
            });




    }
    private void getStoreSearchList() {

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
                        SharedPreferences preff = getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                        String id=preff.getString("Customer", null);
                        String s =id;
                        requestObject1.put("ReqMode", "8");
                        requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                        requestObject1.put("StoreName","");
                        requestObject1.put("PinCode","");
                        requestObject1.put("AreaName","");
                        requestObject1.put("FK_Customer",s);
                        SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                        requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                    Call<String> call = apiService.getStoresList(body);
                    call.enqueue(new Callback<String>() {
                        @Override public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                progressDialog.dismiss();
                                JSONObject jObject = new JSONObject(response.body());
                                Log.i("RESS",response.body());
                                JSONObject jobj = jObject.getJSONObject("StoreListDetailsInfo");
                                /* JSONArray jarray = jobj.getJSONArray("SearchedStoreListInfo");*/

                                Log.i("First ", String.valueOf(jobj));
                                JSONObject object = new JSONObject(String.valueOf(jobj));
                                Log.i("First1 ", String.valueOf(object));
                                JSONArray Jarray = object.getJSONArray("OtherStoreListInfo");


                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject jObj = Jarray.getJSONObject(i);
                                    if (!jObj.getString("Lattitude").equals("") &&
                                            !jObj.getString("Longitude").equals(""))
                                    {
                                        double latitude = Double.parseDouble(jObj.getString("Lattitude"));
                                        double longitude = Double.parseDouble(jObj.getString("Longitude"));
                                        String strlatitude=jObj.getString("Lattitude");
                                        String strlongitude=jObj.getString("Longitude");
                                        String  name = jObj.getString("StoreName");
                                        String add = jObj.getString("Address");
                                        //  Toast.makeText(getApplicationContext(), latitude+"\n"+longitude, Toast.LENGTH_LONG).show();
                                        Log.i("LocationDetails", latitude + "" + longitude);
                                        MarkerOptions marker = new MarkerOptions().position(
                                                new LatLng(latitude, longitude)).title(name + ") " + add);
                                        try {
                                            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                            // marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.bankicon));
                                            mMap.addMarker(marker);
                                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                                    .target(new LatLng(11.2406, 75.7909)).zoom(0).build();
                                            mMap.animateCamera(CameraUpdateFactory
                                                    .newCameraPosition(cameraPosition));
                                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                @Override
                                                public void onInfoWindowClick(Marker marker1) {
                                                    // String title = marker1.getTitle();
                                                    // String[] namesList = title.split("\\)");
                                                    //  String id = namesList[0];
                                                    //  String bank = namesList[1];
                                                    //  Toast.makeText(getActivity(), "Marker Clicked"+"\n"+title, Toast.LENGTH_SHORT).show();
                                                    //  showBranchDetails(id);
                                                    // return false;
                                                }
                                            });
                                              /*  map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                    @Override
                                                    public boolean onMarkerClick(Marker marker1) {
                                                        String title = marker1.getTitle();
                                                        String[] namesList = title.split("\\)");
                                                        String id = namesList[0];
                                                        String bank = namesList[1];
                                                        // Toast.makeText(MapsActivity.this, "Marker Clicked"+"\n"+title, Toast.LENGTH_SHORT).show();
                                                        showBranchDetails(id);
                                                        return false;
                                                    }
                                                });*/
                                        } catch (Exception e) {
                                        }
                                    }

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

                Toast.makeText(getApplicationContext(),Somethingwentwrong+"!",Toast.LENGTH_LONG).show();
            }
        }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }

  /*  private void getStoreSearchList(String strStorename, String strAreaName, String strpinNumber) {

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
                            .baseUrl(Config.BASEURL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create(gson))
                            .client(client)
                            .build();
                    ApiInterface apiService = retrofit.create(ApiInterface.class);
                    final JSONObject requestObject1 = new JSONObject();
                    try {
                        SharedPreferences preff = getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                        String id=preff.getString("Customer", null);
                        String s =id;
                        requestObject1.put("ReqMode", "8");
                        requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                        requestObject1.put("StoreName","");
                        requestObject1.put("PinCode","");
                        requestObject1.put("AreaName","");
                        requestObject1.put("FK_Customer",s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                    Call<String> call = apiService.getStoresList(body);
                    call.enqueue(new Callback<String>() {
                        @Override public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                progressDialog.dismiss();
                                JSONObject jObject = new JSONObject(response.body());
                                Log.i("RESS",response.body());
                                JSONObject jobj = jObject.getJSONObject("StoreListDetailsInfo");
                                *//* JSONArray jarray = jobj.getJSONArray("SearchedStoreListInfo");*//*

                                Log.i("First ", String.valueOf(jobj));
                                JSONObject object = new JSONObject(String.valueOf(jobj));
                                Log.i("First1 ", String.valueOf(object));
                                JSONArray Jarray = object.getJSONArray("OtherStoreListInfo");


                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject jObj = Jarray.getJSONObject(i);
                                    if (!jObj.getString("Lattitude").equals("") &&
                                            !jObj.getString("Longitude").equals(""))
                                    {
                                        double latitude = Double.parseDouble(jObj.getString("Lattitude"));
                                        double longitude = Double.parseDouble(jObj.getString("Longitude"));
                                        String strlatitude=jObj.getString("Lattitude");
                                        String strlongitude=jObj.getString("Longitude");
                                        String  name = jObj.getString("StoreName");
                                        String add = jObj.getString("Address");
                                        //  Toast.makeText(getApplicationContext(), latitude+"\n"+longitude, Toast.LENGTH_LONG).show();
                                        Log.i("LocationDetails", latitude + "" + longitude);
                                        MarkerOptions marker = new MarkerOptions().position(
                                                new LatLng(latitude, longitude)).title(name + ") " + add);
                                        try {
                                            marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                            // marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.bankicon));
                                            mMap.addMarker(marker);
                                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                                    .target(new LatLng(11.2406, 75.7909)).zoom(0).build();
                                            mMap.animateCamera(CameraUpdateFactory
                                                    .newCameraPosition(cameraPosition));
                                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                @Override
                                                public void onInfoWindowClick(Marker marker1) {
                                                    // String title = marker1.getTitle();
                                                    // String[] namesList = title.split("\\)");
                                                    //  String id = namesList[0];
                                                    //  String bank = namesList[1];
                                                    //  Toast.makeText(getActivity(), "Marker Clicked"+"\n"+title, Toast.LENGTH_SHORT).show();
                                                    //  showBranchDetails(id);
                                                    // return false;
                                                }
                                            });
                                              *//*  map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                    @Override
                                                    public boolean onMarkerClick(Marker marker1) {
                                                        String title = marker1.getTitle();
                                                        String[] namesList = title.split("\\)");
                                                        String id = namesList[0];
                                                        String bank = namesList[1];
                                                        // Toast.makeText(MapsActivity.this, "Marker Clicked"+"\n"+title, Toast.LENGTH_SHORT).show();
                                                        showBranchDetails(id);
                                                        return false;
                                                    }
                                                });*//*
                                        } catch (Exception e) {
                                        }
                                    }

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
                Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
            }
        }else {
            Intent in = new Intent(this,NoInternetActivity.class);
            startActivity(in);
        }
    }*/

    private void setHomeNavMenu() {

        final String[] menulist = new String[]{"Home","My Cart", "My Orders", "Favourites","Favourite Stores",
                "Notifications", "Shopping List","My Profile", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,R.drawable.ic_store,
                R.drawable.ic_notifications, R.drawable.ic_list,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(MapsActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(MapsActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(MapsActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(MapsActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(MapsActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(MapsActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position ==5) {
                    startActivity(new Intent(MapsActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(MapsActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 7) {
                    startActivity(new Intent(MapsActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 8) {
                    startActivity(new Intent(MapsActivity.this, AboutUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 9) {
                    doLogout();
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 10) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "\t" + "Invite You  \n\n For Android Users \n http://play.google.com/store/apps/details?id=" + getPackageName() +
                            "\n" );
                    startActivity(Intent.createChooser(share, "Invite this App to your friends"));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }
    private void setHomeNavMenu1() {

        final String[] menulist = new String[]{"Home","My Cart", "Track Orders", "Favourite Items",
                "Notifications", "My Profile", "About Us", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications, R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(MapsActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(MapsActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(MapsActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(MapsActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(MapsActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position ==4) {
                    startActivity(new Intent(MapsActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(MapsActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(MapsActivity.this, AboutUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    doLogout();
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 8) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "\t" + "Invite You  \n\n For Android Users \n http://play.google.com/store/apps/details?id=" + getPackageName() +
                            "\n" );
                    startActivity(Intent.createChooser(share, "Invite this App to your friends"));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 9) {
                    startActivity(new Intent(MapsActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 10) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) MapsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater1.inflate(R.layout.rate_us_pop_up, null);
                        final Button rateUs = layout.findViewById(R.id.rate_us);
                        builder.setView(layout);

                        final AlertDialog alertDialog = builder.create();
                        rateUs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                                }
                            }
                        });
                        alertDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (position == 11) {
                    startActivity(new Intent(MapsActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(MapsActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(MapsActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 14) {
                    startActivity(new Intent(MapsActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        //Initialize Google Play Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                    mLocationRequest, this);
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
//Showing Current Location Marker on Map
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location locations = locationManager.getLastKnownLocation(provider);
        List<String> providerList = locationManager.getAllProviders();
        if (null != locations && null != providerList && providerList.size() > 0) {
            double longitude = locations.getLongitude();
            double latitude = locations.getLatitude();
            Geocoder geocoder = new Geocoder(getApplicationContext(),
                    Locale.getDefault());
            try {
                List<Address> listAddresses = geocoder.getFromLocation(latitude,
                        longitude, 1);
                if (null != listAddresses && listAddresses.size() > 0) {
                    String state = listAddresses.get(0).getAdminArea();
                    String country = listAddresses.get(0).getCountryName();
                    String subLocality = listAddresses.get(0).getSubLocality();
                    markerOptions.title("" + latLng + "," + subLocality + "," + state
                            + "," + country);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        mCurrLocationMarker = mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                    this);
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }
    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                } else {

                    SharedPreferences permissiondeniedsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF77, 0);
                    String permissiondenied = permissiondeniedsp.getString("permissiondenied","");
                    Toast.makeText(this, permissiondenied+".", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }

    }
    private void initiateViews() {
        tv_header =(TextView)findViewById(R.id.tv_header) ;
        ib_search = findViewById(R.id.ib_search);
        etSearch= findViewById(R.id.etSearch);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        im = (ImageView) findViewById(R.id.im);
        imcart = (ImageView) findViewById(R.id.imcart);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        tvcart = (TextView) findViewById(R.id.tvcart);
        search_spinner = findViewById(R.id.search_spinner);
    }
    private void setRegViews() {
        ib_search.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        im.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        imcart.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_search:
               // String area = etSearch.getText().toString();
               // getStoreSearchList(area);
                hideKeyboard();
                if(intPosition==0){
                    if(etSearch.getText().toString().length()<=0){
                        //etSearch.setError("Enter Store Name");
                        SharedPreferences EnterStore = getApplicationContext().getSharedPreferences(Config.SHARED_PREF248, 0);
                        etSearch.setError(""+EnterStore.getString("EnterStore",""));
                    }else {
                        strStorename = etSearch.getText().toString();
                        getStoreList(strStorename, strAreaName, strpinNumber);
                    }
                }
                if(intPosition==1){
                    if(etSearch.getText().toString().length()<=0){
                      //  etSearch.setError("Enter Area");
                        SharedPreferences EnterArea = getApplicationContext().getSharedPreferences(Config.SHARED_PREF249, 0);
                        etSearch.setError(""+EnterArea.getString("EnterArea",""));

                    }else {
                        strAreaName = etSearch.getText().toString();
                        getStoreList(strStorename, strAreaName, strpinNumber);
                    }
                }
                if(intPosition==2){
                    if(etSearch.getText().toString().length()<=0){
                      //  etSearch.setError("Enter Pincode");
                        SharedPreferences EnterPincode = getApplicationContext().getSharedPreferences(Config.SHARED_PREF250, 0);
                        etSearch.setError(""+EnterPincode.getString("EnterPincode",""));

                    }else {
                        strpinNumber = etSearch.getText().toString();
                        getStoreList(strStorename, strAreaName, strpinNumber);
                    }
                }
                break;


            case R.id.etSearch:
               // getAreaDialog();
                break;

            case R.id.im:

                //  drawer.openDrawer(Gravity.START);
                onBackPressed();                break;
            case R.id.tvcart:
                Intent i = new Intent(MapsActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.imcart:
                Intent in = new Intent(MapsActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.tv_notification:
                startActivity(new Intent(MapsActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(MapsActivity.this, NotificationActivity.class));
                break;
        }

    }

    private void hideKeyboard() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
    }

    private void getAreaDialog() {

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater1 = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.area_popup, null);
            list_view = (ListView) layout.findViewById(R.id.list_view);
            etsearch = (EditText) layout.findViewById(R.id.etsearch);
            tv_popuptitle = (TextView) layout.findViewById(R.id.tv_popuptitle);

            SharedPreferences selectarea = getApplicationContext().getSharedPreferences(Config.SHARED_PREF224, 0);
            tv_popuptitle.setText(selectarea.getString("selectarea", null));
//            tv_popuptitle.setText("Select Area");
            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            getArea(alertDialog);
            alertDialog.show();
        }catch (Exception e){e.printStackTrace();}

    }


  /*  private void getStoreSearchListgetStoreSearchList(String area) {

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
                                .baseUrl(Config.BASEURL)
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .client(client)
                                .build();
                        ApiInterface apiService = retrofit.create(ApiInterface.class);
                        final JSONObject requestObject1 = new JSONObject();
                        try {
                            SharedPreferences preff = getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                            String id=preff.getString("Customer", null);
                            String s =id;
                            requestObject1.put("ReqMode", "5");
                            requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                            requestObject1.put("StoreName","");
                            requestObject1.put("PinCode","");
                            requestObject1.put("AreaName",area);
                            requestObject1.put("FK_Customer",s);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                        Call<String> call = apiService.getStores(body);
                        call.enqueue(new Callback<String>() {
                            @Override public void onResponse(Call<String> call, Response<String> response) {
                                try {
                                    progressDialog.dismiss();
                                    JSONObject jObject = new JSONObject(response.body());
                                    Log.i("RESS",response.body());
                                    JSONObject jobj = jObject.getJSONObject("StoreBindListInfo");

                                    Log.i("First ", String.valueOf(jobj));
                                    JSONObject object = new JSONObject(String.valueOf(jobj));
                                    Log.i("First1 ", String.valueOf(object));
                                    JSONArray Jarray = object.getJSONArray("storeListInfos");


                                    for (int i = 0; i < Jarray.length(); i++) {
                                        JSONObject jObj = Jarray.getJSONObject(i);
                                        if (!jObj.getString("Lattitude").equals("") &&
                                                !jObj.getString("Longitude").equals(""))
                                        {
                                            double latitude = Double.parseDouble(jObj.getString("Lattitude"));
                                            double longitude = Double.parseDouble(jObj.getString("Longitude"));
                                            String strlatitude=jObj.getString("Lattitude");
                                            String strlongitude=jObj.getString("Longitude");
                                            String  name = jObj.getString("StoreName");
                                            String add = jObj.getString("Address");
                                            //  Toast.makeText(getApplicationContext(), latitude+"\n"+longitude, Toast.LENGTH_LONG).show();
                                            Log.i("LocationDetails", latitude + "" + longitude);

                                            MarkerOptions marker = new MarkerOptions().position(
                                                    new LatLng(latitude, longitude)).title(name + ") " + add);
                                            try {
                                                 marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                                                mMap.clear();
                                                mMap.addMarker(marker);
                                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                                          .target(new LatLng(11.2406, 75.7909)).zoom(0).build();
                                                mMap.animateCamera(CameraUpdateFactory
                                                        .newCameraPosition(cameraPosition));
                                                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                    @Override
                                                    public void onInfoWindowClick(Marker marker1) {
                                                    }
                                                });
                                            } catch (Exception e) {
                                            }
                                        }

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
                    Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show();
                }
            }else {
                Intent in = new Intent(this,NoInternetActivity.class);
                startActivity(in);
            }
        }*/
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

    public void getArea(final AlertDialog alertDialog){
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
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                    String idstore = pref1.getString("ID_Store", null);
                    requestObject1.put("ReqMode", "15");
                    requestObject1.put("FK_Store", idstore);
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getArea(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, Response<String> response) {
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
                                sadapter = new AreaListAdapter(MapsActivity.this, array_sort);
                                list_view.setAdapter(sadapter);
                                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                     @Override
                                                                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                         etSearch.setText(array_sort.get(position).getAreaName());
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
                                    sadapter = new AreaListAdapter(MapsActivity.this, array_sort);
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
            Toast.makeText(this, Nointernetconnection.getString("Nointernetconnection",""), Toast.LENGTH_SHORT).show();
        }
    }
    public void getStoreList(String storename, String areaname, String pin){
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
                    requestObject1.put("ReqMode", "8");
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    requestObject1.put("StoreName",storename);
                    requestObject1.put("AreaName",areaname);
                    requestObject1.put("PinCode", pin);
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getStores(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, Response<String> response) {
                        try {

                            // progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("StoreBindListInfo");
                            Log.i("STOREResponse",response.body());
                            JSONObject object = new JSONObject(String.valueOf(jobj));
                            Log.i("First1 ", String.valueOf(object));
                            JSONArray Jarray = object.getJSONArray("storeListInfos");

                            for (int i = 0; i < Jarray.length(); i++) {
                                JSONObject jObj = Jarray.getJSONObject(i);
                                if (!jObj.getString("Lattitude").equals("") &&
                                        !jObj.getString("Longitude").equals(""))
                                {
                                    double latitude = Double.parseDouble(jObj.getString("Lattitude"));
                                    double longitude = Double.parseDouble(jObj.getString("Longitude"));
                                    String strlatitude=jObj.getString("Lattitude");
                                    String strlongitude=jObj.getString("Longitude");
                                    String  name = jObj.getString("StoreName");
                                    String add = jObj.getString("Address");
                                    //  Toast.makeText(getApplicationContext(), latitude+"\n"+longitude, Toast.LENGTH_LONG).show();
                                    Log.i("LocationDetails", latitude + "" + longitude);
                                    MarkerOptions marker = new MarkerOptions().position(
                                            new LatLng(latitude, longitude)).title(name + ") " + add);
                                    try {
                                        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                        // marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.bankicon));
                                        mMap.clear();
                                        mMap.addMarker(marker);
                                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                                .target(new LatLng(11.2406, 75.7909)).zoom(0).build();
                                        mMap.animateCamera(CameraUpdateFactory
                                                .newCameraPosition(cameraPosition));
                                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                            @Override
                                            public void onInfoWindowClick(Marker marker1) {
                                                // String title = marker1.getTitle();
                                                // String[] namesList = title.split("\\)");
                                                //  String id = namesList[0];
                                                //  String bank = namesList[1];
                                                //  Toast.makeText(getActivity(), "Marker Clicked"+"\n"+title, Toast.LENGTH_SHORT).show();
                                                //  showBranchDetails(id);
                                                // return false;
                                            }
                                        });
                                              /*  map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                                    @Override
                                                    public boolean onMarkerClick(Marker marker1) {
                                                        String title = marker1.getTitle();
                                                        String[] namesList = title.split("\\)");
                                                        String id = namesList[0];
                                                        String bank = namesList[1];
                                                        // Toast.makeText(MapsActivity.this, "Marker Clicked"+"\n"+title, Toast.LENGTH_SHORT).show();
                                                        showBranchDetails(id);
                                                        return false;
                                                    }
                                                });*/
                                    } catch (Exception e) {
                                    }
                                }

                            }


                          /*  if(jarray.length()==1){
                                JSONObject jobjt = jarray.getJSONObject(0);
                                String strID_Store = jobjt.getString("ID_Store");
                                String strStoreName = jobjt.getString("StoreName");
                                SharedPreferences ID_Store = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                                SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                ID_Storeeditor.putString("ID_Store", strID_Store);
                                ID_Storeeditor.commit();
                                SharedPreferences StoreName = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                StoreNameeditor.putString("StoreName", strStoreName);
                                StoreNameeditor.commit();
                                Intent intent = new Intent(StoreActivity.this, OutShopActivity.class);
                                startActivity(intent);
                            }else {
                                GridLayoutManager lLayout = new GridLayoutManager(StoreActivity.this, 1);
                                rv_storeist.setLayoutManager(lLayout);
                                rv_storeist.setHasFixedSize(true);
                                StoreAdapter adapter = new StoreAdapter(StoreActivity.this, jarray);
                                rv_storeist.setAdapter(adapter);
                            }*/

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
            Toast.makeText(this, Nointernetconnection.getString("Nointernetconnection",""), Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MapsActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) MapsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.logout_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);
            TextView tv_popupdelete= (TextView) layout.findViewById(R.id.tv_popupdelete);
            TextView tv_popupdeleteinfo3= (TextView) layout.findViewById(R.id.tv_popupdeleteinfo3);
            TextView edit= (TextView) layout.findViewById(R.id.edit);
            TextView tv_no= (TextView) layout.findViewById(R.id.tv_no);



            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();

            SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF241, 0);
            tv_popupdelete.setText(pref4.getString("Logoutaccount", null));

            SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF242, 0);
            tv_popupdeleteinfo3.setText(pref5.getString("areyousureyouwanttologout", null));

            SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF186, 0);
            edit.setText(pref6.getString("yes", null));

            SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF187, 0);
            tv_no.setText(pref7.getString("no", null));

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
                    dologoutchanges();

                    if(getResources().getString(R.string.app_name).equals("JZT CART") || getResources().getString(R.string.app_name).equals("Pulikkottil Hypermarket") || getResources().getString(R.string.app_name).equals("NeethiMed")|| getResources().getString(R.string.app_name).equals("Touch n Buy")) {

                        startActivity(new Intent(MapsActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(MapsActivity.this,LocationActivity.class));
                        finish();}
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

    private void dologoutchanges(){
        Config.logOut(this);
    }
}
