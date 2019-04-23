package leebeno.com.leebeno.AdminPart.Fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.AdminPart.CreateGroupActivity;
import leebeno.com.leebeno.CustomerPart.Adapter.ChatListAdapter;
import leebeno.com.leebeno.LabourPart.Fragments.CustomerMessageFragment;
import leebeno.com.leebeno.LabourPart.Fragments.GroupMessageFragment;
import leebeno.com.leebeno.Login;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.SpleshScreen;

public class MessageAminFragment extends Fragment {


    ChatListAdapter chatListAdapterG;
    @Bind(R.id.groupRecycler)
    RecyclerView groupRecycler;
    @Bind(R.id.chatRecycler)
    RecyclerView chatRecycler;
    List<String> listJobItem;
    List<String> listJobItemD;
    List<String> listJobItemT;
    List<String> listJobItemG;
    List<String> listJobItemDG;
    List<String> listJobItemTG;
    ChatListAdapter chatListAdapter;

    @Bind(R.id.createLayout)
    LinearLayout createLayout;
    @Bind(R.id.showAllChats)
    LinearLayout showAllChats;
    private static int SPLASH_TIME_OUT = 1500;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View  v= inflater.inflate(R.layout.fragment_message_amin, container, false);
        ButterKnife.bind(this,v);

        listJobItem = new ArrayList<>();
        listJobItemD = new ArrayList<>();
        listJobItemT = new ArrayList<>();
        listJobItemG = new ArrayList<>();
        listJobItemDG = new ArrayList<>();
        listJobItemTG = new ArrayList<>();
        chatRecycler.setHasFixedSize(true);
        chatRecycler.setNestedScrollingEnabled(false);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        groupRecycler.setHasFixedSize(true);
        groupRecycler.setNestedScrollingEnabled(false);
        groupRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");
        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");
        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");
        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");
        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");
        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");
        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");


        listJobItemG.add("Chire H.");
        listJobItemDG.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemTG.add("11 Minutes ago");

        chatListAdapter = new ChatListAdapter(listJobItem, getActivity(), listJobItemD, listJobItemT);
        chatRecycler.setAdapter(chatListAdapter);
        chatListAdapterG = new ChatListAdapter(listJobItemG, getActivity(), listJobItemDG, listJobItemTG);
        groupRecycler.setAdapter(chatListAdapterG);

        createLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(intent);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showAllChats.setVisibility(View.VISIBLE);
                        createLayout.setVisibility(View.GONE);
                    }
                }, SPLASH_TIME_OUT);

            }
        });

        return v;
    }


}
