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
import leebeno.com.leebeno.AdminPart.Adapters.ReceivedRequestAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.Model.ReceivedRequestModel;
import leebeno.com.leebeno.R;

public class ReceivedRequestFragment extends Fragment implements WebCompleteTask {

    private static ReceivedRequestFragment mInstance;

    @Bind(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recyclerFriendList)
    RecyclerView recyclerFriendList;

    @Bind(R.id.text_empty)
    TextView text_empty;
//    List<String> listGrpMember;

    private ArrayList<ReceivedRequestModel> receivedRequestModelArrayList = new ArrayList<>();
    ReceivedRequestAdapter friendListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_friend_list, container, false);
        ButterKnife.bind(this,view);
        recyclerFriendList.setHasFixedSize(true);
        mInstance=this;
        recyclerFriendList.setNestedScrollingEnabled(true);
        recyclerFriendList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (SharedPrefManager.getUserStatus(getContext(), Constrants.UserStatus).compareTo("labour")==0){
                    getLabReceivedReq();
                } else if (SharedPrefManager.getUserStatus(getContext(),Constrants.UserStatus).compareTo("groupAdmin")==0) {
                    getGroupReceivedReq();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if (SharedPrefManager.getUserStatus(getContext(),Constrants.UserStatus).compareTo("labour")==0){
            getLabReceivedReq();
        } else if (SharedPrefManager.getUserStatus(getContext(),Constrants.UserStatus).compareTo("groupAdmin")==0) {
            getGroupReceivedReq();
        }
        friendListAdapter = new ReceivedRequestAdapter(receivedRequestModelArrayList, getActivity());
        recyclerFriendList.setAdapter(friendListAdapter);

        return view;
    }

    public static ReceivedRequestFragment getInstance(){
        return mInstance;
    }
    public void getLabReceivedReq(){
        HashMap objectNew = new HashMap();
        new WebTask(getActivity(), WebUrls.BASE_URL+WebUrls.getLabReceivedReq,objectNew,ReceivedRequestFragment.this, RequestCode.CODE_GetAllrequest,3);
    }
    public void getGroupReceivedReq(){
        HashMap objectNew = new HashMap();
        new WebTask(getActivity(),WebUrls.BASE_URL+WebUrls.getGroupReceivedReq,objectNew,ReceivedRequestFragment.this,RequestCode.CODE_GetAllrequest,3);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_GetAllrequest){
            receivedRequestModelArrayList.clear();
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
                        ReceivedRequestModel item=new ReceivedRequestModel();

                        item.setReq_id(o.optString("id"));

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
                        receivedRequestModelArrayList.add(item);
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
