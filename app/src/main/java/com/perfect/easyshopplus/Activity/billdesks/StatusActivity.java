package com.perfect.easyshopplus.Activity.billdesks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.billdesk.sdk.PaymentOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.perfect.easyshopplus.Activity.AddressAddActivty;
import com.perfect.easyshopplus.Activity.AddressListActivity;
import com.perfect.easyshopplus.Activity.CartActivity;
import com.perfect.easyshopplus.Activity.HomeActivity;
import com.perfect.easyshopplus.Activity.NoInternetActivity;
import com.perfect.easyshopplus.Activity.ThanksActivity;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class StatusActivity extends Activity {
	TextView status;
	String TAG = "StatusActivity";
	String mStatus[];
	ProgressDialog progressDialog;

	String StrDeliveryDate;
	String DeliveryTime;
	String strRemark ="";
	int inExpressdelivery=0;
	String strLongitude = "";
	String strLatitude = "";
	private File fileimage = null;
	String strPaymenttype ="";
	String finalamount;

	String  OK;



	String MerchantID;
//	String UniqueTxnID;
	String TxnReferenceNo;
	String BankReferenceNo;
//	String TxnAmount;
	String BankID;
	String BIN;
//	String TxnType;
	String CurrencyName;
	String ItemCode;
	String SecurityType;
	String SecurityID;
	String SecurityPassword;
	String TxnDate;
//	String AuthStatus;
	String SettlementType;
	String AdditionalInfo1;
	String AdditionalInfo2;
	String AdditionalInfo3;
	String AdditionalInfo4 ;
	String AdditionalInfo5;
	String AdditionalInfo6;
	String AdditionalInfo7;
	String ErrorStatus;
	String ErrorDescription;
	String CheckSum;

	LinearLayout lnr_error;
	TextView tv_ok;
	String Checksumkey=  "G3eAmyVkAzKp8jFq0fqPEqxF4agynvtJ";
	String result = "";

	String ID_SalesOrder ;
	String FK_PaymentMethod;
	String UniqueTxnID;
	String PayDescription = "0";
	String PayResponseId = "0";
	String TxnAmount;
	String TxnType;
	String AuthStatus;
	String FK_SalesOrder;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_status);

		Log.e(TAG,"Start     25");

		status = (TextView) findViewById(R.id.status);
		lnr_error = (LinearLayout) findViewById(R.id.lnr_error);
		tv_ok = (TextView) findViewById(R.id.tv_ok);

		Bundle bundle = this.getIntent().getExtras();
		mStatus = bundle.getString("status").toString().split("\\|");

//		status.setText(bundle.getString("status"));
		lnr_error.setVisibility(View.GONE);

		SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
		OK = (OKsp.getString("OK", ""));
		tv_ok.setText(""+OKsp.getString("OK", ""));

		result =bundle.getString("status");
		Log.e(TAG,"result   175    "+result);


		Log.e(TAG,"status   175    "+bundle.getString("status"));
//		String[] separated = bundle.getString("status").split("\\|");
//		String result = bundle.getString("status").replace(bundle.getString("status").split("\\|")[25],"");
//		result = result+Checksumkey;
//		Log.e(TAG,"result   175    "+result);
//
//
//
//
//		MerchantID = separated[0];
//		UniqueTxnID = separated[1];
//		TxnReferenceNo = separated[2];
//		BankReferenceNo = separated[3];
//		TxnAmount = separated[4];
//
//		BankID = separated[5];
//		BIN = separated[6];
//		TxnType = separated[7];
//		CurrencyName = separated[8];
//		ItemCode = separated[9];
//
//		SecurityType = separated[10];
//		SecurityID = separated[11];
//		SecurityPassword = separated[12];
//		TxnDate = separated[13];
//		AuthStatus = separated[14];
//
//		SettlementType = separated[15];
//		AdditionalInfo1 = separated[16];
//		AdditionalInfo2 = separated[17];
//		AdditionalInfo3 = separated[18];
//		AdditionalInfo4 = separated[19];
//
//		AdditionalInfo5 = separated[20];
//		AdditionalInfo6 = separated[21];
//		AdditionalInfo7 = separated[22];
//		ErrorStatus = separated[23];
//		ErrorDescription = separated[24];
//		CheckSum = separated[25];



