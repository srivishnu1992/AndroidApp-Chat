package com.example.srivi.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatRoomActivity extends AppCompatActivity {
    TextView title;
    EditText message;
    OkHttpClient okHttpClient = new OkHttpClient();
    ArrayList<ChatThread.Message> chatlist =new ArrayList<>();
    Thread.Currentthread mainthread;
    SharedPreferences sharedPref;
    String token ;
    String f_name;
    String l_name;
    private  RecyclerView.Adapter adapter;
    private RecyclerView mRecyclerView;
    private  RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        setTitle("Chat Room");
        mainthread = new Thread.Currentthread();
        if (!getIntent().getExtras().isEmpty()) {

            mainthread = (Thread.Currentthread) getIntent().getExtras().get(ThreadsActivity.DATA_KEY);
            Log.d("main", mainthread.toString());
        }
        sharedPref = this.getSharedPreferences(MainActivity.SEND_KEY, Context.MODE_PRIVATE);
        token = sharedPref.getString(getString(R.string.Referencekey), "");
        f_name = sharedPref.getString(getString(R.string.FirstName), "");
        l_name = sharedPref.getString(getString(R.string.LastName), "");
        title = findViewById(R.id.tvchatname);
        message = findViewById(R.id.etmessage);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        title.setText(mainthread.getTitle().toString());
        mLayoutManager = new LinearLayoutManager(ChatRoomActivity.this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        getChatThreads();
        findViewById(R.id.imgsend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addthread(message.getText().toString());

            }
        });

        findViewById(R.id.imghome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent=new Intent(ChatRoomActivity.this,ThreadsActivity.class);
              startActivity(intent);
               finish();
            }


        });
    }
    public void delthread(int id)
    {
        Request request = new Request.Builder()
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/message/delete/"+ id)
                .addHeader("Authorization", "BEARER "+token)
                .get()
                .build();

        okHttpClient.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Request", "Failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if(response.isSuccessful())
                {
                    String responseBody = response.body().string();
                    Log.d("Request", responseBody);


                }

            }
        });

    }
    public void addthread(final String msg) {
        RequestBody requestBody = new FormBody.Builder(  )
                .add( "message", msg)
                .add( "thread_id", String.valueOf(mainthread.id) )
                .build();
        Request request = new Request.Builder()
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/message/add")
                .addHeader("Authorization", "BEARER "+token)
                .post(requestBody)
                .build();

        okHttpClient.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Request", "Failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                   if(response.isSuccessful())
                   {
                       String responseBody = response.body().string();
                       Log.d("Request", responseBody);
                       getChatThreads();
                       message.setText("");
                   }
                   else
                   {
                       Log.d("inadd","failed");
                   }
                }
            });

        }
    public void getChatThreads()
    {
        Request request = new Request.Builder()
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/messages/"+mainthread.getId())
                .addHeader("Authorization", "BEARER "+token)
                .build();
okHttpClient.newCall(request).enqueue(new Callback() {
    @Override
    public void onFailure(Call call, IOException e) {
        Log.d("Request failed","in displaying messages");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
if(response.isSuccessful())
{
    String responsebody=response.body().string();
    Gson gson=new Gson();
    ChatThread chat=gson.fromJson(responsebody,ChatThread.class);
    chatlist=chat.getMessages();
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            adapter = new CustomAdapter(chatlist,ChatRoomActivity.this,mainthread.user_id);

            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setHasFixedSize(true);

        }
    });




}
    }
});
    }


}