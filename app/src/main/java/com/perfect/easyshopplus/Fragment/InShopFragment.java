package com.perfect.easyshopplus.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.perfect.easyshopplus.Activity.CartActivity;
import com.perfect.easyshopplus.Activity.CheckoutInshopActivity;
import com.perfect.easyshopplus.Activity.StoreInShopActivity;
import com.perfect.easyshopplus.Adapter.InShopCartAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.CartModel;
import com.perfect.easyshopplus.Model.InshopCartModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class InShopFragment extends Fragment implements CartChangedListener{

    String TAG = "InShopFragment";
    ArrayList<InshopCartModel> cartlist = new ArrayList<>();
    DBHandler db;
    RecyclerView rv_cartlist;
    TextView bttoalamount, btcheckout, tvclear, bttoalsvdamount;
    TextView tvCartCount, tv_cartisempty, tv_additemstoitnow;
    LinearLayout emptyCart, bottom_navigation,lvTopCartCount;
    Button btShopNow;
    float subtotal, totalgst, aamountPay, totalMRP, totalRetailPrice, discount, memberdiscount, yousaved;
    public static CartChangedListener cartChangedListener = null;
    String strFrom;
    int currentPosition;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        CartActivity activity = (CartActivity) getActivity();
        strFrom = activity.getMyData();
        currentPosition =activity.getcurrentPosition();
        View v = inflater.inflate(R.layout.activity_inshop, container,
                false);

        Log.e(TAG,"Start   63   ");

        SharedPreferences pref4 = getContext().getApplicationContext().getSharedPreferences(Config.SHARED_PREF38, 0);
        ImageView imLogo=v.findViewById(R.id.imLogo);
        SharedPreferences baseurlpref = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getActivity().getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        String strImagepath1= IMAGEURL + pref4.getString("LogoImageCode", null);
        // PicassoTrustAll.getInstance(getContext()).load(strImagepath1).into(imLogo);
        Glide.with( this ).load( R.drawable.emptycartgf ).into( imLogo );

        rv_cartlist=(RecyclerView)v.findViewById(R.id.rv_cartlist);
        bttoalamount=(TextView)v.findViewById(R.id.bttoalamount);
        btcheckout=(TextView)v.findViewById(R.id.btcheckout);
        tvclear=(TextView)v.findViewById(R.id.tvclear);
        emptyCart=(LinearLayout)v.findViewById(R.id.emptyCart);
        btShopNow=(Button) v.findViewById(R.id.btShopNow);
        bttoalsvdamount=(TextView)v.findViewById(R.id.bttoalsvdamount);
        tv_additemstoitnow=(TextView)v.findViewById(R.id.tv_additemstoitnow);
        tvCartCount=(TextView)v.findViewById(R.id.tvCartCount);
        tv_cartisempty=(TextView)v.findViewById(R.id.tv_cartisempty);
        lvTopCartCount=(LinearLayout)v.findViewById(R.id.lvTopCartCount);
        bottom_navigation=(LinearLayout)v.findViewById(R.id.change_location_bottom_navigation);


        SharedPreferences proceed = getContext().getSharedPreferences(Config.SHARED_PREF132, 0);
        btcheckout.setText(proceed.getString("proceed",""));
        SharedPreferences clearall = getContext().getSharedPreferences(Config.SHARED_PREF128, 0);
        tvclear.setText(clearall.getString("clearall",""));

        SharedPreferences yourcartisempty = getContext().getSharedPreferences(Config.SHARED_PREF125, 0);
        tv_cartisempty.setText(yourcartisempty.getString("yourcartisempty", ""));
        Log.e(TAG,"yourcartisempty   "+yourcartisempty.getString("yourcartisempty", ""));


        SharedPreferences additemstoitnow = getContext().getSharedPreferences(Config.SHARED_PREF126, 0);
        tv_additemstoitnow.setText(additemstoitnow.getString("additemstoitnow", ""));
        SharedPreferences shopnow = getContext().getSharedPreferences(Config.SHARED_PREF127, 0);
        btShopNow.setText(shopnow.getString("shopnow", ""));

        getAllIncart();
        String string = "\u20B9";
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {e.printStackTrace();}
        float totalAmount=db.selectInshopCartTotalActualPrice();
        DecimalFormat f = new DecimalFormat("##.00");
        subtotal=db.selectInshopCartTotalActualPrice();
        totalgst=db.InshopCartTotalGST();
        totalMRP=db.InshopCartTotalMRP();
        totalRetailPrice=db.InshopCartTotalRetailPrice();
        discount=totalMRP-totalRetailPrice;
        memberdiscount=   totalRetailPrice-subtotal;
        aamountPay=subtotal+totalgst;
        yousaved=discount+memberdiscount;
        SharedPreferences totalamount = getContext().getSharedPreferences(Config.SHARED_PREF131, 0);
        bttoalamount.setText(totalamount.getString("totalamount", "")+" : "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        SharedPreferences youhavesaved = getContext().getSharedPreferences(Config.SHARED_PREF212, 0);
        bttoalsvdamount.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");

//        bttoalamount.setText("Total Amount : "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
//        bttoalsvdamount.setText("( You have saved "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");
        btcheckout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CheckoutInshopActivity.class));
            }
        });
        btShopNow.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), StoreInShopActivity.class));
            }
        });
        tvclear.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHandler db=new DBHandler(getContext());
                db.deleteallCart();
                onCartChanged();
                getAllIncart();
            }
        });
        SharedPreferences totalitems = getContext().getSharedPreferences(Config.SHARED_PREF129, 0);
        tvCartCount.setText(totalitems.getString("totalitems", "") + " : " + (db.selectCartCount()));
