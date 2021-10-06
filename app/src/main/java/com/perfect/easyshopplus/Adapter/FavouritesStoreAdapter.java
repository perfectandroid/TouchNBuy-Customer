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
import android.widget.Toast;

import com.perfect.easyshopplus.Activity.FavouriteActivity;
import com.perfect.easyshopplus.Activity.FavouriteStoreActivity;
import com.perfect.easyshopplus.Activity.OutShopActivity;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FavouritesStoreAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    DBHandler db;
    CartChangedListener cartChangedListener;

    public FavouritesStoreAdapter(Context context, JSONArray jsonArray) {
        this.context=context;
        this.jsonArray=jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.favourites_store_list_adapter, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {
            jsonObject=jsonArray.getJSONObject(position);
            if (holder instanceof MainViewHolder) {
                String ImageName=jsonObject.getString("ImageName");
//                Glide.with(context)
//                        .load(ImageName)
//                        .placeholder(R.drawable.store)
//                        .error(R.drawable.store)
//                        .into(((MainViewHolder) holder).iv_itemimage);

                PicassoTrustAll.getInstance(context).load(ImageName).fit().error(R.drawable.store).into(((MainViewHolder) holder).iv_itemimage);
                db = new DBHandler(context);

                ((MainViewHolder)holder).tvPrdName.setText(jsonObject.getString("ItemName"));


                ((MainViewHolder)holder).lnLayout.setTag(position);
                ((MainViewHolder)holder).lnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            final SharedPreferences ID_Store = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                            if(ID_Store.getString("ID_Store", null) == null ||
                                    ID_Store.getString("ID_Store", null).equals("") ||
                                    ID_Store.getString( "ID_Store", null).equals(jsonObject.getString("ID_Items")))  {
                                Intent intent = new Intent(context, OutShopActivity.class);
                                intent.putExtra("from", "favstore");
                                context.startActivity(intent);
                                ((Activity)context).finish();
                                SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                ID_Storeeditor.putString("ID_Store", jsonObject.getString("ID_Items"));
                                ID_Storeeditor.commit();
                                SharedPreferences StoreName = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                StoreNameeditor.putString("StoreName", jsonObject.getString("ItemName"));
                                StoreNameeditor.commit();
                            }
                            else {
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                                LayoutInflater inflater1 = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View layout = inflater1.inflate(R.layout.warning_popup, null);
                                LinearLayout ll_cancel = (LinearLayout) layout.findViewById(R.id.ll_cancel);
                                LinearLayout ll_ok = (LinearLayout) layout.findViewById(R.id.ll_ok);
                                builder.setView(layout);
                                final android.app.AlertDialog alertDialog = builder.create();
                                ll_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog.dismiss();
                                    }
                                });
                                ll_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        DBHandler db = new DBHandler(context);
                                        db.deleteallCart();
                                        db.deleteallFav();
                                        Intent intent = new Intent(context, OutShopActivity.class);
                                        intent.putExtra("from", "favstore");
                                        context.startActivity(intent);
                                        ((Activity)context).finish();
                                        SharedPreferences.Editor ID_Storeeditor = ID_Store.edit();
                                        try {
                                            ID_Storeeditor.putString("ID_Store", jsonObject.getString("ID_Items"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        ID_Storeeditor.commit();
                                        SharedPreferences StoreName = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
                                        SharedPreferences.Editor StoreNameeditor = StoreName.edit();
                                        try {
                                            StoreNameeditor.putString("StoreName", jsonObject.getString("ItemName"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        StoreNameeditor.commit();
                                        alertDialog.dismiss();
                                    }
                                });
                                alertDialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            /*    ((MainViewHolder)holder).lnLayout.setTag(position);
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
                            i.putExtra("ImageName",jsonObject.getString("ImageName"));
                            context.startActivity(i);
                            ((Activity)context).finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });*/

                ((MainViewHolder)holder).ivdelete.setTag(position);
                ((MainViewHolder)holder).ivdelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences Storeremovedfromthefavourites = context.getSharedPreferences(Config.SHARED_PREF279, 0);
                        try {
                            jsonObject=jsonArray.getJSONObject(position);
                        db = new DBHandler(context);
                        db.deleteFavStorePrdouct(jsonObject.getString("ID_Items"));
                        Toast.makeText(context,""+Storeremovedfromthefavourites.getString("Storeremovedfromthefavourites",null),Toast.LENGTH_LONG).show();
                            Intent i = new Intent(context, FavouriteStoreActivity.class);
                            context.startActivity(i);
                            ((FavouriteStoreActivity)context).finish();
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
        ImageView ivdelete, iv_itemimage;
        public MainViewHolder(View v) {
            super(v);
            cartChangedListener = FavouriteActivity.cartChangedListener;
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            ivdelete=(ImageView)v.findViewById(R.id.ivdelete);
            iv_itemimage=(ImageView)v.findViewById(R.id.iv_itemimage);
        }
    }

}
