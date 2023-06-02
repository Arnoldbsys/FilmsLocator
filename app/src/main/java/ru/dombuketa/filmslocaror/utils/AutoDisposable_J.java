package ru.dombuketa.filmslocaror.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import kotlin.NotImplementedError;

public class AutoDisposable_J implements LifecycleObserver {
    //Используем CompositeDisposable для отмены всех Observable
    CompositeDisposable compositeDisposable;

    //Сюда передаем ссылку на ЖЦ компонента, за которым будет слежение
    public void bindTo(Lifecycle lifecycle){
        lifecycle.addObserver(this);
        compositeDisposable  = new CompositeDisposable();
    }

    //Метод для добавления Observable в CompositeDisposable
    public void add(Disposable disposable){
        if (compositeDisposable != null){
            compositeDisposable.add(disposable);
        } else {
            throw new NotImplementedError("Первым добжен быть привязан AutoDisposable в Lifecycle");
        }
    }

    //Этот аннотация позволяет вызывать метод по событию ЖЦ
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy(){
        compositeDisposable.dispose();
    }
}