//        tvCartCount.setText("Total items : " + (db.selectInshopCartCount()));
        onCartChanged();
        cartChangedListener = this;
        return v;
    }

    public void getAllIncart(){
        db = new DBHandler(getContext());
        if(db.selectInshopCartCount()==0){
            rv_cartlist.setVisibility(View.GONE);
            emptyCart.setVisibility(View.VISIBLE);
            bottom_navigation.setVisibility(View.GONE);
            lvTopCartCount.setVisibility(View.GONE);
        }else{
            rv_cartlist.setVisibility(View.VISIBLE);
            emptyCart.setVisibility(View.GONE);
            bottom_navigation.setVisibility(View.VISIBLE);
            lvTopCartCount.setVisibility(View.VISIBLE);
        }
        cartlist = new ArrayList<>(db.getAllInshopCart());
        try {
            Gson gson = new Gson();
            String listString = gson.toJson(cartlist,new TypeToken<ArrayList<CartModel>>() {}.getType());
            JSONArray jarray =  new JSONArray(listString);
            GridLayoutManager lLayout = new GridLayoutManager(getContext(), 1);
            rv_cartlist.setLayoutManager(lLayout);
            rv_cartlist.setHasFixedSize(true);
            InShopCartAdapter adapter = new InShopCartAdapter(getContext(), jarray, this);
            rv_cartlist.setAdapter(adapter);
        }catch (Exception e){
        }
    }

    @Override
    public void onCartChanged() {
        db=new DBHandler(getContext());
        String string = "\u20B9";
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {e.printStackTrace();}
        DecimalFormat f = new DecimalFormat("##.00");
        subtotal=db.selectInshopCartTotalActualPrice();
        totalgst=db.InshopCartTotalGST();
        totalMRP=db.InshopCartTotalMRP();
        totalRetailPrice=db.InshopCartTotalRetailPrice();
        discount=totalMRP-totalRetailPrice;
        memberdiscount=   totalRetailPrice-subtotal;
        aamountPay=subtotal+totalgst;
        yousaved=discount+memberdiscount;
        SharedPreferences totalamount = getContext().getSharedPreferences(Config.SHARED_PREF131, 0);
        bttoalamount.setText(totalamount.getString("totalamount", "")+" : "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        SharedPreferences youhavesaved = getContext().getSharedPreferences(Config.SHARED_PREF212, 0);
        bttoalsvdamount.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");

//        bttoalamount.setText("Total Amount : "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
//        bttoalsvdamount.setText("( You have saved "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");
        TextView textview = (TextView)getActivity().findViewById(R.id.tvcart);
        if(strFrom.equals("Inshop")){
            textview.setText(String.valueOf(db.selectInshopCartCount()));}

        SharedPreferences totalitems = getContext().getSharedPreferences(Config.SHARED_PREF129, 0);
        tvCartCount.setText(totalitems.getString("totalitems", "") + " : " + (db.selectCartCount()));
//        tvCartCount.setText("Total items : " + (db.selectInshopCartCount()));
    }

}