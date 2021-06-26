package com.SakshmBhat.sit_hub_administrator.feed;

public class FeedData {

    String title, image,date,time,key,link,linkText,uploader,uploaderProfilePicUrl;

    public FeedData() {
    }

    public FeedData(String title, String image, String date, String time, String key, String link, String linkText, String uploader, String uploaderProfilePicUrl) {
        this.title = title;
        this.image = image;
        this.date = date;
        this.time = time;
        this.key = key;
        this.link = link;
        this.linkText = linkText;
        this.uploader = uploader;
        this.uploaderProfilePicUrl = uploaderProfilePicUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getUploaderProfilePicUrl() {
        return uploaderProfilePicUrl;
    }

    public void setUploaderProfilePicUrl(String uploaderProfilePicUrl) {
        this.uploaderProfilePicUrl = uploaderProfilePicUrl;
    }
}
