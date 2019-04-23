package leebeno.com.leebeno;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Adapters.ImagesCrossAdapter;
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.Services.Utility;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static leebeno.com.leebeno.Services.Utility.close;
import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;
import static leebeno.com.leebeno.Services.Utility.showProgress;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class BidPlace extends AppCompatActivity implements View.OnClickListener, WebCompleteTask {

    public static String providerId;
    @Bind(R.id.jobBidPlace)
    TextView jobBidPlace;
    @Bind(R.id.estimatedTime)
    TextView estimatedTime;
    @Bind(R.id.noOfLabours)
    TextView noOfLabours;
    @Bind(R.id.payPerHour)
    TextView payPerHour;
    @Bind(R.id.startDate)
    TextView startDate;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.btnBidPlace)
    Button btnBidPlace;
    @Bind(R.id.layoutTimeEstimation)
    LinearLayout layoutTimeEstimation;
    @Bind(R.id.layoutCostEstimation)
    LinearLayout layoutCostEstimation;
    @Bind(R.id.uploadImageLayout)
    LinearLayout uploadImageLayout;
    @Bind(R.id.imageRecycler)
    RecyclerView imageRecycler;
    Uri imageUriIDProof;
    List<Bitmap> listBitmap;
    List<File> filesList;
    @Bind(R.id.providerName)
    TextView providerName;
    @Bind(R.id.cardBidPlace)
    CardView cardBidPlace;
    @Bind(R.id.cardUploadedImage)
    CardView cardUploadedImage;
    @Bind(R.id.btnSubmit)
    Button btnSubmit;
    String bidRequest;
    @Bind(R.id.layoutImages)
    LinearLayout layoutImages;
    @Bind(R.id.jobTitle)
    TextView jobTitle;
    @Bind(R.id.providerPostedTime)
    TextView providerPostedTime;
    @Bind(R.id.skillValue)
    TextView skillValue;
    @Bind(R.id.descriptionValue)
    TextView descriptionValue;
    @Bind(R.id.addressValue)
    TextView addressValue;
    @Bind(R.id.streetValue)
    TextView streetValue;
    String amountpph;
    @Bind(R.id.estimationTime)
    EditText estimationTime;
    @Bind(R.id.estimationCost)
    EditText estimationCost;
    @Bind(R.id.reasonEscalationTitle)
    TextView reasonEscalationTitle;
    @Bind(R.id.reasonEscalationValue)
    TextView reasonEscalationValue;


    @Bind(R.id.commentQuery)
    EditText commentQuery;

    @Bind(R.id.imageOne)
    ImageView imageOne;
    @Bind(R.id.imageTwo)
    ImageView imageTwo;

    @Bind(R.id.escalationLayoutImages)
    LinearLayout escalationLayoutImages;

    @Bind(R.id.imageOneEscalation)
    ImageView imageOneEscalation;
    @Bind(R.id.imageTwoEscalation)
    ImageView imageTwoEscalation;

    String jobId, bidId;

    @Bind(R.id.reviewTitle)
    TextView reviewTitle;
    @Bind(R.id.reviewValue)
    TextView reviewValue;
    @Bind(R.id.ratingTitle)
    TextView ratingTitle;
    @Bind(R.id.jobRating)
    RatingBar jobRating;
    @Bind(R.id.paymentType)
    TextView paymentType;

    String getImageOne="",getImageTwo="",getImageThree="",getImageFour="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_place);
        ButterKnife.bind(this);

        bidRequest = getIntent().getStringExtra("bid");
        jobId = getIntent().getStringExtra("jobId");

        listBitmap = new ArrayList<>();
        filesList = new ArrayList<>();

        imageRecycler.setHasFixedSize(true);
        imageRecycler.setNestedScrollingEnabled(true);
        imageRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        uploadImageLayout.setOnClickListener(this);
        providerName.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        backSignUp.setOnClickListener(this);
        btnBidPlace.setOnClickListener(this);
        imageOneEscalation.setOnClickListener(this);
        imageTwoEscalation.setOnClickListener(this);
        imageOne.setOnClickListener(this);
        imageTwo.setOnClickListener(this);


