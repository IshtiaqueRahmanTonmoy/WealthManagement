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
public class DatewiseAdapter extends RecyclerView.Adapter<DatewiseAdapter.MyViewHolder>{

    private List<Transaction> transactionList;

    public DatewiseAdapter(List<Transaction> transactionList){
        this.transactionList = transactionList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.datecustom_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.headerdate.setText(transaction.getDate());
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
        public TextView category, date, price,description,headerdate;

        public MyViewHolder(View view) {
            super(view);
            headerdate = (TextView) view.findViewById(R.id.dateTxt);
            category = (TextView) view.findViewById(R.id.category);
            date = (TextView) view.findViewById(R.id.date);
            price = (TextView) view.findViewById(R.id.price);
            description = (TextView) view.findViewById(R.id.description);
        }
    }

}