//		Log.e(TAG,"MerchantID   175   "+MerchantID);
//		Log.e(TAG,"UniqueTxnID   175   "+UniqueTxnID);
//		Log.e(TAG,"TxnReferenceNo   175   "+TxnReferenceNo);
//		Log.e(TAG,"BankReferenceNo   175   "+BankReferenceNo);
//		Log.e(TAG,"TxnAmount   175   "+TxnAmount);
//
//		Log.e(TAG,"BankID   175   "+BankID);
//		Log.e(TAG,"BIN   175   "+BIN);
//		Log.e(TAG,"TxnType   175   "+TxnType);
//		Log.e(TAG,"CurrencyName   175   "+CurrencyName);
//		Log.e(TAG,"ItemCode   175   "+ItemCode);
//
//		Log.e(TAG,"SecurityType   175   "+SecurityType);
//		Log.e(TAG,"SecurityID   175   "+SecurityID);
//		Log.e(TAG,"SecurityPassword   175   "+SecurityPassword);
//		Log.e(TAG,"TxnDate   175   "+TxnDate);
//		Log.e(TAG,"AuthStatus   175   "+AuthStatus);
//
//		Log.e(TAG,"SettlementType   175   "+SettlementType);
//		Log.e(TAG,"AdditionalInfo1   175   "+AdditionalInfo1);
//		Log.e(TAG,"AdditionalInfo2   175   "+AdditionalInfo2);
//		Log.e(TAG,"AdditionalInfo3   175   "+AdditionalInfo3);
//		Log.e(TAG,"AdditionalInfo4   175   "+AdditionalInfo4);
//
//		Log.e(TAG,"AdditionalInfo5   175   "+AdditionalInfo5);
//		Log.e(TAG,"AdditionalInfo6   175   "+AdditionalInfo6);
//		Log.e(TAG,"AdditionalInfo7   175   "+AdditionalInfo7);
//		Log.e(TAG,"ErrorStatus   175   "+ErrorStatus);
//		Log.e(TAG,"ErrorDescription   175   "+ErrorDescription);
//		Log.e(TAG,"CheckSum   175   "+CheckSum);


//		SharedPreferences MerchantKey = getApplicationContext().getSharedPreferences(Config.SHARED_PREF340, 0);
//		MerchantKey.getString("MerchantKey", null)

