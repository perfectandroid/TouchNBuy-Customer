package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.perfect.easyshopplus.Activity.ReOrderCartActivity;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public class ReOrderCartAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    DBHandler db;
    int counter= 1;
    String ID_Store, ShopType, ID_SalesOrder,ID_CustomerAddress,OrderType,DeliveryCharge,MinimumDeliveryAmount;
    CartChangedListener cartChangedListener;
    String Id_order, orderDate, deliveryDate, status, order_id, itemcount,  storeName,OrderNo;

    public ReOrderCartAdapter(Context context, JSONArray jsonArray,
                              String ID_SalesOrder, String order_id, String deliveryDate,
                              String Id_order, String orderDate,
                              String status, String ID_Store, String ShopType,
                              String itemcount,String ID_CustomerAddress,String OrderType,
                              String storeName,String DeliveryCharge,String MinimumDeliveryAmount,String OrderNo) {



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
        this.MinimumDeliveryAmount=MinimumDeliveryAmount;
        this.OrderNo= OrderNo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_inshop_cartitem_list, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            jsonObject=jsonArray.getJSONObject(position);
            DecimalFormat f = new DecimalFormat("##.00");
            if (holder instanceof MainViewHolder) {
                db = new DBHandler(context);


                if(jsonObject.getString("Packed").equals("false")){
                    ((MainViewHolder) holder).llQty.setVisibility(View.VISIBLE);
                    ((MainViewHolder) holder).lladdQty.setVisibility(View.GONE);
                }else{
                    ((MainViewHolder) holder). llQty.setVisibility(View.GONE);
                    ((MainViewHolder) holder).lladdQty.setVisibility(View.VISIBLE);
                }
                ((MainViewHolder)holder).tvQtyValueloose.setText(jsonObject.getString("Count"));

                String imagepath= /*Config.IMAGEURL + */jsonObject.getString("ImageName");
//                Glide.with(context)
//                        .load(imagepath)
//                        .placeholder(R.drawable.items)
//                        .error(R.drawable.items)
//                        .into(((MainViewHolder) holder).iv_itemimage);


                PicassoTrustAll.getInstance(context).load(imagepath).error(R.drawable.items).into(((MainViewHolder) holder).iv_itemimage);
                ((MainViewHolder)holder).tvPrdName.setText(jsonObject.getString("ItemName"));
                String string = "\u20B9";
                byte[] utf8 = new byte[0];
                try {
                    utf8 = string.getBytes("UTF-8");
                    string = new String(utf8, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ((MainViewHolder)holder).tvPrdAmount.setText(/*string+" "+*/f.format(Double.parseDouble(jsonObject.getString("SalesPrice"))));
                ((MainViewHolder)holder).product_count.setText(jsonObject.getString("Count"));
                counter=jsonObject.getInt("Count");
                double yousaved=Float.parseFloat(jsonObject.getString("MRP"))-Float.parseFloat(jsonObject.getString("SalesPrice"));
                if(yousaved<=0){
                    ((MainViewHolder)holder).tvPrdMRPAmount.setVisibility(View.GONE);
                    ((MainViewHolder)holder).tvPrdMRPAmountsaved.setVisibility(View.GONE);
                }else {
                    ((MainViewHolder)holder).tvPrdMRPAmount.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).tvPrdMRPAmountsaved.setVisibility(View.VISIBLE);
                }
                quantityEdition(((MainViewHolder)holder).tvQtyValueloose,jsonObject.getString("ID_Items"));

                SharedPreferences MRPS = context.getSharedPreferences(Config.SHARED_PREF115, 0);
                SharedPreferences yousaveds = context.getSharedPreferences(Config.SHARED_PREF117, 0);
                ((MainViewHolder)holder).tvPrdMRPAmount.setPaintFlags(((MainViewHolder)holder).tvPrdMRPAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ((MainViewHolder)holder).tvPrdMRPAmount.setText(MRPS.getString("MRP",null)+" "+/*string+" "+*/f.format(Double.parseDouble(jsonObject.getString("MRP"))));
                ((MainViewHolder)holder).tvPrdMRPAmountsaved.setText(yousaveds.getString("yousaved",null)+" "+/*string+" "+*/f.format((yousaved)));
                ((MainViewHolder)holder).ivQtyAdd.setTag(position);
                ((MainViewHolder)holder).ivQtyAdd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            counter = Integer.valueOf(((MainViewHolder)holder).product_count.getText().toString()) + 1;
                            ((MainViewHolder)holder).product_count.setText("" + counter);
                            db.updateReorderCart(Integer.valueOf(jsonObject.getString("ID_Items")), ((MainViewHolder)holder).product_count.getText().toString());
                            cartChangedListener.onCartChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                                db.updateReorderCart(Integer.valueOf(jsonObject.getString("ID_Items")), ((MainViewHolder)holder).product_count.getText().toString());
                            }cartChangedListener.onCartChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                ((MainViewHolder)holder).ivQtyAddloose.setTag(position);
                ((MainViewHolder)holder).ivQtyAddloose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);

                            if(Integer.valueOf(((MainViewHolder)holder).tvQtyValueloose.getText().toString())>=2) {
                                counter = Integer.valueOf(((MainViewHolder)holder).tvQtyValueloose.getText().toString());
                                ((MainViewHolder)holder).tvQtyValueloose.setText("" + counter);
                                db.updateReorderCart(jsonObject.getInt("ID_Items"),((MainViewHolder)holder).tvQtyValueloose.getText().toString());
                            }cartChangedListener.onCartChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                ((MainViewHolder)holder).ivdelete.setTag(position);
                ((MainViewHolder)holder).ivdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            db = new DBHandler(context);
                            db.deleteReorderItem(jsonObject.getString("ID_Items"));
                            Intent i = new Intent(context, ReOrderCartActivity.class);
//                            in.putExtra("ID_Store", ID_Store);
//                            in.putExtra("ShopType", ShopType);
//                            in.putExtra("ID_SalesOrder", ID_SalesOrder);
//                            in.putExtra("ID_CustomerAddress", ID_CustomerAddress);
//                            in.putExtra("OrderType", OrderType);
//                            in.putExtra("DeliveryCharge", DeliveryCharge);
                            i.putExtra("ID_SalesOrder", ID_SalesOrder);
                            i.putExtra("order_id", order_id);
                            i.putExtra("OrderNo",OrderNo);
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
                            i.putExtra("DeliveryCharge",  DeliveryCharge);
                            i.putExtra("MinimumDeliveryAmount",  MinimumDeliveryAmount);
                            context.startActivity(i);
                            ((Activity)context).finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });cartChangedListener.onCartChanged();


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
        LinearLayout lnLayout, lladdQty, llQty;
        TextView tvPrdName, tvPrdAmount,tvPrdMRPAmount, tvPrdMRPAmountsaved, product_count, tvoutofstock;
        ImageView ivdelete, ivQtyAdd, ivQtyMinus, iv_itemimage, ivQtyAddloose;
        EditText tvQtyValueloose;
        public MainViewHolder(View v) {
            super(v);
            cartChangedListener = ReOrderCartActivity.cartChangedListener;
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            lladdQty=(LinearLayout) v.findViewById(R.id.lladdQty);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            tvPrdAmount=(TextView)v.findViewById(R.id.tvPrdAmount);
            ivdelete=(ImageView)v.findViewById(R.id.ivdelete);
            tvPrdMRPAmount=(TextView)v.findViewById(R.id.tvPrdMRPAmount);
            tvPrdMRPAmountsaved=(TextView)v.findViewById(R.id.tvPrdMRPAmountsaved);
            product_count = (TextView) v.findViewById(R.id.tvQtyValue);
            tvoutofstock = (TextView) v.findViewById(R.id.tvoutofstock);
            ivQtyAdd =(ImageView)v.findViewById(R.id.ivQtyAdd);
            ivQtyMinus =(ImageView)v.findViewById(R.id.ivQtyMinus);
            iv_itemimage =(ImageView)v.findViewById(R.id.iv_itemimage);


            llQty =v.findViewById(R.id.llQty);
            ivQtyAddloose =v.findViewById(R.id.ivQtyAddloose);
            tvQtyValueloose =v.findViewById(R.id.tvQtyValueloose);
        }
    }





    /*Place comma seperator on edit text and display amount in words on a text view*/
    public void quantityEdition(EditText editText, String ID_Items) {

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

                SharedPreferences Itemquantityupdatedtothecart = context.getSharedPreferences(Config.SHARED_PREF335, 0);
                SharedPreferences Pleaseupdatequantitytothecart = context.getSharedPreferences(Config.SHARED_PREF336, 0);
                if(s.length() != 0) {
                    if (s.toString().equals("00")||s.toString().equals("000")||s.toString().equals("0000")||s.toString().equals("00000")||s.toString().equals("000000")){
                        editText.setText("0");
                        db.updateReorderCart(Integer.valueOf(ID_Items), editText.getText().toString());
                        Toast.makeText(context, Itemquantityupdatedtothecart.getString("Itemquantityupdatedtothecart", null), Toast.LENGTH_SHORT).show();
                        cartChangedListener.onCartChanged();
                    }
                    else{
                        db.updateReorderCart(Integer.valueOf(ID_Items), editText.getText().toString());
                        Toast.makeText(context, Itemquantityupdatedtothecart.getString("Itemquantityupdatedtothecart", null), Toast.LENGTH_SHORT).show();
                        cartChangedListener.onCartChanged();
                    }

                }
                else {
                    db.updateReorderCart(Integer.valueOf(ID_Items), "0");
                    Toast.makeText(context, Pleaseupdatequantitytothecart.getString("Pleaseupdatequantitytothecart", null), Toast.LENGTH_SHORT).show();
                }
                cartChangedListener.onCartChanged();

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }
        });
    }

}

