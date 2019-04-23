package leebeno.com.leebeno.Api;


/**
 * Created by Advosoft2 on 6/28/2017.
 */
public class WebUrls {
//    public static final String BASE_URL = "https://api.babbabazri.com";
    public static final String BASE_URL = "http://139.59.71.150:3013";
    public final static String login_api = "/api/People/login";
    public final static String signup_api = "/api/People/signup";
    public final static String resetPassRequest = "/api/People/resetPassRequest";
    public final static String updateAdminProfileApi = "/api/People/editGroupAdmin";
    public final static String getProfileApi = "/api/People/getProfile";
    public final static String updateLabourProfileApi = "/api/People/selfEditLabour";
    public final static String updateCustomerProfileApi = "/api/People/editCustomer";
    public final static String changeLanguage = "/api/People/changeLanguage";
    public final static String changePassword = "/api/People/change-password";
    public final static String getProfileInfo = "/api/People/getProfile";
    public final static String UploadProfilePic = "/api/People/uploadProfilePic";

    public final static String createNewLabour = "/api/People/addLabour";
    public static final String getAllSkills_api = "/api/Skills/getAllSkills";
    public final static String getCustomerAllJob = "/api/Jobs/getCustAllJobs?";
    public final static String getSingleJobCustomer= "/api/Jobs/getSingleJob?";
    public final static String getLabourAllJob= "/api/Jobs/getJobs?";
    public final static String getLabourOwnJob= "/api/Jobs/getOwnJobs?";
    public final static String getSatisfyJob= "/api/Jobs/satisfyJob";
    public final static String getNotSatisfyJob= "/api/Jobs/notSatisfyJob";
    public final static String getCustEscalatedJob= "/api/Jobs/getCustEscalatedJobs";
    public final static String getEscalatedJob= "/api/Jobs/getEscalatedJobs";

    public final static String getPostedJobs= "/api/Ratings/getGivenRatings?";


    public final static String getCreateBid= "/api/Bids/createBid";
    public final static String getCreateJob = "/api/Jobs/createJob";
    public final static String EditBid = "/api/Bids/editBid?";
    public final static String getReceivedBidList = "/api/Bids/getSingleJobBids?";
    public final static String getSingleBid = "/api/Bids/getSingleBid?";
    public final static String acceptBid = "/api/Bids/acceptBid";
    public final static String rejectBid = "/api/Bids/rejectBid";
    public final static String getProfileById = "/api/People/getProfileById?";

    public final static String getAllGroups = "/api/People/getAllGroups";
    public final static String getAllLabours = "/api/People/getAllLabours";
    public final static String makeRequest = "/api/Requests/makeRequest";
    public final static String getGroupReceivedReq = "/api/Requests/getGroupReceivedReq";
    public final static String getLabReceivedReq = "/api/Requests/getLabReceivedReq";
    public final static String getGroupSentReq ="/api/Requests/getGroupSentReq";
    public final static String getLabSentReq= "/api/Requests/getLabSentReq";
    public final static String acceptRequest="/api/Requests/acceptRequest";
    public final static String rejectRequest="/api/Requests/rejectRequest";
    public final static String getConfirmedRequests = "/api/Requests/getConfirmedRequests";
    public final static String removeFromGrouplist = "/api/Requests/removeFromGrouplist";
    public final static String addMember = "/api/Groups/addMember";
    public final static String getGroupData = "/api/Groups/getGroupData";
    public final static String removeMember= "/api/Groups/removeMember";
    public final static String cancelRequest = "/api/Requests/cancelRequest";
    public final static String getGroupList = "/api/Groups/getGroupList";
    public final static String getLabConfirmedRequests = "/api/Requests/getLabConfirmedRequests";
    public final static String leftGrouplist = "/api/Requests/leftGrouplist";
    public final static String archiveAcceptBids = "/api/Bids/archiveAcceptBids";
    public final static String archiveRejectBids = "/api/Bids/archiveRejectBids";

    public final static String getMyLabourList= "/api/People/getMyLabourList?";
    public final static String addOwnMember = "/api/Groups/addOwnMember";
    public final static String removeOwnMember= "/api/Groups/removeOwnMember";


    //Google api for Tracking
    public static final String baseURL = "https://maps.googleapis.com";

    public static ApiConfig getGoogleAPI(){
        return RetrofitClient.getRetrofitClient(baseURL).create(ApiConfig.class);
    }

}
