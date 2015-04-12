package cityscape.com.model;

import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.Date;

/**
 * Created by Chirag on 29-03-2015.
 */
public class EventInfo {

    private String eventName;
    private String eventCity;
    private Date eventDate;
    private URL image;

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventCity() {
        return eventCity;
    }

    public void setEventCity(String eventCity) {
        this.eventCity = eventCity;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public URL getImage() {
        return image;
    }

    public void setImage(URL image) {
        this.image = image;
    }
}
