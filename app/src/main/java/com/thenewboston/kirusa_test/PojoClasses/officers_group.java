package com.thenewboston.kirusa_test.PojoClasses;

/**
 * Created by adarsh on 21/9/16.
 */

public class officers_group {
    String group_id;
    String group_name;
    public officers_group(){

    }
    public officers_group(String group_id,String group_name){
        this.group_id=group_id;
        this.group_name=group_name;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
