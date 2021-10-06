package com.perfect.easyshopplus.Utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.perfect.easyshopplus.DB.DBHandler;

public class Config {

  /*  private static Context context;

    public Config(Context context){
        this.context = context;
    }*/
//    public static final String BASEURL = "https://40.81.77.56:83/shopawayapidemo/api/";  //DEMO
//    public static final String IMAGEURL = "https://40.81.77.56:83/shopawayapidemo"; //DEMO

  /*  public static final String BASEURL = "https://40.81.77.56:86/touchnbuyapidemo/api/";  //DEMO
    public static final String IMAGEURL = "https://40.81.77.56:86/touchnbuyapidemo"; //DEMO*/

//    public static final String BASEURL = "https://202.21.32.35:14001/TouchNBuyAPIDEMO/api/";  //DEMO
//    public static final String IMAGEURL = "http://202.21.32.35:91/TouchNBuyAPIDEMO"; //DEMO

  /*  public static final String BASEURL = "https://202.21.32.35:14001/touchnbuyapiship/api/"; //SHIPYARD
    public static final String IMAGEURL = "https://202.21.32.35:14001/touchnbuyapiship";//SHIPYARD*/

    /* public static final String BASEURL = "https://40.81.77.56:93/touchnbuyapifamily/api/";  //FAMILY MART
    public static final String IMAGEURL = "https://40.81.77.56:93/touchnbuyapifamily"; //FAMILY MART*/

//     public static final String BASEURL = "https://202.21.32.35:14050/TouchNBuyApiPlus/api/";  //demo
//    public static final String IMAGEURL = "https://202.21.32.35:14050/TouchNBuyApiPlus"; //demo
//

//     public static final String BASEURL = "https://202.21.32.35:14001/TouchNBuyAPIQA/api/";  //QA
//    public static final String IMAGEURL = "https://202.21.32.35:14001/TouchNBuyAPIQA"; //QA

//    public static final String BASEURL = "https://202.21.32.35:14001/TouchNBuyAPI/api/"; //debug
//    public static final String IMAGEURL = "http://202.21.32.35:91/TouchNBuyAPI"; //debug

    /*  public static final String BASEURL = "https://40.81.77.56:14001/touchnbuyapilive/api/"; //Live
      public static final String IMAGEURL = "https://40.81.77.56:14001/touchnbuyapilive"; //Live*/

   /* public static final String BASEURL = "https://117.221.70.96:14090/touchnbuyapi/api/"; //shipyard Live
    public static final String IMAGEURL = "http://117.221.70.96:84/touchnbuyapi"; //shipyard Live*/

    //public static final String BASEURL = "https://104.211.115.238/sHOPAWAYaPI/api/"; //SHOPAWAY Live
    //public static final String IMAGEURL = "https://104.211.115.238/sHOPAWAYaPI"; //SHOPAWAY Live

//    public static final String BASEURL = "https://104.211.115.238/shopawayapiasiademo/api/"; //SHOPAWAY Demo
//    public static final String IMAGEURL = "https://104.211.115.238/shopawayapiasiademo"; //SHOPAWAY Demo

   /* public static final String BASEURL = "https://web.shopaway.asia/shopawayapi/api/"; //SHOPAWAY Demo
    public static final String IMAGEURL = "https://web.shopaway.asia/shopawayapi"; //SHOPAWAY Demo*/

  /*  public static final String BASEURL = "https://demo.shopaway.shop/shopawayapi/api/"; //SHOPAWAY Demo
    public static final String IMAGEURL = "https://demo.shopaway.shop/shopawayapi"; //SHOPAWAY Demo*/

    public static boolean isGPSEnable  = false;
    public static final String SHARED_PREF  = "loginsession";
    public static final String SHARED_PREF1 = "userid";
    public static final String SHARED_PREF2 = "username";
    public static final String SHARED_PREF3 = "useremail";
    public static final String SHARED_PREF4 = "userphoneno";
    public static final String SHARED_PREF5 = "memberid";
    public static final String SHARED_PREF6 = "FK_CustomerPlus";
    public static final String SHARED_PREF7 = "ID_Store";
    public static final String SHARED_PREF8 = "StoreName";
    public static final String SHARED_PREF9 = "ShopType";
    public static final String SHARED_PREF10= "Latitude";
    public static final String SHARED_PREF11= "Longitude";
    public static final String SHARED_PREF12= "ID_Store_Inshop";
    public static final String SHARED_PREF13= "StoreName_Inshop";
    public static final String SHARED_PREF14= "notificationcount";
    public static final String SHARED_PREF15= "Address";
    public static final String SHARED_PREF16= "PIN";
    public static final String SHARED_PREF17= "AddressID";
    public static final String SHARED_PREF18= "DeliUsername";
    public static final String SHARED_PREF19= "DeliAddress";
    public static final String SHARED_PREF20= "DeliAddressID";
    public static final String SHARED_PREF21= "DeliPincode";
    public static final String SHARED_PREF22= "DeliArea";
    public static final String SHARED_PREF23= "DeliAreaID";
    public static final String SHARED_PREF24= "DeliLandmark";
    public static final String SHARED_PREF25= "DeliMobNumb";
    public static final String SHARED_PREF26= "StoreImage";
    public static final String SHARED_PREF27= "homedelivery";
    public static final String SHARED_PREF28= "Landmark";
    public static final String SHARED_PREF29= "MinimumDeliveryAmount";
    public static final String SHARED_PREF30= "DeliveryCriteria";
    public static final String SHARED_PREF31= "ResellerName";
    public static final String SHARED_PREF32= "SplashImageCode";
    public static final String SHARED_PREF33= "AppIconImageCode";
    public static final String SHARED_PREF34= "CompanyLogoImageCode";
    public static final String SHARED_PREF35= "ProductName";
    public static final String SHARED_PREF36= "RequiredStore";
    public static final String SHARED_PREF37= "RequiredStoreCategory";
    public static final String SHARED_PREF38= "LogoImageCode";
    public static final String SHARED_PREF39= "ExpressDelivery";
    public static final String SHARED_PREF40= "ExpressDeliveryAmount";
    public static final String SHARED_PREF41= "HomeIconImageCode";
    public static final String SHARED_PREF42= "RequiredBranch";
    public static final String SHARED_PREF43= "RequiredInshop";
    public static final String SHARED_PREF44= "Requiredcounterpickup";
    public static final String SHARED_PREF45= "RequiredShoppinglist";
    public static final String SHARED_PREF46= "DeliveryCharge";
    public static final String SHARED_PREF47= "StoreCategory";
    public static final String SHARED_PREF48= "PlayStoreLink";
    public static final String SHARED_PREF49= "HomeImage";
    public static final String SHARED_PREF50= "OTPEmailSend";
    public static final String SHARED_PREF51= "CategoryList";
    public static final String SHARED_PREF52= "SubCategoryList";
    public static final String SHARED_PREF53= "CashOnDelivery";
    public static final String SHARED_PREF54= "OnlinePayment";
    public static final String SHARED_PREF55= "OTPMsgSend";
    public static final String SHARED_PREF56= "BaseURL";
    public static final String SHARED_PREF57= "ImageURL";
    public static final String SHARED_PREF58= "country";
    public static final String SHARED_PREF59= "UPIID";
    public static final String SHARED_PREF60= "TimeSlotCheck";
    public static final String SHARED_PREF61= "TimeSlotCheckReorder";
    public static final String SHARED_PREF_PRIVACYPOLICY= "PRIVACYPOLICY";
    public static final String SHARED_PREF_TERMSCO= "termsco";
    public static final String SHARED_PREF_ABOUT_US= "about_us";
    public static final String SHARED_PREF_CONTACT_US= "contact_us";
    public static final String SHARED_PREF62= "OnlinePaymentMethods";
    public static final String SHARED_PREF63= "Area";
    public static final String SHARED_PREF64= "AreaID";