//		try{
//
//			SharedPreferences ShareStrDeliveryDate = getApplicationContext().getSharedPreferences(Config.SHARED_PREF373, 0);
//			Log.e(TAG,"StrDeliveryDate  37  "+ShareStrDeliveryDate.getString("StrDeliveryDate", null));
//			StrDeliveryDate = ShareStrDeliveryDate.getString("StrDeliveryDate", null);
//
//			SharedPreferences ShareDeliveryTime = getApplicationContext().getSharedPreferences(Config.SHARED_PREF374, 0);
//			Log.e(TAG,"DeliveryTime  37  "+ShareDeliveryTime.getString("DeliveryTime", null));
//			DeliveryTime = ShareDeliveryTime.getString("DeliveryTime", null);
//
//			SharedPreferences SharestrRemark = getApplicationContext().getSharedPreferences(Config.SHARED_PREF375, 0);
//			Log.e(TAG,"strRemark  37  "+SharestrRemark.getString("strRemark", null));
//			strRemark = SharestrRemark.getString("strRemark", null);
//
//
//			SharedPreferences ShareinExpressdelivery = getApplicationContext().getSharedPreferences(Config.SHARED_PREF376, 0);
//			Log.e(TAG,"inExpressdelivery  37  "+ShareinExpressdelivery.getInt("inExpressdelivery", 0));
//
//			inExpressdelivery = ShareinExpressdelivery.getInt("inExpressdelivery", 0);
//
//			SharedPreferences SharestrLongitude = getApplicationContext().getSharedPreferences(Config.SHARED_PREF377, 0);
//			Log.e(TAG,"strLongitude  37  "+SharestrLongitude.getString("strLongitude", null));
//			strLongitude = SharestrLongitude.getString("strLongitude", null);
//
//			SharedPreferences SharestrLatitude = getApplicationContext().getSharedPreferences(Config.SHARED_PREF378, 0);
//			Log.e(TAG,"strLatitude  37  "+SharestrLatitude.getString("strLatitude", null));
//			strLatitude = SharestrLatitude.getString("strLatitude", null);
//
//			SharedPreferences Sharesfileimage = getApplicationContext().getSharedPreferences(Config.SHARED_PREF379, 0);
//			Log.e(TAG,"fileimage  37  "+Sharesfileimage.getString("fileimage", null));
//			String destination = Sharesfileimage.getString("fileimage", null);
//			if (destination.equals("")){
//				Log.e(TAG,"fileimage if  37  "+destination);
//				fileimage = null;
//			}else {
//				Log.e(TAG,"fileimage else 37  "+Sharesfileimage.getString("fileimage", null));
//				fileimage = new File(destination);
//			}


			SharedPreferences SharesstrPaymenttype = getApplicationContext().getSharedPreferences(Config.SHARED_PREF380, 0);
			Log.e(TAG,"SharesstrPaymenttype  277  "+SharesstrPaymenttype.getString("strPaymenttype", null));
			strPaymenttype = SharesstrPaymenttype.getString("strPaymenttype", null);

			SharedPreferences Sharesfinalamount = getApplicationContext().getSharedPreferences(Config.SHARED_PREF381, 0);
			Log.e(TAG,"finalamount  277  "+Sharesfinalamount.getString("finalamount", null));
			finalamount = Sharesfinalamount.getString("finalamount", null);



		SharedPreferences ConfirmOrderNo = getApplicationContext().getSharedPreferences(Config.SHARED_PREF402, 0);
		Log.e(TAG,"ConfirmOrderNo  277  "+ConfirmOrderNo.getString("ConfirmOrderNo", null));
		ID_SalesOrder = ConfirmOrderNo.getString("ConfirmOrderNo", null);

		SharedPreferences prefstrPaymentId = getApplicationContext().getSharedPreferences(Config.SHARED_PREF403, 0);
		Log.e(TAG,"strPaymentId  277  "+prefstrPaymentId.getString("strPaymentId", null));
		FK_PaymentMethod = prefstrPaymentId.getString("strPaymentId", null);

		SharedPreferences prefFK_SalesOrder = getApplicationContext().getSharedPreferences(Config.SHARED_PREF404, 0);
		Log.e(TAG,"FK_SalesOrder  277  "+prefFK_SalesOrder.getString("FK_SalesOrder", null));
		FK_SalesOrder = prefFK_SalesOrder.getString("FK_SalesOrder", null);


		VerifyStatus();
