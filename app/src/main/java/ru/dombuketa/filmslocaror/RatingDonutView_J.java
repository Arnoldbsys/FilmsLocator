package ru.dombuketa.filmslocaror;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

public class RatingDonutView_J extends View {
    public final static float KOEF_FOR_PAINT = 10f;
    final float START_ANGLE  = -90f;


    private Float XPoint = 20.0f;
    //Овал для рисования сегментов прогресс бара
    private RectF oval = new RectF();
    //Координаты центра View, а также Radius
    Float radius = 0f;
    Float centerX = 0f;
    Float centerY = 0f;
    //Толщина линии прогресса
    Float stroke = 10f;
    //Значение прогресса от 0 - 100
    int progress = 50;
    //Значения размера текста внутри кольца
    private boolean hideWhenProgressIsZero = false;
    private String textWhenProgressIsZero = "0";

    Float scaleSize = 60f;
    //Краски для наших фигур
    Paint strokePaint;
    Paint digitPaint;
    Paint circlePaint;

    public RatingDonutView_J(Context context) {
        super(context, null);
    }

    public RatingDonutView_J(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //Получаем атрибуты и устанавливаем их в соответствующие поля
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RatingDonutView, 0, 0);
        try {
            stroke = a.getFloat(R.styleable.RatingDonutView_stroke, stroke);
            progress = a.getInt(R.styleable.RatingDonutView_progress, progress);
            hideWhenProgressIsZero = a.getBoolean(R.styleable.RatingDonutView_hideWhenProgressIsZero, hideWhenProgressIsZero);
            if (a.getString(R.styleable.RatingDonutView_textWhenProgressIsZero) != null) {
                textWhenProgressIsZero = a.getString(R.styleable.RatingDonutView_textWhenProgressIsZero);
            }

        } finally {
            a.recycle();
        }
        //Инициализируем первоначальные краски
        initPaint();
    }
    public void setProgress(int p){
        progress = p; //Кладем новое значение в наше поле класса
        initPaint();  //Создаем краски с новыми цветами
        invalidate(); //вызываем перерисовку View
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if (progress != 0 || !hideWhenProgressIsZero) {
            //Рисуем кольцо и задний фон
            drawRating(canvas);
            //Рисуем цифры
            drawText(canvas);
            startAnim();
        }
    }

    private void drawRating(Canvas canvas){
        //Здесь мы можем регулировать размер нашего кольца
        Float scale = radius * .8f;
        //Сохраняем канвас
        canvas.save();
        //Перемещаем нулевые координаты канваса в центр, вы помните, так проще рисовать все круглое
        canvas.translate(centerX, centerY);
        //Устанавливаем размеры под наш овал
        oval.set(0f - scale, 0f - scale, scale, scale);
        //Рисуем задний фон(Желательно его отрисовать один раз в bitmap, так как он статичный)
        canvas.drawCircle(0f, 0f, radius, circlePaint);
        //Рисуем "арки", из них и будет состоять наше кольцо + у нас тут специальный метод
        canvas.drawArc(oval, START_ANGLE, convertProgressToDegrees(progress),false, strokePaint);
        //Восстанавливаем канвас
        canvas.restore();
    }
    private void drawText(Canvas canvas) {
        //Форматируем текст, чтобы мы выводили дробное число с одной цифрой после точки
        String message = (progress == 0) ? textWhenProgressIsZero : String.format("%.1f", progress / 10f);
        //Получаем ширину и высоту текста, чтобы компенсировать их при отрисовке, чтобы текст был
        //точно в центре
        float[] widths = new float[message.length()];
        digitPaint.getTextWidths(message, widths);
        float advance = 0f;
        for (float width: widths) advance += width;
        //Рисуем наш текст
        canvas.drawText(message, centerX - advance / 2, centerY + advance / 4, digitPaint);
    }



    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (getWidth() > getHeight()){
            radius = getHeight() / 2f;
        } else{
            radius = getWidth() / 2f;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int chosenWidth = chooseDimension(widthMode, widthSize);
        int chosenHeight = chooseDimension(heightMode, heightSize);

        int minSide = Math.min(chosenWidth, chosenHeight);
        centerX = minSide / 2f;
        centerY = minSide / 2f;
        setMeasuredDimension(minSide, minSide);
    }

    private int chooseDimension(int mode, int size) {
        if (mode == MeasureSpec.UNSPECIFIED){
            return 300;
        } else {
            return size;
        }
    }

    private void initPaint(){
        //Краска для колец
        strokePaint = new Paint();
        strokePaint.setStyle(Paint.Style.STROKE);
        //Сюда кладем значение из поля класса, потому как у нас краски будут видоизменяться
        strokePaint.setStrokeWidth(stroke);
        //Цвет мы тоже будем получать в специальном методе, потому что в зависимости от рейтинга
        //мы будем менять цвет нашего кольца
        strokePaint.setColor(getPaintColor(progress));
        strokePaint.setAntiAlias(true);

        //Краска для цифр
        digitPaint = new Paint();
        digitPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        digitPaint.setStrokeWidth(2f);
        digitPaint.setShadowLayer(5f, 0f, 0f, Color.DKGRAY);
        digitPaint.setTextSize(scaleSize - 2);
        digitPaint.setTypeface(Typeface.MONOSPACE);
        digitPaint.setColor(getPaintColor(progress));
        digitPaint.setAntiAlias(true);

        //Краска для заднего фона
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setColor(Color.DKGRAY);
    }

    private int getPaintColor(int progress) {
        if (progress <= 25){
            return Color.parseColor("#e84258");
        } else if (progress > 25 && progress <= 50) {
            return Color.parseColor("#fd8060");
        } else if (progress > 50 && progress <= 75) {
            return Color.parseColor("#fee191");
        } else return Color.parseColor("#b0d8a4");

    }

    private Float convertProgressToDegrees(int progress){
        return  progress * 3.6f;
    }

    public void startAnim() {
        final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                RatingDonutView_J.this.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

}
