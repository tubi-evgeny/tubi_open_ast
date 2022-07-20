package ru.tubi.project.models;

import java.util.Date;

public class CodeModel {
    private int code_id;
    private String code;
    private int create_user_id;
    private int used_user_id;
    private long creation_time;
    private long activation_millis;
    private int activated;
    private int code_used;

    public CodeModel(int code_id, String code, int used_user_id
            , long creation_time, long activation_millis, int code_used) {
        this.code_id = code_id;
        this.code = code;
        this.used_user_id = used_user_id;
        this.creation_time = creation_time;
        this.activation_millis = activation_millis;
        this.code_used = code_used;
    }

    public int getCode_id() {
        return code_id;
    }

    public void setCode_id(int code_id) {
        this.code_id = code_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCreate_user_id() {
        return create_user_id;
    }

    public void setCreate_user_id(int create_user_id) {
        this.create_user_id = create_user_id;
    }

    public int getUsed_user_id() {
        return used_user_id;
    }

    public void setUsed_user_id(int used_user_id) {
        this.used_user_id = used_user_id;
    }

    public long getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(long creation_time) {
        this.creation_time = creation_time;
    }


    public long getActivation_millis() {
        return activation_millis;
    }

    public void setActivation_millis(long activation_millis) {
        this.activation_millis = activation_millis;
    }

    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }

    public int getCode_used() {
        return code_used;
    }

    public void setCode_used(int code_used) {
        this.code_used = code_used;
    }
}
