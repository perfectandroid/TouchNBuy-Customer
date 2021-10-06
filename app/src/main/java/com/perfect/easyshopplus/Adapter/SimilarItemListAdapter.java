package com.perfect.easyshopplus.Adapter;

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


import com.perfect.easyshopplus.Activity.ItemDetailsActivity;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.CartModel;
import com.perfect.easyshopplus.Model.CheckCartModel;
import com.perfect.easyshopplus.Model.FavModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public class SimilarItemListAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    String PrdctName,MRP,SalesPrice,Id_Item,ID_Stock,CurrentStock,ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst;
    DBHandler db;
    boolean isCart;
    boolean isFavorite;
    CartChangedListener cartChangedListener;

    public SimilarItemListAdapter(Context context, JSONArray jsonArray) {
        this.context=context;
        this.jsonArray=jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_similar_item_list, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            jsonObject=jsonArray.getJSONObject(position);
            DecimalFormat f = new DecimalFormat("##.00");
            if (holder instanceof MainViewHolder) {
                PrdctName=jsonObject.getString("ItemName");
                MRP=jsonObject.getString("MRP");
                SalesPrice=jsonObject.getString("SalesPrice");
                Id_Item=jsonObject.getString("ID_Items");
                ID_Stock=jsonObject.getString("ID_Stock");
                CurrentStock=jsonObject.getString("CurrentStock");
                if(jsonObject.getInt("CurrentStock")==0){
                    ((MainViewHolder)holder).txtStock.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).ivCart.setVisibility(View.INVISIBLE);
                }else{
                    ((MainViewHolder)holder).txtStock.setVisibility(View.GONE);
                    ((MainViewHolder)holder).ivCart.setVisibility(View.VISIBLE);

                }

                SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
                SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
                String BASEURL = baseurlpref.getString("BaseURL", null);
                final String IMAGEURL = imgpref.getString("ImageURL", null);
                String imagepath=IMAGEURL + jsonObject.getString("ImageName");
//                Glide.with(context)
//                        .load(imagepath)
//                        .placeholder(R.drawable.items)
//                        .error(R.drawable.items)
//                        .into(((MainViewHolder) holder).iv_itemimage);

                PicassoTrustAll.getInstance(context).load(imagepath).error(R.drawable.items).into(((MainViewHolder) holder).iv_itemimage);
               /* String ImageName=""*//*jsonObject.getString("ImageName")*//*;
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
                SharedPreferences MRPS = context.getSharedPreferences(Config.SHARED_PREF115, 0);
                SharedPreferences yousaveds = context.getSharedPreferences(Config.SHARED_PREF117, 0);
                SharedPreferences Outofstock = context.getSharedPreferences(Config.SHARED_PREF116, 0);

                ((MainViewHolder)holder).tvPrdMRPAmount.setText(""+MRPS.getString("MRP",null)+" "+/*string+" "+*/f.format(Double.parseDouble(MRP)));
                ((MainViewHolder)holder).tvPrdMRPAmountsaved.setText(""+yousaveds.getString("yousaved",null)+" "/*+string+" "*/+f.format(yousaved));
                ((MainViewHolder)holder).txtStock.setText(""+Outofstock.getString("Outofstock",null));

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
                            i.putExtra("ID_Stock", jsonObject.getString("ID_Stock"));
                            i.putExtra("CurrentStock", jsonObject.getString("CurrentStock"));
                            i.putExtra("RetailPrice",  jsonObject.getString("RetailPrice"));
                            i.putExtra("PrivilagePrice",  jsonObject.getString("PrivilagePrice"));
                            i.putExtra("WholesalePrice",  jsonObject.getString("WholesalePrice"));
                            i.putExtra("GST",  jsonObject.getString("GST"));
                            i.putExtra("CESS",  jsonObject.getString("CESS"));
                            i.putExtra("from", "similarproducts");
                            i.putExtra("ID_CategorySecond", ID_CategorySecond);
                            i.putExtra("SecondCategoryName", SecondCategoryName);
                            i.putExtra("FirstCategoryName", FirstCategoryName);
                            i.putExtra("ID_CategoryFirst", ID_CategoryFirst);
                            i.putExtra("Description", jsonObject.getString("Description"));
                            i.putExtra("ImageName", IMAGEURL + jsonObject.getString("ImageName"));
                            i.putExtra("ItemMalayalamName", jsonObject.getString("ItemMalayalamName"));
                            i.putExtra("Packed",  jsonObject.getString("Packed"));
                            context.startActivity(i);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                                        jsonObject.getString("Packed"), jsonObject.getString("Description"),
                                        IMAGEURL + jsonObject.getString("ImageName"),jsonObject.getString("ItemMalayalamName")));
                                ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_fill);
                                Toast.makeText(context, "Item added to the favourites", Toast.LENGTH_SHORT).show();
                                isFavorite = true;
                            } else {
                                db.deleteFavPrdouct(jsonObject.getString("ID_Items"));
                                ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_border);
                                Toast.makeText(context, "Item removed from the favourites", Toast.LENGTH_SHORT).show();
                                isFavorite = false;
                            }
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
                            SharedPreferences pref2 = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                        jsonObject = jsonArray.getJSONObject(position);
                        db = new DBHandler(context);
                        isCart=db.checkIcartItem(jsonObject.getString("ID_Items"));
                        if (!isCart) {
                            db.addtoCart(new CartModel(jsonObject.getString("ID_Items"), jsonObject.getString("ItemName"), jsonObject.getString("SalesPrice"),jsonObject.getString("MRP"), jsonObject.getString("ID_Stock"), jsonObject.getString("CurrentStock"),
                                     jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"), jsonObject.getString("WholesalePrice")
                                    , jsonObject.getString("GST"), jsonObject.getString("CESS"),"1", jsonObject.getString("Packed"),jsonObject.getString("Description"), IMAGEURL + jsonObject.getString("ImageName"),jsonObject.getString("ItemMalayalamName")));
                            db.addtoCartCheck(new CheckCartModel(jsonObject.getString("ID_Items"), jsonObject.getString("ID_Stock"), jsonObject.getString("CurrentStock"),pref2.getString("ID_Store", null)));
                            ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticonred);
                            Toast.makeText(context, "Item added to the cart", Toast.LENGTH_SHORT).show();
                            isCart = true;
                        } else {
                            db.deleteCartPrdouct(jsonObject.getString("ID_Items"));
                            db.deleteCheckCart(jsonObject.getString("ID_Items"));
                            ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticongrey);
                            Toast.makeText(context, "Item removed from the cart", Toast.LENGTH_SHORT).show();
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
        LinearLayout lnLayout;
        TextView tvPrdName, txtStock;
        TextView tvPrdAmount, tvPrdMRPAmountsaved, tvPrdMRPAmount;
        ImageView ivFavourates,ivCart, iv_itemimage;
        public MainViewHolder(View v) {
            super(v);
            cartChangedListener = ItemDetailsActivity.cartChangedListener;
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            tvPrdAmount=(TextView)v.findViewById(R.id.tvPrdAmount);
            tvPrdMRPAmount=(TextView)v.findViewById(R.id.tvPrdMRPAmount);
            tvPrdMRPAmountsaved=(TextView)v.findViewById(R.id.tvPrdMRPAmountsaved);
            txtStock=(TextView)v.findViewById(R.id.txtStock);
            ivFavourates=(ImageView)v.findViewById(R.id.ivFavourates);
            ivCart=(ImageView)v.findViewById(R.id.ivCart);
            iv_itemimage=(ImageView)v.findViewById(R.id.iv_itemimage);
        }
    }

}
