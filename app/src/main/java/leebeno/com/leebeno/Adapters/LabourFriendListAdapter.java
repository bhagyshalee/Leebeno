package leebeno.com.leebeno.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import leebeno.com.leebeno.AdminPart.Fragments.ConnectionsFragments.FriendListFragment;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Model.FriendListModel;
import leebeno.com.leebeno.R;


public class LabourFriendListAdapter extends RecyclerView.Adapter<LabourFriendListAdapter.ViewHolder> {
    private ArrayList<FriendListModel> friendListModelArrayList = null;
    private Context context;
    String stringStatus;
    FriendListModel friendListModel;
    Dialog dialog;


    public LabourFriendListAdapter(ArrayList<FriendListModel> friendListModelArrayList, Context context, String stringStatus) {
        super();
        this.friendListModelArrayList = friendListModelArrayList;
        this.context = context;
        this.stringStatus=stringStatus;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_groupmember, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        friendListModel = friendListModelArrayList.get(position);

        holder.groupMemberName.setText(friendListModel.getName());
        Picasso.get().load(WebUrls.BASE_URL+friendListModel.getImage())
                .fit()
                .placeholder(R.drawable.user).into(holder.groupMemberImage);

        if (friendListModel.getAddedInGroup()){
            holder.btnGroupMemberAdd.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_checked));
            holder.btnGroupMemberAdd.setEnabled(false);
        }else {
            holder.btnGroupMemberAdd.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_group_add));
            holder.btnGroupMemberAdd.setEnabled(true);

        }

    }


    @Override
    public int getItemCount() {
        return friendListModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements WebCompleteTask,View.OnClickListener {

        TextView groupMemberName;
        TextView btnGroupMemberRemove;
        ImageView btnGroupMemberAdd;
        ImageView groupMemberImage;
        int pos;

        public ViewHolder(View itemView) {
            super(itemView);
            groupMemberImage = (ImageView) itemView.findViewById(R.id.groupMemberImage);
            groupMemberName = (TextView) itemView.findViewById(R.id.groupMemberName);
            btnGroupMemberRemove = (TextView) itemView.findViewById(R.id.btnGroupMemberRemove);
            btnGroupMemberAdd = (ImageView) itemView.findViewById(R.id.btnGroupMemberAdd);
            btnGroupMemberAdd.setVisibility(View.GONE);
            btnGroupMemberRemove.setOnClickListener(this);
            btnGroupMemberAdd.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.btnGroupMemberRemove){

                int poss = getAdapterPosition();
                FriendListModel friendListModel =friendListModelArrayList.get(poss);
                if (friendListModel.getAddedInGroup()){
                    dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_layout);

                    TextView msg = (TextView)dialog.findViewById(R.id.popup_msg);
                    TextView ok = (TextView)dialog.findViewById(R.id.popu_ok_tv);
                    TextView cancel = (TextView)dialog.findViewById(R.id.popup_cancel_tv);

                    msg.setText("You can't remove from friend list because your are connected with group");
                    cancel.setVisibility(View.GONE);
                    ok.setText(R.string.cancel);
                    ok.setBackground(context.getDrawable(R.drawable.border_pruple_f));
                    ok.setTextColor(context.getResources().getColor(R.color.pruple));
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                }else {
                    dialog = new Dialog(context);
                    dialog.setContentView(R.layout.popup_layout);
                    TextView msg = (TextView)dialog.findViewById(R.id.popup_msg);
                    TextView ok = (TextView)dialog.findViewById(R.id.popu_ok_tv);
                    TextView cancel = (TextView)dialog.findViewById(R.id.popup_cancel_tv);
                    msg.setText(R.string.romve_from_frnd);
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RemoveMethod(pos);
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
        }
        public void RemoveMethod(int poss){
            poss = getAdapterPosition();
            FriendListModel friendListModel =friendListModelArrayList.get(poss);
                HashMap objectNew = new HashMap();
                    objectNew.put("requestId",friendListModel.getReq_id());
//                    objectNew.put("labourId",friendListModel.getId());
                    new WebTask(context,WebUrls.BASE_URL+WebUrls.leftGrouplist,objectNew,this, RequestCode.CODE_LeftGroupList,1);

        }

        @Override
        public void onComplete(String response, int taskcode) {
            if (taskcode == RequestCode.CODE_LeftGroupList){
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject successObj = jsonObject.optJSONObject("success");
                    JSONObject dagaObje=successObj.optJSONObject("data");

                    String reqMadeBy =dagaObje.optString("reqMadeBy");
//                    btnGroupMemberRemove.setText(dagaObje.optString("reqStatus"));
                    dialog.dismiss();
                    FriendListFragment.getInstance().getFriendList();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}