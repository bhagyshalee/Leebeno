package leebeno.com.leebeno.AdminPart;

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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import leebeno.com.leebeno.AdminPart.Fragments.ConnectionsFragments.FriendListFragment;
import leebeno.com.leebeno.AdminPart.Adapters.GroupMemberAdapter;
import leebeno.com.leebeno.AdminPart.Fragments.ConnectionsFragments.ReceivedRequestFragment;
import leebeno.com.leebeno.AdminPart.Fragments.ConnectionsFragments.SentRequestFragment;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Model.ConnectionsModel;
import leebeno.com.leebeno.R;

public class ConnectionsAdmin extends AppCompatActivity implements TabLayout.OnTabSelectedListener,
        ViewPager.OnPageChangeListener , WebCompleteTask {


    @Bind(R.id.refresh)
    SwipeRefreshLayout refresh;
    @Bind(R.id.tabLayoutConnections)
    TabLayout tabLayoutConnections;
    @Bind(R.id.pagerBookingConnections)
    ViewPager pagerBookingConnections;
    @Bind(R.id.recyclerGroupMember)
    RecyclerView recyclerGroupMember;

    private ArrayList<ConnectionsModel> connectionsModelArrayList = new ArrayList<>();
    GroupMemberAdapter groupMemberAdapter;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.addConnection)

    ImageView addConnection;
    @Bind(R.id.text_empty)
    TextView text_empty;

    public static ConnectionsAdmin mInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connections);
        ButterKnife.bind(this);

        mInstance = this;

        addConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ConnectionsAdmin.this, NewConnectionRequest.class);
                startActivity(intent);
            }
        });

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
        groupMemberAdapter = new GroupMemberAdapter(connectionsModelArrayList, ConnectionsAdmin.this);
        recyclerGroupMember.setAdapter(groupMemberAdapter);

        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GroupMemberList();
                refresh.setRefreshing(false);
            }
        });
        GroupMemberList();
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
        private String[] tabTitles = new String[]{String.valueOf(getText(R.string.friendList)),
                String.valueOf(getText(R.string.receivedRequest)), String.valueOf(getText(R.string.sentRequest))};

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
                    FriendListFragment tab1 = new FriendListFragment();
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

//    @Override
//    protected void onStart() {
//        super.onStart();
//        GroupMemberList();
//    }

    public static ConnectionsAdmin  getInstance(){
        return mInstance;
    }
    public void GroupMemberList(){
        HashMap objectNew = new HashMap();
        new WebTask(ConnectionsAdmin.this,WebUrls.BASE_URL+ WebUrls.getGroupData,objectNew,ConnectionsAdmin.this, RequestCode.CODE_GetGroupData,3);
    }

    @Override
    public void onComplete(String response, int taskcode) {
        if (taskcode == RequestCode.CODE_GetGroupData){
//            connectionsModelArrayList.clear();
            try {
                JSONObject jsonObject=new JSONObject(response);
                JSONObject successObj = jsonObject.optJSONObject("success");
                JSONObject dataObj=successObj.optJSONObject("data");
                if (dataObj.optJSONArray("groupMember")!=null){
                    JSONArray groupMemberArray = dataObj.optJSONArray("groupMember");
                    connectionsModelArrayList.clear();
                    if (groupMemberArray!=null && groupMemberArray.length()>0){
                        recyclerGroupMember.setVisibility(View.VISIBLE);
                        text_empty.setVisibility(View.GONE);
                        for (int i = 0; i < groupMemberArray.length(); i++) {
                            JSONObject o = groupMemberArray.optJSONObject(i);
                            ConnectionsModel item=new ConnectionsModel();
                            item.setGroup_id(dataObj.optString("id"));
                            item.setImage(o.optString("image"));
                            item.setFullName(o.optString("fullName"));
                            item.setId(o.optString("id"));
                            connectionsModelArrayList.add(item);
                        }
                        groupMemberAdapter.notifyDataSetChanged();
                    }else {
                        recyclerGroupMember.setVisibility(View.GONE);
                        text_empty.setVisibility(View.VISIBLE);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
