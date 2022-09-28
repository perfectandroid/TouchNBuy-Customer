package com.perfect.easyshopplus.Adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;

public class NavMenuAdapter extends ArrayAdapter<String> {

    String TAG = "NavMenuAdapter";
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
        Log.e("TAG","388    NAVIGATION");
        SharedPreferences pref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        SharedPreferences notifications = context.getSharedPreferences(Config.SHARED_PREF190, 0);
        Log.e("TAG","388122    NAVIGATION     "+notifications.getString("notifications", ""));

        Log.e("TAG","3881    NAVIGATION     "+pref.getString("notifications", ""));
        Log.e("TAG","388122    NAVIGATION     "+notifications.getString("notifications", ""));

        String notific = notifications.getString("notifications", "");

        if (notific.equals(txtTitle.getText().toString())){
            tvnotificationCount.setVisibility(View.VISIBLE);

            String strNotiCount = pref.getString("notificationcount", null);
            Log.e(TAG,"IF   5001   "+txtTitle.getText().toString()+"   :   "+strNotiCount);

            if (strNotiCount != null && !strNotiCount.isEmpty() && !strNotiCount.equals("")){
                Log.e(TAG,"IF IF  5001   "+txtTitle.getText().toString()+"   :   "+strNotiCount);
               // Integer intNotiCount = Integer.parseInt(strNotiCount);
                tvnotificationCount.setText(strNotiCount);
            }else {
                Log.e(TAG,"IF ELSE  5001   "+txtTitle.getText().toString()+"   :   "+strNotiCount);
                tvnotificationCount.setText("0");
                SharedPreferences notificationcount = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
                SharedPreferences.Editor notificationcounteditor = notificationcount.edit();
                notificationcounteditor.putString("notificationcount", "0");
                notificationcounteditor.commit();
            }

        }else{
            Log.e(TAG,"ELSE   5002   "+txtTitle.getText().toString());
            tvnotificationCount.setVisibility(View.GONE);

        }

//        if(pref.getString("notificationcount", null) == null ){
//            tvnotificationCount.setVisibility(View.GONE);
//            SharedPreferences notificationcount = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
//            SharedPreferences.Editor notificationcounteditor = notificationcount.edit();
//            notificationcounteditor.putString("notificationcount", "0");
//            notificationcounteditor.commit();
//        }
//        else if(pref.getString("notificationcount", null) != null && !pref.getString("notificationcount", null).isEmpty()&&Integer.parseInt(pref.getString("notificationcount", null))>0) {
//            for (int i = 0; i <= position; i++) {
//                if (txtTitle.getText().toString().equals("Notifications")) {
//                    Log.e("TAG","3882    NAVIGATION     "+pref.getString("notificationcount", null)+"  :  "+txtTitle.getText().toString());
//                    tvnotificationCount.setVisibility(View.VISIBLE);
//                    tvnotificationCount.setText(pref.getString("notificationcount", null));
//                } else {
//                    Log.e("TAG","3883    NAVIGATION     "+pref.getString("notificationcount", null)+"  :  "+txtTitle.getText().toString());
//                    tvnotificationCount.setVisibility(View.GONE);
//                }
//            }
//        }

        return rowView;
    }

}