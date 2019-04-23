package leebeno.com.leebeno.LabourPart.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.CustomerPart.Adapter.ChatListAdapter;
import leebeno.com.leebeno.R;


public class GroupMessageFragment extends Fragment {

    @Bind(R.id.chatRecycler)
    RecyclerView chatRecycler;
    List<String> listJobItem;
    List<String> listJobItemD;
    List<String> listJobItemT;
    ChatListAdapter chatListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_group_message, container, false);
        ButterKnife.bind(this, v);
        listJobItem = new ArrayList<>();
        listJobItemD = new ArrayList<>();
        listJobItemT = new ArrayList<>();
        chatRecycler.setHasFixedSize(true);
        chatRecycler.setNestedScrollingEnabled(true);
        chatRecycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");
        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");
        listJobItem.add("Chire H.");
        listJobItemD.add("hey  how r u and where are u... so may years ago to see u");
        listJobItemT.add("11 Minutes ago");

        chatListAdapter = new ChatListAdapter(listJobItem, getActivity(), listJobItemD, listJobItemT);
        chatRecycler.setAdapter(chatListAdapter);

        return v;
    }


}
