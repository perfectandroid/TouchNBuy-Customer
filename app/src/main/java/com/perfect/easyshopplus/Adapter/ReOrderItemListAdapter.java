package com.perfect.easyshopplus.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.perfect.easyshopplus.Activity.ReOrderCartActivity;
import com.perfect.easyshopplus.Activity.ReOrderItemListingActivity;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.Model.CartModel;
import com.perfect.easyshopplus.Model.CheckCartModel;
import com.perfect.easyshopplus.Model.FavModel;
import com.perfect.easyshopplus.Model.ReorderCartModel;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.CartChangedListener;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

public class ReOrderItemListAdapter extends RecyclerView.Adapter {

    JSONArray jsonArray;
    JSONObject jsonObject=null;
    Context context;
    String  PrdctName,MRP,SalesPrice,Id_Item,ID_Stock,CurrentStock;
    String Id_order, orderDate, MinimumDeliveryAmount,deliveryDate, status, order_id, ShopType, ID_Store, itemcount, ID_SalesOrder, storeName,DeliveryCharge,OrderType, ID_CustomerAddress;

    DBHandler db;
    boolean isCart;
    boolean isFavorite;
    CartChangedListener cartChangedListener;
    int counter= 1;
    Boolean isInCart;

    public ReOrderItemListAdapter(Context context, JSONArray jsonArray,
                                  String ID_SalesOrder, String order_id, String deliveryDate,
                                  String Id_order, String orderDate,
                                  String status, String ID_Store, String ShopType,
                                  String itemcount,String ID_CustomerAddress,String OrderType,
                                  String storeName,String DeliveryCharge, String MinimumDeliveryAmount) {



        this.context=context;
        this.jsonArray=jsonArray;
        this.ID_Store=ID_Store;
        this.ID_CustomerAddress=ID_CustomerAddress;
        this.OrderType=OrderType;
        this.DeliveryCharge=DeliveryCharge;
        this.ShopType=ShopType;
        this.ID_SalesOrder=ID_SalesOrder;
        this.Id_order=Id_order;
        this.orderDate=orderDate;
        this.deliveryDate=deliveryDate;
        this.status=status;
        this.order_id=order_id;
        this.itemcount=itemcount;
        this.storeName=storeName;
        this.MinimumDeliveryAmount=MinimumDeliveryAmount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.activity_reorder_item_list, parent, false);
        vh = new MainViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        try {




            jsonObject=jsonArray.getJSONObject(position);
            DecimalFormat f = new DecimalFormat("##.00");
            if (holder instanceof MainViewHolder) {



                SharedPreferences AddToOrder = context.getSharedPreferences(Config.SHARED_PREF283, 0);
                String strAddToOrder=AddToOrder.getString("AddToOrder","");
                ((MainViewHolder)holder).btAddtoOrder.setText(strAddToOrder);
                ((MainViewHolder)holder).btlooseAddtoOrder.setText(strAddToOrder);

                SharedPreferences qty = context.getSharedPreferences(Config.SHARED_PREF130, 0);
                String strqty=qty.getString("qty","");
                ((MainViewHolder)holder).tvqty.setText(strqty+" : ");
                ((MainViewHolder)holder).tvQty.setText(strqty+" : ");

                SharedPreferences Outofstock = context.getSharedPreferences(Config.SHARED_PREF116, 0);
                String strOutofstock=Outofstock.getString("Outofstock","");
                ((MainViewHolder)holder).txtStock.setText(strOutofstock);

                SharedPreferences baseurlpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
                SharedPreferences imgpref = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
                String BASEURL = baseurlpref.getString("BaseURL", null);
                final String IMAGEURL = imgpref.getString("ImageURL", null);
                String imagepath=IMAGEURL + jsonObject.getString("ImageName");
//                Glide.with(context)
//                        .load(imagepath)
//                        .placeholder(R.drawable.items)
//                        .error(R.drawable.items)
//                        .into(((MainViewHolder) holder).iv_itemimage);

                PicassoTrustAll.getInstance(context).load(imagepath).error(R.drawable.items).into(((MainViewHolder) holder).iv_itemimage);
                PrdctName=jsonObject.getString("ItemName");
                MRP=jsonObject.getString("MRP");
                SalesPrice=jsonObject.getString("SalesPrice");
                Id_Item=jsonObject.getString("ID_Items");
                ID_Stock=jsonObject.getString("ID_Stock");
                CurrentStock=jsonObject.getString("CurrentStock");

                ((MainViewHolder)holder).tvQtyValueloose.setText("1");

                if(jsonObject.getInt("CurrentStock")==0){
                    ((MainViewHolder)holder).txtStock.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).llQty.setVisibility(View.GONE);
                    ((MainViewHolder)holder).lladdQty.setVisibility(View.GONE);
                    ((MainViewHolder)holder).btAddtoOrder.setVisibility(View.GONE);
                    ((MainViewHolder)holder).btlooseAddtoOrder.setVisibility(View.GONE);
                }else{
                    ((MainViewHolder)holder).txtStock.setVisibility(View.GONE);
                    if(jsonObject.getString("Packed").equals("false")){
                        ((MainViewHolder)holder).llQty.setVisibility(View.VISIBLE);
                        ((MainViewHolder)holder).lladdQty.setVisibility(View.GONE);
                        ((MainViewHolder)holder).btAddtoOrder.setVisibility(View.GONE);
                        ((MainViewHolder)holder).btlooseAddtoOrder.setVisibility(View.VISIBLE);
                    }else{
                        ((MainViewHolder)holder). llQty.setVisibility(View.GONE);
                        ((MainViewHolder)holder).lladdQty.setVisibility(View.VISIBLE);
                        ((MainViewHolder)holder).btAddtoOrder.setVisibility(View.VISIBLE);
                        ((MainViewHolder)holder).btlooseAddtoOrder.setVisibility(View.GONE);
                    }
                }

                db = new DBHandler(context);
                isCart=db.checkIcartItem(Id_Item);
                isFavorite=db.checkIfavItem(Id_Item);
                if (!isCart) {
                    ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticongrey);
                } else {
                    ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticonred);
                }
                if (!isFavorite) {
                    ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_border);
                } else {
                    ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_fill);
                }
                ((MainViewHolder)holder).tvPrdName.setText(PrdctName);
                String string = "\u20B9";
                byte[] utf8 = new byte[0];
                try {
                    utf8 = string.getBytes("UTF-8");
                    string = new String(utf8, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }


                ((MainViewHolder)holder).tvPrdAmount.setText(/*string+" "+*/f.format(Double.parseDouble(SalesPrice)));
                ((MainViewHolder)holder).tvPrdMRPAmount.setPaintFlags(((MainViewHolder)holder).tvPrdMRPAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                double yousaved=Float.parseFloat(MRP)-Float.parseFloat(SalesPrice);
                if(yousaved<=0){
                    ((MainViewHolder)holder).tvPrdMRPAmount.setVisibility(View.GONE);
                    ((MainViewHolder)holder).tvPrdMRPAmountsaved.setVisibility(View.GONE);
                }else {
                    ((MainViewHolder)holder).tvPrdMRPAmount.setVisibility(View.VISIBLE);
                    ((MainViewHolder)holder).tvPrdMRPAmountsaved.setVisibility(View.VISIBLE);
                }

                SharedPreferences yousavedsp = context.getSharedPreferences(Config.SHARED_PREF117, 0);
                String stryousaved=yousavedsp.getString("yousaved","");
                SharedPreferences MRPsp = context.getSharedPreferences(Config.SHARED_PREF115, 0);
                String strMRP=MRPsp.getString("MRP","");


                ((MainViewHolder)holder).tvPrdMRPAmount.setText(strMRP+" : "+/*string+" "+*/f.format(Double.parseDouble(MRP)));
                ((MainViewHolder)holder).tvPrdMRPAmountsaved.setText(stryousaved+" "/*+string+" "*/+f.format(yousaved));
                ((MainViewHolder) holder).btAddtoOrder.setTag(position);
                ((MainViewHolder) holder).btAddtoOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject=jsonArray.getJSONObject(position);
                            DBHandler db=new DBHandler(context);
                            isInCart= db.doesRecordExistReorder( jsonObject.getString("ID_Items"));
                            if(!isInCart){
                                db.addtoReorderCart(new ReorderCartModel(
                                        jsonObject.getString("ID_Items"),
                                        jsonObject.getString("ItemName"),
                                        jsonObject.getString("SalesPrice"),
                                        jsonObject.getString("MRP"),
                                        jsonObject.getString("ID_Stock"),
                                        jsonObject.getString("CurrentStock"),
                                        jsonObject.getString("RetailPrice"),
                                        jsonObject.getString("PrivilagePrice"),
                                        jsonObject.getString("WholesalePrice"),
                                        jsonObject.getString("GST"),
                                        jsonObject.getString("CESS"),
                                        ((MainViewHolder)holder).tvQtyValue.getText().toString(),
                                        jsonObject.getString("Packed"),
                                        jsonObject.getString("Description"),
                                        IMAGEURL + jsonObject.getString("ImageName")));

                                SharedPreferences ItemaddedtotheReorder = context.getSharedPreferences(Config.SHARED_PREF284, 0);
                                String strItemaddedtotheReorder=ItemaddedtotheReorder.getString("ItemaddedtotheReorder","");
                                Toast.makeText(context, ""+strItemaddedtotheReorder, Toast.LENGTH_SHORT).show();
//                                Intent in=new Intent(context, ReOrderCartActivity.class);
//                                in.putExtra("ID_Store", ID_Store);
//                                in.putExtra("ShopType", ShopType);
//                                in.putExtra("ID_SalesOrder", ID_SalesOrder);
//                                in.putExtra("ID_CustomerAddress", ID_CustomerAddress);
//                                in.putExtra("OrderType", OrderType);
//                                in.putExtra("DeliveryCharge", DeliveryCharge);
//                                context.startActivity(in);
//                                ((Activity)context).finish();
                            }
                            else {
                                db.updateReorderCart(Integer.valueOf(jsonObject.getString("ID_Items")), ((MainViewHolder)holder).tvQtyValue.getText().toString());

                                SharedPreferences Itemupdatedtothereorder = context.getSharedPreferences(Config.SHARED_PREF309, 0);
                                String strItemupdatedtothereorderr=Itemupdatedtothereorder.getString("Itemupdatedtothereorder","");
                                Toast.makeText(context, ""+strItemupdatedtothereorderr, Toast.LENGTH_SHORT).show();
//                                Intent in=new Intent(context, ReOrderCartActivity.class);
//                                in.putExtra("ID_Store", ID_Store);
//                                in.putExtra("ShopType", ShopType);
//                                in.putExtra("ID_SalesOrder", ID_SalesOrder);
//                                in.putExtra("ID_CustomerAddress", ID_CustomerAddress);
//                                in.putExtra("OrderType", OrderType);
//                                in.putExtra("DeliveryCharge", DeliveryCharge);
//                                context.startActivity(in);
//                                ((Activity)context).finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((ReOrderItemListingActivity)context).hideKeyboard();

                    }
                });
                ((MainViewHolder) holder).btlooseAddtoOrder.setTag(position);
                ((MainViewHolder) holder).btlooseAddtoOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject=jsonArray.getJSONObject(position);
                            DBHandler db=new DBHandler(context);
                            isInCart= db.doesRecordExistReorder( jsonObject.getString("ID_Items"));
                            if(!isInCart){
                                db.addtoReorderCart(new ReorderCartModel(
                                        jsonObject.getString("ID_Items"),
                                        jsonObject.getString("ItemName"),
                                        jsonObject.getString("SalesPrice"),
                                        jsonObject.getString("MRP"),
                                        jsonObject.getString("ID_Stock"),
                                        jsonObject.getString("CurrentStock"),
                                        jsonObject.getString("RetailPrice"),
                                        jsonObject.getString("PrivilagePrice"),
                                        jsonObject.getString("WholesalePrice"),
                                        jsonObject.getString("GST"),
                                        jsonObject.getString("CESS"),
                                        ((MainViewHolder)holder).tvQtyValueloose.getText().toString(),
                                        jsonObject.getString("Packed"),
                                        jsonObject.getString("Description"),
                                        IMAGEURL + jsonObject.getString("ImageName")));

                                SharedPreferences ItemaddedtotheReorder = context.getSharedPreferences(Config.SHARED_PREF284, 0);
                                String strItemaddedtotheReorder=ItemaddedtotheReorder.getString("ItemaddedtotheReorder","");
                                Toast.makeText(context, ""+strItemaddedtotheReorder, Toast.LENGTH_SHORT).show();
//                                Intent in=new Intent(context, ReOrderCartActivity.class);
//                                in.putExtra("ID_Store", ID_Store);
//                                in.putExtra("ShopType", ShopType);
//                                in.putExtra("ID_SalesOrder", ID_SalesOrder);
//                                in.putExtra("ID_CustomerAddress", ID_CustomerAddress);
//                                in.putExtra("OrderType", OrderType);
//                                in.putExtra("DeliveryCharge", DeliveryCharge);
//                                context.startActivity(in);
//                                ((Activity)context).finish();
                            }
                            else {
                                db.updateReorderCart(Integer.valueOf(jsonObject.getString("ID_Items")), ((MainViewHolder)holder).tvQtyValueloose.getText().toString());

                                SharedPreferences Itemupdatedtothereorder = context.getSharedPreferences(Config.SHARED_PREF309, 0);
                                String strItemupdatedtothereorderr=Itemupdatedtothereorder.getString("Itemupdatedtothereorder","");
                                Toast.makeText(context, ""+strItemupdatedtothereorderr, Toast.LENGTH_SHORT).show();
//                                Intent in=new Intent(context, ReOrderCartActivity.class);
//                                in.putExtra("ID_Store", ID_Store);
//                                in.putExtra("ShopType", ShopType);
//                                in.putExtra("ID_SalesOrder", ID_SalesOrder);
//                                in.putExtra("ID_CustomerAddress", ID_CustomerAddress);
//                                in.putExtra("OrderType", OrderType);
//                                in.putExtra("DeliveryCharge", DeliveryCharge);
//                                context.startActivity(in);
//                                ((Activity)context).finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((ReOrderItemListingActivity)context).hideKeyboard();

                    }
                });
                ((MainViewHolder) holder).ivFavourates.setTag(position);
                ((MainViewHolder) holder).ivFavourates.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            db = new DBHandler(context);
                            isFavorite=db.checkIfavItem(jsonObject.getString("ID_Items"));
                            if (!isFavorite) {
                                db.addtoFav(new FavModel(jsonObject.getString("ID_Items"), jsonObject.getString("ItemName"),
                                        jsonObject.getString("SalesPrice"),jsonObject.getString("MRP"),
                                        jsonObject.getString("ID_Stock"),jsonObject.getString("CurrentStock"),
                                        jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"), jsonObject.getString("WholesalePrice")
                                        , jsonObject.getString("GST"), jsonObject.getString("CESS"),
                                        jsonObject.getString("Packed"),  jsonObject.getString("Description"),
                                        jsonObject.getString("ImageName"),jsonObject.getString("ItemMalayalamName")));
                                ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_fill);
                                Toast.makeText(context, "Item added to the favourites", Toast.LENGTH_SHORT).show();
                                isFavorite = true;
                            } else {
                                db.deleteFavPrdouct(jsonObject.getString("ID_Items"));
                                ((MainViewHolder)holder).ivFavourates.setImageResource(R.drawable.ic_favorite_border);
                                Toast.makeText(context, "Item removed from the favourites", Toast.LENGTH_SHORT).show();
                                isFavorite = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ReOrderItemListingActivity)context).hideKeyboard();

                    }
                });
                ((MainViewHolder) holder).ivCart.setTag(position);
                ((MainViewHolder) holder).ivCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            SharedPreferences pref2 = context.getApplicationContext().getSharedPreferences(Config.SHARED_PREF7, 0);
                            jsonObject = jsonArray.getJSONObject(position);
                            db = new DBHandler(context);
                            isCart=db.checkIcartItem(jsonObject.getString("ID_Items"));
                            if (!isCart) {
                                db.addtoCart(new CartModel(jsonObject.getString("ID_Items"), jsonObject.getString("ItemName"), jsonObject.getString("SalesPrice"),jsonObject.getString("MRP"), jsonObject.getString("ID_Stock"), jsonObject.getString("CurrentStock"),
                                        jsonObject.getString("RetailPrice"), jsonObject.getString("PrivilagePrice"), jsonObject.getString("WholesalePrice")
                                        , jsonObject.getString("GST"), jsonObject.getString("CESS"),"1", jsonObject.getString("Packed"),jsonObject.getString("Description"), "",jsonObject.getString("ItemMalayalamName")));
                                db.addtoCartCheck(new CheckCartModel(jsonObject.getString("ID_Items"), jsonObject.getString("ID_Stock"), jsonObject.getString("CurrentStock"),pref2.getString("ID_Store", null)));
                                ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticonred);
                                Toast.makeText(context, "Item added to the cart", Toast.LENGTH_SHORT).show();
                                isCart = true;
                            } else {
                                db.deleteCartPrdouct(jsonObject.getString("ID_Items"));
                                db.deleteCheckCart(jsonObject.getString("ID_Items"));
                                ((MainViewHolder)holder).ivCart.setImageResource(R.drawable.carticongrey);
                                Toast.makeText(context, "Item removed from the cart", Toast.LENGTH_SHORT).show();
                                isCart = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cartChangedListener.onCartChanged();
                        ((ReOrderItemListingActivity)context).hideKeyboard();

                    }
                });
                ((MainViewHolder)holder).ivQtyAdd.setTag(position);
                ((MainViewHolder)holder).ivQtyAdd.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            counter = Integer.valueOf(((MainViewHolder)holder).tvQtyValue.getText().toString()) + 1;
                            ((MainViewHolder)holder).tvQtyValue.setText("" + counter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((ReOrderItemListingActivity)context).hideKeyboard();

                    }
                });
                ((MainViewHolder)holder).ivQtyMinus.setTag(position);
                ((MainViewHolder)holder).ivQtyMinus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            jsonObject = jsonArray.getJSONObject(position);
                            if(Integer.valueOf(((MainViewHolder)holder).tvQtyValue.getText().toString())>=2) {
                                counter = Integer.valueOf(((MainViewHolder)holder).tvQtyValue.getText().toString()) - 1;
                                ((MainViewHolder)holder).tvQtyValue.setText("" + counter);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((ReOrderItemListingActivity)context).hideKeyboard();

                    }
                });
                jsonObject = jsonArray.getJSONObject(position);



