package cityscape.com.library;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Created by Chirag on 28-03-2015.
 */
public class EventDAO {

    String city;
    Date date;

    public EventDAO()
    {
        date= new Date();
    }

    public void setCity(String city)
    {
        this.city=city;
    }

    public void getEventTypes(List<String> eventList)
    {
        int count=0;
        String get_event_types = "https://secure-journey-4788.herokuapp.com/getEventType?city="+city ;
        try
        {
            URL url = new URL(get_event_types);

            HttpURLConnection urlConnection =
                    (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // gets the server json data
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
            String next=bufferedReader.readLine();
            String type="";
            for(int i=0;i<next.length();i++)
            {
                if(next.charAt(i)!='"' && next.charAt(i)!='[' && next.charAt(i)!=']')
                {
                    type+=next.charAt(i);
                }
            }
            Log.d("Types : : ",type);
            StringTokenizer types=new StringTokenizer(type,",");

            while(types.hasMoreTokens()) {
             next=types.nextToken();
                if (!eventList.contains(next)) {
                    eventList.add(next);
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getEventData(Map<String, List<JSONObject>> eventMap)
    {
       // SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<JSONObject> eventList ;
        int count=0;
        String get_events = "https://secure-journey-4788.herokuapp.com/getDetails?city="+city ;
        try {

           // while(count<7)
            //{
              //  count++;
                //get_events+="&date="+sdf.format(date);
                URL url = new URL(get_events);

                HttpURLConnection urlConnection =
                        (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // gets the server json data
                BufferedReader bufferedReader =
                        new BufferedReader(new InputStreamReader(
                                urlConnection.getInputStream()));
                String next;
                while ((next = bufferedReader.readLine()) != null) {

                    JSONArray ja = new JSONArray(next);

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = (JSONObject) ja.get(i);
                        String eventtype = jo.getString("type");
                        Log.d("EventDAO","type = "+eventtype);
                        if (eventMap.get(eventtype) != null) {
                            eventList=eventMap.get(eventtype);
                            eventList.add(jo);
                            eventMap.put(eventtype, eventList);
                        } else {
                            eventList = new ArrayList<JSONObject>();
                            eventList.add(jo);
                            eventMap.put(eventtype, eventList);
                        }
                    }
                }
                Calendar cal = new GregorianCalendar();
                cal.setTime(date);
                cal.add(Calendar.DAY_OF_YEAR, 1);
                date = cal.getTime();
           // }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Log.d("Events" , eventMap.toString());
    }

    public void fetchCity(Location gpsLocation) throws IOException,MalformedURLException {

        String get_events = "https://secure-journey-4788.herokuapp.com/getUserLocation?lat="+gpsLocation.getLatitude()+"&lon="+gpsLocation.getLongitude();
            URL url = new URL(get_events);

            HttpURLConnection urlConnection =
                    (HttpURLConnection) url.openConnection();

            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // gets the server json data
            BufferedReader bufferedReader =
                    new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream()));
            this.city=bufferedReader.readLine();
            Log.d("EventDAO","City : "+city);

    }

    public String getCity() {
        return city;
    }

    public void getCities(List<String> cities) throws MalformedURLException, IOException, JSONException {
        String get_events = "https://secure-journey-4788.herokuapp.com/getCities";
        URL url = new URL(get_events);

        HttpURLConnection urlConnection =
                (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.connect();
        // gets the server json data
        BufferedReader bufferedReader =
                new BufferedReader(new InputStreamReader(
                        urlConnection.getInputStream()));
        String next;
        while ((next = bufferedReader.readLine()) != null) {

            JSONArray ja = new JSONArray(next);

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                String city = jo.getString("city");
                cities.add(city);
            }
        }
    }
}
