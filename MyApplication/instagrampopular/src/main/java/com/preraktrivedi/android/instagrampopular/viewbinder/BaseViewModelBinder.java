package com.preraktrivedi.android.instagrampopular.viewbinder;

import android.support.annotation.NonNull;

/**
 * Created by preraktrivedi on 11/25/16.
 * Defines a binding contract between the View and the ViewModel
 */
public interface BaseViewModelBinder {

    /**
     * Defines all the View Callbacks that the Fragment would need to implement
     */
    interface ViewCallback {

    }

    /**
     * Defines all the ViewModel callbacks to bind with the View and observe its lifecycle
     */
    interface ViewModelCallback {
        void onViewAttached(@NonNull ViewCallback viewCallback);
        void onViewCreated();
        void onViewDestroyed();
    }
}