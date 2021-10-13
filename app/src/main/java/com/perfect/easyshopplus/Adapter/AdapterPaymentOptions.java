package com.perfect.easyshopplus.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.ItemClickListener;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AdapterPaymentOptions extends RecyclerView.Adapter{
    String TAG="AdapterPaymentOptions";
    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    private ItemClickListener clickListener;
    int pos =-1;

    public AdapterPaymentOptions(Context context, JSONArray jsonArray) {
        this.context=context;
        this.jsonArray=jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_paymentoption, parent, false);
        vh = new AdapterPaymentOptions.MainViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        try {

            jsonObject=jsonArray.getJSONObject(position);
            ((MainViewHolder)holder).tv_payname.setText(jsonObject.getString("PaymentName"));
            SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
            String IMAGEURL = imgpref.getString("ImageURL", null);
            Log.e(TAG,"IMAGEURL  56   "+IMAGEURL+jsonObject.getString("ImagePath"));
            if (pos == position){
                ((MainViewHolder)holder).chk_payment.setChecked(true);
            }else {
                ((MainViewHolder)holder).chk_payment.setChecked(false);
            }
            PicassoTrustAll.getInstance(context).load(IMAGEURL+jsonObject.getString("ImagePath")).into(((MainViewHolder)holder).img_payments);
            ((MainViewHolder)holder).chk_payment.setTag(position);
            ((MainViewHolder)holder).chk_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        clickListener.onClick(position,jsonObject.getString("PaymentName"));
                        pos = position;
                        notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        }catch (Exception e){

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
        TextView tv_payname;
        CheckBox chk_payment;
        ImageView img_payments;
        public MainViewHolder(View v) {
            super(v);
            tv_payname=(TextView)v.findViewById(R.id.tv_payname);
            chk_payment=(CheckBox) v.findViewById(R.id.chk_payment);
            img_payments=(ImageView) v.findViewById(R.id.img_payments);
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
