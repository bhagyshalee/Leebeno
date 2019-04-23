package leebeno.com.leebeno.AdminPart.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import leebeno.com.leebeno.AdminPart.NewConnectionRequest;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.LabourProfile;
import leebeno.com.leebeno.Model.ConnectionsModel;
import leebeno.com.leebeno.R;


public class NewConnectionAdapter extends RecyclerView.Adapter<NewConnectionAdapter.ViewHolder> {
    private ArrayList<ConnectionsModel> connectionsModelArrayList = null;
    private Context context;
    String stringStatus;
    ConnectionsModel connectionsModel;


    public NewConnectionAdapter(ArrayList<ConnectionsModel> connectionsModelArrayList, Context context, String stringStatus) {
        super();
        this.connectionsModelArrayList = connectionsModelArrayList;
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

        connectionsModel = connectionsModelArrayList.get(position);

        holder.groupMemberName.setText(connectionsModel.getFullName());
        Picasso.get().load(WebUrls.BASE_URL+connectionsModel.getImage())
                .fit()
                .placeholder(R.drawable.user_).into(holder.groupMemberImage);

        if (stringStatus.equals("send request"))
        {
            if (connectionsModel.getRequested()!=null&&connectionsModel.getRequested().compareTo("requested")==0){
                if (SharedPrefManager.getUserStatus(context, Constrants.UserStatus).compareTo("labour")==0&&connectionsModel.getRequestby().compareTo("GroupAdmin")==0){
                    holder.btnGroupMemberRemove.setText(context.getString(R.string.requesting));
                    holder.btnGroupMemberRemove.setEnabled(false);
                }else if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("groupAdmin")==0&&connectionsModel.getRequestby().compareTo("Labour")==0){
                    holder.btnGroupMemberRemove.setText(context.getString(R.string.requesting));
                    holder.btnGroupMemberRemove.setEnabled(false);
                } else {
                    holder.btnGroupMemberRemove.setText(context.getString(R.string.cancel));
                }
                holder.btnGroupMemberRemove.setBackground(context.getResources().getDrawable(R.drawable.gray_rect_background));
            }else {
                holder.btnGroupMemberRemove.setText(context.getResources().getString(R.string.send_request));
                holder.btnGroupMemberRemove.setBackground(context.getResources().getDrawable(R.drawable.green_rect_background));
            }

        }else {
            holder.btnGroupMemberRemove.setText(context.getResources().getString(R.string.addToGroup));
            holder.btnGroupMemberRemove.setBackground(context.getResources().getDrawable(R.drawable.green_rect_background));
        }

    }


    @Override
    public int getItemCount() {
        return connectionsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements WebCompleteTask,View.OnClickListener {

        TextView groupMemberName;
        TextView btnGroupMemberRemove;
        ImageView groupMemberImage;
        int pos;

        public ViewHolder(View itemView) {
            super(itemView);
            groupMemberImage = (ImageView) itemView.findViewById(R.id.groupMemberImage);
            groupMemberName = (TextView) itemView.findViewById(R.id.groupMemberName);
            btnGroupMemberRemove = (TextView) itemView.findViewById(R.id.btnGroupMemberRemove);
            btnGroupMemberRemove.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int poss = getAdapterPosition();
                    ConnectionsModel connectionsModel =connectionsModelArrayList.get(poss);
                    Intent intentnew =new Intent(context,LabourProfile.class);
                    intentnew.putExtra("bidPersonId",connectionsModelArrayList.get(poss).getId());
                    context.startActivity(intentnew);
                }
            });
        }
        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.btnGroupMemberRemove){
                LikeMethod(pos);
            }
        }

        public void LikeMethod(int poss){
            poss = getAdapterPosition();
            ConnectionsModel connectionsModel =connectionsModelArrayList.get(poss);
            HashMap objectNew = new HashMap();
            Log.e("getAllRequestIDS  ","  Requested"+connectionsModel.getRequested()
            +"   ID"+connectionsModel.getId()+"  RequestedID"+connectionsModel.getRequest_id());

            if (connectionsModel.getRequested()!=null&&connectionsModel.getRequested().compareTo("requested")==0){
                if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("labour")==0){
//                    objectNew.put("groupAdminId",connectionsModel.getId());
                    objectNew.put("requestId",connectionsModel.getRequest_id());
                    new WebTask(context,WebUrls.BASE_URL+WebUrls.cancelRequest,objectNew,this, RequestCode.CODE_CancelRequest,5);
                } else if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("groupAdmin")==0) {
//                    objectNew.put("groupAdminId",SharedPrefManager.getUserID(context,Constrants.UserID));
                    objectNew.put("requestId",connectionsModel.getRequest_id());
                    new WebTask(context,WebUrls.BASE_URL+WebUrls.cancelRequest,objectNew,this,RequestCode.CODE_CancelRequest,5);
                }

            }else {
                if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("labour")==0){
                    objectNew.put("groupAdminId",connectionsModel.getId());
                    objectNew.put("labourId",SharedPrefManager.getUserID(context,Constrants.UserID));
                    new WebTask(context,WebUrls.BASE_URL+WebUrls.makeRequest,objectNew,this,RequestCode.CODE_MakeRequest,5);
                } else if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("groupAdmin")==0) {
                    objectNew.put("groupAdminId",SharedPrefManager.getUserID(context,Constrants.UserID));
                    objectNew.put("labourId",connectionsModel.getId());
                    new WebTask(context,WebUrls.BASE_URL+WebUrls.makeRequest,objectNew,this,RequestCode.CODE_MakeRequest,5
                    );
                }
            }
        }

        @Override
        public void onComplete(String response, int taskcode) {
            if (taskcode == RequestCode.CODE_MakeRequest){
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject successObj = jsonObject.optJSONObject("success");
                    JSONObject dagaObje=successObj.optJSONObject("data");

                    String reqMadeBy =dagaObje.optString("reqMadeBy");
//                    btnGroupMemberRemove.setText(dagaObje.optString("reqStatus"));
                    if (dagaObje.optString("reqStatus").compareTo("requested")==0) {
                        btnGroupMemberRemove.setText(context.getString(R.string.cancel));
                        btnGroupMemberRemove.setBackground(context.getResources().getDrawable(R.drawable.gray_rect_background));
                        if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("labour")==0){
                            NewConnectionRequest.getInstance().GroupMemberList(0.0,0.0);
                        } else if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("groupAdmin")==0) {
                            NewConnectionRequest.getInstance().LabourMemberList(0.0,0.0);
                        }
                    }
//                    if (msgObje.optString("replyCode").compareTo("Success")==0) {

//                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (taskcode == RequestCode.CODE_CancelRequest){
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject successObj = jsonObject.optJSONObject("success");
                    JSONObject msgObje=successObj.optJSONObject("msg");

                    if (msgObje.optString("replyCode").compareTo("Success")==0) {
                        if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("labour")==0){
                            NewConnectionRequest.getInstance().GroupMemberList(0.0,0.0);
                        } else if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("groupAdmin")==0) {
                            NewConnectionRequest.getInstance().LabourMemberList(0.0,0.0);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}