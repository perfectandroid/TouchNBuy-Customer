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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.perfect.easyshopplus.Activity.HomeActivity;
import com.perfect.easyshopplus.Activity.OutShopActivity;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.FavStoreModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static android.content.Context.MODE_PRIVATE;

public class StoreAdapter extends RecyclerView.Adapter {

    String TAG = "StoreAdapter";
    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    boolean isFavorite;
    DBHandler db;

    public StoreAdapter(Context context, JSONArray jsonArray) {
        this.context=context;
        this.jsonArray=jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout./*activity_store_list*/storesearch_list, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            jsonObject=jsonArray.getJSONObject(position);
            if (holder instanceof MainViewHolder) {
                String string = "\u20B9";
                //  "HomeDelivery": false

                Log.e(TAG,"onBindViewHolder   63    "+jsonObject.getString("StoreName")+"    "+jsonArray.length());
                ((MainViewHolder) holder).tvPrdName.setText(jsonObject.getString("StoreName"));
                ((MainViewHolder) holder).tvAddress.setText(jsonObject.getString("Address"));
                DecimalFormat f = new DecimalFormat("##.00");
               ((MainViewHolder) holder).tvtime.setText(jsonObject.getString("WorkingHours"));
                if(jsonObject.getString("StoreRate").isEmpty()){
                    ((MainViewHolder) holder).txtvRating.setVisibility(View.GONE);
                }else{
                   // ((MainViewHolder)holder).txtvRating.setText(jsonObject.getString("StoreRate"));
                    if(jsonObject.getString("StoreRate").equals("5")){  ((MainViewHolder)holder).txtvRating.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.str1) );}
                    else if(jsonObject.getString("StoreRate").equals("4")){  ((MainViewHolder)holder).txtvRating.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.str2) );}
                    else if(jsonObject.getString("StoreRate").equals("3")){  ((MainViewHolder)holder).txtvRating.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.str3) );}
                    else if(jsonObject.getString("StoreRate").equals("2")){  ((MainViewHolder)holder).txtvRating.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.str4) );}
                    else if(jsonObject.getString("StoreRate").equals("1")){  ((MainViewHolder)holder).txtvRating.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.str5) );}
                    else {((MainViewHolder)holder).txtvRating.setBackgroundDrawable( context.getResources().getDrawable(R.drawable.str6) ); }                 }
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
                if(jsonObject.getString("HomeDelivery").equals("false")){
                    ((MainViewHolder) holder).tvMinamt.setVisibility(View.GONE);
                    ((MainViewHolder)holder).ivHomedelivery.setVisibility(View.INVISIBLE);
                }else {
                    SharedPreferences MinDeliveryAmt = context.getSharedPreferences(Config.SHARED_PREF277, 0);
//                    ((MainViewHolder) holder).tvMinamt.setText("Min. Delivery Amt : " /*+ string + " "*/ + f.format(Double.parseDouble(jsonObject.getString("MinimumDeliveryAmount"))));
                    ((MainViewHolder) holder).tvMinamt.setText(""+MinDeliveryAmt.getString("MinDeliveryAmt", null)+" : " /*+ string + " "*/ + f.format(Double.parseDouble(jsonObject.getString("MinimumDeliveryAmount"))));
                    ((MainViewHolder)holder).ivHomedelivery.setVisibility(View.VISIBLE);
                }

                SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
                SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
                String BASEURL = baseurlpref.getString("BaseURL", null);
                final String IMAGEURL = imgpref.getString("ImageURL", null);
                String imagepath=IMAGEURL + jsonObject.getString("ImagePath");

                PicassoTrustAll.getInstance(context).load(imagepath).error(R.drawable.items).into(((MainViewHolder) holder).iv_itemimage);
               /* Glide.with(context)
                        .load(imagepath)
                        .placeholder(R.drawable.store)
                        .error(R.drawable.store)
                        .into(((MainViewHolder) holder).iv_itemimage);*/

                db = new DBHandler(context);
                isFavorite=db.checkIfavStore(jsonObject.getString("ID_Store"));
                if (!isFavorite) {
                    ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_border);
                } else {
                    ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_fill);
                }

                ((MainViewHolder) holder).ivFavourates.setTag(position);
                ((MainViewHolder) holder).ivFavourates.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            db = new DBHandler(context);
                            isFavorite=db.checkIfavStore(jsonObject.getString("ID_Store"));
                            if (!isFavorite) {
                                db.addtoFavSTORE(new FavStoreModel(jsonObject.getString("ID_Store"), jsonObject.getString("StoreName"),
                                      IMAGEURL + jsonObject.getString("ImagePath")));
                                ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_fill);
                                SharedPreferences Storeaddedtothefavourites = context.getSharedPreferences(Config.SHARED_PREF278, 0);
                                Toast.makeText(context, ""+Storeaddedtothefavourites.getString("Storeaddedtothefavourites", null), Toast.LENGTH_SHORT).show();
                                isFavorite = true;
                            } else {
                                db.deleteFavStorePrdouct(jsonObject.getString("ID_Store"));
                                ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_border);
                                SharedPreferences Storeremovedfromthefavourites = context.getSharedPreferences(Config.SHARED_PREF279, 0);
                                Toast.makeText(context, ""+Storeremovedfromthefavourites.getString("Storeremovedfromthefavourites", null), Toast.LENGTH_SHORT).show();
                                isFavorite = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                ((MainViewHolder)holder).lnLayout.setTag(position);
                ((MainViewHolder)holder).lnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            final SharedPreferences ID_Store = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
