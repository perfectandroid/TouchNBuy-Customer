package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Activity.ItemListingActivity;
import com.perfect.easyshopplus.Activity.NoInternetActivity;
import com.perfect.easyshopplus.Activity.OrderDetailsActivity;
import com.perfect.easyshopplus.Activity.OrderItemListingActivity;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.CartModel;
import com.perfect.easyshopplus.Model.CheckCartModel;
import com.perfect.easyshopplus.Model.FavModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class OrderItemListAdapter extends RecyclerView.Adapter {

    ProgressDialog progressDialog;
    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    String PrdctName,MRP,SalesPrice,Id_Item,ID_Stock,CurrentStock;
    String Id_order, orderDate, deliveryDate, status, order_id, ShopType, ID_Store, itemcount, ID_SalesOrder, storeName,DeliveryCharge,OrderType, ID_CustomerAddress;
    DBHandler db;
    boolean isCart;
    boolean isFavorite;
    CartChangedListener cartChangedListener;
    int counter= 1;

    public OrderItemListAdapter(Context context, JSONArray jsonArray,
                                String ID_SalesOrder, String order_id, String deliveryDate,
                                String Id_order, String orderDate,
                                String status, String ID_Store, String ShopType,
                                String itemcount,String ID_CustomerAddress,String OrderType,
                                String storeName,String DeliveryCharge) {



        this.context=context;
        this.jsonArray=jsonArray;
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
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_order_item_list, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {

            SharedPreferences MRPS = context.getSharedPreferences(Config.SHARED_PREF115, 0);
            SharedPreferences syousaved = context.getSharedPreferences(Config.SHARED_PREF117, 0);
            SharedPreferences squantity = context.getSharedPreferences(Config.SHARED_PREF122, 0);
            SharedPreferences Outofstock = context.getSharedPreferences(Config.SHARED_PREF116, 0);
            SharedPreferences addtoorderlist = context.getSharedPreferences(Config.SHARED_PREF173, 0);
            SharedPreferences addtocart = context.getSharedPreferences(Config.SHARED_PREF121, 0);
            SharedPreferences Itemaddedtothefavourites = context.getSharedPreferences(Config.SHARED_PREF332, 0);
            SharedPreferences Itemaddedtothecart = context.getSharedPreferences(Config.SHARED_PREF337, 0);

            jsonObject=jsonArray.getJSONObject(position);
            DecimalFormat f = new DecimalFormat("##.00");
            if (holder instanceof MainViewHolder) {
                PrdctName=jsonObject.getString("ItemName");
                MRP=jsonObject.getString("MRP");
                SalesPrice=jsonObject.getString("SalesPrice");
                Id_Item=jsonObject.getString("ID_Items");
                ID_Stock=jsonObject.getString("ID_Stock");
                CurrentStock=jsonObject.getString("CurrentStock");
                ((MainViewHolder)holder).txtStock.setText(""+Outofstock.getString("Outofstock",null));
                if(jsonObject.getInt("CurrentStock")==0){
                    ((MainViewHolder)holder).txtStock.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).tvlblqty.setVisibility(View.GONE);
                    ((MainViewHolder)holder).lladdQty.setVisibility(View.GONE);
                    ((MainViewHolder)holder).btAddtoOrder.setVisibility(View.GONE);

                }else{
                    ((MainViewHolder)holder).txtStock.setVisibility(View.GONE);
                    ((MainViewHolder)holder).tvlblqty.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).lladdQty.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).btAddtoOrder.setVisibility(View.VISIBLE);


                }

                SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
                SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
                String BASEURL = baseurlpref.getString("BaseURL", null);
                final String IMAGEURL = imgpref.getString("ImageURL", null);
                String imagepath= IMAGEURL + jsonObject.getString("ImageName");
//                Glide.with(context)
//                        .load(imagepath)
//                        .placeholder(R.drawable.items)
//                        .error(R.drawable.items)
//                        .into(((MainViewHolder) holder).iv_itemimage);

                PicassoTrustAll.getInstance(context).load(imagepath).error(R.drawable.items).into(((MainViewHolder) holder).iv_itemimage);

                /*               String ImageName=""*//*jsonObject.getString("ImageName")*//*;
                if(!ImageName.isEmpty()) {
                    byte[] decodedString = Base64.decode(jsonObject.getString("ImageName"), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    decodedByte.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Glide.with(context)
                            .load(stream.toByteArray())
                            .placeholder(R.drawable.items)
                            .error(R.drawable.items)
                            .into(((MainViewHolder) holder).iv_itemimage);
                }else {
                    ((MainViewHolder)holder).iv_itemimage.setImageResource(R.drawable.items);
                }*/

                db = new DBHandler(context);
                isCart=db.checkIcartItem(Id_Item);
                isFavorite=db.checkIfavItem(Id_Item);
                if (!isCart) {
                    ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticongrey);
                } else {
                    ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticonred);
                }
                if (!isFavorite) {
                    ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_border);
                } else {
                    ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_fill);
                }
                ((MainViewHolder)holder).tvPrdName.setText(PrdctName);
                String string = "\u20B9";
                byte[] utf8 = new byte[0];
                try {
                    utf8 = string.getBytes("UTF-8");
                    string = new String(utf8, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                ((MainViewHolder)holder).tvPrdAmount.setText(/*string+" "+*/f.format(Double.parseDouble(SalesPrice)));
                ((MainViewHolder)holder).tvPrdMRPAmount.setPaintFlags(((MainViewHolder)holder).tvPrdMRPAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                double yousaved=Float.parseFloat(MRP)-Float.parseFloat(SalesPrice);
                if(yousaved<=0){
                    ((MainViewHolder)holder).tvPrdMRPAmount.setVisibility(View.GONE);
                    ((MainViewHolder)holder).tvPrdMRPAmountsaved.setVisibility(View.GONE);
                }else {
                    ((MainViewHolder)holder).tvPrdMRPAmount.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).tvPrdMRPAmountsaved.setVisibility(View.VISIBLE);
                }
//                ((MainViewHolder)holder).tvPrdMRPAmount.setText("MRP "+/*string+" "+*/f.format(Double.parseDouble(MRP)));
//                ((MainViewHolder)holder).tvPrdMRPAmountsaved.setText("You Saved "+/*string+" "+*/f.format(yousaved));
                ((MainViewHolder)holder).tvlblqty.setText(""+squantity.getString("quantity",null)+" : ");
                ((MainViewHolder)holder).tvPrdMRPAmount.setText(""+MRPS.getString("MRP",null)+" "+/*string+" "+*/f.format(Double.parseDouble(MRP)));
                ((MainViewHolder)holder).tvPrdMRPAmountsaved.setText(""+syousaved.getString("yousaved",null)+" "+/*string+" "+*/f.format(yousaved));

                ((MainViewHolder) holder).btAddtoOrder.setText(""+addtoorderlist.getString("addtoorderlist",null));
                ((MainViewHolder) holder).btAddtoOrder.setTag(position);
                ((MainViewHolder) holder).btAddtoOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            addItemtoOrderlist(jsonObject.getString("ID_Items"), jsonObject.getString("ItemName"),
                                    jsonObject.getString("MRP"),jsonObject.getString("SalesPrice"),
                                    ((MainViewHolder) holder).product_count.getText().toString(), jsonObject.getString("RetailPrice"), jsonObject.getString("GST"), jsonObject.getString("CESS"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((OrderItemListingActivity)context).hideKeyboard();

                    }
                });
                ((MainViewHolder) holder).btAddtocart.setText(""+addtocart.getString("addtocart",null));
                ((MainViewHolder) holder).btAddtocart.setTag(position);
                if(position==0)
                {
                   // ((MainViewHolder) holder).btAddtocart.setVisibility(View.VISIBLE);
                }
                ((MainViewHolder) holder).btAddtocart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            Intent i = new Intent(context, OrderDetailsActivity.class);

                            i.putExtra("order_id", order_id);
                            i.putExtra("deliveryDate", deliveryDate);
                            i.putExtra("Id_order", Id_order);
                            i.putExtra("orderDate", orderDate);
                            i.putExtra("status", status);
                            i.putExtra("ID_Store", ID_Store);
                            i.putExtra("ShopType", ShopType);
                            i.putExtra("itemcount", itemcount);
                            i.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                            i.putExtra("OrderType", OrderType);
                            i.putExtra("storeName", storeName);
                            i.putExtra("DeliveryCharge", DeliveryCharge);

//                            i.putExtra("order_id", order_id);
//                            i.putExtra("Id_order", Id_order);
//                            i.putExtra("orderDate", orderDate);
//                            i.putExtra("deliveryDate", deliveryDate);
//                            i.putExtra("status", status);
//                            i.putExtra("ShopType", ShopType);
//                            i.putExtra("ID_Store", ID_Store);
//                            i.putExtra("itemcount", itemcount);
//                            i.putExtra("storeName", storeName);
                            context.startActivity(i);
                            ((Activity)context).finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((OrderItemListingActivity)context).hideKeyboard();

                    }
                });

                ((MainViewHolder) holder).ivFavourates.setTag(position);
                ((MainViewHolder) holder).ivFavourates.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            db = new DBHandler(context);
                            isFavorite=db.checkIfavItem(jsonObject.getString("ID_Items"));
                            if (!isFavorite) {
                                db.addtoFav(new FavModel(jsonObject.getString("ID_Items"), jsonObject.getString("ItemName"),
                                        jsonObject.getString("SalesPrice"),jsonObject.getString("MRP"),
                                        jsonObject.getString("ID_Stock"),jsonObject.getString("CurrentStock"),
                                        jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"), jsonObject.getString("WholesalePrice")
                                        , jsonObject.getString("GST"), jsonObject.getString("CESS"),
                                        jsonObject.getString("Packed"),jsonObject.getString("Description"),
                                        IMAGEURL + jsonObject.getString("ImageName"),jsonObject.getString("ItemMalayalamName")));
                                ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_fill);

