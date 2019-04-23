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
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import leebeno.com.leebeno.CustomerPart.CustomerJobDetail;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Services.Utility;

import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;
import static leebeno.com.leebeno.Services.Utility.inputFormat;


public class CreateOldJobAdapter extends RecyclerView.Adapter<CreateOldJobAdapter.ViewHolder> implements Filterable {
    private List<JobGetterSetter> additionalServices = null;
    private List<JobGetterSetter> services_filter = null;
    private Context context;

    float setUserRating;

    public CreateOldJobAdapter(List<JobGetterSetter> services, Context context) {
        super();
        this.additionalServices = services;
        this.services_filter = services;
        this.context = context;
    }

    @Override
    public CreateOldJobAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_old_job, parent, false);
        CreateOldJobAdapter.ViewHolder viewHolder = new CreateOldJobAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final CreateOldJobAdapter.ViewHolder holder, final int position) {
        Date d = null;
        try {
            d = inputFormat.parse("" + additionalServices.get(position).getCreatedJob());
        } catch (ParseException ex) {
            Log.e("neerbbgbt", "" + ex);
        }

        holder.jobTitle.setText(additionalServices.get(position).getJobTitle());
        holder.jobDescription.setText(additionalServices.get(position).getDescription());
//        holder.setPostTime.setText(getDateDifference(d));

        holder.setPostTime.setText("Posted: " + Utility.printDifference(additionalServices.get(position).getCreatedJob()));
        holder.jobCompletedBy.setText("Completed By : " + additionalServices.get(position).getApplierName());
        holder.jobCompletedOn.setText("Completed On :" + getDateFromUtc(additionalServices.get(position).getCompletedDate()));
//        holder.jobCompletedCost.setText("$ " + additionalServices.get(position).getBidAmount());
        holder.jobCompletedCost.setText("$ " + additionalServices.get(position).getAmount());
        holder.ratingJob.setVisibility(View.VISIBLE);

        if (additionalServices.get(position).getJobRating() != null) {
            Log.e("fgjkdnjklvf", "" + additionalServices.get(position).getJobRating());
            setUserRating = Float.parseFloat(additionalServices.get(position).getJobRating());
            holder.ratingJob.setRating(setUserRating);
        }

        holder.layoutOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CustomerJobDetail.class);
                intent.putExtra("jobStatus", "completed");
                intent.putExtra("jobId", additionalServices.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return additionalServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView jobTitle, jobDescription, setPostTime, jobCompletedBy, jobCompletedOn, jobCompletedCost;
        RatingBar ratingJob;
        LinearLayout layoutOld;

        public ViewHolder(View itemView) {
            super(itemView);

            jobTitle = (TextView) itemView.findViewById(R.id.jobTitle);
            jobDescription = (TextView) itemView.findViewById(R.id.jobDescription);
            setPostTime = (TextView) itemView.findViewById(R.id.setPostTime);
            jobCompletedBy = (TextView) itemView.findViewById(R.id.jobCompletedBy);
            jobCompletedOn = (TextView) itemView.findViewById(R.id.jobCompletedOn);
            jobCompletedCost = (TextView) itemView.findViewById(R.id.jobCompletedCost);
            layoutOld = (LinearLayout) itemView.findViewById(R.id.layoutOld);
            ratingJob = (RatingBar) itemView.findViewById(R.id.ratingJob);


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