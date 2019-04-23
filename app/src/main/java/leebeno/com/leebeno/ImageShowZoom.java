package leebeno.com.leebeno;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;
import leebeno.com.leebeno.Api.WebUrls;

public class ImageShowZoom extends AppCompatActivity {

    @Bind(R.id.zoomImageView)
    ImageViewTouch zoomImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_show_zoom);
        ButterKnife.bind(this);

        Matrix matrix = zoomImageView.getDisplayMatrix();

        zoomImageView.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);



        Bundle extras = getIntent().getExtras();
        Bitmap bmp = (Bitmap) extras.getParcelable("imagebitmap");
        zoomImageView.setImageBitmap(bmp);
/*
        Picasso.get()
                .load(String.valueOf(bmp))
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .resize(400,600)
                .into(zoomImageView);
*/

//        Intent intent=getIntent();
//        Bitmap bitmap = (Bitmap) getIntent().getParcelableExtra("BitmapImage");
      /*  ImageViewZoomConfig imageViewZoomConfig=new ImageViewZoomConfig();
        imageViewZoomConfig.saveProperty(true);*/
//
    }
}
