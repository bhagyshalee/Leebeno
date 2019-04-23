package leebeno.com.leebeno.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import leebeno.com.leebeno.Api.WebUrls;
import leebeno.com.leebeno.CustomerPart.JobGetterSetter;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.ReviewDetail;


public class ProfileReviewAdapter extends RecyclerView.Adapter<ProfileReviewAdapter.ViewHolder> {
    private List<JobGetterSetter> additionalServices = null;
    private Context context;


    public ProfileReviewAdapter(List<JobGetterSetter> services, Context context) {
        super();
        this.additionalServices = services;
        this.context = context;
    }

    @Override
    public ProfileReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_profile_review, parent, false);
        ProfileReviewAdapter.ViewHolder viewHolder = new ProfileReviewAdapter.ViewHolder(view);
        if (view.getLayoutParams().height == RecyclerView.LayoutParams.MATCH_PARENT)
            view.getLayoutParams().height = parent.getHeight();


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ProfileReviewAdapter.ViewHolder holder, final int position) {

        holder.reviewerName.setText(additionalServices.get(position).getApplierName());
        holder.reviewerDescription.setText(additionalServices.get(position).getReviewComment());
        Picasso.get()
                .load(WebUrls.BASE_URL + additionalServices.get(position).getPofilePic())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(holder.reviewerProfile);

        holder.layoutProfileReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReviewDetail.class);
                intent.putExtra("reviewerName",  holder.reviewerName.getText().toString());
                intent.putExtra("reviewerDescription",  holder.reviewerDescription.getText().toString());
                intent.putExtra("reviewerPic", WebUrls.BASE_URL + additionalServices.get(position).getPofilePic());
                intent.putExtra("postedOn", additionalServices.get(position).getCreatedJob());
                intent.putExtra("ratingUser", additionalServices.get(position).getJobRating());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return additionalServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView reviewerName, reviewerDescription;
        ImageView reviewerProfile;
        LinearLayout layoutProfileReview;

        public ViewHolder(View itemView) {
            super(itemView);

            reviewerName = (TextView) itemView.findViewById(R.id.reviewerName);
            reviewerDescription = (TextView) itemView.findViewById(R.id.reviewerDescription);
            layoutProfileReview = (LinearLayout) itemView.findViewById(R.id.layoutProfileReview);
            reviewerProfile = (ImageView) itemView.findViewById(R.id.reviewerProfile);


        }
    }
}