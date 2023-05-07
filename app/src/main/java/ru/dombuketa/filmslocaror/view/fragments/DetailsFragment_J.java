package ru.dombuketa.filmslocaror.view.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.Executors;

import ru.dombuketa.filmslocaror.R;
//import ru.dombuketa.filmslocaror.data.ApiConstants;
import ru.dombuketa.filmslocaror.databinding.FragmentDetailsBinding;
import ru.dombuketa.filmslocaror.domain.Film;
import ru.dombuketa.filmslocaror.viewmodel.DetailsFragmentViewModel_J;
import ru.dombuketa.net_module.entity.ApiConstants;

public class DetailsFragment_J extends Fragment {
    private FragmentDetailsBinding binding;
    private Film film;
    private DetailsFragmentViewModel_J viewModel;
    private DetailsFragmentViewModel_J getViewModel(){
        return viewModel == null ? new ViewModelProvider(this).get(DetailsFragmentViewModel_J.class) : viewModel;
    }
/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_details);
        //Получаем наш фильм из переданного бандла
        Film film = getIntent().getParcelableExtra("film");

        ImageView details_poster = findViewById(R.id.details_poster);
        androidx.appcompat.widget.Toolbar details_toolbar = findViewById(R.id.details_toolbar);
        TextView details_description = findViewById(R.id.details_description);
        com.google.android.material.floatingactionbutton.FloatingActionButton details_fab =
                findViewById(R.id.details_fab);

        details_poster.setImageResource(film.getPoster());
        details_toolbar.setTitle(film.getTitle());
        details_description.setText(film.getDescription());

        details_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.main_cantainer_details),"Поделюсь с друзьями", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

    }*/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailsBinding.inflate(inflater, container, false);
        getViewModel().error.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (!s.isEmpty()) {
                    Toast.makeText(requireActivity(), s, Toast.LENGTH_SHORT).show();
                    getViewModel().clearError(); //42*
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        film = getArguments().getParcelable("film");

        com.google.android.material.floatingactionbutton.FloatingActionButton details_fab_favorites =
                requireActivity().findViewById(R.id.details_fab_favorites);

        if (film.isInFavorites()){
            details_fab_favorites.setImageResource(R.drawable.ic_favorite_24);
        }else{
            details_fab_favorites.setImageResource(R.drawable.ic_favorite_border_24);
        }

        details_fab_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!film.isInFavorites()){
                    details_fab_favorites.setImageResource(R.drawable.ic_favorite_24);
                    film.setInFavorites(true);
                }else{
                    details_fab_favorites.setImageResource(R.drawable.ic_favorite_border_24);
                    film.setInFavorites(false);
                }
            }
        });

        //binding.detailsPoster.setImageResource(film.getPoster());
        Glide.with(this)
                .load(ApiConstants.IMAGES_URL + "w780" + film.getPoster())
                .centerCrop()
                .into(binding.detailsPoster);

        binding.detailsToolbar.setTitle(film.getTitle());
        binding.detailsDescription.setText(film.getDescription());

        binding.detailsFabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                //Указываем action с которым он запускается
                intent.setAction(Intent.ACTION_SEND);
                //Кладем данные о нашем фильме
                intent.putExtra(Intent.EXTRA_TEXT,String.format("Отправить к %s /n/n %s",film.getTitle(), film.getDescription()));
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, "Текст сообщения"));
            }
        });
        binding.detailsFabDownloadWp.setOnClickListener(v -> DetailsFragment_J.this.performAsyncLoadOfPoster());
    }

    //Узнаем, было ли получено разрешение ранее
    private Boolean checkPermission(){
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED;
    }
    //Запрашиваем разрешение
    private void requestPermission(){
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    private void saveToGallery(Bitmap bitmap) {
        //Проверяем версию системы
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            //Создаем объект для передачи данных
            ContentValues cv = new ContentValues();
            //Составляем информацию для файла (имя, тип, дата создания, куда сохранять и т.д.)
            cv.put(MediaStore.Images.Media.TITLE, handleSingleQuote(film.getTitle()));
            cv.put(MediaStore.Images.Media.DISPLAY_NAME, handleSingleQuote(film.getTitle()));
            cv.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            cv.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
            cv.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
            cv.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/FilmsLocator");
            //Получаем ссылку на объект Content resolver, который помогает передавать информацию из приложения вовне
            ContentResolver contentResolver = requireActivity().getContentResolver();
            Uri uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
            //Открываем канал для записи на диск
            try {
                OutputStream outputStream = contentResolver.openOutputStream(uri);
                //Передаем нашу картинку, может сделать компрессию
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                //Закрываем поток
                outputStream.close();
            } catch (FileNotFoundException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        else {
            //То же, но для более старых версий ОС
            MediaStore.Images.Media.insertImage(requireActivity().getContentResolver()
                    , bitmap, handleSingleQuote(film.getTitle()), handleSingleQuote(film.getDescription()));
        }
    }

    private String handleSingleQuote(String s){ return s.replace("'", ""); }

    private void performAsyncLoadOfPoster(){
        //Проверяем есть ли разрешение
        if (!checkPermission()){
            //Если нет, то запрашиваем и выходим из метода
            requestPermission();
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getViewModel().loadWallpaper(ApiConstants.IMAGES_URL + "original" + film.getPoster(),
                    new AsyncCallback() {
                        @Override
                        public void onSuccess(Bitmap bitmap) {
                            saveToGallery(bitmap);
                            //Выводим снекбар с кнопкой перейти в галерею
                            Snackbar.make(binding.getRoot(), R.string.downloaded_to_gallery, Snackbar.LENGTH_LONG)
                                .setAction(R.string.open, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.setType("image/*");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }).show();
                            //Отключаем Прогресс-бар
                            getActivity().runOnUiThread(() -> binding.progressBar.setVisibility(View.INVISIBLE));
                        }
                        @Override
                        public void onFailure() {
                            getActivity().runOnUiThread(() -> {
                                Toast.makeText(getContext(), "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
                                binding.progressBar.setVisibility(View.INVISIBLE);
                            });
                        }
                   }
               );
            }
        });
    }

    private void saveAndOpen(Bitmap b){}

    public interface AsyncCallback{
        void onSuccess(Bitmap bitmap);
        void onFailure();
    }

}