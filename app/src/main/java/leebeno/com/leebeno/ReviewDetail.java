package leebeno.com.leebeno;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Api.WebUrls;

import static leebeno.com.leebeno.Services.Utility.getDateFromUtc;

public class ReviewDetail extends AppCompatActivity {


    @Bind(R.id.backSignUp)
    ImageView backSignUp;
    @Bind(R.id.reviewerPic)
    ImageView reviewerPic;
    @Bind(R.id.reviewerName)
    TextView reviewerName;
    @Bind(R.id.reviewerRatingBar)
    RatingBar reviewerRatingBar;
    @Bind(R.id.postedDate)
    TextView postedDate;
    @Bind(R.id.reviewerDescription)
    TextView reviewerDescription;
    String reviewerNamestr, reviewerDescriptionstr, reviewerPicstr, postedOnstr, ratingUserstr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);
        ButterKnife.bind(this);

        reviewerNamestr = getIntent().getStringExtra("reviewerName");
        reviewerDescriptionstr = getIntent().getStringExtra("reviewerDescription");
        reviewerPicstr = getIntent().getStringExtra("reviewerPic");
        postedOnstr = getIntent().getStringExtra("postedOn");
        ratingUserstr = getIntent().getStringExtra("ratingUser");

        Picasso.get()
                .load(reviewerPicstr)
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(reviewerPic);
        reviewerName.setText(reviewerNamestr);
        reviewerDescription.setText(reviewerDescriptionstr);
        postedDate.setText("Posted On  " + getDateFromUtc(postedOnstr));
        reviewerRatingBar.setRating(Float.parseFloat(ratingUserstr));

        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