//
//
//
//			if (AuthStatus.equals("0300")){
//				lnr_error.setVisibility(View.GONE);
//				//doOrderConfirm();
//			}else {
//				lnr_error.setVisibility(View.VISIBLE);
//				status.setText(""+ErrorDescription);
//
////				String strPGMsg = result;
////				String strTokenMsg = null;
////				SampleCallBack objSampleCallBack = new SampleCallBack();
////
////				Intent sdkIntent = new Intent(this, PaymentOptions.class);
////				sdkIntent.putExtra("msg",strPGMsg);
////				if(strTokenMsg != null && strTokenMsg.length() > strPGMsg.length()) {
////
////					sdkIntent.putExtra("token",strTokenMsg);
////				}
////				sdkIntent.putExtra("user-email","test@bd.com");
////				sdkIntent.putExtra("user-mobile","9800000000");
////				sdkIntent.putExtra("callback",objSampleCallBack);
////
////				startActivity(sdkIntent);
//
//			//	Log.e(TAG,"CheckSum Results  37  "+checkSumSHA256( result));
//
//
//			}
//
//		//
//
//		}catch (Exception e){
//			Log.e(TAG,"Exception  37  "+e.toString());
//		}



		tv_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent i = new Intent(StatusActivity.this, HomeActivity.class);
				startActivity(i);
				finish();
			}
		});

	}

	private void VerifyStatus() {

		SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
		SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
		String BASEURL = baseurlpref.getString("BaseURL", null);
		String IMAGEURL = imgpref.getString("ImageURL", null);
		if (new InternetUtil(this).isInternetOn()) {
			progressDialog = new ProgressDialog(this, R.style.Progress);
			progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar);
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(true);
			progressDialog.setIndeterminateDrawable(this.getResources()
					.getDrawable(R.drawable.progress));
			progressDialog.show();
			try{
				OkHttpClient client = new OkHttpClient.Builder()
						.sslSocketFactory(getSSLSocketFactory())
						.hostnameVerifier(getHostnameVerifier())
						.build();
				Gson gson = new GsonBuilder()
						.setLenient()
						.create();
				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(BASEURL)
						.addConverterFactory(ScalarsConverterFactory.create())
						.addConverterFactory(GsonConverterFactory.create(gson))
						.client(client)
						.build();
				ApiInterface apiService = retrofit.create(ApiInterface.class);
				final JSONObject requestObject1 = new JSONObject();
				try {


					requestObject1.put("Message", Utils.encryptStart(result));

					Log.e(TAG,"requestObject1   359    "+requestObject1);


				} catch (JSONException e) {
					e.printStackTrace();
				}
				RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
				Call<String> call = apiService.getVerifyStatus(body);
				call.enqueue(new Callback<String>() {
					@Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
						try {
							progressDialog.dismiss();
							Log.e(TAG,"response   371    "+response.body());
							JSONObject jObject = new JSONObject(response.body());
//
//							if(jObject.getString("StatusCode").equals("0")){
								JSONObject jobj = jObject.getJSONObject("GateWayResult");
								Log.e(TAG,"msg   1697   "+jobj.getString("Status"));
								Log.e(TAG,"msg   1697   "+jobj.getString("ResponseMessage"));
								String Status = jobj.getString("Status");
								if (Status.equals("true")){
//									{"GateWayResult":{"Status":true,"TransDateTime":"24-08-2021 20:41:34","TransType":"01","TransactionId":"BDSK11102","TranStatus":"0300",
//											"TranAmnt":"00000002.00","ResponseCode":"0","ResponseMessage":"Transaction Verified"},"StatusCode":0,"EXMessage":null}
									UniqueTxnID = jobj.getString("TransactionId");
									TxnAmount = jobj.getString("TranAmnt");
									AuthStatus = jobj.getString("TranStatus");
									TxnType = jobj.getString("TransType");
									if (jobj.getString("TranStatus").equals("0300")){
										Log.e(TAG,"msg   1697 1   "+jobj.getString("Status"));
										updatePayments(ID_SalesOrder,FK_SalesOrder,FK_PaymentMethod,UniqueTxnID,PayDescription,AuthStatus,PayResponseId,TxnAmount,TxnType);
									}else {
										AlertDialog.Builder builder = new AlertDialog.Builder(StatusActivity.this);
										builder.setMessage("Authentication Failed")
												.setCancelable(false)
												.setPositiveButton(OK, new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialog, int id) {
														updatePayments(ID_SalesOrder,FK_SalesOrder,FK_PaymentMethod,UniqueTxnID,PayDescription,AuthStatus,PayResponseId,TxnAmount,TxnType);
													}
												});
										AlertDialog alert = builder.create();
										alert.show();
									}



//									{"ID_SalesOrder":"20469","FK_PaymentMethod":"0","PayTransactionID":"0",
//											"PayDescription":"0","PayStatus":"0","PayResponseId":"0","Amount":"0","TransType":"0"}


								}else {
									AlertDialog.Builder builder= new AlertDialog.Builder(StatusActivity.this);
									builder.setMessage(jobj.getString("ResponseMessage"))
											.setCancelable(false)
											.setPositiveButton(OK, new DialogInterface.OnClickListener() {
												public void onClick(DialogInterface dialog, int id) {
													startActivity(new Intent(StatusActivity.this, HomeActivity.class));
												}
											});
									AlertDialog alert = builder.create();
									alert.show();
									Log.e(TAG,"msg   1697 2   "+jobj.getString("Status"));
								}
//
//								dsfsd
//
//								upDatepayments();

								//            String strPGMsg = "BDSKUATY|ARP10234|NA|2.00|NA|NA|NA|INR|NA|R|bdskuaty|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|http://www.domain.com/response.jsp|892409133";
//                                String strPGMsg = "|ARP1553593909862|NA|2|NA|NA|NA|INR|NA|R|airmtst|NA|NA|F|NA|NA|NA|NA|NA|NA|NA|https://uat.billdesk.com/pgidsk/pgmerc/pg_dump.jsp|892409133";

//							}else{
//								JSONObject jobj = jObject.getJSONObject("GateWayResult");
//								Log.e(TAG,"msg   1697   "+jobj.getString("ResponseMessage"));
//								Toast.makeText(getApplicationContext(),jobj.getString("ResponseMessage")+ "!!",Toast.LENGTH_LONG).show();
//							}
						} catch (JSONException e) {
							e.printStackTrace();
							progressDialog.dismiss();
						}
					}
					@Override
					public void onFailure(Call<String> call, Throwable t) {
						progressDialog.dismiss();
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			Intent in = new Intent(this,NoInternetActivity.class);
			startActivity(in);
		}
	}

	private void updatePayments(String id_salesOrder, String FK_SalesOrder,String fk_paymentMethod, String uniqueTxnID, String payDescription,
								String authStatuss, String payResponseId, String txnAmount, String txnType) {

		Log.e(TAG,"455   id_salesOrder       "+id_salesOrder);
		Log.e(TAG,"455   FK_SalesOrder       "+FK_SalesOrder);
		Log.e(TAG,"455   fk_paymentMethod    "+fk_paymentMethod);
		Log.e(TAG,"455   uniqueTxnID         "+uniqueTxnID);
		Log.e(TAG,"455   payDescription      "+payDescription);
		Log.e(TAG,"455   authStatus          "+authStatuss);
		Log.e(TAG,"455   payResponseId       "+payResponseId);
		Log.e(TAG,"455   txnAmount           "+txnAmount);
		Log.e(TAG,"455   txnType             "+txnType);

		SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
		SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
		String BASEURL = baseurlpref.getString("BaseURL", null);
		String IMAGEURL = imgpref.getString("ImageURL", null);
		if (new InternetUtil(this).isInternetOn()) {
			progressDialog = new ProgressDialog(this, R.style.Progress);
			progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar);
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(true);
			progressDialog.setIndeterminateDrawable(this.getResources()
					.getDrawable(R.drawable.progress));
			progressDialog.show();
			try{
				OkHttpClient client = new OkHttpClient.Builder()
						.sslSocketFactory(getSSLSocketFactory())
						.hostnameVerifier(getHostnameVerifier())
						.build();
				Gson gson = new GsonBuilder()
						.setLenient()
						.create();
				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(BASEURL)
						.addConverterFactory(ScalarsConverterFactory.create())
						.addConverterFactory(GsonConverterFactory.create(gson))
						.client(client)
						.build();
				ApiInterface apiService = retrofit.create(ApiInterface.class);
				final JSONObject requestObject1 = new JSONObject();
				try {
					DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
					Calendar cal = Calendar.getInstance();
					String currentdate = dateFormat.format(cal.getTime());
					SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");


					SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
					SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
					SharedPreferences pref3= getApplicationContext().getSharedPreferences(Config.SHARED_PREF9, 0);
					SharedPreferences pref4= getApplicationContext().getSharedPreferences(Config.SHARED_PREF20, 0);


//					uniqueTxnID = "";
//					authStatus = "";
//					txnAmount = "";
//					txnType  ="";

					requestObject1.put("ID_SalesOrder", FK_SalesOrder);
					requestObject1.put("FK_PaymentMethod", fk_paymentMethod);
					requestObject1.put("PayTransactionID",uniqueTxnID);
					requestObject1.put("PayDescription", payDescription);
					requestObject1.put("PayStatus", authStatuss);
					requestObject1.put("PayResponseId",payResponseId );
					requestObject1.put("Amount",txnAmount );
					requestObject1.put("TransType", txnType);


					Log.e(TAG,"requestObject1   516    "+requestObject1);


				} catch (JSONException e) {
					e.printStackTrace();
			}

                RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.PaymentDetailUpdate(body);
				call.enqueue(new Callback<String>() {
					@Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
						try {
							progressDialog.dismiss();
							Log.e(TAG,"response   1675  "+response.body());


//							{"GateWayResult":{"Status":true,"TransDateTime":"25-08-2021 11:14:31","TransType":"01","TransactionId":"BDSK11112","TranStatus":"0300",
//									"TranAmnt":"00000002.00","ResponseCode":"0","ResponseMessage":"Transaction Verified"},"StatusCode":0,"EXMessage":null}


							JSONObject jObject = new JSONObject(response.body());
							JSONObject jobj = jObject.getJSONObject("SalesOrderDetails");
							if(jObject.getString("StatusCode").equals("0")){

								Log.e(TAG,"authStatus   567   "+authStatuss+"   ");

								if (authStatuss.equals("0300")){
									Log.e(TAG,"authStatus   567 1   "+authStatuss+"   ");
									startActivity(new Intent(StatusActivity.this, ThanksActivity.class));
									SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
									Intent intent = new Intent(StatusActivity.this,ThanksActivity.class);
									intent.putExtra("StoreName", pref1.getString("StoreName", null));
									intent.putExtra("OrderNumber",jobj.getString("OrderNumber"));
									intent.putExtra("strPaymenttype",strPaymenttype);
									intent.putExtra("finalamount",finalamount);
//
//
									startActivity(intent);

									finish();

								}else {
									Log.e(TAG,"authStatus   567 2   "+authStatuss+"   ");
									startActivity(new Intent(StatusActivity.this, HomeActivity.class));
								}




							}else {

							}

//

						} catch (JSONException e) {
							e.printStackTrace();
							progressDialog.dismiss();
						}
					}
					@Override
					public void onFailure(Call<String> call, Throwable t) {
						progressDialog.dismiss();
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			Intent in = new Intent(this, NoInternetActivity.class);
			startActivity(in);
		}



	}

	private void doOrderConfirm(){
		SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
		SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
		String BASEURL = baseurlpref.getString("BaseURL", null);
		String IMAGEURL = imgpref.getString("ImageURL", null);
		if (new InternetUtil(this).isInternetOn()) {
			progressDialog = new ProgressDialog(this, R.style.Progress);
			progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar);
			progressDialog.setCancelable(false);
			progressDialog.setIndeterminate(true);
			progressDialog.setIndeterminateDrawable(this.getResources()
					.getDrawable(R.drawable.progress));
			progressDialog.show();
			try{
				OkHttpClient client = new OkHttpClient.Builder()
						.sslSocketFactory(getSSLSocketFactory())
						.hostnameVerifier(getHostnameVerifier())
						.build();
				Gson gson = new GsonBuilder()
						.setLenient()
						.create();
				Retrofit retrofit = new Retrofit.Builder()
						.baseUrl(BASEURL)
						.addConverterFactory(ScalarsConverterFactory.create())
						.addConverterFactory(GsonConverterFactory.create(gson))
						.client(client)
						.build();
				ApiInterface apiService = retrofit.create(ApiInterface.class);
				final JSONObject requestObject1 = new JSONObject();
				try {
					DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
					Calendar cal = Calendar.getInstance();
					String currentdate = dateFormat.format(cal.getTime());
					SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");


					SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF1, 0);
					SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
					SharedPreferences pref3= getApplicationContext().getSharedPreferences(Config.SHARED_PREF9, 0);
					SharedPreferences pref4= getApplicationContext().getSharedPreferences(Config.SHARED_PREF20, 0);



					requestObject1.put("ID_Customer", pref1.getString("userid", null));
					requestObject1.put("OrderDate", currentdate);
					requestObject1.put("DeliveryDate",StrDeliveryDate);
					requestObject1.put("DeliveryTime", DeliveryTime);
					requestObject1.put("Remarks", strRemark);
					requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
					JSONArray jsonArray = new JSONArray();
					DBHandler db=new DBHandler(this);
					Cursor cursor = db.select("cart");
					int i = 0;
					if (cursor.moveToFirst()) {
						do {
							JSONObject jsonObject1 = new JSONObject();
							try {
								jsonObject1.put("ID_Stock", cursor.getString(cursor.getColumnIndex("Stock_ID")));
								jsonObject1.put("ID_Item", cursor.getString(cursor.getColumnIndex("ID_Items")));
								jsonObject1.put("ItemName", cursor.getString(cursor.getColumnIndex("ItemName")));
								jsonObject1.put("MRP",cursor.getString(cursor.getColumnIndex("MRP")));
								jsonObject1.put("SalesPrice", cursor.getString(cursor.getColumnIndex("SalesPrice")));
								jsonObject1.put("Quantity", cursor.getString(cursor.getColumnIndex("Count")));
								jsonObject1.put("RetailPrice",cursor.getString(cursor.getColumnIndex("RetailPrice")));
								jsonObject1.put("VAT", cursor.getString(cursor.getColumnIndex("GST")));
								jsonObject1.put("Cess",cursor.getString(cursor.getColumnIndex("CESS")));
							} catch (JSONException e) {
								e.printStackTrace();
							}
							try {
								jsonArray.put(i, jsonObject1);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							i++;
						} while (cursor.moveToNext());
					}
					cursor.close();
					requestObject1.put("Data", jsonArray);
					requestObject1.put("UserAction", "1");
					requestObject1.put("ShopType", pref3.getString("ShopType", null));
					requestObject1.put("DeliveryType", 1);
					requestObject1.put("ID_CustomerAddress", pref4.getString("DeliAddressID", null));
					requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
					requestObject1.put("ExpressDelivery", inExpressdelivery);
					requestObject1.put("SOLongitude", strLongitude);
					requestObject1.put("SOLattitude", strLatitude);


					requestObject1.put("PrescriptionImage", "");
					SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
					requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));

					Log.e(TAG,"requestObject1   1393    "+requestObject1);


				} catch (JSONException e) {
					e.printStackTrace();
				}
				Log.e(TAG,"fileimage  1092   "+fileimage);
				MultipartBody.Part imageFiles = null;
				Call<String> call = null;
				if (fileimage !=null){
					Log.e("jsondattaa","fileimage  1618   "+fileimage);
					RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), fileimage);
					imageFiles = MultipartBody.Part.createFormData("JsonData", fileimage.getName(), requestFile);
					// RequestBody body = RequestBody.create(MediaType.parse("text/plain"), requestObject1.toString());
					RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
					Log.e("jsondattaa","BODY  1676 "+body);
					Log.e("jsondattaa","BODY  1677 "+imageFiles);

					call = apiService.getSalesOrderUpdateImgFile(body,imageFiles);

				}
				else {

					Log.e("jsondattaa","BODY  1683 "+requestObject1);
					RequestBody body = RequestBody.create(MediaType.parse("text/plain"), requestObject1.toString());
					RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
					imageFiles = MultipartBody.Part.createFormData("JsonData", "", requestFile);
					call = apiService.getSalesOrderUpdateImgFile(body,imageFiles);

//                    RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
//                     call = apiService.confirmOrder(body);

				}
