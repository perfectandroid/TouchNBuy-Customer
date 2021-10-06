package com.perfect.easyshopplus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.perfect.easyshopplus.Activity.StoreActivity;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoryListInfoAdapter extends RecyclerView.Adapter {

    String TAG = "CategoryListInfoAdapter";
    JSONArray jsonArray;
    JSONObject jsonObject = null;
    Context context;
    String storecategory,categoryname,image,StoreDescription;
    String colorcode;

    public CategoryListInfoAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_categorylist_info, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {

            SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
            SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
            String BASEURL = baseurlpref.getString("BaseURL", null);
            String IMAGEURL = imgpref.getString("ImageURL", null);
           // Log.e(TAG,"IMAGEURL    57   "+IMAGEURL);
            jsonObject = jsonArray.getJSONObject(position);
            if (holder instanceof MainViewHolder) {
                storecategory = jsonObject.getString("StoreCategory");
                categoryname = jsonObject.getString("CategoryName");
                image = IMAGEURL +jsonObject.getString("ImageCode");
                colorcode = jsonObject.getString("ColorCode");
                StoreDescription = jsonObject.getString("StoreDescription");
               // Log.e(TAG,"IMAGEURL    64   "+image);
                ((MainViewHolder) holder).txt_CategoryName.setText(categoryname);
//                ((MainViewHolder) holder).txt_subCategoryName.setText("Shop from our curation of labels All into impress yourswelf");
                ((MainViewHolder) holder).txt_subCategoryName.setText(StoreDescription);
               // ((MainViewHolder) holder).txtv_ctgryName.setBackgroundColor(Color.parseColor(colorcode));
                PicassoTrustAll.getInstance(context).load(image).error(R.drawable.items).into(((MainViewHolder) holder).imgv_catgry);
                ((MainViewHolder)holder).rlcat.setTag(position);
                ((MainViewHolder)holder).rlcat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);

                            SharedPreferences ShopType1 = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF9, 0);
                            SharedPreferences.Editor ShopTypeeditor1 = ShopType1.edit();
                            ShopTypeeditor1.putString("ShopType", "2");
                            ShopTypeeditor1.commit();

                            SharedPreferences StoreCategorySP = context.getSharedPreferences(Config.SHARED_PREF47, 0);
                            SharedPreferences.Editor StoreCategorySPeditor = StoreCategorySP.edit();
                            StoreCategorySPeditor.putString("StoreCategory", jsonObject.getString("StoreCategory"));
                            StoreCategorySPeditor.commit();


                            Intent intent = new Intent(context, StoreActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("StoreCategory", jsonObject.getString("StoreCategory"));
                            context.startActivity(intent);


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
        TextView txt_CategoryName,txt_subCategoryName;
        ImageView imgv_catgry;
        LinearLayout rlcat;


        public MainViewHolder(View v) {
            super(v);
            txt_CategoryName = v.findViewById(R.id.txt_CategoryName);
            txt_subCategoryName = v.findViewById(R.id.txt_subCategoryName);
            imgv_catgry = v.findViewById(R.id.imgv_catgry);
            rlcat = v.findViewById(R.id.rlcat);
        }
    }
}
