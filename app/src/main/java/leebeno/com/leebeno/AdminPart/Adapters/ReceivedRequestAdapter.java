package leebeno.com.leebeno.AdminPart.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import leebeno.com.leebeno.AdminPart.Fragments.ConnectionsFragments.FriendListFragment;
import leebeno.com.leebeno.AdminPart.Fragments.ConnectionsFragments.ReceivedRequestFragment;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.Model.ReceivedRequestModel;
import leebeno.com.leebeno.R;

public class ReceivedRequestAdapter extends RecyclerView.Adapter<ReceivedRequestAdapter.ViewHolder> {
    private ArrayList<ReceivedRequestModel> receivedRequestModelArrayList = null;
    private Context context;
    ReceivedRequestModel receivedRequestModel;


    public ReceivedRequestAdapter(ArrayList<ReceivedRequestModel> receivedRequestModelArrayList, Context context) {
        super();
        this.receivedRequestModelArrayList = receivedRequestModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_received_chat, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        receivedRequestModel = receivedRequestModelArrayList.get(position);

        holder.groupMemberName.setText(receivedRequestModel.getName());
        Picasso.get().load(WebUrls.BASE_URL+receivedRequestModel.getImage())
                .fit()
                .placeholder(R.drawable.user_).into(holder.groupMemberImage);

    }

    @Override
    public int getItemCount() {
        return receivedRequestModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements WebCompleteTask,View.OnClickListener {

        TextView groupMemberName;
        Button btnGroupMemberAccept,btnGroupMemberReject;
        CircleImageView groupMemberImage;
        int pos;

        public ViewHolder(View itemView) {
            super(itemView);

            groupMemberImage = (CircleImageView) itemView.findViewById(R.id.groupMemberImage);
            groupMemberName = (TextView) itemView.findViewById(R.id.groupMemberName);
            btnGroupMemberAccept = (Button) itemView.findViewById(R.id.btnGroupMemberAccept);
            btnGroupMemberReject = (Button) itemView.findViewById(R.id.btnGroupMemberReject);
            btnGroupMemberAccept.setOnClickListener(this);
            btnGroupMemberReject.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
           if (v.getId()==R.id.btnGroupMemberAccept){
               AcceptMethod();
           }else if (v.getId()==R.id.btnGroupMemberReject){
               RejectMethod();
           }
        }

        private void AcceptMethod(){
            int poss = getAdapterPosition();
            receivedRequestModel = receivedRequestModelArrayList.get(poss);
            HashMap objectNew = new HashMap();
            objectNew.put("requestId",receivedRequestModel.getReq_id());
            new WebTask(context,WebUrls.BASE_URL+WebUrls.acceptRequest,objectNew,this, RequestCode.CODE_AcceptRequest,1);

        }
        private void RejectMethod(){
            int poss = getAdapterPosition();
            receivedRequestModel = receivedRequestModelArrayList.get(poss);
            HashMap objectNew = new HashMap();
            objectNew.put("requestId",receivedRequestModel.getReq_id());
            new WebTask(context,WebUrls.BASE_URL+WebUrls.rejectRequest,objectNew,this,RequestCode.CODE_RejectRequest,1);
        }

        @Override
        public void onComplete(String response, int taskcode) {
            if (taskcode == RequestCode.CODE_AcceptRequest){
                try {
                    Log.d("CODE_AcceptRequest",response);
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject successObj = jsonObject.optJSONObject("success");
                    JSONObject dagaObje=successObj.optJSONObject("data");

                    String reqMadeBy =dagaObje.optString("reqMadeBy");
                    if (SharedPrefManager.getUserStatus(context, Constrants.UserStatus).compareTo("labour")==0){
                        ReceivedRequestFragment.getInstance().getLabReceivedReq();
                        FriendListFragment.getInstance().getFriendList();
                    } else if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("groupAdmin")==0) {
                        ReceivedRequestFragment.getInstance().getGroupReceivedReq();
                        FriendListFragment.getInstance().getFriendList();
                    }
//                    btnGroupMemberRemove.setText(dagaObje.optString("reqStatus"));
//                    btnGroupMemberRemove.setBackground(context.getResources().getDrawable(R.drawable.gray_rect_background));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else  if (taskcode == RequestCode.CODE_RejectRequest){
                try {
                    Log.d("CODE_RejectRequest",response);
                    JSONObject jsonObject=new JSONObject(response);
                    JSONObject successObj = jsonObject.optJSONObject("success");
                    JSONObject dagaObje=successObj.optJSONObject("data");

                    String reqMadeBy =dagaObje.optString("reqMadeBy");
                    if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("labour")==0){
                        ReceivedRequestFragment.getInstance().getLabReceivedReq();
                    } else if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("groupAdmin")==0) {
                        ReceivedRequestFragment.getInstance().getGroupReceivedReq();
                    }
//                    btnGroupMemberRemove.setText(dagaObje.optString("reqStatus"));
//                    btnGroupMemberRemove.setBackground(context.getResources().getDrawable(R.drawable.gray_rect_background));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}