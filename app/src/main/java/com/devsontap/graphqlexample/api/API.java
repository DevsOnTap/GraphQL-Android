package com.devsontap.graphqlexample.api;

import com.devsontap.graphqlexample.models.Account;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Retrofit API for requesting resources from a GraphQL endpoint
 */

public interface API {

    @GET("/gql/")
    Observable<Account> queryWithObservable(@Query("query") String query);

    @GET("/gql/")
    Call<Account> queryWithCall(@Query("query") String query);

}
