package leebeno.com.leebeno.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import leebeno.com.leebeno.R;


public class BankHistoryAdapter extends RecyclerView.Adapter<BankHistoryAdapter.ViewHolder> {
    private  List<String> additionalServicesTitle = null;
    private  List<String> additionalServicesPrice = null;
    private  List<String> additionalServicesTranId = null;
    private  List<String> additionalServicesDebitFrom = null;
    private  List<String> additionalServicesDebitTo = null;
    private Context context;


    public BankHistoryAdapter(List<String> additionalServicesTitle, Context context, List<String> additionalServicesPrice , List<String> additionalServicesTranId
            , List<String> additionalServicesDebitFrom , List<String> additionalServicesDebitTo ) {
        super();
        this.additionalServicesTitle = additionalServicesTitle;
        this.additionalServicesPrice = additionalServicesPrice;
        this.additionalServicesTranId = additionalServicesTranId;
        this.additionalServicesDebitFrom = additionalServicesDebitFrom;
        this.additionalServicesDebitTo = additionalServicesDebitTo;
        this.context = context;
    }

    @Override
    public BankHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_bank_history, parent, false);
        BankHistoryAdapter.ViewHolder viewHolder = new BankHistoryAdapter.ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final BankHistoryAdapter.ViewHolder holder, final int position) {

        holder.titleTransaction.setText(additionalServicesTitle.get(position));
        holder.priceTransaction.setText(additionalServicesPrice.get(position));
        holder.transactionId.setText("Transaction Id : "+additionalServicesTranId.get(position));
        holder.debitFrom.setText(additionalServicesDebitFrom.get(position));
        holder.debitOnDate.setText("Debit On "+additionalServicesDebitTo.get(position));


    }

    @Override
    public int getItemCount() {
        return additionalServicesTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTransaction,priceTransaction,transactionId,debitFrom,debitOnDate;

        public ViewHolder(View itemView) {
            super(itemView);

            titleTransaction = (TextView) itemView.findViewById(R.id.titleTransaction);
            priceTransaction = (TextView) itemView.findViewById(R.id.priceTransaction);
            transactionId = (TextView) itemView.findViewById(R.id.transactionId);
            debitFrom = (TextView) itemView.findViewById(R.id.debitFrom);
            debitOnDate = (TextView) itemView.findViewById(R.id.debitOnDate);



        }
    }
}