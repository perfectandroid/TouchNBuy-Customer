package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;

public class NavMenuAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;

    public NavMenuAdapter(Activity context, String[] web, Integer[] imageId){
        super(context, R.layout.navmenuitems, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.navmenuitems, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        TextView tvnotificationCount = (TextView) rowView.findViewById(R.id.tvnotificationCount);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);
        imageView.setImageResource(imageId[position]);
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        if(pref.getString("notificationcount", null) == null ){
            tvnotificationCount.setVisibility(View.GONE);
            SharedPreferences notificationcount = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
            SharedPreferences.Editor notificationcounteditor = notificationcount.edit();
            notificationcounteditor.putString("notificationcount", "0");
            notificationcounteditor.commit();
        }
        else if(pref.getString("notificationcount", null) != null && !pref.getString("notificationcount", null).isEmpty()&&Integer.parseInt(pref.getString("notificationcount", null))>0) {
            for (int i = 0; i <= position; i++) {
                if (txtTitle.getText().toString().equals("Notifications")) {
                    tvnotificationCount.setVisibility(View.VISIBLE);
                    tvnotificationCount.setText(pref.getString("notificationcount", null));
                } else {
                    tvnotificationCount.setVisibility(View.GONE);
                }
            }
        }
        return rowView;
    }

}