package ru.dombuketa.filmslocaror

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator

class RatingDonutView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null)
        : View(context, attributeSet) {
    // Аниматор
    private lateinit var animatos : Animation
    //Овал для рисования сегментов прогресс бара
    private val oval = RectF()
    //Координаты центра View, а также Radius
    private var radius: Float = 0f
    private var centerX: Float = 0f
    private var centerY: Float = 0f
    //Толщина линии прогресса
    private var stroke = 10f
    //Значение прогресса от 0 - 100
    private var progress = 50
    private var hideWhenProgressIsZero = false
    private var textWhenProgressIsZero = "0"
    //Значения размера текста внутри кольца
    private var scaleSize = 60f
    //Краски для наших фигур
    private lateinit var strokePaint: Paint
    private lateinit var digitPaint: Paint
    private lateinit var circlePaint: Paint

    init {
        //Получаем атрибуты и устанавливаем их в соответствующие поля
        val a = context.theme.obtainStyledAttributes(attributeSet, R.styleable.RatingDonutView, 0, 0)
        try {
            stroke = a.getFloat(R.styleable.RatingDonutView_stroke, stroke)
            progress = a.getInt(R.styleable.RatingDonutView_progress, progress)
            hideWhenProgressIsZero = a.getBoolean(R.styleable.RatingDonutView_hideWhenProgressIsZero, hideWhenProgressIsZero)
            textWhenProgressIsZero = a.getString(R.styleable.RatingDonutView_textWhenProgressIsZero)?.toString() ?: textWhenProgressIsZero
        } finally {
            a.recycle()
        }
        //Инициализируем первоначальные краски
        initPaint()
    }

    override fun onDraw(canvas: Canvas) {
        if (progress != 0 || !hideWhenProgressIsZero) {
            //Рисуем кольцо и задний фон
            drawRating(canvas)
            //Рисуем цифры
            drawText(canvas)
            startAnim()
        }
    }


    private fun drawRating(canvas: Canvas){
        //Здесь мы можем регулировать размер нашего кольца
        val scale = radius * KOEF_FOR_SCALE_RADIUS
        //Сохраняем канвас
        canvas.save()
        //Перемещаем нулевые координаты канваса в центр, вы помните, так проще рисовать все круглое
        canvas.translate(centerX, centerY)
        //Устанавливаем размеры под наш овал
        oval.set(0f - scale, 0f - scale, scale, scale)
        //Рисуем задний фон(Желательно его отрисовать один раз в bitmap, так как он статичный)
        canvas.drawCircle(0f, 0f, radius, circlePaint)
        //Рисуем "арки", из них и будет состоять наше кольцо + у нас тут специальный метод
        canvas.drawArc(oval, START_ANGLE, convertProgressToDegrees(progress), false, strokePaint)
        //Восстанавливаем канвас
        canvas.restore()
    }

    private fun drawText(canvas: Canvas){
        //Форматируем текст, чтобы мы выводили дробное число с одной цифрой после точки
        val message = if (progress == 0 ) textWhenProgressIsZero else  String.format("%.1f", progress / KOEF_FOR_PAINT)
        //Получаем ширину и высоту текста, чтобы компенсировать их при отрисовке, чтобы текст был
        //точно в центре
        val widths = FloatArray(message.length)
        digitPaint.getTextWidths(message, widths)
        var advance = 0f
        for (width in widths) advance += width
        //Рисуем наш текст
        canvas.drawText(message, centerX - advance / 2, centerY + advance / 4, digitPaint)
    }

    fun setProgress(p: Int){
        //Кладем новое значение в наше поле класса
        progress = p
        //Создаем краски с новыми цветами
        initPaint()
        //вызываем перерисовку View
        invalidate()
    }

    private fun convertProgressToDegrees(progress: Int): Float = progress * KOEF_FOR_DEGREE

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = if (width > height) {
            height.div(2f)
        } else {
            width.div(2f)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val chosenWidth = chooseDimension(widthMode, widthSize)
        val chosenHeight = chooseDimension(heightMode, heightSize)

        val minSide = Math.min(chosenWidth, chosenHeight)
        centerX = minSide.div(2f)
        centerY = minSide.div(2f)

        setMeasuredDimension(minSide, minSide)
    }


    private fun chooseDimension(mode: Int, size: Int) =
        when (mode) {
            MeasureSpec.AT_MOST, MeasureSpec.EXACTLY -> size
            else -> DEFAULT_SIZE_VIEW
        }

    private fun initPaint(){
        //Краска для колец
        strokePaint = Paint().apply {
            style = Paint.Style.STROKE
            //Сюда кладем значение из поля класса, потому как у нас краски будут видоизменяться
            strokeWidth = stroke
            //Цвет мы тоже будем получать в специальном методе, потому что в зависимости от рейтинга
            //мы будем менять цвет нашего кольца
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        //Краска для цифр
        digitPaint = Paint().apply {
            style = Paint.Style.FILL_AND_STROKE
            strokeWidth = 2f
            setShadowLayer(5f, 0f, 0f, Color.DKGRAY)
            textSize = scaleSize
            typeface = Typeface.SANS_SERIF
            color = getPaintColor(progress)
            isAntiAlias = true
        }
        //Краска для заднего фона
        circlePaint = Paint().apply {
            style = Paint.Style.FILL
            color = Color.DKGRAY
        }
    }

    private fun getPaintColor(progress: Int): Int = when(progress) {
        in 0 .. RANGE_1 -> Color.parseColor("#e84258")
        in RANGE_1 + 1 .. RANGE_2 -> Color.parseColor("#fd8060")
        in RANGE_2 + 1 .. RANGE_3 -> Color.parseColor("#fee191")
        else -> Color.parseColor("#b0d8a4")
    }

    fun startAnim() {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 1000
        animator.addUpdateListener {
            this.alpha = it.animatedValue as Float
        }
        animator.start()
    }

    companion object{
        const val KOEF_FOR_PAINT : Float = 10f
        const val KOEF_FOR_DEGREE : Float = 3.6f
        const val KOEF_FOR_SCALE_RADIUS : Float = 0.8f
        const val START_ANGLE : Float = -90f
        const val DEFAULT_SIZE_VIEW : Int = 300
        const val RANGE_1 : Int = 25
        const val RANGE_2 : Int = 50
        const val RANGE_3 : Int = 75

    }

}