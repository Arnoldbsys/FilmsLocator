package ru.dombuketa.filmslocaror;

import java.util.ArrayList;
import java.util.List;

import ru.dombuketa.filmslocaror.domain.Film;

public class FilmsDataBase_J {
    public FilmsDataBase_J() {
        initDatabase();
    }

    private List<Film> filmsDataBase = new ArrayList<Film>();
    public List<Film> getFilmsDataBase() {
        return filmsDataBase;
    }

    private void initDatabase(){
        filmsDataBase = new ArrayList<Film>();
//        filmsDataBase.add(new Film(1,"Титаник", String.valueOf(R.drawable.poster_1),"Очень хороший фильм про любовь, но я его не смотрел.", 1f,false));
//        filmsDataBase.add(new Film(2,"Тар", String.valueOf(R.drawable.poster_2),"Не знаю, про что фильм, но по картинке похоже, что глубокомысленные, тоже не смотрел.", 7.7f,false));
//        filmsDataBase.add(new Film(3,"ГудВилХантер",String.valueOf(R.drawable.poster_3),"Кто-то кого-то искал и нашел, а может и не нашел, можно посмотреть если делать нечего.", 0f,true));
//        filmsDataBase.add(new Film(4,"Шининг", String.valueOf(R.drawable.poster_4),"Фильм про мальчика на велосипеде и двух сестер в коридоре. Затронута тема безопасности езды в помещениях. Неплохой фильм для расширения кругозора.", 5.5f,false));
//        filmsDataBase.add(new Film(5,"Завтрак клуб",String.valueOf(R.drawable.poster_5),"Фильм про то, что нужно брать с собой на природу чтобы не выглядеть глупо перед сверстниками во время приема пищи. Старый добрый фильм про взаимовыручку и жадность.", 9f,false));
//        filmsDataBase.add(new Film(6,"Криминальное чтиво",String.valueOf(R.drawable.poster_01),"Все уже хорошо знают эти четыре знаменитых сюжета, где тесно сплетаются судьбы незнакомых людей.", 0f,false));
//        filmsDataBase.add(new Film(7,"Звездные войны",String.valueOf(R.drawable.poster_02),"2014 год. Дарт Вэйдер хочет встать во главе Космической Федерации, но на пути у него встает с бластером ловкий Люк с Чабукой. Захватывающий фильм про будущее.", 0f, false));
//        filmsDataBase.add(new Film(8,"Е.Т.", String.valueOf(R.drawable.poster_03),"Приключения велосипедиста на луне. Молодой человек после вечеринки обнаруживает себя на велосипеде на луне. Фильм, пропагандирующий трезвый образ жизни. Обязателен к просмотру молодежи. Сам не смотрел.", 0f, true));
//        filmsDataBase.add(new Film(9,"Назад в будующее", String.valueOf(R.drawable.poster_04),"Гениальный и неженатый профессор изобрел машину времени, которая помогла ему в итоге найти спутницу жизни при помощи паровоза.", 1f,false));
    }

}
