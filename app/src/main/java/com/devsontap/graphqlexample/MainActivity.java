package com.devsontap.graphqlexample;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.devsontap.graphqlexample.api.API;
import com.devsontap.graphqlexample.databinding.ActivityMainBinding;
import com.devsontap.graphqlexample.listeners.MainActivityListener;
import com.devsontap.graphqlexample.models.Account;
import com.devsontap.graphqlexample.utils.Logger;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements MainActivityListener {

    private API mApi;
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setListener(this);

        initializeStetho();

        mApi = createApi();
    }

    private void initializeStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    /**
     * Creates a Retrofit API instance. Normally this would be injected via Dagger
     */
    private API createApi() {
        OkHttpClient ok = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://core-graphql.dev.waldo.photos/")
                .client(ok)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(API.class);
    }

    @Override
    public void onRequestWithRxJavaClick() {
        mApi.queryWithObservable(new Account().formatQuery(1))
                .subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Account>() {
                    @Override
                    public void call(Account account) {
                        Logger.debug("got the account");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Logger.error("error while fetching account", throwable);
                    }
                });
    }

    @Override
    public void onRequestWithSynchronousCallClick() {
        new AsyncTask<Void, Void, Account>() {
            @Override
            protected Account doInBackground(Void... voids) {
                Call<Account> accountCall = mApi.queryWithCall(new Account().formatQuery(1));
                try {
                    Response<Account> response = accountCall.execute();
                    if (response.isSuccessful()) {
                        Account account = response.body();
                        return account;
                    } else {
                        Logger.debug("failed to get account: " + response.code());
                    }
                } catch(IOException exception) {
                    Logger.error("error while fetching account", exception);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Account account) {
                if (account != null) {
                    Logger.debug("got the account");
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onRequestWithAsyncCallClick() {
        Call<Account> accountCall = mApi.queryWithCall(new Account().formatQuery(1));
        accountCall.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful()) {
                    Account account = response.body();
                    Logger.debug("got the account");
                } else {
                    Logger.debug("failed to get account: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Logger.error("error while fetching account", t);
            }
        });
    }
}
