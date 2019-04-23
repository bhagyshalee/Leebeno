package leebeno.com.leebeno.CustomerPart.Adapter;

import android.content.Context;
import android.content.Intent;
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
import java.util.List;

import leebeno.com.leebeno.CustomerPart.CustomerJobDetail;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Services.Utility;

import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;


public class OngoingJobAdapter extends RecyclerView.Adapter<OngoingJobAdapter.ViewHolder> implements Filterable {
    private List<JobGetterSetter> additionalServices = null;
    private List<JobGetterSetter> services_filter;
    private Context context;


    public OngoingJobAdapter(List<JobGetterSetter> services, Context context) {
        super();
        this.additionalServices = services;
        this.services_filter = services;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_onging_job, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

      /*  Date d = null;
        try {
            d = inputFormat.parse("" + additionalServices.get(position).getCreatedJob());
        } catch (ParseException ex) {
            Log.e("neerbbgbt", "" + ex);
        }
*/

        holder.jobTitle.setText(additionalServices.get(position).getJobTitle());
        holder.jobDescription.setText(additionalServices.get(position).getDescription());
//        holder.setPostTime.setText(getDateDifference(d));

        holder.setPostTime.setText("Posted: " + Utility.printDifference(additionalServices.get(position).getCreatedJob()));
      /*  if (additionalServices.get(position).getBidAmount() != 0) {
            holder.jobCost.setText("$ " + additionalServices.get(position).getBidAmount());
        } else {
            holder.jobCost.setText("$ " + "0");
        }*/
        holder.jobCost.setText("$ " + additionalServices.get(position).getAmount());

        if (additionalServices.get(position).getApplierName() != null) {
            holder.jobBidBy.setText("Bid By : " + additionalServices.get(position).getApplierName());
        }

        holder.jobStartDate.setText("Start Date : " + getDateFromUtc(additionalServices.get(position).getStartDate()));
        holder.jobStatus.setText(additionalServices.get(position).getStatus());
        if (additionalServices.get(position).getStatus().compareTo(context.getResources().getString(R.string.ongoingStatus)) == 0) {
            holder.jobStatus.setTextColor(context.getResources().getColor(R.color.blue));
        } else if (additionalServices.get(position).getStatus().compareTo(context.getResources().getString(R.string.amountpendingStatus)) == 0) {
            holder.jobStatus.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        } else if (additionalServices.get(position).getStatus().compareTo(context.getResources().getString(R.string.satisfiedStatus)) == 0) {
            holder.jobStatus.setTextColor(context.getResources().getColor(R.color.pruple));
        }

        holder.layoutOngoing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CustomerJobDetail.class);
                intent.putExtra("jobStatus", "ongoing");
                intent.putExtra("jobId", additionalServices.get(position).getId());
                if (additionalServices.get(position).getStatus().compareTo(context.getResources().getString(R.string.amountpendingStatus)) == 0) {
                    intent.putExtra("noti", true);
                }
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return additionalServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView jobTitle, jobDescription, setPostTime, jobCost, jobBidBy, jobStartDate, jobStatus;
        LinearLayout layoutOngoing;

        public ViewHolder(View itemView) {
            super(itemView);

            jobTitle = (TextView) itemView.findViewById(R.id.jobTitle);
            jobDescription = (TextView) itemView.findViewById(R.id.jobDescription);
            setPostTime = (TextView) itemView.findViewById(R.id.setPostTime);
            jobCost = (TextView) itemView.findViewById(R.id.jobCost);
            jobBidBy = (TextView) itemView.findViewById(R.id.jobBidBy);
            jobStartDate = (TextView) itemView.findViewById(R.id.jobStartDate);
            jobStatus = (TextView) itemView.findViewById(R.id.jobStatus);
            layoutOngoing = (LinearLayout) itemView.findViewById(R.id.layoutOngoing);


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