package ru.dombuketa.filmslocaror.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.animation.Animator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;

import ru.dombuketa.filmslocaror.App_J;
import ru.dombuketa.filmslocaror.FilmsDataBase_J;
import ru.dombuketa.filmslocaror.R;
import ru.dombuketa.filmslocaror.data.PreferenceProvider_J;
import ru.dombuketa.filmslocaror.databinding.ActivityMainBinding;
import ru.dombuketa.filmslocaror.domain.Film;
import ru.dombuketa.filmslocaror.view.fragments.CastsFragment_J;
import ru.dombuketa.filmslocaror.view.fragments.DetailsFragment_J;
import ru.dombuketa.filmslocaror.view.fragments.FavoritesFragment_J;
import ru.dombuketa.filmslocaror.view.fragments.HomeFragment_J;
import ru.dombuketa.filmslocaror.view.fragments.SeelaterFragment_J;
import ru.dombuketa.filmslocaror.view.fragments.SettingsFragment_J;

public class MainActivity_J extends AppCompatActivity {
    private ActivityMainBinding binding;
    private static final int TIME_INTERVAL = 2000;
    private long backPressed = 0L;

    private List<Film> dataBase = null; //App_J.getInstance().repo.filmsDataBase; // new FilmsDataBase_J().getFilmsDataBase();
    public List<Film> getDataBase() {
        return dataBase;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());

        initNavigationMenu();

        LottieAnimationView lottieAnimationView = findViewById(R.id.lottie_anim);

//                  getSupportFragmentManager().beginTransaction().add(R.id.fragment_placeholder, new HomeFragment_J())
//                 .addToBackStack(null).commit();

        Animator.AnimatorListener listener = new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) { }

            @Override
            public void onAnimationEnd(Animator animator) {
                lottieAnimationView.setVisibility(View.GONE);
                Fragment fragmentHome = checkFragmentExistence("home");
                changeFragment( (fragmentHome == null) ? new HomeFragment_J() : fragmentHome, "home");
            }
            @Override
            public void onAnimationCancel(Animator animator) { }
            @Override
            public void onAnimationRepeat(Animator animator) { }
        };
        lottieAnimationView.addAnimatorListener(listener);
        lottieAnimationView.playAnimation();
        //initTopBar();
        //setSupportActionBar(findViewById(R.id.app_bar2));


    }




    public void launchDetailsFragment(Film film){
        //Создаем "посылку"
        Bundle bundle = new Bundle();
        //Кладем наш фильм в "посылку"
        bundle.putParcelable("film",film);
        //Кладем фрагмент с деталями в перменную
        Fragment fragment = new DetailsFragment_J();
        //Прикрепляем нашу "посылку" к фрагменту
        fragment.setArguments(bundle);
        //Запускаем фрагмент
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder,fragment)
                .addToBackStack(null).commit();
    }


    /*void initTopBar() {
        MaterialToolbar topAppBar = findViewById(R.id.topAppBar);
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case (R.id.settings):
                        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES) {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            Toast.makeText(getApplicationContext(), "Ночной режим", Toast.LENGTH_SHORT).show();
                            item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_mode_night));
                        } else {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            Toast.makeText(getApplicationContext(), "Дневной режим", Toast.LENGTH_SHORT).show();
                            item.setIcon(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_mode_light));
                        }
                        return true;
                    default:
                        return false;
                }
            }
        });
    }*/


    void initNavigationMenu() {
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Snackbar snackbar = Snackbar.make(binding.mainContainer,"", Snackbar.LENGTH_SHORT);
                String tag;
                Fragment fragment;
                switch (item.getItemId()){
                    case (R.id.home):
                        tag = "home";
                        fragment = checkFragmentExistence(tag);
                        changeFragment( (fragment == null) ? new HomeFragment_J() : fragment, tag);
                        return true;
                    case (R.id.favorites):
                        tag = "favorites";
                        fragment = checkFragmentExistence(tag);
                        changeFragment( (fragment == null) ? new FavoritesFragment_J() : fragment, tag);
                        return true;
                    case (R.id.watch_later):
                        tag = "watch_later";
                        fragment = checkFragmentExistence(tag);
                        changeFragment( (fragment == null) ? new SeelaterFragment_J() : fragment, tag);
                        return true;
                    case (R.id.casts):
                        tag = "casts";
                        fragment = checkFragmentExistence(tag);
                        changeFragment( (fragment == null) ? new CastsFragment_J() : fragment, tag);
                        return true;
                    case (R.id.settings):
                        tag = "settings";
                        fragment = checkFragmentExistence(tag);
                        changeFragment( (fragment == null) ? new SettingsFragment_J() : fragment, tag);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private Fragment checkFragmentExistence(String tag){
        return  getSupportFragmentManager().findFragmentByTag(tag);
    }
    private void changeFragment(Fragment fragment, String tag){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_placeholder, fragment, tag)
                .addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            if (backPressed + TIME_INTERVAL > System.currentTimeMillis()){
                super.onBackPressed();
                finish();
            } else {
                Toast.makeText(getApplicationContext(),R.string.alert_2clickToExit, Toast.LENGTH_SHORT).show();
            }
            backPressed = System.currentTimeMillis();

        } else {
            super.onBackPressed();
        }
    }

}