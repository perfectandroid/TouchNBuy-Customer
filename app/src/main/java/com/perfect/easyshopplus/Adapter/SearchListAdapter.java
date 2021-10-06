package com.perfect.easyshopplus.Adapter;

import android.content.Context;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.perfect.easyshopplus.Model.SearchModel;
import com.perfect.easyshopplus.R;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class SearchListAdapter extends BaseAdapter {

	Context mContext;
	LayoutInflater inflater;
	private ArrayList<SearchModel> arraylist;

	public SearchListAdapter(Context context, ArrayList<SearchModel> arraylist) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
		this.arraylist = arraylist;
	}

	public class ViewHolder {
		TextView tvPrdName;
	}

	@Override
	public int getCount() {
		return arraylist.size();
	}

	@Override
	public SearchModel getItem(int position) {
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
			view = inflater.inflate(R.layout.searchlist, null);
			holder.tvPrdName = (TextView) view.findViewById(R.id.tvPrdName);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		String string = "\u20B9";
		byte[] utf8 = new byte[0];
		try {
			utf8 = string.getBytes("UTF-8");
			string = new String(utf8, "UTF-8");
		} catch (UnsupportedEncodingException e) {e.printStackTrace();}

        DecimalFormat f = new DecimalFormat("##.00");
		String strProduct = getColoredSpanned(arraylist.get(position).getItemName(), "#000080");
		String strPrice = getColoredSpanned("["+/*string+*/f.format(Double.parseDouble(arraylist.get(position).getSalesPrice()))+"/-","#800000");
		String strMRP = getColoredSpanned(/*string+*/f.format(Double.parseDouble(arraylist.get(position).getMRP()))+"/-" ,"#757575");

		String strEnd = getColoredSpanned(" ]" ,"#800000");
		holder.tvPrdName.setText(Html.fromHtml(strProduct+" "+strPrice+" "+getStrikeSpanned(strMRP) + strEnd));
		return view;
	}

	private String getStrikeSpanned(String text) {
		String input = "<strike>"+ text +"</strike>";
		return input;
	}
	private String getColoredSpanned(String text, String color) {
		String input = "<font color=" + color + ">" + text + "</font>";
		return input;
	}

}