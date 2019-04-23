package leebeno.com.leebeno.CustomerPart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.LabourProfile;
import leebeno.com.leebeno.R;

import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;
import static leebeno.com.leebeno.Services.Utility.showToast;

public class RequestBid extends AppCompatActivity implements View.OnClickListener, WebCompleteTask {

    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.providerName)
    TextView providerName;
    @Bind(R.id.providerPostedTime)
    TextView providerPostedTime;
    @Bind(R.id.estimationTime)
    TextView estimationTime;
    @Bind(R.id.estimationCost)
    TextView estimationCost;
    @Bind(R.id.commentQuery)
    TextView commentQuery;
    @Bind(R.id.btnAccept)
    Button btnAccept;
    @Bind(R.id.btnReject)
    Button btnReject;
    String bidId, BidderId, bidStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_bid);
        ButterKnife.bind(this);

        bidId = getIntent().getStringExtra("bidId");
        bidStatus = getIntent().getStringExtra("bidStatus");
        Log.e("fnjkfdrgfgbb", SharedPrefManager.getAccessToken(RequestBid.this, Constrants.AccessToken)
                + "" + bidId);


        HashMap hashMap = new HashMap();
        new WebTask(RequestBid.this, WebUrls.BASE_URL + WebUrls.getSingleBid + "bidId=" + bidId
                , hashMap, RequestBid.this, RequestCode.CODE_getSingleBid, 0);


        if (bidStatus.compareTo(getResources().getString(R.string.rejected)) == 0) {
            btnAccept.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
        }
        backSignUp.setOnClickListener(this);
        btnAccept.setOnClickListener(this);
        btnReject.setOnClickListener(this);
        providerName.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == providerName) {
            Intent intent = new Intent(RequestBid.this, LabourProfile.class);
            intent.putExtra("jobStatus", "pending");
            intent.putExtra("bidPersonId", BidderId);
            startActivity(intent);
        }
        if (v == backSignUp) {
            finish();
        }
        if (v == btnAccept) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("bidId", "" + bidId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new WebTask(RequestBid.this, WebUrls.BASE_URL + WebUrls.acceptBid
                    , jsonObject, RequestBid.this, RequestCode.CODE_acceptBid, 1);

        }
        if (v == btnReject) {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("bidId", "" + bidId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new WebTask(RequestBid.this, WebUrls.BASE_URL + WebUrls.rejectBid
                    , jsonObject, RequestBid.this, RequestCode.CODE_rejectBid, 1);

        }
    }

    @Override
    public void onComplete(String response, int taskcode) {
        try {

            if (taskcode == RequestCode.CODE_acceptBid) {
                JSONObject jsonObject = null;
                JSONObject jsonObjectData = null, jsonObjectBidPerson;
                try {

                    jsonObject = new JSONObject(response);

                    JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                    jsonObjectData = jsonObjSuccess.getJSONObject("msg");
                    showToast(RequestBid.this, "" + jsonObjectData.getString("replyMessage"));
                    Intent intent = new Intent(RequestBid.this, HomeCustomerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Log.e("acceptDATAA", "" + jsonObjSuccess);
                    finish();

                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }
            if (taskcode == RequestCode.CODE_rejectBid) {
                JSONObject jsonObject = null;
                JSONObject jsonObjectData = null, jsonObjectBidPerson;
                try {

                    jsonObject = new JSONObject(response);

                    JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                    jsonObjectData = jsonObjSuccess.getJSONObject("msg");
                    showToast(RequestBid.this, "" + jsonObjectData.getString("replyMessage"));
                    Log.e("rejectDATAA", "" + jsonObjSuccess);
                    Intent intent = new Intent(RequestBid.this, HomeCustomerActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }
            if (taskcode == RequestCode.CODE_getSingleBid) {
                JSONObject jsonObject = null;
                JSONObject jsonObjectData = null, jsonObjectBidPerson;
                try {

                    jsonObject = new JSONObject(response);

                    JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                    jsonObjectData = jsonObjSuccess.getJSONObject("data");
                    jsonObjectBidPerson = jsonObjectData.getJSONObject("bidPerson");
                    Log.e("fdvvbsjkdsv", jsonObjSuccess.toString() + "");

                    String labourName = "Bidder Name: " + jsonObjectBidPerson.getString("fullName");
                    SpannableString content = new SpannableString(labourName);
                    content.setSpan(new UnderlineSpan(), 0, labourName.length(), 0);
                    providerName.setText(content);

//                    providerName.setText("Bidder Name: " + );
                    BidderId = jsonObjectBidPerson.getString("id");
                    providerPostedTime.setText(getDateFromUtc(jsonObjectData.getString("updatedAt")));
                    estimationTime.setText("Time Estimation:   " + jsonObjectData.getString("time") + " Days");
                    estimationCost.setText("Cost Estimation:   " + jsonObjectData.getString("cost") + " $");
                    commentQuery.setText(jsonObjectData.getString("query"));


                } catch (JSONException e1) {
                    e1.printStackTrace();
                }


            }
        } catch (Exception e) {
            Log.e("exceptionGet", "" + e);
        }

    }
}
