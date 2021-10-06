package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.perfect.easyshopplus.Activity.CartActivity;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Fragment.InShopFragment;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public class InShopCartAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    int intposition;
    DBHandler db;
    int counter= 1;
    InShopFragment inShopFragment;
    CartChangedListener cartChangedListener;


    public InShopCartAdapter(Context context, JSONArray jsonArray, InShopFragment inShopFragment) {
        this.context=context;
        this.jsonArray=jsonArray;
        this.inShopFragment=inShopFragment;
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
            SharedPreferences SMRP = context.getSharedPreferences(Config.SHARED_PREF115, 0);
            SharedPreferences Syousaved = context.getSharedPreferences(Config.SHARED_PREF117, 0);
            SharedPreferences Itemremovedfromthecart = context.getSharedPreferences(Config.SHARED_PREF339, 0);

            jsonObject=jsonArray.getJSONObject(position);
            DecimalFormat f = new DecimalFormat("##.00");
            if (holder instanceof MainViewHolder) {
                    if(jsonObject.getString("Packed").equals("false")){
                        ((MainViewHolder) holder).llQty.setVisibility(View.VISIBLE);
                        ((MainViewHolder) holder).lladdQty.setVisibility(View.GONE);
                    }else{
                        ((MainViewHolder) holder). llQty.setVisibility(View.GONE);
                        ((MainViewHolder) holder).lladdQty.setVisibility(View.VISIBLE);
                    }
                ((MainViewHolder)holder).tvQtyValueloose.setText(jsonObject.getString("Count"));
                String ImageName=jsonObject.getString("ImageName");
           /*     if(!ImageName.isEmpty()) {
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
//                Glide.with(context)
//                        .load(ImageName)
//                        .placeholder(R.drawable.items)
//                        .error(R.drawable.items)
//                        .into(((MainViewHolder) holder).iv_itemimage);

                PicassoTrustAll.getInstance(context).load(ImageName).fit().error(R.drawable.items).into(((MainViewHolder) holder).iv_itemimage);
             /*   String ImageName=""*//*jsonObject.getString("ImageName")*//*;
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
                ((MainViewHolder)holder).tvPrdMRPAmount.setPaintFlags(((MainViewHolder)holder).tvPrdMRPAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ((MainViewHolder)holder).tvPrdMRPAmount.setText(""+SMRP.getString("MRP",null)+" "+/*string+" "+*/f.format(Double.parseDouble(jsonObject.getString("MRP"))));
                ((MainViewHolder)holder).tvPrdMRPAmountsaved.setText(""+Syousaved.getString("yousaved",null)+" "/*+string+" "*/+f.format((yousaved)));
                ((MainViewHolder)holder).ivQtyAdd.setTag(position);
                ((MainViewHolder)holder).ivQtyAdd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                                counter = Integer.valueOf(((MainViewHolder)holder).product_count.getText().toString()) + 1;
                                ((MainViewHolder)holder).product_count.setText("" + counter);
                                if(db.updateInshopCart(jsonObject.getInt("ID_Items"),((MainViewHolder)holder).product_count.getText().toString())){
                                } else{
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }cartChangedListener.onCartChanged();
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
                                if(db.updateInshopCart(jsonObject.getInt("ID_Items"),((MainViewHolder)holder).product_count.getText().toString())){
                                } else{
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }cartChangedListener.onCartChanged();
                    }
                });

                ((MainViewHolder)holder).ivQtyAddloose.setTag(position);
                ((MainViewHolder)holder).ivQtyAddloose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            ((MainViewHolder)holder).tvQtyValueloose.setText(jsonObject.getString("Count"));
                            if(db.updateCart(jsonObject.getInt("ID_Items"),((MainViewHolder)holder).tvQtyValueloose.getText().toString())){ } else{ }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }cartChangedListener.onCartChanged();
                    }
                });
                ((MainViewHolder)holder).ivdelete.setTag(position);
                ((MainViewHolder)holder).ivdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                        jsonObject = jsonArray.getJSONObject(position);
                        db = new DBHandler(context);
                        db.deleteInShopCartPrdouct(jsonObject.getString("ID_Items"));
                        Toast.makeText(context,""+Itemremovedfromthecart.getString("Itemremovedfromthecart",null),Toast.LENGTH_LONG).show();
                            inShopFragment.getAllIncart();
                            Intent i = new Intent(context, CartActivity.class);
                            i.putExtra("From",  jsonObject.getString("Inshop"));
                            context.startActivity(i);
                            ((Activity)context).finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }cartChangedListener.onCartChanged();
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
        LinearLayout lnLayout, lladdQty, llQty;
        TextView tvPrdName, tvPrdAmount,tvPrdMRPAmount, tvPrdMRPAmountsaved, product_count, tvoutofstock;
        ImageView ivdelete, ivQtyAdd, ivQtyMinus, iv_itemimage, ivQtyAddloose;
        EditText tvQtyValueloose;
        public MainViewHolder(View v) {
            super(v);
            cartChangedListener = CartActivity.cartChangedListener;
            cartChangedListener = InShopFragment.cartChangedListener;
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            lladdQty=(LinearLayout) v.findViewById(R.id.lladdQty);
            llQty=(LinearLayout) v.findViewById(R.id.llQty);
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
            ivQtyAddloose =(ImageView)v.findViewById(R.id.ivQtyAddloose);
            tvQtyValueloose =(EditText) v.findViewById(R.id.tvQtyValueloose);
        }
    }

}
