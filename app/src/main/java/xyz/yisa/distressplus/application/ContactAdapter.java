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
import xyz.yisa.distressplus.models.User;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private ArrayList<User> contacts;
    private final Context context;
    private OnItemClicked onClick;

    public ContactAdapter(Context context) {
        contacts = new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.contact_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        User contact = getItem(position);
        holder.name.setText(contact.name);
        holder.phone.setText(contact.phone);
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClick.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public void addItem(User contact){
        contacts.add(contact);
        notifyItemInserted(contacts.size() - 1);
    }
    public User getItem(int position){
        return contacts.get(position);
    }
    public void clear(){
        contacts.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View container;
        TextView name;
        TextView phone;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contact_name);
            phone = itemView.findViewById(R.id.contact_phone);
            container = itemView.findViewById(R.id.contact_container);
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
