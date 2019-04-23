package leebeno.com.leebeno.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import leebeno.com.leebeno.R;


public class ImagesCrossAdapter extends RecyclerView.Adapter<ImagesCrossAdapter.ViewHolder> {
    private   List<Bitmap> additionalServices = null;
    private   List<File> fileList = null;
    private Context context;
    private RecyclerView recyclerView;


    public ImagesCrossAdapter(List<Bitmap> services, List<File> fileList, Context context, RecyclerView recyclerView) {
        super();
        this.additionalServices = services;
        this.context = context;
        this.fileList = fileList;
        this.recyclerView = recyclerView;
    }

    @Override
    public ImagesCrossAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_id_images, parent, false);
        ImagesCrossAdapter.ViewHolder viewHolder = new ImagesCrossAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ImagesCrossAdapter.ViewHolder holder, final int position) {

        holder.imageIdProof.setImageBitmap(additionalServices.get(position));
        holder.closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (additionalServices.size() == 1) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }*/
                additionalServices.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, additionalServices.size());
                fileList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, fileList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return additionalServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageIdProof,closeImg;

        public ViewHolder(View itemView) {
            super(itemView);

            imageIdProof = (ImageView) itemView.findViewById(R.id.imageIdProof);
            closeImg = (ImageView) itemView.findViewById(R.id.closeImg);


        }
    }
}