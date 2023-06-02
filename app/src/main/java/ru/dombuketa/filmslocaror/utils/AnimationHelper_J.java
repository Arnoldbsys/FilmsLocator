package ru.dombuketa.filmslocaror.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

import java.util.concurrent.Executors;

import ru.dombuketa.filmslocaror.R;

public class AnimationHelper_J {
    private static final int MENU_ITEMS_COUNT = 5;
    private static final int ANIM_DURATION = 300;
    private static final int SIDES = 2;
    //В метод у нас приходит 3 параметра:
    //1 - наше rootView, которое одновременно является и контейнером
    //и объектом анимации
    //2 - активити для того, чтобы вернуть выполнение нового треда в UI поток
    //3 - позиция в меню навигации, чтобы круг проявления расходился именно от иконки меню навигации
    public static void performFragmentCircularRevealAnimation(View rootView, Activity activity, int position){
        //Создаем новый тред
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                //В бесконечном цикле проверяем, когда наше анимированное view будет "прикреплено" к экрану
                while (true){
                    //Когда оно будет прикреплено, выполним код
                    if (rootView.isAttachedToWindow()){
                        //Возвращаемся в главный тред, чтобы выполнить анимацию
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Cуперсложная математика вычисления старта анимации
                                //val itemCenter = rootView.width / menuItems * 2
                                int itenCenter = rootView.getWidth() / (MENU_ITEMS_COUNT * SIDES);
                                int step = (itenCenter * SIDES) * (position - 1) + itenCenter;

                                int x = step;
                                int y = rootView.getTop() + rootView.getHeight();

                                int startRadius = 0;
                                int endRadius = (int) Math.hypot(rootView.getWidth(), rootView.getHeight());
                                //Создаем саму анимацию
                                Animator animator = ViewAnimationUtils.createCircularReveal(rootView, x, y , startRadius, endRadius);
                                animator.setDuration(ANIM_DURATION);
                                animator.setInterpolator(new AccelerateInterpolator());
                                // Поставим background перехода на текущий
                                animator.addListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        activity.findViewById(R.id.main_container).setBackground(rootView.getBackground());

                                    }
                                });
                                animator.start();
                                //Выставляем видимость нашего элемента
                                rootView.setVisibility(View.VISIBLE);
                            }
                        });
                        return;
                    }
                }
            }
        });

    }

}