    public static final String SHARED_PREF80= "ID_Languages";


    public static final String SHARED_PREF65= "availabilityofthestockpackaging";
    public static final String SHARED_PREF66= "GPSisdisabledinyourdevice";
    public static final String SHARED_PREF67= "GotoSettingsPageToEnableGPS";
    public static final String SHARED_PREF68= "PleaseacceptTermsandConditions";
    public static final String SHARED_PREF69= "Pleaseselectdeliverytime";
    public static final String SHARED_PREF70= "sorryonedayprocessdeliciousorderselectnextday";
    public static final String SHARED_PREF71= "PleaseselectdeliveryDate";
    public static final String SHARED_PREF72= "Somethingwentwrong";
    public static final String SHARED_PREF73= "HolidayPleaseselectotherdate";
    public static final String SHARED_PREF74= "LocationnotDetected";
    public static final String SHARED_PREF75= "LocationNotFound";
    public static final String SHARED_PREF76= "GPSisEnabledinyourdevice";
    public static final String SHARED_PREF77= "permissiondenied";
    public static final String SHARED_PREF78= "Servicesforyourselectedcountryisnotavailable";
    public static final String SHARED_PREF79= "ReloadingFailedPleasecheckyourinternetconnection";
    public static final String SHARED_PREF81= "Languages";
    public static final String SHARED_PREF82= "ShortName";
    public static final String SHARED_PREF83= "SelectYourCountry";
    public static final String SHARED_PREF84= "GetStarted";
    public static final String SHARED_PREF85= "Signup";
    public static final String SHARED_PREF86= "Login";
    public static final String SHARED_PREF87= "Register";
    public static final String SHARED_PREF88= "CustomerName";
    public static final String SHARED_PREF89= "MobileNo";
    public static final String SHARED_PREF90= "Password";
    public static final String SHARED_PREF91= "confirmPassword";
    public static final String SHARED_PREF92= "Emailid";
    public static final String SHARED_PREF93= "Emailid_Optional";
    public static final String SHARED_PREF94= "address";
    public static final String SHARED_PREF95= "Address_Optional";
    public static final String SHARED_PREF96= "LandmarkS";
    public static final String SHARED_PREF97= "Landmark_Optional";
    public static final String SHARED_PREF98= "Optional";
    public static final String SHARED_PREF99= "SendOTP";
    public static final String SHARED_PREF100= "PleaseProvideyourname";
    public static final String SHARED_PREF101= "PleaseProvideyourPhoneNo";
    public static final String SHARED_PREF102= "EnterYourOTP";
    public static final String SHARED_PREF103= "EnterOTP";
    public static final String SHARED_PREF104= "OK";
    public static final String SHARED_PREF105= "Cancel";
    public static final String SHARED_PREF106= "ShowPassword";
    public static final String SHARED_PREF107= "ForgotPassword";
    public static final String SHARED_PREF108= "NotaMember";
    public static final String SHARED_PREF109= "RegisterNow";
    public static final String SHARED_PREF110= "PleaseProvideyourMobilenumber";
    public static final String SHARED_PREF111= "PleaseProvideyourPassword";
    public static final String SHARED_PREF112= "LetsGo";
    public static final String SHARED_PREF113= "sort";
    public static final String SHARED_PREF114= "filter";
    public static final String SHARED_PREF115= "MRP";
    public static final String SHARED_PREF116= "Outofstock";
    public static final String SHARED_PREF117= "yousaved";
    public static final String SHARED_PREF118= "SearchForProducts";
    public static final String SHARED_PREF119= "Store";
    public static final String SHARED_PREF120= "similarproducts";
    public static final String SHARED_PREF121= "addtocart";
    public static final String SHARED_PREF122= "quantity";
    public static final String SHARED_PREF123= "mycart";
    public static final String SHARED_PREF124= "continueshopping";
    public static final String SHARED_PREF125= "yourcartisempty";
    public static final String SHARED_PREF126= "additemstoitnow";
    public static final String SHARED_PREF127= "shopnow";
    public static final String SHARED_PREF128= "clearall";
    public static final String SHARED_PREF129= "totalitems";
    public static final String SHARED_PREF130= "qty";
    public static final String SHARED_PREF131= "totalamount";
    public static final String SHARED_PREF132= "proceed";
    public static final String SHARED_PREF133= "chooseyourdeliveryoptions";
    public static final String SHARED_PREF134= "storepickup";
    public static final String SHARED_PREF135= "doordelivery";
    public static final String SHARED_PREF136= "favoritesitems";
    public static final String SHARED_PREF137= "chooseyourpickupdateandtime";
    public static final String SHARED_PREF138= "selecttime";
    public static final String SHARED_PREF139= "pricedetail";
    public static final String SHARED_PREF140= "item";
    public static final String SHARED_PREF141= "othercharges";
    public static final String SHARED_PREF142= "amountpayable";
    public static final String SHARED_PREF143= "iacceptthetermsAndconditions";
    public static final String SHARED_PREF144= "thankyouforpurchasingfromus";
    public static final String SHARED_PREF145= "orderconfirmed";
    public static final String SHARED_PREF146= "backtohome";
    public static final String SHARED_PREF147= "checkyourorder";
    public static final String SHARED_PREF148= "all";
    public static final String SHARED_PREF149= "pending";
    public static final String SHARED_PREF150= "confirmed";
    public static final String SHARED_PREF151= "packed";
    public static final String SHARED_PREF152= "delivered";
    public static final String SHARED_PREF153= "search";
    public static final String SHARED_PREF154= "fromdate";
    public static final String SHARED_PREF155= "todate";
    public static final String SHARED_PREF156= "ordernumber";
    public static final String SHARED_PREF157= "ordertype";
    public static final String SHARED_PREF158= "orderedon";
    public static final String SHARED_PREF159= "closedon";
    public static final String SHARED_PREF160= "OrderDetails";
    public static final String SHARED_PREF161= "ItemsOrdered";
    public static final String SHARED_PREF162= "Subtotal";
    public static final String SHARED_PREF163= "Items";
    public static final String SHARED_PREF164= "Discount";
    public static final String SHARED_PREF165= "memberdiscount";
    public static final String SHARED_PREF166= "GrandTotal";
    public static final String SHARED_PREF167= "AmountPaid";
    public static final String SHARED_PREF168= "Reorder";
    public static final String SHARED_PREF169= "OrderedItems";
    public static final String SHARED_PREF170= "paidamount";
    public static final String SHARED_PREF171= "Ordereddate";
    public static final String SHARED_PREF172= "addnewitem";
    public static final String SHARED_PREF173= "addtoorderlist";
    public static final String SHARED_PREF174= "sortby";
    public static final String SHARED_PREF175= "pricelowtohigh";
    public static final String SHARED_PREF176= "pricehightolow";
    public static final String SHARED_PREF177= "minprice";
    public static final String SHARED_PREF178= "maxprice";
    public static final String SHARED_PREF179= "clear";
    public static final String SHARED_PREF180= "apply";
    public static final String SHARED_PREF181= "shopbycategory";
    public static final String SHARED_PREF182= "gotocart";
    public static final String SHARED_PREF183= "categoryname";
    public static final String SHARED_PREF184= "Confirmexit";
    public static final String SHARED_PREF185= "areyousureyouwanttoexit";
    public static final String SHARED_PREF186= "yes";
    public static final String SHARED_PREF187= "no";
    public static final String SHARED_PREF188= "trackorder";
    public static final String SHARED_PREF189= "favouriteitems";
    public static final String SHARED_PREF190= "notifications";
    public static final String SHARED_PREF191= "aboutus";
    public static final String SHARED_PREF192= "termsandconditions";
    public static final String SHARED_PREF193= "Privacypolicies";
    public static final String SHARED_PREF194= "suggetions";
    public static final String SHARED_PREF195= "faq";
    public static final String SHARED_PREF196= "rateus";
    public static final String SHARED_PREF197= "share";
    public static final String SHARED_PREF198= "ShortName";
    public static final String SHARED_PREF199= "logout";
    public static final String SHARED_PREF200= "welcome";
    public static final String SHARED_PREF201= "Deliveryaddress";
    public static final String SHARED_PREF202= "emptyshippingaddress";
    public static final String SHARED_PREF203= "addaddress";
    public static final String SHARED_PREF204= "editadress";
    public static final String SHARED_PREF205= "choosedeliverydateandtime";
    public static final String SHARED_PREF206= "selectdate";
    public static final String SHARED_PREF207= "starttime";
    public static final String SHARED_PREF208= "endtime";
    public static final String SHARED_PREF209= "remarks";
    public static final String SHARED_PREF210= "ordersummary";
    public static final String SHARED_PREF211= "DeliveryCharges";
    public static final String SHARED_PREF212= "youhavesaved";
    public static final String SHARED_PREF213= "paymenttype";
    public static final String SHARED_PREF214= "expressdelivery";
    public static final String SHARED_PREF215= "extraamountfordelivery";
    public static final String SHARED_PREF216= "iaccepttermsAndconditions";
    public static final String SHARED_PREF217= "placeorder";
    public static final String SHARED_PREF218= "markallasread";
    public static final String SHARED_PREF219= "orderno";
    public static final String SHARED_PREF220= "notificationdetails";
    public static final String SHARED_PREF221= "myprofile";
    public static final String SHARED_PREF222= "changepassword";
    public static final String SHARED_PREF223= "area";
    public static final String SHARED_PREF224= "selectarea";
    public static final String SHARED_PREF225= "oldpassword";
    public static final String SHARED_PREF226= "newpassword";
    public static final String SHARED_PREF227= "save";
    public static final String SHARED_PREF228= "versioncode";
    public static final String SHARED_PREF229= "technologypartner";
    public static final String SHARED_PREF230= "contactus";
    public static final String SHARED_PREF231= "privacypolicy";
    public static final String SHARED_PREF232= "feedback";
    public static final String SHARED_PREF233= "reportanerror";
    public static final String SHARED_PREF234= "giveasuggestion";
    public static final String SHARED_PREF235= "anythingelse";
    public static final String SHARED_PREF236= "submit";
    public static final String SHARED_PREF237= "frequentlyaskedquestions";
    public static final String SHARED_PREF238= "wewantyourfeedback";
    public static final String SHARED_PREF239= "lovethisapprateus";
    public static final String SHARED_PREF240= "ratenow";
    public static final String SHARED_PREF241= "Logoutaccount";
    public static final String SHARED_PREF242= "areyousureyouwanttologout";
    public static final String SHARED_PREF243= "Changelanguage";
    public static final String SHARED_PREF244= "favoriteStore";
    public static final String SHARED_PREF245= "Store_Name";
    public static final String SHARED_PREF246= "AreaName";
    public static final String SHARED_PREF247= "Pincode";
    public static final String SHARED_PREF248= "EnterStore";
    public static final String SHARED_PREF249= "EnterArea";
    public static final String SHARED_PREF250= "EnterPincode";
    public static final String SHARED_PREF251= "StoreLocations";
    public static final String SHARED_PREF252= "Thereisnonotificationstoshow";
    public static final String SHARED_PREF253= "Homedeliveryoptionwillstartshortly";
    public static final String SHARED_PREF254= "MinimumamountHomedelivery";
    public static final String SHARED_PREF255= "Homeshortlypleasedocounterpickupdeliveryoption";
    public static final String SHARED_PREF256= "Pleaseupdateallitemquantity";
    public static final String SHARED_PREF260= "ChangeAddress";
    public static final String SHARED_PREF261= "usernameandpasswordareverified";
    public static final String SHARED_PREF262= "ThereissometechnicalissuesPleaseuseanotherpaymentoptions";
    public static final String SHARED_PREF263= "PleaseuseanotherpaymentoptionsGooglePayisnotinstalledinyourdevice";
    public static final String SHARED_PREF264= "Pleaseselectanypaymentoption";
    public static final String SHARED_PREF265= "PleaseselectdeliveryStarttimeEndtime";
    public static final String SHARED_PREF266= "Pleaseselectdeliveryendtime";
    public static final String SHARED_PREF267= "Pleaseselectdeliverystarttime";
    public static final String SHARED_PREF268= "Androidversionneedsanupgrade";
    public static final String SHARED_PREF269= "Thisappisntcompatiblewithyourdevice";
    public static final String SHARED_PREF270= "Description";
    public static final String SHARED_PREF271= "AddressList";
    public static final String SHARED_PREF272= "Thereisnoaddresslisttoshow";
    public static final String SHARED_PREF273= "Getlocation";
    public static final String SHARED_PREF274= "PermanentAddress";
    public static final String SHARED_PREF275= "ShippingAddresslist";
    public static final String SHARED_PREF276= "PhoneNo";
    public static final String SHARED_PREF277= "MinDeliveryAmt";
    public static final String SHARED_PREF278= "Storeaddedtothefavourites";
    public static final String SHARED_PREF279= "Storeremovedfromthefavourites";
    public static final String SHARED_PREF280= "SomethingwentwrongTryagainlater";
    public static final String SHARED_PREF281= "Nodatafound";
    public static final String SHARED_PREF282= "Nointernetconnection";
    public static final String SHARED_PREF283= "AddToOrder";
    public static final String SHARED_PREF284= "ItemaddedtotheReorder";
    public static final String SHARED_PREF285= "Youarenotinsideornearthestoresoyoucantselectinshoppleaseselectoutshopforpurchase";
    public static final String SHARED_PREF286= "Storelocationnotfound";
    public static final String SHARED_PREF287= "LocationsettingsareinadequateandcannotbefixedhereFixinSettings";
    public static final String SHARED_PREF288= "InviteYou";
    public static final String SHARED_PREF289= "ForAndroidUsers";
    public static final String SHARED_PREF290= "Youwillgetaccessonlyifyouareinsideornearthestore";
    public static final String SHARED_PREF291= "PleaseprovideMinimumPrice";
    public static final String SHARED_PREF292= "PleaseprovideMaximumPrice";
    public static final String SHARED_PREF293= "GPSisdisabledinyourdeviceYouShouldenableittoproceedyourpurchase";
    public static final String SHARED_PREF294= "Pleaseentername";
    public static final String SHARED_PREF295= "Pleaseenteraddress";
    public static final String SHARED_PREF296= "Pleaseselectarea";
    public static final String SHARED_PREF297= "Pleaseenterlandmark";
    public static final String SHARED_PREF298= "OrderhasbeenRemovedSuccessfully";
    public static final String SHARED_PREF299= "Thankyouforyoursupport";
    public static final String SHARED_PREF300= "HomeDeliverys";
    public static final String SHARED_PREF301= "Verifiedon";
    public static final String SHARED_PREF302= "Packedon";
    public static final String SHARED_PREF303= "Deliveredon";
    public static final String SHARED_PREF304= "at";
    public static final String SHARED_PREF305= "CounterPickup";
    public static final String SHARED_PREF306= "AreyousureDoyouwanttodeletethisorder";
    public static final String SHARED_PREF307= "OrderhasbeenRemoved";
    public static final String SHARED_PREF308= "Howwasyourexperiencewiththisstore";
    public static final String SHARED_PREF309= "Itemupdatedtothereorder";
    public static final String SHARED_PREF310= "PleaseAddvalidAddress";
    public static final String SHARED_PREF311= "OrderSubmittedFailed";
    public static final String SHARED_PREF312= "";
    public static final String SHARED_PREF313= "UploadPrescription";
    public static final String SHARED_PREF314= "Browse";
    public static final String SHARED_PREF315= "Pleaseuploadimage";
    public static final String SHARED_PREF316= "CameraPermissionerror";
    public static final String SHARED_PREF317= "Price";
    public static final String SHARED_PREF318= "amountpayablelessthanminimumDeliveryamount";
    public static final String SHARED_PREF319= "lastproductoforder";
    public static final String SHARED_PREF320= "AreyousureDoyouwanttodeletethisproductfromorder";
    public static final String SHARED_PREF321= "EditQuantity";
    public static final String SHARED_PREF322= "GST";
    public static final String SHARED_PREF323= "Included";
    public static final String SHARED_PREF324= "Upload";
    public static final String SHARED_PREF325= "Transactionsuccessful";
    public static final String SHARED_PREF326= "Paymentcancelledbyuser";
    public static final String SHARED_PREF327= "TransactionfailedPleasetryagain";
    public static final String SHARED_PREF328= "Errorinpayment";
    public static final String SHARED_PREF329= "PaymentSuccessfully";
    public static final String SHARED_PREF330= "Paymentfailed";
    public static final String SHARED_PREF331= "PleaseprovideKeywordforsearch";
    public static final String SHARED_PREF332= "Itemaddedtothefavourites";
    public static final String SHARED_PREF333= "ItemAddedToYourOrderList";
    public static final String SHARED_PREF334= "ItemAddedToYourOrderListFailed";
    public static final String SHARED_PREF335= "Itemquantityupdatedtothecart";
    public static final String SHARED_PREF336= "Pleaseupdatequantitytothecart";
    public static final String SHARED_PREF337= "Itemaddedtothecart";
    public static final String SHARED_PREF338= "Itemremovedfromthefavourites";
    public static final String SHARED_PREF339= "Itemremovedfromthecart";
    public static final String SHARED_PREF344= "Thereisnoorderlisttoshow";
    public static final String SHARED_PREF345= "Thereisnofavouriteitemstoshow";
    public static final String SHARED_PREF346= "Thereisnofavouritestoretoshow";
    public static final String SHARED_PREF347= "EnterNewPassword";
    public static final String SHARED_PREF348= "Scanorinsertbarcodeontheitemandpleaseaddittoyourcart";
    public static final String SHARED_PREF349= "InsertCode";
    public static final String SHARED_PREF350= "ScanCode";
    public static final String SHARED_PREF351= "Noitemfoundwhilescanning";
    public static final String SHARED_PREF352= "Pleaseprovidebarcodevalue";
    public static final String SHARED_PREF353= "EnterOldPassword";
    public static final String SHARED_PREF354= "Invalidtime";
    public static final String SHARED_PREF355= "Selecttimegreaterthancurrenttime";
    public static final String SHARED_PREF356= "SelectStore";
    public static final String SHARED_PREF357= "Sendemail";
    public static final String SHARED_PREF358= "EmailAddressMobileNumber";
    public static final String SHARED_PREF359= "EnterEmailAddress";
    public static final String SHARED_PREF360= "EnterMobileNumber";
    public static final String SHARED_PREF361= "NewPasswordissendtoyournumber";
    public static final String SHARED_PREF362= "EnterValidMobileNumber";
    public static final String SHARED_PREF363= "Pleaseprovideotp";
    public static final String SHARED_PREF364= "PleaseprovideavalidEmailAddress";
    public static final String SHARED_PREF365= "YourPasswordandconfirmationpassworddonotmatch";
    public static final String SHARED_PREF366= "PleaseprovideyourConfirmPassword";
    public static final String SHARED_PREF367= "OTPissendtoyourregisteredMobilenumber";
    public static final String SHARED_PREF368= "errorinscanning";
    public static final String SHARED_PREF369= "Camerapermissiondenied";
    public static final String SHARED_PREF370= "PleaseEnterQuantity";
    public static final String SHARED_PREF371= "Selected";
    public static final String SHARED_PREF372= "PermissionGranted";
    public static final String SHARED_PREF257= "Pleasewaityourorderisunderprocessing";
    public static final String SHARED_PREF258= "pleaseselectcorrectdateinterval";
    public static final String SHARED_PREF259= "Noitemfound";

