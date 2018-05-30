package com.example.srivi.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static com.example.srivi.chat.MainActivity.SEND_KEY;

/**
 * Created by srivi on 08-04-2018.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Viewholder> {
   ArrayList<ChatThread.Message> data;
   ChatRoomActivity context;
   public static final String SEND_KEY="send";
    SharedPreferences sharedPref ;
    int userid;

    public CustomAdapter(ArrayList<ChatThread.Message> data, ChatRoomActivity context, int userid) {
        this.data = data;
        this.context = context;
        this.userid = userid;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatthread, parent, false);
        Context context=parent.getContext();
        return new Viewholder(view,context);

    }

    @Override
    public void onBindViewHolder(Viewholder viewholder, final int position) {
        final ChatThread.Message thread=data.get(position);
        viewholder.message.setText(thread.message.toString());
        viewholder.time.setText(thread.created_at.toString());
        viewholder.username.setText(thread.user_fname.toString()+""+thread.user_lname.toString());
        sharedPref = context.getSharedPreferences( SEND_KEY, Context.MODE_PRIVATE );
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date inputDate=null;
        try {
           /* inputDate = newDateFormat.parse(time);
            String formattedDateString = newDateFormat.format(inputDate);
            Date outputDate = newDateFormat.parse(formattedDateString);*/
            inputDate = sdf.parse(thread.created_at.toString());
            String formattedDateString = sdf.format(inputDate);
            Date outputDate = sdf.parse(formattedDateString);
            PrettyTime prettyTime=new PrettyTime();
            String prettyTimeString=prettyTime.format(outputDate);
            viewholder.time.setText(prettyTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(thread.user_id==sharedPref.getInt("userid",0))
        {
            viewholder.delete.setVisibility(View.VISIBLE);
        }
        else
        {
            viewholder.delete.setVisibility(View.INVISIBLE);
        }

        viewholder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(position);
                context.delthread(thread.id);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, data.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class Viewholder extends RecyclerView.ViewHolder
    {
        TextView username;
        TextView message;
        TextView time;
        ImageView delete;

        public Viewholder(View itemView,Context context ) {
            super(itemView);
            message= (TextView) itemView.findViewById(R.id.tvmessage);
            username= (TextView) itemView.findViewById(R.id.tvname);
            time=(TextView) itemView.findViewById(R.id.tvtime);
            delete=(ImageView) itemView.findViewById(R.id.imgdelete1);
        }

    }

    /*public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        Viewholder viewholder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chatthread, parent, false);
            viewholder=new Viewholder();


            convertView.setTag(viewholder);
        }
        else {
            viewholder=(Viewholder) convertView.getTag();
        }

       viewholder.message.setText(thread.message.toString());
       viewholder.time.setText(thread.time.toString());
       viewholder.username.setText(thread.username.toString());
       viewholder.delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               removechat();
           }
       });
       return convertView;
    }

    public void removechat()
    {

    }*/


}
