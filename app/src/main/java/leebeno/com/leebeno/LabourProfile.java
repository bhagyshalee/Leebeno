package leebeno.com.leebeno;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Adapters.ProfileReviewAdapter;
import leebeno.com.leebeno.Adapters.ShowSkillAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;

public class LabourProfile extends AppCompatActivity implements WebCompleteTask {

    @Bind(R.id.skillRecycler)
    RecyclerView skillRecycler;
    @Bind(R.id.reviewRecycler)
    RecyclerView reviewRecycler;

    List<String> skillList;
    List<String> reviewT;
    List<String> reviewD;
    ShowSkillAdapter addSkillAdapter;
    ProfileReviewAdapter profileReviewAdapter;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.btnMessage)
    Button btnMessage;
    @Bind(R.id.profilePicLabour)
    ImageView profilePicLabour;
    @Bind(R.id.labourName)
    TextView labourName;
    @Bind(R.id.labourRating)
    RatingBar labourRating;
    @Bind(R.id.addressValue)
    TextView addressValue;
    @Bind(R.id.payPerHour)
    TextView payPerHour;
    @Bind(R.id.descriptionValue)
    TextView descriptionValue;
    String bidPersonId;
    @Bind(R.id.skillText)
    TextView skillText;
    @Bind(R.id.reviewTitle)
    TextView reviewTitle;
    @Bind(R.id.cardReview)
    CardView cardReview;

    String id ;
    List<JobGetterSetter> reviewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_profile);
        ButterKnife.bind(this);
        String bidRequest = getIntent().getStringExtra("jobStatus");
        bidPersonId = getIntent().getStringExtra("bidPersonId");


        reviewList = new ArrayList<>();

        skillList = new ArrayList<>();
        reviewT = new ArrayList<>();
        reviewD = new ArrayList<>();
        skillList.add("Carpenter");
        skillList.add("Carpenter");
        skillList.add("Carpenter");
        skillList.add("Carpenter");

        skillRecycler.setHasFixedSize(true);
        skillRecycler.setNestedScrollingEnabled(false);
        skillRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        reviewRecycler.setHasFixedSize(true);
        reviewRecycler.setNestedScrollingEnabled(false);
        reviewRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


       /* reviewT.add("Deny Roy");
        reviewD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k frb bfbebf  jhefb refbjheb");
        reviewT.add("Deny Roy");
        reviewD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k frb bfbebf  jhefb refbjheb");
        reviewT.add("Deny Roy");
        reviewD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k frb bfbebf  jhefb refbjheb");
        reviewT.add("Deny Roy");
        reviewD.add("Hey i am waiting for your job gfgd fdg gg fd theht hu hy ht hi k frb bfbebf  jhefb refbjheb");
*/

        id = getIntent().getStringExtra("id");
        if (bidRequest!=null){
            if (bidRequest.equals("completed")) {
                btnMessage.setVisibility(View.GONE);
            } else {
                btnMessage.setVisibility(View.VISIBLE);
//                bidPersonId = id;
            }
        }else {
//            bidPersonId = id;
        }

      /*  profileReviewAdapter = new ProfileReviewAdapter(reviewT, LabourProfile.this, reviewD);
        reviewRecycler.setAdapter(profileReviewAdapter);
*/
        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if (SharedPrefManager.getUserStatus(LabourProfile.this, Constrants.UserStatus).compareTo("labour")==0){
            skillText.setVisibility(View.GONE);
            skillRecycler.setVisibility(View.GONE);
        } else if (SharedPrefManager.getUserStatus(LabourProfile.this,Constrants.UserStatus).compareTo("groupAdmin")==0) {
            skillText.setVisibility(View.VISIBLE);
            skillRecycler.setVisibility(View.VISIBLE);
        }
