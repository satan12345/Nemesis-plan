package com.able.gcstudy.chapter2;

/**
 * @author jipeng
 * @date 2018-12-12 9:52
 * @description
 */
public class User {
    /**
     *
     */
    private String id;
    /**
    *
    */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User() {
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
