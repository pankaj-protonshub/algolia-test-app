package com.search.algolia.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Hits{

    @SerializedName("hits")
    private ArrayList<Story> stories = new ArrayList();

    public Hits(ArrayList<Story> stories){
        this.stories = stories;
    }

    public ArrayList<Story> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Story> stories) {
        this.stories = stories;
    }

    public class Story {

        @SerializedName("title")
        private String title;

        @SerializedName("created_at")
        private Date createdAt;


        public Story(String title, Date createdAt){
            this.title = title;
            this.createdAt = createdAt;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public String getCreatedAtAsString() {
            return createdAt.toString();
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
    }
}