//                                Toast.makeText(context, "Item added to the favourites", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, ""+Itemaddedtothefavourites.getString("Itemaddedtothefavourites",null), Toast.LENGTH_SHORT).show();
                                isFavorite = true;
                            } else {
                                db.deleteFavPrdouct(jsonObject.getString("ID_Items"));
                                ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_border);
                                SharedPreferences Itemremovedfromthefavourites = context.getSharedPreferences(Config.SHARED_PREF338, 0);
//                                Toast.makeText(context, "Item removed from the favourites", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, ""+Itemremovedfromthefavourites.getString("Itemremovedfromthefavourites",null), Toast.LENGTH_SHORT).show();
                                isFavorite = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((OrderItemListingActivity)context).hideKeyboard();

                    }
                });
                ((MainViewHolder) holder).ivCart.setTag(position);
                ((MainViewHolder) holder).ivCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            SharedPreferences pref2 = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                            jsonObject = jsonArray.getJSONObject(position);
                            db = new DBHandler(context);
                            isCart=db.checkIcartItem(jsonObject.getString("ID_Items"));
                            if (!isCart) {
                                db.addtoCart(new CartModel(jsonObject.getString("ID_Items"), jsonObject.getString("ItemName"), jsonObject.getString("SalesPrice"),jsonObject.getString("MRP"), jsonObject.getString("ID_Stock"), jsonObject.getString("CurrentStock"),
                                        jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"), jsonObject.getString("WholesalePrice")
                                        , jsonObject.getString("GST"), jsonObject.getString("CESS"),"1",
                                        jsonObject.getString("Packed"),jsonObject.getString("Description"),
                                        IMAGEURL + jsonObject.getString("ImageName"),jsonObject.getString("ItemMalayalamName")));
                                db.addtoCartCheck(new CheckCartModel(jsonObject.getString("ID_Items"), jsonObject.getString("ID_Stock"), jsonObject.getString("CurrentStock"),pref2.getString("ID_Store", null)));
                                ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticonred);
