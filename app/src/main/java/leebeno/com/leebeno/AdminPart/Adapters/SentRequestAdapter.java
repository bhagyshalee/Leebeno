package leebeno.com.leebeno.AdminPart.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.Model.SentRequestModel;
import leebeno.com.leebeno.R;


public class SentRequestAdapter extends RecyclerView.Adapter<SentRequestAdapter.ViewHolder> {
    private ArrayList<SentRequestModel> sentRequestModelArrayList = null;
    private Context context;
    SentRequestModel sentRequestModel;


    public SentRequestAdapter(ArrayList<SentRequestModel> sentRequestModelArrayList, Context context) {
        super();
        this.sentRequestModelArrayList = sentRequestModelArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_groupmember, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        sentRequestModel = sentRequestModelArrayList.get(position);

        holder.groupMemberName.setText(sentRequestModel.getName());
        Picasso.get().load(WebUrls.BASE_URL+sentRequestModel.getImage())
                .fit()
                .placeholder(R.drawable.user_).into(holder.groupMemberImage);

        holder.btnGroupMemberRemove.setVisibility(View.GONE);
        holder.waitForResponse.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return sentRequestModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView groupMemberName,waitForResponse;
        TextView btnGroupMemberRemove;
        CircleImageView groupMemberImage;

        public ViewHolder(View itemView) {
            super(itemView);

            groupMemberImage = (CircleImageView) itemView.findViewById(R.id.groupMemberImage);
            groupMemberName = (TextView) itemView.findViewById(R.id.groupMemberName);
            waitForResponse = (TextView) itemView.findViewById(R.id.waitForResponse);
            btnGroupMemberRemove = (TextView) itemView.findViewById(R.id.btnGroupMemberRemove);


        }
    }
}