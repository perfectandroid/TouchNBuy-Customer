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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.perfect.easyshopplus.Activity.FavouriteActivity;
import com.perfect.easyshopplus.Activity.ItemDetailsActivity;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.CartModel;
import com.perfect.easyshopplus.Model.CheckCartModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public class FavouritesAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    String catId="";
    DBHandler db;
    boolean isCart;
    CartChangedListener cartChangedListener;

    public FavouritesAdapter(Context context, JSONArray jsonArray) {
        this.context=context;
        this.jsonArray=jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.favourites_list_adapter, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {

            SharedPreferences SMRP = context.getSharedPreferences(Config.SHARED_PREF115, 0);
            SharedPreferences Syousaved = context.getSharedPreferences(Config.SHARED_PREF117, 0);
            SharedPreferences Itemaddedtothecart = context.getSharedPreferences(Config.SHARED_PREF337, 0);
            SharedPreferences Itemremovedfromthecart = context.getSharedPreferences(Config.SHARED_PREF339, 0);
            SharedPreferences addtocart = context.getSharedPreferences(Config.SHARED_PREF121, 0);

            DecimalFormat f = new DecimalFormat("##.00");
            jsonObject=jsonArray.getJSONObject(position);
            if (holder instanceof MainViewHolder) {
                String ImageName=jsonObject.getString("ImageName");
//                Glide.with(context)
//                        .load(ImageName)
//                        .placeholder(R.drawable.items)
//                        .error(R.drawable.items)
//                        .into(((MainViewHolder) holder).iv_itemimage);

                PicassoTrustAll.getInstance(context).load(ImageName).error(R.drawable.items).into(((MainViewHolder) holder).iv_itemimage);
                db = new DBHandler(context);
                isCart=db.checkIcartItem(jsonObject.getString("ID_Items"));
                if (!isCart) {
                    ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticongrey);
                } else {
                    ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticonred);
                }
                String string = "\u20B9";
                byte[] utf8 = new byte[0];
                try {
                    utf8 = string.getBytes("UTF-8");
                    string = new String(utf8, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                ((MainViewHolder)holder).tvPrdName.setText(jsonObject.getString("ItemName"));
                ((MainViewHolder)holder).tvPrdAmount.setText(/*string+*/""+f.format(Double.parseDouble(jsonObject.getString("SalesPrice"))));
                float yousaved=Float.parseFloat(jsonObject.getString("MRP"))-Float.parseFloat(jsonObject.getString("SalesPrice"));
                if(yousaved<=0){
                    ((MainViewHolder)holder).tvPrdMRPAmount.setVisibility(View.GONE);
                    ((MainViewHolder)holder).tvPrdMRPAmountsaved.setVisibility(View.GONE);
                }else {
                    ((MainViewHolder)holder).tvPrdMRPAmount.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).tvPrdMRPAmountsaved.setVisibility(View.VISIBLE);
                }
                ((MainViewHolder)holder).tvPrdMRPAmount.setPaintFlags(((MainViewHolder)holder).tvPrdMRPAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ((MainViewHolder)holder).tvPrdMRPAmount.setText(""+SMRP.getString("MRP",null)+" "/*+string+" "*/+f.format(Double.parseDouble(jsonObject.getString("MRP"))));
                ((MainViewHolder)holder).tvPrdMRPAmountsaved.setText(""+Syousaved.getString("yousaved",null)+" "+/*string+" "+*/f.format(yousaved));
                ((MainViewHolder)holder).lnLayout.setTag(position);
                ((MainViewHolder)holder).lnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            Intent i = new Intent(context, ItemDetailsActivity.class);
                            i.putExtra("ItemName", jsonObject.getString("ItemName"));
                            i.putExtra("MRP", jsonObject.getString("MRP"));
                            i.putExtra("SalesPrice",jsonObject.getString("SalesPrice"));
                            i.putExtra("Id_Item",jsonObject.getString("ID_Items"));
                            i.putExtra("ID_Stock",  jsonObject.getString("Stock_ID"));
                            i.putExtra("CurrentStock", jsonObject.getString("QTY"));
                            i.putExtra("RetailPrice",  jsonObject.getString("RetailPrice"));
                            i.putExtra("PrivilagePrice",  jsonObject.getString("PrivilagePrice"));
                            i.putExtra("WholesalePrice",  jsonObject.getString("WholesalePrice"));
                            i.putExtra("GST",  jsonObject.getString("GST"));
                            i.putExtra("CESS",  jsonObject.getString("CESS"));
                            i.putExtra("from","favourite");
                            i.putExtra("Description",jsonObject.getString("Description"));
                            i.putExtra("Packed",jsonObject.getString("Packed"));
                            i.putExtra("ImageName",jsonObject.getString("ImageName"));
                            i.putExtra("ItemMalayalamName", jsonObject.getString("ItemMalayalamName"));

                            context.startActivity(i);
                            ((Activity)context).finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ((MainViewHolder)holder).ivdelete.setTag(position);
                ((MainViewHolder)holder).ivdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                        db = new DBHandler(context);
                        db.deleteFavPrdouct(jsonObject.getString("ID_Items"));
                            SharedPreferences Itemremovedfromthefavourites = context.getSharedPreferences(Config.SHARED_PREF338, 0);
                        Toast.makeText(context,""+Itemremovedfromthefavourites.getString("Itemremovedfromthefavourites",null),Toast.LENGTH_LONG).show();
                            Intent i = new Intent(context, FavouriteActivity.class);
                            context.startActivity(i);
                            ((FavouriteActivity)context).finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ((MainViewHolder)holder).lldelete.setTag(position);
                ((MainViewHolder)holder).lldelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                        db = new DBHandler(context);
                        db.deleteFavPrdouct(jsonObject.getString("ID_Items"));
                            SharedPreferences Itemremovedfromthefavourites = context.getSharedPreferences(Config.SHARED_PREF338, 0);
                        Toast.makeText(context,""+Itemremovedfromthefavourites.getString("Itemremovedfromthefavourites",null),Toast.LENGTH_LONG).show();
                            Intent i = new Intent(context, FavouriteActivity.class);
                            context.startActivity(i);
                            ((FavouriteActivity)context).finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ((MainViewHolder) holder).ivCart.setTag(position);
                ((MainViewHolder) holder).ivCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            db = new DBHandler(context);
                            isCart=db.checkIcartItem(jsonObject.getString("ID_Items"));
                            if (!isCart) {
                                SharedPreferences pref2 = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                                db.addtoCart(new CartModel(jsonObject.getString("ID_Items"), jsonObject.getString("ItemName"), jsonObject.getString("SalesPrice"),jsonObject.getString("MRP"), jsonObject.getString("Stock_ID"), jsonObject.getString("QTY"),
                                        jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"), jsonObject.getString("WholesalePrice")
                                        , jsonObject.getString("GST"), jsonObject.getString("CESS"),"1", jsonObject.getString("Packed"),jsonObject.getString("Description"), jsonObject.getString("ImageName"), jsonObject.getString("ItemMalayalamName")));
                                db.addtoCartCheck(new CheckCartModel(jsonObject.getString("ID_Items"), jsonObject.getString("Stock_ID"),jsonObject.getString("QTY"),pref2.getString("ID_Store", null)));
                                ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticonred);
                                Toast.makeText(context, ""+Itemaddedtothecart.getString("Itemaddedtothecart",null), Toast.LENGTH_SHORT).show();
                                isCart = true;
                            } else {
                                db.deleteCartPrdouct(jsonObject.getString("ID_Items"));
                                db.deleteCheckCart(jsonObject.getString("ID_Items"));
                                ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticongrey);
                                Toast.makeText(context, ""+Itemremovedfromthecart.getString("Itemremovedfromthecart",null), Toast.LENGTH_SHORT).show();
                                isCart = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cartChangedListener.onCartChanged();
                    }
                });
                ((MainViewHolder) holder).llCart.setTag(position);
                ((MainViewHolder) holder).llCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            db = new DBHandler(context);
                            isCart=db.checkIcartItem(jsonObject.getString("ID_Items"));
                            if (!isCart) {
                                SharedPreferences pref2 = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                                db.addtoCart(new CartModel(jsonObject.getString("ID_Items"), jsonObject.getString("ItemName"), jsonObject.getString("SalesPrice"),jsonObject.getString("MRP"), jsonObject.getString("Stock_ID"), jsonObject.getString("QTY"),
                                        jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"), jsonObject.getString("WholesalePrice")
                                        , jsonObject.getString("GST"), jsonObject.getString("CESS"),"1", jsonObject.getString("Packed"),jsonObject.getString("Description"), jsonObject.getString("ImageName"), jsonObject.getString("ItemMalayalamName")));
                                db.addtoCartCheck(new CheckCartModel(jsonObject.getString("ID_Items"), jsonObject.getString("Stock_ID"),jsonObject.getString("QTY"),pref2.getString("ID_Store", null)));
                                ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticonred);
                                Toast.makeText(context, ""+Itemaddedtothecart.getString("Itemaddedtothecart",null), Toast.LENGTH_SHORT).show();
                                isCart = true;
                            } else {
                                db.deleteCartPrdouct(jsonObject.getString("ID_Items"));
                                db.deleteCheckCart(jsonObject.getString("ID_Items"));
                                ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticongrey);
                                Toast.makeText(context, ""+Itemremovedfromthecart.getString("Itemremovedfromthecart",null), Toast.LENGTH_SHORT).show();
                                isCart = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cartChangedListener.onCartChanged();
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
        LinearLayout lnLayout, lldelete, llCart;
        TextView tvPrdName, tvPrdAmount,tvPrdMRPAmount, tvPrdMRPAmountsaved;
        TextView tv_remove;
        ImageView ivdelete, ivCart, iv_itemimage;
        public MainViewHolder(View v) {
            super(v);
            cartChangedListener = FavouriteActivity.cartChangedListener;
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            lldelete=(LinearLayout) v.findViewById(R.id.lldelete);
            llCart=(LinearLayout) v.findViewById(R.id.llCart);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            tvPrdAmount=(TextView)v.findViewById(R.id.tvPrdAmount);
            ivdelete=(ImageView)v.findViewById(R.id.ivdelete);
            ivCart=(ImageView)v.findViewById(R.id.ivCart);
            tvPrdMRPAmount=(TextView)v.findViewById(R.id.tvPrdMRPAmount);
            tvPrdMRPAmountsaved=(TextView)v.findViewById(R.id.tvPrdMRPAmountsaved);
            iv_itemimage=(ImageView)v.findViewById(R.id.iv_itemimage);
            tv_remove = (TextView)v.findViewById(R.id.tv_remove);
        }
    }

}
