package com.perfect.easyshopplus.Activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.Adapter.SimilarItemListAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.CartModel;
import com.perfect.easyshopplus.Model.CheckCartModel;
import com.perfect.easyshopplus.Model.FavModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Retrofit.ApiInterface;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.InternetUtil;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ItemDetailsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, CartChangedListener{

    float subtotal, totalgst, aamountPay, totalMRP, totalRetailPrice, discount, memberdiscount, yousaved;
    String OK, strQty, ItemName, MRP, SalesPrice, Id_Item, ID_Stock, CurrentStock, RetailPrice, PrivilagePrice,
            WholesalePrice, GST, CESS,From, ID_CategorySecond, SecondCategoryName, FirstCategoryName, ID_CategoryFirst,
            Description, ImageName, Packed, ItemMalayalamName;
    EditText etSearch;
    ImageView im, imcart, ivFavourites, imaddcart, im_notification, ivQtyMinus, ivQtyAdd,ivShare, iv_itemimage;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvPrdName, txtStock, tvdescptntitle, tvdescptn, tvStore, tv_notification, tvQtyValue, tvPrdAmount,
            tvuser, tvaddtocart, tvexit, tvcart, iv_headert, tvPrdMRPAmount, tvPrdMRPAmountsaved, tvaddcart,
            tvFavourites, tvqty;
    TextView txt_Quantity,txt_SimilarProducts,tvtoalamount,tvtoalsvdamount;
    boolean isFavorite;
    DBHandler db;
    boolean isCart;
    public static CartChangedListener cartChangedListener = null;
    RelativeLayout rl_notification;
    int counter= 1;
    ArrayList<CartModel> cartlist = new ArrayList<>();
    RecyclerView rv_similarprdct;
    TextView bttoalamount, bttoalsvdamount;
    double doubleTotalAmt;
    LinearLayout llQty, lladdQty,ll_addcart,ll_quantity;
    EditText tvQtyValueloose;
    View viewline;
    String TAG = "ItemDetailsActivity";
    BottomNavigationView navigation;
    String fromCategory = "from";
    String valueCategory = "home";
    String fromCart = "From";
    String valueCart = "Home";
    String fromHome = "";
    String valueHome = "";
    String fromFavorite = "From";
    String valueFavorite = "Home";
    SharedPreferences addtocart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_item_details_main);
        setInitialise();
        setBottomBar();
        setRegister();





        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
        ImageView imApplogo=findViewById(R.id.imApplogo);
        String strImagepath= IMAGEURL + pref3.getString("AppIconImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath).into(imApplogo);

        SharedPreferences SearchForProducts = getApplicationContext().getSharedPreferences(Config.SHARED_PREF118, 0);
        etSearch.setHint(""+SearchForProducts.getString("SearchForProducts",null));


        DecimalFormat f = new DecimalFormat("##.00");
        db = new DBHandler(this);
        Intent in = getIntent();
        Log.e(TAG,"ItemName  1555  "+in.getStringExtra("ItemName"));
        ItemName = in.getStringExtra("ItemName");
        MRP = in.getStringExtra("MRP");
        SalesPrice = in.getStringExtra("SalesPrice");
        Id_Item = in.getStringExtra("Id_Item");
        ID_Stock = in.getStringExtra("ID_Stock");
        CurrentStock = in.getStringExtra("CurrentStock");
        RetailPrice = in.getStringExtra("RetailPrice");
        PrivilagePrice = in.getStringExtra("PrivilagePrice");
        WholesalePrice = in.getStringExtra("WholesalePrice");
        Description = in.getStringExtra("Description");
        ImageName = in.getStringExtra("ImageName");
        GST = in.getStringExtra("GST");
        CESS = in.getStringExtra("CESS");
        From = in.getStringExtra("from");
        Packed = in.getStringExtra("Packed");
        ItemMalayalamName = in.getStringExtra("ItemMalayalamName");
        if (From.equals("HomeItem")||From.equals("HomeCatItem")||From.equals("Home")){
            ID_CategorySecond = in.getStringExtra("ID_CategorySecond");
            SecondCategoryName = in.getStringExtra("SecondCategoryName");
            FirstCategoryName = in.getStringExtra("FirstCategoryName");
            ID_CategoryFirst = in.getStringExtra("ID_CategoryFirst");
        }
        if (Description.isEmpty()){
            tvdescptntitle.setVisibility(View.GONE);
            tvdescptn.setVisibility(View.GONE);
            viewline.setVisibility(View.GONE);
        }else{
            tvdescptntitle.setVisibility(View.VISIBLE);
            tvdescptn.setVisibility(View.VISIBLE);
            viewline.setVisibility(View.VISIBLE);
            tvdescptn.setText(Description);
        }
        if(Double.parseDouble(CurrentStock)==0){
            txtStock.setVisibility(View.VISIBLE);
            ll_addcart.setVisibility(View.GONE);
            ll_quantity.setVisibility(View.GONE);


        }else{
            txtStock.setVisibility(View.GONE);
            ll_addcart.setVisibility(View.VISIBLE);
            ll_quantity.setVisibility(View.VISIBLE);
        }

        isCart=db.checkIcartItem(Id_Item);
        isFavorite=db.checkIfavItem(Id_Item);
        iv_headert.setText(ItemName);
        String string = "\u20B9";
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // ImageName="";
        //  String imagepath=Config.IMAGEURL + jsonObject.getString("ImagePath");
//        Glide.with(this)
//                .load(ImageName)
//                .placeholder(R.drawable.items)
//                .error(R.drawable.items)
//                .into(iv_itemimage);

        Log.e(TAG,"ImageName   309   "+ImageName);
        Log.e(TAG,"ImageName   309   "+ImageName);


        PicassoTrustAll.getInstance(this).load(ImageName).error(R.drawable.items).into(iv_itemimage);

        try{
            if(ItemMalayalamName.equals("")||ItemMalayalamName.isEmpty()) {
                tvPrdName.setText(ItemName);
            }else {
                tvPrdName.setText(ItemName+"\n[ "+ItemMalayalamName+" ]");
            }
        }catch (Exception e){
            Log.e(TAG,"Exception   309   "+e.toString());
        }
        tvPrdAmount.setText(/*string+" "+*/f.format(Double.parseDouble(SalesPrice))+" /-");
        double yousaved=Float.parseFloat(MRP)-Float.parseFloat(SalesPrice);
        if(yousaved<=0){
            tvPrdMRPAmount.setVisibility(View.GONE);
            tvPrdMRPAmountsaved.setVisibility(View.GONE);
        }else {
            tvPrdMRPAmount.setVisibility(View.VISIBLE);
            tvPrdMRPAmountsaved.setVisibility(View.VISIBLE);
        }
        SharedPreferences MRPS = getApplicationContext().getSharedPreferences(Config.SHARED_PREF115, 0);
        SharedPreferences yousaveds = getApplicationContext().getSharedPreferences(Config.SHARED_PREF117, 0);
        SharedPreferences Outofstock = getApplicationContext().getSharedPreferences(Config.SHARED_PREF116, 0);
        addtocart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF121, 0);
        SharedPreferences quantity = getApplicationContext().getSharedPreferences(Config.SHARED_PREF122, 0);
        SharedPreferences similarproducts = getApplicationContext().getSharedPreferences(Config.SHARED_PREF120, 0);

        tvPrdMRPAmount.setText(""+MRPS.getString("MRP",null)+" "+/*string+" "+*/f.format(Double.parseDouble(MRP))+" /-");
        tvPrdMRPAmount.setPaintFlags(tvPrdMRPAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        tvPrdMRPAmountsaved.setText(""+yousaveds.getString("yousaved",null)+" "+/*string+" "+*/f.format(yousaved));
        txtStock.setText(""+Outofstock.getString("Outofstock",null));
        txt_Quantity.setText(""+quantity.getString("quantity",null));
        txt_SimilarProducts.setText(""+similarproducts.getString("similarproducts",null));
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));
        setHomeNavMenu1();

       /* SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/

        SharedPreferences welcome = getApplicationContext().getSharedPreferences(Config.SHARED_PREF200, 0);
        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
//        tvuser.setText("Welcome "+pref2.getString("username", null));
        tvuser.setText(""+welcome.getString("welcome", null)+" "+pref2.getString("username", null));
        cartlist = new ArrayList<>(db.getAllCart());
        if (!isCart) {
            tvaddcart.setText(""+addtocart.getString("addtocart",null));
            imaddcart.setImageResource(R.drawable.cart_iconss);
            tvQtyValue.setText("1");
            tvQtyValueloose.setText("1");
        } else {
            tvaddcart.setText("View Cart");

            imaddcart.setImageResource(R.drawable.cart_icons);
            try {
                Gson gson = new Gson();
                String listString = gson.toJson(cartlist,new TypeToken<ArrayList<CartModel>>() {}.getType());
                JSONArray jarray =  new JSONArray(listString);
                for(int i=0; i<=jarray.length();i++){
                    JSONObject jobjt =  jarray.getJSONObject(i);
                    if(jobjt.getString("ID_Items").equals(Id_Item)){
                        int count=jobjt.getInt("Count");
                        tvQtyValue.setText(String.valueOf(count));
                        tvQtyValueloose.setText(String.valueOf(count));
                        break;
                    }
                }
            } catch (JSONException e) {e.printStackTrace();}
        }
        if (!isFavorite) {
            ivFavourites.setImageResource(R.drawable.ic_favoritegrey);
        } else {
            ivFavourites.setImageResource(R.drawable.ic_favorite_fill);
        }
        onCartChanged();
        db=new DBHandler(this);
        tvcart.setText(String.valueOf(db.selectCartCount()));
        tvqty.setText(CurrentStock);
        SharedPreferences Store = getApplicationContext().getSharedPreferences(Config.SHARED_PREF119, 0);
        SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF8, 0);
        tvStore.setText("SOLD BY "/*Store.getString("Store", null) */+" : "+pref1.getString("StoreName", null));
        getSimilarItemList();
        if(Packed.equals("false")){
            llQty.setVisibility(View.VISIBLE);
            lladdQty.setVisibility(View.GONE);
        }else{
            llQty.setVisibility(View.GONE);
            lladdQty.setVisibility(View.VISIBLE);
        }
        quantityEdition(tvQtyValueloose,Id_Item);


        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        OK = (OKsp.getString("OK", ""));


    }

    private void setBottomBar() {
        navigation= (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().setGroupCheckable(0, false, true);
    }

    private void setInitialise() {
        ivShare= findViewById(R.id.ivShare);
        tvQtyValueloose=(EditText) findViewById(R.id.tvQtyValueloose);
        txtStock=(TextView) findViewById(R.id.txtStock);
        ll_addcart = findViewById(R.id.ll_addcart);
        ll_quantity = findViewById(R.id.ll_quantity);
        tvPrdName=(TextView) findViewById(R.id.tvPrdName);
        tvStore=(TextView) findViewById(R.id.tvStore);
        tvPrdAmount=(TextView) findViewById(R.id.tvPrdAmount);
        tvQtyValue=(TextView) findViewById(R.id.tvQtyValue);
        etSearch=(EditText)findViewById(R.id.etSearch);
        imcart=(ImageView) findViewById(R.id.imcart);
        ivQtyMinus=(ImageView) findViewById(R.id.ivQtyMinus);
        ivQtyAdd=(ImageView) findViewById(R.id.ivQtyAdd);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        im = (ImageView) findViewById(R.id.im);
        ivFavourites = (ImageView) findViewById(R.id.ivFavourites);
        imaddcart = (ImageView) findViewById(R.id.imaddcart);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvaddtocart = (TextView) findViewById(R.id.tvaddtocart);
        tvexit = (TextView) findViewById(R.id.tvexit);
        tvcart  = (TextView) findViewById(R.id.tvcart);
        iv_headert = (TextView) findViewById(R.id.iv_headert);
        tvPrdMRPAmount  = (TextView) findViewById(R.id.tvPrdMRPAmount);
        tvPrdMRPAmountsaved = (TextView) findViewById(R.id.tvPrdMRPAmountsaved);
        tvaddcart = (TextView) findViewById(R.id.tvaddcart);
        tvFavourites = (TextView) findViewById(R.id.tvFavourites);
        tvqty = (TextView) findViewById(R.id.tvqty);
        tvdescptn = (TextView) findViewById(R.id.tvdescptn);
        tvdescptntitle = (TextView) findViewById(R.id.tvdescptntitle);
        rl_notification=(RelativeLayout) findViewById(R.id.rl_notification);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);
        rv_similarprdct = (RecyclerView) findViewById(R.id.rv_similarprdct);
        iv_itemimage = (ImageView) findViewById(R.id.iv_itemimage);
        bttoalamount=(TextView)findViewById(R.id.bttoalamount);
        bttoalsvdamount=(TextView)findViewById(R.id.bttoalsvdamount);
        llQty =(LinearLayout) findViewById(R.id.llQty);
        lladdQty=(LinearLayout) findViewById(R.id.lladdQty);

        txt_Quantity = (TextView) findViewById(R.id.txt_Quantity);
        txt_SimilarProducts = (TextView) findViewById(R.id.txt_SimilarProducts);
        viewline =  findViewById(R.id.viewline);
        tvtoalsvdamount =  findViewById(R.id.tvtoalsvdamount);
        tvtoalamount =  findViewById(R.id.tvtoalamount);

    }

    private void setRegister() {
        etSearch.setOnClickListener(this);
        etSearch.setKeyListener(null);
        imcart.setOnClickListener(this);
        im.setOnClickListener(this);
        ivFavourites.setOnClickListener(this);
        imaddcart.setOnClickListener(this);
        tvaddtocart.setOnClickListener(this);
        tvexit.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        tvFavourites.setOnClickListener(this);
        tvaddcart.setOnClickListener(this);
        cartChangedListener = this;
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        ivQtyMinus.setOnClickListener(this);
        ivQtyAdd.setOnClickListener(this);
        ivShare.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivShare:
                shareItemDetails();
                break;
            case R.id.im:

                //  drawer.openDrawer(Gravity.START);
                onBackPressed();
                break;
            case R.id.ivFavourites:
                doFavourites();
                break;
            case R.id.etSearch:
                startActivity(new Intent(ItemDetailsActivity.this,SearchActivity.class));
                break;
            case R.id.imcart:
            case R.id.tvcart:
                Intent i = new Intent(this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.imaddcart:
                doCart();
                cartChangedListener.onCartChanged();
                break;
            case R.id.tvaddcart:
                if(tvaddcart.getText().toString().equals(addtocart.getString("addtocart",null)))
                {
                    doCart();
                    cartChangedListener.onCartChanged();
                }
                else if(tvaddcart.getText().toString().equals("View Cart")){
                    Intent inn = new Intent(this, CartActivity.class);
                    inn.putExtra("From", "Home");
                    startActivity(inn);
                    //finish();
                }
                break;
            case R.id.tvFavourites:
                doFavourites();
                break;
            case R.id.tvaddtocart:
                //doCart();
                startActivity(new Intent(ItemDetailsActivity.this, CartActivity.class));
                break;
            case R.id.tvexit:
                finish();
                break;
            case R.id.tv_notification:
                startActivity(new Intent(ItemDetailsActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(ItemDetailsActivity.this, NotificationActivity.class));
                break;
            case R.id.ivQtyAdd:
                doAddQty();
                break;
            case R.id.ivQtyMinus:
                doMinusQty();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (From.equals("favourite")){
                Intent i = new Intent(this, FavouriteActivity.class);
                startActivity(i);
                finish();
            }
            else if (From.equals("cart")){
                Intent i = new Intent(this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                finish();
            }
            else if (From.equals("Search")){
                super.onBackPressed();
            }
            else if (From.equals("similarproducts")){
                super.onBackPressed();
            }else if (From.equals("Home")||From.equals("Home")||From.equals("HomeItem")||From.equals("HomeCatItem")){
                Intent i = new Intent(this, ItemListingActivity.class);
                i.putExtra("ID_CategorySecond", ID_CategorySecond);
                i.putExtra("SecondCategoryName", SecondCategoryName);
                i.putExtra("FirstCategoryName", FirstCategoryName);
                i.putExtra("ID_CategoryFirst", ID_CategoryFirst);
                i.putExtra("From", From);
                startActivity(i);
                finish();
            }
            else if (From.equals("HomeOffer") ){
                Intent i = new Intent(this, HomeActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
    private void doCart() {
        if(tvQtyValueloose.getText().toString().equals("")){

            SharedPreferences PleaseEnterQuantitysp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF370, 0);
            String PleaseEnterQuantity = PleaseEnterQuantitysp.getString("PleaseEnterQuantity","");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(PleaseEnterQuantity+".");
            builder.setNegativeButton(OK, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else{
            if (!isCart) {

                if (Packed.equals("false")) {
                    strQty = tvQtyValueloose.getText().toString();
                } else {
                    strQty = tvQtyValue.getText().toString();
                }
                SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                SharedPreferences Itemaddedtothecart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF337, 0);

                db.addtoCart(new CartModel(Id_Item, ItemName, SalesPrice, MRP, ID_Stock, pref2.getString("ID_Store", null),
                        RetailPrice, PrivilagePrice, WholesalePrice, GST, CESS, strQty, Packed, Description, ImageName, ItemMalayalamName));
                db.addtoCartCheck(new CheckCartModel(Id_Item, ID_Stock, CurrentStock, pref2.getString("ID_Store", null)));
                imaddcart.setImageResource(R.drawable.cart_icons);
                Toast.makeText(getApplicationContext(), ""+Itemaddedtothecart.getString("Itemaddedtothecart",null), Toast.LENGTH_SHORT).show();

//                Toast.makeText(getApplicationContext(), "Item added to the cart", Toast.LENGTH_SHORT).show();
                isCart = true;
                tvaddcart.setText("View Cart");

              /*  if(tvaddcart.getText().toString().equals("View Cart"))
                {
                    startActivity(new Intent(ItemDetailsActivity.this, CartActivity.class));
                }
                else
                {

                }*/
                //  onBackPressed();
            }
            else  if (isCart) {

                SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                SharedPreferences Itemaddedtothecart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF337, 0);


                db.updateCart(Integer.valueOf(Id_Item), tvQtyValue.getText().toString());
                imaddcart.setImageResource(R.drawable.cart_icons);
                Toast.makeText(getApplicationContext(), ""+Itemaddedtothecart.getString("Itemaddedtothecart",null), Toast.LENGTH_SHORT).show();

//                Toast.makeText(getApplicationContext(), "Item added to the cart", Toast.LENGTH_SHORT).show();
                isCart = true;
                tvaddcart.setText("View Cart");

             /*   db.deleteCartPrdouct(Id_Item);
                db.deleteCheckCart(Id_Item);
                imaddcart.setImageResource(R.drawable.shoppingcart);
                SharedPreferences Itemremovedfromthecart = getApplicationContext().getSharedPreferences(Config.SHARED_PREF339, 0);
                Toast.makeText(getApplicationContext(), ""+Itemremovedfromthecart.getString("Itemremovedfromthecart",null), Toast.LENGTH_SHORT).show();

//                Toast.makeText(getApplicationContext(), "Item removed from the cart", Toast.LENGTH_SHORT).show();
                isCart = false;
                onBackPressed();*/
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setHomeNavMenu() {
        final String[] menulist = new String[]{"Home","My Cart", "My Orders", "Favourites","Favourite Stores",
                "Notifications", "Shopping List","My Profile", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home, R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite, R.drawable.ic_store,
                R.drawable.ic_notifications, R.drawable.ic_list, R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ItemDetailsActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ItemDetailsActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ItemDetailsActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ItemDetailsActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ItemDetailsActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(ItemDetailsActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(ItemDetailsActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(ItemDetailsActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    startActivity(new Intent(ItemDetailsActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 8) {
                    startActivity(new Intent(ItemDetailsActivity.this, AboutUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 9) {
                    doLogout();
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 10) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "\t" + "Invite You  \n\n For Android Users \n http://play.google.com/store/apps/details?id=" + getPackageName() +
                            "\n" );
                    startActivity(Intent.createChooser(share, "Invite this App to your friends"));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }
    private void setHomeNavMenu1() {
        final String[] menulist = new String[]{"Home","My Cart", "Track Orders", "Favourite Items",
                "Notifications","My Profile", "About Us", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home, R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications, R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ItemDetailsActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ItemDetailsActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ItemDetailsActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ItemDetailsActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ItemDetailsActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(ItemDetailsActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 5) {
                    startActivity(new Intent(ItemDetailsActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(ItemDetailsActivity.this, AboutUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    doLogout();
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 8) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "\t" + "Invite You  \n\n For Android Users \n http://play.google.com/store/apps/details?id=" + getPackageName() +
                            "\n" );
                    startActivity(Intent.createChooser(share, "Invite this App to your friends"));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 9) {
                    startActivity(new Intent(ItemDetailsActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 10) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailsActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) ItemDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View layout = inflater1.inflate(R.layout.rate_us_pop_up, null);
                        final Button rateUs = layout.findViewById(R.id.rate_us);
                        builder.setView(layout);

                        final AlertDialog alertDialog = builder.create();
                        rateUs.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog.dismiss();
                                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                                try {
                                    startActivity(goToMarket);
                                } catch (ActivityNotFoundException e) {
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                                }
                            }
                        });
                        alertDialog.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                else if (position == 11) {
                    startActivity(new Intent(ItemDetailsActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(ItemDetailsActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(ItemDetailsActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 14) {
                    startActivity(new Intent(ItemDetailsActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailsActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ItemDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.logout_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);
            TextView tv_popupdelete= (TextView) layout.findViewById(R.id.tv_popupdelete);
            TextView tv_popupdeleteinfo3= (TextView) layout.findViewById(R.id.tv_popupdeleteinfo3);
            TextView edit= (TextView) layout.findViewById(R.id.edit);
            TextView tv_no= (TextView) layout.findViewById(R.id.tv_no);



            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();

            SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF241, 0);
            tv_popupdelete.setText(pref4.getString("Logoutaccount", null));

            SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF242, 0);
            tv_popupdeleteinfo3.setText(pref5.getString("areyousureyouwanttologout", null));

            SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF186, 0);
            edit.setText(pref6.getString("yes", null));

            SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF187, 0);
            tv_no.setText(pref7.getString("no", null));

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    dologoutchanges();


                    if(getResources().getString(R.string.app_name).equals("JZT CART") || getResources().getString(R.string.app_name).equals("Pulikkottil Hypermarket") || getResources().getString(R.string.app_name).equals("NeethiMed")|| getResources().getString(R.string.app_name).equals("Touch n Buy")) {

                        startActivity(new Intent(ItemDetailsActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(ItemDetailsActivity.this,LocationActivity.class));
                        finish();}
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }

                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void dologoutchanges(){
        Config.logOut(this);
    }

    private void doFavourites() {
        if (!isFavorite) {
            SharedPreferences Itemaddedtothefavourites = getApplicationContext().getSharedPreferences(Config.SHARED_PREF332, 0);
            db.addtoFav(new FavModel(Id_Item, ItemName, SalesPrice, MRP, ID_Stock, CurrentStock
                    , RetailPrice, PrivilagePrice, WholesalePrice, GST, CESS, Packed, Description, ImageName,ItemMalayalamName));
            ivFavourites.setImageResource(R.drawable.ic_favorite_fill);
            Toast.makeText(getApplicationContext(), Itemaddedtothefavourites.getString("Itemaddedtothefavourites",""), Toast.LENGTH_SHORT).show();
            isFavorite = true;
        } else {
            db.deleteFavPrdouct(Id_Item);
            ivFavourites.setImageResource(R.drawable.ic_favoritegrey);
            SharedPreferences Itemremovedfromthefavourites = getApplicationContext().getSharedPreferences(Config.SHARED_PREF338, 0);
            Log.e(TAG,"8322    "+Itemremovedfromthefavourites.getString("Itemremovedfromthefavourites",""));
            Toast.makeText(getApplicationContext(), ""+Itemremovedfromthefavourites.getString("Itemremovedfromthefavourites",null), Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(), "Item removed from the favourites", Toast.LENGTH_SHORT).show();
            isFavorite = false;
        }
    }


    @Override
    public void onCartChanged() {
        db=new DBHandler(this);
        tvcart.setText(String.valueOf(db.selectCartCount()));

        String string = "\u20B9";
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
        } catch (UnsupportedEncodingException e) {e.printStackTrace();}
        DecimalFormat f = new DecimalFormat("##.00");
        subtotal=db.selectCartTotalActualPrice();
        totalgst=db.CartTotalGST();
        totalMRP=db.CartTotalMRP();
        totalRetailPrice=db.CartTotalRetailPrice();
        discount=totalMRP-totalRetailPrice;
        memberdiscount=   totalRetailPrice-subtotal;
        aamountPay=subtotal+totalgst;
        yousaved=discount+memberdiscount;
        doubleTotalAmt=Double.parseDouble(String.valueOf(subtotal));
        SharedPreferences totalamount = getSharedPreferences(Config.SHARED_PREF131, 0);
        bttoalamount.setText(totalamount.getString("totalamount", "")+" : "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(subtotal))));
        SharedPreferences youhavesaved = getSharedPreferences(Config.SHARED_PREF212, 0);
        bttoalsvdamount.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+/*string+" "+*/f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");
        if (db.selectCartCount() > 0) {
            tvtoalamount.setText(totalamount.getString("totalamount", "")+" : "+string+""+f.format(Double.parseDouble(String.valueOf(subtotal))));
            tvtoalsvdamount.setText("( "+youhavesaved.getString("youhavesaved", "")+" "+string+" "+f.format(Double.parseDouble(String.valueOf(yousaved)))+" )");
        }
        else {
            tvtoalamount.setText("");
            tvtoalsvdamount.setText("");
        }
    }
    public void doAddQty(){
        try {
            counter = Integer.valueOf(tvQtyValue.getText().toString()) + 1;
            tvQtyValue.setText("" + counter);
            if(db.updateCart(Integer.valueOf(Id_Item), tvQtyValue.getText().toString())){} else{}
        } catch (Exception e) {
            e.printStackTrace();
        }cartChangedListener.onCartChanged();
    }

    public void doMinusQty(){
        try {
            if(Integer.valueOf(tvQtyValue.getText().toString())>=2) {
                counter = Integer.valueOf(tvQtyValue.getText().toString()) - 1;
                tvQtyValue.setText("" + counter);
                if(db.updateCart(Integer.valueOf(Id_Item),tvQtyValue.getText().toString())){} else{}
            }else {}
        } catch (Exception e) {
            e.printStackTrace();
        }cartChangedListener.onCartChanged();
    }

    private void getSimilarItemList() {
        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);
        if (new InternetUtil(this).isInternetOn()) {
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
                    SharedPreferences pref1 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF6, 0);
                    SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                    SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF5, 0);
                    requestObject1.put("ReqMode", "4");
                    requestObject1.put("IS_Relative", 1);
                    requestObject1.put("ID_Items", Id_Item);
                    requestObject1.put("ShopType", "2");
                    requestObject1.put("FK_CustomerPlus", pref1.getString("FK_CustomerPlus", null));
                    requestObject1.put("ID_Store", pref2.getString("ID_Store", null));
                    requestObject1.put("MemberId", pref3.getString("memberid", null));
                    requestObject1.put("Bank_Key", getResources().getString(R.string.BankKey));
                    SharedPreferences IDLanguages = getApplicationContext().getSharedPreferences(Config.SHARED_PREF80, 0);
                    requestObject1.put("ID_Languages",IDLanguages.getString("ID_Languages", null));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), requestObject1.toString());
                Call<String> call = apiService.getItemList(body);
                call.enqueue(new Callback<String>() {
                    @Override public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        try {
                            JSONObject jObject = new JSONObject(response.body());
                            JSONObject jobj = jObject.getJSONObject("ItemListSearchInfo");
                            JSONArray jarray = jobj.getJSONArray("ItemListInfo");
                       /* LinearLayoutManager layoutManager
                                = new LinearLayoutManager(ItemDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
                        rv_similarprdct.setLayoutManager(layoutManager);
                        SimilarItemListAdapter adapter = new SimilarItemListAdapter(ItemDetailsActivity.this, jarray);
                        rv_similarprdct.setAdapter(adapter);*/


                            GridLayoutManager lLayout = new GridLayoutManager(getApplicationContext(),2);
                            lLayout.setOrientation(GridLayoutManager.VERTICAL);
                            rv_similarprdct.setLayoutManager(lLayout);
                            rv_similarprdct.setHasFixedSize(true);
                            SimilarItemListAdapter adapter = new SimilarItemListAdapter(ItemDetailsActivity.this, jarray);

                            rv_similarprdct.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
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

    private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
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

    @Override
    public void onRestart(){
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected( MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_category:
                    Intent io = new Intent(ItemDetailsActivity.this, OutShopActivity.class);
                    io.putExtra(fromCategory, valueCategory);
                    finish();
                    startActivity(io);
                    return true;
                case R.id.navigation_cart:
                    Log.e(TAG,"navigation_cart    ");
//                    Intent ic = new Intent(ItemDetailsActivity.this, CartActivity.class);
//                    ic.putExtra(fromCart, valueCart);
//                    startActivity(ic);
//                    finish();
                    onBackPressed();
                    return true;
                case R.id.navigation_home:
                    Log.e(TAG,"navigation_home    ");
                    Intent iha = new Intent(ItemDetailsActivity.this, HomeActivity.class);
                    iha.putExtra(fromFavorite, valueFavorite);
                    startActivity(iha);
                    return true;
                case R.id.navigation_favorite:
                    Log.e(TAG,"navigation_favorite    ");
                    Intent ifa = new Intent(ItemDetailsActivity.this, FavouriteActivity.class);
                    ifa.putExtra(fromFavorite, valueFavorite);
                    startActivity(ifa);
                    return true;

                case R.id.navigation_quit:
                    Log.e(TAG,"navigation_quit    ");
                    //quitapp();
                    showBackPopup();
                    return true;
            }

            //navigation.setSelectedItemId(R.id.navigation_home);
            return false;
        }
    };


    private void showBackPopup() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailsActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ItemDetailsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.exit_back_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);

            TextView tv_popupdelete = (TextView) layout.findViewById(R.id.tv_popupdelete);
            TextView tv_popupdeleteinfo3 = (TextView) layout.findViewById(R.id.tv_popupdeleteinfo3);
            TextView edit = (TextView) layout.findViewById(R.id.edit);
            TextView tvno = (TextView) layout.findViewById(R.id.tvno);



            builder.setView(layout);
            final AlertDialog alertDialog = builder.create();
            SharedPreferences pref4 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF184, 0);
            tv_popupdelete.setText(pref4.getString("Confirmexit", null));

            SharedPreferences pref5 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF185, 0);
            tv_popupdeleteinfo3.setText(pref5.getString("areyousureyouwanttoexit", null));

            SharedPreferences pref6 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF186, 0);
            edit.setText(pref6.getString("yes", null));

            SharedPreferences pref7 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF187, 0);
            tvno.setText(pref7.getString("no", null));


            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    finishAffinity();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        finishAffinity();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quantityEdition(EditText editText, String ID_Items) {

        SharedPreferences Itemquantityupdatedtothecart = ItemDetailsActivity.this.getSharedPreferences(Config.SHARED_PREF335, 0);
        SharedPreferences Pleaseupdatequantitytothecart = ItemDetailsActivity.this.getSharedPreferences(Config.SHARED_PREF336, 0);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (db.doesRecordExistOrder(Id_Item)){
                    if(s.length() != 0) {
                        if (s.toString().equals("00")||s.toString().equals("000")||s.toString().equals("0000")||s.toString().equals("00000")||s.toString().equals("000000")){
                            editText.setText("0");
                            db.updateCart(Integer.valueOf(ID_Items), editText.getText().toString());
//                            Toast.makeText(ItemDetailsActivity.this, "Item quantity updated to the reorder", Toast.LENGTH_SHORT).show();
                            Toast.makeText(ItemDetailsActivity.this, Itemquantityupdatedtothecart.getString("Itemquantityupdatedtothecart", null), Toast.LENGTH_SHORT).show();
                            cartChangedListener.onCartChanged();
                        }
                        else{
                            db.updateCart(Integer.valueOf(ID_Items), editText.getText().toString());
                            Toast.makeText(ItemDetailsActivity.this, Itemquantityupdatedtothecart.getString("Itemquantityupdatedtothecart", null), Toast.LENGTH_SHORT).show();

//                            Toast.makeText(ItemDetailsActivity.this, "Item quantity updated to the reorder", Toast.LENGTH_SHORT).show();
                            cartChangedListener.onCartChanged();
                        }

                    }
                    else {
                        db.updateCart(Integer.valueOf(ID_Items), "0");
//                        Toast.makeText(ItemDetailsActivity.this, "Please updated quantity to the reorder.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ItemDetailsActivity.this, Pleaseupdatequantitytothecart.getString("Pleaseupdatequantitytothecart", null), Toast.LENGTH_SHORT).show();

                    }
                    cartChangedListener.onCartChanged();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });
    }

    private void shareItemDetails(){
//        Log.e(TAG,"ItemName  1555  "+in.getStringExtra("ItemName"));
//        ItemName = in.getStringExtra("ItemName");
//        MRP = in.getStringExtra("MRP");
//        SalesPrice = in.getStringExtra("SalesPrice");
//        Id_Item = in.getStringExtra("Id_Item");
//        ID_Stock = in.getStringExtra("ID_Stock");
//        CurrentStock = in.getStringExtra("CurrentStock");
//        RetailPrice = in.getStringExtra("RetailPrice");
//        PrivilagePrice = in.getStringExtra("PrivilagePrice");
//        WholesalePrice = in.getStringExtra("WholesalePrice");
//        Description = in.getStringExtra("Description");
//        ImageName = in.getStringExtra("ImageName");
//        GST = in.getStringExtra("GST");
//        CESS = in.getStringExtra("CESS");
//        From = in.getStringExtra("from");
//        Packed = in.getStringExtra("Packed");
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        share.putExtra(Intent.EXTRA_SUBJECT,  "Item Details." );
        share.putExtra(Intent.EXTRA_TEXT, ItemName + "\n\nMRP : "+ MRP+ "\n\nSales Price : "+ SalesPrice );
        startActivity(Intent.createChooser(share, "Item Details"));
        drawer.closeDrawer(GravityCompat.START);
    }

}