package leebeno.com.leebeno.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.CustomerPart.RequestBid;
import leebeno.com.leebeno.R;

import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;


public class ReceivedBidAdapter extends RecyclerView.Adapter<ReceivedBidAdapter.ViewHolder> {
    private List<JobGetterSetter> additionalServices = null;
    private Context context;


    public ReceivedBidAdapter(List<JobGetterSetter> services, Context context) {
        super();
        this.additionalServices = services;
        this.context = context;
    }

    @Override
    public ReceivedBidAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_received_bid, parent, false);
        ReceivedBidAdapter.ViewHolder viewHolder = new ReceivedBidAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ReceivedBidAdapter.ViewHolder holder, final int position) {

        holder.titleBid.setText("Bidder Name: " + additionalServices.get(position).getJobTitle());
        holder.descriptionBid.setText(additionalServices.get(position).getDescription());
        holder.priceBid.setText(getDateFromUtc(additionalServices.get(position).getCreatedJob()));
        if (additionalServices.get(position).getStatus().compareTo(context.getResources().getString(R.string.rejected)) == 0) {
            holder.rejectedBidReq.setVisibility(View.VISIBLE);
        } else{
            holder.rejectedBidReq.setVisibility(View.GONE);
        }
        holder.layoutBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   Intent intent = new Intent(context, RequestBid.class);
                    intent.putExtra("bidId", additionalServices.get(position).getId());
                    intent.putExtra("bidStatus", additionalServices.get(position).getStatus());
                    context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return additionalServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleBid, priceBid, descriptionBid, rejectedBidReq;
        LinearLayout layoutBid;

        public ViewHolder(View itemView) {
            super(itemView);

            rejectedBidReq = (TextView) itemView.findViewById(R.id.rejectedBidReq);
            titleBid = (TextView) itemView.findViewById(R.id.titleBid);
            priceBid = (TextView) itemView.findViewById(R.id.priceBid);
            descriptionBid = (TextView) itemView.findViewById(R.id.descriptionBid);
            layoutBid = (LinearLayout) itemView.findViewById(R.id.layoutBid);


        }
    }
}