    public static final String EMAIL ="";
    public static final String PASSWORD ="";

    public static final String SHARED_PREF340 = "MerchantKey";
    public static final String SHARED_PREF341 = "SaltKey";
    public static final String SHARED_PREF342 = "Mobile";
    public static final String SHARED_PREF343 = "BranchEmail";


    public static final String SHARED_PREF373= "StrDeliveryDate";
    public static final String SHARED_PREF374= "DeliveryTime";
    public static final String SHARED_PREF375= "StrRemark";
    public static final String SHARED_PREF376= "inExpressdelivery";
    public static final String SHARED_PREF377= "strLongitude";
    public static final String SHARED_PREF378= "strLatitude";
    public static final String SHARED_PREF379= "fileimage";
    public static final String SHARED_PREF380= "strPaymenttype";
    public static final String SHARED_PREF381= "finalamount";

    public static final String SHARED_PREF382= "ScratchCard";

    public static final String SHARED_PREF383= "JustUploadyourPrescriptiontoplaceorder";
    public static final String SHARED_PREF384= "GiftVoucher";
    public static final String SHARED_PREF385= "Earnyourrewards";
    public static final String SHARED_PREF386= "TotalRewards";
    public static final String SHARED_PREF387= "redeemAmount";

    public static final String SHARED_PREF388= "YourRewards";
    public static final String SHARED_PREF389= "GiftCoupons";
    public static final String SHARED_PREF390= "MyRewards";
    public static final String SHARED_PREF391= "BalancetoRedeem";
    public static final String SHARED_PREF392= "Rewards";
    public static final String SHARED_PREF393= "Doyouwanttoreddemfromyourrewards";
    public static final String SHARED_PREF394= "Yourreward";
    public static final String SHARED_PREF395= "Apply";
    public static final String SHARED_PREF396= "RedeemYourRewards";
    public static final String SHARED_PREF397= "ApplyGiftCoupon";
    public static final String SHARED_PREF398= "RedeemAmount";
    public static final String SHARED_PREF399= "EnterRedeemAmount";
    public static final String SHARED_PREF400= "betterlucknexttime";
    public static final String SHARED_PREF401= "ScratchWin";
    public static final String SHARED_PREF402= "ConfirmOrderNo";
    public static final String SHARED_PREF403= "strPaymentId";
    public static final String SHARED_PREF404= "FK_SalesOrder";
    public static final String SHARED_PREF405= "FK_SalesOrder_new";

