package com.perfect.easyshopplus.Adapter;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Activity.NoInternetActivity;
import com.perfect.easyshopplus.Activity.OrderDetailsActivity;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;

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

public class OrderDetailsListAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    String status,order_id, Id_order,orderDate, deliveryDate,  ShopType, ID_Store, itemcount, storeName, OrderType,ID_CustomerAddress,DeliveryCharge,OrderNo;
    ProgressDialog progressDialog;
    int counter= 1;
    Double dbMinimumDeliveryAmount, dbAmountPayable;
    public OrderDetailsListAdapter(Context context, JSONArray jsonArray, String status, String order_id, String Id_order, String orderDate,
                                   String deliveryDate,
                                   String ShopType,String ID_Store,String itemcount,String storeName,
                                   Double dbMinimumDeliveryAmount,Double dbAmountPayable, String OrderType, String ID_CustomerAddress, String DeliveryCharge,String OrderNo) {
        this.context=context;
        this.jsonArray=jsonArray;
        this.status=status;
        this.order_id=order_id;
        this.Id_order=Id_order;
        this.orderDate=orderDate;
        this.deliveryDate=deliveryDate;
        this.ShopType=ShopType;
        this.ID_Store=ID_Store;
        this.itemcount=itemcount;
        this.storeName=storeName;
        this.dbMinimumDeliveryAmount=dbMinimumDeliveryAmount;
        this.dbAmountPayable=dbAmountPayable;
        this.OrderType=OrderType;
        this.ID_CustomerAddress=ID_CustomerAddress;
        this.DeliveryCharge=DeliveryCharge;
        this.OrderNo = OrderNo;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_det_list, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            jsonObject=jsonArray.getJSONObject(position);
            if (holder instanceof MainViewHolder) {
                SharedPreferences qty = context.getSharedPreferences(Config.SHARED_PREF130, 0);
                SharedPreferences Price = context.getSharedPreferences(Config.SHARED_PREF317, 0);
                SharedPreferences OK = context.getSharedPreferences(Config.SHARED_PREF104, 0);
                SharedPreferences Cancel = context.getSharedPreferences(Config.SHARED_PREF105, 0);
                SharedPreferences yes = context.getSharedPreferences(Config.SHARED_PREF186, 0);
                SharedPreferences amountpayablelessthanminimumDeliveryamount = context.getSharedPreferences(Config.SHARED_PREF318, 0);
                SharedPreferences lastproductoforder = context.getSharedPreferences(Config.SHARED_PREF319, 0);
                SharedPreferences AreyousureDoyouwanttodeletethisproductfromorder = context.getSharedPreferences(Config.SHARED_PREF320, 0);


                String PrdctName=jsonObject.getString("ItemName");
                ((MainViewHolder)holder).tvPrdName.setText(position+1+".  "+PrdctName);
//                ((MainViewHolder)holder).tvQty.setText(" [Qty: "+Math.round(jsonObject.getInt("Quantity"))+",");
                ((MainViewHolder)holder).tvQty.setText("["+qty.getString("qty",null)+": "+Math.round(jsonObject.getInt("Quantity"))+",");
                DecimalFormat f = new DecimalFormat("##.00");
                String string = "\u20B9";
                byte[] utf8 = new byte[0];
                try {
                    utf8 = string.getBytes("UTF-8");
                    string = new String(utf8, "UTF-8");
                } catch (UnsupportedEncodingException e) {e.printStackTrace();}
//                ((MainViewHolder)holder).tvPrice.setText("Price: "+/*string+" "+*/f.format(Double.parseDouble(jsonObject.getString("SalesPrice")))+"]");
                ((MainViewHolder)holder).tvPrice.setText(""+Price.getString("Price",null)+": "+/*string+" "+*/f.format(Double.parseDouble(jsonObject.getString("SalesPrice")))+"]");
                if(status.equals("Pending")){
                    ((MainViewHolder)holder).imedit.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).imdlt.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).lledit.setVisibility(View.VISIBLE);
                }else{
                    ((MainViewHolder)holder).imedit.setVisibility(View.GONE);
                    ((MainViewHolder)holder).imdlt.setVisibility(View.GONE);
                    ((MainViewHolder)holder).lledit.setVisibility(View.GONE);
                }
                ((MainViewHolder)holder).imedit.setTag(position);
                ((MainViewHolder)holder).imedit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject=jsonArray.getJSONObject(position);
                            int strQty= Math.round(jsonObject.getInt("Quantity"));
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            View layout = inflater1.inflate(R.layout.editlist, null);
                            SharedPreferences Cancel = context.getSharedPreferences(Config.SHARED_PREF105, 0);
                            SharedPreferences save = context.getSharedPreferences(Config.SHARED_PREF227, 0);
                            SharedPreferences EditQuantity = context.getSharedPreferences(Config.SHARED_PREF321, 0);
                            final TextView quntity = (TextView) layout.findViewById(R.id.tvQtyValue);
                            ImageView QtyAdd = (ImageView) layout.findViewById(R.id.ivQtyAdd);
                            ImageView QtyMinus = (ImageView) layout.findViewById(R.id.ivQtyMinus);
                            TextView tvEdtqty = (TextView)layout.findViewById(R.id.tvEdtqty) ;
                            Button pop_changecancel = (Button) layout.findViewById(R.id.pop_changecancel);
                            Button btn_changesave = (Button) layout.findViewById(R.id.btn_changesave);
                            pop_changecancel.setText(""+Cancel.getString("Cancel",null));
                            btn_changesave.setText(""+save.getString("save",null));
                            tvEdtqty.setText(""+EditQuantity.getString("EditQuantity",null));
                            quntity.setText(""+strQty);
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
                                try{

                                    SharedPreferences Counterpickuppref = context.getSharedPreferences(Config.SHARED_PREF44, 0);
                                    if(Counterpickuppref.getString("Requiredcounterpickup", null).equals("false")) {
                                        if (OrderType.equals("true")){
                                            if (jsonObject.getDouble("Quantity") > Integer.parseInt(quntity.getText().toString())){
                                                jsonObject = jsonArray.getJSONObject(position);
                                                double editedPrice = dbAmountPayable - (jsonObject.getDouble("SalesPrice") * (jsonObject.getDouble("Quantity")-Integer.parseInt(quntity.getText().toString())));
                                                if (dbMinimumDeliveryAmount > editedPrice) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                                    builder.setMessage("Your amount payable will be less than minimum Home Delivery amount. So, you can't edit this item.")
                                                    builder.setMessage(""+amountpayablelessthanminimumDeliveryamount.getString("amountpayablelessthanminimumDeliveryamount",null))
                                                            .setCancelable(false)
                                                            .setPositiveButton(""+OK.getString("OK",null), new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                }
                                                            })
                                                            .setNegativeButton(""+Cancel.getString("Cancel",null), new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.cancel();
                                                                }
                                                            });
                                                    AlertDialog alert = builder.create();
                                                    alert.show();
                                                }
                                                else {
                                                    itemCountChange(jsonObject.getString("ID_SalesOrderDetails"),jsonObject.getString("ID_Item"),jsonObject.getString("ID_Stock"), quntity.getText().toString(),"false");
                                                }
                                            }
                                            else {
                                                itemCountChange(jsonObject.getString("ID_SalesOrderDetails"),jsonObject.getString("ID_Item"),jsonObject.getString("ID_Stock"), quntity.getText().toString(),"false");
                                            }
                                        }
                                        if (OrderType.equals("false")) {
                                            itemCountChange(jsonObject.getString("ID_SalesOrderDetails"), jsonObject.getString("ID_Item"), jsonObject.getString("ID_Stock"), quntity.getText().toString(), "false");
                                        }
                                    }else{
                                        if (OrderType.equals("true")){
                                            if (jsonObject.getDouble("Quantity") > Integer.parseInt(quntity.getText().toString())){
                                                jsonObject = jsonArray.getJSONObject(position);
                                                double editedPrice = dbAmountPayable - (jsonObject.getDouble("SalesPrice") * (jsonObject.getDouble("Quantity")-Integer.parseInt(quntity.getText().toString())));
                                                if (dbMinimumDeliveryAmount > editedPrice) {
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                   // builder.setMessage("No minimum amount for Home Delivery, You can change delivery type to store pickup?")
                                                    builder.setMessage(""+amountpayablelessthanminimumDeliveryamount.getString("amountpayablelessthanminimumDeliveryamount",null))
                                                            .setCancelable(false)
                                                            .setPositiveButton(""+yes.getString("yes",null), new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();
                                                                  /*  try {
                                                                        itemCountChange(jsonObject.getString("ID_SalesOrderDetails"),jsonObject.getString("ID_Item"),jsonObject.getString("ID_Stock"), quntity.getText().toString(),"true");
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }*/
                                                                }
                                                            })
                                                            .setNegativeButton(""+Cancel.getString("Cancel",null), new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.cancel();
                                                                }
                                                            });
                                                    AlertDialog alert = builder.create();
                                                    alert.show();
                                                }
                                                else {
                                                    itemCountChange(jsonObject.getString("ID_SalesOrderDetails"),jsonObject.getString("ID_Item"),jsonObject.getString("ID_Stock"), quntity.getText().toString(),"false");
                                                }
                                            }
                                            else {
                                                itemCountChange(jsonObject.getString("ID_SalesOrderDetails"),jsonObject.getString("ID_Item"),jsonObject.getString("ID_Stock"), quntity.getText().toString(),"false");
                                            }
                                        }
                                        if (OrderType.equals("false")) {
                                            itemCountChange(jsonObject.getString("ID_SalesOrderDetails"), jsonObject.getString("ID_Item"), jsonObject.getString("ID_Stock"), quntity.getText().toString(), "false");
                                        }
                                    }






                                }catch (Exception e){e.printStackTrace();}
                                    alertDialog.dismiss();
                                }
                            });
                            QtyAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    counter = Integer.valueOf(quntity.getText().toString()) + 1;
                                    quntity.setText("" + counter);
                                }
                            });
                            QtyMinus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(Integer.valueOf(quntity.getText().toString())>=2) {
                                        counter = Integer.valueOf(quntity.getText().toString()) - 1;
                                        quntity.setText("" + counter);
                                    }
                                }
                            });
                            alertDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                ((MainViewHolder)holder).imdlt.setTag(position);
                ((MainViewHolder)holder).imdlt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            SharedPreferences Counterpickuppref = context.getSharedPreferences(Config.SHARED_PREF44, 0);
                            if(Counterpickuppref.getString("Requiredcounterpickup", null).equals("false")) {
                                if(OrderType.equals("true")) {
                                    jsonObject = jsonArray.getJSONObject(position);

                                    if (dbMinimumDeliveryAmount > (dbAmountPayable - (jsonObject.getDouble("SalesPrice") * jsonObject.getInt("Quantity")))) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage(""+amountpayablelessthanminimumDeliveryamount.getString("amountpayablelessthanminimumDeliveryamount",null))
                                                .setCancelable(false)
                                                .setPositiveButton(""+OK.getString("OK",null), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                })
                                                .setNegativeButton(""+Cancel.getString("Cancel",null), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    } else {
                                        if (jsonArray.length() == 1) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                            builder.setMessage("It is the last product of this order. Please delete this from MY Orders.")
                                            builder.setMessage(""+lastproductoforder.getString("lastproductoforder",null))
                                                    .setCancelable(false)
                                                    .setNegativeButton(""+OK.getString("OK",null), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                            builder.setMessage("Are you sure ? Do you want to delete this product from order?")
                                            builder.setMessage(""+AreyousureDoyouwanttodeletethisproductfromorder.getString("AreyousureDoyouwanttodeletethisproductfromorder",null))
                                                    .setCancelable(false)
                                                    .setPositiveButton(""+yes.getString("yes",null), new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            try {
                                                                jsonObject = jsonArray.getJSONObject(position);
                                                                itemDelete(jsonObject.getString("ID_SalesOrderDetails"), jsonObject.getString("ID_Item"), jsonObject.getString("ID_Stock"), "false");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            dialog.cancel();
                                                        }
                                                    })
                                                    .setNegativeButton(""+Cancel.getString("Cancel",null), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }
                                    }
                                }
                                else if(OrderType.equals("false")) {
                                    if (jsonArray.length() == 1) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage(""+lastproductoforder.getString("lastproductoforder",null))
                                                .setCancelable(false)
                                                .setNegativeButton(""+OK.getString("OK",null), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                    else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage(""+AreyousureDoyouwanttodeletethisproductfromorder.getString("AreyousureDoyouwanttodeletethisproductfromorder",null))
                                                .setCancelable(false)
                                                .setPositiveButton(""+yes.getString("yes",null), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        try {
                                                            jsonObject = jsonArray.getJSONObject(position);
                                                            itemDelete(jsonObject.getString("ID_SalesOrderDetails"), jsonObject.getString("ID_Item"), jsonObject.getString("ID_Stock"), "false");
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        dialog.cancel();
                                                    }
                                                })
                                                .setNegativeButton(""+Cancel.getString("Cancel",null), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }
                            }else{
                                if(OrderType.equals("true")) {
                                    jsonObject = jsonArray.getJSONObject(position);

                                    if (dbMinimumDeliveryAmount > (dbAmountPayable - (jsonObject.getDouble("SalesPrice") * jsonObject.getInt("Quantity")))) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage(""+amountpayablelessthanminimumDeliveryamount.getString("amountpayablelessthanminimumDeliveryamount",null))
                                                .setCancelable(false)
                                                .setPositiveButton(""+yes.getString("yes",null), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {

                                                        dialog.cancel();
/*
                                                        if (jsonArray.length() == 1) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                            builder.setMessage("It is the last product of this order. Please delete this from MY Orders.")
                                                                    .setCancelable(false)
                                                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            dialog.cancel();
                                                                        }
                                                                    });
                                                            AlertDialog alert = builder.create();
                                                            alert.show();
                                                        }
                                                        else {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                                            builder.setMessage("Are you sure ? Do you want to delete this product from order?")
                                                                    .setCancelable(false)
                                                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                                        public void onClick(DialogInterface dialog, int id) {
                                                                            try {
                                                                                jsonObject = jsonArray.getJSONObject(position);
                                                                                itemDelete(jsonObject.getString("ID_SalesOrderDetails"), jsonObject.getString("ID_Item"), jsonObject.getString("ID_Stock"), "true");
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                            dialog.cancel();
                                                                        }
                                                                    })
                                                                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            dialog.cancel();
                                                                        }
                                                                    });
                                                            AlertDialog alert = builder.create();
                                                            alert.show();
                                                        }*/

                                                    }
                                                })
                                                .setNegativeButton(""+Cancel.getString("Cancel",null), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    } else {
                                        if (jsonArray.length() == 1) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setMessage(""+lastproductoforder.getString("lastproductoforder",null))
                                                    .setCancelable(false)
                                                    .setNegativeButton(""+OK.getString("OK",null), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                            builder.setMessage(""+AreyousureDoyouwanttodeletethisproductfromorder.getString("AreyousureDoyouwanttodeletethisproductfromorder",null))
                                                    .setCancelable(false)
                                                    .setPositiveButton(""+yes.getString("yes",null), new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            try {
                                                                jsonObject = jsonArray.getJSONObject(position);
                                                                itemDelete(jsonObject.getString("ID_SalesOrderDetails"), jsonObject.getString("ID_Item"), jsonObject.getString("ID_Stock"), "false");
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            dialog.cancel();
                                                        }
                                                    })
                                                    .setNegativeButton(""+Cancel.getString("Cancel",null), new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }
                                    }
                                }
                                else if(OrderType.equals("false")) {
                                    if (jsonArray.length() == 1) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage(""+lastproductoforder.getString("lastproductoforder",null))
                                                .setCancelable(false)
                                                .setNegativeButton(""+OK.getString("OK",null), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                    else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setMessage(""+AreyousureDoyouwanttodeletethisproductfromorder.getString("AreyousureDoyouwanttodeletethisproductfromorder",null))
                                                .setCancelable(false)
                                                .setPositiveButton(""+yes.getString("yes",null), new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        try {
                                                            jsonObject = jsonArray.getJSONObject(position);
                                                            itemDelete(jsonObject.getString("ID_SalesOrderDetails"), jsonObject.getString("ID_Item"), jsonObject.getString("ID_Stock"), "false");
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        dialog.cancel();
                                                    }
                                                })
                                                .setNegativeButton(""+Cancel.getString("Cancel",null), new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    }
                                }
                            }

                    }catch (Exception e) {
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
        LinearLayout lnLayout, lledit;
        TextView tvPrdName, tvQty, tvPrice;
        ImageView imedit,imdlt ;
        public MainViewHolder(View v) {
            super(v);
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            lledit=(LinearLayout) v.findViewById(R.id.lledit);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            tvQty=(TextView)v.findViewById(R.id.tvQty);
            tvPrice=(TextView)v.findViewById(R.id.tvPrice);
            imedit =(ImageView)v.findViewById(R.id.imedit);
            imdlt =(ImageView)v.findViewById(R.id.imdlt);
        }
    }

    public  void itemDelete(String ID_SalesOrderDetails,String ID_Item,String ID_Stock,String ChangeDeliveryType ){
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
                requestObject1.put("UserAction", "2");
                requestObject1.put("ID_Items", ID_Item);
                requestObject1.put("ID_Stock", ID_Stock);
                requestObject1.put("ID_SalesOrderDetails", ID_SalesOrderDetails);
                requestObject1.put("Bank_Key", context.getResources().getString(R.string.BankKey));
                requestObject1.put("ChangeDeliveryType", ChangeDeliveryType);
                SharedPreferences IDLanguages = context.getSharedPreferences(Config.SHARED_PREF80, 0);
                requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
            Call<String> call = apiService.removeOrderItem(body);
            call.enqueue(new Callback<String>() {
                @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    try {
                        progressDialog.dismiss();
                        JSONObject jObject = new JSONObject(response.body());
                        JSONObject jobj = jObject.getJSONObject("RemoveSalesOrderItemsDetails");
                        if(jObject.getString("StatusCode").equals("4")){
                            Toast.makeText(context,jobj.getString("ResponseMessage"),Toast.LENGTH_LONG).show();
                            Intent i = new Intent(context, OrderDetailsActivity.class);
                            i.putExtra("order_id", order_id);
                            i.putExtra("Id_order", Id_order);
                            i.putExtra("OrderNo", OrderNo);
                            i.putExtra("orderDate", orderDate);
                            i.putExtra("deliveryDate", deliveryDate);
                            i.putExtra("status", status);
                            i.putExtra("ShopType", ShopType);
                            i.putExtra("ID_Store", ID_Store);
                            i.putExtra("itemcount", itemcount);
                            i.putExtra("storeName", storeName);
                            i.putExtra("ID_CustomerAddress",ID_CustomerAddress);
                            i.putExtra("OrderType", OrderType);
                            i.putExtra("DeliveryCharge", DeliveryCharge);

                            context.startActivity(i);
                            ((OrderDetailsActivity)context).finish();
                        }else {
                            Toast.makeText(context,jobj.getString("ResponseMessage"),Toast.LENGTH_LONG).show();
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
        } }else {
            Intent in = new Intent(context, NoInternetActivity.class);
            context.startActivity(in);
        }
    }

    public  void itemCountChange(String ID_SalesOrderDetails,String ID_Item,String ID_Stock, String Qty , String ChangeDeliveryType ){
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
                requestObject1.put("UserAction", "4");
                requestObject1.put("ID_Items", ID_Item);
                requestObject1.put("ID_Stock", ID_Stock);
                requestObject1.put("ID_SalesOrderDetails", ID_SalesOrderDetails);
                requestObject1.put("Quantity", Qty);
                requestObject1.put("Bank_Key", context.getResources().getString(R.string.BankKey));
                requestObject1.put("ChangeDeliveryType", ChangeDeliveryType);
                SharedPreferences IDLanguages = context.getSharedPreferences(Config.SHARED_PREF80, 0);
                requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
            Call<String> call = apiService.editOrderItemCount(body);
            call.enqueue(new Callback<String>() {
                @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    try {
                        progressDialog.dismiss();
                        JSONObject jObject = new JSONObject(response.body());
                        JSONObject jobj = jObject.getJSONObject("EditSalesOrder");
                        if(jObject.getString("StatusCode").equals("4")){
                            Toast.makeText(context,jobj.getString("ResponseMessage"),Toast.LENGTH_LONG).show();
                            Intent i = new Intent(context, OrderDetailsActivity.class);
                            i.putExtra("order_id", order_id);
                            i.putExtra("OrderNo", OrderNo);
                            i.putExtra("orderDate", orderDate);
                            i.putExtra("deliveryDate", deliveryDate);
                            i.putExtra("status", status);
                            i.putExtra("ShopType", ShopType);
                            i.putExtra("ID_Store", ID_Store);
                            i.putExtra("itemcount", itemcount);
                            i.putExtra("storeName", storeName);
                            i.putExtra("ID_CustomerAddress",ID_CustomerAddress);
                            i.putExtra("OrderType", OrderType);
                            i.putExtra("DeliveryCharge", DeliveryCharge);
                            context.startActivity(i);
                            ((OrderDetailsActivity)context).finish();
                        }else {
                            Toast.makeText(context,jobj.getString("ResponseMessage"),Toast.LENGTH_LONG).show();
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
