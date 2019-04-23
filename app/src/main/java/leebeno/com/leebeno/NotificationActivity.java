package leebeno.com.leebeno;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Adapters.NotificationAdapter;
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import static leebeno.com.leebeno.Services.Utility.close;
import static leebeno.com.leebeno.Services.Utility.showProgress;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class NotificationActivity extends AppCompatActivity {

    @Bind(R.id.recyclerNotification)
    RecyclerView recyclerNotification;
    NotificationAdapter notificationAdapter;

    @Bind(R.id.emptyLayout)
    LinearLayout emptyLayout;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.cardAdmin)
    CardView cardAdmin;
    String jobId;
    List<JobGetterSetter> getPostedJobs;
    int amount;
    static int valueGett = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);

        jobId = getIntent().getStringExtra("jobId");

        getPostedJobs = new ArrayList<>();
        recyclerNotification.setHasFixedSize(true);
        recyclerNotification.addItemDecoration(new DividerItemDecoration(NotificationActivity.this, DividerItemDecoration.VERTICAL));
        recyclerNotification.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        notificationAdapter = new NotificationAdapter(getPostedJobs, NotificationActivity.this);
        recyclerNotification.setAdapter(notificationAdapter);
        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getNotificationDetail();

    }


    private void getNotificationDetail() {
        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        showProgress(NotificationActivity.this);


        Log.e("SENDDATA", "" + SharedPrefManager.getAccessToken(NotificationActivity.this, Constrants.AccessToken));
        Call<JsonObject> getPostData = apiInterface.getNotifications(SharedPrefManager.getAccessToken(NotificationActivity.this, Constrants.AccessToken));
        getPostData.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        close();
//                        assert response.body() != null;
                        JSONObject jsonObject = new JSONObject(response.body().toString());
                        Log.e("fdvvbsjkdsv", jsonObject.toString() + "");
                        JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                        JSONObject jsonObjectMsg = jsonObjSuccess.getJSONObject("msg");
                        JSONArray jsonObjectData = jsonObjSuccess.getJSONArray("data");
                        if (jsonObjectData != null && jsonObjectData.length() > 0) {

                            for (int i = 0; i < jsonObjectData.length(); i++) {

                                JobGetterSetter jobGetterSetter = new JobGetterSetter();
                                jobGetterSetter.setJobTitle(jsonObjectData.getJSONObject(i).getString("title"));
                                jobGetterSetter.setDescription(jsonObjectData.getJSONObject(i).getString("body"));
                                jobGetterSetter.setJobType(jsonObjectData.getJSONObject(i).getString("type"));
                                jobGetterSetter.setCreatedJob(jsonObjectData.getJSONObject(i).getString("Date"));
                                jobGetterSetter.setJOBID(jsonObjectData.getJSONObject(i).getString("jobId"));
                                getPostedJobs.add(jobGetterSetter);

                            }

                            Log.e("listSize",getPostedJobs.size()+"");
                            notificationAdapter.notifyDataSetChanged();
                            valueGett = 1;
                            emptyLayout.setVisibility(View.GONE);

                        }else if (valueGett == 0) {
                            emptyLayout.setVisibility(View.VISIBLE);

                        }

                    } catch (Exception e) {
                        Log.e("BidPlace", "" + e);
                    }
                }else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(NotificationActivity.this, "" + jsonObjError.getString("message"));
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


//    private void getJobDetail(final String id) {
//        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
////        showProgress(HomeLabourActivity.this);
//
//
//        Log.e("SENDDATA", "" + SharedPrefManager.getAccessToken(NotificationActivity.this, Constrants.AccessToken) + "  " + SharedPrefManager.getUserID(NotificationActivity.this, Constrants.UserID) + "  " + id + "  " + "  ");
//        Call<JsonObject> getPostData = apiInterface.getJobDetail(SharedPrefManager.getAccessToken(NotificationActivity.this, Constrants.AccessToken), SharedPrefManager.getLangId(NotificationActivity.this
//                , Constrants.UserLang), id, SharedPrefManager.getUserID(NotificationActivity.this, Constrants.UserID));
//        getPostData.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//                    try {
////                        close();
//                        JSONObject jsonObject = new JSONObject(response.body().toString());
//                        Log.e("fdvvbsjkdsv", jsonObject.toString() + "");
//                        JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
//                        JSONObject jsonObjectMsg = jsonObjSuccess.getJSONObject("msg");
//                        JSONObject jsonObjectData = jsonObjSuccess.getJSONObject("data");
////                        showToast(HomeLabourActivity.this, jsonObjectMsg.getString("replyMessage"));
//
//                        if (SharedPrefManager.getUserStatus(NotificationActivity.this, Constrants.UserStatus).compareTo("labour")==0)
//                        {
//                            Intent intent = new Intent(NotificationActivity.this, BidPlace.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("jobId", id);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//
//                        }else if (SharedPrefManager.getUserStatus(NotificationActivity.this, Constrants.UserStatus).compareTo("groupAdmin")==0)
//                        {
//                            Intent intent = new Intent(NotificationActivity.this, BidPlace.class);
//                            Bundle bundle = new Bundle();
//                            bundle.putString("jobId", id);
//                            intent.putExtras(bundle);
//                            startActivity(intent);
//
//                        }else if (SharedPrefManager.getUserStatus(NotificationActivity.this, Constrants.UserStatus).compareTo("customer")==0)
//                        {
//
//
//                            if (jsonObjectData.getString("paymentType").compareTo(getResources().getString(R.string.range)) == 0)
//                            {
//                                amount= Integer.parseInt(jsonObjectData.getInt("max")+" "+jsonObjectData.getInt("min"));
//
//                            }else
//                            {
//                                amount=jsonObjectData.getInt("amount");
//                            }
//
//                            if (jsonObjectData.getString("status").compareTo(getResources().getString(R.string.amountpendingStatus))==0)
//                            {
//
//                                showPopupDailog(jsonObjectData.getString("jobTitle"),jsonObjectData.getString("createdAt")
//                                        ,jsonObjectData.getJSONObject("applier").getString("fullName"), String.valueOf(amount));
////                                jobGetterSetter.setApplierName(jsonObjectData.getJSONObject("applier").getString("fullName"));
//
//                            }else {
//                                Intent intent = new Intent(NotificationActivity.this, CustomerJobDetail.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("jobId", id);
//                                bundle.putBoolean("noti", true);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                            }
//
//                        }
//
//
//
//                    } catch (Exception e) {
//                        Log.e("BidPlace", "" + e);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.e("BidPlace", "" + t);
////                close();
//            }
//        });
//    }


