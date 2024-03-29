package ru.dombuketa.filmslocaror.utils;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent_J<T> extends MutableLiveData<T> {
    private AtomicBoolean mPending = new AtomicBoolean(false);

    @MainThread
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super T> observer) {
        if (hasActiveObservers()){
            //Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }
        super.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                if (mPending.compareAndSet(true,false)){
                    observer.onChanged(t);
                }
            }
        });
    }

    @MainThread
    @Override
    public void setValue(T value) {
        mPending.set(true);
        super.setValue(value);
    }
    /**
     * Used for cases where T is Void, to make calls cleaner.
     */

    @MainThread
    void call(){
        setValue(null);
    }


}
