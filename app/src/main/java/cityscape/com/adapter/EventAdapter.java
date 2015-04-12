package cityscape.com.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cityscape.com.androidapp.R;
import cityscape.com.model.EventInfo;
import cityscape.com.library.ImageLoader;

/**
 * Created by Chirag on 29-03-2015.
 */
public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {


    private List<EventInfo> eventList;

    public EventAdapter(List<EventInfo> eventList) {
        this.eventList = eventList;
    }

    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.card_layout, viewGroup, false);
        return new EventViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(EventAdapter.EventViewHolder eventViewHolder, int i) {

        EventInfo event = eventList.get(i);
        eventViewHolder.eventName.setText(event.getEventName());
        eventViewHolder.eventCity.setText(event.getEventCity());
        eventViewHolder.eventDate.setText(new SimpleDateFormat("dd/MM/yyyy").format(event.getEventDate()));
        //eventViewHolder.image.setTag(event.getImage());
        new ImageLoader(eventViewHolder.image).execute("http://visionevents.co.uk/wp-content/uploads/2012/09/event-production-quirky1.jpg");

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        protected TextView eventName;
        protected TextView eventCity;
        protected TextView eventDate;
        protected ImageView image;

        public EventViewHolder(View itemView) {
            super(itemView);

            eventName =  (TextView) itemView.findViewById(R.id.eventName);
            eventCity = (TextView)  itemView.findViewById(R.id.eventCity);
            eventDate = (TextView)  itemView.findViewById(R.id.eventDate);
            image = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}
