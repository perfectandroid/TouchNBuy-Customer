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
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
        import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
        import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
        import androidx.core.app.ActivityCompat;
        import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
        import androidx.cardview.widget.CardView;
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
import com.perfect.easyshopplus.Fragment.OutShopFragment;
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

public class ShoppingListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
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
    String imgbinary, OK;
    ByteArrayOutputStream baos;
    byte[] byteImage_photo;
    String encodedImage;
   // double doubleTotalAmt;
    ImageView im, imcart, im_notification;
    private ListView lvNavMenu;
    DrawerLayout drawer;
    TextView tvuser,tvcart,tv_notification;
    DBHandler db;

    String TAG = "ShoppingListActivity";
    BottomNavigationView navigation;
    String fromCategory = "from";
    String valueCategory = "home";
    String fromCart = "From";
    String valueCart = "Home";
    String fromHome = "";
    String valueHome = "";
    String fromFavorite = "From";
    String valueFavorite = "Home";
    TextView tv_header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_main);
        imgbinary = "0";
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        initiateViews();
        setRegViews();
        setHomeNavMenu();
        setBottomBar();


        Glide.with( this ).load( R.drawable.upload_gif ).into( imgv1 );
        SharedPreferences OKsp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF104, 0);
        OK = (OKsp.getString("OK", ""));

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
        tvcart.setText(String.valueOf(db.selectCartCount()+db.selectInshopCartCount()));
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF14, 0);
        tv_notification.setText(pref.getString("notificationcount", null));

        SharedPreferences UploadPrescription = getApplicationContext().getSharedPreferences(Config.SHARED_PREF313, 0);
        tv_header.setText(""+UploadPrescription.getString("UploadPrescription",""));

        SharedPreferences Browse = getApplicationContext().getSharedPreferences(Config.SHARED_PREF314, 0);
        btnBrowse.setText(""+Browse.getString("Browse",""));

        SharedPreferences Upload = getApplicationContext().getSharedPreferences(Config.SHARED_PREF324, 0);
        btnUpload.setText(""+Upload.getString("Upload",""));
    }

    private void initiateViews() {

        btnBrowse = (Button) findViewById(R.id.btnBrowse);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imgv1= (ImageView) findViewById(R.id.imgv1);
        im = (ImageView) findViewById(R.id.im);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        lvNavMenu = (ListView) findViewById(R.id.lvNavMenu);
        tvuser = (TextView) findViewById(R.id.tvuser);
        tvcart = (TextView) findViewById(R.id.tvcart);
        imcart=(ImageView) findViewById(R.id.imcart);
        tv_notification = (TextView) findViewById(R.id.tv_notification);
        im_notification = (ImageView) findViewById(R.id.im_notification);

        tv_header = (TextView) findViewById(R.id.tv_header);
    }

    private void setRegViews() {
        im.setOnClickListener(this);
        btnBrowse.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
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
                Intent i = new Intent(ShoppingListActivity.this, CartActivity.class);
                i.putExtra("From", "Home");
                startActivity(i);
                break;
            case R.id.tvcart:
                Intent in = new Intent(ShoppingListActivity.this, CartActivity.class);
                in.putExtra("From", "Home");
                startActivity(in);
                break;
                case R.id.tv_notification:
                startActivity(new Intent(ShoppingListActivity.this, NotificationActivity.class));
                break;
            case R.id.im_notification:
                startActivity(new Intent(ShoppingListActivity.this, NotificationActivity.class));
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
                Log.e(TAG,"destination   182   "+destination);
                SharedPreferences Counterpickuppref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF44, 0);
                SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF27, 0);
                String strhomedelivery = pref2.getString("homedelivery", null);
                if(destination==null)
                {

                    SharedPreferences Pleaseuploadimagesp = getApplicationContext().getSharedPreferences(Config.SHARED_PREF315, 0);
                    Toast.makeText(getApplicationContext(),Pleaseuploadimagesp.getString("Pleaseuploadimage", ""),Toast.LENGTH_LONG).show();
                }

                else if(Counterpickuppref.getString("Requiredcounterpickup", null).equals("false") && strhomedelivery.equals("true")){

                    if (strhomedelivery.equals("false")) {

                        SharedPreferences Homedeliveryoptionwillstartshortly = getSharedPreferences(Config.SHARED_PREF253, 0);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        builder.setMessage(Homedeliveryoptionwillstartshortly.getString("Homedeliveryoptionwillstartshortly","")+".")
//                                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                                                    builder.setMessage("Home delivery option will start shortly.")
                                .setCancelable(false)
                                .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  startActivity(new Intent(getContext(), CheckoutActivity.class));
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                    else {
                        SharedPreferences pref3 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF29, 0);
                        String strMinimumDeliveryAmount = pref3.getString("MinimumDeliveryAmount", null);
//                        if (Double.parseDouble(strMinimumDeliveryAmount) > ShoppingListActivity.this.doubleTotalAmt) {
//
//
//                            SharedPreferences MinimumamountHomedelivery = getSharedPreferences(Config.SHARED_PREF254, 0);
//                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//                            builder.setMessage(MinimumamountHomedelivery.getString("MinimumamountHomedelivery","")+" "+strMinimumDeliveryAmount)
////                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
////                                                        builder.setMessage("Minimum amount required for Home delivery is " + strMinimumDeliveryAmount)
//                                    .setCancelable(false)
//                                    .setPositiveButton(OK, new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog, int id) {
//
//                                        }
//                                    });
//                            AlertDialog alert = builder.create();
//                            alert.show();
//                        } else {
                            Intent intent = new Intent(getApplicationContext(), AddressAddActivty.class);
                            intent.putExtra("destination", destination);
                            startActivity(intent);
                      //  }
                    }

                }

                else if(Counterpickuppref.getString("Requiredcounterpickup", null).equals("true") && strhomedelivery.equals("false")){

                    Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                    intent.putExtra("destination", destination);
                    startActivity(intent);
                }
                else
                {
                   /* Log.i("VALUE",imgbinary);
                    Intent i = new Intent(PrescriptionUploadActivity.this,CheckoutActivity.class);
                    i.putExtra("Image", imgbinary);
                    startActivity(i);*/


                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    final View customLayout = getLayoutInflater().inflate(R.layout.orderconfirm_dialog, null);
                    builder.setView(customLayout);

                    CardView crdPickUp = customLayout.findViewById(R.id.crdPickUp);
                    CardView crdHomeDelivery = customLayout.findViewById(R.id.crdHomeDelivery);

                    TextView txt_dialog = customLayout.findViewById(R.id.txt_dia);
                    TextView tv_storepickup = customLayout.findViewById(R.id.tv_storepickup);
                    TextView tv_doordelivery = customLayout.findViewById(R.id.tv_doordelivery);

                    SharedPreferences chooseyourdeliveryoptions = getApplicationContext().getSharedPreferences(Config.SHARED_PREF133, 0);
                    SharedPreferences storepickup = getApplicationContext().getSharedPreferences(Config.SHARED_PREF134, 0);
                    SharedPreferences doordelivery = getApplicationContext().getSharedPreferences(Config.SHARED_PREF135, 0);

                    txt_dialog.setText(""+chooseyourdeliveryoptions.getString("chooseyourdeliveryoptions",""));
                    tv_storepickup.setText(""+storepickup.getString("storepickup",""));
                    tv_doordelivery.setText(""+doordelivery.getString("doordelivery",""));

                    crdPickUp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                           // startActivity(new Intent(ShoppingListActivity.this, CheckoutshoppingListcounterpickupActivity.class));
                            Intent i = new Intent(ShoppingListActivity.this,CheckoutshoppingListcounterpickupActivity.class);
                          //  i.putExtra("Image", imgbinary);
                            i.putExtra("destination", destination);
                            Log.e(TAG,"destination   207   "+destination);
                            startActivity(i);
                        }
                    });
                    crdHomeDelivery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            SharedPreferences pref2 = getApplicationContext().getSharedPreferences(Config.SHARED_PREF27, 0);
                            String strhomedelivery = pref2.getString("homedelivery", null);
                            if (strhomedelivery.equals("false")) {
                                SharedPreferences Homedeliveryoptionwillstartshortly = getApplicationContext().getSharedPreferences(Config.SHARED_PREF253, 0);
                                AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListActivity.this);
                                builder.setMessage(Homedeliveryoptionwillstartshortly.getString("Homedeliveryoptionwillstartshortly","")+".")