//                            if(ID_Store.getString("ID_Store", null) == null ||ID_Store.getString("ID_Store", null).equals("") || ID_Store.getString( "ID_Store", null).equals(jsonObject.getString("ID_Store")))  {

                                db.deleteallCart();
                                db.deleteallFav();


                                //MinimumDeliveryAmount
                                SharedPreferences homedeliverySP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF27, 0);
                                SharedPreferences.Editor homedeliverySPeditor = homedeliverySP.edit();
                                homedeliverySPeditor.putString("homedelivery", jsonObject.getString("HomeDelivery"));
                                homedeliverySPeditor.commit();

                                SharedPreferences MinimumDeliveryAmountSP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                                SharedPreferences.Editor MinimumDeliveryAmounteditor = MinimumDeliveryAmountSP.edit();
                                MinimumDeliveryAmounteditor.putString("MinimumDeliveryAmount", jsonObject.getString("MinimumDeliveryAmount"));
                                MinimumDeliveryAmounteditor.commit();

                                SharedPreferences DeliveryCriteriaSP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                                SharedPreferences.Editor DeliveryCriteriaeditor = DeliveryCriteriaSP.edit();
                                DeliveryCriteriaeditor.putString("DeliveryCriteria", jsonObject.getString("DeliveryCriteria"));
                                DeliveryCriteriaeditor.commit();

                                SharedPreferences ExpressDeliverypref = context.getSharedPreferences(Config.SHARED_PREF39, 0);
                                SharedPreferences.Editor ExpressDeliveryeditor = ExpressDeliverypref.edit();
                                ExpressDeliveryeditor.putString("ExpressDelivery", jsonObject.getString("ExpressDelivery"));
                                ExpressDeliveryeditor.commit();

                                SharedPreferences ExpressDeliveryAmountpref = context.getSharedPreferences(Config.SHARED_PREF40, 0);
                                SharedPreferences.Editor ExpressDeliveryAmounteditor = ExpressDeliveryAmountpref.edit();
                                ExpressDeliveryAmounteditor.putString("ExpressDeliveryAmount", jsonObject.getString("ExpressDeliveryAmount"));
                                ExpressDeliveryAmounteditor.commit();

                                SharedPreferences CategoryListpref = context.getSharedPreferences(Config.SHARED_PREF51, 0);
                                SharedPreferences.Editor CategoryListeditor = CategoryListpref.edit();
                                CategoryListeditor.putString("CategoryList", jsonObject.getString("CategoryList"));
                                CategoryListeditor.commit();

                                SharedPreferences SubCategoryListpref = context.getSharedPreferences(Config.SHARED_PREF52, 0);
                                SharedPreferences.Editor SubCategoryListeditor = SubCategoryListpref.edit();
                                SubCategoryListeditor.putString("SubCategoryList", jsonObject.getString("SubCategoryList"));
                                SubCategoryListeditor.commit();

                                SharedPreferences CashOnDeliverypref = context.getSharedPreferences(Config.SHARED_PREF53, 0);
                                SharedPreferences.Editor CashOnDeliveryeditor = CashOnDeliverypref.edit();
                                CashOnDeliveryeditor.putString("CashOnDelivery", jsonObject.getString("CashOnDelivery"));
                                CashOnDeliveryeditor.commit();

                                SharedPreferences OnlinePaymentpref = context.getSharedPreferences(Config.SHARED_PREF54, 0);
                                SharedPreferences.Editor OnlinePaymenteditor = OnlinePaymentpref.edit();
                                OnlinePaymenteditor.putString("OnlinePayment", jsonObject.getString("OnlinePayment"));
                                OnlinePaymenteditor.commit();

                                SharedPreferences UPIIDpref = context.getSharedPreferences(Config.SHARED_PREF59, 0);
                                SharedPreferences.Editor UPIIDeditor = UPIIDpref.edit();
                                UPIIDeditor.putString("UPIID", jsonObject.getString("UPIID"));
                                UPIIDeditor.commit();

                                SharedPreferences TimeSlotCheckpref = context.getSharedPreferences(Config.SHARED_PREF60, 0);
                                SharedPreferences.Editor TimeSlotCheckeditor = TimeSlotCheckpref.edit();
                                TimeSlotCheckeditor.putString("TimeSlotCheck", jsonObject.getString("TimeSlotCheck"));
                                TimeSlotCheckeditor.commit();

                                SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                ID_Storeeditor.putString("ID_Store", jsonObject.getString("ID_Store"));
                                ID_Storeeditor.commit();
                                SharedPreferences StoreName = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                StoreNameeditor.putString("StoreName", jsonObject.getString("StoreName"));

                                StoreNameeditor.commit();
                                SharedPreferences Requiredcounterpickuppref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF44, 0);
                                SharedPreferences.Editor Requiredcounterpickupeditor = Requiredcounterpickuppref.edit();
                                Requiredcounterpickupeditor.putString("Requiredcounterpickup", jsonObject.getString("CounterDelivery"));
                                Requiredcounterpickupeditor.commit();


                                Log.e(TAG,"OnlinePaymentMethods  226   "+jsonObject.getString("OnlinePaymentMethods"));
                                SharedPreferences OnlinePaymentmeth = context.getSharedPreferences(Config.SHARED_PREF62, 0);
                                SharedPreferences.Editor OnlinePaymentmetheditor = OnlinePaymentmeth.edit();
                                OnlinePaymentmetheditor.putString("OnlinePaymentMethods", jsonObject.getString("OnlinePaymentMethods"));
                                OnlinePaymentmetheditor.commit();


                                SharedPreferences MerchantKey = context.getSharedPreferences(Config.SHARED_PREF340, 0);
                                SharedPreferences.Editor MerchantKeyeditor = MerchantKey.edit();
                                MerchantKeyeditor.putString("MerchantKey", jsonObject.getString("MerchantKey"));
                                MerchantKeyeditor.commit();

                                SharedPreferences SaltKey = context.getSharedPreferences(Config.SHARED_PREF341, 0);
                                SharedPreferences.Editor SaltKeyeditor = SaltKey.edit();
                                SaltKeyeditor.putString("SaltKey", jsonObject.getString("SaltKey"));
                                SaltKeyeditor.commit();

                                SharedPreferences sMobile = context.getSharedPreferences(Config.SHARED_PREF342, 0);
                                SharedPreferences.Editor sMobileeditor = sMobile.edit();
                                sMobileeditor.putString("Mobile", jsonObject.getString("Mobile"));
                                sMobileeditor.commit();

                                SharedPreferences BranchEmail = context.getSharedPreferences(Config.SHARED_PREF343, 0);
                                SharedPreferences.Editor BranchEmaileditor = BranchEmail.edit();
                                BranchEmaileditor.putString("BranchEmail", jsonObject.getString("BranchEmail"));
                                BranchEmaileditor.commit();

                                SharedPreferences ScratchCard = context.getSharedPreferences(Config.SHARED_PREF382, 0);
                                SharedPreferences.Editor ScratchCardeditor = ScratchCard.edit();
                                ScratchCardeditor.putString("ScratchCard", jsonObject.getString("GiftVoucher"));
                                ScratchCardeditor.commit();

                                SharedPreferences sharedPreferences = context.getSharedPreferences("localpref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                jsonObject = jsonArray.getJSONObject(position);
                                JSONArray jsonpayment = jsonObject.getJSONArray("OnlinePaymentMethods");
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


                            SharedPreferences PrivilageCardEnable = context.getSharedPreferences(Config.SHARED_PREF429, 0);
                            SharedPreferences.Editor PrivilageCardEnableeditor = PrivilageCardEnable.edit();
                            PrivilageCardEnableeditor.putString("PrivilageCardEnable", jsonObject.getString("PrivilageCardEnable"));
                            PrivilageCardEnableeditor.commit();
//
//                                Intent intent = new Intent(context, OutShopActivity.class);
//                                intent.putExtra("from", "store");
//                                context.startActivity(intent);
//                                ((Activity)context).finish();



                                Intent intent = new Intent(context, HomeActivity.class);
                                context.startActivity(intent);
                                ((Activity)context).finish();
//                            }


                           /* else {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View layout = inflater1.inflate(R.layout.warning_popup, null);
                                LinearLayout ll_cancel = (LinearLayout) layout.findViewById(R.id.ll_cancel);
                                LinearLayout ll_ok = (LinearLayout) layout.findViewById(R.id.ll_ok);
                                builder.setView(layout);
                                final android.app.AlertDialog alertDialog = builder.create();
                                ll_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog.dismiss();
                                    }
                                });
                                ll_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DBHandler db = new DBHandler(context);
                                        db.deleteallCart();
                                        db.deleteallFav();

                                        SharedPreferences homedeliverySP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF27, 0);
                                        SharedPreferences.Editor homedeliverySPeditor = homedeliverySP.edit();
                                        try {
                                            homedeliverySPeditor.putString("homedelivery", jsonObject.getString("HomeDelivery"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        homedeliverySPeditor.commit();

                                        SharedPreferences MinimumDeliveryAmountSP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                                        SharedPreferences.Editor MinimumDeliveryAmounteditor = MinimumDeliveryAmountSP.edit();
                                        try {
                                            MinimumDeliveryAmounteditor.putString("MinimumDeliveryAmount", jsonObject.getString("MinimumDeliveryAmount"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        MinimumDeliveryAmounteditor.commit();

                                        SharedPreferences DeliveryCriteriaSP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF30, 0);
                                        SharedPreferences.Editor DeliveryCriteriaeditor = DeliveryCriteriaSP.edit();
                                        try {
                                            DeliveryCriteriaeditor.putString("DeliveryCriteria", jsonObject.getString("DeliveryCriteria"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        DeliveryCriteriaeditor.commit();

                                        Intent intent = new Intent(context, OutShopActivity.class);
                                        intent.putExtra("from", "favstore");
                                        context.startActivity(intent);
                                        ((Activity)context).finish();
                                        SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                        try {
                                            ID_Storeeditor.putString("ID_Store", jsonObject.getString("ID_Store"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        ID_Storeeditor.commit();
                                        SharedPreferences StoreName = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                        SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                        try {
                                            StoreNameeditor.putString("StoreName", jsonObject.getString("StoreName"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        StoreNameeditor.commit();
                                        alertDialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }*/

                            // Hide 07.09.2021
//                           else{
//                                DBHandler db = new DBHandler(context);
//                                if (db.getAllCart().isEmpty() && db.getAllFav().isEmpty()){
//                                    db.deleteallCart();
//                                    db.deleteallFav();
//
//                                    SharedPreferences homedeliverySP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF27, 0);
//                                    SharedPreferences.Editor homedeliverySPeditor = homedeliverySP.edit();
//                                    try {
//                                        homedeliverySPeditor.putString("homedelivery", jsonObject.getString("HomeDelivery"));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    homedeliverySPeditor.commit();
//
//                                    SharedPreferences MinimumDeliveryAmountSP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
//                                    SharedPreferences.Editor MinimumDeliveryAmounteditor = MinimumDeliveryAmountSP.edit();
//                                    try {
//                                        MinimumDeliveryAmounteditor.putString("MinimumDeliveryAmount", jsonObject.getString("MinimumDeliveryAmount"));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    MinimumDeliveryAmounteditor.commit();
//
//                                    SharedPreferences DeliveryCriteriaSP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF30, 0);
//                                    SharedPreferences.Editor DeliveryCriteriaeditor = DeliveryCriteriaSP.edit();
//                                    try {
//                                        DeliveryCriteriaeditor.putString("DeliveryCriteria", jsonObject.getString("DeliveryCriteria"));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    DeliveryCriteriaeditor.commit();
//
//
//                                    SharedPreferences ExpressDeliverypref = context.getSharedPreferences(Config.SHARED_PREF39, 0);
//                                    SharedPreferences.Editor ExpressDeliveryeditor = ExpressDeliverypref.edit();
//                                    ExpressDeliveryeditor.putString("ExpressDelivery", jsonObject.getString("ExpressDelivery"));
//                                    ExpressDeliveryeditor.commit();
//
//                                    SharedPreferences ExpressDeliveryAmountpref = context.getSharedPreferences(Config.SHARED_PREF40, 0);
//                                    SharedPreferences.Editor ExpressDeliveryAmounteditor = ExpressDeliveryAmountpref.edit();
//                                    ExpressDeliveryAmounteditor.putString("ExpressDeliveryAmount", jsonObject.getString("ExpressDeliveryAmount"));
//                                    ExpressDeliveryAmounteditor.commit();
//
//                                    SharedPreferences CategoryListpref = context.getSharedPreferences(Config.SHARED_PREF51, 0);
//                                    SharedPreferences.Editor CategoryListeditor = CategoryListpref.edit();
//                                    CategoryListeditor.putString("CategoryList", jsonObject.getString("CategoryList"));
//                                    CategoryListeditor.commit();
//
//                                    SharedPreferences SubCategoryListpref = context.getSharedPreferences(Config.SHARED_PREF52, 0);
//                                    SharedPreferences.Editor SubCategoryListeditor = SubCategoryListpref.edit();
//                                    SubCategoryListeditor.putString("SubCategoryList", jsonObject.getString("SubCategoryList"));
//                                    SubCategoryListeditor.commit();
//
//                                    SharedPreferences CashOnDeliverypref = context.getSharedPreferences(Config.SHARED_PREF53, 0);
//                                    SharedPreferences.Editor CashOnDeliveryeditor = CashOnDeliverypref.edit();
//                                    CashOnDeliveryeditor.putString("CashOnDelivery", jsonObject.getString("CashOnDelivery"));
//                                    CashOnDeliveryeditor.commit();
//
//                                    SharedPreferences OnlinePaymentpref = context.getSharedPreferences(Config.SHARED_PREF54, 0);
//                                    SharedPreferences.Editor OnlinePaymenteditor = OnlinePaymentpref.edit();
//                                    OnlinePaymenteditor.putString("OnlinePayment", jsonObject.getString("OnlinePayment"));
//                                    OnlinePaymenteditor.commit();
//
//
//                                    SharedPreferences UPIIDpref = context.getSharedPreferences(Config.SHARED_PREF59, 0);
//                                    SharedPreferences.Editor UPIIDeditor = UPIIDpref.edit();
//                                    UPIIDeditor.putString("UPIID", jsonObject.getString("UPIID"));
//                                    UPIIDeditor.commit();
//
//                                    SharedPreferences TimeSlotCheckpref = context.getSharedPreferences(Config.SHARED_PREF60, 0);
//                                    SharedPreferences.Editor TimeSlotCheckeditor = TimeSlotCheckpref.edit();
//                                    TimeSlotCheckeditor.putString("TimeSlotCheck", jsonObject.getString("TimeSlotCheck"));
//                                    TimeSlotCheckeditor.commit();
//
//                                    Intent intent = new Intent(context, OutShopActivity.class);
//                                    intent.putExtra("from", "favstore");
//                                    context.startActivity(intent);
//                                    ((Activity)context).finish();
//                                    SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
//                                    try {
//                                        ID_Storeeditor.putString("ID_Store", jsonObject.getString("ID_Store"));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    ID_Storeeditor.commit();
//                                    SharedPreferences StoreName = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
//                                    SharedPreferences.Editor StoreNameeditor = StoreName.edit();
//                                    try {
//                                        StoreNameeditor.putString("StoreName", jsonObject.getString("StoreName"));
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    StoreNameeditor.commit();
//
//
//                                    SharedPreferences ScratchCard = context.getSharedPreferences(Config.SHARED_PREF382, 0);
//                                    SharedPreferences.Editor ScratchCardeditor = ScratchCard.edit();
//                                    ScratchCardeditor.putString("ScratchCard", jsonObject.getString("GiftVoucher"));
//                                    ScratchCardeditor.commit();
//                                }
//                                else{
//
//                                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
//                                    LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                                    View layout = inflater1.inflate(R.layout.warning_popup, null);
//                                    LinearLayout ll_cancel = (LinearLayout) layout.findViewById(R.id.ll_cancel);
//                                    LinearLayout ll_ok = (LinearLayout) layout.findViewById(R.id.ll_ok);
//                                    builder.setView(layout);
//                                    final android.app.AlertDialog alertDialog = builder.create();
//                                    ll_cancel.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            alertDialog.dismiss();
//                                        }
//                                    });
//                                    ll_ok.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View view) {
//                                            DBHandler db = new DBHandler(context);
//                                            db.deleteallCart();
//                                            db.deleteallFav();
//
//                                            SharedPreferences homedeliverySP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF27, 0);
//                                            SharedPreferences.Editor homedeliverySPeditor = homedeliverySP.edit();
//                                            try {
//                                                homedeliverySPeditor.putString("homedelivery", jsonObject.getString("HomeDelivery"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            homedeliverySPeditor.commit();
//
//                                            SharedPreferences MinimumDeliveryAmountSP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
//                                            SharedPreferences.Editor MinimumDeliveryAmounteditor = MinimumDeliveryAmountSP.edit();
//                                            try {
//                                                MinimumDeliveryAmounteditor.putString("MinimumDeliveryAmount", jsonObject.getString("MinimumDeliveryAmount"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            MinimumDeliveryAmounteditor.commit();
//
//                                            SharedPreferences DeliveryCriteriaSP = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF30, 0);
//                                            SharedPreferences.Editor DeliveryCriteriaeditor = DeliveryCriteriaSP.edit();
//                                            try {
//                                                DeliveryCriteriaeditor.putString("DeliveryCriteria", jsonObject.getString("DeliveryCriteria"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            DeliveryCriteriaeditor.commit();
//
//
//                                            SharedPreferences ExpressDeliverypref = context.getSharedPreferences(Config.SHARED_PREF39, 0);
//                                            SharedPreferences.Editor ExpressDeliveryeditor = ExpressDeliverypref.edit();
//                                            try {
//                                                ExpressDeliveryeditor.putString("ExpressDelivery", jsonObject.getString("ExpressDelivery"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            ExpressDeliveryeditor.commit();
//
//                                            SharedPreferences ExpressDeliveryAmountpref = context.getSharedPreferences(Config.SHARED_PREF40, 0);
//                                            SharedPreferences.Editor ExpressDeliveryAmounteditor = ExpressDeliveryAmountpref.edit();
//                                            try {
//                                                ExpressDeliveryAmounteditor.putString("ExpressDeliveryAmount", jsonObject.getString("ExpressDeliveryAmount"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            ExpressDeliveryAmounteditor.commit();
//
//                                            SharedPreferences CategoryListpref = context.getSharedPreferences(Config.SHARED_PREF51, 0);
//                                            SharedPreferences.Editor CategoryListeditor = CategoryListpref.edit();
//                                            try {
//                                                CategoryListeditor.putString("CategoryList", jsonObject.getString("CategoryList"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            CategoryListeditor.commit();
//
//                                            SharedPreferences SubCategoryListpref = context.getSharedPreferences(Config.SHARED_PREF52, 0);
//                                            SharedPreferences.Editor SubCategoryListeditor = SubCategoryListpref.edit();
//                                            try {
//                                                SubCategoryListeditor.putString("SubCategoryList", jsonObject.getString("SubCategoryList"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            SubCategoryListeditor.commit();
//
//                                            SharedPreferences CashOnDeliverypref = context.getSharedPreferences(Config.SHARED_PREF53, 0);
//                                            SharedPreferences.Editor CashOnDeliveryeditor = CashOnDeliverypref.edit();
//                                            try {
//                                                CashOnDeliveryeditor.putString("CashOnDelivery", jsonObject.getString("CashOnDelivery"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            CashOnDeliveryeditor.commit();
//
//                                            SharedPreferences OnlinePaymentpref = context.getSharedPreferences(Config.SHARED_PREF54, 0);
//                                            SharedPreferences.Editor OnlinePaymenteditor = OnlinePaymentpref.edit();
//                                            try {
//                                                OnlinePaymenteditor.putString("OnlinePayment", jsonObject.getString("OnlinePayment"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            OnlinePaymenteditor.commit();
//
//
//                                            SharedPreferences UPIIDpref = context.getSharedPreferences(Config.SHARED_PREF59, 0);
//                                            SharedPreferences.Editor UPIIDeditor = UPIIDpref.edit();
//                                            try {
//                                                UPIIDeditor.putString("UPIID", jsonObject.getString("UPIID"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            UPIIDeditor.commit();
//
//                                            SharedPreferences TimeSlotCheckpref = context.getSharedPreferences(Config.SHARED_PREF60, 0);
//                                            SharedPreferences.Editor TimeSlotCheckeditor = TimeSlotCheckpref.edit();
//                                            try {
//                                                TimeSlotCheckeditor.putString("TimeSlotCheck", jsonObject.getString("TimeSlotCheck"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            TimeSlotCheckeditor.commit();
//
//                                            Intent intent = new Intent(context, OutShopActivity.class);
//                                            intent.putExtra("from", "favstore");
//                                            context.startActivity(intent);
//                                            ((Activity)context).finish();
//                                            SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
//                                            try {
//                                                ID_Storeeditor.putString("ID_Store", jsonObject.getString("ID_Store"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            ID_Storeeditor.commit();
//                                            SharedPreferences StoreName = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
//                                            SharedPreferences.Editor StoreNameeditor = StoreName.edit();
//                                            try {
//                                                StoreNameeditor.putString("StoreName", jsonObject.getString("StoreName"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            StoreNameeditor.commit();
//
//
//                                            SharedPreferences ScratchCard = context.getSharedPreferences(Config.SHARED_PREF382, 0);
//                                            SharedPreferences.Editor ScratchCardeditor = ScratchCard.edit();
//                                            try {
//                                                ScratchCardeditor.putString("ScratchCard", jsonObject.getString("GiftVoucher"));
//                                            } catch (JSONException e) {
//                                                e.printStackTrace();
//                                            }
//                                            ScratchCardeditor.commit();
//                                            alertDialog.dismiss();
//                                        }
//                                    });
//                                    alertDialog.show();
//                                }
//                            }

                            // Hide 07.09.2021
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
        TextView tvPrdName,tvAddress,tvMinamt,tvtime,tvMob, tvnote, txtvRating;
        ImageView iv_itemimage, ivFavourates, ivHomedelivery;
        public MainViewHolder(View v) {
            super(v);
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            iv_itemimage=(ImageView)v.findViewById(R.id.iv_itemimage);
            ivFavourates=(ImageView)v.findViewById(R.id.ivFavourates);
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

}
