package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perfect.easyshopplus.Activity.OrderItemListingActivity;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderCategoryAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
//    String Id_order, orderDate, deliveryDate, status, order_id, ShopType, ID_Store, itemcount,ID_SalesOrder, storeName;
    String Id_order, orderDate, deliveryDate, status,DeliveryCharge, order_id,OrderType,ID_CustomerAddress, ShopType, ID_Store, itemcount,ID_SalesOrder, storeName;

    public OrderCategoryAdapter(Context context, JSONArray jsonArray,
                                String ID_SalesOrder, String order_id, String deliveryDate,
                                String Id_order, String orderDate,
                                String status, String ID_Store, String ShopType,
                                String itemcount,String ID_CustomerAddress,String OrderType,
                                String storeName,String DeliveryCharge) {



        this.context=context;
        this.jsonArray=jsonArray;
        this.ID_Store=ID_Store;
        this.DeliveryCharge=DeliveryCharge;
        this.OrderType=OrderType;
        this.ID_CustomerAddress=ID_CustomerAddress;
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
                R.layout.activity_cat_grid, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            jsonObject=jsonArray.getJSONObject(position);
            if (holder instanceof MainViewHolder) {
                String PrdctName=jsonObject.getString("FirstCategoryName");

                SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
                SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
                String BASEURL = baseurlpref.getString("BaseURL", null);
                final String IMAGEURL = imgpref.getString("ImageURL", null);
                String imagepath= IMAGEURL + jsonObject.getString("ImgePath");
//                Glide.with(context)
//                        .load(imagepath)
//                        .placeholder(R.drawable.items)

//                        .error(R.drawable.items)
//                        .into(((MainViewHolder) holder).imCat);

                PicassoTrustAll.getInstance(context).load(imagepath).error(R.drawable.items).into(((MainViewHolder) holder).imCat);
                ((MainViewHolder)holder).tvPrdName.setText(PrdctName);
                ((MainViewHolder)holder).lnLayout.setTag(position);
                ((MainViewHolder)holder).lnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                        jsonObject = jsonArray.getJSONObject(position);
                      /*      Intent intent = new Intent(context, NewOrderSubCategoryActivity.class);
                            intent.putExtra("ID_CategoryFirst", jsonObject.getString("ID_CategoryFirst"));
                            intent.putExtra("FirstCategoryName", jsonObject.getString("FirstCategoryName"));
                            intent.putExtra("ID_Store", ID_Store);
                            intent.putExtra("ShopType", ShopType);
                            intent.putExtra("ID_SalesOrder", ID_SalesOrder);
                            intent.putExtra("order_id", order_id);
                            intent.putExtra("Id_order", Id_order);
                            intent.putExtra("orderDate", orderDate);
                            intent.putExtra("deliveryDate", deliveryDate);
                            intent.putExtra("status", status);
                            intent.putExtra("itemcount", itemcount);
                        context.startActivity(intent);
                        ((Activity)context).finish();*/


                            Intent intent = new Intent(context, OrderItemListingActivity.class);
                           // intent.putExtra("ID_CategorySecond", "");
                           // intent.putExtra("SecondCategoryName", "");
                            intent.putExtra("ID_CategoryFirst", jsonObject.getString("ID_CategoryFirst"));
                            intent.putExtra("FirstCategoryName", jsonObject.getString("FirstCategoryName"));
                            intent.putExtra("ImageName",IMAGEURL + jsonObject.getString("ImgePath"));
                            intent.putExtra("ID_SalesOrder", order_id);
                            intent.putExtra("order_id", order_id);
                            intent.putExtra("deliveryDate", deliveryDate);
                            intent.putExtra("Id_order", Id_order);
                            intent.putExtra("orderDate", orderDate);
                            intent.putExtra("status", status);
                            intent.putExtra("ID_Store", ID_Store);
                            intent.putExtra("ShopType", ShopType);
                            intent.putExtra("itemcount", itemcount);
                            intent.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                            intent.putExtra("OrderType", OrderType);
                            intent.putExtra("storeName", storeName);
                            intent.putExtra("DeliveryCharge", DeliveryCharge);

////                            intent.putExtra("ID_SalesOrder", order_id);
//                            intent.putExtra("order_id", order_id);
//                            intent.putExtra("Id_order", Id_order);
//                            intent.putExtra("orderDate", orderDate);
//                            intent.putExtra("deliveryDate", deliveryDate);
//                            intent.putExtra("status", status);
//                            intent.putExtra("ShopType", ShopType);
//                            intent.putExtra("ID_Store", ID_Store);
//                            intent.putExtra("itemcount", itemcount);
//                            intent.putExtra("storeName",storeName);
                            context.startActivity(intent);
                            ((Activity)context).finish();


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
        TextView tvPrdName;
        ImageView imCat;
        public MainViewHolder(View v) {
            super(v);
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            imCat=(ImageView) v.findViewById(R.id.imCat);
        }
    }

}
