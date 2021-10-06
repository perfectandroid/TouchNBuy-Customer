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

import com.perfect.easyshopplus.Activity.CartActivity;
import com.perfect.easyshopplus.Activity.ItemDetailsActivity;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Fragment.OutShopFragment;
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

public class OutShopCartAdapter extends RecyclerView.Adapter /*implements AdapterView.OnItemSelectedListener*/ {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    DBHandler db;
    boolean isFavorite;
    int counter= 1;
    OutShopFragment outShopFragment;
    CartChangedListener cartChangedListener;

    public OutShopCartAdapter(Context context, JSONArray jsonArray,OutShopFragment outShopFragment) {
        this.context=context;
        this.jsonArray=jsonArray;
        this.outShopFragment=outShopFragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_outshop_cartitem_list, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        SharedPreferences Itemaddedtothefavourites = context.getSharedPreferences(Config.SHARED_PREF332, 0);
        SharedPreferences Itemremovedfromthefavourites = context.getSharedPreferences(Config.SHARED_PREF338, 0);

        try {
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

                PicassoTrustAll.getInstance(context).load(ImageName).error(R.drawable.items).into(((MainViewHolder) holder).iv_itemimage);
                db = new DBHandler(context);
                isFavorite=db.checkIfavItem(jsonObject.getString("ID_Items"));
                if (!isFavorite) {
                    ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_border);
                } else {
                    ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_fill);
                }
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

                SharedPreferences MRPS = context.getSharedPreferences(Config.SHARED_PREF115, 0);
                SharedPreferences yousaveds = context.getSharedPreferences(Config.SHARED_PREF117, 0);
                ((MainViewHolder)holder).tvPrdMRPAmount.setText(MRPS.getString("MRP",null)+" "+/*string+" "+*/f.format(Double.parseDouble(jsonObject.getString("MRP"))));
                ((MainViewHolder)holder).tvPrdMRPAmountsaved.setText(yousaveds.getString("yousaved",null)+" "+/*string+" "+*/f.format((yousaved)));


                SharedPreferences Saveforlater = context.getSharedPreferences(Config.SHARED_PREF412, 0);
                SharedPreferences Remove = context.getSharedPreferences(Config.SHARED_PREF413, 0);

                ((MainViewHolder)holder).tv_savefor.setText(""+Saveforlater.getString("Saveforlater",null));
                ((MainViewHolder)holder).tv_remove.setText(""+Remove.getString("Remove",null));


