package com.example.srivi.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnSignUp;
    EditText editEmail;
    EditText editPassword;
    OkHttpClient okHttpClient = new OkHttpClient();
    static String RESPONSE_KEY="response";
    public static final String SEND_KEY = "send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        setTitle( "Chat Room" );
        btnLogin = findViewById( R.id.btnLogin );
        btnSignUp = findViewById( R.id.btnSignUp );
        editEmail = findViewById( R.id.editEmail );
        editPassword = findViewById( R.id.editPassword );
        btnLogin.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strEmail = editEmail.getText().toString();
                String strPassword = editPassword.getText().toString();
                if(strEmail.equals( "" )) {
                    editEmail.setError( "Please enter email" );
                }
                else if(strPassword.equals( "" )) {
                    editPassword.setError( "Please enter password" );
                }
                else {
                    login(strEmail, strPassword);
                }
            }
        } );
        btnSignUp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( MainActivity.this, Signup.class );
                startActivity( intent );
                finish();
            }
        } );
    }
    public void login(String email, String password) {
        final RequestBody requestBody = new FormBody.Builder(  )
                .add( "email", email )
                .add( "password", password )
                .build();
        Request request = new Request.Builder()
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/login")
                .post(requestBody)
                .build();
        okHttpClient.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Request", "Failed");
                Toast.makeText( MainActivity.this, "Login not successful", Toast.LENGTH_SHORT ).show();

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d("Request", responseBody);
                Gson gson = new Gson();
                final Token tokenResponse = gson.fromJson( responseBody, Token.class );
                Log.d( "Request", tokenResponse.status );
                if(!tokenResponse.status.equals( "ok" )) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( MainActivity.this, tokenResponse.message, Toast.LENGTH_SHORT ).show();
                        }
                    } );
                } else {
                    savetoken( tokenResponse);
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            editEmail.setText( "" );
                            editPassword.setText( "" );
                            Intent intent = new Intent( MainActivity.this, ThreadsActivity.class );
                            intent.putExtra( RESPONSE_KEY, tokenResponse );
                            startActivity(intent);
                        }
                    } );
                }
            }
        } );
    }
    public void savetoken(Token tokenresponse){
        SharedPreferences sharedPref = this.getSharedPreferences( SEND_KEY, Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.Referencekey), tokenresponse.token);
        editor.putString(getString(R.string.FirstName),  tokenresponse.user_fname);
        editor.putString(getString(R.string.LastName),tokenresponse.user_lname);
        editor.putInt(getString(R.string.struser_id),Integer.parseInt(tokenresponse.user_id));
        Log.d("userid",String.valueOf(Integer.parseInt(tokenresponse.user_id)));
        editor.commit();
    }
}