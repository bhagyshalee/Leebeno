package leebeno.com.leebeno.AdminPart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Adapters.ShowSkillAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.LabourProfile;
import leebeno.com.leebeno.R;

public class CreatedLabourDetail extends AppCompatActivity implements WebCompleteTask {

    @Bind(R.id.profilePicLabour)
    ImageView profilePicLabour;
    @Bind(R.id.labourName)
    TextView labourName;
    @Bind(R.id.labourEmail)
    TextView labourEmail;
    @Bind(R.id.btnMessage)
    Button btnMessage;
    @Bind(R.id.skillRecycler)
    RecyclerView skillRecycler;
    @Bind(R.id.descriptionValue)
    TextView descriptionValue;
    @Bind(R.id.skillText)
    TextView skillText;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;

    String bidPersonId;
    ShowSkillAdapter addSkillAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_labour_detail);
        ButterKnife.bind(CreatedLabourDetail.this);
        String bidRequest = getIntent().getStringExtra("jobStatus");
        bidPersonId = getIntent().getStringExtra("bidPersonId");

        skillRecycler.setHasFixedSize(true);
        skillRecycler.setNestedScrollingEnabled(true);
        skillRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        HashMap hashMap = new HashMap();
        new WebTask(CreatedLabourDetail.this, WebUrls.BASE_URL + WebUrls.getProfileById + "peopleId=" + bidPersonId,
                hashMap, CreatedLabourDetail.this, RequestCode.CODE_getProfileById, 0);
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
                labourEmail.setText(jsonObjectData.optString("email"));
               /* addressValue.setText(jsonObjectData.getJSONObject("address").optString("street") + " " + jsonObjectData.getJSONObject("address").optString("value"));
                payPerHour.setText("Pay per hour " + jsonObjectData.optString("pph"));
              */
                descriptionValue.setText(jsonObjectData.optString("description"));


                Picasso.get()
                        .load(WebUrls.BASE_URL + jsonObjectData.optString("image"))
                        .placeholder(R.drawable.user)
                        .error(R.drawable.user)
                        .into(profilePicLabour);

                Log.e("neerbbgbt", "" + jsonArraySkill.length());

                List<String> listSkill = new ArrayList<>();
                for (int i = 0; i < jsonArraySkill.length(); i++) {
                    //skillGet = TextUtils.join(", ", new String[]{jsonArraySkill.getJSONObject(i).optString("name")});

                    listSkill.add(jsonArraySkill.getJSONObject(i).optString("name"));
                }

                if (jsonObjectData.optString("realm").compareTo("labour") == 0) {
                    skillRecycler.setVisibility(View.VISIBLE);
                    skillText.setVisibility(View.VISIBLE);
                } else if (jsonObjectData.optString("realm").compareTo("groupAdmin") == 0) {
                    skillRecycler.setVisibility(View.GONE);
                    skillText.setVisibility(View.GONE);
                }

                addSkillAdapter = new ShowSkillAdapter(listSkill, CreatedLabourDetail.this, skillRecycler);
                skillRecycler.setAdapter(addSkillAdapter);



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
