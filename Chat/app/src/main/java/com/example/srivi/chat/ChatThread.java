package com.example.srivi.chat;

import java.util.ArrayList;

/**
 * Created by namaluuu on 4/9/2018.
 */

public class ChatThread {
    ArrayList<Message> messages;

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "ChatThread{" +
                "messages=" + messages +
                '}';
    }

    public static class Message {
       String user_fname, user_lname, message, created_at;
       int user_id, id;

       @Override
       public String toString() {
           return "Message{" +
                   "user_fname='" + user_fname + '\'' +
                   ", user_lname='" + user_lname + '\'' +
                   ", message='" + message + '\'' +
                   ", created_at='" + created_at + '\'' +
                   ", user_id=" + user_id +
                   ", id=" + id +
                   '}';
       }
   }


}
