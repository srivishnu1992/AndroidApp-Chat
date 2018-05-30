package com.example.srivi.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by namaluuu on 4/10/2018.
 */

public class MessagethreadAdapter extends ArrayAdapter<Thread.Currentthread> {

    public static final String SEND_KEY = "send";
    SharedPreferences sharedPreferences;
    List<Thread.Currentthread> mdata;
    ThreadsActivity mContext;
    int mResource;


    public MessagethreadAdapter(ThreadsActivity context, int resource, List<Thread.Currentthread> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mdata = objects;
        this.mResource = resource;

    }


    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Thread.Currentthread thread = mdata.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }
        sharedPreferences = mContext.getSharedPreferences(SEND_KEY, MODE_PRIVATE);
        TextView tvthread = (TextView) convertView.findViewById(R.id.tvthread);
        ImageView imgdelete = (ImageView) convertView.findViewById(R.id.imgdelete);
        imgdelete.setVisibility(View.INVISIBLE);
        if (thread.getUser_id() == sharedPreferences.getInt("userid", 0)) {
            imgdelete.setVisibility(View.VISIBLE);
            imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mdata.remove(position);
                    mContext.deleteMessageThread(thread);
                    notifyDataSetChanged();
                }

            });
        }

        tvthread.setText(thread.getTitle());
        return convertView;
    }


}