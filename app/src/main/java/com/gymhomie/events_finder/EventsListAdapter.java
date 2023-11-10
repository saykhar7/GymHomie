package com.gymhomie.events_finder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gymhomie.R;

import java.util.ArrayList;

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ViewHolder> {


    Context context;
    ArrayList<EventsList> eventsLists;




    public EventsListAdapter( Context context, ArrayList<EventsList> arrayList) {

        this.context = context;
        this.eventsLists = arrayList;

    }

    @NonNull
    @Override
    public EventsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rv_eventslistlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsListAdapter.ViewHolder holder, int position) {


        holder.bind(eventsLists.get(position));




    }

    @Override
    public int getItemCount() {
        return eventsLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView date, state, zipCode, city, name, link;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            date = itemView.findViewById(R.id.eventDateID);
            state = itemView.findViewById(R.id.eventStateID);
            zipCode = itemView.findViewById(R.id.eventZipcodeID);
            city = itemView.findViewById(R.id.eventCityID);
            name = itemView.findViewById(R.id.eventNameID);
            link = itemView.findViewById(R.id.eventlinkID);
        }

        public void bind(EventsList eventsList)
        {
            date.setText(eventsList.getDate());
            state.setText(eventsList.getState());
            zipCode.setText(eventsList.getZipCode());
            city.setText(eventsList.getCity());
            name.setText(eventsList.getName());
            link.setText(eventsList.getLink());
        }
    }
}
