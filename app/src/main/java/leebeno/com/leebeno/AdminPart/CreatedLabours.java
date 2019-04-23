package leebeno.com.leebeno.AdminPart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.AdminPart.Adapters.CreatedLabourAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.Model.CreateByAdminListModel;
import leebeno.com.leebeno.R;

public class CreatedLabours extends AppCompatActivity implements WebCompleteTask {

    @Bind(R.id.text_empty)
    TextView text_empty;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.btnCreateNew)
    Button btnCreateNew;
    @Bind(R.id.createdLaboursRecycler)
    RecyclerView createdLaboursRecycler;
    ArrayList<CreateByAdminListModel> createByAdminListModelArrayList;
    CreatedLabourAdapter createdLabourAdapter;
   /* @Bind(R.id.titleLabourCreation)
    TextView titleLabourCreation;*/

    private static CreatedLabours mInstance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_labours);
        ButterKnife.bind(this);
        createByAdminListModelArrayList = new ArrayList<>();
//        createdDate = new ArrayList<>();

        mInstance = this;

        createdLaboursRecycler.setHasFixedSize(true);
        createdLaboursRecycler.setNestedScrollingEnabled(false);
        createdLaboursRecycler.setLayoutManager(new LinearLayoutManager(CreatedLabours.this, LinearLayoutManager.VERTICAL, false));

        createdLabourAdapter = new CreatedLabourAdapter(createByAdminListModelArrayList, CreatedLabours.this);
        createdLaboursRecycler.setAdapter(createdLabourAdapter);


        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreatedLabours.this, CreateNewLabour.class);
                intent.putExtra("labourMode","create");
                startActivity(intent);
            }
        });

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LabourCreateMemberList();
                refresh.setRefreshing(false);
            }
        });
        LabourCreateMemberList();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LabourCreateMemberList();
    }

    public static CreatedLabours getInstance(){
        if (mInstance == null)
            mInstance = new CreatedLabours();
        return mInstance;
    }

    public void LabourCreateMemberList(){
        HashMap objectNew = new HashMap();
        new WebTask(CreatedLabours.this,WebUrls.BASE_URL+ WebUrls.getMyLabourList+"groupAdminId="+ SharedPrefManager.getUserID(CreatedLabours.this, Constrants.UserID),objectNew,CreatedLabours.this,RequestCode.CODE_GetMyLabourList,3);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_GetMyLabourList){
//            connectionsModelArrayList.clear();
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray dataObj=successObj.optJSONArray("data");
                Log.e("getCreatedLabourDetail",""+successObj);

                    if (dataObj!=null && dataObj.length()>0){
                        createdLaboursRecycler.setVisibility(View.VISIBLE);
                        text_empty.setVisibility(View.GONE);
                        createByAdminListModelArrayList.clear();
                        for (int i = 0; i < dataObj.length(); i++) {
                            JSONObject o = dataObj.optJSONObject(i);
                            CreateByAdminListModel item=new CreateByAdminListModel();
                            item.setAddedInGroup(dataObj.optJSONObject(i).optBoolean("addedInGroup"));
                            item.setImage(dataObj.optJSONObject(i).optString("image"));
                            item.setName(dataObj.optJSONObject(i).optString("fullName"));
                            item.setId(dataObj.optJSONObject(i).optString("id"));
                            item.setDate(dataObj.optJSONObject(i).optString("createdAt"));
                            createByAdminListModelArrayList.add(item);
                        }
                        createdLabourAdapter.notifyDataSetChanged();
                    }else {
                        text_empty.setText(R.string.not_created_labour);
                        createdLaboursRecycler.setVisibility(View.GONE);
                        text_empty.setVisibility(View.VISIBLE);
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
