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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import leebeno.com.leebeno.BidPlace;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Services.Utility;

import static leebeno.com.leebeno.Services.Utility.getDateDifference;
import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;
import static leebeno.com.leebeno.Services.Utility.inputFormat;


public class MyBidedJobAdapter extends RecyclerView.Adapter<MyBidedJobAdapter.ViewHolder> implements Filterable {
    private List<JobGetterSetter> additionalServices = null;
    private List<JobGetterSetter> services_filter;
    private Context context;


    public MyBidedJobAdapter(List<JobGetterSetter> services, Context context) {
        super();
        this.additionalServices = services;
        this.services_filter = services;
        this.context = context;
    }

    @Override
    public MyBidedJobAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_bided_job, parent, false);
        MyBidedJobAdapter.ViewHolder viewHolder = new MyBidedJobAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final MyBidedJobAdapter.ViewHolder holder, final int position) {
     /*   Date d = null;
        try {
            d = inputFormat.parse("" + additionalServices.get(position).getCreatedJob());
        } catch (ParseException ex) {
            Log.e("neerbbgbt", "" + ex);
        }

*/
        holder.skillName.setText(additionalServices.get(position).getJobTitle());
        holder.skillDescription.setText(additionalServices.get(position).getDescription());
        holder.paymentType.setText("$ " + additionalServices.get(position).getAmount());
//        holder.agoTiming.setText("Posted " + getDateDifference(d));
        holder.agoTiming.setText("Posted: " + Utility.printDifference(additionalServices.get(position).getCreatedJob()));
        holder.startDate.setText("Start Date : " + getDateFromUtc(additionalServices.get(position).getStartDate()));
        holder.jobStatus.setText(additionalServices.get(position).getStatus());
        if (additionalServices.get(position).getStatus().equals(context.getResources().getString(R.string.ongoingStatus))) {
            holder.jobStatus.setTextColor(context.getResources().getColor(R.color.blue));
        }
        if (additionalServices.get(position).getStatus().equals(context.getResources().getString(R.string.amountpendingStatus))) {
            holder.jobStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        if (additionalServices.get(position).getStatus().equals(context.getResources().getString(R.string.satisfiedStatus))) {
            holder.jobStatus.setTextColor(context.getResources().getColor(R.color.pruple));
        }
        if (additionalServices.get(position).getStatus().equals(context.getResources().getString(R.string.completedStatus))) {
            holder.jobStatus.setTextColor(context.getResources().getColor(R.color.green));
        }

        holder.layoutBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (additionalServices.get(position).getStatus().equals(context.getResources().getString(R.string.ongoingStatus))) {

                Intent intent = new Intent(context, BidPlace.class);
                intent.putExtra("bid", additionalServices.get(position).getStatus());
                intent.putExtra("jobId", additionalServices.get(position).getId());
                context.startActivity(intent);
//                }
             /*   if (additionalServices.get(position).getStatus().equals(context.getResources().getString(R.string.amountpendingStatus))) {
                    Intent intent = new Intent(context, BidPlace.class);
                    intent.putExtra("jobId", additionalServices.get(position).getId());
                    context.startActivity(intent);
                }
*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return additionalServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView skillName, skillDescription, paymentType, startDate, agoTiming, jobStatus;
        LinearLayout layoutBid;
        CardView bidCard;

        public ViewHolder(View itemView) {
            super(itemView);

            skillName = (TextView) itemView.findViewById(R.id.skillName);
            skillDescription = (TextView) itemView.findViewById(R.id.skillDescription);
            paymentType = (TextView) itemView.findViewById(R.id.paymentType);
            startDate = (TextView) itemView.findViewById(R.id.startDate);
            agoTiming = (TextView) itemView.findViewById(R.id.agoTiming);
            jobStatus = (TextView) itemView.findViewById(R.id.jobStatus);
            layoutBid = (LinearLayout) itemView.findViewById(R.id.layoutBid);
            bidCard = (CardView) itemView.findViewById(R.id.bidCard);


        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    additionalServices = services_filter;
                } else {
                    ArrayList<JobGetterSetter> filteredList = new ArrayList<>();
                    for (JobGetterSetter row : services_filter) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getJobTitle().toLowerCase().contains(charString.toLowerCase())
                        ) {
                            filteredList.add(row);
                        }
                    }

                    additionalServices = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = additionalServices;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                additionalServices = (List<JobGetterSetter>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}