/*
package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perfect.easyshopplus.Activity.ReOrderCartActivity;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public class ReOrderCartAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    DBHandler db;
    int counter= 1;
    String ID_Store, ShopType, ID_SalesOrder;
    CartChangedListener cartChangedListener;

    public ReOrderCartAdapter(Context context, JSONArray jsonArray,String ID_Store, String ShopType, String ID_SalesOrder) {
        this.context=context;
        this.jsonArray=jsonArray;
        this.ID_Store=ID_Store;
        this.ShopType=ShopType;
        this.ID_SalesOrder=ID_SalesOrder;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_inshop_cartitem_list, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            jsonObject=jsonArray.getJSONObject(position);
            DecimalFormat f = new DecimalFormat("##.00");
            if (holder instanceof MainViewHolder) {
                db = new DBHandler(context);

                String imagepath= */
/*Config.IMAGEURL + *//*
jsonObject.getString("ImageName");
//                Glide.with(context)
//                        .load(imagepath)
//                        .placeholder(R.drawable.items)
//                        .error(R.drawable.items)
//                        .into(((MainViewHolder) holder).iv_itemimage);


                PicassoTrustAll.getInstance(context).load(imagepath).error(R.drawable.items).into(((MainViewHolder) holder).iv_itemimage);
                ((MainViewHolder)holder).tvPrdName.setText(jsonObject.getString("ItemName"));
                String string = "\u20B9";
                byte[] utf8 = new byte[0];
                try {
                    utf8 = string.getBytes("UTF-8");
                    string = new String(utf8, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ((MainViewHolder)holder).tvPrdAmount.setText(string+" "+f.format(Double.parseDouble(jsonObject.getString("SalesPrice"))));
                ((MainViewHolder)holder).product_count.setText(jsonObject.getString("Count"));
                counter=jsonObject.getInt("Count");
                double yousaved=Float.parseFloat(jsonObject.getString("MRP"))-Float.parseFloat(jsonObject.getString("SalesPrice"));
                if(yousaved<=0){
                    ((MainViewHolder)holder).tvPrdMRPAmount.setVisibility(View.GONE);
                    ((MainViewHolder)holder).tvPrdMRPAmountsaved.setVisibility(View.GONE);
                }else {
                    ((MainViewHolder)holder).tvPrdMRPAmount.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).tvPrdMRPAmountsaved.setVisibility(View.VISIBLE);
                }
                ((MainViewHolder)holder).tvPrdMRPAmount.setPaintFlags(((MainViewHolder)holder).tvPrdMRPAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ((MainViewHolder)holder).tvPrdMRPAmount.setText("MRP "+string+" "+f.format(Double.parseDouble(jsonObject.getString("MRP"))));
                ((MainViewHolder)holder).tvPrdMRPAmountsaved.setText("You Saved "+string+" "+f.format((yousaved)));
                ((MainViewHolder)holder).ivQtyAdd.setTag(position);
                ((MainViewHolder)holder).ivQtyAdd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                                counter = Integer.valueOf(((MainViewHolder)holder).product_count.getText().toString()) + 1;
                                ((MainViewHolder)holder).product_count.setText("" + counter);
                                 db.updateReorderCart(Integer.valueOf(jsonObject.getString("ID_Items")), ((MainViewHolder)holder).product_count.getText().toString());
                            cartChangedListener.onCartChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                                db.updateReorderCart(Integer.valueOf(jsonObject.getString("ID_Items")), ((MainViewHolder)holder).product_count.getText().toString());
                            }cartChangedListener.onCartChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                ((MainViewHolder)holder).ivdelete.setTag(position);
                ((MainViewHolder)holder).ivdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                        jsonObject = jsonArray.getJSONObject(position);
                        db = new DBHandler(context);
                        db.deleteReorderItem(jsonObject.getString("ID_Items"));
                            Intent in = new Intent(context, ReOrderCartActivity.class);
                            in.putExtra("ID_Store", ID_Store);
                            in.putExtra("ShopType", ShopType);
                            in.putExtra("ID_SalesOrder", ID_SalesOrder);
                            context.startActivity(in);
                            ((Activity)context).finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });cartChangedListener.onCartChanged();
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
        LinearLayout lnLayout, lladdQty;
        TextView tvPrdName, tvPrdAmount,tvPrdMRPAmount, tvPrdMRPAmountsaved, product_count, tvoutofstock;
        ImageView ivdelete, ivQtyAdd, ivQtyMinus, iv_itemimage;
        public MainViewHolder(View v) {
            super(v);
            cartChangedListener = ReOrderCartActivity.cartChangedListener;
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            lladdQty=(LinearLayout) v.findViewById(R.id.lladdQty);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            tvPrdAmount=(TextView)v.findViewById(R.id.tvPrdAmount);
            ivdelete=(ImageView)v.findViewById(R.id.ivdelete);
            tvPrdMRPAmount=(TextView)v.findViewById(R.id.tvPrdMRPAmount);
            tvPrdMRPAmountsaved=(TextView)v.findViewById(R.id.tvPrdMRPAmountsaved);
            product_count = (TextView) v.findViewById(R.id.tvQtyValue);
            tvoutofstock = (TextView) v.findViewById(R.id.tvoutofstock);
            ivQtyAdd =(ImageView)v.findViewById(R.id.ivQtyAdd);
            ivQtyMinus =(ImageView)v.findViewById(R.id.ivQtyMinus);
            iv_itemimage =(ImageView)v.findViewById(R.id.iv_itemimage);
        }
    }

}
*/
