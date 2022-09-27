package com.perfect.easyshopplus.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.perfect.easyshopplus.Adapter.NavMenuAdapter;
import com.perfect.easyshopplus.DB.DBHandler;
import com.perfect.easyshopplus.R;
import com.perfect.easyshopplus.Utility.Config;
import com.perfect.easyshopplus.Utility.FileUtils;
import com.perfect.easyshopplus.Utility.PicassoTrustAll;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReorderPrescriptionUploadActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    String TAG = "ReorderPrescriptionUploadActivity";
    Button btnBrowse, btnUpload;
    private ImageView imageview,imgv1;
    private Button btnSelectImage;
    private Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    private static final int PERMISSION_CODE= 111;
    private static final int PICK_DOCUMRNT_GALLERY = 3;
    private String selectedFilePath = "";
    private static final int PERMISSION_CAMERA = 1;
    Context context;
    Bitmap bmImg;
    String imgbinary,ID_SalesOrder,order_id,Id_order,orderDate,deliveryDate,status,itemcount,storeName,DeliveryCharge,OrderNo;
    ByteArrayOutputStream baos;
    byte[] byteImage_photo;
    String encodedImage, OK;
    String ID_Store, ShopType, ID_CustomerAddress, OrderType;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvuser,tvcart,tv_notification;
    DBHandler db;
    double doubleTotalAmt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reorder_prescription_upload_main);
        Intent in = getIntent();
