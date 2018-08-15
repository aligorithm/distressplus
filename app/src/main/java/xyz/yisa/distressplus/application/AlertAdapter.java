package xyz.yisa.distressplus.application;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.yisa.distressplus.R;
import xyz.yisa.distressplus.models.Alert;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder>  {

    private ArrayList<Alert> alerts;
    private final Context context;
    private OnItemClicked onClick;

    public AlertAdapter(Context context) {
        alerts = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public AlertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.alert_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AlertAdapter.ViewHolder holder, int position) {
        Alert alert = getItem(position);
        holder.name.setText(alert.name);
        holder.location.setText(alert.phone);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    public Alert getItem(int position){
        return alerts.get(position);
    }
    @Override
    public int getItemCount() {
        return alerts.size();
    }
    public void clear(){
        alerts.clear();
        notifyDataSetChanged();
    }
    public void addItem(Alert alert){
        alerts.add(alert);
        notifyItemInserted(alerts.size() - 1);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        View container;
        TextView name;
        TextView location;
        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.alert_container);
            name = itemView.findViewById(R.id.alert_name);
            location = itemView.findViewById(R.id.alert_location);
        }
    }
    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }
    public interface OnItemClicked {
        void onItemClick(int position);
    }
}
