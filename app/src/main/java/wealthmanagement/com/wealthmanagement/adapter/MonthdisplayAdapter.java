package wealthmanagement.com.wealthmanagement.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wealthmanagement.com.wealthmanagement.R;
import wealthmanagement.com.wealthmanagment.Transaction;

/**
 * Created by TONMOYPC on 10/31/2017.
 */
public class MonthdisplayAdapter extends RecyclerView.Adapter<MonthdisplayAdapter.MyViewHolder>{

    private List<Transaction> transactionList;
    private String total;
    public MonthdisplayAdapter(List<Transaction> transactionList){
        this.transactionList = transactionList;

        for(int i=0 ;i<transactionList.size(); i++){
            //double value += transactionList.get(i).getPrice();
        }
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.paymentmode_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.headermode.setText(transaction.getPayment_method());
        holder.category.setText(transaction.getCategory());
        holder.date.setText(transaction.getDate());
        holder.price.setText(transaction.getPrice());
        holder.description.setText(transaction.getDescription());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView category, date, price,description,headermode;

        public MyViewHolder(View view) {
            super(view);
            headermode = (TextView) view.findViewById(R.id.cashTxt);
            category = (TextView) view.findViewById(R.id.category);
            date = (TextView) view.findViewById(R.id.date);
            price = (TextView) view.findViewById(R.id.price);
            description = (TextView) view.findViewById(R.id.description);
        }
    }

}