//        ID_Store = in.getStringExtra("ID_Store");
//        ShopType = in.getStringExtra("ShopType");
//        ID_CustomerAddress = in.getStringExtra("ID_CustomerAddress");
//        OrderType = in.getStringExtra("OrderType");
        Log.e(TAG,"Start   95 "+in.getStringExtra("doubleTotalAmt"));
        doubleTotalAmt = Double.parseDouble(in.getStringExtra("doubleTotalAmt"));
        imgbinary = "0";
        Log.e(TAG,"Start   97 "+doubleTotalAmt);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());



        Log.e(TAG,"Start   101 ");

        ID_SalesOrder = in.getStringExtra("ID_SalesOrder");
        order_id = in.getStringExtra("order_id");
        Id_order = in.getStringExtra("Id_order");
        OrderNo = in.getStringExtra("OrderNo");
        orderDate = in.getStringExtra("orderDate");
        deliveryDate = in.getStringExtra("deliveryDate");
        status = in.getStringExtra("status");
        ID_Store = in.getStringExtra("ID_Store");
        ShopType = in.getStringExtra("ShopType");
        itemcount = in.getStringExtra("itemcount");
        ID_CustomerAddress = in.getStringExtra("ID_CustomerAddress");
        OrderType = in.getStringExtra("OrderType");
        storeName = in.getStringExtra("storeName");
        DeliveryCharge = in.getStringExtra("DeliveryCharge");

        initiateViews();
        setRegViews();
        setHomeNavMenu1();

        /*SharedPreferences RequiredShoppinglistpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF45, 0);
        if(RequiredShoppinglistpref.getString("RequiredShoppinglist", null).equals("false")){
            setHomeNavMenu1();
        }else{
            setHomeNavMenu();
        }*/

        SharedPreferences baseurlpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences imgpref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF57, 0);
        String BASEURL = baseurlpref.getString("BaseURL", null);
        String IMAGEURL = imgpref.getString("ImageURL", null);

        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF33, 0);
        ImageView imApplogo=findViewById(R.id.imApplogo);
        String strImagepath= IMAGEURL + pref3.getString("AppIconImageCode", null);
        PicassoTrustAll.getInstance(this).load(strImagepath).into(imApplogo);
        SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF2, 0);
        tvuser.setText("Welcome "+pref2.getString("username", null));
        db=new DBHandler(this);
        tvcart.setText(String.valueOf(db.selectCartCount()));
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));


        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        OK = (OKsp.getString("OK", ""));

    }

    private void initiateViews() {

        btnBrowse = (Button) findViewById(R.id.btnBrowse);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imgv1= (ImageView) findViewById(R.id.imgv1);
        im = (ImageView) findViewById(R.id.im);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        imcart=(ImageView) findViewById(R.id.imcart);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);
    }

    private void setRegViews() {

        btnBrowse.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        im.setOnClickListener(this);
        imcart.setOnClickListener(this);
        tvcart.setOnClickListener(this);
        im_notification.setOnClickListener(this);
        tv_notification.setOnClickListener(this);
        imgv1.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.im:

                //  drawer.openDrawer(Gravity.START);
                onBackPressed();                break;
            case R.id.imcart:
                Intent i = new Intent(ReorderPrescriptionUploadActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.tvcart:
                Intent in = new Intent(ReorderPrescriptionUploadActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
            case R.id.tv_notification:
                startActivity(new Intent(ReorderPrescriptionUploadActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(ReorderPrescriptionUploadActivity.this, NotificationActivity.class));
                break;
            case R.id.btnBrowse:
                selectImage();
                break;
            case R.id.imgv1:
                if(destination!=null){
                    imgv1.setRotation(imgv1.getRotation()+90);
                }
                break;
            case R.id.btnUpload:
//                if(imgbinary.equals("0"))
                if(destination==null && imgbinary.equals("0"))
                {
                    SharedPreferences Pleaseuploadimagesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF315, 0);
                    Toast.makeText(getApplicationContext(),Pleaseuploadimagesp.getString("Pleaseuploadimage", "")+".",Toast.LENGTH_LONG).show();
                }
                else
                {
                    if(OrderType.equals("true")) {


                        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                        String strMinimumDeliveryAmount = pref3.getString("MinimumDeliveryAmount", null);

                        SharedPreferences MinimumamountHomedeliverysp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF254, 0);
                        String MinimumamountHomedelivery = MinimumamountHomedeliverysp.getString("MinimumamountHomedelivery","");

                        if (Double.parseDouble(strMinimumDeliveryAmount) > doubleTotalAmt) {
                            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReorderPrescriptionUploadActivity.this);
                            builder.setMessage(MinimumamountHomedelivery + strMinimumDeliveryAmount)
//                            builder.setMessage("Minimum amount required for Home delivery is " + strMinimumDeliveryAmount)
                                    .setCancelable(false)
                                    .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                            android.app.AlertDialog alert = builder.create();
                            alert.show();
                        } else {
                            Intent intn = new Intent(ReorderPrescriptionUploadActivity.this, CheckoutReorderHomeDeliveryActivity.class);
//                            intn.putExtra("ID_Store", ID_Store);
//                            intn.putExtra("ShopType", ShopType);
//                            intn.putExtra("OrderType", "1");
//                            intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                         //   intn.putExtra("Image", imgbinary);

                            intn.putExtra("destination", destination);
                            Log.e(TAG,"destination   232   "+destination);

                            intn.putExtra("order_id", order_id);
                            intn.putExtra("OrderNo",OrderNo);
                            intn.putExtra("deliveryDate", deliveryDate);
                            intn.putExtra("Id_order", Id_order);
                            intn.putExtra("orderDate", orderDate);
                            intn.putExtra("status", status);
                            intn.putExtra("ID_Store", ID_Store);
                            intn.putExtra("ShopType", ShopType);
                            intn.putExtra("itemcount", itemcount);
                            intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                            intn.putExtra("OrderType", "1");
                            intn.putExtra("storeName", storeName);
                            intn.putExtra("DeliveryCharge", DeliveryCharge);
                            startActivity(intn);
                            finish();
                        }

                    }else{
                        Intent intn = new Intent(ReorderPrescriptionUploadActivity.this, CheckoutReorderActivity.class);
//                        intn.putExtra("ID_Store", ID_Store);
//                        intn.putExtra("ShopType", ShopType);
//                        intn.putExtra("OrderType", "0");
//                        intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);

                        intn.putExtra("Image", imgbinary);
                        intn.putExtra("destination", destination);
                        Log.e(TAG,"destination   232   "+destination);

                        intn.putExtra("order_id", order_id);
                        intn.putExtra("OrderNo",OrderNo);
                        intn.putExtra("deliveryDate", deliveryDate);
                        intn.putExtra("Id_order", Id_order);
                        intn.putExtra("orderDate", orderDate);
                        intn.putExtra("status", status);
                        intn.putExtra("ID_Store", ID_Store);
                        intn.putExtra("ShopType", ShopType);
                        intn.putExtra("itemcount", itemcount);
                        intn.putExtra("ID_CustomerAddress", ID_CustomerAddress);
                        intn.putExtra("OrderType", "0");
                        intn.putExtra("storeName", storeName);
                        intn.putExtra("DeliveryCharge", DeliveryCharge);

                        startActivity(intn);
                        finish();
                    }


                }


                break;
        }
    }


