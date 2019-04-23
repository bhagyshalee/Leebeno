package leebeno.com.leebeno.Fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Adapters.RejectedAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.Model.RejectModel;
import leebeno.com.leebeno.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Rejeted extends Fragment implements WebCompleteTask {

    @Bind(R.id.text_empty)
    TextView text_empty;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.rejectedecycler)
    RecyclerView rejectedecycler;
    ArrayList<RejectModel> rejectModelArrayList;
    RejectedAdapter rejectedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_rejeted, container, false);

        ButterKnife.bind(this,view);
        rejectModelArrayList = new ArrayList<>();
        rejectedecycler.setHasFixedSize(true);
        rejectedecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        rejectedAdapter = new RejectedAdapter(rejectModelArrayList, getActivity());
        rejectedecycler.setAdapter(rejectedAdapter);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RejectedList();
                refresh.setRefreshing(false);
            }
        });
        RejectedList();
        return  view;
    }
    public void RejectedList(){
        HashMap objectNew = new HashMap();
        String groupAdminId = SharedPrefManager.getUserID(getActivity(), Constrants.UserID);
        Log.d("user_id",groupAdminId);
        new WebTask(getActivity(), WebUrls.BASE_URL+WebUrls.archiveRejectBids,objectNew,Rejeted.this,RequestCode.Code_archiveRejectBids,3);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.Code_archiveRejectBids){
//            connectionsModelArrayList.clear();
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray dataObj=successObj.optJSONArray("data");

                if (dataObj!=null && dataObj.length()>0){
                    rejectedecycler.setVisibility(View.VISIBLE);
                    text_empty.setVisibility(View.GONE);
                    rejectModelArrayList.clear();
                    for (int i = 0; i < dataObj.length(); i++) {
                        JSONObject o = dataObj.optJSONObject(i);
                        RejectModel item=new RejectModel();
                        item.setId(o.optString("id"));
                        item.setJobid(o.optString("jobId"));
                        item.setPeopleid(o.optString("peopleId"));
                        item.setComment(o.optString("query"));
                        item.setJobtile(o.optJSONObject("job").optString("jobTitle"));
                        item.setUsd(o.optString("cost"));
                        item.setRejected_date(o.optString("updatedAt"));
                        item.setPlaceddate(o.optString("createdAt"));
                        rejectModelArrayList.add(item);
                    }
                    rejectedAdapter.notifyDataSetChanged();
                }else {
                    text_empty.setText(R.string.empty_list);
                    rejectedecycler.setVisibility(View.GONE);
                    text_empty.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
