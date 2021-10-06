package com.perfect.easyshopplus.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.perfect.easyshopplus.Activity.HomeActivity;
import com.perfect.easyshopplus.Activity.StoreActivity;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeCategoryListInfoAdapter extends RecyclerView.Adapter {

    String TAG = "CategoryListInfoAdapter";
    JSONArray jsonArray;
    JSONObject jsonObject = null;
    Context context;
    String categoryname;

    public HomeCategoryListInfoAdapter(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_home_category_list_info, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {

            jsonObject = jsonArray.getJSONObject(position);
            if (holder instanceof MainViewHolder) {
                categoryname = jsonObject.getString("CatName");
                JSONArray CategoryItemList = jsonObject.getJSONArray("CategoryItemList");
                ((MainViewHolder) holder).txt_CategoryName.setText(categoryname);


                LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                ((MainViewHolder)holder).flrv_CategoryItemList.setLayoutManager(horizontalLayoutManagaer);
                HomeCategoryItemInfoAdapter adapter = new HomeCategoryItemInfoAdapter(context.getApplicationContext(), CategoryItemList);
                ((MainViewHolder)holder).flrv_CategoryItemList.setAdapter(adapter);

                ((MainViewHolder)holder).rlcat.setTag(position);
                ((MainViewHolder)holder).rlcat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);

//                            SharedPreferences ShopType1 = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF9, 0);
//                            SharedPreferences.Editor ShopTypeeditor1 = ShopType1.edit();
//                            ShopTypeeditor1.putString("ShopType", "2");
//                            ShopTypeeditor1.commit();
//
//                            SharedPreferences StoreCategorySP = context.getSharedPreferences(Config.SHARED_PREF47, 0);
//                            SharedPreferences.Editor StoreCategorySPeditor = StoreCategorySP.edit();
//                            StoreCategorySPeditor.putString("StoreCategory", jsonObject.getString("StoreCategory"));
//                            StoreCategorySPeditor.commit();
//
//
//                            Intent intent = new Intent(context, StoreActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            intent.putExtra("StoreCategory", jsonObject.getString("StoreCategory"));
//                            context.startActivity(intent);


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
        TextView txt_CategoryName;
        LinearLayout rlcat;
        RecyclerView flrv_CategoryItemList;

        public MainViewHolder(View v) {
            super(v);
            txt_CategoryName = v.findViewById(R.id.txt_CategoryName);
            flrv_CategoryItemList = v.findViewById(R.id.flrv_CategoryItemList);
            rlcat = v.findViewById(R.id.rlcat);
        }
    }
}