//                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ShoppingListActivity.this);
//                                builder.setMessage("Home delivery option will start shortly, please do counter pickup delivery option.")
                                        .setCancelable(false)
                                        .setPositiveButton(OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // startActivity(new Intent(PrescriptionUploadActivity.this, CheckoutActivity.class));
                                                Intent i = new Intent(ShoppingListActivity.this,CheckoutshoppingListcounterpickupActivity.class);
                                          //      i.putExtra("Image", imgbinary);
                                                Log.e(TAG,"destination   230   "+destination);
                                                i.putExtra("destination", destination);
                                                startActivity(i);
                                            }
                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                try {
                                    Intent i2 = new Intent(ShoppingListActivity.this,CheckoutShoppinglistAddressAddActivty.class);
//                                    i2.putExtra("Image", imgbinary);
//                                    Log.e(TAG,"destination   240   "+destination);
                                    i2.putExtra("destination", destination);
                                    startActivity(i2);
                                }catch (Exception e){
                                    Log.e(TAG,"Exception   246   "+e.toString());
                                    Log.e(TAG,"Exception   246   "+e.toString());
                                }

                            }
                        }
                    });


                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setView(customLayout, 0, 0, 0, 0);
                    dialog.show();


                }


                break;
        }
    }

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
//
//                    if (ContextCompat.checkSelfPermission(ShoppingListActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(ShoppingListActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
//                    }
//                    else
//                    {
//                        final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
//                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                        builder.setTitle("Select Option");
//                        builder.setItems(options, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int item) {
//                                if (options[item].equals("Take Photo")) {
//                                    dialog.dismiss();
////                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                                        startActivityForResult(intent, PICK_IMAGE_CAMERA);
//                                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                                        // Create the File where the photo should go
//                                        try {
//
//
//                                            destination = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name), "IMG_" + System.currentTimeMillis() + ".jpg");
//                                            if (!destination.getParentFile().exists()){
//                                                destination.getParentFile().mkdirs();
//                                            }
//                                            if (!destination.exists())
//                                            {
//                                                destination.createNewFile();
//
//                                            }
//                                            Log.e(TAG,"destination   315   "+ destination);
//                                            if (destination != null) {
//                                                Uri photoURI = Uri.fromFile(new File(destination.toURI()));
//                                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                                                startActivityForResult(takePictureIntent, PICK_IMAGE_CAMERA);
//                                            }
//                                        } catch (Exception ex) {
//                                            // Error occurred while creating the File
//
//                                            Log.e(TAG,"Exception   234   "+ ex.toString());
//                                        }
//
//
//                                    }
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
//                    }
//
//                    // HIDE 02.07.2021
////                    final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
////                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
////                    builder.setTitle("Select Option");
////                    builder.setItems(options, new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int item) {
////                            if (options[item].equals("Take Photo")) {
////                                dialog.dismiss();
////                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                                startActivityForResult(intent, PICK_IMAGE_CAMERA);
////                            } else if (options[item].equals("Choose From Gallery")) {
////                                dialog.dismiss();
////                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                                startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
////
////                            } else if (options[item].equals("Choose Document")) {
////                                browseDocuments();
////                            } else if (options[item].equals("Cancel")) {
////                                dialog.dismiss();
////                            }
////                            dialog.dismiss();
////                        }
////                    });
////                    builder.show();
//
//                    // HIDE 02.07.2021
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
//                        if (ContextCompat.checkSelfPermission(ShoppingListActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                            ActivityCompat.requestPermissions(ShoppingListActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
//                        }
//                        else
//                        {
//                            final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
//                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                            builder.setTitle("Select Option");
//                            builder.setItems(options, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int item) {
//                                    if (options[item].equals("Take Photo")) {
//                                        dialog.dismiss();
////                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                                        startActivityForResult(intent, PICK_IMAGE_CAMERA);
//                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//                                            // Create the File where the photo should go
//                                            try {
//
//
////                                                destination = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name), "IMG_" + System.currentTimeMillis() + ".jpg");
////                                                if (!destination.getParentFile().exists()){
////                                                    destination.getParentFile().mkdirs();
////                                                }
////                                                if (!destination.exists())
////                                                {
////                                                    destination.createNewFile();
////
////                                                }
//                                                if (destination != null) {
//                                                    Uri photoURI = Uri.fromFile(new File(destination.toString()));
//
//                                                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                                                    startActivityForResult(takePictureIntent, PICK_IMAGE_CAMERA);
//                                                }
//                                            } catch (Exception ex) {
//                                                // Error occurred while creating the File
//
//                                            }
//
//
//                                        }
//                                    } else if (options[item].equals("Choose From Gallery")) {
//                                        dialog.dismiss();
//                                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
//
//                                    } else if (options[item].equals("Choose Document")) {
//                                        browseDocuments();
//                                    } else if (options[item].equals("Cancel")) {
//                                        dialog.dismiss();
//                                    }
//                                    dialog.dismiss();
//                                }
//                            });
//                            builder.show();
//                        }
//
//                        //HIDE 02.07.2021
////                        final CharSequence[] options = {"Take Photo", "Choose From Gallery","Cancel"};
////                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
////                        builder.setTitle("Select Option");
////                        builder.setItems(options, new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int item) {
////                                if (options[item].equals("Take Photo")) {
////                                    dialog.dismiss();
////                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                                    startActivityForResult(intent, PICK_IMAGE_CAMERA);
////                                } else if (options[item].equals("Choose From Gallery")) {
////                                    dialog.dismiss();
////                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
////                                    startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
////
////                                } else if (options[item].equals("Choose Document")) {
////                                    browseDocuments();
////                                } else if (options[item].equals("Cancel")) {
////                                    dialog.dismiss();
////                                }
////                                dialog.dismiss();
////                            }
////                        });
////                        builder.show();
//
//                        //HIDE 02.07.2021
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
//
//                // HIDE 02.07.2021
////
////                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
////                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
////                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
////                destination = new File(Environment.getExternalStorageDirectory() + "/" + getString(R.string.app_name), "IMG_" + System.currentTimeMillis() + ".jpg");
////
////                Log.e(TAG,"460   destination   "+destination);
////
////                FileOutputStream fo;
////                try {
////                    if (!destination.getParentFile().exists()){
////                        destination.getParentFile().mkdirs();
////                    }
////                    if (!destination.exists())
////                    {
////                        destination.createNewFile();
////
////                    }
////                    fo = new FileOutputStream(destination);
////                    fo.write(bytes.toByteArray());
////                    fo.close();
////                } catch (FileNotFoundException e) {
////                    e.printStackTrace();
////                } catch (Exception e) {
////                    e.printStackTrace();
////                    Log.e(TAG,"476   Exception   "+e.toString());
////                }
////                imgPath = destination.getAbsolutePath();
////
////                destination = new File(imgPath);
////                if(destination.exists())
////                {
////                    bmImg = BitmapFactory.decodeFile(imgPath);
////                    imgv1.setImageBitmap(bmImg);
////                    baos = new ByteArrayOutputStream();
////
////                    bmImg.compress(Bitmap.CompressFormat.JPEG,100,baos);
////
////
////                    // bitmap object
////
////                    byteImage_photo = baos.toByteArray();
////
////                    //generate base64 string of image
////
////                    encodedImage = Base64.encodeToString(byteImage_photo,Base64.DEFAULT);
////                    imgbinary =encodedImage;
////                }
//                // HIDE 02.07.2021
//
//                Log.e(TAG,"PATH   449       "+destination.getAbsolutePath());
//                Bitmap myBitmap = BitmapFactory.decodeFile(destination.getAbsolutePath());
//                imgv1.setImageBitmap(myBitmap);
//
//                Log.e(TAG,"destination   487   "+destination);
//                // mTxtAttchment.setText(imgPath);
//            }
//        }
//        else if (requestCode == PICK_IMAGE_GALLERY) {
//            Uri selectedImage = data.getData();
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
//                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//                imgPath = getRealPathFromURI(selectedImage);
//                destination = new File(imgPath.toString());
//                if(destination.exists())
//                {
//                    // HIDE 02.07.2021
//
////                    bmImg = BitmapFactory.decodeFile(imgPath);
////                    imgv1.setImageBitmap(bmImg);
////
////                    baos = new ByteArrayOutputStream();
////
////                    bmImg.compress(Bitmap.CompressFormat.JPEG,100,baos);
////
////
////                    // bitmap object
////
////                    byteImage_photo = baos.toByteArray();
////
////                    //generate base64 string of image
////
////                    encodedImage = Base64.encodeToString(byteImage_photo,Base64.DEFAULT);
////                    imgbinary =encodedImage;
//
//                    // HIDE 02.07.2021
//
//
//                    Bitmap myBitmap = BitmapFactory.decodeFile(destination.getAbsolutePath());
//                    imgv1.setImageBitmap(myBitmap);
//
//                }
//
//                //   mTxtAttchment.setText(imgPath);
//
//                Log.e(TAG,"destination   524   "+destination);
//
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e(TAG,"Exception   543   "+e.toString());
//            }
//        }
//        else if (requestCode == PICK_DOCUMRNT_GALLERY) {
//            Uri uri = data.getData();
//            selectedFilePath = FileUtils.getPath(this, uri);
//            destination = new File(selectedFilePath.toString());
//            //  mTxtAttchment.setText(selectedFilePath);
//            Log.e(TAG,"destination   535   "+destination);
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


                    if (ContextCompat.checkSelfPermission(ShoppingListActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ShoppingListActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
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


                                            Log.e(TAG,"785  "+getString(R.string.app_name));
                                           // destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"test.jpg");
//                                            File direct = new File(Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.app_name)+"/");
//                                            destination = new File(Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.app_name)+"/"+"IMG_" + System.currentTimeMillis() + ".jpg");
                                            //destination = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getResources().getString(R.string.app_name), "IMG_" + System.currentTimeMillis() + ".jpg");
//                                            destination = new File(Environment.getExternalStorageDirectory() + "/" + getResources().getString(R.string.app_name), "IMG_" + System.currentTimeMillis() + ".jpg");
//                                            if (!destination.getParentFile().exists()){
//                                                destination.getParentFile().mkdirs();
//                                            }
//                                            if (!destination.exists())
//                                            {
//                                                destination.createNewFile();
//
//                                            }

//                                            if (!destination.exists()){
//                                                destination.mkdirs();
//                                            }
//                                            if (!destination.exists())
//                                            {
//                                                destination.createNewFile();
//
//                                            }

//                                            "/" + getResources().getString(R.string.app_name)
//                                            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/"+ getResources().getString(R.string.app_name));
                                            File docsFolder = new File(Environment.getExternalStorageDirectory() + "/Download"+ "/" + getResources().getString(R.string.app_name));
                                            boolean isPresent = true;
                                            if (!docsFolder.exists()) {
                                                isPresent = docsFolder.mkdir();
                                            }
                                            else {
                                                // Failure
                                                docsFolder.createNewFile();
                                            }
                                            if (isPresent) {
                                                destination = new File(docsFolder.getAbsolutePath(),"IMG_" + System.currentTimeMillis() +".jpg");
                                            } else {
                                                // Failure
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
                        if (ContextCompat.checkSelfPermission(ShoppingListActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(ShoppingListActivity.this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
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

                try {
                    imgPath = destination.getAbsolutePath();
                    destination = new File(imgPath);
                    if(destination.exists())
                    {
                        Bitmap myBitmap = BitmapFactory.decodeFile(destination.getAbsolutePath());
                        imgv1.setImageBitmap(myBitmap);
                    }

                }catch (Exception e){
                    Log.e(TAG,"Exception   487   "+e.toString());
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
                //"font/*",
                //"image/*",
                //essage/*",
                //"model/*",
                //"multipart/*",
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
        final String[] menulist = new String[]{"Home","My Cart","Track Orders", "Favourite Items", "Favourite Stores",
                "Notifications", "My Profile", "About Us", "Logout", "Share",
                "Contact Us", "Rate Us", "Terms & Conditions", "Privacy Policies",
                "Suggestion", "FAQ"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,R.drawable.ic_store,
                R.drawable.ic_notifications, R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share,
                R.drawable.contact, R.drawable.rate, R.drawable.tnc, R.drawable.pp,
                R.drawable.navsuggestion, R.drawable.navfaq
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ShoppingListActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ShoppingListActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ShoppingListActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ShoppingListActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ShoppingListActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 4) {
                    startActivity(new Intent(ShoppingListActivity.this, FavouriteStoreActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 5) {
                    startActivity(new Intent(ShoppingListActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 6) {
                    startActivity(new Intent(ShoppingListActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 7) {
                    startActivity(new Intent(ShoppingListActivity.this, AboutUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 8) {
                    doLogout();
                    drawer.closeDrawer(GravityCompat.START);
                }else if (position == 9) {
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("text/plain");
                    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    share.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + "\t" + "Invite You  \n\n For Android Users \n http://play.google.com/store/apps/details?id=" + getPackageName() +
                            "\n" );
                    startActivity(Intent.createChooser(share, "Invite this App to your friends"));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 10) {
                    startActivity(new Intent(ShoppingListActivity.this, ContactUsActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 11) {

                    try {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListActivity.this);
                        LayoutInflater inflater1 = (LayoutInflater) ShoppingListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                else if (position == 12) {
                    startActivity(new Intent(ShoppingListActivity.this, TermsnConditionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 13) {
                    startActivity(new Intent(ShoppingListActivity.this, PrivacyActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 14) {
                    startActivity(new Intent(ShoppingListActivity.this, SuggestionActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if (position == 15) {
                    startActivity(new Intent(ShoppingListActivity.this, FAQActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }
    private void setHomeNavMenu1() {
        final String[] menulist = new String[]{"Home","My Cart","My Orders", "Favourites",
                "Notifications", "My Profile", "About Us", "Logout", "Share"};
        Integer[] imageId = {
                R.drawable.ic_home,R.drawable.ic_cart, R.drawable.ic_order, R.drawable.ic_favorite,
                R.drawable.ic_notifications, R.drawable.ic_member,
                R.drawable.ic_about, R.drawable.ic_logout, R.drawable.ic_share
        };
        NavMenuAdapter adapter = new NavMenuAdapter(ShoppingListActivity.this, menulist, imageId);
        lvNavMenu.setAdapter(adapter);
        lvNavMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (position == 0) {
                    startActivity(new Intent(ShoppingListActivity.this, HomeActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 1) {
                    Intent i = new Intent(ShoppingListActivity.this, CartActivity.class);
                    i.putExtra("From", "Home");
                    startActivity(i);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 2) {
                    startActivity(new Intent(ShoppingListActivity.this, OrdersActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 3) {
                    startActivity(new Intent(ShoppingListActivity.this, FavouriteActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 4) {
                    startActivity(new Intent(ShoppingListActivity.this, NotificationActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                }  else if (position == 5) {
                    startActivity(new Intent(ShoppingListActivity.this, ProfileActivity.class));
                    drawer.closeDrawer(GravityCompat.START);
                } else if (position == 6) {
                    startActivity(new Intent(ShoppingListActivity.this, AboutUsActivity.class));
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
            }
        });
    }

    private void doLogout() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ShoppingListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

                        startActivity(new Intent(ShoppingListActivity.this,SplashActivity.class));
                        finish();}
                    else{
                        startActivity(new Intent(ShoppingListActivity.this,LocationActivity.class));
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

    private void setBottomBar() {
        navigation= (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().setGroupCheckable(0, false, true);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected( MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_category:
                    Intent io = new Intent(ShoppingListActivity.this, OutShopActivity.class);
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
                    Intent iha = new Intent(ShoppingListActivity.this, HomeActivity.class);
                    iha.putExtra(fromFavorite, valueFavorite);
                    startActivity(iha);
                    return true;
                case R.id.navigation_favorite:
                    Log.e(TAG,"navigation_favorite    ");
                    Intent ifa = new Intent(ShoppingListActivity.this, FavouriteActivity.class);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingListActivity.this);
            LayoutInflater inflater1 = (LayoutInflater) ShoppingListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

