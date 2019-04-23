package leebeno.com.leebeno.AdminPart;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.AdminPart.Adapters.NewConnectionAdapter;
import leebeno.com.leebeno.Api.ApiConfig;
import leebeno.com.leebeno.Api.AppConfig;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.LabourPart.HomeLabourActivity;
import leebeno.com.leebeno.Model.ConnectionsModel;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Services.LocationTrack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static leebeno.com.leebeno.Services.Utility.close;
import static leebeno.com.leebeno.Services.Utility.showProgress;

public class NewConnectionRequest extends AppCompatActivity implements WebCompleteTask {

    @Bind(R.id.recyclerSuggestConn)
    RecyclerView recyclerSuggestConn;
    @Bind(R.id.sortedAlpha)
    ImageView sortedAlpha;
    @Bind(R.id.textNearBy)
    TextView textNearBy;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    LocationTrack gps;

//    List<String> listGrpMember;
SwipeRefreshLayout setOnRefreshListener;
    private ArrayList<ConnectionsModel> connectionsModelArrayList = new ArrayList<>();
    NewConnectionAdapter newConnectionAdapter;
    private static NewConnectionRequest mInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_connection_request);
        ButterKnife.bind(this);
        recyclerSuggestConn.setHasFixedSize(true);


//        recyclerSuggestConn.setNestedScrollingEnabled(true);
        mInstance = this;
        recyclerSuggestConn.setLayoutManager(new LinearLayoutManager(NewConnectionRequest.this, LinearLayoutManager.VERTICAL, false));

        sortedAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerSuggestConn.setLayoutManager(new LinearLayoutManager(NewConnectionRequest.this, LinearLayoutManager.VERTICAL, true));
            }
        });
        if (SharedPrefManager.getUserStatus(NewConnectionRequest.this, Constrants.UserStatus).compareTo("labour")==0){
            GroupMemberList(0.0,0.0);
        } else if (SharedPrefManager.getUserStatus(NewConnectionRequest.this,Constrants.UserStatus).compareTo("groupAdmin")==0) {
            LabourMemberList(0.0,0.0);
        }

        setOnRefreshListener = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        setOnRefreshListener.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                setOnRefreshListener.setRefreshing(true);
//                dateSearch.setText(messages[new java.util.Random().nextInt(messages.length)]);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setOnRefreshListener.setRefreshing(false);
                        if (SharedPrefManager.getUserStatus(NewConnectionRequest.this, Constrants.UserStatus).compareTo("labour")==0){
                            GroupMemberList(0.0,0.0);
                        } else if (SharedPrefManager.getUserStatus(NewConnectionRequest.this,Constrants.UserStatus).compareTo("groupAdmin")==0) {
                            LabourMemberList(0.0,0.0);
                        }


                    }
                }, 2000);
            }
        });

        for (int i = 0; i < recyclerSuggestConn.getChildCount(); i++) {
            if (recyclerSuggestConn.getChildAt(i) == null)
                continue;
            // Continue base behavior
        }
        newConnectionAdapter = new NewConnectionAdapter(connectionsModelArrayList, NewConnectionRequest.this,"send request");
        recyclerSuggestConn.setAdapter(newConnectionAdapter);

        textNearBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(NewConnectionRequest.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(NewConnectionRequest.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(NewConnectionRequest.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                } else {
//                    Toast.makeText(context,"You need have granted permission",Toast.LENGTH_SHORT).show();
                    gps = new LocationTrack(NewConnectionRequest.this, NewConnectionRequest.this);

                    // Check if GPS enabled
                    if (gps.canGetLocation()) {

                        double latitude = gps.latitude;
                        double longitude = gps.longitude;
                        if(latitude!=0.0 && longitude!=0.0)
                        {

                            if (SharedPrefManager.getUserStatus(NewConnectionRequest.this,Constrants.UserStatus).compareTo("labour")==0){
                                NewConnectionRequest.getInstance().GroupMemberList(latitude,longitude);
                            } else if (SharedPrefManager.getUserStatus(NewConnectionRequest.this,Constrants.UserStatus).compareTo("groupAdmin")==0) {
                                NewConnectionRequest.getInstance().LabourMemberList(latitude,longitude);
                            }

                        }

//                        Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                    } else {
                        // Can't get location.
                        // GPS or network is not enabled.
                        // Ask user to enable GPS/network in settings.
                        gps.showSettingsAlert();
                    }
                }
            }
        });

        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static NewConnectionRequest getInstance(){
        return mInstance;
    }

    public void LabourMemberList(double lat,double lng){
        JSONObject locationobj = null;
        if (lat != 0.0 && lng != 0.0) {

            try {

                locationobj = new JSONObject();
                locationobj.put("lat", lat);
                locationobj.put("lng", lng);
            } catch (Exception e) {
                Log.e("rdgerff", "" + e);
            }
        }

        HashMap objectNew = new HashMap();
        new WebTask(NewConnectionRequest.this, WebUrls.BASE_URL+WebUrls.getAllLabours+"?"+"loc=" + locationobj ,objectNew,NewConnectionRequest.this, RequestCode.CODE_GetAllGroups,3);
    }
    public void GroupMemberList(double lat,double lng){
        JSONObject locationobj = null;
        if (lat != 0.0 && lng != 0.0) {

            try {

                locationobj = new JSONObject();
                locationobj.put("lat", lat);
                locationobj.put("lng", lng);
            } catch (Exception e) {
                Log.e("rdgerff", "" + e);
            }
        }
        HashMap objectNew = new HashMap();
        new WebTask(NewConnectionRequest.this,WebUrls.BASE_URL+WebUrls.getAllGroups+"?"+"loc=" + locationobj,objectNew,NewConnectionRequest.this,RequestCode.CODE_GetAllGroups,3);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_GetAllGroups){
            connectionsModelArrayList.clear();
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray jsonArray=successObj.optJSONArray("data");
                Log.e("getAllRequestDetail",""+successObj);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject o = jsonArray.optJSONObject(i);
                    ConnectionsModel item=new ConnectionsModel();
                    item.setImage(o.optString("image"));
                    item.setFullName(o.optString("fullName"));
                    item.setId(o.optString("id"));

                    if (o.optJSONObject("groupRequest")!=null){
                        item.setRequest_id(o.optJSONObject("groupRequest").optString("id"));
                        item.setRequested(o.optJSONObject("groupRequest").optString("reqStatus"));
                        item.setRequestby(o.optJSONObject("groupRequest").optString("reqMadeBy"));
                    }

                    if (o.optJSONObject("labourRequest")!=null){
                        item.setRequest_id(o.optJSONObject("labourRequest").optString("id"));
                        item.setRequested(o.optJSONObject("labourRequest").optString("reqStatus"));
                        item.setRequestby(o.optJSONObject("labourRequest").optString("reqMadeBy"));
                    }
                    connectionsModelArrayList.add(item);
                }
                newConnectionAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
