package cityscape.com.androidapp;

import android.location.Location;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cityscape.com.library.EventDAO;

/**
 * Created by Chirag on 28-03-2015.
 */
public class RetrieveEventTask extends AsyncTask {

    @Override
    protected Object doInBackground(Object[] params) {
        EventDAO eventDAO = (EventDAO)params[0];
        Map<String,List<JSONObject>> eventMap=(Map<String,List<JSONObject>>)params[1];
        eventDAO.getEventData(eventMap);
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
