package ru.dombuketa.filmslocaror

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class HomeFragment : Fragment() {
    val filmsDataBase = mutableListOf<Film>()
    private lateinit var filmsAdapter: FilmsListRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initRV()

    }

    fun initData(){
        filmsDataBase.add(Film(1,"Титаник",R.drawable.poster_1,"Очень хороший фильм про любовь, но я его не смотрел."))
        filmsDataBase.add(Film(2,"Тар",R.drawable.poster_2,"Не знаю, про что фильм, но по картинке похоже, что глубокомысленные, тоже не смотрел."))
        filmsDataBase.add(Film(3,"ГудВилХантер",R.drawable.poster_3,"Кто-то кого-то искал и нашел, а может и не нашел, можно посмотреть если делать нечего."))
        filmsDataBase.add(Film(4,"Шининг",R.drawable.poster_4,"Фильм про мальчика на велосипеде и двух сестер в коридоре. Затронута тема безопасности езды в помещениях. Неплохой фильм для расширения кругозора."))
        filmsDataBase.add(Film(5,"Завтрак клуб",R.drawable.poster_5,"Фильм про то, что нужно брать с собой на природу чтобы не выглядеть глупо перед сверстниками во время приема пищи. Старый добрый фильм про взаимовыручку и жадность."))
        filmsDataBase.add(Film(6,"Криминальное чтиво",R.drawable.poster_01,"Все уже хорошо знают эти четыре знаменитых сюжета, где тесно сплетаются судьбы незнакомых людей."))
        filmsDataBase.add(Film(7,"Звездные войны",R.drawable.poster_02,"2014 год. Дарт Вэйдер хочет встать во главе Космической Федерации, но на пути у него встает с бластером ловкий Люк с Чабукой. Захватывающий фильм про будущее."))
        filmsDataBase.add(Film(8,"Е.Т.",R.drawable.poster_03,"Приключения велосипедиста на луне. Молодой человек после вечеринки обнаруживает себя на велосипеде на луне. Фильм, пропагандирующий трезвый образ жизни. Обязателен к просмотру молодежи. Сам не смотрел."))
        filmsDataBase.add(Film(9,"Назад в будующее",R.drawable.poster_04,"Гениальный и неженатый профессор изобрел машину времени, которая помогла ему в итоге найти спутницу жизни при помощи паровоза."))
    }

    private fun initRV() {
        val main_recycler = (requireContext() as MainActivity).findViewById<RecyclerView>(R.id.main_recycler)
        //Инициализируем наш адаптер в конструктор передаем анонимно инициализированный интерфейс,
        //оставим его пока пустым, он нам понадобится во второй части задания
        main_recycler.apply {
            filmsAdapter = FilmsListRecyclerAdapter(object : FilmsListRecyclerAdapter.OnItemClickListener{
                override fun click(film: Film) {
                    (requireActivity() as MainActivity).lanunchDetailsFragment(film)
                }
            })
            //Присваиваем адаптер
            adapter = filmsAdapter
            //Присвои layoutmanager
            layoutManager= LinearLayoutManager(requireContext())
            //Применяем декоратор для отступов
            val decorator = TopSpacingItemDecoration(8)
            addItemDecoration(decorator)

        }
        filmsAdapter.addItems(filmsDataBase)
    }

}