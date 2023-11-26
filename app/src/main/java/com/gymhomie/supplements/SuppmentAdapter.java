package com.gymhomie.supplements;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gymhomie.R;

public class SuppmentAdapter extends FirestoreRecyclerAdapter<SupplementPost, SuppmentAdapter.SupplementViewHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    public SuppmentAdapter(@NonNull FirestoreRecyclerOptions<SupplementPost> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull SupplementViewHolder holder, int position, @NonNull SupplementPost supplement) {

        holder.nameTV.setText(supplement.name);
        holder.desTV.setText(supplement.des);
        holder.usernameTV.setText(supplement.username);
        holder.timestampTV.setText(Utility.timestamptoStr(supplement.timestamp));

    }

    @NonNull
    @Override
    public SupplementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_supplement,parent,false);
        return new SupplementViewHolder(view);
    }

    class SupplementViewHolder extends RecyclerView.ViewHolder{

        TextView nameTV, desTV, usernameTV, timestampTV;

        public SupplementViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.supplement_nameID);
            desTV = itemView.findViewById(R.id.supplement_desID);
            usernameTV = itemView.findViewById(R.id.usernameID);
            timestampTV = itemView.findViewById(R.id.timeID);
        }
    }
}
