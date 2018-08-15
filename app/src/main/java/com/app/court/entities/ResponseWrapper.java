package com.app.court.entities;

public class ResponseWrapper<T> {

    private String Message;
    private String Code;
    private T Result;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public T getResult() {
        return Result;
    }

    public void setResult(T result) {
        Result = result;
    }
}
