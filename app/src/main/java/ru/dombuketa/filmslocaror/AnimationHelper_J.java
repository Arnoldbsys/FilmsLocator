package ru.dombuketa.filmslocaror;

import android.animation.Animator;
import android.app.Activity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AnimationHelper_J {
    private static final int menuItems = 4;
    //В метод у нас приходит 3 параметра:
    //1 - наше rootView, которое одновременно является и контейнером
    //и объектом анимации
    //2 - активити для того, чтобы вернуть выполнение нового треда в UI поток
    //3 - позиция в меню навигации, чтобы круг проявления расходился именно от иконки меню навигации
    static void performFragmentCircularRevealAnimation(View rootView, Activity activity, int position){
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
                                int itenCenter = rootView.getWidth() / (menuItems * 2);
                                int step = (itenCenter * 2) * (position - 1) + itenCenter;

                                int x = step;
                                int y = rootView.getTop() + rootView.getHeight();

                                int startRadius = 0;
                                int endRadius = (int) Math.hypot(rootView.getWidth(), rootView.getHeight());
                                //Создаем саму анимацию
                                Animator animator = ViewAnimationUtils.createCircularReveal(rootView, x, y , startRadius, endRadius);
                                animator.setDuration(300);
                                animator.setInterpolator(new AccelerateInterpolator());
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
