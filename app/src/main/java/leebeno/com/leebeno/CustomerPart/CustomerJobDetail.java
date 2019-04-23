package leebeno.com.leebeno.CustomerPart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
import leebeno.com.leebeno.BidPlace;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.LabourProfile;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.RatingReview;
import leebeno.com.leebeno.ReceivedBidRequest;
import leebeno.com.leebeno.Services.Utility;
import leebeno.com.leebeno.ShowImageActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static leebeno.com.leebeno.Api.RequestCode.CODE_getSatisfy;
import static leebeno.com.leebeno.Services.Utility.close;
import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;
import static leebeno.com.leebeno.Services.Utility.showProgress;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class CustomerJobDetail extends AppCompatActivity implements WebCompleteTask {

    private static int SPLASH_TIME_OUT = 2000;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
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
    String bidNo;
    int bidNoint;
    @Bind(R.id.completedDate)
    TextView completedDate;
    @Bind(R.id.layoutImages)
    LinearLayout layoutImages;
    @Bind(R.id.reasonTitle)
    TextView reasonTitle;
    @Bind(R.id.reasonValue)
    TextView reasonValue;
    @Bind(R.id.jobBidUsdAmt)
    TextView jobBidUsdAmt;
    @Bind(R.id.creditAmt)
    TextView creditAmt;
    @Bind(R.id.layoutEscalation)
    LinearLayout layoutEscalation;
    @Bind(R.id.jobTitle)
    TextView jobTitle;
    @Bind(R.id.providerPostedTime)
    TextView providerPostedTime;
    @Bind(R.id.skillsValue)
    TextView skillsValue;
    @Bind(R.id.descriptionValue)
    TextView descriptionValue;
    @Bind(R.id.addressValue)
    TextView addressValue;
    @Bind(R.id.streetValue)
    TextView streetValue;
    @Bind(R.id.imageOne)
    ImageView imageOne;
    @Bind(R.id.imageTwo)
    ImageView imageTwo;
    @Bind(R.id.reasonEscalationTitle)
    TextView reasonEscalationTitle;
    @Bind(R.id.reasonEscalationValue)
    TextView reasonEscalationValue;
    @Bind(R.id.amountPending)
    TextView amountPending;

    @Bind(R.id.reviewTitle)
    TextView reviewTitle;
    @Bind(R.id.reviewValue)
    TextView reviewValue;
    @Bind(R.id.ratingTitle)
    TextView ratingTitle;
    @Bind(R.id.jobRating)
    RatingBar jobRating;


    String amountpph;
    String bidRequest;
    String jobId, BidderId;
    Date d = null;
    ImagesCrossAdapter imagesAdapter;

    int REQUEST_CAMERA_PERMISSION = 1;
    List<Bitmap> listBitmap;
    Uri imageUri, imageUriIDProof;
    List<File> filesList;
    boolean getNoti = false;
    RecyclerView imageRecycler;

    String getSatisfyStatus, jobTitlestr, createdAtstr, applierNamestr, bidCoststr;

    @Bind(R.id.btnGiveRating)
    Button btnGiveRating;

    @Bind(R.id.escalationLayoutImages)
    LinearLayout escalationLayoutImages;

    @Bind(R.id.imageOneEscalation)
    ImageView imageOneEscalation;
    @Bind(R.id.imageTwoEscalation)
    ImageView imageTwoEscalation;
    @Bind(R.id.paymentType)
    TextView paymentType;

    String getImageOne="",getImageTwo="",getImageThree="",getImageFour="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_job_detail);
        ButterKnife.bind(this);

        listBitmap = new ArrayList<>();
        filesList = new ArrayList<>();

        getSatisfyStatus = getResources().getString(R.string.approve);
//        String text = "<font color=#224496>Bids Placed </font> <font color=#959595>5</font>";
        //jobBidPlace.setText(Html.fromHtml(text));
       /* String textestimatedTime = "<font color=#224496>Job hours: </font> <font color=#959595>5 hour</font>";
        estimatedTime.setText(Html.fromHtml(textestimatedTime));
        String textnoOfLabours = "<font color=#224496>No of labours: </font> <font color=#959595>5</font>";
        noOfLabours.setText(Html.fromHtml(textnoOfLabours));
        String textpayPerHour = "<font color=#224496>Pay per hour: </font> <font color=#959595>296 $</font>";
        payPerHour.setText(Html.fromHtml(textpayPerHour));
        String textstartDate = "<font color=#224496>Start Date: </font> <font color=#959595>3 Sept, 2018</font>";
        String usdTen = "<font color=#224496>USD 10 : </font> <font color=#959595>deducted commission</font>";
        String creditAmtText = "<font color=#224496>Credit : </font> <font color=#959595>10 sept 2018</font>";*/
       /* startDate.setText(Html.fromHtml(textstartDate));
        jobBidUsdAmt.setText(Html.fromHtml(usdTen));
        creditAmt.setText(Html.fromHtml(creditAmtText));*/

        String amountRequest = "Release Amount";
        SpannableString content = new SpannableString(amountRequest);
        content.setSpan(new UnderlineSpan(), 0, amountRequest.length(), 0);
        amountPending.setText(content);

        amountPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPopupDailog(jobTitlestr, createdAtstr
                        , applierNamestr, bidCoststr);

            }
        });


        //jobBidPlace.setText(Html.fromHtml("<p><u><font color=#224496>Bids Placed </font> <font color=#959595>5</font></u></p>").toString());
        // providerName.setText(Html.fromHtml("<p><u>Labour Name</u></p>").toString());

        String peopleId = SharedPrefManager.getUserID(CustomerJobDetail.this, Constrants.UserID);
