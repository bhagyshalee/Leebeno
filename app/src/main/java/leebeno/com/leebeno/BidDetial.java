package leebeno.com.leebeno;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;

import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;

public class BidDetial extends AppCompatActivity implements WebCompleteTask {

    @Bind(R.id.backSignUp)
    ImageView backSignUp;

    @Bind(R.id.title_job)
    TextView title_job;

    @Bind(R.id.status_job)
    TextView status_job;

    @Bind(R.id.query_bid)
    TextView query_bid;

    @Bind(R.id.usd_price)
    TextView usd_price;

    @Bind(R.id.time_estimation)
    TextView time_estimation;

    @Bind(R.id.bid_placed_date)
    TextView bid_placed_date;

    @Bind(R.id.bid_reject_date)
    TextView bid_reject_date;
    @Bind(R.id.titleJobRejectDate)
    TextView titleJobRejectDate;


    String bidId,jobid,bid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_detial);

        ButterKnife.bind(this);
        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

/*
        title_job.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BidDetial.this,BidPlace.class).putExtra("jobId",jobid).putExtra("bid",bid));
            }
        });
*/

            bidId = getIntent().getStringExtra("bid_id");
            GetBidDetails();

//        }
    }

    public void GetBidDetails(){
        HashMap objectNew = new HashMap();
        new WebTask(BidDetial.this, WebUrls.BASE_URL+WebUrls.getSingleBid+ "bidId=" + bidId,objectNew,BidDetial.this, RequestCode.CODE_getSingleBid,3);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_getSingleBid) {
            JSONObject jsonObject = null;
            JSONObject jsonObjectData = null, jsonObjectBidPerson;
            try {

                jsonObject = new JSONObject(response);

                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                jsonObjectData = jsonObjSuccess.getJSONObject("data");
                jsonObjectBidPerson = jsonObjectData.getJSONObject("bidPerson");
                Log.e("fdvvbsjkdsv", jsonObjSuccess.toString() + "");
                String labourName = jsonObjectData.optJSONObject("job").optString("jobTitle");
               /* SpannableString content = new SpannableString(labourName);
                content.setSpan(new UnderlineSpan(), 0, labourName.length(), 0);*/
                title_job.setText(labourName);
//                    providerName.setText("Bidder Name: " + );
//                BidderId = jsonObjectBidPerson.getString("id");
//                title_job.setText(jsonObjectData.optJSONObject("job").optString("jobTitle"));
                jobid = jsonObjectData.optString("jobId");
                bid = jsonObjectData.optString("bidStatus");
                status_job.setText(jsonObjectData.optString("bidStatus"));
                if (jsonObjectData.optString("bidStatus").equals("accepted"))
                {
                    status_job.setTextColor(getResources().getColor(R.color.green));
                    titleJobRejectDate.setText(getResources().getString(R.string.bid_accept_date));
                }else {
                    status_job.setTextColor(getResources().getColor(R.color.red));
                    titleJobRejectDate.setText(getResources().getString(R.string.bid_reject_date));
                }
                bid_placed_date.setText(getDateFromUtc(jsonObjectData.getString("createdAt")));
                bid_reject_date.setText(getDateFromUtc(jsonObjectData.getString("updatedAt")));
                time_estimation.setText(jsonObjectData.getString("time")+" Days");
                usd_price.setText("$ " + jsonObjectData.getString("cost"));
                query_bid.setText(jsonObjectData.getString("query"));


            } catch (JSONException e1) {
                e1.printStackTrace();
            }


        }
    }
}
