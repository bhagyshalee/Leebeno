package leebeno.com.leebeno.AdminPart.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import leebeno.com.leebeno.AdminPart.Fragments.ConnectionsFragments.FriendListFragment;
import leebeno.com.leebeno.AdminPart.ConnectionsAdmin;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Model.ConnectionsModel;
import leebeno.com.leebeno.R;


public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.ViewHolder> {
    private ArrayList<ConnectionsModel> connectionsModelArrayList;
    private Context context;
    ConnectionsModel connectionsModel;
    Dialog dialog;


    public GroupMemberAdapter(ArrayList<ConnectionsModel> connectionsModelArrayList, Context context) {
        super();
        this.connectionsModelArrayList = connectionsModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grouplist_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        connectionsModel = connectionsModelArrayList.get(position);
        holder.groupMemberName.setText(connectionsModel.getFullName()+"");
        Picasso.get().load(WebUrls.BASE_URL+connectionsModel.getImage())
                .fit()
                .placeholder(R.drawable.user_).into(holder.groupMemberImage);

    }

    @Override
    public int getItemCount() {
        return connectionsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,WebCompleteTask {

        TextView groupMemberName;
        TextView btnGroupMemberRemove;
        CircleImageView groupMemberImage;
        int pos;

        public ViewHolder(View itemView) {
            super(itemView);

            groupMemberImage = (CircleImageView) itemView.findViewById(R.id.groupMemberImage);
            groupMemberName = (TextView) itemView.findViewById(R.id.groupMemberName);
            btnGroupMemberRemove = (TextView) itemView.findViewById(R.id.btnGroupMemberleft);
            btnGroupMemberRemove.setVisibility(View.VISIBLE);

            btnGroupMemberRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            if (v.getId()==R.id.btnGroupMemberleft){
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.popup_layout);
                TextView msg = (TextView)dialog.findViewById(R.id.popup_msg);
                TextView ok = (TextView)dialog.findViewById(R.id.popu_ok_tv);
                TextView cancel = (TextView)dialog.findViewById(R.id.popup_cancel_tv);
                msg.setText(R.string.rvm_frm_grp);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RemoveFromGroup();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

            }
        }

        public void RemoveFromGroup(){
            int poss = getAdapterPosition();
            ConnectionsModel connectionsModel = connectionsModelArrayList.get(poss);
            HashMap objectNew = new HashMap();
            objectNew.put("groupId",connectionsModel.getGroup_id());
            objectNew.put("memberId",connectionsModel.getId());
            new WebTask(context,WebUrls.BASE_URL+WebUrls.removeMember,objectNew,this, RequestCode.CODE_RemoveMember,1);
        }

        @Override
        public void onComplete(String response, int taskcode) {

            if (taskcode == RequestCode.CODE_RemoveMember){
                try {
                    JSONObject successObject = new JSONObject(response);
                    JSONObject dataObj = successObject.optJSONObject("data");
                    dialog.dismiss();
                    FriendListFragment.getInstance().getFriendList();
                    ConnectionsAdmin.getInstance().GroupMemberList();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

        }
    }
}