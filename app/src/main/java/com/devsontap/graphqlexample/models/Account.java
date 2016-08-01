package com.devsontap.graphqlexample.models;

/**
 * Account model that is de-serialized from a graphQL response
 */

public class Account {

    long id;
    String username;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
