package cityscape.com.androidapp;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.android.Facebook;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import cityscape.com.adapter.DrawerListAdapter;
import cityscape.com.adapter.EventAdapter;
import cityscape.com.adapter.FragmentAdapter;
import cityscape.com.adapter.NavItem;
import cityscape.com.library.FBUserDetails;
import cityscape.com.library.MyTabFactory;
import cityscape.com.model.EventInfo;
import cityscape.com.library.EventDAO;
import cityscape.com.library.LocationService;


public class SearchActivity extends FragmentActivity implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    private TabHost mTabHost;

    LocationService locationService;

    FragmentAdapter pageAdapter;

    private static Map<String,List<JSONObject>> eventMap;

    private List<String> eventList;

    private static EventDAO eventDao;

    Bitmap bitmap = null;
    DrawerLayout mDrawerLayout;
    ListView mDrawerList;
    ActionBarDrawerToggle mDrawerToggle;
    ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    RelativeLayout mDrawerPane;
    String user_id = "877555828977050";
    static String userId= null;
    static String fbUserName= "";
    static String fbEmail = null;
    Facebook fb = new Facebook("877555828977050");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d("Search Activity ", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Intent intent=getIntent();

        eventMap = new HashMap<String, List<JSONObject>>();
        eventDao = new EventDAO();
        eventDao.setCity(intent.getStringExtra("City"));

        eventList = new ArrayList<String>();
        eventDao.getEventTypes(eventList);
        for(String type:eventList)
        {
            eventMap.put(type,new ArrayList<JSONObject>());
        }

        eventDao.getEventData(eventMap);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        initialiseTabHost();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


       // List<Fragment> fragments = getFragments();
        //mViewPager.setAdapter(pageAdapter);
        mViewPager.setOnPageChangeListener(SearchActivity.this);

        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        final ImageButton fblogin = (ImageButton) findViewById(R.id.img_login);
        final TextView username = (TextView) findViewById(R.id.userName);
        final ImageButton button = (ImageButton) findViewById(R.id.imageView1);
        final ImageView avatar = (ImageView) findViewById(R.id.avatar);
        Session session = Session.getActiveSession();

        if(savedInstanceState !=null && session !=null)
        {
            Log.v("Inside Saved ","Instance");
            userId = savedInstanceState.getString("FbUserId");
            Log.v("TAG USER-ID SAVE",userId);
            username.setText(savedInstanceState.getString("FbUserName"));
            fbUserName = savedInstanceState.getString("FbUserName");
            avatar.setImageBitmap((Bitmap)savedInstanceState.getParcelable("bitmap"));
            bitmap = (Bitmap)savedInstanceState.getParcelable("bitmap");
        }

        if(session!=null && userId != null) {
            FBUserDetails details;
            mDrawerList.setPadding(0,0,0,150);
            //Log.v("TAG CLASS G/S username",details.getFbUserName());
            fblogin.setImageResource(R.drawable.logout);
        }
        else
        {
            mDrawerList.setPadding(0, 0, 0, 50);
            fblogin.setImageResource(R.drawable.login);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
                {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                else {
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });

        mNavItems.add(new NavItem("Browse", "Change your preferences", R.drawable.ic_action_search));
        mNavItems.add(new NavItem("About Us", "Know about us", R.drawable.ic_action_person));
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        DrawerListAdapter adapter = new DrawerListAdapter(getApplicationContext(), mNavItems);
        mDrawerList.setAdapter(adapter);

        fblogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Session.getActiveSession() != null && userId != null) {
                    Log.v("TAG", "INSIDE FB LOGOUT");
                    new logoutOfFb().execute();
                    Session.getActiveSession().close();
                    Session.getActiveSession().closeAndClearTokenInformation();
                    Session.setActiveSession(null);

                    SharedPreferences.Editor editor =  getPreferences(MODE_PRIVATE).edit();
                    editor.putString("access_token", null);
                    editor.commit();
                    SharedPreferences.Editor editor2 =  getPreferences(MODE_PRIVATE).edit();
                    editor2.putLong("access_expires", 0);
                    editor2.commit();

                    userId=null;
                    fbUserName="";
                    mDrawerList.setPadding(0,0,0,50);
                    android.os.Handler handler = new android.os.Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                            startActivity(getIntent());
                        }
                    }, 1000);
                }
                else
                {
                    Log.v("TAG","INSIDE FB LOGIN");
                    final List<String> PERMISSIONS = Arrays.asList("email", "public_profile");
                    Session.openActiveSession(SearchActivity.this, true, new Session.StatusCallback() {
                        @Override
                        public void call(Session session, SessionState state, Exception exception) {
                            Log.v("TAG ", "INSIDE Call");
                            Log.v("TAG SESSION ID ",session.toString());
                            boolean pendingPublishReauthorization = false;
                            Log.v("Session State",state.toString());
                            List<String> permissions = session.getPermissions();

                            if (state.isOpened()) {
                                Log.v("TAG ", "STATE IS OPENED");
                                if(!isSubsetOf(PERMISSIONS, permissions)){
                                    pendingPublishReauthorization = true;
                                    Session.NewPermissionsRequest newPermissionsRequest = new Session.NewPermissionsRequest(SearchActivity.this,PERMISSIONS);
                                    session.requestNewReadPermissions(newPermissionsRequest);
                                    return;
                                }

                                Request.newMeRequest(session, new Request.GraphUserCallback() {

                                    @Override
                                    public void onCompleted(GraphUser user, Response response) {
                                        Log.v("TAG ", "INSIDE ONCOMPLETE");
                                        if (response != null) {
                                            FBUserDetails details = new FBUserDetails();
                                            Log.v("TAG USERNAME", user.getName());
                                            fblogin.setImageResource(R.drawable.logout);
                                            username.setText(user.getName());
                                            userId = user.getId();
                                            fbUserName = user.getName();
                                            details.setFbUserName(user.getName());
                                            //Log.v("TAG FB USERNAME",details.getFbUserName());
                                            Log.v("TAG USER ID", user.getId());
                                            new getFbImage().execute();
                                            onSaveInstanceState(new Bundle());
                                            mDrawerList.setPadding(0, 0, 0, 150);
                                        }
                                    }
                                }).executeAsync();
                            }
                        }
                    });
                }
            }
        });
    }

    class logoutOfFb extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                fb.logout(getApplicationContext());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class getFbImage extends AsyncTask<Void,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... params) {
            Log.v("TAG : ", "INSIDE IMAGE RETRIEVAL");

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet("http://graph.facebook.com/"+userId+"/picture?type=small");
            HttpResponse response;
            try {
                response = (HttpResponse)client.execute(request);
                HttpEntity entity = response.getEntity();
                BufferedHttpEntity bufferedEntity = new BufferedHttpEntity(entity);
                InputStream inputStream = bufferedEntity.getContent();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
            catch (ClientProtocolException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            final ImageView avatar = (ImageView) findViewById(R.id.avatar);
            avatar.setImageBitmap(bitmap);
        }
    }
    private boolean isSubsetOf(Collection<String> subset,
                               Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode,
                resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
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

    @Override
    public void onTabChanged(String tabId) {
        int pos = this.mTabHost.getCurrentTab();
        this.mViewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        int pos = this.mViewPager.getCurrentItem();
        this.mTabHost.setCurrentTab(pos);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            ListFragment fragment = PlaceholderFragment.newInstance(position + 1,getPageTitle(position).toString());
            return fragment;
        }

        @Override
        public int getCount() {
            return eventList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            int count=0;
            Iterator<String> iterator=eventList.iterator();
            while(iterator.hasNext())
            {
                if(count==position)
                {
                    return iterator.next();
                }
                iterator.next();
                count++;
            }

            return null;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends ListFragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private static int position;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, String title) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            position=sectionNumber-1;
            args.putString("TITLE",title);
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            Log.d("Title : ",title);
            Log.d("Position : ",position+"");
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_search, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated (View view, Bundle savedInstanceState){

            Bundle args=getArguments();
            String title = args.getString("TITLE");
            Log.d("Title : ", title);
            RecyclerView rv = (RecyclerView)view.findViewById(R.id.list);
            rv.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(this.getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(llm);
            List<JSONObject> list = eventMap.get(title);
            Log.d("List",list.toString());
            List<EventInfo> eventList= new ArrayList<EventInfo>();
            try {

                for (JSONObject jo : list) {
                    Log.d("Fragement","Create View!!");
                    EventInfo event = new EventInfo();
                    event.setEventName(jo.getString("eventname"));
                    event.setEventCity(jo.getString("eventcity"));
                    event.setEventDate(new SimpleDateFormat("yyyy-MM-dd").parse(jo.getString("eventdate").substring(0,jo.getString("eventdate").indexOf("T"))));
                    eventList.add(event);
                }
                EventAdapter eventAdapter = new EventAdapter(eventList);
                rv.setAdapter(eventAdapter);
            }catch(JSONException e)
            {
                Log.e("Error : ",e.toString());
            }
            catch(ParseException e)
            {
                Log.e("Error : ",e.toString());
            }
        }

    }

    private void initialiseTabHost() {
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup();

        Iterator<String> iterator = eventMap.keySet().iterator();
        while(iterator.hasNext()) {
            String type = iterator.next();
            SearchActivity.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec(type).setIndicator(type));
        }

        mTabHost.setOnTabChangedListener(this);
    }

    private static void AddTab(SearchActivity activity, TabHost tabHost, TabHost.TabSpec tabSpec) {
        tabSpec.setContent(new MyTabFactory(activity));
        tabHost.addTab(tabSpec);
    }

}
