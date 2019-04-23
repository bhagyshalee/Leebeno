package leebeno.com.leebeno.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import leebeno.com.leebeno.AdminPart.CreateNewLabour;

import leebeno.com.leebeno.AdminPart.HomeAdminActivity;
import leebeno.com.leebeno.CustomerPart.HomeCustomerActivity;
import leebeno.com.leebeno.LabourPart.HomeLabourActivity;
import leebeno.com.leebeno.LabourPart.UpdateProfileLabour;
import leebeno.com.leebeno.Common.MySpinner;
import leebeno.com.leebeno.R;
import leebeno.com.leebeno.Signup;


public class AddSkillAdapter extends RecyclerView.Adapter<AddSkillAdapter.ViewHolder> {
    private  List<String> additionalServices = null;
    private ArrayList<String> additionalServicesId = null;
    RecyclerView recyclerView;
    private Context context;
//    AdapterView<?> adapterView;
    MySpinner sp;

    public AddSkillAdapter(ArrayList<String>additionalServicesId, List<String> services, Context context, RecyclerView recyclerView, MySpinner sp) {
        super();
        this.additionalServices = services;
//        this.adapterView =adapterView;
        this.additionalServicesId = additionalServicesId;
        this.context = context;
        this.recyclerView = recyclerView;
        this.sp=sp;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.skills_design, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.skillText.setText(additionalServices.get(position));
        holder.crossImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UpdateProfileLabour.labour!=null&&UpdateProfileLabour.labour.compareTo("up")==0){
                    if (UpdateProfileLabour.edit){
                        additionalServicesId.remove(position);
                        additionalServices.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, additionalServices.size());
                        notifyItemRangeChanged(position,additionalServicesId.size());
                        recyclerView.setVisibility(View.VISIBLE);

                        for (int i = 0;i<UpdateProfileLabour.listSkillsName.size();i++){
                            if (additionalServices.size()!=0){
                                if (additionalServices.get(additionalServices.size()-1).compareTo(UpdateProfileLabour.listSkillsName.get(i))==0) {
                                    Signup.cl=true;
                                    sp.setSelection(i);

                                }
                            }else {
                                Signup.cl=true;
                                sp.setSelection(position);
                            }
                        }
                        if (additionalServices.size()==0){
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                }else if(CreateNewLabour.createlabour!=null&&CreateNewLabour.createlabour.compareTo("createLab")==0){
                    additionalServicesId.remove(position);
                    additionalServices.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, additionalServices.size());
                    notifyItemRangeChanged(position,additionalServicesId.size());
                    recyclerView.setVisibility(View.VISIBLE);

                    for (int i = 0;i<CreateNewLabour.listSkillsName.size();i++){
                        if (additionalServices.size()!=0){
                            if (additionalServices.get(additionalServices.size()-1).compareTo(CreateNewLabour.listSkillsName.get(i))==0) {
                                Signup.cl=true;
                                sp.setSelection(i);
                            }
                        }else {
                            Signup.cl=true;
                            sp.setSelection(position);
                        }
                    }
                    if (additionalServices.size()==0){
                        recyclerView.setVisibility(View.GONE);
                    }
                }else if(HomeLabourActivity.labourSkillHome!=null&&HomeLabourActivity.labourSkillHome.compareTo("SkillLabour")==0){
                    additionalServicesId.remove(position);
                    additionalServices.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, additionalServices.size());
                    notifyItemRangeChanged(position,additionalServicesId.size());
                    recyclerView.setVisibility(View.VISIBLE);

                    for (int i = 0;i<HomeLabourActivity.listSkillsName.size();i++){
                        if (additionalServices.size()!=0){
                            if (additionalServices.get(additionalServices.size()-1).compareTo(HomeLabourActivity.listSkillsName.get(i))==0) {
                                Signup.cl=true;
                                sp.setSelection(i);
                            }
                        }else {
                            Signup.cl=true;
                            sp.setSelection(position);
                        }
                    }
                    if (additionalServices.size()==0){
                        recyclerView.setVisibility(View.GONE);
                    }
                }else if(HomeAdminActivity.adminSkillHome!=null&&HomeAdminActivity.adminSkillHome.compareTo("SkillAdmin")==0){
                    additionalServicesId.remove(position);
                    additionalServices.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, additionalServices.size());
                    notifyItemRangeChanged(position,additionalServicesId.size());
                    recyclerView.setVisibility(View.VISIBLE);

                    for (int i = 0;i<HomeAdminActivity.listSkillsName.size();i++){
                        if (additionalServices.size()!=0){
                            if (additionalServices.get(additionalServices.size()-1).compareTo(HomeAdminActivity.listSkillsName.get(i))==0) {
                                Signup.cl=true;
                                sp.setSelection(i);
                            }
                        }else {
                            Signup.cl=true;
                            sp.setSelection(position);
                        }
                    }
                    if (additionalServices.size()==0){
                        recyclerView.setVisibility(View.GONE);
                    }
                }else if(HomeCustomerActivity.customerSkillHome!=null&&HomeCustomerActivity.customerSkillHome.compareTo("SkillCustomer")==0){
                    additionalServicesId.remove(position);
                    additionalServices.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, additionalServices.size());
                    notifyItemRangeChanged(position,additionalServicesId.size());
                    recyclerView.setVisibility(View.VISIBLE);

                    for (int i = 0;i<HomeCustomerActivity.listSkillsName.size();i++){
                        if (additionalServices.size()!=0){
                            if (additionalServices.get(additionalServices.size()-1).compareTo(HomeCustomerActivity.listSkillsName.get(i))==0) {
                                Signup.cl=true;
                                sp.setSelection(i);
                            }
                        }else {
                            Signup.cl=true;
                            sp.setSelection(position);
                        }
                    }
                    if (additionalServices.size()==0){
                        recyclerView.setVisibility(View.GONE);
                    }
                }else {
                    additionalServicesId.remove(position);
                    additionalServices.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, additionalServices.size());
                    notifyItemRangeChanged(position,additionalServicesId.size());
                    recyclerView.setVisibility(View.VISIBLE);

                    for (int i = 0;i<Signup.listSkillsName.size();i++){
                        if (additionalServices.size()!=0){
                            if (additionalServices.get(additionalServices.size()-1).compareTo(Signup.listSkillsName.get(i))==0) {
                                Signup.cl=true;
                                sp.setSelection(i);
                            }
                        }else {
                            Signup.cl=true;
                            sp.setSelection(position);
                        }
                    }
                    if (additionalServices.size()==0){
                        recyclerView.setVisibility(View.GONE);
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

        TextView skillText;
        ImageView crossImg;

        public ViewHolder(View itemView) {
            super(itemView);

            skillText = (TextView) itemView.findViewById(R.id.skillText);
            crossImg = (ImageView) itemView.findViewById(R.id.crossImg);

        }
    }
}