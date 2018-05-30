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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.srivi.chat.MainActivity.SEND_KEY;

public class Signup extends AppCompatActivity {

    EditText editFirstName;
    EditText editLastName;
    EditText editEmail;
    EditText editChoose;
    EditText editRepeat;
    OkHttpClient okHttpClient = new OkHttpClient();
    static String SIGNUP_KEY="response";
    public static final String SEND_KEY = "send";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_signup );
        setTitle( "Sign Up" );

        editFirstName = findViewById( R.id.editFirstName );
        editLastName = findViewById( R.id.editLastName );
        editEmail = findViewById( R.id.editEmailSign );
        editChoose = findViewById( R.id.editChooseSign );
        editRepeat = findViewById( R.id.editRepeatSign );

        findViewById( R.id.btnCancel ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( Signup.this, MainActivity.class );
                startActivity(intent);
                finish();

            }
        } );
        findViewById( R.id.btnSignSign ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = editFirstName.getText().toString();
                String lastName = editLastName.getText().toString();
                String email = editEmail.getText().toString();
                String choose = editChoose.getText().toString();
                String repeat = editRepeat.getText().toString();
                if(firstName.equals( "" ) || lastName.equals( "" )|| email.equals( "" )|| choose.equals( "" )|| repeat.equals( "" ))
                    Toast.makeText( Signup.this, "Please enter all values", Toast.LENGTH_SHORT ).show();
                else if(!choose.equals( repeat )){
                    Toast.makeText( Signup.this, "Passwords doesn't match", Toast.LENGTH_SHORT ).show();
                }
                else {
                    signup( firstName, lastName, email, choose, repeat );
                }
            }
        } );
    }
    public void signup(String firstName, String lastName, String email, String choose, String repeat) {
        RequestBody requestBody = new FormBody.Builder(  )
                .add( "fname", firstName )
                .add( "lname", lastName )
                .add( "email", email )
                .add( "password", choose )
                .build();
        Request request = new Request.Builder()
                .url( "http://ec2-54-91-96-147.compute-1.amazonaws.com/api/signup" )
                .post( requestBody )
                .build();
        okHttpClient.newCall( request ).enqueue( new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("Request", "Signup failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                Log.d( "Request", responseBody );
                Gson gson = new Gson();
                final Token tokenResponse = gson.fromJson( responseBody, Token.class );
                Log.d( "Request", tokenResponse.status );
                if(tokenResponse.token==null) {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText( Signup.this, tokenResponse.message, Toast.LENGTH_SHORT ).show();
                        }
                    } );
                }
                else {
                    runOnUiThread( new Runnable() {
                        @Override
                        public void run() {
                            Log.d("iam","in");
                            Intent intent = new Intent( Signup.this, ThreadsActivity.class );
                            startActivity(intent);
                            savetoken( tokenResponse);
                            finish();

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
        editor.commit();
    }
}