    public static final String SHARED_PREF406= "CreateAccount";
    public static final String SHARED_PREF407= "signuptogetstarted";
    public static final String SHARED_PREF408= "PleseProvideavalidemailaddress";
    public static final String SHARED_PREF409= "Pleaseenterthecodewassentinyourphonenumber";
    public static final String SHARED_PREF410= "signintocontinue!";

    public static final String SHARED_PREF411= "Continue";
    public static final String SHARED_PREF412= "Saveforlater";
    public static final String SHARED_PREF413= "Remove";
    public static final String SHARED_PREF414= "PaymentMode";
    public static final String SHARED_PREF415= "PayNow";
    public static final String SHARED_PREF416= "ShippingAddress";
    public static final String SHARED_PREF417= "SecurePayment|GenuineProducts";
    public static final String SHARED_PREF418= "Selectanaddresstoplacetheorder";
    public static final String SHARED_PREF419= "ADDNEW";
    public static final String SHARED_PREF420= "ProductPrice";

    public static final String SHARED_PREF421= "CASHONDELIVERYAVAILABLE";
    public static final String SHARED_PREF422= "SOLDBY";
    public static final String SHARED_PREF423= "GenuineProduct";
    public static final String SHARED_PREF424= "Qualitychecked";
    public static final String SHARED_PREF425= "Greatesavings";
    public static final String SHARED_PREF426= "PaymentStatus";
    public static final String SHARED_PREF427= "CartView";
    public static final String SHARED_PREF428= "Changepaymenttype";

