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
 * Created by TONMOYPC on 10/30/2017.
 */
public class TransactionAdapter  extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder>{

    private List<Transaction> transactionList;

    TransactionAdapter(List<Transaction> transactionList){
        this.transactionList = transactionList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_user_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.category.setText(transaction.getCategory());
        holder.date.setText(transaction.getDate());
        holder.price.setText(transaction.getPrice());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView category, date, price;

        public MyViewHolder(View view) {
            super(view);
            category = (TextView) view.findViewById(R.id.categoryTxt);
            date = (TextView) view.findViewById(R.id.dateTxt);
            price = (TextView) view.findViewById(R.id.priceTxt);
        }
    }

}
