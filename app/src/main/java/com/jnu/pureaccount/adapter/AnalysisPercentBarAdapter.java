package com.jnu.pureaccount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jnu.pureaccount.R;
import com.jnu.pureaccount.data.PercentBarItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnalysisPercentBarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<PercentBarItem> adpList;
    private Context context;

    public AnalysisPercentBarAdapter(Context context, ArrayList<PercentBarItem> list){
        this.context = context;
        this.adpList = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_percent_bar,parent,false);
        return new BarHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        BarHolder barHolder = (BarHolder) holder;
        PercentBarItem item = adpList.get(position);
        barHolder.name.setText(item.getName());
        barHolder.account.setText(String.format("%.2f",item.getAccount()));
        barHolder.progressBar.setProgress(item.getPercent());
        barHolder.icon.setBackground(context.getResources().getDrawable(item.getIcon()));
    }

    class BarHolder extends RecyclerView.ViewHolder{
        TextView account;
        TextView name;
        ImageView icon;
        ProgressBar progressBar;
        public BarHolder(@NonNull View itemView) {
            super(itemView);
            account = itemView.findViewById(R.id.total_account);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.reason_name);
            progressBar = itemView.findViewById(R.id.percent_bar);
        }
    }

    @Override
    public int getItemCount() {
        return adpList.size();
    }
}
