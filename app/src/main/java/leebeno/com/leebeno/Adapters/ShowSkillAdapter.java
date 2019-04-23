package leebeno.com.leebeno.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import leebeno.com.leebeno.R;


public class ShowSkillAdapter extends RecyclerView.Adapter<ShowSkillAdapter.ViewHolder> {
    private  List<String> additionalServices = null;
    RecyclerView recyclerView;
    private Context context;

    public ShowSkillAdapter(List<String> services, Context context, RecyclerView recyclerView) {
        super();
        this.additionalServices = services;
        this.context = context;
        this.recyclerView = recyclerView;
    }

    @Override
    public ShowSkillAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skills_design, parent, false);
        ShowSkillAdapter.ViewHolder viewHolder = new ShowSkillAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ShowSkillAdapter.ViewHolder holder, final int position) {

        holder.skillText.setText(additionalServices.get(position));
        holder.crossImg.setVisibility(View.GONE);
        holder.crossImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (additionalServices.size()==1)
                {
                    recyclerView.setVisibility(View.GONE);
                }
                else
                {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                additionalServices.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, additionalServices.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return additionalServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView skillText;
        ImageView crossImg;

        public ViewHolder(View itemView) {
            super(itemView);

            skillText = (TextView) itemView.findViewById(R.id.skillText);
            crossImg = (ImageView) itemView.findViewById(R.id.crossImg);


        }
    }
}