package com.perfect.easyshopplus.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.perfect.easyshopplus.Activity.WelcomeActivity;
import com.perfect.easyshopplus.Model.SearchModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONObject;

public class LanguageAdapter  extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    JSONArray jsonArray;
    JSONObject jsonObject=null;
    String image;
    String TAG = "LanguageAdapter";



    public LanguageAdapter(Context mContext, JSONArray jarray) {
        mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        jsonArray = jarray;
    }
    public class ViewHolder {
        TextView tv_title;
        ImageView img_lang;
        LinearLayout ll_lang;
    }

    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final LanguageAdapter.ViewHolder holder;
        if (view == null) {
            holder = new LanguageAdapter.ViewHolder();
            view = inflater.inflate(R.layout.adapter_laguage, null);
            holder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            holder.img_lang = (ImageView) view.findViewById(R.id.img_lang);
            holder.ll_lang = (LinearLayout)view.findViewById(R.id.ll_lang);



            view.setTag(holder);
        } else {
            holder = (LanguageAdapter.ViewHolder) view.getTag();
        }

        try {
            jsonObject=jsonArray.getJSONObject(i);


            holder.tv_title.setText(""+jsonObject.getString("Languages"));
            SharedPreferences imgpref = inflater.getContext().getSharedPreferences(Config.SHARED_PREF57, 0);
            if (!jsonObject.getString("Colorcode").equals("")){
                holder.ll_lang.setBackgroundColor(Color.parseColor(""+jsonObject.getString("Colorcode")));
            }

            String IMAGEURL = imgpref.getString("ImageURL", null);
            image = IMAGEURL +jsonObject.getString("ImagePath");
            Log.e(TAG,"image   84   "+image);
            PicassoTrustAll.getInstance(inflater.getContext()).load(image).error(R.drawable.white_circle).into((holder.img_lang));

        }catch (Exception e){
            Log.e(TAG,"Exception   104   "+e.toString());
        }
        return view;
    }


}
