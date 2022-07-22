package ru.tubi.project.models;

public class MessageModel {
    private String message_name;
    private String message_text;
    private String message_out;
    private String message_in;

    public MessageModel(String message_text) {
        this.message_text = message_text;
    }

    public String getMessage_name() {
        return message_name;
    }

    public void setMessage_name(String message_name) {
        this.message_name = message_name;
    }

    public String getMessage_text() {
        return message_text;
    }

    public void setMessage_text(String message_text) {
        this.message_text = message_text;
    }

    public String getMessage_out() {
        return message_out;
    }

    public void setMessage_out(String message_out) {
        this.message_out = message_out;
    }

    public String getMessage_in() {
        return message_in;
    }

    public void setMessage_in(String message_in) {
        this.message_in = message_in;
    }
}
