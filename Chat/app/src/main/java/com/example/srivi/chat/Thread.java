package com.example.srivi.chat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by namaluuu on 4/8/2018.
 */

public class Thread implements Serializable{
    ArrayList<Currentthread> threads=new ArrayList<>();


    public ArrayList<Currentthread> getThreads() {
        return threads;
    }

    public void setThreads(ArrayList<Currentthread> threads) {
        this.threads = threads;
    }

    @Override
    public String toString() {
        return "Thread{" +
                "threads=" + threads +
                '}';
    }

    public static class Currentthread implements Serializable{

        String user_fname;
        String user_lname;
        int user_id;
        int id;
        String title;
        String created_at;

        public String getUser_fname() {
            return user_fname;
        }

        public void setUser_fname(String user_fname) {
            this.user_fname = user_fname;
        }

        public String getUser_lname() {
            return user_lname;
        }

        public void setUser_lname(String user_lname) {
            this.user_lname = user_lname;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        @Override
        public String toString() {
            return "Currentthread{" +
                    "user_fname='" + user_fname + '\'' +
                    ", user_lname='" + user_lname + '\'' +
                    ", user_id=" + user_id +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    ", created_at='" + created_at + '\'' +
                    '}';
        }
    }

}
