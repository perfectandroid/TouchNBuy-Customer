package com.perfect.easyshopplus.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Activity.NoInternetActivity;
import com.perfect.easyshopplus.Activity.OrderDetailsActivity;
import com.perfect.easyshopplus.Activity.OrdersActivity;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class OrderListAdapter extends RecyclerView.Adapter {

    String TAG = "OrderListAdapter";
    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    String Id_order,orderDate,deliveryDate,deliveryTime,status,Order_number;
    ProgressDialog progressDialog;
    private Date ordrdate,dlvrdate, dlvrtime;
    RatingBar ratingbar;
    TextView txtvSbmt,txtlater,tv_ratesub;

    public OrderListAdapter(Context context, JSONArray jsonArray) {
        this.context=context;
        this.jsonArray=jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_orders_list, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            jsonObject=jsonArray.getJSONObject(position);
            SharedPreferences ordertype = context.getSharedPreferences(Config.SHARED_PREF157, 0);
            SharedPreferences Store = context.getSharedPreferences(Config.SHARED_PREF119, 0);
            SharedPreferences closedon = context.getSharedPreferences(Config.SHARED_PREF159, 0);
            SharedPreferences CounterPickup = context.getSharedPreferences(Config.SHARED_PREF305, 0);
            SharedPreferences HomeDeliverys = context.getSharedPreferences(Config.SHARED_PREF300, 0);
            SharedPreferences Verifiedon = context.getSharedPreferences(Config.SHARED_PREF301, 0);
            SharedPreferences Packedon = context.getSharedPreferences(Config.SHARED_PREF302, 0);
            SharedPreferences ordernumber = context.getSharedPreferences(Config.SHARED_PREF156, 0);
            SharedPreferences orderedon = context.getSharedPreferences(Config.SHARED_PREF158, 0);
            SharedPreferences Ordereddate = context.getSharedPreferences(Config.SHARED_PREF171, 0);
            SharedPreferences Deliveredon = context.getSharedPreferences(Config.SHARED_PREF303, 0);
            SharedPreferences yes = context.getSharedPreferences(Config.SHARED_PREF186, 0);
            SharedPreferences Cancel = context.getSharedPreferences(Config.SHARED_PREF105, 0);
            SharedPreferences rateus = context.getSharedPreferences(Config.SHARED_PREF196, 0);
            SharedPreferences AreyousureDoyouwanttodeletethisorder = context.getSharedPreferences(Config.SHARED_PREF306, 0);

            if (holder instanceof MainViewHolder) {
                Id_order=jsonObject.getString("ID_SalesOrder");
                Order_number=jsonObject.getString("OrderNo");
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");
                SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");


                if(jsonObject.getString("OrderType").equals("true")){
                    ((MainViewHolder)holder).tvDeliveryType.setText(""+ordertype.getString("ordertype",null)+" : "+HomeDeliverys.getString("HomeDeliverys",null));
                }
				else{
                    ((MainViewHolder)holder).tvDeliveryType.setText(""+ordertype.getString("ordertype",null)+" : "+CounterPickup.getString("CounterPickup",null));
				}
                if(jsonObject.getString("DeliveryDate").isEmpty()){
                    orderDate = "";
                    deliveryDate = "";
				}
				else {
                    try {
                        ordrdate = new SimpleDateFormat("dd-MM-yyyy").parse(jsonObject.getString("OrderDate"));
                        dlvrdate = new SimpleDateFormat("dd-MM-yyyy").parse(jsonObject.getString("DeliveryDate"));
                        orderDate = sdf.format(ordrdate);
                        deliveryDate = sdf1.format(dlvrdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if(jsonObject.getString("DeliveryTime").isEmpty()){
                    deliveryTime = "";
                }else {
                    try {
                        dlvrtime = new SimpleDateFormat("hh:mm aa").parse(jsonObject.getString("DeliveryTime"));
                        deliveryTime = sdf2.format(dlvrtime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                status=jsonObject.getString("status");
                if(status.equals("Pending")){
                    PicassoTrustAll.getInstance(context).load(R.drawable.status_pending).into(((MainViewHolder)holder).imStatus);
                    ((MainViewHolder)holder).llrateus.setVisibility(View.GONE);
                    ((MainViewHolder)holder).imdelete.setVisibility(View.VISIBLE);
                    SharedPreferences pending = context.getSharedPreferences(Config.SHARED_PREF149, 0);
//                    ((MainViewHolder)holder).tvStatus.setText(status);
                    ((MainViewHolder)holder).tvStatus.setText(pending.getString("pending",""));
                    ((MainViewHolder)holder).tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pending, 0, 0, 0);
                    ((MainViewHolder)holder).tvStatus.setTextColor(context.getResources().getColor(R.color.red));
                    ((MainViewHolder)holder).tvDeliveryDate.setText("");
                    ((MainViewHolder)holder).tvDeliveryDate.setVisibility(View.GONE);

                    SharedPreferences delivered = context.getSharedPreferences(Config.SHARED_PREF152, 0);
                    SharedPreferences confirmed = context.getSharedPreferences(Config.SHARED_PREF150, 0);
                    SharedPreferences packed = context.getSharedPreferences(Config.SHARED_PREF151, 0);
                    ((MainViewHolder)holder).tv_pend.setText(pending.getString("pending",""));
                    ((MainViewHolder)holder).tv_conf.setText(confirmed.getString("confirmed",""));
                    ((MainViewHolder)holder).tv_pack.setText(packed.getString("packed",""));
                    ((MainViewHolder)holder).tv_deli.setText(delivered.getString("delivered",""));
                }else{
                    ((MainViewHolder)holder).imdelete.setVisibility(View.GONE);
                }
                if(status.equals("Confirmed")){
                    PicassoTrustAll.getInstance(context).load(R.drawable.status_verified).into(((MainViewHolder)holder).imStatus);
                    ((MainViewHolder)holder).llrateus.setVisibility(View.GONE);
                    SharedPreferences confirmed = context.getSharedPreferences(Config.SHARED_PREF150, 0);
//                    ((MainViewHolder)holder).tvStatus.setText(status);
                    ((MainViewHolder)holder).tvStatus.setText(confirmed.getString("confirmed",""));
                    ((MainViewHolder)holder).tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.verifiedtick, 0, 0, 0);
                    ((MainViewHolder)holder).tvStatus.setTextColor(context.getResources().getColor(R.color.green));

                    SharedPreferences pending = context.getSharedPreferences(Config.SHARED_PREF149, 0);
                    SharedPreferences delivered = context.getSharedPreferences(Config.SHARED_PREF152, 0);
                    SharedPreferences packed = context.getSharedPreferences(Config.SHARED_PREF151, 0);
                    ((MainViewHolder)holder).tv_pend.setText(pending.getString("pending",""));
                    ((MainViewHolder)holder).tv_conf.setText(confirmed.getString("confirmed",""));
                    ((MainViewHolder)holder).tv_pack.setText(packed.getString("packed",""));
                    ((MainViewHolder)holder).tv_deli.setText(delivered.getString("delivered",""));

                    ((MainViewHolder)holder).img_conf.setBackground(context.getDrawable(R.drawable.img_confirm));
                    ((MainViewHolder)holder).view_conf.setBackgroundResource(R.color.color_pbstatus);

                    if(deliveryTime.isEmpty()){
                        ((MainViewHolder)holder).tvDeliveryDate.setText(""+Verifiedon.getString("Verifiedon",null)+" : "+ deliveryDate +".");
                    }else{
                        ((MainViewHolder)holder).tvDeliveryDate.setText(""+Verifiedon.getString("Verifiedon",null)+" : "+ deliveryDate + " , "+deliveryTime);
                    }
                }
                if(status.equals("Delivered")){
                    PicassoTrustAll.getInstance(context).load(R.drawable.status_deliverd).into(((MainViewHolder)holder).imStatus);
                    ((MainViewHolder)holder).llrateus.setVisibility(View.VISIBLE);
                    SharedPreferences delivered = context.getSharedPreferences(Config.SHARED_PREF152, 0);
//                    ((MainViewHolder)holder).tvStatus.setText(status);
                    ((MainViewHolder)holder).tvStatus.setText(delivered.getString("delivered",""));
                    ((MainViewHolder)holder).tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.closeddoubletick, 0, 0, 0);
                    ((MainViewHolder)holder).tvStatus.setTextColor(context.getResources().getColor(R.color.colorAccent));

                    SharedPreferences pending = context.getSharedPreferences(Config.SHARED_PREF149, 0);
                    SharedPreferences confirmed = context.getSharedPreferences(Config.SHARED_PREF150, 0);
                    SharedPreferences packed = context.getSharedPreferences(Config.SHARED_PREF151, 0);
                    ((MainViewHolder)holder).tv_pend.setText(pending.getString("pending",""));
                    ((MainViewHolder)holder).tv_conf.setText(confirmed.getString("confirmed",""));
                    ((MainViewHolder)holder).tv_pack.setText(packed.getString("packed",""));
                    ((MainViewHolder)holder).tv_deli.setText(delivered.getString("delivered",""));
                    ((MainViewHolder)holder).img_conf.setBackground(context.getDrawable(R.drawable.img_confirm));
                    ((MainViewHolder)holder).img_pack.setBackground(context.getDrawable(R.drawable.img_pack));
                    ((MainViewHolder)holder).img_deli.setBackground(context.getDrawable(R.drawable.img_del));
                    ((MainViewHolder)holder).view_conf.setBackgroundResource(R.color.color_pbstatus);
                    ((MainViewHolder)holder).view_packed.setBackgroundResource(R.color.color_pbstatus);
                    ((MainViewHolder)holder).view_deli.setBackgroundResource(R.color.color_pbstatus);

                    if(deliveryTime.isEmpty()){
                        ((MainViewHolder)holder).tvDeliveryDate.setText(""+closedon.getString("closedon",null)+" : "+ deliveryDate + ".");
                    }else{
                        ((MainViewHolder)holder).tvDeliveryDate.setText(""+closedon.getString("closedon",null)+" : "+ deliveryDate + " , "+deliveryTime);
                    }
                }
                if(status.equals("Packed")){
                    PicassoTrustAll.getInstance(context).load(R.drawable.status_packed).into(((MainViewHolder)holder).imStatus);
                    ((MainViewHolder)holder).llrateus.setVisibility(View.GONE);
                    SharedPreferences packed = context.getSharedPreferences(Config.SHARED_PREF151, 0);
//                    ((MainViewHolder)holder).tvStatus.setText(status);
                    ((MainViewHolder)holder).tvStatus.setText(packed.getString("packed",""));
                    ((MainViewHolder)holder).tvStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.packed, 0, 0, 0);
                    ((MainViewHolder)holder).tvStatus.setTextColor(context.getResources().getColor(R.color.orange));

                    SharedPreferences pending = context.getSharedPreferences(Config.SHARED_PREF149, 0);
                    SharedPreferences delivered = context.getSharedPreferences(Config.SHARED_PREF152, 0);
                    SharedPreferences confirmed = context.getSharedPreferences(Config.SHARED_PREF150, 0);
                    ((MainViewHolder)holder).tv_pend.setText(pending.getString("pending",""));
                    ((MainViewHolder)holder).tv_conf.setText(confirmed.getString("confirmed",""));
                    ((MainViewHolder)holder).tv_pack.setText(packed.getString("packed",""));
                    ((MainViewHolder)holder).tv_deli.setText(delivered.getString("delivered",""));


                    ((MainViewHolder)holder).img_conf.setBackground(context.getDrawable(R.drawable.img_confirm));
                    ((MainViewHolder)holder).img_pack.setBackground(context.getDrawable(R.drawable.img_pack));
                    ((MainViewHolder)holder).view_conf.setBackgroundResource(R.color.color_pbstatus);
                    ((MainViewHolder)holder).view_packed.setBackgroundResource(R.color.color_pbstatus);

                    if(deliveryTime.isEmpty()){
                        ((MainViewHolder)holder).tvDeliveryDate.setText(""+Packedon.getString("Packedon",null)+" : "+ deliveryDate + ".");
                    }else{
                        ((MainViewHolder)holder).tvDeliveryDate.setText(""+Packedon.getString("Packedon",null)+" : "+ deliveryDate + " , "+deliveryTime);

                    }
                }

                ((MainViewHolder)holder).tvOrderID.setText(""+ordernumber.getString("ordernumber",null)+" : "+Order_number);
                ((MainViewHolder)holder).tvOrderDate.setText(""+orderedon.getString("orderedon",null)+" : "+orderDate);

				
                if (!jsonObject.getString("StoreShortName").equals("")){
                    ((MainViewHolder)holder).llStoreName.setTag(position);
                    ((MainViewHolder)holder).tvstoreName.setTag(position);
                    ((MainViewHolder)holder).llStoreName.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).tvstoreName.setText(""+Store.getString("Store",null)+" :"+jsonObject.getString("StoreShortName"));
                }
                else {
                    ((MainViewHolder)holder).llStoreName.setTag(position);
                    ((MainViewHolder)holder).llStoreName.setVisibility(View.GONE);
                }
                ((MainViewHolder)holder).lnLayout.setTag(position);
                ((MainViewHolder)holder).lnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            SharedPreferences TimeSlotCheckpref = context.getSharedPreferences(Config.SHARED_PREF61, 0);
                            SharedPreferences.Editor TimeSlotCheckeditor = TimeSlotCheckpref.edit();
                            TimeSlotCheckeditor.putString("TimeSlotCheckReorder", jsonObject.getString("IsAsOnDateApplicable"));
                            TimeSlotCheckeditor.commit();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");
                            SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
                            if (jsonObject.getString("DeliveryTime").isEmpty()) {
                            } else {
                                try {
                                    ordrdate = new SimpleDateFormat(/*"MM/dd/yyyy hh:mm:ss a"*/"dd-MM-yyyy").parse(jsonObject.getString("OrderDate"));
                                    dlvrdate = new SimpleDateFormat(/*"MM/dd/yyyy hh:mm:ss a"*/"dd-MM-yyyy").parse(jsonObject.getString("DeliveryDate"));
                                    dlvrtime = new SimpleDateFormat("hh:mm aa").parse(jsonObject.getString("DeliveryTime"));
                                    orderDate = sdf.format(ordrdate);
                                    deliveryDate = sdf1.format(dlvrdate);
                                    deliveryTime = sdf2.format(dlvrtime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            Intent i = new Intent(context, OrderDetailsActivity.class);
                                i.putExtra("order_id", jsonObject.getString("ID_SalesOrder"));
//                                SharedPreferences spOrderNumber = context.getSharedPreferences(Config.SHARED_PREF66, 0);
//                                i.putExtra("Id_order", spOrderNumber.getString("OrderNumber", null) + jsonObject.getString("OrderNo"));
//                                SharedPreferences spOrderedDate = context.getSharedPreferences(Config.SHARED_PREF68, 0);
//                                i.putExtra("orderDate", spOrderedDate.getString("OrderedDate", null) + orderDate);


                                i.putExtra("OrderNo", jsonObject.getString("OrderNo"));

                                i.putExtra("Id_order", ""+ordernumber.getString("ordernumber",null)+" : " + jsonObject.getString("OrderNo"));
                                i.putExtra("orderDate", ""+Ordereddate.getString("Ordereddate",null)+" : " + orderDate);

                                if (jsonObject.getString("status").equals("Closed")) {
                                    i.putExtra("deliveryDate", ""+closedon.getString("closedon",null)+" " + deliveryDate + " , " + deliveryTime);
                                }
                                if (jsonObject.getString("status").equals("Confirmed")) {
                                    i.putExtra("deliveryDate", ""+Verifiedon.getString("Verifiedon",null)+" " + deliveryDate + " , " + deliveryTime);
                                }
                                if (jsonObject.getString("status").equals("Packed")) {
                                    i.putExtra("deliveryDate", ""+Packedon.getString("Packedon",null)+" " + deliveryDate + " , " + deliveryTime);
                                }
                                if (jsonObject.getString("status").equals("Delivered")) {
                                    i.putExtra("deliveryDate", ""+Deliveredon.getString("Deliveredon",null)+" " + deliveryDate + " , " + deliveryTime);
                                }
                                if (jsonObject.getString("status").equals("Pending")) {
                                    i.putExtra("deliveryDate", "");
                                }
                                i.putExtra("status", jsonObject.getString("status"));
                                i.putExtra("ID_Store", jsonObject.getString("ID_Store"));
                                i.putExtra("ShopType", jsonObject.getString("ShopType"));
                                i.putExtra("itemcount", jsonArray.length());
                                i.putExtra("ID_CustomerAddress", jsonObject.getString("ID_CustomerAddress"));
                                i.putExtra("OrderType", jsonObject.getString("OrderType"));
                                i.putExtra("storeName", jsonObject.getString("StoreLongName"));
                                i.putExtra("DeliveryCharge", jsonObject.getString("DeliveryCharge"));
                                i.putExtra("MinimumDeliveryAmount", jsonObject.getString("MinimumDeliveryAmount"));

                            context.startActivity(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                      /*  try {
                            jsonObject = jsonArray.getJSONObject(position);
                            if (jsonObject.getInt("AmountPayable")<1) {

                            } else {
                                try {
                                    jsonObject = jsonArray.getJSONObject(position);
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
                                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd MMM yyyy");
                                    SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm a");
                                    if (jsonObject.getString("DeliveryTime").isEmpty()) {
                                    } else {
                                        try {
                                            ordrdate = new SimpleDateFormat(*//*"MM/dd/yyyy hh:mm:ss a"*//*"dd-MM-yyyy").parse(jsonObject.getString("OrderDate"));
                                            dlvrdate = new SimpleDateFormat(*//*"MM/dd/yyyy hh:mm:ss a"*//*"dd-MM-yyyy").parse(jsonObject.getString("DeliveryDate"));
                                            dlvrtime = new SimpleDateFormat("hh:mm aa").parse(jsonObject.getString("DeliveryTime"));
                                            orderDate = sdf.format(ordrdate);
                                            deliveryDate = sdf1.format(dlvrdate);
                                            deliveryTime = sdf2.format(dlvrtime);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    Intent i = new Intent(context, OrderDetailsActivity.class);
                                    i.putExtra("order_id", jsonObject.getString("ID_SalesOrder"));
                                    i.putExtra("Id_order", "Order Number : " + jsonObject.getString("OrderNo"));
                                    i.putExtra("orderDate", "Ordered on " + orderDate);
                                    if (jsonObject.getString("status").equals("Closed")) {
                                        i.putExtra("deliveryDate", "Closed on " + deliveryDate + " at " + deliveryTime);
                                    }
                                    if (jsonObject.getString("status").equals("Packed")) {
                                        i.putExtra("deliveryDate", "Packed on " + deliveryDate + " at " + deliveryTime);
                                    }
                                    if (jsonObject.getString("status").equals("Closed")) {
                                        i.putExtra("deliveryDate", "Closed on " + deliveryDate + " at " + deliveryTime);
                                    }
                                    if (jsonObject.getString("status").equals("Pending")) {
                                        i.putExtra("deliveryDate", "");
                                    }
                                    i.putExtra("status", jsonObject.getString("status"));
                                    i.putExtra("ID_Store", jsonObject.getString("ID_Store"));
                                    i.putExtra("ShopType", jsonObject.getString("ShopType"));
                                    i.putExtra("itemcount", jsonArray.length());
                                    i.putExtra("ID_CustomerAddress", jsonObject.getString("ID_CustomerAddress"));
                                    i.putExtra("OrderType", jsonObject.getString("OrderType"));
                                    i.putExtra("storeName", jsonObject.getString("StoreLongName"));
                                    i.putExtra("DeliveryCharge", jsonObject.getString("DeliveryCharge"));

                                    context.startActivity(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                });
             /*   ((MainViewHolder)holder).btnreorder.setTag(position);
                ((MainViewHolder)holder).btnreorder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                          }
                });*/

                ((MainViewHolder)holder).btnrate.setTag(position);
                ((MainViewHolder)holder).btnrate.setText(""+rateus.getString("rateus",null));

                ((MainViewHolder)holder).btnrate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                ratePopup();
                            }
                        }, 100);
                          }
                });

                ((MainViewHolder)holder).imdelete.setTag(position);
                ((MainViewHolder)holder).imdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder= new AlertDialog.Builder(context);
                        builder.setMessage(""+AreyousureDoyouwanttodeletethisorder.getString("AreyousureDoyouwanttodeletethisorder",null))
                                .setCancelable(false)
                                .setPositiveButton(""+yes.getString("yes",null), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            jsonObject=jsonArray.getJSONObject(position);
                                            doItemDelete(jsonObject.getString("ID_SalesOrder"));
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
        LinearLayout lnLayout,llStoreName,llrateus;
        TextView tvOrderID,tvOrderDate,tvDeliveryDate,tvStatus,tvDeliveryType,tvstoreName;
        ImageView imdelete,imStatus;
        Button btnrate ;
        View view_conf,view_packed,view_deli;
        ImageView img_conf,img_pack,img_deli;
        TextView tv_pend,tv_conf,tv_pack,tv_deli;
        public MainViewHolder(View v) {
            super(v);
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            llStoreName=(LinearLayout) v.findViewById(R.id.llStoreName);
            tvOrderID=(TextView)v.findViewById(R.id.tvOrderId);
            tvStatus=(TextView) v.findViewById(R.id.tvStatus);
            tvOrderDate=(TextView)v.findViewById(R.id.tvOrderDate);
            tvDeliveryDate=(TextView)v.findViewById(R.id.tvDeliveryDate);
            tvDeliveryType=(TextView)v.findViewById(R.id.tvDeliveryType);
            tvstoreName=(TextView)v.findViewById(R.id.tvstoreName);
            imStatus =(ImageView) v.findViewById(R.id.imStatus);
            imdelete=(ImageView) v.findViewById(R.id.imdelete);
            btnrate=(Button) v.findViewById(R.id.btnrate);
            llrateus=(LinearLayout) v.findViewById(R.id.llrateus);

            view_conf=(View) v.findViewById(R.id.view_conf);
            view_packed=(View) v.findViewById(R.id.view_packed);
            view_deli=(View) v.findViewById(R.id.view_deli);

            img_conf =(ImageView) v.findViewById(R.id.img_conf);
            img_pack =(ImageView) v.findViewById(R.id.img_pack);
            img_deli =(ImageView) v.findViewById(R.id.img_deli);

            tv_pend=(TextView)v.findViewById(R.id.tv_pend);
            tv_conf=(TextView)v.findViewById(R.id.tv_conf);
            tv_pack=(TextView)v.findViewById(R.id.tv_pack);
            tv_deli=(TextView)v.findViewById(R.id.tv_deli);
        }
    }

    public  void doItemDelete(String ID_SalesOrder ){
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
                requestObject1.put("UserAction", "3");
                requestObject1.put("ID_SalesOrder", ID_SalesOrder);
                requestObject1.put("Bank_Key", context.getResources().getString(R.string.BankKey));
                SharedPreferences IDLanguages = context.getSharedPreferences(Config.SHARED_PREF80, 0);
                requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
            Call<String> call = apiService.removeSalesOrder(body);
            call.enqueue(new Callback<String>() {
                @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                    try {
                        progressDialog.dismiss();
                        JSONObject jObject = new JSONObject(response.body());
                        JSONObject jobj = jObject.getJSONObject("deleteSalesOrder");
                        if(jObject.getString("StatusCode").equals("4")){
                          Toast.makeText(context,"Order has been Removed Successfully",Toast.LENGTH_LONG).show();
                          Intent i = new Intent(context, OrdersActivity.class);
                            context.startActivity(i);
                            ((OrdersActivity)context).finish();
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

    private void ratePopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater1 = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        SharedPreferences Cancel = context.getSharedPreferences(Config.SHARED_PREF105, 0);
        SharedPreferences submit = context.getSharedPreferences(Config.SHARED_PREF236, 0);
        SharedPreferences Howwasyourexperiencewiththisstore = context.getSharedPreferences(Config.SHARED_PREF308, 0);

        View layout = inflater1.inflate(R.layout.ratingbarpopup, null);
        ratingbar = (RatingBar) layout.findViewById(R.id.ratingBar);
        txtvSbmt = (TextView) layout.findViewById(R.id.txtvSubmit);
        txtlater= (TextView) layout.findViewById(R.id.txtlater);
        tv_ratesub = (TextView) layout.findViewById(R.id.tv_ratesub);

        builder.setView(layout);
        final AlertDialog alertDialog = builder.create();
        txtlater.setText(""+Cancel.getString("Cancel",null));
        txtvSbmt.setText(""+submit.getString("submit",null));
        tv_ratesub.setText(""+Howwasyourexperiencewiththisstore.getString("Howwasyourexperiencewiththisstore",null));

        txtvSbmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating=String.valueOf(ratingbar.getRating());
                getRating(rating);
                alertDialog.dismiss();
            }
        });
        txtlater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();

    }
    private void getRating(String rating) {

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
                    SharedPreferences pref = context.getSharedPreferences(Config.SHARED_PREF1, 0);

                    requestObject1.put("FK_SalesOrder", Id_order);
                    String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                    requestObject1.put("TransDate",date);
                    requestObject1.put("Rate", rating);
                    requestObject1.put("UserAction", "1");
                    requestObject1.put("Bank_Key", context.getString(R.string.BankKey));
                    SharedPreferences IDLanguages = context.getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

                    Log.e(TAG,"requestObject1  7271   "+requestObject1);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getRating(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            progressDialog.dismiss();
                            JSONObject jObject = new JSONObject(response.body());
                            Log.e(TAG,"response  7272   "+response.body());

                            JSONObject jobj = jObject.getJSONObject("StoreRate");
                            String msg = jobj.getString("ResponseMessage");
                            Toast.makeText(context,""+msg,Toast.LENGTH_LONG).show();

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
        }

    }
}
