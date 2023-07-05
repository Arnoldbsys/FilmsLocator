package ru.dombuketa.filmslocaror.utils

import android.app.Activity
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import ru.dombuketa.filmslocaror.R
import java.util.concurrent.Executors
import kotlin.math.hypot
import kotlin.math.roundToInt

object AnimationHelper {
    //Это переменная для того, чтобы круг проявления расходился именно от иконки меню навигации
    private const val MENU_ITEMS_COUNT = 5
    private const val ANIM_DURATION = 500L
    private const val SIDES = 2
    //В метод у нас приходит 3 параметра:
    //1 - наше rootView, которое одновременно является и контейнером
    //и объектом анимации
    //2 - активити для того, чтобы вернуть выполнение нового треда в UI поток
    //3 - позиция в меню навигации, чтобы круг проявления расходился именно от иконки меню навигации
    fun performFragmentCircularRevealAnimation(rootView: View, activity: Activity, position: Int){
        //Создаем новый тред
        Executors.newSingleThreadExecutor().execute {
            //В бесконечном цикле проверяем, когда наше анимированное view будет "прикреплено" к экрану
            while (true) {
                //Когда оно будет прикреплено, выполним код
                if (rootView.isAttachedToWindow){
                    //Возвращаемся в главный тред, чтобы выполнить анимацию
                    activity.runOnUiThread(){
                        //Cуперсложная математика вычисления старта анимации
                        val itemCenter = rootView.width / (MENU_ITEMS_COUNT * SIDES)
                        val step = (itemCenter * SIDES) * (position - 1) + itemCenter

                        val x: Int = step
                        val y: Int = rootView.y.roundToInt() + rootView.height

                        val startRadius = 0
                        val endRadius = hypot(rootView.width.toDouble(), rootView.height.toDouble())
                        //Создаем саму анимацию
                        ViewAnimationUtils.createCircularReveal(rootView, x, y, startRadius.toFloat(), endRadius.toFloat()).apply {
                            //Устанавливаем время анимации
                            duration = ANIM_DURATION
                            //Интерполятор для более естественной анимации
                            interpolator = AccelerateInterpolator()
                            // Поставим background перехода на текущий
                            doOnEnd {
                                activity.findViewById<ConstraintLayout>(R.id.main_container).background =
                                    rootView.background
                            }
                            start()
                        }
                        //Выставляем видимость нашего элемента
                        rootView.visibility = View.VISIBLE
                    }
                    return@execute
                }
            }

        }
    }

}