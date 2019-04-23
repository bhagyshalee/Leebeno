package leebeno.com.leebeno.AdminPart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.AdminPart.Adapters.LabourCreateGroupAdapter;
import leebeno.com.leebeno.R;

public class CreateGroupActivity extends AppCompatActivity {


    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.recyclerGroupMember)
    RecyclerView recyclerGroupMember;
    @Bind(R.id.btnCreateGroup)
    Button btnCreateGroup;
    LabourCreateGroupAdapter groupMemberAdapter;
    List<String> listGrpMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        ButterKnife.bind(this);

        listGrpMember = new ArrayList<>();
        recyclerGroupMember.setHasFixedSize(true);
        recyclerGroupMember.setNestedScrollingEnabled(false);
        recyclerGroupMember.setLayoutManager(new LinearLayoutManager(CreateGroupActivity.this, LinearLayoutManager.VERTICAL, false));


        listGrpMember.add("Deny Roy");
        listGrpMember.add("Jon Miller");
        listGrpMember.add("Deny Roy");
        listGrpMember.add("Jon Mille");
        listGrpMember.add("Deny Roy");
        listGrpMember.add("Jon Miller");
        listGrpMember.add("Deny Roy");
        listGrpMember.add("Jon Mille");
        listGrpMember.add("Deny Roy");
        listGrpMember.add("Jon Miller");
        listGrpMember.add("Deny Roy");
        listGrpMember.add("Jon Mille");
        listGrpMember.add("Deny Roy");
        listGrpMember.add("Jon Miller");
        listGrpMember.add("Deny Roy");
        listGrpMember.add("Jon Mille");


        groupMemberAdapter = new LabourCreateGroupAdapter(listGrpMember, CreateGroupActivity.this,btnCreateGroup);
        recyclerGroupMember.setAdapter(groupMemberAdapter);
        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
