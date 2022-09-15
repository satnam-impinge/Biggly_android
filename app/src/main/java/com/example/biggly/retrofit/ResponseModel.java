package com.example.biggly.retrofit;


import java.io.Serializable;

public class ResponseModel implements Serializable {

    String msg;
    String status;
    String submit;
    int isbot;

    public int isIsbot() {
        return isbot;
    }

    public void setIsbot(int isbot) {
        this.isbot = isbot;
    }
//    {"status":"success","msg":"Upload success","submit":"system","isweb":false}

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }
}
