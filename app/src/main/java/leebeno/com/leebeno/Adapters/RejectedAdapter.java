package leebeno.com.leebeno.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import leebeno.com.leebeno.BidDetial;
import leebeno.com.leebeno.Model.RejectModel;
import leebeno.com.leebeno.R;


public class RejectedAdapter extends RecyclerView.Adapter<RejectedAdapter.ViewHolder> {
    private ArrayList<RejectModel> rejectModelArrayList = null;
    private Context context;
    RejectModel rejectModel;
    Dialog dialog;


    public RejectedAdapter(ArrayList<RejectModel> rejectModelArrayList, Context context) {
        super();
        this.rejectModelArrayList = rejectModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rejected_raw, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        rejectModel = rejectModelArrayList.get(position);
        holder.jobtile.setText(rejectModel.getJobtile());
        holder.comment.setText(rejectModel.getComment());
        holder.placeddate.setText(rejectModel.getPlaceddate());
        holder.usd.setText("$ "+rejectModel.getUsd());

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat target_date = new SimpleDateFormat("dd MMM hh:mm aaa", Locale.ENGLISH);
        Calendar calendar = Calendar.getInstance();
        TimeZone tz = calendar.getTimeZone();
        target_date.setTimeZone(tz);

        try {
            Date startDate = df.parse(rejectModel.getRejected_date());
            String readReminderdate = target_date.format(startDate);
            holder.rejecteddate.setText(readReminderdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            Date startDate = df.parse(rejectModel.getPlaceddate());
//            String newDateString = df.format(startDate);
            String readReminderdate = target_date.format(startDate);
            holder.placeddate.setText(readReminderdate);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return rejectModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView jobtile,customer_name,comment,placeddate,rejecteddate,usd;
        int pos;

        public ViewHolder(View itemView) {
            super(itemView);
            jobtile = (TextView) itemView.findViewById(R.id.jobtile);
            customer_name = (TextView) itemView.findViewById(R.id.customer_name);
            comment = (TextView)itemView.findViewById(R.id.comment);
            placeddate = (TextView) itemView.findViewById(R.id.placeddate);
            rejecteddate = (TextView) itemView.findViewById(R.id.rejecteddate);
            usd = (TextView) itemView.findViewById(R.id.usd);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pos = getAdapterPosition();
                    RejectModel rejectModel =rejectModelArrayList.get(pos);
                    context.startActivity(new Intent(context,BidDetial.class).putExtra("bid_id",rejectModel.getId()));
                }
            });
        }

        @Override
        public void onClick(View v) {
//            if (v.getId()==R.id.delete){
//                dialog = new Dialog(context);
//                dialog.setContentView(R.layout.popup_layout);
//                TextView msg = (TextView)dialog.findViewById(R.id.popup_msg);
//                TextView ok = (TextView)dialog.findViewById(R.id.popu_ok_tv);
//                TextView cancel = (TextView)dialog.findViewById(R.id.popup_cancel_tv);
//                msg.setText(R.string.are_you_sure_rvm_frm_grp);
//                ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        RemoveMethod(pos);
//                    }
//                });
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//            }
//            if (v.getId()== R.id.add_group){
//                dialog = new Dialog(context);
//                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.popup_layout);
//
//                TextView msg = (TextView)dialog.findViewById(R.id.popup_msg);
//                TextView ok = (TextView)dialog.findViewById(R.id.popu_ok_tv);
//                TextView cancel = (TextView)dialog.findViewById(R.id.popup_cancel_tv);
//
//                msg.setText(R.string.add_on_group);
//                ok.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AddMemberMethod(pos);
//                    }
//                });
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//            }
        }

//        public void RemoveMethod(int poss){
//            poss = getAdapterPosition();
//            CreateByAdminListModel createByAdminListModel =createByAdminListModelArrayList.get(poss);
//            HashMap objectNew = new HashMap();
//            if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("groupAdmin")==0) {
//                objectNew.put("memberId",createByAdminListModel.getId());
//                new WebTask(context,WebUrls.BASE_URL+WebUrls.removeOwnMember,objectNew,this,RequestCode.CODE_removeOwnMember,1);
//            }
//        }
//
//        public void AddMemberMethod(int poss){
//            poss = getAdapterPosition();
//            CreateByAdminListModel createByAdminListModel = createByAdminListModelArrayList.get(poss);
//
//            HashMap objectNew = new HashMap();
//            if (SharedPrefManager.getUserStatus(context,Constrants.UserStatus).compareTo("groupAdmin")==0){
//                objectNew.put("groupMemberId",createByAdminListModel.getId());
//                new WebTask(context,WebUrls.BASE_URL+WebUrls.addOwnMember,objectNew,this,RequestCode.CODE_addOwnMember,1);
//            }
//        }
//
//        @Override
//        public void onComplete(String response, int taskcode) {
//            if (taskcode == RequestCode.CODE_removeOwnMember){
//                try {
//                    JSONObject jsonObject=new JSONObject(response);
//                    JSONObject successObj = jsonObject.optJSONObject("success");
//                    JSONObject dagaObje=successObj.optJSONObject("data");
//
//                    String reqMadeBy =dagaObje.optString("reqMadeBy");
//                    dialog.dismiss();
//                    CreatedLabours.getInstance().LabourCreateMemberList();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (taskcode == RequestCode.CODE_addOwnMember){
//                try {
//                    JSONObject jsonObject=new JSONObject(response);
//                    JSONObject successObj = jsonObject.optJSONObject("success");
//                    JSONObject dagaObje=successObj.optJSONObject("data");
//                    dialog.dismiss();
//                    CreatedLabours.getInstance().LabourCreateMemberList();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}