//    private void getReadNoti(String id) {
//        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
//        showProgress(NotificationActivity.this);
//
//
//        Log.e("SENDDATA", "" + SharedPrefManager.getAccessToken(NotificationActivity.this, Constrants.AccessToken) + "  " + SharedPrefManager.getUserID(NotificationActivity.this, Constrants.UserID) + "  " + id + "  " + "  ");
//        Call<JsonObject> getPostData = apiInterface.readAllNoti(SharedPrefManager.getAccessToken(NotificationActivity.this, Constrants.AccessToken));
//        getPostData.enqueue(new Callback<JsonObject>() {
//            @Override
//            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
//                if (response.isSuccessful()) {
//                    try {
//                        close();
//                        JSONObject jsonObject = new JSONObject(response.body().toString());
//                        Log.e("fdvvbsjkdsv", jsonObject.toString() + "");
//                        JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
//                        JSONObject jsonObjectMsg = jsonObjSuccess.getJSONObject("msg");
//                        JSONArray jsonObjectData = jsonObjSuccess.getJSONArray("data");
//                        JobGetterSetter jobGetterSetter = new JobGetterSetter();
//                        for (int i = 0; i < jsonObjectData.length(); i++) {
//
//                            showPopupDailog();
//
//                            jobGetterSetter.setJobTitle(jsonObjectData.getJSONObject(i).getString("title"));
//                            jobGetterSetter.setDescription(jsonObjectData.getJSONObject(i).getString("body"));
//                            jobGetterSetter.setJobType(jsonObjectData.getJSONObject(i).getString("type"));
//                            jobGetterSetter.setCreatedJob(jsonObjectData.getJSONObject(i).getString("Date"));
//                            jobGetterSetter.setJOBID(jsonObjectData.getJSONObject(i).getString("jobId"));
//
//                            getPostedJobs.add(jobGetterSetter);
//
//                        }
//                        notificationAdapter = new NotificationAdapter(getPostedJobs, NotificationActivity.this);
//                        recyclerNotification.setAdapter(notificationAdapter);
//
//
//                    } catch (Exception e) {
//                        Log.e("BidPlace", "" + e);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<JsonObject> call, Throwable t) {
//                Log.e("BidPlace", "" + t);
//                close();
//            }
//        });
//    }

/*
    private void showPopupDailog(String jobTitle,String postedDate,String completedBy,String payPerHour) {
        try {
            final Dialog dialog = new Dialog(NotificationActivity.this);
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
            imageRecycler = (RecyclerView) dialog.findViewById(R.id.imageRecycler);

            textJobTitle.setText(jobTitle);
            textPostedDate.setText(getDateFromUtc(postedDate));
            textCompletedBy.setText(completedBy);
            textPayPerHour.setText(payPerHour);

            imageRecycler.setHasFixedSize(true);
            imageRecycler.setNestedScrollingEnabled(true);
            imageRecycler.setLayoutManager(new LinearLayoutManager(NotificationActivity.this, LinearLayoutManager.HORIZONTAL, false));

            Button btnSubmit = (Button) dialog.findViewById(R.id.btnSubmit);
            final Button btnApprove = (Button) dialog.findViewById(R.id.btnApprove);
            final Button btnDecline = (Button) dialog.findViewById(R.id.btnDecline);

            btnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editReason.setVisibility(View.VISIBLE);
                    textAttachFile.setVisibility(View.VISIBLE);
                    imageRecycler.setVisibility(View.VISIBLE);
                    btnApprove.setBackground(getResources().getDrawable(R.drawable.primary_circle_background));
                    btnApprove.setTextColor(getResources().getColor(R.color.white));
                    btnDecline.setBackground(getResources().getDrawable(R.drawable.round_border));
                    btnDecline.setTextColor(getResources().getColor(R.color.gray));
                }
            });
            btnDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editReason.setVisibility(View.GONE);
                    textAttachFile.setVisibility(View.GONE);
                    imageRecycler.setVisibility(View.GONE);
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
            textAttachFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final Dialog dialog = new Dialog(NotificationActivity.this);
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
                                    if (ActivityCompat.checkSelfPermission(NotificationActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(NotificationActivity.this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
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
                                    if (ActivityCompat.checkSelfPermission(NotificationActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(NotificationActivity.this, new String[]{android.Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
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
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/





/*
    private void getNotificationAll() {
        showProgress(NotificationActivity.this);
        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<JSONObject> call = apiInterface.getAllNotification();
        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                close();
                if (response.isSuccessful()) {

                        notificationAdapter = new NotificationAdapter(response.body().getSuccess().getData(), NotificationActivity.this);
                        recyclerNotification.setAdapter(notificationAdapter);

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(NotificationActivity.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                close();
            }
        });
    }
*/

}