//                quantityEdition(((MainViewHolder)holder).tvQtyValueloose,jsonObject.getString("ID_Items"));


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        LinearLayout lnLayout, lladdQty, llQty;
        TextView tvPrdName, txtStock, tvoutofstock , btAddtoOrder,btlooseAddtoOrder, tvqty, tvQty;
        TextView tvPrdAmount, tvPrdMRPAmountsaved, tvPrdMRPAmount, tvQtyValue;
        ImageView ivFavourates,ivCart,ivQtyAdd, ivQtyMinus,iv_itemimage, ivQtyAddloose ;
        EditText tvQtyValueloose;

        public MainViewHolder(View v) {
            super(v);
            cartChangedListener = ReOrderCartActivity.cartChangedListener;
            lnLayout=(LinearLayout) v.findViewById(R.id.lnLayout);
            tvPrdName=(TextView)v.findViewById(R.id.tvPrdName);
            tvPrdAmount=(TextView)v.findViewById(R.id.tvPrdAmount);
            tvPrdMRPAmount=(TextView)v.findViewById(R.id.tvPrdMRPAmount);
            tvPrdMRPAmountsaved=(TextView)v.findViewById(R.id.tvPrdMRPAmountsaved);
            ivFavourates=(ImageView)v.findViewById(R.id.ivFavourates);
            ivCart=(ImageView)v.findViewById(R.id.ivCart);
            btAddtoOrder= v.findViewById(R.id.btAddtoOrder);
            tvqty= v.findViewById(R.id.tvqty);
            tvQty= v.findViewById(R.id.tvQty);
            btlooseAddtoOrder= v.findViewById(R.id.btlooseAddtoOrder);
            tvQtyValue = (TextView) v.findViewById(R.id.tvQtyValue);
            txtStock = (TextView) v.findViewById(R.id.txtStock);
//            ivQtyAdd =(ImageView)v.findViewById(R.id.ivQtyAdd);
//            ivQtyMinus =(ImageView)v.findViewById(R.id.ivQtyMinus);
            iv_itemimage =(ImageView)v.findViewById(R.id.iv_itemimage);




            lladdQty=(LinearLayout) v.findViewById(R.id.lladdQty);
            llQty=(LinearLayout) v.findViewById(R.id.llQty);
            tvoutofstock = (TextView) v.findViewById(R.id.tvoutofstock);

            ivQtyAdd =(ImageView)v.findViewById(R.id.ivQtyAdd);
            ivQtyMinus =(ImageView)v.findViewById(R.id.ivQtyMinus);

            ivQtyAddloose =(ImageView)v.findViewById(R.id.ivQtyAddloose);
            tvQtyValueloose =(EditText) v.findViewById(R.id.tvQtyValueloose);
        }
    }

}


