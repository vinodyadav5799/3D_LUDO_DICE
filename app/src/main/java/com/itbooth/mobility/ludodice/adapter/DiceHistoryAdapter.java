package com.itbooth.mobility.ludodice.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.itbooth.mobility.ludodice.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DiceHistoryAdapter extends RecyclerView.Adapter<DiceHistoryAdapter.ViewHolder> {
    private ArrayList<String> data;
    private Context context;
    public DiceHistoryAdapter (Context context, ArrayList<String> data){
        this.data = data;
        this.context = context;
    }

    @Override
    public DiceHistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.dice_history_item, parent, false);
        return new ViewHolder(rowItem);
    }

    @Override
    public void onBindViewHolder(DiceHistoryAdapter.ViewHolder holder, int position) {
        holder.textView.setText(this.data.get(position));

        if(position == data.size()-1){
            holder.cardView.setCardBackgroundColor(this.context.getResources().getColor(R.color.light_blue));
        } else {
            holder.cardView.setCardBackgroundColor(this.context.getResources().getColor(R.color.yellow));
        }

    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private CardView cardView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.textView = view.findViewById(R.id.pointTV);
            this.cardView = view.findViewById(R.id.cardViewWrapper);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "position : " + getLayoutPosition() + " text : " + this.textView.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}