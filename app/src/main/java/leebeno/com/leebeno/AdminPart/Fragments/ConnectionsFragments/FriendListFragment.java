package leebeno.com.leebeno.AdminPart.Fragments.ConnectionsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import leebeno.com.leebeno.AdminPart.Adapters.FriendListAdapter;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Model.FriendListModel;
import leebeno.com.leebeno.R;

public class FriendListFragment extends Fragment implements WebCompleteTask {

    @Bind(R.id.refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.text_empty)
    TextView text_empty;
    @Bind(R.id.recyclerFriendList)
    RecyclerView recyclerFriendList;
//    List<String> listGrpMember;
    FriendListAdapter friendListAdapter;
    ArrayList<FriendListModel> friendListModelArrayList;

    private static FriendListFragment mInstance=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_friend_list, container, false);
        ButterKnife.bind(this,v);

        mInstance = this;
        friendListModelArrayList = new ArrayList<>();
        recyclerFriendList.setHasFixedSize(true);
        recyclerFriendList.setNestedScrollingEnabled(true);
        recyclerFriendList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFriendList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        getFriendList();

        friendListAdapter = new FriendListAdapter(friendListModelArrayList, getActivity(),"");
        recyclerFriendList.setAdapter(friendListAdapter);
        return v;
    }
    public static FriendListFragment getInstance(){
        if (mInstance == null)
            mInstance = new FriendListFragment();
        return mInstance;
    }

    public void getFriendList(){
        HashMap objectNew = new HashMap();
        new WebTask(getActivity(),WebUrls.BASE_URL+WebUrls.getConfirmedRequests,objectNew,FriendListFragment.this,RequestCode.CODE_GetConfirmedRequests,3);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_GetConfirmedRequests){
            Log.d("GetConfirmedRequests",response);
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray jsonArray=successObj.optJSONArray("data");

                if (jsonArray!=null&&jsonArray.length()>0){
                    recyclerFriendList.setVisibility(View.VISIBLE);
                    text_empty.setVisibility(View.GONE);
                    friendListModelArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject o = jsonArray.optJSONObject(i);
                        FriendListModel item=new FriendListModel();

                        item.setReq_id(o.optString("id"));
                        item.setAddedInGroup(o.optBoolean("addedInGroup"));
//                        if (o.optJSONObject("groupAdmin")!=null){
//                            item.setImage(o.optJSONObject("groupAdmin").optString("image"));
//                            item.setName(o.optJSONObject("groupAdmin").optString("fullName"));
//                            item.setId(o.optJSONObject("groupAdmin").optString("id"));
//                        }

                        if (o.optJSONObject("labour")!=null){
                            item.setImage(o.optJSONObject("labour").optString("image"));
                            item.setName(o.optJSONObject("labour").optString("fullName"));
                            item.setId(o.optJSONObject("labour").optString("id"));
                        }
                        friendListModelArrayList.add(item);
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
