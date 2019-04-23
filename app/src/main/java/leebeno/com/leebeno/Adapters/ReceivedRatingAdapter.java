package leebeno.com.leebeno.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.squareup.picasso.Picasso;

import java.util.List;

import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.BidPlace;
import leebeno.com.leebeno.Common.Constrants;
import leebeno.com.leebeno.Common.SharedPrefManager;
import leebeno.com.leebeno.CustomerPart.CustomerJobDetail;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.ReceivedRatings;
import leebeno.com.leebeno.Services.Utility;

import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;


public class ReceivedRatingAdapter extends RecyclerView.Adapter<ReceivedRatingAdapter.ViewHolder> {
    private List<JobGetterSetter> additionalServices = null;
    private Context context;


    public ReceivedRatingAdapter(List<JobGetterSetter> services, Context context) {
        super();
        this.additionalServices = services;
        this.context = context;
    }

    @Override
    public ReceivedRatingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_received_request, parent, false);
        ReceivedRatingAdapter.ViewHolder viewHolder = new ReceivedRatingAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ReceivedRatingAdapter.ViewHolder holder, final int position) {

//        holder.ratingTitle.setText(additionalServices.get(position));
        holder.pointReward.setVisibility(View.VISIBLE);
        if (additionalServices.get(position).getJobTitle() != null) {
            holder.ratingTitle.setText(additionalServices.get(position).getJobTitle());
        }

//        holder.description.setText(additionalServices.get(position).getDescription());
        holder.agoTime.setText("Posted On: " + getDateFromUtc(additionalServices.get(position).getCreatedJob()));

        if (additionalServices.get(position).getRewardPoint()!=null)
        {
            holder.pointReward.setText("" + additionalServices.get(position).getRewardPoint());
        }
        if (additionalServices.get(position).getJobRating() != null) {
            holder.ratingBar.setRating(Float.parseFloat(additionalServices.get(position).getJobRating()));
        }
        if (additionalServices.get(position).getReviewComment() != null) {
            holder.description.setText(additionalServices.get(position).getReviewComment());
        }

        holder.layoutRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (additionalServices.get(position).getJOBID()!=null)
                {
                    if (SharedPrefManager.getUserStatus(context, Constrants.UserStatus).compareTo("customer") == 0) {
                        Intent intent = new Intent(context, CustomerJobDetail.class);
                        intent.putExtra("jobId", additionalServices.get(position).getJOBID());
                        context.startActivity(intent);
                    }else
                    {
                        Intent intent = new Intent(context, BidPlace.class);
                        intent.putExtra("jobId", additionalServices.get(position).getJOBID());
                        context.startActivity(intent);
                    }
                }

            }
        });
/*
        Picasso.get()
                .load(WebUrls.BASE_URL + additionalServices.get(position).getPofilePic())
                .placeholder(R.drawable.user)
                .error(R.drawable.user)
                .into(holder.imgReviewProfile);
*/

    }

    @Override
    public int getItemCount() {
        return additionalServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView ratingTitle, agoTime, description, pointReward;
        RatingBar ratingBar;
        LinearLayout layoutRating;

        public ViewHolder(View itemView) {
            super(itemView);

            ratingTitle = (TextView) itemView.findViewById(R.id.ratingTitle);
            agoTime = (TextView) itemView.findViewById(R.id.agoTime);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            description = (TextView) itemView.findViewById(R.id.description);
            pointReward = (TextView) itemView.findViewById(R.id.pointReward);
            layoutRating = (LinearLayout) itemView.findViewById(R.id.layoutRating);

        }
    }
}