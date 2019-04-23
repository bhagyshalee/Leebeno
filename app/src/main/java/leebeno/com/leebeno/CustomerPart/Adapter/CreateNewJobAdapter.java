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
import java.util.Date;
import java.util.List;

import leebeno.com.leebeno.CustomerPart.CustomerJobDetail;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Services.Utility;

import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;
import static leebeno.com.leebeno.Services.Utility.getTimeFromUtc;
import static leebeno.com.leebeno.Services.Utility.inputFormat;

public class CreateNewJobAdapter extends RecyclerView.Adapter<CreateNewJobAdapter.ViewHolder> implements Filterable{
    private List<JobGetterSetter> getAllJobs = null;
    private  List<JobGetterSetter> services_filter;
    private Context context;


    public CreateNewJobAdapter(List<JobGetterSetter> getAllJobs, Context context) {
        super();
        this.getAllJobs = getAllJobs;
        this.context = context;
        this.services_filter = getAllJobs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_created_job, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.jobTitle.setText(getAllJobs.get(position).getJobTitle());
        holder.jobDescription.setText(getAllJobs.get(position).getDescription());

        Date d = null;
        try {
            d = inputFormat.parse("" + getAllJobs.get(position).getCreatedJob());
        } catch (ParseException ex) {
            Log.e("neerbbgbt", "" + ex);
        }


        Log.e("getTime ",""+getTimeFromUtc(getAllJobs.get(position).getCreatedJob()));

        holder.setPostTime.setText("Posted On : " + Utility.printDifference(getAllJobs.get(position).getCreatedJob()));


        Log.e("neerbbgbt", "" + getAllJobs.get(position).getAmount());
        if (getAllJobs.get(position).getPaymentType().compareTo(context.getResources().getString(R.string.range)) == 0) {
            holder.jobCost.setText("$ " + getAllJobs.get(position).getMin() + " - " + "$ "+getAllJobs.get(position).getMax());
        } else {
            holder.jobCost.setText("$ " + getAllJobs.get(position).getAmount());
        }

        holder.jobBids.setText("No of Bids : " + getAllJobs.get(position).getBidCount());
        holder.jobStartDate.setText("Start Date : " + getDateFromUtc(getAllJobs.get(position).getStartDate()));

        holder.layoutBid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CustomerJobDetail.class);
                intent.putExtra("jobStatus", "pending");
                intent.putExtra("jobId", getAllJobs.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return getAllJobs.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView jobTitle, jobDescription, setPostTime, jobCost, jobBids, jobStartDate;

        LinearLayout layoutBid;


        public ViewHolder(View itemView) {
            super(itemView);

            jobTitle = (TextView) itemView.findViewById(R.id.jobTitle);
            jobDescription = (TextView) itemView.findViewById(R.id.jobDescription);
            setPostTime = (TextView) itemView.findViewById(R.id.setPostTime);
            jobCost = (TextView) itemView.findViewById(R.id.jobCost);
            jobBids = (TextView) itemView.findViewById(R.id.jobBids);
            jobStartDate = (TextView) itemView.findViewById(R.id.jobStartDate);
            layoutBid = (LinearLayout) itemView.findViewById(R.id.layoutBid);


        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    getAllJobs = services_filter;
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

                    getAllJobs = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = getAllJobs;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                getAllJobs = (List<JobGetterSetter>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}