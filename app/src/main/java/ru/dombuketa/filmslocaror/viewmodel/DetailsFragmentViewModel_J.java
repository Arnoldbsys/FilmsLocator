package ru.dombuketa.filmslocaror.viewmodel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;

import ru.dombuketa.filmslocaror.utils.SingleLiveEvent_J;
import ru.dombuketa.filmslocaror.view.fragments.DetailsFragment;
import ru.dombuketa.filmslocaror.view.fragments.DetailsFragment_J;

public class DetailsFragmentViewModel_J extends ViewModel {
    public SingleLiveEvent_J<String> error = new SingleLiveEvent_J<>(); //42*

    public Bitmap loadWallpaper(String url, DetailsFragment_J.AsyncCallback callback) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
            callback.onSuccess(bitmap);
        } catch (IOException e) {
            error.postValue(e.getMessage());
            callback.onFailure();
        }
        return bitmap;
    }

    public void clearError() { error.postValue(""); }
}