//                                Toast.makeText(context, "Item added to the cart", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, ""+Itemaddedtothecart.getString("Itemaddedtothecart",null), Toast.LENGTH_SHORT).show();


                                isCart = true;
                            } else {
                                db.deleteCartPrdouct(jsonObject.getString("ID_Items"));
                                db.deleteCheckCart(jsonObject.getString("ID_Items"));
                                ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticongrey);
                                SharedPreferences Itemremovedfromthecart = context.getSharedPreferences(Config.SHARED_PREF339, 0);
//                                Toast.makeText(context, "Item removed from the cart", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, ""+Itemremovedfromthecart.getString("Itemremovedfromthecart",null), Toast.LENGTH_SHORT).show();
                                isCart = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cartChangedListener.onCartChanged();
                        ((OrderItemListingActivity)context).hideKeyboard();

                    }
                });
                ((MainViewHolder)holder).ivQtyAdd.setTag(position);
                ((MainViewHolder)holder).ivQtyAdd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            counter = Integer.valueOf(((MainViewHolder)holder).product_count.getText().toString()) + 1;
                            ((MainViewHolder)holder).product_count.setText("" + counter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((OrderItemListingActivity)context).hideKeyboard();

                    }
                });
                ((MainViewHolder)holder).ivQtyMinus.setTag(position);
                ((MainViewHolder)holder).ivQtyMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            if(Integer.valueOf(((MainViewHolder)holder).product_count.getText().toString())>=2) {
                                counter = Integer.valueOf(((MainViewHolder)holder).product_count.getText().toString()) - 1;
                                ((MainViewHolder)holder).product_count.setText("" + counter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((OrderItemListingActivity)context).hideKeyboard();

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
        LinearLayout lnLayout,lladdQty;
        TextView tvPrdName,txtStock;
        TextView tvPrdAmount, tvPrdMRPAmountsaved, tvPrdMRPAmount, product_count;
        TextView tvlblqty;
        ImageView ivFavourates,ivCart,ivQtyAdd, ivQtyMinus,iv_itemimage;
        Button btAddtoOrder,btAddtocart;
        public MainViewHolder(View v) {
            super(v);
            cartChangedListener = ItemListingActivity.cartChangedListener;
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            tvPrdAmount=(TextView)v.findViewById(R.id.tvPrdAmount);
            tvPrdMRPAmount=(TextView)v.findViewById(R.id.tvPrdMRPAmount);
            tvPrdMRPAmountsaved=(TextView)v.findViewById(R.id.tvPrdMRPAmountsaved);
            ivFavourates=(ImageView)v.findViewById(R.id.ivFavourates);
            ivCart=(ImageView)v.findViewById(R.id.ivCart);
            btAddtoOrder=(Button) v.findViewById(R.id.btAddtoOrder);
            btAddtocart=(Button) v.findViewById(R.id.btAddtocart);
            product_count = (TextView) v.findViewById(R.id.tvQtyValue);
            txtStock = (TextView) v.findViewById(R.id.txtStock);
            ivQtyAdd =(ImageView)v.findViewById(R.id.ivQtyAdd);
            ivQtyMinus =(ImageView)v.findViewById(R.id.ivQtyMinus);
            iv_itemimage =(ImageView)v.findViewById(R.id.iv_itemimage);
            tvlblqty = (TextView) v.findViewById(R.id.tvlblqty);
            lladdQty = (LinearLayout)v.findViewById(R.id.lladdQty);
        }
    }

    private void addItemtoOrderlist(String ID_Items, String ItemName, String Mrp, String SalesPrice,
                                    String Quantity, String RetailPrice, String VAT, String Cess) {
        SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(context).isInternetOn()) {
            try {
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
                        requestObject1.put("UserAction", "5");
                        requestObject1.put("ID_SalesOrder", ID_SalesOrder);
                        requestObject1.put("ID_Stock", ID_Stock);
                        requestObject1.put("ID_Items", ID_Items);
                        requestObject1.put("ItemName", ItemName);
                        requestObject1.put("Mrp", Mrp);
                        requestObject1.put("SalesPrice", SalesPrice);
                        requestObject1.put("Quantity", Quantity);
                        requestObject1.put("RetailPrice", RetailPrice);
                        requestObject1.put("VAT", VAT);
                        requestObject1.put("Cess", Cess);
                        requestObject1.put("Bank_Key", context.getResources().getString(R.string.BankKey));
                        SharedPreferences IDLanguages = context.getSharedPreferences(Config.SHARED_PREF80, 0);
                        requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                    Call<String> call = apiService.additemtoorder(body);
                    call.enqueue(new Callback<String>() {
                        @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                            try {
                                progressDialog.dismiss();
                                JSONObject jObject = new JSONObject(response.body());
                                if(jObject.getString("StatusCode").equals("5")){
                                    SharedPreferences ItemAddedToYourOrderList = context.getSharedPreferences(Config.SHARED_PREF333, 0);
//                                    Toast.makeText(context, "Item Added To Your OrderList",Toast.LENGTH_LONG).show();
                                    Toast.makeText(context, ""+ItemAddedToYourOrderList.getString("ItemAddedToYourOrderList",null),Toast.LENGTH_LONG).show();
                                /*    Intent i = new Intent(context, OrderDetailsActivity.class);
                                    i.putExtra("order_id", order_id);
                                    i.putExtra("Id_order", Id_order);
                                    i.putExtra("orderDate", orderDate);
                                    i.putExtra("deliveryDate", deliveryDate);
                                    i.putExtra("status", status);
                                    i.putExtra("ShopType", ShopType);
                                    i.putExtra("ID_Store", ID_Store);
                                    i.putExtra("itemcount", itemcount);
                                    i.putExtra("storeName", storeName);
                                    context.startActivity(i);
                                    ((Activity)context).finish();*/
                                }else{
                                    SharedPreferences ItemAddedToYourOrderListFailed = context.getSharedPreferences(Config.SHARED_PREF334, 0);
//                                    Toast.makeText(context,"Item Added To Your OrderLis Failed !",Toast.LENGTH_LONG).show();
                                    Toast.makeText(context,""+ItemAddedToYourOrderListFailed.getString("ItemAddedToYourOrderListFailed",null),Toast.LENGTH_LONG).show();
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
//                Toast.makeText(context,"Something went wrong!",Toast.LENGTH_LONG).show();
                SharedPreferences Somethingwentwrong = context.getSharedPreferences(Config.SHARED_PREF72, 0);
                Toast.makeText(context,""+Somethingwentwrong.getString("Somethingwentwrong",null),Toast.LENGTH_LONG).show();
            } }else {
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