    public static final String SHARED_PREF429= "PrivilageCardEnable";







    public static void logOut(Context context) {

        DBHandler db=new DBHandler(context);
        db.deleteallCart();
        db.deleteallInshopCart();
        db.deleteallFav();
        db.deleteallFavstore();

        SharedPreferences Loginpref = context.getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor logineditor = Loginpref.edit();
        logineditor.putString("loginsession", "No");
        logineditor.commit();

        SharedPreferences userid = context.getSharedPreferences(Config.SHARED_PREF1, 0);
        SharedPreferences.Editor userideditor = userid.edit();
        userideditor.putString("userid", "");
        userideditor.commit();

        SharedPreferences username = context.getSharedPreferences(Config.SHARED_PREF2, 0);
        SharedPreferences.Editor usernameeditor = username.edit();
        usernameeditor.putString("username", "");
        usernameeditor.commit();

        SharedPreferences useremail = context.getSharedPreferences(Config.SHARED_PREF3, 0);
        SharedPreferences.Editor useremailideditor = useremail.edit();
        useremailideditor.putString("useremail", "");
        useremailideditor.commit();

        SharedPreferences userphoneno = context.getSharedPreferences(Config.SHARED_PREF4, 0);
        SharedPreferences.Editor userphoneeditor = userphoneno.edit();
        userphoneeditor.putString("userphoneno", "");
        userphoneeditor.commit();

        SharedPreferences memberid = context.getSharedPreferences(Config.SHARED_PREF5, 0);
        SharedPreferences.Editor memberideditor = memberid.edit();
        memberideditor.putString("memberid", "");
        memberideditor.commit();

        SharedPreferences FK_CustomerPlus = context.getSharedPreferences(Config.SHARED_PREF6, 0);
        SharedPreferences.Editor FK_CustomerPluseditor = FK_CustomerPlus.edit();
        FK_CustomerPluseditor.putString("FK_CustomerPlus", "");
        FK_CustomerPluseditor.commit();


        SharedPreferences storeId = context.getSharedPreferences(Config.SHARED_PREF7, 0);
        SharedPreferences.Editor storeIdeditor = storeId.edit();
        storeIdeditor.putString("ID_Store", "");
        storeIdeditor.commit();

        SharedPreferences storename = context.getSharedPreferences(Config.SHARED_PREF8, 0);
        SharedPreferences.Editor storenameeditor = storename.edit();
        storenameeditor.putString("StoreName", "");
        storenameeditor.commit();

        SharedPreferences shoptype = context.getSharedPreferences(Config.SHARED_PREF9, 0);
        SharedPreferences.Editor shoptypeeditor = shoptype.edit();
        shoptypeeditor.putString("ShopType", "");
        shoptypeeditor.commit();

        SharedPreferences lattitude = context.getSharedPreferences(Config.SHARED_PREF10, 0);
        SharedPreferences.Editor lattitudeeditor = lattitude.edit();
        lattitudeeditor.putString("Latitude", "");
        lattitudeeditor.commit();

        SharedPreferences longitude = context.getSharedPreferences(Config.SHARED_PREF11, 0);
        SharedPreferences.Editor longitudeeditor = longitude.edit();
        longitudeeditor.putString("Longitude", "");
        longitudeeditor.commit();

        SharedPreferences inshopstoreid = context.getSharedPreferences(Config.SHARED_PREF12, 0);
        SharedPreferences.Editor inshopstoreideditor = inshopstoreid.edit();
        inshopstoreideditor.putString("ID_Store_Inshop", "");
        inshopstoreideditor.commit();

        SharedPreferences inshopstorename = context.getSharedPreferences(Config.SHARED_PREF13, 0);
        SharedPreferences.Editor inshopstorenameeditor = inshopstorename.edit();
        inshopstorenameeditor.putString("StoreName_Inshop", "");
        inshopstorenameeditor.commit();


        SharedPreferences notificationcount = context.getSharedPreferences(Config.SHARED_PREF14, 0);
        SharedPreferences.Editor notificationcounteditor = notificationcount.edit();
        notificationcounteditor.putString("notificationcount", "0");
        notificationcounteditor.commit();

        SharedPreferences Address = context.getSharedPreferences(Config.SHARED_PREF15, 0);
        SharedPreferences.Editor Addresseditor = Address.edit();
        Addresseditor.putString("Address", "");
        Addresseditor.commit();

        SharedPreferences PIN = context.getSharedPreferences(Config.SHARED_PREF16, 0);
        SharedPreferences.Editor PINeditor = PIN.edit();
        PINeditor.putString("PIN","");
        PINeditor.commit();

        SharedPreferences Address1ID = context.getSharedPreferences(Config.SHARED_PREF17, 0);
        SharedPreferences.Editor Address1IDeditor = Address1ID.edit();
        Address1IDeditor.putString("AddressID","");
        Address1IDeditor.commit();

        SharedPreferences DeliUsername = context.getSharedPreferences(Config.SHARED_PREF18, 0);
        SharedPreferences.Editor DeliUsernameeditor = DeliUsername.edit();
        DeliUsernameeditor.putString("DeliUsername","");
        DeliUsernameeditor.commit();

        SharedPreferences DeliAddress = context.getSharedPreferences(Config.SHARED_PREF19, 0);
        SharedPreferences.Editor DeliAddresseditor = DeliAddress.edit();
        DeliAddresseditor.putString("DeliAddress","");
        DeliAddresseditor.commit();

        SharedPreferences DeliAddressID =  context.getSharedPreferences(Config.SHARED_PREF20, 0);
        SharedPreferences.Editor DeliAddressIDeditor = DeliAddressID.edit();
        DeliAddressIDeditor.putString("DeliAddressID","");
        DeliAddressIDeditor.commit();

        SharedPreferences DeliPincode = context.getSharedPreferences(Config.SHARED_PREF21, 0);
        SharedPreferences.Editor DeliPincodeeditor = DeliPincode.edit();
        DeliPincodeeditor.putString("DeliPincode","");
        DeliPincodeeditor.commit();

        SharedPreferences DeliArea = context.getSharedPreferences(Config.SHARED_PREF22, 0);
        SharedPreferences.Editor DeliAreaeditor = DeliArea.edit();
        DeliAreaeditor.putString("DeliArea","");
        DeliAreaeditor.commit();

        SharedPreferences DeliAreaID = context.getSharedPreferences(Config.SHARED_PREF23, 0);
        SharedPreferences.Editor DeliAreaIDeditor = DeliAreaID.edit();
        DeliAreaIDeditor.putString("DeliAreaID","");
        DeliAreaIDeditor.commit();

        SharedPreferences DeliLandmark = context.getSharedPreferences(Config.SHARED_PREF24, 0);
        SharedPreferences.Editor DeliLandmarkeditor = DeliLandmark.edit();
        DeliLandmarkeditor.putString("DeliLandmark","");
        DeliLandmarkeditor.commit();

        SharedPreferences DeliMobNumb = context.getSharedPreferences(Config.SHARED_PREF25, 0);
        SharedPreferences.Editor DeliMobNumbeditor = DeliMobNumb.edit();
        DeliMobNumbeditor.putString("DeliMobNumb","");
        DeliMobNumbeditor.commit();

        SharedPreferences StoreImage = context.getSharedPreferences(Config.SHARED_PREF26, 0);
        SharedPreferences.Editor StoreImageeditor = StoreImage.edit();
        StoreImageeditor.putString("StoreImage","");
        StoreImageeditor.commit();

        SharedPreferences homedeliverySP = context.getSharedPreferences(Config.SHARED_PREF27, 0);
        SharedPreferences.Editor homedeliverySPeditor = homedeliverySP.edit();
        homedeliverySPeditor.putString("homedelivery", "");
        homedeliverySPeditor.commit();

        SharedPreferences Landmark = context.getSharedPreferences(Config.SHARED_PREF28, 0);
        SharedPreferences.Editor Landmarkeditor = Landmark.edit();
        Landmarkeditor.putString("Landmark", "");
        Landmarkeditor.commit();

        SharedPreferences MinimumDeliveryAmountSP = context.getSharedPreferences(Config.SHARED_PREF29, 0);
        SharedPreferences.Editor MinimumDeliveryAmounteditor = MinimumDeliveryAmountSP.edit();
        MinimumDeliveryAmounteditor.putString("MinimumDeliveryAmount", "");
        MinimumDeliveryAmounteditor.commit();

        SharedPreferences DeliveryCriteriaSP = context.getSharedPreferences(Config.SHARED_PREF30, 0);
        SharedPreferences.Editor DeliveryCriteriaeditor = DeliveryCriteriaSP.edit();
        DeliveryCriteriaeditor.putString("DeliveryCriteria", "");
        DeliveryCriteriaeditor.commit();

        SharedPreferences ResellerNameSP = context.getSharedPreferences(Config.SHARED_PREF31, 0);
        SharedPreferences.Editor ResellerNameeditor = ResellerNameSP.edit();
        ResellerNameeditor.putString("ResellerName", "");
        ResellerNameeditor.commit();

        SharedPreferences SplashImageCodeSP = context.getSharedPreferences(Config.SHARED_PREF32, 0);
        SharedPreferences.Editor SplashImageCodeeditor = SplashImageCodeSP.edit();
        SplashImageCodeeditor.putString("SplashImageCode", "");
        SplashImageCodeeditor.commit();

        SharedPreferences AppIconImageCodeSP = context.getSharedPreferences(Config.SHARED_PREF33, 0);
        SharedPreferences.Editor AppIconImageCodeeditor = AppIconImageCodeSP.edit();
        AppIconImageCodeeditor.putString("AppIconImageCode", "");
        AppIconImageCodeeditor.commit();

        SharedPreferences CompanyLogoImageCodeSP = context.getSharedPreferences(Config.SHARED_PREF34, 0);
        SharedPreferences.Editor CompanyLogoImageCodeeditor = CompanyLogoImageCodeSP.edit();
        CompanyLogoImageCodeeditor.putString("CompanyLogoImageCode", "");
        CompanyLogoImageCodeeditor.commit();

        SharedPreferences ProductNameSP = context.getSharedPreferences(Config.SHARED_PREF35, 0);
        SharedPreferences.Editor ProductNameeditor = ProductNameSP.edit();

        ProductNameeditor.putString("ProductName", "");
        ProductNameeditor.commit();

        SharedPreferences RequiredStoreSP = context.getSharedPreferences(Config.SHARED_PREF36, 0);
        SharedPreferences.Editor RequiredStoreeditor = RequiredStoreSP.edit();
        RequiredStoreeditor.putString("RequiredStore", "");
        RequiredStoreeditor.commit();

        SharedPreferences RequiredStoreCategorySP = context.getSharedPreferences(Config.SHARED_PREF37, 0);
        SharedPreferences.Editor RequiredStoreCategoryeditor = RequiredStoreCategorySP.edit();
        RequiredStoreCategoryeditor.putString("RequiredStoreCategory", "");
        RequiredStoreCategoryeditor.commit();

        SharedPreferences LogoImageCodeSP = context.getSharedPreferences(Config.SHARED_PREF38, 0);
        SharedPreferences.Editor LogoImageCodeeditor = LogoImageCodeSP.edit();
        LogoImageCodeeditor.putString("LogoImageCode", "");
        LogoImageCodeeditor.commit();

        SharedPreferences ExpressDeliverySP = context.getSharedPreferences(Config.SHARED_PREF39, 0);
        SharedPreferences.Editor ExpressDeliveryeditor = ExpressDeliverySP.edit();
        ExpressDeliveryeditor.putString("ExpressDelivery", "");
        ExpressDeliveryeditor.commit();

        SharedPreferences ExpressDeliveryAmountSP = context.getSharedPreferences(Config.SHARED_PREF40, 0);
        SharedPreferences.Editor ExpressDeliveryAmounteditor = ExpressDeliveryAmountSP.edit();
        ExpressDeliveryAmounteditor.putString("ExpressDeliveryAmount", "");
        ExpressDeliveryAmounteditor.commit();

        SharedPreferences HomeIconImageCodeSP = context.getSharedPreferences(Config.SHARED_PREF41, 0);
        SharedPreferences.Editor HomeIconImageCodeeditor = HomeIconImageCodeSP.edit();
        HomeIconImageCodeeditor.putString("HomeIconImageCode", "");
        HomeIconImageCodeeditor.commit();

        SharedPreferences RequiredBranchSP = context.getSharedPreferences(Config.SHARED_PREF42, 0);
        SharedPreferences.Editor RequiredBrancheditor = RequiredBranchSP.edit();
        RequiredBrancheditor.putString("RequiredBranch", "");
        RequiredBrancheditor.commit();

        SharedPreferences RequiredInshopSP = context.getSharedPreferences(Config.SHARED_PREF43, 0);
        SharedPreferences.Editor RequiredInshopeditor = RequiredInshopSP.edit();
        RequiredInshopeditor.putString("RequiredInshop", "");
        RequiredInshopeditor.commit();

        SharedPreferences RequiredcounterpickupSP = context.getSharedPreferences(Config.SHARED_PREF44, 0);
        SharedPreferences.Editor Requiredcounterpickupeditor = RequiredcounterpickupSP.edit();
        Requiredcounterpickupeditor.putString("Requiredcounterpickup", "");
        Requiredcounterpickupeditor.commit();

        SharedPreferences RequiredShoppinglistSP = context.getSharedPreferences(Config.SHARED_PREF45, 0);
        SharedPreferences.Editor RequiredShoppinglisteditor = RequiredShoppinglistSP.edit();
        RequiredShoppinglisteditor.putString("RequiredShoppinglist", "");
        RequiredShoppinglisteditor.commit();

        SharedPreferences DeliveryChargeSP = context.getSharedPreferences(Config.SHARED_PREF46, 0);
        SharedPreferences.Editor DeliveryChargeeditor = DeliveryChargeSP.edit();
        DeliveryChargeeditor.putString("DeliveryCharge", "");
        DeliveryChargeeditor.commit();

        SharedPreferences StoreCategorySP = context.getSharedPreferences(Config.SHARED_PREF47, 0);
        SharedPreferences.Editor StoreCategoryeditor = StoreCategorySP.edit();
        StoreCategoryeditor.putString("StoreCategory", "");
        StoreCategoryeditor.commit();

        SharedPreferences PlayStoreLinkSP = context.getSharedPreferences(Config.SHARED_PREF48, 0);
        SharedPreferences.Editor PlayStoreLinkeditor = PlayStoreLinkSP.edit();
        PlayStoreLinkeditor.putString("PlayStoreLink", "");
        PlayStoreLinkeditor.commit();

        SharedPreferences HomeImageSP = context.getSharedPreferences(Config.SHARED_PREF49, 0);
        SharedPreferences.Editor HomeImageeditor = HomeImageSP.edit();
        HomeImageeditor.putString("HomeImage", "");
        HomeImageeditor.commit();

        SharedPreferences OTPEmailSendSP = context.getSharedPreferences(Config.SHARED_PREF50, 0);
        SharedPreferences.Editor OTPEmailSendeditor = OTPEmailSendSP.edit();
        OTPEmailSendeditor.putString("OTPEmailSend", "");
        OTPEmailSendeditor.commit();

        SharedPreferences CategoryListSP = context.getSharedPreferences(Config.SHARED_PREF51, 0);
        SharedPreferences.Editor CategoryListeditor = CategoryListSP.edit();
        CategoryListeditor.putString("CategoryList", "");
        CategoryListeditor.commit();

        SharedPreferences SubCategoryListSP = context.getSharedPreferences(Config.SHARED_PREF52, 0);
        SharedPreferences.Editor SubCategoryListeditor = SubCategoryListSP.edit();
        SubCategoryListeditor.putString("SubCategoryList", "");
        SubCategoryListeditor.commit();

        SharedPreferences CashOnDeliverySP = context.getSharedPreferences(Config.SHARED_PREF53, 0);
        SharedPreferences.Editor CashOnDeliveryeditor = CashOnDeliverySP.edit();
        CashOnDeliveryeditor.putString("CashOnDelivery", "");
        CashOnDeliveryeditor.commit();

        SharedPreferences OnlinePaymentSP = context.getSharedPreferences(Config.SHARED_PREF54, 0);
        SharedPreferences.Editor OnlinePaymenteditor = OnlinePaymentSP.edit();
        OnlinePaymenteditor.putString("OnlinePayment", "");
        OnlinePaymenteditor.commit();

        SharedPreferences OTPMsgSendSP = context.getSharedPreferences(Config.SHARED_PREF55, 0);
        SharedPreferences.Editor OTPMsgSendeditor = OTPMsgSendSP.edit();
        OTPMsgSendeditor.putString("OTPMsgSend", "");
        OTPMsgSendeditor.commit();

        SharedPreferences BaseURLSP = context.getSharedPreferences(Config.SHARED_PREF56, 0);
        SharedPreferences.Editor BaseURLeditor = BaseURLSP.edit();
        BaseURLeditor.putString("BaseURL", "");
        BaseURLeditor.commit();

        SharedPreferences ImageURLSP = context.getSharedPreferences(Config.SHARED_PREF57, 0);
        SharedPreferences.Editor ImageURLeditor = ImageURLSP.edit();
        ImageURLeditor.putString("ImageURL", "");
        ImageURLeditor.commit();

        SharedPreferences countrySP = context.getSharedPreferences(Config.SHARED_PREF58, 0);
        SharedPreferences.Editor countryeditor = countrySP.edit();
        countryeditor.putString("country", "");
        countryeditor.commit();

        SharedPreferences UPIIDSP = context.getSharedPreferences(Config.SHARED_PREF59, 0);
        SharedPreferences.Editor UPIIDeditor = UPIIDSP.edit();
        UPIIDeditor.putString("UPIID", "");
        UPIIDeditor.commit();

        SharedPreferences TimeSlotCheckSP = context.getSharedPreferences(Config.SHARED_PREF60, 0);
        SharedPreferences.Editor TimeSlotCheckeditor = TimeSlotCheckSP.edit();
        TimeSlotCheckeditor.putString("TimeSlotCheck", "");
        TimeSlotCheckeditor.commit();

        SharedPreferences TimeSlotCheckReorderSP = context.getSharedPreferences(Config.SHARED_PREF61, 0);
        SharedPreferences.Editor TimeSlotCheckReordereditor = TimeSlotCheckReorderSP.edit();
        TimeSlotCheckReordereditor.putString("TimeSlotCheckReorder", "");
        TimeSlotCheckReordereditor.commit();

        SharedPreferences OnlinePaymentMethodsSP = context.getSharedPreferences(Config.SHARED_PREF62, 0);
        SharedPreferences.Editor OnlinePaymentMethodseditor = OnlinePaymentMethodsSP.edit();
        OnlinePaymentMethodseditor.putString("OnlinePaymentMethods", "");
        OnlinePaymentMethodseditor.commit();

        SharedPreferences Areasp = context.getSharedPreferences(Config.SHARED_PREF63, 0);
        SharedPreferences.Editor Areaeditor = Areasp.edit();
        Areaeditor.putString("Area",null);
        Areaeditor.commit();

        SharedPreferences AreaId = context.getSharedPreferences(Config.SHARED_PREF64, 0);
        SharedPreferences.Editor AreaeditorId = AreaId.edit();
        AreaeditorId.putString("AreaId", null);
        AreaeditorId.commit();

        //payment
        SharedPreferences payment = context.getSharedPreferences("localpref", 0);
        SharedPreferences.Editor paymentedit = payment.edit();
        paymentedit.putString("pref_data","");
        paymentedit.commit();


        SharedPreferences PRIVACYPOLICYSP = context.getSharedPreferences(Config.SHARED_PREF_PRIVACYPOLICY, 0);
        SharedPreferences.Editor PRIVACYPOLICYeditor = PRIVACYPOLICYSP.edit();
        PRIVACYPOLICYeditor.putString("PRIVACYPOLICY", null);
        PRIVACYPOLICYeditor.commit();

        SharedPreferences termscoSP = context.getSharedPreferences(Config.SHARED_PREF_TERMSCO, 0);
        SharedPreferences.Editor termscoeditor = termscoSP.edit();
        termscoeditor.putString("termsco", null);
        termscoeditor.commit();

        SharedPreferences about_usSP = context.getSharedPreferences(Config.SHARED_PREF_ABOUT_US, 0);
        SharedPreferences.Editor about_useditor = about_usSP.edit();
        about_useditor.putString("about_us", null);
        about_useditor.commit();

        SharedPreferences contact_usSP = context.getSharedPreferences(Config.SHARED_PREF_CONTACT_US, 0);
        SharedPreferences.Editor contact_useditor = contact_usSP.edit();
        contact_useditor.putString("contact_us", null);
        contact_useditor.commit();

    }

}

