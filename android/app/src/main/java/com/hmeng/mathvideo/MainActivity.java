package com.hmeng.mathvideo;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;


public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private PaintView paintView;
    private WebSocketClient webSocketClient;
    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayer mYouTubePlayer;

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://10.0.2.2:9001/websocket");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                //webSocketClient.send("Hello World!");
            }
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                //final String message = s;
                YouTubePlayerUtils.loadOrCueVideo(
                        mYouTubePlayer, getLifecycle(),
                        s, 0f
                );

                runOnUiThread(new Runnable() {
                    public void run() {
                        try{
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            public void onBinaryReceived(byte[] data) {
            }
            public void onPingReceived(byte[] data) {
            }
            public void onPongReceived(byte[] data) {
            }
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    public void sendMessage() {
        Log.i("WebSocket", "Button was clicked");
        // Send button id string to WebSocket Server
        String stokes = paintView.getPaths();
        webSocketClient.send(stokes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Searching", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //NavigationView navigationView = findViewById(R.id.nav_view);
        initYouTubePlayerView();

        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        */
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);
        createWebSocketClient();
    }
    private void initYouTubePlayerView() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {

                setSearchPlayVideoButtonClickListener(youTubePlayer);

                YouTubePlayerUtils.loadOrCueVideo(
                        youTubePlayer, getLifecycle(),
                        "mdon2Ei4B-A", 0f
                );
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
            }

            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            // inflate the layout of the popup window
            //LayoutInflater inflater = (LayoutInflater)
            //        getSystemService(LAYOUT_INFLATER_SERVICE);
            //View popupView = inflater.inflate(R.layout.popup_window, null);


            // create the popup window
            //int width = LinearLayout.LayoutParams.WRAP_CONTENT;
            //int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            //boolean focusable = true; // lets taps outside the popup also dismiss it
            //inal PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

            // show the popup window
            // which view you pass in doesn't matter, it is only used for the window tolken
            //popupWindow.showAtLocation(youTubePlayerView, Gravity.CENTER, 0, 0);

            PaintView pv = (PaintView) findViewById(R.id.paintView);
            pv.clear();
            /*
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            paintView.init(metrics);
            */
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void setSearchPlayVideoButtonClickListener(final YouTubePlayer youTubePlayer) {

        FloatingActionButton fabButton = findViewById(R.id.fab);
        mYouTubePlayer = youTubePlayer;

        fabButton.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
/*
                                             YouTubePlayerUtils.loadOrCueVideo(
                                                     youTubePlayer, getLifecycle(),
                                                     "EYG1XvNUZF0", 0f
                                             );
*/
                                             sendMessage();
                                         }
                                     }
        );
        
    }
}
