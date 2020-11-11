package com.SakshmBhat.sit_hub_administrator.faculty_list;

public class FacultyAttributes {

    private String Name,Email,Post,ImageUrl,Key;

    //Constructor
    public FacultyAttributes(){
    }
    //Constructor;
    public FacultyAttributes(String name,String email,String post,String imageUrl, String key) {

        this.Name = name;
        this.Email = email;
        this.Post = post;
        this.ImageUrl = imageUrl;
        this.Key = key;


    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPost() {
        return Post;
    }

    public void setPost(String post) {
        Post = post;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
