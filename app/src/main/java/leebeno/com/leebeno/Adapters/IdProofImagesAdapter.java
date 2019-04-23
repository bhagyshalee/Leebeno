package leebeno.com.leebeno.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import leebeno.com.leebeno.R;


public class IdProofImagesAdapter extends RecyclerView.Adapter<IdProofImagesAdapter.ViewHolder> {
    private   List<Bitmap> additionalServices = null;
    ArrayList<String> fileDoc;
    private Context context;
    private RecyclerView recyclerView;


    public IdProofImagesAdapter(ArrayList<String> fileDoc,List<Bitmap> services, Context context, RecyclerView recyclerView) {
        super();
        this.fileDoc= fileDoc;
        this.additionalServices = services;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_id_images, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.imageIdProof.setImageBitmap(additionalServices.get(position));

        holder.closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (additionalServices.size() == 1) {

                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }*/
                additionalServices.remove(position);
                fileDoc.remove(position);
                notifyItemRemoved(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, additionalServices.size());
                notifyItemRangeChanged(position, fileDoc.size());
                if (additionalServices.size()==0){
                    recyclerView.setVisibility(View.GONE);
                }
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