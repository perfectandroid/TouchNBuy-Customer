package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perfect.easyshopplus.Activity.OrderItemListingActivity;
import com.perfect.easyshopplus.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderSubCategoryAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    String FirstCategoryName, ID_CategoryFirst;
    String  Id_order, orderDate, deliveryDate, status, order_id, ShopType, ID_Store, itemcount,ID_SalesOrder;

    public OrderSubCategoryAdapter(Context context, JSONArray jsonArray, String FirstCategoryName, String ID_CategoryFirst,
                                   String Id_order, String orderDate, String deliveryDate, String status, String order_id, String ShopType, String ID_Store, String itemcount,String ID_SalesOrder) {
        this.context=context;
        this.jsonArray=jsonArray;
        this.FirstCategoryName=FirstCategoryName;
        this.ID_CategoryFirst=ID_CategoryFirst;
        this.ID_Store=ID_Store;
        this.ShopType=ShopType;
        this.ID_SalesOrder=ID_SalesOrder;
        this.Id_order=Id_order;
        this.orderDate=orderDate;
        this.deliveryDate=deliveryDate;
        this.status=status;
        this.order_id=order_id;
        this.itemcount=itemcount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_cat_list, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            jsonObject=jsonArray.getJSONObject(position);
            if (holder instanceof MainViewHolder) {
                String PrdctName=jsonObject.getString("SecondCategoryName");
                ((MainViewHolder)holder).tvPrdName.setText(PrdctName);
                ((MainViewHolder)holder).lnLayout.setTag(position);
                ((MainViewHolder)holder).lnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                        jsonObject = jsonArray.getJSONObject(position);
                            Intent intent = new Intent(context, OrderItemListingActivity.class);
                            intent.putExtra("ID_CategorySecond", jsonObject.getString("ID_CategorySecond"));
                            intent.putExtra("SecondCategoryName", jsonObject.getString("SecondCategoryName"));
                            intent.putExtra("ID_CategoryFirst", ID_CategoryFirst);
                            intent.putExtra("FirstCategoryName", FirstCategoryName);
                            intent.putExtra("ID_SalesOrder", order_id);
                            intent.putExtra("order_id", order_id);
                            intent.putExtra("Id_order", Id_order);
                            intent.putExtra("orderDate", orderDate);
                            intent.putExtra("deliveryDate", deliveryDate);
                            intent.putExtra("status", status);
                            intent.putExtra("ShopType", ShopType);
                            intent.putExtra("ID_Store", ID_Store);
                            intent.putExtra("itemcount", itemcount);
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
        public MainViewHolder(View v) {
            super(v);
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
        }
    }

}
