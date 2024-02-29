package com.example.tradingview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;

// on below line we are creating our adapter class
// in this class we are passing our array list
// and our View Holder class which we have created.
public class CurrencyRVAdapter extends RecyclerView.Adapter<CurrencyRVAdapter.CurrencyViewholder> {
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private ArrayList<CurrencyModal> currencyModals;
    private Context context;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public CurrencyRVAdapter(ArrayList<CurrencyModal> currencyModals, Context context, OnItemClickListener listener) {
        this.currencyModals = currencyModals;
        this.context = context;
        this.listener = listener;
    }

    // below is the method to filter our list.
    public void filterList(ArrayList<CurrencyModal> filterlist) {
        // adding filtered list to our
        // array list and notifying data set changed
        currencyModals = filterlist;
        notifyDataSetChanged();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrencyRVAdapter.CurrencyViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        View view = LayoutInflater.from(context).inflate(R.layout.currency_rv_item, parent, false);
        return new CurrencyViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyRVAdapter.CurrencyViewholder holder, int position) {
        // on below line we are setting data to our item of
        // recycler view and all its views.
        CurrencyModal modal = currencyModals.get(position);
        holder.nameTV.setText(modal.getName());
        holder.rateTV.setText(modal.getPrice() == -1.0 ? " " : "$ " + df2.format(modal.getPrice()));
        holder.symbolTV.setText(modal.getSymbol());

        holder.percentageTV.setTextColor(modal.getPercent_change_24h() < 0 ?
                ContextCompat.getColor(holder.itemView.getContext(), R.color.red) :
                ContextCompat.getColor(holder.itemView.getContext(), R.color.green));
        holder.percentageTV.setText(modal.getPrice() == -1.0 ? " " : (modal.getPercent_change_24h_str() + "%").replace('-', ' '));
//        holder.percentageTV.setText((modal.getPercent_change_24h_str() + "%").replace('-', ' '));
    }

    @Override
    public int getItemCount() {
        // on below line we are returning
        // the size of our array list.
        return currencyModals.size();
    }

    // on below line we are creating our view holder class
    // which will be used to initialize each view of our layout file.
    public class CurrencyViewholder extends RecyclerView.ViewHolder {
        private TextView symbolTV, rateTV, nameTV, percentageTV;

        public CurrencyViewholder(@NonNull View itemView) {
            super(itemView);
            symbolTV = itemView.findViewById(R.id.idTVSymbol);
            rateTV = itemView.findViewById(R.id.idTVRate);
            nameTV = itemView.findViewById(R.id.idTVName);
            percentageTV = itemView.findViewById(R.id.idTVPercentage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null && getAdapterPosition() != RecyclerView.NO_POSITION){
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
