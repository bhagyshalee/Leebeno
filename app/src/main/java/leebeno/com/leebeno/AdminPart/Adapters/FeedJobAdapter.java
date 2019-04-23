package leebeno.com.leebeno.AdminPart.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import leebeno.com.leebeno.BidPlace;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Services.Utility;

import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;


public class FeedJobAdapter extends RecyclerView.Adapter<FeedJobAdapter.ViewHolder> implements Filterable {

    ArrayList<Boolean> data_check = new ArrayList<>();
    private List<JobGetterSetter> services = null;
    private Context context;
    private List<JobGetterSetter> services_filter;

    public FeedJobAdapter(List<JobGetterSetter> services, Context context) {
        super();
        this.services = services;
        this.services_filter = services;
        this.context = context;
        data_check = new ArrayList<>();
        for (int j = 0; j < services.size(); j++) {
            data_check.add(false);

        }
        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getBidStatus() != null) {
                if (services.get(i).getBidStatus().compareTo(context.getResources().getString(R.string.rejected)) == 0) {
                    data_check.set(i, true);
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_feed_job, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


      /*  Date d = null;
        try {
            d = inputFormat.parse("" + services.get(position).getCreatedJob());
        } catch (ParseException ex) {
            Log.e("neerbbgbt", "" + ex);
        }
*/


        holder.skillName.setText(services.get(position).getJobTitle());
        holder.skillDescription.setText(services.get(position).getDescription());

        if (services.get(position).getPaymentType().compareTo(context.getResources().getString(R.string.range)) == 0) {
            holder.paymentType.setText("$ " + services.get(position).getMin() + " - " + "$ " + services.get(position).getMax());
        } else {
            holder.paymentType.setText("$ " + services.get(position).getAmount());
        }
        //holder.paymentType.setText(services.get(position).getPaymentType());

        holder.startDate.setText("Start Date " + getDateFromUtc(services.get(position).getStartDate()));
//        holder.agoTiming.setText("Posted " + getDateDifference(d));

        holder.agoTiming.setText("Posted: " + Utility.printDifference(services.get(position).getCreatedJob()));

        holder.noOfBids.setText("No of bids : " + services.get(position).getBidCount());

        if (services.get(position).getBidStatus() != null) {

//            Log.e("sdfjnjkdfg",""+data_check.size()+" "+services.size());
//            if (data_check.get(position)) {
//                for (int j=0;j<=data_check.size();j++){
            if (services.get(position).getBidStatus().compareTo(context.getResources().getString(R.string.rejected)) == 0) {
                holder.alreadyBid.setText(context.getResources().getString(R.string.rejected));
                holder.alreadyBid.setTextColor(context.getResources().getColor(R.color.red));
//                    }

//                }

            } else {
                holder.alreadyBid.setText(context.getResources().getString(R.string.applied));
                holder.alreadyBid.setTextColor(context.getResources().getColor(R.color.blue));
            }
        } else {
            holder.alreadyBid.setText(context.getResources().getString(R.string.notapplied));
            holder.alreadyBid.setTextColor(context.getResources().getColor(R.color.blue));
        }

        holder.layoutC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.alreadyBid.getText().equals(context.getResources().getString(R.string.applied))) {
                    Intent intent = new Intent(context, BidPlace.class);
                    intent.putExtra("bid", holder.alreadyBid.getText().toString());
                    intent.putExtra("jobId", services.get(position).getId());
                    context.startActivity(intent);
                } else if (holder.alreadyBid.getText().equals(context.getResources().getString(R.string.notapplied))) {
                    Intent intent = new Intent(context, BidPlace.class);
                    intent.putExtra("bid", holder.alreadyBid.getText().toString());
                    intent.putExtra("jobId", services.get(position).getId());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, BidPlace.class);
                    intent.putExtra("bid", holder.alreadyBid.getText().toString());
                    intent.putExtra("jobId", services.get(position).getId());
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    services = services_filter;
                }
                else if (charString.compareTo("            ")==0){
                    Log.e("njsvnjv","blank text");
                }else {
                    ArrayList<JobGetterSetter> filteredList = new ArrayList<>();
                    for (JobGetterSetter row : services_filter) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getJobTitle().toLowerCase().contains(charString.toLowerCase())
                                ) {
                            filteredList.add(row);
                        }
                    }

                    services = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = services;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                services = (List<JobGetterSetter>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView skillName, skillDescription, paymentType, startDate, agoTiming, alreadyBid, noOfBids;
        LinearLayout layoutC;
        CardView homeCard;

        public ViewHolder(View itemView) {
            super(itemView);

            skillName = (TextView) itemView.findViewById(R.id.skillName);
            skillDescription = (TextView) itemView.findViewById(R.id.skillDescription);
            paymentType = (TextView) itemView.findViewById(R.id.paymentType);
            startDate = (TextView) itemView.findViewById(R.id.startDate);
            agoTiming = (TextView) itemView.findViewById(R.id.agoTiming);
            alreadyBid = (TextView) itemView.findViewById(R.id.alreadyBid);
            noOfBids = (TextView) itemView.findViewById(R.id.noOfBids);
            layoutC = (LinearLayout) itemView.findViewById(R.id.layoutC);
            homeCard = (CardView) itemView.findViewById(R.id.homeCard);


        }
    }
}