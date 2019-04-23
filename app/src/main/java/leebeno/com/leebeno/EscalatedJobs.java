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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Adapters.EscalatedJobsAdapter;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;

import static leebeno.com.leebeno.Api.RequestCode.CODE_getEscalatedJob;

public class EscalatedJobs extends AppCompatActivity implements WebCompleteTask {


    static int valueGett = 0;
    @Bind(R.id.recyclerEscalated)
    RecyclerView recyclerEscalated;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    EscalatedJobsAdapter ratingAdapter;
    @Bind(R.id.homeCard)
    CardView homeCard;
    @Bind(R.id.serviceNotMsg)
    TextView serviceNotMsg;
    List<JobGetterSetter> getPostedJobs;

    List<String> listJobItem;
    List<String> listJobItemT;
    List<String> listJobItemD;
    List<String> listJobItemR;
    List<String> listJobItemCR;
    List<String> listJobItemM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escalated_jobs);
        ButterKnife.bind(this);

        getPostedJobs = new ArrayList<>();
        recyclerEscalated.setHasFixedSize(true);
        recyclerEscalated.setNestedScrollingEnabled(false);
        recyclerEscalated.setLayoutManager(new LinearLayoutManager(EscalatedJobs.this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration itemDecor = new DividerItemDecoration(EscalatedJobs.this, DividerItemDecoration.VERTICAL);
        recyclerEscalated.addItemDecoration(itemDecor);


        listJobItem = new ArrayList<>();
        listJobItemD = new ArrayList<>();
        listJobItemT = new ArrayList<>();
        listJobItemR = new ArrayList<>();
        listJobItemCR = new ArrayList<>();
        listJobItemM = new ArrayList<>();

        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k frb bfbebf  jhefb refbjheb");
        listJobItemT.add("$ 50 Deduct as commission");
        listJobItemR.add("Reason for escalation");
        listJobItemCR.add("$ 300");
        listJobItemM.add("Credited On 28th sept,2018");

        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k");
        listJobItemT.add("$ 50 Deduct as commission");
        listJobItemR.add("Reason for escalation");
        listJobItemCR.add("$ 300");
        listJobItemM.add("Credited On 28th sept,2018");

        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu nf bb vfd vdv fhu vbd v dv h b grhe hy ht bhf ebre vhb vrehbfre hevburevfevbub vbv hfbvre rhe vf bvrebfhre hi k");
        listJobItemT.add("$ 50 Deduct as commission");
        listJobItemR.add("Reason for escalation");
        listJobItemCR.add("$ 300");
        listJobItemM.add("Credited On 28th sept,2018");

        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k");
        listJobItemT.add("$ 50 Deduct as commission");
        listJobItemR.add("Reason for escalation");
        listJobItemCR.add("$ 300");
        listJobItemM.add("Credited On 28th sept,2018");

        listJobItem.add("Deny Roy");
        listJobItemD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu nf bb vfd vdv fhu vbd v dv h b grhe hy ht bhf ebre vhb vrehbfre hevburevfevbub vbv hfbvre rhe vf bvrebfhre hi k");
        listJobItemT.add("$ 50 Deduct as commission");
        listJobItemR.add("Reason for escalation");
        listJobItemCR.add("$ 300");
        listJobItemM.add("Credited On 28th sept,2018");

        if (SharedPrefManager.getUserStatus(EscalatedJobs.this, Constrants.UserStatus).compareTo("groupAdmin")==0 || SharedPrefManager.getUserStatus(EscalatedJobs.this, Constrants.UserStatus).compareTo("labour")==0) {
            HashMap hashMap = new HashMap();
            new WebTask(EscalatedJobs.this, WebUrls.BASE_URL + WebUrls.getEscalatedJob, hashMap, EscalatedJobs.this, CODE_getEscalatedJob, 0);

        }else
        {
            HashMap hashMap = new HashMap();
            new WebTask(EscalatedJobs.this, WebUrls.BASE_URL + WebUrls.getCustEscalatedJob, hashMap, EscalatedJobs.this, CODE_getEscalatedJob, 0);

        }




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
            if (taskcode == CODE_getEscalatedJob) {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONArray jsonArrayData = jsonObjSuccess.getJSONArray("data");
                Log.e("kvfjdvbdv", "" + jsonObjSuccess);
                for (int i = 0; i < jsonArrayData.length(); i++) {
                    JobGetterSetter jobGetterSetter = new JobGetterSetter();
                    jobGetterSetter.setJobTitle(jsonArrayData.getJSONObject(i).getString("jobTitle"));
                    jobGetterSetter.setDescription(jsonArrayData.getJSONObject(i).getString("description"));
//                        jobGetterSetter.setStartDate(jsonArrayData.getJSONObject(i).getString("startDate"));
//                        jobGetterSetter.setBidCount(jsonArrayData.getJSONObject(i).getInt("bidCount"));
                    jobGetterSetter.setCreatedJob(jsonArrayData.getJSONObject(i).getString("createdAt"));
                    jobGetterSetter.setId(jsonArrayData.getJSONObject(i).getString("id"));
                    jobGetterSetter.setReasonEscalation(jsonArrayData.getJSONObject(i).getString("reason"));


                    jobGetterSetter.setAmount(jsonArrayData.getJSONObject(i).getInt("finalAmount"));

                    jobGetterSetter.setApplierName(jsonArrayData.getJSONObject(i).getJSONObject("applier").getString("fullName"));

                       /* jobGetterSetter.setPaymentType(jsonArrayData.getJSONObject(i).getString("paymentType"));
                        if (jsonArrayData.getJSONObject(i).getString("paymentType").compareTo(getResources().getString(R.string.range)) == 0) {
                            jobGetterSetter.setMin(jsonArrayData.getJSONObject(i).getInt("min"));
                            jobGetterSetter.setMax(jsonArrayData.getJSONObject(i).getInt("max"));
                        } else {
                            jobGetterSetter.setAmount(jsonArrayData.getJSONObject(i).getInt("amount"));
                        }*/
                    valueGett = 1;
                    getPostedJobs.add(jobGetterSetter);
                    serviceNotMsg.setVisibility(View.GONE);
                    homeCard.setVisibility(View.GONE);

                }
            } else if (valueGett == 0) {
                serviceNotMsg.setVisibility(View.VISIBLE);
                homeCard.setVisibility(View.VISIBLE);
            }


            ratingAdapter = new EscalatedJobsAdapter(getPostedJobs, EscalatedJobs.this);
            recyclerEscalated.setAdapter(ratingAdapter);


        } catch (Exception e) {
            Log.e("exceptionGet", "" + e);
        }
    }
}
