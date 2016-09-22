package com.thenewboston.kirusa_test.PojoClasses;

/**
 * Created by adarsh on 22/9/16.
 */

public class message_object {
    String group_id;
    String message;

    public message_object(){

    }
    public message_object(String group_id, String message) {
        this.group_id = group_id;
        this.message = message;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