//        bidRequest = getIntent().getStringExtra("jobStatus");
        jobId = getIntent().getStringExtra("jobId");
        getNoti = getIntent().getBooleanExtra("noti", false);

        Log.e("nfvgjkdfbgfd", peopleId + " " + jobId + " " + SharedPrefManager.getAccessToken(CustomerJobDetail.this, Constrants.AccessToken));
        HashMap hashMap = new HashMap();
        new WebTask(CustomerJobDetail.this, WebUrls.BASE_URL + WebUrls.getSingleJobCustomer + "jobId=" + jobId
                + "&peopleId=" + peopleId, hashMap, CustomerJobDetail.this, RequestCode.CODE_GetSingleJob, 0);


        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnGiveRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerJobDetail.this, RatingReview.class);
                intent.putExtra("jobId", jobId);
                startActivity(intent);
            }
        });

        imageOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle extras = new Bundle();
                extras.putString("imagebitmap", getImageOne);
                Intent intent = new Intent(CustomerJobDetail.this, ShowImageActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        imageTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle extras = new Bundle();
                extras.putString("imagebitmap", getImageTwo);

                Intent intent = new Intent(CustomerJobDetail.this, ShowImageActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        imageOneEscalation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* imageOneEscalation.buildDrawingCache();
                Bitmap image= imageOneEscalation.getDrawingCache();*/

                Bundle extras = new Bundle();
                extras.putString("imagebitmap", getImageThree);
                Intent intent = new Intent(CustomerJobDetail.this, ShowImageActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
        imageTwoEscalation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* imageTwoEscalation.buildDrawingCache();
                Bitmap image= imageTwoEscalation.getDrawingCache();*/
                Bundle extras = new Bundle();
                extras.putString("imagebitmap", getImageFour);
                Intent intent = new Intent(CustomerJobDetail.this, ShowImageActivity.class);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });


        jobBidPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bidNoint != 0 && bidRequest.equals(getResources().getString(R.string.postedStatus))) {
                    Intent intent = new Intent(CustomerJobDetail.this, ReceivedBidRequest.class);
                    intent.putExtra("jobId", jobId);
                    startActivity(intent);
                } else if (jobBidPlace.getText().toString().contains("Completed") || jobBidPlace.getText().toString().contains("Bid Placed by")
                        || jobBidPlace.getText().toString().contains("Escalated by")) {
                    Intent intent = new Intent(CustomerJobDetail.this, LabourProfile.class);
                    intent.putExtra("jobStatus", "pending");
                    intent.putExtra("bidPersonId", BidderId);
                    startActivity(intent);

                 /*   if (bidRequest.equals("ongoing")) {
                        Intent intent = new Intent(CustomerJobDetail.this, LabourProfile.class);
                        intent.putExtra("jobStatus", "ongoing");
                        startActivity(intent);
                    }
                    if (bidRequest.equals("completed")) {
                        Intent intent = new Intent(CustomerJobDetail.this, LabourProfile.class);
                        intent.putExtra("jobStatus", "completed");
                        startActivity(intent);
                    }
                    if (bidRequest.equals("escalated")) {
                        Intent intent = new Intent(CustomerJobDetail.this, LabourProfile.class);
                        intent.putExtra("jobStatus", "escalated");
                        startActivity(intent);
                    }*/

                }

            }
        });

