package com.example.biggly.retrofit.response;

/**
 * @author PA1810.
 */

//receiving data from api data status and error or success message

public class RestResponse<T> {

    private T data;
    private int status;
    private String msg;
    private String username;
    public String id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public T getData() {
        return data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T data() {
        return data;
    }

    public int status() {
        return status;
    }

    public String msg() {
        return msg;
    }
}
