package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
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

import com.perfect.easyshopplus.Activity.ItemListingActivity;
import com.perfect.easyshopplus.Activity.SubcategoryActivity;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CategoryAdapter extends RecyclerView.Adapter {

    String TAG="CategoryAdapter";
    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;

    public CategoryAdapter(Context context, JSONArray jsonArray) {
        this.context=context;
        this.jsonArray=jsonArray;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_cat_grid, parent, false);
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
            jsonObject=jsonArray.getJSONObject(position);
            if (holder instanceof MainViewHolder) {
                String PrdctName=jsonObject.getString("FirstCategoryName");
                ((MainViewHolder)holder).tvPrdName.setText(PrdctName);

                String imagepath=IMAGEURL + jsonObject.getString("ImgePath");

//                Glide.with(context)
//                        .load(imagepath)
//                        .placeholder(R.drawable.items)
//                        .error(R.drawable.items)
//                        .into(((MainViewHolder) holder).imCat);

                PicassoTrustAll.getInstance(context).load(imagepath).error(R.drawable.items).into(((MainViewHolder) holder).imCat);
                ((MainViewHolder)holder).lnLayout.setTag(position);
                ((MainViewHolder)holder).lnLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                        public void onClick(View v) {
                        try {

                            SharedPreferences pref1 = context.getSharedPreferences(Config.SHARED_PREF52, 0);
                            String SubCategoryList = pref1.getString("SubCategoryList", null);
                            if (SubCategoryList.equals("true")){
                                jsonObject = jsonArray.getJSONObject(position);
                                Intent intent = new Intent(context, SubcategoryActivity.class);
                                intent.putExtra("ID_CategoryFirst", jsonObject.getString("ID_CategoryFirst"));
                                intent.putExtra("FirstCategoryName", jsonObject.getString("FirstCategoryName"));
                                intent.putExtra("From", "HomeCat");
                                context.startActivity(intent);
                                ((Activity)context).finish();

                            }else if (SubCategoryList.equals("false")){
                                jsonObject = jsonArray.getJSONObject(position);
                                Intent intent = new Intent(context, ItemListingActivity.class);
                                intent.putExtra("ID_CategoryFirst", jsonObject.getString("ID_CategoryFirst"));
                                intent.putExtra("FirstCategoryName", jsonObject.getString("FirstCategoryName"));
                                intent.putExtra("ID_CategorySecond", "0");
                                intent.putExtra("SecondCategoryName", "");
                                intent.putExtra("From", "HomeCatItem");
                                context.startActivity(intent);
                                ((Activity)context).finish();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
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
        TextView tvPrdName;
        ImageView imCat;
        public MainViewHolder(View v) {
            super(v);
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            imCat=(ImageView)v.findViewById(R.id.imCat);
        }
    }

}
