package leebeno.com.leebeno.AdminPart.Fragments.ConnectionsFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.AdminPart.Adapters.SentRequestAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.Model.SentRequestModel;
import leebeno.com.leebeno.R;

public class SentRequestFragment extends Fragment implements WebCompleteTask {

    @Bind(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recyclerFriendList)
    RecyclerView recyclerFriendList;

    @Bind(R.id.text_empty)
    TextView text_empty;
//    List<String> listGrpMember;
    SentRequestAdapter friendListAdapter;
    private ArrayList<SentRequestModel> sentRequestModelArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_friend_list, container, false);

        ButterKnife.bind(this,v);
        recyclerFriendList.setHasFixedSize(true);
        recyclerFriendList.setNestedScrollingEnabled(true);
        recyclerFriendList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (SharedPrefManager.getUserStatus(getContext(), Constrants.UserStatus).compareTo("labour")==0){
                    getLabSentReceivedReq();
                } else if (SharedPrefManager.getUserStatus(getContext(),Constrants.UserStatus).compareTo("groupAdmin")==0) {
                    getGroupSentReceivedReq();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        if (SharedPrefManager.getUserStatus(getContext(),Constrants.UserStatus).compareTo("labour")==0){
            getLabSentReceivedReq();
        } else if (SharedPrefManager.getUserStatus(getContext(),Constrants.UserStatus).compareTo("groupAdmin")==0) {
            getGroupSentReceivedReq();
        }
        friendListAdapter = new SentRequestAdapter(sentRequestModelArrayList, getActivity());
        recyclerFriendList.setAdapter(friendListAdapter);

        return v;
    }

    @Override
    public void onStart() {
        if (SharedPrefManager.getUserStatus(getContext(),Constrants.UserStatus).compareTo("labour")==0){
            getLabSentReceivedReq();
        } else if (SharedPrefManager.getUserStatus(getContext(),Constrants.UserStatus).compareTo("groupAdmin")==0) {
            getGroupSentReceivedReq();
        }
        super.onStart();
    }

    private void getLabSentReceivedReq(){
        HashMap objectNew = new HashMap();
        new WebTask(getActivity(), WebUrls.BASE_URL+WebUrls.getLabSentReq,objectNew,SentRequestFragment.this, RequestCode.CODE_GetAllSentrequest,3);
    }
    private void getGroupSentReceivedReq(){
        HashMap objectNew = new HashMap();
        new WebTask(getActivity(),WebUrls.BASE_URL+WebUrls.getGroupSentReq,objectNew,SentRequestFragment.this,RequestCode.CODE_GetAllSentrequest,3);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_GetAllSentrequest){
            sentRequestModelArrayList.clear();
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray jsonArray=successObj.optJSONArray("data");
//                if (trending_product.size()<=jsonArray.length()){
//                    trending_product.add(null);
//                    mAdapter.notifyItemInserted(trending_product.size()-1);
//                    progressBar.setVisibility(View.GONE);
//                    trending_product.remove(trending_product.size()-1);
//                    mAdapter.notifyItemRemoved(trending_product.size());
//                    int index = trending_product.size();
//                    int end = index + 16;

                if (jsonArray.length()>0){
                    recyclerFriendList.setVisibility(View.VISIBLE);
                    text_empty.setVisibility(View.GONE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject o = jsonArray.optJSONObject(i);
                        SentRequestModel item=new SentRequestModel();
                        if (o.optJSONObject("groupAdmin")!=null){
                            item.setImage(o.optJSONObject("groupAdmin").optString("image"));
                            item.setName(o.optJSONObject("groupAdmin").optString("fullName"));
                            item.setId(o.optJSONObject("groupAdmin").optString("id"));
                        }

                        if (o.optJSONObject("labour")!=null){
                            item.setImage(o.optJSONObject("labour").optString("image"));
                            item.setName(o.optJSONObject("labour").optString("fullName"));
                            item.setId(o.optJSONObject("labour").optString("id"));
                        }
                        sentRequestModelArrayList.add(item);
                    }
                    friendListAdapter.notifyDataSetChanged();
                }else {
                    recyclerFriendList.setVisibility(View.GONE);
                    text_empty.setVisibility(View.VISIBLE);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onDestroyView() {
        recyclerFriendList.setAdapter(null);
        super.onDestroyView();
    }
}