                SharedPreferences quantity = context.getSharedPreferences(Config.SHARED_PREF122, 0);
                ((MainViewHolder)holder).tv_Quantity.setText(quantity.getString("quantity",null)+" : ");

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
                            i.putExtra("from","cart");
                            i.putExtra("Packed", jsonObject.getString("Packed"));
                            i.putExtra("Description", jsonObject.getString("Description"));
                            i.putExtra("ImageName", jsonObject.getString("ImageName"));
                            i.putExtra("ItemMalayalamName", jsonObject.getString("ItemMalayalamName"));
                            context.startActivity(i);
                            ((Activity)context).finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                            if(db.updateCart(jsonObject.getInt("ID_Items"),((MainViewHolder)holder).product_count.getText().toString())){} else{ }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }cartChangedListener.onCartChanged();
                    }
                });
                quantityEdition(((MainViewHolder)holder).tvQtyValueloose,jsonObject.getString("ID_Items"));

                ((MainViewHolder)holder).ivQtyMinus.setTag(position);
                ((MainViewHolder)holder).ivQtyMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            if(Integer.valueOf(((MainViewHolder)holder).product_count.getText().toString())>=2) {
                                counter = Integer.valueOf(((MainViewHolder)holder).product_count.getText().toString()) - 1;
                                ((MainViewHolder)holder).product_count.setText("" + counter);
                                if(db.updateCart(jsonObject.getInt("ID_Items"),((MainViewHolder)holder).product_count.getText().toString())){ } else{ }
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
                            if(db.updateCart(jsonObject.getInt("ID_Items"),((MainViewHolder)holder).tvQtyValueloose.getText().toString())){ } else{ }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }cartChangedListener.onCartChanged();
                    }
                });
                ((MainViewHolder)holder).lldelete.setTag(position);
                ((MainViewHolder)holder).lldelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            db = new DBHandler(context);
                            db.deleteCartPrdouct(jsonObject.getString("ID_Items"));
                            db.deleteCheckCart(jsonObject.getString("ID_Items"));
                            Toast.makeText(context,"Item Removed",Toast.LENGTH_LONG).show();
                            outShopFragment.getAllcart();
                            Intent i = new Intent(context, CartActivity.class);
                            i.putExtra("From",  jsonObject.getString("Inshop"));
                            context.startActivity(i);
                            ((Activity)context).finish();
                        } catch (JSONException e) {
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
                            db.deleteCartPrdouct(jsonObject.getString("ID_Items"));
                            db.deleteCheckCart(jsonObject.getString("ID_Items"));
                            Toast.makeText(context,"Item Removed",Toast.LENGTH_LONG).show();
                            outShopFragment.getAllcart();
                            Intent i = new Intent(context, CartActivity.class);
                            i.putExtra("From",  jsonObject.getString("Inshop"));
                            context.startActivity(i);
                            ((Activity)context).finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }cartChangedListener.onCartChanged();
                    }
                });
                ((MainViewHolder)holder).ivFavourates.setTag(position);
                ((MainViewHolder)holder).ivFavourates.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            db = new DBHandler(context);
                            isFavorite=db.checkIfavItem(jsonObject.getString("ID_Items"));
                            if (!isFavorite) {
                                db.addtoFav(new FavModel(jsonObject.getString("ID_Items"), jsonObject.getString("ItemName"), jsonObject.getString("SalesPrice"), jsonObject.getString("MRP"), jsonObject.getString("Stock_ID"), jsonObject.getString("QTY"),
                                        jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"), jsonObject.getString("WholesalePrice")
                                        , jsonObject.getString("GST"), jsonObject.getString("CESS"),
                                        jsonObject.getString("Packed"),jsonObject.getString("Description"),
                                        jsonObject.getString("ImageName"),jsonObject.getString("ItemMalayalamName")));
                                ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_fill);
                                Toast.makeText(context, ""+Itemaddedtothefavourites.getString("Itemaddedtothefavourites",null), Toast.LENGTH_SHORT).show();

//                                Toast.makeText(context, "Item added to the favourites", Toast.LENGTH_SHORT).show();
                                isFavorite = true;
                            } else {
                                db.deleteFavPrdouct(jsonObject.getString("ID_Items"));
                                ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_border);
//                                Toast.makeText(context, "Item removed from the favourites", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, ""+Itemremovedfromthefavourites.getString("Itemremovedfromthefavourites",null), Toast.LENGTH_SHORT).show();
                                isFavorite = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ((MainViewHolder)holder).llFavourates.setTag(position);
                ((MainViewHolder)holder).llFavourates.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            db = new DBHandler(context);
                            isFavorite=db.checkIfavItem(jsonObject.getString("ID_Items"));
                            if (!isFavorite) {
                                db.addtoFav(new FavModel(jsonObject.getString("ID_Items"), jsonObject.getString("ItemName"), jsonObject.getString("SalesPrice"), jsonObject.getString("MRP"), jsonObject.getString("Stock_ID"), jsonObject.getString("QTY"),
                                        jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"), jsonObject.getString("WholesalePrice")
                                        , jsonObject.getString("GST"), jsonObject.getString("CESS"),
                                        jsonObject.getString("Packed"),jsonObject.getString("Description"),
                                        jsonObject.getString("ImageName"),jsonObject.getString("ItemMalayalamName")));
                                ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_fill);
                                Toast.makeText(context, ""+Itemaddedtothefavourites.getString("Itemaddedtothefavourites",null), Toast.LENGTH_SHORT).show();

//                                Toast.makeText(context, "Item added to the favourites", Toast.LENGTH_SHORT).show();
                                isFavorite = true;
                            } else {
                                db.deleteFavPrdouct(jsonObject.getString("ID_Items"));
                                ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_border);