//        Log.e("avdddddddddsvdfb", bidPersonId);
        HashMap hashMap = new HashMap();
        new WebTask(LabourProfile.this, WebUrls.BASE_URL + WebUrls.getProfileById + "peopleId=" + bidPersonId,
                hashMap, LabourProfile.this, RequestCode.CODE_getProfileById, 0);




    }

    @Override
    public void onComplete(String response, int taskcode) {
        try {
            if (taskcode == RequestCode.CODE_getProfileById) {
                JSONObject jsonObject = new JSONObject(response);
                JSONObject jsonObjSuccess = jsonObject.getJSONObject("success");
                JSONObject jsonObjectData = jsonObjSuccess.getJSONObject("data");
                JSONArray jsonArraySkill = jsonObjectData.getJSONArray("skill");
                labourName.setText(jsonObjectData.optString("fullName"));
                addressValue.setText(jsonObjectData.getJSONObject("address").optString("street") + " " + jsonObjectData.getJSONObject("address").optString("value"));
                payPerHour.setText("Pay per hour " + jsonObjectData.optString("pph"));
                descriptionValue.setText(jsonObjectData.optString("description"));


                Picasso.get()
                        .load(WebUrls.BASE_URL + jsonObjectData.optString("image"))
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .into(profilePicLabour);

                Log.e("neerbbgbt", "" + jsonArraySkill.length());

                List<String> listSkill = new ArrayList<>();
                if (jsonArraySkill.length()!=0)
                {
                    for (int i = 0; i < jsonArraySkill.length(); i++) {
                        //skillGet = TextUtils.join(", ", new String[]{jsonArraySkill.getJSONObject(i).optString("name")});
                        listSkill.add(jsonArraySkill.getJSONObject(i).optString("name"));
                    }

                }else {
                    skillText.setVisibility(View.GONE);
                }


                if (jsonObjectData.optString("realm").compareTo("labour") == 0) {
                    skillRecycler.setVisibility(View.VISIBLE);
                    skillText.setVisibility(View.VISIBLE);
                    payPerHour.setVisibility(View.VISIBLE);
                } else if (jsonObjectData.optString("realm").compareTo("groupAdmin") == 0) {
//                    skillRecycler.setVisibility(View.GONE);
//                    skillText.setVisibility(View.GONE);
                    payPerHour.setVisibility(View.GONE);
                }

                addSkillAdapter = new ShowSkillAdapter(listSkill, LabourProfile.this, skillRecycler);
                skillRecycler.setAdapter(addSkillAdapter);

                JobGetterSetter jobGetterSetter = new JobGetterSetter();

                if (jsonObjectData.optJSONArray("ratings").length() != 0 && jsonObjectData.optJSONArray("ratings")!= null) {

                    for (int i = 0; i < jsonObjectData.optJSONArray("ratings").length(); i++) {
                        if (jsonObjectData.optJSONArray("ratings").getJSONObject(i).getString("forUser").compareTo("applier") == 0) {

                            Log.e("reviewDetail", jsonObjectData.optJSONArray("ratings").getJSONObject(i).getString("comment")
                                    + "  " + jsonObjectData.optJSONArray("ratings").getJSONObject(i).getJSONObject("provider").getString("image")
                                    + "  " + jsonObjectData.optJSONArray("ratings").getJSONObject(i).getJSONObject("provider").getString("fullName"));
                            jobGetterSetter.setPofilePic(jsonObjectData.optJSONArray("ratings").getJSONObject(i).getJSONObject("provider").getString("image"));
                            jobGetterSetter.setApplierName(jsonObjectData.optJSONArray("ratings").getJSONObject(i).getJSONObject("provider").getString("fullName"));
                            jobGetterSetter.setJobRating(jsonObjectData.optJSONArray("ratings").getJSONObject(i).getString("userRating"));
                            jobGetterSetter.setCreatedJob(jsonObjectData.optJSONArray("ratings").getJSONObject(i).getString("createdAt"));
                            jobGetterSetter.setReviewComment(jsonObjectData.optJSONArray("ratings").getJSONObject(i).getString("comment"));
                            labourRating.setRating(jsonObjectData.optJSONObject("rating").optInt("avgRating"));
                            reviewList.add(jobGetterSetter);
                        }
                    }

                } else {
                    reviewTitle.setVisibility(View.GONE);
                    cardReview.setVisibility(View.GONE);
                }

                profileReviewAdapter = new ProfileReviewAdapter(reviewList, LabourProfile.this);
                reviewRecycler.setAdapter(profileReviewAdapter);



              /*  Log.e("dATA", "" + jsonObjSuccess.toString());
                Date d = null;
                try {
                    d = inputFormat.parse("" + jsonObjectData.optString("createdAt"));
                } catch (ParseException ex) {
                    Log.e("neerbbgbt", "" + ex);
                }

                providerPostedTime.setText("Posted " + getDateDifference(d));*/

            }
        } catch (Exception e) {
            Log.e("dfbmdbfdb", "" + e);
        }
    }
}
