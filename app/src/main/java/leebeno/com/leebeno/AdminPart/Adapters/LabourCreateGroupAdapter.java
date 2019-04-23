package leebeno.com.leebeno.AdminPart.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
;import leebeno.com.leebeno.R;

import static leebeno.com.leebeno.Services.Utility.showToast;


public class LabourCreateGroupAdapter extends RecyclerView.Adapter<LabourCreateGroupAdapter.ViewHolder> {
    List<String> list;
    List<String> expertiseId;
    Button btnCreateGroup;
    private List<String> additionalServices = null;
    private Context context;

    public LabourCreateGroupAdapter(List<String> services, Context context, Button btnCreateGroup) {
        super();
        this.additionalServices = services;
        this.context = context;
        this.btnCreateGroup = btnCreateGroup;
    }

    @Override
    public LabourCreateGroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_labour_add, parent, false);
        LabourCreateGroupAdapter.ViewHolder viewHolder = new LabourCreateGroupAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final LabourCreateGroupAdapter.ViewHolder holder, final int position) {


        holder.LabourMemberName.setText(additionalServices.get(position));
        expertiseId = new ArrayList<>();
        holder.LabourCheckBoxSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.LabourCheckBoxSelect.isChecked()) {
                    expertiseId.add(additionalServices.get(position));
                    if (expertiseId.size() == 5) {
                        btnCreateGroup.setBackgroundColor(context.getResources().getColor(R.color.white));
                    } else if (expertiseId.size() > 5) {
                        showToast(context, "You have to pay for add more then 5 members");
                    } else {
                        btnCreateGroup.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                    }
                } else {
                    expertiseId.remove(additionalServices.get(position));
                    if (expertiseId.size() == 5) {
                        btnCreateGroup.setBackgroundColor(context.getResources().getColor(R.color.white));
                    } else if (expertiseId.size() > 5) {
                        showToast(context, "You have to pay for add more then 5 members");
                    } else {
                        btnCreateGroup.setBackgroundColor(context.getResources().getColor(R.color.lightGray));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return additionalServices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView LabourMemberName;
        CheckBox LabourCheckBoxSelect;
        ImageView LabourMemberImage;

        public ViewHolder(View itemView) {
            super(itemView);

            LabourMemberImage = (ImageView) itemView.findViewById(R.id.LabourMemberImage);
            LabourMemberName = (TextView) itemView.findViewById(R.id.LabourMemberName);
            LabourCheckBoxSelect = (CheckBox) itemView.findViewById(R.id.LabourCheckBoxSelect);


        }
    }
}