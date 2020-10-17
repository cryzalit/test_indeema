package com.example.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.transform.Source;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<String> title;
    private List<String> author;
    private List<String> rating;
    private List<String> ncoments;
    private List<String> subreddit;
    private List<String> url;
    private List<String> permalink;
    private List<String> dates;
    private List<Bitmap> bitmap;



    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, List<String> title , List<String> author , List<String> rating , List<String> ncoments , List<String> subreddit, List<String> url, List<String> permalink, List<String> dates) {
        this.mInflater = LayoutInflater.from(context);

        this.title = title;
        this.author = author;
        this.rating = rating;
        this.ncoments = ncoments;
        this.subreddit = subreddit;
        this.url = url;
        this.permalink = permalink;
        this.dates = dates;
        this.bitmap = bitmap;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String tl = title.get(position);
        String au = author.get(position);
        String rt = rating.get(position);
        String sb = subreddit.get(position);
        String cm = ncoments.get(position);
        String imurl = url.get(position);
        String dt = dates.get(position);


        holder.title.setText(tl);
        holder.author.setText("Posted by  %n"+au);
        holder.rating.setText(rt);
        holder.ncoments.setText(cm);
        holder.subreddit.setText(sb);
        holder.dates.setText(dt);
        Glide.with(holder.itemView.getContext()).load(imurl).apply(new RequestOptions().override(200, 200).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.RESOURCE)).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return title.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView author;
        TextView rating;
        TextView ncoments;
        TextView subreddit;
        TextView dates;

        ImageView image;


        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            author = itemView.findViewById(R.id.author);
            rating = itemView.findViewById(R.id.rating);
            ncoments = itemView.findViewById(R.id.ncomments);
            subreddit = itemView.findViewById(R.id.subreddit);
            image = itemView.findViewById(R.id.image);
            dates = itemView.findViewById(R.id.date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    String getItem(int id) {
        return permalink.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
