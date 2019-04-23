package leebeno.com.leebeno;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import leebeno.com.leebeno.Api.WebUrls;

public class ShowImageActivity extends AppCompatActivity {

    @Bind(R.id.imageShow)
    ImageView imageShow;
    @Bind(R.id.backSignUp)
    ImageView backSignUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ButterKnife.bind(this);

        String getImage=getIntent().getStringExtra("imagebitmap");
//        ByteA[] extras = getIntent().getByteArrayExtra("imagebitmap");
//        Bitmap bmp = (Bitmap) extras.getByteArray("imagebitmap");
       /* byte[] byteArray = getIntent().getByteArrayExtra("imagebitmap");
        Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        imageShow.setImageBitmap(bmp);*/

       if (getImage.equals(""))
       {
           imageShow.setImageDrawable(getResources().getDrawable(R.drawable.no_image));
       }else
       {
           Picasso.get()
                   .load(getImage)
                   .placeholder(R.drawable.no_image)
                   .error(R.drawable.no_image)
                   .into(imageShow);

       }

        backSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

/*
        Picasso.get()
                .load()
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(imageShow);
*/


    }
}
