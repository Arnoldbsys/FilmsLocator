package ru.dombuketa.filmslocaror.data

import ru.dombuketa.filmslocaror.R
import ru.dombuketa.filmslocaror.domain.Film

class MainRepository {
    val filmsDataBase = listOf(
        Film(
            1,
            "Титаник",
            R.drawable.poster_1,
            "Очень хороший фильм про любовь, но я его не смотрел.",
            3f
        ),
        Film(
            2,
            "Тар",
            R.drawable.poster_2,
            "Не знаю, про что фильм, но по картинке похоже, что глубокомысленные, тоже не смотрел.",
            7.7f,
            true
        ),
        Film(
            3,
            "ГудВилХантер",
            R.drawable.poster_3,
            "Кто-то кого-то искал и нашел, а может и не нашел, можно посмотреть если делать нечего."
        ),
        Film(
            4,
            "Шининг",
            R.drawable.poster_4,
            "Фильм про мальчика на велосипеде и двух сестер в коридоре. Затронута тема безопасности езды в помещениях. Неплохой фильм для расширения кругозора."
        ),
        Film(
            5,
            "Завтрак клуб",
            R.drawable.poster_5,
            "Фильм про то, что нужно брать с собой на природу чтобы не выглядеть глупо перед сверстниками во время приема пищи. Старый добрый фильм про взаимовыручку и жадность."
        ),
        Film(
            6,
            "Криминальное чтиво",
            R.drawable.poster_01,
            "Все уже хорошо знают эти четыре знаменитых сюжета, где тесно сплетаются судьбы незнакомых людей."
        ),
        Film(
            7,
            "Звездные войны",
            R.drawable.poster_02,
            "2014 год. Дарт Вэйдер хочет встать во главе Космической Федерации, но на пути у него встает с бластером ловкий Люк с Чабукой. Захватывающий фильм про будущее."
        ),
        Film(
            8,
            "Е.Т.",
            R.drawable.poster_03,
            "Приключения велосипедиста на луне. Молодой человек после вечеринки обнаруживает себя на велосипеде на луне. Фильм, пропагандирующий трезвый образ жизни. Обязателен к просмотру молодежи. Сам не смотрел."
        ),
        Film(
            9,
            "Назад в будующее",
            R.drawable.poster_04,
            "Гениальный и неженатый профессор изобрел машину времени, которая помогла ему в итоге найти спутницу жизни при помощи паровоза."
        )
    )


}