/*
        if (bidRequest.equals("Applied")) {
            btnBidPlace.setText(getResources().getString(R.string.editBid));
            btnSubmit.setText(getResources().getString(R.string.editBid));
            commentQuery.setVisibility(View.VISIBLE);
            layoutCostEstimation.setVisibility(View.VISIBLE);
            layoutTimeEstimation.setVisibility(View.VISIBLE);
            uploadImageLayout.setVisibility(View.GONE);
            imageRecycler.setVisibility(View.GONE);
            cardUploadedImage.setVisibility(View.GONE);
            String text = "<font color=#224496>No. of Bids </font> <font color=#959595>5</font>";
            jobBidPlace.setText(Html.fromHtml(text));
        }
*/

     /*   if (bidRequest.compareTo(getResources().getString(R.string.applied)) == 0) {
            btnBidPlace.setText(getResources().getString(R.string.editBid));
            btnSubmit.setText(getResources().getString(R.string.editBid));
            commentQuery.setVisibility(View.VISIBLE);
            layoutCostEstimation.setVisibility(View.VISIBLE);
            layoutTimeEstimation.setVisibility(View.VISIBLE);
            uploadImageLayout.setVisibility(View.GONE);
            imageRecycler.setVisibility(View.GONE);
            cardUploadedImage.setVisibility(View.GONE);
            String text = "<font color=#224496>No. of Bids </font> <font color=#959595>5</font>";
            jobBidPlace.setText(Html.fromHtml(text));


        } else if (bidRequest.compareTo(getResources().getString(R.string.rejected)) == 0) {

            commentQuery.setVisibility(View.GONE);
            layoutCostEstimation.setVisibility(View.GONE);
            layoutTimeEstimation.setVisibility(View.GONE);

            btnSubmit.setVisibility(View.GONE);
            cardBidPlace.setVisibility(View.GONE);
        }

*/
       /* String textestimatedTime = "<font color=#224496>Job hours: </font> <font color=#959595>5 hour</font>";
        estimatedTime.setText(Html.fromHtml(textestimatedTime));
        String textnoOfLabours = "<font color=#224496>No of labours: </font> <font color=#959595>5</font>";
        noOfLabours.setText(Html.fromHtml(textnoOfLabours));

        String textpayPerHour = "<font color=#224496>Pay per hour: </font> <font color=#959595>296 $</font>";
        payPerHour.setText(Html.fromHtml(textpayPerHour));
        String textstartDate = "<font color=#224496>Start Date: </font> <font color=#959595>3 Sept, 2018</font>";
        startDate.setText(Html.fromHtml(textstartDate));*/

       /* String providerName = "Job Provider : kutra";

        SpannableString content = new SpannableString(providerName);

        content.setSpan(new UnderlineSpan(), 0, providerName.length(), 0);
        jobProviderName.setText(content);*/
        String peopleId = SharedPrefManager.getUserID(BidPlace.this, Constrants.UserID);
        final String jobId = getIntent().getStringExtra("jobId");
        HashMap hashMap = new HashMap();
        Log.e("BIDPLLAA", "" + jobId + " " + SharedPrefManager.getAccessToken(BidPlace.this, Constrants.AccessToken)
                + " " + peopleId);
        new WebTask(BidPlace.this, WebUrls.BASE_URL + WebUrls.getSingleJobCustomer + "jobId=" + jobId
                + "&peopleId=" + peopleId
                , hashMap, BidPlace.this, RequestCode.CODE_GetSingleJob, 0);


    }

    @Override
    public void onComplete(String response, int taskcode) {
        try {


            if (taskcode == RequestCode.CODE_EditBid) {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONObject jsonObjectData = jsonObjSuccess.getJSONObject("msg");
                showToast(BidPlace.this, jsonObjectData.getString("replyMessage"));
                finish();
            }
            if (taskcode == RequestCode.CODE_CreateBid) {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                Log.e("getResponseCreate", "" + jsonObjSuccess);
                JSONObject jsonObjectData = jsonObjSuccess.getJSONObject("msg");
                showToast(BidPlace.this, jsonObjectData.getString("replyMessage"));
                finish();

            }
            if (taskcode == RequestCode.CODE_GetSingleJob) {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONObject jsonObjectData = jsonObjSuccess.getJSONObject("data");
                JSONArray jsonArraySkill = jsonObjectData.getJSONArray("skill");
                jobTitle.setText(jsonObjectData.optString("jobTitle"));


                Log.e("BidInfo", "" + jsonObjSuccess);
             /*   Date d = null;
                try {
                    d = inputFormat.parse("" + jsonObjectData.optString("createdAt"));
                } catch (ParseException ex) {
                    Log.e("neerbbgbt", "" + ex);
                }*/

                String providerNamestr = "Provider Name: " + jsonObjectData.getJSONObject("customer").getString("fullName");
                providerId = jsonObjectData.getJSONObject("customer").getString("id");

                SpannableString content = new SpannableString(providerNamestr);
                content.setSpan(new UnderlineSpan(), 0, providerNamestr.length(), 0);
                providerName.setText(content);

//                providerPostedTime.setText("Posted " + getDateDifference(d));
                providerPostedTime.setText("Posted: " + Utility.printDifference(jsonObjectData.optString("createdAt")));
                estimatedTime.setText("Job Days: " + jsonObjectData.optString("jobHours"));
                noOfLabours.setText("No. of labours: " + jsonObjectData.optString("noOfLabours"));
                jobBidPlace.setText("No. of bids: " + jsonObjectData.optString("bidCount"));


                //bidNoint=jsonObjectData.optInt("bidCount");
                for (int i = 0; i < jsonArraySkill.length(); i++) {
                    //skillGet = TextUtils.join(", ", new String[]{jsonArraySkill.getJSONObject(i).optString("name")});
                    if (skillValue.getText().toString().compareTo(jsonArraySkill.getJSONObject(i).optString("name")) != 0) {
                        if (i == 0) {
                            skillValue.setText(jsonArraySkill.getJSONObject(i).optString("name"));
                        } else if (i > 0) {
                            skillValue.setText(skillValue.getText().toString() + "," + jsonArraySkill.getJSONObject(i).optString("name"));
                        }
                    }
                }


                if (jsonObjectData.optString("paymentType").compareTo(getResources().getString(R.string.range)) == 0) {
                    amountpph = "Job Amount: " + "$ " + jsonObjectData.optString("min");
                    amountpph = amountpph + " - " + "$ " + jsonObjectData.optString("max");
                } else {
                    amountpph = "Job Amount: " + "$ " + jsonObjectData.optString("amount");
                }

                if (jsonObjectData.optString("paymentType").compareTo("pph")==0)
                {
                    paymentType.setText("Payment Type : Pay Per Hour");
                }else  if (jsonObjectData.optString("paymentType").compareTo("fix")==0)
                {
                    paymentType.setText("Payment Type : Fix ");
                }
                else
                {
                    paymentType.setText("Payment Type : Range ");
                }

                //JSONObject jsonObjectMyBid = jsonObjectData.getJSONObject("myBid");

                payPerHour.setText(amountpph);
                descriptionValue.setText("" + jsonObjectData.optString("description"));
                startDate.setText("Start Date: " + getDateFromUtc(jsonObjectData.optString("startDate")));
                addressValue.setText("" + jsonObjectData.getJSONObject("address").getString("value"));
                streetValue.setText("" + jsonObjectData.getJSONObject("address").getString("street"));

                if (jsonObjectData.getString("status").compareTo(getResources().getString(R.string.postedStatus)) == 0) {
//                    jobBidPlace.setVisibility(View.GONE);

//                    if (bidRequest.compareTo(getResources().getString(R.string.applied)) == 0) {
                    for (int k = 0; k < jsonObjectData.getJSONArray("bidders").length(); k++) {
                        if (jsonObjectData.getJSONArray("bidders").getString(k).compareTo(SharedPrefManager.getUserID(BidPlace.this, Constrants.UserID)) == 0) {
                            estimationTime.setText(jsonObjectData.getJSONObject("myBid").optString("time"));
                            estimationCost.setText(jsonObjectData.getJSONObject("myBid").optString("cost"));
                            commentQuery.setText(jsonObjectData.getJSONObject("myBid").optString("query"));
                            bidId = jsonObjectData.getJSONObject("myBid").optString("id");
//                                bidRequest = jsonObjectData.getJSONObject("myBid").optString("bidStatus");

                        }
                    }
                    if (jsonObjectData.optJSONObject("myBid").optString("bidStatus") != null) {
                        if (jsonObjectData.optJSONObject("myBid").optString("bidStatus").compareTo(getResources().getString(R.string.rejected)) == 0) {
                            bidRequest = getResources().getString(R.string.rejected);
                            commentQuery.setVisibility(View.GONE);
                            layoutCostEstimation.setVisibility(View.GONE);
                            layoutTimeEstimation.setVisibility(View.GONE);
                            btnSubmit.setVisibility(View.GONE);
                            cardBidPlace.setVisibility(View.GONE);

                        } else {
                            bidRequest = getResources().getString(R.string.applied);
                            btnBidPlace.setText(getResources().getString(R.string.editBid));
                            btnSubmit.setText(getResources().getString(R.string.editBid));
                            commentQuery.setVisibility(View.VISIBLE);
                            layoutCostEstimation.setVisibility(View.VISIBLE);
                            layoutTimeEstimation.setVisibility(View.VISIBLE);
                            uploadImageLayout.setVisibility(View.GONE);
                            imageRecycler.setVisibility(View.GONE);
                            cardUploadedImage.setVisibility(View.GONE);
                            String text = "<font color=#224496>No. of Bids </font> <font color=#959595>5</font>";
                            jobBidPlace.setText(Html.fromHtml(text));
                        }
                    } else {
                        bidRequest = getResources().getString(R.string.notapplied);
                    }


                   /*     btnBidPlace.setText(getResources().getString(R.string.editBid));
                        btnSubmit.setText(getResources().getString(R.string.editBid));
                        commentQuery.setVisibility(View.VISIBLE);
                        layoutCostEstimation.setVisibility(View.VISIBLE);
                        layoutTimeEstimation.setVisibility(View.VISIBLE);
                        uploadImageLayout.setVisibility(View.GONE);
                        imageRecycler.setVisibility(View.GONE);
                        cardUploadedImage.setVisibility(View.GONE);
                        String text = "<font color=#224496>No. of Bids </font> <font color=#959595>5</font>";
                        jobBidPlace.setText(Html.fromHtml(text));

                    } else if (bidRequest.compareTo(getResources().getString(R.string.rejected)) == 0) {

                        commentQuery.setVisibility(View.GONE);
                        layoutCostEstimation.setVisibility(View.GONE);
                        layoutTimeEstimation.setVisibility(View.GONE);
                        btnSubmit.setVisibility(View.GONE);
                        cardBidPlace.setVisibility(View.GONE);
                    }*/

                   /* if (jsonObjectData.getString("status").compareTo(getResources().getString(R.string.postedStatus)) == 0) {
                        btnBidPlace.setText(getResources().getString(R.string.editBid));
                        btnSubmit.setText(getResources().getString(R.string.editBid));
                        commentQuery.setVisibility(View.VISIBLE);
                        layoutCostEstimation.setVisibility(View.VISIBLE);
                        layoutTimeEstimation.setVisibility(View.VISIBLE);
                        uploadImageLayout.setVisibility(View.GONE);
                        imageRecycler.setVisibility(View.GONE);
                        cardUploadedImage.setVisibility(View.GONE);
                        String text = "<font color=#224496>No. of Bids </font> <font color=#959595>5</font>";
                        jobBidPlace.setText(Html.fromHtml(text));


                    } else*/

                } else if (jsonObjectData.getString("status").compareTo(getResources().getString(R.string.ongoingStatus)) == 0) {
                    btnSubmit.setText(getResources().getString(R.string.competeJob));
                    bidRequest = getResources().getString(R.string.competeJob);
                    commentQuery.setVisibility(View.GONE);
                    layoutCostEstimation.setVisibility(View.GONE);
                    layoutTimeEstimation.setVisibility(View.GONE);
                    cardUploadedImage.setVisibility(View.GONE);
                    uploadImageLayout.setVisibility(View.VISIBLE);

                    String text = "<font color=#224496>No. of Bids </font> <font color=#959595>5</font>";
                    jobBidPlace.setText(Html.fromHtml(text));
                    jobBidPlace.setVisibility(View.GONE);
                    paymentType.setVisibility(View.GONE);
//                    payPerHour.setText("Job Amount: " + jsonObjectData.getJSONObject("bid").optString("cost"));
                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getInt("finalAmount"));

                } else if (jsonObjectData.getString("status").compareTo(getResources().getString(R.string.amountpendingStatus)) == 0 || jsonObjectData.getString("status").compareTo(getResources().getString(R.string.satisfiedStatus)) == 0) {
                    btnSubmit.setText(getResources().getString(R.string.requestAmount));
                    bidRequest = getResources().getString(R.string.requestAmount);
                    layoutCostEstimation.setVisibility(View.GONE);
                    commentQuery.setVisibility(View.GONE);
                    layoutTimeEstimation.setVisibility(View.GONE);
                    uploadImageLayout.setVisibility(View.GONE);
                    imageRecycler.setVisibility(View.VISIBLE);
                    cardUploadedImage.setVisibility(View.VISIBLE);
                    btnSubmit.setVisibility(View.GONE);
                    paymentType.setVisibility(View.GONE);
                    layoutImages.setVisibility(View.VISIBLE);
//                    payPerHour.setText("Job Amount: " + jsonObjectData.getJSONObject("bid").optString("cost"));
                    // String text =   "<font color=#224496>Bids Placed </font> <font color=#959595>5</font>";
                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getInt("finalAmount"));
                    jobBidPlace.setText("Completed On: " + getDateFromUtc(jsonObjectData.optString("applierCompleteAt")));

                    for (int i = 0; i < jsonObjectData.getJSONArray("labImages").length(); i++) {
                        if (i == 0) {

                            Picasso.get()
                                    .load(WebUrls.BASE_URL + jsonObjectData.getJSONArray("labImages").get(0))
                                    .placeholder(R.drawable.default_image)
                                    .error(R.drawable.default_image)
                                    .into(imageOne);
                            getImageOne=WebUrls.BASE_URL + jsonObjectData.getJSONArray("labImages").get(0);
                        }
                        if (i == 1) {
                            Picasso.get()
                                    .load(WebUrls.BASE_URL + jsonObjectData.getJSONArray("labImages").get(1))
                                    .placeholder(R.drawable.default_image)
                                    .error(R.drawable.default_image)
                                    .into(imageTwo);
                            getImageTwo=WebUrls.BASE_URL + jsonObjectData.getJSONArray("labImages").get(1);
                        }

                    }

                } else if (jsonObjectData.getString("status").compareTo(getResources().getString(R.string.escalatedStatus)) == 0) {
                    reasonEscalationTitle.setVisibility(View.VISIBLE);
                    reasonEscalationValue.setVisibility(View.VISIBLE);
                    layoutImages.setVisibility(View.VISIBLE);
                    jobBidPlace.setText("Completed On: " + getDateFromUtc(jsonObjectData.optString("applierCompleteAt")));
//                    payPerHour.setText("Job Amount: " + jsonObjectData.getJSONObject("bid").optString("cost"));
                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getInt("finalAmount"));

                    btnSubmit.setVisibility(View.GONE);
                    paymentType.setVisibility(View.GONE);
                    reasonEscalationValue.setText(jsonObjectData.getString("reason"));
                    for (int i = 0; i < jsonObjectData.getJSONArray("labImages").length(); i++) {
                        if (i == 0) {

                            Picasso.get()
                                    .load(WebUrls.BASE_URL + jsonObjectData.getJSONArray("labImages").get(0))
                                    .placeholder(R.drawable.default_image)
                                    .error(R.drawable.default_image)
                                    .into(imageOne);

                            getImageOne=WebUrls.BASE_URL + jsonObjectData.getJSONArray("labImages").get(0);
                        }
                        if (i == 1) {
                            Picasso.get()
                                    .load(WebUrls.BASE_URL + jsonObjectData.getJSONArray("labImages").get(1))
                                    .placeholder(R.drawable.default_image)
                                    .error(R.drawable.default_image)
                                    .into(imageTwo);
                            getImageTwo=WebUrls.BASE_URL + jsonObjectData.getJSONArray("labImages").get(1);

                        }

                    }


                    for (int i = 0; i < jsonObjectData.getJSONArray("custImages").length(); i++) {
                        if (i == 0) {

                            Picasso.get()
                                    .load(WebUrls.BASE_URL + jsonObjectData.getJSONArray("custImages").get(0))
                                    .placeholder(R.drawable.default_image)
                                    .error(R.drawable.default_image)
                                    .into(imageOneEscalation);
                            escalationLayoutImages.setVisibility(View.VISIBLE);
                            getImageThree=WebUrls.BASE_URL + jsonObjectData.getJSONArray("custImages").get(0);
                        }
                        if (i == 1) {
                            Picasso.get()
                                    .load(WebUrls.BASE_URL + jsonObjectData.getJSONArray("custImages").get(1))
                                    .placeholder(R.drawable.default_image)
                                    .error(R.drawable.default_image)
                                    .into(imageTwoEscalation);
                            getImageFour=WebUrls.BASE_URL + jsonObjectData.getJSONArray("custImages").get(1);

                        }

                    }
//                    payPerHour.setText("Job Amount: " + jsonObjectData.getJSONObject("bid").optString("cost"));
                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getInt("finalAmount"));

                } else if (jsonObjectData.getString("status").compareTo(getResources().getString(R.string.completedStatus)) == 0) {
                    jobBidPlace.setText("Completed On: " + getDateFromUtc(jsonObjectData.optString("completedAt")));

                    layoutCostEstimation.setVisibility(View.GONE);
                    commentQuery.setVisibility(View.GONE);
                    layoutTimeEstimation.setVisibility(View.GONE);
                    uploadImageLayout.setVisibility(View.GONE);
                    paymentType.setVisibility(View.GONE);
                    imageRecycler.setVisibility(View.VISIBLE);
                    cardUploadedImage.setVisibility(View.VISIBLE);
                    layoutImages.setVisibility(View.VISIBLE);
                    /*JSONArray jsonArray=new JSONArray();
                    jsonArray.put();*/
//                    payPerHour.setText("Job Amount: " + jsonObjectData.getJSONObject("bid").optString("cost"));
                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getInt("finalAmount"));

                    bidRequest = getResources().getString(R.string.giveRating);
                    btnSubmit.setText(getResources().getString(R.string.giveRating));

                    for (int i = 0; i < jsonObjectData.getJSONArray("labImages").length(); i++) {
                        if (i == 0) {

                            Picasso.get()
                                    .load(WebUrls.BASE_URL + jsonObjectData.getJSONArray("labImages").get(0))
                                    .placeholder(R.drawable.default_image)
                                    .error(R.drawable.default_image)
                                    .into(imageOne);
                            imageOne.setVisibility(View.VISIBLE);
                            getImageOne=WebUrls.BASE_URL + jsonObjectData.getJSONArray("labImages").get(0);

                        }
                        if (i == 1) {
                            Picasso.get()
                                    .load(WebUrls.BASE_URL + jsonObjectData.getJSONArray("labImages").get(1))
                                    .placeholder(R.drawable.default_image)
                                    .error(R.drawable.default_image)
                                    .into(imageTwo);
                            imageTwo.setVisibility(View.VISIBLE);
                            getImageTwo=WebUrls.BASE_URL + jsonObjectData.getJSONArray("labImages").get(1);

                        }

                    }


                    if (jsonObjectData.optJSONArray("ratedetail") != null) {

                        for (int i = 0; i < jsonObjectData.optJSONArray("ratedetail").length(); i++) {
                            if (jsonObjectData.optJSONArray("ratedetail").optJSONObject(i).optString("forUser") != null) {
                                if (jsonObjectData.optJSONArray("ratedetail").optJSONObject(i).optString("forUser").compareTo("applier") == 0) {

                                    reviewTitle.setVisibility(View.VISIBLE);
                                    reviewValue.setVisibility(View.VISIBLE);
                                    ratingTitle.setVisibility(View.VISIBLE);
                                    jobRating.setVisibility(View.VISIBLE);
                                    reviewValue.setText("" + jsonObjectData.optJSONArray("ratedetail").optJSONObject(i).optString("comment"));
                                    jobRating.setRating(jsonObjectData.optJSONArray("ratedetail").optJSONObject(i).optInt("userRating"));
                                    btnSubmit.setVisibility(View.GONE);

                                }/* else {
                                    bidRequest = getResources().getString(R.string.giveRating);
                                    btnGiveRating.setText(getResources().getString(R.string.giveRating));
                                    btnGiveRating.setVisibility(View.VISIBLE);
                                }*/
                            }


                        }

                    }
                }



                /*if (jsonObjectData.getString("status").compareTo(getResources().getString(R.string.ongoingStatus)) == 0) {

                }*/


            }
        } catch (Exception e) {
            Log.e("exceptionGet", "" + e);
        }

    }


    private void selectCameraImageDoc() {
        // TODO Auto-generated method stub
        String fileName = "new-photo-name.jpg";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image captured by camera");
        imageUriIDProof = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUriIDProof);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 10);
        startActivityForResult(intent, 300);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 400) {
            if (data != null) {
                ContentResolver cR = getContentResolver();
                String mime = cR.getType(data.getData());
                String[] numbers = mime.split("/");
                //System.out.println(numbers[0]);
                // if (numbers[0].equals("image")) {
                File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/drawim");

                if (fileImage.mkdirs() || fileImage.isDirectory()) {
                    new ImageCompressionAsyncTaskDoc(BidPlace.this).execute(data.getData().toString(),
                            fileImage.getPath());
                }
                //   }
            }
        } else if (requestCode == 300) {
            Uri selectedImageUri = null;
            String filePath = null;
            selectedImageUri = imageUriIDProof;

/* if (selectedImageUri != null) {
                        try {
                            String filemanagerstring = selectedImageUri.getPath();
                            String selectedImagePath = getPath(selectedImageUri);

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Internal error",
                                    Toast.LENGTH_LONG).show();
                            Log.e(e.getClass().getName(), e.getMessage(), e);
                        }
                    }*/


            File fileImage = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/media/drawim");

            if (fileImage.mkdirs() || fileImage.isDirectory()) {
                if (fileImage.length() == 0) {
                } else {

                    new ImageCompressionAsyncTaskDoc(BidPlace.this).execute(selectedImageUri.toString(),
                            fileImage.getPath());
                }
            }

        }
    }

    @Override
    public void onClick(View view) {
        if (view == imageOneEscalation) {
//            imageOneEscalation.buildDrawingCache();
//            Bitmap image= imageOneEscalation.getDrawingCache();
           /* BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageOneEscalation.getDrawable());
            Bitmap bitmap = bitmapDrawable .getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageInByte = stream.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
*/
            Bundle extras = new Bundle();
            extras.putString("imagebitmap", getImageThree);

            Intent intent = new Intent(BidPlace.this, ShowImageActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
        if (view == imageTwoEscalation) {
          /*  BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageTwoEscalation.getDrawable());
            Bitmap bitmap = bitmapDrawable .getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] imageInByte = stream.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
*/
            Bundle extras = new Bundle();
            extras.putString("imagebitmap", getImageFour);


            Intent intent = new Intent(BidPlace.this, ShowImageActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
        if (view == imageOne) {
          /*  BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageOne.getDrawable());
            Bitmap bitmap = bitmapDrawable .getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] imageInByte = stream.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
            Bundle extras = new Bundle();
            extras.putByteArray("imagebitmap", imageInByte);*/

            Bundle extras = new Bundle();
            extras.putString("imagebitmap", getImageOne);
            Intent intent = new Intent(BidPlace.this, ShowImageActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }
        if (view == imageTwo) {
           /* BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageTwo.getDrawable());
            Bitmap bitmap = bitmapDrawable .getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] imageInByte = stream.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);*/
            Bundle extras = new Bundle();
            extras.putString("imagebitmap", getImageTwo);
            Intent intent = new Intent(BidPlace.this, ShowImageActivity.class);
            intent.putExtras(extras);
            startActivity(intent);
        }

        if (view == btnSubmit) {
           /* if (btnSubmit.getText().toString().compareTo(getResources().getString(R.string.competeJob)) == 0) {
                if (listBitmap.size() != 0) {
                    sendCompleteStatus();
                } else {
                    showToast(BidPlace.this, getResources().getString(R.string.errorUploadImageWork));
                }
            }
            if (btnSubmit.getText().toString().compareTo(getResources().getString(R.string.giveRating)) == 0) {
                Intent intent = new Intent(BidPlace.this, RatingReview.class);
                startActivity(intent);
            }*/
            if (bidRequest.compareTo(getResources().getString(R.string.applied)) == 0) {
                cardBidPlace.setVisibility(View.VISIBLE);

                btnSubmit.setVisibility(View.GONE);
            }
            if (bidRequest.compareTo(getResources().getString(R.string.notapplied)) == 0) {
                cardBidPlace.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.GONE);

            }
            if (bidRequest.compareTo(getResources().getString(R.string.ongoingStatus)) == 0) {
                cardBidPlace.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);
                finish();
            }
            if (bidRequest.compareTo(getResources().getString(R.string.competeJob)) == 0) {
                if (listBitmap.size() != 0) {
                    sendCompleteStatus();
                } else {
                    showToast(BidPlace.this, getResources().getString(R.string.errorUploadImageWork));
                }
                cardBidPlace.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);

            }
            if (bidRequest.compareTo(getResources().getString(R.string.giveRating)) == 0) {
                Intent intent = new Intent(BidPlace.this, RatingReview.class);
                intent.putExtra("jobId", jobId);
                startActivity(intent);
            }
          /*  if (bidRequest.equals("Ongoing")) {
                cardBidPlace.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);
                finish();
            }
            if (bidRequest.equals("Applied")) {
                cardBidPlace.setVisibility(View.VISIBLE);

                btnSubmit.setVisibility(View.GONE);
            }
            if (bidRequest.equals("NotApplied")) {
                cardBidPlace.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.GONE);

            }
            if (bidRequest.equals("Completed")) {
                cardBidPlace.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.VISIBLE);

                if (btnSubmit.getText().toString().equals("Give Rating")) {
                    Intent intent = new Intent(BidPlace.this, RatingReview.class);
                    startActivity(intent);
                } else {
                    //requestForAmount();
                }
            }
*/

        }
        if (view == btnBidPlace) {

            if (btnBidPlace.getText().toString().compareTo(getResources().getString(R.string.place_bid)) == 0) {
                double costes = 0;
                try {
                    costes = Double.parseDouble(estimationCost.getText().toString().trim());
                } catch (NumberFormatException e) {
                    Log.e("vreerffeweb", e + "");
                }

                if (estimationTime.getText().toString().trim().isEmpty()) {
                    estimationTime.setError(getResources().getString(R.string.errorEstimationTime));
                    estimationTime.requestFocus();
                } else if (estimationCost.getText().toString().trim().isEmpty()) {
                    estimationCost.setError(getResources().getString(R.string.errorEstimationCost));
                    estimationCost.requestFocus();
                } else if (commentQuery.getText().toString().trim().isEmpty()) {
                    commentQuery.setError(getResources().getString(R.string.errorQueries));
                    commentQuery.requestFocus();
                } else {
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("jobId", jobId);
                        jsonObject.put("time", "" + estimationTime.getText().toString().trim());
                        jsonObject.put("cost", costes);
                        jsonObject.put("query", commentQuery.getText().toString().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    new WebTask(BidPlace.this, WebUrls.BASE_URL + WebUrls.getCreateBid
                            , jsonObject, BidPlace.this, RequestCode.CODE_CreateBid, 1);

                }
            } else if (btnBidPlace.getText().toString().compareTo(getResources().getString(R.string.editBid)) == 0) {
                double costes = 0;
                try {
                    costes = Double.parseDouble(estimationCost.getText().toString().trim());
                } catch (NumberFormatException e) {
                    Log.e("vreerffeweb", e + "");
                }

                if (estimationTime.getText().toString().trim().isEmpty()) {
                    estimationTime.setError(getResources().getString(R.string.errorEstimationTime));
                    estimationTime.requestFocus();
                } else if (estimationCost.getText().toString().trim().isEmpty()) {
                    estimationCost.setError(getResources().getString(R.string.errorEstimationCost));
                    estimationCost.requestFocus();
                } else if (commentQuery.getText().toString().trim().isEmpty()) {
                    commentQuery.setError(getResources().getString(R.string.errorQueries));
                    commentQuery.requestFocus();
                } else {
                    JSONObject jsonObject = new JSONObject();

                    try {
                        jsonObject.put("jobId", jobId);
                        jsonObject.put("time", estimationTime.getText().toString().trim());
                        jsonObject.put("cost", costes);
                        jsonObject.put("query", commentQuery.getText().toString().trim());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    new WebTask(BidPlace.this, WebUrls.BASE_URL + WebUrls.EditBid + "bidId=" + bidId
                            , jsonObject, BidPlace.this, RequestCode.CODE_EditBid, 1);
                }

            }
        }
        if (view == providerName) {
            Intent intent = new Intent(BidPlace.this, CustomerProfile.class);
            intent.putExtra("jobStatus", "pending");
            intent.putExtra("providerId", providerId);
            startActivity(intent);
        }
        if (view == backSignUp) {
            finish();
        }
        if (view == uploadImageLayout) {
            if (filesList.size() >= 2) {
                showToast(BidPlace.this, getResources().getString(R.string.PhotoValidation));
            } else {
                try {
                    final Dialog dialog = new Dialog(BidPlace.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                    dialog.setContentView(R.layout.popuup_camragallary_design);
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                    LinearLayout txtCamera = (LinearLayout) dialog.findViewById(R.id.layoutGallery);
                    LinearLayout txtGallery = (LinearLayout) dialog.findViewById(R.id.layoutCamera);

                    txtGallery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int currentAPIVersion = Build.VERSION.SDK_INT;
                            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                                if (ActivityCompat.checkSelfPermission(BidPlace.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(BidPlace.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                                } else {
                                    selectCameraImageDoc();
                                    dialog.dismiss();
                                }
                            } else {
                                selectCameraImageDoc();
                                dialog.dismiss();
                            }
                        }
                    });
                    txtCamera.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int currentAPIVersion = Build.VERSION.SDK_INT;
                            if (currentAPIVersion >= Build.VERSION_CODES.M) {
                                if (ActivityCompat.checkSelfPermission(BidPlace.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(BidPlace.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                                } else {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_PICK);
                                    startActivityForResult(Intent.createChooser(intent, "Select Image"), 400);
//                startActivityForResult(i, 100);
                                }
                            } else {
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_PICK);
                                startActivityForResult(Intent.createChooser(intent, "Select Image"), 400);
                            }
                        }
                    });

                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }

        }
    }

    private void sendCompleteStatus() {
        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        List<MultipartBody.Part> photos = new ArrayList<>();
        showProgress(BidPlace.this);
        MultipartBody.Part filePartmultipleImages;
        for (int i = 0; i < filesList.size(); i++) {
            // File file = null;
            // file = new File(String.valueOf(listBitmap.get(i)));
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), filesList.get(i));
            filePartmultipleImages = MultipartBody.Part.createFormData("labImages", filesList.get(i).getName(), requestBody);
            photos.add(filePartmultipleImages);

        }
        JSONObject jsonObject = new JSONObject();


      /*  try {
            jsonObject.put("", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        String jsonObjData = String.valueOf(jsonObject);
        RequestBody objdata = RequestBody.create(MediaType.parse("text/plain"), jsonObjData);
        Log.e("SENDDATA", "" + SharedPrefManager.getAccessToken(BidPlace.this, Constrants.AccessToken) + "  " + SharedPrefManager.getLangId(BidPlace.this, Constrants.UserLang) + "  " + jobId + "  " + photos + "  " + objdata);
        Call<JsonObject> getPostData = apiInterface.sendCompleteRequest(SharedPrefManager.getAccessToken(BidPlace.this, Constrants.AccessToken), SharedPrefManager.getLangId(BidPlace.this, Constrants.UserLang), jobId, photos, objdata);
        getPostData.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        close();
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e("fdvvbsjkdsv", jsonObject.toString() + "");
                        JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                        JSONObject jsonObjectMsg = jsonObjSuccess.getJSONObject("msg");
                        showToast(BidPlace.this, jsonObjectMsg.getString("replyMessage"));

                        finish();

                    } catch (Exception e) {
                        Log.e("BidPlace", "" + e);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(BidPlace.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("BidPlace", "" + t);
                close();
            }
        });
    }

/*
    private void requestForAmount() {
        try {
            final Dialog dialog = new Dialog(BidPlace.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialog.setContentView(R.layout.show_msg_trans);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            final Button btnSubmitD = (Button) dialog.findViewById(R.id.btnSubmit);
            final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
            final TextView titlePopup = (TextView) dialog.findViewById(R.id.titlePopup);
            titlePopup.setText("You want to send request for job amount to system admin");
            btnSubmitD.setText(getResources().getString(R.string.yes));
            btnCancel.setText(getResources().getString(R.string.no));
            dialog.show();

            btnSubmitD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnSubmit.setText(getResources().getString(R.string.giveRating));
                    dialog.dismiss();

                }
            });
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/


    class ImageCompressionAsyncTaskDoc extends AsyncTask<String, Void, String> {

        Context mContext;

        public ImageCompressionAsyncTaskDoc(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(BidPlace.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(BidPlace.this).compress(params[0], new File(params[1]));

            } catch (Exception e) {

            }
            return filePath;
        }

        @Override
        protected void onPostExecute(String s) {
            close();
            if (s != null) {
                String strCompressedFileImage = s;
                File compressedImage = new File(s);
                Uri compressUri = Uri.fromFile(compressedImage);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(BidPlace.this.getContentResolver(), compressUri);

                    if (listBitmap.size() < 2) {
                        listBitmap.add(bitmap);
                        filesList.add(compressedImage);
                        ImagesCrossAdapter imagesAdapter = new ImagesCrossAdapter(listBitmap, filesList, BidPlace.this, imageRecycler);
                        imageRecycler.setAdapter(imagesAdapter);
                        imageRecycler.setVisibility(View.VISIBLE);
                    } else {
                        showToast(BidPlace.this, getResources().getString(R.string.PhotoValidation));
                    }


                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
    }
}
