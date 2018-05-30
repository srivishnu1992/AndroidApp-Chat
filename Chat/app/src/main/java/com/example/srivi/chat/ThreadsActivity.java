package com.example.srivi.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ThreadsActivity extends AppCompatActivity {

    OkHttpClient okHttpClient = new OkHttpClient();
    SharedPreferences sharedPref;
    ImageView ibLogout;
    TextView tvName;
    EditText addName;
    String token;
    ArrayList<String> messagetitle;
    ArrayList<Thread.Currentthread> threadarray;
    ListView listView;
    Token tokenResponse;
    public static String DATA_KEY="Arraylist";
    public static final String SEND_KEY = "send";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_threads );
        setTitle( "Message Threads" );


        tvName = findViewById( R.id.tvfullname);
        final EditText editText=findViewById(R.id.etthreadname);
       // tvName.setText( tokenResponse.user_fname+" "+tokenResponse.user_lname );
        sharedPref = this.getSharedPreferences( SEND_KEY, Context.MODE_PRIVATE );
        token = sharedPref.getString( getString( R.string.Referencekey ), "" );
        String f_name= sharedPref.getString( getString( R.string.FirstName ), "" );
        String l_name=sharedPref.getString(getString(R.string.LastName),"");
        int user_id=sharedPref.getInt(getString(R.string.struser_id),0);
        tvName.setText( f_name+" "+l_name );
        Log.d("token", token);
        listView=findViewById(R.id.listView);
        displayThreads(token);
        ibLogout = findViewById( R.id.imglogout );
        ibLogout.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearToken();
                Intent intent = new Intent( ThreadsActivity.this, ThreadsActivity.class );
                startActivity(intent);
                finish();
            }
        } );
        addName=findViewById(R.id.etthreadname);
    findViewById(R.id.imgadd).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final RequestBody requestBody = new FormBody.Builder(  )
                    .add("title",editText.getText().toString()  )
                    .build();

            Request request = new Request.Builder()
                    .url( "http://ec2-54-91-96-147.compute-1.amazonaws.com/api/thread/add" )
                    .addHeader( "Authorization", "BEARER "+token )
                    .post(requestBody)
                    .build();
            okHttpClient.newCall( request ).enqueue( new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("GetThreadsRequest", "Failed");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response.isSuccessful())
                        displayThreads(token);

                }
            } );
editText.setText(null);

        }
    });

    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(ThreadsActivity.this,ChatRoomActivity.class);
            intent.putExtra(DATA_KEY, threadarray.get(position));
            startActivity(intent);
        }
    });
    }


    public void displayThreads(String token) {
        Request request = new Request.Builder()
                .url( "http://ec2-54-91-96-147.compute-1.amazonaws.com/api/thread" )
                .addHeader( "Authorization", "BEARER "+token )
                .get()
                .build();
        okHttpClient.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("GetThreadsRequest", "Failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("GetThreadsRequest", responseBody);
                Gson gson = new Gson();
                Thread allthreads=gson.fromJson(responseBody,Thread.class);
                Log.d("allthreads",allthreads.toString());
                threadarray=allthreads.getThreads();
                Log.d("array",allthreads.getThreads().toString());
              /*  messagetitle=new ArrayList<>();
                int size=threadarray.size();
                Log.d("size",String.valueOf(size));
                for(int i=0;i<size;i++)
                {
                    messagetitle.add(threadarray.get(i).title);
                }*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        MessagethreadAdapter adapter = new MessagethreadAdapter(ThreadsActivity.this,R.layout.messagethread,threadarray);
                        listView.setAdapter(adapter);
                    }
                });
            }
        } );

}
    public void deleteMessageThread(Thread.Currentthread delMessageThread) {
        int delMessage = delMessageThread.getId();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/thread/delete/" + delMessage)
                .header("Authorization", "BEARER " + token)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.getMessage();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String val = response.body().string();
                    Log.d("demo_ra_Success", val);
                } else {
                    Log.d("demoerror", "Not Success\ncode : " + response.code());
                }


            }
        });

    }
    public void clearToken() {
        sharedPref.edit().putString( getString(R.string.Referencekey), "" );
    }
}