package cityscape.com.androidapp;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import cityscape.com.library.EventDAO;
import cityscape.com.library.LocationService;


public class SelectCityActivity extends ActionBarActivity {

    ListView listview;
    String[] cities;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        Log.d("SelectCityAcivity : ","onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        context = this;
        Location gpsLocation = null;
        EventDAO eventDAO=new EventDAO();
            gpsLocation = new LocationService(SelectCityActivity.this)
                    .getLocation(LocationManager.GPS_PROVIDER);
            try {
                if (gpsLocation != null) {
                    eventDAO.fetchCity(gpsLocation);
                }
            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }
        if(eventDAO.getCity()!=null) {
             Intent intent = new Intent(SelectCityActivity.this,SearchActivity.class);
            intent.putExtra("City",eventDAO.getCity());
             startActivity(intent);
        }
        else
        {
            final EditText city = (EditText)findViewById(R.id.editText);
            listview = (ListView) findViewById(R.id.listView);
            //string array
            final List<String> cities = new ArrayList<String>();

            try {
                new EventDAO().getCities(cities);
            } catch (IOException e) {
                Toast toast = Toast.makeText(context, "Unable to fetch data!", Toast.LENGTH_LONG);
                toast.show();
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.list_item, R.id.checkedTextView, cities);
            city.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(city.getText().toString());
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            });
            listview.setAdapter(adapter);
            listview.setItemsCanFocus(false);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String city = adapter.getItem(position);
                    Intent intent = new Intent(SelectCityActivity.this, SearchActivity.class);
                    intent.putExtra("City", city);
                    startActivity(intent);
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_city, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
