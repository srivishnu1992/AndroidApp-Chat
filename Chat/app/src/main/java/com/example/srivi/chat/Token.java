package com.example.srivi.chat;

import java.io.Serializable;

/**
 * Created by srivi on 04-04-2018.
 */

public class Token implements Serializable {
     String status;
     String token;
     String user_id;
     String user_email;
     String user_fname;
     String user_lname;
     String user_role;
     String message;

    @Override
    public String toString() {
        return "Token{" +
                "status='" + status + '\'' +
                ", token='" + token + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_fname='" + user_fname + '\'' +
                ", user_lname='" + user_lname + '\'' +
                ", user_role='" + user_role + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
