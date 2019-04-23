package leebeno.com.leebeno;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import leebeno.com.leebeno.Adapters.ReceivedRatingAdapter;
import leebeno.com.leebeno.AdminPart.HomeAdminActivity;
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.HomeCustomerActivity;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.LabourPart.HomeLabourActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static leebeno.com.leebeno.Services.Utility.close;
import static leebeno.com.leebeno.Services.Utility.showProgress;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class PostedRatings extends AppCompatActivity {
    @Bind(R.id.recyclerRRating)
    RecyclerView recyclerRRating;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.serviceNotMsg)
    TextView serviceNotMsg;

    ReceivedRatingAdapter ratingAdapter;
    List<JobGetterSetter> getPostedJobs;

    static int valueGett = 0;
    /*List<String> listJobItem;
    List<String> listJobItemT;
    List<String> listJobItemD;
    List<Float> listJobItemR;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_ratings);
        ButterKnife.bind(this);

        recyclerRRating.setHasFixedSize(true);
        recyclerRRating.setNestedScrollingEnabled(false);
        recyclerRRating.setLayoutManager(new LinearLayoutManager(PostedRatings.this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecor = new DividerItemDecoration(PostedRatings.this, DividerItemDecoration.VERTICAL);
        recyclerRRating.addItemDecoration(itemDecor);

        getPostedJobs = new ArrayList<>();
       /* listJobItem=new ArrayList<>();
        listJobItemD=new ArrayList<>();
        listJobItemT=new ArrayList<>();
        listJobItemR=new ArrayList<>();

        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k");
        listJobItemT.add("11 Minutes ago");
        listJobItemR.add((float) 1.3);
        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k");
        listJobItemT.add("11 Minutes ago");
        listJobItemR.add((float) 3.3);
        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k");
        listJobItemT.add("11 Minutes ago");
        listJobItemR.add((float) 1.3);
        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k");
        listJobItemT.add("11 Minutes ago");
        listJobItemR.add((float) 4.3);
        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k");
        listJobItemT.add("11 Minutes ago");
        listJobItemR.add((float) 1.3);
        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k");
        listJobItemT.add("11 Minutes ago");
        listJobItemR.add((float) 3.3);
        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k");
        listJobItemT.add("11 Minutes ago");
        listJobItemR.add((float) 1.3);
        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k");
        listJobItemT.add("11 Minutes ago");
        listJobItemR.add((float) 4.3);*/

        ratingAdapter = new ReceivedRatingAdapter(getPostedJobs, PostedRatings.this);
        recyclerRRating.setAdapter(ratingAdapter);


        showAllPostedRating();
        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showAllPostedRating() {
        showProgress(PostedRatings.this);
      /*  JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("jobId", jobId);
        jsonObject.addProperty("userRating", ratingValue);
        jsonObject.addProperty("comment", "" + etReview.getText().toString().trim());*/
      Log.e("vfdbfdbfgbg",""+SharedPrefManager.getAccessToken(PostedRatings.this, Constrants.AccessToken)+"  "+
              SharedPrefManager.getUserID(PostedRatings.this, Constrants.UserID));

        ApiConfig apiInterface = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<JsonObject> call = apiInterface.getPostedRating(SharedPrefManager.getAccessToken(PostedRatings.this, Constrants.AccessToken), SharedPrefManager.getLangId(PostedRatings.this, Constrants.UserLang), SharedPrefManager.getUserID(PostedRatings.this, Constrants.UserID));
//        Log.e("dfgdf", "" + jsonObject);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                close();
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {

                        getPostedJobs.clear();
                        jsonObject = new JSONObject(response.body().toString());
                        JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                        JSONArray jsonArrayData = jsonObjSuccess.getJSONArray("data");
                        Log.e("PostedRatings", "" + jsonObject);
                        if (jsonArrayData != null && jsonArrayData.length() > 0) {

//                            Log.e("PostedRatings", "" + jsonObject);
                            for (int i = 0; i < jsonArrayData.length(); i++) {
                                JobGetterSetter jobGetterSetter = new JobGetterSetter();
//                                JSONArray jsonArrayBidders = jsonArrayData.getJSONObject(i).getJSONObject("job").getJSONArray("bidders");

                                if (jsonArrayData.getJSONObject(i).optJSONObject("job")!=null)
                                {
                                    jobGetterSetter.setJobTitle(jsonArrayData.getJSONObject(i).getJSONObject("job").getString("jobTitle"));

//                                jobGetterSetter.setDescription(jsonArrayData.getJSONObject(i).getJSONObject("job").getString("description"));
                                    jobGetterSetter.setStartDate(jsonArrayData.getJSONObject(i).getJSONObject("job").getString("startDate"));
//                                jobGetterSetter.setBidCount(jsonArrayData.getJSONObject(i).getJSONObject("job").getInt("bidCount"));

                                    jobGetterSetter.setId(jsonArrayData.getJSONObject(i).getJSONObject("job").getString("id"));
                                    jobGetterSetter.setJOBID(jsonArrayData.getJSONObject(i).getString("jobId"));
                                }
                                jobGetterSetter.setRewardPoint(jsonArrayData.getJSONObject(i).optString("rewardPoints"));
                                jobGetterSetter.setJobRating(jsonArrayData.getJSONObject(i).getString("userRating"));
                                jobGetterSetter.setReviewComment(jsonArrayData.getJSONObject(i).getString("comment"));
                                jobGetterSetter.setCreatedJob(jsonArrayData.getJSONObject(i).getString("createdAt"));

                             /*   if (SharedPrefManager.getUserStatus(PostedRatings.this, Constrants.UserStatus).compareTo("customer") == 0) {

                                    if (jsonArrayData.getJSONObject(i).getJSONObject("job").optJSONArray("ratedetail") != null) {

                                        for (int j = 0; j < jsonArrayData.getJSONObject(i).getJSONObject("job").optJSONArray("ratedetail").length(); j++) {
                                            if (jsonArrayData.getJSONObject(i).getJSONObject("job").optJSONArray("ratedetail").optJSONObject(j).optString("forUser") != null) {
                                                if (jsonArrayData.getJSONObject(i).getJSONObject("job").optJSONArray("ratedetail").optJSONObject(j).optString("forUser").compareTo("customer") == 0) {
                                                    jobGetterSetter.setReviewComment(jsonArrayData.getJSONObject(i).getJSONObject("job").optJSONArray("ratedetail").optJSONObject(j).getString("comment"));
                                                    jobGetterSetter.setJobRating(jsonArrayData.getJSONObject(i).getJSONObject("job").optJSONArray("ratedetail").optJSONObject(j).getString("userRating"));
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (jsonArrayData.getJSONObject(i).getJSONObject("job").optJSONArray("ratedetail") != null) {

                                        for (int j = 0; j < jsonArrayData.getJSONObject(i).getJSONObject("job").optJSONArray("ratedetail").length(); j++) {
                                            if (jsonArrayData.getJSONObject(i).getJSONObject("job").optJSONArray("ratedetail").optJSONObject(j).optString("forUser") != null) {
                                                if (jsonArrayData.getJSONObject(i).getJSONObject("job").optJSONArray("ratedetail").optJSONObject(j).optString("forUser").compareTo("applier") == 0) {
                                                    jobGetterSetter.setReviewComment(jsonArrayData.getJSONObject(i).getJSONObject("job").optJSONArray("ratedetail").optJSONObject(j).getString("comment"));
                                                    jobGetterSetter.setJobRating(jsonArrayData.getJSONObject(i).getJSONObject("job").optJSONArray("ratedetail").optJSONObject(j).getString("userRating"));
                                                }
                                            }
                                        }
                                    }

                                }*/
                                valueGett = 1;
                                getPostedJobs.add(jobGetterSetter);
                                serviceNotMsg.setVisibility(View.GONE);

                            }
                        } else {
                            serviceNotMsg.setVisibility(View.VISIBLE);
                        }
                        ratingAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONObject jsonObjError = jsonObject.getJSONObject("error");
                        showToast(PostedRatings.this, "" + jsonObjError.getString("message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                close();
                Log.e("vvxvxvxv", t + "");
            }
        });
    }
}
