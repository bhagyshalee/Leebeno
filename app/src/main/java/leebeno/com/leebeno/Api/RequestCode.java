package leebeno.com.leebeno.Api;

/**
 * Created by suarebits on 3/12/15.
 */
public class RequestCode {
    public static final int CODE_Register = 1;
    public static final int CODE_Login = 2;
    public static final int CODE_Signup = 3;
    public static final int CODE_ResetPassRequest = 4;
    public static final int CODE_new_resetPassword = 5;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 6;
    public static final int ERROR_DIALOG_REQUEST = 7;
    public static final int CODE_checkResetOtp = 8;
    public static final int CODE_getskills = 9;
    public static final int CODE_AfterBtn = 10;
    public static final int CODE_Sub_Product = 11;
    public static final int CODE_TrendingProduct=12;
    public static final int CODE_GET_NearbyDrivers=13;
    public static final int CODE_GET_NearbyDriversVehicle=13;
    public static final int CODE_Direction_Api=14;
    public static final int CODE_BookNow_Api = 15;
    public static final int CODE_MyOrderlist_Api = 16;
    public static final int CODE_VerifiyMobileNo_Api = 17;
    public static final int CODE_Resend_VerifiyMobileNo_Api=18;
    public static final int CODE_Logout_Api=19;
    public static final int CODE_Notification_Api=20;
    public static final int CODE_BadgeCount=21;
    public static final int CODE_ReadNotification=22;
    public static final int CODE_Rating=23;
    public static final int CODE_UserProfile=24;
    public static final int CODE_UpdateUserName=25;
    public static final int CODE_UpdateUserEmail=26;
    public static final int CODE_ChangePassword=27;
    public static final int CODE_Sub_ProductHide=28;
    public static final int CODE_AfterSearchHide = 29;
    public static final int CODE_fetchGetMaterials=30;
    public static final int CODE_fetchGetVehicles =31;
    public static final int CODE_updateHotCountn =32;
    public static final int CODE_GetByUnitQty =33;
    public static final int CODE_GetPriceing =34;
    public static final int CODE_GetCurrentOrder=35;
    public static final int CODE_ChangeLanguage=36;
    public static final int CODE_GetSubTypesObj=37;
    public static final int CODE_UpdateCustomerProfile=38;
    public static final int CODE_UpdateAdminProfile=39;
    public static final int CODE_UpdateLabourProfile=42;
    public static final int CODE_getProfileInfo=43;
    public static final int CODE_createJob=44;
    public static final int CODE_GetCustomerAllJobs=45;
    public static final int CODE_GetSingleJob=46;
    public static final int CODE_GetLabourAllJobs=47;
    public static final int CODE_CreateBid=48;
    public static final int CODE_EditBid=49;
    public static final int CODE_getAllBid=50;
    public static final int CODE_getSingleBid=51;
    public static final int CODE_acceptBid=52;
    public static final int CODE_rejectBid=53;
    public static final int CODE_getProfileById=54;
    public static final int CODE_getDetailNotification=55;
    public static final int CODE_getSatisfy=56;
    public static final int CODE_getEscalatedJob=56;
    public static final int CODE_getPostedJob=56;


    public static final int CODE_GetAllGroups=100;
    public static final int CODE_GetAllrequest=101;
    public static final int CODE_MakeRequest = 102;
    public static final int CODE_GetAllSentrequest=103;
    public static final int CODE_AcceptRequest=104;
    public static final int CODE_RejectRequest=105;
    public static final int CODE_GetConfirmedRequests=106;
    public static final int CODE_RemoveFromGroupList = 107;
    public static final int CODE_AddMember = 108;
    public static final int CODE_GetGroupData=109;
    public static final int CODE_RemoveMember = 110;
    public static final int CODE_CancelRequest = 111;
    public static final int CODE_GetGroupList = 112;
    public static final int CODE_LeftGroupList = 113;
    public static final int CODE_GetMyLabourList = 114;
    public static final int CODE_addOwnMember = 115;
    public static final int CODE_removeOwnMember = 116;
    public static final int Code_archiveAcceptBids = 117;
    public static final int Code_archiveRejectBids = 118;



    //Constants
    public static String SP_CURRENT_LAT = "lat";
    public static String SP_CURRENT_LONG = "lng";
    public static String SP_NEW_LAT = "newLat";
    public static String SP_NEW_LONG = "newLng";
    public static String SP_DriverStatus = "driverStatus";
    public static final String UserID = "userID";
    public static final String userType = "user_type";
    public static final String KEY_ANIM_TYPE="anim_type";
    public static final String KEY_TITLE="anim_title";
    public static final String LangId = "langid";

    public static int AUTOPLACE_CODE=80;


    public enum TransitionType{
        ExplodeJava,ExplodeXML,SlideJava,SlideXML,FadeJava,FadeXML;
    }

}
