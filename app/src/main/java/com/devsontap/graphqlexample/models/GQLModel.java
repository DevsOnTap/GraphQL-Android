package com.devsontap.graphqlexample.models;

/**
 * Base class for a Model that will be de-serialized from a GraphQL json response. These models are
 * tightly coupled to the query that is sent to GraphQL in order to fetch their corresponding json
 */

public abstract class GQLModel {

    protected abstract String getRawQuery();

    public String formatQuery(Object... tokens) {
        return String.format(getRawQuery(), tokens);
    }
}
