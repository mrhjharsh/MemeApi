package com.example.memeapi;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.gesture.GestureLibraries;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
     String[] url = new String[100];
    ProgressBar p1;
     int k = 0;
     String memeapi = "https://meme-api.herokuapp.com/gimme";
     String curr = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
        ImageView i = findViewById(R.id.imageView);
        TextView i2 = findViewById(R.id.ui);
        p1 = findViewById(R.id.p1);
        p1.setVisibility(View.INVISIBLE);

        Button b1;
        b1 = findViewById(R.id.button);
        Button b2;
        b2 = findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(CheckNetwork.isInternetAvailable(MainActivity.this)) //returns true if internet available
                {

                    p1.setVisibility(View.VISIBLE);
                    i2.setVisibility(View.INVISIBLE);
                    RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                    JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, memeapi,null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        url[k] = response.getString("url");
                                        Glide.with(MainActivity.this).load(url[k]).listener(new RequestListener<Drawable>() {
                                            @Override
                                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                p1.setVisibility(View.INVISIBLE);
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                                p1.setVisibility(View.INVISIBLE);
                                                return false;
                                            }
                                        }).into(i);
                                        curr = url[k];
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue.add(stringRequest);
                    k++;
                             }
                else
                {
                    Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
                }

            }
        });
b2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent  i = new Intent(Intent.ACTION_SEND);
       i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT,"see this meme "+curr);
        Intent choose = Intent.createChooser(i,"share this memem using ...");
        startActivity(choose);
    }
});


    }
}
class CheckNetwork {


    private static final String TAG = CheckNetwork.class.getSimpleName();



    public static boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            Log.d(TAG,"no internet connection");
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                Log.d(TAG," internet connection available...");
                return true;
            }
            else
            {
                Log.d(TAG," internet connection");
                return true;
            }

        }
    }
}



