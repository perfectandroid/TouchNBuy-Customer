package com.perfect.easyshopplus.Retrofit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @POST("ItemsTB/CategorySearchList")
    Call<String> getCat(@Body RequestBody body);

    @POST("ItemsTB/SubCategorySearchList")
    Call<String> getSubCat(@Body RequestBody body);

    @POST("MemberTB/StoreList")
    Call<String> getStores(@Body RequestBody body);

    @POST("MemberTB/StoreSearchListDetails")
    Call<String> getStoresList(@Body RequestBody body);

    @POST("MemberTB/StoreTimedetails")
    Call<String> getWorkingtime(@Body RequestBody body);

    @POST("ItemsTB/ItemsSearchList")
    Call<String> getItemList(@Body RequestBody body);

    @POST("MemberTB/LoginVerification")
    Call<String> getlogin(@Body RequestBody body);

    @POST("MemberTB/ForgotPassword")
    Call<String> getforgetpasswrd(@Body RequestBody body);

    @POST("MemberTB/MemberVarification")
    Call<String> getMemberVerification(@Body RequestBody body);

    @POST("MemberTB/MemberRegisterCheck")
    Call<String> getMemberRegCheck(@Body RequestBody body);

    @POST("MemberTB/CustomerRegistration")
    Call<String> getRegister(@Body RequestBody body);

    @POST("Customer/CustomerRegistration")
    Call<String> getCustRegister(@Body RequestBody body);

    @POST("MemberTB/VerificationCall")
    Call<String> getOTPVerification(@Body RequestBody body);

    @POST("MemberTB/ChangePassword")
    Call<String> getChangePassword(@Body RequestBody body);

    @POST("MemberTB/NotificationDetails")
    Call<String> getNotification(@Body RequestBody body);

    @POST("MemberTB/UpdateNotificationDetails")
    Call<String> getNotificationAllread(@Body RequestBody body);

    @POST("SalesOrderTB/SalesOrderDetails")
    Call<String> confirmOrder(@Body RequestBody body);

    @POST("SalesOrderTB/SalesOrderStatusDetails")
    Call<String> getOrderList(@Body RequestBody body);

    @POST("SalesOrderTB/SalesOrderStatusDetails")
    Call<String> getStatuslist(@Body RequestBody body);

    @POST("SalesOrderTB/DateWiseSalesOrderStatusDetails")
    Call<String> getSearchOrderList(@Body RequestBody body);

    @POST("SalesOrderTB/SalesOrderItemsDetails")
    Call<String> getOrderDetailList(@Body RequestBody body);

    @POST("SalesOrderTB/RemoveSalesOrderItemsDetails")
    Call<String> removeOrderItem(@Body RequestBody body);

    @POST("SalesOrderTB/EditSalesOrder")
    Call<String> editOrderItemCount(@Body RequestBody body);

    @POST("SalesOrderTB/DeleteSalesOrder")
    Call<String> removeSalesOrder(@Body RequestBody body);

    @POST("MemberTB/StoreLocationDetails")
    Call<String> getStoresLocation(@Body RequestBody body);

    @POST("ItemsTB/BarcodeSearch")
    Call<String> scanBarcode(@Body RequestBody body);

    @POST("SalesOrderTB/AddItemSalesOrder")
    Call<String> additemtoorder(@Body RequestBody body);

    @POST("MemberTB/StoreTimedetails")
    Call<String> getStoreTimeDate(@Body RequestBody body);

    @POST("SalesOrderTB/ReSalesOrderDetails")
    Call<String> getReOrderDetails(@Body RequestBody body);

    @POST("ItemsTB/ItemsSortAndFilter")
    Call<String> getSort(@Body RequestBody body);

    @POST("Customer/AreaDetails")
    Call<String> getArea(@Body RequestBody body);

    @POST("Customer/CustmerAddressChangeActions")
    Call<String> getCustmerAddressChangeActions(@Body RequestBody body);

    @POST("Customer/CustomerAddressDetails")
    Call<String> getCusAddress(@Body RequestBody body);

    @POST("Customer/BannerImageDetails")
    Call<String> getBanner(@Body RequestBody body);

    @POST("SalesOrderTB/HolidayChecking")
    Call<String> getHolidayChecking(@Body RequestBody body);

    @POST("SalesOrderTB/StoreRate")
    Call<String> getRating(@Body RequestBody body);

    @POST("Customer/StoreCategoryDetails")
    Call<String> getStoreCategoryDetails(@Body RequestBody body);

    @POST("MemberTB/ResellerDetails")
    Call<String> getResellerDetails(@Body RequestBody body);

    @POST("SalesOrderTB/OrderItemsDetails")
    Call<String> getPrescription(@Body RequestBody body);

    @POST("SalesOrderTB/UploadMedicalItems")
    Call<String> getUploadMedicalItems(@Body RequestBody body);

    @POST("MemberTB/SingleStoreDetails")
    Call<String> getSingleStoreDetails(@Body RequestBody body);

    @POST("Customer/MobileVersionAdd")
    Call<String> saveMobileVersionAdd(@Body RequestBody body);

    @POST("Customer/AppVersionCheck")
    Call<String> getAppVersionCheck(@Body RequestBody body);

    @POST("SalesOrderTB/AsOnDateApplicableChecking")
    Call<String> getAsOnDateApplicableChecking(@Body RequestBody body);

    @POST("MemberTB/LanguageList")
    Call<String> getLanguageList(@Body RequestBody body);

    @POST("MemberTB/Labels")
    Call<String> getLabels(@Body RequestBody body);

//    @Multipart
//    @POST("SalesOrderTB/UploadMedicalItems")
//    Call<String> getUploadMedicalItemsImgFile(@Part("JsonData") RequestBody JsonData,
//                                              @Part MultipartBody.Part Body);

    @Multipart
    @POST("SalesOrderTB/SalesOrderUpdate")
    Call<String> getSalesOrderUpdateImgFile(@Part("JsonData") RequestBody JsonData,
                                              @Part MultipartBody.Part Body);


    @POST("MemberTB/GiftVoucherList")
    Call<String> getGiftVoucherList(@Body RequestBody body);


    @POST("MemberTB/UpdateScratchCard")
    Call<String> UpdateScratchCard(@Body RequestBody body);

    @POST("PaymentGateway/Verify")
    Call<String> getVerifychecksum(@Body RequestBody body);

    @POST("PaymentGateway/GateWayResult")
    Call<String> getVerifyStatus(@Body RequestBody body);
    @POST("MemberTB/OfferCategoryList")
    Call<String> getOfferCategoryList(@Body RequestBody body);

    @POST("SalesOrderTB/PaymentDetailUpdate")
    Call<String> PaymentDetailUpdate(@Body RequestBody body);

    @POST("SalesOrderTB/PaymentMethod")
    Call<String> getPaymentMethod(@Body RequestBody body);

    @POST("MemberTB/CusBalanceList")
    Call<String> getCusBalanceList(@Body RequestBody body);
}