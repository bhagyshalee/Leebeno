package leebeno.com.leebeno.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import leebeno.com.leebeno.BidPlace;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.CustomerJobDetail;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Services.Utility;

import static leebeno.com.leebeno.Services.Utility.inputFormat;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    List<JobGetterSetter> services;
    private Context context;


    public NotificationAdapter(List<JobGetterSetter> services, Context context) {
        super();
        this.services = services;
        this.context = context;
    }

    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_design, parent, false);
        NotificationAdapter.ViewHolder viewHolder = new NotificationAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(NotificationAdapter.ViewHolder holder, final int position) {
//        getUserDetailId(context);

        Date d = null;
        try {
            d = inputFormat.parse(services.get(position).getCreatedJob());
        } catch (ParseException ex) {
            Logger.getLogger(NotificationAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }

        Log.e("njkvfnjklvfkl", d + "");
        holder.notiTitle.setText("" + services.get(position).getJobTitle());
        holder.notiBody.setText("" + services.get(position).getDescription());
        holder.notiHoursAgo.setText("" + Utility.printDifference(services.get(position).getCreatedJob()));
//        holder.notiDateTime.setText("" + Utility.printDifference(services.get(position).getCreatedJob()));
        holder.notiDateTime.setVisibility(View.GONE);

        holder.Layoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPrefManager.getUserStatus(context, Constrants.UserStatus).compareTo("labour") == 0 ||
                        SharedPrefManager.getUserStatus(context, Constrants.UserStatus).compareTo("groupAdmin") == 0) {
                    Intent intent = new Intent(context, BidPlace.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("jobId", services.get(position).getJOBID());
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }/* else if (SharedPrefManager.getUserStatus(context, Constrants.UserStatus).compareTo("groupAdmin") == 0) {
                    Intent intent = new Intent(context, BidPlace.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("jobId", services.get(position).getJOBID());
                    intent.putExtras(bundle);
                    context.startActivity(intent);

                }*/ else if (SharedPrefManager.getUserStatus(context, Constrants.UserStatus).compareTo("customer") == 0) {

                    Intent intent = new Intent(context, CustomerJobDetail.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("jobId", services.get(position).getJOBID());
                    intent.putExtra("noti", true);
                    intent.putExtras(bundle);
                    context.startActivity(intent);


                }

            }
        });
    }

    @Override
    public int getItemCount() {

        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView notiTitle, notiHoursAgo, notiBody, notiDateTime;
        LinearLayout Layoutt;

        public ViewHolder(View itemView) {
            super(itemView);

            notiHoursAgo = (TextView) itemView.findViewById(R.id.notiHoursAgo);
            notiBody = (TextView) itemView.findViewById(R.id.notiBody);
            notiTitle = (TextView) itemView.findViewById(R.id.notiTitle);
            notiDateTime = (TextView) itemView.findViewById(R.id.notiDateTime);
            Layoutt = (LinearLayout) itemView.findViewById(R.id.Layoutt);
        }
    }
}
