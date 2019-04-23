package leebeno.com.leebeno.Api;


import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface ApiConfig {

//    bhagyshalee


    @Multipart
    @POST("/api/Jobs/amtPendingJob")
    Call<JsonObject> sendCompleteRequest(@Header("Authorization") String authorization,@Header("ln") String lang,@Query("jobId") String callback ,@Part List<MultipartBody.Part> photos, @Part("data") RequestBody name);



    @GET("/api/Jobs/getSingleJob")
    Call<JsonObject> getJobDetail(@Header("Authorization") String authorization,@Header("ln") String lang,
                                   @Query("jobId") String jobId,
                                  @Query("peopleId") String peopleId);


    @GET("/api/Notifications/getNotifications")
    Call<JsonObject> getNotifications(@Header("Authorization") String authorization);

    @GET("/api/Notifications/readAllNoty")
    Call<JsonObject> readAllNoti(@Header("Authorization") String authorization);


    @POST("/api/Jobs/satisfyJob")
    Call<JsonObject> satisfy(@Header("Authorization") String authorization,@Header("ln") String lang,@Body JsonObject similarJobParameter);

    @Multipart
    @POST("/api/Jobs/notSatisfyJob")
    Call<JsonObject> notSatisfy(@Header("Authorization") String authorization,@Header("ln") String lang,@Query("jobId") String callback ,@Part List<MultipartBody.Part> photos, @Part("data") RequestBody name);


    @FormUrlEncoded
    @POST("api/People/muteNotification")
    Call<JsonObject> getNotyMute(@Header("Authorization") String authorization,
                                       @FieldMap Map<String, String> filters);


    @GET("/api/Jobs/getJobs")
    Call<JsonObject> getFilteredJob(@Header("Authorization") String authorization, @Header("ln") String lang,
                                    @Query("sDate") String sDate,
                                    @Query("paymentType") String paymentType, @Query("amount") double amount, @Query("minAmt") double minAmt
                    , @Query("maxAmt") double maxAmt , @Query("jobSkill") JSONArray jobSkill , @Query("loc") JSONObject loc);


    @GET("/api/Jobs/getNearByJobs")
    Call<JsonObject> getNearMeJobs(@Header("Authorization") String authorization, @Header("ln") String lang,
                                   @Query("loc") JSONObject loc);


    @POST("api/Ratings/giveRating")
    Call<JsonObject> giveRating(@Header("Authorization") String authorization,@Header("ln") String lang,
                                @Body JsonObject valuesGroup);

    @GET("api/Wallets/getWallet")
    Call<JsonObject> getWallet(@Header("Authorization") String authorization);


    @GET("api/Ratings/getGivenRatings")
    Call<JsonObject> getPostedRating(@Header("Authorization") String authorization,@Header("ln") String lang,
                               @Query("peopleId") String sDate);

    @GET("api/Ratings/getRatings")
    Call<JsonObject> receivedRating(@Header("Authorization") String authorization,@Header("ln") String lang,
                                    @Query("peopleId") String sDate);
    @GET("api/People/logout")
    Call<JsonObject> logoutService(@Header("Authorization") String authorization,@Header("ln") String lang);


    //String  token=PostPropertyFragment.accesstokrn;


//    @Multipart
//    @POST(WebUrls.UpdateProfile)
//    Call<JsonObject> PostEditprofile(@Part MultipartBody.Part profileImage,
//                                     @Part("data") RequestBody lookingFor,
//                                     @Query("access_token") String accessToken
//    );

//    @Multipart
//    @POST(WebUrls.Add_SendImage)
//    Call<JsonObject> PostChatImage(@Part MultipartBody.Part profileImage,
//                                   @Part("data") RequestBody lookingFor,
//                                   @Query("access_token") String accessToken
//    );

    @Multipart
    @POST(WebUrls.signup_api)
    Call<JsonObject> PostContract(@Header("ln") String authorization,
                                  @Part List<MultipartBody.Part> photos,
                                  @Part MultipartBody.Part image,
                                  @Part("data") RequestBody name
    );

//    @Multipart
//    @POST("api/Requestjobs/createJob")
//    Call<JsonObject> postFixProblem(@Header("ln") String authorization,
//                                    @Part List<MultipartBody.Part> photos,
//                                    @Part MultipartBody.Part image,
//                                    @Part("data") RequestBody name);
//
    @Multipart
    @POST(WebUrls.UploadProfilePic)
    Call<JsonObject> UpdateProfilePic(@Header("ln") String ln,
                                @Header("Authorization") String authorization,
                                @Part MultipartBody.Part postImage

    );

//    @Multipart
//    @POST(WebUrls.UpdateProfile)
//    Call<JsonObject> addImage(@Part MultipartBody.Part files,
//                              @Part("request") RequestBody lookingFor
//
//    );
//
//    @Multipart
//    @POST(WebUrls.UpdateProfile)
//    Call<JsonObject> addmarketplace(@PartMap Map<String, RequestBody> files,
//                                    @Part("request") RequestBody lookingFor
//    );
//    @Multipart
//    @POST(WebUrls.UpdateProfile)
//    Call<JsonObject> updatemarketplace(
//            @Part("request") RequestBody lookingFor
//    );
//    @Multipart
//    @POST(WebUrls.UpdateProfile)
//    Call<JsonObject> uploadProfile(@Part MultipartBody.Part files,
//                                   @Part("request") RequestBody lookingFor
//    );
//
//    @Multipart
//    @POST(WebUrls.UpdateProfile)
//    Call<JsonObject> addProductNew(@PartMap Map<String, RequestBody> files,
//                                   @Part("request") RequestBody lookingFor
//    );
//
//
//
//    @Multipart
//    @POST(WebUrls.UpdateProfile)
//    Call<JsonObject> Updatepostdata(@Part("request") RequestBody lookingFor
//
//    );

}
