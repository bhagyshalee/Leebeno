package leebeno.com.leebeno;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Adapters.ReceivedBidAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;

public class ReceivedBidRequest extends AppCompatActivity implements WebCompleteTask {

    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.recyclerBids)
    RecyclerView recyclerBids;
    ReceivedBidAdapter ratingAdapter;
    String jobId;
    List<JobGetterSetter> getPostedJobs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_bid_request);
        ButterKnife.bind(this);

        jobId = getIntent().getStringExtra("jobId");
        getPostedJobs = new ArrayList<>();


        recyclerBids.setHasFixedSize(true);
        recyclerBids.setNestedScrollingEnabled(false);
        recyclerBids.setLayoutManager(new LinearLayoutManager(ReceivedBidRequest.this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecor = new DividerItemDecoration(ReceivedBidRequest.this, DividerItemDecoration.VERTICAL);
        recyclerBids.addItemDecoration(itemDecor);

        Log.e("rbrtnrehrehrth", jobId + " " + SharedPrefManager.getAccessToken(ReceivedBidRequest.this, Constrants.AccessToken));

        HashMap hashMap = new HashMap();
        new WebTask(ReceivedBidRequest.this, WebUrls.BASE_URL + WebUrls.getReceivedBidList + "jobId=" + jobId
                , hashMap, ReceivedBidRequest.this, RequestCode.CODE_getAllBid, 0);


        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == RequestCode.CODE_getAllBid) {
                getPostedJobs.clear();
                JSONObject jsonObject = null;
                JSONArray jsonArrayData = null;
                try {

                    jsonObject = new JSONObject(response);

                    JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                    jsonArrayData = jsonObjSuccess.getJSONArray("data");
                    Log.e("ReceivedBidRequests", jsonObjSuccess.toString() + "");

                    //JSONObject jsonMsg = jsonObjSuccess.getJSONObject("msg");
                    JobGetterSetter jobGetterSetter = null;
                    JSONObject jsonObjectJobbidPerson = null;
                    for (int i = 0; i < jsonArrayData.length(); i++) {
                        jobGetterSetter = new JobGetterSetter();
                        jsonObjectJobbidPerson = jsonArrayData.getJSONObject(i).getJSONObject("bidPerson");
                        jobGetterSetter.setJobTitle(jsonObjectJobbidPerson.getString("fullName"));
                        jobGetterSetter.setDescription(jsonArrayData.getJSONObject(i).getString("query"));
                        jobGetterSetter.setId(jsonArrayData.getJSONObject(i).getString("id"));
                        jobGetterSetter.setCreatedJob(jsonArrayData.getJSONObject(i).getString("updatedAt"));
                        jobGetterSetter.setStatus(jsonArrayData.getJSONObject(i).getString("bidStatus"));
                        getPostedJobs.add(jobGetterSetter);
                    }


//                    jobGetterSetter.setBidCount(jsonObjectJob.getInt("bidCount"));

                    ratingAdapter = new ReceivedBidAdapter(getPostedJobs, ReceivedBidRequest.this);
                    recyclerBids.setAdapter(ratingAdapter);

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }



            }
        } catch (Exception e) {
            Log.e("exceptionGet", "" + e);
        }

    }


}