//
//    private void selectImage() {
//        try {
//            PackageManager pm = getPackageManager();
//            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
//            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
//                if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
//                    // Permission is not granted
//                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//                        // Show an explanation to the user *asynchronously* -- don't block
//                        // this thread waiting for the user's response! After the user
//                        // sees the explanation, try again to request the permission.
//
//                    } else {
//                        // No explanation needed; request the permission
//                        ActivityCompat.requestPermissions(this,
//                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
//                    }
//                }
//                else {
////                    final CharSequence[] options = {"Take Photo", "Choose From Gallery","Choose Document","Cancel"};
//                    final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
//                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                    builder.setTitle("Select Option");
//                    builder.setItems(options, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int item) {
//                            if (options[item].equals("Take Photo")) {
//                                dialog.dismiss();
//                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                startActivityForResult(intent, PICK_IMAGE_CAMERA);
//                            } else if (options[item].equals("Choose From Gallery")) {
//                                dialog.dismiss();
//                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
//
//                            } else if (options[item].equals("Choose Document")) {
//                                browseDocuments();
//                            } else if (options[item].equals("Cancel")) {
//                                dialog.dismiss();
//                            }
//                            dialog.dismiss();
//                        }
//                    });
//                    builder.show();
//                }
//            } else
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.CAMERA},
//                        PERMISSION_CAMERA);
//        } catch (Exception e) {
//            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_CAMERA: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
//                        // Permission is not granted
//                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                        } else {
//                            // No explanation needed; request the permission
//                            ActivityCompat.requestPermissions(this,
//                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
//                        }
//                    }
//                    else {
////                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                        startActivityForResult(intent, PICK_IMAGE_CAMERA);
//                        final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
//                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                        builder.setTitle("Select Option");
//                        builder.setItems(options, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int item) {
//                                if (options[item].equals("Take Photo")) {
//                                    dialog.dismiss();
//                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    startActivityForResult(intent, PICK_IMAGE_CAMERA);
//                                } else if (options[item].equals("Choose From Gallery")) {
//                                    dialog.dismiss();
//                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                    startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
//
//                                } else if (options[item].equals("Choose Document")) {
//                                    browseDocuments();
//                                } else if (options[item].equals("Cancel")) {
//                                    dialog.dismiss();
//                                }
//                                dialog.dismiss();
//                            }
//                        });
//                        builder.show();
//
//                    }
//                }
//                return;
//            }
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        inputStreamImg = null;
//        if (requestCode == PICK_IMAGE_CAMERA) {
//            if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
//                // Permission is not granted
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//
//                } else {
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                            MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
//                }
//            }
//            else {
//                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
//                destination = new File(Environment.getExternalStorageDirectory() + "/" +
//                        getString(R.string.app_name), "IMG_" + System.currentTimeMillis() + ".jpg");
//                FileOutputStream fo;
//                try {
//                    if (!destination.getParentFile().exists()){
//                        destination.getParentFile().mkdirs();
//                    }
//                    if (!destination.exists())
//                    {
//                        destination.createNewFile();
//                    }
//                    fo = new FileOutputStream(destination);
//                    fo.write(bytes.toByteArray());
//                    fo.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                imgPath = destination.getAbsolutePath();
//                destination = new File(imgPath);
//                if(destination.exists())
//                {
//                    bmImg = BitmapFactory.decodeFile(imgPath);
//                    imgv1.setImageBitmap(bmImg);
//                    baos = new ByteArrayOutputStream();
//
//                    bmImg.compress(Bitmap.CompressFormat.JPEG,40,baos);
//
//
//                    // bitmap object
//
//                    byteImage_photo = baos.toByteArray();
//
//                    //generate base64 string of image
//
//                    encodedImage = Base64.encodeToString(byteImage_photo,Base64.DEFAULT);
//                    imgbinary =encodedImage;
//                }
//
//                Log.e(TAG,"destination   528   "+destination);
//                // mTxtAttchment.setText(imgPath);
//            }
//        }
//        else if (requestCode == PICK_IMAGE_GALLERY) {
//            Uri selectedImage = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
//                imgPath = getRealPathFromURI(selectedImage);
//                destination = new File(imgPath.toString());
//                if(destination.exists())
//                {
//                    bmImg = BitmapFactory.decodeFile(imgPath);
//                    imgv1.setImageBitmap(bmImg);
//
//                    baos = new ByteArrayOutputStream();
//
//                    bmImg.compress(Bitmap.CompressFormat.JPEG,100,baos);
//
//
//                    // bitmap object
//
//                    byteImage_photo = baos.toByteArray();
//
//                    //generate base64 string of image
//
//                    encodedImage = Base64.encodeToString(byteImage_photo,Base64.DEFAULT);
//                    imgbinary =encodedImage;
//
//
//
//                }
//                Log.e(TAG,"destination   562   "+destination);
//                //   mTxtAttchment.setText(imgPath);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        else if (requestCode == PICK_DOCUMRNT_GALLERY) {
//            Uri uri = data.getData();
//            selectedFilePath = FileUtils.getPath(this, uri);
//            destination = new File(selectedFilePath.toString());
//            //  mTxtAttchment.setText(selectedFilePath);
//        }
//    }


    // Start 02.07.2021
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                    }
                }
                else {


                    if (ContextCompat.checkSelfPermission(ReorderPrescriptionUploadActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ReorderPrescriptionUploadActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                    }
                    else
                    {
                        final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Select Option");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                if (options[item].equals("Take Photo")) {
                                    dialog.dismiss();
//                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    startActivityForResult(intent, PICK_IMAGE_CAMERA);

//                                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                                        ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
//                                    }
//                                    else
//                                    {
                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                        // Create the File where the photo should go
                                        try {


                                            destination = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name), "IMG_" + System.currentTimeMillis() + ".jpg");
                                            if (!destination.getParentFile().exists()){
                                                destination.getParentFile().mkdirs();
                                            }
                                            if (!destination.exists())
                                            {
                                                destination.createNewFile();

                                            }
                                            Log.e(TAG,"destination   694  "+destination);
                                            if (destination != null) {
                                                Uri photoURI = Uri.fromFile(new File(destination.toString()));
                                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                                startActivityForResult(takePictureIntent, PICK_IMAGE_CAMERA);
                                            }
                                        } catch (Exception ex) {
                                            // Error occurred while creating the File
                                            Log.e(TAG,"Exception   702  "+ex.toString());

                                        }


                                    }else
                                    {

                                    }
                                    // }

                                } else if (options[item].equals("Choose From Gallery")) {
                                    dialog.dismiss();
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);

                                } else if (options[item].equals("Choose Document")) {

                                } else if (options[item].equals("Cancel")) {
                                    dialog.dismiss();
                                }
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
            } else

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_CAMERA);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        // Permission is not granted
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        } else {
                            // No explanation needed; request the permission
                            ActivityCompat.requestPermissions(this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                        }
                    } else {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        if (ContextCompat.checkSelfPermission(ReorderPrescriptionUploadActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ReorderPrescriptionUploadActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                        }
                        else
                        {
                            final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Select Option");
                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int item) {
                                    if (options[item].equals("Take Photo")) {
                                        dialog.dismiss();
//                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    startActivityForResult(intent, PICK_IMAGE_CAMERA);

//                                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                                        ActivityCompat.requestPermissions(MainActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
//                                    }
//                                    else
//                                    {
                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                            // Create the File where the photo should go
                                            try {


                                                destination = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name), "IMG_" + System.currentTimeMillis() + ".jpg");
                                                if (!destination.getParentFile().exists()){
                                                    destination.getParentFile().mkdirs();
                                                }
                                                if (!destination.exists())
                                                {
                                                    destination.createNewFile();

                                                }

                                                Log.e(TAG,"Exception   807  "+destination);
                                                if (destination != null) {
                                                    Uri photoURI = Uri.fromFile(new File(destination.toString()));
                                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                                                    startActivityForResult(takePictureIntent, PICK_IMAGE_CAMERA);
                                                }
                                            } catch (Exception ex) {
                                                // Error occurred while creating the File
                                                Log.e(TAG,"Exception   807  "+ex.toString());
                                            }


                                        }else
                                        {

                                        }
                                        // }

                                    } else if (options[item].equals("Choose From Gallery")) {
                                        dialog.dismiss();
                                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);

                                    } else if (options[item].equals("Choose Document")) {

                                    } else if (options[item].equals("Cancel")) {
                                        dialog.dismiss();
                                    }
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }
                    }

                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                }
            }
            else {

                imgPath = destination.getAbsolutePath();
                destination = new File(imgPath);
                if(destination.exists())
                {
                    Bitmap myBitmap = BitmapFactory.decodeFile(destination.getAbsolutePath());
                    imgv1.setImageBitmap(myBitmap);
                }



                Log.e(TAG,"destination   487   "+destination);
                // mTxtAttchment.setText(imgPath);
            }
        }
        else if (requestCode == PICK_IMAGE_GALLERY) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                imgPath = getRealPathFromURI(selectedImage);
                destination = new File(imgPath.toString());
                if(destination.exists())
                {
//                    bmImg = BitmapFactory.decodeFile(imgPath);
//                    viewImage.setImageBitmap(bmImg);
//
//                    baos = new ByteArrayOutputStream();
//
//                    bmImg.compress(Bitmap.CompressFormat.JPEG,100,baos);
//
//
//                    // bitmap object
//
//                    byteImage_photo = baos.toByteArray();
//
//                    //generate base64 string of image
//
//                    encodedImage = Base64.encodeToString(byteImage_photo,Base64.DEFAULT);
//                    imgbinary =encodedImage;

                    Bitmap myBitmap = BitmapFactory.decodeFile(destination.getAbsolutePath());
                    imgv1.setImageBitmap(myBitmap);



                }

                //   mTxtAttchment.setText(imgPath);

                Log.e(TAG,"destination   524   "+destination);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG,"Exception   543   "+e.toString());
            }
        }
        else if (requestCode == PICK_DOCUMRNT_GALLERY) {
            Uri uri = data.getData();
//            selectedFilePath = FileUtils.getPath(this, uri);
//            destination = new File(selectedFilePath.toString());
//            //  mTxtAttchment.setText(selectedFilePath);
//            Log.e(TAG,"destination   535   "+destination);
        }
    }

    //End  02.07.2021

    private void browseDocuments(){

//        final Intent fsIntent = new Intent();
//        fsIntent.setType("*/*");
//        fsIntent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(fsIntent, PICK_DOCUMRNT_GALLERY);

        String[] mimetypes = {
                "application/*",
                //"audio/*",
                "font/*",
                //"image/*",
                "message/*",
                "model/*",
                "multipart/*",
                "text/*",
                //"video/*"
        };
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes); //Important part here
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_DOCUMRNT_GALLERY);
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
        final String[] menulist = new String[]{"Home","My Cart","My Orders", "Favourites", "Favourite Stores",
                "Notifications", "Shopping List","My Profile", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,R.drawable.ic_store,
                R.drawable.ic_notifications, R.drawable.ic_list,R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ReorderPrescriptionUploadActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ReorderPrescriptionUploadActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, ShoppingListActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 7) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 8) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, AboutUsActivity.class));
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
        final String[] menulist = new String[]{"Home","My Cart","Track Orders", "Favourite Items",
                "Notifications", "My Profile", "About Us", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications, R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ReorderPrescriptionUploadActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ReorderPrescriptionUploadActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, AboutUsActivity.class));
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
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 10) {

                    try {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReorderPrescriptionUploadActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) ReorderPrescriptionUploadActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 12) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 14) {
                    startActivity(new Intent(ReorderPrescriptionUploadActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void doLogout() {
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ReorderPrescriptionUploadActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ReorderPrescriptionUploadActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater1.inflate(R.layout.logout_popup, null);
            LinearLayout ok = (LinearLayout) layout.findViewById(R.id.checkout_back_ok);
            LinearLayout cancel = (LinearLayout) layout.findViewById(R.id.checkout_back_cancel);
            TextView tv_popupdelete= (TextView) layout.findViewById(R.id.tv_popupdelete);
            TextView tv_popupdeleteinfo3= (TextView) layout.findViewById(R.id.tv_popupdeleteinfo3);
            TextView edit= (TextView) layout.findViewById(R.id.edit);
            TextView tv_no= (TextView) layout.findViewById(R.id.tv_no);



            builder.setView(layout);
            final android.app.AlertDialog alertDialog = builder.create();

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

                        startActivity(new Intent(ReorderPrescriptionUploadActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(ReorderPrescriptionUploadActivity.this,LocationActivity.class));
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

    @Override
    protected void onPostResume() {
        super.onPostResume();
        try {
            SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
            tv_notification.setText(pref.getString("notificationcount", null));
        }catch (Exception e){

        }
    }
}
