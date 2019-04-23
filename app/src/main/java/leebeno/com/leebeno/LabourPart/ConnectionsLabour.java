package leebeno.com.leebeno.LabourPart;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import leebeno.com.leebeno.Adapters.GroupListAdapter;
import leebeno.com.leebeno.AdminPart.Fragments.ConnectionsFragments.ReceivedRequestFragment;
import leebeno.com.leebeno.AdminPart.Fragments.ConnectionsFragments.SentRequestFragment;
import leebeno.com.leebeno.AdminPart.NewConnectionRequest;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.LabourPart.Fragments.LabourFriendListFragment;
import leebeno.com.leebeno.Model.ConnectionsModel;
import leebeno.com.leebeno.R;

public class ConnectionsLabour extends AppCompatActivity implements TabLayout.OnTabSelectedListener, ViewPager.OnPageChangeListener,WebCompleteTask {

    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.tabLayoutConnections)
    TabLayout tabLayoutConnections;
    @Bind(R.id.pagerBookingConnections)
    ViewPager pagerBookingConnections;
    @Bind(R.id.recyclerGroupMember)
    RecyclerView recyclerGroupMember;
//    List<String> listGrpMember;
    GroupListAdapter groupMemberAdapter;

    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.addConnection)
    ImageView addConnection;
    @Bind(R.id.text_empty)
    TextView text_empty;
    private ArrayList<ConnectionsModel> connectionsModelArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);
        ButterKnife.bind(this);

        addConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ConnectionsLabour.this, NewConnectionRequest.class);
                startActivity(intent);
            }
        });
//        listGrpMember = new ArrayList<>();
        recyclerGroupMember.setHasFixedSize(true);
        recyclerGroupMember.setNestedScrollingEnabled(true);
        recyclerGroupMember.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        tabLayoutConnections.addTab(tabLayoutConnections.newTab());
        tabLayoutConnections.addTab(tabLayoutConnections.newTab());
        tabLayoutConnections.addTab(tabLayoutConnections.newTab());
        tabLayoutConnections.setTabGravity(TabLayout.GRAVITY_FILL);


        PagerAdpterr adapter = new PagerAdpterr(getSupportFragmentManager(), tabLayoutConnections.getTabCount());
        pagerBookingConnections.setAdapter(adapter);
        pagerBookingConnections.setOffscreenPageLimit(2);
        tabLayoutConnections.setOnTabSelectedListener(this);
        tabLayoutConnections.setupWithViewPager(pagerBookingConnections);

        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        groupMemberAdapter = new GroupListAdapter(connectionsModelArrayList, ConnectionsLabour.this);
        recyclerGroupMember.setAdapter(groupMemberAdapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LabourGroupList();
                refresh.setRefreshing(false);
            }
        });
        LabourGroupList();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public class PagerAdpterr extends FragmentStatePagerAdapter

    {
        int tabCount;
        private String[] tabTitles = new String[]{String.valueOf(getText(R.string.friendList)),String.valueOf(getText(R.string.receivedRequest)), String.valueOf(getText(R.string.sentRequest))};

        public PagerAdpterr(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount = tabCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            //Returning the current tabs
            switch (position) {
                case 0:
                    LabourFriendListFragment tab1 = new LabourFriendListFragment();
                    return tab1;
                case 1:
                    ReceivedRequestFragment tab2 = new ReceivedRequestFragment();
                    return tab2;
                case 2:
                    SentRequestFragment tab3 = new SentRequestFragment();
                    return tab3;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return tabCount;

        }
    }

    public void LabourGroupList(){
        HashMap objectNew = new HashMap();
        new WebTask(ConnectionsLabour.this, WebUrls.BASE_URL+WebUrls.getGroupList,objectNew,ConnectionsLabour.this, RequestCode.CODE_GetGroupList,3);
    }
    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode==RequestCode.CODE_GetGroupList){
            connectionsModelArrayList.clear();
            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONArray dataArray = successObj.optJSONArray("data");
                connectionsModelArrayList.clear();
                if (dataArray!=null && dataArray.length()>0) {
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject object = dataArray.getJSONObject(i);
                        ConnectionsModel item = new ConnectionsModel();
                        item.setGroup_id(object.optString("id"));
                        item.setFullName(object.optJSONObject("groupAdmin").optString("fullName"));
                        item.setId(object.optJSONObject("groupAdmin").optString("id"));
                        item.setImage(object.optJSONObject("groupAdmin").optString(
                                "image"));
                        connectionsModelArrayList.add(item);
                    }
                    groupMemberAdapter.notifyDataSetChanged();
                }else {
                    recyclerGroupMember.setVisibility(View.GONE);
                    text_empty.setVisibility(View.VISIBLE);
                }

            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
