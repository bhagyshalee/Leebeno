package leebeno.com.leebeno.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;import android.widget.TextView;
import android.widget.LinearLayout;


import java.util.List;

import leebeno.com.leebeno.BidPlace;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.CustomerJobDetail;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;

import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;


public class EscalatedJobsAdapter extends RecyclerView.Adapter<EscalatedJobsAdapter.ViewHolder> {
    private List<JobGetterSetter> additionalServices = null;
    private Context context;


    public EscalatedJobsAdapter(List<JobGetterSetter> services, Context context) {
        super();
        this.additionalServices = services;
        this.context = context;
    }

    @Override
    public EscalatedJobsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_escalated_jobs, parent, false);
        EscalatedJobsAdapter.ViewHolder viewHolder = new EscalatedJobsAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final EscalatedJobsAdapter.ViewHolder holder, final int position) {

        holder.titleEscalation.setText(additionalServices.get(position).getJobTitle());
        holder.priceEscalation.setText("$ "+additionalServices.get(position).getAmount());
        holder.reasonEscalation.setText("Reason: "+additionalServices.get(position).getReasonEscalation());
        holder.descriptionEscalation.setText(additionalServices.get(position).getDescription());
        holder.deductMoney.setText("$ 50 Deduct as commission");
        holder.creditOnDate.setText("Escalated On "+ getDateFromUtc(additionalServices.get(position).getCreatedJob()));
        holder.escalatedJobLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPrefManager.getUserStatus(context, Constrants.UserStatus).compareTo("customer")==0) {
                    Intent intent = new Intent(context, CustomerJobDetail.class);
                    intent.putExtra("jobStatus", "escalated");
                    intent.putExtra("jobId", additionalServices.get(position).getId());
                    context.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(context, BidPlace.class);
                    intent.putExtra("jobStatus", "escalated");
                    intent.putExtra("jobId", additionalServices.get(position).getId());
                    context.startActivity(intent);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return additionalServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleEscalation, priceEscalation, reasonEscalation, descriptionEscalation, deductMoney, creditOnDate;
        LinearLayout escalatedJobLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            titleEscalation = (TextView) itemView.findViewById(R.id.titleEscalation);
            priceEscalation = (TextView) itemView.findViewById(R.id.priceEscalation);
            reasonEscalation = (TextView) itemView.findViewById(R.id.reasonEscalation);
            descriptionEscalation = (TextView) itemView.findViewById(R.id.descriptionEscalation);
            deductMoney = (TextView) itemView.findViewById(R.id.deductMoney);
            creditOnDate = (TextView) itemView.findViewById(R.id.creditOnDate);
            escalatedJobLayout = (LinearLayout) itemView.findViewById(R.id.escalatedJobLayout);


        }
    }
}