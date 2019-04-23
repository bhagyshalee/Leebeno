package leebeno.com.leebeno.AdminPart.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import leebeno.com.leebeno.AdminPart.CreatedLabourDetail;
import leebeno.com.leebeno.AdminPart.CreatedLabours;
import leebeno.com.leebeno.Api.RequestCode;
import leebeno.com.leebeno.Api.WebCompleteTask;
import leebeno.com.leebeno.Api.WebTask;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.Model.CreateByAdminListModel;
import leebeno.com.leebeno.R;

import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;


public class CreatedLabourAdapter extends RecyclerView.Adapter<CreatedLabourAdapter.ViewHolder> {
    private ArrayList<CreateByAdminListModel> createByAdminListModelArrayList = null;
    private Context context;
    CreateByAdminListModel createByAdminListModel;
    Dialog dialog;


    public CreatedLabourAdapter(ArrayList<CreateByAdminListModel> createByAdminListModelArrayList, Context context) {
        super();
        this.createByAdminListModelArrayList = createByAdminListModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_created_labour, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        createByAdminListModel = createByAdminListModelArrayList.get(position);
        holder.labourName.setText(createByAdminListModel.getName());
        holder.createdDate.setText("Created On " + getDateFromUtc(createByAdminListModel.getDate()));
        Picasso.get().load(WebUrls.BASE_URL + createByAdminListModel.getImage())
                .fit()
                .placeholder(R.drawable.user).into(holder.labourProfile);

        holder.layoutProfileReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("jhjhjh",""+createByAdminListModel.getId());
                Intent intent = new Intent(context, CreatedLabourDetail.class);
                intent.putExtra("bidPersonId",createByAdminListModelArrayList.get(position).getId());
                context.startActivity(intent);
            }
        });

        if (createByAdminListModel.getAddedInGroup()) {
            holder.add_group.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_checked));
            holder.add_group.setEnabled(false);
            holder.delete.setEnabled(true);
        } else {
            holder.add_group.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_group_add));
            holder.add_group.setEnabled(true);
            holder.delete.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {
        return createByAdminListModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, WebCompleteTask {

        TextView labourName, createdDate;
        ImageView labourProfile, add_group, delete;
        RelativeLayout layoutProfileReview;
        int pos;

        public ViewHolder(View itemView) {
            super(itemView);

            labourProfile = (ImageView) itemView.findViewById(R.id.labourProfile);
            labourName = (TextView) itemView.findViewById(R.id.labourName);
            createdDate = (TextView) itemView.findViewById(R.id.createdDate);
            add_group = (ImageView) itemView.findViewById(R.id.add_group);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            layoutProfileReview = (RelativeLayout) itemView.findViewById(R.id.layoutProfileReview);
            delete.setVisibility(View.GONE);
            add_group.setOnClickListener(this);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.delete) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.popup_layout);
                TextView msg = (TextView) dialog.findViewById(R.id.popup_msg);
                TextView ok = (TextView) dialog.findViewById(R.id.popu_ok_tv);
                TextView cancel = (TextView) dialog.findViewById(R.id.popup_cancel_tv);
                msg.setText(R.string.are_you_sure_rvm_frm_grp);
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
            if (v.getId() == R.id.add_group) {
                dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_layout);

                TextView msg = (TextView) dialog.findViewById(R.id.popup_msg);
                TextView ok = (TextView) dialog.findViewById(R.id.popu_ok_tv);
                TextView cancel = (TextView) dialog.findViewById(R.id.popup_cancel_tv);

                msg.setText(R.string.add_on_group);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddMemberMethod(pos);
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

        public void RemoveMethod(int poss) {
            poss = getAdapterPosition();
            CreateByAdminListModel createByAdminListModel = createByAdminListModelArrayList.get(poss);
            HashMap objectNew = new HashMap();
            if (SharedPrefManager.getUserStatus(context, Constrants.UserStatus).compareTo("groupAdmin") == 0) {
                objectNew.put("memberId", createByAdminListModel.getId());
                new WebTask(context, WebUrls.BASE_URL + WebUrls.removeOwnMember, objectNew, this, RequestCode.CODE_removeOwnMember, 1);
            }
        }

        public void AddMemberMethod(int poss) {
            poss = getAdapterPosition();
            CreateByAdminListModel createByAdminListModel = createByAdminListModelArrayList.get(poss);

            HashMap objectNew = new HashMap();
            if (SharedPrefManager.getUserStatus(context, Constrants.UserStatus).compareTo("groupAdmin") == 0) {
                objectNew.put("groupMemberId", createByAdminListModel.getId());
                new WebTask(context, WebUrls.BASE_URL + WebUrls.addOwnMember, objectNew, this, RequestCode.CODE_addOwnMember, 1);
            }
        }

        @Override
        public void onComplete(String response, int taskcode) {
            if (taskcode == RequestCode.CODE_removeOwnMember) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject successObj = jsonObject.optJSONObject("success");
                    JSONObject dagaObje = successObj.optJSONObject("data");

                    String reqMadeBy = dagaObje.optString("reqMadeBy");
                    dialog.dismiss();
                    CreatedLabours.getInstance().LabourCreateMemberList();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (taskcode == RequestCode.CODE_addOwnMember) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject successObj = jsonObject.optJSONObject("success");
                    JSONObject dagaObje = successObj.optJSONObject("data");
                    dialog.dismiss();
                    CreatedLabours.getInstance().LabourCreateMemberList();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}