package com.perfect.easyshopplus.Activity.billdesks;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.billdesk.sdk.LibraryPaymentStatusProtocol;

public class SampleCallBack implements LibraryPaymentStatusProtocol, Parcelable {
	String TAG = "Callback ::: > ";


	public SampleCallBack() {
		Log.e(TAG, "CallBack()....");
	}

	public SampleCallBack(Parcel in) {
		Log.v(TAG, "CallBack(Parcel in)....");
	}

	@Override
	public void paymentStatus(String status, Activity context) {
		Log.v(TAG,
				"paymentStatus(String status, Activity context)....::::status:::::"
						+ status);
//		Toast.makeText(context, "PG Response:: " + status, Toast.LENGTH_LONG)
//				.show();

		Log.e(TAG, "paymentStatus 32   " + status);

		try {
			Log.e(TAG, "paymentStatus 32   pass" );
			Intent mIntent = new Intent(context,StatusActivity.class);
			mIntent.putExtra("status", status);
			context.startActivity(mIntent);
			context.finish();

		}catch (Exception e){
			Log.e(TAG,"Exception  32    "+e.toString());
		}



//		Intent data = new Intent();
//		data.putExtra("status", status);
//		context.setResult(context.RESULT_OK, data);
////---close the activity---
//		context.finish();

	}

	@Override
	public void tryAgain() {
		Log.e(TAG, "tryAgain() called");
	}

	@Override
	public void onError(Exception e) {
		Log.e(TAG, "onError() called with: e = [" + e + "]");
	}

	@Override
	public void cancelTransaction() {
		Log.e(TAG, "cancelTransaction() called");
	}

	@Override
	public int describeContents() {
		Log.e(TAG, "describeContents()....");
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.e(TAG, "writeToParcel(Parcel dest, int flags)....");
		// TODO Auto-generated method stub
	}

	@SuppressWarnings("rawtypes")
	public static final Creator CREATOR = new Creator() {
		String TAG = "Callback --- Parcelable.Creator ::: > ";

		@Override
		public SampleCallBack createFromParcel(Parcel in) {
			Log.e(TAG, "CallBackActivity createFromParcel(Parcel in)....");
			return new SampleCallBack(in);
		}

		@Override
		public Object[] newArray(int size) {
			Log.e(TAG, "Object[] newArray(int size)....");
			return new SampleCallBack[size];
		}
	};
}
