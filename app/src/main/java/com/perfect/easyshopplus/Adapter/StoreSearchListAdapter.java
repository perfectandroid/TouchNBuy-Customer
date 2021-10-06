package com.perfect.easyshopplus.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.perfect.easyshopplus.Model.StoreselectModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import java.util.ArrayList;

public class StoreSearchListAdapter extends BaseAdapter {

	Context mContext;
	LayoutInflater inflater;
	private ArrayList<StoreselectModel> arraylist;

	public StoreSearchListAdapter(Context context, ArrayList<StoreselectModel> arraylist) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = arraylist;
	}

	public class ViewHolder {
		TextView tvPrdName;
		ImageView iv_itemimage;
	}

	@Override
	public int getCount() {
		return arraylist.size();
	}

	@Override
	public StoreselectModel getItem(int position) {
		return arraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = inflater.inflate(R.layout.storesearchlist, null);
			holder.tvPrdName = (TextView) view.findViewById(R.id.tvPrdName);
			holder.iv_itemimage = (ImageView) view.findViewById(R.id.iv_itemimage);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.tvPrdName .setText(arraylist.get(position).getStoreName());

		SharedPreferences baseurlpref = mContext.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
		SharedPreferences imgpref = mContext.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
		String BASEURL = baseurlpref.getString("BaseURL", null);
		String IMAGEURL = imgpref.getString("ImageURL", null);
		String imagepath= IMAGEURL + arraylist.get(position).getImagePath();

		PicassoTrustAll.getInstance(mContext).load(imagepath).error(R.drawable.items).into(holder.iv_itemimage);
		/*Glide.with(mContext)
				.load(imagepath)
				.placeholder(R.drawable.items)
				.error(R.drawable.items)
				.into(holder.iv_itemimage);*/
		return view;
	}

}