//                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
//                Call<String> call = apiService.confirmOrder(body);
				call.enqueue(new Callback<String>() {
					@Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
						try {
							progressDialog.dismiss();
							Log.e(TAG,"response   1675  "+response.body());


							JSONObject jObject = new JSONObject(response.body());
							JSONObject jobj = jObject.getJSONObject("SalesOrderDetails");
							if(jObject.getString("StatusCode").equals("3")){
								Toast.makeText(getApplicationContext(), jobj.getString("ResponseMessage"),Toast.LENGTH_LONG).show();
								DBHandler db=new DBHandler(StatusActivity.this);
								db.deleteallCart();
//                                startActivity(new Intent(AddressAddActivty.this, ThanksActivity.class));
								SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
								Intent intent = new Intent(StatusActivity.this, ThanksActivity.class);
								intent.putExtra("StoreName", pref1.getString("StoreName", null));
								intent.putExtra("OrderNumber",jobj.getString("OrderNumber"));
								intent.putExtra("strPaymenttype",strPaymenttype);
								intent.putExtra("finalamount",finalamount);
								startActivity(intent);

								finish();
							}
							else if(jObject.getString("StatusCode").equals("10")){
								AlertDialog.Builder builder= new AlertDialog.Builder(StatusActivity.this);
								builder.setMessage(jobj.getString("ResponseMessage"))
										.setCancelable(false)
										.setPositiveButton(OK, new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {
												Intent i = new Intent(StatusActivity.this, CartActivity.class);
												i.putExtra("From", "Home");
												startActivity(i);
											}
										});
								AlertDialog alert = builder.create();
								alert.show();
							}
							else if(jObject.getString("StatusCode").equals("-12")){
								AlertDialog.Builder builder= new AlertDialog.Builder(StatusActivity.this);
								builder.setMessage(jobj.getString("ResponseMessage"))
										.setCancelable(false)
										.setPositiveButton(OK, new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {
												startActivity(new Intent(StatusActivity.this, AddressListActivity.class));
											}
										});
								AlertDialog alert = builder.create();
								alert.show();
							}
							else if(jObject.getString("StatusCode").equals("-13")){
								AlertDialog.Builder builder= new AlertDialog.Builder(StatusActivity.this);
								builder.setMessage(jobj.getString("ResponseMessage"))
										.setCancelable(false)
										.setPositiveButton(OK, new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int id) {

											}
										});
								AlertDialog alert = builder.create();
								alert.show();
							}
							else{
								SharedPreferences OrderSubmittedFailed = getApplicationContext().getSharedPreferences(Config.SHARED_PREF311, 0);
								Toast.makeText(getApplicationContext(),OrderSubmittedFailed.getString("OrderSubmittedFailed", "")+ " !",Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
							progressDialog.dismiss();
						}
					}
					@Override
					public void onFailure(Call<String> call, Throwable t) {
						progressDialog.dismiss();
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			Intent in = new Intent(this, NoInternetActivity.class);
			startActivity(in);
		}
	}

	private HostnameVerifier getHostnameVerifier() {
		return new HostnameVerifier() {
			@Override
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
	}

	private SSLSocketFactory getSSLSocketFactory()
			throws CertificateException, KeyStoreException, IOException,
			NoSuchAlgorithmException, KeyManagementException {
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		InputStream caInput = getResources().openRawResource(R.raw.my_cert); // File path: app\src\main\res\raw\your_cert.cer
		Certificate ca = cf.generateCertificate(caInput);
		caInput.close();
		KeyStore keyStore = KeyStore.getInstance("BKS");
		keyStore.load(null, null);
		keyStore.setCertificateEntry("ca", ca);
		String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
		tmf.init(keyStore);
		TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, wrappedTrustManagers, null);
		return sslContext.getSocketFactory();
	}

	private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
		final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
		return new TrustManager[]{
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						return originalTrustManager.getAcceptedIssuers();
					}
					public void checkClientTrusted(X509Certificate[] certs, String authType) {
						try {
							if (certs != null && certs.length > 0){
								certs[0].checkValidity();
							} else {
								originalTrustManager.checkClientTrusted(certs, authType);
							}
						} catch (CertificateException e) {
							Log.w("checkClientTrusted", e.toString());
						}
					}
					public void checkServerTrusted(X509Certificate[] certs, String authType) {
						try {
							if (certs != null && certs.length > 0){
								certs[0].checkValidity();
							} else {
								originalTrustManager.checkServerTrusted(certs, authType);
							}
						} catch (CertificateException e) {
							Log.w("checkServerTrusted", e.toString());
						}
					}
				}
		};
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		Intent i = new Intent(StatusActivity.this, HomeActivity.class);
		startActivity(i);
		finish();
	}


	public static String checkSumSHA256(String plaintext) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256"); // step 2
			md.update(plaintext.getBytes("UTF-8")); // step 3
		} catch (Exception e) {
			md = null;
		}

		StringBuffer ls_sb = new StringBuffer();
		byte raw[] = md.digest(); // step 4
		for (int i = 0; i < raw.length; i++)
			ls_sb.append(char2hex(raw[i]));
		return ls_sb.toString(); // step 6
	}
	public static String char2hex(byte x) {
		char arr[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F'};

		char c[] = {arr[(x & 0xF0) >> 4], arr[x & 0x0F]};
		return (new String(c));
	}

}
