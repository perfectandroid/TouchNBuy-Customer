package com.perfect.easyshopplus.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Activity.AddressAddActivty;
import com.perfect.easyshopplus.Activity.AddressListActivity;
import com.perfect.easyshopplus.Activity.CheckoutReorderHomeDeliveryActivity;
import com.perfect.easyshopplus.Activity.CheckoutShoppinglistAddressAddActivty;
import com.perfect.easyshopplus.Activity.NoInternetActivity;
import com.perfect.easyshopplus.Model.AreaModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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

public class AddressListAdapter extends RecyclerView.Adapter {
    boolean changing = false;
    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    ProgressDialog progressDialog;

    ListView list_view;
    EditText etsearch;
    AreaListAdapter sadapter;
    TextView tv_popuptitle;
    int textlength = 0;
    ArrayList<AreaModel> areaArrayList = new ArrayList<>();
    public static ArrayList<AreaModel> array_sort= new ArrayList<>();
    String areaId, idstore="null", From,Image;
    String Id_order, orderDate, deliveryDate, status, order_id, ShopType, ID_Store, itemcount, ID_SalesOrder, storeName,DeliveryCharge,OrderType, ID_CustomerAddress;
    String strfileimage = "null";
    private File fileimage = null;

    public AddressListAdapter(Context context, JSONArray jsonArray, String From,
                              String ID_SalesOrder, String order_id, String deliveryDate,
                              String Id_order, String orderDate,
                              String status, String ID_Store, String ShopType,
                              String itemcount, String ID_CustomerAddress, String OrderType,
                              String storeName, String DeliveryCharge, File fileimage) {



        this.context=context;
        this.jsonArray=jsonArray;
        this.From=From;
        this.ID_Store=ID_Store;
        this.ID_CustomerAddress=ID_CustomerAddress;
        this.OrderType=OrderType;
        this.DeliveryCharge=DeliveryCharge;
        this.ShopType=ShopType;
        this.ID_SalesOrder=ID_SalesOrder;
        this.Id_order=Id_order;
        this.orderDate=orderDate;
        this.deliveryDate=deliveryDate;
        this.status=status;
        this.order_id=order_id;
        this.itemcount=itemcount;
        this.storeName=storeName;
        this.fileimage = fileimage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.address_list_adapter, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            jsonObject=jsonArray.getJSONObject(position);



            if (holder instanceof MainViewHolder) {
             /*   ((MainViewHolder)holder).tvAddress.setText("Name : "+jsonObject.getString("Name")+"\nAddress : "+jsonObject.getString("Address")+
                        "\n\t\t\tPin : "+jsonObject.getString("Pincode")+
                        "\nLandMark : "+jsonObject.getString("LandMark")+
                        "\nArea : "+jsonObject.getString("AreaName")+
                        "\nPhone Number : "+jsonObject.getString("MobileNumber"));*/

                SharedPreferences addressSP = context.getSharedPreferences(Config.SHARED_PREF94, 0);
                String address = addressSP.getString("address", null);

                SharedPreferences CustomerNamesp = context.getSharedPreferences(Config.SHARED_PREF88, 0);
                String CustomerName = CustomerNamesp.getString("CustomerName", null);

                SharedPreferences Landmarksp = context.getSharedPreferences(Config.SHARED_PREF96, 0);
                String Landmark = Landmarksp.getString("LandmarkS", null);

                SharedPreferences areasp = context.getSharedPreferences(Config.SHARED_PREF223, 0);
                String area = areasp.getString("area", null);

                SharedPreferences PhoneNosp = context.getSharedPreferences(Config.SHARED_PREF276, 0);
                String PhoneNo = PhoneNosp.getString("PhoneNo", null);

                if (jsonObject.getString("MobileNumber").length()!=0){
                    ((MainViewHolder)holder).tvAddress.setText(CustomerName+ " : "+jsonObject.getString("Name")+"\n"+address +" : "+jsonObject.getString("Address")+
//                        "\n\t\t\tPin : "+jsonObject.getString("Pincode")+
                            "\n"+ Landmark+" : "+jsonObject.getString("LandMark")+
                            "\n"+area +" : "+jsonObject.getString("AreaName")+
                            "\n"+PhoneNo +" : "+jsonObject.getString("MobileNumber"));
                }
                else{
                    ((MainViewHolder)holder).tvAddress.setText(CustomerName+ " : "+jsonObject.getString("Name")+"\n"+address +" : "+jsonObject.getString("Address")+
//                        "\n\t\t\tPin : "+jsonObject.getString("Pincode")+
                            "\n"+ Landmark+" : "+jsonObject.getString("LandMark")+
                            "\n"+area +" : "+jsonObject.getString("AreaName"));
                }
                ((MainViewHolder)holder).lnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                ((MainViewHolder)holder).addressSelection.setTag(position);
                ((MainViewHolder)holder).addressSelection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            jsonObject=jsonArray.getJSONObject(position);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        SharedPreferences name = context.getSharedPreferences(Config.SHARED_PREF18, 0);
                        SharedPreferences.Editor nameeditor = name.edit();
                        try {
                            nameeditor.putString("DeliUsername", jsonObject.getString("Name"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        nameeditor.commit();
                        SharedPreferences Address = context.getSharedPreferences(Config.SHARED_PREF19, 0);
                        SharedPreferences.Editor Addresseditor = Address.edit();
                        try {
                            Addresseditor.putString("DeliAddress", jsonObject.getString("Address"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Addresseditor.commit();
                        SharedPreferences AddressId = context.getSharedPreferences(Config.SHARED_PREF20, 0);
                        SharedPreferences.Editor AddresseditorId = AddressId.edit();
                        try {
                            AddresseditorId.putString("DeliAddressID", jsonObject.getString("ID_CustomerAddress"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AddresseditorId.commit();

                        SharedPreferences pincode = context.getSharedPreferences(Config.SHARED_PREF21, 0);
                        SharedPreferences.Editor pincodeeditor = pincode.edit();
                        try {
                            pincodeeditor.putString("DeliPincode", jsonObject.getString("Pincode"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pincodeeditor.commit();
                        SharedPreferences area = context.getSharedPreferences(Config.SHARED_PREF22, 0);
                        SharedPreferences.Editor areaeditor = area.edit();
                        try {
                            areaeditor.putString("DeliArea", jsonObject.getString("AreaName"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        areaeditor.commit();
                        SharedPreferences AreaId = context.getSharedPreferences(Config.SHARED_PREF23, 0);
                        SharedPreferences.Editor AreaIdeditor = AreaId.edit();
                        try {
                            AreaIdeditor.putString("DeliAreaID", jsonObject.getString("FK_Area"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AreaIdeditor.commit();
                        SharedPreferences landmark = context.getSharedPreferences(Config.SHARED_PREF24, 0);
                        SharedPreferences.Editor landmarkeditor = landmark.edit();
                        try {
                            landmarkeditor.putString("DeliLandmark", jsonObject.getString("LandMark"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        landmarkeditor.commit();
                        SharedPreferences mobNumb = context.getSharedPreferences(Config.SHARED_PREF25, 0);
                        SharedPreferences.Editor mobNumbeditor = mobNumb.edit();
                        try {
                            mobNumbeditor.putString("DeliMobNumb", jsonObject.getString("MobileNumber"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mobNumbeditor.commit();


                        SharedPreferences DeliveryCharge = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF46, 0);
                        SharedPreferences.Editor DeliveryChargeeditor = DeliveryCharge.edit();
                        try {
                            DeliveryChargeeditor.putString("DeliveryCharge", jsonObject.getString("DeliveryCharge"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        DeliveryChargeeditor.commit();

                        if(From.equals("reorder")) {
                           // context. startActivity(new Intent(context, CheckoutReorderHomeDeliveryActivity.class));
                            Intent intn = new Intent(context, CheckoutReorderHomeDeliveryActivity.class);
//                            intn.putExtra("ID_Store", ID_Store);
//                            intn.putExtra("ShopType", ShopType);
//                            intn.putExtra("OrderType", "1");
//                            intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);


                            intn.putExtra("order_id", order_id);
                            intn.putExtra("deliveryDate", deliveryDate);
                            intn.putExtra("Id_order", Id_order);
                            intn.putExtra("orderDate", orderDate);
                            intn.putExtra("status", status);
                            intn.putExtra("ID_Store", ID_Store);
                            intn.putExtra("ShopType", ShopType);
                            intn.putExtra("itemcount", itemcount);
                            intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                            intn.putExtra("OrderType", OrderType);
                            intn.putExtra("storeName", storeName);

                            try {
                                intn.putExtra("DeliveryCharge", jsonObject.getString("DeliveryCharge"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            intn.putExtra("Image", Image);
                            intn.putExtra("destination", fileimage);
                            context.startActivity(intn);
                        }else if(From.equals("shopping")) {
                            Intent intn = new Intent(context, CheckoutShoppinglistAddressAddActivty.class);
                            intn.putExtra("Image", Image);
                            intn.putExtra("destination", fileimage);
                            context.startActivity(intn);

                        }else{
                            Intent intn = new Intent(context, AddressAddActivty.class);
                            intn.putExtra("destination", fileimage);
                            context.startActivity(intn);

//                            context. startActivity(new Intent(context, AddressAddActivty.class));

                        }
                        /*
                        context.startActivity(new Intent(context, AddressAddActivty.class));
                        ((Activity)context).finish();*/



                    }
                });
                ((MainViewHolder)holder).ivdelete.setTag(position);
                ((MainViewHolder)holder).ivdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject=jsonArray.getJSONObject(position);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            deleteAddress(jsonObject.getString("ID_CustomerAddress"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ((MainViewHolder) holder).ivEdit.setTag(position);
                ((MainViewHolder) holder).ivEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject=jsonArray.getJSONObject(position);

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View layout = inflater1.inflate(R.layout.add_edit_address_popup, null);



                            SharedPreferences editadress = context.getSharedPreferences(Config.SHARED_PREF204, 0);
                            SharedPreferences addressSP = context.getSharedPreferences(Config.SHARED_PREF94, 0);
                            SharedPreferences CustomerNamesp = context.getSharedPreferences(Config.SHARED_PREF88, 0);
                            SharedPreferences Landmarksp = context.getSharedPreferences(Config.SHARED_PREF96, 0);
                            SharedPreferences areasp = context.getSharedPreferences(Config.SHARED_PREF223, 0);
                            SharedPreferences PhoneNosp = context.getSharedPreferences(Config.SHARED_PREF276, 0);

                            SharedPreferences Getlocation = context.getSharedPreferences(Config.SHARED_PREF273, 0);
                            SharedPreferences OK = context.getSharedPreferences(Config.SHARED_PREF104, 0);
                            SharedPreferences Cancel = context.getSharedPreferences(Config.SHARED_PREF105, 0);


                            final TextView tv_popupchange = layout.findViewById(R.id.tv_popupchange);
                            tv_popupchange.setText(editadress.getString("editadress", null));

                            final EditText name = (EditText) layout.findViewById(R.id.etName);
                            name.setHint(CustomerNamesp.getString("CustomerName", null));

                            final EditText address = (EditText) layout.findViewById(R.id.etaddress);
                            address.setHint(addressSP.getString("address", null));

                            final EditText pinCode = (EditText) layout.findViewById(R.id.etPincode);
                            final EditText landmark = (EditText) layout.findViewById(R.id.etLandmark);
                            landmark.setHint(Landmarksp.getString("LandmarkS", null));

                            final EditText area = (EditText) layout.findViewById(R.id.etArea);
                            area.setHint(areasp.getString("area", null));

                            final EditText phoneNumber = (EditText) layout.findViewById(R.id.etPhoneNumber);
                            phoneNumber.setHint(PhoneNosp.getString("PhoneNo", null));

                            final TextView pop_getlocation = layout.findViewById(R.id.pop_getlocation);
                            pop_getlocation.setText(Getlocation.getString("Getlocation", null));

                            final Button pop_changecancel = layout.findViewById(R.id.pop_changecancel);
                            pop_changecancel.setText(Cancel.getString("Cancel", null));

                            final Button btn_changesave = layout.findViewById(R.id.btn_changesave);
                            btn_changesave.setText(OK.getString("OK", null));

//                            final TextView header = (TextView) layout.findViewById(R.id.tv_popupchange);
//                            header.setText("EDIT ADDRESS");

                            name.setText(jsonObject.getString("Name"));
                            address.setText(jsonObject.getString("Address"));
                            pinCode.setText(jsonObject.getString("Pincode"));
                            area.setText(jsonObject.getString("AreaName"));
                            areaId = jsonObject.getString("FK_Area");
                            landmark.setText(jsonObject.getString("LandMark"));
                            phoneNumber.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void afterTextChanged(Editable s) {
                                    if (!changing && phoneNumber.getText().toString().startsWith("0")){
                                        changing = true;
                                        phoneNumber.setText(phoneNumber.getText().toString().replace("0", ""));
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
                            phoneNumber.setText(jsonObject.getString("MobileNumber"));

//                            Button pop_getlocation = (Button) layout.findViewById(R.id.pop_getlocation);
//                            Button ok = (Button) layout.findViewById(R.id.btn_changesave);
//                            Button cancel = (Button) layout.findViewById(R.id.pop_changecancel);
                            builder.setView(layout);
                            final AlertDialog alertDialog = builder.create();

                            area.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                   getArea(area);
                                }
                            });
                            pop_getlocation.setVisibility(View.GONE);
                            pop_changecancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    alertDialog.dismiss();
                                }
                            });
                            btn_changesave.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(name.getText().toString().length()!=0){
                                        if(address.getText().toString().length()!=0){
                                           // if(pinCode.getText().toString().length()==6){
                                                if(area.getText().toString().length()!=0){
                                                    if(landmark.getText().toString().length()!=0){
                                                        if(phoneNumber.getText().toString().length()!=0){
                                                           // if(phoneNumber.getText().toString().length()==10){
                                                                alertDialog.dismiss();
                                                                try {
                                                                    String phone = phoneNumber.getText().toString();
                                                                    if(phone.contains("+")){
                                                                        phone = phone.replace("+","");
                                                                    }
                                                                    String  name1 = name.getText().toString().replaceAll("\\s+", " ");
                                                                    String  adrress1 = address.getText().toString().replaceAll("\\s+", " ");
                                                                    String  pinCode1 = pinCode.getText().toString().replaceAll("\\s+", " ");
                                                                    String  landmark1 = landmark.getText().toString().replaceAll("\\s+", " ");
//                                                                    editAddress(jsonObject.getString("ID_CustomerAddress"),name.getText().toString(),
//                                                                            adrress1,pinCode.getText().toString(),
//                                                                            areaId,landmark.getText().toString(),phone);

                                                                    editAddress(jsonObject.getString("ID_CustomerAddress"),name1,adrress1,pinCode1,areaId, landmark1,phone);
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                           /* }
                                                            else{
                                                                phoneNumber.setError("Please enter 10 digit mobile number.");
                                                            }*/
                                                        }
                                                        else{
                                                            alertDialog.dismiss();
                                                            try {
                                                                String phone = phoneNumber.getText().toString();
                                                                if(phone.contains("+")){
                                                                    phone = phone.replace("+","");
                                                                }
                                                                String  name1 = name.getText().toString().replaceAll("\\s+", " ");
                                                                String  adrress1 = address.getText().toString().replaceAll("\\s+", " ");
                                                                String  pinCode1 = pinCode.getText().toString().replaceAll("\\s+", " ");
                                                                String  landmark1 = landmark.getText().toString().replaceAll("\\s+", " ");
//                                                                editAddress(jsonObject.getString("ID_CustomerAddress"),name.getText().toString(),
//                                                                        adrress1,pinCode.getText().toString(),
//                                                                        areaId,landmark.getText().toString(),phone);

                                                                editAddress(jsonObject.getString("ID_CustomerAddress"),name1,adrress1,pinCode1,areaId, landmark1,phone);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }                                              }
                                                    }
                                                    else{
                                                        SharedPreferences Pleaseenterlandmark = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF297, 0);
                                                        landmark.setError(""+Pleaseenterlandmark.getString("Pleaseenterlandmark",null));
                                                    }
                                                }
                                                else {
                                                    SharedPreferences Pleaseselectarea = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF296, 0);
                                                    area.setError("" + Pleaseselectarea.getString("Pleaseselectarea", null));
                                                }
//                                                        landmark.setError("Please enter landmark.");
//                                                    }
//                                                }
//                                                else{
//                                                    area.setError("Please select area.");
//                                                }
                                          /*  }
                                            else{
                                                pinCode.setError("Please enter valid pin code.");
                                            }*/

                                        }
                                        else{
                                            SharedPreferences Pleaseenteraddress = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF295, 0);
                                            address.setError(""+Pleaseenteraddress.getString("Pleaseenteraddress",null));
                                        }
                                    }
                                    else{
                                        SharedPreferences Pleaseentername = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF294, 0);
                                        name.setError(""+Pleaseentername.getString("Pleaseentername",null));
                                    }
//                                        }
//                                        else{
//                                            address.setError("Please enter address.");
//                                        }
//                                    }
//                                    else{
//                                        name.setError("Please enter name.");
//                                    }
                                 /*   alertDialog.dismiss();
                                    try {
                                        editAddress(jsonObject.getString("ID_CustomerAddress"),name.getText().toString(),address.getText().toString(),pinCode.getText().toString(),
                                                areaId,landmark.getText().toString(),phoneNumber.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }*/
                                }
                            });
                            alertDialog.getWindow().setSoftInputMode(
                                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                            alertDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void editAddress(String addressId, String name,String addres, String pincode, String areaId, String landmark, String phoneNumb){

        SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        if (new InternetUtil(context).isInternetOn()) {
            progressDialog = new ProgressDialog(context, R.style.Progress);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(context.getResources()
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


                    requestObject1.put("ReqMode", "18");
                    requestObject1.put("ReqSubMode", "2");
                    requestObject1.put("ID_CustomerAddress", addressId);
                    requestObject1.put("CustomerAddress", addres);
                    requestObject1.put("LandMark", landmark);
                    SharedPreferences pref1 = context.getSharedPreferences(Config.SHARED_PREF1, 0);
                    requestObject1.put("FK_Customer", pref1.getString("userid", null));
                    requestObject1.put("FK_Area",areaId);
                    requestObject1.put("PinCode",pincode);
                    requestObject1.put("ContactName",name);
                    requestObject1.put("ContactNumber",phoneNumb);
                    requestObject1.put("Bank_Key", context.getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = context.getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getCustmerAddressChangeActions(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jmember = jObject.getJSONObject("CustomerAddressDetails");
                            String jstatus = jObject.getString("StatusCode");
                            if(jstatus.equals("0")){
                                if (context instanceof AddressListActivity) {
                                    ((AddressListActivity)context).getAddressList();
                                }
                            }
                            else {
                                if (context instanceof AddressListActivity) {
                                    ((AddressListActivity)context).getAddressList();
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
        }else {
            Intent in = new Intent(context,NoInternetActivity.class);
            context.startActivity(in);
        }
    }


    private void getArea(EditText area) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.area_popup, null);
            list_view = (ListView) layout.findViewById(R.id.list_view);
            etsearch = (EditText) layout.findViewById(R.id.etsearch);
            tv_popuptitle = (TextView) layout.findViewById(R.id.tv_popuptitle);

            SharedPreferences selectarea = context.getSharedPreferences(Config.SHARED_PREF224, 0);
            tv_popuptitle.setText(selectarea.getString("selectarea", null));

//            tv_popuptitle.setText("Select Area");
            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            getAreaList(alertDialog, area);
            alertDialog.show();
        }catch (Exception e){e.printStackTrace();}
    }

    public void getAreaList(final AlertDialog alertDialog, final EditText area){
        SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(context).isInternetOn()) {
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
                    SharedPreferences pref1 = context.getSharedPreferences(Config.SHARED_PREF7, 0);
                    idstore = pref1.getString("ID_Store", null);
                    requestObject1.put("ReqMode", "15");
                    requestObject1.put("FK_Store", idstore);
                    requestObject1.put("Bank_Key", context.getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = context.getSharedPreferences(Config.SHARED_PREF80, 0);
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
                                sadapter = new AreaListAdapter(context, array_sort);
                                list_view.setAdapter(sadapter);
                                list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                                     @Override
                                                                     public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                                         area.setText(array_sort.get(position).getAreaName());
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
                                    sadapter = new AreaListAdapter(context, array_sort);
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
            SharedPreferences Nointernetconnection = context.getSharedPreferences(Config.SHARED_PREF282, 0);
            Toast.makeText(context, Nointernetconnection.getString("Nointernetconnection",""), Toast.LENGTH_SHORT).show();

//            Toast.makeText(context,"No internet connection",Toast.LENGTH_SHORT).show();
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
        LinearLayout lnLayout,addressSelection;
        TextView tvAddress;
        ImageView ivdelete, ivEdit;
        public MainViewHolder(View v) {
            super(v);
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            addressSelection=(LinearLayout) v.findViewById(R.id.addressSelection);
            tvAddress=(TextView)v.findViewById(R.id.tvAddress);
            ivdelete=(ImageView)v.findViewById(R.id.ivdelete);
            ivEdit=(ImageView)v.findViewById(R.id.ivEdit);
        }
    }



    private void deleteAddress(String ID_CustomerAddress){
        SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(context).isInternetOn()) {
            progressDialog = new ProgressDialog(context, R.style.Progress);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.setIndeterminateDrawable(context.getResources()
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
                    requestObject1.put("ReqMode", "18");
                    requestObject1.put("ReqSubMode", "1");
                    requestObject1.put("ID_CustomerAddress", ID_CustomerAddress);
                    requestObject1.put("FK_Area", "1");
                    SharedPreferences pref1 = context.getSharedPreferences(Config.SHARED_PREF1, 0);
                    requestObject1.put("FK_Customer", pref1.getString("userid", null));
                    requestObject1.put("Bank_Key", context.getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = context.getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getCustmerAddressChangeActions(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jmember = jObject.getJSONObject("CustomerAddressDetails");
                            String jstatus = jObject.getString("StatusCode");
//                            JSONArray CustomerAddressList = jmember.getJSONArray("CustomerAddressList");
                            if(jstatus.equals("0")){

                                if (context instanceof AddressListActivity) {
                                    ((AddressListActivity)context).getAddressList();
                                }
                            }
                            else if (jstatus.equals("3")) {

                                if (context instanceof AddressListActivity) {
                                    ((AddressListActivity)context).getAddressList();
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
            }
            catch (Exception e) {
                e.printStackTrace();
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
