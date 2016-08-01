package com.devsontap.graphqlexample.listeners;

/**
 * Listener interface for binding onClick methods via Android's data binding mechanism
 */

public interface MainActivityListener {

    public void onRequestWithRxJavaClick();

    public void onRequestWithSynchronousCallClick();

    public void onRequestWithAsyncCallClick();

}
