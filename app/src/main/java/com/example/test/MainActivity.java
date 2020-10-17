package com.example.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.View;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    ArrayList<String> title = new ArrayList<>(50);
    ArrayList<String> author = new ArrayList<>(50);
    ArrayList<String> dates = new ArrayList<>(50);
    ArrayList<String> rating = new ArrayList<>(50);
    ArrayList<String> ncoments = new ArrayList<>(50);
    ArrayList<String> subreddit = new ArrayList<>(50);
    ArrayList<String> url = new ArrayList<>(50);
    ArrayList<String> permalink = new ArrayList<>(50);



    JSONObject rdt ;
    JSONObject data ;
    JSONArray jArray3 ;

    TinyDB tinydb;


    MyRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.rv);
        tinydb = new TinyDB(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new Getdata().execute();
        adapter = new MyRecyclerViewAdapter(this,title, author,rating,ncoments,subreddit,url,permalink,dates);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


    }

    private class Getdata extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url_reddit = "https://www.reddit.com/top.json?limit=50";
            String jsonStr = sh.makeServiceCall(url_reddit);


            Log.e("jsonStr", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    rdt = new JSONObject(jsonStr);
                    data = rdt.getJSONObject("data");
                    jArray3 = data.getJSONArray("children");
                    for (int i = 0; i < 50; i++) {
                        JSONObject obj = jArray3.getJSONObject(i);
                        JSONObject data2 = obj.getJSONObject("data");
                        author.add(data2.getString("author"));
                        rating.add(Integer.parseInt(data2.getString("score")) / 1000 + " k");
                        double tstap = Double.parseDouble(data2.getString("created"));
                        long x = Math.round(tstap);
                        dates.add(getDate(x));
                        title.add(data2.getString("title"));
                        ncoments.add(data2.getString("num_comments"));
                        subreddit.add("/" + data2.getString("subreddit"));
                        url.add(data2.getString("thumbnail"));
                        permalink.add("https://www.reddit.com" + data2.getString("permalink"));
                        tinydb.putListString("permalink", permalink);

                    }
                    tinydb.putListString("author", author);
                    tinydb.putListString("rating", rating);
                    tinydb.putListString("dates", dates);
                    tinydb.putListString("title", title);
                    tinydb.putListString("ncoments", ncoments);
                    tinydb.putListString("subreddit", subreddit);
                    tinydb.putListString("url", url);
                    tinydb.putListString("permalink", permalink);

                } catch (final JSONException e) {
                    Log.e("TAG", "Json parsing error: " + e.getMessage());

                }

            } else {


            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            author = tinydb.getListString("author");
            rating = tinydb.getListString("rating");
            dates = tinydb.getListString("dates");
            title = tinydb.getListString("title");
            ncoments = tinydb.getListString("ncoments");
            subreddit = tinydb.getListString("subreddit");
            url = tinydb.getListString("url");
            permalink = tinydb.getListString("permalink");

            adapter.notifyDataSetChanged();


        }
    }


    @Override
    public void onItemClick(View view, int position) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(adapter.getItem(position)));
        this.startActivity(browserIntent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        return true;
    }
    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("hh:mm %n (dd-MM-yyyy)", cal).toString();
        return date;
    }



}
