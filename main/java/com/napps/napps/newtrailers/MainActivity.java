package com.napps.napps.newtrailers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

import java.util.ArrayList;
import java.util.List;

import static com.napps.napps.newtrailers.R.id.navigation_movie;
import static com.napps.napps.newtrailers.R.menu.menu_main;


public class MainActivity extends AppCompatActivity implements GetXmlData.OnDataAvailable,
        RecyclerItemClickListener.OnRecyclerClickListener {

    private static final String TAG = "MainActivity";
    private final String URL_CONTENT = "Url content";
    private final String FAVORITES = "favorites";
    private final String NETFLIX = "netflix";
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private static final String GOOGLE_API_KEY = "AIzaSyAokTHLocXGerhj5Myzke22MWn8L10o-hY";
    private GetXmlData getXmlData;
    private List<Data> mList;
    private Intent intent = null;
    boolean inNetflix=false;
    boolean inFavorite=false;
    private BaseManagement bm;
    private Data dataItem;
    private String url="https://www.youtube.com/feeds/videos.xml?playlist_id=PLScC8g4bqD47c-qHlsfhGH3j6Bg7jzFy-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        if (savedInstanceState!=null) {
            inNetflix=savedInstanceState.getBoolean(NETFLIX);
            inFavorite=savedInstanceState.getBoolean(FAVORITES);
            url=savedInstanceState.getString(URL_CONTENT);
            if (inNetflix) {
                getXmlData = new GetXmlData(this, "netflix");
                getXmlData.execute(url);
                getSupportActionBar().show();
            } else if (inFavorite) {
                mRecyclerViewAdapter.loadNewMovie(bm.loadData(), Type.YouTube);
            } else {
                getXmlData = new GetXmlData(this, "youtube");
                getXmlData.execute(url);
            }
        }else {
            getXmlData = new GetXmlData(this, "youtube");
            getXmlData.execute(url);
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, this));

        mRecyclerViewAdapter = new RecyclerViewAdapter(this, new ArrayList<Data>());
        recyclerView.setAdapter(mRecyclerViewAdapter);
        bm = new BaseManagement(this);

    }

    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case navigation_movie:
                    setTitle(R.string.title_activity_movie_trailers);
                    inNetflix = false;
                    inFavorite = false;
                    getXmlData = new GetXmlData(MainActivity.this, "youtube");
                    url = "https://www.youtube.com/feeds/videos.xml?playlist_id=PLScC8g4bqD47c-qHlsfhGH3j6Bg7jzFy-";
                    getXmlData.execute(url);
                    getSupportActionBar().hide();
                    return true;
                case R.id.navigation_series:
                    setTitle(R.string.title_activity_series_trailers);
                    inNetflix = false;
                    inFavorite = false;
                    getXmlData = new GetXmlData(MainActivity.this, "youtube");
                    url ="https://www.youtube.com/feeds/videos.xml?channel_id=UC1cBYqj3VXJDeUee3kMDvPg";
                    getXmlData.execute(url);
                    getSupportActionBar().hide();
                    return true;
                case R.id.navigation_favorite:
                    setTitle(R.string.title_favorite);
                    mRecyclerViewAdapter.loadNewMovie(bm.loadData(), Type.YouTube);
                    getSupportActionBar().hide();
                    inNetflix = false;
                    inFavorite = true;
                    return true;
                case R.id.navigation_netflixTOP:
                    setTitle(getString(R.string.title_activity_netflix_top) + getString(R.string.feed_menu1));
                    getXmlData = new GetXmlData(MainActivity.this, "netflix");
                    url="http://dvd.netflix.com/Top25RSS?gid=296";
                    getXmlData.execute(url);
                    getSupportActionBar().show();
                    inNetflix = true;
                    inFavorite = false;
                    return true;
            }
            return false;
        }

    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        getXmlData = new GetXmlData(this, "netflix");

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_and_adventure) {
            url ="http://dvd.netflix.com/Top25RSS?gid=296";
            setTitle(getString(R.string.title_activity_netflix_top) + getString(R.string.feed_menu1));
            getXmlData.execute(url);
            return true;
        } else if (id == R.id.anime_and_animation){
            url="http://dvd.netflix.com/Top25RSS?gid=623";
            setTitle(getString(R.string.title_activity_netflix_top) + getString(R.string.feed_menu2));
            getXmlData.execute(url);
            return true;
        } else if (id == R.id.classics){
            url="http://dvd.netflix.com/Top25RSS?gid=306";
            setTitle(getString(R.string.title_activity_netflix_top) + getString(R.string.feed_menu3));
            getXmlData.execute(url);
            return true;
        } else if (id == R.id.comedy){
            url="http://dvd.netflix.com/Top25RSS?gid=307";
            setTitle(getString(R.string.title_activity_netflix_top) + getString(R.string.feed_menu4));
            getXmlData.execute(url);
            return true;
        } else if (id == R.id.drama){
            url="http://dvd.netflix.com/Top25RSS?gid=315";
            setTitle(getString(R.string.title_activity_netflix_top) + getString(R.string.feed_menu5));
            getXmlData.execute(url);
            return true;
        } else if (id == R.id.documentary){
            url="http://dvd.netflix.com/Top25RSS?gid=864";
            setTitle(getString(R.string.title_activity_netflix_top) + getString(R.string.feed_menu6));
            getXmlData.execute(url);
            return true;
        } else if (id == R.id.thrillers){
            url="http://dvd.netflix.com/Top25RSS?gid=387";
            setTitle(getString(R.string.title_activity_netflix_top) + getString(R.string.feed_menu9));
            getXmlData.execute(url);
            return true;
        } else if (id == R.id.romance){
            url="http://dvd.netflix.com/Top25RSS?gid=371";
            setTitle(getString(R.string.title_activity_netflix_top) + getString(R.string.feed_menu7));
            getXmlData.execute(url);
            return true;
        } else if (id == R.id.fantasy){
            url="http://dvd.netflix.com/Top25RSS?gid=373";
            setTitle(getString(R.string.title_activity_netflix_top) + getString(R.string.feed_menu8));
            getXmlData.execute(url);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<Data> data, DownloadStatus status, Type mType) {
        Log.d(TAG, "onDataAvailable: starts" + mType);
        if (status == DownloadStatus.OK) {
            mRecyclerViewAdapter.loadNewMovie(data, mType);
//            Log.e(TAG, "onDataAvailable succed " + data);
            mList = data;
        } else {
            // download or processing failed
            Log.e(TAG, "onDataAvailable failed with status " + status);
        }

//        Log.d(TAG, "onDataAvailable: ends");
    }

    @Override
    public void onItemClick(View view, int position) {
        if (inNetflix == false) {
            Toast.makeText(MainActivity.this, "Double tap to watch, \n Long tap to save", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemLongClick(View view, int position) {
        if (inNetflix == false && inFavorite == false) {
            dataItem = mList.get(position);
            bm.addSlot(dataItem);
            Toast.makeText(MainActivity.this, "Trailer saved", Toast.LENGTH_SHORT).show();
        } else if (inFavorite){
            bm.removeSlot(((bm.loadData()).get(position)).getTitle());
            mRecyclerViewAdapter.loadNewMovie(bm.loadData(), Type.YouTube);
            Toast.makeText(MainActivity.this, "Trailer removed", Toast.LENGTH_SHORT).show();
            getXmlData = new GetXmlData(this, "youtube");
        }
    }

    @Override
    public void onDoubleTap(View view, int position) {
        if (inNetflix == false) {
            dataItem = mList.get(position);
            intent = YouTubeStandalonePlayer.createVideoIntent(this, GOOGLE_API_KEY, dataItem.getID(), 0, true, false);
            startActivity(intent);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(URL_CONTENT, url);
        outState.putBoolean(FAVORITES,inFavorite);
        outState.putBoolean(NETFLIX,inNetflix);
        super.onSaveInstanceState(outState);
    }
    View.OnClickListener opListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            getXmlData = new GetXmlData(MainActivity.this, "netflix");
            getXmlData.execute("http://dvd.netflix.com/Top25RSS?gid=296");
        }
    };


}