/*
        jobBidPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(CustomerJobDetail.this, LabourProfile.class);
                startActivity(intent);
            }
        });
*/
    }


    private void showPopupDailog(String jobTitle, String postedDate, String completedBy, String payPerHour) {
        try {
            final Dialog dialog = new Dialog(CustomerJobDetail.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            dialog.setContentView(R.layout.popup_release_amount);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
            ImageView crossPopup = (ImageView) dialog.findViewById(R.id.crossPopup);
            final TextView textAttachFile = (TextView) dialog.findViewById(R.id.textAttachFile);
            TextView textJobTitle = (TextView) dialog.findViewById(R.id.textJobTitle);
            TextView textPostedDate = (TextView) dialog.findViewById(R.id.textPostedDate);
            TextView textCompletedBy = (TextView) dialog.findViewById(R.id.textCompletedBy);
            TextView textPayPerHour = (TextView) dialog.findViewById(R.id.textPayPerHour);
            final EditText editReason = (EditText) dialog.findViewById(R.id.editReason);
            final EditText editFeedBack = (EditText) dialog.findViewById(R.id.editFeedBack);
            imageRecycler = (RecyclerView) dialog.findViewById(R.id.imageRecycler);

            textJobTitle.setText("Job Title : " + jobTitle);
            textPostedDate.setText("Posted On : " + getDateFromUtc(postedDate));
            textCompletedBy.setText("Completed By : " + completedBy);
            textPayPerHour.setText("Job Amount : " + payPerHour);

            imageRecycler.setHasFixedSize(true);
            imageRecycler.setNestedScrollingEnabled(true);
            imageRecycler.setLayoutManager(new LinearLayoutManager(CustomerJobDetail.this, LinearLayoutManager.HORIZONTAL, false));

            Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
            final Button btnApprove = (Button) dialog.findViewById(R.id.btnApprove);
            final Button btnDecline = (Button) dialog.findViewById(R.id.btnDecline);
            editReason.setVisibility(View.GONE);
            textAttachFile.setVisibility(View.GONE);
            imageRecycler.setVisibility(View.GONE);
            editFeedBack.setVisibility(View.VISIBLE);
            btnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSatisfyStatus = getResources().getString(R.string.approve);
                    editReason.setVisibility(View.GONE);
                    textAttachFile.setVisibility(View.GONE);
                    imageRecycler.setVisibility(View.GONE);
                    editFeedBack.setVisibility(View.VISIBLE);
                    btnApprove.setBackground(getResources().getDrawable(R.drawable.primary_circle_background));
                    btnApprove.setTextColor(getResources().getColor(R.color.white));
                    btnDecline.setBackground(getResources().getDrawable(R.drawable.round_border));
                    btnDecline.setTextColor(getResources().getColor(R.color.gray));
                }
            });
            btnDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getSatisfyStatus = getResources().getString(R.string.decline);
                    editReason.setVisibility(View.VISIBLE);
                    textAttachFile.setVisibility(View.VISIBLE);
                    imageRecycler.setVisibility(View.VISIBLE);
                    editFeedBack.setVisibility(View.GONE);
                    btnApprove.setBackground(getResources().getDrawable(R.drawable.round_border));
                    btnApprove.setTextColor(getResources().getColor(R.color.gray));
                    btnDecline.setBackground(getResources().getDrawable(R.drawable.primary_circle_background));
                    btnDecline.setTextColor(getResources().getColor(R.color.white));
                }
            });
            crossPopup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (getSatisfyStatus.compareTo(getResources().getString(R.string.approve)) == 0) {
                        if (editFeedBack.getText().toString().isEmpty()) {
                            editFeedBack.requestFocus();
                            editFeedBack.setError(getResources().getString(R.string.enter_feedback));
                        } else {
                            dialog.dismiss();
                            sendSatisfyStatus(editFeedBack.getText().toString());
                        }

                    } else if (getSatisfyStatus.compareTo(getResources().getString(R.string.decline)) == 0) {
                        if (editReason.getText().toString().isEmpty()) {
                            editReason.requestFocus();
                            editReason.setError(getResources().getString(R.string.enter_reason));
                        }/* else if (filesList.size() == 0) {
                            showToast(CustomerJobDetail.this, getResources().getString(R.string.photo));
                        } */ else {
                            dialog.dismiss();
                            sendNotSatisfyStatus(editReason.getText().toString());
                        }
                    }

                }
            });

            textAttachFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listBitmap.size() < 2) {
                        try {
                            final Dialog dialog = new Dialog(CustomerJobDetail.this);
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
                                        if (ActivityCompat.checkSelfPermission(CustomerJobDetail.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(CustomerJobDetail.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
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
                                        if (ActivityCompat.checkSelfPermission(CustomerJobDetail.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                            ActivityCompat.requestPermissions(CustomerJobDetail.this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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

                    } else {
                        showToast(CustomerJobDetail.this, getResources().getString(R.string.PhotoValidation));
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void sendSatisfyStatus(String feedback) {
        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        List<MultipartBody.Part> photos = new ArrayList<>();
        showProgress(CustomerJobDetail.this);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("jobId", jobId);
        jsonObject.addProperty("feedBack", feedback);


        String jsonObjData = String.valueOf(jsonObject);
        RequestBody objdata = RequestBody.create(MediaType.parse("text/plain"), jsonObjData);
//        Log.e("SENDDATA", "" + SharedPrefManager.getAccessToken(CustomerJobDetail.this, Constrants.AccessToken) + "  " + SharedPrefManager.getLangId(CustomerJobDetail.this, Constrants.UserLang) + "  " + jobId + "  " + photos + "  " + objdata);
        Call<JsonObject> getPostData = apiInterface.satisfy(SharedPrefManager.getAccessToken(CustomerJobDetail.this, Constrants.AccessToken), SharedPrefManager.getLangId(CustomerJobDetail.this, Constrants.UserLang), jsonObject);
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
                        showToast(CustomerJobDetail.this, jsonObjectMsg.getString("replyMessage"));

                        finish();

                    } catch (Exception e) {
                        Log.e("BidPlace", "" + e);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(CustomerJobDetail.this, "" + jsonObjError.getString("message"));
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


    private void sendNotSatisfyStatus(String reason) {
        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        List<MultipartBody.Part> photos = new ArrayList<>();
        showProgress(CustomerJobDetail.this);
        MultipartBody.Part filePartmultipleImages;
        for (int i = 0; i < filesList.size(); i++) {
            // File file = null;
            // file = new File(String.valueOf(listBitmap.get(i)));
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), filesList.get(i));
            filePartmultipleImages = MultipartBody.Part.createFormData("custImages", filesList.get(i).getName(), requestBody);
            photos.add(filePartmultipleImages);

        }
        JSONObject jsonObject = new JSONObject();


        try {
            jsonObject.put("reason", "" + reason);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String jsonObjData = String.valueOf(jsonObject);
        RequestBody objdata = RequestBody.create(MediaType.parse("text/plain"), jsonObjData);
        Log.e("SENDDATA", "" + SharedPrefManager.getAccessToken(CustomerJobDetail.this, Constrants.AccessToken) + "  " + SharedPrefManager.getLangId(CustomerJobDetail.this, Constrants.UserLang) + "  " + jobId + "  " + photos + "  " + objdata);
        Call<JsonObject> getPostData = apiInterface.notSatisfy(SharedPrefManager.getAccessToken(CustomerJobDetail.this, Constrants.AccessToken), SharedPrefManager.getLangId(CustomerJobDetail.this, Constrants.UserLang), jobId, photos, objdata);
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
                        showToast(CustomerJobDetail.this, jsonObjectMsg.getString("replyMessage"));

                        finish();

                    } catch (Exception e) {
                        Log.e("BidPlace", "" + e);
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(CustomerJobDetail.this, "" + jsonObjError.getString("message"));
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
                    new ImageCompressionAsyncTaskDoc(CustomerJobDetail.this).execute(data.getData().toString(),
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

                    new ImageCompressionAsyncTaskDoc(CustomerJobDetail.this).execute(selectedImageUri.toString(),
                            fileImage.getPath());
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == CODE_getSatisfy) {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONObject jsonObjectData = jsonObjSuccess.getJSONObject("data");

            }
            if (taskcode == RequestCode.CODE_GetSingleJob) {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONObject jsonObjectData = jsonObjSuccess.getJSONObject("data");
                JSONArray jsonArraySkill = jsonObjectData.getJSONArray("skill");
                jobTitle.setText(jsonObjectData.optString("jobTitle"));
                Log.e("dATAwergweg", "" + jsonObjSuccess.toString());


                providerPostedTime.setText("Posted: " + Utility.printDifference(jsonObjectData.optString("createdAt")));
                startDate.setText("Start Date: " + getDateFromUtc(jsonObjectData.optString("startDate")));

                estimatedTime.setText("Job Days: " + jsonObjectData.optString("jobHours"));
                noOfLabours.setText("No. of labours: " + jsonObjectData.optString("noOfLabours"));

                bidNoint = jsonObjectData.optInt("bidCount");
                for (int i = 0; i < jsonArraySkill.length(); i++) {
                    //skillGet = TextUtils.join(", ", new String[]{jsonArraySkill.getJSONObject(i).optString("name")});
                    if (skillsValue.getText().toString().compareTo(jsonArraySkill.getJSONObject(i).optString("name")) != 0) {
                        if (i == 0) {
                            skillsValue.setText(jsonArraySkill.getJSONObject(i).optString("name"));
                        } else if (i > 0) {
                            skillsValue.setText(skillsValue.getText().toString() + "," + jsonArraySkill.getJSONObject(i).optString("name"));
                        }
                    }
                }

                descriptionValue.setText("" + jsonObjectData.optString("description"));

                addressValue.setText("" + jsonObjectData.getJSONObject("address").getString("value"));
                streetValue.setText("" + jsonObjectData.getJSONObject("address").getString("street"));
//                jobGetterSetter.setApplierName(jsonArrayData.getJSONObject(i).getJSONObject("applier").getString("fullName"));
//                jobGetterSetter.setBidAmount(jsonArrayData.getJSONObject(i).getJSONObject("bid").getInt("cost"));

                bidRequest = jsonObjectData.optString("status");
                if (jsonObjectData.optString("status").equals(getResources().getString(R.string.postedStatus))) {

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

                    payPerHour.setText(amountpph);

                    bidNo = "No. of bids: " + jsonObjectData.optString("bidCount");

                    SpannableString content = new SpannableString(bidNo);
                    content.setSpan(new UnderlineSpan(), 0, bidNo.length(), 0);
                    jobBidPlace.setText(content);
//                    jobBidPlace.setText("No. of bids: " + jsonObjectData.optString("bidCount"));
                    completedDate.setVisibility(View.GONE);
                    layoutImages.setVisibility(View.GONE);
                }

                if (jsonObjectData.optString("status").equals(getResources().getString(R.string.ongoingStatus))) {

//                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getJSONObject("bid").getString("cost"));
                    bidNo = "Bid Placed by: " + jsonObjectData.getJSONObject("applier").getString("fullName");
                    BidderId = jsonObjectData.getString("applierId");
                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getInt("finalAmount"));
                    SpannableString content = new SpannableString(bidNo);
                    content.setSpan(new UnderlineSpan(), 0, bidNo.length(), 0);
                    jobBidPlace.setText(content);
                    completedDate.setVisibility(View.GONE);
                    layoutImages.setVisibility(View.GONE);
                    paymentType.setVisibility(View.GONE);
                }

                if (jsonObjectData.optString("status").equals(getResources().getString(R.string.completedStatus))) {

                    bidNo = "Completed by: " + jsonObjectData.getJSONObject("applier").getString("fullName");
//                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getJSONObject("bid").getString("cost"));
                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getInt("finalAmount"));
                    BidderId = jsonObjectData.getString("applierId");
                    SpannableString content = new SpannableString(bidNo);
                    content.setSpan(new UnderlineSpan(), 0, bidNo.length(), 0);
                    jobBidPlace.setText(content);
                    completedDate.setVisibility(View.VISIBLE);
                    jobBidPlace.setVisibility(View.VISIBLE);
                    layoutImages.setVisibility(View.VISIBLE);
                    paymentType.setVisibility(View.GONE);

                    completedDate.setText("Completed On: " + getDateFromUtc(jsonObjectData.getString("completedAt")));

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
//                    amountPending.setVisibility(View.VISIBLE);
//                    if (getNoti) {
                    jobTitlestr = jsonObjectData.getString("jobTitle");
                    createdAtstr = jsonObjectData.getString("createdAt");
                    applierNamestr = jsonObjectData.getJSONObject("applier").getString("fullName");
                    bidCoststr = jsonObjectData.getJSONObject("bid").getString("cost");

                    if (jsonObjectData.optJSONArray("ratedetail") != null) {

                        for (int i = 0; i < jsonObjectData.optJSONArray("ratedetail").length(); i++) {
                            if (jsonObjectData.optJSONArray("ratedetail").optJSONObject(i).optString("forUser") != null) {
                                if (jsonObjectData.optJSONArray("ratedetail").optJSONObject(i).optString("forUser").compareTo("customer") == 0) {
                                    btnGiveRating.setVisibility(View.GONE);

                                    reviewTitle.setVisibility(View.VISIBLE);
                                    reviewValue.setVisibility(View.VISIBLE);
                                    ratingTitle.setVisibility(View.VISIBLE);
                                    jobRating.setVisibility(View.VISIBLE);
                                    reviewValue.setText("" + jsonObjectData.optJSONArray("ratedetail").optJSONObject(i).optString("comment"));
                                    jobRating.setRating(jsonObjectData.optJSONArray("ratedetail").optJSONObject(i).optInt("userRating"));


                                }/* else {
                                    bidRequest = getResources().getString(R.string.giveRating);
                                    btnGiveRating.setText(getResources().getString(R.string.giveRating));
                                    btnGiveRating.setVisibility(View.VISIBLE);
                                }*/
                            }


                        }

                    } else {
                        bidRequest = getResources().getString(R.string.giveRating);
                        btnGiveRating.setText(getResources().getString(R.string.giveRating));
                        btnGiveRating.setVisibility(View.VISIBLE);
                    }
                }
                if (jsonObjectData.optString("status").equals(getResources().getString(R.string.amountpendingStatus))) {
                    bidNo = "Bid Placed by: " + jsonObjectData.getJSONObject("applier").getString("fullName");
//                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getJSONObject("bid").getString("cost"));
                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getInt("finalAmount"));
                    BidderId = jsonObjectData.getString("applierId");
                    SpannableString content = new SpannableString(bidNo);
                    content.setSpan(new UnderlineSpan(), 0, bidNo.length(), 0);
                    jobBidPlace.setText(content);
                    completedDate.setVisibility(View.VISIBLE);
                    jobBidPlace.setVisibility(View.VISIBLE);
                    layoutImages.setVisibility(View.VISIBLE);
                    paymentType.setVisibility(View.GONE);
                    completedDate.setText("Completed On: " + getDateFromUtc(jsonObjectData.getString("applierCompleteAt")));

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
//                    if (getNoti) {
                    amountPending.setVisibility(View.VISIBLE);
                    jobTitlestr = jsonObjectData.getString("jobTitle");
                    createdAtstr = jsonObjectData.getString("createdAt");
                    applierNamestr = jsonObjectData.getJSONObject("applier").getString("fullName");
                    bidCoststr = jsonObjectData.getJSONObject("bid").getString("cost");
                    showPopupDailog(jsonObjectData.getString("jobTitle"), jsonObjectData.getString("createdAt")
                            , jsonObjectData.getJSONObject("applier").getString("fullName"), jsonObjectData.getJSONObject("bid").getString("cost"));

//                    }


                }

                if (jsonObjectData.optString("status").equals(getResources().getString(R.string.escalatedStatus))) {
                    bidNo = "Bid Placed by: " + jsonObjectData.getJSONObject("applier").getString("fullName");
//                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getJSONObject("bid").getString("cost"));
                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getInt("finalAmount"));
                    BidderId = jsonObjectData.getString("applierId");

                    SpannableString content = new SpannableString(bidNo);
                    content.setSpan(new UnderlineSpan(), 0, bidNo.length(), 0);
                    jobBidPlace.setText(content);

                    completedDate.setVisibility(View.VISIBLE);
                    jobBidPlace.setVisibility(View.VISIBLE);
                    layoutImages.setVisibility(View.VISIBLE);

                    reasonEscalationTitle.setVisibility(View.VISIBLE);
                    reasonEscalationValue.setVisibility(View.VISIBLE);
                    paymentType.setVisibility(View.GONE);

                    completedDate.setText("Completed On: " + getDateFromUtc(jsonObjectData.getString("applierCompleteAt")));
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

                }
                if (jsonObjectData.optString("status").equals(getResources().getString(R.string.satisfiedStatus))) {
                    bidNo = "Bid Placed by: " + jsonObjectData.getJSONObject("applier").getString("fullName");
//                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getJSONObject("bid").getString("cost"));
                    payPerHour.setText("Job Amount: " + "$ " + jsonObjectData.getInt("finalAmount"));
                    BidderId = jsonObjectData.getString("applierId");
                    SpannableString content = new SpannableString(bidNo);
                    content.setSpan(new UnderlineSpan(), 0, bidNo.length(), 0);
                    jobBidPlace.setText(content);
                    completedDate.setVisibility(View.VISIBLE);
                    jobBidPlace.setVisibility(View.VISIBLE);
                    layoutImages.setVisibility(View.VISIBLE);
                    paymentType.setVisibility(View.GONE);
                    completedDate.setText("Completed On: " + getDateFromUtc(jsonObjectData.getString("applierCompleteAt")));
                    reasonEscalationValue.setText(jsonObjectData.getString("reason"));

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
                }

/*
                if (bidRequest.equals("escalated")) {
                    bidNo = "Labour Name : eggi";
                    reasonValue.setVisibility(View.VISIBLE);
                    reasonTitle.setVisibility(View.VISIBLE);
                    layoutEscalation.setVisibility(View.VISIBLE);
                    layoutImages.setVisibility(View.VISIBLE);
                }
*/

            }
        } catch (Exception e) {
            Log.e("exceptionGet", "" + e);
        }

    }

    class ImageCompressionAsyncTaskDoc extends AsyncTask<String, Void, String> {

        Context mContext;

        public ImageCompressionAsyncTaskDoc(Context context) {
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(CustomerJobDetail.this);
        }

        @Override
        protected String doInBackground(String... params) {
            String filePath = null;
            try {

                filePath = SiliCompressor.with(CustomerJobDetail.this).compress(params[0], new File(params[1]));

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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(CustomerJobDetail.this.getContentResolver(), compressUri);
                    listBitmap.add(bitmap);
                    filesList.add(compressedImage);
                    Log.e("bererbwef", listBitmap.size() + "");
                    // notificationIcon.setImageBitmap(bitmap);
                    if (listBitmap.size() <= 2) {
                        imagesAdapter = new ImagesCrossAdapter(listBitmap, filesList, CustomerJobDetail.this, imageRecycler);
                        imageRecycler.setAdapter(imagesAdapter);
                    } else {
                        showToast(CustomerJobDetail.this, getResources().getString(R.string.PhotoValidation));
                    }


                } catch (IOException e) {
                    e.printStackTrace();

                }

            }
        }
    }

}