//                                Toast.makeText(context, "Item removed from the favourites", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, ""+Itemremovedfromthefavourites.getString("Itemremovedfromthefavourites",null), Toast.LENGTH_SHORT).show();
                                isFavorite = false;
                            }
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
        LinearLayout lnLayout, lladdQty, llQty, lldelete, llFavourates;
        TextView tvPrdName, tvPrdAmount,tvPrdMRPAmount, tvPrdMRPAmountsaved, product_count, tvoutofstock, tv_Quantity;
        ImageView ivdelete, ivFavourates, ivQtyAdd, ivQtyMinus, iv_itemimage, ivQtyAddloose;
        EditText tvQtyValueloose;
        TextView tv_remove,tv_savefor;
        public MainViewHolder(View v) {
            super(v);
            cartChangedListener = CartActivity.cartChangedListener;
            cartChangedListener = OutShopFragment.cartChangedListener;
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            lladdQty=(LinearLayout) v.findViewById(R.id.lladdQty);
            llQty=(LinearLayout) v.findViewById(R.id.llQty);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            tvPrdAmount=(TextView)v.findViewById(R.id.tvPrdAmount);
            ivdelete=(ImageView)v.findViewById(R.id.ivdelete);
            ivFavourates=(ImageView)v.findViewById(R.id.ivFavourates);
            tvPrdMRPAmount=(TextView)v.findViewById(R.id.tvPrdMRPAmount);
            tvPrdMRPAmountsaved=(TextView)v.findViewById(R.id.tvPrdMRPAmountsaved);
            product_count = (TextView) v.findViewById(R.id.tvQtyValue);
            tvoutofstock = (TextView) v.findViewById(R.id.tvoutofstock);
            ivQtyAdd =(ImageView)v.findViewById(R.id.ivQtyAdd);
            ivQtyMinus =(ImageView)v.findViewById(R.id.ivQtyMinus);
            iv_itemimage =(ImageView)v.findViewById(R.id.iv_itemimage);
            ivQtyAddloose =(ImageView)v.findViewById(R.id.ivQtyAddloose);
            tvQtyValueloose =(EditText) v.findViewById(R.id.tvQtyValueloose);
            tv_Quantity = v.findViewById(R.id.tv_Quantity);
            llFavourates = v.findViewById(R.id.llFavourates);
            lldelete = v.findViewById(R.id.lldelete);

            tv_savefor = v.findViewById(R.id.tv_savefor);
            tv_remove = v.findViewById(R.id.tv_remove);
        }
    }
    public void quantityEdition(EditText editText, String ID_Items) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                SharedPreferences Itemquantityupdatedtothecart = context.getSharedPreferences(Config.SHARED_PREF335, 0);
                SharedPreferences Pleaseupdatequantitytothecart = context.getSharedPreferences(Config.SHARED_PREF336, 0);
                if(s.length() != 0) {

                    if (s.toString().equals("00")||s.toString().equals("000")||s.toString().equals("0000")||s.toString().equals("00000")||s.toString().equals("000000")){
                        editText.setText("0");
                        db.updateCart(Integer.valueOf(ID_Items), editText.getText().toString());
                        //  Toast.makeText(context, Itemquantityupdatedtothecart.getString("Itemquantityupdatedtothecart", null), Toast.LENGTH_SHORT).show();
                        cartChangedListener.onCartChanged();
                    }
                    else{
                        db.updateCart(Integer.valueOf(ID_Items), editText.getText().toString());
                        //  Toast.makeText(context, Itemquantityupdatedtothecart.getString("Itemquantityupdatedtothecart", null), Toast.LENGTH_SHORT).show();
                        cartChangedListener.onCartChanged();
                    }

                }
                else {
                    db.updateCart(Integer.valueOf(ID_Items), "0");

                    Toast.makeText(context, Pleaseupdatequantitytothecart.getString("Pleaseupdatequantitytothecart", null), Toast.LENGTH_SHORT).show();
                }
                cartChangedListener.onCartChanged();
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
    }

}