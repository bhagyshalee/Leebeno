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
import leebeno.com.leebeno.Adapters.AssignedAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Model.RejectModel;
import leebeno.com.leebeno.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Assigned extends Fragment implements WebCompleteTask {

    @Bind(R.id.text_empty)
    TextView text_empty;
    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.assignedecycler)
    RecyclerView assignedecycler;
    ArrayList<RejectModel> rejectModelArrayList;
    AssignedAdapter assignedAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_assigned, container, false);
        ButterKnife.bind(this, view);
        rejectModelArrayList = new ArrayList<>();
        assignedecycler.setHasFixedSize(true);
        assignedecycler.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

//        assignedAdapter = new AssignedAdapter(rejectModelArrayList, getActivity());
//        assignedecycler.setAdapter(assignedAdapter);
//        backSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
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
        new WebTask(getActivity(), WebUrls.BASE_URL+WebUrls.archiveAcceptBids,objectNew,Assigned.this, RequestCode.Code_archiveAcceptBids,3);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.Code_archiveAcceptBids){
//            connectionsModelArrayList.clear();
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray dataObj=successObj.optJSONArray("data");

                if (dataObj!=null && dataObj.length()>0){
                    assignedecycler.setVisibility(View.VISIBLE);
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
                    assignedAdapter = new AssignedAdapter(rejectModelArrayList, getActivity());
                    assignedecycler.setAdapter(assignedAdapter);
//                    assignedAdapter.notifyDataSetChanged();
                }else {
                    text_empty.setText(R.string.empty_list);
                    assignedecycler.setVisibility(View.GONE);
                    text_empty.setVisibility(View.VISIBLE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
