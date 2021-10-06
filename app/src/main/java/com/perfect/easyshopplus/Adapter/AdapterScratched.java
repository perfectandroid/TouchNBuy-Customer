package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.perfect.easyshopplus.Activity.ItemListingActivity;
import com.perfect.easyshopplus.Activity.SubcategoryActivity;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class AdapterScratched extends RecyclerView.Adapter{

    String TAG="AdapterScratched";
    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;

    public AdapterScratched(Context context, JSONArray jsonArray) {
        this.context=context;
        this.jsonArray=jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_scratched, parent, false);
        vh = new AdapterScratched.MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {

            SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
            SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
            String BASEURL = baseurlpref.getString("BaseURL", null);
            String IMAGEURL = imgpref.getString("ImageURL", null);
            jsonObject=jsonArray.getJSONObject(position);

            String CRScratch=jsonObject.getString("CRScratch");
            DecimalFormat f = new DecimalFormat("##.00");
            ((MainViewHolder)holder).tv_rewad_amnt.setText(""+f.format(Double.parseDouble(jsonObject.getString("CRAmount"))));
            SharedPreferences YourRewards = context.getSharedPreferences(Config.SHARED_PREF388, 0);
            ((MainViewHolder)holder).tv_cashback.setText(""+YourRewards.getString("YourRewards",""));
//            if (CRScratch.equals("true")){
//               // ((AdapterScratched.MainViewHolder)holder).lnLayout.setVisibility(View.GONE);
//
//
//            }else {
//                ((AdapterScratched.MainViewHolder)holder).lnLayout.setVisibility(View.VISIBLE);
//
//            }


        } catch (JSONException e) {
            e.printStackTrace();
        }/* catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
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
        TextView tv_rewad_amnt,tv_cashback;
        ImageView imCat;
        public MainViewHolder(View v) {
            super(v);
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            tv_rewad_amnt=(TextView)v.findViewById(R.id.tv_rewad_amnt);
            tv_cashback=(TextView)v.findViewById(R.id.tv_cashback);
//            imCat=(ImageView)v.findViewById(R.id.imCat);
        }
    }



}
