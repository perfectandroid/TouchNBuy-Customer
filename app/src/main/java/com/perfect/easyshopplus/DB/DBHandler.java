package com.perfect.easyshopplus.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.perfect.easyshopplus.Model.CartModel;
import com.perfect.easyshopplus.Model.CheckCartModel;
import com.perfect.easyshopplus.Model.FavModel;
import com.perfect.easyshopplus.Model.FavStoreModel;
import com.perfect.easyshopplus.Model.InshopCartModel;
import com.perfect.easyshopplus.Model.ReorderCartModel;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "productManager";
    String TAG = "DBHandler";

    private static final String TABLE_CART = "cart";
    private static final String KEY_ID_ITEMS= "ID_Items";
    private static final String KEY_ITEMNAME = "ItemName";
    private static final String KEY_SALESPRICE = "SalesPrice";
    private static final String KEY_MRP = "MRP";
    private static final String KEY_CART_QTY = "Cart_QTY";
    private static final String KEY_CART_STOCKID = "Stock_ID";
    private static final String KEY_CART_RETAILPRICE = "RetailPrice";
    private static final String KEY_CART_PRIVILAGEPRICE = "PrivilagePrice";
    private static final String KEY_CART_WHOLESALEPRICE = "WholesalePrice";
    private static final String KEY_CART_GST = "GST";
    private static final String KEY_CART_CESS = "CESS";
    private static final String KEY_CART_COUNT = "Count";
    private static final String KEY_CART_PACKED = "Packed";
    private static final String KEY_CART_Description = "Description";
    private static final String KEY_CART_ImageName = "ImageName";
    private static final String KEY_CART_ItemMalayalamName = "ItemMalayalamName";

    private static final String TABLE_REORDER_CART = "reordercart";
    private static final String KEY_REORDER_ID_ITEMS= "ID_Items";
    private static final String KEY_REORDER_ITEMNAME = "ItemName";
    private static final String KEY_REORDER_SALESPRICE = "SalesPrice";
    private static final String KEY_REORDER_MRP = "MRP";
    private static final String KEY_REORDER_CART_QTY = "Cart_QTY";
    private static final String KEY_REORDER_CART_STOCKID = "Stock_ID";
    private static final String KEY_REORDER_CART_RETAILPRICE = "RetailPrice";
    private static final String KEY_REORDER_CART_PRIVILAGEPRICE = "PrivilagePrice";
    private static final String KEY_REORDER_CART_WHOLESALEPRICE = "WholesalePrice";
    private static final String KEY_REORDER_CART_GST = "GST";
    private static final String KEY_REORDER_CART_CESS = "CESS";
    private static final String KEY_REORDER_CART_COUNT = "Count";
    private static final String KEY_REORDER_CART_PACKED = "Packed";
    private static final String KEY_REORDER_CART_Description = "Description";
    private static final String KEY_REORDER_CART_ImageName = "ImageName";

    private static final String TABLE_CHECK_ITEM = "CheckCartItem";
    private static final String KEY_CHECK_ID_ITEMS= "Itemid";
    private static final String KEY_CHECK_STOCKID = "StockId";
    private static final String KEY_CHECK_QTY = "QTY";
    private static final String KEY_CHECK_STOREID= "StoreId";

    private static final String TABLE_FAV = "fav";
    private static final String KEY_FAVID_ITEMS= "FAV_ID_Items";
    private static final String KEY_FAVITEMNAME = "FAV_ItemName";
    private static final String KEY_FAVSALESPRICE = "FAV_SalesPrice";
    private static final String KEY_FAVMRP = "FAV_MRP";
    private static final String KEY_FAV_STOCKID = "fav_Stock_ID";
    private static final String KEY_FAV_QTY = "FAV_QTY";
    private static final String KEY_FAV_RETAILPRICE = "FAV_RetailPrice";
    private static final String KEY_FAV_PRIVILAGEPRICE = "FAV_PrivilagePrice";
    private static final String KEY_FAV_WHOLESALEPRICE = "FAV_WholesalePrice";
    private static final String KEY_FAV_GST = "FAV_GST";
    private static final String KEY_FAV_CESS = "FAV_CESS";
    private static final String KEY_FAV_PACKED = "FAV_Packed";
    private static final String KEY_FAV_Description = "FAV_Description";
    private static final String KEY_FAV_ImageName = "FAV_ImageName";
    private static final String KEY_FAV_ItemMalayalamName = "FAV_ItemMalayalamName";

    private static final String TABLE_FAVSTORE = "favstore";
    private static final String KEY_FAVSTORE_ID_ITEMS= "FAVSTORE_ID_Items";
    private static final String KEY_FAVSTORE_ITEMNAME = "FAVSTORE_ItemName";
    private static final String KEY_FAVSTORE__ImageName = "FAVSTORE_ImageName";

    private static final String TABLE_INSHOP_CART = "Inshop_cart";
    private static final String KEY_INSHOP_ID_ITEMS= "Inshop_ID_Items";
    private static final String KEY_INSHOP_ITEMNAME = "Inshop_ItemName";
    private static final String KEY_INSHOP_SALESPRICE = "Inshop_SalesPrice";
    private static final String KEY_INSHOP_MRP = "Inshop_MRP";
    private static final String KEY_INSHOP_CART_QTY = "Inshop_Cart_QTY";
    private static final String KEY_INSHOP_CART_STOCKID = "Inshop_Stock_ID";
    private static final String KEY_INSHOP_CART_RETAILPRICE = "Inshop_RetailPrice";
    private static final String KEY_INSHOP_CART_PRIVILAGEPRICE = "Inshop_PrivilagePrice";
    private static final String KEY_INSHOP_CART_WHOLESALEPRICE = "Inshop_WholesalePrice";
    private static final String KEY_INSHOP_CART_GST = "Inshop_GST";
    private static final String KEY_INSHOP_CART_CESS = "Inshop_CESS";
    private static final String KEY_INSHOP_CART_COUNT = "Inshop_Count";
    private static final String KEY_INSHOP_CART_PACKED = "Inshop_Packed";
    private static final String KEY_INSHOP_CART_ImageName = "Inshop_ImageName";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CITY_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + /*KEY_CART_ID + " INTEGER PRIMARY KEY," +*/ KEY_ID_ITEMS + " TEXT,"
                + KEY_ITEMNAME + " TEXT,"
                + KEY_SALESPRICE + " TEXT,"
                + KEY_MRP + " TEXT,"
                + KEY_CART_STOCKID + " TEXT,"
                + KEY_CART_QTY + " TEXT,"
                + KEY_CART_RETAILPRICE + " TEXT,"
                + KEY_CART_PRIVILAGEPRICE + " TEXT,"
                + KEY_CART_WHOLESALEPRICE + " TEXT,"
                + KEY_CART_GST + " TEXT,"
                + KEY_CART_CESS + " TEXT,"
                + KEY_CART_COUNT + " TEXT,"
                + KEY_CART_PACKED + " TEXT,"
                + KEY_CART_Description + " TEXT,"
                + KEY_CART_ImageName + " TEXT,"
                + KEY_CART_ItemMalayalamName + " TEXT"
                + ")";
        db.execSQL(CREATE_CITY_TABLE);

        String CREATE_REORDER_TABLE = "CREATE TABLE " + TABLE_REORDER_CART + "("
                + KEY_REORDER_ID_ITEMS + " TEXT,"
                + KEY_REORDER_ITEMNAME + " TEXT,"
                + KEY_REORDER_SALESPRICE + " TEXT,"
                + KEY_REORDER_MRP + " TEXT,"
                + KEY_REORDER_CART_STOCKID + " TEXT,"
                + KEY_REORDER_CART_QTY + " TEXT,"
                + KEY_REORDER_CART_RETAILPRICE + " TEXT,"
                + KEY_REORDER_CART_PRIVILAGEPRICE + " TEXT,"
                + KEY_REORDER_CART_WHOLESALEPRICE + " TEXT,"
                + KEY_REORDER_CART_GST + " TEXT,"
                + KEY_REORDER_CART_CESS + " TEXT,"
                + KEY_REORDER_CART_COUNT + " TEXT,"
                + KEY_REORDER_CART_PACKED + " TEXT,"
                + KEY_REORDER_CART_Description + " TEXT,"
                + KEY_REORDER_CART_ImageName + " TEXT"
                + ")";
        db.execSQL(CREATE_REORDER_TABLE);

        String CREATE_CHECK_ITEM = "CREATE TABLE " + TABLE_CHECK_ITEM + "("
                + KEY_CHECK_ID_ITEMS + " TEXT,"
                + KEY_CHECK_STOCKID + " TEXT,"
                + KEY_CHECK_QTY + " TEXT,"
                + KEY_CHECK_STOREID + " TEXT"
                + ")";
        db.execSQL(CREATE_CHECK_ITEM);

        String CREATE_FAV_TABLE = "CREATE TABLE " + TABLE_FAV + "("
                + KEY_FAVID_ITEMS + " TEXT,"
                + KEY_FAVITEMNAME + " TEXT,"
                + KEY_FAVSALESPRICE + " TEXT,"
                + KEY_FAVMRP + " TEXT,"
                + KEY_FAV_STOCKID + " TEXT,"
                + KEY_FAV_QTY + " TEXT,"
                + KEY_FAV_RETAILPRICE + " TEXT,"
                + KEY_FAV_PRIVILAGEPRICE + " TEXT,"
                + KEY_FAV_WHOLESALEPRICE + " TEXT,"
                + KEY_FAV_GST + " TEXT,"
                + KEY_FAV_CESS + " TEXT,"
                + KEY_FAV_PACKED + " TEXT,"
                + KEY_FAV_Description + " TEXT,"
                + KEY_FAV_ImageName + " TEXT,"
                + KEY_FAV_ItemMalayalamName + " TEXT"
                + ")";
        db.execSQL(CREATE_FAV_TABLE);

        String CREATE_FAVSTORE_TABLE = "CREATE TABLE " + TABLE_FAVSTORE + "("
                + KEY_FAVSTORE_ID_ITEMS + " TEXT,"
                + KEY_FAVSTORE_ITEMNAME + " TEXT,"
                + KEY_FAVSTORE__ImageName + " TEXT"
                + ")";
        db.execSQL(CREATE_FAVSTORE_TABLE);

        String CREATE_INSHOPCART_TABLE = "CREATE TABLE " + TABLE_INSHOP_CART + "("
                + /*KEY_CART_ID + " INTEGER PRIMARY KEY," +*/ KEY_INSHOP_ID_ITEMS + " TEXT,"
                + KEY_INSHOP_ITEMNAME + " TEXT,"
                + KEY_INSHOP_SALESPRICE + " TEXT,"
                + KEY_INSHOP_MRP + " TEXT,"
                + KEY_INSHOP_CART_STOCKID + " TEXT,"
                + KEY_INSHOP_CART_QTY + " TEXT,"
                + KEY_INSHOP_CART_RETAILPRICE + " TEXT,"
                + KEY_INSHOP_CART_PRIVILAGEPRICE + " TEXT,"
                + KEY_INSHOP_CART_WHOLESALEPRICE + " TEXT,"
                + KEY_INSHOP_CART_GST + " TEXT,"
                + KEY_INSHOP_CART_CESS + " TEXT,"
                + KEY_INSHOP_CART_COUNT + " TEXT,"
                + KEY_INSHOP_CART_PACKED + " TEXT,"
                + KEY_INSHOP_CART_ImageName + " TEXT"
                + ")";
        db.execSQL(CREATE_INSHOPCART_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REORDER_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHECK_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAV);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVSTORE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSHOP_CART);
        onCreate(db);
    }

    /**
     * Generic method to insert values to a table.
     *
     * @param tableName     of the table to be inserted.
     * @param contentValues containing table values.
     * @return inserted row id of the table.
     */
    public long insert(String tableName, ContentValues contentValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(tableName, null, contentValues);
    }

    public void update(ContentValues cv, String tableName,
                       String fieldName, String ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        long s = db.update(tableName, cv, fieldName + "=" + ID, null);
    }

    public Cursor select(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM '" + tableName + "'", null);
        return cursor;
    }

    public Cursor selectc(String tableName, String iditem) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + tableName +" WHERE ID_Items = " + iditem +"", null);
        return cursor;
    }

    public boolean updateCart (Integer id, String Count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Count", Count);
        db.update("cart", contentValues, "ID_Items = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public void addtoCart(CartModel cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues civalues = new ContentValues();
        civalues.put(KEY_ID_ITEMS, cart.getID_Items());
        civalues.put(KEY_ITEMNAME, cart.getItemName());
        civalues.put(KEY_SALESPRICE, cart.getSalesPrice());
        civalues.put(KEY_MRP, cart.getMRP());
        civalues.put(KEY_CART_STOCKID, cart.getStock_ID());
        civalues.put(KEY_CART_QTY, cart.getQTY());
        civalues.put(KEY_CART_RETAILPRICE, cart.getRetailPrice());
        civalues.put(KEY_CART_PRIVILAGEPRICE, cart.getPrivilagePrice());
        civalues.put(KEY_CART_WHOLESALEPRICE, cart.getWholesalePrice());
        civalues.put(KEY_CART_GST, cart.getGST());
        civalues.put(KEY_CART_CESS, cart.getCESS());
        civalues.put(KEY_CART_COUNT, cart.getCount());
        civalues.put(KEY_CART_PACKED, cart.getPacked());
        civalues.put(KEY_CART_Description, cart.getDescription());
        civalues.put(KEY_CART_ImageName, cart.getImageName());
        civalues.put(KEY_CART_ItemMalayalamName, cart.getItemMalayalamName());
        db.insert(TABLE_CART, null, civalues);
        db.close();
    }

    public void addtoReorderCart(ReorderCartModel cart) {

        Log.e("addtoReorderCart","276    "+cart.getItemName());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues civalues = new ContentValues();
        civalues.put(KEY_REORDER_ID_ITEMS, cart.getID_Items());
        civalues.put(KEY_REORDER_ITEMNAME, cart.getItemName());
        civalues.put(KEY_REORDER_SALESPRICE, cart.getSalesPrice());
        civalues.put(KEY_REORDER_MRP, cart.getMRP());
        civalues.put(KEY_REORDER_CART_STOCKID, cart.getStock_ID());
        civalues.put(KEY_REORDER_CART_QTY, cart.getQTY());
        civalues.put(KEY_REORDER_CART_RETAILPRICE, cart.getRetailPrice());
        civalues.put(KEY_REORDER_CART_PRIVILAGEPRICE, cart.getPrivilagePrice());
        civalues.put(KEY_REORDER_CART_WHOLESALEPRICE, cart.getWholesalePrice());
        civalues.put(KEY_REORDER_CART_GST, cart.getGST());
        civalues.put(KEY_REORDER_CART_CESS, cart.getCESS());
        civalues.put(KEY_REORDER_CART_COUNT, cart.getCount());
        civalues.put(KEY_REORDER_CART_PACKED, cart.getPacked());
        civalues.put(KEY_REORDER_CART_Description, cart.getDescription());
        civalues.put(KEY_REORDER_CART_ImageName, cart.getImageName());
        db.insert(TABLE_REORDER_CART, null, civalues);
        db.close();
    }

    public List<CartModel> getAllCart() {
        List<CartModel> cartList = new ArrayList<CartModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_CART;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                CartModel cart = new CartModel();
                cart.setID_Items(cursor.getString(0));
                cart.setItemName(cursor.getString(1));
                cart.setSalesPrice(cursor.getString(2));
                cart.setMRP(cursor.getString(3));
                cart.setStock_ID(cursor.getString(4));
                cart.setQTY(cursor.getString(5));
                cart.setRetailPrice(cursor.getString(6));
                cart.setPrivilagePrice(cursor.getString(7));
                cart.setWholesalePrice(cursor.getString(8));
                cart.setGST(cursor.getString(9));
                cart.setCESS(cursor.getString(10));
                cart.setCount(cursor.getString(11));
                cart.setPacked(cursor.getString(12));
                cart.setDescription(cursor.getString(13));
                cart.setImageName(cursor.getString(14));
                cart.setItemMalayalamName(cursor.getString(15));
                cartList.add(cart);
            } while (cursor.moveToNext());
        }
        return cartList;
    }

    public List<ReorderCartModel> getAllReOrderCart() {
        List<ReorderCartModel> cartList = new ArrayList<ReorderCartModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_REORDER_CART;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                ReorderCartModel cart = new ReorderCartModel();
                cart.setID_Items(cursor.getString(0));
                cart.setItemName(cursor.getString(1));
                cart.setSalesPrice(cursor.getString(2));
                cart.setMRP(cursor.getString(3));
                cart.setStock_ID(cursor.getString(4));
                cart.setQTY(cursor.getString(5));
                cart.setRetailPrice(cursor.getString(6));
                cart.setPrivilagePrice(cursor.getString(7));
                cart.setWholesalePrice(cursor.getString(8));
                cart.setGST(cursor.getString(9));
                cart.setCESS(cursor.getString(10));
                cart.setCount(cursor.getString(11));
                cart.setPacked(cursor.getString(12));
                cart.setDescription(cursor.getString(13));
                cart.setImageName(cursor.getString(14));
                cartList.add(cart);

            } while (cursor.moveToNext());
        }
        return cartList;
    }

    public Integer deleteCartPrdouct(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(TABLE_CART,
                "ID_Items = ? ",
                new String[]{id});
        db.close();
        return status;
    }

    public boolean checkIcartItem(String ID) {
        boolean Value = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE "
                + KEY_ID_ITEMS + "=" + ID, null);
        if (cursor != null && cursor.getCount() != 0)
            Value = true;
        cursor.close();
        return Value;
    }

    public void addtoFav(FavModel fav) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues civalues = new ContentValues();
        civalues.put(KEY_FAVID_ITEMS, fav.getID_Items());
        civalues.put(KEY_FAVITEMNAME, fav.getItemName());
        civalues.put(KEY_FAVSALESPRICE, fav.getSalesPrice());
        civalues.put(KEY_FAVMRP, fav.getMRP());
        civalues.put(KEY_FAV_STOCKID, fav.getStock_ID());
        civalues.put(KEY_FAV_QTY, fav.getQTY());
        civalues.put(KEY_FAV_RETAILPRICE, fav.getRetailPrice());
        civalues.put(KEY_FAV_PRIVILAGEPRICE, fav.getPrivilagePrice());
        civalues.put(KEY_FAV_WHOLESALEPRICE, fav.getWholesalePrice());
        civalues.put(KEY_FAV_GST, fav.getGST());
        civalues.put(KEY_FAV_CESS, fav.getCESS());
        civalues.put(KEY_FAV_PACKED, fav.getPacked());
        civalues.put(KEY_FAV_Description, fav.getDescription());
        civalues.put(KEY_FAV_ImageName, fav.getImageName());
        civalues.put(KEY_FAV_ItemMalayalamName, fav.getItemMalayalamName());
        db.insert(TABLE_FAV, null, civalues);
        db.close();

    }
    public void addtoFavSTORE(FavStoreModel fav) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues civalues = new ContentValues();
        civalues.put(KEY_FAVSTORE_ID_ITEMS, fav.getID_Items());
        civalues.put(KEY_FAVSTORE_ITEMNAME, fav.getItemName());
        civalues.put(KEY_FAVSTORE__ImageName, fav.getImageName());
        db.insert(TABLE_FAVSTORE, null, civalues);
        db.close();

    }

    public List<FavModel> getAllFav() {
        List<FavModel> favList = new ArrayList<FavModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAV;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FavModel fav = new FavModel();
                fav.setID_Items(cursor.getString(0));
                fav.setItemName(cursor.getString(1));
                fav.setSalesPrice(cursor.getString(2));
                fav.setMRP(cursor.getString(3));
                fav.setStock_ID(cursor.getString(4));
                fav.setQTY(cursor.getString(5));
                fav.setRetailPrice(cursor.getString(6));
                fav.setPrivilagePrice(cursor.getString(7));
                fav.setWholesalePrice(cursor.getString(8));
                fav.setGST(cursor.getString(9));
                fav.setCESS(cursor.getString(10));
                fav.setPacked(cursor.getString(11));
                fav.setDescription(cursor.getString(12));
                fav.setImageName(cursor.getString(13));
                fav.setItemMalayalamName(cursor.getString(14));
                favList.add(fav);
            } while (cursor.moveToNext());
        }
        return favList;
    }

    public List<FavStoreModel> getAllFavStore() {
        List<FavStoreModel> favList = new ArrayList<FavStoreModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVSTORE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                FavStoreModel fav = new FavStoreModel();
                fav.setID_Items(cursor.getString(0));
                fav.setItemName(cursor.getString(1));
                fav.setImageName(cursor.getString(2));
                favList.add(fav);
            } while (cursor.moveToNext());
        }
        return favList;
    }

    public Integer deleteFavPrdouct(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(TABLE_FAV,
                "FAV_ID_Items = ? ",
                new String[]{id});
        db.close();
        return status;
    }
    public Integer deleteFavStorePrdouct(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(TABLE_FAVSTORE,
                "FAVSTORE_ID_Items = ? ",
                new String[]{id});
        db.close();
        return status;
    }

    public boolean checkIfavItem(String ID) {
        boolean Value = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FAV + " WHERE "
                + KEY_FAVID_ITEMS + "=" + ID, null);
        if (cursor != null && cursor.getCount() != 0)
            Value = true;
        cursor.close();
        return Value;
    }

    public boolean checkIfavStore(String ID) {
        boolean Value = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FAVSTORE + " WHERE "
                + KEY_FAVSTORE_ID_ITEMS + "=" + ID, null);
        if (cursor != null && cursor.getCount() != 0)
            Value = true;
        cursor.close();
        return Value;
    }

    public void addtoCartCheck(CheckCartModel cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues civalues = new ContentValues();
        civalues.put(KEY_CHECK_ID_ITEMS, cart.getItemid());
        civalues.put(KEY_CHECK_STOCKID, cart.getStockId());
        civalues.put(KEY_CHECK_QTY, cart.getQTY());
        civalues.put(KEY_CHECK_STOREID, cart.getStoreId());
        db.insert(TABLE_CHECK_ITEM, null, civalues);
        db.close();
    }

    public Integer deleteCheckCart(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(TABLE_CHECK_ITEM,
                "Itemid = ? ",
                new String[]{id});
        db.close();
        return status;
    }

    public boolean deleteallCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM cart");
        db.close();
        return true;
    }

    public boolean deleteallFav() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM fav");
        db.close();
        return true;
    }

    public boolean deleteallFavstore() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM favstore");
        db.close();
        return true;
    }

    public boolean deleteallInshopCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Inshop_cart");
        db.close();
        return true;
    }

    public float selectCartTotalActualPrice() {
        float amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(SalesPrice*Count) AS total FROM '"
                + TABLE_CART + "'", null);
        if (cursor.moveToNext()) {
            amount = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return amount;
    }

    public float CartTotalGST() {
        float amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(GST*Count) AS total FROM '"
                + TABLE_CART + "'", null);
        if (cursor.moveToNext()) {
            amount = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return amount;
    }

    public float CartTotalMRP() {
        float amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(MRP*Count) AS total FROM '"
                + TABLE_CART + "'", null);
        if (cursor.moveToNext()) {
            amount = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return amount;
    }

    public float CartTotalRetailPrice() {
        float amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(RetailPrice*Count) AS total FROM '"
                + TABLE_CART + "'", null);
        if (cursor.moveToNext()) {
            amount = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return amount;
    }

    public int selectCartCount() {
        int cartcount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(ID_Items) AS count FROM '"
                + TABLE_CART + "'", null);
        if (cursor.moveToNext()) {
            cartcount = cursor.getInt(cursor.getColumnIndex("count"));
        }
        Log.e(TAG,"cartcount  601  "+cartcount);
        cursor.close();
        return cartcount;
    }

    public int selectInshopCartCount() {
        int cartcount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(Inshop_ID_Items) AS count FROM '"
                + TABLE_INSHOP_CART + "'", null);
        if (cursor.moveToNext()) {
            cartcount = cursor.getInt(cursor.getColumnIndex("count"));
        }
        cursor.close();
        return cartcount;
    }

    public void addtoInshopCart(InshopCartModel cart) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues civalues = new ContentValues();
        civalues.put(KEY_INSHOP_ID_ITEMS, cart.getID_Items());
        civalues.put(KEY_INSHOP_ITEMNAME, cart.getItemName());
        civalues.put(KEY_INSHOP_SALESPRICE, cart.getSalesPrice());
        civalues.put(KEY_INSHOP_MRP, cart.getMRP());
        civalues.put(KEY_INSHOP_CART_STOCKID, cart.getStock_ID());
        civalues.put(KEY_INSHOP_CART_QTY, cart.getQTY());
        civalues.put(KEY_INSHOP_CART_RETAILPRICE, cart.getRetailPrice());
        civalues.put(KEY_INSHOP_CART_PRIVILAGEPRICE, cart.getPrivilagePrice());
        civalues.put(KEY_INSHOP_CART_WHOLESALEPRICE, cart.getWholesalePrice());
        civalues.put(KEY_INSHOP_CART_GST, cart.getGST());
        civalues.put(KEY_INSHOP_CART_CESS, cart.getCESS());
        civalues.put(KEY_INSHOP_CART_COUNT, cart.getCount());
        civalues.put(KEY_INSHOP_CART_PACKED, cart.getPacked());
        civalues.put(KEY_INSHOP_CART_ImageName, cart.getImageName());
        db.insert(TABLE_INSHOP_CART, null, civalues);
        db.close();
    }

    public List<InshopCartModel> getAllInshopCart() {
        List<InshopCartModel> cartList = new ArrayList<InshopCartModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_INSHOP_CART;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                InshopCartModel cart = new InshopCartModel();
                cart.setID_Items(cursor.getString(0));
                cart.setItemName(cursor.getString(1));
                cart.setSalesPrice(cursor.getString(2));
                cart.setMRP(cursor.getString(3));
                cart.setStock_ID(cursor.getString(4));
                cart.setQTY(cursor.getString(5));
                cart.setRetailPrice(cursor.getString(6));
                cart.setPrivilagePrice(cursor.getString(7));
                cart.setWholesalePrice(cursor.getString(8));
                cart.setGST(cursor.getString(9));
                cart.setCESS(cursor.getString(10));
                cart.setCount(cursor.getString(11));
                cart.setPacked(cursor.getString(12));
                cart.setImageName(cursor.getString(13));
                cartList.add(cart);
            } while (cursor.moveToNext());
        }
        return cartList;
    }

    public float selectInshopCartTotalActualPrice() {
        float amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(Inshop_SalesPrice*Inshop_Count) AS total FROM '"
                + TABLE_INSHOP_CART + "'", null);
        if (cursor.moveToNext()) {
            amount = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return amount;
    }

    public float InshopCartTotalGST() {
        float amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(Inshop_GST*Inshop_Count) AS total FROM '"
                + TABLE_INSHOP_CART + "'", null);
        if (cursor.moveToNext()) {
            amount = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return amount;
    }

    public float InshopCartTotalMRP() {
        float amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(Inshop_MRP*Inshop_Count) AS total FROM '"
                + TABLE_INSHOP_CART + "'", null);
        if (cursor.moveToNext()) {
            amount = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return amount;
    }

    public float InshopCartTotalRetailPrice() {
        float amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(Inshop_RetailPrice*Inshop_Count) AS total FROM '"
                + TABLE_INSHOP_CART + "'", null);
        if (cursor.moveToNext()) {
            amount = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return amount;
    }

    public boolean updateInshopCart (Integer id, String Count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Inshop_Count", Count);
        db.update("Inshop_cart", contentValues, "Inshop_ID_Items = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteInShopCartPrdouct(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(TABLE_INSHOP_CART,
                "Inshop_ID_Items = ? ",
                new String[]{id});
        db.close();
        return status;
    }

    public Boolean doesRecordExist(String ColumnData) {
        String q = "Select * FROM "+TABLE_INSHOP_CART+" WHERE "+KEY_INSHOP_ID_ITEMS+"='"+ColumnData+"';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteallReOrderCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM reordercart");
        db.close();
        return true;
    }

    public Integer deleteReorderItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status = db.delete(TABLE_REORDER_CART,
                "ID_Items = ? ",
                new String[]{id});
        db.close();
        return status;
    }

    public Boolean doesRecordExistReorder(String ColumnData) {
        String q = "Select * FROM "+TABLE_REORDER_CART+" WHERE "+KEY_REORDER_ID_ITEMS+"='"+ColumnData+"';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean doesRecordExistOrder(String ColumnData) {
        String q = "Select * FROM "+TABLE_CART+" WHERE "+KEY_ID_ITEMS+"='"+ColumnData+"';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean updateReorderCart (Integer id, String Count) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Count", Count);
        db.update("reordercart", contentValues, "ID_Items = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public boolean checkReOrderCartCount() {
        String q = "Select * FROM "+TABLE_REORDER_CART+" WHERE "+KEY_REORDER_CART_COUNT+"='"+"0"+"';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkCartCount() {
        String q = "Select * FROM "+TABLE_CART+" WHERE "+KEY_CART_COUNT+"='"+"0"+"';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public float selectReorderTotalActualPrice() {
        float amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(SalesPrice*Count) AS total FROM '"
                + TABLE_REORDER_CART + "'", null);
        if (cursor.moveToNext()) {
            amount = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return amount;
    }

    public float ReorderTotalGST() {
        float amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(GST*Count) AS total FROM '"
                + TABLE_REORDER_CART + "'", null);
        if (cursor.moveToNext()) {
            amount = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return amount;
    }

    public float ReorderTotalMRP() {
        float amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(MRP*Count) AS total FROM '"
                + TABLE_REORDER_CART + "'", null);
        if (cursor.moveToNext()) {
            amount = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return amount;
    }

    public float ReorderTotalRetailPrice() {
        float amount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(RetailPrice*Count) AS total FROM '"
                + TABLE_REORDER_CART + "'", null);
        if (cursor.moveToNext()) {
            amount = cursor.getFloat(cursor.getColumnIndex("total"));
        }
        cursor.close();
        return amount;
    }

    public int selectReorderCount() {
        int cartcount = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(ID_Items) AS count FROM '"
                + TABLE_REORDER_CART + "'", null);
        if (cursor.moveToNext()) {
            cartcount = cursor.getInt(cursor.getColumnIndex("count"));
        }
        cursor.close();
        return cartcount;
    }

}
